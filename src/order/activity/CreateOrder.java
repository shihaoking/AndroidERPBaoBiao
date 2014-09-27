package order.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import order.data.factory.BMEntity;
import order.data.factory.CGDEntity;
import order.data.factory.CGDWLEntity;
import order.data.factory.GHSEntity;
import order.data.factory.YWYEntity;
import order.data.handler.BMHandler;
import order.data.handler.CGDHandler;
import order.data.handler.CGDWLHandler;
import order.data.handler.GHSHandler;
import order.data.handler.YWYHandler;
import order.http.base.DataDowlond;
import order.http.base.DataUpload;
import order.http.base.HttpDownload;
import order.http.base.SendSms;
import order.activity.R;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class CreateOrder extends Activity {

	private static final String[] CATEGORYS = { "半成品/物料", "产品" };
	private static final String[] JSFS_STRINGS = { "请选择", "现金", "银行转账" };
	private static final String[] CGQYS = { "国内采购", "国外采购" };
	private static final String[] DDKIND_STRINGS = { "请选择", "原材料采购", "辅助材料采购",
			"五金材料采购" };
	private static final String[] YSFS_STRINGS = { "请选择", "客户送", "自运", "客户自提" };
	private static final String[] FKTJ_STRINGS = { "请选择", "现金付款", "货到银行转账",
			"货到现金付款" };
	private final int GET_BM = 0;
	private final int GET_GHS = 1;
	private final int GET_WL = 2;

	private TextView title;
	private Button selectTimeBtn;
	private LinearLayout checkStatusLayout;
	private ImageView checkStautsBtn;
	private Spinner categorySp;
	private Button ghsBtn;
	private Spinner jsfsSp;
	private Spinner xmSp;
	private Button bumenBtn;
	private TextView ywyEt;
	private Spinner fktjSp;
	private Spinner ysfsSp;
	private Spinner ddKindSp;
	private EditText djEt;
	private EditText yfEt;
	private EditText hlEt;
	private EditText dzEt;
	private EditText bzEt;
	private Button wlBtn;

	private Button backToMainBtn;
	private ImageView editBtn;

	private Button submitBtn;

	private Calendar calendar = Calendar.getInstance();
	private ArrayAdapter<String> adapter;

	public CGDHandler cgdHandler;
	public static CGDEntity cgdEntity;

	public static List<CGDWLEntity> sltedWlEntities;

	private boolean wlChanged = false;
	private boolean isEdited = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_order_main);

		Login.activityList.add(this);

		title = (TextView) findViewById(R.id.newOrderTitle);
		// 绑定控件
		backToMainBtn = (Button) findViewById(R.id.backToMain);
		editBtn = (ImageView) findViewById(R.id.editBtn);
		submitBtn = (Button) findViewById(R.id.submitCgdBtn);
		checkStatusLayout = (LinearLayout) findViewById(R.id.checkStatusLayout);
		checkStautsBtn = (ImageView) findViewById(R.id.checkStatus);
		selectTimeBtn = (Button) findViewById(R.id.createTime);
		categorySp = (Spinner) findViewById(R.id.category);
		ghsBtn = (Button) findViewById(R.id.ghsNo);
		jsfsSp = (Spinner) findViewById(R.id.jsfsNo);
		xmSp = (Spinner) findViewById(R.id.xmNo);
		bumenBtn = (Button) findViewById(R.id.bumenBtn);
		ywyEt = (TextView) findViewById(R.id.ywyNo);
		fktjSp = (Spinner) findViewById(R.id.fktjNo);
		ysfsSp = (Spinner) findViewById(R.id.ysfsNo);
		ddKindSp = (Spinner) findViewById(R.id.ddKindNo);
		djEt = (EditText) findViewById(R.id.dj);
		yfEt = (EditText) findViewById(R.id.yf);
		hlEt = (EditText) findViewById(R.id.hl);
		bzEt = (EditText) findViewById(R.id.bz);
		dzEt = (EditText) findViewById(R.id.dhAddress);
		wlBtn = (Button) findViewById(R.id.wl);

		InitializeData();

		// 打开物料添加Activity
		wlBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CreateOrder.this, WlList.class);
				Bundle bundle = new Bundle();
				if (categorySp.getSelectedItemPosition() == 0) {
					bundle.putString("wl_category", "WL");
				} else {
					bundle.putString("wl_category", "CP");
				}
				intent.putExtras(bundle);

				startActivityForResult(intent, GET_WL);
			}
		});

		// 弹出时间选择Dialog
		selectTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(CreateOrder.this, dateDialog, calendar
						.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		// 改变审核状态
		checkStautsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (cgdEntity.GetChecker() == null
						|| cgdEntity.GetChecker().isEmpty()) {
					checkStautsBtn.setImageResource(R.drawable.checked_on);
					cgdEntity.SetChecker(Login.userName);
					cgdEntity.SetCheckerLc(Login.userNo + "." + Login.userName);
				} else {
					checkStautsBtn.setImageResource(R.drawable.checked_off);
					cgdEntity.SetChecker("");
					cgdEntity.SetCheckerLc("");
				}
				isEdited = true;
			}
		});

		// 返回主操作界面
		backToMainBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isEdited) {
					ShowConfirmDialog("退出后当前数据将无法保存！", false);
				} else {
					finish();
				}
			}
		});

		// 编辑当前订单
		editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 改变标题
				// 激活控件的可用状态
				EnableView();
			}
		});

		// 提交订单
		submitBtn.setOnClickListener(new SubmitOrderData());
	}

	public void ShowToast(String msg) {
		Toast.makeText(CreateOrder.this, "请选择\"" + msg + "\"",
				Toast.LENGTH_LONG).show();
	}

	public class SubmitOrderData implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (!HttpDownload.CheckNetAvailable(CreateOrder.this)) {
				Toast.makeText(CreateOrder.this, "当前网络连接不可用，无法添加新订单！",
						Toast.LENGTH_LONG);
				return;
			}

			if (cgdEntity.GetGhsNo() == null || cgdEntity.GetGhsNo().isEmpty()) {
				ShowToast("供货商");
				return;
			}
			if (cgdEntity.GetFkfsNo() == null
					|| cgdEntity.GetFkfsNo().isEmpty()) {
				ShowToast("结算方式");
				return;
			}
			if (cgdEntity.GetBumenNo() == null
					|| cgdEntity.GetBumenNo().isEmpty()) {
				ShowToast("采购部门");
				return;
			}
			if (cgdEntity.GetFktjNo() == null
					|| cgdEntity.GetFktjNo().isEmpty()) {
				ShowToast("付款条件");
				return;
			}
			if (cgdEntity.GetYsfsNo() == null
					|| cgdEntity.GetYsfsNo().isEmpty()) {
				ShowToast("运输方式");
				return;
			}
			if (cgdEntity.GetDdKindNo() == null
					|| cgdEntity.GetDdKindNo().isEmpty()) {
				ShowToast("订单类型");
				return;
			}
			if (sltedWlEntities == null || sltedWlEntities.isEmpty()) {
				Toast.makeText(CreateOrder.this, "请添加\"订单物料\"",
						Toast.LENGTH_LONG).show();
				return;
			}

			double dj = 0;
			double yf = 0;
			double hl = 1;
			if (!djEt.getText().toString().isEmpty()) {
				dj = Double.parseDouble(djEt.getText().toString());
			}
			if (!yfEt.getText().toString().isEmpty()) {
				yf = Double.parseDouble(yfEt.getText().toString());
			}
			if (!hlEt.getText().toString().isEmpty()) {
				hl = Double.parseDouble(hlEt.getText().toString());
			}

			cgdEntity.SetDj(dj);
			cgdEntity.SetYf(yf);
			cgdEntity.setHl(hl);

			cgdEntity.SetDhAdress(dzEt.getText().toString());
			cgdEntity.SetBz(bzEt.getText().toString());

			int result = 0;
			String msg = "";
			// 如果是新订单，则添加业务员编号和制单员名称
			if (cgdEntity.GetNo() == null || cgdEntity.GetNo().isEmpty()) {
				cgdEntity.SetYwyNo(Login.userNo);
				cgdEntity.SetMaker(Login.userName);
				String cgdNo = DataUpload.AddNewCgdData(cgdEntity,
						sltedWlEntities);

				if (!cgdNo.isEmpty()) {
					result = 1;
					msg = "订单添加";
					cgdEntity.SetNo(cgdNo);
					cgdHandler.Save(cgdEntity);
				}
			} else {
				result = DataUpload.UpdateCgdData(cgdEntity, sltedWlEntities);
				cgdHandler.update(cgdEntity);
				if (Login.userLevel == 2) {
					msg = "订单审核状态更改成功";
				} else {
					msg = "订单更改";
				}
			}

			if (result == 1) {
				CGDWLHandler cgdwlHandler = new CGDWLHandler(CreateOrder.this);
				cgdwlHandler.DeleteByCgdNo(cgdEntity.GetNo());
				for (CGDWLEntity item : sltedWlEntities) {
					item.SetCgdNo(cgdEntity.GetNo());
					cgdwlHandler.Save(item);
				}
				if (Login.userLevel == 2) {
					SendSms.SendToYwy("您的编号为'" + cgdEntity.GetNo()
							+ "'的订单已经通过审核！");
				} else {
					SendSms.SendToManager("编号为'" + cgdEntity.GetNo() + "'的新订单等待您审核！");
				}
				ShowConfirmDialog(msg + "！是否返回到订单列表？", true);
			} else {
				ShowAttentionDialog("订单更新失败！");
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			switch (requestCode) {
			case GET_BM:
				String bumen = bundle.getString("bumen");
				if (bumen != "") {
					cgdEntity.SetBumenNo(bumen.split("-")[0]);
					bumenBtn.setText(bumen.split("-")[1]);
				}
				break;

			case GET_GHS:
				String ghs = bundle.getString("ghs");
				if (ghs != "") {
					cgdEntity.SetGhsNo(ghs.split("-")[0]);
					ghsBtn.setText(ghs.split("-")[1]);
				}
				break;
			case GET_WL:
				wlChanged = bundle.getBoolean("wl_changed");
				break;
			}
		}

	}

	private void InitializeData() {
		cgdHandler = new CGDHandler(CreateOrder.this);
		cgdEntity = new CGDEntity();
		sltedWlEntities = new ArrayList<CGDWLEntity>();

		if (cgdEntity.GetNo() != null && !cgdEntity.GetNo().isEmpty()) {
			title.setText("订单详情");
		}

		// 采购类别
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, CATEGORYS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySp.setAdapter(adapter);
		categorySp.setOnItemSelectedListener(new categorySpinnerClick());

		// 结算方式
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, JSFS_STRINGS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jsfsSp.setAdapter(adapter);
		jsfsSp.setOnItemSelectedListener(new jsfsSpinnerClick());

		// 采购区域
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, CGQYS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		xmSp.setAdapter(adapter);
		xmSp.setOnItemSelectedListener(new xmSpinnerClick());

		// 供货商
		ghsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(CreateOrder.this,
						GhsSelector.class), GET_GHS);
			}
		});

		// 采购部门
		bumenBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(CreateOrder.this,
						BumenSelector.class), GET_BM);
			}
		});

		// 业务员，默认为当前系统操作员
		ywyEt.setText(Login.userName);

		// 订单类型
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, DDKIND_STRINGS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ddKindSp.setAdapter(adapter);
		ddKindSp.setOnItemSelectedListener(new ddKindSpinnerClick());

		// 运输方式
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, YSFS_STRINGS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ysfsSp.setAdapter(adapter);
		ysfsSp.setOnItemSelectedListener(new ysfsSpinnerCLick());

		// 付款条件
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, FKTJ_STRINGS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fktjSp.setAdapter(adapter);
		fktjSp.setOnItemSelectedListener(new fktjSpinnerClick());

		// 汇率
		hlEt.setText("1.0");

		// 接收SearchOrder Activity传来的采购单编号
		// 如果当前Activity不是由SearchOrder Activity启动，则采购单编号为空
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			String cgdNo = bundle.getString("cgdNo");
			InitializeCGDDetail(cgdNo);
		}

	}

	private void InitializeCGDDetail(String cgdNo) {

		cgdEntity.SetNo(cgdNo);

		CGDWLHandler cgdwlHandler = new CGDWLHandler(CreateOrder.this);
		if (sltedWlEntities.isEmpty() && !wlChanged) {
			sltedWlEntities = cgdwlHandler.GetCgdWlByCgaNo(cgdEntity.GetNo());
		}

		try {
			cgdEntity = DataDowlond
					.GetCGDMainDetailData("select * from Cggl_dingdan where Danhao='"
							+ cgdEntity.GetNo() + "'");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (cgdEntity != null) {

			editBtn.setVisibility(View.VISIBLE);

			if (Login.userLevel == 2) {
				checkStatusLayout.setVisibility(View.VISIBLE);
			} else {
				checkStatusLayout.setVisibility(View.GONE);
			}

			if (cgdEntity.GetChecker() == null
					|| cgdEntity.GetChecker().isEmpty()) {
				checkStautsBtn.setImageResource(R.drawable.checked_off);
			} else {
				checkStautsBtn.setImageResource(R.drawable.checked_on);
			}

			selectTimeBtn.setText(cgdEntity.GetCreateTime());

			GHSHandler ghsHandler = new GHSHandler(this);
			GHSEntity ghsEntity = ghsHandler.GetGhsByNo(cgdEntity.GetGhsNo());
			cgdEntity.SetGhsNo(ghsEntity.GetNo());
			ghsBtn.setText(ghsEntity.GetName());

			BMHandler bmHandler = new BMHandler(this);
			BMEntity bmEntity = bmHandler.GetBmByNo(cgdEntity.GetBumenNo());
			cgdEntity.SetBumenNo(bmEntity.GetNo());
			bumenBtn.setText(bmEntity.GetName());

			jsfsSp.setSelection(Integer.parseInt(cgdEntity.GetFkfsNo()));

			for (int i = 0; i < CGQYS.length; i++) {
				if (cgdEntity.GetXmNo().equals(CGQYS[i].toString())) {
					xmSp.setSelection(i);
				}
			}

			YWYHandler ywyHandler = new YWYHandler(this);
			YWYEntity ywyEntity = ywyHandler.GetYWYByNo(cgdEntity.GetYwyNo());
			ywyEt.setText(ywyEntity.GetName());

			fktjSp.setSelection(Integer.parseInt(cgdEntity.GetFktjNo()));
			ysfsSp.setSelection(Integer.parseInt(cgdEntity.GetYsfsNo()));
			ddKindSp.setSelection(Integer.parseInt(cgdEntity.GetDdKindNo()));
			djEt.setText(String.valueOf(cgdEntity.GetDj()));
			yfEt.setText(String.valueOf(cgdEntity.GetYf()));
			hlEt.setText(String.valueOf(cgdEntity.GetHl()));
			dzEt.setText(cgdEntity.GetDhAdress());
			bzEt.setText(cgdEntity.GetBz());

			DisableView();
		}
	}

	// 选择制单日期
	DatePickerDialog.OnDateSetListener dateDialog = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			cgdEntity.SetCreateTime(year + "年" + (monthOfYear + 1) + "月"
					+ dayOfMonth + "日");

			selectTimeBtn.setText(cgdEntity.GetCreateTime());
		}
	};

	private class categorySpinnerClick implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			cgdEntity.SetCategory(CATEGORYS[arg2]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	private class jsfsSpinnerClick implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 != 0) {
				cgdEntity.SetFkfsNo(String.format("%1$03d", arg2));
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

	private class xmSpinnerClick implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int i, long l) {
			cgdEntity.SetXMNo(CGQYS[i]);
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapterView) {
		}
	}

	private class ddKindSpinnerClick implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 != 0) {
				cgdEntity.SetDDKindNo(String.format("%1$05d", arg2));
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	public class ysfsSpinnerCLick implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2 != 0) {
				cgdEntity.SetYsfsNo(String.format("%1$05d", arg2));
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	private class fktjSpinnerClick implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			if (arg2 != 0) {
				cgdEntity.SetFktjNo(String.format("%1$05d", arg2));
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	private void DisableView() {
		checkStautsBtn.setEnabled(false);
		selectTimeBtn.setEnabled(false);
		categorySp.setEnabled(false);
		ghsBtn.setEnabled(false);
		jsfsSp.setEnabled(false);
		xmSp.setEnabled(false);
		bumenBtn.setEnabled(false);
		ywyEt.setEnabled(false);
		fktjSp.setEnabled(false);
		ysfsSp.setEnabled(false);
		ddKindSp.setEnabled(false);
		djEt.setEnabled(false);
		yfEt.setEnabled(false);
		hlEt.setEnabled(false);
		bzEt.setEnabled(false);
		dzEt.setEnabled(false);
		wlBtn.setEnabled(false);
		submitBtn.setEnabled(false);
	}

	private void EnableView() {
		if (Login.userLevel == 2) {
			// 如果当前登陆地是管理员，则可以对订单进行审核和弃审
			checkStautsBtn.setEnabled(true);
			submitBtn.setEnabled(true);
			title.setText("审核订单");
		} else if (Login.userNo.equals(cgdEntity.GetYwyNo())) {
			// 如果订单未审核，且是属于当前登录的业务员编写，则可以编辑
			if ((cgdEntity.GetChecker() == null || cgdEntity.GetChecker()
					.isEmpty())) {
				title.setText("编辑订单");
				selectTimeBtn.setEnabled(true);
				categorySp.setEnabled(true);
				ghsBtn.setEnabled(true);
				jsfsSp.setEnabled(true);
				xmSp.setEnabled(true);
				bumenBtn.setEnabled(true);
				ywyEt.setEnabled(true);
				fktjSp.setEnabled(true);
				ysfsSp.setEnabled(true);
				ddKindSp.setEnabled(true);
				djEt.setEnabled(true);
				yfEt.setEnabled(true);
				hlEt.setEnabled(true);
				bzEt.setEnabled(true);
				dzEt.setEnabled(true);
				wlBtn.setEnabled(true);
				submitBtn.setEnabled(true);
				isEdited = true;
			} else {
				ShowAttentionDialog("订单已审核，无法编辑！");
			}
		}
	}

	public void ShowAttentionDialog(String msg) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(CreateOrder.this);
		dialog.setTitle("提示");
		dialog.setMessage(msg);
		dialog.setPositiveButton("确认", null);
		dialog.create().show();
	}

	public void ShowConfirmDialog(String msg, final boolean isRefreshList) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(CreateOrder.this);
		dialog.setTitle("提示");
		dialog.setMessage(msg);
		dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (isRefreshList) {
					setResult(RESULT_OK);
				}
				finish();

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
