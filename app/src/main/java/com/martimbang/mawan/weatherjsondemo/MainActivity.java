package com.martimbang.mawan.weatherjsondemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    TextView resultTextView;
    EditText cityEditText;

    public void getWeather(View view) {
        String content = "";
        String cityName = "";

        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

        try {

            cityName = URLEncoder.encode(cityEditText.getText().toString(), "UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=58014140638c5a34d8b8a0e15e5c2ca8");

        } catch (UnsupportedEncodingException e) {
            Toast.makeText(getApplicationContext(), "Can not find weather", Toast.LENGTH_LONG);
        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }

                return result;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Can not find weather", Toast.LENGTH_LONG);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Can not find weather", Toast.LENGTH_LONG);
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String city = "";
            String main = "";
            String tem = "";
            String hum  = "";
            String textToDisplay = "";

            try {

                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                String mainInfo = jsonObject.getString("main");

                city = jsonObject.getString("name");

                JSONArray weatherArray = new JSONArray(weatherInfo);

                for (int i = 0; i < weatherArray.length(); i++) {
                    JSONObject jsonPart = weatherArray.getJSONObject(i);
                    main = jsonPart.getString("main");
//                    Log.i("main", jsonPart.getString("main"));
//                    Log.i("description", jsonPart.getString("description"));
                }

                JSONObject temp = new JSONObject(mainInfo);

                tem = temp.getString("temp");
                hum = temp.getString("humidity");

//                Log.i("temp", temp.getString("temp"));
//                Log.i("humidity", temp.getString("humidity"));

                textToDisplay = "Weather for " + city + "\r\n" + main + "\r\n" + "Temp: " + tem + "\r\n" + "Hum: " + hum;
                resultTextView.setText(textToDisplay);


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Can not find weather", Toast.LENGTH_LONG);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.textView);
        cityEditText = (EditText) findViewById(R.id.editText);

    }
}
