package com.fileupload.db;

import java.io.Serializable;

public class UserPreferences implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer userId;
	
	private String fileUploadPath;
	
	private Integer chunkSize;
	
	private String dedupeType;
	
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

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public Integer getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(Integer chunkSize) {
		this.chunkSize = chunkSize;
	}

	public String getDedupeType() {
		return dedupeType;
	}

	public void setDedupeType(String dedupeType) {
		this.dedupeType = dedupeType;
	}

}
