package com.tivic.manager.triagem.dtos;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.seg.Usuario;

public class GrupoEventoParamsDTO {

	private Long cdGrupoEvento;
	private int cdOrgao;
	private String nmOrgao;
	private String idOrgao;
	private int cdCidade;
	private String nmCidade;
    private GregorianCalendar dtGrupoEvento;
    private Usuario usuario;
    private Boolean stEmProcessamento;
    private Long qtEventos;

	public Long getCdGrupoEvento() {
		return cdGrupoEvento;
	}

	public void setCdGrupoEvento(Long cdGrupoEvento) {
		this.cdGrupoEvento = cdGrupoEvento;
	}

	public int getCdOrgao() {
		return cdOrgao;
	}

	public void setCdOrgao(int cdOrgao) {
		this.cdOrgao = cdOrgao;
	}

	public String getNmOrgao() {
		return nmOrgao;
	}

	public void setNmOrgao(String nmOrgao) {
		this.nmOrgao = nmOrgao;
	}

	public String getIdOrgao() {
		return idOrgao;
	}

	public void setIdOrgao(String idOrgao) {
		this.idOrgao = idOrgao;
	}

	public int getCdCidade() {
		return cdCidade;
	}

	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}

	public String getNmCidade() {
		return nmCidade;
	}

	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}

	public GregorianCalendar getDtGrupoEvento() {
		return dtGrupoEvento;
	}

	public void setDtGrupoEvento(GregorianCalendar dtGrupoEvento) {
		this.dtGrupoEvento = dtGrupoEvento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getStEmProcessamento() {
		return stEmProcessamento;
	}

	public void setStEmProcessamento(Boolean stEmProcessamento) {
		this.stEmProcessamento = stEmProcessamento;
	}

	public Long getQtEventos() {
		return qtEventos;
	}

	public void setQtEventos(Long qtEventos) {
		this.qtEventos = qtEventos;
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
