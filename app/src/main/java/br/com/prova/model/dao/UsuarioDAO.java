package br.com.prova.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.prova.model.bean.Usuario;

/**
 * Created by Éverdes on 29/09/2015.
 *
 * Classe responsável por executar o CRUD com a tabela de Usuario
 */
public class UsuarioDAO {

    private Banco mBanco;
    private SQLiteDatabase db;

    public UsuarioDAO(Context context) {
        if (mBanco == null)
            mBanco = new Banco(context);
    }

    /**
     *
     * @param login
     * @return Usuario
     *
     * Método chamado na ActivityLogin, que recebe por parâmetro o login digitado pelo usuário
     * e retorna um BEAN de Usuario, caso seja retornado um registro do Banco de Dados
     */
    public Usuario selecionarPorLogin(String login) {
        Usuario usuario = null;
        Cursor cursor;
        String sql = "select * from " + mBanco.TB_USUARIO +
                " where " + mBanco.LOGIN_USUARIO + "='" + login + "'";

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext())
            usuario = getUsuario(cursor);

        db.close();
        cursor.close();

        return usuario;
    }

    /**
     *
     * @param id
     * @return Usuario
     *
     * Método que seleciona um Usuario, através de um Id passado por parâmetro
     */
    public Usuario selecionarPorId(int id) {
        Usuario usuario = null;
        Cursor cursor;
        String condicao = mBanco.ID_USUARIO + "=" + id + "";

        db = mBanco.getReadableDatabase();

        cursor = db.rawQuery("select * from " + mBanco.TB_USUARIO + " where " + condicao, null);

        while (cursor.moveToNext())
            usuario = getUsuario(cursor);

        db.close();
        cursor.close();

        return usuario;
    }

    /**
     *
     * @param cursor
     * @return Usuario
     *
     * Método que recebe por parâmetro um cursor, na linha onde o ponteiro está posicionado, e retorna
     * um Bean de Usuario.
     * Utilizado em todos os métodos de seleção e listagem desta classe
     */
    private Usuario getUsuario(Cursor cursor) {
        Usuario usuario = new Usuario();

        usuario.setId(cursor.getInt(cursor.getColumnIndexOrThrow(mBanco.ID_USUARIO)));
        usuario.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.LOGIN_USUARIO)));
        usuario.setSenha(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.SENHA_USUARIO)));
        usuario.setPerfil(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.PERFIL_USUARIO)));
        usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(mBanco.EMAIL_USUARIO)));

        return usuario;
    }
}
