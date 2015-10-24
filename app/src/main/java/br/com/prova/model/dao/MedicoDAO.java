package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.Especialidade;
import br.com.prova.model.bean.Medico;

/**
 * Created by Éverdes on 30/09/2015.
 * <p/>
 * Classe responsável por executar o CRUD com a tabela de Medico
 */
public class MedicoDAO {

    private Banco mBanco;
    private SQLiteDatabase db;
    private EspecialidadeDAO mEspecialidadeDAO;

    public MedicoDAO(Context context) {
        if (mBanco == null)
            mBanco = new Banco(context);
        mEspecialidadeDAO = new EspecialidadeDAO(context);
    }

    /**
     *
     * @param id
     * @return Medico
     *
     * Método que seleciona um Medico, através de um Id passado por parâmetro
     */
    public Medico selecionarPorId(int id) {
        Medico medico = null;
        Cursor cursor;
        String condicao = mBanco.ID_MEDICO + "=" + id;

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery("select * from " + mBanco.TB_MEDICO + " where " + condicao, null);

        while (cursor.moveToNext())
            medico = getMedico(cursor);

        db.close();

        return medico;
    }

    /**
     *
     * @return List<Medico>
     *
     * Método que retorna a lista de todos os Médicos
     */
    public List listar() {
        Cursor cursor;
        List<Medico> medicos = new ArrayList<>();

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery("select * from " + mBanco.TB_MEDICO, null);

        try {
            while (cursor.moveToNext())
                medicos.add(getMedico(cursor));
        } catch (Exception e) {
            Log.e("Medico.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return medicos;
    }

    /**
     *
     * @return List<LocalAtendimento>
     *
     * Método que retorna a lista dos Medicos que possuem determinada espcialidade informada no parâmetro
     */
    public List<Medico> listarPorEspecialidade(Especialidade especialidade) {
        Cursor cursor;
        List<Medico> medicos = new ArrayList<>();
        String sql = "select * from " + mBanco.TB_MEDICO +
                " where " + mBanco.ESPECIALIDADE_MEDICO + " = " + especialidade.getId();

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery(sql, null);

        try {
            while (cursor.moveToNext())
                medicos.add(getMedico(cursor));
        } catch (Exception e) {
            Log.e("Medico.listarPorEspecialidade()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return medicos;
    }

    /**
     *
     * @param cursor
     * @return Medico
     *
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de Medico.
     * Utilizado em todos os métodos de seleção e listagem desta classe
     */
    private Medico getMedico(Cursor cursor) {
        Medico medico = new Medico();

        medico.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_MEDICO)));
        medico.setNome(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.NOME_MEDICO)));
        medico.setCrm(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.CRM_MEDICO)));
        medico.setEspecialidade(mEspecialidadeDAO.selecionarPorId(
                cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ESPECIALIDADE_MEDICO))));

        return medico;
    }
}
