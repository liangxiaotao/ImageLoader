package com.taotao7.imageloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {
    private CircleImageView civHomeActivity;
    private String imageUrl = "http://g.hiphotos.baidu.com/image/pic/item/63d0f703918fa0ec9687de0c249759ee3d6ddb6b.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        civHomeActivity = findViewById(R.id.civ_home_activity);

        ImageLoader.newInstance().displayImage(imageUrl,civHomeActivity);
    }
}
