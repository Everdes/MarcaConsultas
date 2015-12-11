package br.com.prova.marcaconsultas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

import br.com.prova.adapters.AdapterConsultaMarcada;
import br.com.prova.interfaces.ConsultaMarcadaAPI;
import br.com.prova.interfaces.events.OnItemClickListener;
import br.com.prova.interfaces.events.OnItemLongClickListener;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Usuario;
import br.com.prova.util.Constantes;
import br.com.prova.util.OnScrollListener;
import br.com.prova.util.Util;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Éverdes on 27/09/2015.
 * <p/>
 * Classe responsável por exibir a lista de Consultas Marcadas pelo mesmo e oferecer as opções de
 * pesquisar o endereço da consulta ou cancelá-la.
 */
public class ActivityListaConsultasMarcadas extends AppCompatActivity {

    private List<ConsultaMarcada> mConsultasMarcadas;

    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;

    private AdapterConsultaMarcada mAdapter;
    private ConsultaMarcada mConsultaSelecionada;

    private Usuario mUsuarioLogado;

    private Gson mGson = new GsonBuilder().create();

    private Retrofit mRetrofit = new Retrofit
            .Builder()
            .baseUrl(Constantes.API_BASE)
            .addConverterFactory(GsonConverterFactory.create(mGson))
            .build();

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            mConsultaSelecionada = mConsultasMarcadas.get(position);

            showMap();
        }
    };
    private OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
        @Override
        public void onItemLongClick(View v, int position) {
            mConsultaSelecionada = mConsultasMarcadas.get(position);

            desmarcarConsulta(position);
        }
    };
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            callProgress();
        }
    };

    private void callProgress() {
        mSwipeContainer.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeContainer.setRefreshing(false);
            }
        }, 2000);


    }

    private void showMap() {
        Intent itAbrirMapa = new Intent(this, ActivityMostrarMapa.class);
        itAbrirMapa.putExtra("endereco", mConsultaSelecionada.getAgendaMedica().getLocalAtendimento().getEndereco());
        startActivity(itAbrirMapa);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consultas_marcadas);

        /**
         * Lê o usuario enviado pela ActivityLogin
         */
        mUsuarioLogado = (Usuario) getIntent().getSerializableExtra("usuarioLogado");

        FloatingActionButton mFabNovo = (FloatingActionButton) findViewById(R.id.fabNovo);
        mFabNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Chama a Activity de Marcação de Consulta, passando o usuario logado,
                 * para que a mesma possa controlar as RN de marcação de consulta por usuário.
                 */
                Intent itMarcarConsulta = new Intent(ActivityListaConsultasMarcadas.this, ActivityMarcarConsultas.class);
                itMarcarConsulta.putExtra("usuarioLogado", mUsuarioLogado);
                startActivity(itMarcarConsulta);
            }
        });

        LinearLayoutManager llManager = new LinearLayoutManager(getParent());

        OnScrollListener endlessListener = new OnScrollListener(llManager) {

            @Override
            public void onScroll(RecyclerView recyclerView, int dx, int dy, boolean onScroll) {

            }

            @Override
            public void onLoadMore(int currentPage) {

            }
        };

        mSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(onRefreshListener);
        mSwipeContainer.setColorSchemeResources(R.color.red, R.color.orange, R.color.yellow, R.color.primary);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llManager);
        mRecyclerView.addOnScrollListener(endlessListener);

        registerForContextMenu(mRecyclerView);

        listarConsultasMarcadas();
//        AdapterAgendaMedica adapterAgendaMedica = new AdapterAgendaMedica(ActivityListaConsultasMarcadas.this, mConsultasMarcadas);
//        mRecyclerView.setAdapter(adapterAgendaMedica);

//        mBtnFloatAdd.setOnClickListener(onClickFloatAdd);


    }

    private void createAdapter() {
        mAdapter = new AdapterConsultaMarcada(this, mConsultasMarcadas);
        mAdapter.setOnItemClickListener(onItemClickListener);
        mAdapter.setOnItemLongClickListener(onItemLongClickListener);
    }

    /**
     * Método que chama a lista de Consultas Marcadas.
     * Caso o usuário logado seja Adm as Consultas de todos os usuários são retornadas,
     * caso seja User, somente as suas consultas são retornadas.
     */
    private void listarConsultasMarcadas() {
        Call<List<ConsultaMarcada>> call;
        ConsultaMarcadaAPI consultaMarcadaAPI = mRetrofit.create(ConsultaMarcadaAPI.class);

        if (mUsuarioLogado.getPerfil().equals("A"))
            call = consultaMarcadaAPI.getConsultaMarcada();
        else
            call = consultaMarcadaAPI.getConsultaMarcadaPorUsuario(mUsuarioLogado.getId());

        call.enqueue(new Callback<List<ConsultaMarcada>>() {
            @Override
            public void onResponse(Response<List<ConsultaMarcada>> response, Retrofit retrofit) {
                mConsultasMarcadas = response.body();

                atualizarLista();
            }

            @Override
            public void onFailure(Throwable t) {
                Util.showMessage("Erro",
                        "Ocorreu um erro ao consultar os dados, por favor tente novamente.",
                        getApplicationContext());
            }
        });
    }

    /**
     * Método responsável por desmarcar a consulta selecionada pelo usuário.
     */
    private void desmarcarConsulta(int position) {
        /**
         * Verifica se o usuario logado é o mesmo que marcou a consulta, e caso seja a consulta é cancelada,
         * caso contrário um alerta é mostrado para o usuário.
         */
        if (criticarHoras()) {
            Util.showMessage("Aviso", "Não é possível desmarcar consultas com menos de 24 horas.", this);
            return;
        } else if (mConsultaSelecionada.getUsuario().getId() == mUsuarioLogado.getId()) {
            mAdapter.remove(position);
            Util.enviarEmail(ActivityListaConsultasMarcadas.this, new String[]{mUsuarioLogado.getEmail()}, "Consulta desmarcada pelo usuario.");
//            if (mConsultaMarcadaDAO.cancelar(mConsultaSelecionada, Util.getToday())) {
//                atualizarLista();
//                Util.enviarEmail(ActivityListaConsultasMarcadas.this, new String[]{mUsuarioLogado.getEmail()}, "Consulta desmarcada pelo usuario.");
//            } else
//                Util.showMessage("Aviso", "Não foi possível desmarcar esta consulta.", this);
        } else
            Util.showMessage("Aviso", "Não é possível desmarcar consultas de outros usuários", this);
    }

    /**
     * Método que verifica a quantidade de horas faltantes, para a realização da consulta,
     * caso a diferença seja de menos de 24 horas ele retorna True, caso contrário False.
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
        createAdapter();

        if (!mConsultasMarcadas.isEmpty()) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
