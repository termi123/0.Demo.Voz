package com.example.demo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class Helper {

	public static boolean writeLogFile;
	public static File logFile;
	
	
	@SuppressLint("NewApi")
	public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> task,
			T... params) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			task.execute(params);
		}
	}

	public static String escapeURL(String link) {
		try {
			String path = link;
			path = java.net.URLEncoder.encode(path, "utf8");
			path = path.replace("%3A", ":");
			path = path.replace("%2F", "/");
			path = path.replace("+", "%20");
			path = path.replace("%23", "#");
			path = path.replace("%3D", "=");
			return path;
		} catch (Exception e) {
			link = link.replaceAll("\\[", "%5B");
			link = link.replaceAll("\\]", "%5D");
			link = link.replaceAll("\\s", "%20");
		}
		return link;
	}

	public static void writeLog(String msg) {
		try {
			// if file doesnt exists, then create it
			if (!logFile.exists()) {
				logFile.createNewFile();
			}

			FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(msg);
			bw.close();
		} catch (Exception e) {
		}
	}

	public static void logError(Object errorClass, String msg, Exception e) {
		logError(errorClass.getClass().toString(), msg, e);
	}

	public static void logError(String errorClass, String msg, Exception e) {
		Log.e(errorClass, msg, e);

		if (writeLogFile && logFile != null) {
			String completeError = "\n" + "\n" + errorClass + " // " + msg
					+ " // " + e.getMessage() + "\n";
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			completeError += errors.toString() + "\n";

			writeLog(completeError);
		}
	}

	public static String getHTML(String url) throws IOException {
		url = escapeURL(url);

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();

		if (code != 200)
			System.out.print(code);

		String html = "";
		InputStream in = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			str.append(line);
		}
		in.close();
		html = str.toString();
		return html;
	}

	public static String getHTML(String url, CookieStore cs) throws IOException {
		url = escapeURL(url);

		// Create local HTTP context
		HttpContext localContext = new BasicHttpContext();
		// Bind custom cookie store to the local context
		if (cs != null)
			localContext.setAttribute(ClientContext.COOKIE_STORE, cs);

		HttpGet get = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(get, localContext);
		int code = response.getStatusLine().getStatusCode();

		if (code != 200)
			System.out.print(code);

		HttpEntity ent = response.getEntity();
		InputStream is = ent.getContent();

		String html = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder str = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			str.append(line);
		}
		is.close();
		html = str.toString();
		return html;
	}

	public static String getHTMLCORS(String url) throws IOException {
//		url = escapeURL(url);
		 Log.i("url  getHTMLCORS: ",url);
		HttpClient client = new DefaultHttpClient();
		((AbstractHttpClient) client)
				.addRequestInterceptor(new HttpRequestInterceptor() {
					public void process(final HttpRequest request,
							final HttpContext context) throws HttpException,
							IOException {
						// request.setHeader(HTTP.TARGET_HOST, "www.fakku.net");
					}
				});
		HttpGet request = new HttpGet(url);
		// request.setHeader("Referer", "http://www.fakku.net");
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		 Log.i("code : ",Integer.toString(code));
		
		if (code != 200)
			System.out.print(code);
		
		String html = "";
		InputStream in = response.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			str.append(line);
		}
		in.close();
		html = str.toString();
		return html;
	}

	public static void logInfo(String tag, String msg) {
		Log.i(tag, msg);

		if (writeLogFile && logFile != null) {
			String message = "\n\n" + tag + "\n" + msg;
			writeLog(message);
		}
	}

	public static String limitString(String s, int maxSize, String fill) {
		int sizeFill = fill.length();
		if (s.length() > maxSize) {
			return s.substring(0, maxSize - sizeFill) + fill;
		}
		return s;
	}

	public static File getCacheDir(Context context) {
		File file = null;
		SharedPreferences prefs = PreferenceManager

		.getDefaultSharedPreferences(context);
		String settingDir = prefs.getString("dir_download", "0");
		if (settingDir.equals(Constants.EXTERNAL_STORAGE + "")) {
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				file = new File(Environment.getExternalStorageDirectory()
						+ Constants.CACHE_DIRECTORY);
				boolean success = true;
				if (!file.exists()) {
					success = file.mkdirs();
				}

				if (!success)
					file = null;
			}
		}
		if (file == null)
			try {
				file = new File(Environment.getRootDirectory()
						+ Constants.CACHE_DIRECTORY);
			} catch (Exception e) {
				file = context.getDir("", Context.MODE_WORLD_WRITEABLE);
				file = new File(file, Constants.CACHE_DIRECTORY);
			}

		if (!file.exists()) {
			if (!file.mkdirs()) {
				file = context.getDir("", Context.MODE_WORLD_WRITEABLE);
				file = new File(file, Constants.CACHE_DIRECTORY);
				if (!file.exists())
					file.mkdirs();
			}
		}
		return file;
	}

	public static <T> List<List<T>> splitArrayList(List<T> array, int size) {
		List<List<T>> result = new ArrayList<List<T>>();

		for (int i = 0; i < size; i++) {
			List<T> list = new ArrayList<T>();
			result.add(list);
		}
		int c = 0;

		for (T t : array) {
			result.get(c++).add(t);
			if (c >= size)
				c = 0;
		}

		return result;
	}
}
