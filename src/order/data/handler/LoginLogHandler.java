package order.data.handler;

import order.data.factory.DBHelper;
import order.data.factory.LoginLogEntity;
import order.data.factory.YWYEntity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoginLogHandler {
	private Context context;

	public LoginLogHandler(Context context) {
		this.context = context;
	}
	
	public void Save(LoginLogEntity loginEntity) {
		DeleteAll();
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("no", loginEntity.GetUserNo());
		cv.put("name", loginEntity.GetUserName());
		db.insert("login_log", " ", cv);
		db.close();
	}
	

	public void DeleteAll() {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("login_log", "1", null);
		db.close();
	}
	
	public LoginLogEntity GetLastLog() {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("login_log", new String[] { "no", "name"},
				null, null, null, null, null, null);

		LoginLogEntity entity = null;

		if (c.moveToFirst()) {
			String no = c.getString(0);
			String name = c.getString(1);
			entity = new LoginLogEntity(no, name);
		}

		c.close();
		db.close();

		return entity;
	}
}
