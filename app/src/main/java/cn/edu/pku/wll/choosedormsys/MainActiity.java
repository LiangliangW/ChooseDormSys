package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


    final private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();
        judgeChecked();
        intentBackHint();
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
                jump();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {
            Toast.makeText(MainActiity.this, "点击了登录", Toast.LENGTH_LONG).show();
            jump();
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

    public void jump() {
        account = mAccount.getText().toString();
        passWord = mPassword.getText().toString();

        if (mRmbPwd.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ACCOUNT", account);
            editor.putString("PASSWORD", passWord);
            editor.commit();
        }

        //TODO：将账号密码传给跳转页面，验证工作由跳转页面处理
        Intent intent = new Intent(MainActiity.this, Jump.class);
        intent.putExtra("ACCOUNT", account);
        intent.putExtra("PASSWORD", passWord);
        startActivity(intent);
        finish();
    }

    protected void intentBackHint() {
        Intent intent_back = this.getIntent();
        int errorCode = intent_back.getIntExtra("ERRORCODE", -1);

        if (errorCode == 40001) {
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
        errorCode = -1;
    }
}
