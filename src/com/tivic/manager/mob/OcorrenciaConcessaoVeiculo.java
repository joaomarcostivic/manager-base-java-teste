package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class OcorrenciaConcessaoVeiculo extends com.tivic.manager.grl.Ocorrencia {

	private int cdConcessaoVeiculo;
	private int lgManutencaoAnterior;
	private int lgManutencaoPosterior;

	public OcorrenciaConcessaoVeiculo() { }

	public OcorrenciaConcessaoVeiculo(int cdOcorrencia,
			int cdPessoa,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia,
			int cdTipoOcorrencia,
			int stOcorrencia,
			int cdSistema,
			int cdUsuario,
			int cdConcessaoVeiculo,
			int lgManutencaoAnterior,
			int lgManutencaoPosterior) {
		super(cdOcorrencia,
			cdPessoa,
			txtOcorrencia,
			dtOcorrencia,
			cdTipoOcorrencia,
			stOcorrencia,
			cdSistema,
			cdUsuario);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setLgManutencaoAnterior(lgManutencaoAnterior);
		setLgManutencaoPosterior(lgManutencaoPosterior);
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setLgManutencaoAnterior(int lgManutencaoAnterior){
		this.lgManutencaoAnterior=lgManutencaoAnterior;
	}
	public int getLgManutencaoAnterior(){
		return this.lgManutencaoAnterior;
	}
	public void setLgManutencaoPosterior(int lgManutencaoPosterior){
		this.lgManutencaoPosterior=lgManutencaoPosterior;
	}
	public int getLgManutencaoPosterior(){
		return this.lgManutencaoPosterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", lgManutencaoAnterior: " +  getLgManutencaoAnterior();
		valueToString += ", lgManutencaoPosterior: " +  getLgManutencaoPosterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OcorrenciaConcessaoVeiculo(getCdOcorrencia(),
			getCdPessoa(),
			getTxtOcorrencia(),
			getDtOcorrencia(),
			getCdTipoOcorrencia(),
			getStOcorrencia(),
			getCdSistema(),
			getCdUsuario(),
			getCdConcessaoVeiculo(),
			getLgManutencaoAnterior(),
			getLgManutencaoPosterior());
	}

}