package com.tivic.manager.triagem.dtos;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.triagem.serializer.CalendarDeserializerWithoutTimezone;
import com.tivic.manager.triagem.serializer.CalendarSerializerWithoutTimezone;

public class NotificacaoEstacionamentoDigitalDTO {

	private String nmOrgaoAutuador;
    private String nrNotificacao;
    @JsonSerialize(using = CalendarSerializerWithoutTimezone.class)
    @JsonDeserialize(using = CalendarDeserializerWithoutTimezone.class)
    private GregorianCalendar dtNotificacao;
    private String dsNotificacao;
    private String nrPlaca;
    private String idDispositivo;
    private String nrVaga;
    private String nmRuaNotificacao;
    private String nrImovelReferencia;
    private String idColaborador;
    private List<ImagemNotificacaoDTO> imagemNotificacaoDTOList;
    
	public String getNmOrgaoAutuador() {
		return nmOrgaoAutuador;
	}

	public void setNmOrgaoAutuador(String nmOrgaoAutuador) {
		this.nmOrgaoAutuador = nmOrgaoAutuador;
	}

	public String getNrNotificacao() {
		return nrNotificacao;
	}
	
	public void setNrNotificacao(String nrNotificacao) {
		this.nrNotificacao = nrNotificacao;
	}
	
	public GregorianCalendar getDtNotificacao() {
		return dtNotificacao;
	}
	
	public void setDtNotificacao(GregorianCalendar dtNotificacao) {
		this.dtNotificacao = dtNotificacao;
	}
	
	public String getDsNotificacao() {
		return dsNotificacao;
	}
	
	public void setDsNotificacao(String dsNotificacao) {
		this.dsNotificacao = dsNotificacao;
	}
	
	public String getNrPlaca() {
		return nrPlaca;
	}
	
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	
	public String getIdDispositivo() {
		return idDispositivo;
	}
	
	public void setIdDispositivo(String idDispositivo) {
		this.idDispositivo = idDispositivo;
	}
	
	public String getNrVaga() {
		return nrVaga;
	}
	
	public void setNrVaga(String nrVaga) {
		this.nrVaga = nrVaga;
	}
	
	public String getNmRuaNotificacao() {
		return nmRuaNotificacao;
	}
	
	public void setNmRuaNotificacao(String nmRuaNotificacao) {
		this.nmRuaNotificacao = nmRuaNotificacao;
	}
	
	public String getNrImovelReferencia() {
		return nrImovelReferencia;
	}
	
	public void setNrImovelReferencia(String nrImovelReferencia) {
		this.nrImovelReferencia = nrImovelReferencia;
	}
	
	public String getIdColaborador() {
		return idColaborador;
	}
	
	public void setIdColaborador(String idColaborador) {
		this.idColaborador = idColaborador;
	}
	
	public List<ImagemNotificacaoDTO> getImagemNotificacaoDTOList() {
		return imagemNotificacaoDTOList;
	}
	
	public void setImagemNotificacaoDTOList(List<ImagemNotificacaoDTO> imagemNotificacaoDTOList) {
		this.imagemNotificacaoDTOList = imagemNotificacaoDTOList;
	}
	
}
