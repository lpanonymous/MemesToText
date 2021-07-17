package com.example.pruebacloudvision.Interface;

import com.example.pruebacloudvision.Modelo.Templates;
import com.example.pruebacloudvision.Modelo.Templates;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MemesApi {
    @GET("api/templates/")
    Call<List<Templates>> getPosts();
}
