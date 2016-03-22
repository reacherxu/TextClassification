package com.svm;

import java.io.IOException;

/**
 * 后台用的生成模型，直接运行即可
 * @author Administrator
 *
 */
public class SVMMain {
	
	public static void predict(int t) throws Exception {
		//scale参数
		String[] sarg_train = {"-l","0","-o","D:/lda/train.svm","D:/lda/train_"+t+".lda"};
		String[] sarg_test = {"-l","0","-o","D:/lda/test.svm","D:/lda/test_"+t+".lda"};

		System.out.println("训练集开始缩放");
		svm_scale.main(sarg_train);
		System.out.println("缩放结束");

		System.out.println("测试集开始缩放");
		svm_scale.main(sarg_test);
		System.out.println("缩放结束");


		//train参数
		String[] arg = {"-t","0","D:/lda/train.svm","svm.model"};
		//predict参数
		String[] parg = {"D:/lda/test.svm","svm.model","D:/lda/result_"+t+".txt"};

		System.out.println("训练开始");
		svm_train.main(arg);
		System.out.println("训练结束");

		System.out.println("分类开始");
		svm_predict.main(parg);
		System.out.println("分类结束");
	}

	public static void main(String[] args) throws IOException{
//		int t = 10;
		
	/*	//scale参数
		String[] sarg = {"-l","0","-s","corpus_train/svm.scale","-o","corpus_train/svmscale.train","corpus_train/svm.train"};
		//train参数
		String[] arg = {"-t","0","corpus_train/svmscale.train","corpus_train/svm.model"};
		//predict参数
		String[] parg = {"corpus_test/svmscale.test","corpus_train/svm.model","corpus_test/result.txt"};

		System.out.println("开始缩放");
		svm_scale scale = new svm_scale();
		scale.main(sarg);
		System.out.println("缩放结束");

		System.out.println("训练开始");
		svm_train.main(arg);
		System.out.println("训练结束");

		System.out.println("分类开始");
		svm_predict.main(parg);
		System.out.println("分类结束");*/



		/*//scale参数
		String[] sarg = {"-l","0","-s","trainfile/svm.scale","-o","trainfile/svmscale.train","trainfile/svm.train"};
		//train参数
		String[] arg = {"-t","0","-v","5","trainfile/svmscale.train","trainfile/svm.model"};
		//predict参数
		String[] parg = {"testfile/svmscale.test","trainfile/svm.model","testfile/result.txt"};

		System.out.println("开始缩放");
		svm_scale scale = new svm_scale();
		scale.main(sarg);
		System.out.println("缩放结束");

		System.out.println("训练开始");
		svm_train.main(arg);
		System.out.println("训练结束");

		System.out.println("分类开始");
		svm_predict.main(parg);
		System.out.println("分类结束");*/

	}
}
