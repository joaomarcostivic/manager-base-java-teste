package com.tivic.manager.seg;

public class AgrupamentoAcao {

	private int cdAgrupamento;
	private int cdModulo;
	private int cdSistema;
	private String nmAgrupamento;
	private String idAgrupamento;
	private String dsAgrupamento;
	private int cdAgrupamentoSuperior;
	private int lgAtivo;
	private int nrOrdem;

	private Acoes acoes;
	
	public AgrupamentoAcao() { }

	public AgrupamentoAcao(int cdAgrupamento,
			int cdModulo,
			int cdSistema,
			String nmAgrupamento,
			String idAgrupamento,
			String dsAgrupamento,
			int cdAgrupamentoSuperior,
			int lgAtivo,
			int nrOrdem) {
		setCdAgrupamento(cdAgrupamento);
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setNmAgrupamento(nmAgrupamento);
		setIdAgrupamento(idAgrupamento);
		setDsAgrupamento(dsAgrupamento);
		setCdAgrupamentoSuperior(cdAgrupamentoSuperior);
		setLgAtivo(lgAtivo);
		setNrOrdem(nrOrdem);
	}
	public void setCdAgrupamento(int cdAgrupamento){
		this.cdAgrupamento=cdAgrupamento;
	}
	public int getCdAgrupamento(){
		return this.cdAgrupamento;
	}
	public void setCdModulo(int cdModulo){
		this.cdModulo=cdModulo;
	}
	public int getCdModulo(){
		return this.cdModulo;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setNmAgrupamento(String nmAgrupamento){
		this.nmAgrupamento=nmAgrupamento;
	}
	public String getNmAgrupamento(){
		return this.nmAgrupamento;
	}
	public void setIdAgrupamento(String idAgrupamento){
		this.idAgrupamento=idAgrupamento;
	}
	public String getIdAgrupamento(){
		return this.idAgrupamento;
	}
	public void setDsAgrupamento(String dsAgrupamento){
		this.dsAgrupamento=dsAgrupamento;
	}
	public String getDsAgrupamento(){
		return this.dsAgrupamento;
	}
	public void setCdAgrupamentoSuperior(int cdAgrupamentoSuperior){
		this.cdAgrupamentoSuperior=cdAgrupamentoSuperior;
	}
	public int getCdAgrupamentoSuperior(){
		return this.cdAgrupamentoSuperior;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setAcoes(Acoes acoes) {
		this.acoes = acoes;
	}
	public Acoes getAcoes() {
		return acoes;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgrupamento: " +  getCdAgrupamento();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", nmAgrupamento: " +  getNmAgrupamento();
		valueToString += ", idAgrupamento: " +  getIdAgrupamento();
		valueToString += ", dsAgrupamento: " +  getDsAgrupamento();
		valueToString += ", cdAgrupamentoSuperior: " +  getCdAgrupamentoSuperior();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgrupamentoAcao(getCdAgrupamento(),
			getCdModulo(),
			getCdSistema(),
			getNmAgrupamento(),
			getIdAgrupamento(),
			getDsAgrupamento(),
			getCdAgrupamentoSuperior(),
			getLgAtivo(),
			getNrOrdem());
	}

}