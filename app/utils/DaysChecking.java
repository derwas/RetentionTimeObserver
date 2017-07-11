package utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


import play.Logger;


public class DaysChecking {

	public static String getInterval(long durationInMS){

		Calendar c2 = Calendar.getInstance() ;
		c2.setTime(new Date(c2.getTimeInMillis() - (c2.get(c2.HOUR) * 3600 * 1000) - (c2.get(c2.MINUTE) * 60 *1000) - (c2.get(c2.SECOND) * 1000) - (c2.get(c2.MILLISECOND))));

		Calendar c = Calendar.getInstance();
		c.setTime(new Date (c2.getTimeInMillis()-durationInMS));
		
		String result = "";

		result = c.get(c.YEAR) +"-"+(c.get(c.MONTH)+1)+"-"+c.get(c.DAY_OF_MONTH)+"T"+c.get(c.HOUR) +":"+c.get(c.MINUTE)
		+"/"
		+c2.get(c2.YEAR) +"-"+(c2.get(c2.MONTH)+1)+"-"+c2.get(c2.DAY_OF_MONTH)+"T"+c2.get(c2.HOUR) +":"+c2.get(c2.MINUTE);
		return result;
	}
	
	public static String durationAsString(long duration){
		String result ="";
		String separator ="";
		
		int dd = (int) (duration / (24 *60 *60 * 1000));
		long remains = duration - dd * 24 * 60 *60 *1000;
		int hh = (int) (remains / (60 *60 * 1000));
		remains = remains - hh * 60 *60 *60 *1000;
		int mm = (int) (remains / (60 * 1000));
		remains = remains - mm *60 *60 *1000;
		int ss = (int) (remains / (1000));
		
		if (dd==1){
			result = result + " 1 day"; 
			separator =",";
		}
		if(dd > 1){
			result = result + " "+dd+" days"; 
			separator = ",";
		}
		
		if (hh==1){
			result = result +separator+ " 1 hour"; 
			separator =",";
		}
		if(hh > 1){
			result = result +separator+ " "+hh+" hours"; 
			separator = ",";
		}
		
		if (mm==1){
			result = result +separator+ " 1 minute"; 
			separator =",";
		}
		if(mm > 1){
			result = result +separator+ " "+mm+" minutes"; 
			separator = ",";
		}
		
		if (ss==1){
			result = result +separator+ " 1 second"; 
			separator =",";
		}
		if(ss > 1){
			result = result +separator+ " "+ss+" seconds"; 
			separator = ",";
		}
		return result;
	}
	
	public static String durationHoursAsString(long duration){
		String result ="";
		String separator ="";
		
		int dd = (int) (duration / (24 *60 *60 * 1000));
		long remains = duration - dd * 24 * 60 *60 *1000;
		int hh = (int) (remains / (60 *60 * 1000));
		remains = remains - hh * 60 *60 *60 *1000;
		int mm = (int) (remains / (60 * 1000));
		remains = remains - mm *60 *60 *1000;
		int ss = (int) (remains / (1000));
		hh = hh + dd * 24;

		if (hh==1){
			result = result +separator+ " 1 hour"; 
			separator =",";
		}
		if(hh > 1){
			result = result +separator+ " "+hh+" hours"; 
			separator = ",";
		}
		
		if (mm==1){
			result = result +separator+ " 1 minute"; 
			separator =",";
		}
		if(mm > 1){
			result = result +separator+ " "+mm+" minutes"; 
			separator = ",";
		}
		
		if (ss==1){
			result = result +separator+ " 1 second"; 
			separator =",";
		}
		if(ss > 1){
			result = result +separator+ " "+ss+" seconds"; 
			separator = ",";
		}
		return result;
	}
	

}
