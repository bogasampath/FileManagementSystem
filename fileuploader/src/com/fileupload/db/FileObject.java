package com.fileupload.db;

import java.io.Serializable;

public class FileObject implements Serializable {
	
	private Integer fileId = null;
	
	private Integer userId = null;
	
	private String fileName;
	
	private String filePath;
	
	private Long fileSize;

	private String fileHash;
	
	private Integer duplicateFileId = null;
	
	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

	public Integer getDuplicateFileId() {
		return duplicateFileId;
	}

	public void setDuplicateFileId(Integer duplicateFileId) {
		this.duplicateFileId = duplicateFileId;
	}
	
	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer()
				.append("FileObject: fileId - ")
				.append(getFileId())
				.append(", fileName - ")
				.append(getFileName())
				.append(", filePath - ")
				.append(getFilePath())
				.append(", fileHash - ")
				.append(getFileHash())
				.append(", fileSize - ")
				.append(getFileSize())
				.append(", userId - ")
				.append(getUserId())
				.append(", duplicateFileId: ")
				.append(getDuplicateFileId());
		return sb.toString();
	}
}
