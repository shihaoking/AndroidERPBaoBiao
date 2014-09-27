package order.data.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import order.data.factory.CGDEntity;
import order.data.factory.DBHelper;
import order.http.base.DataDowlond;
import order.http.base.DataUpload;
import order.http.base.HttpDownload;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class CGDHandler {
	private Context context;

	public CGDHandler(Context context) {
		this.context = context;
	}

	public void Save(CGDEntity cgdEntity) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("no", cgdEntity.GetNo());
		cv.put("category", cgdEntity.GetCategory());
		cv.put("ywyNo", cgdEntity.GetYwyNo());
		cv.put("checker", cgdEntity.GetChecker());
		cv.put("createtime", cgdEntity.GetCreateTime().toString());
		db.insert("cgd_main", " ", cv);
		db.close();
	}

	public List<CGDEntity> GetCgdByFilter(String _ywyNo, int checkStatus,
			String startDate, String endDate) {
		DBHelper dbHelper = new DBHelper(context, 2);
		List<CGDEntity> list = new ArrayList<CGDEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String filterString = "";

		if (!_ywyNo.isEmpty()) {
			filterString = "ywyNo='" + _ywyNo + "' and ";
		}

		if (checkStatus == 0) { // 选取未审核订单
			filterString += "(checker is null or checker = '') and ";
		} else if (checkStatus == 1) { // 选取已审核订单
			filterString += "(checker is not null and checker <> '') and ";
		}

		Cursor c = db.query("cgd_main", new String[] { "no", "category",
				"ywyNo", "checker", "createtime" }, filterString
				+ "createtime >= ? and createtime <=?", new String[] {
				startDate, endDate }, null, null, "createtime desc", null);

		while (c.moveToNext()) {
			String no = c.getString(0);
			String category = c.getString(1);
			String ywyNo = c.getString(2);
			String checker = c.getString(3);
			String createTime = c.getString(4);
			CGDEntity d = new CGDEntity(no, category, ywyNo, checker,
					createTime);
			list.add(d);
		}
		c.close();
		db.close();
		return list;
	}

	public void DeleteAll() {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("cgd_main", "1", null);
		db.close();
	}

	public void DeleteByYwyNo(String ywyNo) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("cgd_main", "ywyNo=?", new String[] { ywyNo });
		db.close();
	}
	
	public void DeleteByNo(String no) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("cgd_main", "no=?", new String[] { no });
		DataUpload.DeleteCgdByNo(no);
		db.close();
	}

	public void update(CGDEntity cgdEntity) {
		DBHelper dbHelper = new DBHelper(context, 2);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("category", cgdEntity.GetCategory());
		cv.put("ywyNo", cgdEntity.GetYwyNo());
		cv.put("checker", cgdEntity.GetChecker());
		cv.put("createtime", cgdEntity.GetCreateTime().toString());
		db.update("cgd_main", cv, "no=?",
				new String[] { cgdEntity.GetNo() + "" });
		db.close();
	}

	public List<CGDEntity> GetCgdMainByYwyNo(String _ywyNo) {
		CheckData();
		DBHelper dbHelper = new DBHelper(context, 2);
		List<CGDEntity> list = new ArrayList<CGDEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("cgd_main", new String[] { "no", "category",
				"ywyNo", "checker", "createtime" }, "ywyNo=?",
				new String[] { _ywyNo }, null, null, "createtime desc", null);
		while (c.moveToNext()) {
			String no = c.getString(0);
			String category = c.getString(1);
			String ywyNo = c.getString(2);
			String checker = c.getString(3);
			String createTime = c.getString(4);
			CGDEntity d = new CGDEntity(no, category, ywyNo, checker,
					createTime);
			list.add(d);
		}
		c.close();
		db.close();
		return list;
	}

	public List<CGDEntity> getAllCgd() {
		CheckData();
		DBHelper dbHelper = new DBHelper(context, 2);
		List<CGDEntity> list = new ArrayList<CGDEntity>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("cgd_main", new String[] { "no", "category",
				"ywyNo", "checker", "createtime" }, null, null, null, null,
				"createtime desc", null);
		while (c.moveToNext()) {
			String no = c.getString(0);
			String category = c.getString(1);
			String ywyNo = c.getString(2);
			String checker = c.getString(3);
			String createTime = c.getString(4);
			CGDEntity d = new CGDEntity(no, category, ywyNo, checker,
					createTime);
			list.add(d);
		}
		c.close();
		db.close();
		return list;
	}

	public CGDEntity getCgdByNo(String no) {
		DBHelper dbHelper = new DBHelper(context, 2);

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("cgd_main", new String[] { "category", "ywyNo",
				"checker", "createtime" }, "no =?", new String[] { (no) + "" },
				null, null, "createtime desc", null);

		CGDEntity entity = null;

		if (c.moveToFirst()) {
			String category = c.getString(0);
			String ywyNo = c.getString(1);
			String checker = c.getString(2);
			String createTime = c.getString(3);
			entity = new CGDEntity(no, category, ywyNo, checker, createTime);
		}

		c.close();
		db.close();
		return entity;
	}

	public List<CGDEntity> UpdateCGDDataFromServer(String ywyNo) {
		List<CGDEntity> cgdEntities = new ArrayList<CGDEntity>();
		if (!HttpDownload.CheckNetAvailable(context)) {
			Toast.makeText(context, "当前网络连接不可用，无法更新采购订单数据！", Toast.LENGTH_LONG)
					.show();
		} else {
			try {
				String sql = "select * from Cggl_dingdan";

				if (!ywyNo.isEmpty()) {
					sql += " where YwyNo='" + ywyNo + "'";
				}
				
				sql += " order by Date_1 desc";

				cgdEntities = DataDowlond.GetCGDMainData(sql);
				if (!cgdEntities.isEmpty()) {
					if (ywyNo.isEmpty()) {
						DeleteAll();
					} else {
						DeleteByYwyNo(ywyNo);
					}
					for (CGDEntity cgdEntity : cgdEntities) {
						Save(cgdEntity);
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
		return cgdEntities;
	}

	public void CheckData() {
		DBHelper dbHelper = new DBHelper(context, 2);

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query("cgd_main", new String[] { "count(*)" }, null,
				null, null, null, null, null);
		if (c.moveToFirst()) {
			int count = c.getInt(0);
			if (count == 0) {
				UpdateCGDDataFromServer("");
			}
		}
		db.close();
	}
}
