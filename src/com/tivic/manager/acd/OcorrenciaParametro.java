package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OcorrenciaParametro extends com.tivic.manager.grl.Ocorrencia {

	
	private int cdParametro;
	private int cdOpcaoAnterior;
	private int cdOpcaoPosterior;

	public OcorrenciaParametro() { }

	public OcorrenciaParametro(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdParametro,
			int cdOpcaoAnterior,
			int cdOpcaoPosterior) {
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdParametro(cdParametro);
		setCdOpcaoAnterior(cdOpcaoAnterior);
		setCdOpcaoPosterior(cdOpcaoPosterior);
	}
	public void setCdParametro(int cdParametro){
		this.cdParametro=cdParametro;
	}
	public int getCdParametro(){
		return this.cdParametro;
	}
	public void setCdOpcaoAnterior(int cdOpcaoAnterior){
		this.cdOpcaoAnterior=cdOpcaoAnterior;
	}
	public int getCdOpcaoAnterior(){
		return this.cdOpcaoAnterior;
	}
	public void setCdOpcaoPosterior(int cdOpcaoPosterior){
		this.cdOpcaoPosterior=cdOpcaoPosterior;
	}
	public int getCdOpcaoPosterior(){
		return this.cdOpcaoPosterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdParametro: " +  getCdParametro();
		valueToString += ", cdOpcaoAnterior: " +  getCdOpcaoAnterior();
		valueToString += ", cdOpcaoPosterior: " +  getCdOpcaoPosterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaParametro(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdParametro(),
			getCdOpcaoAnterior(),
			getCdOpcaoPosterior());
	}

}