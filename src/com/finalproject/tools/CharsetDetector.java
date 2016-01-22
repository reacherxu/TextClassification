package com.finalproject.tools;

import java.io.File;
/**
 * 文档编码检测工具
 * @author Administrator
 *
 */
public class CharsetDetector {

	/**
	 * 检测文档编码
	 * @param path
	 */
	public static void detect(String path) {
		cpdetector.io.CodepageDetectorProxy detector = cpdetector.io.CodepageDetectorProxy
				.getInstance();

		detector.add(new cpdetector.io.ParsingDetector(false));

		detector.add(cpdetector.io.JChardetFacade.getInstance());
		// ASCIIDetector用于ASCII编码测定
		detector.add(cpdetector.io.ASCIIDetector.getInstance());
		// UnicodeDetector用于Unicode家族编码的测定
		detector.add(cpdetector.io.UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		File f = new File(path);
		try {
			charset = detector.detectCodepage(f.toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			if (!charset.name().equals("GB2312"))
				System.out.println(f.getName() + "编码是：" + charset.name());
			System.out.println(DocumentReader.readFile(path));
			System.out.println();
		} else
			System.out.println(f.getName() + "未知");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path="c:/train/";
		File root=new File(path);
		String []dirnameStrings=root.list();
		for(int i=0;i<dirnameStrings.length;i++){
			String newPathString=path+dirnameStrings[i]+"/";
			File dirFile=new File(newPathString);
			String []fileStrings=dirFile.list();
			for(int j=0;j<fileStrings.length;j++){
				CharsetDetector.detect(newPathString+fileStrings[j]);
			}
		}
	}
}
