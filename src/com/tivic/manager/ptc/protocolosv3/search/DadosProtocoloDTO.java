package com.tivic.manager.ptc.protocolosv3.search;

import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.util.Util;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DadosProtocoloDTO {
	private String nrProcesso;
	private int cdAit;
	private int cdMovimento;
	private int cdOcorrencia;
	private int cdTipoDocumento;
	private String idAit;
	private String idTipoDocumento;
	private String nrPlaca;
	private String nrDocumento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtProtocolo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private String nmTipoDocumento;
	private String txtObservacao;
	private int cdDocumento;
	private int cdFase;
	private String nmFase;
	private String nmRequerente;
	private int registros;
	private int cdSituacaoDocumento;
	private String nmSituacaoDocumento;
	private int lgEnviadoDetran;
	private int lgCancelaMovimento;
	private int tpDocumento;
	
	
	public String getNrProcesso() {
		return nrProcesso;
	}

	public void setNrProcesso(String nrProcesso) {
		this.nrProcesso = nrProcesso;
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
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
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
	public String getNmFase() {
		return nmFase;
	}
	public void setNmFase(String nmFase) {
		this.nmFase = nmFase;
	}
	
	public int getRegistros() {
		return registros;
	}
	public void setRegistros(int registros) {
		this.registros = registros;
	}
	
	@Override
	public String toString() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("nrProcesso", nrProcesso);
			jsonObj.put("idAit", idAit);
			jsonObj.put("nrPlaca", nrPlaca);
			jsonObj.put("nrDocumento", nrDocumento);
			jsonObj.put("dtProtocolo", Util.convCalendarStringIso(getDtProtocolo()));
			jsonObj.put("dtMovimento", Util.convCalendarStringIso(getDtMovimento()));
			jsonObj.put("dtInfracao", Util.convCalendarStringIso(getDtInfracao()));
			jsonObj.put("nmTipoDocumento", nmTipoDocumento);
			jsonObj.put("cdDocumento", cdDocumento);
			jsonObj.put("txtObservacao", txtObservacao);
			jsonObj.put("nmRequerente", nmRequerente);
			jsonObj.put("cdFase", cdFase);
			jsonObj.put("nmFase", nmFase);
			jsonObj.put("registros", registros);
			jsonObj.put("cdAit", cdAit);
			jsonObj.put("cdSituacaoDocumento", cdSituacaoDocumento);
			jsonObj.put("nmSituacaoDocumento", nmSituacaoDocumento);
			jsonObj.put("lgCancelaMovimento", lgCancelaMovimento);
			jsonObj.put("lgEnviadoDetran", lgEnviadoDetran);
			jsonObj.put("cdMovimento", cdMovimento);
			jsonObj.put("idTipoDocumento", idTipoDocumento);
			jsonObj.put("cdOcorrencia", cdOcorrencia);
			jsonObj.put("cdTipoDocumento", cdTipoDocumento);
			jsonObj.put("tpDocumento", tpDocumento);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj.toString();
	}

	public String getNmRequerente() {
		return nmRequerente;
	}

	public void setNmRequerente(String nmRequerente) {
		this.nmRequerente = nmRequerente;
	}

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public int getCdFase() {
		return cdFase;
	}

	public void setCdFase(int cdFase) {
		this.cdFase = cdFase;
	}

	
	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdSituacaoDocumento() {
		return cdSituacaoDocumento;
	}

	public void setCdSituacaoDocumento(int cdSituacaoDocumento) {
		this.cdSituacaoDocumento = cdSituacaoDocumento;
	}

	public String getNmSituacaoDocumento() {
		return nmSituacaoDocumento;
	}

	public void setNmSituacaoDocumento(String nmSituacaoDocumento) {
		this.nmSituacaoDocumento = nmSituacaoDocumento;
	}

	public int getLgEnviadoDetran() {
		return lgEnviadoDetran;
	}

	public void setLgEnviadoDetran(int lgEnviadoDetran) {
		this.lgEnviadoDetran = lgEnviadoDetran;
	}

	public int getLgCancelaMovimento() {
		return lgCancelaMovimento;
	}

	public void setLgCancelaMovimento(int lgCancelaMovimento) {
		this.lgCancelaMovimento = lgCancelaMovimento;
	}

	public int getCdMovimento() {
		return cdMovimento;
	}

	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
	}

	public String getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(String idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public int getCdTipoDocumento() {
		return cdTipoDocumento;
	}

	public void setCdTipoDocumento(int cdTipoDocumento) {
		this.cdTipoDocumento = cdTipoDocumento;
	}

	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
}
