package cn.edu.hfut.dmic.util;

import java.util.ArrayList;
import java.util.Date;

public class StringUtil {

	/**
	 * 去除字符串中的控制符和空格
	 * 
	 * @param allContent
	 * @return
	 */
	// TODO TO be modified
	public static String filterCtrlsAndBlanks(String allContent) {
		StringBuffer s = new StringBuffer();
		int i = 0;
		int index = 0;
		char ch;
		int chType;
		boolean bSPACE;

		while (i < allContent.codePointCount(0, allContent.length())) {
			index = allContent.offsetByCodePoints(0, i);
			ch = allContent.charAt(index);
			chType = Character.getType(ch);

			bSPACE = (chType == Character.DIRECTIONALITY_WHITESPACE)
					|| (chType == Character.CONTROL);

			// 过滤控制符和空格
			if (!bSPACE)
				s.append(ch);
			i++;
		}
		return new String(s);
	}

	/**
	 * 抽取字符串的中文成分
	 * 
	 * @param allContent
	 * @return
	 */
	// TODO TO be modified
	public static String extractCContent(String allContent) {
		StringBuffer s = new StringBuffer();
		int i = 0;
		int index = 0;
		char ch;
		int chType;
		boolean bOTHER_LETTER;
		boolean bCurWhitespace;
		boolean bFlag;

		bCurWhitespace = false;
		while (i < allContent.codePointCount(0, allContent.length())) {
			index = allContent.offsetByCodePoints(0, i);
			ch = allContent.charAt(index);
			chType = Character.getType(ch);
			bOTHER_LETTER = (chType == Character.OTHER_LETTER);

			// 非汉字转换为空格，汉字则置当前字符是否为空格的标志
			if (!bOTHER_LETTER) {
				// 非汉字，转换为空格
				if (bCurWhitespace)
					bFlag = false;
				else {
					bFlag = true;
					ch = ' ';
					bCurWhitespace = true;
				}
			} else {
				// 为汉字
				bCurWhitespace = false;
				bFlag = true;
			}

			if (bFlag)
				s.append(ch);
			i++;
		}
		return new String(s);
	}

	public static int getLevenshteinDistance(String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		/*
		 * The difference between this impl. and the previous is that, rather
		 * than creating and retaining a matrix of size s.length()+1 by
		 * t.length()+1, we maintain two single-dimensional arrays of length
		 * s.length()+1. The first, d, is the 'current working' distance array
		 * that maintains the newest distance cost counts as we iterate through
		 * the characters of String s. Each time we increment the index of
		 * String t we are comparing, d is copied to p, the second int[]. Doing
		 * so allows us to retain the previous cost counts as required by the
		 * algorithm (taking the minimum of the cost count to the left, up one,
		 * and diagonally up and to the left of the current cost count being
		 * calculated). (Note that the arrays aren't really copied anymore, just
		 * switched...this is clearly much better than cloning an array or doing
		 * a System.arraycopy() each time through the outer loop.)
		 * 
		 * Effectively, the difference between the two implementations is this
		 * one does not cause an out of memory condition when calculating the LD
		 * over two very large strings.
		 */

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
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

	public static String delLGS(char[] str1, char[] str2) {
		/*
		 * int lenLeft = left.length; int lenRight = right.length; int c[][] =
		 * new int[lenLeft][lenRight]; char[] p; int start, end, len, i, j = 0;
		 * 
		 * end = len = 0;
		 * 
		 * for (i = 0; i < lenLeft; i++) { for (j = lenRight - 1; j >= 0; j--) {
		 * if (left[i] == right[j])// 元素相等时 { if (i == 0 || j == 0) c[i][j] = 1;
		 * else { c[i][j] = c[i - 1][j - 1] + 1; } } else c[i][j] = 0;
		 * 
		 * if (c[i][j] > len) { len = c[i][j]; end = j; } } }
		 * 
		 * start = end - len + 1; p = new char[len]; for (i = start; i <= end;
		 * i++) { p[i - start] = right[i]; } return p;
		 */
		int i, j;
		int len1, len2;
		len1 = str1.length;
		len2 = str2.length;
		int maxLen = len1 > len2 ? len1 : len2;
		int[] max = new int[maxLen];
		int[] maxIndex = new int[maxLen];
		int[] c = new int[maxLen];
		String commonString = "";
		for (i = 0; i < len2; i++) {
			for (j = len1 - 1; j >= 0; j--) {
				if (str2[i] == str1[j]) {
					if ((i == 0) || (j == 0))
						c[j] = 1;
					else
						c[j] = c[j - 1] + 1;
				} else {
					c[j] = 0;
				}

				if (c[j] > max[0]) { // 如果是大于那暂时只有一个是最长的,而且要把后面的清0;
					max[0] = c[j];
					maxIndex[0] = j;

					for (int k = 1; k < maxLen; k++) {
						max[k] = 0;
						maxIndex[k] = 0;
					}
				}
			}
		}

		for (j = 0; j < maxLen; j++) {
			if (max[j] > 0) {
				for (i = maxIndex[j] - max[j] + 1; i <= maxIndex[j]; i++)
					commonString = commonString + str1[i];
			}
		}
		return commonString;
	}

	public static int getLCS(String s1, String s2) {
		char[] left = s1.toCharArray();    
		char[] right = s2.toCharArray();
		int lenLeft = left.length;
		int lenRight = right.length;
		int[] preSubNum = new int[lenRight+1];
		int[] nowSubNum = new int[lenRight+1]; 
	

		for(int i = 0; i<lenRight+1;i++){
			preSubNum[i] = 0;
			nowSubNum[i] = 0;
		}
		
		for (int i = 1; i < lenLeft+1; i++) {
			for (int j = 1; j< lenRight+1; j++) {
				if (left[i-1] == right[j-1])// 元素相等时
				{
					nowSubNum[j] = preSubNum[j-1]+1;
				} else if(nowSubNum[j-1] > preSubNum[j]){
					nowSubNum[j] = nowSubNum[j-1];
				}else{
					nowSubNum[j] = preSubNum[j];
				}				
			}
			for(int k = 0; k<lenRight+1;k++){
				preSubNum[k] = nowSubNum[k];
			}	
		}

		return nowSubNum[lenRight];
	}

	public static String getCommonSubString(String s1, String s2) {
		String commonString = "";
		while (intesect(s1, s2)) {
			char[] left = s1.toCharArray();
			char[] right = s2.toCharArray();
			String result = delLGS(left, right);
			commonString = commonString + result;
			s1 = s1.replace(result, "");
			s2 = s2.replace(result, "");
		}
		return commonString;
	}

	public static boolean intesect(String s1, String s2) {
		char[] c1 = s1.toCharArray();
		boolean intesect = false;
		for (int i = 0; i < c1.length; i++) {
			char c = c1[i];
			if (s2.contains(String.valueOf(c))) {
				intesect = true;
				break;
			}
		}
		return intesect;

	}

	public static void main(String[] args) {
		String s1 = "xxyxxxz";
		String s2 = "yxxz";

		System.out.println(getLevenshteinDistance(s1,s2));
	}

}

