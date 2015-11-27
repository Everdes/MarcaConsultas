package br.com.prova.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import br.com.prova.marcaconsultas.R;
import br.com.prova.model.bean.AgendaMedica;
import br.com.prova.model.bean.Medico;
import br.com.prova.util.Util;

/**
 * Created by Éverdes on 28/09/2015.
 * <p/>
 * Classe responsável por receber uma lista de AgendaMedico, e adaptar para um ListView
 */
public class AdapterListaAgendaMedico extends BaseAdapter {

    private Context mContexto;
    private List<AgendaMedica> mListaAgendaMedica;

    public AdapterListaAgendaMedico(Context contexto, List listaAgendaMedico) {
        mContexto = contexto;
        mListaAgendaMedica = listaAgendaMedico;
    }

    @Override
    public int getCount() {
        return mListaAgendaMedica.size();
    }

    @Override
    public Object getItem(int position) {
        return mListaAgendaMedica.get(position);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.modelo_lista_marcar_consulta, null);

            AgendaMedica agendaMedica = mListaAgendaMedica.get(position);

            Medico medico = agendaMedica.getMedico();

            ((TextView) convertView.findViewById(R.id.txtMedico)).setText(medico.getNome());
            ((TextView) convertView.findViewById(R.id.txtEspecialidade)).setText(medico.getEspecialidade().getNome());
            ((TextView) convertView.findViewById(R.id.txtLocalAtendimento)).setText(agendaMedica.getLocalAtendimento().getNome());
            ((TextView) convertView.findViewById(R.id.txtDataHora)).
                    setText(Util.convertDateToStr(agendaMedica.getDataAgenda()));
//            ((TextView) convertView.findViewById(R.id.txtDataHora)).
//                    setText(Util.convertDateToStr(agendaMedica.getData()) + "  " + agendaMedica.getHora());
            ((CheckBox) convertView.findViewById(R.id.chkBoxListaConsulta)).setChecked(false);
        }

        return convertView;
    }
}
