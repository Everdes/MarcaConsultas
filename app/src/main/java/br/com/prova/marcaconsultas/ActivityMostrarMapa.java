package br.com.prova.marcaconsultas;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.prova.util.Util;

/**
 * Created by Éverdes on 27/09/2015.
 *
 * Classe responsável por exibir o endereço no mapa, do local de atendimento.
 */
public class ActivityMostrarMapa extends FragmentActivity {

    private GoogleMap mMapa;
    private String mEndereco;
    private LatLng mCoordenadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_mapa);

        TextView tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        tvEndereco.setText(getIntent().getStringExtra("endereco"));
        mEndereco = tvEndereco.getText().toString();

        ImageButton btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        instanciarMapa();
    }

    /**
     * Método que instancia um objeto mapa
     */
    private void instanciarMapa() {
        if (mMapa == null) {
            mMapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMapa != null) {
                getCoordenadas();
            }
        }
    }

    /**
     * Método que lê o endereço e encontra a latitude e longitude do mesmo, e invoca o método que marca o endereço no mapa
     */
    private void getCoordenadas() {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> enderecos = geocoder.getFromLocationName(mEndereco, 1);

            if (!enderecos.isEmpty()) {
                Address enderecoLocalizado = enderecos.get(0);
                double latitude = enderecoLocalizado.getLatitude();
                double longitude = enderecoLocalizado.getLongitude();

                mCoordenadas = new LatLng(latitude, longitude);

                if (mCoordenadas != null)
                    marcarNoMapa();

            } else {
                Util.showMessage("Aviso", "Endereço não encontrado.", this);
            }
        } catch (IOException e) {
            Log.i("Erro", "Endereço não localizado");
        }

    }

    /**
     * Método que lê a latitude e longitude do endereço e marca o mesmo no mapa
     */
    public void marcarNoMapa() {
        mMapa.addMarker(new MarkerOptions().position(mCoordenadas).title("Localizacao").
                snippet(mEndereco));

        mMapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mCoordenadas, 15);
        mMapa.animateCamera(update);

    }
}
