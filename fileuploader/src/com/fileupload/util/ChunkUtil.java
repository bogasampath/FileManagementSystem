package com.fileupload.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.fileupload.db.FileChunk;

public class ChunkUtil {
	
	public static List<ChunkBoundary> prepareChunkBoundaries(String fileName, String filePath, int chunkSize) 
	throws Exception {

		File file = new File(filePath, fileName);
		InputStream inputStream = new FileInputStream(file);
		List<ChunkBoundary> boundaries = new LinkedList<ChunkBoundary>();
		try {
			byte[] chunk = new byte[chunkSize];
			byte[] tempChunk = new byte[chunkSize];
			byte[] slideChunk = new byte[chunkSize];
			long fileSize = file.length();
			long fileSizeToRead = fileSize;
			int readLength = chunkSize;
			ChunkBoundary boundary = null;
			int iteration = 0;
			boolean duplicateChunk = false;
			long lastChunkIndex = 0;
			int unmatchedDataLength = 0;
			int slideCounter = 0;
			boolean dataAvailable = false;
			int read = 0;
			do {
				if(!dataAvailable) {
					readLength = chunkSize;
					if(fileSizeToRead < chunkSize) {
						readLength = (int) fileSizeToRead;
					}
					chunk = new byte[readLength];
					read = inputStream.read(chunk, 0, readLength);
					fileSizeToRead = fileSizeToRead - read;
				}
				if(iteration == 0) {
					System.out.println("iteration == 0");
					duplicateChunk = isDuplicateChunk(chunk);
					if(duplicateChunk) {
						boundary = new ChunkBoundary(lastChunkIndex,
								(lastChunkIndex = lastChunkIndex + chunk.length));
						boundaries.add(boundary);
					} else {
						tempChunk = new byte[chunk.length];
						System.arraycopy(chunk, 0, tempChunk, 0, chunk.length);
						++unmatchedDataLength;
					}
				} else if (duplicateChunk && slideCounter == 0) {
					System.out.println("duplicateChunk && slideCounter == 0");
					System.out.println("Slide: " + new String(slideChunk) + ", TempChunk: " + new String(tempChunk) +", Chunk: " + new String(chunk));
					duplicateChunk = isDuplicateChunk(chunk);
					if(duplicateChunk) {
						boundary = new ChunkBoundary((lastChunkIndex),
								(lastChunkIndex = lastChunkIndex + chunk.length));
						System.out.println("matched boundary: "+ boundary.toString());
						boundaries.add(boundary);
						unmatchedDataLength = 0;
					} else {
						System.arraycopy(chunk, 0, tempChunk, 0, chunk.length);
						++unmatchedDataLength;
					}
					dataAvailable = false;
				} else if (!duplicateChunk && slideCounter == 0) {
					System.out.println("!duplicateChunk && slideCounter == 0");
					do {
						//System.out.println("do -> !duplicateChunk && slideCounter == 0");
						slideChunk = new byte[tempChunk.length];
						System.arraycopy(tempChunk, 1, slideChunk, 0, (tempChunk.length-1));
						slideChunk[tempChunk.length-1] = chunk[slideCounter];
						System.out.println("Slide: " + new String(slideChunk) + ", TempChunk: " + new String(tempChunk) +", Chunk: " + new String(chunk));
						duplicateChunk = isDuplicateChunk(slideChunk);
						++slideCounter;
						if(duplicateChunk) {
							System.out.println("Duplicate Chunk found");
							// unmatched data boundary
							boundary = new ChunkBoundary((lastChunkIndex),
									(lastChunkIndex = lastChunkIndex + unmatchedDataLength));
							boundaries.add(boundary);
							System.out.println("Unmatched boundary: "+ boundary.toString());
							// matched data boundary
							boundary = new ChunkBoundary((lastChunkIndex),
									(lastChunkIndex = lastChunkIndex + slideChunk.length));
							boundaries.add(boundary);
							System.out.println("matched boundary: "+ boundary.toString());
							unmatchedDataLength = 0;
							if(slideCounter < chunk.length) {
								readLength = (chunkSize - (chunk.length - slideCounter));//chunkSize - slideCounter;
								if(readLength > fileSizeToRead) {
									readLength = (int) fileSizeToRead;
								}
								//tempChunk = new byte[(chunk.length - slideCounter)+readLength];
								tempChunk = new byte[chunkSize];
								int lengthToCopy = chunk.length - slideCounter;
								System.arraycopy(chunk, slideCounter, tempChunk, 0, lengthToCopy);
								chunk = new byte[readLength];
								read = inputStream.read(chunk, 0, readLength);
								System.arraycopy(chunk, 0, tempChunk, lengthToCopy, chunk.length);
								chunk = new byte[tempChunk.length];
								System.arraycopy(tempChunk, 0, chunk, 0, tempChunk.length);
								fileSizeToRead = fileSizeToRead - read;
								dataAvailable = true;
								slideCounter = 0;
							}
						} else {
							++unmatchedDataLength;
							System.arraycopy(slideChunk, 0, tempChunk, 0, slideChunk.length);
							dataAvailable = false;
						}
					} while(!duplicateChunk && (slideCounter < chunk.length));
					if(!duplicateChunk) {
						slideCounter = 0;
					}
				} else {
					System.out.println("******************* Unknown state *************");
				}
				++iteration;
			} while (fileSizeToRead > 0);
			if(lastChunkIndex < fileSize) {
				ChunkBoundary b = new ChunkBoundary(lastChunkIndex, fileSize);
				boundaries.add(b);
			}
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					
				}
			}
		}
		
		// rearrange boundaries
		List<ChunkBoundary> bList = new LinkedList<ChunkBoundary>();
		for(ChunkBoundary b : boundaries) {
			if(chunkSize < (b.getEndIndex() - b.getStartIndex())) {
				int j = 0;
				long boundaryLength = (b.getEndIndex() - b.getStartIndex());
				do {
					long startIndex = 0L;
					long endIndex = 0L;
					if(j == 0) {
						startIndex = b.getStartIndex();
						endIndex = (b.getStartIndex() + chunkSize);
					} else {
						startIndex = b.getStartIndex() + (j*chunkSize);
						if(boundaryLength < chunkSize) {
							endIndex = b.getEndIndex();
						} else {
							endIndex = startIndex + chunkSize;
						}
					}
					ChunkBoundary cb = new ChunkBoundary(startIndex, endIndex);
					bList.add(cb);
					j++;
					boundaryLength = boundaryLength - chunkSize;
				} while (boundaryLength > 0);
			} else {
				bList.add(b);
			}
		}
		return bList;
	}
	
	private static boolean isDuplicateChunk(byte[] chunk)
	throws Exception {
		String chunkHash = FileUtils.getShaHash(chunk);
		FileChunk fileChunk = FileManager.findDuplicateChunk(chunkHash);
		return (fileChunk != null);
	}
	

}
