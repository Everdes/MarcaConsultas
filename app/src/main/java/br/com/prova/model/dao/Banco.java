package br.com.prova.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jorge on 29/09/2015.
 *
 * Classe responsável por criar a base de dados e as tabelas que serão utilizadas pela aplicação
 */
public class Banco extends SQLiteOpenHelper {

    /**
     * Constantes criadas para padronizar a nomenclatura, e reduzir possíveis erros de digitação,
     * ao criar metodos de consulta, inserção, alteração e deleção, nas classes DAO
     */
    private static final int VERSAO = 12;
    private static final String DATABASE = "marca_consulta";

    //Criação da tabela Médico
    public static final String TB_MEDICO = "tb_medico";
    public static final String ID_MEDICO = "id";
    public static final String NOME_MEDICO = "nome";
    public static final String CRM_MEDICO = "crm";
    public static final String ESPECIALIDADE_MEDICO = "especialidade";

    //Criação da tabela Usuario
    public static final String TB_USUARIO = "tb_usuario";
    public static final String ID_USUARIO = "id";
    public static final String LOGIN_USUARIO = "login";
    public static final String SENHA_USUARIO = "senha";
    public static final String PERFIL_USUARIO = "perfil";
    public static final String EMAIL_USUARIO = "email";

    //Criação da tabela Localatendimento
    public static final String TB_LOCAL_ATENDIMENTO = "tb_local_atendimento";
    public static final String ID_LOCAL_ATENDIMENTO = "id";
    public static final String NOME_LOCAL_ATENDIMENTO = "nome";
    public static final String ENDERECO_LOCAL_ATENDIMENTO = "endereco";

    //Criação da tabela Especialidades
    public static final String TB_ESPECIALIDADE = "tb_especialidade";
    public static final String ID_ESPECIALIDADE = "id";
    public static final String NOME_ESPECIALIDADE = "nome";

    //Criação da tabela Consulta Marcada
    public static final String TB_CONSULTA_MARCADA = "tb_consulta_marcada";
    public static final String ID_CONSULTA_MARCADA = "id";
    public static final String ID_AGENDA_MEDICO_CONSULTA_MARCADA = "id_agenda_medico";
    public static final String USUARIO_CONSULTA_MARCADA = "usuario";
    public static final String DATA_MARCACAO_CONSULTA_MARCADA = "data_marcacao_consulta";
    public static final String SITUACAO_CONSULTA_MARCADA = "situacao";
    public static final String DATA_CANCELAMENTO_CONSULTA_MARCADA = "data_cancelamento";

    //Criação da tabela Agenda Médica
    public static final String TB_AGENDA_MEDICO = "tb_agenda_medico";
    public static final String ID_AGENDA_MEDICO = "id";
    public static final String MEDICO_AGENDA_MEDICO = "medico";
    public static final String DATA_AGENDA_MEDICO = "data";
    public static final String HORA_AGENDA_MEDICO = "hora";
    public static final String LOCAL_ATENDIMENTO_AGENDA_MEDICO = "local_atendimento";
    public static final String SITUACAO_AGENDA_MEDICO = "situacao";

    public Banco(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    /**
     *
     * @param db
     *
     * Método responsável por criar as tabelas no banco de dados
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = " CREATE TABLE " + TB_MEDICO + " ( " +
                ID_MEDICO + " INTEGER PRIMARY KEY, " +
                NOME_MEDICO + " TEXT, " +
                CRM_MEDICO + " INTEGER, " +
                ESPECIALIDADE_MEDICO + " INTEGER) ";

        db.execSQL(ddl);

        ddl = "CREATE TABLE " + TB_USUARIO + "(" +
                ID_USUARIO + " INTEGER PRIMARY KEY, " +
                LOGIN_USUARIO + " TEXT, " +
                SENHA_USUARIO + " TEXT, " +
                PERFIL_USUARIO + " TEXT, " +
                EMAIL_USUARIO + " TEXT) ";

        db.execSQL(ddl);

        ddl = " CREATE TABLE " + TB_LOCAL_ATENDIMENTO + " ( " +
                ID_LOCAL_ATENDIMENTO + " INTEGER PRIMARY KEY, " +
                NOME_LOCAL_ATENDIMENTO + " TEXT, " +
                ENDERECO_LOCAL_ATENDIMENTO + " TEXT) ";

        db.execSQL(ddl);

        ddl = " CREATE TABLE " + TB_ESPECIALIDADE + " ( " +
                ID_ESPECIALIDADE + " INTEGER PRIMARY KEY, " +
                NOME_ESPECIALIDADE + " TEXT) ";

        db.execSQL(ddl);

        ddl = " CREATE TABLE " + TB_CONSULTA_MARCADA + " ( " +
                ID_CONSULTA_MARCADA + " INTEGER PRIMARY KEY, " +
                ID_AGENDA_MEDICO_CONSULTA_MARCADA + " INTEGER, " +
                USUARIO_CONSULTA_MARCADA + " INTEGER, " +
                DATA_MARCACAO_CONSULTA_MARCADA + " TEXT, " +
                SITUACAO_CONSULTA_MARCADA + " TEXT, " +
                DATA_CANCELAMENTO_CONSULTA_MARCADA + " TEXT) ";

        db.execSQL(ddl);

        ddl = "CREATE TABLE " + TB_AGENDA_MEDICO + " ( " +
                ID_AGENDA_MEDICO + " INTEGER PRIMARY KEY, " +
                MEDICO_AGENDA_MEDICO + " INTEGER, " +
                DATA_AGENDA_MEDICO + " DATE, " +
                HORA_AGENDA_MEDICO + " TIME, " +
                LOCAL_ATENDIMENTO_AGENDA_MEDICO + " INTEGER, " +
                SITUACAO_AGENDA_MEDICO + " TEXT) "
        ;

        db.execSQL(ddl);

        alimentarTabelas(db);
    }

    /**
     *
     * @param db
     *
     * Método usado para alimentar as tabelas do Banco
     */
    private void alimentarTabelas(SQLiteDatabase db) {
        String ddl = null;

        ddl = "INSERT INTO " + TB_MEDICO + " VALUES(1,'Jose Carlos', 12345, 1);";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_MEDICO + " VALUES(2,'Maria Agripina', 54321, 2);";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_MEDICO + " VALUES(3,'Gustavo Mendes', 12345, 3);";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_MEDICO + " VALUES(4,'Antonio Francisco', 12345, 1);";
        db.execSQL(ddl);

        ddl = "INSERT INTO " + TB_USUARIO + " VALUES(1,'admin','admin', 'A', 'e_verde_s@hotmail.com');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_USUARIO + " VALUES(2,'user','user', 'U', 'verdes.ps87@gmail.com');";
        db.execSQL(ddl);

        ddl = "INSERT INTO " + TB_LOCAL_ATENDIMENTO + " VALUES(1, 'Hospital São Carlos', 'Avenida Pontes Vieira 2531 Fortaleza');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_LOCAL_ATENDIMENTO + " VALUES(2, 'Hospital de Messejana', 'Avenida Frei Cirilo 3480 Fortaleza');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_LOCAL_ATENDIMENTO + " VALUES(3, 'Hospital São Mateus', 'Avenida Santos Dumont 5633 Fortaleza');";
        db.execSQL(ddl);

        ddl = "INSERT INTO " + TB_ESPECIALIDADE + " VALUES(1, 'Cirurgião');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_ESPECIALIDADE + " VALUES(2, 'Pediatra');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_ESPECIALIDADE + " VALUES(3, 'Clínico Geral');";
        db.execSQL(ddl);

        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(1, 1, '2015-10-15', '08:00:00', 1, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(2, 2, '2015-10-05', '13:00:00', 1, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(3, 3, '2015-10-05', '08:00:00', 1, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(4, 1, '2015-10-10', '13:00:00', 2, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(5, 2, '2015-10-05', '08:00:00', 2, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(6, 3, '2015-10-05', '13:00:00', 2, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(7, 1, '2015-10-07', '10:00:00', 3, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(8, 2, '2015-10-06', '10:00:00', 3, 'D');";
        db.execSQL(ddl);
        ddl = "INSERT INTO " + TB_AGENDA_MEDICO + " VALUES(9, 3, '2015-10-05', '10:00:00', 3, 'D');";
        db.execSQL(ddl);
    }

    /**
     * @param db
     * @param oldVersion
     * @param newVersion
     *
     * Método executado sempre que a versão do banco de dados é atualizada,
     * que além de dropar todas as tabelas do banco, invoca o método onCreate,
     * para que elas sejam recriadas
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String ddl = "DROP TABLE IF EXISTS " + TB_MEDICO;
        db.execSQL(ddl);

        ddl = "DROP TABLE IF EXISTS " + TB_USUARIO;
        db.execSQL(ddl);

        ddl = "DROP TABLE IF EXISTS " + TB_LOCAL_ATENDIMENTO;
        db.execSQL(ddl);

        ddl = "DROP TABLE IF EXISTS " + TB_ESPECIALIDADE;
        db.execSQL(ddl);

        ddl = "DROP TABLE IF EXISTS " + TB_CONSULTA_MARCADA;
        db.execSQL(ddl);

        ddl = "DROP TABLE IF EXISTS " + TB_AGENDA_MEDICO;
        db.execSQL(ddl);

        onCreate(db);
    }
}
