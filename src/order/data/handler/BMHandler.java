package order.data.handler;

import java.util.ArrayList;
import java.util.List;

import order.data.factory.BMEntity;
import order.data.factory.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class BMHandler {
	private Context context;

	public BMHandler(Context context) {
		this.context = context;
	}

	public List<BMEntity> GetAllBm() {
		return GetBmSubByKey(1);
	}

	public BMEntity GetBmByNo(String _no) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("bm", new String[] { "no", "name", "key",
				"parent_key" }, "no =?", new String[] { (_no) + "" }, null,
				null, null, null);

		BMEntity entity = null;

		c.moveToFirst();
		String no = c.getString(0);
		String name = c.getString(1);
		int key = c.getInt(2);
		int parentKey = c.getInt(3);
		entity = new BMEntity(no, name, key, parentKey);
		c.close();
		db.close();

		return entity;
	}

	public List<BMEntity> GetBmSubByKey(int _key) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<BMEntity> entities = new ArrayList<BMEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("bm", new String[] { "no", "name", "key",
				"parent_key" }, "parent_key=?", new String[] { (_key) + "" },
				null, null, "no", null);

		while (c.moveToNext()) {
			String no = c.getString(0);
			String name = c.getString(1);
			int key = c.getInt(2);
			int parentKey = c.getInt(3);
			BMEntity d = new BMEntity(no, name, key, parentKey);
			entities.add(d);
		}

		c.close();
		db.close();

		return entities;
	}
	
	public int GetBmKeyByNo(String _no) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("bm", new String[] { "key" }, "no =?", new String[] { (_no) + "" }, null,
				null, null, null);

		c.moveToFirst();
		int key = c.getInt(0);
		c.close();
		db.close();

		return key;
	}
	
	public List<BMEntity> GetSubBmByNo(String _no) {
		int key = GetBmKeyByNo(_no);
		return GetBmSubByKey(key);
	}

}
