package com.hankcs.lda;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LabelUtil {
	public static Map<String,Integer> loadLabel() {
		Map<String,Integer> labelMap = new HashMap<String,Integer>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("data/label"));
			
			int i = 1;
			while(reader.ready()) {
				labelMap.put(reader.readLine(), i ++);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return labelMap;
	}
}
