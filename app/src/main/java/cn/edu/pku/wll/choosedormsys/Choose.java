package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import cn.edu.pku.wll.util.MyX509TrustManager;

/**
 * Created by WLL on 2017/12/24.
 */

public class Choose extends Activity implements View.OnClickListener{
    private ImageView mExit, mBack;
    private EditText mId1, mId2, mId3, mId4, mVcode1, mVcode2, mVcode3, mVcode4;
    private Spinner mSpinnerNum, mSpinnerBuilding;
    private LinearLayout mInfo1, mInfo2, mInfo3, mInfo4;
    private Button mConfirm;
    private TextView mErrorHint;

    private String id1, vcode1, id2, vcode2, id3, vcode3, id4, vcode4, res;
    private String[] dormRes;
    private int people, building;
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
        mInfo1 = findViewById(R.id.info_1);
        mId1 = findViewById(R.id.id_1);
        mVcode1 = findViewById(R.id.vcode_1);
        mInfo2 = findViewById(R.id.info_2);
        mId2 = findViewById(R.id.id_2);
        mVcode2 = findViewById(R.id.vcode_2);
        mInfo3 = findViewById(R.id.info_3);
        mId3 = findViewById(R.id.id_3);
        mVcode3 = findViewById(R.id.vcode_3);
        mInfo4 = findViewById(R.id.info_4);
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
        mConfirm = findViewById(R.id.confirm);
        mConfirm.setOnClickListener(this);
        mErrorHint = findViewById(R.id.errorHint);

        //判断不同宿舍楼剩余床位数，若为0则不显示此可选项
        res = intent.getStringExtra("DORMRES");
        Log.d("Dorm_res", res);
        dormRes = res.split(";");

        //设置人数下拉菜单
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
                switch (position) {
                    case 0:
                        people = 1;
                        mInfo1.setVisibility(View.VISIBLE);
                        mInfo2.setVisibility(View.INVISIBLE);
                        mInfo3.setVisibility(View.INVISIBLE);
                        mInfo4.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        people = 2;
                        mInfo1.setVisibility(View.VISIBLE);
                        mInfo2.setVisibility(View.VISIBLE);
                        mInfo3.setVisibility(View.INVISIBLE);
                        mInfo4.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        people = 3;
                        mInfo1.setVisibility(View.VISIBLE);
                        mInfo2.setVisibility(View.VISIBLE);
                        mInfo3.setVisibility(View.VISIBLE);
                        mInfo4.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        people = 4;
                        mInfo1.setVisibility(View.VISIBLE);
                        mInfo2.setVisibility(View.VISIBLE);
                        mInfo3.setVisibility(View.VISIBLE);
                        mInfo4.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置楼号下拉菜单
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
        mSpinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        building = 5;
                        break;
                    case 1:
                        building = 13;
                        break;
                    case 2:
                        building = 14;
                        break;
                    case 3:
                        building = 8;
                        break;
                    case 4:
                        building = 9;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

            case R.id.confirm:
                confirmChoice(infoToJson());
                break;
            default:
                break;
        }
    }

    public String infoToJson() {
        id1 = mId1.getText().toString();
        vcode1 = mVcode1.getText().toString();
        id2 = mId2.getText().toString();
        vcode2 = mVcode2.getText().toString();
        id3 = mId3.getText().toString();
        vcode3 = mVcode3.getText().toString();
        id4 = mId4.getText().toString();
        vcode4 = mVcode4.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("num", people);
            jsonObject.put("stuid", id1);
            jsonObject.put("stu1id", id2);
            jsonObject.put("v1code", vcode2);
            jsonObject.put("stu2id", id3);
            jsonObject.put("v2code", vcode3);
            jsonObject.put("stu3id", id4);
            jsonObject.put("v3code", vcode4);
            jsonObject.put("buildingNo", building);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Dorm_infoJsonStr", jsonObject.toString());
        return jsonObject.toString();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4:
                    int errorCode = (int) msg.obj;
                    confirmBackHint(errorCode);
                    break;
                default:
                    break;
            }
        }
    };

    public void confirmChoice(final String infoJsonStr) {
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                int errorCode = -1;
                try {
                    MyX509TrustManager.allowAllSSL();
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(4000);

                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "utf-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(infoJsonStr);
                    bufferedWriter.flush();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        response.append(str);
                    }

                    String responseStr = response.toString();
                    Log.d("Dorm_backStr", responseStr);
                    bufferedReader.close();
                    inputStream.close();
                    bufferedWriter.close();
                    outputStreamWriter.close();
                    errorCode = getErrorCode(responseStr);

                    Message message = new Message();
                    message.what = 4;
                    message.obj = errorCode;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
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

    protected void confirmBackHint(int errorCode) {
        if (errorCode == 0) {
            Log.d("Dorm_chooseResult", "成功");
            Toast.makeText(Choose.this, "选择宿舍成功", Toast.LENGTH_LONG).show();
            Intent jumpIntent = new Intent(Choose.this, Info.class);
            jumpIntent.putExtra("STUID", id1);
            startActivity(jumpIntent);
            finish();
        } else if (errorCode == 40001) {
            Log.d("Dorm_chooseResult", "40001");
            mErrorHint.setText("学号不存在，请确认后重新填写");
            mErrorHint.setVisibility(View.VISIBLE);
        } else if (errorCode == 40002) {
            Log.d("Dorm_chooseResult", "40002");
            mErrorHint.setText("验证码错误，请确认后重新填写");
            mErrorHint.setVisibility(View.VISIBLE);
        } else if (errorCode == 40009) {
            Log.d("Dorm_chooseResult", "40009");
            mErrorHint.setText("参数错误，请通知管理员");
            mErrorHint.setVisibility(View.VISIBLE);

        }
    }
}
