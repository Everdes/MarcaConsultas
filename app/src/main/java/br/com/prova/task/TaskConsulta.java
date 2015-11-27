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

//        retorno = new Gson().toJson(getTesteLista());
        return retorno;
    }

    private List getTesteLista(){
        List lista = new ArrayList<>();

        Especialidade especialidade = new Especialidade();
        especialidade.setId(1);
        especialidade.setNome("Cirurgiao");

        Medico medico = new Medico();
        medico.setId(2);
        medico.setNome("Zé Goiaba");
        medico.setCrm(12345);
        medico.setEspecialidade(especialidade);

        LocalAtendimento localAtendimento = new LocalAtendimento();
        localAtendimento.setId(1);
        localAtendimento.setNome("Hospital São Carlos");

        AgendaMedica agendaMedica = new AgendaMedica();
        agendaMedica.setId(1);
//        try {
////            agendaMedica.setData(new SimpleDateFormat("yyyy-MM-dd").parse(Util.getToday()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        agendaMedica.setMedico(medico);
        agendaMedica.setLocalAtendimento(localAtendimento);

        Usuario usuario = new Usuario();
        usuario.setId(2);
        usuario.setLogin("Everdes");
        usuario.setPerfil("A");

        ConsultaMarcada consultaMarcada1 = new ConsultaMarcada();
        consultaMarcada1.setId(1);
        consultaMarcada1.setIdAgendaMedico(1);
        consultaMarcada1.setSituacao(Situacao.DISPONIVEL);
        consultaMarcada1.setAgendaMedica(agendaMedica);
        consultaMarcada1.setUsuario(usuario);

        try {
            consultaMarcada1.setDataMarcacaoConsulta(new SimpleDateFormat("yyyy-MM-dd").parse(Util.getToday()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        ConsultaMarcada consultaMarcada2 = new ConsultaMarcada();
//        consultaMarcada2.setId(2);
//        consultaMarcada2.setIdAgendaMedico(2);
//        consultaMarcada2.setSituacao(Situacao.DISPONIVEL);

        lista.add(consultaMarcada1);
//        lista.add(consultaMarcada2);

        return lista;
    }
}
