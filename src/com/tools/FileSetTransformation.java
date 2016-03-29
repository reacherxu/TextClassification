package com.tools;

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
		root = FilePathHandler.pathNormalize(root);
		File saveFile = new File(savePath);
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
				saveFile)));
		
		//记录每类下文件数量
		File rootDir = new File(root);
		String fileNames[] = rootDir.list();
		/*int count = 0;
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
		writer.println();*/

		/*
		 *对每个类下的文件记录词频 
		 */
		for (int i = 0; i < fileNames.length; i++) {
			String classPath = root + fileNames[i] + "/";

			classTrainformation(fileNames[i], classPath, writer);
		}
		writer.close();
	}

	/**
	 * 将某个类下的全部文件转换
	 * @param className  类名
	 * @param classPath	 类路径
	 * @param writer
	 */
	public void classTrainformation(String className, String classPath,
			PrintWriter writer) {
		File classFile = new File(classPath);
		String[] filelist = classFile.list();  
		
		for (int i = 0; i < filelist.length; i++) {
			String filePath = classPath + filelist[i];
			
			String content = DocumentReader.readFile(filePath);
			
//			writer.print(className.trim() + ":" + filelist[i].trim());
//			writer.print(" ");
			fileTrainFormation(className, content, writer);
		}
	}
	
	/**
	 * 对单个文件进行转换
	 * @param file
	 */
	public void fileTransformation(String file) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					"d:/document_test.txt")));
			String content = DocumentReader.readFile(file);
			
			fileTrainFormation(null, content, writer);
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.log("File transformation fininshed");
		
	}

	/**
	 * 记录文档词频
	 * @param className
	 * @param content
	 * @param writer
	 */
	public void fileTrainFormation(String className, String content,
			PrintWriter writer) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		content = DocumentPrepare.prepare(content);
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		String[] words = splitter.split(content);
		/*for (int i = 0; i < words.length; i++) {
			if (!words[i].trim().equals("")) {
				if (map.containsKey(words[i])) {
					int count = map.get(words[i]);
					map.put(words[i], count + 1);
				} else {
					map.put(words[i], 1);
				}
			}
		}
		
		for (String word : map.keySet()) {
			writer.print(word);
			writer.print(":");
			writer.print(map.get(word));
			writer.print(" ");
		}*/
		for (int i = 0; i < words.length; i++) {
			String word = filter(words[i]);
			if( word != null ) {
				writer.print(word);
				writer.print(" ");
			}
		}
		writer.println();
	}
//-------------------------------------------------------------
	
	public void trainformation(String root) throws IOException {
		Log.log("Lda file process started....");
		
		root = FilePathHandler.pathNormalize(root);
		
		//记录每类下文件数量
		File rootDir = new File(root);
		String fileNames[] = rootDir.list();
		
		for (int i = 0; i < fileNames.length; i++) {
			String classPath = root + fileNames[i] + "/";

			/* 处理每类下的文件 */
			File classFile = new File(classPath);
			String[] filelist = classFile.list();  
			
			for (int j = 0; j < filelist.length; j++) {
				String filePath = classPath + filelist[j];
				
				LdaFile(filePath, filelist[j]);
			}
		}
		
		Log.log("Lda file process ended....");
	}

	private void LdaFile(String filePath, String fileName) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("d:/lda/ldaFileTest/"+fileName));
		
		String content = DocumentReader.readFile(filePath);
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		String[] words = splitter.split(content);
		
		for (int i = 0; i < words.length; i++) {
			
			String word = filter(words[i].trim());
			if( null != word)
				writer.write(word + " ");
		}
		
		writer.flush();
		writer.close();

	}
	
	private String filter(String str) {
		String wt[] = str.split("/");
		if( wt.length < 2) 
			return null;
		
		String item = wt[0];
		String ext = wt[1];
		
		//TODO　看看其他词性有没有影响
		if ( (ext.startsWith("n")&& !ext.startsWith("nr")) || ext.startsWith("un")
				|| ext.startsWith("v") || ext.startsWith("a")) {
			return item;
		}
		return null;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		FileSetTransformation transformation = new FileSetTransformation();
//		transformation.trainformation("D:\\temp\\fudan_subset_subset\\test\\");
//		transformation.fileTransformation("d:/C11-Space0028.txt");
//		Log.log("test files transformation started......");
//		transformation.trainformation("D:\\temp\\law-article\\test", "D:\\temp\\law-article\\testSetFiles.txt");
//		Log.log("test files transformation ended......");
		
		Log.log("train files files transformation started......");
		transformation.trainformation("D:\\temp\\law-article\\train", "D:\\temp\\law-article\\file.w2v");
		Log.log("train files transformation ended......");
	}
}
