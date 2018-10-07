package com.example.azem.listingpictures.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.azem.listingpictures.BitmapHelper;
import com.example.azem.listingpictures.R;
import com.example.azem.listingpictures.SinglePictureActivity;
import com.example.azem.listingpictures.database.DBHelper;
import com.example.azem.listingpictures.database.DBUtils;
import com.example.azem.listingpictures.model.DatabasePhoto;
import com.example.azem.listingpictures.model.Photo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

// Recyclerview adapter for offline mode
public class OfflineAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<DatabasePhoto> values;
    private Context context;

    private DBHelper dbHelper;

    public OfflineAdapter(Context context) {
        dbHelper= new DBHelper(context);
        this.values = dbHelper.getAllBitmaps();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.list_item,viewGroup,false);

        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
        final DatabasePhoto item = values.get(i);
        String message = item.getTitle();

        if (message.length() > 50)
            message = message.substring(0, 50) + "...";

        if (message.length() < 1)
            message = "Description...";


        recyclerViewHolder.imageTitle.setText(message);
        recyclerViewHolder.image.setImageBitmap(DBUtils.getImage(item.getData()));



        recyclerViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, SinglePictureActivity.class);
                BitmapHelper.getInstance().setBitmap(DBUtils.getImage(item.getData()));
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return values.size();
    }
}