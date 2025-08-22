package com.tivic.manager.mob;

public class PassageiroHorario {

	private int cdConcessionarioPessoa;
	private int cdHorario;
	private int cdTabelaHorario;
	private int cdLinha;
	private int cdRota;
	private int cdTrecho;
	private int cdControle;
	private int lgPresenca;

	public PassageiroHorario() { }

	public PassageiroHorario(int cdConcessionarioPessoa,
			int cdHorario,
			int cdTabelaHorario,
			int cdLinha,
			int cdRota,
			int cdTrecho,
			int cdControle,
			int lgPresenca) {
		setCdConcessionarioPessoa(cdConcessionarioPessoa);
		setCdHorario(cdHorario);
		setCdTabelaHorario(cdTabelaHorario);
		setCdLinha(cdLinha);
		setCdRota(cdRota);
		setCdTrecho(cdTrecho);
		setCdControle(cdControle);
		setLgPresenca(lgPresenca);
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
	public void setCdControle(int cdControle){
		this.cdControle=cdControle;
	}
	public int getCdControle(){
		return this.cdControle;
	}
	public void setLgPresenca(int lgPresenca){
		this.lgPresenca=lgPresenca;
	}
	public int getLgPresenca(){
		return this.lgPresenca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessionarioPessoa: " +  getCdConcessionarioPessoa();
		valueToString += ", cdHorario: " +  getCdHorario();
		valueToString += ", cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdTrecho: " +  getCdTrecho();
		valueToString += ", cdControle: " +  getCdControle();
		valueToString += ", lgPresenca: " +  getLgPresenca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PassageiroHorario(getCdConcessionarioPessoa(),
			getCdHorario(),
			getCdTabelaHorario(),
			getCdLinha(),
			getCdRota(),
			getCdTrecho(),
			getCdControle(),
			getLgPresenca());
	}

}