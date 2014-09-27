package order.activity;

import java.util.Calendar;
import java.util.List;

import order.data.factory.CGDWLEntity;
import order.data.factory.WLEntity;
import order.data.handler.WLHandler;
import android.R.string;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

public class WlDetailEdit extends Activity {

	private TextView wlNoTv;
	private TextView wlNameTv;
	private TextView wlLevelTv;
	private Button xqDateBtn;
	private Button dhDateBtn;
	private EditText wlPriceEt;
	private EditText wlCountEt;
	private TextView wlPreUniTv;
	private TextView wlUnitTv;

	private Calendar xqCalendar = Calendar.getInstance();
	private Calendar dhCalendar = Calendar.getInstance();

	private Button submitBtn;
	private Button backBtn;

	public List<CGDWLEntity> sltedCgdWl;
	private String wlNo;
	private double wlPrice;
	private double wlCount;
	private String xqDate;
	private String dhDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wl_detail_edit);

		wlNoTv = (TextView) findViewById(R.id.wlDetailNoTv);
		wlNameTv = (TextView) findViewById(R.id.wlDetailNameTv);
		wlLevelTv = (TextView) findViewById(R.id.wlDetailLevelTv);
		wlPriceEt = (EditText) findViewById(R.id.wlDetailPriceEt);
		wlPreUniTv = (TextView) findViewById(R.id.wlDetailPreUnitTv);
		wlCountEt = (EditText) findViewById(R.id.wlDetailCountEt);
		wlUnitTv = (TextView) findViewById(R.id.wlDetailUnitTv);
		xqDateBtn = (Button) findViewById(R.id.wlDetailXqDateSp);
		dhDateBtn = (Button) findViewById(R.id.wlDetailDhDateSp);

		backBtn = (Button) findViewById(R.id.backWlDetailBtn);
		submitBtn = (Button) findViewById(R.id.submitWlDetailBtn);

		initializeViewData();

		Login.activityList.add(this);
		
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wlPrice = Double.parseDouble(wlPriceEt.getText().toString()
						.isEmpty() ? "0" : wlPriceEt.getText().toString());
				wlCount = Double.parseDouble(wlCountEt.getText().toString()
						.isEmpty() ? "0" : wlCountEt.getText().toString());

				if (wlPrice == 0 || wlCount == 0) {
					Toast.makeText(WlDetailEdit.this, "物料的数量和价格不能为零",
							Toast.LENGTH_SHORT).show();
					return;
				}

				for (CGDWLEntity cgdwl : sltedCgdWl) {
					if (cgdwl.GetNo().equals(wlNo)) {
						cgdwl.SetPrice(wlPrice);
						cgdwl.SetCount(wlCount);
						cgdwl.SetDhDate(dhDate);
						cgdwl.SetXqDate(xqDate);
					}
				}
				setResult(RESULT_OK);
				finish();
			}
		});

		// 弹出时间选择Dialog
		xqDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(WlDetailEdit.this, xqDateDialog,
						xqCalendar.get(Calendar.YEAR), xqCalendar
								.get(Calendar.MONTH), xqCalendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		dhDateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DatePickerDialog(WlDetailEdit.this, dhDateDialog,
						dhCalendar.get(Calendar.YEAR), dhCalendar
								.get(Calendar.MONTH), dhCalendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
	}

	DatePickerDialog.OnDateSetListener xqDateDialog = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			xqCalendar.set(Calendar.YEAR, year);
			xqCalendar.set(Calendar.MONTH, monthOfYear);
			xqCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			xqDate = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";

			xqDateBtn.setText(xqDate);
		}
	};

	DatePickerDialog.OnDateSetListener dhDateDialog = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dhCalendar.set(Calendar.YEAR, year);
			dhCalendar.set(Calendar.MONTH, monthOfYear);
			dhCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			dhDate = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";

			dhDateBtn.setText(dhDate);
		}
	};

	private void initializeViewData() {
		sltedCgdWl = CreateOrder.sltedWlEntities;

		Bundle bundle = this.getIntent().getExtras();
		wlNo = bundle.getString("No");
		wlPrice = bundle.getDouble("Price");
		wlCount = bundle.getDouble("Count");
		xqDate = bundle.getString("xqDate");
		dhDate = bundle.getString("dhDate");

		WLHandler wlHandler = new WLHandler(this);
		WLEntity wlInfo = wlHandler.GetWlByNo(wlNo);

		wlNoTv.setText(wlNo);
		wlNameTv.setText(wlInfo.GetName());
		wlLevelTv.setText(wlInfo.GetLevel());
		wlPriceEt.setText(wlPrice == 0 ? "" : wlPrice + "");
		wlPreUniTv.setText("元/" + wlInfo.GetUnit());
		wlCountEt.setText(wlCount == 0 ? "" : wlCount + "");
		wlUnitTv.setText(wlInfo.GetUnit());
		if (xqDate != null && !xqDate.isEmpty()) {
			xqDateBtn.setText(xqDate);
		}
		if (dhDate != null && !dhDate.isEmpty()) {
			dhDateBtn.setText(dhDate);
		}
	}
}
