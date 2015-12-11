package br.com.prova.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.prova.marcaconsultas.R;
import br.com.prova.model.bean.AgendaMedica;
import br.com.prova.util.Util;

/**
 * Created by Wolfstein on 04/12/2015.
 */
public class AdapterAgendaMedica extends RecyclerView.Adapter<AdapterAgendaMedica.ViewHolderAgendaMedica> {

    private List<AgendaMedica> mAgendas;
    private LayoutInflater mLayoutInflater;

    public AdapterAgendaMedica(Context context, List agendasMedica){
        mAgendas = agendasMedica;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolderAgendaMedica onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.modelo_lista_marcar_consulta, parent, false);
        ViewHolderAgendaMedica viewHolderAgendaMedica = new ViewHolderAgendaMedica(view);

        return viewHolderAgendaMedica;
    }

    @Override
    public void onBindViewHolder(ViewHolderAgendaMedica holder, int position) {
        holder.txtMedico.setText(mAgendas.get(position).getMedico().getNome());
        holder.txtEspecialidade.setText(mAgendas.get(position).getMedico().getEspecialidade().getNome());
        holder.txtLocal.setText(mAgendas.get(position).getLocalAtendimento().getNome());
        holder.txtData.setText(Util.convertDateToStr(mAgendas.get(position).getDataAgenda()));
    }

    @Override
    public int getItemCount() {
        return mAgendas.size();
    }

    public class ViewHolderAgendaMedica extends RecyclerView.ViewHolder{

        public TextView txtMedico;
        public TextView txtEspecialidade;
        public TextView txtLocal;
        public TextView txtData;

        public ViewHolderAgendaMedica(View itemView) {
            super(itemView);

            txtMedico = (TextView) itemView.findViewById(R.id.txtMedico);
            txtEspecialidade = (TextView) itemView.findViewById(R.id.txtEspecialidade);
            txtLocal = (TextView) itemView.findViewById(R.id.txtLocalAtendimento);
            txtData = (TextView) itemView.findViewById(R.id.txtDataHora);

        }
    }

}
