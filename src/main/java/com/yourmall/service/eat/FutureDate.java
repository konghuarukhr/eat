package com.yourmall.service.eat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FutureDate {
	public static String[] getWeek() {
		String[] week = new String[7];
		
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		now.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - now.get(Calendar.DAY_OF_WEEK));
		for (int i = 0; i < 7; i++) {
			week[i] = df.format(now.getTime()) + " (" + getDayName(now) + ")";
			now.add(Calendar.DAY_OF_WEEK, 1);
		}
		return week;
	}
	
	private static String getDayName(Calendar day) {
		switch (day.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY: return "周日";
		case Calendar.MONDAY: return "周一";
		case Calendar.TUESDAY: return "周二";
		case Calendar.WEDNESDAY: return "周三";
		case Calendar.THURSDAY: return "周四";
		case Calendar.FRIDAY: return "周五";
		case Calendar.SATURDAY: return "周六";
		}
		return null;
	}
	
	public static String[] getWeek(String day) throws ParseException {
		String[] week = new String[7];
		
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		now.setTime(df.parse(day));
		now.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - now.get(Calendar.DAY_OF_WEEK));
		for (int i = 0; i < 7; i++) {
			week[i] = df.format(now.getTime());
			now.add(Calendar.DAY_OF_WEEK, 1);
		}
		return week;
	}
	
	public static String getDayName(int i) {
		switch (i) {
		case 0: return "周日";
		case 1: return "周一";
		case 2: return "周二";
		case 3: return "周三";
		case 4: return "周四";
		case 5: return "周五";
		case 6: return "周六";
		}
		return null;
	}
}
