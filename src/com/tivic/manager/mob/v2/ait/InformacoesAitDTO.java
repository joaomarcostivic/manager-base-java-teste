package com.tivic.manager.mob.v2.ait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InformacoesAitDTO {

    private Integer cdEvento;
    private Integer cdUsuarioConfirmacao;
    private String nrPlaca;
    private String nmProprietario;
    private Integer cdCorVeiculo;
    private Integer cdMarcaVeiculo;
    private Integer cdMarcaAutuacao;
    private Integer cdTipoVeiculo;
    private Integer cdEspecieVeiculo;
    private Integer nrAnoFabricacao;
    private Integer nrAnoModelo;
    private Integer cdCidade;
    private Integer cdInfracao;
    private String dsObservacao;
    private String dsLocalInfracao;
    private String dsPontoReferencia;
    private Integer cdAgente;
    private Integer stAit;

    public InformacoesAitDTO() {
    }

    public Integer getCdEvento() {
        return cdEvento;
    }

    public void setCdEvento(Integer cdEvento) {
        this.cdEvento = cdEvento;
    }

    public Integer getCdUsuarioConfirmacao() {
        return cdUsuarioConfirmacao;
    }

    public void setCdUsuarioConfirmacao(Integer cdUsuarioConfirmacao) {
        this.cdUsuarioConfirmacao = cdUsuarioConfirmacao;
    }

    public String getNrPlaca() {
        return nrPlaca;
    }

    public void setNrPlaca(String nrPlaca) {
        this.nrPlaca = nrPlaca;
    }

    public String getNmProprietario() {
        return nmProprietario;
    }

    public void setNmProprietario(String nmProprietario) {
        this.nmProprietario = nmProprietario;
    }

    public Integer getCdCorVeiculo() {
        return cdCorVeiculo;
    }

    public void setCdCorVeiculo(Integer cdCorVeiculo) {
        this.cdCorVeiculo = cdCorVeiculo;
    }

    public Integer getCdMarcaVeiculo() {
        return cdMarcaVeiculo;
    }

    public void setCdMarcaVeiculo(Integer cdMarcaVeiculo) {
        this.cdMarcaVeiculo = cdMarcaVeiculo;
    }

    public Integer getCdMarcaAutuacao() {
        return cdMarcaAutuacao;
    }

    public void setCdMarcaAutuacao(Integer cdMarcaAutuacao) {
        this.cdMarcaAutuacao = cdMarcaAutuacao;
    }

    public Integer getCdTipoVeiculo() {
        return cdTipoVeiculo;
    }

    public void setCdTipoVeiculo(Integer cdTipoVeiculo) {
        this.cdTipoVeiculo = cdTipoVeiculo;
    }

    public Integer getCdEspecieVeiculo() {
        return cdEspecieVeiculo;
    }

    public void setCdEspecieVeiculo(Integer cdEspecieVeiculo) {
        this.cdEspecieVeiculo = cdEspecieVeiculo;
    }

    public Integer getNrAnoFabricacao() {
        return nrAnoFabricacao;
    }

    public void setNrAnoFabricacao(Integer nrAnoFabricacao) {
        this.nrAnoFabricacao = nrAnoFabricacao;
    }

    public Integer getNrAnoModelo() {
        return nrAnoModelo;
    }

    public void setNrAnoModelo(Integer nrAnoModelo) {
        this.nrAnoModelo = nrAnoModelo;
    }

    public Integer getCdCidade() {
        return cdCidade;
    }

    public void setCdCidade(Integer cdCidade) {
        this.cdCidade = cdCidade;
    }

    public Integer getCdInfracao() {
        return cdInfracao;
    }

    public void setCdInfracao(Integer cdInfracao) {
        this.cdInfracao = cdInfracao;
    }

    public String getDsObservacao() {
        return dsObservacao;
    }

    public void setDsObservacao(String dsObservacao) {
        this.dsObservacao = dsObservacao;
    }

    public String getDsLocalInfracao() {
        return dsLocalInfracao;
    }

    public void setDsLocalInfracao(String dsLocalInfracao) {
        this.dsLocalInfracao = dsLocalInfracao;
    }

    public String getDsPontoReferencia() {
        return dsPontoReferencia;
    }

    public void setDsPontoReferencia(String dsPontoReferencia) {
        this.dsPontoReferencia = dsPontoReferencia;
    }

    public Integer getCdAgente() {
        return cdAgente;
    }

    public void setCdAgente(Integer cdAgente) {
        this.cdAgente = cdAgente;
    }
    
    public void setStAit(Integer stAit) {
		this.stAit = stAit;
	}
    
    public Integer getStAit() {
		return stAit;
	}
    
    @Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
}

