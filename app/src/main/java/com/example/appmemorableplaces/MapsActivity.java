package com.example.appmemorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    Intent intent;

    public void CenterCamera(Location location,String title)
    {
        if(location!=null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
           //mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Marker in  Users Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));
        }


    }




        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            CenterCamera(lastKnownLocation,"User Location");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      //  mMap.setOnMapLongClickListener(this);

        intent=getIntent();
        Log.i("intent", String.valueOf(intent.getIntExtra("placeNumber",0)));
        int index=intent.getIntExtra("placeNumber",0);

        if(index==0)
        {
            locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
               CenterCamera(location,"User Location");


            }
        };

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                Location lastKnownLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                CenterCamera(lastKnownLocation,"User Location");
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            }


        }
        else
        {

            Location location=new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(MainActivity.latLngs.get(index).latitude);
            location.setLongitude(MainActivity.latLngs.get(index).longitude);

            String placeTitle=MainActivity.arrayList.get(index);
            CenterCamera(location,placeTitle);

        }


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                String address="";


                List<Address> addressList;
                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    Log.i("DHUKSE","DHHUKSE0");

                    addressList= geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    //if(addressList!=null && addressList.size()>0)
                   // {
                        Log.i("DHUKSE","DHHUKSE");
                        if(addressList.get(0).getSubThoroughfare()!=null)
                        {
                            address+=addressList.get(0).getSubThoroughfare();
                            Log.i("DHUKSE","DHHUKSE2");

                        }
                        if(addressList.get(0).getAddressLine(0)!=null)
                        {
                            address+=" "+addressList.get(0).getAddressLine(0);
                            Log.i("DHUKSE","DHHUKSE3");

                        }
                        if(addressList.get(0).getAdminArea()!=null)
                        {
                            address+=" "+addressList.get(0).getAdminArea();
                            Log.i("DHUKSE","DHHUKSE4");

                        }
                        if(addressList.get(0).getCountryName()!=null)
                        {
                            address+=addressList.get(0).getCountryName();
                            Log.i("DHUKSE","DHHUKSE5");

                        }



                   // }
;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //if(address.equals(""))
              //  {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd--MM--yyyy");
                    address+=simpleDateFormat.format(new Date());
             //   }

                MainActivity.arrayList.add(address);
                MainActivity.latLngs.add(latLng);
                MainActivity.arrayAdapter.notifyDataSetChanged();

                mMap.addMarker(new MarkerOptions().position(latLng).title(address));

                Toast.makeText(getApplicationContext(),"Location saved",Toast.LENGTH_SHORT).show();

            }
        });




    }


}