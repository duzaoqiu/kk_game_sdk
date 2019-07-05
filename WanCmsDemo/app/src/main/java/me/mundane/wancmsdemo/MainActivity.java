package me.mundane.wancmsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import com.wancms.sdk.WancmsSDKManager;
import com.wancms.sdk.domain.LoginErrorMsg;
import com.wancms.sdk.domain.LogincallBack;
import com.wancms.sdk.domain.LogoutErrorMsg;
import com.wancms.sdk.domain.LogoutcallBack;
import com.wancms.sdk.domain.OnLoginListener;
import com.wancms.sdk.domain.OnLogoutListener;
import com.wancms.sdk.domain.OnPaymentListener;
import com.wancms.sdk.domain.PaymentCallbackInfo;
import com.wancms.sdk.domain.PaymentErrorMsg;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    
    public WancmsSDKManager mWancmsSDKManager;
    private EditText mEtMoney;
    
    private OnLogoutListener mLogoutListener = new OnLogoutListener() {
        @Override
        public void logoutSuccess(LogoutcallBack logoutncallback) {
            Toast.makeText(getApplication(), "用户" + logoutncallback.username + "退出登录", Toast.LENGTH_LONG).show();
        }
        
        @Override
        public void logoutError(LogoutErrorMsg errorMsg) {
        }
    };
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWancmsSDKManager = WancmsSDKManager.getInstance(this);
        mWancmsSDKManager.setActivity(this);
        mEtMoney = findViewById(R.id.et_money);
        findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
        
            }
        });
        
        findViewById(R.id.btn_charge).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                charge();
            }
        });
        
        findViewById(R.id.btn_role).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setRole();
            }
        });
        
        findViewById(R.id.btn_logout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    
    @Override
    protected void onStop() {
        mWancmsSDKManager.removeFloatView();
        super.onStop();
    }
    
    @Override
    protected void onResume() {
        mWancmsSDKManager.setActivity(this);
        mWancmsSDKManager.showFloatView(mLogoutListener);
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        mWancmsSDKManager.setActivity(null);
        super.onDestroy();
    }
    
    private void logout() {
        mWancmsSDKManager.recycle();
    }
    
    private void setRole() {
        try {
            JSONObject json = new JSONObject();
            json.put("time", "20170417");
            mWancmsSDKManager.setRoleDate(this, "caobawang11", "草霸王", "100", "1", "wancms", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private void charge() {
        String money_str = mEtMoney.getText().toString().trim();
        String money = "1";
        if (!TextUtils.isEmpty(money_str) && !"".equals(money_str)) {
            money = money_str;
        }
        mWancmsSDKManager.showPay(this, "caobawang158", money, "1", "杀人书", "金币", "", new OnPaymentListener() {
            @Override
            public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
                Toast.makeText(getApplication(), "充值金额数" + callbackInfo.money + " 消息提示：" + callbackInfo.msg, Toast.LENGTH_LONG).show();
            }
        
            @Override
            public void paymentError(PaymentErrorMsg errorMsg) {
                Toast.makeText(getApplication(), "充值失败：code:" + errorMsg.code + "  ErrorMsg:" + errorMsg.msg + "  预充值的金额：" + errorMsg.money, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void login() {
        mWancmsSDKManager.showLogin(this, true, new OnLoginListener() {
            @Override
            public void loginSuccess(LogincallBack logincallback) {
                Toast.makeText(getApplication(), "登录成功\n" + " username:" + logincallback.username, Toast.LENGTH_LONG).show();
                mWancmsSDKManager.showFloatView(mLogoutListener);
            }
        
            @Override
            public void loginError(LoginErrorMsg errorMsg) {
                Toast.makeText(getApplication(), errorMsg.msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
