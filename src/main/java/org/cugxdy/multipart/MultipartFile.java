package org.cugxdy.multipart;

import java.util.Arrays;

public class MultipartFile {
	
	private String fileName;
	
	private String fileType;
	
	private byte[] fileData;

	public final String getFileName() {
		return fileName;
	}

	public final void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public final String getFileType() {
		return fileType;
	}

	public final void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public final byte[] getFileData() {
		return fileData;
	}

	public final void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	@Override
	public String toString() {
		return "MultipartFile [fileName=" + fileName + ", fileType=" + fileType + ", fileData="
				+ Arrays.toString(fileData) + "]";
	}
}
