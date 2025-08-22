package com.tivic.manager.mob.lotes.impressao.pix;

import java.util.GregorianCalendar;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.util.Util;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DadosArrecadacaoRetorno {

	private int cdCobranca;
	private String txtTxid;
	private int stCobranca;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCriacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	private Double vlCobranca; 
	private String txtObservacao;
	private int tpCobranca;
	private String nrCodigoGuiaRecebimento;
    private Devedor devedor;
	private String location;
	private String qrCode;

	public int getCdCobranca() {
		return cdCobranca;
	}

	public void setCdCobranca(int cdCobranca) {
		this.cdCobranca = cdCobranca;
	}

	public String getTxtTxid() {
		return txtTxid;
	}

	public void setTxtTxid(String txtTxid) {
		this.txtTxid = txtTxid;
	}

	public int getStCobranca() {
		return stCobranca;
	}

	public void setStCobranca(int stCobranca) {
		this.stCobranca = stCobranca;
	}

	public GregorianCalendar getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(GregorianCalendar dtCriacao) {
		this.dtCriacao = dtCriacao;
	}

	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Double getVlCobranca() {
		return vlCobranca;
	}

	public void setVlCobranca(Double vlCobranca) {
		this.vlCobranca = vlCobranca;
	}

	public String getTxtObservacao() {
		return txtObservacao;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public int getTpCobranca() {
		return tpCobranca;
	}

	public void setTpCobranca(int tpCobranca) {
		this.tpCobranca = tpCobranca;
	}

	public String getNrCodigoGuiaRecebimento() {
		return nrCodigoGuiaRecebimento;
	}

	public void setNrCodigoGuiaRecebimento(String nrCodigoGuiaRecebimento) {
		this.nrCodigoGuiaRecebimento = nrCodigoGuiaRecebimento;
	}

	public Devedor getDevedor() {
		return devedor;
	}

	public void setDevedor(Devedor devedor) {
		this.devedor = devedor;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	@Override
	public String toString() {
	    return "{\"cdCobranca\": \"" + getCdCobranca() + "\""
	            + ", \"txtTxid\": \"" + getTxtTxid() + "\""
	            + ", \"stCobranca\": \"" + getStCobranca() + "\""
	            + ", \"dtCriacao\": \"" + Util.formatDate(getDtCriacao(), "yyyyMMdd") + "\""
	            + ", \"dtVencimento\": \"" + Util.formatDate(getDtVencimento(), "yyyyMMdd") + "\""
	            + ", \"vlCobranca\": \"" + getVlCobranca() + "\""
	            + ", \"txtObservacao\": \"" + getTxtObservacao() + "\""
	            + ", \"tpCobranca\": \"" + getTpCobranca() + "\""
	            + ", \"nrCodigoGuiaRecebimento\": \"" + getNrCodigoGuiaRecebimento() + "\""
	            + ", \"devedor\": \"" + getDevedor() + "\""
	            + ", \"location\": \"" + getLocation() + "\""
	            + ", \"qrCode\": \"" + getQrCode() + "\""
	            + "}";
	}

}