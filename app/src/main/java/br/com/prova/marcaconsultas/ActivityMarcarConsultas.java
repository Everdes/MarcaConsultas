package br.com.prova.marcaconsultas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.adapters.AdapterListaAgendaMedico;
import br.com.prova.model.bean.AgendaMedico;
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
import br.com.prova.util.Util;

/**
 * Created by Éverdes on 27/09/2015.
 *
 * Activity responsavel por exibir a Agenda dos médicos, e por controlar e permitir, a marcação de consultas.
 */
public class ActivityMarcarConsultas extends AppCompatActivity {

    private List<AgendaMedico> mListaAgendaMedico;
    private ListView mLvAgendaMedico;
    private AgendaMedicoDAO mAgendaMedicoDAO;
    private FloatingActionButton mFabMarcarConsulta;
    private ConsultaMarcadaDAO mConsultaMarcadaDAO;
    private Usuario mUsuarioLogado;
    private LocalAtendimentoDAO mLocalAtendimentoDAO;
    private MedicoDAO mMedicoDAO;
    private EspecialidadeDAO mEspecialidadeDAO;
    private Spinner mSpnFiltro;
    private ArrayAdapter<String> mAdapterSpinner;
    private List<LocalAtendimento> mLocalAtendimentosFiltro;
    private List<Medico> mMedicosFiltro;
    private List<Especialidade> mEspecialidades;
    private RadioGroup mRadGrpFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_consultas);

        mAgendaMedicoDAO = new AgendaMedicoDAO(this);
        mConsultaMarcadaDAO = new ConsultaMarcadaDAO(this);
        mLocalAtendimentoDAO = new LocalAtendimentoDAO(this);
        mMedicoDAO = new MedicoDAO(this);
        mEspecialidadeDAO = new EspecialidadeDAO(this);

        mLvAgendaMedico = (ListView) findViewById(R.id.lvAgendaMedico);

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
                    case R.id.radBtnData:
                        for (AgendaMedico agendaMedico : mListaAgendaMedico)
                            listaSpinner.add(Util.convertDateToStr(agendaMedico.getData()));

                        alimentarSpinner(listaSpinner);

                        break;
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
                        mListaAgendaMedico = mAgendaMedicoDAO.listarPorLocalAtendimento(mLocalAtendimentosFiltro.get(position));
                        break;
                    case R.id.radBtnMedico:
                        mListaAgendaMedico = mAgendaMedicoDAO.listarPorMedico(mMedicosFiltro.get(position));
                        break;
                    case R.id.radBtnEspecialidade:
                        mMedicosFiltro = mMedicoDAO.listarPorEspecialidade(mEspecialidades.get(position));

                        mListaAgendaMedico.clear();
                        for(Medico medico : mMedicosFiltro) {
                            mListaAgendaMedico.addAll(mAgendaMedicoDAO.listarPorMedico(medico));
                        }
                        break;
                    case R.id.radBtnData:
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("dd/MM/yyyy").parse(mAdapterSpinner.getItem(position));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mListaAgendaMedico = mAgendaMedicoDAO.listarPorData(date);
                        break;
                }

                atualizarLista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFabMarcarConsulta = (FloatingActionButton) findViewById(R.id.fabMarcarConsulta);
        mFabMarcarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AgendaMedico> agendaMedicoSelecionadas = getAgendaMedicoSelecionadas();

                if (agendaMedicoSelecionadas.size() == 1) {

                    if (criticarCarencia(agendaMedicoSelecionadas.get(0))) {
                        Util.showMessage("Aviso",
                                "Não é possível marcar um consulta para a mesma especialidade, em menos de 30 dias", ActivityMarcarConsultas.this);

                    } else if (mConsultaMarcadaDAO.inserir(getConsultaMarcada(agendaMedicoSelecionadas.get(0)))) {
                        listarAgendaMedico();
                        atualizarLista();
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

        listarAgendaMedico();
        atualizarLista();
    }

    /**
     * Método que recebe uma lista e seta no Spinner, para que o usuário possa escolher um filtro.
     * @param listaSpinner
     */
    private void alimentarSpinner(List<String> listaSpinner) {
        mAdapterSpinner =
                new ArrayAdapter<String>(ActivityMarcarConsultas.this, android.R.layout.simple_spinner_dropdown_item, listaSpinner);
        mSpnFiltro.setAdapter(mAdapterSpinner);
    }

    /**
     * Método que recebe uma Agenda Medico e retorna um BEAN de Consulta Marcada.
     * @param agendaMedico
     * @return
     */
    private ConsultaMarcada getConsultaMarcada(AgendaMedico agendaMedico) {
        ConsultaMarcada consultaMarcada = new ConsultaMarcada();

        consultaMarcada.setIdAgendaMedico(agendaMedico.getId());
        consultaMarcada.setUsuario(mUsuarioLogado);
        consultaMarcada.setDataMarcacaoConsulta(Calendar.getInstance().getTime());
        consultaMarcada.setSituacao(Situacao.MARCADA);

        return consultaMarcada;
    }

    /**
     * Método que atualiza o List View da AgendaMedico.
     */
    private void atualizarLista() {
        if (mListaAgendaMedico != null) {
            /**
             * Seta um adaptador personalizado que "transforma" os dados da lista, em componentes de tela do List View.
             */
            mLvAgendaMedico.setAdapter(new AdapterListaAgendaMedico(this, mListaAgendaMedico));
        }
    }

    /**
     * Método que retorna uma lista de AgendaMedico disponíveis.
     */
    private void listarAgendaMedico() {
        mListaAgendaMedico = mAgendaMedicoDAO.listarPorSituacao(Situacao.DISPONIVEL);
    }

    /**
     *
     * @return
     *
     * Método que retorna os itens selecionados no ListView.
     */
    private List<AgendaMedico> getAgendaMedicoSelecionadas() {
        List<AgendaMedico> agendaMedicoSelecionadas = new ArrayList<>();

        for (int i = 0; i < mLvAgendaMedico.getCount(); i++) {
            if (mLvAgendaMedico.getChildAt(i) != null)
                if ((CheckBox) mLvAgendaMedico.getChildAt(i).findViewById(R.id.chkBoxListaConsulta) != null) {

                    CheckBox cBox = (CheckBox) mLvAgendaMedico.getChildAt(i).findViewById(R.id.chkBoxListaConsulta);
                    if (cBox.isChecked())
                        agendaMedicoSelecionadas.add((AgendaMedico) mLvAgendaMedico.getItemAtPosition(i));
                }
        }
        return agendaMedicoSelecionadas;
    }

    /**
     *
     * @param agendaMedicoSelecionada
     * @return
     *
     * Método que verifica se o usuário marcou outra consulta para a mesma especialidade, num período
     * inferior a 30 dias.
     */
    private boolean criticarCarencia(AgendaMedico agendaMedicoSelecionada) {
        boolean criticado = false;
        AgendaMedico agendaMedico = null;

        List<ConsultaMarcada> consultasMarcadasPeloUsuario = mConsultaMarcadaDAO.listarMarcadasPorUsuario(mUsuarioLogado);

        for (ConsultaMarcada consultaMarcadaAnterior : consultasMarcadasPeloUsuario) {
            agendaMedico = mAgendaMedicoDAO.selecionarPorId(consultaMarcadaAnterior.getIdAgendaMedico());
            if (agendaMedico != null)
                if (agendaMedico.getMedico().getEspecialidade().getId() == agendaMedicoSelecionada.getMedico().getEspecialidade().getId())
                    if (agendaMedicoSelecionada.getData().compareTo(agendaMedico.getData()) <= 30) {
                        criticado = true;
                        break;
                    }
        }
        return criticado;
    }
}
