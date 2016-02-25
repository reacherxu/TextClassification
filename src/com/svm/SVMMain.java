package com.svm;

import java.io.IOException;

/**
 * 后台用的生成模型，直接运行即可
 * @author Administrator
 *
 */
public class SVMMain {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException{
		
		//scale参数
		/*String[] sarg_train = {"-l","0","-o","D:/fudan/fudan_svm_scale.train","D:/fudan/train.txt"};
		String[] sarg_test = {"-l","0","-o","D:/fudan/fudan_svm_scale.test","D:/fudan/test.txt"};

		System.out.println("训练集开始缩放");
		svm_scale scale = new svm_scale();
		scale.main(sarg_train);
		System.out.println("缩放结束");
		
		System.out.println("测试集开始缩放");
		scale = new svm_scale();
		scale.main(sarg_test);
		System.out.println("缩放结束");*/
		
		
		//train参数
		String[] arg = {"-t","0","D:/fudan/fudan_svm_scale.train","corpus_train/svm.model"};
		//predict参数
		String[] parg = {"D:/fudan/fudan_svm_scale.test","corpus_train/svm.model","D:/fudan/result.txt"};
		
		System.out.println("训练开始");
		svm_train.main(arg);
		System.out.println("训练结束");

		System.out.println("分类开始");
		svm_predict.main(parg);
		System.out.println("分类结束");
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
