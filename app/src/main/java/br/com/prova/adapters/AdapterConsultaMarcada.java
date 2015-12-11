package br.com.prova.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.prova.interfaces.events.OnItemClickListener;
import br.com.prova.interfaces.events.OnItemLongClickListener;
import br.com.prova.marcaconsultas.R;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.util.Util;

/**
 * Created by Wolfstein on 04/12/2015.
 */
public class AdapterConsultaMarcada extends RecyclerView.Adapter<AdapterConsultaMarcada.ViewHolderConsultaMarcada> {

    private List<ConsultaMarcada> mConsultas;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public AdapterConsultaMarcada(Context context, List<ConsultaMarcada> consultasMarcadas) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mConsultas = consultasMarcadas;
    }

    @Override
    public ViewHolderConsultaMarcada onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.modelo_lista_consulta_medica, parent, false);

        return new ViewHolderConsultaMarcada(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderConsultaMarcada holder, int position) {
        holder.txtMedico.setText(mConsultas.get(position).getAgendaMedica().getMedico().getNome());
        holder.txtNome.setText(mConsultas.get(position).getUsuario().toString());
        holder.txtLocal.setText(mConsultas.get(position).getAgendaMedica().getLocalAtendimento().getNome());
        holder.txtData.setText(Util.convertDateToStr(mConsultas.get(position).getAgendaMedica().getDataAgenda()));
    }

    @Override
    public int getItemCount() {
        return mConsultas.size();
    }

    public void remove(int position){
        mConsultas.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public class ViewHolderConsultaMarcada extends RecyclerView.ViewHolder {

        public View view;

        public TextView txtNome;
        public TextView txtMedico;
        public TextView txtLocal;
        public TextView txtData;

        public ViewHolderConsultaMarcada(View itemView) {
            super(itemView);

            view = itemView;

            txtMedico = (TextView) itemView.findViewById(R.id.txtMedico);
            txtNome = (TextView) itemView.findViewById(R.id.txtNome);
            txtLocal = (TextView) itemView.findViewById(R.id.txtLocal);
            txtData = (TextView) itemView.findViewById(R.id.txtDataHora);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mOnItemClickListener != null && position >= 0)
                        mOnItemClickListener.onItemClick(v, position);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (mOnItemLongClickListener != null && position >= 0)
                        mOnItemLongClickListener.onItemLongClick(v, position);

                    return true;
                }
            });
        }
    }
}