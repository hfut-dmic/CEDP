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
 *    HtmlUtil.java
 *    Copyright (C) 2012 Gongqing Wu, Li Li
 *
 */

package cn.edu.hfut.dmic.util;

import java.util.ArrayList;

import javax.swing.JTree;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.parserapplications.filterbuilder.HtmlTreeCellRenderer;
import org.htmlparser.parserapplications.filterbuilder.HtmlTreeModel;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * Class for processing a html file.
 * 
 * @author Gongqing Wu (wugq@hfut.edu.cn) and Li Li (banli811@163.com)
 * @version $Version: 1.0 $
 */
public class HtmlUtil {

	/**
	 * Preclean a HTML content string. Including replace all "&nbsp;" to "",
	 * filter the HTML comments, filter the scripts, filter the styles, convert
	 * "&lt;" to "<", and convert "&gt;" to ">".
	 * 
	 * @param strHtmlContent
	 *            a HTML content string for precleaning
	 * @return the precleaned HTML content string
	 */
	public static String preCleaning(String strHtmlContent) {
		strHtmlContent = strHtmlContent.replaceAll("&nbsp;", "");
		strHtmlContent = strHtmlContent.replaceAll("(?s)<!--.*?-->", "");
		strHtmlContent = strHtmlContent.replaceAll("(?s)<!-.*?-!>", "");
		strHtmlContent = strHtmlContent.replaceAll(
				"(?s)<(?i)script.*?>.*?</(?i)script>", "");
		strHtmlContent = strHtmlContent.replaceAll(
				"(?s)<(?i)style.*?>.*?</(?i)style>", "");
		strHtmlContent = strHtmlContent.replaceAll("&lt;", "<");
		strHtmlContent = strHtmlContent.replaceAll("&gt;", ">");
		return strHtmlContent;
	}

	/**
	 * Parser a HTML file to a JTree.
	 * 
	 * @param htmlfilename
	 *            a source HTML file
	 * @return a JTree object corresponding to the htmlfilename
	 */
	// TODO It seems JTree not a good component type for returning
	public static JTree HtmlToDomTree(String htmlfilename) {
		JTree DomTree = null;
		NodeList list = new NodeList();
		String fileContentAsaString = new String();
		Parser parser = new Parser();
		fileContentAsaString = FileUtil.getFileContentAsaString(htmlfilename);
		try {
			try {
				// Whether to use the contents instead of a file name to judge?
				// new ByteArrayInputStream(str.getBytes());
				// TODO I want to change getPageEncode(htmlfilename) to
				// getPageEncode(fileContentAsaString)
				parser.setEncoding(FileUtil.getPageEncode(htmlfilename));
				parser = new Parser(fileContentAsaString);

				for (NodeIterator iterator = parser.elements(); iterator
						.hasMoreNodes();) {
					org.htmlparser.Node node = iterator.nextNode();
					list.add(node);
				}
			} catch (EncodingChangeException ece) {
				list.removeAll();
				parser.reset();
				for (NodeIterator iterator = parser.elements(); iterator
						.hasMoreNodes();)
					list.add(iterator.nextNode());
			}
		} catch (ParserException pe) {
			pe.printStackTrace();
		}
		DomTree = new JTree(new HtmlTreeModel(list));
		DomTree.setCellRenderer(new HtmlTreeCellRenderer());
		return DomTree;
	}
	
	public static String removeAllTags(String html) throws ParserException{
		try {
			Parser p = new Parser(html);
			NodeList nl = p.parse(null);
			return getText(nl, new StringBuffer());
		} catch (Exception e) {
			return html.replaceAll("<[^>]*>", "");
		}
	}
	
	public static String getText(NodeList nl, StringBuffer sb){
		int i = 0;
		while (i < nl.size()) {
			Node next = nl.elementAt(i);
			if (next instanceof TextNode) {
				sb.append(((TextNode)next).getText()).append(" ");
			}else{
				if (next.getChildren() != null) {
					getText(next.getChildren(), sb);
				}
			}
			i++;
		}
		return sb.toString();
	}
	
	public static ArrayList<TagNode> getTagNodeChildren(TagNode tagNode) {
		ArrayList<TagNode> tagNodeChildren= new ArrayList<TagNode>();
		
		NodeList nodeList = tagNode.getChildren();
		
		if (nodeList != null) {
			for (int i = 0; i < nodeList.size(); i++) {
				Node node = nodeList.elementAt(i);
				if (node instanceof TagNode)
					tagNodeChildren.add((TagNode) node);
			}
		}
		
		return tagNodeChildren;
	}
	
	public static ArrayList<TextNode> getTextNodeChildren(TagNode tagNode) {
		ArrayList<TextNode> textNodeChildren= new ArrayList<TextNode>();
		
		NodeList nodeList = tagNode.getChildren();
		
		if (nodeList != null) {
			for (int i = 0; i < nodeList.size(); i++) {
				Node node = nodeList.elementAt(i);
				if (node instanceof TextNode)
					textNodeChildren.add((TextNode) node);
			}
		}
		
		return textNodeChildren;
	}
	
	public static TagNode getBodyNode(TagNode element, String bodyTag) {
		String tagName = element.getTagName();
		if (tagName.equalsIgnoreCase(bodyTag)) {
			return element;
		} else {
			ArrayList<TagNode> children = HtmlUtil.getTagNodeChildren(element);
			for (int i = 0; i < children.size(); i++) {
				TagNode childTarget = getBodyNode(children.get(i), bodyTag);
				if (childTarget != null)
					return childTarget;
			}
			return null;
		}
	}

	public static TagNode getBodyNode(NodeList nodelist, String bodyTag) {
		for (int i = 0; i < nodelist.size(); i++) {
			if (nodelist.elementAt(i) instanceof TagNode) {
				TagNode tagNode = (TagNode) (nodelist.elementAt(i));
				TagNode body = getBodyNode(tagNode, bodyTag);
				if (body != null) {
					return body;
				}
			}
		}
		return null;
	}
	
	public static String getNodePath(TagNode tagNode) {
		String tagPath = tagNode.getTagName();
		Node pNode = tagNode.getParent();
		while ((pNode != null) && (pNode instanceof TagNode)) {
			tagPath = ((TagNode) pNode).getTagName() + "." + tagPath;
			pNode = pNode.getParent();
		}
		return tagPath;
	}

}
