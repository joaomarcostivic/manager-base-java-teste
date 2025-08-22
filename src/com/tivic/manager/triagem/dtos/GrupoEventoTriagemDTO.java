package com.tivic.manager.triagem.dtos;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.seg.Usuario;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class GrupoEventoTriagemDTO {

	private Long cdGrupoEvento;
    private OrgaoTriagemDTO orgao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
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

	public OrgaoTriagemDTO getOrgao() {
		return orgao;
	}

	public void setOrgao(OrgaoTriagemDTO orgao) {
		this.orgao = orgao;
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
