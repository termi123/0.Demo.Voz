package com.example.demo.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.example.demo.bean.VersionBean;
import com.example.demo.bean.vozCommentBean;
import com.example.demo.bean.vozPostBean;
import com.example.demo.bean.vozURLBean;
import com.example.demo.bean.vozUserBean;
import com.example.demo.hl.exception.ExceptionNotLoggedIn;
import com.example.demo.util.Constants;
import com.example.demo.util.Helper;
import com.example.demo.util.Utilities;
import com.google.gson.Gson;


public class vozConnection {
	private static CookieStore cookiesStore = null;

	public static boolean isConnected() {
		return cookiesStore != null;
	}

	public static void disconnect() {
		cookiesStore = null;
	}
	
	
	public static void connect(vozUserBean user) throws ClientProtocolException,
			IOException {
		if (cookiesStore != null) {
			user.setChecked(true);
			return;
		}
		boolean result = false;
		DefaultHttpClient httpclient = new DefaultHttpClient();

		HttpGet httpget = new HttpGet(Constants.SITELOGIN);

		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();

		Log.i("response", response.getStatusLine().toString());
		
		if (entity != null) {
			entity.consumeContent();
		}

//		HttpPost httpost = new HttpPost(Constants.SITELOGIN);
		HttpPost httpost = new HttpPost("http://vozforums.com/login.php?do=login");
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
//		nvps.add(new BasicNameValuePair("vb_login_username", user.getUser()));
		nvps.add(new BasicNameValuePair("vb_login_username", "termihana2"));
		nvps.add(new BasicNameValuePair("username", "termihana2"));
//		nvps.add(new BasicNameValuePair("vb_login_password", user.getPassword()));
		nvps.add(new BasicNameValuePair("vb_login_password", "termi"));
		nvps.add(new BasicNameValuePair("password", "termi"));
		
		nvps.add(new BasicNameValuePair("s", ""));
		nvps.add(new BasicNameValuePair("securitytoken", "guest"));
		nvps.add(new BasicNameValuePair("do", "login"));
//		nvps.add(new BasicNameValuePair("vb_login_md5password", Utilities.md5(user.getPassword())));
		nvps.add(new BasicNameValuePair("vb_login_md5password", Utilities.md5("termihana2")));
//        nvps.add(new BasicNameValuePair("vb_login_md5password_utf", Utilities.md5(user.getPassword())));
        nvps.add(new BasicNameValuePair("vb_login_md5password_utf", Utilities.md5("termi")));
        
//        nvps.add(new BasicNameValuePair("vb_login_username", user.getUser()));
//        nvps.add(new BasicNameValuePair("vb_login_password", ""));
//		nvps.add(new BasicNameValuePair("cookieuser", "1"));
        
//        nvps.add(new BasicNameValuePair("url", "index.php"));
//        nvps.add(new BasicNameValuePair("vb_login_md5password", Utilities.md5(user.getPassword())));
//        nvps.add(new BasicNameValuePair("vb_login_md5password_utf", Utilities.md5(user.getPassword())));
//        nvps.add(new BasicNameValuePair("vb_login_md5password", user.getPassword()));
//        nvps.add(new BasicNameValuePair("vb_login_md5password_utf", user.getPassword()));
		
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		response = httpclient.execute(httpost);
		entity = response.getEntity();
		
		Log.i("response httpost", response.getStatusLine().toString());
		
		CookieStore cookies = httpclient.getCookieStore();
		if (cookies.getCookies().isEmpty()) {
			Log.d(new vozConnection().getClass().toString(),
					"Post logon cookies: None");
		} else {
			for (int i = 0; i < cookies.getCookies().size(); i++) {
				if (cookies.getCookies().get(i).getName()
						.equalsIgnoreCase("fakku_sid")) {
					result = true;
				}
			}
		}
		if (!result)
			cookiesStore = null;
		else {
			cookiesStore = cookies;

			InputStream is = null;
			try {
				is = entity.getContent();
				String html = "";
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder str = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
				html = str.toString();

				Document doc = Jsoup.parse(html);
				String url = doc.select("a#user-menu-favorites").first()
						.select("a").first().attr("href").substring(7);
				url = url.substring(0, url.indexOf("/"));
				user.setUrlUser(url);
			} catch (Exception ex) {
			} finally {
				if (is != null)
					is.close();
			}
		}
		user.setChecked(result);
	}

	public static void transaction(String url) throws ExceptionNotLoggedIn,
			IOException {
		if (cookiesStore == null)
			throw new ExceptionNotLoggedIn();

		String html = Helper.getHTML(url, cookiesStore);

		if (html.contains("Please enter your username and password to login"))
			throw new ExceptionNotLoggedIn();
	}

	public static LinkedList<vozCommentBean> parseComments(String url)
			throws IOException, URISyntaxException {

		String html = Helper.getHTMLCORS(url);

		Document doc = Jsoup.parse(html);

		return parseHTMLtoComments(doc.select("div.comment-row"));
	}

	public static LinkedList<vozCommentBean> parseHTMLtoComments(Elements comments) {
		LinkedList<vozCommentBean> result = new LinkedList<vozCommentBean>();

		for (Element comment : comments) {
			int level = 0;
			if (comment.hasClass("comment-tree")) {
				level = 2;
			} else if (comment.hasClass("comment-reply")) {
				level = 1;
			} else if (comment.hasClass("comment-")) {
				level = 0;
			} else {
				continue;
			}

			vozCommentBean c = new vozCommentBean();
			c.setLevel(level);

			c.setId(comment.select("a").first().id());
			vozURLBean user = new vozURLBean();
			Element userCreator = comment.select("[itemprop=creator]").first();
			user.setUrl(Constants.SITEROOT + userCreator.attr("href"));
			user.setDescription(userCreator.text());

			c.setUser(user);

			c.setDate(comment.select("[itemprop=commentTime]").first().text());

			Element likeA = comment.select("a.arrow").select(".like").first();
			Element disLikeA = comment.select("a.arrow").select(".dislike")
					.first();

			c.setUrlLike(Constants.SITEROOT + likeA.attr("href"));
			c.setUrlDislike(Constants.SITEROOT + disLikeA.attr("href"));

			if (likeA.hasClass("selected")) {
				c.setSelectLike(1);
			} else if (disLikeA.hasClass("selected")) {
				c.setSelectLike(-1);
			}

			Element rank = comment.select("i").first();
			if (rank != null)
				c.setRank(Integer.parseInt(rank.text().replace("+", "")
						.replace(" points", "")));

			c.setComment(comment.select("div.comment_text").first().html());
			result.add(c);
		}

		return result;
	}

	public static VersionBean getLastversion() throws IOException {
		VersionBean result = null;
		String html = Helper.getHTML(Constants.UPDATE_SERVICE);
		if (!html.equals("null")) {
			Gson gson = new Gson();
			result = gson.fromJson(html, VersionBean.class);
		}
		return result;
	}

	// ////////////////////////////////////parse HTML Catalog
	public static LinkedList<vozPostBean> parseHTMLCatalog(String url)
			throws IOException, URISyntaxException {
		LinkedList<vozPostBean> result = new LinkedList<vozPostBean>();
		 Log.i("url parse : ",url);
		String html = Helper.getHTMLCORS(url);
//		 Log.i("html : ",html);

		Document doc = Jsoup.parse(html);
		// Document doc =
		// Jsoup.connect("http://i-edc.com/category/every-day-carry-blog/kinh-nghiem-du-lich-phuot/").get();
		// Helper.logInfo("Document : " + url, doc.toString());
//		 Log.i("Document : ",doc.toString());

		// Elements elements = doc.select(".content-row");
		// Elements elements = doc.select("div#main-products");
//		 #threadbits_forum_33"
//		Elements elements = doc.select("tbody#threadbits_forum_17");
		Elements elements = doc.select("table.tborder");
		 Log.i("Elements : ",elements.toString());
		// Helper.logInfo("Elements : ", elements.toString());

		for (Element e : elements) {
			// if(e.hasClass("manga")||e.hasClass("doujinshi")){
//			if (e.hasClass("clearfix")) {
				 Log.i("Element e ", e.toString());
				vozPostBean bean = new vozPostBean();
				
//				Elements ef1 = e.select("tr");
//				Log.i("ef1 tr : ",ef1.toString());
//				Log.i("title: ",ef1.select("td").get(1).attr("vozsticky").toString());
//				Elements ef2 = ef1.select("td");
				Log.i("a:",e.select("a").get(2).text());
//				Log.i("href:",e.select("a").get(2).attr("href"));
//				bean.setTitle(e.select("vozsticky").toString());
//				ef1.get(1).select("title");
//				Log.i("title : ",ef1.attr("title").toString());
				
//				String link = "";
//				if(ef1.hasClass("vozsticky")){
//					link = ef1.select(".vozsticky").first().attr("href");
//				}
//				Log.i("link", link);
//				String result1 = ef1.get(1).attr("vozsticky").text();
//				Log.i("result1", result1);
				
				// url
				// bean.setUrl(Constants.SITEROOT +
				// e.select("a").first().attr("href"));
//				bean.setUrl(e.select("a").first().attr("href"));
				// Log.i("URL : ",e.select("a").first().attr("href"));

				// Images
//				Elements elementsAux = e.select("img");
//				String aux = "";
//				if (elementsAux.hasClass("size-full")) {
//					aux = elementsAux.select(".size-full").first().attr("src");
//				} else if (elementsAux.hasClass("blog-thumb")) {
//					aux = elementsAux.select(".blog-thumb").first().attr("src");
//				}
				// bean.setUrlImageTitle(aux);
				// bean.setImageServer(aux);
				// .split("/thumbs/")[0]+"/images/");

				// Log.i("IMG Image Title: ",bean.getUrlImageTitle());
				// Look for the next image tag
//				bean.setUrlImagePage(aux);
				// elementsAux.select(".sample").first().attr("src"));
				// Log.i("IMG Image Title: ",bean.getUrlImagePage());

				// title
				// bean.setTitle(e.select(".content-meta").first().select("h2").first().select("a").first().text());
//				String tit = e.select(".title").first().select("h2").first()
//						.select("a").first().text();
//				// Log.i("Title : ",tit);
//				bean.setTitle(tit);

				// elementsAux = e.select("div.left").select("a");

				// address
//				String add = e.select(".res-common-add").first().text();
//				bean.setSerie(add);

				// artist
				// bean.setArtist(parseURLBean(elementsAux.get(1)));

				// elementsAux = e.select("div.right").select("a");
				// language
				// bean.setLanguage(parseURLBean(elementsAux.get(0)));
				// translator
				// if(elementsAux.size()>1)
				// bean.setTranslator(parseURLBean(elementsAux.get(1)));

				// description
				// Element description = e.select(".row.short.small").first();
				// description.select("h3").remove();

				// Element des = e.select("p").first();
				// des.re(des.select(".meta-info"));
				// String des =
				// e.select("p").text().select("p").first().remove();
				// Log.i("Meta Info : ",des.select(".meta-info").text());
//				Element des = e.select(".res-common-price").first();
				// Log.i("Description : ",des.text());
//				bean.setDescription(des.html());

				// tags
				// List<URLBean> lstTags = new ArrayList<URLBean>();
				//
				// try {
				// elementsAux =
				// e.select(".row.short.small").last().select("a");
				//
				// for (Element tag : elementsAux) {
				// lstTags.add(parseURLBean(tag));
				// }
				// }catch (Exception ex){}
				//
				// bean.setLstTags(lstTags);

				result.add(bean);
				// }else{
				// Log.i("Check blogpost: ", "NONE");
//			}
		}

		return result;
	}

	public static LinkedList<vozPostBean> parseHTMLFavorite(String url)
			throws IOException, URISyntaxException {
		LinkedList<vozPostBean> result = new LinkedList<vozPostBean>();

		String html = Helper.getHTMLCORS(url);

		Document doc = Jsoup.parse(html);

		Helper.logInfo("parseHTMLFavorite : " + url, html);

		Elements favorites = doc.select(".favorite");
		for (Element favorite : favorites) {
			vozPostBean bean = new vozPostBean();

			// Images
			Element img = favorite.select("img").first();
//			bean.setUrlImageTitle(img.attr("src"));
//
			bean.setTitle(img.attr("alt"));

			String aux = favorite.select(".cover").first().attr("href");
			bean.setUrl(Constants.SITEROOT + aux);
//			bean.setImageServer(aux.split("/thumbs/")[0] + "/images/");

			result.add(bean);
		}

		return result;
	}

	// ////////////////////////////////////parse HTML Doujin
	public static void parseHTMLDoujin(vozPostBean bean) throws IOException {
		String url = bean.getUrl();
		// Log.i("url", url);

		String html = Helper.getHTML(url, cookiesStore);
		Log.i("html", html);
		// Helper.logInfo("parseHTMLDoujin : " + url, html);

		Document doc = Jsoup.parse(html);
		// Log.i("doc", doc.toString());

		// bean.setAddedInFavorite(!html.contains("Add To Favorites"));

		Elements elements = doc.select("#main-products");
		Log.i("elements", elements.toString());

		// int idx = 0;

		// Series
		// Element el = elements.get(idx++).select("a").first();
		// bean.setSerie(parseURLBean(el));

		// Artist
		// el = elements.get(idx++).select("a").first();
		// bean.setArtist(parseURLBean(el));
		// Translator
		// if(elements.size()==8){
		// el = elements.get(idx++).select("a").first();
		// bean.setTranslator(parseURLBean(el));
		// }
		// Uploader
		// el = elements.get(idx).select("a").first();
		// bean.setUploader(parseUserBean(el));
		// el = elements.get(idx++).select(".right").first();
		// el.select("a").remove();
		// bean.setDate(el.text());
		// Language
		// el = elements.get(idx++).select("a").first();
		// bean.setLanguage(parseURLBean(el));

		// Qty Pages
		// el = elements.get(idx++).select(".right").first();
		// int c = 0;
		// try {
		// c = Integer.parseInt(el.text().split(" ")[0]);
		// } catch (Exception e) {}
		//
		// bean.setQtyPages(c);
		//
		// el = elements.get(idx++).select(".right").first();
		// bean.setDescription(el.html());
		//
		// // tags
		// List<URLBean> lstTags = new ArrayList<URLBean>();
		//
		// try{
		// elements = elements.get(idx++).select("a");
		//
		// for (int i = 0; i<=elements.size()-2;i++){
		// lstTags.add(parseURLBean(elements.get(i)));
		// }
		// }catch (Exception e){}
		//
		// // bean.setLstTags(lstTags);
		//
		// URL
		String title = "";
		// elements.select(".title").first().attr("href");
		// el.removeAttr("a");
		// el.removeClass(".raquo");
		// String aux = el.attr("href");
		// bean.setUrl(aux);
		// Log.i("setUrl", aux);

		// Log.i("setTitle", title);
		bean.setTitle(title);
		//

		// Image
		// el = doc.select(".breadcrumbs a").last();
		String aux = "";
		// = Constants.SITEROOT + el.attr("href");
		Elements elementsAux = elements.select("img");
		if (elementsAux.hasClass("blog-thumb")) {
			aux = elementsAux.select(".blog-thumb").first().attr("src");
		}
		// aux = doc.select(".post-thumbnail").first().attr("src");
//		Log.i("aux", aux);
//		bean.setUrlImageTitle(aux);

//		Element e = elements.select(".res-common-add").first();
//		bean.setDescription(e.toString());
		// bean.setImageServer(aux.split("/thumbs/")[0]+"/images/");
		//
		// try{
		// Elements comments = doc.select("div.comment-row");
		// Elements topComments = new Elements();
		// Elements recentComments = new Elements();
		// for (Element comment : comments){
		// if(comment.parent().hasClass("ajax-container")){
		// recentComments.add(comment);
		// }else
		// topComments.add(comment);
		// }
		// bean.setLstTopComments(parseHTMLtoComments(topComments));
		// bean.setLstRecentComments(parseHTMLtoComments(recentComments));
		// }catch(Exception e){}

		bean.setCompleted(true);
		// Log.i("active", Boolean.toString(bean.isCompleted()));
		// Log.i("active af", bean.toString());
	}

	public static String imageServerUrl(String url) throws IOException {
		String result = null;

		String html = Helper.getHTMLCORS(url + "/read#page=1");

		Helper.logInfo("parseHTMLDoujin / imageServer : " + url
				+ "/read#page=1", html);

		String token = "function imgpath(x)";
		int idxStart = html.indexOf(token, 0) + token.length();
		token = "return";
		idxStart = html.indexOf(token, idxStart) + token.length();
		token = "'";
		idxStart = html.indexOf(token, idxStart) + token.length();
		int idxEnd = html.indexOf(token, idxStart) + token.length();
		result = html.substring(idxStart, idxEnd - 1);
		return result;
	}

	public static LinkedList<vozURLBean> parseHTMLSeriesList(String url)
			throws IOException {
		LinkedList<vozURLBean> result = new LinkedList<vozURLBean>();
		String html = Helper.getHTMLCORS(url);

		String token = "attribute-row";
		String[] sections = html.split(token);

		for (int i = 1; i < sections.length; i++) {
			String section = sections[i];
			token = "href=\"";

			int idxStart = section.indexOf(token) + token.length();
			int idxEnd = section.indexOf("\"", idxStart);

			vozURLBean b = new vozURLBean();
			b.setUrl(Constants.SITEROOT + section.substring(idxStart, idxEnd));

			token = ">";

			idxStart = section.indexOf(token) + token.length();
			idxEnd = section.indexOf("<", idxStart);

			b.setDescription(section.substring(idxStart, idxEnd));

			// Log.i("HTMLParser", b.toString());

			result.add(b);
		}

		return result;
	}

	private static vozURLBean parseURLBean(Element a) {
		vozURLBean b = new vozURLBean();

		b.setDescription(((Element) a).text());
		b.setUrl(Constants.SITEROOT + a.attr("href"));

		return b;
	}

	private static vozUserBean parseUserBean(Element a) {
		vozUserBean b = new vozUserBean();
		b.setUser(a.text());

		String href = a.attr("href");
		String token = "/users/";
		int idxStart = href.indexOf(token) + token.length();
		String s = href.substring(idxStart);

		b.setUrlUser(s.trim());

		return b;
	}
}
