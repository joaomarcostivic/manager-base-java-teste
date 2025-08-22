package com.tivic.manager.mob;

import com.tivic.manager.util.Util;

public class ItemCodigoBarrasMG {
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
	
	public String formarCodigoBarras()
	{
		
		String codigoBarrasSemDv = getIdentificadorArrecadacao()
								 + getIdentificadorSegmento()
								 + getIdentificadorValorReferencia()
								 + getValorInfracao()
								 + getCodigoFebraban()
								 + getDtVencimento()
								 + getDigitoPlaca()
								 + getNrProcessamento()
								 + getOrgaoAutuador()
								 + getCodigoInfracao();
		
		 setDigitoGeral(Integer.toString(Util.getDvMod10(codigoBarrasSemDv))); 
		 
		 String codigoBarrasComDv = getIdentificadorArrecadacao()
			 	  		  		  + getIdentificadorSegmento()
			 	  		  		  + getIdentificadorValorReferencia()
			 	  		  		  + getDigitoGeral()
			 	  		  		  + getValorInfracao()
			 	  		  		  + getCodigoFebraban()
			 	  		  		  + getDtVencimento()
			 	  		  		  + getDigitoPlaca()
			 	  		  		  + getNrProcessamento()
			 	  		  		  + getOrgaoAutuador()
			 	  		  		  + getCodigoInfracao();
		
		 return codigoBarrasComDv;
		
	}
	
}
