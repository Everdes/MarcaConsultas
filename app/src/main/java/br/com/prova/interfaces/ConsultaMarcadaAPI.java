package br.com.prova.interfaces;

import java.util.List;

import br.com.prova.model.bean.ConsultaMarcada;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Wolfstein on 30/11/2015.
 */
public interface ConsultaMarcadaAPI {

    @GET("consultaMarcada/listarConsultaMarcada")
    Call<List<ConsultaMarcada>> getConsultaMarcada();

    @GET("consultaMarcada/listarConsultaMarcadaPorUsuario/{idUsuario}")
    Call<List<ConsultaMarcada>> getConsultaMarcadaPorUsuario(@Path("idUsuario")int id);
}
