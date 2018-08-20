package com.fileupload.db;

public class FileChunk {
	
	private Integer chunkId;
	
	private Integer fileId;
	
	private Integer chunkSequence;
	
	private Integer duplicateChunkId;
	
	private String chunkHash;

	public Integer getChunkId() {
		return chunkId;
	}

	public void setChunkId(Integer chunkId) {
		this.chunkId = chunkId;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getChunkSequence() {
		return chunkSequence;
	}

	public void setChunkSequence(Integer chunkSequence) {
		this.chunkSequence = chunkSequence;
	}

	public Integer getDuplicateChunkId() {
		return duplicateChunkId;
	}

	public void setDuplicateChunkId(Integer duplicateChunkId) {
		this.duplicateChunkId = duplicateChunkId;
	}

	public String getChunkHash() {
		return chunkHash;
	}

	public void setChunkHash(String chunkHash) {
		this.chunkHash = chunkHash;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer()
				.append("FileChunk - chunkId: ")
				.append(getChunkId())
				.append(", fileId: ")
				.append(getFileId())
				.append(", chunkSequence: ")
				.append(getChunkSequence())
				.append(", chunkHash: ")
				.append(getChunkHash())
				.append(", duplicateChunkId: ")
				.append(getDuplicateChunkId());
		return sb.toString();
	}
}
