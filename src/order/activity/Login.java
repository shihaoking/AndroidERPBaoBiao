package order.activity;

import java.util.ArrayList;
import java.util.List;
import order.http.base.HttpDownload;
import order.activity.R;
import order.data.factory.LoginLogEntity;
import order.data.factory.YWYEntity;
import order.data.handler.LoginLogHandler;
import order.data.handler.YWYHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	public static List<Activity> activityList = new ArrayList<Activity>();
	private Button loginButton;
	private EditText userNameText;
	private EditText userPwdText;

	private LoginLogHandler loginHandler;
	private LoginLogEntity loginEntity;
	private YWYHandler ywyHandler;

	public static String userName;
	public static String userNo;
	public static int userLevel = 2;

	private final static int MENU_EXIT = 0;
	private final static int MENU_SET = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		ywyHandler = new YWYHandler(this);
		ywyHandler.CheckYwyData();

		loginButton = (Button) findViewById(R.id.loginButton);
		userNameText = (EditText) findViewById(R.id.userName);
		userPwdText = (EditText) findViewById(R.id.userPwd);

		loginButton.setOnClickListener(new LoginButtonClick());

		activityList.add(this);

		loginHandler = new LoginLogHandler(Login.this);
		loginEntity = loginHandler.GetLastLog();

		if (loginEntity != null) {
			userNameText.setText(loginEntity.GetUserName());
		}
	}

	public class LoginButtonClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (HttpDownload.CheckNetAvailable(Login.this)) {
				userName = userNameText.getText().toString();
				String userPwd = userPwdText.getText().toString();

				YWYEntity ywyEntity = ywyHandler.GetYWYByName(userName);

				if (ywyEntity != null && userPwd.equals("123")) {
					// 如果用户是管理员，则保存用户等级
					userLevel = ywyEntity.GetLevel();
					userNo = ywyEntity.GetNo();

					if (loginEntity == null
							|| !userNo.equals(loginEntity.GetUserNo())) {
						loginEntity = new LoginLogEntity(userNo, userName);
						loginHandler.Save(loginEntity);
					}

					Intent intent = new Intent(Login.this, Main.class);
					startActivity(intent);
					return;
				} else {
					Toast.makeText(Login.this, "用户名或密码错误", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(Login.this, "当前网络不可用，请检查网络连接！",
						Toast.LENGTH_LONG).show();

			}
			return;
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
			Intent intent = new Intent(Login.this, SystemSetting.class);
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
		AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
		dialog.setTitle("提示");
		dialog.setMessage("确定退出系统？");
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for (Activity activity : activityList) {
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