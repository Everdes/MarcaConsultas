package br.com.prova.model.bean;

import java.io.Serializable;

/**
 * Created by Jorge on 27/09/2015.
 */
public class Usuario implements Serializable {
    private int id;
    private String login;
    private String senha;
    private String perfil;
    private String email;

    @Override
    public String toString() {
        return login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
