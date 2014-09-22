package cloudtagsdk.demo.eflake.com.sdkstudiodemo;


import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunji.sdk.bt.CloudTag;
import com.yunji.sdk.ot.sdk.CloudTagDevicePoi;

public class MainActivity extends Activity implements CoreService.CloudTagStateListener {
	private CoreService service;
	private TextView message_tv;
	private ScrollView message_content;
	private boolean isCloudtagScanning = false;
	private Button func_btn;
	private Button toogle_btn;
	private String lastMessage="";
	private TextView enter_tv;
	private TextView exit_tv;
	public static final String LOG_TAG = "eflake_poi";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpViews();
		Intent intent = new Intent(this, CoreService.class);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 更新扫描结果并显示
	 */
	private void updateMessage(String new_message) {
		message_content.fullScroll(ScrollView.FOCUS_DOWN);
		StringBuilder builder = new StringBuilder(lastMessage);
		builder.append("\n" + new_message);
		lastMessage = builder.toString();
		message_tv.setText(lastMessage+"\n"+"\n");
	}

	/**
	 * 初始化控件
	 */
	private void setUpViews() {
		message_tv = (TextView) findViewById(R.id.message_tv);
		message_content = (ScrollView) findViewById(R.id.message_content);
		enter_tv = (TextView)findViewById(R.id.enter_tv);
		exit_tv = (TextView)findViewById(R.id.exit_tv);
		func_btn = (Button) findViewById(R.id.func_btn);
		func_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		toogle_btn = (Button) findViewById(R.id.toogle_btn);
		toogle_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (service != null) {
					if (isCloudtagScanning) {
						service.stopLocationObserve();
						toogle_btn.setText("开始扫描");
					} else {
						service.startLocationObserve();
						toogle_btn.setText("停止扫描");
					}
					isCloudtagScanning = !isCloudtagScanning;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my, menu);
		return true;
	}

	ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = ((CoreService.CoreBinder) binder).getService();
			service.setStateListener(MainActivity.this);
			Log.d(MainActivity.LOG_TAG, "service create");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
	};


	@Override
	protected void onDestroy() {
		service.stopLocationObserve();
		unbindService(conn);
		super.onDestroy();
		System.exit(0);

	}

	/** 
	 * Callback method when enter a nearest Poi position
	 */
	@Override
	public void onCloudTagEnter(CloudTagDevicePoi devicePoi) {
		updateMessage("Entering poi = " + devicePoi.getName() + ".");
		enter_tv.setText(devicePoi.getName());
	}

	/** 
	 * Callback method when enter a nearest Poi position
	 */
	@Override
	public void onCloudTagExit(CloudTagDevicePoi devicePoi, long stay_time) {
		updateMessage("Exitting poi = " + devicePoi.getName()
				+ ",stay_time = " + stay_time + ".");
		exit_tv.setText(devicePoi.getName());
	}

	/** (non-Javadoc)
	 * @see com.yunji.sdkdemo.main.CoreService.CloudTagStateListener#onCLoudTagEnter(com.yunji.sdk.bt.CloudTag)
	 */
	@Override
	public void onCLoudTagEnter(CloudTag cloudTag) {
		
	}

	/** (non-Javadoc)
	 * @see com.yunji.sdkdemo.main.CoreService.CloudTagStateListener#onCloudTagExit(com.yunji.sdk.bt.CloudTag, long)
	 */
	@Override
	public void onCloudTagExit(CloudTag cloudTag, long stay_time) {
		
	}

}
