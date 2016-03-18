package com.classifier;

import java.util.Iterator;

import com.classmanage.ClassManager;
import com.featureselect.FeatureManager;
import com.featureselect.FeatureSelector;
import com.featureselect.FeatureVectorSpace;

/**
 * KNN分类器的功能实现类
 * 
 * @author Administrator
 * 
 */
public class KNNClassifierCore {
	String rootPath;
	private FileSet trainSet;
	private ClassManager classManager;
	private FeatureSelector featureSelector;
	private FeatureVectorSpace featureVectorSpace;
	private TrainingSetForKNN trainingSetForKNN;
	
	private int K = 5;

	public TrainingSetForKNN getTrainingSetForKNN() {
		return trainingSetForKNN;
	}

	private class Simulation {
		int classID;
		double simValue;
		String docPosition;

		public Simulation(int classID, double simValue, String docPosition) {
			this.classID = classID;
			this.simValue = simValue;
			this.docPosition = docPosition;
		}

		public String getDocPosition() {
			return docPosition;
		}
		
		public int getClassID() {
			return classID;
		}

		public double getSimValue() {
			return simValue;
		}

		public String toString() {
			return String.valueOf(classID) + "   " + simValue;
		}

	}

	public KNNClassifierCore(String path) {
		this.rootPath = path;
		classManager = new ClassManager(path);
		featureSelector = FeatureSelector.getInstance(path, classManager);
	}

	public KNNClassifierCore(FileSet trainSet, String function, int dimension) {
		this.trainSet = trainSet;
		classManager = new ClassManager(trainSet);
		featureSelector = FeatureSelector.getInstance(trainSet, classManager);
		FeatureManager featureManager = featureSelector.doFeatureSelect(
				function, dimension);
		featureVectorSpace = featureManager.getFeatureVectorSpace();
		trainingSetForKNN = new TrainingSetForKNN(trainSet, featureVectorSpace,
				classManager);
	}

	public KNNClassifierCore(FileSet trainSet) {
		this.trainSet = trainSet;
		classManager = new ClassManager(trainSet);
		featureSelector = FeatureSelector.getInstance(trainSet, classManager);
	}

	/* 根据参数进行特征选择 */
	public void refresh(String function, int dimension, int K) {
		FeatureManager featureManager = featureSelector.doFeatureSelect(
				function, dimension);
		featureVectorSpace = featureManager.getFeatureVectorSpace();
		trainingSetForKNN = new TrainingSetForKNN(trainSet, featureVectorSpace,
				classManager);
		this.K = K;
	}
	
	/* 根据参数进行特征选择 */
	public void refresh(String function, int dimension) {
		FeatureManager featureManager = featureSelector.doFeatureSelect(
				function, dimension);
		featureVectorSpace = featureManager.getFeatureVectorSpace();
		trainingSetForKNN = new TrainingSetForKNN(trainSet, featureVectorSpace,
				classManager);
	}

	public DocumentVector getDocumentVector(Document document) {
		return new DocumentVector(featureVectorSpace,
				document, classManager);
	}
	
	public int classifyByID(Document document) {
		DocumentVector documentVector = new DocumentVector(featureVectorSpace,
				document, classManager);
		int classID = classify(documentVector);
		return classID;
	}
	
	/**
	 * 寻找最相似的文章
	 * @param document
	 * @return
	 */
	public void maxSimilarity(Document document) {
		DocumentVector documentVector = new DocumentVector(featureVectorSpace,
				document, classManager);
		Simulation[] simulations = getSimilarity(documentVector);
		
		for (int i = 0; i < K; i++) {
			System.out.println("最相似的文章是：" + classManager.getClassName(simulations[i].getClassID()) + ":" + simulations[i].getDocPosition());
		}
	}

	public int classifyByID(String path) {
		DocumentVector documentVector = new DocumentVector(featureVectorSpace,
				-1, path);
		int classID = classify(documentVector);
		return classID;
	}

	public String classifyByName(String path) {
		DocumentVector documentVector = new DocumentVector(featureVectorSpace,
				-1, path);
		int classID = classify(documentVector);
		String className = classManager.getClassName(classID);
		return className;
	}

	public int classify(DocumentVector documentVector) {
		Simulation[] simulations = getSimilarity(documentVector);
		
		int[] values = new int[classManager.getClassCount()];
		
//		int dimension = K * classManager.getClassCount() + 1;
		int dimension = K ;
		for (int i = 0; i < dimension; i++) {
			int classID = simulations[i].getClassID();
			values[classID]++;
		}
		int classID = getMaxValueID(values);
		return classID;
	}


	private Simulation[] getSimilarity(DocumentVector documentVector) {
		int count = 0;
		Simulation[] simulations = new Simulation[trainingSetForKNN.size()];
		Iterator<DocumentVector> iterator = trainingSetForKNN.iterator();
		while (iterator.hasNext()) {
			DocumentVector tempDocumentVector = iterator.next();
			int classID = tempDocumentVector.getClassID();
			String pos = tempDocumentVector.getDocPosition();
			double simValue = simulate(tempDocumentVector, documentVector);
			simulations[count++] = new Simulation(classID, simValue, pos);

		}
		sort(simulations);
		return simulations;
	}

	/**
	 * 两个向量之间的相似度计算
	 * @param first
	 * @param second
	 * @return
	 */
	private double simulate(DocumentVector first, DocumentVector second) {
		double part1 = 0, part2 = 0, part3 = 0;
		double[] firstVector = first.getTFIDF();
		double[] secondVector = second.getTFIDF();
		for (int i = 0; i < firstVector.length; i++) {
			part1 += firstVector[i] * secondVector[i];
			part2 += firstVector[i] * firstVector[i];
			part3 += secondVector[i] * secondVector[i];

		}
		double result = 0;
		if (part2 != 0 && part3 != 0)
			result = part1 / Math.sqrt(part2 * part3);
		return result;
	}

	private void sort(Simulation[] simulations) {
		quickSort(simulations, 0, simulations.length - 1);
	}

	private void quickSort(Simulation[] simulations, int low, int high) {

		if (low < high) {
			int pivotloc = quickSort_Patition(simulations, low, high);
			quickSort(simulations, low, pivotloc - 1);
			quickSort(simulations, pivotloc + 1, high);
		}
	}

	private int quickSort_Patition(Simulation[] simulations, int low, int high) {

		Simulation tempSimulation = simulations[low];
		double piovotkey = simulations[low].getSimValue();
		while (low < high) {
			while (low < high && simulations[high].getSimValue() <= piovotkey)
				--high;
			simulations[low] = simulations[high];
			while (low < high && simulations[low].getSimValue() >= piovotkey)
				++low;
			simulations[high] = simulations[low];
		}
		simulations[low] = tempSimulation;
		return low;
	}

	private int getMaxValueID(int[] values) {
		int maxValue = values[0];
		int maxValueID = 0;
		for (int i = 1; i < values.length; i++) {
			if (maxValue < values[i]) {
				maxValue = values[i];
				maxValueID = i;
			}
		}
		return maxValueID;
	}

	public ClassManager getClassManager() {
		return classManager;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
}
