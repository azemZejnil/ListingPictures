package com.example.azem.listingpictures.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;

import com.example.azem.listingpictures.model.DatabasePhoto;
import com.example.azem.listingpictures.model.Photo;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteAssetHelper {

    private static final String DB_Name="images.db";
    private static final int DB_VER=1;

    private static final String TABLE_Name="Gallery";
    private static final String COL_Name="Name";
    private static final String COL_Data="Data";

    private SQLiteDatabase database = this.getWritableDatabase();


    public DBHelper(Context context) {
        super(context, DB_Name, null, DB_VER);
    }



    public void addAllBitmaps(List<Photo> photo) throws SQLiteException{
        for(int i = 0; i<photo.size();i++){
            final String finalTitle = photo.get(i).getTitle();
            final String imageUrl = "https://farm" + photo.get(i).getFarm() + ".staticflickr.com/" + photo.get(i).getServer() + "/" + photo.get(i).getId() + "_" + photo.get(i).getSecret() + ".jpg";
            try {
                URL url = new URL(imageUrl);
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ContentValues cv = new ContentValues();
                cv.put(COL_Name,finalTitle);
                cv.put(COL_Data,DBUtils.getBytes(bitmap));
                database.insert(TABLE_Name,null,cv);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public List<DatabasePhoto> getAllBitmaps(){
        List<DatabasePhoto> temp = new ArrayList<>();

        Cursor c;
        try {
            c = database.rawQuery("SELECT * FROM Gallery", null);
            if (c == null) return null;

            c.moveToFirst();
            do {
                byte[] data= c.getBlob(c.getColumnIndex(COL_Data));
                String title= c.getString(c.getColumnIndex("Name"));
                DatabasePhoto databasePhoto= new DatabasePhoto(data,title);

                temp.add(databasePhoto);
            } while (c.moveToNext());
            c.close();
        }
        catch (Exception ex) {}

        return temp;
    }

    public void deleteImages(){
        database.execSQL("delete from "+ TABLE_Name);
    }

}
