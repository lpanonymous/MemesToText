package com.example.pruebacloudvision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView tvAbout;
    private TextView tvColabs;
    private TextView tvContact;
    private String about;
    private String colabs;
    private String contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tvAbout = findViewById(R.id.tvAbout);
        tvColabs = findViewById(R.id.tvColabs);
        tvContact = findViewById(R.id.tvContact);
        about = getString(R.string.about_name) +"."+ "\n"
                +getString(R.string.aboutText);
        colabs = getString(R.string.colaborators) + "\n"
                +getString(R.string.projectDirector) + "\n\n"
                +getString(R.string.betaTester) + "\n\n"
                +getString(R.string.developer);
        contact = getString(R.string.contact) + "\n"
                +getString(R.string.contactEmail) + "\n"
                +getString(R.string.contactCel);
        tvAbout.setText(about);
        tvAbout.setContentDescription(about);
        tvColabs.setText(colabs);
        tvColabs.setContentDescription(colabs);
        tvContact.setText(contact);
        tvContact.setContentDescription(contact);
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