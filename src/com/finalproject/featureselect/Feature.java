package com.finalproject.featureselect;

import com.finalproject.classmanage.ClassManager;
/**
 * 数据结构 ，带统计信息的特征
 * @author Administrator
 *
 */
public class Feature {
	String name;
	int totalFileCount;
	int classFileCountS[];
	
	public Feature(String name,ClassManager classManager){
		this.name=name;
		classFileCountS=new int[classManager.getClassCount()];		
	}
	
	public void increaseFileCount(String feature, int classID) {
		totalFileCount++;
		classFileCountS[classID]++;
	}

	public String getName() {
		return name;
	}

	public int getTotalFileCount() {
		return totalFileCount;
	}

	public int getClassFileCount(int classID) {
		return classFileCountS[classID];
	}	
}
