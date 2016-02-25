package com.classifier;

import com.classmanage.ClassManager;
import com.featureselect.FeatureSelector;
import com.tools.Log;

/**
 * KNN分类的功能接口类
 * 
 * @author Administrator
 * 
 */
public class KNNClassifier {

	private String function = FeatureSelector.DF;
	private int dimension = 500;
	private boolean isTrained;
	private KNNClassifierCore core;

	public KNNClassifierCore getCore() {
		return core;
	}

	public KNNClassifier() {
		isTrained = false;
	}

	public KNNClassifier(String function, int lemon) {
		this.function = function;
		this.dimension = lemon;
		isTrained = false;
	}

	public void changeSetting(String function, int lemon) {
		this.function = function;
		this.dimension = lemon;
		if (isTrained)
			core.refresh(function, lemon);
	}

	public void prepare(String root) {
		Log.log("preparing the KNN classifier......");
		root = pathNormalize(root);
		core = new KNNClassifierCore(root);
		isTrained = true;
		Log.log("KNN classifier is prepared for being trained!");
	}

	public void prepare(FileSet trainSet) {
		Log.log("preparing the KNN classifier......");
		core = new KNNClassifierCore(trainSet);
		isTrained = true;
		Log.log("KNN classifier is prepared for being trained!");
	}

	public void train(FileSet trainSet, String function, int dimension) {

		core = new KNNClassifierCore(trainSet, function, dimension);
		isTrained = true;
		Log.log("KNN classifier has been trained!");
	}

	public int classifyByID(String filePath) {
		return core.classifyByID(filePath);
	}

	public int classifyByID(Document document) {
		return core.classifyByID(document);
	}

	public String classifyByName(String filePath) {
		return core.classifyByName(filePath);
	}

	public String pathNormalize(String path) {
		if (!path.endsWith("/"))
			path = path + "/";
		return path;
	}

	public ClassManager getClassManager() {
		if (isTrained)
			return core.getClassManager();
		else {
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
