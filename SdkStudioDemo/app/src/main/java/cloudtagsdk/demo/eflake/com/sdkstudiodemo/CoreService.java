/**   
 * @Title: CoreService.java 
 * @Package com.example.eflakesdkproject 
 * @Description: 
 * @author Eflake 
 * @date 2014-6-3 16:26:50 
 * @version V1.0   
 */
package cloudtagsdk.demo.eflake.com.sdkstudiodemo;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.yunji.sdk.bt.CloudTag;
import com.yunji.sdk.ot.sdk.CloudTagDevicePoi;
import com.yunji.sdk.poi.PoiInfoGetStateCallback;
import com.yunji.sdk.service.LocationService;

/**
 * @ClassName: CoreService
 * @Description: Demo Service extends from YunJiService.
 * @author Eflake
 * @date 2014-6-3 16:26:50
 * 
 */
public class CoreService extends LocationService {

	/**
	 * @Title CoreService.java
	 * @Description Using binder mechanism to let our Activity component
	 *              communicate with CoreService,using it's member method.
	 * @author Eflake
	 * @date 2014-6-6 下午5:17:30
	 */
	public CloudTagStateListener stateListener;

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化SDK
				setupAppidAndAppkey("cc2e1fd23f0211e4acd40024e8793b64","cc2e237e3f0211e4acd40024e8793b64","",
						new PoiInfoGetStateCallback() {
							@Override
							public void onSuccess(String success_str) {
								Log.d("eflake_poi", success_str);
								// leisure,busy,custom-0,1,2
								setUpLogSendConfig(1, 0, 0, 10);
								// range,monitor-0,1
								startLocation(0,"1","1");
							}

							@Override
							public void onFailure(String error_str) {
								Log.e("eflake_poi", error_str);
							}
						});
	}

	/**
	 * 
	 * @see com.yunji.sdk.service.LocationService#onCloudTagEnter(com.yunji.sdk.ot.sdk.CloudTagDevicePoi)
	 */
	@Override
	protected void onCloudTagEnter(CloudTagDevicePoi poi) {
		super.onCloudTagEnter(poi);
		Log.d(MainActivity.LOG_TAG, "core on exit,poi =" + poi.getName());
		stateListener.onCloudTagEnter(poi);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.yunji.sdk.service.LocationService#onCloudTagExit(com.yunji.sdk.ot.sdk.CloudTagDevicePoi,
	 *      long)
	 */
	@Override
	protected void onCloudTagExit(CloudTagDevicePoi poi, long stay_time) {
		super.onCloudTagExit(poi, stay_time);
		Log.d(MainActivity.LOG_TAG, "core on exit,poi =" + poi.getName()
				+ ",stay_time=" + stay_time);
		stateListener.onCloudTagExit(poi, stay_time);
	}

	/**
	 * @Title CoreService.java
	 * @Description 回调接口，用于将蓝牙设备状态回调,发送给Activity
	 * @author Eflake
	 * @date 2014-6-13 下午2:44:31
	 */
	public interface CloudTagStateListener {
		public void onCLoudTagEnter(CloudTag cloudTag);

		public void onCloudTagExit(CloudTag cloudTag, long stay_time);

		public void onCloudTagEnter(CloudTagDevicePoi devicePoi);

		public void onCloudTagExit(CloudTagDevicePoi devicePoi, long stay_time);

	}

	/**
	 * @return the stateListener
	 */
	public CloudTagStateListener getStateListener() {
		return stateListener;
	}

	/**
	 * @param stateListener
	 *            the stateListener to set
	 */
	public void setStateListener(CloudTagStateListener stateListener) {
		this.stateListener = stateListener;
	}

	/**
	 * 启动扫描
	 * 
	 * @param
	 * @return void
	 */
	public void startLocationObserve() {
	}

	/**
	 * 关闭扫描
	 * 
	 * @param
	 * @return void
	 */
	public void stopLocationObserve() {
		stopLocation();
	}

	public class CoreBinder extends Binder {
		CoreService getService() {
			return CoreService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(MainActivity.LOG_TAG, "onBind,create CoreBinder");
		return new CoreBinder();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.yunji.sdk.service.LocationService#onNetWorkState(int)
	 */
	@Override
	protected void onNetWorkState(int arg0) {
		super.onNetWorkState(arg0);
		Log.d("eflake_test", "onNetWorkState=" + arg0);
	}

}
