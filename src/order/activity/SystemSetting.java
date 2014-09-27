package order.activity;

import order.data.handler.YWYHandler;
import order.http.base.HttpDownload;
import order.http.base.SendSms;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SystemSetting extends Activity{
	
	private EditText managerPhoneEt;
	private EditText ywyPhoneEt;
	private EditText serverUrlEt;
	
	private Button submitBtn;
	private Button backBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_setting);
	
		Login.activityList.add(this);
		
		
		YWYHandler ywyHandler = new YWYHandler(this);
		ywyHandler.CheckYwyData();
		
		managerPhoneEt = (EditText)findViewById(R.id.managerPhone);
		ywyPhoneEt = (EditText)findViewById(R.id.ywyPhone);
		serverUrlEt = (EditText)findViewById(R.id.serverUrl);
		
		submitBtn = (Button)findViewById(R.id.submitSetting);
		backBtn = (Button)findViewById(R.id.backToMain);
		
		managerPhoneEt.setText(SendSms.mphoneBumber);
		ywyPhoneEt.setText(SendSms.yphoneNumber);
		serverUrlEt.setText(HttpDownload.serverUrl);
		
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (managerPhoneEt.getText() != null && !managerPhoneEt.getText().toString().isEmpty()) {
					SendSms.mphoneBumber = managerPhoneEt.getText().toString();
				}
				
				if (ywyPhoneEt.getText() != null && !ywyPhoneEt.getText().toString().isEmpty()) {
					SendSms.yphoneNumber = ywyPhoneEt.getText().toString();
				}
				
				if (serverUrlEt.getText() != null && !serverUrlEt.getText().toString().isEmpty()) {
					HttpDownload.SetServerUrl(serverUrlEt.getText().toString());
				}
				
				finish();
			}
		});
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
}
