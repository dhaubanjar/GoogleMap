package com.samriddhi_android.www.googlemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button go, satellite, terrain, normal;
    EditText etAddress;
    LatLng clickLatLng;
    GoogleMap map;
    String url="http://maps.googleapis.com/maps/api/geocode/json?address=";
    RequestQueue requestQueue;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        button ma click halna baki
        go=findViewById(R.id.btngo);
        satellite=findViewById(R.id.btnsatellite);
        terrain=findViewById(R.id.btnterrain);
        normal=findViewById(R.id.btnnormal);
        etAddress = findViewById(R.id.etAddress);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = etAddress.getText().toString();
                String Url = url + address;
                requestQueue=Volley.newRequestQueue(MainActivity.this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj1 = new JSONObject(response);
                            JSONArray array1 = obj1.getJSONArray("results");
                            JSONObject obj2 = array1.getJSONObject(0);
                            JSONObject obj3 = obj2.getJSONObject("geometry");
                            JSONObject obj4 = obj3.getJSONObject("location");
                            Double lat = obj4.getDouble("lat");
                            Double lng = obj4.getDouble("lng");
                            LatLng l = new LatLng(lat, lng);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 10));
                            map.addMarker(new MarkerOptions().position(l).title("Marked Item"));
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });
                requestQueue.add(stringRequest);
            }
    });

        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            }
        });
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_maps);
        mapFragment.getMapAsync(this );
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map=googleMap;
        LatLng l=new LatLng(27.676460, 85.360988);
       googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,10));
       googleMap.addMarker(new MarkerOptions().position(l));
       googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
           @Override
           public void onMapClick(LatLng latLng) {
               clickLatLng=latLng;
               googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
               googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));



           }
       });
       googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
           @Override
           public void onMapLongClick(LatLng latLng) {
               PolylineOptions options=new PolylineOptions();
               options.add(clickLatLng);
               options.add(latLng);
               googleMap.addPolyline(options);
               googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
           }
       });
    }
}
