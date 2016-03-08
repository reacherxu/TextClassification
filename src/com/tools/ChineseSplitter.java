package com.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import ICTCLAS.I3S.AC.CLibrary;

import com.sun.jna.Native;
/**
 * 中文分词工具，使用中科院的ICTCLAS3.0分词组件进行分词
 * @author Administrator
 *
 */
public class ChineseSplitter {
	private static ChineseSplitter instance;
	private CLibrary ictclas30;

	private ChineseSplitter() {
		ictclas30 = (CLibrary)Native.loadLibrary(
				System.getProperty("user.dir")+"\\source\\NLPIR", CLibrary.class);
		int init_flag = ictclas30.NLPIR_Init("", 1, "0");
		String resultString = null;
		if (0 == init_flag) {
			resultString = ictclas30.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！\n"+resultString);
			return;
		}

	}

	/**
	 * 返回分词结果
	 * @param source
	 * @return
	 */
	public String[] split(String source) {
		try {
			String resultString = ictclas30.NLPIR_ParagraphProcess(source, 1);
			//	            System.out.println("分词结果为：\n " + resultString);

			String[] allWords = resultString.split("\\s");
			return allWords;
		} catch (Exception e) {
			System.out.println("错误信息：");
			e.printStackTrace();
		}

		return null;
	}
	
	public String[] split(String source, int pos) {
		try {
			String resultString = ictclas30.NLPIR_ParagraphProcess(source, pos);
			//	            System.out.println("分词结果为：\n " + resultString);

			String[] allWords = resultString.split("\\s");
			return allWords;
		} catch (Exception e) {
			System.out.println("错误信息：");
			e.printStackTrace();
		}

		return null;
	}


	public static ChineseSplitter getInstance() {
		if (instance == null) {
			instance = new ChineseSplitter();
		}
		return instance;
	}

	public void close() {
		ictclas30.NLPIR_Exit();
	}

	
	public String pathNormalize(String path) {
		if (!path.endsWith("/"))
			path = path + "/";
		return path;
	}
	
	public void trainformation(String root, String savePath) throws IOException {
		root = FilePathHandler.pathNormalize(root);
		File saveFile = new File(savePath);
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
				saveFile)));
		
		//记录每类下文件数量
		File rootDir = new File(root);
		String fileNames[] = rootDir.list();
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
		content = DocumentPrepare.prepare(content);
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		String[] words = splitter.split(content, 0);
		
		for (String word :words) {
			writer.print(word);
			writer.print(" ");
		}
		writer.println();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*ChineseSplitter splitter = ChineseSplitter.getInstance();
		String sInput = "哎~那个金刚圈尺寸太差，前重后轻，左宽右窄，他戴上去很不舒服，"
				+ "整晚失眠会连累我嘛，他虽然是只猴子，但你也不能这样对他啊，官府知道会说我虐待动物的，"
				+ "说起那个金刚圈，啊~去年我在陈家村认识了一个铁匠，他手工精美，价钱又公道，童叟无欺，"
				+ "干脆我介绍你再定做一个吧！";
		String[] words = splitter.split(sInput, 0);
		for (int i = 0; i < words.length; i++)
			System.out.print(words[i] + " ");
		splitter.close();*/
		
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		try {
			Log.log("w2v file transformation started.....");
			splitter.trainformation("D:\\temp\\fudan_subset_subset\\train", "D:\\temp\\fudan_subset_subset\\file.w2v");
			Log.log("w2v file transformation ended.....");
		} catch (IOException e) {
			e.printStackTrace();
		}
		splitter.close();
	}
}
