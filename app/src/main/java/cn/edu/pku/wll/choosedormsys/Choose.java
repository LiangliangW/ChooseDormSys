package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by WLL on 2017/12/24.
 */

public class Choose extends Activity implements View.OnClickListener{
    private ImageView mExit, mBack;
    private EditText mId1, mId2, mId3, mId4, mVcode1, mVcode2, mVcode3, mVcode4;
    private Spinner mSpinnerNum, mSpinnerBuilding;

    private String id1, vcode1, res;
    private String[] dormRes;
    private ArrayList<String> chooseNum, chooseBuilding;
    private ArrayAdapter<String> adapterNum, adapterBuilding;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        init();
    }

    public void init() {
        mExit = findViewById(R.id.exit_choose);
        mExit.setOnClickListener(this);
        mBack = findViewById(R.id.back_choose);
        mBack.setOnClickListener(this);
        mId1 = findViewById(R.id.id_1);
        mVcode1 = findViewById(R.id.vcode_1);
        mId2 = findViewById(R.id.id_2);
        mVcode2 = findViewById(R.id.vcode_2);
        mId3 = findViewById(R.id.id_3);
        mVcode3 = findViewById(R.id.vcode_3);
        mId4 = findViewById(R.id.id_4);
        mVcode4 = findViewById(R.id.vcode_4);
        mSpinnerNum = (Spinner) findViewById(R.id.spinner_1);
        mSpinnerBuilding = (Spinner) findViewById(R.id.spinner_2);
        Intent intent = this.getIntent();
        id1 = intent.getStringExtra("STUDENTID");
        vcode1 = intent.getStringExtra("STUDENTVCODE");
        mId1.setText(id1);
        mVcode1.setText(vcode1);
        mId1.setFocusable(false);
        mVcode1.setFocusable(false);

        res = intent.getStringExtra("DORMRES");
        Log.d("Dorm_res", res);
        dormRes = res.split(";");

        chooseNum = new ArrayList<String>();
        chooseNum.add("单人");
        chooseNum.add("双人");
        chooseNum.add("三人");
        chooseNum.add("四人");
        adapterNum = new ArrayAdapter<String>(Choose.this, R.layout.custom_spiner_text_item, chooseNum);
        adapterNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerNum.setAdapter(adapterNum);
        mSpinnerNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chooseBuilding = new ArrayList<String>();
        if (Integer.parseInt(dormRes[0]) > 0) {
            chooseBuilding.add("5号楼");
        }
        if (Integer.parseInt(dormRes[1]) > 0) {
            chooseBuilding.add("13号楼");
        }
        if (Integer.parseInt(dormRes[2]) > 0) {
            chooseBuilding.add("14号楼");
        }
        if (Integer.parseInt(dormRes[3]) > 0) {
            chooseBuilding.add("8号楼");
        }
        if (Integer.parseInt(dormRes[4]) > 0) {
            chooseBuilding.add("9号楼");
        }
        adapterBuilding = new ArrayAdapter<String>(Choose.this, R.layout.custom_spiner_text_item, chooseBuilding);
        adapterBuilding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerBuilding.setAdapter(adapterBuilding);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_choose:
                Intent intentToInfo = new Intent(Choose.this, Info.class);
                intentToInfo.putExtra("STUID", id1);
                startActivity(intentToInfo);
                finish();
                break;
            case R.id.exit_choose:
                sharedPreferences.edit().putBoolean("AUTO_LOGIN", false).commit();
                Intent intentLogout = new Intent(Choose.this, MainActiity.class);
                startActivity(intentLogout);
                finish();
                break;
            default:
                break;
        }
    }
}
