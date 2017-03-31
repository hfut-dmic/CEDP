package cn.edu.hfut.dmic.wi.wrapper;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import weka.clusterers.AbstractClusterer;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Cobweb;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.SimpleKMeans;
import weka.clusterers.SpectralClusterer;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import cn.edu.hfut.dmic.util.ArrayListUtil;
import cn.edu.hfut.dmic.util.DateUtil;
import cn.edu.hfut.dmic.util.FileUtil;
import cn.edu.hfut.dmic.util.HtmlUtil;
import cn.edu.hfut.dmic.util.MathUtil;
import cn.edu.hfut.dmic.util.Pair;
import cn.edu.hfut.dmic.util.PairTwo;



public class CEDP {


	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setSmoothParam(int param){
		this.paramDistance = param;
	}
	

	private void getPathAndTxtByPreorder(String sourceFileName) {
		// Parsing a source HTML file to a DOM tree.
		NodeList nodeList = new NodeList();
		String fileContentAsaString = new String();
		Parser parser = new Parser();
		fileContentAsaString = FileUtil.getFileContentAsaString(sourceFileName);
		fileContentAsaString = HtmlUtil.preCleaning(fileContentAsaString);
		try {
			try {
				// Whether to use the contents instead of a file name to judge?
				// new ByteArrayInputStream(str.getBytes());
				// TODO I want to change getPageEncode(htmlfilename) to
				// getPageEncode(fileContentAsaString)
				parser.setEncoding(FileUtil.getPageEncode(sourceFileName));
				parser = new Parser(fileContentAsaString);

				for (NodeIterator iterator = parser.elements(); iterator
						.hasMoreNodes();) {
					Node node = iterator.nextNode();
					nodeList.add(node);
				}
			} catch (EncodingChangeException ece) {
				nodeList.removeAll();
				parser.reset();
				for (NodeIterator iterator = parser.elements(); iterator
						.hasMoreNodes();)
					nodeList.add(iterator.nextNode());
			}
		} catch (ParserException pe) {
			pe.printStackTrace();
			System.out.println(sourceFileName);
		}

		// Visit each node in the nodeList for get the path and text information
		// of the node
		
		m_nodelist = nodeList;
		if (m_nodelist != null && m_nodelist.size() > 0) {
			m_root = new TagNode();
			m_root.setTagName("#root");
			m_root.setChildren(m_nodelist);
			for (int i = 0; i < m_nodelist.size(); i++) {
				m_nodelist.elementAt(i).setParent(m_root);
			}
		}

		for (int i = 0; i < nodeList.size(); i++) {
			visitDomTreeByPreorder(nodeList.elementAt(i));
		}
	}

	/**
	 * 
	 * @param node
	 */

	private void visitDomTreeByPreorder(Node node) {
		if (node == null)
			return;

		if (node instanceof TextNode) {
			String strText = ((TextNode) node).getText();
			strText = strText.trim();
			String nonPointText = strText.replaceAll("\\p{P}", "");
			nonPointText = nonPointText.replaceAll("\\|", "");
			if (nonPointText.length() == 0) {
				strText = "";
			}
			if (strText.length() > 0) {
				Node pNode = node.getParent();
				String nodePath = "Empty Path?!";
				if ((pNode != null) && (pNode instanceof TagNode))
					nodePath = getNodePath((TagNode) pNode);
				textNodes.add((TextNode) node);
				textNodePaths.add(nodePath);
				textNodeTxts.add(strText);
				txtNodeCount++;
				int textPuctNum = strText.length() - nonPointText.length();
				
				if (!nodePathMap.containsKey(nodePath)) {					
					nodePathMap.put(nodePath, new Pair(1, strText.length(),textPuctNum));
					textPaths.add(nodePath);
					textPathCount++;
					
					/***********************/
					ArrayList<Integer> arrayText = new ArrayList<Integer>();
					ArrayList<Integer> arrayPuct = new ArrayList<Integer>();
					arrayText.add(strText.length());
					arrayPuct.add(textPuctNum);
					pathMap.put(nodePath, new Pair(1, arrayText, arrayPuct));
					/***********************/
					
				} else {
					Pair value = nodePathMap.get(nodePath);
					int val1 = (Integer) value.getValue1() + 1;
					int val2 = (Integer) value.getValue2() + strText.length();
					int val3 = (Integer) value.getValue3() + strText.length()
							- nonPointText.length();
					value.setValue1(val1);
					value.setValue2(val2);
					value.setValue3(val3);
					
					/***********************/
					Pair v = pathMap.get(nodePath);
					int v1 = (Integer) v.getValue1() + 1;
					ArrayList<Integer> v2 = (ArrayList<Integer>) v.getValue2();
					v2.add(strText.length());
					ArrayList<Integer> v3 = (ArrayList<Integer>) v
							.getValue3();
					v3.add(textPuctNum);
					v.setValue1(v1);
					v.setValue2(v2);
					v.setValue3(v3);
					/***********************/
				}

				if (debug) {
					System.out.println(nodePath + ":");
					System.out.println(strText);
					System.out.println(strText.length());
					System.out
							.println(strText.length() - nonPointText.length());
					System.out.println();
				}
			}
			return;
		}

		if (!(node instanceof TagNode)) {
			return;
		}

		TagNode tagNode = (TagNode) node;
		

		NodeList nodeList = tagNode.getChildren();
		if (nodeList == null) {
			return;
		}
		for (int i = 0; i < nodeList.size(); i++) {
			visitDomTreeByPreorder(nodeList.elementAt(i));
		}
	}

	private String getNodePath(TagNode tagNode) {
		String tagPath = tagNode.getTagName();
		if (!(tagNode instanceof TagNode))
			return tagPath;
		Node pNode = tagNode.getParent();
		while ((pNode != null) && (pNode instanceof TagNode)) {
			tagPath = ((TagNode) pNode).getTagName() + "." + tagPath;
			pNode = pNode.getParent();
		}
		return tagPath;
	}

	private void calculateMap() {
		for (Map.Entry<String, Pair<Integer, Integer, Integer>> entry : nodePathMap
				.entrySet()) {
			String path = entry.getKey();
			Pair<Integer, Integer, Integer> value = entry.getValue();
			double textLength = ((Integer) value.getValue2()).doubleValue();
			double eTextLength = ((Integer) value.getValue3()).doubleValue();
			double textRatio = textLength / value.getValue1();
			double eTextRatio = eTextLength / value.getValue1();

			textPathLength.put(path, textLength);
			eTextPathLength.put(path, eTextLength);
			textPathRatio.put(path, textRatio);
			eTextPathRatio.put(path, eTextRatio);

			int numLevel = path.split("\\.").length;
			double textLevelRatio = textLength / numLevel;
			double eTextLevelRatio = eTextLength / numLevel;
			textPathLevelRatio.put(path, textLevelRatio);
			eTextPathLevelRatio.put(path, eTextLevelRatio);
		}

	}

	private void calculateArray() {
		int length = textPathCount;

		array_tpl = new double[length];
		array_tpr = new double[length];
		array_etpl = new double[length];
		array_etpr = new double[length];
		array_tplr = new double[length];
		array_etplr = new double[length];

		for (int i = 0; i < length; i++) {
			String key = textPaths.get(i);
			array_tpl[i] = textPathLength.get(key);
			array_tpr[i] = textPathRatio.get(key);
			array_etpl[i] = eTextPathLength.get(key);
			array_etpr[i] = eTextPathRatio.get(key);
			array_tplr[i] = textPathLevelRatio.get(key);
			array_etplr[i] = eTextPathLevelRatio.get(key);

		}
	}
	
	

	private void spectral_cluster(){
		
		FastVector fv = new FastVector();
		
		for(int i = 0; i<textPathCount;i++){
			fv.addElement(new Attribute(String.valueOf(i)));
		}
		
	    
		Instances inst = new Instances("Tag Path Feature", fv, 6);
		
		Instance ins_tpl = new Instance(fv.size());
		
		for(int i = 0; i<textPathCount;i++){
			ins_tpl.setValue(i, array_tpl[i]);
		}
		
		Instance ins_tpr = new Instance(fv.size());
		for(int i = 0; i<textPathCount;i++){
			ins_tpr.setValue(i, array_tpr[i]);
		}
		
		
		Instance ins_tplr = new Instance(fv.size());
		for(int i = 0; i<textPathCount;i++){
			ins_tplr.setValue(i, array_tplr[i]);
		}		
		
		Instance ins_etpl = new Instance(fv.size());
		for(int i = 0; i<textPathCount;i++){
			ins_etpl.setValue(i, array_etpl[i]);
		}
				
		Instance ins_etpr = new Instance(fv.size());
		for(int i = 0; i<textPathCount;i++){
			ins_etpr.setValue(i, array_etpr[i]);
		}
			
		Instance ins_etplr = new Instance(fv.size());
		for(int i = 0; i<textPathCount;i++){
			ins_etplr.setValue(i, array_etplr[i]);
		}
	
		
		inst.add(ins_tpl);
		inst.add(ins_tpr);
		inst.add(ins_tplr);	
		inst.add(ins_etpl);
		inst.add(ins_etpr);
		inst.add(ins_etplr);
        

		SpectralClusterer scluster = new SpectralClusterer();
		
		scluster.buildClusterer(inst);
		
		int[] ass = new int[6];
		for(int i = 0;i<ass.length;i++){
			ass[i] = scluster.clusterInstance(inst.instance(i));
		}
	
		
		for(int i = 0; i<ass.length;i++){
			System.out.println(ass[i]);
		}
	
		
		int clusterNum = scluster.numberOfClusters();
		getDecisiveValues(clusterNum,ass,inst);
	}
	
	
	public void getDecisiveValues(int clusterNumber, int[] ass, Instances inst){
	    
		decisiveValues = new double[textPathCount];
	
		for(int i = 0 ; i < decisiveValues.length;i++){
		
			
			String path = textPaths.get(i);
			Pair<Integer, ArrayList<Integer>, ArrayList<Integer>> val = pathMap.get(path);
			ArrayList<Integer> val2 = val.getValue2();
			ArrayList<Integer> val3 = val.getValue3();
			double std2 = MathUtil.ComputerStandardDeviationInt(val2);
			double std3 = MathUtil.ComputerStandardDeviationInt(val3);
			decisiveValues[i] = std2*std3;

		}
		
	/*
		for(int i = 0 ; i < decisiveValues.length;i++){
			decisiveValues[i]=1;
		}
	*/	
		
	    FastVector fv = new FastVector();
		for(int i = 0; i<textPathCount;i++){
			fv.addElement(new Attribute(String.valueOf(i)));
		}

		Map<Integer,Instances> classMap = new TreeMap<Integer,Instances>();
		
		for(int i = 0;i<clusterNumber;i++){
			int eleNum = getElementNumber(i,ass);
			classMap.put(i, new Instances("Features", fv , eleNum));
		}
		
		for(int i = 0; i<ass.length;i++){
		    int cl = ass[i];
		    Instances value = classMap.get(cl);
		    value.add(inst.instance(i));
		}
		
		for(int i = 0; i<clusterNumber;i++){
			Instances value = classMap.get(i);
			Instance ele = getCenterElement(value);
			decisiveValues = merge(decisiveValues,ele);
		}	
	}
	
	
	private double[] merge(double[] v, Instance c){
		for(int i = 0; i<v.length;i++){
			v[i] = v[i] * c.value(i);
		}
		return v;
	}
	
	
	
	
	private Instance getCenterElement(Instances inst){
		if(inst.numInstances() == 1){
			return inst.instance(0);
		}
		else{
			/*
				double[] distance = new double[inst.numInstances()];
				for(int i = 0; i<inst.numInstances();i++){
					distance[i] = getDistance(inst.instance(i),inst);
				}
				int index = MathUtil.getMinValueNumber(distance);
				return inst.instance(index);
			*/
				int index = (int)(Math.random()*inst.numInstances());
				return inst.instance(index);
						
		}
		
	}
	
	
	private double getDistance(Instance c, Instances inst){
		double distance = 0;

		for(int i = 0; i<inst.numInstances();i++){
			Instance b = inst.instance(i);
			distance +=getDistance(c,b);
		}
		return distance/inst.numInstances();
	}
	
	
	private double getDistance(Instance first, Instance second){
		double distance = 0;
		for(int i = 0; i < first.numValues();i++){
			distance += (first.value(i)-second.value(i))*(first.value(i)-second.value(i));
		}
		return distance;
	}
	
	
	
	private int getElementNumber(int index, int[] array){
		int number = 0;
		for(int i = 0; i<array.length;i++){
			if(array[i]==index)
				number++;
		}
		return number;
	}
	
	
	// Gsmooth
   private double[] nodeLevelGauss_Smooth(double [] nodeETTPR) {
		
		double[] kArray = calculateKArray();
		double [] smoothNodeETTPR = new double[txtNodeCount];
		if (nodeETTPR.length > 2 * windowSize) {
			for (int i = 0; i < windowSize; i++) {
				smoothNodeETTPR[i] = nodeETTPR[i];
			}

			for (int i = windowSize; i < textNodePaths.size() - windowSize; i++) {
				double smoothVal = 0;
				String key = textNodePaths.get(i);
				for (int j = 0; j < kArray.length; j++) {
					int p = j - windowSize;
					String path = textNodePaths.get(i - p);
					double val = nodeETTPR[i - p];
					double wEdit = calculateEditWeight(key, path);
					double k = kArray[j];
					smoothVal += wEdit * k * val;
				}
				smoothNodeETTPR[i] = smoothVal;
			}

			for (int i = textNodePaths.size() - windowSize; i < textNodePaths
					.size(); i++) {
				smoothNodeETTPR[i] = nodeETTPR[i];
			}
		}else{
			    smoothNodeETTPR = nodeETTPR;
		}
		return smoothNodeETTPR;
	}

	private double calculateEditWeight(String path1, String path2) {
		double w = 0;
		String[] tags1 = path1.split("\\.");
		String[] tags2 = path2.split("\\.");
		ArrayList<String> ArrayList1 = new ArrayList<String>();
		ArrayList<String> ArrayList2 = new ArrayList<String>();
		
		for(int i = 0; i<tags1.length;i++){
			ArrayList1.add(tags1[i]);
		}
		for(int i = 0; i<tags2.length;i++){
			ArrayList2.add(tags2[i]);
		}
		
		if (path1.equals(path2)) {
			w = 1;
		} else {
			w = (double) ArrayListUtil.getLevenshteinDistance(ArrayList1, ArrayList2);
			w = Math.pow(w, paramDistance);
			w = 1 / w;
		}
		return w;
	}

	private double[] calculateKArray() {
		double[] array = new double[2 * windowSize + 1];
		int index = 0;
		for (int i = -windowSize; i <= windowSize; i++) {
			double f = i * i /(double)( 2 * windowSize * windowSize);
			f = -f;
			array[index] = Math.exp(f);
			index++;
		}
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		for (int i = 0; i < array.length; i++) {
		//	System.out.println(array[i]);
			array[i] = array[i] / sum;
		//	System.out.println(array[i]);
		}
		return array;
	}
	
	
	
	
/*	
	//std
    private double calculateStdByStd(double[] values,double[] pathValues){
		
		double fangcha = MathUtil.ComputerStandardDeviation(values);
		
		double ave=0.0;
		
		double[] m_pathValues = new double[pathValues.length];
		
		for(int i = 0; i < pathValues.length; i++)
			m_pathValues[i] = pathValues[i];
		Arrays.sort(m_pathValues);

		//候选阈值
		ArrayList<Double> value = new ArrayList<Double>();
		for(double i=0.5;i<=1.5;i=i+0.1){
			value.add(i*fangcha);
		}
		for(int i=0;i<m_pathValues.length;i++){
//			if ((0.8*fangcha <= m_pathValues[i])&&(m_pathValues[i] <= 1.5*fangcha)) {
//				value.add(m_pathValues[i]-0.5);
//				value.add(m_pathValues[i]+0.5);
//
//			}
//			value.add(m_pathValues[i]);
				
			ave+=m_pathValues[i];;
		}
		ave=ave/pathValues.length;
		//System.out.println(ave);

		//阈值
		double threashold=0.1;

		//计算OTSU的值
		double tvalue=0.0;

		//尝试每个smoothetpr的值
		for(double th:value){
			double noise = 0,content=0;
			double noiseave=0.0,contentave=0.0;

			for(int j=0;j<pathValues.length;j++){
				//大于阈值则为文本
				if(pathValues[j]>=th){
					content++;
					contentave+=pathValues[j];
				}
				else{
					noise++;
					noiseave+=pathValues[j];
				}
			}
			if(noise==0)
				return threashold;
			if(content==0)
				break;
			//t=MAX[w0(t)*(u0(t)-u)2+w1(t)*(u1(t)-u)2]	
			noiseave=noiseave/noise;
			contentave=contentave/content;				
			noise=noise/pathValues.length;
			content=content/pathValues.length;	

			double t= noise*Math.pow((noiseave-ave),2)+content*Math.pow((contentave-ave), 2);
			//double t= Math.abs((noiseave-ave)-(contentave-ave));
		//	System.out.println("fangcha:"+t);
			if(t>tvalue){
				tvalue=t;
				threashold=th;
			}
			//if(t<tvalue)
			//	break;			
		}
		//System.out.println("-*****************************************");
		//System.out.println("yuzhi:"+threashold);
		return threashold;	
	}
*/
	
    private double calculateStdByStd(double[] values, double[] pathValues){
		
		double fangcha = MathUtil.ComputerStandardDeviation(values);
		
		double ut=0.0;
		
		double[] m_pathValues = new double[pathValues.length];
		
		for(int i = 0; i < pathValues.length; i++)
			m_pathValues[i] = pathValues[i];
		Arrays.sort(m_pathValues);
		
		double[] pr = new double[pathValues.length];
		double sum = MathUtil.ComputerSum(m_pathValues);
		for (int i = 0; i < pathValues.length; i++) {
			pr[i] = m_pathValues[i] / sum;
		}

		//候选阈值
		ArrayList<Double> value = new ArrayList<Double>();
		for(double i=0.5;i<=1.5;i=i+0.1){
			value.add(i*fangcha);
		}
		for(int i=0;i<m_pathValues.length;i++){
//			if ((0.8*fangcha <= m_pathValues[i])&&(m_pathValues[i] <= 1.5*fangcha)) {
//				value.add(m_pathValues[i]-0.5);
//				value.add(m_pathValues[i]+0.5);
//
//			}
//			value.add(m_pathValues[i]);
				
			ut += m_pathValues[i] * pr[i];
		}
		//System.out.println(ave);

		//阈值
		double threashold=0.1;

		//计算OTSU的值
		double tvalue=0.0;

		//尝试每个smoothetpr的值
		for(double th:value){
			double w0 = 0,w1=0;
			double u0 = 0.0, u1=0.0;
			
			for(int j=0; j < pathValues.length; j++){
				//大于阈值则为文本
				if(m_pathValues[j] >= th){
					w1 += pr[j];
				}
				else{
					w0 += pr[j];
				}
			}
			
			for(int j=0; j < pathValues.length; j++){
				//大于阈值则为文本
				if(m_pathValues[j] >= th){
					u1 += m_pathValues[j] * pr[j] / w1;
				}
				else{
					u0 += m_pathValues[j] * pr[j] / w1;
				}
			}
			
			if(w0==0)
				return threashold;
			if(w1==0)
				break;
			
			//t=MAX[w0(t)*(u0(t)-u)2+w1(t)*(u1(t)-u)2]	

			double t= w0*Math.pow((u0-ut),2)+w1*Math.pow((u1-ut), 2);
			//double t= Math.abs((noiseave-ave)-(contentave-ave));
		//	System.out.println("fangcha:"+t);
			if(t>tvalue){
				tvalue=t;
				threashold=th;
			}
			//if(t<tvalue)
			//	break;			
		}
		//System.out.println("-*****************************************");
		//System.out.println("yuzhi:"+threashold);
		return threashold;	
	}
	

	
	//阈值的自动获取计算
	private double calculateStdByMean(double[] values){
		double std = 0;
		double newStd = 0;
		double maxValue = MathUtil.getMaxValue(values);
		double minValue = MathUtil.getMinValue(values);
			
		newStd = (maxValue+minValue)/2;
		
		int diedai = 0;
		
		while(Math.abs(std-newStd)>0.5){
			std = newStd;
			minValue = getMinSum(values,std);
			maxValue = getMaxSum(values,std);
			newStd = (minValue+maxValue)/2;
			diedai++;
			System.out.println("std:"+std+"   newStd:"+newStd);
		}
		return newStd;
	}
	
	private double getMinSum(double[] values, double std){
		double sum = 0;
		int num = 0;
		for(int i = 0; i< values.length;i++){
			if(values[i]<std){
				sum += values[i];
				num++;
			}
		}
		return sum/num;
	}
	
	
	
	private double getMaxSum(double[] values, double std){
		double sum = 0;
		int num = 0;
		for(int i = 0; i< values.length;i++){
			if(values[i]>=std){
				sum += values[i];
				num ++;
			}
		}
		return sum/num;
	}
	
	
	
	public String extract(){
		String content = "";
		getPathAndTxtByPreorder(sourceFileName);		
		calculateMap();
		calculateArray();
		spectral_cluster();
		
		
        double[] nodeValues = new double[txtNodeCount];
		
		for(int i = 0;  i < textNodeTxts.size();i++){
			String path = textNodePaths.get(i);
			int index = textPaths.indexOf(path);
			nodeValues[i] = decisiveValues[index];
		}		
	
//		nodeValues = nodeLevelGauss_Smooth(nodeValues);
		nodeValues = nodeLevelGauss_Smooth_DensityProb(nodeValues);
		double std =  calculateStdByStd(nodeValues,decisiveValues);
		
		for(int i = 0;  i < textNodeTxts.size();i++){
			if(nodeValues[i]>=std){
				content += textNodeTxts.get(i) + "\n";
			}
		}
		return content;
	}
	
	

	public void draw(){

		double[] nodeValues = {0.0,0.0,0.0,274077.19999999995,274077.19999999995,0.0,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,0.0,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,0.0,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,0.0,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,0.0,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,0.0,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,0.0,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,2.388486100159413E8,4.72396926646695E8,15415.7744413925,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,947382.6030119713,0.0,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,44271.29914286177,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,12418.111160720056,0.0,0.0,0.0,1670.3480760686562,1670.3480760686562,1670.3480760686562,1670.3480760686562,1670.3480760686562,1670.3480760686562,0.0,0.0,0.0,0.0,0.0,2123.6522379229013,2123.6522379229013,0.0,1.872997665859579E9,1.872997665859579E9,1.872997665859579E9,1.872997665859579E9,1.872997665859579E9,62658.958333333336,1.872997665859579E9,1.872997665859579E9,1.872997665859579E9,1.872997665859579E9,62658.958333333336,1.872997665859579E9,122579.1038574683,8072101.901836742,0.0,0.0,0.0,0.0,1511.2673069192606,1511.2673069192606,1511.2673069192606,1511.2673069192606,1511.2673069192606,1511.2673069192606,0.0,0.0,8025.733985071098,8025.733985071098,0.0,0.0,0.0,291673.83636483195,0.0,291673.83636483195,0.0,0.0,291673.83636483195,0.0,291673.83636483195,0.0,0.0,291673.83636483195,0.0,291673.83636483195,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,3928643.330164481,3928643.330164481,3928643.330164481,3928643.330164481,3928643.330164481,3928643.330164481,10065.229067387636,0.0,3928643.330164481,3928643.330164481,3928643.330164481,10065.229067387636,3928643.330164481,10065.229067387636,3928643.330164481,10065.229067387636,3928643.330164481,10065.229067387636,366974.5058120353,366974.5058120353,0.0,341360.97680110816,341360.97680110816,0.0,0.0,341360.97680110816,0.0,341360.97680110816,341360.97680110816,341360.97680110816,341360.97680110816,341360.97680110816,341360.97680110816,341360.97680110816,0.0,0.0,341360.97680110816,0.0,341360.97680110816,0.0,0.0,8025.733985071098,0.0,0.0,0.0,0.0,13720.0,2.2164970266018696E7,13720.0,2.2164970266018696E7,13720.0,2.2164970266018696E7,13720.0,2.2164970266018696E7,13720.0,2.2164970266018696E7,0.0,2745715.3745677704,0.0,2745715.3745677704,0.0,2745715.3745677704,0.0,2745715.3745677704,0.0,2745715.3745677704,2.2164970266018696E7,13720.0,2.2164970266018696E7,13720.0,2.2164970266018696E7,13720.0,2.2164970266018696E7,13720.0,2.2164970266018696E7,13720.0,0.0,0.0,0.0,366974.5058120353,366974.5058120353,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,15415.7744413925,366974.5058120353,366974.5058120353,0.0,0.0,0.0,139709.67504179495,139709.67504179495,139709.67504179495,0.0,139709.67504179495,139709.67504179495,139709.67504179495,139709.67504179495,0.0,139709.67504179495,0.0,0.0,1.2806686751345937E8,7046.345428667466,12418.111160720056,12418.111160720056,7046.345428667466,12418.111160720056,7046.345428667466,12418.111160720056,12418.111160720056,7046.345428667466,12418.111160720056,122579.1038574683,122579.1038574683,8072101.901836742,122579.1038574683,0.0,0.0,1.2806686751345937E8,1.2806686751345937E8,8072101.901836742,0.0,494381.2869036113,1.2245593043787729E7,1.2245593043787729E7,1.2245593043787729E7,1.2245593043787729E7,1.2245593043787729E7,1.2245593043787729E7,1.2245593043787729E7,494381.2869036113,0.0,494381.2869036113,494381.2869036113,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,8072101.901836742,494381.2869036113,2123.6522379229013,0.0,2123.6522379229013,0.0,2123.6522379229013,0.0,2123.6522379229013,0.0,2123.6522379229013,0.0,2123.6522379229013,0.0,2123.6522379229013,0.0,494381.2869036113,0.0,494381.2869036113,494381.2869036113,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,5.1959658416912675E7,8072101.901836742,494381.2869036113,0.0,8072101.901836742,0.0,0.0,0.0,0.0,0.0,8025.733985071098,8025.733985071098,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,0.0,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,1415581.2204229543,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,24653.644197225538,927773.7096173512,927773.7096173512,95499.28636105763,95499.28636105763,927773.7096173512,95499.28636105763,0.0};
		nodeValues = nodeLevelGauss_Smooth(nodeValues);
		for(int i = 0; i<nodeValues.length;i++){
			System.out.print(nodeValues[i]+",");
		}
	
	}
	
	private static String strFormat(String s) {
		s = s.replaceAll("&nbsp;", "");
		s = s.replaceAll("&quot;", "\"");
		s = s.replaceAll("&lt;", "<");
		s = s.replaceAll("&nbsp;", "");
		s = s.replaceAll("&quot;", "\"");
		s = s.replaceAll("&lt;", "<");
		s = s.replaceAll("&gt;", ">");
		s = s.replaceAll("&rdquo;", "”");
		s = s.replaceAll("&ldquo;", "“");
		s = s.replaceAll("&mdash;", "——");
		return s;
	}

	public static void extract(String sourceDir,
			String targetDir) throws FileNotFoundException {
		File fileSourceDir = new File(sourceDir);
		File fileTargetDir = new File(targetDir);
		if (!fileTargetDir.exists())
			fileTargetDir.mkdirs();
		String[] sourceFilenames = fileSourceDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if ((name.toLowerCase().endsWith(".html"))
						|| (name.toLowerCase().endsWith(".htm"))) {
					return true;
				} else {
					return false;
				}
			}
		});

		for (int i = 0; i < sourceFilenames.length; i++) {
			CEDP curCEDPWrapper = new CEDP();
			String curSourceFileName = sourceDir + "\\" + sourceFilenames[i];
			curCEDPWrapper.setSourceFileName(curSourceFileName);
			String content = "";
			content = curCEDPWrapper.extract();
			content = strFormat(content);
			String s = sourceFilenames[i];
			int j = s.lastIndexOf(".");
			if (j >= 0)
				s = s.substring(0, j);
			PrintWriter out = new PrintWriter(targetDir + "\\" + s + ".txt");
			out.println(content);
			out.close();
		}
	}

	private double[] nodeLevelGauss_Smooth_DensityProb(double[] nodeETTPR) {
		
		// CEDP-NLTD
		TextDensity.computeDensitys(m_nodelist, m_root);
		double[] arr_node_density_probability = TextNodeDensity.get_node_density_probability(textNodes, CEDP_Global.m_text_density);

		/*
		// CEDP-TD
		TextDensity.computeDensitys(m_nodelist, m_root);
		double[] arr_node_density_probability = TextNodeDensity.get_node_density_probability(textNodes, CEDP_Global.m_text_density);
		*/
		
		/*
		// CEDP-CTD
		TextDensity.computeDensitys_CETD(m_nodelist, m_root);
		double[] arr_node_density_probability = TextNodeDensity.get_node_density_probability(textNodes, CEDP_Global.m_text_density);
		*/
		
		// CEDP-DSum
		/*
		TextDensity.computeDensitys_CETD(m_nodelist, m_root);
		double[] arr_node_density_probability = TextNodeDensity.get_node_density_probability(textNodes, CEDP_Global.m_density_sum);
		*/
		
		// CEDP-DSum
		/*
		TextDensity.computeDensitys_CETD(m_nodelist, m_root);
		double[] arr_node_density_probability = TextNodeDensity.get_node_density_probability(textNodes, CEDP_Global.m_density_sum);
		*/
		
		double[] kArray = calculateKArray();
		double[] smoothNodeETTPR = new double[txtNodeCount];
		if (nodeETTPR.length > 2 * windowSize) {
			for (int i = 0; i < windowSize; i++) {
//				smoothNodeETTPR[i] = nodeETTPR[i];
				double m = arr_node_density_probability[i];
				smoothNodeETTPR[i] = nodeETTPR[i] * m;
			}

			for (int i = windowSize; i < textNodePaths.size() - windowSize; i++) {
				double smoothVal = 0;
				String key = textNodePaths.get(i);
				for (int j = 0; j < kArray.length; j++) {
					int p = j - windowSize;
					String path = textNodePaths.get(i - p);
//					double val = nodeETTPR[i - p];
					double m = arr_node_density_probability[i - p];
					double val = nodeETTPR[i - p] * m;
					double wEdit = calculateEditWeight(key, path);
					double k = kArray[j];
					smoothVal += wEdit * k * val;
				}
				smoothNodeETTPR[i] = smoothVal;
			}

			for (int i = textNodePaths.size() - windowSize; i < textNodePaths.size(); i++) {
//				smoothNodeETTPR[i] = nodeETTPR[i];
				double m = arr_node_density_probability[i];
				smoothNodeETTPR[i] = nodeETTPR[i] * m;
			}
		} else {
//			smoothNodeETTPR = nodeETTPR;
			for (int i = 0; i < nodeETTPR.length; i++) {
				double m = arr_node_density_probability[i];
				smoothNodeETTPR[i] = nodeETTPR[i] * m;
			}
		}
		return smoothNodeETTPR;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();

		String subDir = "20170331-CEDP-CTD";
		String sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\CleanEval-en";
		String targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\CleanEval-en\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\CleanEval-zh";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\CleanEval-zh\\";
		CEDP.extract(sourceDir, targetDir);
		
		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\nypost";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\nypost\\";
		CEDP.extract(sourceDir, targetDir);
		
		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\freep";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\freep\\";
		CEDP.extract(sourceDir, targetDir);
		
		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\suntimes";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\suntimes\\";
		CEDP.extract(sourceDir, targetDir);
		
		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\Techweb";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\Techweb\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\Tribune";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\Tribune\\";
		CEDP.extract(sourceDir, targetDir);
		
		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\nytimes";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\nytimes\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\BBC";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\BBC\\";
		CEDP.extract(sourceDir, targetDir);
		
		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\reuters";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\reuters\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\Yahoo!";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\Yahoo!\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\sina";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\sina\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\人民网";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\人民网\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\网易";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\网易\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\新华网";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\新华网\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\wb_Tengxun";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\wb_Tengxun\\";
		CEDP.extract(sourceDir, targetDir);

		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\wb_Sina";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\wb_Sina\\";
		CEDP.extract(sourceDir, targetDir);
		
		sourceDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\original\\wb_Sohu";
		targetDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\wb_Sohu\\";
		CEDP.extract(sourceDir, targetDir);

		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}

	

	
	private String sourceFileName = "";
	NodeList m_nodelist;
	TagNode m_root;
	private boolean debug = false;
	private int txtNodeCount = 0;
	private int textPathCount = 0;

	private ArrayList<String> textPaths = new ArrayList<String>();
	private ArrayList<String> textNodePaths = new ArrayList<String>();
	private ArrayList<TextNode> textNodes = new ArrayList<TextNode>();
	private ArrayList<String> textNodeTxts = new ArrayList<String>();
	private Map<String, Pair<Integer, Integer, Integer>> nodePathMap = new TreeMap<String, Pair<Integer, Integer, Integer>>();
	private Map<String, Pair<Integer, ArrayList<Integer>, ArrayList<Integer>>> pathMap = new TreeMap<String, Pair<Integer, ArrayList<Integer>, ArrayList<Integer>>>();

	private Map<String, Double> textPathLength = new TreeMap<String, Double>();
	private Map<String, Double> textPathRatio = new TreeMap<String, Double>();
	private Map<String, Double> eTextPathLength = new TreeMap<String, Double>();
	private Map<String, Double> eTextPathRatio = new TreeMap<String, Double>();
	private Map<String, Double> textPathLevelRatio = new TreeMap<String, Double>();
	private Map<String, Double> eTextPathLevelRatio = new TreeMap<String, Double>();


	private double[] array_tpl = null;
	private double[] array_tpr = null;
	private double[] array_etpl = null;
	private double[] array_etpr = null;
	private double[] array_tplr = null;
	private double[] array_etplr = null;
	private double[] decisiveValues = null;

	
	private int windowSize = 1;
	private int paramDistance = 3;
}

