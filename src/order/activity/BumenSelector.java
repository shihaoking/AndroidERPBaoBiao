package order.activity;

import java.util.ArrayList;
import java.util.List;
import order.data.factory.BMEntity;
import order.data.handler.BMHandler;
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
import android.widget.TableRow;

public class BumenSelector extends Activity {

	private static String[] BUMEN_STRINGS = null;
	private static String[] SUBBM_STRINGS = null;
	private ArrayAdapter<String> adapter;
	private Spinner bumenMainSp = null;
	private Spinner bumenSubSp = null;
	private Button submitBtn = null;
	private TableRow subRow = null;
	private String bumen = "";
	private BMHandler bmHandle = new BMHandler(BumenSelector.this);

	private List<BMEntity> bmEntities = null;
	private List<BMEntity> subBmEntities = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bumen_selector);
		bumenMainSp = (Spinner) findViewById(R.id.bumenMain);
		bumenSubSp = (Spinner) findViewById(R.id.bumenSub);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		subRow = (TableRow) findViewById(R.id.subRow);

		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!bumen.isEmpty()) {
					Bundle bundle = new Bundle();
					bundle.putString("bumen", bumen);
					System.out.println(bumen);
					setResult(RESULT_OK,
							BumenSelector.this.getIntent().putExtras(bundle));
				}else {
					setResult(RESULT_CANCELED);

				}
				finish();
			}
		});

		// 采购部门
		bmEntities = new ArrayList<BMEntity>();
		bmEntities = bmHandle.GetAllBm();
		BUMEN_STRINGS = new String[bmEntities.size() + 1];
		BUMEN_STRINGS[0] = "请选择";
		for (int i = 0; i < bmEntities.size(); i++) {
			BUMEN_STRINGS[i + 1] = bmEntities.get(i).GetName();
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, BUMEN_STRINGS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bumenMainSp.setAdapter(adapter);
		bumenMainSp.setOnItemSelectedListener(new BumenSpinnerClick());

	}

	public class BumenSpinnerClick implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			final int mainArg = arg2;
			if (arg2 != 0) {
				subBmEntities = new ArrayList<BMEntity>();
				subBmEntities = bmHandle.GetSubBmByNo(bmEntities.get(arg2 - 1)
						.GetNo());
				if (subBmEntities.isEmpty()) {
					bumen = bmEntities.get(arg2 - 1).GetNo() + "-"
							+ BUMEN_STRINGS[arg2];
				} else {
					SUBBM_STRINGS = new String[subBmEntities.size() + 1];
					SUBBM_STRINGS[0] = "请选择";
					
					for (int i = 0; i < SUBBM_STRINGS.length - 1; i++) {
						SUBBM_STRINGS[i + 1] = subBmEntities.get(i).GetName();
					}
					adapter = new ArrayAdapter<String>(BumenSelector.this,
							android.R.layout.simple_spinner_item, SUBBM_STRINGS);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					bumenSubSp.setAdapter(adapter);

					subRow.setVisibility(View.VISIBLE);

					bumenSubSp
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									if (arg2 != 0) {
										bumen = subBmEntities.get(arg2 - 1).GetNo()
												+ "-" + SUBBM_STRINGS[arg2];
									}else {
										bumen = bmEntities.get(mainArg - 1).GetNo() + "-"
												+ BUMEN_STRINGS[mainArg];
									}
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
								}

							});
				}
			} else {
				bumen = "";
				subRow.setVisibility(View.GONE);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}

	}

}
