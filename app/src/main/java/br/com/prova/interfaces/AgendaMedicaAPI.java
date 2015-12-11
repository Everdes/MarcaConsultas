package br.com.prova.interfaces;

import java.util.List;

import br.com.prova.model.bean.AgendaMedica;
import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Wolfstein on 30/11/2015.
 */
public interface AgendaMedicaAPI {

    @GET("agendaMedica/listarAgendaMedica")
    Call<List<AgendaMedica>> listarAgendaMedica();
//    void listarAgendaMedica(Callback<List<AgendaMedica>> agendaMedica);

}
