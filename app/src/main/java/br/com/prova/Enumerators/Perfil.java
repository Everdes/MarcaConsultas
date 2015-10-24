package br.com.prova.Enumerators;

/**
 * Created by Jorge on 27/09/2015.
 *
 * Enumerator para identificar o perfil de usuário
 */
public enum Perfil {
    USER("U"), ADM("A");

    private final String nome;

    Perfil(String nome) {
        this.nome = nome;
    }
}
