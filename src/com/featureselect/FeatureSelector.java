package com.featureselect;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.classifier.Document;
import com.classifier.FileSet;
import com.classmanage.ClassManager;
import com.featureselect.evaluate.Evaluate;
import com.featureselect.evaluate.EvaluateFactory;
import com.tools.ChineseSplitter;
import com.tools.DocumentPrepare;
import com.tools.DocumentReader;
import com.tools.Log;
import com.tools.StopWordHandler;
/**
 * 特征选择器，使用特征选择方法对特征进行打分并根据给定的阈值提取特征
 * @author Administrator
 *
 */
public class FeatureSelector {

	public static FeatureSelector instance;
	
	/* 特征选择方法 */
	public static final String DF = "DF";
	public static final String IG = "IG";
	public static final String MI = "MI";
	public static final String CE = "CE";
	public static final String X2 = "X2";
	public static final String X2_Impo = "X2_Impo";
	public static final String IMPROVE = "IMPROVE";
	
	private FeatureManager featureManager;
	private StopWordHandler stopWordHandler;
	private ClassManager classManager;
	private ChineseSplitter splitter;
	private int count = 0;
	
	/**
	 * 单例模式
	 * @param path
	 * @param classManager
	 * @return
	 */
	public static FeatureSelector getInstance(String path,
			ClassManager classManager) {
		if (instance == null) {
			instance = new FeatureSelector(path, classManager);
		}
		return instance;
	}

	public static FeatureSelector getInstance(FileSet trainSet,
			ClassManager classManager) {
		if (instance == null) {
			instance = new FeatureSelector(trainSet, classManager);
		}
		return instance;
	}

	private FeatureSelector(String path, ClassManager classManager) {
		this.classManager = classManager;
		stopWordHandler = StopWordHandler.getInstance();
		splitter = ChineseSplitter.getInstance();
		featureManager = new FeatureManager(classManager);

		Log.log("preparing for feature-selecting......");

		doStatistic(path);
	}

	private FeatureSelector(FileSet trainSet, ClassManager classManager) {
		this.classManager = classManager;
		stopWordHandler = StopWordHandler.getInstance();
		splitter = ChineseSplitter.getInstance();
		featureManager = new FeatureManager(classManager);

		Log.log("preparing for feature-selecting......");

		doStatistic(trainSet);
	}
 
	/**
	 * 统计训练集的文本特征情况
	 * @param trainSet
	 */
	private void doStatistic(FileSet trainSet) {
		
		for (int i = 0; i < trainSet.size(); i++) {
			Document document = trainSet.get(i);
			
			Set<String> tempSet = document.keySet();
			Iterator<String> iterator = tempSet.iterator();
			int classID = classManager.getClassID(document.getClassNameString());
			while (iterator.hasNext()) {
				String feature = iterator.next();  //特征为词
				if (!stopWordHandler.isStopWord(feature)) {
					if (!hasFeature(feature))
						addFeature(feature);
					increaseFeatureCount(feature, classID);
				}
			}
		}
	}

	private void doStatistic(String rootPath) {
		String[] classNames = classManager.getClassNames();
		for (int i = 0; i < classNames.length; i++) {
			String newPath = rootPath + classNames[i] + "/";
			int currentClassID = classManager.getClassID(classNames[i]);
			doClassStatistic(newPath, currentClassID);
			System.gc();
		}
	}

	private void doClassStatistic(String path, int classID) {
		File classRoot = new File(path);
		String[] fileNameStrings = classRoot.list();
		for (int i = 0; i < fileNameStrings.length; i++) {
			String newPath = path + fileNameStrings[i] + "/";
			// System.out.println(++count+"\t\t"+fileNameStrings[i]);
			doFileStatistic(newPath, classID);
		}
	}

	private void doFileStatistic(String path, int classID) {
		/* 对文件预处理+分词 */
		String content = DocumentReader.readFile(path);
		content = DocumentPrepare.prepare(content);
		String[] word = splitter.split(content);
		
		/* FeatureManager 管理所有的feature */
		//TODO  并没有利用tf
		Set<String> tempSet = new HashSet<String>();
		for (int i = 0; i < word.length; i++) {
			if (!stopWordHandler.isStopWord(word[i])
					&& !word[i].trim().equals(""))
				tempSet.add(word[i]);
		}
		Iterator<String> iterator = tempSet.iterator();
		while (iterator.hasNext()) {
			String feature = iterator.next();
			 
			/* featureManager中的map管理所有feature */
			if (!hasFeature(feature))
				addFeature(feature);
			
			increaseFeatureCount(feature, classID);
		}
	}

	/**
	 * 特征选择过程
	 * @param function
	 * @param dimension
	 * @return
	 */
	public FeatureManager doFeatureSelect(String function, int dimension) {
		Log.log("selecting features with function at " + function
				+ " and dimension at " + dimension + "......");
		
		MarkedFeature[] markedFeatures = new MarkedFeature[getFeatureCount()];
		String[] featureStrings = getFeature();
		EvaluateFactory evaluateFactory = EvaluateFactory.getInstance();
		Evaluate evaluate = evaluateFactory.getEvaluate(function);
		for (int i = 0; i < featureStrings.length; i++) {
			MarkedFeature markedFeature = new MarkedFeature(featureStrings[i]);
			double mark = evaluate.mark(featureStrings[i], classManager, this);
			markedFeature.setMark(mark);
			markedFeatures[i] = markedFeature;
		}
		// System.out.println(getFeatureCount());
		count = 0;
		bubble_sort(markedFeatures);
		FeatureManager result = select(markedFeatures, dimension);
		return result;
	}

	/**
	 * 从大到小排序
	 * @param features
	 */
	private void bubble_sort(MarkedFeature[] features) {
		for (int i = 0; i < features.length; i++) {
			for (int j = i; j < features.length; j++) {
				if (features[i].getMark() < features[j].getMark()) {

					MarkedFeature temp = features[i];
					features[i] = features[j];
					features[j] = temp;
				}
			}
		}
	}
	/**
	 * 排序处理
	 * @param features
	 */
	private void sort(MarkedFeature[] features) {
		quickSort(features, 0, features.length - 1);
	}

	/**
	 * 快速排序算法
	 * @param features
	 * @param low
	 * @param high
	 */
	private void quickSort(MarkedFeature[] features, int low, int high) {
		if (low < high) {
			int pivotloc = quickSort_Patition(features, low, high);
			// System.out.println(++count);
			quickSort(features, low, pivotloc - 1);
			quickSort(features, pivotloc + 1, high);
		}
	}

	private int quickSort_Patition(MarkedFeature[] features, int low, int high) {
		MarkedFeature tempMarkedFeature = features[low];
		double piovotkey = features[low].getMark();
		while (low < high) {
			while (low < high && features[high].getMark() <= piovotkey)
				--high;
			features[low] = features[high];
			while (low < high && features[low].getMark() >= piovotkey)
				++low;
			features[high] = features[low];
		}
		features[low] = tempMarkedFeature;
		return low;
	}
    /**
     * 采用给定的阈值对排序后的序列进行特征选取
     * @param features
     * @param limen
     * @return
     */
	private FeatureManager select(MarkedFeature[] features, int limen) {
		if (limen < features.length) {
			FeatureManager tempFeatureManager = new FeatureManager(classManager);
			for (int i = 0; i < limen; i++) {
				Feature tempFeature = featureManager.getFeature(features[i]
						.getName());
				tempFeatureManager.addFeature(tempFeature);
			}
			return tempFeatureManager;
		} else {
			return featureManager;
		}
	}

	private void addFeature(String feature) {
		featureManager.addFeature(feature);
	}

	// private void removeFeature(String feature){
	// featureManager.removeFeature(feature);
	// }

	private void increaseFeatureCount(String feature, int classID) {
		featureManager.increaseFeatureCount(feature, classID);
	}

	private boolean hasFeature(String feature) {
		return featureManager.hasFeature(feature);
	}

	public int getFeatureCount() {
		return featureManager.getFeatureCount();
	}

	public String[] getFeature() {
		return featureManager.getFeatures();
	}

	public int getTotalFileCountOfFeature(String feature) {
		return featureManager.getTotalFileCountOfFeature(feature);
	}

	public int getClassFileCountOfFeature(int classID, String feature) {
		return featureManager.getClassFileCountOfFeature(classID, feature);
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassManager classManager = new ClassManager("D:\\data\\corpus_mini\\train\\");
		FeatureSelector featureItemManager = FeatureSelector.getInstance(
				"D:\\data\\corpus_mini\\train\\", classManager);
		FeatureManager featureManager = featureItemManager.doFeatureSelect(
				FeatureSelector.DF, 500);
		String[] featureStrings = featureManager.getFeatures();
		for (int i = 0; i < featureStrings.length; i++)
			System.out.println(featureStrings[i]
					+ "\t"
					+ featureItemManager
							.getTotalFileCountOfFeature(featureStrings[i]));
		System.out.println("done");
	}

}
