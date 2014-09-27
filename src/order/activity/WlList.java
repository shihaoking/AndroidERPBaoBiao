package order.activity;

import java.util.ArrayList;
import java.util.List;

import order.data.factory.CGDWLEntity;
import order.data.factory.WLEntity;
import order.data.handler.WLHandler;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WlList extends Activity {

	private List<CGDWLEntity> sltedWlEntities;

	private ListView wlListView;
	private WlListArray wlListArray;
	private Button submitWlBtn;
	private ImageButton addNewWlBtn;

	private String wl_category = "";

	private final int ITEM_MULTISELECT = 0;
	private final int ITEM_DELETE = 1;
	private final int ITEM_CANCEL = 2;

	private final static int GET_NEWWL = 0;
	private final static int EDIT_WLDETAIL = 1;

	private boolean isMultiSelect = false;
	private List<String> selectedItem = new ArrayList<String>();
	private List<CheckBox> checkBoxs = new ArrayList<CheckBox>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_order_add_wl_main);

		submitWlBtn = (Button) findViewById(R.id.submitWlBtn);
		addNewWlBtn = (ImageButton) findViewById(R.id.addWlBtn);
		wlListView = (ListView) findViewById(R.id.addedWlListView);

		initializeViewData();

		wlListView.setOnItemClickListener(new wlListItemClick());
		// 绑定长按item弹出菜单事件
		wlListView.setOnCreateContextMenuListener(new wlListItemMenuClick());

		submitWlBtn.setOnClickListener(new SubmitWlChange());

		addNewWlBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WlList.this, WlSelector.class);
				intent.putExtra("wl_category", wl_category);
				startActivityForResult(intent, GET_NEWWL);
			}
		});
		
		Login.activityList.add(this);
	}

	private void initializeViewData() {
		Bundle bundle = this.getIntent().getExtras();
		// 获取标示，判断当前的订单类型数据是"产品"还是"半成品/物料"
		wl_category = bundle.getString("wl_category");
		// 获取已添加的物料清单
		sltedWlEntities = CreateOrder.sltedWlEntities;

		if (sltedWlEntities != null || !sltedWlEntities.isEmpty()) {
			wlListArray = new WlListArray(WlList.this,
					R.layout.order_list_item, sltedWlEntities);
			wlListView.setAdapter(wlListArray);
		}
	}

	public class SubmitWlChange implements OnClickListener {

		@Override
		public void onClick(View v) {
			for (CGDWLEntity item : sltedWlEntities) {
				if (item.GetCount() == 0 || item.GetPrice() == 0) {
					Toast.makeText(WlList.this,
							"物料\"" + item.GetNo() + "\"的数量和价格不能为零",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			Intent intent = new Intent(WlList.this, CreateOrder.class);
			intent.putExtra("wl_changed", true);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case GET_NEWWL:
				wlListArray.notifyDataSetChanged();
				break;
			case EDIT_WLDETAIL:
				wlListArray.notifyDataSetChanged();
				break;
			}
		}

	}

	public class wlListItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int index,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(WlList.this, WlDetailEdit.class);
			intent.putExtra("No", sltedWlEntities.get(index).GetNo());
			intent.putExtra("Price", sltedWlEntities.get(index).GetPrice());
			intent.putExtra("Count", sltedWlEntities.get(index).GetCount());
			intent.putExtra("xqDate", sltedWlEntities.get(index).GetXqDate());
			intent.putExtra("dhDate", sltedWlEntities.get(index).GetDhDate());

			startActivityForResult(intent, EDIT_WLDETAIL);
		}

	}

	public class wlListItemMenuClick implements OnCreateContextMenuListener {

		@Override
		public void onCreateContextMenu(ContextMenu arg0, View arg1,
				ContextMenuInfo arg2) {
			// TODO Auto-generated method stub

		}

	}

	private class WlListArray extends ArrayAdapter {

		@SuppressWarnings("unchecked")
		public WlListArray(Context context, int textViewResourceId,
				List<CGDWLEntity> objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View newView = null;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				newView = inflater.inflate(R.layout.new_order_add_wl_list,
						parent, false);
			} else {
				newView = convertView;
			}
			
			final CGDWLEntity cgdwlEntity = sltedWlEntities.get(position);
			WLHandler wlHandler = new WLHandler(WlList.this);
			WLEntity wlEntity = wlHandler.GetWlByNo(cgdwlEntity.GetNo());

			final String itemIndex = String.valueOf(position);

			CheckBox checkBox = (CheckBox) newView
					.findViewById(R.id.wlCheckBox);
			TextView noText = (TextView) newView
					.findViewById(R.id.wlNoTextView);
			TextView levelText = (TextView) newView
					.findViewById(R.id.wlLevelTextView);
			TextView priceText = (TextView) newView
					.findViewById(R.id.wlPriceTextView);
			TextView nameText = (TextView) newView
					.findViewById(R.id.wlNameTextView);
			TextView countText = (TextView) newView
					.findViewById(R.id.wlCountTextView);
			TextView totalPriceText = (TextView) newView
					.findViewById(R.id.wlTotalPriceTextView);

			noText.setText(cgdwlEntity.GetNo());
			levelText.setText(wlEntity.GetLevel());
			priceText.setText(cgdwlEntity.GetPrice() + "元/"
					+ wlEntity.GetUnit());
			nameText.setText(wlEntity.GetName());
			countText.setText(cgdwlEntity.GetCount() + wlEntity.GetUnit());
			totalPriceText.setText("" + cgdwlEntity.GetCount()
					* cgdwlEntity.GetPrice());

			checkBoxs.add(checkBox);
			checkBox.setVisibility(View.GONE);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						selectedItem.add(itemIndex);
					} else {
						selectedItem.remove(itemIndex);
					}
				}
			});

			return newView;
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (isMultiSelect) {
			menu.findItem(ITEM_MULTISELECT).setVisible(false);
			menu.findItem(ITEM_CANCEL).setVisible(true);
			menu.findItem(ITEM_DELETE).setVisible(true);
		} else {
			menu.findItem(ITEM_MULTISELECT).setVisible(true);
			menu.findItem(ITEM_CANCEL).setVisible(false);
			menu.findItem(ITEM_DELETE).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(0, ITEM_DELETE, 0, getResources().getString(R.string.delete))
				.setIcon(R.drawable.common_menu_delete);

		menu.add(0, ITEM_MULTISELECT, 0,
				getResources().getString(R.string.multiSelect)).setIcon(
				R.drawable.common_menu_multiselect);

		menu.add(0, ITEM_CANCEL, 0, getResources().getString(R.string.cancel))
				.setIcon(R.drawable.common_menu_cancel);
		return true;

	}

	/**
	 * 菜单项点击事件处理(android框架回调)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem aMenuItem) {

		switch (aMenuItem.getItemId()) {
		case ITEM_MULTISELECT:
			for (CheckBox checkBox : checkBoxs) {
				checkBox.setChecked(false);
				checkBox.setVisibility(View.VISIBLE);
			}
			isMultiSelect = true;
			break;
		case ITEM_CANCEL:
			for (CheckBox checkBox : checkBoxs) {
				checkBox.setVisibility(View.GONE);
				checkBox.setChecked(false);
			}
			isMultiSelect = false;
			break;
		case ITEM_DELETE:
			if (selectedItem.isEmpty()) {
				Toast.makeText(WlList.this, "请选择要删除的项！", Toast.LENGTH_SHORT)
						.show();
			} else {
				for (String itemIndex : selectedItem) {
					sltedWlEntities.remove(Integer.parseInt(itemIndex));
				}
				wlListArray.notifyDataSetChanged();
			}
			break;
		}
		return super.onOptionsItemSelected(aMenuItem);

	}
}
