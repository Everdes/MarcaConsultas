package br.com.prova.marcaconsultas;

import android.content.Entity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLConnection;

/**
 * Created by Wolfstein on 03/02/2015.
 */
public class WebClient {

    private final String urlServer;

    public WebClient(String urlServer){
        this.urlServer = urlServer;
    }

    public String get() {
        try{
            HttpGet get = new HttpGet(urlServer);

            get.setHeader("Content-type", "application/json");
            get.setHeader("Accept", "application/json");

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            HttpEntity resposta = response.getEntity();

            String respostaEmJSON = EntityUtils.toString(resposta);

            return respostaEmJSON;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
