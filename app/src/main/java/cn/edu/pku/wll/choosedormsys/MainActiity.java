package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by WLL on 2017/10/27.
 */

public class MainActiity extends Activity implements View.OnClickListener{

    private EditText mAccount, mPassword;
    private ImageView mAccountClear, mPasswordClear;
    private CheckBox mRmbPwd, mAutoLogin;
    private Button mLogin;
    private String account, passWord;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        init();
    }

    public void init() {
        mAccount = findViewById(R.id.account_input);
        mPassword = findViewById(R.id.key_input);
        mAccountClear = findViewById(R.id.account_delete);
        mPasswordClear = findViewById(R.id.key_delete);
        mRmbPwd = findViewById(R.id.check_rememberkey);
        mAutoLogin = findViewById(R.id.check_autologin);
        mLogin = findViewById(R.id.login);

        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mAccountClear.setVisibility(View.INVISIBLE);
                } else {
                    mAccountClear.setVisibility(View.VISIBLE);
                }
            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mPasswordClear.setVisibility(View.INVISIBLE);
                } else {
                    mPasswordClear.setVisibility(View.VISIBLE);
                }
            }
        });

        mRmbPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mRmbPwd.isChecked()) {
                    sharedPreferences.edit().putBoolean("RMB_PWD", true).commit();
                } else {
                    sharedPreferences.edit().putBoolean("RMB_PWD", false).commit();
                }
            }
        });

        mAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAutoLogin.isChecked()) {
                    sharedPreferences.edit().putBoolean("AUTO_LOGIN", true).commit();

                } else {
                    sharedPreferences.edit().putBoolean("AUTO_LOGIN", false).commit();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {
            account = mAccount.getText().toString();
            passWord = mPassword.getText().toString();

            if (mRmbPwd.isChecked()) {
                //TODO：验证登录是否成功，若成功，检验是否保存密码，若保存则将密码存在sharedPreferences，并跳转至info页面
            }
        }
    }

}
