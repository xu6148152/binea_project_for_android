package com.zepp.www.flatbufferdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.zepp.www.flatbufferdemo.flatmodel.PeopleList;
import com.zepp.www.flatbufferdemo.jsonmodel.PeopleListJson;
import com.zepp.www.flatbufferdemo.utils.Utils;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private TextView textViewFlat, textViewJson, textViewJackson, textViewFastJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewFlat = (TextView) findViewById(R.id.textViewFlat);
        textViewJson = (TextView) findViewById(R.id.textViewJson);

        textViewJackson = (TextView) findViewById(R.id.textViewJackson);
        textViewFastJson = (TextView) findViewById(R.id.textViewFastJson);
    }

    public void loadFromFlatBuffer(View view) {
        byte[] buffer = Utils.readRawResource(getApplication(), R.raw.sample_flatbuffer);
        long startTime = System.currentTimeMillis();
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        PeopleList peopleList = PeopleList.getRootAsPeopleList(bb);
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "FlatBuffer : " + timeTaken + "ms";
        textViewFlat.setText(logText);
        Log.d(TAG, "loadFromFlatBuffer " + logText);
    }

    public void loadFromGson(View view) {
        String jsonText = new String(Utils.readRawResource(getApplication(), R.raw.sample_json));
        long startTime = System.currentTimeMillis();
        PeopleListJson peopleList = new Gson().fromJson(jsonText, PeopleListJson.class);
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "Json : " + timeTaken + "ms";
        textViewJson.setText(logText);
        Log.d(TAG, "loadFromJson " + logText);
    }

    public void loadFromJackson(View view) {
        byte[] jsonData = Utils.readRawResource(getApplication(), R.raw.sample_json);
        long startTime = System.currentTimeMillis();
        ObjectMapper mapper = new ObjectMapper();
        try {
            PeopleListJson peopleList = mapper.readValue(jsonData, PeopleListJson.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "Json : " + timeTaken + "ms";
        textViewJackson.setText(logText);
        Log.d(TAG, "loadFromJson " + logText);
    }

    public void loadFromFastJson(View view) {
        String jsonText = new String(Utils.readRawResource(getApplication(), R.raw.sample_json));
        long startTime = System.currentTimeMillis();
        PeopleListJson peopleList = JSON.parseObject(jsonText, PeopleListJson.class);
        long timeTaken = System.currentTimeMillis() - startTime;
        String logText = "Json : " + timeTaken + "ms";
        textViewFastJson.setText(logText);
        Log.d(TAG, "loadFromJson " + logText);
    }
}
