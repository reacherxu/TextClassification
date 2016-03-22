/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/1/29 19:07</create-date>
 *
 * <copyright file="LdaUtil.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.lda;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.classmanage.ClassManager;

/**
 * @author hankcs
 */
public class LdaUtil
{
    /**
     * To translate a LDA matrix to readable result
     * @param phi the LDA model
     * @param vocabulary
     * @param limit limit of max words in a topic
     * @return a map array
     */
    @SuppressWarnings("unchecked")
	public static Map<String, Double>[] translate(double[][] phi, Vocabulary vocabulary, int limit)
    {
        limit = Math.min(limit, phi[0].length);
        Map<String, Double>[] result = new Map[phi.length];
        for (int k = 0; k < phi.length; k++)
        {
            Map<Double, String> rankMap = new TreeMap<Double, String>(Collections.reverseOrder());
            for (int i = 0; i < phi[k].length; i++)
            {
                rankMap.put(phi[k][i], vocabulary.getWord(i));
            }
            Iterator<Map.Entry<Double, String>> iterator = rankMap.entrySet().iterator();
            result[k] = new LinkedHashMap<String, Double>();
            for (int i = 0; i < limit; ++i)
            {
                Map.Entry<Double, String> entry = iterator.next();
                result[k].put(entry.getValue(), entry.getKey());
            }
        }
        return result;
    }

    public static Map<String, Double> translate(double[] tp, double[][] phi, Vocabulary vocabulary, int limit)
    {
        Map<String, Double>[] topicMapArray = translate(phi, vocabulary, limit);
        double p = -1.0;
        int t = -1;
        for (int k = 0; k < tp.length; k++)
        {
            if (tp[k] > p)
            {
                p = tp[k];
                t = k;
            }
        }
        return topicMapArray[t];
    }

    /**
     * To print the result in a well formatted form
     * @param result
     */
    public static void explain(Map<String, Double>[] result)
    {
        int i = 0;
        for (Map<String, Double> topicMap : result)
        {
            System.out.printf("topic %d :\n", i++);
            explain(topicMap);
            System.out.println();
        }
    }

    public static void explain(Map<String, Double> topicMap)
    {
        for (Map.Entry<String, Double> entry : topicMap.entrySet())
        {
            System.out.println(entry);
        }
    }
    
    public static void doc_explain(double[] tp, String fileName, 
    		BufferedWriter writer, ClassManager classManager)
    {
    	try {
    		writer.write(getLabel(fileName,classManager) + " ");
    		for (int j = 0; j < tp.length; j++) {
    			writer.write( (j+1) + ":" + tp[j] + " ");
			}

    		writer.newLine();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

	private static Integer getLabel(String fileName, ClassManager classManager)
	{
		String[] classNames = classManager.getClassNames();
		for (int i = 0; i < classNames.length; i++) {
			if(fileName.startsWith(classNames[i]))
				return i;
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private static Integer getLabel(String fileName)
	{
		Map<String,Integer> labelMap =LabelUtil.loadLabel();
		for(Map.Entry<String,Integer> entry : labelMap.entrySet())
		{
			if(fileName.startsWith(entry.getKey()))
				return entry.getValue();
		}
		return null;
	}
	
	/**
	 * 将主题-文档矩阵输出到文件
	 * @param theta
	 * @param fileNames
	 * @param writer
	 * @param classManager
	 */
	public static void write(double[][] theta, List<String> fileNames, 
			BufferedWriter writer, ClassManager classManager) 
	{
		try {
			for (int i = 0; i < theta.length; i++) {

				writer.write( getLabel(fileNames.get(i), classManager) + " ");

				for (int j = 0; j < theta[0].length; j++) {
					writer.write( (j+1) + ":" + theta[i][j] + " ");
				}
				
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得word的词向量
	 * @param phi
	 * @param vocabulary
	 * @param word
	 * @return
	 */
	public static double[] getVector(double[][] phi, Vocabulary vocabulary, String word) 
	{
		if( !vocabulary.word2idMap.containsKey(word)) {
			return  new double[phi.length];
		}
		
		int id = vocabulary.getId(word);
		double[] vector = new double[phi.length];
		for (int i = 0; i < phi.length; i++) {
			vector[i] = phi[i][id];
		}
		return vector;
	}
}
