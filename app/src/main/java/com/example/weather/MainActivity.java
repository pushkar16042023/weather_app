package com.example.weather;

import static com.android.volley.Request.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;



import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView weather;
    private LinearLayout linearLayout;
    private TextView location,status,temp,longitude,latitude,rain,wind,humidity,pressure;
    private EditText searchbar;
    private ImageView statusimage,search;
    //String API = "ef63a1507a6c2c07595bb4478e20a019";
    private ArrayList<weatherecycler> weatherlist;
    private weatheradapter weatheradapter;
    private LocationManager LocationManager;
    private static final int PERMISSION_CODE = 1;
    private String cityname;
    private HashMap<String, Integer> customImageMapping;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherlist = new ArrayList<>();
        weather = findViewById(R.id.weather);
        linearLayout = findViewById(R.id.linearlayout);
        location = findViewById(R.id.location);
        status = findViewById(R.id.status);
        temp = findViewById(R.id.temp);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        rain = findViewById(R.id.rain);
        wind = findViewById(R.id.wind);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        search = findViewById(R.id.search);
        searchbar = findViewById(R.id.searchbar);
        statusimage = findViewById(R.id.statusimage);

        statusimage.setImageResource(R.drawable.clearskyimage);
        statusimage.setImageResource(R.drawable.cloudyimage);


        customImageMapping = new HashMap<>();
        customImageMapping.put("Partly cloudy", R.drawable.cloudyimage);
        customImageMapping.put("Cloudy", R.drawable.cloudyimage);
        customImageMapping.put("Sunny", R.drawable.coloredsun);
        customImageMapping.put("Clear", R.drawable.clearskyimage);
        customImageMapping.put("Mist", R.drawable.mistimage);
        customImageMapping.put("Moderate or heavy rain with thunder", R.drawable.stormimage);
        customImageMapping.put("Moderate rain at times",R.drawable.rain);
        customImageMapping.put("Moderate rain",R.drawable.rain);
        customImageMapping.put("Patchy light rain with thunder",R.drawable.rain);
        customImageMapping.put("Overcast",R.drawable.overcastimage);
        customImageMapping.put("Thundery outbreaks possible",R.drawable.stormimage);
        customImageMapping.put("Patchy rain possibl",R.drawable.drizzle);
        customImageMapping.put("Moderate or heavy rain shower",R.drawable.stormimage);

        weatheradapter = new weatheradapter(this,weatherlist,customImageMapping);

        weather.setAdapter(weatheradapter);
        weather.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView recyclerView = findViewById(R.id.weather);
// Use LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);





        LocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Check and request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        } else {
            // Permissions are already granted, proceed with location retrieval
            Location location1 = LocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location1 != null) {
                cityname = getlocation(location1.getLongitude(), location1.getLatitude());
                getweather(cityname);
            }
        }

// Handle the search button click
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = searchbar.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter a city name", Toast.LENGTH_SHORT).show();
                } else {
                    location.setText(city); // Set the location text to the entered city
                    getweather(city);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private String getlocation(double longitude, double latitude) {
        String cityname = "not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address address : addresses) {
                // Loop through the addresses
                if (addresses != null && !addresses.isEmpty()){
                    String city = address.getLocality();

                    if (city != null && !city.isEmpty()) {
                        cityname = city;
                        break; // Exit the loop once a valid city name is found
                    }
                    else {
                        // Handle the case where no addresses are found
                        Log.d("TAG", "No addresses found");
                        Toast.makeText(this, "No addresses found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            // Handle the IOException gracefully
            e.printStackTrace();
            Toast.makeText(this, "An error occurred while getting location.", Toast.LENGTH_SHORT).show();
        }

        return cityname;
    }


    private void getweather(String cityname) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=ceaa3dd52a294abfa29114407231009&q=" + cityname + "&days=1&aqi=yes";
        location.setText(cityname);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                linearLayout.setVisibility(View.VISIBLE);
                weatherlist.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temp.setText(temperature + " °C");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    status.setText(condition);


                    String image = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    if (customImageMapping.containsKey(image)) {
                        // Get the custom image URL for the current weather condition
                        Integer customImageUrl = customImageMapping.get(image);

                        // Load the custom image into the ImageView
                        statusimage.setImageResource(customImageUrl);
                    }
                    else {
                        // Handle the case where there's no custom image defined for the current condition
                        // You can load a default image or do something else here
                        statusimage.setImageResource(R.drawable.clearskyimage);
                    }
                    String lati = response.getJSONObject("location").getString("lat");
                    latitude.setText(lati);
                    String longi = response.getJSONObject("location").getString("lon");
                    longitude.setText(longi);
                    String wind2 = response.getJSONObject("current").getString("wind_kph");
                    wind.setText(wind2 + " km/h");
                    String humidity2 = response.getJSONObject("current").getString("humidity");
                    humidity.setText(humidity2 + "%");
                    String uv2 = response.getJSONObject("current").getString("uv");
                    pressure.setText(uv2);

                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject forecastday = forecast.getJSONArray("forecastday").getJSONObject(0);

                    int rain2 = forecastday.getJSONObject("day").getInt("daily_chance_of_rain");
                    rain.setText(rain2+"%");

                    JSONArray hourArray = forecastday.getJSONArray("hour");
                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hour = hourArray.getJSONObject(i);
                        String time1 = hour.getString("time");
                        String temp1 = hour.getString("temp_c");
                        String img1 = hour.getJSONObject("condition").getString("icon");
                        String wind1 = hour.getString("wind_kph");
                        weatherlist.add(new weatherecycler(time1, temp1, img1, wind1,condition));
                    }
                    weatheradapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Enter valid city", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(objectRequest);
    }

}