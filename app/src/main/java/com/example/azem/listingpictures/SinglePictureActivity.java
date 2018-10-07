package com.example.azem.listingpictures;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.azem.listingpictures.database.DBHelper;
import com.example.azem.listingpictures.database.DBUtils;

import okhttp3.internal.Util;

public class SinglePictureActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_picture);

        imageView=(ImageView)findViewById(R.id.image);

        if(getIntent().getStringExtra("imageUrl")!=null)
            Glide.with(this).load(getIntent().getStringExtra("imageUrl")).into(imageView);

        else
            imageView.setImageBitmap(BitmapHelper.getInstance().getBitmap());

    }
}
