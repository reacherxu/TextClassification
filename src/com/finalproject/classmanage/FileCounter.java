package com.finalproject.classmanage;
/**
 * 文件计数器，统计总体及各个类别的文档个数
 * @author Administrator
 *
 */
public class FileCounter {
	
	private int[] classFileCount;
	private int totalFileCount;
	
	public FileCounter(int classCount){
		classFileCount=new int[classCount];
		totalFileCount=0;
	}

	public int getClassFileCount(int classID) {
		return classFileCount[classID];
	}

	public void setClassFileCount(int classID,int fileCount) {
		this.classFileCount[classID] = fileCount;
	}

	public int getTotalFileCount() {
		return totalFileCount;
	}

	public void setTotalFileCount(int totalFileCount) {
		this.totalFileCount = totalFileCount;
	}
}
