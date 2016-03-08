package com.word2vec.vec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;

import com.word2vec.util.Tokenizer;

public class TestWord2Vec {

    public static void readByJava(String textFilePath, String modelFilePath){

        Word2Vec wv = new Word2Vec.Factory()
                .setMethod(Word2Vec.Method.Skip_Gram)
                .setNumOfThread(1).build();

        try {
        	BufferedReader br =
                    new BufferedReader(new InputStreamReader(new FileInputStream(textFilePath), "GBK"));
            for (String line = br.readLine(); line != null;
                    line = br.readLine()){
                wv.readTokens(new Tokenizer(line, " "));

            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        wv.training();
        wv.saveModel(new File(modelFilePath));
    }

    public static void testVector(String word, String modelFilePath){

        VectorModel vm = VectorModel.loadFromFile(modelFilePath);
        Set<VectorModel.WordScore> result1 = Collections.emptySet();

        result1 = vm.similar(word);
        for (VectorModel.WordScore we : result1){
            System.out.println(we.name + " :\t" + we.score);
        }
    }
    
    public static void getVector(String modelFilePath) {
    	VectorModel vm = VectorModel.loadFromFile(modelFilePath);
    	float[] vec = vm.getWordVector("材料");
    	for (int i = 0; i < vec.length; i++) {
			System.out.print(vec[i] + "\t");
		}
    }
    
    public static void dist(String modelFilePath) {
    	VectorModel vm = VectorModel.loadFromFile(modelFilePath);
    	System.out.println(vm.dist("史料", "材料"));    	
    }

    public static void main(String[] args){

//        String textFilePath = "D:\\temp\\fudan_subset_subset\\file.w2v";
        String modelFilePath = "D:/temp/w2v_corpus.nn";
//        readByJava(textFilePath, modelFilePath);
        dist(modelFilePath);
//        getVector(modelFilePath);
//        testVector("材料", modelFilePath);
    }

}
