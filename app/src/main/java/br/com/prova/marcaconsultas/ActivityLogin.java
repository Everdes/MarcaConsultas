package br.com.prova.marcaconsultas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import br.com.prova.helpers.ActivityLoginHelper;
import br.com.prova.model.bean.Usuario;
import br.com.prova.model.dao.UsuarioDAO;
import br.com.prova.task.TaskConsulta;
import br.com.prova.util.Util;

/**
 * Created by Éverdes on 27/09/2015.
 *
 * Activity responsável por lê os dados de login e senha digitados pelo usuário e realizar a validação dos mesmos,
 * permitindo o acesso e uso da aplicação caso os dados estejam corretos, ou barrando caso não estajam de acordo.
 */
public class ActivityLogin extends AppCompatActivity {

    private Button mBtnEntrar;
    private ActivityLoginHelper mHelper;
    private Usuario mUsuarioSelecionado;
    private EditText mEdtIP;
    private Usuario mUsuarioInformado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mHelper = new ActivityLoginHelper(this);

        mEdtIP = (EditText) findViewById(R.id.edtIP);

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
        // TODO Será implementado um método GET no WS, para trazer o usuario, caso esteja correto.
//        UsuarioWS usuarioWS = new UsuarioWS(this);
//        mUsuarioSelecionado = usuarioWS.selecionarPorLogin(usuarioInformado.getLogin().toLowerCase());

        new TaskConsulta("http://" + mEdtIP.getText().toString() + ":8090/WSAgendaMedica/usuario/autenticarUsuario/" + mUsuarioInformado.getLogin()) {

            ProgressDialog iProgresso;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                iProgresso = ProgressDialog.show(ActivityLogin.this, "Aguarde...", "Verificando usuário informado.");
            }

            @Override
            protected void onPostExecute(String retorno) {
                super.onPostExecute(retorno);

                if (retorno.isEmpty())
                Util.showMessage("Atenção", "Usuário não encontrado", ActivityLogin.this);
                else {
                    mUsuarioSelecionado = new Gson().fromJson(retorno, Usuario.class);
                    if (validarUsuario()) {
                        invocarIntent();
                    }
                }

                iProgresso.dismiss();
            }
        }.execute();
    }

    private void invocarIntent() {
        Intent itLogar = new Intent(ActivityLogin.this, ActivityListaConsultasMarcadas.class);
        itLogar.putExtra("usuarioLogado", (java.io.Serializable) mUsuarioSelecionado);
        itLogar.putExtra("IP", mEdtIP.getText().toString());
        startActivity(itLogar);
    }

    private boolean validarCampoNulo() {
        boolean camposValidos = true;

        if (mUsuarioInformado.getLogin().isEmpty() || mUsuarioInformado.getSenha().isEmpty()) {
            Util.showMessage("Acesso", "Usuário ou senha não informado.", this);
            camposValidos = false;
        }

        if (mEdtIP.getText().toString().isEmpty()){
            Util.showMessage("Acesso", "Obrigatório informar o IP do Web Service", this);
            camposValidos = false;
        }

        return camposValidos;
    }

    /**
     *
     * @return
     *
     * Método responsável por verificar se os dados informados pelo usuário são válidos,
     * caso não sejam uma mensagem é mostrada ao usuário.
     */
    private boolean validarUsuario() {
        if (mUsuarioSelecionado == null) {
            Util.showMessage("Acesso", "Usuário não encontrado.", this);
            return false;
        } else if (!mUsuarioSelecionado.getSenha().equals(mUsuarioInformado.getSenha().toString())) {
            Util.showMessage("Acesso", "Senha incorreta.", this);
            return false;
        } else
            return true;
    }
}
