package com.finalproject.classifier;

import com.finalproject.classmanage.ClassManager;
import com.finalproject.featureselect.FeatureVectorSpace;
import com.finalproject.tools.ChineseSplitter;
import com.finalproject.tools.DocumentReader;
/**
 * 由特征向量空间模型表示的文档
 * @author Administrator
 *
 */
public class DocumentVector {
	
	int classID;	
	double[] featureCount;
	
	public DocumentVector(FeatureVectorSpace featureVectorSpace,int classID,String path){
		this.classID=classID;
		featureCount=new double[featureVectorSpace.getFeatureCount()];
		String content=DocumentReader.readFile(path);
		ChineseSplitter splitter=ChineseSplitter.getInstance();
		String[]wordStrings=splitter.split(content);
		for(int i=0;i<wordStrings.length;i++){
			if(featureVectorSpace.isFeature(wordStrings[i])){
				int index=featureVectorSpace.getFeatureID(wordStrings[i]);
				featureCount[index]++;
			}
		}
	}

	public DocumentVector(FeatureVectorSpace featureVectorSpace,
			Document document,ClassManager classManager) {
		classID=classManager.getClassID(document.getClassNameString());
		featureCount=new double[featureVectorSpace.getFeatureCount()];
		for(int i=0;i<featureVectorSpace.getFeatureCount();i++){
			String name=featureVectorSpace.getFeatureName(i);
			int  count=0;
			if(document.containsKey(name)){				
				featureCount[i]=document.get(name);			    
			}else{
				featureCount[i]=0;		
			}
			
			//TODO   看看为什么注释起来这三行
			double totalFileCount=classManager.getTotalFileCount();
			double totalFileCountOfFeature=featureVectorSpace.getFileCount(featureVectorSpace.getFeatureName(i));
			featureCount[i]=count*Math.log10(totalFileCount/totalFileCountOfFeature+0.01);
		}
	}

	public double[] getFeatureCount() {
		return featureCount;
	}
	
	public int getClassID(){
		return classID;
	}
	
	public String toString(){
		String result=String.valueOf(classID);
		
		for(int i=0;i<featureCount.length;i++)
			result=result+" "+i+":"+featureCount[i];
		return result;
	}
}
