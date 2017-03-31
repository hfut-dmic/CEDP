package cn.edu.hfut.dmic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
//import java.util.Date;

/**
 * Class for processing a html file.
 * 
 * @author Gongqing Wu (wugq@hfut.edu.cn) and Li Li (banli811@163.com)
 * @version $Version: 1.0 $
 */
public class FileUtil {
	/*
	public static void main(String[] args) throws Exception {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		String sourceDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\goldWu";
		String destDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\goldWu-URL";
		File fileSourceDir = new File(sourceDir);
		File fileDestDir = new File(destDir);
		if (!fileDestDir.exists())
			fileDestDir.mkdirs();
		
		String[] sourceFilenames = fileSourceDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".txt")) {
					return true;
				} else {
					return false;
				}
			}
		});
//		FileOutputStream fos = new FileOutputStream("D:\\GQ-DEV\\Results\\20160709\\" + "log" + ".txt",
//				false);
//		PrintStream ps = new PrintStream(fos);
		
		for (int i = 0; i < sourceFilenames.length; i++) {
			String curSourceFileName = sourceDir + "\\" + sourceFilenames[i];
			String curDestFileName = destDir + "\\" + sourceFilenames[i];
			String content = FileUtil.getFileContentAsaString(curSourceFileName, " ");
//			ps.println(sourceFilenames[i]);
//			Pattern p = Pattern.compile("URL: [a-zA-z]+://[^\\s]*");
			content = content.replaceFirst("URL: [a-zA-z]+://[^\\s]*", "");
			PrintWriter out = new PrintWriter(curDestFileName);
			out.println(content);
			out.close();
		}

//		if (ps != null)
//			ps.close();

		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}
*/

	public static void main(String[] args) throws Exception {
		System.out.println("Start running, please wait ...");
//		Date startDate = new Date();
		String sourceDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\zh\\original";
		File fileSourceDir = new File(sourceDir);
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
		FileOutputStream fos = new FileOutputStream("D:\\GQ-DEV\\Results\\20160709\\" + "log" + ".txt",
				false);
		PrintStream ps = new PrintStream(fos);
		
		for (int i = 0; i < sourceFilenames.length; i++) {
			String curSourceFileName = sourceDir + "\\" + sourceFilenames[i];
			ps.print(sourceFilenames[i]);
			ps.print("\t");
			String sPageEncode = getPageEncode(curSourceFileName);
			ps.println(sPageEncode);
		}

		if (ps != null)
			ps.close();

//		Date endDate = new Date();
//		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}

	
	/**
	 * Get the page encode of a file.
	 * 
	 * @param fileName
	 *            the file name of a file to be detected
	 * @return the page encode of the detected file
	 */
	// TODO Change getPageEncode(String fileName) to getPageEncode(File f)
	// TODO Add getPageEncode(String htmlContentString) and Test it.
	// new ByteArrayInputStream(str.getBytes()) can be used for the task.
	@SuppressWarnings("deprecation")
	public static String getPageEncode(String fileName) {
		cpdetector.io.CodepageDetectorProxy detector = cpdetector.io.CodepageDetectorProxy.getInstance();
		detector.add(new cpdetector.io.ParsingDetector(false));
		detector.add(cpdetector.io.JChardetFacade.getInstance());
		detector.add(cpdetector.io.ASCIIDetector.getInstance());
		detector.add(cpdetector.io.UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		File f = new File(fileName);
		try {
			charset = detector.detectCodepage(f.toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return charset.name();
	}

	/**
	 * Get the file content as a string for a given filename.
	 * 
	 * @param filename
	 *            a file name to be processed
	 * @return the content string for the given filename
	 */
	public static String getFileContentAsaString(String filename) {

		String content = new String();
		BufferedReader reader = null;
		String charSet = getPageEncode(filename);
		System.out.println("filename=" + filename);
		System.out.println("CharsetWu =" + charSet);
		if (filename.equals("D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\original\\43.html"))
			charSet = "windows-1252";
		if (filename.equals("D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\original\\521.html"))
			charSet = "windows-1252";
		if (filename.equals("D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\original\\919.html"))
			charSet = "windows-1252";
		if (filename.equals("D:\\GQ-DEV\\cetr-dataset\\cleaneval\\zh\\original\\161.html"))
			charSet = "GB18030";
		try {
			InputStreamReader reader1 = new InputStreamReader(new FileInputStream(filename), charSet);
			reader = new BufferedReader(reader1);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				content += tempString.trim() + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return content;
	}

	public static String getFileContentAsaString(String filename, PrintStream logPrintStream) {

		String content = new String();
		BufferedReader reader = null;
		String charSet = getPageEncode(filename);
		logPrintStream.println("filename=" + filename);
		logPrintStream.println("CharsetWu =" + charSet);
		if (filename.equals("D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\original\\43.html"))
			charSet = "UTF-8";
		try {
			InputStreamReader reader1 = new InputStreamReader(new FileInputStream(filename), charSet);
			reader = new BufferedReader(reader1);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				content += tempString.trim() + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return content;
	}

	public static String getFileContentAsaString(String filename, String wrapStr) {

		String content = new String();
		BufferedReader reader = null;
		String charSet = getPageEncode(filename);
		try {
			InputStreamReader reader1 = new InputStreamReader(new FileInputStream(filename), charSet);
			reader = new BufferedReader(reader1);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				content += tempString.trim() + wrapStr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return content;
	}

	public static String getFileContentAsaString(String filename, String wrapStr, String charSet) {

		String content = new String();
		BufferedReader reader = null;
		try {
			InputStreamReader reader1 = new InputStreamReader(new FileInputStream(filename), charSet);
			reader = new BufferedReader(reader1);
			String tempString;
			while ((tempString = reader.readLine()) != null) {
				content += tempString.trim() + wrapStr;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return content;
	}
	
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

}
