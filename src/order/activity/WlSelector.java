package order.activity;

import java.util.ArrayList;
import java.util.List;

import order.data.factory.CGDEntity;
import order.data.factory.CGDWLEntity;
import order.data.factory.WLCategoryEntity;
import order.data.factory.WLEntity;
import order.data.handler.WLCategoryHandler;
import order.data.handler.WLHandler;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class WlSelector extends Activity {

	private TableRow subWlCategoryRow;
	private Spinner mainWlCategorySp;
	private Spinner subWlCategorySp;
	private ListView wlSelectorLv;
	private Button submitBtn;

	private String wl_category = "";
	private WlListArray wlListArray;
	private List<WLEntity> selectedWl = new ArrayList<WLEntity>();
	private List<WLCategoryEntity> wlMainCategoryItems;
	private List<WLCategoryEntity> wlSubCategoryItems;
	private ArrayAdapter<String> wlMainCategoryAdapter;
	private ArrayAdapter<String> wlSubCategoryAdapter;
	private List<WLEntity> newWlEntities;

	private List<CGDWLEntity> sltedCgdWlEntities;
	private String[] MAIN_CATEGORY;
	private String[] SUB_CATEGORY;

	private WLCategoryHandler wlCategoryHandler;
	private WLHandler wlHandler;

	private int categorySelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO 将代码放在此处
		setContentView(R.layout.wl_selector);

		subWlCategoryRow = (TableRow) findViewById(R.id.wlCategorySubRow);
		mainWlCategorySp = (Spinner) findViewById(R.id.mainWlCategory);
		subWlCategorySp = (Spinner) findViewById(R.id.subWlCategory);
		wlSelectorLv = (ListView) findViewById(R.id.wlSelectorList);
		submitBtn = (Button) findViewById(R.id.submitNewWlBtn);

		// 初始化数据
		initializeViewData();

		// 绑定物料主类别选项数据
		mainWlCategorySp.setAdapter(wlMainCategoryAdapter);
		mainWlCategorySp.setOnItemSelectedListener(new WlMainCateorySelected());

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				// TODO Auto-generated method stub
				for (WLEntity item : selectedWl) {
					sltedCgdWlEntities.add(new CGDWLEntity(item.GetNo(),
							wl_category));
				}
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void initializeViewData() {
		Bundle bundle = this.getIntent().getExtras();
		// 获取标示，判断当前的订单类型数据是"产品"还是"半成品/物料"
		wl_category = bundle.getString("wl_category");
		sltedCgdWlEntities = CreateOrder.sltedWlEntities;

		wlHandler = new WLHandler(this);

		wlMainCategoryItems = new ArrayList<WLCategoryEntity>();
		wlCategoryHandler = new WLCategoryHandler(this);
		if (wl_category.equals("WL")) {
			wlMainCategoryItems = wlCategoryHandler.GetWlCategoryByParentKey(0);
		} else if (wl_category.equals("CP")) {
			wlMainCategoryItems = wlCategoryHandler.GetCpCategoryByParentKey(0);
		}

		MAIN_CATEGORY = new String[wlMainCategoryItems.size() + 1];
		MAIN_CATEGORY[0] = "请选择";
		for (int i = 0; i < wlMainCategoryItems.size(); i++) {
			MAIN_CATEGORY[i + 1] = wlMainCategoryItems.get(i).GetName();
		}

		wlMainCategoryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MAIN_CATEGORY);
		wlMainCategoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	public class WlMainCateorySelected implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int mainArg,
				long arg3) {
			// TODO Auto-generated method stub
			if (mainArg != 0) {

				categorySelected = wlMainCategoryItems.get(mainArg - 1)
						.GetKey();

				wlSubCategoryItems = new ArrayList<WLCategoryEntity>();
				// 获取物料子类别
				if (wl_category.equals("WL")) {
					wlSubCategoryItems = wlCategoryHandler
							.GetWlCategoryByParentKey(wlMainCategoryItems.get(
									mainArg - 1).GetKey());
				}else {
					wlSubCategoryItems = wlCategoryHandler
							.GetCpCategoryByParentKey(wlMainCategoryItems.get(
									mainArg - 1).GetKey());
				}


				if (!wlSubCategoryItems.isEmpty()) {
					BindSubCategorySpinner(mainArg);
				} else {
					// 显示物料类别对应的物料清单
					subWlCategoryRow.setVisibility(View.GONE);
					ShowWlList(categorySelected);
				}

			} else {
				categorySelected = 0;
				subWlCategoryRow.setVisibility(View.GONE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	}

	public void BindSubCategorySpinner(final int mainArg) {
		subWlCategoryRow.setVisibility(View.VISIBLE);

		SUB_CATEGORY = new String[wlSubCategoryItems.size() + 1];
		SUB_CATEGORY[0] = "请选择";

		for (int i = 0; i < wlSubCategoryItems.size(); i++) {
			SUB_CATEGORY[i + 1] = wlSubCategoryItems.get(i).GetName();
		}

		// 绑定物料子类别数据
		wlSubCategoryAdapter = new ArrayAdapter<String>(WlSelector.this,
				android.R.layout.simple_spinner_item, SUB_CATEGORY);
		wlSubCategoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		subWlCategorySp.setAdapter(wlSubCategoryAdapter);
		subWlCategorySp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int subArg, long arg3) {
				// TODO Auto-generated method stub
				if (subArg != 0) {
					categorySelected = wlSubCategoryItems.get(subArg - 1)
							.GetKey();
					// 显示物料类别对应的物料清单
					ShowWlList(categorySelected);
				} else {
					categorySelected = wlMainCategoryItems.get(mainArg - 1)
							.GetKey();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void ShowWlList(int _category) {
		if (wl_category.equals("WL")) {
			newWlEntities = wlHandler.GetWlByCategory(_category);
		} else {
			newWlEntities = wlHandler.GetCpByCategory(_category);
		}

		wlListArray = new WlListArray(WlSelector.this,
				R.layout.order_list_item, newWlEntities);
		wlSelectorLv.setAdapter(wlListArray);
	}

	private class WlListArray extends ArrayAdapter {

		@SuppressWarnings("unchecked")
		public WlListArray(Context context, int textViewResourceId,
				List<WLEntity> objects) {
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
				newView = inflater.inflate(R.layout.wl_selector_list, parent,
						false);
			} else {
				newView = convertView;
			}

			final WLEntity wlEntity = newWlEntities.get(position);

			CheckBox checkBox = (CheckBox) newView
					.findViewById(R.id.newWlCheck);
			TextView noText = (TextView) newView.findViewById(R.id.newWlNoText);
			TextView levelText = (TextView) newView
					.findViewById(R.id.newWlLevelText);
			TextView nameText = (TextView) newView
					.findViewById(R.id.newWlNameText);
			TextView unitText = (TextView) newView
					.findViewById(R.id.newWlUnitText);

			noText.setText(wlEntity.GetNo());
			nameText.setText(wlEntity.GetName());
			levelText.setText(wlEntity.GetLevel());
			unitText.setText(wlEntity.GetUnit());

			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						selectedWl.add(wlEntity);
					} else {
						selectedWl.remove(wlEntity);
					}
				}
			});

			return newView;
		}
	}

}
