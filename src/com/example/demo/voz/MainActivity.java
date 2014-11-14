package com.example.demo.voz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.SubMenu;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.demo.fragment.LoginFragment;
import com.example.demo.fragment.MainListFragment;
import com.example.demo.fragment.MenuListFragment;
import com.example.demo.fragment.vozListFragment;
import com.example.demo.util.Constants;

public class MainActivity extends SherlockFragmentActivity {

	public final static String INTENT_VAR_CURRENT_CONTENT = "intentVarCurrentContent";
	public final static String INTENT_VAR_IS_RELATED = "intentVarIsRelated";
	public final static String INTENT_VAR_URL = "intentVarUrl";
	public final static String INTENT_VAR_TITLE = "intentVarTitle";

	public static final int MAIN_LIST = 0;
	public static final int VOZ_LIST = 1;
	public static final int VOZ_LOGIN = 2;

	public final static String TITLE_F33 = "Điểm báo";
	public final static String TITLE_F17 = "Chuyện trò linh tinh™";

	private DrawerLayout mDrawerLayout;
	private MenuListFragment frmMenu;
	private ActionBarDrawerToggle mDrawerToggle;
	ProgressDialog prgDialog;

	private int currentContent = MAIN_LIST;

	private MainListFragment frmMainList;
	private vozListFragment frmvozList;
	private LoginFragment frmLoginFragment;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// //////DrawerLayout////////

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View view) {
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// //////Fragment Left Menu////////
		frmMenu = new MenuListFragment();
		frmMenu.setMainActivity(this);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.menu_frame, frmMenu)
				.commit();

		// //////////////////////////

		int tempCurrentContent = getIntent().getIntExtra(
				INTENT_VAR_CURRENT_CONTENT, -1);

		if (tempCurrentContent != -1)
			currentContent = tempCurrentContent;

		if (currentContent == MAIN_LIST) {
			mainlistload();
		} else if (currentContent == VOZ_LIST) {
			String url = getIntent().getStringExtra(INTENT_VAR_URL);
			url = url == null ? Constants.SITEROOT : url;
			Log.i("url", url);
			String title = getIntent().getStringExtra(INTENT_VAR_TITLE);
			title = title == null ? getResources().getString(R.string.app_name)
					: title;
			Log.i("title", title);
			vozlistload(url, title);
		} else if (currentContent == VOZ_LOGIN) {
			loginload();

		}
	}

	private void loginload() {
		frmLoginFragment = new LoginFragment();
		frmLoginFragment.setMainActivity(this);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, frmLoginFragment).commit();
	}

	private void mainlistload() {
//		 frmMainList = new MainListFragment();
//		 frmMainList.setMainActivity(this);
//		 getSupportFragmentManager().beginTransaction()
//		 .replace(R.id.content_frame, frmMainList)
//		 // .addToBackStack(null)
//		 .commit();
		frmLoginFragment = new LoginFragment();
		frmLoginFragment.setMainActivity(this);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, frmLoginFragment).commit();
	}

	private void vozlistload(String url, String title) {
		boolean isRelated = getIntent().getBooleanExtra(INTENT_VAR_IS_RELATED,
				false);
		frmvozList = new vozListFragment();
		frmvozList.setMainActivity(this);
		frmvozList.setRelated(isRelated);
		url = url == null ? Constants.SITEROOT : url;
		frmvozList.setUrl(url);
		Log.i("url", url);

		title = title == null ? getResources().getString(R.string.app_name)
				: title;
		setTitle(title);
		Log.i("title", title);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, frmvozList).addToBackStack(null)
				.commit();
	}

	// ////////////////////////////////////LeftMenuList/////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		frmMenu.createMainMenu();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item))) {
			return true;
		}
		return false;
	}

	private android.view.MenuItem getMenuItem(final MenuItem item) {
		return new android.view.MenuItem() {
			@Override
			public int getItemId() {
				return item.getItemId();
			}

			public boolean isEnabled() {
				return true;
			}

			@Override
			public boolean collapseActionView() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean expandActionView() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ActionProvider getActionProvider() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getActionView() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getAlphabeticShortcut() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getGroupId() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Drawable getIcon() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Intent getIntent() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getNumericShortcut() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getOrder() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public SubMenu getSubMenu() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitleCondensed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasSubMenu() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isActionViewExpanded() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCheckable() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChecked() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isVisible() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(
					ActionProvider actionProvider) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(
					OnActionExpandListener listener) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar,
					char alphaChar) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum) {
				// TODO Auto-generated method stub

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			Intent itMain = new Intent(this, MainActivity.class);
			itMain.putExtra(MainActivity.INTENT_VAR_CURRENT_CONTENT,
					MainActivity.MAIN_LIST);
			// itMain.putExtra("order", ORDER);
			this.startActivityForResult(itMain, 1);
			// mainlistload();
			// getFragmentManager().popBackStack(null,
			// FragmentManager.POP_BACK_STACK_INCLUSIVE);
		} else {
			super.onBackPressed();
		}

	}
}
