package com.finalproject.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
/**
 * 停用词过滤工具
 * @author Administrator
 *
 */
public class StopWordHandler {
	private static StopWordHandler instance;
	private HashSet<String> stopWordSet;

	private StopWordHandler() {
		stopWordSet = new HashSet<String>();
		try {
			Scanner scanner = new Scanner(new File("file/stop_words_zh.txt"));
			while (scanner.hasNextLine()) {
				String tempString = scanner.nextLine().trim();
				if (!tempString.equals("")) {
					stopWordSet.add(tempString);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 单例调用接口
	 * @return
	 */
	public static StopWordHandler getInstance(){
		if(instance==null){
			instance=new StopWordHandler();
		}
		return instance;
	}

    /**
     * 判断一个词是否为停用词
     * @param word
     * @return
     */
	public boolean isStopWord(String word) {
		if (stopWordSet.contains(word))
			return true;
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StopWordHandler stopWordHandler=new StopWordHandler();
		System.out.print(stopWordHandler.isStopWord("战争"));

	}

}
