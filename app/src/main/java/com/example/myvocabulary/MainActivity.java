package com.example.myvocabulary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private EditText edit;
    private Button button;
    private TextView answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = findViewById(R.id.edit);
        button = findViewById(R.id.button);
        answer = findViewById(R.id.answer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit.getText().toString().equals(""))
                    Toast.makeText(MainActivity.this,"Введите слово",Toast.LENGTH_LONG).show();
                else {
                    String word = edit.getText().toString();
                    String url = "https://wooordhunt.ru/word/" + word;

                    new GetUrlData().execute(url);
                }
            }
        });
    }
    private class GetUrlData extends AsyncTask <String,String,String> {
        protected void onPreExecute(){
            super.onPreExecute();
            answer.setText("Ожидайте ...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null ;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String word = edit.getText().toString();
            String url = "https://wooordhunt.ru/word/" + word;
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements element = document.getElementsByClass("t_inline_en");
            answer.setText(element.text());


            if (connection != null)
                connection.disconnect();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
            return  null;
        }
    }
}
