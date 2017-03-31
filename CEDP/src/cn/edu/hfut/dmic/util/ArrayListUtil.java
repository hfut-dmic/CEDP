package cn.edu.hfut.dmic.util;

import java.util.ArrayList;

public class ArrayListUtil {
	public static int getLevenshteinDistance (ArrayList<String> s, ArrayList<String> t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("ArrayList must not be null");
		}
				
		int n = s.size(); // length of s
		int m = t.size(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into ArrayList s and t
		int i; // iterates through s
		int j; // iterates through t

		String t_j; // jth string of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.get(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.get(i - 1).equals(t_j)? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally left
				// and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
						+ cost);
			}

			// copy current distance counts to 'previous row' distance counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}
	
	
	public static ArrayList<String> delLGS(ArrayList<String> str1, ArrayList<String> str2) {   
		int i,j;
        int len1,len2;
        len1 = str1.size();
        len2 = str2.size();
        int maxLen = len1 > len2?len1:len2;
        int[] max = new int[maxLen];
        int[] maxIndex = new int[maxLen];
        int[] c = new int[maxLen];
        ArrayList<String> commonString = new ArrayList<String>();
        for (i = 0; i < len2 ; i++)
        {
            for (j = len1 -1; j >= 0; j--)
            {
                if (str2.get(i).endsWith(str1.get(j))) 
                {
                    if ( ( i == 0) || (j == 0) )
                        c[j] = 1;
                    else
                        c[j] = c[j-1] + 1;
                }
                else
               {
                    c[j] = 0;
                }
                
                if (c[j] > max[0])
                {   //如果是大于那暂时只有一个是最长的,而且要把后面的清0;
                    max[0] = c[j];
                    maxIndex[0] = j;
                    
                    for (int k = 1; k < maxLen; k++)
                    {
                        max[k] = 0;
                        maxIndex[k] = 0;
                    }
                }
            }
        }
        
        for (j = 0; j < maxLen; j++)
        {
            if (max[j] > 0)
           {    
            	int num = 0;
                for (i = maxIndex[j] - max[j] + 1; i <= maxIndex[j]; i++){
                    commonString.add( str1.get(i));
                    num++;
                } 
               
            }            
        }  
        return commonString;
    }   
	
	
	public static ArrayList<String> getCommonSubString(ArrayList<String> left, ArrayList<String> right){
		ArrayList<String> commonString = new ArrayList<String>();
        while(intesect(left,right)){
        	ArrayList<String> result = delLGS(left, right);   
            String strResult="";   
            for(int index = 0;index<result.size();index++){   
            //英文
           //     strResult = strResult+ result.get(index)+" ";  
           //中文
                commonString.add(result.get(index));  
            } 
            
            left  = removeElement(left, result);
            right = removeElement(right, result);
        }       
        return commonString; 
	}
	
	public static ArrayList<String> removeElement(ArrayList<String> aList ,ArrayList<String> str){
		for (int i = 0; i < aList.size(); i++) {
			boolean judge = true;
			for (int j = 0; j < str.size(); j++) {
				if((i+j)<aList.size()){
					if(!aList.get(i+j).equals(str.get(j))){
						judge = false;
						break;
					}
				}else{
					judge = false;
				}		
			}
			if (judge) {
				int k = 0;
				while(k < str.size()){
					aList.remove(i);
					k++;
				}
				break;
			}
		}
		return aList;
	}
	public static boolean intesect(ArrayList<String> left, ArrayList<String> right){
		boolean intesect = false;
		for(int i = 0 ; i< left.size();i++){
			String str = left.get(i);
			if(right.contains(str)){
				intesect = true;
				break;
			}
		}
		return intesect;
		
	}
	
	public static int getLCS(ArrayList<String> left, ArrayList<String> right){

		int lenLeft = left.size();
		int lenRight = right.size();
		int[] preSubNum = new int[lenRight+1];
		int[] nowSubNum = new int[lenRight+1];

		for(int i = 0; i<lenRight+1;i++){
			preSubNum[i] = 0;
			nowSubNum[i] = 0;
		}
		
		for (int i = 1;  i < lenLeft+1; i++) {
			for (int j = 1; j< lenRight+1; j++) {
				if(left.get(i-1).equals(right.get(j-1)))
				// 元素相等时
				{   
					nowSubNum[j] = preSubNum[j-1]+1;
				} else if(nowSubNum[j-1] > preSubNum[j] ){
					nowSubNum[j]  = nowSubNum[j-1] ;
				}else{
					nowSubNum[j]  = preSubNum[j] ;
				}				
			}
			for(int k = 0; k<lenRight+1;k++){
				preSubNum[k] = nowSubNum[k];
			}		
		}	
		
		return nowSubNum[lenRight];
	}

}
