package br.com.prova.task;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import br.com.prova.interfaces.AgendaMedicaAPI;
import br.com.prova.model.bean.AgendaMedica;
import br.com.prova.util.Constantes;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Wolfstein on 24/11/2015.
 */
public class TaskConsulta extends AsyncTask<Integer, Double, String> {

    @Override
    protected String doInBackground(Integer... params) {
        String retorno = new String();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(Constantes.API_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        AgendaMedicaAPI agendaMedicaAPI = retrofit.create(AgendaMedicaAPI.class);

        Call<List<AgendaMedica>> call = agendaMedicaAPI.listarAgendaMedica();

        try {
            retorno = gson.toJson(call.execute().body());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retorno;
    }
}
