package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import br.com.prova.Enumerators.Situacao;
import br.com.prova.model.bean.AgendaMedica;
import br.com.prova.model.bean.LocalAtendimento;
import br.com.prova.model.bean.Medico;
import br.com.prova.util.Util;

/**
 * Created by Éverdes on 30/09/2015.
 *
 * Classe responsável por executar o CRUD com a tabela de Agenda do Medico
 */
public class AgendaMedicoDAO {

    private Banco mBanco;
    private SQLiteDatabase db;
    private MedicoDAO mMedicoDAO;
    private LocalAtendimentoDAO mLocalAtendimentoDAO;

    public AgendaMedicoDAO(Context context) {

        if (mBanco == null)
            mBanco = new Banco(context);
        mMedicoDAO = new MedicoDAO(context);
        mLocalAtendimentoDAO = new LocalAtendimentoDAO(context);
    }

    /**
     *
     * @param id
     * @return AgendaMedico
     *
     * Método que seleciona uma AgendaMedico, através de um Id passado por parâmetro
     */
    public AgendaMedica selecionarPorId(int id) {
        AgendaMedica agendaMedica = null;
        Cursor cursor;
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.ID_AGENDA_MEDICO + "=" + id;

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext())
            agendaMedica = getAgendaMedico(cursor);

        db.close();

        return agendaMedica;
    }

    /**
     *
     * @param situacao
     * @return List<AgendaMedico>
     *
     * Método que retorna uma lista de AgendaMedico, através do valor da Situação, passado por parâmetro
     */
    public List<AgendaMedica> listarPorSituacao(Situacao situacao) {
        Cursor cursor;
        List<AgendaMedica> agendasMedico = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + situacao.getNome() + "'";

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                agendasMedico.add(getAgendaMedico(cursor));
        } catch (Exception e) {
            Log.e("AgendaMedicoDAO.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return agendasMedico;
    }

    /**
     *
     * @param idAgendaMedico
     * @param situacao
     * @return boolean
     *
     * Método que altera o valor da Situação, da Agenda Medico, especificados por parâmetro
     * e retorna True caso seja bem-sucedida e False caso não seja.
     */
    public boolean alterar(int idAgendaMedico, Situacao situacao) {
        String sql = "update " + mBanco.TB_AGENDA_MEDICO +
                " set " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + situacao.getNome() +
                "' where " + mBanco.ID_AGENDA_MEDICO + " = " + idAgendaMedico;

        db = mBanco.getWritableDatabase();

        try {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    /**
     *
     * @param localAtendimento
     * @return List<AgendaMedico>
     *
     * Método que retorna uma lista de AgendaMedico, de um determiando Local de Atendimento
     */
    public List<AgendaMedica> listarPorLocalAtendimento(LocalAtendimento localAtendimento) {
        Cursor cursor;
        List<AgendaMedica> agendasMedico = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + Situacao.DISPONIVEL.getNome() +
                "' and " + mBanco.LOCAL_ATENDIMENTO_AGENDA_MEDICO + " = " + localAtendimento.getId();


        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                agendasMedico.add(getAgendaMedico(cursor));
        } catch (Exception e) {
            Log.e("AgendaMedicoDAO.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return agendasMedico;
    }

    /**
     *
     * @param medico
     * @return List<AgendaMedico>
     *
     * Método que retorna uma lista de AgendaMedico, de um determinado medico
     */
    public List<AgendaMedica> listarPorMedico(Medico medico) {
        Cursor cursor;
        List<AgendaMedica> agendasMedico = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + Situacao.DISPONIVEL.getNome() +
                "' and " + mBanco.MEDICO_AGENDA_MEDICO + " = " + medico.getId();

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                agendasMedico.add(getAgendaMedico(cursor));
        } catch (Exception e) {
            Log.e("AgendaMedicoDAO.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return agendasMedico;
    }

    /**
     *
     * @param data
     * @return List<AgendaMedico>
     *
     * Método que retorna uma lista de AgendaMedico, através de uma data passada por parâmetro
     */
    public List<AgendaMedica> listarPorData(java.util.Date data) {
        Cursor cursor;
        List<AgendaMedica> agendasMedico = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_AGENDA_MEDICO +
                " where " + mBanco.SITUACAO_AGENDA_MEDICO + " = '" + Situacao.DISPONIVEL.getNome() +
                "' and " + mBanco.DATA_AGENDA_MEDICO + " = '" + Util.convertDateToStrInvertido(data) + "'";

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                agendasMedico.add(getAgendaMedico(cursor));
        } catch (Exception e) {
            Log.e("AgendaMedicoDAO.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return agendasMedico;
    }

    /**
     *
     * @param cursor
     * @return AgendaMedico
     *
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de AgendaMedico.
     * Utilizado em todos os métodos de seleção e listagem desta classe
     */
    private AgendaMedica getAgendaMedico(Cursor cursor) {
        AgendaMedica agendaMedica = new AgendaMedica();

        agendaMedica.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_AGENDA_MEDICO)));
        agendaMedica.setMedico(mMedicoDAO.selecionarPorId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.MEDICO_AGENDA_MEDICO))));
//        agendaMedica.setData(Date.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.DATA_AGENDA_MEDICO))));
//        agendaMedica.setHora(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.HORA_AGENDA_MEDICO)));
        agendaMedica.setLocalAtendimento(mLocalAtendimentoDAO.selecionarPorId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.LOCAL_ATENDIMENTO_AGENDA_MEDICO))));

        switch (cursor.getString(cursor.getColumnIndexOrThrow(mBanco.SITUACAO_AGENDA_MEDICO))) {
            case "D":
                agendaMedica.setSituacao(Situacao.DISPONIVEL);
                break;
            case "M":
                agendaMedica.setSituacao(Situacao.MARCADA);
                break;
            case "C":
                agendaMedica.setSituacao(Situacao.CANCELADA);
                break;
        }

        return agendaMedica;
    }
}
