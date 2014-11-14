package com.example.demo.core;

import com.example.demo.bean.vozPostBean;
import com.example.demo.bean.vozUserBean;
import com.example.demo.util.Constants;
import com.example.demo.util.Helper;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class vozDroidApplication extends Application {

	private vozPostBean current = null;
	private vozUserBean settingBean = null;
    private boolean remindMeLater = false;
	
	public vozUserBean getSettingBean() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
		if(settingBean==null){
			DataBaseHandler db = new DataBaseHandler(this.getApplicationContext());
			settingBean = db.getSetting();

			if(settingBean==null)
				settingBean = db.addSetting();
		}
		settingBean.setUrlUser(prefs.getString("url_user", null));
		return settingBean;
	}

	public void setSettingBean(vozUserBean settingBean) {
		this.settingBean = settingBean;
	}

	public vozPostBean getCurrent() {
		return current;
	}

	public void setCurrent(vozPostBean current) {
		this.current = current;
	}

	public String getTitle(int nroPage, String title){
		return title + " #" + nroPage;
	}
	public String getUrl(int nroPage, String url) {
        if (nroPage > 1) {
            return url + Constants.PAGE + nroPage;
        }
        return url;
    }
    public String getRelatedUrl(int nroPage, String url) {
        return url + "/" + (--nroPage*10);
    }
	public String getUrlFavorite(int numPage, String user) {
		return Helper.escapeURL(Constants.SITEFAVORITE.replace("@user", user.toLowerCase()).replace("@numpage", numPage + ""));
	}

    public boolean isRemindMeLater() {
        return remindMeLater;
    }

    public void setRemindMeLater(boolean remindMeLater) {
        this.remindMeLater = remindMeLater;
    }
}
