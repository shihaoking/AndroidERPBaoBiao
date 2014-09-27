package order.json.factory;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import order.activity.Login;
import order.data.factory.CGDEntity;
import order.data.factory.CGDWLEntity;

public class JsonHandler {
	public static JSONObject converCgdMainToJsonObject(CGDEntity cgdEntity){
		// TODO Auto-generated method stub
		JSONObject cgdJObject = new JSONObject();
		try {
			cgdJObject.put("Danhao", cgdEntity.GetNo());
			cgdJObject.put("DDKindNo", cgdEntity.GetDdKindNo());
			cgdJObject.put("GhsNo", cgdEntity.GetGhsNo());
			cgdJObject.put("YwyNo", cgdEntity.GetYwyNo());
			cgdJObject.put("BumenNo", cgdEntity.GetBumenNo());
			cgdJObject.put("XmNo", cgdEntity.GetXmNo());
			cgdJObject.put("FkfsNo", cgdEntity.GetFkfsNo());
			cgdJObject.put("YsfsNo", cgdEntity.GetYsfsNo());
			cgdJObject.put("FktjNo", cgdEntity.GetFktjNo());
			cgdJObject.put("MoneyKindNo", "001");
			cgdJObject.put("Date_1", cgdEntity.GetCreateTime());
			cgdJObject.put("DhAddress", cgdEntity.GetDhAdress());
			cgdJObject.put("YF", cgdEntity.GetYf());
			cgdJObject.put("DJ", cgdEntity.GetDj());
			cgdJObject.put("HL", cgdEntity.GetHl());
			cgdJObject.put("BZ", cgdEntity.GetBz());
			cgdJObject.put("Checker", cgdEntity.GetChecker());
			cgdJObject.put("CheckerLc", cgdEntity.GetCheckerLc());
			cgdJObject.put("Maker", cgdEntity.GetMaker());
			cgdJObject.put("CGType", cgdEntity.GetCategory());
			cgdJObject.put("MoneyKind", "人民币");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
		//String jString = cgdJObject.toString();
		return cgdJObject;
	}
	
	public static JSONArray ConvertCgdWlToJsonArray(List<CGDWLEntity> cgdwlEntities)
	{
		JSONArray cgdWJArray = new JSONArray();
		
		JSONObject cgdWJObject;
		for (CGDWLEntity cgdWl : cgdwlEntities) {
			cgdWJObject = new JSONObject();
			try {
				cgdWJObject.put("Danhao1",cgdWl.GetCgdNo());
				cgdWJObject.put("MASTERID", cgdWl.GetMasterId());
				cgdWJObject.put("Wlbm", cgdWl.GetNo());
				cgdWJObject.put("Count_1", cgdWl.GetCount());
				cgdWJObject.put("DanJiaYb", cgdWl.GetPrice());
				cgdWJObject.put("DhDate", cgdWl.GetDhDate());
				cgdWJObject.put("qxtime", cgdWl.GetXqDate());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cgdWJArray.put(cgdWJObject);
		}
		
		//String jString = cgdWJArray.toString();
		return cgdWJArray;
	}
	
	public static JSONObject converCgdWithWlToJsonObject(CGDEntity cgdEntity,List<CGDWLEntity> cgdwlEntities)
	{
		JSONObject cgdMainJsonObject = converCgdMainToJsonObject(cgdEntity);
		JSONArray cgdWlJsonArray = ConvertCgdWlToJsonArray(cgdwlEntities);

		try {
			cgdMainJsonObject.put("CgdWl", cgdWlJsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cgdMainJsonObject;
	}
}
