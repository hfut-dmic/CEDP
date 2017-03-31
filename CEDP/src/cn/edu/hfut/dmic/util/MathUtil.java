/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 *    StandarDeviation.java
 *    Copyright (C) 2012 Gongqing Wu, Li Li
 *
 */
package cn.edu.hfut.dmic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class for processing Date Classes.
 * 
 * @author Gongqing Wu (wugq@hfut.edu.cn)and Li Li (banli811@163.com)
 * @version $Version: 1.0 $
 */
public class MathUtil {
	/**
	 * Computer the standardDeviation of a ratio Array.
	 * 
	 * @param ratioArray
	 *            a tag ratio array
	 * @return standardDeviation
	 */
	public static double euclideanDistance(double[] array1, double[] array2) throws Exception{
		double distance = 0;
		if(array1.length != array2.length){
			throw new Exception("The length of array1 must equal with the length of array2");
		}else{
			for(int i = 0; i<array1.length;i++){
				distance += (array1[i]-array2[i])*(array1[i]-array2[i]);
			}
			distance = Math.sqrt(distance);
		}
		return distance;
	}
	
	
	public static double ComputerStandardDeviation(double[] v, int k){
		double standardDeviation = 0.0;
		double mean = 0.0;
		double sum = 0.0;
		for(int i = 0 ; i < v.length && i !=k; i++){
			sum += v[i];
		}
		mean = sum /(v.length-1);
		sum = 0.0;
		for(int i = 0 ; i < v.length ;i++ ){
			if(i != k)
				sum +=(v[i]-mean)*(v[i]-mean);
		}
		standardDeviation = Math.sqrt(sum / (v.length - 1));
		return standardDeviation;
	}
	
	public static int getMinValueNumber(double[] v){
		int num = 0;
		double minValue = Double.MAX_VALUE;
		for(int i = 0; i<v.length; i++){
			if(v[i]<minValue){
				num = i;
				minValue = v[i];
			}
		}
		return num;
	}
	
	public static int[] getIndexOrder(double[] value){
		int[] index = new int[value.length];
		for(int i = 0; i<value.length;i++){
			index[i] = i;
		}
		
		for (int i = 0; i < value.length - 1; i++) {
	          for (int j = i + 1; j < value.length; j++) {
	   
	        	  if (value[i] <= value[j]) {
	                    double tempv = value[i];
	                    value[i] = value[j];
	                    value[j] = tempv;

	                    int tempi = index[i];
	                    index[i] = index[j];
	                    index[j] = tempi;
	               }
	           }
	       }
		return index;
	}
	
	public static double getMinValue(double[] v,int length){
		double minValue = Double.MAX_VALUE;
		for(int i = 0; i<length; i++){
			if(v[i]<minValue){
				minValue = v[i];
			}
		}
		return minValue;
	}
	
	public static double getMinValue(double[] v){
		double minValue = Double.MAX_VALUE;
		for(int i = 0; i<v.length; i++){
			if(v[i]<minValue){
				minValue = v[i];
			}
		}
		return minValue;
	}
	
	
	public static double ComputerStandardDeviation(ArrayList<Double> ratioArray) {
		double standardDeviation = 0.0;
		double mean = 0.0;
		double sum = 0.0;
		for (int i = 0; i < ratioArray.size(); i++)
			sum += ratioArray.get(i);
		mean = sum / ratioArray.size();
		sum = 0.0;
		for (int i = 0; i < ratioArray.size(); i++) {
			double ratio = ratioArray.get(i);
			sum += (ratio - mean) * (ratio - mean);
		}
		standardDeviation = Math.sqrt(sum / ratioArray.size());
		return standardDeviation;
	}
   
	public static double ComputerStandardDeviationInt(ArrayList<Integer> array){
		double sum = (double)ComputerSum(array);
		double mean = (double)sum/array.size();
		sum = 0;
		double std = 0;
		for(int i = 0 ; i< array.size();i++){
			double value = array.get(i);
			sum += (value - mean) * (value - mean);
		}
		std = Math.sqrt(sum / array.size());
		return std;	
	}
	
	public static TreeMap<String, Double> ComputerProbability(Map<String, Double> ratioMap) {
		TreeMap<String, Double> probMap = new TreeMap<String, Double>();
		double sum = 0.0;
		for(Map.Entry<String, Double>entry: ratioMap.entrySet()){
			sum += entry.getValue();
		}
		for(Map.Entry<String, Double>entry: ratioMap.entrySet()){
			String key = entry.getKey();
			double value = entry.getValue()/sum;
			probMap.put(key, value);
		}
		return probMap;
	}
	
	public static double[] ComputerProbabilityArray(Map<String, Double> ratioMap) {
		int num = ratioMap.size();
		double[] array = new double[num];
		double sum = 0.0;
		int i=0;
		for(Map.Entry<String, Double>entry: ratioMap.entrySet()){
			sum += entry.getValue();
		}

		for(Map.Entry<String, Double>entry: ratioMap.entrySet()){
			double value = entry.getValue()/sum;
			array[i]= value;
			i++;
		}
		return array;
	}
	
	public static double[] ComputerProbabilityArray(double[] array) {
		double sum = 0;
		for(int i = 0; i<array.length;i++){
			sum +=array[i];
		}
		for(int i = 0; i< array.length;i++){
			array[i] /=sum;
		}
		return array;
	}
	
	
	
	
	
	public static double getMinValue(Map<String,Double> ratioMap){
		double min = Double.MAX_VALUE;
		for(Map.Entry<String, Double>entry: ratioMap.entrySet()){
			double value = entry.getValue();
			if(value < min){
				min = value;
			}
		}
		return min;
	}
	
	public static double getMinProb(Map<String,Double> map){
		double min = getMinValue(map);
		double sum = 0;
		for(Map.Entry<String, Double>entry:map.entrySet()){
			sum += entry.getValue();
		}
		sum = sum + min;
		return min/sum;
	}
	
	public static double ComputerStandardDeviation(double[] ratioArray){
		double standardDeviation=0.0;
		double mean=0.0;
		double sum=0.0;
		for(int i=0;i<ratioArray.length;i++)
			sum+=ratioArray[i];
		mean=sum/ratioArray.length;
		sum=0.0;
		for(int i=0;i<ratioArray.length;i++){
			double ratio=ratioArray[i];
			sum+=(ratio-mean)*(ratio-mean);
		}
		standardDeviation=Math.sqrt(sum/ratioArray.length);
		return standardDeviation;
	}
	
    public static double[] ComputerProbabilityArray2(Map<String,Double> ratioMap){
    	int num = ratioMap.size();
		double[] array = new double[num+1];
		double sum = 0.0;
		int i=0;
		for(Map.Entry<String, Double>entry: ratioMap.entrySet()){
			sum += entry.getValue();
		}
		double min = getMinValue(ratioMap);
		sum = sum + min;
		for(Map.Entry<String, Double>entry: ratioMap.entrySet()){
			double value = entry.getValue()/sum;
			array[i]= value;
			i++;
		}
		array[num] = min/sum;
		return array;
    	
    }
	
	public static double ComputerStandardDeviation(Map<String, Double> probMap){
		double standardDeviation = 0.0;
		double mean = 0.0;
		double sum = 0.0;
		for(Map.Entry<String, Double>entry:probMap.entrySet()){
			sum +=entry.getValue();
		}
		mean = sum/probMap.size();
		sum = 0.0;
		for(Map.Entry<String, Double>entry:probMap.entrySet()){
			sum += (mean - entry.getValue())*(mean - entry.getValue());
		}
		standardDeviation = Math.sqrt(sum/probMap.size());
		return standardDeviation;
	}
	
	public static double ComputerMean(Map<String, Double> probMap){
		double mean = 0.0;
		double sum = 0.0;
		for(Map.Entry<String, Double>entry:probMap.entrySet()){
			sum +=entry.getValue();
		}
		mean = sum/probMap.size();
		return mean;
	}
	
	public static double ComputerSum(double[] array){
		double sum = 0;
		for(int i =0; i<array.length;i++){
			sum += array[i];
		}
		return sum;
	}
	
	public static int ComputerSum(ArrayList<Integer> array){
		int sum = 0;
		for(int i = 0; i<array.size();i++){
			sum += array.get(i);
		}
		return sum;
	}
	
	public static double ComputerMean(ArrayList<Double> array ){
	     double sum = 0;
	     for(int i= 0 ;i< array.size();i++){
	    	 sum +=array.get(i);
	     }
	     return sum/array.size();
	}
	
	public static double ComputerMean(double[] array){
	    double sum = 0;
	     for(int i= 0 ;i< array.length;i++){
	    	 sum +=array[i];
	     }
	     return sum/array.length;
	}
	
	public static int getMaxNumber(double [] array){
		double max = Double.MIN_VALUE;
		int index = 0;
		for(int i = 1 ; i<array.length;i++){
			if(array[i]>max){
				index = i;
				max = array[i];
			}
		}
		return index;
	}	
	
	public static ArrayList<Integer> getMaxNumbers(double[] array){
		ArrayList<Integer> maxArray = new ArrayList<Integer>();
		double std = ComputerStandardDeviation(array)+ComputerMean(array);
		for(int i = 0 ;i < array.length;i++){
			if(array[i]>=std){
				maxArray.add(i);
			}
		}
		return maxArray;
	}
	public static double getMaxValue(double[] array){
		double value = Double.MIN_VALUE;
		for(int i = 0; i<array.length;i++){
			if(array[i]>value )
				value = array[i];
		}		
		return value;
	}
	public static double getCenterValue(double[] array){
		double centerValue = 0;
		for(int i = 1; i< array.length;i++){
			double key = array[i];
			int j = i-1;
			while(j>=0 && array[j]> key){
				array[j+1] = array[j];
				j--;
			}
			array[j+1]= key;
		}
		centerValue = array[array.length/2];
		return centerValue;
	}
   
	public static double getSecendValue(double [] list){
		double value = 0;
		double max = 0;
		for(int i = 0; i< list.length;i++){
			if(list[i]>max){
				max = list[i];
			}
		}
		for(int i = 0; i<list.length;i++){
			if(list[i]>value && list[i]!=max){
				value = list[i];
			}
		}
		return value;
		
		
	}	
	public static double getMaxValue1(double [] list){
		if(list.length>0){
		    Arrays.sort(list);
		    double max = list[list.length-1];
		    return max;    
		}
		return 0;
	}
	
	public static double calculatePoissionCorrelation(double[] arr1, double[] arr2){
		double sum1 = 0, sum2 = 0, sum3 = 0;
		double avg1 = ComputerMean(arr1);
		double avg2 = ComputerMean(arr2);
		for(int i = 0; i < arr1.length; i++){
			sum1 +=(arr1[i]-avg1)*(arr2[i]-avg2);
			sum2 +=(arr1[i]-avg1)*(arr1[i]-avg1);
			sum3 +=(arr2[i]-avg2)*(arr2[i]-avg2);		
		}		
		return  sum1/Math.sqrt(sum2*sum3);
	}
	
	public static void main(String[] args){
		double[] v = {0.5,0.5,0.4};
		System.out.println(MathUtil.ComputerStandardDeviation(v)/MathUtil.ComputerMean(v));
	}
}
