package com.tivic.manager.mob.lote.impressao.publicacao.dto;

import java.util.GregorianCalendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LotePublicacaoNotificacaoDto {
	
	private int tpDocumento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPublicacao;
	private String nmUsuario;
	private int cdLoteImpressao;
	
	public int getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
	public GregorianCalendar getDtCriacao() {
		return dtCriacao;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	public GregorianCalendar getDtPublicacao() {
		return dtPublicacao;
	}
	public void setDtPublicacao(GregorianCalendar dtPublicacao) {
		this.dtPublicacao = dtPublicacao;
	}
	public String getNmUsuario() {
		return nmUsuario;
	}
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}
	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}
	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
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
