package com.example.appmemorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Intent intent;
    static ArrayList<String> arrayList;
    static  ArrayList<LatLng> latLngs;
    static ArrayAdapter<String> arrayAdapter;



//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if(resultCode == RESULT_OK) {
//                String addr = data.getStringExtra("Location");
//                if(addr!=null)
//                {
//                     arrayList.add(addr);
//                     Log.i("ADD",addr);
//                }
//            }
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        intent=getIntent();
//        String got=intent.getStringExtra("Location");
//        Log.i("randoom","random");
//        if(got!=null)
//        {
//            Log.i("GOT",got);
//        }

        listView=findViewById(R.id.listview);
        arrayList=new ArrayList<>();
        latLngs=new ArrayList<>();
        latLngs.add(new LatLng(0,0));
        
        arrayList.add("Add new places");

         arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  if(position==0)
              //  {
                    intent=new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("placeNumber",position);
                    startActivity(intent);
              //  }
            }
        });

    }
}