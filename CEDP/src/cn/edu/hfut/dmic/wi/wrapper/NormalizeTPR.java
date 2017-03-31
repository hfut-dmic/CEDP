package cn.edu.hfut.dmic.wi.wrapper;

import java.util.Arrays;
import java.util.Comparator;

public class NormalizeTPR {
	
	public static double[] normalize(double[] values) {
		double[] normalized_values = new double[values.length];
		
		class M_Element {
			int index;
			double value;
		}
		M_Element[] arr_M_Element = new M_Element[values.length];
		for (int i = 0; i < values.length; i++) {
			M_Element cur_M_Element = new M_Element();
			cur_M_Element.index = i;
			cur_M_Element.value = values[i];
			arr_M_Element[i] = cur_M_Element;
		}
		Arrays.sort(arr_M_Element, new Comparator<M_Element>() {
			public int compare(M_Element arg0, M_Element arg1) {
				return Double.compare(arg0.value, arg1.value);
			}
		});
		
		for (int i = 0; i < arr_M_Element.length; i++) {
			normalized_values[arr_M_Element[i].index] = i + 1;
		}
		
		return normalized_values;
	}

}
