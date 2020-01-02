package com.example.aqi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    EditText latitude,longitude;
    Button search;
    TextView details;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        latitude = findViewById(R.id.editText);
        longitude = findViewById(R.id.editText2);
        search = findViewById(R.id.button2);
        details = findViewById(R.id.textView5);
        mQueue = Volley.newRequestQueue(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l1 = latitude.getText().toString();
                String l2 = longitude.getText().toString();
                int f=0;
                if(l1.isEmpty())
                {
                    latitude.setError("Please enter Latitude");
                    latitude.requestFocus();
                    f++;
                }
                else if(!l1.isEmpty())
                {
                    try
                    {
                        Double lat = Double.parseDouble(l1);
                    }
                    catch (Exception e)
                    {
                        latitude.setError("Please enter Latitude");
                        latitude.requestFocus();
                        f++;
                    }
                }
                if(f==0 && l2.isEmpty())
                {
                    longitude.setError("Please enter Latitude");
                    longitude.requestFocus();
                    f++;
                }
                else if(!l2.isEmpty())
                {
                    try
                    {
                        Double lon = Double.parseDouble(l2);
                    }
                    catch (Exception e)
                    {
                        longitude.setError("Please enter Latitude");
                        longitude.requestFocus();
                        f++;
                    }
                }
                if(f==0)
                {
                    details.setText("");
                    String url = "https://api.waqi.info/feed/geo:";
                    String token = "f10961f16c37c8ba35a72397aee5f3e777dca2fd";
                    url = url+l1+";"+l2+"/?token="+token;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                JSONObject data = response.getJSONObject("data");
                                JSONObject city = data.getJSONObject("city");

                                String name = city.getString("name");
                                details.append("NAME: "+name+"\n");

                                int AQI = data.getInt("aqi");
                                details.append("AQI: "+AQI+"\n\n");

                                try {
                                    JSONObject iaqi = data.getJSONObject("iaqi");
                                    try {
                                        JSONObject co = iaqi.getJSONObject("co");
                                        int cov = co.getInt("v");
                                        details.append("CO: "+cov+"\n");
                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject dew = iaqi.getJSONObject("dew");
                                        int dewv = dew.getInt("v");
                                        details.append("DEW: "+dewv+"\n");
                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject h = iaqi.getJSONObject("h");
                                        int hov = h.getInt("v");
                                        details.append("HO: "+hov+"\n");

                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject no2 = iaqi.getJSONObject("no2");
                                        int no2v = no2.getInt("v");
                                        details.append("NO2: "+no2v+"\n");
                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject o3 = iaqi.getJSONObject("o3");
                                        int o3v = o3.getInt("v");
                                        details.append("O3: "+o3v+"\n");

                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject p = iaqi.getJSONObject("p");
                                        int pv = p.getInt("v");
                                        details.append("P: "+pv+"\n");
                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject pm10 = iaqi.getJSONObject("pm10");
                                        int pm10v = pm10.getInt("v");
                                        details.append("PM10: "+pm10v+"\n");
                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject pm25 = iaqi.getJSONObject("pm25");
                                        int pm25v = pm25.getInt("v");
                                        details.append("PM25: "+pm25v+"\n");
                                    }
                                    catch (Exception q){}

                                    try {
                                        JSONObject so2 = iaqi.getJSONObject("so2");
                                        int so2v = so2.getInt("v");
                                        details.append("SO2: "+so2v+"\n");
                                    }
                                    catch (Exception q){}
                                }
                                catch (Exception e){}

                                try {
                                    JSONObject time = data.getJSONObject("time");
                                    String s = time.getString("s");
                                    details.append("\nTIME UPDATED: "+s+"\n");
                                }
                                catch (Exception q){}

                            } catch (JSONException e) {
                                Toast.makeText(SearchActivity.this,"Could not fetch data", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SearchActivity.this,"Could not fetch data", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mQueue.add(request);
                }
            }
        });
    }
}
