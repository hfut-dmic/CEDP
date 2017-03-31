package cn.edu.hfut.dmic.wi.wrapper;

import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;

import cn.edu.hfut.dmic.util.MathUtil;

public class TextNodeDensity {
	
	public static double[] get_node_max_density_sum(ArrayList<TextNode> textNodes) {
		double[] arr_node_max_density_sum = new double[textNodes.size()];
		for (int i = 0; i < textNodes.size(); i++) {
			double max_density_sum = 0;

			TagNode p = (TagNode) textNodes.get(i).getParent();
			while (p != null) {
				double cur_density_sum = Double.valueOf(p.getAttribute(CEDP_Global.m_density_sum)).doubleValue();
				if (cur_density_sum - max_density_sum > -1 * Double.MIN_VALUE)
					max_density_sum = cur_density_sum;
				p = (TagNode) p.getParent();
			}
			
			arr_node_max_density_sum[i] = max_density_sum;
		}
		return arr_node_max_density_sum;
	}

	public static double[] get_node_density_sum_probability(ArrayList<TextNode> textNodes) {
		double[] arr_node_density_sum_probability = new double[textNodes.size()];
		double[] arr_node_max_density_sum = get_node_max_density_sum(textNodes);
		
		double maxVal = MathUtil.getMaxValue(arr_node_max_density_sum);
		if (maxVal == 0)
			maxVal = 1;

		for (int i = 0; i < textNodes.size(); i++) {
			arr_node_density_sum_probability[i] = arr_node_max_density_sum[i] / maxVal;
		}
		
		return arr_node_density_sum_probability;
	}

	public static double[] get_node_max_text_density(ArrayList<TextNode> textNodes) {
		double[] arr_node_max_text_density = new double[textNodes.size()];
		for (int i = 0; i < textNodes.size(); i++) {
			double max_text_density = 0;

			TagNode p = (TagNode) textNodes.get(i).getParent();
			while (p != null) {
				double cur_text_density = Double.valueOf(p.getAttribute(CEDP_Global.m_text_density)).doubleValue();
				if (cur_text_density - max_text_density > -1 * Double.MIN_VALUE)
					max_text_density = cur_text_density;
				p = (TagNode) p.getParent();
			}
			
			arr_node_max_text_density[i] = max_text_density;
		}
		return arr_node_max_text_density;
	}
	
	public static double[] get_node_text_density_probability(ArrayList<TextNode> textNodes) {
		double[] arr_node_text_density_probability = new double[textNodes.size()];
		double[] arr_node_max_text_density = get_node_max_text_density(textNodes);
		
		double maxVal = MathUtil.getMaxValue(arr_node_max_text_density);
		if (maxVal == 0)
			maxVal = 1;

		for (int i = 0; i < textNodes.size(); i++) {
			arr_node_text_density_probability[i] = arr_node_max_text_density[i] / maxVal;
		}
		
		return arr_node_text_density_probability;
	}

	public static double[] get_node_max_density(ArrayList<TextNode> textNodes, String densityTye) {
		double[] arr_node_max_density = new double[textNodes.size()];
		for (int i = 0; i < textNodes.size(); i++) {
			double max_density = 0;

			TagNode p = (TagNode) textNodes.get(i).getParent();
			while (p != null) {
				double cur_density = Double.valueOf(p.getAttribute(densityTye)).doubleValue();
				if (cur_density - max_density > -1 * Double.MIN_VALUE)
					max_density = cur_density;
				p = (TagNode) p.getParent();
			}
			
			arr_node_max_density[i] = max_density;
		}
		return arr_node_max_density;
	}

	public static double[] get_node_density_probability(ArrayList<TextNode> textNodes, String densityTye) {
		double[] arr_node_density_probability = new double[textNodes.size()];
		double[] arr_node_max_density = get_node_max_density(textNodes, densityTye);
		
		double maxVal = MathUtil.getMaxValue(arr_node_max_density);
		if (maxVal == 0)
			maxVal = 1;

		for (int i = 0; i < textNodes.size(); i++) {
			arr_node_density_probability[i] = arr_node_max_density[i] / maxVal;
		}
		
		return arr_node_density_probability;
	}

}
