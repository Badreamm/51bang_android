package cn.xcom.helper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.AuthenticationListActivity;
import cn.xcom.helper.activity.CityPickerActivity;
import cn.xcom.helper.activity.ConvenienceActivity;
import cn.xcom.helper.activity.HelpMeActivity;
import cn.xcom.helper.activity.IHelpActivity;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面——地图
 */
public class MapFragment extends Fragment implements View.OnClickListener{
    private  String TAG="MapFragment";
    private Context mContext;
    private RelativeLayout rl_location,rl_authentication_list;
    private TextView tv_I_help,tv_help_me,tv_city_interaction;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    MapView mMapView;
    BaiduMap mBaiduMap;
    BitmapDescriptor mCurrentMarker = null;
    boolean isFirstLoc = true; // 是否首次定位
    MyLocationData locData;

    /**
     * 当前地点击点
     */
    private LatLng currentPt;
    private Marker marker,marker2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext=getActivity();
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        initLocation();
        initListener();
        Button button = (Button) getView().findViewById(R.id.btn_location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation();
            }
        });
        initView();
    }


    /**
     * 对地图事件的消息响应
     */
    private void initListener() {
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {

            @Override
            public void onTouch(MotionEvent event) {

            }
        });


        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 单击地图
             */
            public void onMapClick(LatLng point) {
                if (marker != null) {
                    marker.remove();
                }
                mBaiduMap.clear();
                currentPt = point;
                updateMapState();
            }

            /**
             * 单击地图中的POI点
             */
            public boolean onMapPoiClick(MapPoi poi) {
                currentPt = poi.getPosition();
                updateMapState();
                return false;
            }
        });
    }

    /**
     * 更新地图状态显示面板
     */
    private void updateMapState() {
        String state = "";
        if (currentPt == null) {
            state = "点击、长按、双击地图以获取经纬度和地图状态";
        } else {
            state = String.format(",当前经度： %f 当前纬度：%f",
                    currentPt.longitude, currentPt.latitude);
        }
        createMarker(currentPt);
        Log.e("坐标",state);

    }

    /**
     * 构建坐标点
     * @param mPt
     */
    public void createMarker(final LatLng mPt){
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_dingwei_shou);
        OverlayOptions options = new MarkerOptions()
                .position(mPt)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .zIndex(9)  //设置marker所在层级
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        marker = (Marker) (mBaiduMap.addOverlay(options));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(mPt));
        //创建InfoWindow展示的view
        Button button = new Button(mContext);
        button.setText("点击下单");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HelpMeActivity.class);
                intent.putExtra("lat",mPt.latitude);
                intent.putExtra("lon",mPt.longitude);
                startActivity(intent);
            }
        });
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, mPt, -47);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    /**
     * 开始定位
     */
    private void initLocation() {
        // 地图初始化
        mMapView = (MapView) getView().findViewById(R.id.mapView_fragment_map);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void initView(){
        rl_location= (RelativeLayout) getView().findViewById(R.id.rl_fragment_map_location);
        rl_location.setOnClickListener(this);
        rl_authentication_list= (RelativeLayout) getView().findViewById(R.id.rl_fragment_map_authentication_list);
        rl_authentication_list.setOnClickListener(this);
        tv_I_help= (TextView) getView().findViewById(R.id.tv_fragment_map_I_help);
        tv_I_help.setOnClickListener(this);
        tv_help_me= (TextView) getView().findViewById(R.id.tv_fragment_map_help_me);
        tv_help_me.setOnClickListener(this);
        tv_city_interaction= (TextView) getView().findViewById(R.id.tv_fragment_map_city_interaction);
        tv_city_interaction.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_fragment_map_location:
                startActivity(new Intent(mContext, CityPickerActivity.class));
                break;
            case R.id.rl_fragment_map_authentication_list:
                startActivity(new Intent(mContext, AuthenticationListActivity.class));
                break;
            case R.id.tv_fragment_map_I_help:
                startActivity(new Intent(mContext, IHelpActivity.class));
                break;
            case R.id.tv_fragment_map_help_me:
                startActivity(new Intent(mContext, HelpMeActivity.class));
                break;
            case R.id.tv_fragment_map_city_interaction:
                startActivity(new Intent(mContext, ConvenienceActivity.class));
                break;
        }

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            mLocClient.stop();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
