package order.data.factory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static String dbName = "cg_db";

	public DBHelper(Context context, int version) {
		super(context, dbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		CreateLoginLog(db);
		CreateCgdTable(db);
		CreateCgdWlTable(db);
		CreateBmTable(db);
		CreateGhsTable(db);
		CreateYwyTable(db);
		CreateWlTable(db);
		CreateWlCategoryTable(db);
	}

	public void CreateLoginLog(SQLiteDatabase db) {
		db.execSQL("create table login_log(_id integer primary key autoincrement,no varchar(10),name varchar(50))");
	}
	
	public void CreateCgdTable(SQLiteDatabase db) {
		db.execSQL("create table cgd_main(_id integer primary key  autoincrement,no varchar(20),category varchar(20),checker varcahr(30),ywyNo varchar(8),createtime datetime)");
	}
	
	public void CreateCgdWlTable(SQLiteDatabase db) {
		db.execSQL("create table cgd_wl(_id integer primary key autoincrement,cgd_no nvarchar(20),no nvarchar(15),"
				+ "masterid nvarchar(4),price double,count double,xqdate datetime,dhdate datetime)");
	}

	public void CreateBmTable(SQLiteDatabase db) {
		db.execSQL("create table bm(_id integer primary key  autoincrement,no varchar(10),name varchar(20),parent_key int,key int)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0001\",\"总经理室\",1,2)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0002\",\"财务部\",1,3)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0003\",\"生产部\",1,4)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0004\",\"熔铸车间\",4,5)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0006\",\"小熔铸车间\",4,6)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0007\",\"化验室\",1,7)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0008\",\"仓管部\",1,8)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0009\",\"原材料仓\",8,9)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0010\",\"成品仓\",8,10)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0011\",\"物料仓\",8,11)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0012\",\"业务部\",1,12)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0013\",\"品管部\",4,13)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0014\",\"董事长室\",1,14)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0015\",\"后勤部\",1,15)");
		db.execSQL("insert into bm(no,name,parent_key,key) values(\"BM0016\",\"采购部\",1,16)");
	}

	public void CreateGhsTable(SQLiteDatabase db) {
		db.execSQL("create table ghs(_id integer primary key  autoincrement,no varchar(10),name varchar(50),category integer)");
		db.execSQL("insert into ghs(no,name,category) values(\"00000001\",\"金创利\",1)");
		db.execSQL("insert into ghs(no,name,category) values(\"00000002\",\"高鸿\",1)");
		db.execSQL("insert into ghs(no,name,category) values(\"00000003\",\"金喜\",1)");
		db.execSQL("insert into ghs(no,name,category) values(\"00000005\",\"泰丰源\",5)");
		db.execSQL("insert into ghs(no,name,category) values(\"00000009\",\"陈跃湘\",1)");
		db.execSQL("insert into ghs(no,name,category) values(\"00000010\",\"张生\",1)");
	}

	public void CreateYwyTable(SQLiteDatabase db) {
		db.execSQL("create table ywy(_id integer primary key  autoincrement,no varchar(10),name varchar(50),level integer)");
	}

	public void CreateWlCategoryTable(SQLiteDatabase db) {
		db.execSQL("create table wl_category(_id integer primary key autoincrement,key integer,parent_key integer,name varchar(50),category integer)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(1,0,\"五金物料类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(2,1,\"五金类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(3,1,\"物料类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(4,0,\"原材料类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(5,4,\"原料类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(6,4,\"燃料类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(7,4,\"辅助材料类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(9,1,\"工具类\",0)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(1,0,\"实心棒类\",1)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(2,1,\"178铝棒\",1)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(3,1,\"127铝棒\",1)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(4,1,\"114铝棒\",1)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(5,0,\"铝型材类\",1)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(6,0,\"空心管类\",1)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(7,6,\"铝管\",1)");
		db.execSQL("insert into wl_category(key,parent_key,name,category) values(8,1,\"228铝棒\",1)");
	}

	public void CreateWlTable(SQLiteDatabase db) {
		db.execSQL("create table wl(_id integer primary key autoincrement,no nvarchar(20),name varchar(50),level nvarchar(10),unit nvarchar(10),category integer,kind integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
