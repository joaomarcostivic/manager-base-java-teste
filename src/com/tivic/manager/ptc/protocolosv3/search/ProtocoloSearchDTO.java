package com.tivic.manager.ptc.protocolosv3.search;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class ProtocoloSearchDTO {
	private int cdAit;
	private String idAit;
	private String nmTipoDocumento;
	private String nmFase;
	private int cdFase;
	private int cdDocumento;
	private String nrDocumento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazo;
	private String nmSituacaoDocumento;
	private int cdSituacaoDocumento;
	private int cdTipoDocumento;
	private int tpStatus;
	private int cdApresentacaoCondutor;
	private int tpDocumento;
	private String nmProprietario;
	private String nrPlaca;
	private int qtdAit;
	private int totalAits;
	private int nrAno;
	private int nrMes;
	private int qtdJariJulgada;
	private int mesJulgamento;
	private String nrCpf;
	private String nmEmail;
	private String nrTelefone1;
	private String nmPessoa;
	private int stAtual;
	private String txtOcorrencia;
	
	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String getNmTipoDocumento() {
		return nmTipoDocumento;
	}

	public void setNmTipoDocumento(String nmTipoDocumento) {
		this.nmTipoDocumento = nmTipoDocumento;
	}

	public String getNmFase() {
		return nmFase;
	}

	public void setNmFase(String nmFase) {
		this.nmFase = nmFase;
	}

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public GregorianCalendar getDtProtocolo() {
		return dtProtocolo;
	}

	public void setDtProtocolo(GregorianCalendar dtProtocolo) {
		this.dtProtocolo = dtProtocolo;
	}

	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}

	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}

	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}

	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}

	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}

	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public int getCdApresentacaoCondutor() {
		return cdApresentacaoCondutor;
	}

	public void setCdApresentacaoCondutor(int cdApresentacaoCondutor) {
		this.cdApresentacaoCondutor = cdApresentacaoCondutor;
	}

	public int getStAtual() {
		return stAtual;
	}

	public void setStAtual(int stAtual) {
		this.stAtual = stAtual;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public GregorianCalendar getDtPrazo() {
		return dtPrazo;
	}

	public void setDtPrazo(GregorianCalendar dtPrazo) {
		this.dtPrazo = dtPrazo;
	}

	public String getNmProprietario() {
		return nmProprietario;
	}

	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public int getQtdAit() {
		return qtdAit;
	}

	public void setQtdAit(int qtdAit) {
		this.qtdAit = qtdAit;
	}

	public int getTotalAits() {
		return totalAits;
	}

	public void setTotalAits(int totalAits) {
		this.totalAits = totalAits;
	}

	public int getNrAno() {
		return nrAno;
	}

	public void setNrAno(int nrAno) {
		this.nrAno = nrAno;
	}

	public int getNrMes() {
		return nrMes;
	}

	public void setNrMes(int nrMes) {
		this.nrMes = nrMes;
	}

	public int getQtdJariJulgada() {
		return qtdJariJulgada;
	}

	public void setQtdJariJulgada(int qtdJariJulgada) {
		this.qtdJariJulgada = qtdJariJulgada;
	}

	public int getMesJulgamento() {
		return mesJulgamento;
	}

	public void setMesJulgamento(int mesJulgamento) {
		this.mesJulgamento = mesJulgamento;
	}

	public String getNrCpf() {
		return nrCpf;
	}

	public void setNrCpf(String nrCpf) {
		this.nrCpf = nrCpf;
	}

	public String getNmEmail() {
		return nmEmail;
	}

	public void setNmEmail(String nmEmail) {
		this.nmEmail = nmEmail;
	}

	public String getNrTelefone1() {
		return nrTelefone1;
	}

	public void setNrTelefone1(String nrTelefone1) {
		this.nrTelefone1 = nrTelefone1;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public int getCdFase() {
		return cdFase;
	}

	public void setCdFase(int cdFase) {
		this.cdFase = cdFase;
	}

	public String getTxtOcorrencia() {
		return txtOcorrencia;
	}

	public void setTxtOcorrencia(String txtOcorrencia) {
		this.txtOcorrencia = txtOcorrencia;
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
