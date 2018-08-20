package com.fileupload.db;

import java.io.Serializable;

public class ApplicationStatistics implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer userId;
	
	private Long totalStorageSize;
	
	private Long compressedStorageSize;
	
	private Integer totalFilesCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getTotalFilesSize() {
		return totalStorageSize;
	}

	public void setTotalFilesSize(Long totalFilesSize) {
		this.totalStorageSize = totalFilesSize;
	}

	public Long getCompressedStorageSize() {
		return compressedStorageSize;
	}

	public void setCompressedStorageSize(Long compressedStorageSize) {
		this.compressedStorageSize = compressedStorageSize;
	}

	public Integer getTotalFilesCount() {
		return totalFilesCount;
	}

	public void setTotalFilesCount(Integer totalFilesCount) {
		this.totalFilesCount = totalFilesCount;
	}

}
