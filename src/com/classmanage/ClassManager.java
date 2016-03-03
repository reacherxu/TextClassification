package com.classmanage;

import java.io.File;
import java.util.Vector;

import com.classifier.FileSet;

/**
 * 类别管理器，统计类别名、类别ID及各个类别文档个数等
 * @author Administrator
 * 
 */
public class ClassManager {
	/**
	 *  记录类别名
	 *  与fileset中map中keyset对应
	 */
	private Vector<String> classVector;
	/**
	 *  记录类别文档数
	 */
	private FileCounter fileCounter;

	public ClassManager(FileSet trainSet) {
		classVector = new Vector<String>();
		
		String[] classNameStrings = trainSet.getClassNameStrings();
		fileCounter = new FileCounter(trainSet.getClassCount());
		fileCounter.setTotalFileCount(trainSet.getTotal());
		for (int i = 0; i < classNameStrings.length; i++) {
			classVector.add(classNameStrings[i]);
			fileCounter.setClassFileCount(i,
					trainSet.getCount(classNameStrings[i]));
			
//			fileCounter.setClassFile(document, i);
		}
	}

	/**
	 * 二级文档目录，无fileset时用
	 * @param path
	 */
	public ClassManager(String path) {
		classVector = new Vector<String>();
		try {
			File root = new File(path);
			String[] dirs = root.list();
			fileCounter = new FileCounter(dirs.length);
			for (int i = 0; i < dirs.length; i++) {
				classVector.add(dirs[i]);
				String newPath = path + dirs[i] + "/";
				int number = new File(newPath).list().length;
				fileCounter.setClassFileCount(i, number);
				fileCounter.setTotalFileCount(fileCounter.getTotalFileCount()
						+ number);
			}
		} catch (Exception e) {
		}
	}

	public int getClassCount() {
		return classVector.size();
	}

	public int getClassID(String name) {
		return classVector.indexOf(name);
	}

	public String getClassName(int classID) {
		return classVector.get(classID);
	}

	public String[] getClassNames() {
		String[] names = new String[classVector.size()];
		classVector.toArray(names);
		return names;
	}

	public int getTotalFileCount() {
		return fileCounter.getTotalFileCount();
	}

	public int getClassFileCount(int classID) {
		return fileCounter.getClassFileCount(classID);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
}
