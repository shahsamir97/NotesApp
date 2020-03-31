package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static List<String> notesList = new ArrayList<String>();
    static ArrayAdapter<String> listViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), MODE_PRIVATE);
        try {
            notesList = (List<String>) ObjectSerializer.
                    deserialize(sharedPreferences.getString("notes", ObjectSerializer.serialize(new ArrayList<>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (notesList.isEmpty()) {
            notesList = new ArrayList<String>();
            notesList.add("Example Note");
        }

        listView = findViewById(R.id.listView);
         listViewAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,notesList);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WriteNote.class);
                intent.putExtra("noteNo", position);
                startActivity(intent);
            }
        });

      listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          @Override
          public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
              try {
                  deleteNote(position);
              } catch (IOException e) {
                  e.printStackTrace();
              }
              return true;
          }
      });

    }

    public void deleteNote(int position) throws IOException {
        final int itemToDelete = position;

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Warning!")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notesList.remove(itemToDelete);
                        listViewAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("NO", null)
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.addNewNote :
                Intent intent = new Intent(getApplicationContext(), WriteNote.class);
                intent.putExtra("noteNo", -2);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName(), MODE_PRIVATE);
        try {
            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize((Serializable) notesList)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
