package br.com.prova.interfaces;

import br.com.prova.model.bean.Usuario;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Wolfstein on 29/11/2015.
 */
public interface UsuarioAPI {

    @GET("usuario/autenticarUsuario/{login}")
    Call<Usuario> getUsuario(@Path("login") String login);

}
