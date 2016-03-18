package com.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.hankcs.lda.Corpus;

public class Test {
	public static void main(String[] args) {
		String ldaCorpusPath = "D:/lda/corpus_10.lda";
		ObjectInputStream oin;
		try {
			oin = new ObjectInputStream((new FileInputStream(ldaCorpusPath)));
			Corpus corpus = (Corpus)oin.readObject();
			oin.close();
			corpus.getVocabulary();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
