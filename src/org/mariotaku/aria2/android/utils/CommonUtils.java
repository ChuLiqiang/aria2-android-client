package org.mariotaku.aria2.android.utils;

import java.io.File;
import java.text.DecimalFormat;

public class CommonUtils {

	public static String formatSpeedString(String src) {
		if (src == null) return "0 B";
		int speed = 0;
		String result = "";
		try {
			speed = Integer.parseInt(src);
		} catch (NumberFormatException e) {
			return src;
		}
		if (speed < 1024) {
			result = speed + " B";
		} else if (speed > 1024 && speed < 1024 * 1024) {
			result = new DecimalFormat("###.##").format(((double) speed) / 1024) + " KB";
		} else if (speed > 1024 * 1024 && speed < 1024 * 1024 * 1024){
			result = new DecimalFormat("###.##").format(((double) speed) / 1024 / 1024) + " MB";
		} else {
			result = new DecimalFormat("###.##").format(((double) speed) / 1024 / 1024 / 1024) + " GB";
		}
			

		return result;
	}
	
	public static String formatSizeString(String src) {
		return formatSpeedString(src);
	}
	
	public static String getFileNameFromPath(String path)
	{
		File userFile = new File(path);
		String filename = userFile.getName();
		return filename;
	}
	
	
	

}
