package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.pku.wll.bean.Student;

/**
 * Created by WLL on 2017/12/24.
 * 圣诞快乐！
 */

public class Info extends Activity implements View.OnClickListener{

    TextView mName, mId, mGender, mVcode, mRoom, mBuilding, mLocation, mGrade;
    TextView m5, m13, m14, m8, m9;
    ImageView mMapIcon, mExit;

    Student student = new Student();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        init();
//        setInfo(student);
    }

    public void init() {
        mName = findViewById(R.id.name_input);
        mId = findViewById(R.id.id_input);
        mGender = findViewById(R.id.gender_input);
        mVcode = findViewById(R.id.vcode_input);
        mRoom = findViewById(R.id.room_input);
        mRoom.setOnClickListener(this);
        mBuilding = findViewById(R.id.building_input);
        mLocation = findViewById(R.id.location_input);
        mGrade = findViewById(R.id.grade_input);
        m5 = findViewById(R.id.num_5);
        m13 = findViewById(R.id.num_13);
        m14 = findViewById(R.id.num_14);
        m8 = findViewById(R.id.num_8);
        m9 = findViewById(R.id.num_9);

        mMapIcon = findViewById(R.id.mapico);
        mMapIcon.setOnClickListener(this);
        mExit = findViewById(R.id.exit);
        mExit.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    public void setInfo(Student student) {
        mName.setText(student.getName());
        mId.setText(student.getId());
        mGender.setText(student.getGender());
        mVcode.setText(student.getvCode());
        if (student.getRoom() == null) {
            mRoom.setText("去设置");
            mBuilding.setText("");
        } else {
            mRoom.setText(student.getRoom());
            mBuilding.setText(student.getBuilding());
        }
        mLocation.setText(student.getLocation());
        mGrade.setText(student.getGrade());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.room_input) {
            if (mRoom.getText().equals("去设置")) {
                Intent intentChoose = new Intent(Info.this, Choose.class);
                intentChoose.putExtra("STUDENTNAME", student.getName());
                intentChoose.putExtra("STUDENTID", student.getId());
                startActivity(intentChoose);
            }
        }

        if (v.getId() == R.id.mapico) {
            Intent intentMap = new Intent(Info.this, Map.class);
            startActivity(intentMap);
        }

        if (v.getId() == R.id.exit) {
            sharedPreferences.edit().putBoolean("AUTO_LOGIN", false).commit();
            Intent intentLogout = new Intent(Info.this, MainActiity.class);
            startActivity(intentLogout);
            finish();
        }
    }
}
