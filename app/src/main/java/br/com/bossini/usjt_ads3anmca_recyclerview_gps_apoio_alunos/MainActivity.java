package br.com.bossini.usjt_ads3anmca_recyclerview_gps_apoio_alunos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List <Localizacao> localizacoes;
    private MeuAdapter adapter;
    private LocationListener listener;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        localizacoes = new ArrayList<>();
        adapter = new MeuAdapter(localizacoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                Localizacao l = new Localizacao(lat, lon);
                localizacoes.add(l);
                adapter.notifyDataSetChanged();
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
    }


    private class MeuViewHolder extends RecyclerView.ViewHolder{

        TextView latitudeTextView;
        TextView longitudeTextView;
        public MeuViewHolder (View raiz){
            super (raiz);
            latitudeTextView = raiz.findViewById(R.id.latitudeTextView);
            longitudeTextView = raiz.findViewById(R.id.longitudeTextView);
        }
    }

    private class MeuAdapter extends RecyclerView.Adapter <MeuViewHolder>{
        List<Localizacao> localizacoes;

        public MeuAdapter (List <Localizacao> localizacoes){
            this.localizacoes = localizacoes;
        }

        @NonNull
        @Override
        public MeuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View raiz = inflater.inflate(R.layout.list_item, viewGroup, false);
            return new MeuViewHolder(raiz);
        }

        @Override
        public void onBindViewHolder(@NonNull MeuViewHolder meuViewHolder, int i) {
            Localizacao localizacao = localizacoes.get(i);
            meuViewHolder.latitudeTextView.setText(
                    Double.toString(localizacao.latitude)
            );
            meuViewHolder.longitudeTextView.setText(
                    Double.toString(localizacao.longitude)
            );
        }

        @Override
        public int getItemCount() {
            return localizacoes.size();
        }
    }


    private class Localizacao{
        double latitude;
        double longitude;

        public Localizacao (double latitude, double longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                1000
           );
        }
        else{
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0,
                    listener
            );

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
      if (requestCode == 1000){
          if (grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED){
              if (ActivityCompat.checkSelfPermission(
                      this,
                      Manifest.permission.ACCESS_COARSE_LOCATION
              ) == PackageManager.PERMISSION_GRANTED){
                  locationManager.requestLocationUpdates(
                          LocationManager.NETWORK_PROVIDER,
                          0,
                          0,
                          listener
                  );

              }



          }
      }
    }
}
