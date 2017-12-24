package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by WLL on 2017/12/24.
 */

public class Jump extends Activity{

    TextView mAccount, mPassword, mSuccessTest;
    String account, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump);

        init();

        Intent intent = this.getIntent();
        account = intent.getStringExtra("ACCOUNT");
        password = intent.getStringExtra("PASSWORD");

        mAccount.setText("学号：" + account);
        mPassword.setText("密码：" + password);

        test();
    }

    public void init() {
        mAccount = findViewById(R.id.mAccountTest);
        mPassword = findViewById(R.id.mPwdTest);
        mSuccessTest = findViewById(R.id.mSuccessTest);
    }

    public void test() {
        if (account.equals("1701210924") && password.equals("1701210924")) {
            mSuccessTest.setVisibility(View.VISIBLE);
        } else {
            Intent intent_back = new Intent(Jump.this, MainActiity.class);
            intent_back.putExtra("ERRORCODE", 40002);
            setResult(RESULT_OK, intent_back);
            startActivity(intent_back);
            finish();
        }
    }
}
