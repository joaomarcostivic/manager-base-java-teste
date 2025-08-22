package com.tivic.manager.mob.lote.impressao.codigobarras;

import java.awt.image.BufferedImage;

public class CodigoBarras {
	String identificadorArrecadacao;
	String identificadorSegmento;
	String identificadorValorReferencia;
	String digitoGeral;
	String valorInfracao;
	String codigoFebraban;
	String dtVencimento;
	String digitoPlaca;
	String nrProcessamento;
	String orgaoAutuador;
	String codigoInfracao;
	String codigoBarrasComDv;
	String linhaDigitavel;
	BufferedImage barcodeImage;
	
	public String getIdentificadorArrecadacao() {
		return identificadorArrecadacao;
	}
	
	public void setIdentificadorArrecadacao(String identificadorArrecadacao) {
		this.identificadorArrecadacao = identificadorArrecadacao;
	}
	
	public String getIdentificadorSegmento() {
		return identificadorSegmento;
	}
	
	public void setIdentificadorSegmento(String identificadorSegmento) {
		this.identificadorSegmento = identificadorSegmento;
	}
	
	public String getIdentificadorValorReferencia() {
		return identificadorValorReferencia;
	}
	
	public void setIdentificadorValorReferencia(String identificadorValorReferencia) {
		this.identificadorValorReferencia = identificadorValorReferencia;
	}
	
	public String getDigitoGeral() {
		return digitoGeral;
	}
	
	public void setDigitoGeral(String digitoGeral) {
		this.digitoGeral = digitoGeral;
	}
	
	public String getValorInfracao() {
		return valorInfracao;
	}
	
	public void setValorInfracao(String valorInfracao) {
		this.valorInfracao = valorInfracao;
	}
	
	public String getCodigoFebraban() {
		return codigoFebraban;
	}
	
	public void setCodigoFebraban(String codigoFebraban) {
		this.codigoFebraban = codigoFebraban;
	}
	
	public String getDtVencimento() {
		return dtVencimento;
	}
	
	public void setDtVencimento(String dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	
	public String getDigitoPlaca() {
		return digitoPlaca;
	}
	
	public void setDigitoPlaca(String digitoPlaca) {
		this.digitoPlaca = digitoPlaca;
	}
	
	public String getNrProcessamento() {
		return nrProcessamento;
	}
	
	public void setNrProcessamento(String nrProcessamento) {
		this.nrProcessamento = nrProcessamento;
	}
	
	public String getOrgaoAutuador() {
		return orgaoAutuador;
	}
	
	public void setOrgaoAutuador(String orgaoAutuador) {
		this.orgaoAutuador = orgaoAutuador;
	}
	
	public String getCodigoInfracao() {
		return codigoInfracao;
	}
	
	public void setCodigoInfracao(String codigoInfracao) {
		this.codigoInfracao = codigoInfracao;
	}
	
	public String getCodigoBarrasComDv() {
		return codigoBarrasComDv;
	}
	
	public void setCodigoBarrasComDv(String codigoBarrasComDv) {
		this.codigoBarrasComDv = codigoBarrasComDv;
	}
	
	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}
	
	public void setLinhaDigitavel(String linhaDigitavel) {
		this.linhaDigitavel = linhaDigitavel;
	}
	
	public BufferedImage getBarcodeImage() {
		return barcodeImage;
	}
	
	public void setBarcodeImage(BufferedImage barcodeImage) {
		this.barcodeImage = barcodeImage;
	}
	
	@Override
	public String toString() {
		return "CodigoBarras [identificadorArrecadacao=" + identificadorArrecadacao + ", identificadorSegmento="
				+ identificadorSegmento + ", identificadorValorReferencia=" + identificadorValorReferencia
				+ ", digitoGeral=" + digitoGeral + ", valorInfracao=" + valorInfracao + ", codigoFebraban="
				+ codigoFebraban + ", dtVencimento=" + dtVencimento + ", digitoPlaca=" + digitoPlaca
				+ ", nrProcessamento=" + nrProcessamento + ", orgaoAutuador=" + orgaoAutuador + ", codigoInfracao="
				+ codigoInfracao + ", codigoBarrasComDv=" + codigoBarrasComDv + ", linhaDigitavel=" + linhaDigitavel
				+ ", barcodeImage=" + barcodeImage + "]";
	}
	
}
