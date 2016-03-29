package com.hankcs.lda;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;

import com.classmanage.ClassManager;
import com.tools.Log;

public class TestCorpus 
{

	public static void generateSVMModel(int t, ClassManager classManager) throws Exception {
		/*// 1. Load corpus from disk
		Corpus corpus = Corpus.load("D:\\lda\\ldaFileTrain\\");
		// 2. Create a LDA sampler
		LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(corpus.getDocument(), 
				corpus.getVocabularySize());
		// 3. Train it with t subject
		ldaGibbsSampler.gibbs(t);

		ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream("D:/lda/corpus_"+t+".lda"));
		oout.writeObject(corpus);
		oout.close();

		oout = new ObjectOutputStream(new FileOutputStream("D:/lda/gibbs_"+t+".lda"));
		oout.writeObject(ldaGibbsSampler);
		oout.close();

		Log.log("serialization finished......");*/

		ObjectInputStream oin = new ObjectInputStream(new FileInputStream("D:/lda/corpus_" +t+ ".lda"));
		Corpus corpus = (Corpus)oin.readObject();
		oin.close();

		oin = new ObjectInputStream(new FileInputStream("D:/lda/gibbs_" +t+ ".lda"));
		LdaGibbsSampler ldaGibbsSampler = (LdaGibbsSampler)oin.readObject();
		oin.close();



		// 4. The phi matrix is a LDA model, you can use LdaUtil to explain it.
		//	        double[][] theta = ldaGibbsSampler.getTheta();
		/*  double[][] phi = ldaGibbsSampler.getPhi();
	        double wordvec[] = LdaUtil.getVector(phi, corpus.getVocabulary(), "ÐÄ¾ªÈâÌø");
	        for (int i = 0; i < wordvec.length; i++) {
				System.out.print(wordvec[i] + ",");
			}*/
		//	        Map<String, Double>[] topicMap = LdaUtil.translate(phi, corpus.getVocabulary(), 50);
		//	        LdaUtil.explain(topicMap);


		// 5. TODO:Predict. I'm not sure whether it works, it is not stable.
		double[][] theta = ldaGibbsSampler.getTheta();
		double[][] phi = ldaGibbsSampler.getPhi();
		BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\lda\\train_" +t+ ".lda"));
		LdaUtil.write(theta, corpus.getFileNames(), writer , classManager);
		writer.flush();
		writer.close();

		Log.log("test process started");
		writer = new BufferedWriter(new FileWriter("D:\\lda\\test_" +t+ ".lda"));
		long start = System.currentTimeMillis();
		File category = new File("D:\\lda\\ldaFileTest\\");
		File[] files = category.listFiles();
		for(File f : files) {
			int[] document = Corpus.loadDocument(f, corpus.getVocabulary());
			double[] tp = LdaGibbsSampler.inference(phi, document);
			//	        	Map<String, Double> topic = LdaUtil.translate(tp, phi, corpus.getVocabulary(), 50);
			//        LdaUtil.explain(topic);
			LdaUtil.doc_explain(tp, f.getName(), writer, classManager);
		}
		long end = System.currentTimeMillis();

		writer.flush();
		writer.close();
		Log.log("test process ended,take " + (end-start) + " ms");
	}

	public static void main(String[] args) throws Exception {

		/* document = Corpus.loadDocument("data/mini/IT_110.txt", corpus.getVocabulary());
        tp = LdaGibbsSampler.inference(phi, document);
        topic = LdaUtil.translate(tp, phi, corpus.getVocabulary(), 10);
        LdaUtil.explain(topic);*/
	}
}
