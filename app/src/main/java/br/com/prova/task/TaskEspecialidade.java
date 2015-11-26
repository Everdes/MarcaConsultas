package br.com.prova.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.prova.marcaconsultas.WebClient;

/**
 * Created by Wolfstein on 23/11/2015.
 */
public class TaskEspecialidade extends AsyncTask<Integer, Double, String> {
    private Context mContexto;

    public TaskEspecialidade(Context contexto){
        mContexto = contexto;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Integer[] params) {

        String retorno;




        WebClient clienteWS = new WebClient("http://192.168.10.122:8090/WSAgendaMedica/especialidade/listarEspecialidade");
        retorno = clienteWS.get();
        return retorno;
    }

    @Override
    protected void onPostExecute(String retorno) {
        super.onPostExecute(retorno);

        Toast.makeText(mContexto, retorno, Toast.LENGTH_LONG).show();


    }
}
