package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

public class WriteNote extends AppCompatActivity {

    EditText editText;
    int itemPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setContentView(R.layout.notes_writing);

           Intent intent = getIntent();
        itemPosition = intent.getIntExtra("noteNo", -1);

           editText = findViewById(R.id.editText);



        if (intent.getIntExtra("noteNo", -1) != -1 && intent.getIntExtra("noteNo", -1) != -2) {
            String noteData = MainActivity.notesList.get(intent.getIntExtra("noteNo",-1));
                editText.setText(noteData);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String noteData = editText.getText().toString();
        if (itemPosition == -2) {
            if (!noteData.equals(""))
            MainActivity.notesList.add(noteData);
        } else {
            MainActivity.notesList.set(itemPosition, noteData);
        }
        MainActivity.listViewAdapter.notifyDataSetChanged();
        HashSet<String> strings = new HashSet<>(MainActivity.notesList);
        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize((Serializable) MainActivity.notesList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.addNewNote :
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

}
