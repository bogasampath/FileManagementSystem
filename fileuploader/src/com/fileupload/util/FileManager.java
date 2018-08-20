package com.fileupload.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fileupload.db.DBUtil;
import com.fileupload.db.FileChunk;
import com.fileupload.db.FileObject;
import com.fileupload.db.User;
import com.fileupload.db.UserPreferences;
import com.fileupload.util.FileUtils;

public class FileManager {
	
	private static final Logger LOGGER = Logger.getLogger(FileManager.class.getName());
	public static final String DEFAULT_FILE_PATH_FOR_UPLOAD = "c:\\filemanager\\uploads\\";
	public static final String ADMIN_USER_NAME = "admin";
	public static final String DEDUPE_TYPE_FIXED = "fixed_chunk";
	public static final String DEDUPE_TYPE_VARIABLE = "variable_chunk";
	public static final String DEFAULT_DEDUPE_TYPE = DEDUPE_TYPE_FIXED;
	
	public static FileObject findFileById(Integer fileId)
	throws Exception {
		return DBUtil.findFileById(fileId);
	}
	
	public static List<String> fetchChunksPathList(FileObject file)
	throws Exception {
		List<String> chunksPathList = new LinkedList<String>();
		if(file == null) {
			LOGGER.log(Level.SEVERE, "file got null");
			throw new Exception("file got null");
		}
		// Check if the file is duplicate
		FileObject fileObjectToFetchChunks = file;
		if(file.getDuplicateFileId() != null && file.getDuplicateFileId() > 0) {
			fileObjectToFetchChunks = 
					DBUtil.findFileById(file.getDuplicateFileId());
		}
		List<FileChunk> fileChunks = DBUtil.fetchChunksByFileId(
					fileObjectToFetchChunks.getFileId());
		String chunkFilePath = null;
		for(FileChunk chunk : fileChunks) {
			if(chunk.getDuplicateChunkId() != null && chunk.getDuplicateChunkId() > 0) {
				FileChunk duplicateChunk = 
						DBUtil.findChunkById(chunk.getDuplicateChunkId());
				FileObject fileObjectOfChunk =
						DBUtil.findFileById(duplicateChunk.getFileId());
				chunkFilePath = fileObjectOfChunk.getFilePath()+
						fileObjectOfChunk.getFileName()+FileUtils.CHUNK_PREFIX+
						duplicateChunk.getChunkSequence();
			} else {
				chunkFilePath = fileObjectToFetchChunks.getFilePath()+
						fileObjectToFetchChunks.getFileName()+
						FileUtils.CHUNK_PREFIX + chunk.getChunkSequence();
			}
			chunksPathList.add(chunkFilePath);
		}
		
		return chunksPathList;
	}
	
	public static FileObject findDuplicateFile(String hash)
	throws Exception {
		return DBUtil.findFileByHash(hash);
	}
	
	public static FileChunk findDuplicateChunk(String hash)
	throws Exception {
		return DBUtil.findChunkByHash(hash);
	}
	
	private static boolean isChunkingRequired(String path,
			String fileName, int userId, long chunkSize)
	throws Exception {
		boolean isChunkingRequired = false;
		
		File file = new File((path + fileName));
		long fileSize = file.length();
		
		if(fileSize > chunkSize) {
			isChunkingRequired = true;
		}
		return isChunkingRequired;
	}
	
	public static FileObject saveFile(String path, String fileName, int userId)
	throws Exception {
		FileObject file = new FileObject();
		file.setFileName(fileName);
		file.setFilePath(path);
		file.setUserId(userId);
		
		String hash = FileUtils.getShaHash(path, fileName);
		file.setFileHash(hash);
		
		Long uploadFileSize = FileUtils.getFileSize(path, fileName);
		file.setFileSize(uploadFileSize);
		// Initialize the compressed size with actual file size.
		// If duplicate file or duplicate chunk not available
		// then the complete physical file will be stored in the drive.
		// So that uploaded file size becomes compressed size.
		Long compressedSize = uploadFileSize;

		String newFilePath = file.getFilePath();
		// Check for duplicate file
		FileObject duplicateFile = FileManager.findDuplicateFile(hash);
		if(duplicateFile == null) {
			LOGGER.log(Level.INFO, "Duplicate file not exists for fileName: "+
					fileName + " with hash: " + hash);
		} else {
			LOGGER.log(Level.INFO, "Duplicate file found for fileName: "+
					fileName + " with hash: " + hash + " is: "+
					duplicateFile);
			file.setDuplicateFileId(duplicateFile.getFileId());
			file.setFilePath(duplicateFile.getFilePath());
		}
		file = DBUtil.saveFileObject(file);
		// Delete newly uploaded file if duplicate file exists 
		if(duplicateFile != null) {
			// Mark the compressed size of the current file to zero
			// since we are not storing the physical file because
			// this is a duplicate file
			compressedSize = 0L;
			DBUtil.updateApplicationStats(uploadFileSize,
					compressedSize, fetchAdminUserId());
			
			File newFile = new File((newFilePath + file.getFileName()));
        	if(newFile != null) {
        		newFile.delete();
        	}
        	File newDir = new File(newFilePath);
        	if(newDir != null) {
        		newDir.delete();
        	}
        	// Duplicate file object found,
        	// hence no need to proceed further
        	return file;
		}
		UserPreferences prefs = DBUtil.getUserPreferences(userId);
		long chunkSize = new Long(prefs.getChunkSize());
		boolean isChunkingRequired = FileManager.isChunkingRequired(
				path, fileName, userId, chunkSize);
		// If file size is less than the configured chunk size
		// then save the file as a single chunk, no need to create chunks.
		if(!isChunkingRequired) {
			LOGGER.info("Chunking not required for fileName: "+
					fileName + ", filePath: " + path + ", userId: "+
					userId);
		} else {
			LOGGER.info("Creating chunks for fileName: "+fileName+
					", filePath: " + path + ", userId: "+ userId);
		}
		List<FileChunk> fileChunks = null;
		if(DEDUPE_TYPE_VARIABLE.equalsIgnoreCase(prefs.getDedupeType())) {
			fileChunks = FileUtils.createContentAwareFileChunks(
					fileName, path, path, chunkSize);
		} else {
			fileChunks = FileUtils.createFileChunks(
					fileName, path, path, chunkSize);
		}
		
		FileChunk duplicateChunk = null;
		for(FileChunk chunk : fileChunks) {
			chunk.setFileId(file.getFileId());
			duplicateChunk = FileManager.findDuplicateChunk(chunk.getChunkHash());
			if(duplicateChunk != null) {
				LOGGER.log(Level.INFO, "Duplicate chunk found for chunkNumber: "+
						chunk.getChunkSequence() + ", fileId: " + file.getFileId());
				chunk.setDuplicateChunkId(duplicateChunk.getChunkId());
				// Delete current chunk since duplicate chunk available
				File currentChunkFile = new File((path + fileName+"-"+chunk.getChunkSequence()));
	        	if(currentChunkFile != null && currentChunkFile.exists()) {
	        		// Deduct the current chunk size from compressed size
	        		// since this is a duplicate chunk and we are not
	        		// storing the physical file for this chunk.
	        		compressedSize = compressedSize - currentChunkFile.length();
	        		currentChunkFile.delete();
	        	}
			}
			DBUtil.saveFileChunk(chunk);
		}
		DBUtil.updateApplicationStats(uploadFileSize,
				compressedSize, fetchAdminUserId());
		return file;
	}
	
	public static List<FileObject> getFilesForUser(Integer userId)
	throws IOException {
		try {
			return DBUtil.findFilesByUSer(userId);
		} catch(Exception e) {
			throw new IOException(e);
		}
	}
	
	public static Integer fetchAdminUserId() throws Exception {
		User user = fetchAdminUser();
		if(user != null)
			return user.getUserId();
		throw new IllegalStateException("Unable to fetch admin user");
	}
	
	public static User fetchAdminUser() throws Exception {
		return DBUtil.findUserByUserName(ADMIN_USER_NAME);
	}
}
