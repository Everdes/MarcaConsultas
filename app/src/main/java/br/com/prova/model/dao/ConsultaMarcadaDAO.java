package br.com.prova.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.model.bean.ConsultaMarcada;
import br.com.prova.model.bean.Usuario;

/**
 * Created by Éverdes on 30/09/2015.
 *
 * Classe responsável por executar o CRUD com a tabela de Consulta Marcada
 */
public class ConsultaMarcadaDAO {

    private Banco mBanco;
    private SQLiteDatabase db;
    private UsuarioDAO mUsuarioDAO;
    private AgendaMedicoDAO mAgendaMedicoDAO;

    public ConsultaMarcadaDAO(Context context) {
        if (mBanco == null)
            mBanco = new Banco(context);
        mUsuarioDAO = new UsuarioDAO(context);
        mAgendaMedicoDAO = new AgendaMedicoDAO(context);
    }

    /**
     *
     * @return List<ConsultaMarcada>
     *
     * Método que retorna uma lista de ConsultaMarcada onde o valor do campo Situação seja igual a 'M'
     */
    public List listarMarcadas() {
        Cursor cursor;
        List<ConsultaMarcada> consultasMarcadas = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_CONSULTA_MARCADA +
                " where " + mBanco.SITUACAO_CONSULTA_MARCADA + " = '" + Situacao.MARCADA.getNome() + "'";

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                consultasMarcadas.add(getConsultaMarcada(cursor));
        } catch (Exception e) {
            Log.e("LocalAtendimento.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return consultasMarcadas;
    }

    /**
     *
     * @param usuario
     * @return List<ConsultaMarcada>
     *
     * Método que lista todas as Consultas Marcadas pelo Usuário passado por parâmetro
     */
    public List<ConsultaMarcada> listarMarcadasPorUsuario(Usuario usuario) {
        Cursor cursor;
        List<ConsultaMarcada> consultasMarcadas = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_CONSULTA_MARCADA + " where " +
                mBanco.USUARIO_CONSULTA_MARCADA + "=" + usuario.getId() +
                " and " + mBanco.SITUACAO_CONSULTA_MARCADA + " = '" + Situacao.MARCADA.getNome() + "'";

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                consultasMarcadas.add(getConsultaMarcada(cursor));
        } catch (Exception e) {
            Log.e("ConsultaMarcada.listarPorUsuario()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return consultasMarcadas;
    }

    /**
     *
     * @param consultaMarcada
     * @param dataCancelamento
     * @return boolean
     *
     * Método que preenche a data do cancelamento
     * e altera o valor da Situação, da Consulta e da Agenda do Médico, para Cancelada,
     * de acordo com os dados passados por parâmetro e retorna True caso a alteração seja
     * bem-sucedida e False caso não seja.
     */
    public boolean cancelar(ConsultaMarcada consultaMarcada, String dataCancelamento) {
        String sql = "update " + mBanco.TB_CONSULTA_MARCADA +
                " set " + mBanco.SITUACAO_CONSULTA_MARCADA + " = '" + Situacao.CANCELADA.getNome() +
                "', " + mBanco.DATA_CANCELAMENTO_CONSULTA_MARCADA + " = '" + dataCancelamento +
                "' where " + mBanco.ID_CONSULTA_MARCADA + " = " + consultaMarcada.getId();

        db = mBanco.getWritableDatabase();

        try {
            db.execSQL(sql);
            mAgendaMedicoDAO.alterar(consultaMarcada.getIdAgendaMedico(), Situacao.CANCELADA);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    /**
     *
     * @param consultaMarcada
     * @return boolean
     *
     * Método responsável por inserir na tabela de Consulta Marcada, um BEAN passado por parâmetro
     * e retorna True caso a inserção seja bem sucedida, e False caso não seja.
     */
    public boolean inserir(ConsultaMarcada consultaMarcada) {
        ContentValues valores = new ContentValues();

        valores.put(mBanco.ID_AGENDA_MEDICO_CONSULTA_MARCADA, consultaMarcada.getIdAgendaMedico());
        valores.put(mBanco.USUARIO_CONSULTA_MARCADA, consultaMarcada.getUsuario().getId());
        valores.put(mBanco.DATA_MARCACAO_CONSULTA_MARCADA, new SimpleDateFormat("yyyy-MM-dd").format(consultaMarcada.getDataMarcacaoConsulta()));
        valores.put(mBanco.SITUACAO_CONSULTA_MARCADA, consultaMarcada.getSituacao().getNome());

        db = mBanco.getWritableDatabase();

        try {
            db.insert(mBanco.TB_CONSULTA_MARCADA, null, valores);
            mAgendaMedicoDAO.alterar(consultaMarcada.getIdAgendaMedico(), Situacao.MARCADA);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    /**
     *
     * @param cursor
     * @return ConsultaMarcada
     *
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de ConsultaMarcada.
     * Utilizado em todos os métodos de seleção e listagem desta classe.
     */
    private ConsultaMarcada getConsultaMarcada(Cursor cursor) {
        ConsultaMarcada consultaMarcada = new ConsultaMarcada();

        consultaMarcada.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_CONSULTA_MARCADA)));
        consultaMarcada.setUsuario(mUsuarioDAO.selecionarPorId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.USUARIO_CONSULTA_MARCADA))));
        consultaMarcada.setIdAgendaMedico(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_AGENDA_MEDICO_CONSULTA_MARCADA)));
        consultaMarcada.setDataMarcacaoConsulta(Date.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.DATA_MARCACAO_CONSULTA_MARCADA))));

        switch (cursor.getString(cursor.getColumnIndexOrThrow(mBanco.SITUACAO_CONSULTA_MARCADA))) {
            case "D":
                consultaMarcada.setSituacao(Situacao.DISPONIVEL);
                break;
            case "M":
                consultaMarcada.setSituacao(Situacao.MARCADA);
                break;
            case "C":
                consultaMarcada.setSituacao(Situacao.CANCELADA);
                break;
        }

        return consultaMarcada;
    }
}
