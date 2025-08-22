package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class Passageiro {

	private int cdConcessionarioPessoa;
	private int cdHorario;
	private int cdTabelaHorario;
	private int cdLinha;
	private int cdRota;
	private int cdTrecho;
	private GregorianCalendar dtVinculacao;
	private int stPassageiro;

	public Passageiro() { }

	public Passageiro(int cdConcessionarioPessoa,
			int cdHorario,
			int cdTabelaHorario,
			int cdLinha,
			int cdRota,
			int cdTrecho,
			GregorianCalendar dtVinculacao,
			int stPassageiro) {
		setCdConcessionarioPessoa(cdConcessionarioPessoa);
		setCdHorario(cdHorario);
		setCdTabelaHorario(cdTabelaHorario);
		setCdLinha(cdLinha);
		setCdRota(cdRota);
		setCdTrecho(cdTrecho);
		setDtVinculacao(dtVinculacao);
		setStPassageiro(stPassageiro);
	}
	public void setCdConcessionarioPessoa(int cdConcessionarioPessoa){
		this.cdConcessionarioPessoa=cdConcessionarioPessoa;
	}
	public int getCdConcessionarioPessoa(){
		return this.cdConcessionarioPessoa;
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
	}
	public void setCdTabelaHorario(int cdTabelaHorario){
		this.cdTabelaHorario=cdTabelaHorario;
	}
	public int getCdTabelaHorario(){
		return this.cdTabelaHorario;
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setCdTrecho(int cdTrecho){
		this.cdTrecho=cdTrecho;
	}
	public int getCdTrecho(){
		return this.cdTrecho;
	}
	public void setDtVinculacao(GregorianCalendar dtVinculacao){
		this.dtVinculacao=dtVinculacao;
	}
	public GregorianCalendar getDtVinculacao(){
		return this.dtVinculacao;
	}
	public void setStPassageiro(int stPassageiro){
		this.stPassageiro=stPassageiro;
	}
	public int getStPassageiro(){
		return this.stPassageiro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessionarioPessoa: " +  getCdConcessionarioPessoa();
		valueToString += ", cdHorario: " +  getCdHorario();
		valueToString += ", cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdTrecho: " +  getCdTrecho();
		valueToString += ", dtVinculacao: " +  sol.util.Util.formatDateTime(getDtVinculacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stPassageiro: " +  getStPassageiro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Passageiro(getCdConcessionarioPessoa(),
			getCdHorario(),
			getCdTabelaHorario(),
			getCdLinha(),
			getCdRota(),
			getCdTrecho(),
			getDtVinculacao()==null ? null : (GregorianCalendar)getDtVinculacao().clone(),
			getStPassageiro());
	}

}