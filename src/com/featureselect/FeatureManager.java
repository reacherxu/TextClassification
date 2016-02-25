package com.featureselect;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.classmanage.ClassManager;


/**
 * 特征管理器，包含训练文本集中所有出现的词汇
 * @author Administrator
 * 
 */
public class FeatureManager {

	Map<String, Feature> featureMap;
	ClassManager classManager;

	public FeatureManager(ClassManager classManager) {
		this.classManager = classManager;
		featureMap = new HashMap<String, Feature>();
	}

	public void addFeature(String feature) {
		Feature tempFeature = new Feature(feature, classManager);
		featureMap.put(feature, tempFeature);
	}

	public void addFeature(Feature feature) {
		String name = feature.getName();
		featureMap.put(name, feature);
	}

	public void removeFeature(String feature) {
		featureMap.remove(feature);
	}

	public void increaseFeatureCount(String feature, int classID) {
		Feature tempFeature = featureMap.get(feature);
		tempFeature.increaseFileCount(feature, classID);
	}

	public boolean hasFeature(String feature) {
		return featureMap.containsKey(feature);
	}

	public int getFeatureCount() {
		return featureMap.size();
	}

	public Feature getFeature(String feature) {
		return featureMap.get(feature);
	}

	public String[] getFeatures() {
		Set<String> keySet = featureMap.keySet();
		String[] features = new String[keySet.size()];
		keySet.toArray(features);
		return features;
	}

	public int getTotalFileCountOfFeature(String feature) {
		Feature tempFeature = featureMap.get(feature);
		return tempFeature.getTotalFileCount();
	}

	public int getClassFileCountOfFeature(int classID, String feature) {
		Feature tempFeature = featureMap.get(feature);
		return tempFeature.getClassFileCount(classID);
	}

	public FeatureVectorSpace getFeatureVectorSpace() {
		Set<String> keySet = featureMap.keySet();
		Vector<String> featureVector = new Vector<String>(keySet);
		FeatureVectorSpace space = new FeatureVectorSpace(featureVector, this);
		return space;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
