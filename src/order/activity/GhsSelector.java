package order.activity;

import java.util.ArrayList;
import java.util.List;

import order.data.factory.GHSEntity;
import order.data.handler.GHSHandler;
import order.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class GhsSelector extends Activity {

	private static final String[] CATEGORY_STRINGS = { "全部", "供应商", "代理商",
			"经销商", "直销商", "零售", "职工销售", "集团消费", "区域销售" };
	private static String[] GHS_STRINGS = null;
	private ArrayAdapter<String> adapter;
	private Spinner categorySp = null;
	private Spinner ghsSp = null;
	private Button submitBtn = null;
	private String ghs = "";
	private GHSHandler ghsHandler = new GHSHandler(GhsSelector.this);

	private List<GHSEntity> ghsEntities = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ghs_selector);

		categorySp = (Spinner) findViewById(R.id.ghsMain);
		ghsSp = (Spinner) findViewById(R.id.ghsSub);
		submitBtn = (Button) findViewById(R.id.submitGhsBtn);

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!ghs.isEmpty()) {
					Bundle bundle = new Bundle();
					bundle.putString("ghs", ghs);
					System.out.println(ghs);
					setResult(RESULT_OK, GhsSelector.this.getIntent()
							.putExtras(bundle));
				} else {
					setResult(RESULT_CANCELED);

				}
				finish();
			}
		});

		// 供货商数据
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, CATEGORY_STRINGS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySp.setAdapter(adapter);
		categorySp.setOnItemSelectedListener(new categorySpinnerClick());

	}

	public class categorySpinnerClick implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ghsEntities = new ArrayList<GHSEntity>();
			if (arg2 == 0) {
				ghsEntities = ghsHandler.GetAllGhs();
			}else {
				ghsEntities = ghsHandler.GetGhsByCategory(arg2);
			}
			
			if (!ghsEntities.isEmpty()) {
				GHS_STRINGS = new String[ghsEntities.size() + 1];
				GHS_STRINGS[0] = "请选择";
				for (int i = 0; i < ghsEntities.size(); i++) {
					GHS_STRINGS[i + 1] = ghsEntities.get(i).GetName();
				}
				ghsSp.setEnabled(true);
			} else {
				GHS_STRINGS = new String[1];
				GHS_STRINGS[0] = "无供应商";
				ghsSp.setEnabled(false);
			}

			adapter = new ArrayAdapter<String>(GhsSelector.this,
					android.R.layout.simple_spinner_item, GHS_STRINGS);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			ghsSp.setAdapter(adapter);

			ghsSp.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (arg2 != 0) {
						ghs = ghsEntities.get(arg2 - 1).GetNo() + "-"
								+ ghsEntities.get(arg2 - 1).GetName();
					} else {
						ghs = "";
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}

			});
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

}
