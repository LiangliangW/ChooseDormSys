package cn.edu.pku.wll.choosedormsys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by WLL on 2017/12/25.
 */

public class Map extends Activity {
    ImageView back;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        Intent intent = this.getIntent();
        id = intent.getStringExtra("STUID");

        back = findViewById(R.id.map_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBackInfo = new Intent(Map.this, Info.class);
                intentBackInfo.putExtra("STUID", id);
                startActivity(intentBackInfo);
                finish();
            }
        });
    }
}
