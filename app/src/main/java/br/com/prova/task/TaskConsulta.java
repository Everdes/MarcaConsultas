package br.com.prova.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.marcaconsultas.WebClient;
import br.com.prova.model.bean.ConsultaMarcada;

/**
 * Created by Wolfstein on 24/11/2015.
 */
public class TaskConsulta extends AsyncTask<Integer, Double, String> {
    private String mUrl;

    public TaskConsulta(String url){
        mUrl = url;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String retorno;

        WebClient clienteWS = new WebClient(mUrl);
        retorno = clienteWS.get();

//        retorno = new Gson().toJson(getTesteLista());
        return retorno;
    }

    private List getTesteLista(){
        List lista = new ArrayList<>();

        ConsultaMarcada consultaMarcada1 = new ConsultaMarcada();
        consultaMarcada1.setId(1);
        consultaMarcada1.setIdAgendaMedico(1);
        consultaMarcada1.setSituacao(Situacao.DISPONIVEL);

        ConsultaMarcada consultaMarcada2 = new ConsultaMarcada();
        consultaMarcada2.setId(2);
        consultaMarcada2.setIdAgendaMedico(2);
        consultaMarcada2.setSituacao(Situacao.DISPONIVEL);

        lista.add(consultaMarcada1);
        lista.add(consultaMarcada2);

        return lista;
    }
}
