package com.fileupload.util;

class ChunkBoundary {
	private Long startIndex;
	private Long endIndex;
	
	public ChunkBoundary(Long startIndex, Long endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public Long getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(Long startIndex) {
		this.startIndex = startIndex;
	}
	public Long getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(Long endIndex) {
		this.endIndex = endIndex;
	}
	
	public String toString() {
		return new StringBuffer()
				.append("ChunkBoundary[startIndex:")
				.append(getStartIndex())
				.append(", endIndex:")
				.append(getEndIndex())
				.append("]")
				.toString();
	}
}
