package order.http.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpDownload {

	public static String serverUrl = "172.31.100.14";

	public static void SetServerUrl(String url) {
		serverUrl = url;
	}

	public static String GetServerData(String url)
			throws ClientProtocolException, IOException {
		String result = "";

		HttpGet httpGet = new HttpGet("http://" + serverUrl + "/Handler.ashx"
				+ url);

		// 设置请求超时和读取返回数据超时的时间
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 40000);
		HttpConnectionParams.setSoTimeout(httpParams, 30000);

		HttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				result = ConvertStreamToString(inputStream);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			throw e;
		} finally {
			httpClient.getConnectionManager().shutdown();
			httpResponse = null;
		}
		return result;

	}

	public static String PostData(String jString, String action, String target) {
		HttpPost request = new HttpPost("http://" + serverUrl + "/Handler.ashx");
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("action", action));
		params.add(new BasicNameValuePair("target", target));
		params.add(new BasicNameValuePair("value", jString));

		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = new DefaultHttpClient().execute(request);

			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				result = ConvertStreamToString(inputStream);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	public static String ConvertStreamToString(InputStream is) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"),
					512 * 1024);
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			if (reader != null) {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}
		} catch (IOException e) {
			Log.e("DataProvier convertStreamToString", e.getLocalizedMessage(),
					e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static boolean CheckNetAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

}
