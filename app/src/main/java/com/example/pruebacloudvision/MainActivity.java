package com.example.pruebacloudvision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebacloudvision.Interface.MemesApi;
import com.example.pruebacloudvision.Modelo.Posts;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.WebEntity;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView helloTextView;
    private ArrayList<String> listaTemplates;
    private ArrayList<String> listawe;
    private List<Posts> postsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helloTextView = findViewById(R.id.tv1);
        helloTextView.setMovementMethod(new ScrollingMovementMethod());

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final Button btnGet = findViewById(R.id.btn_get);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPosts();
            }
        }
        );

        final Button btnSearch = findViewById(R.id.btn_buscar);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.100.9:8000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MemesApi memesApi = retrofit.create(MemesApi.class);
                    Call<List<Posts>> call = memesApi.getPosts();
                    call.enqueue(new Callback<List<Posts>>() {
                        @Override
                        public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                            if(!response.isSuccessful())
                            {
                                helloTextView.setText("Codigo: " + response.code());
                                return;
                            }
                            final List<Posts> postsList = response.body();
                            listaTemplates = new ArrayList<>();
                            for (Posts post: postsList)
                            {
                                String datat = post.getName().toLowerCase();
                                String datat2  = datat.replaceAll("\\s+","");
                                listaTemplates.add(datat2);
                            }
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            Vision.Builder visionBuilder = new Vision.Builder(
                                                    new NetHttpTransport(),
                                                    new AndroidJsonFactory(),
                                                    null).setApplicationName("Traduccion de memes a texto");

                                            visionBuilder.setVisionRequestInitializer(
                                                    new VisionRequestInitializer("AIzaSyAWXphuZUn2J48u8QaM6P-GfPvhJdC4gJI"));
                                            Vision vision = visionBuilder.build();

                                            InputStream inputStream =
                                                    getResources().openRawResource(+R.drawable.disaster_girl);
                                            byte[] photoData;
                                            photoData = IOUtils.toByteArray(inputStream);
                                            inputStream.close();
                                            Image inputImage = new Image();
                                            inputImage.encodeContent(photoData);
                                            Feature desiredFeature = new Feature();
                                            desiredFeature.setType("WEB_DETECTION");
                                            AnnotateImageRequest request = new AnnotateImageRequest();
                                            request.setImage(inputImage);
                                            request.setFeatures(Arrays.asList(desiredFeature));
                                            BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
                                            batchRequest.setRequests(Arrays.asList(request));
                                            BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
                                            List<WebEntity> labels = batchResponse.getResponses().get(0).getWebDetection().getWebEntities();
                                            listawe = new ArrayList<String>();
                                            for (WebEntity entity : labels) {
                                                //System.out.println(entity.getDescription() + " : " + entity.getEntityId() + " : " + entity.getScore());
                                                if (entity.getDescription()!=null)
                                                {
                                                    String datawe = entity.getDescription().toLowerCase();
                                                    String datawe2  = datawe.replaceAll("\\s+","");
                                                    listawe.add(datawe2);
                                                }
                                                else
                                                {
                                                    listawe.add("null");
                                                }
                                            }
                                            //System.out.println(listaTemplates.get(0));
                                            //System.out.println(listawe.get(0));
                                            //System.out.println(listaTemplates.size());
                                            //System.out.println(listawe.size());

                                            for (String element: listaTemplates)
                                            {
                                                if(listawe.contains(element))
                                                {
                                                    System.out.println(element);
                                                    for (Posts post: postsList)
                                                    {
                                                        String datav = post.getName().toLowerCase();
                                                        String datav2  = datav.replaceAll("\\s+","");
                                                        //System.out.println(datav2);
                                                        if(element.equals(datav2))
                                                        {
                                                            helloTextView.setText("");
                                                            helloTextView.setText(post.getContext());
                                                        }
                                                    }
                                                }
                                            }
                                            //helloTextView.setText(listawe.toString());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).start();

                        }

                        @Override
                        public void onFailure(Call<List<Posts>> call, Throwable t) {
                            helloTextView.setText(t.getMessage());
                        }
                    });
            }
        });
    }

    private void getPosts()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.100.9:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MemesApi memesApi = retrofit.create(MemesApi.class);
        Call<List<Posts>> call = memesApi.getPosts();
        call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                if(!response.isSuccessful())
                {
                    helloTextView.setText("");
                    helloTextView.setText("Codigo: " + response.code());
                    return;
                }
                List<Posts> postsList = response.body();
                helloTextView.setText("");
                for (Posts post: postsList)
                {
                    String content = "";
                    content += "id: " + post.getId() + "\n";
                    content += "name: " + post.getName() + "\n";
                    content += "template: " + post.getTemplate() + "\n";
                    content += "pub_date: " + post.getPub_date() + "\n";
                    content += "context: " + post.getContext() + "\n";
                    helloTextView.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                helloTextView.setText(t.getMessage());
            }
        });
    }
}
