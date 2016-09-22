package com.glinkus.sdk2.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

/**
 * @author syz
 * @date 2016-5-3 <br/>
 *       设备唯一标识码生成类
 */
public class UUIDUtils {

	private static final UUIDUtils instance = new UUIDUtils();

	private UUIDUtils() {

	}

	public static UUIDUtils getInstance() {
		return instance;
	}

	/**
	 * The IMEI: 仅仅只对Android手机有效 获取设备的ID 即IMIE The IMEI: 仅仅只对Android手机有效
	 * 有的手机刷机或者不规范,就无法获取到设备号,获取到的是无效IMIE,如0000000000000
	 * 
	 * @param context
	 * @return
	 */
	protected String getDeviceId(Context context) {
		String imei = "";
		TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		imei = TelephonyMgr.getDeviceId();
		if(imei == null) {
			imei = "";
		}
		return imei;
	}

	/**
	 * Pseudo-Unique ID, 这个在任何Android手机中都有效 这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom
	 * 镜像）。但应当明白的是，出现类似情况的可能性基本可以忽略。
	 * 
	 * @return
	 */
	protected String getDevIDShort() {
		String m_szDevIDShort = "35"
				+ // we make this look like a valid IMEI
				Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
				+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length()
				% 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 + Build.TYPE.length()
				% 10 + Build.USER.length() % 10; // 13 digits

		return m_szDevIDShort;
	}

	/**
	 * The Android ID 通常被认为不可信，因为它有时为null。开发文档中说明了：这个ID会改变如果进行了出厂设置。
	 * 并且，如果某个Andorid手机被Root过的话，这个ID也可以被任意改变。9774d56d682e549c
	 * 
	 * @param context
	 * @return
	 */
	protected String getAndroidId(Context context) {
		String m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		return m_szAndroidID;
	}

	/**
	 * The WLAN MAC Address string
	 * 是另一个唯一ID。但是你需要为你的工程加入android.permission.ACCESS_WIFI_STATE
	 * 权限，否则这个地址会为null。 特殊情况： Returns: 00:11:22:33:44:55
	 * (这不是一个真实的地址。而且这个地址能轻易地被伪造。).WLan不必打开，就可读取些值。
	 * 
	 * @param context
	 * @return
	 */
	protected String getWifiMac(Context context) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		return m_szWLANMAC;
	}

	/**
	 * The BT MAC Address string 只在有蓝牙的设备上运行。并且要加入android.permission.BLUETOOTH
	 * 权限.
	 * 
	 * @return
	 */
	protected String getBTAddress() {
		BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
		m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		String m_szBTMAC = m_BluetoothAdapter.getAddress();
		return m_szBTMAC;
	}

	/**
	 * 最好的方法就是通过拼接，或者拼接后的计算出的MD5值来产生一个结果.
	 * 
	 * @param context
	 * @return
	 */
	public String getUUID(Context context) {
		String uuid = UUID.randomUUID().toString();
		uuid += getDeviceId(context) + getDevIDShort();// + getAndroidId(context) + getWifiMac(context) + getBTAddress();
		uuid = uuid.replaceAll("null", "").replaceAll(":", "").replaceAll("_", "").replaceAll("-", "");
		// compute md5
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		m.update(uuid.getBytes(), 0, uuid.length());
		// get md5 bytes
		byte p_md5Data[] = m.digest();
		// create a hex string
		String m_szUniqueID = new String();
		for (int i = 0; i < p_md5Data.length; i++) {
			int b = (0xFF & p_md5Data[i]);
			// if it is a single digit, make sure it have 0 in front (proper
			// padding)
			if (b <= 0xF)
				m_szUniqueID += "0";
			// add number to string
			m_szUniqueID += Integer.toHexString(b);
		} // hex string to uppercase
		m_szUniqueID = m_szUniqueID.toUpperCase(Locale.CHINA);
		return uuid;
	}
}
