package order.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import order.data.factory.BMEntity;
import order.data.factory.CGDEntity;
import order.data.handler.BMHandler;
import order.data.handler.CGDHandler;
import order.activity.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow;

public class OrderFilter extends Activity {
	
	private final String[] CHECK_STATUS={"全部","未审核","已审核"};
	private Spinner checkStatusSp;
	private Button startDateBtn;
	private Button endDateBtn;
	private Button submitBtn;
	
	private String startDate = "";
	private String endDate = "";
	private int checkStatus;
	
	private Calendar startCalendar = Calendar.getInstance();
	private Calendar endCalendar = Calendar.getInstance();
	
	public static List<CGDEntity> filterCgdEntities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_filter);

		checkStatusSp = (Spinner)findViewById(R.id.filterCheckStatus);
		startDateBtn = (Button)findViewById(R.id.filterStartDate);
		endDateBtn = (Button)findViewById(R.id.filterEndDate);
		submitBtn = (Button)findViewById(R.id.submitFilterBtn);
		
		filterCgdEntities = new ArrayList<CGDEntity>();
		
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (startDate.isEmpty())
					startDate = "1900/1/1";
				if(endDate.isEmpty())
					endDate = "9999/1/1";
				
				CGDHandler cgdHandler = new CGDHandler(OrderFilter.this);
				//如果是管理员，则获取所有订单筛选结果
				if (Login.userLevel == 2) {
					filterCgdEntities = cgdHandler.GetCgdByFilter("",checkStatus, startDate, endDate);
				}else {
					//如果是业务员则根据自己的编号筛选
					filterCgdEntities = cgdHandler.GetCgdByFilter(Login.userNo,checkStatus, startDate, endDate);
				}
				
				setResult(RESULT_OK);
				finish();
			}
		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, CHECK_STATUS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		checkStatusSp.setAdapter(adapter);
		checkStatusSp.setOnItemSelectedListener(new CheckStatusSpinnerClick());
		
		startDateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new DatePickerDialog(OrderFilter.this, startDateDialog,
						startCalendar.get(Calendar.YEAR), startCalendar
								.get(Calendar.MONTH), startCalendar
								.get(Calendar.DAY_OF_MONTH)).show();
				
			}
		});
		endDateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new DatePickerDialog(OrderFilter.this, endDateDialog,
						endCalendar.get(Calendar.YEAR), endCalendar
								.get(Calendar.MONTH), endCalendar
								.get(Calendar.DAY_OF_MONTH)).show();
				
			}
		});

	}
	DatePickerDialog.OnDateSetListener startDateDialog = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startCalendar.set(Calendar.YEAR, year);
			startCalendar.set(Calendar.MONTH, monthOfYear);
			startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			startDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

			startDateBtn.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
		}
	};

	DatePickerDialog.OnDateSetListener endDateDialog = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			endCalendar.set(Calendar.YEAR, year);
			endCalendar.set(Calendar.MONTH, monthOfYear);
			endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			endDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth + "/";

			endDateBtn.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
		}
	};
	
	public class CheckStatusSpinnerClick implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			checkStatus = arg2 - 1;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

}
