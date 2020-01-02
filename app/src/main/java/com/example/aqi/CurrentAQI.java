package com.example.aqi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrentAQI extends AppCompatActivity {
    private TextView mtv;
    private LocationManager lm;
    LocationListener ll;
    private RequestQueue mQueue;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
                lm.requestSingleUpdate(LocationManager.GPS_PROVIDER,ll,null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_aqi);
        mQueue = Volley.newRequestQueue(this);
        mtv = findViewById(R.id.textView6);
        lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String latitude = ""+location.getLatitude();
                String longitude = ""+location.getLongitude();
                mtv.setText("CURRENT LOCATION\nLatitude: "+latitude+"\nLongitude: "+longitude+"\n\nMEASURING STATION INFORMATION\n");

                String url = "https://api.waqi.info/feed/geo:";
                String token = "f10961f16c37c8ba35a72397aee5f3e777dca2fd";
                url = url+latitude+";"+longitude+"/?token="+token;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject data = response.getJSONObject("data");
                            JSONObject city = data.getJSONObject("city");
                            JSONArray geo = city.getJSONArray("geo");
                            mtv.append("\nLatitude: "+geo.get(0)+"\n");
                            mtv.append("Longitude: "+geo.get(1)+"\n");

                            String name = city.getString("name");
                            mtv.append("NAME: "+name+"\n");

                            int AQI = data.getInt("aqi");
                            mtv.append("\nAQI: "+AQI+"\n\n");

                            try {
                                JSONObject iaqi = data.getJSONObject("iaqi");
                                try {
                                    JSONObject co = iaqi.getJSONObject("co");
                                    int cov = co.getInt("v");
                                    mtv.append("CO: "+cov+"\n");
                                }
                                catch (Exception q){}

                                try {
                                    JSONObject dew = iaqi.getJSONObject("dew");
                                    int dewv = dew.getInt("v");
                                    mtv.append("DEW: "+dewv+"\n");
                                }
                                catch (Exception q){}

                                try {
                                    JSONObject h = iaqi.getJSONObject("h");
                                    int hov = h.getInt("v");
                                    mtv.append("HO: "+hov+"\n");

                                }
                                catch (Exception q){}

                                try {
                                    JSONObject no2 = iaqi.getJSONObject("no2");
                                    int no2v = no2.getInt("v");
                                    mtv.append("NO2: "+no2v+"\n");
                                }
                                catch (Exception q){}

                                try {
                                    JSONObject o3 = iaqi.getJSONObject("o3");
                                    int o3v = o3.getInt("v");
                                    mtv.append("O3: "+o3v+"\n");

                                }
                                catch (Exception q){}

                                try {
                                    JSONObject p = iaqi.getJSONObject("p");
                                    int pv = p.getInt("v");
                                    mtv.append("P: "+pv+"\n");
                                }
                                catch (Exception q){}

                                try {
                                    JSONObject pm10 = iaqi.getJSONObject("pm10");
                                    int pm10v = pm10.getInt("v");
                                    mtv.append("PM10: "+pm10v+"\n");
                                }
                                catch (Exception q){}

                                try {
                                    JSONObject pm25 = iaqi.getJSONObject("pm25");
                                    int pm25v = pm25.getInt("v");
                                    mtv.append("PM25: "+pm25v+"\n");
                                }
                                catch (Exception q){}

                                try {
                                    JSONObject so2 = iaqi.getJSONObject("so2");
                                    int so2v = so2.getInt("v");
                                    mtv.append("SO2: "+so2v+"\n");
                                }
                                catch (Exception q){}
                            }
                            catch (Exception e){}

                            try {
                                JSONObject time = data.getJSONObject("time");
                                String s = time.getString("s");
                                mtv.append("\nTIME UPDATED: "+s+"\n");
                            }
                            catch (Exception q){}

                        } catch (JSONException e) {
                            Toast.makeText(CurrentAQI.this,"Could not fetch data", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CurrentAQI.this,"Could not fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(request);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll);
            lm.requestSingleUpdate(LocationManager.GPS_PROVIDER,ll,null);
        }
    }
}