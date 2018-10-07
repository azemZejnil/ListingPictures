package com.example.azem.listingpictures.network;

import com.example.azem.listingpictures.model.Photos;
import com.example.azem.listingpictures.model.PhotosWrapper;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FlickrAPI {

    @GET("services/rest/?&method=flickr.photos.getRecent&api_key=a51fb8b898eb6531fe9fb6c793bf893c&format=json&nojsoncallback=1")
    Observable<PhotosWrapper> fetchPictures();

}
