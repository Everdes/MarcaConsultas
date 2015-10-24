package br.com.prova.Enumerators;

/**
 * Created by Jorge on 27/09/2015.
 *
 * Enumerator para identificar a situação da consulta marcada
 */
public enum Situacao {
    DISPONIVEL("D"), MARCADA("M"), CANCELADA("C");

    private final String nome;

    Situacao(String nome) {
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }
}
