package br.com.prova.task;

import android.os.AsyncTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.marcaconsultas.WebClient;
import br.com.prova.model.bean.AgendaMedica;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Especialidade;
import br.com.prova.model.bean.LocalAtendimento;
import br.com.prova.model.bean.Medico;
import br.com.prova.model.bean.Usuario;
import br.com.prova.util.Util;

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

        return retorno;
    }
}
