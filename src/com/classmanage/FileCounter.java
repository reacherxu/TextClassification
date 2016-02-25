package com.classmanage;

/**
 * 文件计数器，统计总体及各个类别的文档个数
 * @author Administrator
 * 
 */
public class FileCounter {
	/**
	 * 文档类别的个数
	 * 与fileset中的map对应
	 */
	private int[] classFileCount;
	/**
	 * fileset中的文件数
	 * 与fileset中的total对应
	 */
	private int totalFileCount;

	public FileCounter(int classCount) {
		classFileCount = new int[classCount];
		totalFileCount = 0;
	}

	/**
	 * 获得某个类别的文档个数
	 * @param classID
	 * @return
	 */
	public int getClassFileCount(int classID) {
		return classFileCount[classID];
	}

	public void setClassFileCount(int classID, int fileCount) {
		this.classFileCount[classID] = fileCount;
	}

	public int getTotalFileCount() {
		return totalFileCount;
	}

	public void setTotalFileCount(int totalFileCount) {
		this.totalFileCount = totalFileCount;
	}
}
