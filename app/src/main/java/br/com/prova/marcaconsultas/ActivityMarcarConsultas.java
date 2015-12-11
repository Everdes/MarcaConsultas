package br.com.prova.marcaconsultas;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.adapters.AdapterAgendaMedica;
import br.com.prova.interfaces.AgendaMedicaAPI;
import br.com.prova.model.bean.AgendaMedica;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Especialidade;
import br.com.prova.model.bean.LocalAtendimento;
import br.com.prova.model.bean.Medico;
import br.com.prova.model.bean.Usuario;
import br.com.prova.model.dao.AgendaMedicoDAO;
import br.com.prova.model.dao.ConsultaMarcadaDAO;
import br.com.prova.model.dao.EspecialidadeDAO;
import br.com.prova.model.dao.LocalAtendimentoDAO;
import br.com.prova.model.dao.MedicoDAO;
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
 * Activity responsavel por exibir a Agenda dos médicos, e por controlar e permitir, a marcação de consultas.
 */
public class ActivityMarcarConsultas extends AppCompatActivity {

    private Usuario mUsuarioLogado;

    private List<AgendaMedica> mListaAgendaMedica;
    private List<LocalAtendimento> mLocalAtendimentosFiltro;
    private List<Medico> mMedicosFiltro;

    private List<Especialidade> mEspecialidades;
    private ConsultaMarcadaDAO mConsultaMarcadaDAO;
    private AgendaMedicoDAO mAgendaMedicoDAO;
    private EspecialidadeDAO mEspecialidadeDAO;
    private LocalAtendimentoDAO mLocalAtendimentoDAO;
    private MedicoDAO mMedicoDAO;

    private RecyclerView mRvAgendaMedica;
    private ProgressDialog mProgresso;
    private Spinner mSpnFiltro;
    private ArrayAdapter<String> mAdapterSpinner;
    private RadioGroup mRadGrpFiltro;

    private Gson mGson = new GsonBuilder().create();
    private Retrofit mRetrofit = new Retrofit
            .Builder()
            .baseUrl(Constantes.API_BASE)
            .addConverterFactory(GsonConverterFactory.create(mGson))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_consultas);

        mAgendaMedicoDAO = new AgendaMedicoDAO(this);
        mConsultaMarcadaDAO = new ConsultaMarcadaDAO(this);
        mLocalAtendimentoDAO = new LocalAtendimentoDAO(this);
        mMedicoDAO = new MedicoDAO(this);
        mEspecialidadeDAO = new EspecialidadeDAO(this);

        mRadGrpFiltro = (RadioGroup) findViewById(R.id.radGrpFiltro);
        mRadGrpFiltro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                List<String> listaSpinner = new ArrayList<>();
                TextView txtSelecione = (TextView) findViewById(R.id.txtSelecione);
                txtSelecione.setVisibility(View.VISIBLE);

                /**
                 * De acordo com o radio button checado é listado uma opção de filtro, e alimentada
                 * uma lista de String, com os dados que aparecerão no Spinner, de escolha do usuário.
                 */
                switch (checkedId) {
                    case R.id.radBtnLocal:
                        mLocalAtendimentosFiltro = mLocalAtendimentoDAO.listar();

                        for (LocalAtendimento localAtendimento : mLocalAtendimentosFiltro)
                            listaSpinner.add(localAtendimento.getNome());

                        alimentarSpinner(listaSpinner);
                        break;
                    case R.id.radBtnMedico:
                        mMedicosFiltro = mMedicoDAO.listar();

                        for (Medico medico : mMedicosFiltro)
                            listaSpinner.add(medico.getNome());

                        alimentarSpinner(listaSpinner);
                        break;
                    case R.id.radBtnEspecialidade:
                        mEspecialidades = mEspecialidadeDAO.listar();

                        for (Especialidade especialidade : mEspecialidades)
                            listaSpinner.add(especialidade.getNome());

                        alimentarSpinner(listaSpinner);
                        break;
//                    case R.id.radBtnData:
//                        for (AgendaMedica agendaMedica : mListaAgendaMedica)
//                            listaSpinner.add(Util.convertDateToStr(agendaMedica.getData()));
//
//                        alimentarSpinner(listaSpinner);
//
//                        break;
                }
            }
        });

        mSpnFiltro = (Spinner) findViewById(R.id.spnFiltro);
        /**
         * De acordo com o radio button checado, quando um item do Spinner é selecionado a lista de Agenda Medico
         * é alimentada, com os dados relacionados ao filtro.
         */
        mSpnFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (mRadGrpFiltro.getCheckedRadioButtonId()) {
                    case R.id.radBtnLocal:
                        mListaAgendaMedica = mAgendaMedicoDAO.listarPorLocalAtendimento(mLocalAtendimentosFiltro.get(position));
                        break;
                    case R.id.radBtnMedico:
                        mListaAgendaMedica = mAgendaMedicoDAO.listarPorMedico(mMedicosFiltro.get(position));
                        break;
                    case R.id.radBtnEspecialidade:
                        mMedicosFiltro = mMedicoDAO.listarPorEspecialidade(mEspecialidades.get(position));

                        mListaAgendaMedica.clear();
                        for (Medico medico : mMedicosFiltro) {
                            mListaAgendaMedica.addAll(mAgendaMedicoDAO.listarPorMedico(medico));
                        }
                        break;
                    case R.id.radBtnData:
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(mAdapterSpinner.getItem(position));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mListaAgendaMedica = mAgendaMedicoDAO.listarPorData(date);
                        break;
                }

                listarAgendaMedico();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fabMarcarConsulta = (FloatingActionButton) findViewById(R.id.fabMarcarConsulta);
        fabMarcarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AgendaMedica> agendaMedicaSelecionadas = getAgendaMedicoSelecionadas();

                if (agendaMedicaSelecionadas.size() == 1) {

                    if (criticarCarencia(agendaMedicaSelecionadas.get(0))) {
                        Util.showMessage("Aviso",
                                "Não é possível marcar um consulta para a mesma especialidade, em menos de 30 dias", ActivityMarcarConsultas.this);

                    } else if (mConsultaMarcadaDAO.inserir(getConsultaMarcada(agendaMedicaSelecionadas.get(0)))) {
                        listarAgendaMedico();
                        Util.enviarEmail(ActivityMarcarConsultas.this, new String[]{mUsuarioLogado.getEmail()}, "A sua consulta foi marcada com sucesso.");
                    } else
                        Util.showMessage("Aviso", "Não foi possível marcar consulta.", ActivityMarcarConsultas.this);
                } else {
                    Util.showMessage("Aviso", "Para marcar uma consulta, selecione somente um item da lista.", ActivityMarcarConsultas.this);
                }

            }
        });

        ImageButton btnVoltar = (ImageButton) findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUsuarioLogado = (Usuario) getIntent().getSerializableExtra("usuarioLogado");

        LinearLayoutManager llManager = new LinearLayoutManager(this);

        mRvAgendaMedica = (RecyclerView) findViewById(R.id.rvAgendaMedica);
        mRvAgendaMedica.setHasFixedSize(true);
        mRvAgendaMedica.setLayoutManager(llManager);
        mRvAgendaMedica.addOnScrollListener(new OnScrollListener(llManager, fabMarcarConsulta) {
            @Override
            public void onScroll(RecyclerView recyclerView, int dx, int dy, boolean onScroll) {

            }

            @Override
            public void onLoadMore(int currentPage) {

            }
        });

        listarAgendaMedico();
    }

    /**
     * Método que recebe uma lista e seta no Spinner, para que o usuário possa escolher um filtro.
     *
     * @param listaSpinner
     */
    private void alimentarSpinner(List<String> listaSpinner) {
        mAdapterSpinner =
                new ArrayAdapter<String>(ActivityMarcarConsultas.this, android.R.layout.simple_spinner_dropdown_item, listaSpinner);
        mSpnFiltro.setAdapter(mAdapterSpinner);
    }

    /**
     * Método que recebe uma Agenda Medico e retorna um BEAN de Consulta Marcada.
     *
     * @param agendaMedica
     * @return
     */
    private ConsultaMarcada getConsultaMarcada(AgendaMedica agendaMedica) {
        ConsultaMarcada consultaMarcada = new ConsultaMarcada();

//        consultaMarcada.setIdAgendaMedico(agendaMedica.getId());
        consultaMarcada.setUsuario(mUsuarioLogado);
        consultaMarcada.setDataMarcacaoConsulta(Calendar.getInstance().getTime());
        consultaMarcada.setSituacao(Situacao.MARCADA);

        return consultaMarcada;
    }

    /**
     * Método que atualiza o List View da AgendaMedico.
     */
    private void atualizarLista() {
        if (!mListaAgendaMedica.isEmpty()) {
            /**
             * Seta um adaptador personalizado que "transforma" os dados da lista, em componentes de tela do List View.
             */
            mRvAgendaMedica.setAdapter(new AdapterAgendaMedica(this, mListaAgendaMedica));
        }
    }

    /**
     * Método que retorna uma lista de AgendaMedico disponíveis.
     */
    private void listarAgendaMedico() {
//        mListaAgendaMedica = mAgendaMedicoDAO.listarPorSituacao(Situacao.DISPONIVEL);

        AgendaMedicaAPI api = mRetrofit.create(AgendaMedicaAPI.class);

        Call<List<AgendaMedica>> call = api.listarAgendaMedica();
        call.enqueue(new Callback<List<AgendaMedica>>() {
            @Override
            public void onResponse(Response<List<AgendaMedica>> response, Retrofit retrofit) {
                mListaAgendaMedica = response.body();

                atualizarLista();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ActivityMarcarConsultas.this, "Ocorreu um erro ao consultar a Agenda Médica.", Toast.LENGTH_LONG).show();
            }
        });


//        new TaskConsulta() {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//                mProgresso = ProgressDialog.show(ActivityMarcarConsultas.this, "Aguarde...", "Consultando o servidor...", true, false);
//            }
//
//            @Override
//            protected void onPostExecute(String retorno) {
//                super.onPostExecute(retorno);
//
//                if (retorno.toString().isEmpty())
//                    Toast.makeText(ActivityMarcarConsultas.this, retorno, Toast.LENGTH_LONG).show();
//                else {
//                    Type type = new TypeToken<List<AgendaMedica>>() {
//                    }.getType();
//                    Gson gson = new Gson();
//                    mListaAgendaMedica = gson.fromJson(retorno, type);
//
//                    atualizarLista();
//                }
//
//                mProgresso.dismiss();
//            }
//        }.execute();
    }

    private List<AgendaMedica> getAgendaMedicoSelecionadas() {
        List<AgendaMedica> agendaMedicaSelecionadas = new ArrayList<>();

//        for (int i = 0; i < mLvAgendaMedico.getCount(); i++) {
//            if (mLvAgendaMedico.getChildAt(i) != null)
//                if ((CheckBox) mLvAgendaMedico.getChildAt(i).findViewById(R.id.chkBoxListaConsulta) != null) {
//
//                    CheckBox cBox = (CheckBox) mLvAgendaMedico.getChildAt(i).findViewById(R.id.chkBoxListaConsulta);
//                    if (cBox.isChecked())
//                        agendaMedicaSelecionadas.add((AgendaMedica) mLvAgendaMedico.getItemAtPosition(i));
//                }
//        }
        return agendaMedicaSelecionadas;
    }

    /**
     * Método que verifica se o usuário marcou outra consulta para a mesma especialidade, num período
     * inferior a 30 dias.
     */
    private boolean criticarCarencia(AgendaMedica agendaMedicaSelecionada) {
        boolean criticado = false;
        AgendaMedica agendaMedica;

        List<ConsultaMarcada> consultasMarcadasPeloUsuario = mConsultaMarcadaDAO.listarMarcadasPorUsuario(mUsuarioLogado);

        for (ConsultaMarcada consultaMarcadaAnterior : consultasMarcadasPeloUsuario) {
            agendaMedica = mAgendaMedicoDAO.selecionarPorId(consultaMarcadaAnterior.getIdAgendaMedico());
            if (agendaMedica != null)
                if (agendaMedica.getMedico().getEspecialidade().getId() == agendaMedicaSelecionada.getMedico().getEspecialidade().getId())
                    if (agendaMedicaSelecionada.getDataAgenda().compareTo(agendaMedica.getDataAgenda()) <= 30) {
                        criticado = true;
                        break;
                    }
        }
        return criticado;
    }
}
