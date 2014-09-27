package order.data.handler;

import java.util.ArrayList;
import java.util.List;

import order.data.factory.DBHelper;
import order.data.factory.GHSEntity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class GHSHandler {
	private Context context;

	public GHSHandler(Context context) {
		this.context = context;
	}

	public List<GHSEntity> GetAllGhs() {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<GHSEntity> entities = new ArrayList<GHSEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("ghs", new String[] { "no", "name", "category" }, null, null,
				null, null, "no", null);
		while (c.moveToNext()) {
			String no = c.getString(0);
			String name = c.getString(1);
			int category = c.getInt(2);
			GHSEntity d = new GHSEntity(no, name, category);
			entities.add(d);
		}

		c.close();
		db.close();

		return entities;
	}

	public GHSEntity GetGhsByNo(String _no) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("ghs", new String[] { "no", "name", "category" }, "no =?", new String[] { (_no) + "" }, null,
				null, null, null);

		GHSEntity entity = null;

		c.moveToFirst();
		String no = c.getString(0);
		String name = c.getString(1);
		int category = c.getInt(2);
		entity = new GHSEntity(no, name, category);
		c.close();
		db.close();

		return entity;
	}

	public List<GHSEntity> GetGhsByCategory(int _category) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<GHSEntity> entities = new ArrayList<GHSEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("ghs", new String[] { "no", "name", "category" }, "category=?", new String[] { (_category) + "" },
				null, null, "no", null);

		while (c.moveToNext()) {
			String no = c.getString(0);
			String name = c.getString(1);
			int category = c.getInt(2);
			GHSEntity d = new GHSEntity(no, name, category);
			entities.add(d);
		}

		c.close();
		db.close();

		return entities;
	}

}
