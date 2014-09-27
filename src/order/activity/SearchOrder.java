package order.activity;

import java.util.List;
import order.ui.handler.PullRefreshListView.OnRefreshListener;
import order.activity.R;
import order.data.factory.CGDEntity;
import order.data.factory.YWYEntity;
import order.data.handler.CGDHandler;
import order.data.handler.CGDWLHandler;
import order.data.handler.YWYHandler;
import order.http.base.HttpDownload;
import order.ui.handler.PullRefreshListView;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchOrder extends Activity {

	private PullRefreshListView orderListView;
	private List<CGDEntity> cgdEntities;

	private Button backToMainBtn = null;
	private ImageButton filterBtn;

	private final int ITEM_REFRASH = 0;
	private final int ITEM_FILTER = 1;
	private final int ITEM_EXIT = 2;

	private final static int EDIT = 0;
	private final static int DELETE = 1;
	private final static int CHECK = 2;

	private final static int GET_FILTER = 0;
	private final static int EDIT_ITEM = 1;
	private OrderListArray orderListArray;

	private boolean isNetAvailable = false;
	private CGDHandler cgdHandle;

	private MyHandler myHandler;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.search_order_main);

		orderListView = (PullRefreshListView) findViewById(R.id.orderListView);
		backToMainBtn = (Button) findViewById(R.id.backToMain);
		filterBtn = (ImageButton) findViewById(R.id.filterBtn);

		backToMainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		filterBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowFilterDialog();
			}
		});

		initializeViewData();

		if (!HttpDownload.CheckNetAvailable(this)) {
			Toast.makeText(this, "当前网络连接不可用，无法更新订单数据！", Toast.LENGTH_LONG)
					.show();
		}

		Login.activityList.add(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GET_FILTER:
				cgdEntities = OrderFilter.filterCgdEntities;
				orderListArray = new OrderListArray(SearchOrder.this,
						R.layout.order_list_item, cgdEntities);
				orderListView.setAdapter(orderListArray);
				break;
			case EDIT_ITEM:
				CGDEntity newData = CreateOrder.cgdEntity;
				for (int i = 0; i < cgdEntities.size(); i++) {
					if (newData.GetNo().equals(cgdEntities.get(i).GetNo())) {
						cgdEntities.set(i, newData);
					}
				}
				orderListArray = new OrderListArray(SearchOrder.this,
						R.layout.order_list_item, cgdEntities);
				orderListView.setAdapter(orderListArray);
				break;
			}
		}
	}

	private void initializeViewData() {
		cgdHandle = new CGDHandler(SearchOrder.this);
		myHandler = new MyHandler();

		if (Login.userLevel == 2) {
			Bundle bundle = this.getIntent().getExtras();
			if (bundle != null && bundle.get("checkFilter") != null) {
				cgdEntities = cgdHandle.GetCgdByFilter("", 0, "1900/1/1",
						"9999/1/1");
			} else {
				cgdEntities = cgdHandle.getAllCgd();
			}

		} else {
			cgdEntities = cgdHandle.GetCgdMainByYwyNo(Login.userNo);
		}

		orderListArray = new OrderListArray(SearchOrder.this,
				R.layout.order_list_item, cgdEntities);
		orderListView.setAdapter(orderListArray);

		orderListView.setOnItemClickListener(new orderListItemClick());
		// 绑定长按item弹出菜单事件
		orderListView
				.setOnCreateContextMenuListener(new orderListItemMenuClick());

		orderListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						myHandler.sendEmptyMessage(1);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						orderListView.onRefreshComplete();
					}

				}.execute();
			}
		});
	}

	private class orderListItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// 获取选中的item对应的采购单编号
			String cgdNoStr = ((TextView) arg1.findViewById(R.id.noTextView))
					.getText().toString();
			ShowDetailActivity(cgdNoStr);
		}

	}

	public void ShowDetailActivity(String cgdNo) {
		Intent intent = new Intent(SearchOrder.this, CreateOrder.class);
		// 向CreateOrder界面发送采购单编号
		intent.putExtra("cgdNo", cgdNo);
		startActivityForResult(intent, EDIT_ITEM);
	}

	public void ShowFilterDialog() {
		Intent intent = new Intent(SearchOrder.this, OrderFilter.class);
		startActivityForResult(intent, GET_FILTER);
	}

	private class orderListItemMenuClick implements OnCreateContextMenuListener {

		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			menu.setHeaderTitle(getResources().getString(R.string.orderOperate));
			int listViewIndex = ((AdapterContextMenuInfo) menuInfo).position;
			String checker = cgdEntities.get(listViewIndex).GetChecker();
			if (Login.userLevel == 2) {
				if (checker.isEmpty()) {
					menu.add(0, CHECK, 0,
							getResources().getString(R.string.check));
				} else {
					menu.add(0, CHECK, 0,
							getResources().getString(R.string.unCheck));
				}

			} else {
				menu.add(0, EDIT, 1, getResources().getString(R.string.edit));
				menu.add(0, DELETE, 2, getResources()
						.getString(R.string.delete));
			}
		}

	}

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int listViewIndex = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
		String cgdNo = cgdEntities.get(listViewIndex).GetNo();
		switch (item.getItemId()) {
		case CHECK:
			ShowDetailActivity(cgdNo);
			break;
		case EDIT:
			ShowDetailActivity(cgdNo);
			break;
		case DELETE:
			cgdHandle.DeleteByNo(cgdNo);
			CGDWLHandler cgdwlHandler = new CGDWLHandler(SearchOrder.this);
			cgdwlHandler.DeleteByCgdNo(cgdNo);
			cgdEntities.remove(listViewIndex);
			orderListArray.notifyDataSetChanged();
			break;
		}
		return super.onContextItemSelected(item);
	}

	private class OrderListArray extends ArrayAdapter {

		public OrderListArray(Context context, int textViewResourceId,
				List<CGDEntity> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object getItem(int position) {
			System.out.print("get item" + position);
			// TODO Auto-generated method stub
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View newView = null;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				newView = inflater.inflate(R.layout.order_list_item, parent,
						false);
			} else {
				newView = convertView;
			}

			CGDEntity cgdEntity = cgdEntities.get(position);
			if (cgdEntity != null) {

				TextView noText = (TextView) newView
						.findViewById(R.id.noTextView);
				TextView categoryText = (TextView) newView
						.findViewById(R.id.categoryTextView);
				ImageView statusImage = (ImageView) newView
						.findViewById(R.id.statusImage);
				TextView createrText = (TextView) newView
						.findViewById(R.id.createrTextView);
				TextView createTimeText = (TextView) newView
						.findViewById(R.id.createTimeTextView);

				YWYHandler ywyHandler = new YWYHandler(SearchOrder.this);
				YWYEntity ywyEntity = ywyHandler.GetYWYByNo(cgdEntity
						.GetYwyNo());
				noText.setText(cgdEntity.GetNo());
				categoryText.setText(cgdEntity.GetCategory());
				createrText.setText(ywyEntity.GetName());
				createTimeText.setText(cgdEntity.GetCreateTime());

				// 绑定审核状态图标
				if (cgdEntity.GetChecker() == null
						|| cgdEntity.GetChecker().isEmpty()) {
					statusImage.setImageDrawable(getResources().getDrawable(
							R.drawable.order_unchecked));
				} else {
					statusImage.setImageDrawable(getResources().getDrawable(
							R.drawable.order_checked));
				}
			}

			return newView;
		}
	}

	/**
	 * 创建菜单(android框架回调,super.onCreate()中)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		menu.add(0, ITEM_REFRASH, 0, getResources().getString(R.string.refresh))
				.setIcon(R.drawable.common_menu_refresh);
		menu.add(0, ITEM_FILTER, 0, getResources().getString(R.string.filter))
				.setIcon(R.drawable.common_menu_filter);
		menu.add(0, ITEM_EXIT, 0, getResources().getString(R.string.exit))
				.setIcon(R.drawable.common_menu_exit);

		return true;

	}

	/**
	 * 菜单项点击事件处理(android框架回调)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem aMenuItem) {

		switch (aMenuItem.getItemId()) {
		case ITEM_REFRASH:
			progressDialog = new ProgressDialog(SearchOrder.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("正在更新...");
			progressDialog.show();

			new Thread() {
				public void run() {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					myHandler.sendEmptyMessage(2);
				}
			}.start();
			break;
		case ITEM_FILTER:
			ShowFilterDialog();
			break;
		case ITEM_EXIT:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					SearchOrder.this);
			dialog.setTitle("提示");
			dialog.setMessage("确定退出系统？");
			dialog.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							for (Activity activity : Login.activityList) {
								activity.finish();
							}
						}
					});

			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dg, int which) {
							// TODO Auto-generated method stub
							dg.dismiss();
						}
					});

			dialog.create().show();
			break;
		}
		return super.onOptionsItemSelected(aMenuItem);

	}

	public void RefreshData(int type) {

		cgdEntities.clear();
		if (Login.userLevel == 2) {
			cgdEntities.addAll(cgdHandle.UpdateCGDDataFromServer(""));
		} else {
			cgdEntities.addAll(cgdHandle.UpdateCGDDataFromServer(Login.userNo));
		}
		if (type == 2) {
			progressDialog.dismiss();
		}
	}

	public class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			RefreshData(msg.what);
		}

	}

}
