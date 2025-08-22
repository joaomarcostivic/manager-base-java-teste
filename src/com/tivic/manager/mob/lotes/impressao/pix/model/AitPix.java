package com.tivic.manager.mob.lotes.impressao.pix.model;

import java.util.GregorianCalendar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitPix {
	
	private int cdPix;
	private int cdAit;
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
	private String nmDevedor;
	private String nrCpfCnpj;
	private String nmLocalizacao;
	private String dsQrCode;
	
	public AitPix(int cdPix,
			int cdAit,
			int cdCobranca,
			String txtTxid,
			int stCobranca,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtVencimento,
			Double vlCobranca,
			String txtObservacao,
			int tpCobranca,
			String nrCodigoGuiaRecebimento,
			String nmDevedor,
			String nrCpfCnpj,
			String nmLocalizacao,
			String dsQrCode) {
		setCdPix(cdPix);
		setCdAit(cdAit);
		setCdCobranca(cdCobranca);
		setTxtTxid(txtTxid);
		setStCobranca(stCobranca);
		setDtCriacao(dtCriacao);
		setDtVencimento(dtVencimento);
		setVlCobranca(vlCobranca);
		setTxtObservacao(txtObservacao);
		setTpCobranca(tpCobranca);
		setNrCodigoGuiaRecebimento(nrCodigoGuiaRecebimento);
		setNmDevedor(nmDevedor);
		setNrCpfCnpj(nrCpfCnpj);
		setNmLocalizacao(nmLocalizacao);
		setDsQrCode(dsQrCode);
	}
	
	public AitPix() {}

	public int getCdPix() {
		return cdPix;
	}
	public void setCdPix(int cdPix) {
		this.cdPix = cdPix;
	}
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
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
	public String getNmDevedor() {
		return nmDevedor;
	}
	public void setNmDevedor(String nmDevedor) {
		this.nmDevedor = nmDevedor;
	}
	public String getNrCpfCnpj() {
		return nrCpfCnpj;
	}
	public void setNrCpfCnpj(String nrCpfCnpj) {
		this.nrCpfCnpj = nrCpfCnpj;
	}
	public String getNmLocalizacao() {
		return nmLocalizacao;
	}
	public void setNmLocalizacao(String nmLocalizacao) {
		this.nmLocalizacao = nmLocalizacao;
	}
	public String getDsQrCode() {
		return dsQrCode;
	}
	public void setDsQrCode(String dsQrCode) {
		this.dsQrCode = dsQrCode;
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
