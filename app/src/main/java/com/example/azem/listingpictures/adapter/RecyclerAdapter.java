package com.example.azem.listingpictures.adapter;


import android.content.ContentValues;
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

import com.bumptech.glide.Glide;
import com.example.azem.listingpictures.BitmapHelper;
import com.example.azem.listingpictures.R;
import com.example.azem.listingpictures.SinglePictureActivity;
import com.example.azem.listingpictures.database.DBHelper;
import com.example.azem.listingpictures.database.DBUtils;
import com.example.azem.listingpictures.model.Photo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


// Recyclerview adapter for online mode
class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView imageTitle;
    public ImageView image;

    private ItemClickListener itemClickListener;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        image= (ImageView)itemView.findViewById(R.id.image);
        imageTitle= (TextView) itemView.findViewById(R.id.image_title);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }
}
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private List<Photo> values;
    private Context context;

    DBHelper dbHelper;

    public RecyclerAdapter(final List<Photo> values, Context context) {
        this.values = values;
        this.context = context;
        dbHelper= new DBHelper(context);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.list_item,viewGroup,false);


        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, final int i) {
        final Photo item = values.get(i);
        String title = item.getTitle();

        if (title.length() > 50)
            title = title.substring(0, 50) + "...";

        if (title.length() < 1)
            title = "Description...";

        final String imageUrl = "https://farm" + item.getFarm() + ".staticflickr.com/" + item.getServer() + "/" + item.getId() + "_" + item.getSecret() + ".jpg";

        recyclerViewHolder.imageTitle.setText(title);
        Glide.with(context).load(imageUrl).into(recyclerViewHolder.image);

        recyclerViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                final String imageUrl = "https://farm" + item.getFarm() + ".staticflickr.com/" + item.getServer() + "/" + item.getId() + "_" + item.getSecret() + ".jpg";

                Intent intent = new Intent(context, SinglePictureActivity.class);
                intent.putExtra("imageUrl",imageUrl);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
