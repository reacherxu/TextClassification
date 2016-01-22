package com.finalproject.classifier;

import java.io.File;
import java.util.ArrayList;

import com.finalproject.classmanage.ClassManager;
import com.finalproject.featureselect.FeatureVectorSpace;
import com.finalproject.tools.Log;
/**
 * 用于KNN分类器的训练集数据结构
 * @author Administrator
 *
 */
public class TrainingSetForKNN extends ArrayList<DocumentVector>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String rootPath;
	private FeatureVectorSpace featureVectorSpace;
	private ClassManager classManager;
	public TrainingSetForKNN(String rootPath,FeatureVectorSpace featureVectorSpace,ClassManager classManager) {
		super();
		
		Log.log("creating trainning set......");
		
		this.rootPath=rootPath;
		this.featureVectorSpace=featureVectorSpace;
		this.classManager=classManager;		
		readFiles();
		
		Log.log("trainningset created!");
	}
	
	private void readFiles() {
		String[] classNames = classManager.getClassNames();
		for (int i = 0; i < classNames.length; i++) {
			String newPath = rootPath + classNames[i] + "/";
			int currentClassID = classManager.getClassID(classNames[i]);
			readClassFiles(newPath, currentClassID);
			System.gc();
		}
	}

	
	private void readClassFiles(String path, int classID) {
		File classRoot = new File(path);
		String[] fileNameStrings = classRoot.list();
		for (int i = 0; i < fileNameStrings.length; i++) {
			String newPath = path + fileNameStrings[i];
			DocumentVector fileVector=new DocumentVector(featureVectorSpace,classID,newPath);
			add(fileVector);
		}
	}
	
	public TrainingSetForKNN(FileSet trainSet,FeatureVectorSpace featureVectorSpace,ClassManager classManager) {
		super();		
		Log.log("creating trainning set......");		
		this.featureVectorSpace=featureVectorSpace;
		this.classManager=classManager;		
		for(int i=0;i<trainSet.size();i++){
			Document document=trainSet.get(i);
			DocumentVector fileVector=new DocumentVector(featureVectorSpace,document,classManager);
			add(fileVector);
		}
		
		Log.log("trainningset created!");
	}
	

	public static void main(String[]args){
	}
}
