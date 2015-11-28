package br.com.prova.marcaconsultas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import br.com.prova.adapters.AdapterListaConsulta;
import br.com.prova.model.bean.AgendaMedica;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Usuario;
import br.com.prova.model.dao.AgendaMedicoDAO;
import br.com.prova.model.dao.ConsultaMarcadaDAO;
import br.com.prova.task.TaskConsulta;
import br.com.prova.util.Util;

/**
 * Created by Éverdes on 27/09/2015.
 * <p/>
 * Classe responsável por exibir a lista de Consultas Marcadas pelo mesmo e oferecer as opções de
 * pesquisar o endereço da consulta ou cancelá-la.
 */
public class ActivityListaConsultasMarcadas extends AppCompatActivity {

    private List<ConsultaMarcada> mConsultasMarcadas;
    private ListView mLvConsultasMarcadas;
    private ConsultaMarcada mConsultaSelecionada;
    private Usuario mUsuarioLogado;
    private ConsultaMarcadaDAO mConsultaMarcadaDAO;
    private FloatingActionButton mFabNovo;
    private AgendaMedica mAgendaMedicaSelecionado;
    private ProgressDialog mProgresso;

    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consultas_marcadas);

        /**
         * Lê o usuario enviado pela ActivityLogin
         */
        mUsuarioLogado = (Usuario) getIntent().getSerializableExtra("usuarioLogado");
        IP = getIntent().getStringExtra("IP");

        mConsultaMarcadaDAO = new ConsultaMarcadaDAO(this);

        mFabNovo = (FloatingActionButton) findViewById(R.id.fabNovo);
        mFabNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Chama a Activity de Marcação de Consulta, passando o usuario logado,
                 * para que a mesma possa controlar as RN de marcação de consulta por usuário.
                 */
                Intent itMarcarConsulta = new Intent(ActivityListaConsultasMarcadas.this, ActivityMarcarConsultas.class);
                itMarcarConsulta.putExtra("usuarioLogado", mUsuarioLogado);
                itMarcarConsulta.putExtra("IP", IP);
                startActivity(itMarcarConsulta);
            }
        });

        mLvConsultasMarcadas = (ListView) findViewById(R.id.lvConsultasMarcadas);
        /**
         * registrando o ListView em um Menu de Contexto
         */
        registerForContextMenu(mLvConsultasMarcadas);
        mLvConsultasMarcadas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * Pega o item selecionado no ListView
                 */
                mConsultaSelecionada = mConsultasMarcadas.get(position);

                AgendaMedicoDAO agendaMedicoDAO = new AgendaMedicoDAO(getApplicationContext());
                /**
                 * Pega a AgendaMedico do item selecionado no ListView
                 */
                mAgendaMedicaSelecionado = agendaMedicoDAO.selecionarPorId(mConsultaSelecionada.getIdAgendaMedico());

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        listarConsultasMarcadas();
    }

    /**
     * @return Método que chama a lista de Consultas Marcadas.
     * Caso o usuário logado seja Adm as Consultas de todos os usuários são retornadas,
     * caso seja User, somente as suas consultas são retornadas.
     */
    private void listarConsultasMarcadas() {
        if (IP.toString().isEmpty())
            IP = "localhost";

        if (mUsuarioLogado.getPerfil().equals("A"))
            consultarWS("http://" + IP + ":8090/WSAgendaMedica/consultaMarcada/listarConsultaMarcada");
        else
            consultarWS("http://" + IP + ":8090/WSAgendaMedica/consultaMarcada/listarConsultaMarcadaPorUsuario/" + mUsuarioLogado.getId());
    }

    private void consultarWS(String url) {
        new TaskConsulta(url) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mProgresso = ProgressDialog.show(ActivityListaConsultasMarcadas.this, "Aguarde...", "Consultando o servidor...", true, false);
            }

            @Override
            protected void onPostExecute(String retorno) {
                super.onPostExecute(retorno);

                if (retorno.toString().isEmpty())
                    Toast.makeText(ActivityListaConsultasMarcadas.this, retorno, Toast.LENGTH_LONG).show();
                else {
                    Type type = new TypeToken<List<ConsultaMarcada>>() {
                    }.getType();
                    Gson gson = new Gson();
                    mConsultasMarcadas = gson.fromJson(retorno, type);

                    atualizarLista();
                }

                mProgresso.dismiss();
            }
        }.execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contexto_marcar_consulta, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMostrarNoMapa:
                /**
                 * Caso o usuário selecione a opção Mostrar no Mapa, a Activity Mostar Mapa é invocada,
                 * passando para ela o endereço da AgendaMedico, da Consulta selecionada.
                 */
                Intent itAbrirMapa = new Intent(this, ActivityMostrarMapa.class);
                itAbrirMapa.putExtra("endereco", mAgendaMedicaSelecionado.getLocalAtendimento().getEndereco());
                startActivity(itAbrirMapa);
                break;
            case R.id.menuDesmarcarConsulta:
                /**
                 * Caso o usuário selecione a opção Desmarcar Consulta, o método que desmarca a consulta é invocado.
                 */
                desmarcarConsulta();
                break;
            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    /**
     * Método responsável por desmarcar a consulta selecionada pelo usuário.
     */
    private void desmarcarConsulta() {
        /**
         * Verifica se o usuario logado é o mesmo que marcou a consulta, e caso seja a consulta é cancelada,
         * caso contrário um alerta é mostrado para o usuário.
         */
        if (criticarHoras()) {
            Util.showMessage("Aviso", "Não é possível desmarcar consultas com menos de 24 horas.", this);
            return;
        } else if (mConsultaSelecionada.getUsuario().getId() == mUsuarioLogado.getId())
            if (mConsultaMarcadaDAO.cancelar(mConsultaSelecionada, Util.getToday())) {
                // TODO Depois esse metodo abaixo deve ser substituido por listarConsultasMarcadas()
                atualizarLista();
                Util.enviarEmail(ActivityListaConsultasMarcadas.this, new String[]{mUsuarioLogado.getEmail()}, "Consulta desmarcada pelo usuario.");
            } else
                Util.showMessage("Aviso", "Não foi possível desmarcar esta consulta.", this);
        else
            Util.showMessage("Aviso", "Não é possível desmarcar consultas de outros usuários", this);
    }

    /**
     * Método que verifica a quantidade de horas faltantes, para a realização da consulta,
     * caso a diferença seja de menos de 24 horas ele retorna True, caso contrário False.
     *
     * @return
     */
    private boolean criticarHoras() {
        Date dataConsulta = null;

//        try {
//            dataConsulta = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mAgendaMedicaSelecionado.getData() + " " + mAgendaMedicaSelecionado.getHora());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long diferencaMiliSegundos = dataConsulta.getTime() - System.currentTimeMillis();
//        long diferencaHoras = (diferencaMiliSegundos / (60 * 60 * 1000));
//
//        if (diferencaHoras < 24)
//            return true;
//        else
        return false;
    }

    /**
     * Método que atualiza o List View de Consultas Marcadas
     */
    public void atualizarLista() {
        /**
         * Seta um adaptador personalizado que "transforma" os dados da lista, em componentes de tela do List View
         */
        if (mConsultasMarcadas != null) {
            mLvConsultasMarcadas.setAdapter(new AdapterListaConsulta(this, mConsultasMarcadas));
        }
    }
}
