package com.example.pruebacloudvision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class HelpActivity extends AppCompatActivity {
    private TextView contentTools;
    private TextView contentUploadImage;
    private TextView contentTranslate;
    //private TabLayout tabLayout;
    //private ViewPager viewPager;
    private String tools;
    private String uploadImage;
    private String translate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        contentTools = findViewById(R.id.tv1);
        contentUploadImage = findViewById(R.id.tv2);
        contentTranslate = findViewById(R.id.tv3);
        contentTools.setMovementMethod(new ScrollingMovementMethod());
        contentUploadImage.setMovementMethod(new ScrollingMovementMethod());
        contentTranslate.setMovementMethod(new ScrollingMovementMethod());
        tools = getString(R.string.helpTools) + "."+ "\n"
                + getString(R.string.toolsDescription) + "\n"
                + getString(R.string.element1) + "\n"
                + getString(R.string.element2) + "\n"
                +getString(R.string.element3) + "\n"
                +getString(R.string.element4) + "\n"
                +getString(R.string.element5) + "\n";

        uploadImage = getString(R.string.helpUploadImage) +"."+ "\n"
                + getString(R.string.cmp) + "\n"
                + getString(R.string.cmp1) + "\n"
                + getString(R.string.cmp2) + "\n"
                +getString(R.string.cmp3) + "\n"
                +getString(R.string.cmp4) + "\n"
                +getString(R.string.cmp5) + "\n"
                +getString(R.string.cmp6) + "\n";

        translate = getString(R.string.helpTransalate) +"."+ "\n"
                + getString(R.string.tmp) + "\n"
                + getString(R.string.tmp1) + "\n"
                + getString(R.string.tmp2) + "\n"
                +getString(R.string.tmp3) + "\n"
                +getString(R.string.tmp4) + "\n"
                +getString(R.string.tmp5) + "\n"
                +getString(R.string.tmp6) + "\n"
                +getString(R.string.tmp7) + "\n";

        contentTools.setText(tools);
        contentTools.setContentDescription(tools);
        contentUploadImage.setText(uploadImage);
        contentUploadImage.setContentDescription(uploadImage);
        contentTranslate.setText(translate);
        contentTranslate.setContentDescription(translate);
        //tabLayout = findViewById(R.id.tabLayout);
        //viewPager = findViewById(R.id.viewpager);
        //tabLayout.setupWithViewPager(viewPager);

        //VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //vpAdapter.addFragment(new Help3Fragment(), "Herramientas");
        //vpAdapter.addFragment(new Help1Fragment(), "Cargar meme");
        //vpAdapter.addFragment(new Help2Fragment(), "Interpretar meme");
        //viewPager.setAdapter(vpAdapter);
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
}