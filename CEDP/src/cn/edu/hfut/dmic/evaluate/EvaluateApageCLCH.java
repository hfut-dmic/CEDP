package cn.edu.hfut.dmic.evaluate;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.htmlparser.util.ParserException;

import cn.edu.hfut.dmic.util.DateUtil;
import cn.edu.hfut.dmic.util.FileUtil;
import cn.edu.hfut.dmic.util.HtmlUtil;

public class EvaluateApageCLCH {

	private double precision = 0;
	private double recall = 0;
	private double F_score = 0;
	private double score = 0;
	public double getScore(){
		return score;
	}
	
	public double getPrecision() {
		return precision;
	}

	public double getRecall() {
		return recall;
	}

	public double getF_score() {
		return F_score;
	}

	public void evaluate(String evalFileName, String goldFileName,
			PrintStream out) throws ParserException {
		TreeMap<String, Integer> evalBagOfWords = getBagOfWords(evalFileName);
		TreeMap<String, Integer> goldBagOfWords = getBagOfWords(goldFileName);
		TreeMap<String, Integer> intersection = new TreeMap<String, Integer>();
		Set<String> ks = evalBagOfWords.keySet();
		for (String s : ks){
			int evalCount = (Integer)evalBagOfWords.get(s);
			if (goldBagOfWords.containsKey(s)) {
				int goldCount = (Integer)goldBagOfWords.get(s);
				int count = ((evalCount-goldCount) < 0) ? evalCount : goldCount;
				intersection.put(s, count);
			}
		}
		Collection<Integer> evalBagOfWordsVals = evalBagOfWords.values();
		int evalBagOfWordsCount = 0;
		for (Integer i : evalBagOfWordsVals)
			evalBagOfWordsCount += i;
		Collection<Integer> goldBagOfWordsVals = goldBagOfWords.values();
		int goldBagOfWordsCount = 0;
		for (Integer i : goldBagOfWordsVals)
			goldBagOfWordsCount += i;
		Collection<Integer> intersectionVals = intersection.values();
		int intersectionCount = 0;
		
		for (Integer i : intersectionVals)
			intersectionCount += i;
		/*
		System.out.println("evalBagOfWords");
		for (Map.Entry<String, Integer> entry : evalBagOfWords.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println("goldBagOfWords");
		for (Map.Entry<String, Integer> entry : goldBagOfWords.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println("intersection");
		for (Map.Entry<String, Integer> entry : intersection.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		System.out.println("evalBagOfWordsCount=" + evalBagOfWordsCount);
		System.out.println("goldBagOfWordsCount=" + goldBagOfWordsCount);
		System.out.println("intersectionCount=" + intersectionCount);
		*/
		if(evalBagOfWordsCount == 0)
			precision = 0;
		else
			precision = 1.0 * intersectionCount / evalBagOfWordsCount;
		
		if(goldBagOfWordsCount == 0)
			recall = 0;
		else
			recall = 1.0 * intersectionCount / goldBagOfWordsCount;
		
		if(precision == 0 || recall == 0)
			F_score = 0;
		else
		   F_score = 2 * precision * recall / (precision + recall);
		score = 1.0 * intersectionCount /(evalBagOfWordsCount +  goldBagOfWordsCount - intersectionCount);
	}
	
	public void displayMetrics(){
		System.out.println("precision=" + precision);
		System.out.println("recall=" + recall);
		System.out.println("F_score=" + F_score);
		System.out.println("score=" + score);
	}

	private TreeMap<String, Integer> getBagOfWords(String fileName) throws ParserException {
		TreeMap<String, Integer> bag = new TreeMap<String, Integer>();
		String fileContentAsaString = FileUtil.getFileContentAsaString(fileName, " ", "gb2312");
		fileContentAsaString = HtmlUtil.removeAllTags(fileContentAsaString);
		fileContentAsaString = fileContentAsaString.replaceAll(" +", " ");
		for (int j = 0; j < fileContentAsaString.length(); j++) {
			String s = "" + fileContentAsaString.charAt(j);
			if (bag.containsKey(s)) {
				Integer i = bag.get(s);
				bag.put(s, i.intValue() + 1);
			}
			else {
				bag.put(s, 1);
			}
		}
		return bag;
	}

	private TreeMap<String, Integer> getBagOfWordsUTF(String fileName) throws ParserException {
		TreeMap<String, Integer> bag = new TreeMap<String, Integer>();
		String fileContentAsaString = FileUtil.getFileContentAsaString(fileName, " ", "UTF-8");
		fileContentAsaString = HtmlUtil.removeAllTags(fileContentAsaString);
		fileContentAsaString = fileContentAsaString.replaceAll(" +", " ");
		for (int j = 0; j < fileContentAsaString.length(); j++) {
			String s = "" + fileContentAsaString.charAt(j);
			if (bag.containsKey(s)) {
				Integer i = bag.get(s);
				bag.put(s, i.intValue() + 1);
			}
			else {
				bag.put(s, 1);
			}
		}
		return bag;
	}

	
	public static void main(String [] args) {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		String evalFileName = "E:\\EXT\\³éÈ¡\\cetr-dataset-extracted\\TTRExtractorThresh\\cleaneval\\zh\\10.txt";
		String goldFileName = "E:\\EXT\\³éÈ¡\\cetr-dataset\\cleaneval\\zh\\gold-ANSI\\10.txt";
		EvaluateApageCLCH evaluateApage = new EvaluateApageCLCH();
		try {
			evaluateApage.evaluate(evalFileName, goldFileName, System.out);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		evaluateApage.displayMetrics();
		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n",
				DateUtil.getDateDiff(startDate, endDate));
	}
}
