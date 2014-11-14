package com.example.demo.fragment;

import java.util.LinkedList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.example.demo.adapter.MenuListAdapter;
import com.example.demo.bean.vozURLBean;
import com.example.demo.component.ActionImageButton;
import com.example.demo.util.Constants;
import com.example.demo.util.Helper;
import com.example.demo.voz.MainActivity;
import com.example.demo.voz.R;

public class MenuListFragment extends SherlockListFragment {

	private LinkedList<vozURLBean> lstURL;
	private View mFormView;
	private View mStatusView;
	private View view;
	int nroPage = 1;
	int currentList = 0;
	int typeView;
	public final static int BROWSER_MANGA = 0;
	public final static int BROWSER_DOUJIN = 1;
	public final static int TYPE_LIST_TAGS = 0;
	public final static int TYPE_LIST_SERIES = 1;

	public void setMainActivity(MainActivity mainActivity) {
	}

	private View findViewById(int resource) {
		return view.findViewById(resource);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final Context contextThemeWrapper = new ContextThemeWrapper(
				getActivity(), com.actionbarsherlock.R.style.Sherlock___Theme);
		LayoutInflater localInflater = inflater
				.cloneInContext(contextThemeWrapper);

		view = localInflater.inflate(R.layout.fragment_menu, container, false);

		mFormView = findViewById(R.id.view_form);
		mStatusView = findViewById(R.id.view_status);
		findViewById(R.id.ll);
		ActionImageButton btnPreviousPage = (ActionImageButton) findViewById(R.id.btnPreviousPage);
		ActionImageButton btnNextPage = (ActionImageButton) findViewById(R.id.btnNextPage);

		btnPreviousPage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				previousPage();
			}
		});

		btnNextPage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				nextPage();
			}
		});

		createMainMenu();
		return view;
	}

	// ////////////////////////////////////Button event
	public void nextPage() {
		nroPage++;
		loadPage();
		CharSequence text = "Page " + nroPage;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getActivity(), text, duration);
		toast.show();
	}

	public void previousPage() {
		if (nroPage - 1 == 0) {

			CharSequence text = "There aren't more pages.";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(getActivity(), text, duration);
			toast.show();
		} else {
			nroPage--;
			loadPage();
			CharSequence text = "Page " + nroPage;
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(getActivity(), text, duration);
			toast.show();
		}
	}

	@SuppressLint("NewApi")
	private void loadPage() {
		TextView tvPage = (TextView) findViewById(R.id.tvPage);
		tvPage.setText("Page " + nroPage);
	}

	// ////////////////////////////////////CreateMenu
	public void createMainMenu() {
		String[] lstMainMenu = getActivity().getResources().getStringArray(
				R.array.main_menu);
		int[] lstIcons = new int[] {
				R.drawable.home,
				R.drawable.navigation_forward_inverse,
				R.drawable.rating_important_inverse,
				R.drawable.rating_important_inverse,
				R.drawable.rating_important_inverse,
				// R.drawable.icon_hotel,
				// R.drawable.icon_att,
				// R.drawable.icon_night,
				// R.drawable.icon_shop,
				// R.drawable.icon_ticket,
				R.drawable.av_upload_inverse,
				R.drawable.action_settings_inverse };
		lstURL = new LinkedList<vozURLBean>();

		for (int i = 0; i < lstMainMenu.length; i++) {
			vozURLBean bean = new vozURLBean(lstMainMenu[i]);
			bean.setIcon(lstIcons[i]);
			lstURL.add(bean);
		}
		this.setListAdapter(new MenuListAdapter(this.getActivity(),
				R.layout.row_menu, 0, lstURL, true));
	}

	// ////////////////////////////////////Menu Item event
	@SuppressLint("NewApi")
	public void onListItemClick(ListView l, View v, int position, long id) {
		vozURLBean bean = lstURL.get(position);
		if (position == 0) {
			Intent itMain = new Intent(getActivity(), MainActivity.class);
			itMain.putExtra(MainActivity.INTENT_VAR_CURRENT_CONTENT,
					MainActivity.MAIN_LIST);
			getActivity().startActivityForResult(itMain, 1);

		} else if (bean.getDescription().equals("My favorites")) {
			Toast.makeText(getActivity(), "My favorites", Toast.LENGTH_LONG)
					.show();
			// Intent itMain = new Intent(getActivity(), MainActivity.class);
			// itMain.putExtra(MainActivity.INTENT_VAR_CURRENT_CONTENT,
			// MainActivity.DOUJIN_LIST);
			// getActivity().startActivityForResult(itMain, 1);

		} else if (bean.getDescription().equals("F33 - Điểm báo")) {
			Toast.makeText(getActivity(), "F33 - Điểm báo", Toast.LENGTH_LONG)
					.show();
			Intent itMain = new Intent(getActivity(), MainActivity.class);
			itMain.putExtra(MainActivity.INTENT_VAR_CURRENT_CONTENT,
					MainActivity.VOZ_LIST);
			itMain.putExtra(MainActivity.INTENT_VAR_URL, Constants.F33);
			itMain.putExtra(MainActivity.INTENT_VAR_TITLE,
					MainActivity.TITLE_F33);
			getActivity().startActivityForResult(itMain, 1);

		} else if (bean.getDescription().equals("F17 - Chuyện trò linh tinh™")) {
			Toast.makeText(getActivity(), "F17 - Chuyện trò linh tinh™",
					Toast.LENGTH_LONG).show();
			Intent itMain = new Intent(getActivity(), MainActivity.class);
			itMain.putExtra(MainActivity.INTENT_VAR_CURRENT_CONTENT,
					MainActivity.VOZ_LIST);
			itMain.putExtra(MainActivity.INTENT_VAR_URL, Constants.F17);
			itMain.putExtra(MainActivity.INTENT_VAR_TITLE,
					MainActivity.TITLE_F17);
			getActivity().startActivityForResult(itMain, 1);

		} else if (bean.getDescription().startsWith("Sign in")) {
			Intent itMain = new Intent(getActivity(), MainActivity.class);
			itMain.putExtra(MainActivity.INTENT_VAR_CURRENT_CONTENT,
					MainActivity.VOZ_LOGIN);
			getActivity().startActivityForResult(itMain, 1);
			//
		} else if (bean.getDescription().equals("Preferences")) {
			// Intent itPreference = new Intent(this.getActivity(),
			// PreferencesActivity.class);
			// getActivity().startActivity(itPreference);

		} else if (bean.getDescription().equals("Logout")) {
			// UserBean sb = app.getSettingBean();
			// sb.setChecked(false);
			// new DataBaseHandler(getActivity()).updateSetting(sb);
			// app.setSettingBean(null);
			// FakkuConnection.disconnect();
			// Toast.makeText(getActivity(),
			// getResources().getString(R.string.loggedout),
			// Toast.LENGTH_SHORT).show();
			// createMainMenu();

		} else if (bean.getDescription().startsWith("Updates")) {
			// PackageInfo pInfo = null;
			// try {
			// pInfo = getActivity().getPackageManager().getPackageInfo(
			// getActivity().getPackageName(), 0);
			// } catch (NameNotFoundException e) {
			// Helper.logError(this, "onOptionsItemSelected", e);
			// }
			// String version = pInfo.versionName;
			// CharSequence text = "Your current version is " + version;
			// int duration = Toast.LENGTH_LONG;
			//
			// Toast toast = Toast.makeText(getActivity(), text, duration);
			// toast.show();
			// Helper.executeAsyncTask(new CheckerVersion());
		}
	}

	// ////////////////////////////////////Progress
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mStatusView.setVisibility(View.VISIBLE);
			mStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mFormView.setVisibility(View.VISIBLE);
			mFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			mStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
