package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AtaDTO {
	private int cdAta;
	private String idAit;
	private String idAta;
	private String nrProtocolo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEntradaRecurso;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAta;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAtaInicial;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAtaFinal;
	private String nmSolicitante;
	private String nmPessoa;
	private int cdUsuario;
	private int tpStatus;
	private String dsObservacao;
	private int cdAit;
	private int cdMovimento;
	private int cdDocumento;
	private String nmSituacaoDocumento;
	private String dsOcorrencia;
	private String txtOcorrencia;
	private String dsAssunto;
	private int tpConsistencia;
	private int cdFase;
	
	public AtaDTO() {}
	
	public AtaDTO(int cdAta, String idAit, String idAta, String nrAta, String nrProtocolo, GregorianCalendar dtEntradaRecurso, GregorianCalendar dtAta, GregorianCalendar dtAtaInicial,
			GregorianCalendar dtAtaFinal, String nmSolicitante, String nmPessoa, int cdUsuario, int tpStatus, String dsObservacao, int cdAit, int cdMovimento, int cdDocumento, 
			String nmSituacaoDocumento, String dsOcorrencia, String txtOcorrencia, String dsAssunto, int tpConsistencia, int cdFase) {
		this.cdAta = cdAta;
		this.idAit = idAit;
		this.idAta = idAta;
		this.nrProtocolo = nrProtocolo;
		this.dtEntradaRecurso = dtEntradaRecurso; 
		this.dtAta = dtAta;
		this.dtAtaInicial = dtAtaInicial;
		this.dtAtaFinal = dtAtaFinal;
		this.nmSolicitante = nmSolicitante;
		this.nmPessoa = nmPessoa;
		this.cdUsuario = cdUsuario;
		this.tpStatus = tpStatus;
		this.dsObservacao = dsObservacao;
		this.cdAit = cdAit;
		this.cdMovimento = cdMovimento;
		this.cdDocumento = cdDocumento;
		this.nmSituacaoDocumento = nmSituacaoDocumento;
		this.dsOcorrencia = dsOcorrencia;
		this.txtOcorrencia = txtOcorrencia;
		this.tpConsistencia = tpConsistencia;
		this.cdFase = cdFase;
	}
	
	public int getCdAta() {
		return cdAta;
	}

	public void setCdAta(int cdAta) {
		this.cdAta = cdAta;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	
	public String getIdAta() {
		return idAta;
	}

	public void setIdAta(String idAta) {
		this.idAta = idAta;
	}

	public String getNrProtocolo() {
		return nrProtocolo;
	}
	
	public void setNrProtocolo(String nrProtocolo) {
		this.nrProtocolo = nrProtocolo;
	}
	
	public GregorianCalendar getDtEntradaRecurso() {
		return dtEntradaRecurso;
	}
	
	public void setDtEntradaRecurso(GregorianCalendar dtEntradaRecurso) {
		this.dtEntradaRecurso = dtEntradaRecurso;
	}
	
	public String getNmSolicitante() {
		return nmSolicitante;
	}
	
	public void setNmSolicitante(String nmSolicitante) {
		this.nmSolicitante = nmSolicitante;
	}
	
	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}
	
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	
	public GregorianCalendar getDtAta() {
		return dtAta;
	}

	public void setDtAta(GregorianCalendar dtAta) {
		this.dtAta = dtAta;
	}
	
	public GregorianCalendar getDtAtaInicial() {
		return dtAtaInicial;
	}

	public void setDtAtaInicial(GregorianCalendar dtAtaInicial) {
		this.dtAtaInicial = dtAtaInicial;
	}

	public GregorianCalendar getDtAtaFinal() {
		return dtAtaFinal;
	}

	public void setDtAtaFinal(GregorianCalendar dtAtaFinal) {
		this.dtAtaFinal = dtAtaFinal;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdMovimento() {
		return cdMovimento;
	}

	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
	}

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}
	
	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}

	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}

	public String getDsOcorrencia() {
		return dsOcorrencia;
	}

	public void setDsOcorrencia(String dsOcorrencia) {
		this.dsOcorrencia = dsOcorrencia;
	}

	public String getTxtOcorrencia() {
		return txtOcorrencia;
	}

	public void setTxtOcorrencia(String txtOcorrencia) {
		this.txtOcorrencia = txtOcorrencia;
	}

	public String getDsAssunto() {
		return dsAssunto;
	}

	public void setDsAssunto(String dsAssunto) {
		this.dsAssunto = dsAssunto;
	}
	
	public int getTpConsistencia() {
		return tpConsistencia;
	}

	public void setTpConsistencia(int tpConsistencia) {
		this.tpConsistencia = tpConsistencia;
	}

	public int getCdFase() {
		return cdFase;
	}

	public void setCdFase(int cdFase) {
		this.cdFase = cdFase;
	}

	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}
}
