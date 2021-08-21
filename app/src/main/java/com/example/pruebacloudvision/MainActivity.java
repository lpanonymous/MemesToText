    package com.example.pruebacloudvision;

//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;

//import android.Manifest;
//import android.content.ContentValues;
import android.content.Intent;
//import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
//import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
//import android.util.Base64;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pruebacloudvision.Interface.MemesApi;
import com.example.pruebacloudvision.Modelo.Templates;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.TextAnnotation;
import com.google.api.services.vision.v1.model.WebEntity;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Date;
import java.util.List;
import java.util.Locale;

//import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private ImageView imgView;
    private TextView translateTextView;
    private ArrayList<String> templateList;
    private ArrayList<String> weList;
    private String tname;
    private String tname2;
    private String datawe;
    private String datawe2;
    private List<Templates> templatesList;
    private String rttv = "";
    private String text2 = "";

    //public static final int REQUEST_CODE_TAKE_PHOTO = 0 /*1*/;
    //private String mCurrentPhotoPath;
    //private Uri photoURI;
    //private MediaPlayer cuakSound;
    //private MediaPlayer tadaSound;
    //private MediaPlayer cameraSound;
    //private MediaPlayer failSound;
    private MediaPlayer waitSound;
    private MediaPlayer welcomeSound;
    private MediaPlayer uploadImageSound;
    private MediaPlayer noUploadImageSound;
    private MediaPlayer translateImageSound;
    private MediaPlayer memeTextSound;
    private MediaPlayer noMemeTextSound;
    private MediaPlayer memeContextSound;
    private MediaPlayer noMemeContextSound;
    private MediaPlayer translateResultSound;
    private MediaPlayer notranslateResultSound;
    private MediaPlayer errorResultSound;
    private Vision vision;
    private  float maxVolume = 100.0f;
    private float currentVolume = 50.0f;
    private float currentVolumeWs = 20.0f;
    private int i;
    private int x;
    private int y;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        translateTextView = findViewById(R.id.tv1);
        translateTextView.setMovementMethod(new ScrollingMovementMethod());
        imgView = (ImageView)findViewById(R.id.image);
        imgView.setImageDrawable(getResources().getDrawable(R.drawable.badluckbrian));

        //cuakSound = MediaPlayer.create(this, R.raw.cuak);
        //tadaSound = MediaPlayer.create(this, R.raw.tada);
        //cameraSound = MediaPlayer.create(this, R.raw.camera);
        //failSound = MediaPlayer.create(this, R.raw.fail);

        waitSound = MediaPlayer.create(this, R.raw.wait);
        welcomeSound = MediaPlayer.create(this, R.raw.welcome);
        uploadImageSound = MediaPlayer.create(this, R.raw.uploadimage);
        noUploadImageSound = MediaPlayer.create(this, R.raw.nouploadimage);
        translateImageSound = MediaPlayer.create(this, R.raw.translateimage);
        memeTextSound = MediaPlayer.create(this, R.raw.memetext);
        noMemeTextSound = MediaPlayer.create(this, R.raw.nomemetext);
        memeContextSound = MediaPlayer.create(this, R.raw.memecontext);
        noMemeContextSound = MediaPlayer.create(this, R.raw.nomemecontext);
        translateResultSound = MediaPlayer.create(this, R.raw.translateresult);
        notranslateResultSound = MediaPlayer.create(this, R.raw.notranslateresult);
        errorResultSound = MediaPlayer.create(this, R.raw.error);

        waitSound.setVolume(currentVolumeWs/maxVolume, currentVolume/maxVolume);
        welcomeSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        uploadImageSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        noUploadImageSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        noUploadImageSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        translateImageSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        memeTextSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        noMemeTextSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        memeContextSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        noMemeContextSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        translateResultSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        notranslateResultSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);
        errorResultSound.setVolume(currentVolume/maxVolume, currentVolume/maxVolume);

        // create an object textToSpeech and adding features into it
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    Locale l = new Locale("spa","MEX");
                    tts.setLanguage(l);
                    tts.setSpeechRate(0.7f);
                }
            }
        });
        welcomeSound.start();

        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null).setApplicationName("Traduccion de memes a texto");
        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer(""));
        vision = visionBuilder.build();


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Button btnSearch = findViewById(R.id.btn_buscar);
        /*final Button btnCamera = findViewById(R.id.buttonCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExternalStoragePermission();
            }
        });*/

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateImageSound.seekTo(0);
                translateImageSound.start();
                i=1;
                while(i == 1)
                {
                    if (translateImageSound.isPlaying() == true)
                    {
                        i=1;
                    }
                    else
                    {
                        waitSound.seekTo(0);
                        waitSound.start();
                        i=0;
                    }
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://crowdmemes.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MemesApi memesApi = retrofit.create(MemesApi.class);
                Call<List<Templates>> call = memesApi.getPosts();
                call.enqueue(new Callback<List<Templates>>() {
                    @Override
                    public void onResponse(Call<List<Templates>> call, Response<List<Templates>> response) {
                        if(!response.isSuccessful())
                        {
                            System.out.println("Codigo: " + response.code());
                            waitSound.pause();
                            errorResultSound.start();
                            return;
                        }

                        templatesList = response.body();
                        templateList = new ArrayList<>();

                        for (Templates templates: templatesList)
                        {
                            tname = templates.getName().toLowerCase();
                            tname2  = tname.replaceAll("\\s+","");
                            templateList.add(tname2);
                        }
                        System.out.println(templateList.toString());

                        //Codificacion de la imagen
                        if(imgView.getDrawable() == null) {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.badluckbrian));
                                    noUploadImageSound.start();
                                    waitSound.pause();
                                    waitSound.seekTo(0);
                                    waitSound.start();
                                    translate(imgView);
                                }
                            });
                        }
                        else
                        {
                            translate(imgView);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Templates>> call, Throwable t) {
                        waitSound.pause();
                        errorResultSound.start();
                        translateTextView.setText("Por el momento la aplicación no está funcionando, por favor intenta más tarde o contacta con el desarrollador.");
                        translateTextView.setContentDescription("Por el momento la aplicación no está funcionando, por favor intenta más tarde o contacta con el desarrollador.");
                        t.getMessage();
                    }
                });
            }
        });
    }

    //Método para mostrar y ocultar el menú
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }

    //Método para asignar las funciones correspondientes a las opciones.
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.helpitem)
        {
            //Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show();
            Intent helpActivity = new Intent(this, HelpActivity.class);
            startActivity(helpActivity);
        }
        else if (id == R.id.homeItem)
        {
            Intent homeActivity = new Intent(this, MainActivity.class);
            startActivity(homeActivity);
        }
        else
        {
            Intent aboutActivity = new Intent(this, AboutActivity.class);
            startActivity(aboutActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onclick(View view)
    {
        cargarImagen();
    }

    private void cargarImagen()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicación de galería"), 10);
    }
    private void translate(ImageView imgView)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                    bitmapdata = IOUtils.toByteArray(bs);
                    Image inputImage = new Image();
                    inputImage.encodeContent(bitmapdata);

                    //Text detection
                    //Descripción del tipo de tarea a realizar sobre la imagen, en este caso se utilizará TEXT_DETECTION
                    Feature textDetection = new Feature();
                    textDetection.setType("TEXT_DETECTION");
                    //Solicitud para realizar tareas del api sobre una imagen con funciones solicitadas por el usuario
                    AnnotateImageRequest request2 = new AnnotateImageRequest();
                    request2.setImage(inputImage);
                    request2.setFeatures(Arrays.asList(textDetection));
                    //Las solicitudes de anotaciones de imagenes multiples se agrupan en una sola llamada de servicio
                    BatchAnnotateImagesRequest batchRequest2 =
                            new BatchAnnotateImagesRequest();
                    batchRequest2.setRequests(Arrays.asList(request2));
                    //Respuesta a una solicitud de anotación de imagen
                    BatchAnnotateImagesResponse batchResponse2 =
                            vision.images().annotate(batchRequest2).execute();
                    //Si se obtuvo respuesta se reproduce un sonido de que se detectó texto
                    //en la imagen y se guarda el texto en un string
                    if (batchResponse2.getResponses().get(0).getFullTextAnnotation()!= null)
                    {
                        memeTextSound.seekTo(0);
                        memeTextSound.start();
                        final TextAnnotation text = batchResponse2.getResponses()
                                .get(0).getFullTextAnnotation();
                        text2 = text.getText();
                    }
                    //Si no se detectó texto de reproduce un sonido de que la imagen tiene texto
                    //y se asigna como vacio al string
                    else
                    {
                        noMemeTextSound.seekTo(0);
                        noMemeTextSound.start();
                        text2 = "";
                    }

                    //Web Detection
                    //Descripción del tipo de tarea a realizar sobre la imagen, en este caso se utilizará WEB_DETECTION
                    Feature desiredFeature = new Feature();
                    desiredFeature.setType("WEB_DETECTION");
                    //Solicitud para realizar tareas del api sobre una imagen con funciones solicitadas por el usuario
                    AnnotateImageRequest request = new AnnotateImageRequest();
                    request.setImage(inputImage);
                    request.setFeatures(Arrays.asList(desiredFeature));
                    //Las solicitudes de anotaciones de imagenes multiples se agrupan en una sola llamada de servicio
                    BatchAnnotateImagesRequest batchRequest = new BatchAnnotateImagesRequest();
                    batchRequest.setRequests(Arrays.asList(request));
                    //Respuesta a una solicitud de anotación de imagen
                    BatchAnnotateImagesResponse batchResponse = vision.images().annotate(batchRequest).execute();
                    //Se asigna a una lista el resultados de las entidades web
                    List<WebEntity> labels = batchResponse.getResponses().get(0).getWebDetection().getWebEntities();
                    //Se inicia el array para guardar la estidades web
                    weList = new ArrayList<String>();
                    //Llenado del array weList con los datos obtenidos de las entidades web
                    for (WebEntity entity : labels) {
                        //Si un dato es distinto de nulo se almacenaran los datos en la lista
                        if (entity.getDescription()!=null)
                        {
                            //Los datos serán almacenados en minusculas y se quitarán los espacios para reducir errores en las comparaciones
                            datawe = entity.getDescription().toLowerCase();
                            datawe2  = datawe.replaceAll("\\s+","");
                            weList.add(datawe2);
                        }
                    }
                    //Se imprimen los resultados ya procesados
                    System.out.println(weList.toString());
                    //Se recorrera la lista de templateList
                    for (String element: templateList)
                    {
                        //si la lista de entidades web contiene algun elemento de templateList
                        if(weList.contains(element))
                        {
                            //Recorrer la lista de Templates obtenidas con retrofit de la api de Templates
                            for (Templates templates: templatesList) {
                                //Obtener los nombres de templates, convertirlos a minusculas y quitar los espacios en blanco
                                tname = templates.getName().toLowerCase();
                                tname2 = tname.replaceAll("\\s+", "");
                                //Comparar si element es igual a tname para obtener el contexto del meme
                                if (element.equals(tname2)) {

                                    rttv = templates.getContext();
                                    break;
                                } else {
                                    rttv = "";
                                }
                            }
                        }
                    }
                    if (rttv.equals("") && text2.equals(""))
                    {
                        waitSound.pause();
                        notranslateResultSound.seekTo(0);
                        notranslateResultSound.start();
                    }
                    else
                    {
                        waitSound.pause();
                        if (!rttv.equals(""))
                        {
                            memeContextSound.seekTo(0);
                            memeContextSound.start();
                        }
                        else
                        {
                            noMemeTextSound.seekTo(0);
                            noMemeTextSound.start();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                x=1;
                                while(x==1)
                                {
                                    if(memeContextSound.isPlaying()==true)
                                    {
                                        x=1;
                                    }
                                    else
                                    {
                                        translateResultSound.start();
                                        x=0;
                                    }
                                }
                                y=1;
                                while(y == 1)
                                {
                                    if(translateResultSound.isPlaying() == true)
                                    {
                                        y=1;
                                    }
                                    else {
                                        tts.speak(text2 + rttv, TextToSpeech.QUEUE_ADD, null);
                                        y=0;
                                    }
                                }

                                translateTextView.setText("");
                                translateTextView.setText(text2);
                                translateTextView.append(rttv);
                                translateTextView.setContentDescription("" + text2 + rttv);
                                rttv ="";
                                text2 ="";
                            }
                        });
                    }

                } catch (IOException e) {
                    waitSound.pause();
                    errorResultSound.seekTo(0);
                    errorResultSound.start();
                    translateTextView.setText("Por el momento la aplicación no está funcionando, por favor intenta más tarde o contacta con el desarrollador");
                    translateTextView.setContentDescription("Por el momento la aplicación no está funcionando, por favor intenta más tarde o contacta con el desarrollador");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else*/
        if(resultCode == RESULT_OK)
        {
            Uri path = data.getData();
            imgView.setImageURI(path);
            uploadImageSound.start();
        }

    }

    /*private void checkExternalStoragePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            Log.e(TAG, "Permission not granted WRITE_EXTERNAL_STORAGE.");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        225);
            }
        }if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission not granted CAMERA.");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        226);
            }
        }
        else{
            dispatchTakePictureIntent();
        }
    }*/

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                photoURI = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                //Uri photoURI = FileProvider.getUriForFile(AddActivity.this, "com.example.android.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }*/

    /*private File createImageFile() throws IOException {
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  prefix
                ".jpg",         suffix
                storageDir      directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }*/


}
