package br.com.prova.helpers;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import br.com.prova.marcaconsultas.R;
import br.com.prova.model.bean.Usuario;

/**
 * Created by Éverdes on 29/09/2015.
 *
 * Classe helper da ActivityLogin, responsável por ler os dados digitados pelo usuário,
 * e devolver uma forma de um Bean de usuário para a activity
 */
public class ActivityLoginHelper extends AppCompatActivity {

    private EditText mEdtUsuario;
    private EditText mEdtSenha;

    private Usuario mUsuario;

    /**
     * @param activity
     *
     * Método construtor que liga os componentes da tela as variáveis de instância
     * e inicializa um Bean
     */
    public ActivityLoginHelper(AppCompatActivity activity) {
        mEdtUsuario = (EditText) activity.findViewById(R.id.edtUsuario);
        mEdtSenha = (EditText) activity.findViewById(R.id.edtSenha);

        mUsuario = new Usuario();
    }

    /**
     * @return
     *
     * Método que lê os dados da tela e apartir deles alimenta e devolve um Bean
     */

    public Usuario getUsuario(){
        mUsuario.setLogin(mEdtUsuario.getText().toString());
        mUsuario.setSenha(mEdtSenha.getText().toString());

        return mUsuario;
    }
}
