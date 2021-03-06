package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.ToastUtil;
import cz.msebera.android.httpclient.Header;

public class NoBeginTaskActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_tradeNo)
    TextView tvTradeNo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_tradeName)
    TextView tvTradeName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_address1)
    TextView tvAddress1;
    @BindView(R.id.tv_address2)
    TextView tvAddress2;
    @BindView(R.id.bt_contact)
    Button btContact;
    @BindView(R.id.bt_service)
    Button btService;
    @BindView(R.id.ll_total)
    LinearLayout llTotal;
    private Context context;
    private TaskItemInfo taskItemInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_no_begin_task);
        ButterKnife.bind(this);
        context = this;
        taskItemInfo = (TaskItemInfo) getIntent().getSerializableExtra("task");
        setData();
    }

    /**
     * 设置数据
     */
    private void setData() {
        tvTradeNo.setText(taskItemInfo.getOrder_num());
        Date date = new Date();
        date.setTime(Long.parseLong(taskItemInfo.getTime()) * 1000);
        tvTime.setText(new SimpleDateFormat("MM-dd  HH:mm").format(date));
        tvTradeName.setText(taskItemInfo.getDescription());
        tvAddress1.setText(taskItemInfo.getAddress());
        tvAddress2.setText(taskItemInfo.getSaddress());
        tvPrice.setText(taskItemInfo.getPrice());
    }

    @OnClick({R.id.rl_back, R.id.bt_contact, R.id.bt_service, R.id.ll_total})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            //联系对方
            case R.id.bt_contact:

                break;
            //上门服务
            case R.id.bt_service:
                updateState();
                break;
            //跳转到详情页
            case R.id.ll_total:
                Intent intent = new Intent(context, TaskDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskItemInfo",taskItemInfo);
                intent.putExtras(bundle);
                //我的任务
                intent.putExtra("type","2");
                startActivity(intent);
                break;
        }
    }

    /**
     * 更新任务状态
     */
    private void updateState() {
        RequestParams params=new RequestParams();
        params.put("order_num", taskItemInfo.getOrder_num());
        params.put("state", "3");
        HelperAsyncHttpClient.get(NetConstant.UPDATE_TASK_STATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("status").equals("success")) {
                    ToastUtil.showShort(context,"上门服务");
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
