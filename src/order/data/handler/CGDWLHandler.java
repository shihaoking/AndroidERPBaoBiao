package order.data.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import order.data.factory.CGDWLEntity;
import order.data.factory.DBHelper;
import order.http.base.DataDowlond;
import order.http.base.HttpDownload;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class CGDWLHandler {
	private Context context;

	public CGDWLHandler(Context context) {
		this.context = context;
	}

	public void Save(CGDWLEntity cgdwlEntity) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("cgd_no", cgdwlEntity.GetCgdNo());
		cv.put("no", cgdwlEntity.GetNo());
		cv.put("masterid", cgdwlEntity.GetMasterId());
		cv.put("price", cgdwlEntity.GetPrice());
		cv.put("count", cgdwlEntity.GetCount());
		cv.put("xqdate", cgdwlEntity.GetXqDate());
		cv.put("dhdate", cgdwlEntity.GetDhDate());

		db.insert("cgd_wl", " ", cv);
		db.close();
	}

	public void DeleteByNo(int no) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("cgd_wl", "no=?", new String[] { no + "" });
		db.close();
	}

	public void DeleteByCgdNo(String cgdNo) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("cgd_wl", "cgd_no=?", new String[] { cgdNo + "" });
		db.close();
	}

	public void DeleteAll() {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("cgd_wl", "1", null);
		db.close();
	}

	public void Update(CGDWLEntity cgdwlEntity) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("no", cgdwlEntity.GetNo());
		cv.put("masterid", cgdwlEntity.GetMasterId());
		cv.put("price", cgdwlEntity.GetPrice());
		cv.put("count", cgdwlEntity.GetCount());
		cv.put("xqdate", cgdwlEntity.GetXqDate());
		cv.put("dhdate", cgdwlEntity.GetDhDate());

		db.update("cgd_wl", cv, "cgd_no=?", new String[] { cgdwlEntity.GetNo()
				+ "" });
		db.close();
	}

	public List<CGDWLEntity> GetCgdWlByCgaNo(String _cgdNo) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<CGDWLEntity> list = new ArrayList<CGDWLEntity>();

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("cgd_wl", new String[] { "cgd_no", "no",
				"masterid", "price", "count", "xqdate", "dhdate" }, "cgd_no=?",
				new String[] { _cgdNo }, null, null, null, null);
		while (c.moveToNext()) {
			String cgdNo = c.getString(0);
			String no = c.getString(1);
			String masterId = c.getString(2);
			double price = c.getDouble(3);
			double count = c.getDouble(4);
			String xqDate = c.getString(5);
			String dhDate = c.getString(6);

			CGDWLEntity cgdwlEntity = new CGDWLEntity();
			cgdwlEntity.SetCgdNo(cgdNo);
			cgdwlEntity.SetNo(no);
			cgdwlEntity.SetMasterId(masterId);
			cgdwlEntity.SetPrice(price);
			cgdwlEntity.SetCount(count);
			cgdwlEntity.SetXqDate(xqDate);
			cgdwlEntity.SetDhDate(dhDate);

			list.add(cgdwlEntity);
		}
		c.close();
		db.close();

		if (list.isEmpty()) {
			list = UpdateCGDDataFromServer(_cgdNo);
		}

		return list;
	}

	public List<CGDWLEntity> UpdateCGDDataFromServer(String cgdNo) {
		List<CGDWLEntity> cgdwlEntities = new ArrayList<CGDWLEntity>();
		if (!HttpDownload.CheckNetAvailable(context)) {
			Toast.makeText(context, "当前网络连接不可用，无法更新订单物料数据！", Toast.LENGTH_LONG)
					.show();
		} else {
			try {
				cgdwlEntities = DataDowlond
						.GetCgdWlData("select * from Cggl_dingdanhw where Danhao1='"
								+ cgdNo + "'");
				if (!cgdwlEntities.isEmpty()) {
					DeleteAll();
					for (CGDWLEntity cgdwlEntity : cgdwlEntities) {
						Save(cgdwlEntity);
					}
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cgdwlEntities;

	}
}
