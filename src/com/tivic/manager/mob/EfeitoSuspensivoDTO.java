package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class EfeitoSuspensivoDTO {
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInicial;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtFinal;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtJulgamento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtResultadoJari;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtResultadoCetran;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtDias;
	private int tpStatus;
	private int cdAit;
	private int cdUsuario;
	private String nmSituacaoDocumento;
	private String idAit;
	private String nrDocumento;
	private String nrProcesso;
	private String ultimoRetorno;
	
	public EfeitoSuspensivoDTO() {
		super();
	}
	
	public EfeitoSuspensivoDTO(GregorianCalendar dtDias, GregorianCalendar dtMovimento, GregorianCalendar dtInicial, GregorianCalendar dtFinal, 
			GregorianCalendar dtResultadoJari, GregorianCalendar dtResultadoCetran, int tpStatus, int cdAit, int cdUsuario, String nmSituacaoDocumento, 
			String idAit, String nrDocumento, String nrProcesso, String ultimoRetorno) {
		this.dtDias = dtDias;
		this.dtMovimento = dtMovimento;
		this.dtInicial = dtInicial;
		this.dtFinal = dtFinal;
		this.dtResultadoJari = dtResultadoJari;
		this.dtResultadoCetran = dtResultadoCetran;
		this.idAit = idAit;
		this.nrDocumento = nrDocumento;
		this.tpStatus = tpStatus;
		this.nmSituacaoDocumento = nmSituacaoDocumento;
		this.cdAit = cdAit;
		this.cdUsuario = cdUsuario;
		this.nrProcesso = nrProcesso;
		this.ultimoRetorno = ultimoRetorno;
	}

	public GregorianCalendar getDtDias() {
		return dtDias;
	}

	public void setDtDias(GregorianCalendar dtDias) {
		this.dtDias = dtDias;
	}

	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public GregorianCalendar getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(GregorianCalendar dtInicial) {
		this.dtInicial = dtInicial;
	}

	public GregorianCalendar getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(GregorianCalendar dtFinal) {
		this.dtFinal = dtFinal;
	}

	public GregorianCalendar getDtJulgamento() {
		return dtJulgamento;
	}

	public void setDtJulgamento(GregorianCalendar dtJulgamento) {
		this.dtJulgamento = dtJulgamento;
	}

	public GregorianCalendar getDtResultadoJari() {
		return dtResultadoJari;
	}

	public void setDtResultadoJari(GregorianCalendar dtResultadoJari) {
		this.dtResultadoJari = dtResultadoJari;
	}

	public GregorianCalendar getDtResultadoCetran() {
		return dtResultadoCetran;
	}

	public void setDtResultadoCetran(GregorianCalendar dtResultadoCetran) {
		this.dtResultadoCetran = dtResultadoCetran;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}

	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getNrProcesso() {
		return nrProcesso;
	}

	public void setNrProcesso(String nrProcesso) {
		this.nrProcesso = nrProcesso;
	}

	public String getUltimoRetorno() {
		return ultimoRetorno;
	}

	public void setUltimoRetorno(String ultimoRetorno) {
		this.ultimoRetorno = ultimoRetorno;
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
