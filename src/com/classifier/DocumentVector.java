package com.classifier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.classmanage.ClassManager;
import com.featureselect.FeatureVectorSpace;
import com.hankcs.lda.Corpus;
import com.hankcs.lda.LdaGibbsSampler;
import com.hankcs.lda.LdaUtil;
import com.tools.ChineseSplitter;
import com.tools.DocumentReader;
import com.word2vec.vec.VectorModel;

/**
 * 由特征向量空间模型表示的文档
 * 
 * @author Administrator
 * 
 */
public class DocumentVector {

	int classID;
	double[] tf;
	double[] tf_idf;
	private String docPosition;
	final int dimension = 50;
	double[] wordVec = new double[dimension];
	/**
	 * word2vec   
	 */
	Boolean word2vec = true;  
	/**
	 * lda
	 */
	Boolean lda = false;
	
	/**
	 * word2vec  model
	 */
	String modelFilePath = "D:/temp/w2v_law_corpus_50.nn";
	
	/**
	 * lda model
	 */
	String ldaCorpusPath = "D:/lda/corpus_" + dimension + ".lda";
	String ldaGibbsPath = "D:/lda/gibbs_" + dimension + ".lda";
	
	public DocumentVector(FeatureVectorSpace featureVectorSpace, int classID,
			String path) {
		this.classID = classID;
		tf = new double[featureVectorSpace.getFeatureCount()];
		String content = DocumentReader.readFile(path);
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		String[] wordStrings = splitter.split(content);
		for (int i = 0; i < wordStrings.length; i++) {
			if (featureVectorSpace.isFeature(wordStrings[i])) {
				int index = featureVectorSpace.getFeatureID(wordStrings[i]);
				tf[index]++;
			}
		}
	}

	public DocumentVector(FeatureVectorSpace featureVectorSpace,
			Document document, ClassManager classManager) {
		classID = classManager.getClassID(document.getClassNameString());
		tf = new double[featureVectorSpace.getFeatureCount()];
		tf_idf = new double[featureVectorSpace.getFeatureCount()];
		docPosition = document.getDocPosition();
		
		//word2vec model
		VectorModel vm = null;
		if( word2vec == true ) {
			vm = VectorModel.loadFromFile(modelFilePath);
		} 

		//lda model
		Corpus corpus = null;
		LdaGibbsSampler ldaGibbsSampler = null;
		if( lda == true ) {
			try {
				ObjectInputStream oin = new ObjectInputStream((new FileInputStream(ldaCorpusPath)));
				corpus = (Corpus)oin.readObject();
				oin.close();

				oin = new ObjectInputStream(new FileInputStream(ldaGibbsPath));
				ldaGibbsSampler = (LdaGibbsSampler)oin.readObject();
				oin.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}


		for (int i = 0; i < featureVectorSpace.getFeatureCount(); i++) {
			String name = featureVectorSpace.getFeatureName(i);
			if (document.containsKey(name)) {
				tf[i] = document.get(name);
				tf_idf[i] = tf[i] * Math.log( (double)classManager.getTotalFileCount() / featureVectorSpace.getFileCount(name) );
			
				if( word2vec == true )
					add(vm.getWordVector(name));
				if( lda == true ) {
					 double[][] phi = ldaGibbsSampler.getPhi();
					 add(LdaUtil.getVector(phi, corpus.getVocabulary(), name));
				}
			} else {
				tf[i] = 0;
				tf_idf[i] = 0;
			}

		}
	}

	private void add(float[] wordVector) {
		for (int i = 0; i < wordVector.length; i++) {
			wordVec[i] += wordVector[i];
		}
	}
	
	private void add(double[] wordVector) {
		for (int i = 0; i < wordVector.length; i++) {
			wordVec[i] += wordVector[i];
		}
	}

	public String getDocPosition() {
		return docPosition;
	}

	public double[] getFeatureCount() {
		return tf;
	}
	
	public double[] getTFIDF() {
		return tf_idf;
	}

	public int getClassID() {
		return classID;
	}

	public String toString() {
		String ID = String.valueOf(classID);

		for (int i = 0; i < tf.length; i++)
			if(tf[i] != 0)
				ID = ID + " " + (i + 1) + ":" + tf_idf[i];
		
		if(word2vec || lda) 
			for (int i = tf.length; i < tf.length+wordVec.length; i++) {
				if(wordVec[i - tf.length] != 0)
					ID = ID + " " + (i + 1) + ":" + wordVec[i - tf.length];
			}
		return ID;
	}
}
