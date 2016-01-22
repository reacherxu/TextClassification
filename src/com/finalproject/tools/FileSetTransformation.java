package com.finalproject.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 文档集转换工具 ，对文档集里的每个文件进行处理，将所有统计信息写入一个文件以便多次读取
 */
public class FileSetTransformation {

	public void trainformation(String root, String savePath) throws IOException {
		root = pathNormalize(root);
		File saveFile = new File(savePath);
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
				saveFile)));
		File rootDir = new File(root);
		String fileNames[] = rootDir.list();
		int count = 0;
		for (int i = 0; i < fileNames.length; i++) {
			String classPath = root + fileNames[i] + "/";
			count += new File(classPath).list().length;
		}
		writer.print("total:");
		writer.print(count);
		writer.print(" ");
		for (int i = 0; i < fileNames.length; i++) {
			String classPath = root + fileNames[i] + "/";
			writer.print(fileNames[i]);
			writer.print(":");
			writer.print(new File(classPath).list().length);
			writer.print(" ");
		}
		writer.println();

		for (int i = 0; i < fileNames.length; i++) {
			String classPath = root + fileNames[i] + "/";

			classTrainformation(fileNames[i], classPath, writer);
		}
		writer.close();
	}

	public void classTrainformation(String className, String classPath,
			PrintWriter writer) {
		File classFile = new File(classPath);
		String[] filelist = classFile.list();
		for (int i = 0; i < filelist.length; i++) {
			String filePath = classPath + filelist[i];
			String content = DocumentReader.readFile(filePath);
			fileTrainFormation(className, content, writer);
		}
	}

	public void fileTrainFormation(String className, String content,
			PrintWriter writer) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		content = DocumentPrepare.prepare(content);
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		String[] words = splitter.split(content);
		for (int i = 0; i < words.length; i++) {
			if (!words[i].trim().equals("")) {
				if (map.containsKey(words[i])) {
					int count = map.get(words[i]);
					map.put(words[i], count + 1);
				} else {
					map.put(words[i], 1);
				}
			}
		}
		writer.print(className);
		writer.print(" ");
		for (String word : map.keySet()) {
			writer.print(word);
			writer.print(":");
			writer.print(map.get(word));
			writer.print(" ");
		}
		writer.println();
	}

	public String pathNormalize(String path) {
		if (!path.endsWith("/"))
			path = path + "/";
		return path;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		FileSetTransformation transformation = new FileSetTransformation();
		System.out.println("transfomation test files......");
		transformation.trainformation("D:\\temp\\fudan corpus\\answer", "D:\\temp\\fudan corpus\\testSetFiles.txt");
		System.out.println("transfomating train files......");
		transformation.trainformation("D:\\temp\\fudan corpus\\train", "D:\\temp\\fudan corpus\\trainSetFiles.txt");
	}
}
