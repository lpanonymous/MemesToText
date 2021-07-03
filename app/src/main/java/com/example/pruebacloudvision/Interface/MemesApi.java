package com.example.pruebacloudvision.Interface;

import com.example.pruebacloudvision.Modelo.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MemesApi {
    @GET("api/templates/")
    Call<List<Posts>> getPosts();
}
