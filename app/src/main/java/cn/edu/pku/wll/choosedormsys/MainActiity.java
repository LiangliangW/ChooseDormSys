package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.pku.wll.util.MyX509TrustManager;

/**
 * Created by WLL on 2017/10/27.
 */

public class MainActiity extends Activity implements View.OnClickListener{

    private EditText mAccount, mPassword;
    private ImageView mAccountClear, mPasswordClear, mDeveloperLogo;
    private TextView mAccountError, mPwdError;
    private CheckBox mRmbPwd, mAutoLogin, mKeyVisible;
    private Button mLogin;

    private String account, passWord;
    private SharedPreferences sharedPreferences;
    private long[] mHints = new long[10];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();
        judgeChecked();
    }

    public void init() {
        mAccount = findViewById(R.id.account_input);
        mPassword = findViewById(R.id.key_input);
        mAccountClear = findViewById(R.id.account_delete);
        mAccountClear.setOnClickListener(this);
        mPasswordClear = findViewById(R.id.key_delete);
        mPasswordClear.setOnClickListener(this);
        mDeveloperLogo = findViewById(R.id.logo);
        mDeveloperLogo.setOnClickListener(this);
        mAccountError = findViewById(R.id.account_error);
        mAccountError.setVisibility(View.INVISIBLE);
        mPwdError = findViewById(R.id.key_error);
        mPwdError.setVisibility(View.INVISIBLE);
        mRmbPwd = findViewById(R.id.check_rememberkey);
        mAutoLogin = findViewById(R.id.check_autologin);
        mKeyVisible = findViewById(R.id.check_key_visible);
        mLogin = findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mAccountClear.setVisibility(View.INVISIBLE);
                } else {
                    mAccountClear.setVisibility(View.VISIBLE);
                    mAccountError.setVisibility(View.INVISIBLE);
                    mPwdError.setVisibility(View.INVISIBLE);
                }
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mPasswordClear.setVisibility(View.INVISIBLE);
                } else {
                    mPasswordClear.setVisibility(View.VISIBLE);
                    mAccountError.setVisibility(View.INVISIBLE);
                    mPwdError.setVisibility(View.INVISIBLE);
                }
            }
        });

        mRmbPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mRmbPwd.isChecked()) {
                    sharedPreferences.edit().putBoolean("RMB_PWD", true).commit();
                } else {
                    mAutoLogin.setChecked(false);
                    sharedPreferences.edit().putBoolean("RMB_PWD", false).commit();
                }
            }
        });

        mAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAutoLogin.isChecked()) {
                    mRmbPwd.setChecked(true);
                    sharedPreferences.edit().putBoolean("AUTO_LOGIN", true).commit();

                } else {
                    sharedPreferences.edit().putBoolean("AUTO_LOGIN", false).commit();
                }
            }
        });

        mKeyVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mKeyVisible.isChecked()) {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    public void judgeChecked() {
        if (sharedPreferences.getBoolean("RMB_PWD", false)) {
            mRmbPwd.setChecked(true);
            mAccount.setText(sharedPreferences.getString("ACCOUNT", ""));
            mPassword.setText(sharedPreferences.getString("PASSWORD", ""));
            if (sharedPreferences.getBoolean("AUTO_LOGIN", false)) {
                mAutoLogin.setChecked(true);
                login();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {
            mLogin.setText("登陆中...");
            login();
        }

        if (v.getId() == R.id.account_delete) {
            mAccount.setText("");
        }

        if (v.getId() == R.id.key_delete) {
            mPassword.setText("");
        }

        if (v.getId() == R.id.logo) {
            System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
            mHints[mHints.length - 1] = SystemClock.uptimeMillis();
            if (SystemClock.uptimeMillis() - mHints[0] < 2000) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("开发者错误代码")
                        .setMessage("40001: 学号不存在" + "\n" + "40002: 密码错误" + "\n" + "40009: 参数错误")
                        .setNeutralButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    int errorCode = (int) msg.obj;
                    loginBackHint(errorCode);
                    break;
                default:
                    break;
            }
        }
    };

    public void login() {
        account = mAccount.getText().toString();
        passWord = mPassword.getText().toString();

        if (!account.equals("") && !passWord.equals("")) {
            if (mRmbPwd.isChecked()) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ACCOUNT", account);
                editor.putString("PASSWORD", passWord);
                editor.commit();
            }

            final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?username=" + account + "&password=" + passWord;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection httpURLConnection = null;
                    int errorCode = -1;
                    try {
                        MyX509TrustManager.allowAllSSL();
                        URL url = new URL(address);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setConnectTimeout(4000);
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        StringBuilder response = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            response.append(str);
                        }

                        String responseStr = response.toString();
                        bufferedReader.close();
                        inputStream.close();
                        errorCode = getErrorCode(responseStr);

                        Message message = new Message();
                        message.what = 2;
                        message.obj = errorCode;
                        mHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        } else {
            Toast.makeText(MainActiity.this, "请输入完整信息", Toast.LENGTH_LONG).show();
        }
    }

    protected void loginBackHint(int errorCode) {
        if (errorCode == 0) {
            Intent jumpIntent = new Intent(MainActiity.this, Info.class);
            jumpIntent.putExtra("STUID", account);
            startActivity(jumpIntent);
            finish();
        } else if (errorCode == 40001) {
            mAccountError.setVisibility(View.VISIBLE);
            mAccount.setText("");
            mPassword.setText("");
        } else if (errorCode == 40002) {
            mPwdError.setVisibility(View.VISIBLE);
            mPassword.setText("");
        } else if (errorCode == 40009) {
            Toast.makeText(MainActiity.this, "参数错误，请通知管理员", Toast.LENGTH_LONG).show();
            mAccount.setText("");
            mPassword.setText("");
        }
    }

    public int getErrorCode(String jsonData) {
        int errorCode = -1;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject != null) {
                errorCode = jsonObject.getInt("errcode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorCode;
    }
}
