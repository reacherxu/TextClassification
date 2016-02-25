package com.tools;

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
			String resultString = ictclas30.NLPIR_ParagraphProcess(source, 0);
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		String sInput = "哎~那个金刚圈尺寸太差，前重后轻，左宽右窄，他戴上去很不舒服，"
				+ "整晚失眠会连累我嘛，他虽然是只猴子，但你也不能这样对他啊，官府知道会说我虐待动物的，"
				+ "说起那个金刚圈，啊~去年我在陈家村认识了一个铁匠，他手工精美，价钱又公道，童叟无欺，"
				+ "干脆我介绍你再定做一个吧！";
		String[] words = splitter.split(sInput);
		for (int i = 0; i < words.length; i++)
			System.out.print(words[i] + " ");
		splitter.close();
	}
}
