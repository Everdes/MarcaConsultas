package br.com.prova.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.prova.marcaconsultas.R;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Éverdes on 03/10/2015.
 * Classe com métodos uteis, que podem ser chamados em qualquer lugar da aplicação
 */
public class Util {

    /**
     *
     * @return String
     *
     * Método que retorna um String com a data atual, em formato 'ano-mes-dia'
     */
    public static String getToday() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    /**
     *
     * @param data
     * @return String
     *
     * Método que recebe um objeto do tipo Date, e retorna um String com a data atual, em formato 'dia/mes/ano'
     */
    public static String convertDateToStr(Date data){
        return new SimpleDateFormat("dd/MM/yyyy").format(data.getTime());
    }

    /**
     *
     * @param data
     * @return String
     *
     * Método que recebe um objeto do tipo Date, e retorna um String com a data atual, em formato 'ano-mes-dia'
     */
    public static String convertDateToStrInvertido(Date data){
        return new SimpleDateFormat("yyyy-MM-dd").format(data.getTime());
    }

    /**
     *
     * @param titulo
     * @param mensagem
     * @param contexto
     *
     * Método que exibe uma mensagem na tela, da aplicação, para o usuário
     */
    public static void showMessage(String titulo, String mensagem, Context contexto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setMessage(mensagem);
        builder.setIcon(R.drawable.ic_exclamacao_preto);
        builder.setNegativeButton("Ok", null);

        AlertDialog dialog = builder.create();
        dialog.setTitle(titulo);
        dialog.show();
    }

    /**
     *
     * @param activity
     * @param emailDestinatario
     * @param corpoEmail
     *
     * Método responsável por enviar um e-mail
     */
    public static void enviarEmail(AppCompatActivity activity, String[] emailDestinatario, String corpoEmail) {
        String[] emailRemetente = {"verdes.ps87@gmail.com"};

        Intent itEnviarEmail = new Intent(Intent.ACTION_SEND);

        itEnviarEmail.putExtra(Intent.EXTRA_EMAIL, emailRemetente);
        itEnviarEmail.putExtra(Intent.EXTRA_CC, emailDestinatario);
        itEnviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
        itEnviarEmail.putExtra(Intent.EXTRA_TEXT, corpoEmail);

        itEnviarEmail.setType("message/rfc822");
        activity.startActivity(Intent.createChooser(itEnviarEmail, emailDestinatario[0]));
    }
}
