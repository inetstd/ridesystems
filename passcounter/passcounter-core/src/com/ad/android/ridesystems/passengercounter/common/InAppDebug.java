package com.ad.android.ridesystems.passengercounter.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;


public class InAppDebug {	
	public static String FILE_NAME_TPL = "log.txt";


	private static String getFileName() {
		return DateFormat.format("dd-MM", new Date()) + "-" + FILE_NAME_TPL;
	}

	/**
	 * Randomly try to delete old logs
	 * @param ctx
	 */
	public static void tryCleanOldLogs(Context ctx) {
		if (Math.random() < 0.1f || true) {
			cleanOldLogs(ctx);
		}
	}
	
	/**
	 * delete all logs except logs for today
	 * @param ctx
	 */
	private static void cleanOldLogs(Context ctx) {
		File appRoot = ctx.getFilesDir();
		for (File f: appRoot.listFiles()) {
			if  (f.getName().contains(FILE_NAME_TPL) && !f.getName().equals(getFileName())) {
				Log.i("delete old log", f.getName());
				ctx.deleteFile(f.getName());
			}
				
		}		
	}

	public static void log(Context ctx, String msg) {										
		tryCleanOldLogs(ctx);
		
		try {					
			String date = new SimpleDateFormat(Config.C_LOG_DATE_FORMAT).format(new Date());

			FileOutputStream fos = ctx.openFileOutput(getFileName(), Context.MODE_APPEND);		
			PrintWriter out = new PrintWriter(fos);

			out.append(date + " - " + msg + "\n");
			out.flush();
			out.close();
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

	public static String read(Context ctx) {
		try {
			FileInputStream inputStream = ctx.openFileInput(getFileName());	
			InputStreamReader isr = new InputStreamReader(inputStream, "UTF8");				
			StringBuffer buffer = new StringBuffer();
			Reader in = new BufferedReader(isr);
			int ch;
			while ((ch = in.read()) > -1) {
				buffer.append((char)ch);
			}
			in.close();
			return buffer.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<empty>";
	}

	public static void clean(Context ctx) {
		ctx.deleteFile(getFileName());
		log(ctx, "Logs was cleaned");

	}

}
