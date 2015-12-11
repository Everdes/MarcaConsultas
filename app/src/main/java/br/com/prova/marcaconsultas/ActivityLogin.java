package br.com.prova.marcaconsultas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.prova.helpers.ActivityLoginHelper;
import br.com.prova.interfaces.UsuarioAPI;
import br.com.prova.model.bean.Usuario;
import br.com.prova.util.Constantes;
import br.com.prova.util.Util;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Éverdes on 27/09/2015.
 * <p/>
 * Activity responsável por lê os dados de login e senha digitados pelo usuário e realizar a validação dos mesmos,
 * permitindo o acesso e uso da aplicação caso os dados estejam corretos, ou barrando caso não estajam de acordo.
 */
public class ActivityLogin extends AppCompatActivity {

    private Button mBtnEntrar;
    private ActivityLoginHelper mHelper;
    private Usuario mUsuarioSelecionado;
    private Usuario mUsuarioInformado;
    private ProgressDialog mProgresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mHelper = new ActivityLoginHelper(this);

        mBtnEntrar = (Button) findViewById(R.id.btnEntrar);
        mBtnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsuarioInformado = mHelper.getUsuario();

                if (validarCampoNulo())
                    consultarUsuario();
            }
        });
    }

    private void consultarUsuario() {
        Gson gson = new GsonBuilder()
                //.registerTypeAdapter(Usuario.class, new DeserializerUsuario())// new Gson().getAdapter(type.getClass()))
                .create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(Constantes.API_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UsuarioAPI usuarioAPI = retrofit.create(UsuarioAPI.class);

        startProgress();

        Call<Usuario> call = usuarioAPI.getUsuario(mUsuarioInformado.getLogin());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Response<Usuario> response, Retrofit retrofit) {
                mUsuarioSelecionado = response.body();

                if (validarUsuario())
                    invocarIntent();

                finalizeProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                finalizeProgress();

                Toast.makeText(getApplicationContext(),
                        "Ocorreu um erro ao consultar os dados, por favor tente novamente!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void finalizeProgress() {
        mProgresso.dismiss();
    }

    private void startProgress() {
        mProgresso = ProgressDialog.show(this, "Aguarde...", "Consultando dados do usuário.", true, false);
    }

    private void invocarIntent() {
        Intent itLogar = new Intent(ActivityLogin.this, ActivityListaConsultasMarcadas.class);
        itLogar.putExtra("usuarioLogado", mUsuarioSelecionado);
        startActivity(itLogar);
    }

    private boolean validarCampoNulo() {
        boolean camposValidos = true;

        if (mUsuarioInformado.getLogin().isEmpty() || mUsuarioInformado.getSenha().isEmpty()) {
            Util.showMessage("Acesso", "Usuário ou senha não informado.", this);
            camposValidos = false;
        }

        return camposValidos;
    }

    /**
     * @return Método responsável por verificar se os dados informados pelo usuário são válidos,
     * caso não sejam uma mensagem é mostrada ao usuário.
     */
    private boolean validarUsuario() {
        if (mUsuarioSelecionado.getLogin() == null) {
            Util.showMessage("Acesso", "Usuário não encontrado.", this);
            return false;
        } else if (!mUsuarioSelecionado.getSenha().equals(mUsuarioInformado.getSenha().toString())) {
            Util.showMessage("Acesso", "Senha incorreta.", this);
            return false;
        } else
            return true;
    }
}
