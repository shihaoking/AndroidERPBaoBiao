package order.http.base;

import java.util.List;

import android.telephony.SmsManager;

public class SendSms {

	public static String yphoneNumber = "18042091309";
	public static String mphoneBumber = "18042091309";

	public static void SendToManager(String msg) {
		SmsManager sms = SmsManager.getDefault();
		// 如果短信没有超过限制长度，则返回一个长度的List。

		List<String> texts = sms.divideMessage(msg);

		for (String text : texts) {
			sms.sendTextMessage(mphoneBumber, null, text, null, null);
			break;
		}

	}
	
	public static void SendToYwy(String msg) {
		SmsManager sms = SmsManager.getDefault();
		// 如果短信没有超过限制长度，则返回一个长度的List。

		List<String> texts = sms.divideMessage(msg);

		for (String text : texts) {
			sms.sendTextMessage(yphoneNumber, null, text, null, null);
			break;
		}

	}
}
