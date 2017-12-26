package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.pku.wll.bean.ResBeds;
import cn.edu.pku.wll.bean.Student;
import cn.edu.pku.wll.util.MyX509TrustManager;

/**
 * Created by WLL on 2017/12/24.
 * 圣诞快乐！
 */

public class Info extends Activity implements View.OnClickListener{

    TextView mName, mId, mGender, mVcode, mRoom, mBuilding, mLocation, mGrade;
    TextView m5, m13, m14, m8, m9;
    ImageView mMapIcon, mExit;
    String id, vCode, gender, dormRes;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        init();

        Intent intent = this.getIntent();
        getStuInfo(intent.getStringExtra("STUID"));
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.room_input) {
            if (mRoom.getText().equals("去选择")) {
                Intent intentChoose = new Intent(Info.this, Choose.class);
                intentChoose.putExtra("STUDENTID", id);
                intentChoose.putExtra("STUDENTVCODE", vCode);
                intentChoose.putExtra("DORMRES", dormRes);

                startActivity(intentChoose);
            }
        }

        if (v.getId() == R.id.mapico) {
            Intent intentMap = new Intent(Info.this, Map.class);
            intentMap.putExtra("STUID",id);
            startActivity(intentMap);
        }

        if (v.getId() == R.id.exit) {
            sharedPreferences.edit().putBoolean("AUTO_LOGIN", false).commit();
            Intent intentLogout = new Intent(Info.this, MainActiity.class);
            startActivity(intentLogout);
            finish();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    Student student2 = (Student) msg.obj;
                    String gender = student2.getGender();
                    Log.d("Dorm_gender", gender);
                    if (gender.equals("男")) {
                        getDormInfo(1);
                        Log.d("Dorm_gender1", gender);
                    } else if (gender.equals("女")) {
                        getDormInfo(2);
                        Log.d("Dorm_gender2", gender);
                    }
                    refreshStuInfo(student2);
                    break;

                case 3:
                    ResBeds resBeds = (ResBeds) msg.obj;
                    refreshResBedsInfo(resBeds);
                    break;

                default:
                    break;
            }
        }
    };

    public void getStuInfo(String stuId) {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid=" + stuId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Student student1 = new Student();
                HttpURLConnection httpURLConnection = null;
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
                    student1 = parseStuInfo(responseStr);

                    Message message = new Message();
                    message.what = 2;
                    message.obj = student1;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public Student parseStuInfo(String jsonStr) throws JSONException {
        Student student3 = new Student();
        JSONObject jsonObject = new JSONObject(jsonStr);
        if (jsonObject != null) {
            if (jsonObject.getInt("errcode") == 0) {
                JSONObject data = jsonObject.getJSONObject("data");
                student3.setName(data.getString("name"));
                student3.setId(data.getString("studentid"));
                student3.setGender(data.getString("gender"));
                student3.setvCode(data.getString("vcode"));
                if (data.has("room")){
                    Log.d("Dorm_hasRoom", "true");
                    student3.setRoom(data.getString("room"));
                    student3.setBuilding(data.getString("building"));
                } else {
                    Log.d("Dorm_hasRoom", "false");
                    student3.setRoom("去选择");
                    student3.setBuilding("无");
                }

                student3.setLocation(data.getString("location"));
                student3.setGrade(data.getString("grade"));
            }
        } else {
            Log.d("dorm_error", "解析个人信息错误");
        }

        return student3;
    }

    public void refreshStuInfo(Student student) {
        id = student.getId();
        vCode = student.getvCode();
        gender = student.getGender();

        mName.setText(student.getName());
        mId.setText(student.getId());
        mGender.setText(student.getGender());
        mVcode.setText(student.getvCode());
        if (!student.getRoom().equals("去选择")) {
            mRoom.setTextColor(Color.BLACK);
            mBuilding.setTextColor(Color.BLACK);
        } else {
            mRoom.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
        mRoom.setText(student.getRoom());
        mBuilding.setText(student.getBuilding());
        mLocation.setText(student.getLocation());
        mGrade.setText(student.getGrade());
    }

    public void getDormInfo(int gender) {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender=" + gender;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ResBeds resBeds = new ResBeds();
                HttpURLConnection httpURLConnection = null;
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
                    Log.d("Dorm_dormStr", responseStr);
                    resBeds = parseResBedsInfo(responseStr);
                    Log.d("Dorm_parse1", "解析宿舍信息完毕");
                    Message message1 = new Message();
                    message1.what = 3;
                    message1.obj = resBeds;
                    mHandler.sendMessage(message1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public ResBeds parseResBedsInfo(String jsonStr) {
        Log.d("Dorm_jsonStr", jsonStr);
        ResBeds resBeds = new ResBeds();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (jsonObject.getInt("errcode") == 0) {
                Log.d("Dorm_parse", "解析宿舍信息");
                JSONObject data = jsonObject.getJSONObject("data");
                resBeds.setFive(data.getString("5"));
                resBeds.setThirteen(data.getString("13"));
                resBeds.setFourteen(data.getString("14"));
                resBeds.setEight(data.getString("8"));
                resBeds.setNine(data.getString("9"));
                dormRes = data.getString("5") + ";" + data.getString("13") + ";"
                        + data.getString("14") + ";" + data.getString("8") + ";" + data.getString("9");
            } else {
                Log.d("Dorm_error", jsonObject.getString("errcode"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resBeds;
    }

    public void refreshResBedsInfo(ResBeds resBeds) {
        Log.d("Dorm_test", resBeds.getFive());
        m5.setText(resBeds.getFive());
        m13.setText(resBeds.getThirteen());
        m14.setText(resBeds.getFourteen());
        m8.setText(resBeds.getEight());
        m9.setText(resBeds.getNine());
    }
}
