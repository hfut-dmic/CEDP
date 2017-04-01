package cn.edu.hfut.dmic.wi.wrapper;

import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;

import cn.edu.hfut.dmic.util.HtmlUtil;

public class TextDensity {

	public static void countChar_CETD(TagNode element) {
		long char_num = 0;
		String l2s_char_num;

		char_num = element.toPlainTextString().length();
		l2s_char_num = Long.toString(char_num);
		element.setAttribute(CEDP_Global.m_char_num, l2s_char_num);

		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			countChar_CETD(child);
		}
	}
	
	public static long countChar(TagNode element) {
		long char_num = 0;
		String l2s_char_num;
		
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			countChar(child);
		}
		
		ArrayList<TextNode> textNodeChildren = HtmlUtil.getTextNodeChildren(element);
		for(TextNode child : textNodeChildren) {
			char_num += child.toPlainTextString().trim().length();
		}
		
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			char_num += Long.valueOf(child.getAttribute(CEDP_Global.m_char_num)).longValue();
		}
		
		l2s_char_num = Long.toString(char_num);
		element.setAttribute(CEDP_Global.m_char_num, l2s_char_num);
		
		return char_num;
	}

	public static long countCharSubTree(TagNode element) {
		long char_num = 0;
		String l2s_char_num;
		
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			countCharSubTree(child);
		}
		
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			ArrayList<TextNode> textNodeChildren = HtmlUtil.getTextNodeChildren(child);
			for(TextNode textNode : textNodeChildren) {
				char_num += textNode.toPlainTextString().trim().length();
			}
			
			char_num += Long.valueOf(child.getAttribute(CEDP_Global.m_char_num)).longValue();
		}
		
		l2s_char_num = Long.toString(char_num);
		element.setAttribute(CEDP_Global.m_char_num, l2s_char_num);
		
		return char_num;
	}

	public static void countPunc(TagNode element) {
		long punc_num = 0;
		String l2s_punc_num;
		
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			countPunc(child);
		}
		
		ArrayList<TextNode> textNodeChildren = HtmlUtil.getTextNodeChildren(element);
		for(TextNode child : textNodeChildren) {
			String curText = child.toPlainTextString().trim();
			punc_num += curText.length() - curText.replaceAll("\\p{P}", "").length();
		}
		
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			punc_num += Long.valueOf(child.getAttribute(CEDP_Global.m_punc_num)).longValue();
		}
		
		l2s_punc_num = Long.toString(punc_num);
		element.setAttribute(CEDP_Global.m_punc_num, l2s_punc_num);
	}

	public static void updateLinkChar(TagNode element) {
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			child.setAttribute(CEDP_Global.m_linkchar_num, child.getAttribute(CEDP_Global.m_char_num));
			updateLinkChar(child);
		}
	}

	// call this function after countChar
	public static void countLinkChar(TagNode element) {
		long linkchar_num = 0;
		String l2s_linkchar_num;
		String tag_name = element.getTagName();

		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			countLinkChar(child);
		}

		// deal with hyperlink and sth like that
		if (tag_name.equalsIgnoreCase("A") || tag_name.equalsIgnoreCase("BUTTON")
				|| tag_name.equalsIgnoreCase("SELECT")) {
			linkchar_num = Long.valueOf(element.getAttribute(CEDP_Global.m_char_num)).longValue();
			updateLinkChar(element);
		} else {
			for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
				linkchar_num += Long.valueOf(child.getAttribute(CEDP_Global.m_linkchar_num)).longValue();
			}
		}

		l2s_linkchar_num = Long.toString(linkchar_num);
		element.setAttribute(CEDP_Global.m_linkchar_num, l2s_linkchar_num);
	}

	public static void countNonLinkChar(TagNode element) {
		long char_num = Long.valueOf(element.getAttribute(CEDP_Global.m_char_num)).longValue();
		long linkchar_num = Long.valueOf(element.getAttribute(CEDP_Global.m_linkchar_num)).longValue();
		long non_linkchar_num = char_num - linkchar_num;
		String l2s_non_linkchar_num = Long.toString(non_linkchar_num);
		element.setAttribute(CEDP_Global.m_nonlinkchar_num, l2s_non_linkchar_num);
		
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			countNonLinkChar(child);
		}
	}

	public static void countTag(TagNode element) {
		long tag_num = 0;
		String l2s_tag_num;

		if (HtmlUtil.getTagNodeChildren(element).size() == 0) {
			l2s_tag_num = Long.toString(0);
			element.setAttribute(CEDP_Global.m_tag_num, l2s_tag_num);
		} else {
			for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
				countTag(child);
			}
			for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
				tag_num += Long.valueOf(child.getAttribute(CEDP_Global.m_tag_num)).longValue() + 1;
			}
			l2s_tag_num = Long.toString(tag_num);
			element.setAttribute(CEDP_Global.m_tag_num, l2s_tag_num);
		}
	}

	public static void updateLinkTag(TagNode element) {
		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			child.setAttribute(CEDP_Global.m_linktag_num, child.getAttribute(CEDP_Global.m_tag_num));
			updateLinkTag(child);
		}
	}
	
	// call this function after countChar, countLinkChar
	public static void countLinkTag(TagNode element) {
		long linktag_num = 0;
		String l2s_linktag_num;
		String tag_name = element.getTagName();

		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			countLinkTag(child);
		}

		// deal with hyperlink and sth like that
		if (tag_name.equalsIgnoreCase("A") || tag_name.equalsIgnoreCase("BUTTON")
				|| tag_name.equalsIgnoreCase("SELECT")) {
			linktag_num = Long.valueOf(element.getAttribute(CEDP_Global.m_tag_num)).longValue();
			updateLinkTag(element);
		} else {
			for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
				linktag_num += Long.valueOf(child.getAttribute(CEDP_Global.m_linktag_num)).longValue();
				tag_name = child.getTagName();

				// if a tag is <a> or sth plays similar role in web pages, then
				// anchor number add 1
				if (tag_name.equalsIgnoreCase("A") || tag_name.equalsIgnoreCase("BUTTON")
						|| tag_name.equalsIgnoreCase("SELECT")) {
					linktag_num++;
				} else {
					long child_linktag_num = Long.valueOf(child.getAttribute(CEDP_Global.m_linktag_num)).longValue();
					long child_tag_num = Long.valueOf(child.getAttribute(CEDP_Global.m_tag_num)).longValue();
					long child_char_num = Long.valueOf(child.getAttribute(CEDP_Global.m_char_num)).longValue();
					long child_linkchar_num = Long.valueOf(child.getAttribute(CEDP_Global.m_linkchar_num)).longValue();

					// child_linktag_num != 0: there are some anchor under this
					// child
					if (child_linktag_num == child_tag_num && child_char_num == child_linkchar_num
							&& 0 != child_linktag_num) {
						linktag_num++;
					}
				}
			}
		}

		l2s_linktag_num = Long.toString(linktag_num);
		element.setAttribute(CEDP_Global.m_linktag_num, l2s_linktag_num);
	}

	// call this function after countChar, countTag, countLinkChar, countLinkTag
	public static void computeTextDensity_CETD(TagNode element, double ratio) {
		long char_num = Long.valueOf(element.getAttribute(CEDP_Global.m_char_num)).longValue();
		long tag_num = Long.valueOf(element.getAttribute(CEDP_Global.m_tag_num)).longValue();
		long linkchar_num = Long.valueOf(element.getAttribute(CEDP_Global.m_linkchar_num)).longValue();
		long linktag_num = Long.valueOf(element.getAttribute(CEDP_Global.m_linktag_num)).longValue();

		double text_density = 0.0;
		String d2s_text_density;

		if (char_num == 0) {
			text_density = 0;
		} else {
			long un_linkchar_num = char_num - linkchar_num;

			if (tag_num == 0) {
				tag_num = 1;
			}
			if (linkchar_num == 0) {
				linkchar_num = 1;
			}
			if (linktag_num == 0) {
				linktag_num = 1;
			}
			if (un_linkchar_num == 0) {
				un_linkchar_num = 1;
			}

			text_density = (1.0 * char_num / tag_num)
					* Math.log((1.0 * char_num * tag_num) / (1.0 * linkchar_num * linktag_num))
					/ Math.log(Math.log(1.0 * char_num * linkchar_num / un_linkchar_num + ratio * char_num + Math.E));

			// text_density = 1.0 * char_num / tag_num;
		}

		// convert double to String
		d2s_text_density = Double.toString(text_density);
		element.setAttribute(CEDP_Global.m_text_density, d2s_text_density);

		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			computeTextDensity_CETD(child, ratio);
		}
	}

	public static void computeText2TagDensity(TagNode element, String charType, String tagType, String densityType) {
		long text_num = Long.valueOf(element.getAttribute(charType)).longValue();
		long tag_num = Long.valueOf(element.getAttribute(tagType)).longValue();

		double text2density = 0.0;
		String d2s_text2tag_density;

		if (text_num == 0) {
			text2density = 0;
		} else {

			if (tag_num == 0) {
				tag_num = 1;
			}
			text2density = (1.0 * text_num / tag_num);
		}

		// convert double to String
		d2s_text2tag_density = Double.toString(text2density);
		element.setAttribute(densityType, d2s_text2tag_density);

		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			computeText2TagDensity(child, charType, tagType, densityType);
		}
	}

	public static void computeDensitySum(TagNode element, double ratio) {
		double density_sum = 0.0;
		// long char_num_sum = 0;
		// long char_num = 0;
		String d2s_density_sum;

		String content = element.toPlainTextString();
		String child_content;
		int from = 0;
		int index = 0;
		int length = 0;
		// double text_density = 0.0;

		if (HtmlUtil.getTagNodeChildren(element).size() == 0) {
			density_sum = Double.valueOf(element.getAttribute(CEDP_Global.m_text_density)).doubleValue();
		} else {
			for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
				computeDensitySum(child, ratio);
			}
			for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
				density_sum += Double.valueOf(child.getAttribute(CEDP_Global.m_text_density)).doubleValue();

				// text before tag
				/*
				child_content = child.toPlainTextString();
				index = content.indexOf(child_content, from);
				if (index > -1) {
					length = index - from;
					if (length > 0) {
						density_sum += length * Math.log(1.0 * length) / Math.log(Math.log(ratio * length + Math.E));
					}
					from = index + child_content.length();
				}
				*/
			}

			// text after tag
			/*
			length = element.toPlainTextString().length() - from;
			if (length > 0) {
				density_sum += length * Math.log(1.0 * length) / Math.log(Math.log(ratio * length + Math.E));
			}
			*/
		}

		d2s_density_sum = Double.toString(density_sum);
		element.setAttribute(CEDP_Global.m_density_sum, d2s_density_sum);
	}
	
	public static void amend(TagNode element) {
		if (element.getAttribute(CEDP_Global.m_char_num) == null)
			element.setAttribute(CEDP_Global.m_char_num, "0");
		
		if (element.getAttribute(CEDP_Global.m_text_density) == null)
			element.setAttribute(CEDP_Global.m_text_density, "0");

		
		/*
		if (element.getAttribute(CEPF_Global.m_punc_num) == null)
			element.setAttribute(CEPF_Global.m_punc_num, "0");
		if (element.getAttribute(CEPF_Global.m_punc_density) == null)
			element.setAttribute(CEPF_Global.m_punc_density, "0");
			*/

		if (element.getAttribute(CEDP_Global.m_linkchar_num) == null)
			element.setAttribute(CEDP_Global.m_linkchar_num, "0");
		/*
		if (element.getAttribute(CEPF_Global.m_linkchar_density) == null)
			element.setAttribute(CEPF_Global.m_linkchar_density, "0");
			*/

		if (element.getAttribute(CEDP_Global.m_nonlinkchar_num) == null)
			element.setAttribute(CEDP_Global.m_nonlinkchar_num, "0");
		/*
		if (element.getAttribute(CEPF_Global.m_nonlinkchar_density) == null)
			element.setAttribute(CEPF_Global.m_nonlinkchar_density, "0");
*/
		if (element.getAttribute(CEDP_Global.m_linktag_num) == null)
			element.setAttribute(CEDP_Global.m_linktag_num, "0");
		/*
		if (element.getAttribute(CEPF_Global.m_linktag_density) == null)
			element.setAttribute(CEPF_Global.m_linktag_density, "0");
			
			*/

		if (element.getAttribute(CEDP_Global.m_density_sum) == null)
			element.setAttribute(CEDP_Global.m_density_sum, "0");
		if (element.getAttribute(CEDP_Global.m_tag_num) == null)
			element.setAttribute(CEDP_Global.m_tag_num, "0");

		for (TagNode child : HtmlUtil.getTagNodeChildren(element)) {
			amend(child);
		}
	}
	
	public static void computeDensitys_CETD(NodeList nodelist, TagNode root) {
		TagNode body = HtmlUtil.getBodyNode(nodelist, "body");
		if (body == null) {
			body = HtmlUtil.getBodyNode(nodelist, "html");
			if (body == null) {
				body = root;
			}
		}
		
		countChar_CETD(body);
		countTag(body);
		countLinkChar(body);
		countLinkTag(body);
		
	    double char_num = Double.valueOf(body.getAttribute(CEDP_Global.m_char_num)).doubleValue();
	    double linkchar_num = Double.valueOf(body.getAttribute(CEDP_Global.m_linkchar_num)).doubleValue();
	    double ratio = linkchar_num / char_num;
	    computeTextDensity_CETD(body, ratio);
	    computeDensitySum(body, ratio);
	    
	    amend(root);
	}
	
	public static void computeDensitys(NodeList nodelist, TagNode root) {
		countChar(root);
//		countPunc(root);
		countLinkChar(root);
		countNonLinkChar(root);
		countTag(root);
//		countLinkTag(root);
//	    computeText2TagDensity(root, CEDP_Global.m_char_num, CEDP_Global.m_tag_num, CEDP_Global.m_text_density);
//	    computeText2TagDensity(root, CEDP_Global.m_punc_num, CEPF_Global.m_tag_num, CEPF_Global.m_punc_density);
//	    computeText2TagDensity(root, CEDP_Global.m_linkchar_num, CEPF_Global.m_tag_num, CEPF_Global.m_linkchar_density);
	    computeText2TagDensity(root, CEDP_Global.m_nonlinkchar_num, CEDP_Global.m_tag_num, CEDP_Global.m_nonlinkchar_density);
//	    computeText2TagDensity(root, CEDP_Global.m_linktag_num, CEPF_Global.m_linktag_num, CEPF_Global.m_linktag_density);
//	    computeDensitySum(body, ratio);
	    amend(root);
	}

	/*
	public static double findMaxDensitySum(Element element) {
	    double max_density_sum = Double.valueOf(element.attr(CETD_Global.m_density_sum)).doubleValue();
	    double temp_max_density_sum = 0.0;

	    for (Element child : element.children()) {
	        temp_max_density_sum = findMaxDensitySum(child);
	        if (temp_max_density_sum - max_density_sum > Double.MIN_VALUE) {
	            max_density_sum = temp_max_density_sum;
	        }
	    }

	    //record the max_density_sum under the element
	    String d2s_max_density_sum = Double.toString(max_density_sum);
	    element.attr(CETD_Global.m_max_density_sum, d2s_max_density_sum);
	    return max_density_sum;
	}

	public static void setMark(Element element, int mark) {
	    String i2s_mark = Integer.toString(mark);

	    element.attr(CETD_Global.m_mark, i2s_mark);

	    for (Element child : element.children()) {
	        setMark(child, mark);
	    }
	}
	
	public static double getThreshold(Element element, double max_density_sum) {
	    double threshold = -1.0;

	    //search the max densitysum element
	    Element target = Foundation.searchTag(element, CETD_Global.m_density_sum, max_density_sum);
	    threshold = Double.valueOf(target.attr(CETD_Global.m_text_density)).doubleValue();
	    setMark(target, 1);

	    Element parent = target.parent();
	    while (true) {
	    	if (parent == null)
	    		break;
	    	if (parent.tagName().equalsIgnoreCase("HTML"))
	    		break;
	        double text_density = Double.valueOf(parent.attr(CETD_Global.m_text_density)).doubleValue();
	            if((threshold - text_density) > -1 * Double.MIN_VALUE) {
	                threshold = text_density;
	            }

	            parent.attr(CETD_Global.m_mark, "2");
	            parent = parent.parent();
	    }

	    return threshold;
	}
	
	public static void findMaxDensitySumTag(Element element, double max_density_sum) {
	    //search the max densitysum element
	    Element target = Foundation.searchTag(element, CETD_Global.m_density_sum, max_density_sum);;

	    int mark = Integer.valueOf(target.attr(CETD_Global.m_mark)).intValue();
	    if(mark == 1) {
	        return;
	    }

	    setMark(target, 1);

	    Element parent = target.parent();
	    while(true) {
	    	if (parent == null)
	    		break;
	    	if (parent.tagName().equalsIgnoreCase("HTML"))
	    		break;
            parent.attr(CETD_Global.m_mark, "2");
	        parent = parent.parent();
	    }
	}

	public static void markContent(Element element, double threshold) {
	    double text_density = Double.valueOf(element.attr(CETD_Global.m_text_density)).doubleValue();
	    double max_density_sum = Double.valueOf(element.attr(CETD_Global.m_max_density_sum)).doubleValue();
	    int mark = Integer.valueOf(element.attr(CETD_Global.m_mark)).intValue();

	    if(mark != 1 && (text_density - threshold > -1 * Double.MIN_VALUE)) {
	        findMaxDensitySumTag(element, max_density_sum);
	        for(Element child : element.children()) {
	            markContent(child, threshold);
	        }
	    }
	}
	*/

}
