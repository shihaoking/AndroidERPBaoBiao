package order.http.base;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import order.data.factory.CGDEntity;
import order.data.factory.CGDWLEntity;
import order.data.factory.WLEntity;
import order.data.factory.YWYEntity;
import order.data.handler.WLHandler;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.os.Handler;

public class DataDowlond {

	public static List<CGDEntity> GetCGDMainData(String sql)
			throws ClientProtocolException, IOException {
		String target = "cgd_main";
		String jsonData = HttpDownload.GetServerData("?action=getData&target="
				+ target + "&sql=" + URLEncoder.encode(sql));
		List<CGDEntity> cgdList = new ArrayList<CGDEntity>();

		try {
			JSONObject jsonObject = new JSONObject(jsonData.toString());
			JSONArray jsonArray = jsonObject.getJSONArray(target);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemObject = jsonArray.getJSONObject(i);
				CGDEntity cgdEntity = new CGDEntity();

				cgdEntity.SetNo(itemObject.getString("Danhao"));
				cgdEntity.SetYwyNo(itemObject.getString("YwyNo"));
				cgdEntity.SetCreateTime(itemObject.getString("Date_1"));
				cgdEntity.SetChecker(itemObject.getString("Checker"));
				cgdEntity.SetCategory(itemObject.getString("CGtype"));

				cgdList.add(cgdEntity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage().toString());
		}

		return cgdList;
	}

	public static CGDEntity GetCGDMainDetailData(String sql)
			throws ClientProtocolException, IOException {
		String target = "cgd_detail";
		String jsonData = HttpDownload.GetServerData("?action=getData&target="
				+ target + "&sql=" + URLEncoder.encode(sql));
		CGDEntity cgdEntity = new CGDEntity();
		try {
			JSONObject jsonObject = new JSONObject(jsonData.toString())
					.getJSONObject(target);

			cgdEntity.SetNo(jsonObject.getString("Danhao"));
			cgdEntity.SetDDKindNo(jsonObject.getString("DDKindNo"));
			cgdEntity.SetGhsNo(jsonObject.getString("GhsNo"));
			cgdEntity.SetYwyNo(jsonObject.getString("YwyNo"));
			cgdEntity.SetBumenNo(jsonObject.getString("BumenNo"));
			cgdEntity.SetXMNo(jsonObject.getString("XmNo"));
			cgdEntity.SetFkfsNo(jsonObject.getString("FkfsNo"));
			cgdEntity.SetFktjNo(jsonObject.getString("FktjNo"));
			cgdEntity.SetYsfsNo(jsonObject.getString("YsfsNo"));
			cgdEntity.SetMoneyKind(jsonObject.getString("MoneyKindNo"));
			cgdEntity.SetDhAdress(jsonObject.getString("DhAdress"));
			cgdEntity.SetYf(jsonObject.getDouble("YF"));
			cgdEntity.SetDj(jsonObject.getDouble("DJ"));
			cgdEntity.setHl(jsonObject.getDouble("HL"));
			cgdEntity.SetBz(jsonObject.getString("BZ"));
			cgdEntity.SetChecker(jsonObject.getString("Checker"));
			cgdEntity.SetMaker(jsonObject.getString("Maker"));
			cgdEntity.SetCheckerLc(jsonObject.getString("CheckerLc"));
			cgdEntity.SetCreateTime(jsonObject.getString("Date_1"));
			cgdEntity.SetCategory(jsonObject.getString("CGtype"));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage().toString());
		}

		return cgdEntity;
	}

	public static List<CGDWLEntity> GetCgdWlData(String sql)
			throws ClientProtocolException, IOException {
		String target = "cgd_wl";
		String jsonData = HttpDownload.GetServerData("?action=getData&target="
				+ target + "&sql=" + URLEncoder.encode(sql));
		List<CGDWLEntity> cgdwlList = new ArrayList<CGDWLEntity>();

		try {
			JSONObject jsonObject = new JSONObject(jsonData.toString());
			JSONArray jsonArray = jsonObject.getJSONArray(target);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemObject = jsonArray.getJSONObject(i);
				CGDWLEntity cgdwlEntity = new CGDWLEntity();

				cgdwlEntity.SetCgdNo(itemObject.getString("CgdNo"));
				cgdwlEntity.SetNo(itemObject.getString("No"));
				cgdwlEntity.SetMasterId(itemObject.getString("MasterId"));
				cgdwlEntity.SetPrice(itemObject.getLong("Price"));
				cgdwlEntity.SetCount(itemObject.getLong("Count"));
				cgdwlEntity.SetXqDate(itemObject.getString("XqDate"));
				cgdwlEntity.SetDhDate(itemObject.getString("DhDate"));

				cgdwlList.add(cgdwlEntity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage().toString());
		}

		return cgdwlList;
	}

	public static List<WLEntity> GetWlData(String sql, String target)
			throws ClientProtocolException, IOException {
		String jsonData = HttpDownload.GetServerData("?action=getData&target="
				+ target + "&sql=" + URLEncoder.encode(sql));
		List<WLEntity> wlList = new ArrayList<WLEntity>();

		try {
			JSONObject jsonObject = new JSONObject(jsonData.toString());
			JSONArray jsonArray = jsonObject.getJSONArray(target);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemObject = jsonArray.getJSONObject(i);
				WLEntity wlEntity = new WLEntity();

				wlEntity.SetNo(itemObject.getString("No"));
				wlEntity.SetName(itemObject.getString("Name"));
				wlEntity.SetUnit(itemObject.getString("Unit"));
				wlEntity.SetCategory(itemObject.getInt("Category"));
				wlEntity.SetLevel(itemObject.getString("Level"));

				wlList.add(wlEntity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage().toString());
		}

		return wlList;
	}

	public static List<YWYEntity> GetYwyData(String sql)
			throws ClientProtocolException, IOException {
		
		String jsonData = HttpDownload
				.GetServerData("?action=getData&target=ywy&sql="
						+ URLEncoder.encode(sql));
		
		List<YWYEntity> wlList = new ArrayList<YWYEntity>();

		try {
			JSONObject jsonObject = new JSONObject(jsonData.toString());
			JSONArray jsonArray = jsonObject.getJSONArray("ywy");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject itemObject = jsonArray.getJSONObject(i);
				YWYEntity ywyEntity = new YWYEntity();

				ywyEntity.SetNo(itemObject.getString("No"));
				ywyEntity.SetName(itemObject.getString("Name"));
				ywyEntity.SetLevel(itemObject.getInt("Level"));

				wlList.add(ywyEntity);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage().toString());
		}

		return wlList;
	}
}
