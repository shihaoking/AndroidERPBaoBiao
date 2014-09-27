package order.data.handler;

import java.util.ArrayList;
import java.util.List;

import order.data.factory.DBHelper;
import order.data.factory.WLCategoryEntity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WLCategoryHandler {
	private Context context;

	public WLCategoryHandler(Context context) {
		this.context = context;
	}

	public List<WLCategoryEntity> GetWlCategoryByParentKey(int _parentKey) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<WLCategoryEntity> entities = new ArrayList<WLCategoryEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("wl_category", new String[] { "key", "parent_key",
				"name", "category" }, "category=0 and parent_key=?",
				new String[] { _parentKey + "" }, null, null, "key", null);

		while (c.moveToNext()) {
			int key = c.getInt(0);
			int parent_key = c.getInt(1);
			String name = c.getString(2);
			int category = c.getInt(3);
			WLCategoryEntity d = new WLCategoryEntity(key, parent_key, name,
					category);
			entities.add(d);
		}

		c.close();
		db.close();
		
		return entities;
	}

	public List<WLCategoryEntity> GetCpCategoryByParentKey(int _parentKey) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<WLCategoryEntity> entities = new ArrayList<WLCategoryEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor c = db.query("wl_category", new String[] { "key", "parent_key",
				"name", "category" }, "category=1 and parent_key=?",
				new String[] { _parentKey + "" }, null, null, "key", null);

		while (c.moveToNext()) {
			int key = c.getInt(0);
			int parent_key = c.getInt(1);
			String name = c.getString(2);
			int category = c.getInt(3);
			WLCategoryEntity d = new WLCategoryEntity(key, parent_key, name,
					category);
			entities.add(d);
		}

		c.close();
		db.close();

		return entities;
	}

	public List<WLCategoryEntity> GetCategoryByParentKeyAndCategory(
			int _parentKey, int _category) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<WLCategoryEntity> entities = new ArrayList<WLCategoryEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("wl_category", new String[] { "key", "parent_key",
				"name", "category" }, "category=? and parent_key=?",
				new String[] { _category + "", _parentKey + "" }, null, null,
				"key", null);

		while (c.moveToNext()) {
			int key = c.getInt(0);
			int parent_key = c.getInt(1);
			String name = c.getString(2);
			int category = c.getInt(3);
			WLCategoryEntity d = new WLCategoryEntity(key, parent_key, name,
					category);
			entities.add(d);
		}

		c.close();
		db.close();

		return entities;
	}

}
