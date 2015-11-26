package br.com.prova.model.bean;

import java.io.Serializable;
import java.util.Date;

import br.com.prova.Enumerators.Situacao;

/**
 * Created by Jorge on 27/09/2015.
 */
public class ConsultaMarcada implements Serializable {

    private int id;
    private int idAgendaMedico;
    private AgendaMedico agendaMedico;
    private Date dataMarcacaoConsulta;
    private Situacao situacao;
    private Date dataCancelamento;
    private Usuario usuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAgendaMedico() {
        return idAgendaMedico;
    }

    public void setIdAgendaMedico(int idAgendaMedico) {
        this.idAgendaMedico = idAgendaMedico;
    }

    public AgendaMedico getAgendaMedico() {
        return agendaMedico;
    }

    public void setAgendaMedico(AgendaMedico agendaMedico) {
        this.agendaMedico = agendaMedico;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataMarcacaoConsulta() {
        return dataMarcacaoConsulta;
    }

    public void setDataMarcacaoConsulta(Date dataMarcacaoConsulta) {
        this.dataMarcacaoConsulta = dataMarcacaoConsulta;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public Date getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(Date dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }
}
