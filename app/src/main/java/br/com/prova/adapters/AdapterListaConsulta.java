package br.com.prova.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.prova.marcaconsultas.R;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.util.Util;

/**
 * Created by Éverdes on 27/09/2015.
 * <p/>
 * Classe responsável por receber uma lista de ConsultaMarcada, e adaptar para um ListView
 */
public class AdapterListaConsulta extends BaseAdapter {
    Context mContexto;
    List<ConsultaMarcada> mLista;

    public AdapterListaConsulta(Context contexto, List<ConsultaMarcada> lista) {
        mContexto = contexto;
        mLista = lista;
    }


    @Override
    public int getCount() {
        return mLista.size();
    }

    @Override
    public Object getItem(int position) {
        return mLista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return convertView
     * <p/>
     * Método responsável por ler os dados da lista, de AgendaMedico e converter em objetos do ListView.
     * Este método é chamado para cada item da lista
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.modelo_lista_consulta_medica, null);

            ConsultaMarcada consultaMarcada = mLista.get(position);

            ((TextView) convertView.findViewById(R.id.txtNome)).setText(consultaMarcada.getUsuario().toString());
            ((TextView) convertView.findViewById(R.id.txtDataHora)).
                    setText(Util.convertDateToStr(consultaMarcada.getAgendaMedico().getData()));
            ((TextView) convertView.findViewById(R.id.txtMedico)).setText(consultaMarcada.getAgendaMedico().getMedico().getNome());
            ((TextView) convertView.findViewById(R.id.txtLocal)).setText(consultaMarcada.getAgendaMedico().getLocalAtendimento().getNome());

//            TODO Consultando as DAO
//            AgendaMedicoDAO agendaMedicoDAO = new AgendaMedicoDAO(mContexto);
//            AgendaMedico agendaMedico;
//            agendaMedico = agendaMedicoDAO.selecionarPorId(consultaMarcada.getIdAgendaMedico());
//
//            ((TextView) convertView.findViewById(R.id.txtNome)).setText(consultaMarcada.getUsuario().toString());
//            ((TextView) convertView.findViewById(R.id.txtDataHora)).
//                    setText(Util.convertDateToStr(agendaMedico.getData()) + " " +
//                    agendaMedico.getHora());
//            ((TextView) convertView.findViewById(R.id.txtMedico)).setText(agendaMedico.getMedico().getNome());
//            ((TextView) convertView.findViewById(R.id.txtLocal)).setText(agendaMedico.getLocalAtendimento().getNome());
        }
        return convertView;

    }
}
