package cn.edu.hfut.dmic.evaluate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import org.htmlparser.util.ParserException;

import cn.edu.hfut.dmic.util.DateUtil;

public class EvaluatePages {

	private double avg_precision = 0;
	private double avg_recall = 0;
	private double avg_F_score = 0;
	private double F_score_recalculate = 0;

	public double getAvg_precision() {
		return avg_precision;
	}

	public double getAvg_recall() {
		return avg_recall;
	}

	public double getAvg_F_score() {
		return avg_F_score;
	}

	public double getF_score_recalculate() {
		return F_score_recalculate;
	}

	public void evaluate(String evalDir, String goldDir, PrintStream out,
			PrintStream ps) throws ParserException, IOException {
		avg_precision = 0;
		avg_recall = 0;
		avg_F_score = 0;
		F_score_recalculate = 0;
		File fileGoldDir = new File(goldDir);
		String[] sourceFilenames = fileGoldDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".txt")) {
					return true;
				} else {
					return false;
				}
			}
		});

		EvaluateApageWL evaluateApage = new EvaluateApageWL();
		int num = sourceFilenames.length;
		for (int i = 0; i < num; i++) {
			// System.out.println("i=" + i + ", filename=" +
			// sourceFilenames[i]);
			evaluateApage.evaluate(evalDir + "\\" + sourceFilenames[i], goldDir
					+ "\\" + sourceFilenames[i], out);
			avg_precision += evaluateApage.getPrecision();
			avg_recall += evaluateApage.getRecall();
			avg_F_score += evaluateApage.getF_score();
			
			System.out.println("filename=" + sourceFilenames[i] + ", "
					+ evaluateApage.getPrecision() + ","
					+ evaluateApage.getRecall() + ","
					+ evaluateApage.getF_score());
			ps.printf("%s  :  %s  :  %f  :  %f   :   %f", evalDir,
					sourceFilenames[i], evaluateApage.getPrecision(),
					evaluateApage.getRecall(), evaluateApage.getF_score());
			ps.println();

		}
		avg_precision /= num;
		avg_recall /= num;
		avg_F_score /= num;
		F_score_recalculate = 2 * avg_precision * avg_recall
				/ (avg_precision + avg_recall);
		out.printf("%s %f %f %f %f", evalDir, avg_precision, avg_recall,
				avg_F_score, F_score_recalculate);
		out.println();
	}
	
	public void evaluateWu(String evalDir, String goldDir, PrintStream out,
			PrintStream ps, PrintStream pstab, double logThresh) throws ParserException, IOException {
		int evalCount = 0;
		avg_precision = 0;
		avg_recall = 0;
		avg_F_score = 0;
		F_score_recalculate = 0;
		File fileGoldDir = new File(goldDir);
		String[] goldFilenames = fileGoldDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".txt")) {
					return true;
				} else {
					return false;
				}
			}
		});
/*
		File fileEvalDir = new File(evalDir);
		String[] evalFilenames = fileEvalDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".txt")) {
					return true;
				} else {
					return false;
				}
			}
		});
		ArrayList<String> evalFilenamesArrList = new ArrayList<String>();
		for (int i = 0; i < evalFilenames.length; i++) {
			evalFilenamesArrList.add(evalFilenames[i]);
		}
		
		int num = goldFilenames.length;
		for (int i = 0; i < num; i++) {
			if (!evalFilenamesArrList.contains(goldFilenames[i]))
					System.out.println(goldFilenames[i]);
		}
*/		
		EvaluateApageWL evaluateApage = new EvaluateApageWL();
		int num = goldFilenames.length;
		for (int i = 0; i < num; i++) {
			evaluateApage.evaluate(evalDir + "\\" + goldFilenames[i], goldDir + "\\" + goldFilenames[i], out);
			avg_precision += evaluateApage.getPrecision();
			avg_recall += evaluateApage.getRecall();
			avg_F_score += evaluateApage.getF_score();

			System.out.println("filename=" + goldFilenames[i] + ", " + evaluateApage.getPrecision() + ","
					+ evaluateApage.getRecall() + "," + evaluateApage.getF_score());
			if (evaluateApage.getF_score() <= logThresh) {
				ps.printf("%s  :  %s  :  %f  :  %f   :   %f", evalDir, goldFilenames[i], evaluateApage.getPrecision(),
						evaluateApage.getRecall(), evaluateApage.getF_score());
				ps.println();
				evalCount++;
			}
		}
		System.out.println("evalCount = " + evalCount);

		avg_precision /= num;
		avg_recall /= num;
		avg_F_score /= num;
		F_score_recalculate = 2 * avg_precision * avg_recall
				/ (avg_precision + avg_recall);
		out.printf("%s %f %f %f %f", evalDir, avg_precision, avg_recall,
				avg_F_score, F_score_recalculate);
		out.println();
		pstab.printf("%f\t", F_score_recalculate);
	}

	public void evaluateZH(String evalDir, String goldDir, PrintStream out,
			PrintStream ps) throws ParserException, IOException {
		avg_precision = 0;
		avg_recall = 0;
		avg_F_score = 0;
		F_score_recalculate = 0;
		File fileGoldDir = new File(goldDir);
		String[] sourceFilenames = fileGoldDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".txt")) {
					return true;
				} else {
					return false;
				}
			}
		});

		EvaluateApageCLCH evaluateApage = new EvaluateApageCLCH();
		int num = sourceFilenames.length;
		for (int i = 0; i < num; i++) {
			// System.out.println("i=" + i + ", filename=" +
			// sourceFilenames[i]);
			evaluateApage.evaluate(evalDir + "\\" + sourceFilenames[i], goldDir
					+ "\\" + sourceFilenames[i], out);
			avg_precision += evaluateApage.getPrecision();
			avg_recall += evaluateApage.getRecall();
			avg_F_score += evaluateApage.getF_score();
			// System.out.println(sourceFilenames[i]+":
			// "+evaluateApage.getPrecision()+" "+evaluateApage.getRecall()+"
			// "+evaluateApage.getF_score());
			System.out.println("filename=" + sourceFilenames[i] + ", "
					+ evaluateApage.getPrecision() + ","
					+ evaluateApage.getRecall() + ","
					+ evaluateApage.getF_score());
			ps.printf("%s  :  %s  :  %f  :  %f   :   %f", evalDir,
					sourceFilenames[i], evaluateApage.getPrecision(),
					evaluateApage.getRecall(), evaluateApage.getF_score());
			ps.println();

		}
		avg_precision /= num;
		avg_recall /= num;
		avg_F_score /= num;
		F_score_recalculate = 2 * avg_precision * avg_recall
				/ (avg_precision + avg_recall);
		out.printf("%s %f %f %f %f", evalDir, avg_precision, avg_recall,
				avg_F_score, F_score_recalculate);
		out.println();
	}

	public void evaluateZHWu(String evalDir, String goldDir, PrintStream out,
			PrintStream ps, PrintStream pstab, double logThresh) throws ParserException, IOException {
		int evalCount = 0;
		avg_precision = 0;
		avg_recall = 0;
		avg_F_score = 0;
		F_score_recalculate = 0;
		File fileGoldDir = new File(goldDir);
		String[] sourceFilenames = fileGoldDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.toLowerCase().endsWith(".txt")) {
					return true;
				} else {
					return false;
				}
			}
		});

		EvaluateApageCLCH evaluateApage = new EvaluateApageCLCH();
		int num = sourceFilenames.length;
		for (int i = 0; i < num; i++) {
			// System.out.println("i=" + i + ", filename=" +
			// sourceFilenames[i]);
			evaluateApage.evaluate(evalDir + "\\" + sourceFilenames[i], goldDir
					+ "\\" + sourceFilenames[i], out);
			avg_precision += evaluateApage.getPrecision();
			avg_recall += evaluateApage.getRecall();
			avg_F_score += evaluateApage.getF_score();
			// System.out.println(sourceFilenames[i]+":
			// "+evaluateApage.getPrecision()+" "+evaluateApage.getRecall()+"
			// "+evaluateApage.getF_score());
			System.out.println("filename=" + sourceFilenames[i] + ", "
					+ evaluateApage.getPrecision() + ","
					+ evaluateApage.getRecall() + ","
					+ evaluateApage.getF_score());
			if (evaluateApage.getF_score() <= logThresh) {
				ps.printf("%s  :  %s  :  %f  :  %f   :   %f", evalDir, sourceFilenames[i], evaluateApage.getPrecision(),
						evaluateApage.getRecall(), evaluateApage.getF_score());
				ps.println();
				evalCount++;
			}
		}
		System.out.println("evalCount = " + evalCount);
		avg_precision /= num;
		avg_recall /= num;
		avg_F_score /= num;
		F_score_recalculate = 2 * avg_precision * avg_recall
				/ (avg_precision + avg_recall);
		out.printf("%s %f %f %f %f", evalDir, avg_precision, avg_recall,
				avg_F_score, F_score_recalculate);
		out.println();
		pstab.printf("%f\t", F_score_recalculate);
	}

	public void displayMetrics() {
		System.out.println("avg_precision=" + avg_precision);
		System.out.println("avg_recall=" + avg_recall);
		System.out.println("avg_F_score=" + avg_F_score);
		System.out.println("F_score_recalculate=" + F_score_recalculate);
	}
	
	/*
	public static void main(String[] args) throws ParserException, IOException {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		EvaluatePagesWu evaluatePages = new EvaluatePagesWu();
		String subDir = "20161121-CETD";
		FileOutputStream fos = new FileOutputStream("D:\\GQ-DEV\\Results\\" + subDir + "\\fos.txt",
				false);
		PrintStream ps = new PrintStream(fos);
		FileOutputStream fostab = new FileOutputStream("D:\\GQ-DEV\\Results\\" + subDir + "\\fostab.txt",
				false);
		PrintStream pstab = new PrintStream(fostab);
		FileOutputStream fosW = new FileOutputStream(
				"D:\\GQ-DEV\\Results\\" + subDir + "fosW_detail.txt", false);
		PrintStream psW = new PrintStream(fosW);

		ps.println("cleanval-en");
		String evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\cleaneval-en\\";
		String goldDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\goldWu-URL";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("arstechnica");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\arstechnica\\";
		goldDir = "D:\\GQ-DEV\\CETD-dataset\\arstechnica\\gold";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("BBC");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\BBC\\";
		goldDir = "D:\\GQ-DEV\\CETD-dataset\\BBC\\gold";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("Chaos");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\Chaos\\";
		goldDir = "D:\\GQ-DEV\\CETD-dataset\\Chaos\\gold";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("nytimes");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\nytimes\\";
		goldDir = "D:\\GQ-DEV\\CETD-dataset\\nytimes\\gold";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("wiki");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\wiki\\";
		goldDir = "D:\\GQ-DEV\\CETD-dataset\\wiki\\gold";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("YAHOO!");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\YAHOO!\\";
		goldDir = "D:\\GQ-DEV\\CETD-dataset\\YAHOO!\\gold";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		pstab.println();
		if (ps != null)
			ps.close();
		if (pstab != null)
			ps.close();
		if (psW != null)
			psW.close();
		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}
	*/

	public static void main(String[] args) throws ParserException, IOException {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		EvaluatePages evaluatePages = new EvaluatePages();
		String subDir = "20170331-CEDP-CTD";
		FileOutputStream fos = new FileOutputStream("D:\\GQ-DEV\\Results\\" + subDir + "\\fos.txt",
				false);
		PrintStream ps = new PrintStream(fos);
		FileOutputStream fostab = new FileOutputStream("D:\\GQ-DEV\\Results\\" + subDir + "\\fostab.txt",
				false);
		PrintStream pstab = new PrintStream(fostab);
		FileOutputStream fosW = new FileOutputStream(
				"D:\\GQ-DEV\\Results\\" + subDir + "fosW_detail.txt", false);
		PrintStream psW = new PrintStream(fosW);
		
		ps.println("CleanEval-en");
		String evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\CleanEval-en\\";
		String goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\CleanEval-en";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("CleanEval-zh");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\CleanEval-zh\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\CleanEval-zh";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("nypost");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\nypost\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Nypost";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("freep");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\freep\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Freep";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("suntimes");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\suntimes\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Suntimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("Techweb");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\Techweb\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Techweb";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("Tribune");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\Tribune\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Tribune";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("nytimes");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\nytimes\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Nytimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("BBC");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\BBC\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\BBC";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("reuters");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\reuters\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Reuters";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("Yahoo!");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\Yahoo!\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Yahoo!";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("sina");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\sina\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\Sina";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("人民网");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\人民网\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\人民网";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("网易");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\网易\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\网易";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("新华网");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\新华网\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\新华网";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("wb_Tengxun");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\wb_Tengxun\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\wb_Tengxun";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("wb_Sina");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\wb_Sina\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\wb_Sina";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("wb_Sohu");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\wb_Sohu\\";
		goldDir = "D:\\GQ-DEV\\JoS-NewsDataSource\\gold\\wb_Sohu";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		pstab.println();
		if (ps != null)
			ps.close();
		if (pstab != null)
			ps.close();
		if (psW != null)
			psW.close();
		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}

	/*
	// 20161116修改
	public static void main(String[] args) throws ParserException, IOException {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		EvaluatePagesWu evaluatePages = new EvaluatePagesWu();
		String subDir = "20161124";
		FileOutputStream fos = new FileOutputStream("D:\\GQ-DEV\\Results\\" + subDir + "\\fos.txt",
				false);
		PrintStream ps = new PrintStream(fos);
		FileOutputStream fostab = new FileOutputStream("D:\\GQ-DEV\\Results\\" + subDir + "\\fostab.txt",
				false);
		PrintStream pstab = new PrintStream(fostab);
		FileOutputStream fosW = new FileOutputStream(
				"D:\\GQ-DEV\\Results\\" + subDir + "fosW_detail.txt", false);
		PrintStream psW = new PrintStream(fosW);

		ps.println("cleanval-en");
		String evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\cleaneval-en\\";
		String goldDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\goldWu-URL";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("cleanval-zh");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\cleaneval-zh\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\zh\\gold-714-ANSI";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("bbc");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\bbc\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\bbc";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("freep");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\freep\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\freep";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("nypost");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\nypost\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\nypost";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("nytimes");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\nytimes\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\nytimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("reuters");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\reuters\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\reuters";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("suntimes");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\suntimes\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\suntimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);

		ps.println("techweb");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\techweb\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\techweb";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		ps.println("tribune");
		evalDir = "D:\\GQ-DEV\\Results\\" + subDir + "\\tribune\\";
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\tribune";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		
		pstab.println();
		if (ps != null)
			ps.close();
		if (pstab != null)
			ps.close();
		if (psW != null)
			psW.close();
		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}
	*/
	
	/* 20161116
	public static void main(String[] args) throws ParserException, IOException {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		EvaluatePagesWu evaluatePages = new EvaluatePagesWu();
		FileOutputStream fos = new FileOutputStream("D:\\GQ-DEV\\Results\\20161113\\" + "fos" + ".txt",
				false);
		PrintStream ps = new PrintStream(fos);
		FileOutputStream fostab = new FileOutputStream("D:\\GQ-DEV\\Results\\20161113\\" + "fostab" + ".txt",
				false);
		PrintStream pstab = new PrintStream(fostab);
		FileOutputStream fosW = new FileOutputStream(
				"D:\\GQ-DEV\\Results\\20161113\\" + "fosW" + "_detail.txt", false);
		PrintStream psW = new PrintStream(fosW);
		String sthresh = "";
		ps.println("cleanval-en");
		String evalDir = "D:\\GQ-DEV\\Results\\20161113\\cleaneval-en\\" + sthresh;
		String goldDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\goldWu-URL";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("cleanval-zh");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\cleaneval-zh\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\zh\\gold-714-ANSI";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("bbc");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\bbc\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\bbc";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("freep");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\freep\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\freep";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("nypost");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\nypost\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\nypost";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("nytimes");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\nytimes\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\nytimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("reuters");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\reuters\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\reuters";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("suntimes");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\suntimes\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\suntimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("techweb");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\techweb\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\techweb";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		ps.println("tribune");
		evalDir = "D:\\GQ-DEV\\Results\\20161113\\tribune\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\tribune";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.10);
		pstab.println();
		if (ps != null)
			ps.close();
		if (pstab != null)
			ps.close();
		if (psW != null)
			psW.close();
		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}
	*/
	
	/*
	public static void main(String[] args) throws ParserException, IOException {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		EvaluatePagesWu evaluatePages = new EvaluatePagesWu();
		FileOutputStream fos = new FileOutputStream("D:\\GQ-DEV\\Results\\20160709\\" + "fos" + ".txt",
				false);
		PrintStream ps = new PrintStream(fos);
		FileOutputStream fostab = new FileOutputStream("D:\\GQ-DEV\\Results\\20160709\\" + "fostab" + ".txt",
				false);
		PrintStream pstab = new PrintStream(fostab);
		FileOutputStream fosW = new FileOutputStream(
				"D:\\GQ-DEV\\Results\\20160709\\" + "fosW" + "_detail.txt", false);
		PrintStream psW = new PrintStream(fosW);
		String sthresh = "1.0";
		ps.println("cleanval-en");
		String evalDir = "D:\\GQ-DEV\\Results\\20160709\\cleaneval-en\\" + sthresh;
		String goldDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\en\\goldWu-URL";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("cleanval-zh");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\cleaneval-zh\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\cleaneval\\zh\\gold-714-ANSI";
		evaluatePages.evaluateZHWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("bbc");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\bbc\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\bbc";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("freep");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\freep\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\freep";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("nypost");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\nypost\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\nypost";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("nytimes");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\nytimes\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\nytimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("reuters");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\reuters\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\reuters";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("suntimes");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\suntimes\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\suntimes";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("techweb");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\techweb\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\techweb";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		ps.println("tribune");
		evalDir = "D:\\GQ-DEV\\Results\\20160709\\tribune\\" + sthresh;
		goldDir = "D:\\GQ-DEV\\cetr-dataset\\news\\gold\\tribune";
		evaluatePages.evaluateWu(evalDir, goldDir, ps, psW, pstab, 0.51);
		pstab.println();
		if (ps != null)
			ps.close();
		if (pstab != null)
			ps.close();
		if (psW != null)
			psW.close();
*/
		/*

		String filename = "E:\\研究\\抽取\\Software\\DataAll\\SCluster\\Smooth\\" + "fos";
		FileOutputStream fos = new FileOutputStream("E:\\研究\\抽取\\Software\\DataAll\\实验结果\\Smooth\\" + "fos" + ".txt",
				true);
		PrintStream ps = new PrintStream(fos);
		FileOutputStream fosW = new FileOutputStream(
				"E:\\研究\\抽取\\Software\\DataAll\\实验结果\\Smooth\\" + "fosW" + "_detail.txt", true);
		PrintStream psW = new PrintStream(fosW);

		String evalDir = filename + "\\CleanEval-en";
		String goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\CleanEval-en";
		evaluatePages.evaluate(evalDir, goldDir, ps, psW);
		*/
	/*
		Date endDate = new Date();
		System.out.printf("Total time-consuming is %s.\n", DateUtil.getDateDiff(startDate, endDate));
	}
	*/

/*	public static void main(String[] args) throws ParserException, IOException {
		System.out.println("Start running, please wait ...");
		Date startDate = new Date();
		EvaluatePages evaluatePages = new EvaluatePages();
	
		
	
        String[] types ={
        		"0","1","2","4","5"
        };
       
		String type = ""; //  ETPL ETPR TPLR ETPLR

            for(int i = 0; i<types.length;i++){
            	
            type = types[i];
        
			String filename = "E:\\研究\\抽取\\Software\\DataAll\\SCluster\\Smooth\\"+type;
			FileOutputStream fos = new FileOutputStream(
					"E:\\研究\\抽取\\Software\\DataAll\\实验结果\\Smooth\\"+type+".txt", true);
			PrintStream ps = new PrintStream(fos);
			FileOutputStream fosW = new FileOutputStream(
					"E:\\研究\\抽取\\Software\\DataAll\\实验结果\\Smooth\\"+type+"_detail.txt", true);
			PrintStream psW = new PrintStream(fosW);

			String evalDir = filename + "\\CleanEval-en";
	    	String goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\CleanEval-en";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);			
			
			evalDir = filename + "\\CleanEval-zh";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\CleanEval-zh";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Nypost";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Nypost";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Freep";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Freep";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Suntimes";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Suntimes";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Techweb";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Techweb";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Tribune";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Tribune";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Nytimes";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Nytimes";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\BBC";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\BBC";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);

			evalDir = filename + "\\Reuters";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Reuters";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
		
			evalDir = filename + "\\Yahoo!";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Yahoo!";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
				
			evalDir = filename + "\\Sina";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Sina";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);

			evalDir = filename + "\\人民网";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\人民网";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);

			evalDir = filename + "\\网易";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\网易";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);

			evalDir =  filename + "\\新华网";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\新华网";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\wb_Tengxun";
	    	goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\wb_Tengxun";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\wb_Sina";
	    	goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\wb_Sina";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\wb_Sohu";
	    	goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\wb_Sohu";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
           
            }
*/            
        
      
/*
       	
		    String type = "Type"; //  ETPL ETPR TPLR ETPLR
		    String[] types = {"TPL","TPR","TPLR","ETPL","ETPR","ETPLR"};
	
		    for(int i = 0; i<types.length;i++){
		    type = types[i];	
		    
			String filename = "E:\\研究\\抽取\\Software\\0.1-2.5\\Type\\"+type;
			FileOutputStream fos = new FileOutputStream(
					"E:\\研究\\抽取\\Software\\实验结果\\0.1-2.5\\Type\\"+type+"_result.txt", true);
			PrintStream ps = new PrintStream(fos);
			FileOutputStream fosW = new FileOutputStream(
					"E:\\研究\\抽取\\Software\\实验结果\\0.1-2.5\\Type\\"+type+"_detail_result.txt", true);
			PrintStream psW = new PrintStream(fosW);

				
			
			String evalDir = filename + "\\CleanEval-en";
	    	String goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\CleanEval-en";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);			
			
			evalDir = filename + "\\CleanEval-zh";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\CleanEval-zh";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Nypost";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Nypost";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Freep";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Freep";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Suntimes";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Suntimes";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Techweb";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Techweb";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Tribune";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Tribune";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\Nytimes";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Nytimes";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
			
			evalDir = filename + "\\BBC";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\BBC";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);

			evalDir = filename + "\\Reuters";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Reuters";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
		
			evalDir = filename + "\\Yahoo!";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Yahoo!";
			evaluatePages.evaluate(evalDir, goldDir, ps, psW);
				
			evalDir = filename + "\\Sina";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Sina";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);

			evalDir = filename + "\\人民网";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\人民网";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);

			evalDir = filename + "\\网易";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\网易";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);

			evalDir =  filename + "\\新华网";
			goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\新华网";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
       
			evalDir = filename + "\\Weibo";
	    	goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Weibo";
			evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);	
		    }
		    
		/*
		String type = "ETPR";
		double thresh =1.8;
		String filename = "E:\\研究\\抽取\\Graduate\\NewDataSource\\Type\\"
	    	+type+"\\"+String.valueOf(thresh)+"\\";
		FileOutputStream fos = new FileOutputStream(
				"E:\\研究\\抽取\\Graduate\\Result\\Type\\"+type+"\\"+String.valueOf(thresh)+".txt", true);
		PrintStream ps = new PrintStream(fos);
		FileOutputStream fosW = new FileOutputStream(
				"E:\\研究\\抽取\\Graduate\\Result\\Type\\"+type+"\\detail_"+String.valueOf(thresh)+".txt", true);
		PrintStream psW = new PrintStream(fosW);

					
		
		String evalDir = filename + "\\Weibo";
	    String goldDir = "E:\\研究\\抽取\\NewDataSource\\gold\\Weibo";
		evaluatePages.evaluateZH(evalDir, goldDir, ps, psW);
		*/
		
	
		
		
	}

