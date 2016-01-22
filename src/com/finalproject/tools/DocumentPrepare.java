package com.finalproject.tools;

/**
 * 文档预处理工具，比如多空格、标点等处理
 * @author Administrator
 *
 */
public class DocumentPrepare {
		
	public static String prepare(String source){
		String result=source.replaceAll("[^\\u4e00-\\u9fa5\\w]", " ");
		result=result.replaceAll("\\d", " ");
		result=result.replaceAll("\\s{2,}", " ");	
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sourceString=DocumentReader.readFile("C:/train/C7-History/C7-History914.txt");
		ChineseSplitter splitter=ChineseSplitter.getInstance();
		System.out.println(splitter.split(DocumentPrepare.prepare(sourceString)));		
	}
}
