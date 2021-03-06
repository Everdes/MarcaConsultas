package br.com.prova.model.bean;

import java.util.Date;

import br.com.prova.Enumerators.Situacao;

/**
 * Created by jorge on 27/09/2015.
 */

public class AgendaMedica {

    private long id;
    private Date dataAgenda;
    private Medico medico;
    private LocalAtendimento localAtendimento;
    private Situacao situacao;

    @Override
    public String toString() {
        return "AgendaMedica [id=" + id + ", dataAgenda=" + dataAgenda
                + ", medico=" + medico + ", localAtendimento="
                + localAtendimento + ", situacao=" + situacao + "]";
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getDataAgenda() {
        return dataAgenda;
    }
    public void setDataAgenda(Date dataAgenda) {
        this.dataAgenda = dataAgenda;
    }
    public Medico getMedico() {
        return medico;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    public LocalAtendimento getLocalAtendimento() {
        return localAtendimento;
    }
    public void setLocalAtendimento(LocalAtendimento localAtendimento) {
        this.localAtendimento = localAtendimento;
    }
    public Situacao getSituacao() {
        return situacao;
    }
    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

}


//public class AgendaMedica {
//    private int id;
//    private Medico medico;
//    private Date data;
//    private String hora;
//    private LocalAtendimento localAtendimento;
//    private Situacao situacao;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getHora() {
//        return hora;
//    }
//
//    public void setHora(String hora) {
//        this.hora = hora;
//    }
//
//    public Date getData() {
//        return data;
//    }
//
//    public void setData(Date data) {
//        this.data = data;
//    }
//
//    public Medico getMedico() {
//        return medico;
//    }
//
//    public void setMedico(Medico medico) {
//        this.medico = medico;
//    }
//
//    public LocalAtendimento getLocalAtendimento() {
//        return localAtendimento;
//    }
//
//    public void setLocalAtendimento(LocalAtendimento localAtendimento) {
//        this.localAtendimento = localAtendimento;
//    }
//
//    public Situacao getSituacao() {
//        return situacao;
//    }
//
//    public void setSituacao(Situacao situacao) {
//        this.situacao = situacao;
//    }
//}