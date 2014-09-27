package order.data.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import order.data.factory.DBHelper;
import order.data.factory.WLEntity;
import order.data.factory.YWYEntity;
import order.http.base.DataDowlond;
import order.http.base.HttpDownload;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class YWYHandler {
	private Context context;

	public YWYHandler(Context context) {
		this.context = context;
	}

	public void Save(YWYEntity ywyEntity) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("no", ywyEntity.GetNo());
		cv.put("name", ywyEntity.GetName());
		cv.put("level", ywyEntity.GetLevel());
		db.insert("ywy", " ", cv);
		db.close();
	}

	public YWYEntity GetYWYByNo(String _no) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("ywy", new String[] { "no", "name", "level" },
				"no =?", new String[] { (_no) + "" }, null, null, null, null);

		YWYEntity entity = null;

		if (c.moveToFirst()) {
			String no = c.getString(0);
			String name = c.getString(1);
			int level = c.getInt(2);
			entity = new YWYEntity(no, name, level);
		}

		c.close();
		db.close();

		return entity;
	}
	
	public YWYEntity GetYWYByName(String _name) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("ywy", new String[] { "no", "name", "level" },
				"name =?", new String[] { (_name) + "" }, null, null, null, null);

		YWYEntity entity = null;

		if (c.moveToFirst()) {
			String no = c.getString(0);
			String name = c.getString(1);
			int level = c.getInt(2);
			entity = new YWYEntity(no, name, level);
		}

		c.close();
		db.close();

		return entity;
	}

	public List<YWYEntity> GetYWYByLevel(int _level) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<YWYEntity> entities = new ArrayList<YWYEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("ywy", new String[] { "no", "name", "level" },
				"level=?", new String[] { (_level) + "" }, null, null, "no",
				null);

		while (c.moveToNext()) {
			String no = c.getString(0);
			String name = c.getString(1);
			int level = c.getInt(2);
			YWYEntity d = new YWYEntity(no, name, level);
			entities.add(d);
		}

		c.close();
		db.close();

		return entities;
	}
	public void CheckYwyData() {
		DBHelper dbHelper = new DBHelper(context, 2);

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("ywy", new String[] { "count(*)" }, null, null,
				null, null, null, null);
		if (c.moveToFirst()) {
			int count = c.getInt(0);
			if (count == 0) {
				UpdateYwyFromServer();
			}
		}
		c.close();
		db.close();
	}

	public List<YWYEntity> UpdateYwyFromServer() {
		List<YWYEntity> ywyEntities = new ArrayList<YWYEntity>();
		if (!HttpDownload.CheckNetAvailable(context)) {
			Toast.makeText(context, "当前网络连接不可用，无法更新业务员数据！", Toast.LENGTH_LONG)
					.show();
		} else {
			DBHelper dbHelper = new DBHelper(context, 2);
			try {
				ywyEntities = DataDowlond
						.GetYwyData("SELECT a.ygbh no,a.yg_name name,b.all_spr access FROM rlzy_Rlzybase a"
								+ " left join Xt_DJSPLC b on b.BillKind = 'CGDD' and b.all_spr like '%'/a.ygbh/'%'");
				for (YWYEntity ywyEntity : ywyEntities) {
					Save(ywyEntity);
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ywyEntities;
	}

}
