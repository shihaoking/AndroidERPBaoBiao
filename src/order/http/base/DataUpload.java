package order.http.base;

import java.util.List;

import org.json.JSONObject;

import order.data.factory.CGDEntity;
import order.data.factory.CGDWLEntity;
import order.json.factory.JsonHandler;

public class DataUpload {
	public static int UpdateCgdData(CGDEntity cgdEntity,
			List<CGDWLEntity> cgdwlEntities) {
		JSONObject postJsonObject = JsonHandler.converCgdWithWlToJsonObject(
				cgdEntity, cgdwlEntities);
		String result = HttpDownload.PostData(postJsonObject.toString(),
				"updateData", "cgd");
		return Integer.parseInt(result);
	}

	public static String AddNewCgdData(CGDEntity cgdEntity,
			List<CGDWLEntity> cgdwlEntities) {
		JSONObject postJsonObject = JsonHandler.converCgdWithWlToJsonObject(
				cgdEntity, cgdwlEntities);
		String result = HttpDownload.PostData(postJsonObject.toString(),
				"addData", "cgd");
		return result;
	}
	
	public static String DeleteCgdByNo(String no) {
		String result = HttpDownload.PostData(no, "deleteData", "cgd");
		return result;
	}
}
