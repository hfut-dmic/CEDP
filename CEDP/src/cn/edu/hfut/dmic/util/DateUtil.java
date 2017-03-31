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
 *    CEPRWrapper1D.java
 *    Copyright (C) 2012 Gongqing Wu
 *
 */

package cn.edu.hfut.dmic.util;

import java.util.Date;

// import java.util.GregorianCalendar;

/**
 * Class for processing Date Classes.
 * 
 * @author Gongqing Wu (wugq@hfut.edu.cn)
 * @version $Version: 1.0 $
 */
public class DateUtil {

	/**
	 * Calculate the difference between two Date objects.
	 * 
	 * @param startDate
	 *            starting Date object
	 * @param endDate
	 *            ending Date object
	 * @return the difference between startDate and endDate
	 */
	public static String getDateDiff(Date startDate, Date endDate) {
		String s;
		long timeDiff = endDate.getTime() - startDate.getTime();
		int days;
		int hours;
		int minutes;
		int seconds;
		int milliseconds;

		days = (int) (timeDiff / (24 * 60 * 60 * 1000));
		timeDiff %= 24 * 60 * 60 * 1000;
		hours = (int) (timeDiff / (60 * 60 * 1000));
		timeDiff %= 60 * 60 * 1000;
		minutes = (int) (timeDiff / (60 * 1000));
		timeDiff %= 60 * 1000;
		seconds = (int) (timeDiff / 1000);
		timeDiff %= 1000;
		milliseconds = (int) (timeDiff);

		s = String.format("%d days %d hours %d minutes %d seconds %d milliseconds", days, hours, minutes, seconds,
				milliseconds);
		return s;
	}

	// @SuppressWarnings("deprecation")
	public static void main(String[] options) {
		// GregorianCalendar gregorianCalendar = new GregorianCalendar();
		// Date dt1 = gregorianCalendar.getTime();
		// Date dt1 = new Date(2008, 2, 29, 8, 23, 55);
		Date dt1 = new Date();
		int j = 0;
		for (int i = 0; i < 1000000000; i++) {
			j++;
		}
		// Date dt2 = new Date(2008, 2, 30, 9, 33, 23);
		Date dt2 = new Date();
		// gregorianCalendar = new GregorianCalendar();
		// Date dt2 = gregorianCalendar.getTime();

		System.out.printf("%d%s", j, getDateDiff(dt1, dt2));
	}
}
