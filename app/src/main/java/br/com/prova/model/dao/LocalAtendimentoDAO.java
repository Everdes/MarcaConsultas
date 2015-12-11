package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.LocalAtendimento;

/**
 * Created by Éverdes on 30/09/2015.
 *
 * Classe responsável por executar o CRUD com a tabela de LocalAtendimento
 */
public class LocalAtendimentoDAO {

    private Banco mBanco;
    private SQLiteDatabase db;

    public LocalAtendimentoDAO(Context context) {
        if (mBanco == null)
        mBanco = new Banco(context);
    }

    /**
     *
     * @param id
     * @return LocalAtendimento
     *
     * Método que seleciona um LocalAtendimento, através de um Id passado por parâmetro
     */
    public LocalAtendimento selecionarPorId(int id) {
        LocalAtendimento localAtendimento = null;
        Cursor cursor;
        String sql = "select * from " + mBanco.TB_LOCAL_ATENDIMENTO +
                " where " + mBanco.ID_LOCAL_ATENDIMENTO + "=" + id;

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            localAtendimento = getLocalAtendimento(cursor);
        }

        db.close();

        return localAtendimento;
    }

    /**
     *
     * @return List<LocalAtendimento>
     *
     * Método que retorna a lista de todas os Locais de Atendimento
     */
    public List<LocalAtendimento> listar() {
        Cursor cursor;
        List<LocalAtendimento> locaisAtendimento = new ArrayList<>();

        db = mBanco.getReadableDatabase();
        cursor = db.rawQuery("select * from " + mBanco.TB_LOCAL_ATENDIMENTO, null);

        try {
            while (cursor.moveToNext()) {
                locaisAtendimento.add(getLocalAtendimento(cursor));
            }
        } catch (Exception e) {
            Log.e("listar()", e.getMessage());
        } finally {
            cursor.close();
            db.close();
        }

        return locaisAtendimento;
    }

    /**
     *
     * @param cursor
     * @return LocalAtendimento
     *
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de LocalAtendimento.
     * Utilizado em todos os métodos de seleção e listagem desta classe
     */
    private LocalAtendimento getLocalAtendimento(Cursor cursor) {
        LocalAtendimento localAtendimento = new LocalAtendimento();

        localAtendimento.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_LOCAL_ATENDIMENTO)));
        localAtendimento.setNome(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.NOME_LOCAL_ATENDIMENTO)));
        localAtendimento.setEndereco(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.ENDERECO_LOCAL_ATENDIMENTO)));

        return localAtendimento;
    }
}
