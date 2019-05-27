package com.baiiu.common.util;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import com.baiiu.lib_common.R;

public class SwitchModeActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bitmap pic = Constant.bitmap;

        ImageView imageView = new ImageView(this);

        if (pic != null) {
            imageView.setImageBitmap(pic);
        }

        setContentView(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                finish();
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        }, 500);
    }
}
