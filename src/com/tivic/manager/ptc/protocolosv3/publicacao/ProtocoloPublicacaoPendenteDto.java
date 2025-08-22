package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.GregorianCalendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ProtocoloPublicacaoPendenteDto {
	
	private int cdAit;
	private int cdDocumento;
	private String idAit;
	private String nrPlaca;
	private String nrProtocolo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtJulgamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;
	private String nmTipoDocumento;
	private String nmJulgamento;
	private String idTipoDocumento;
	private int cdSituacaoDocumento;
	private String idAta;
	
	public int getCdAit() {
		return cdAit;
	}
	
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	
	public int getCdDocumento() {
		return cdDocumento;
	}
	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}
	
	public String getIdAit() {
		return idAit;
	}
	
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	
	public String getNrPlaca() {
		return nrPlaca;
	}
	
	public void setNrPlaca(String nrPlca) {
		this.nrPlaca = nrPlca;
	}
	
	public String getNrProtocolo() {
		return nrProtocolo;
	}
	
	public void setNrProtocolo(String nrProtocolo) {
		this.nrProtocolo = nrProtocolo;
	}
	
	public GregorianCalendar getDtProtocolo() {
		return dtProtocolo;
	}
	
	public void setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}
	
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}
	
	public String getNmTipoDocumento() {
		return nmTipoDocumento;
	}
	
	public void setNmTipoDocumento(String nmTipoDocumento) {
		this.nmTipoDocumento = nmTipoDocumento;
	}
	
	public String getNmJulgamento() {
		return nmJulgamento;
	}
	
	public void setNmJulgamento(String nmJulgamento) {
		this.nmJulgamento = nmJulgamento;
	}
	
	public String getIdTipoDocumento() {
		return idTipoDocumento;
	}
	
	public void setIdTipoDocumento(String idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	
	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}

	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}	

	public GregorianCalendar getDtJulgamento() {
		return dtJulgamento;
	}

	public void setDtJulgamento(GregorianCalendar dtJulgamento) {
		this.dtJulgamento = dtJulgamento;
	}

	public String getIdAta() {
		return idAta;
	}

	public void setIdAta(String idAta) {
		this.idAta = idAta;
	}

	public GregorianCalendar getDtOcorrencia() {
		return dtOcorrencia;
	}

	public void setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
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
