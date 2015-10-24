package br.com.prova.marcaconsultas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.com.prova.helpers.ActivityLoginHelper;
import br.com.prova.model.bean.Usuario;
import br.com.prova.model.dao.UsuarioDAO;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mHelper = new ActivityLoginHelper(this);

        mBtnEntrar = (Button) findViewById(R.id.btnEntrar);
        mBtnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Aqui é invocado o método de validação do usuário, caso o valor retornado seja True,
                 * a Activity de Consultas Marcadas é chamada e o usuario logado é passado, para a Intent,
                 * para que as demais Activities da aplicação possam controlar as permissões, de marcação
                 * de consulta e cancelamento das mesmas.
                 */
                if (validarUsuario()) {
                    Intent itLogar = new Intent(ActivityLogin.this, ActivityListaConsultasMarcadas.class);
                    itLogar.putExtra("usuarioLogado", (java.io.Serializable) mUsuarioSelecionado);
                    startActivity(itLogar);
                }
            }
        });
    }

    /**
     *
     * @return
     *
     * Método responsável por verificar se os dados informados pelo usuário são válidos,
     * caso não sejam uma mensagem é mostrada ao usuário.
     */
    private boolean validarUsuario() {
        /**
         * O método getUsuario(), retorna um BEAN de usuario, com os dados digitados na tela.
         */
        Usuario usuarioInformado = mHelper.getUsuario();

        /**
         * Verifica se existe algum campo vazio.
         */
        if (usuarioInformado.getLogin().isEmpty() || usuarioInformado.getSenha().isEmpty()) {
            Util.showMessage("Acesso", "Usuário ou senha não informado.", this);
            return false;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO(this);
        /**
         * Busca um usuário pelo login informado na tela.
         */
        mUsuarioSelecionado = usuarioDAO.selecionarPorLogin(usuarioInformado.getLogin().toLowerCase());

        if (mUsuarioSelecionado == null) {
            Util.showMessage("Acesso", "Usuário não encontrado.", this);
            return false;
        } else if (!mUsuarioSelecionado.getSenha().equals(usuarioInformado.getSenha().toString())) {
            Util.showMessage("Acesso", "Senha incorreta.", this);
            return false;
        } else
            return true;
    }
}
