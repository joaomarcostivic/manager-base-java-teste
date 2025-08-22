package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.GregorianCalendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class LotePublicacaoDto {
	
	private int tpPublicacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPublicacao;
	private String nmUsuario;
	private int cdLotePublicacao;
	private int cdSituacaoDocumento;
	private String stJulgamento;
	private int cdArquivoEdital;

	public int getTpPublicacao() {
		return tpPublicacao;
	}

	public void setTpPublicacao(int tpPublicacao) {
		this.tpPublicacao = tpPublicacao;
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
	
	public int getCdLotePublicacao() {
		return cdLotePublicacao;
	}

	public void setCdLotePublicacao(int cdLotePublicacao) {
		this.cdLotePublicacao = cdLotePublicacao;
	}

	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}

	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}

	public String getStJulgamento() {
		return stJulgamento;
	}

	public void setStJulgamento(String stJulgamento) {
		this.stJulgamento = stJulgamento;
	}

	public int getCdArquivoEdital() {
		return cdArquivoEdital;
	}

	public void setCdArquivoEdital(int cdArquivoEdital) {
		this.cdArquivoEdital = cdArquivoEdital;
	}

	@Override
	 public String toString() {
	      try {
	          return new ObjectMapper().writeValueAsString(this);
	      } catch (JsonProcessingException e) {
	          return "Não foi possível serializar o objeto informado";
	      }
	}
	
}
