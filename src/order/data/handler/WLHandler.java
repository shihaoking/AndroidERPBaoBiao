package order.data.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.ClientProtocolException;

import order.data.factory.DBHelper;
import order.data.factory.WLEntity;
import order.http.base.DataDowlond;
import order.http.base.HttpDownload;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class WLHandler {

	private Context context;

	public WLHandler(Context context) {
		this.context = context;
	}

	public List<WLEntity> GetWlByCategory(int _category) {
		return GetWlDataByCategoryAndKind(_category, 0);
	}

	public List<WLEntity> GetCpByCategory(int _category) {

		return GetWlDataByCategoryAndKind(_category, 1);
	}

	public List<WLEntity> GetWlDataByCategoryAndKind(int _category, int kind) {
		CheckData();
		DBHelper dbHelper = new DBHelper(context, 2);

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("wl", new String[] { "no", "name", "level", "unit",
				"category" }, "kind=? and category =?", new String[] {
				kind + "", _category + "" }, null, null, null, null);

		List<WLEntity> wlEntities = new ArrayList<WLEntity>();
		while (c.moveToNext()) {
			String no = c.getString(0);
			String name = c.getString(1);
			String level = c.getString(2);
			String unit = c.getString(3);
			int category = c.getInt(4);
			WLEntity entity = new WLEntity(no, name, category, level, unit);
			wlEntities.add(entity);
		}
		c.close();
		db.close();

		return wlEntities;
	}

	public void CheckData() {
		DBHelper dbHelper = new DBHelper(context, 2);

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("wl", new String[] { "count(*)" }, null, null,
				null, null, null, null);
		if (c.moveToFirst()) {
			int count = c.getInt(0);
			if (count == 0) {
				UpdateWlByCatetory();
				UpdateCpByCatetory();
			}
		}
		c.close();
		db.close();
	}

	public WLEntity GetWlByNo(String _no) {
		CheckData();
		DBHelper dbHelper = new DBHelper(context, 2);

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("wl", new String[] { "no", "name", "level", "unit",
				"category" }, "no =?", new String[] { _no }, null, null, null,
				null);

		WLEntity entity = null;

		if (c.moveToFirst()) {
			String no = c.getString(0);
			String name = c.getString(1);
			String level = c.getString(2);
			String unit = c.getString(3);
			int category = c.getInt(4);
			entity = new WLEntity(no, name, category, level, unit);
		}
		c.close();
		db.close();

		return entity;
	}

	public void SaveWl(WLEntity wlEntity) {
		Save(wlEntity, 0);
	}

	public void SaveCp(WLEntity wlEntity) {
		Save(wlEntity, 1);
	}

	public void Save(WLEntity wlEntity, int kind) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("no", wlEntity.GetNo());
		cv.put("name", wlEntity.GetName());
		cv.put("level", wlEntity.GetLevel());
		cv.put("unit", wlEntity.GetUnit());
		cv.put("category", wlEntity.GetCategory());
		cv.put("kind", kind);
		db.insert("wl", " ", cv);
		db.close();
	}

	public List<WLEntity> UpdateWlByCatetory() {
		// 添加新物料数据
		List<WLEntity> wlEntities = DownWlDataFromServer("WL");
		for (WLEntity wlEntity : wlEntities) {
			SaveWl(wlEntity);
		}
		return wlEntities;
	}

	public List<WLEntity> UpdateCpByCatetory() {
		// 添加新物料数据
		List<WLEntity> wlEntities = DownWlDataFromServer("CP");
		for (WLEntity wlEntity : wlEntities) {
			SaveCp(wlEntity);
		}

		return wlEntities;
	}

	public List<WLEntity> DownWlDataFromServer(String target) {
		List<WLEntity> wlEntities = null;

		DBHelper dbHelper = new DBHelper(context, 2);
		if (!HttpDownload.CheckNetAvailable(context)) {
			Toast.makeText(context, "当前网络连接不可用，无法更新物料数据！", Toast.LENGTH_LONG)
					.show();
		} else {

			try {
				wlEntities = DataDowlond.GetWlData("select MasterID," + target
						+ "BM," + target + "MC,ZLDJ,JLDW from SCGL_" + target
						+ "MS", target);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return wlEntities;

	}

}
