package com.example.azem.listingpictures;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.azem.listingpictures.adapter.OfflineAdapter;
import com.example.azem.listingpictures.adapter.RecyclerAdapter;
import com.example.azem.listingpictures.database.DBHelper;
import com.example.azem.listingpictures.model.Photo;
import com.example.azem.listingpictures.model.PhotosWrapper;
import com.example.azem.listingpictures.network.FlickrAPI;
import com.example.azem.listingpictures.network.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ListPicturesActivity extends AppCompatActivity {

    private RecyclerView listView;
    private RecyclerView.LayoutManager layoutManager;
    DBHelper dbHelper;

    FlickrAPI flickrAPI;
    CompositeDisposable compositeDisposable= new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper= new DBHelper(this);
        listView= (RecyclerView)findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);


        if(isNetworkAvailable()) {
            // Network call for getting pictures
            Retrofit retrofit = RetrofitClient.getInstance();
            flickrAPI = retrofit.create(FlickrAPI.class);

            compositeDisposable.add(flickrAPI.fetchPictures()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<PhotosWrapper>() {
                        @Override
                        public void accept(PhotosWrapper photosWrapper) throws Exception {
                            final List<Photo> photoList = photosWrapper.getPhotos().getPhoto();
                            listView.setAdapter(new RecyclerAdapter(photoList, ListPicturesActivity.this));

                            dbHelper.deleteImages();
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    dbHelper.addAllBitmaps(photoList);
                                }
                            });
                            thread.start();
                        }
                    }));
        }

        //read from database if no internet
        else
            listView.setAdapter(new OfflineAdapter(ListPicturesActivity.this));

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
