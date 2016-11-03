package com.syz.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by SYZ on 16/11/3.
 * <br>
 * <hr>Tip:<hr>
 *     This BroadcastReceiver is used to monitor and receive the Broadcast of System Network.
 * <br>
 *     This broadcast has two ways of registration.<br>
 *      1.registered in the code,for example register in the method of Activity onCreate.<br>
 *      e.g.<br>
 *      protected void onCreate(Bundle savedInstanceState) {
 *          ....
 *          IntentFilter filter = new IntentFilter();
 *          filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
 *          registerBroadcast(filter);
 *      }
 * <br>
 *      2.registered in the manifest。<br>
 *      <receiver android:name=".broadcast.NetBroadcastReceiver">
 *           <intent-filter>
 *               <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
 *           </intent-filter>
 *       </receiver>
 * <br>
 *
 */

public class NetBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        /**
         * 检测到网络发生变化
         */
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null){
                // wifi和移动网络关闭的时候info为null
                Toast.makeText(context, "NetworkInfo is null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (info.isAvailable() && info.isConnected()){
                // 当前无可用网络,已连接
                Toast.makeText(context, "isConnected()", Toast.LENGTH_SHORT).show();
            } else {
                // 当前无可用网络,或者未连接
            }

        }
    }
}
