package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.Especialidade;

/**
 * Created by Éverdes on 30/09/2015.
 *
 * Classe responsável por executar o CRUD com a tabela de Especialidade
 */
public class EspecialidadeDAO {

    private Banco mBanco;
    private SQLiteDatabase db;

    public EspecialidadeDAO(Context context) {
        if (mBanco == null)
        mBanco = new Banco(context);
    }

    /**
     *
     * @param Id
     * @return Especialidade
     *
     * Método que seleciona uma Especialidade, através de um Id passado por parâmetro
     */
    public Especialidade selecionarPorId(int Id) {
        Especialidade especialidade = null;
        Cursor cursor;
        String sql = "select * from " + mBanco.TB_ESPECIALIDADE +
                " where " + mBanco.ID_ESPECIALIDADE + "=" + Id;

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            especialidade = getEspecialidade(cursor);
        }

        db.close();
        cursor.close();

        return especialidade;
    }

    /**
     *
     * @return List<Especialidade>
     *
     * Método que retorna a lista de todas as Especialidades
     */
    public List listar() {
        Cursor cursor;
        List<Especialidade> especialidades = new ArrayList<>();

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery("select * from " + mBanco.TB_ESPECIALIDADE, null);

        try {
            while (cursor.moveToNext())
                especialidades.add(getEspecialidade(cursor));
        } catch (Exception e) {
            Log.e("LocalAtendimento.listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return especialidades;
    }

    /**
     *
     * @param cursor
     * @return Especialidade
     *
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de Especialidade.
     * A principal utilidade do método é o reuso do mesmo, em todos os métodos de seleção e listagem desta classe
     */
    private Especialidade getEspecialidade(Cursor cursor) {
        Especialidade especialidade = new Especialidade();

        especialidade.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_ESPECIALIDADE)));
        especialidade.setNome(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.NOME_ESPECIALIDADE)));

        return especialidade;
    }

}
