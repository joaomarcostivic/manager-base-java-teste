package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class VeiculoConcessionario {

	private int cdConcessaoVeiculo;
	private int cdConcessionarioPessoa;
	private GregorianCalendar dtVinculacao;
	private int stVeiculoConcessionario;

	public VeiculoConcessionario() { }

	public VeiculoConcessionario(int cdConcessaoVeiculo,
			int cdConcessionarioPessoa,
			GregorianCalendar dtVinculacao,
			int stVeiculoConcessionario) {
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setCdConcessionarioPessoa(cdConcessionarioPessoa);
		setDtVinculacao(dtVinculacao);
		setStVeiculoConcessionario(stVeiculoConcessionario);
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public void setCdConcessionarioPessoa(int cdConcessionarioPessoa){
		this.cdConcessionarioPessoa=cdConcessionarioPessoa;
	}
	public int getCdConcessionarioPessoa(){
		return this.cdConcessionarioPessoa;
	}
	public void setDtVinculacao(GregorianCalendar dtVinculacao){
		this.dtVinculacao=dtVinculacao;
	}
	public GregorianCalendar getDtVinculacao(){
		return this.dtVinculacao;
	}
	public void setStVeiculoConcessionario(int stVeiculoConcessionario){
		this.stVeiculoConcessionario=stVeiculoConcessionario;
	}
	public int getStVeiculoConcessionario(){
		return this.stVeiculoConcessionario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", cdConcessionarioPessoa: " +  getCdConcessionarioPessoa();
		valueToString += ", dtVinculacao: " +  sol.util.Util.formatDateTime(getDtVinculacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stVeiculoConcessionario: " +  getStVeiculoConcessionario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VeiculoConcessionario(getCdConcessaoVeiculo(),
			getCdConcessionarioPessoa(),
			getDtVinculacao()==null ? null : (GregorianCalendar)getDtVinculacao().clone(),
			getStVeiculoConcessionario());
	}

}