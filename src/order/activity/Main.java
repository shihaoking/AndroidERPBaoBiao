package order.activity;

import order.activity.Login;
import order.activity.R;
import order.data.handler.YWYHandler;
import order.http.base.HttpDownload;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class Main extends Activity {

	TableLayout actionTableLayout = null;
	RelativeLayout auditLayout = null;

	ImageButton newOrder = null;
	ImageButton searchOrder = null;
	ImageButton auditOrder = null;

	private final static int MENU_EXIT = 0;
	private final static int MENU_SET = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		actionTableLayout = (TableLayout) findViewById(R.id.actionTable);
		actionTableLayout.addView(LayoutInflater.from(this).inflate(
				R.layout.main_content_layout, null));
		auditLayout = (RelativeLayout) findViewById(R.id.auditLayout);

		// 如果操作员不是管理者，则隐藏订单审核按钮
		int userLevel = Login.userLevel;
		System.out.println(userLevel);
		if (userLevel != 2) {
			auditLayout.setVisibility(View.GONE);
		}

		// 绑定图标事件
		newOrder = (ImageButton) findViewById(R.id.newOrder);
		searchOrder = (ImageButton) findViewById(R.id.searchOrder);
		auditOrder = (ImageButton) findViewById(R.id.auditOrder);

		newOrder.setOnClickListener(new newOrderShow());

		searchOrder.setOnClickListener(new SearchOrderShow());

		auditOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Main.this, SearchOrder.class);
				intent.putExtra("checkFilter", true);
				startActivity(intent);
			}
		});

		if (!HttpDownload.CheckNetAvailable(this)) {
			Toast.makeText(this, "当前网络连接不可用，请检查网络连接！", Toast.LENGTH_LONG)
					.show();
		}

		Login.activityList.add(this);
	}

	private class newOrderShow implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (HttpDownload.CheckNetAvailable(Main.this)) {
				Intent intent = new Intent(Main.this, CreateOrder.class);
				startActivity(intent);
			} else {
				Toast.makeText(Main.this, "当前网络连接不可用，无法添加新数据！",
						Toast.LENGTH_LONG).show();
			}
		}

	}

	public class SearchOrderShow implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Main.this, SearchOrder.class);
			startActivity(intent);
		}

	}

	/**
	 * 创建菜单(android框架回调,super.onCreate()中)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_SET, 0,
				getResources().getString(R.string.systemSetting)).setIcon(
				R.drawable.common_menu_setting);
		menu.add(0, MENU_EXIT, 0, getResources().getString(R.string.exit))
				.setIcon(R.drawable.common_menu_exit);

		return true;

	}

	/**
	 * 菜单项点击事件处理(android框架回调)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem aMenuItem) {

		switch (aMenuItem.getItemId()) {
		case MENU_EXIT:
			ShowExitDialog();
			break;
		case MENU_SET:
			Intent intent = new Intent(Main.this,SystemSetting.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(aMenuItem);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		ShowExitDialog();
	}

	public void ShowExitDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(Main.this);
		dialog.setTitle("提示");
		dialog.setMessage("确定退出系统？");
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for (Activity activity : Login.activityList) {
					activity.finish();
				}
			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dg, int which) {
				// TODO Auto-generated method stub
				dg.dismiss();
			}
		});

		dialog.create().show();
	}

}
