package cn.xcom.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.MapFragment;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.RegexUtil;
import cn.xcom.helper.utils.SPUtils;
import cz.msebera.android.httpclient.Header;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,TextWatcher{
    private String TAG="LoginActivity";
    private Context mContext;
    private EditText et_phone,et_password;
    private Button bt_submit;
    private TextView tv_register,tv_forget_password;
    private ImageView iv_delete;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        initView();
        sp=getSharedPreferences("data",MODE_PRIVATE);
        boolean b1=sp.getBoolean("isboolean",true);
        if (b1==true){
            bt_submit.setOnClickListener(this);
        }else{
            Intent intent=new Intent(getBaseContext(), MapFragment.class);
            startActivity(intent);
        }
    }
    private void initView(){
        et_phone= (EditText) findViewById(R.id.et_login_phone);
        et_password= (EditText) findViewById(R.id.et_login_password);
        et_password.addTextChangedListener(this);
        iv_delete= (ImageView) findViewById(R.id.iv_login_password_delete);
        iv_delete.setOnClickListener(this);
        iv_delete.setVisibility(View.INVISIBLE);
        iv_delete.setEnabled(false);
        bt_submit= (Button) findViewById(R.id.bt_login_submit);
        bt_submit.setOnClickListener(this);
        tv_forget_password= (TextView) findViewById(R.id.tv_login_forget_pwd);
        tv_forget_password.setOnClickListener(this);
        tv_register= (TextView) findViewById(R.id.tv_login_register);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_login_password_delete:
                et_password.setText("");
                break;
            case R.id.bt_login_submit:
                toLogin();
                break;
            case R.id.tv_login_forget_pwd:
                startActivity(new Intent(mContext,ResetPasswordActivity.class));
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(mContext,RegisterActivity.class));
                break;

        }


    }
    private void toLogin(){
//      http://bang.xiaocool.net/index.php?g=apps&m=index&a=applogin&phone=18653503680&password=123456
        String phone=et_phone.getText().toString().trim();
        String password=et_password.getText().toString().trim();
        if(!RegexUtil.checkMobile(phone)){
            Toast.makeText(mContext,"请正确填写手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        if (password.length()<6){
            Toast.makeText(mContext,"密码不正确！",Toast.LENGTH_LONG).show();
            return;
        }
        RequestParams params=new RequestParams();
        params.put("phone",phone);
        params.put("password",password);
        HelperAsyncHttpClient.get(NetConstant.NET_LOGIN,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+response.toString());
                if (response!=null){
                        try {
                            String state=response.getString("status");
                            if (state.equals("success")){
                                JSONObject jsonObject=response.getJSONObject("data");
                                UserInfo userInfo=new UserInfo(mContext);
                                userInfo.setUserId(jsonObject.getString("id"));
                                Log.d("====登陆id", jsonObject.getString("id"));
                                userInfo.setUserName(jsonObject.getString("name"));
                                userInfo.setUserImg(jsonObject.getString("photo"));
                                userInfo.setUserAddress(jsonObject.getString("address"));
                                userInfo.setUserID(jsonObject.getString("idcard"));
                                userInfo.setUserPhone(jsonObject.getString("phone"));
                                userInfo.setUserGender(jsonObject.getString("sex"));
                                userInfo.writeData(mContext);
                                getNameAuthentication(userInfo.getUserId());
                            }if(state.equals("error")){
                                String data=response.getString("data");
                                Toast.makeText(mContext,data,Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG,"--statusCode->"+statusCode+"==>"+responseString);
            }
        });
    }


    /**
     * 获取实名认证
     */
    private void getNameAuthentication(final String userid) {
        RequestParams params=new RequestParams();
        params.put("userid",userid);
        HelperAsyncHttpClient.get(NetConstant.Check_Had_Authentication, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证",response.toString());
                if(response.optString("status").equals("success")){
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION, "1");
                }else{
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION,"0");
                }
                Intent intent=new Intent(mContext,HomeActivity.class);
                intent.putExtra("userid",userid);

                Log.d("====登陆id", userid);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length()>0){
            iv_delete.setVisibility(View.VISIBLE);
            iv_delete.setEnabled(true);
        }else{
            iv_delete.setVisibility(View.INVISIBLE);
            iv_delete.setEnabled(false);
        }
    }
}
