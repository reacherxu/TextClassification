package com.finalproject.classifier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.finalproject.classmanage.ClassManager;
import com.finalproject.tools.Log;

/**
 * 实验测试类
 * 
 * @author Administrator
 * 
 */
public class ClassifyTest {

	private String defaultTrainSetPath = "c:/train/";
	private String defaultTestSetPath = "c:/test/";
	private int[] testFileCount;
	private int[][] classifyResult;
	private double[] recalls;
	private double[] precisions;
	private KNNClassifier classifier;
	private ClassManager classManager;

	public ClassifyTest() {
		classifier = new KNNClassifier();
	}

	public ClassifyTest(String function, int lemon) {
		classifier = new KNNClassifier(function, lemon);
	}

	public ClassifyTest(String trainSetPath, String testSetPath,
			String function, int lemon) {
		this.defaultTrainSetPath = pathNormalize(trainSetPath);
		this.defaultTestSetPath = pathNormalize(testSetPath);
		classifier = new KNNClassifier(function, lemon);
	}

	/**
	 * 训练分类模型,但不做特征选择
	 * @param trainSet
	 */
	public void prepare(FileSet trainSet) {
		classifier.prepare(trainSet);
		classManager = classifier.getClassManager();
		int count = classManager.getClassCount();
		testFileCount = new int[count];
		classifyResult = new int[count][count];
		recalls = new double[count];
		precisions = new double[count];
	}

	/**
	 * 采用特定特征选择方法对分类模型进行训练
	 * @param trainSet
	 * @param function
	 * @param lemon
	 */
	public void train(FileSet trainSet, String function, int lemon) {
		classifier.train(trainSet, function, lemon);
		classManager = classifier.getClassManager();
		int count = classManager.getClassCount();
		testFileCount = new int[count];
		classifyResult = new int[count][count];
		recalls = new double[count];
		precisions = new double[count];
	}

	/**
	 * 测试采用不同特征选择方法下的分类模型的分类效率
	 * 
	 * @param testSet
	 */
	public void test(FileSet testSet) {
		Log.log("begine testing!");
		String[] classnameStrings = classManager.getClassNames();
		for (int i = 0; i < classnameStrings.length; i++) {
			int classID = classManager.getClassID(classnameStrings[i]);
			testFileCount[classID] = testSet.getCount(classnameStrings[i]);
		}
		for (int i = 0; i < testSet.size(); i++) {
			Document document = testSet.get(i);
			String className = document.getClassNameString();
			int classID = classManager.getClassID(className);
			doTestStatistic(document, classID);
		}

		for (int i = 0; i < classifyResult.length; i++) {
			for (int j = 0; j < classifyResult.length; j++)
				System.out.print(classifyResult[i][j] + "\t");
			System.out.println();
		}
		doTestCalculate();
		Log.log("testing is done!");
	}

	/**
	 * 测试在特定特征选择方法下的分类模型的分类效率 
	 * @param fileSet
	 * @param function
	 * @param lemon
	 */
	public void test(FileSet fileSet, String function, int lemon) {
		classifier.changeSetting(function, lemon);
		classManager = classifier.getClassManager();
		int count = classManager.getClassCount();
		testFileCount = new int[count];
		classifyResult = new int[count][count];
		recalls = new double[count];
		precisions = new double[count];
		test(fileSet);
	}

	/**
	 * 统计分类正确率,回召率,宏f1与微f1值
	 */
	private void doTestCalculate() {
		int count = classManager.getClassCount();
		for (int i = 0; i < count; i++) {
			double rightCount = classifyResult[i][i];
			recalls[i] = rightCount / testFileCount[i];
			int currentFileCount = 0;
			for (int j = 0; j < count; j++)
				currentFileCount += classifyResult[j][i];
			precisions[i] = rightCount / currentFileCount;
		}
	}

	/**
	 * 测试文档分类情况统计
	 * @param document
	 * @param classID
	 */
	private void doTestStatistic(Document document, int classID) {

		int classifyID = classifier.classifyByID(document);
		classifyResult[classID][classifyID]++;
	}

	/**
	 * 输出结果到文件
	 * @param outputPath
	 */
	public void outputResult(String outputPath) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(
					new FileWriter(outputPath)));
			writer.print("subject:\t\t");
			int length = classManager.getClassCount();
			for (int i = 0; i < length; i++) {
				writer.print(classManager.getClassName(i) + "\t\t");
			}
			writer.println();
			writer.println();

			writer.print("recall:\t\t");
			for (int i = 0; i < length; i++) {
				writer.print(getClassRecall(i) + "\t\t");
			}
			writer.println();
			writer.println();

			writer.print("pricise:\t\t");
			for (int i = 0; i < length; i++) {
				writer.print(getClassPrecision(i) + "\t\t");
			}
			writer.println();
			writer.println();

			writer.print("F1:\t\t");
			for (int i = 0; i < length; i++) {
				writer.print(getClassF1(i) + "\t\t");
			}
			writer.println();

			writer.println();
			writer.println();
			writer.println();
			writer.println();

			writer.print("subject:\t\t");
			writer.println("total");
			writer.println();

			writer.print("Mac_r\t\t");
			writer.println(getMacroRecall());
			writer.println();

			writer.print("Mac_p\t\t");
			writer.println(getMacroPrecision());
			writer.println();

			writer.print("Mac_f1\t\t");
			writer.println(getMacroF1());
			writer.println();

			writer.print("Mic_r\t\t");
			writer.println(getMicroRecall());
			writer.println();

			writer.print("Mic_p\t\t");
			writer.println(getMicroPrecision());
			writer.println();

			writer.print("Mic_f1\t\t");
			writer.println(getMicroF1());
			writer.println();

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回类别召回率
	 * @param classID
	 * @return
	 */
	 
	public double getClassRecall(int classID) {
		return recalls[classID];
	}
	/**
	 * 返回类别准确率
	 * @param classID
	 * @return
	 */

	public double getClassPrecision(int classID) {
		return precisions[classID];
	}

	/**
	 * 返回类别F1值
	 * @param classID
	 * @return
	 */
	public double getClassF1(int classID) {
		double F1 = (2 * recalls[classID] * precisions[classID])
				/ (recalls[classID] + precisions[classID]);
		return F1;
	}
	/**
	 * 返回整体宏平均召回率
	 * @return
	 */
	public double getMacroRecall() {
		double recall = 0;
		for (int i = 0; i < recalls.length; i++)
			recall += recalls[i];
		return recall / recalls.length;
	}

	/**
	 * 返回整体宏平均准确率
	 * @return
	 */
	public double getMacroPrecision() {
		double precision = 0;
		for (int i = 0; i < precisions.length; i++)
			precision += precisions[i];
		return precision / precisions.length;
	}

	/**
	 * 返回整体宏平均F1值
	 * @return
	 */
	public double getMacroF1() {
		double macroPrecision = getMacroPrecision();
		double macroRecall = getMacroRecall();
		double macroF1 = (2 * macroRecall * macroPrecision)
				/ (macroRecall + macroPrecision);
		return macroF1;
	}
	

	/**
	 * 返回整体微平均召回率
	 * @return
	 */
	public double getMicroRecall() {
		int totalFileCount = 0;
		double totalRightFileCount = 0;
		for (int i = 0; i < testFileCount.length; i++) {
			totalFileCount += testFileCount[i];
			totalRightFileCount += classifyResult[i][i];
		}
		return totalRightFileCount / totalFileCount;
	}

	/**
	 * 返回整体微平均准确率
	 * @return
	 */
	public double getMicroPrecision() {
		int totalFileCount = 0;
		double totalRightFileCount = 0;
		for (int i = 0; i < testFileCount.length; i++) {
			totalFileCount += testFileCount[i];
			totalRightFileCount += classifyResult[i][i];
		}
		return totalRightFileCount / totalFileCount;
	}

	/**
	 * 返回整体微平均F1值
	 * @return
	 */
	public double getMicroF1() {
		double microPrecision = getMicroPrecision();
		double microRecall = getMicroRecall();
		double microF1 = (2 * microRecall * microPrecision)
				/ (microRecall + microPrecision);
		return microF1;
	}

	/**
	 * 对路径参数进行标准化
	 * @param path
	 * @return
	 */
	public String pathNormalize(String path) {
		if (!path.endsWith("/"))
			path = path + "/";
		return path;
	}
	
	/**
	 * 设置训练,测试文本集路径
	 * @param trainPath
	 * @param testPath
	 */
	public void setPath(String trainPath, String testPath) {
		this.defaultTrainSetPath = trainPath;
		this.defaultTestSetPath = testPath;
	}

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		//读入文档集
		FileSet trainSet = new FileSet("d:/trainSetFiles.txt");
		FileSet testSet = new FileSet("d:/testSetFiles.txt");
		ClassifyTest test = new ClassifyTest();
		//分类准备
		test.prepare(trainSet);
		//读入参数列表
		Scanner scanner = new Scanner(new File("file/parameters1.txt"));
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (!line.trim().equals("")) {
				String[] parameters = line.split(" ");
				String function = parameters[0];
				int lemon = Integer.parseInt(parameters[1]);
				test.test(testSet, function, lemon);
				String outputPath = "result/" + function + "_" + lemon + ".txt";
				test.outputResult(outputPath);
				System.out.println();
				System.out
						.println("********************************************");
				System.out.println();
			}
		}
	}
}
