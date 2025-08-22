package com.tivic.manager.acd;

public class InstituicaoDependencia {

	private int cdDependencia;
	private int cdInstituicao;
	private int cdTipoDependencia;
	private String nmDependencia;
	private String txtDependencia;
	private int stDependencia;
	private int lgPermanente;
	private int tpLocalizacao;
	private int vlCapacidade;
	private int lgRampaAcesso;
	private String idDependencia;
	private int cdPeriodoLetivo;

	public InstituicaoDependencia() { }

	public InstituicaoDependencia(int cdDependencia,
			int cdInstituicao,
			int cdTipoDependencia,
			String nmDependencia,
			String txtDependencia,
			int stDependencia,
			int lgPermanente,
			int tpLocalizacao,
			int vlCapacidade,
			int lgRampaAcesso,
			String idDependencia,
			int cdPeriodoLetivo) {
		setCdDependencia(cdDependencia);
		setCdInstituicao(cdInstituicao);
		setCdTipoDependencia(cdTipoDependencia);
		setNmDependencia(nmDependencia);
		setTxtDependencia(txtDependencia);
		setStDependencia(stDependencia);
		setLgPermanente(lgPermanente);
		setTpLocalizacao(tpLocalizacao);
		setVlCapacidade(vlCapacidade);
		setLgRampaAcesso(lgRampaAcesso);
		setIdDependencia(idDependencia);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdDependencia(int cdDependencia){
		this.cdDependencia=cdDependencia;
	}
	public int getCdDependencia(){
		return this.cdDependencia;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTipoDependencia(int cdTipoDependencia){
		this.cdTipoDependencia=cdTipoDependencia;
	}
	public int getCdTipoDependencia(){
		return this.cdTipoDependencia;
	}
	public void setNmDependencia(String nmDependencia){
		this.nmDependencia=nmDependencia;
	}
	public String getNmDependencia(){
		return this.nmDependencia;
	}
	public void setTxtDependencia(String txtDependencia){
		this.txtDependencia=txtDependencia;
	}
	public String getTxtDependencia(){
		return this.txtDependencia;
	}
	public void setStDependencia(int stDependencia){
		this.stDependencia=stDependencia;
	}
	public int getStDependencia(){
		return this.stDependencia;
	}
	public void setLgPermanente(int lgPermanente){
		this.lgPermanente=lgPermanente;
	}
	public int getLgPermanente(){
		return this.lgPermanente;
	}
	public void setTpLocalizacao(int tpLocalizacao){
		this.tpLocalizacao=tpLocalizacao;
	}
	public int getTpLocalizacao(){
		return this.tpLocalizacao;
	}
	public void setVlCapacidade(int vlCapacidade){
		this.vlCapacidade=vlCapacidade;
	}
	public int getVlCapacidade(){
		return this.vlCapacidade;
	}
	public void setLgRampaAcesso(int lgRampaAcesso){
		this.lgRampaAcesso=lgRampaAcesso;
	}
	public int getLgRampaAcesso(){
		return this.lgRampaAcesso;
	}
	public void setIdDependencia(String idDependencia){
		this.idDependencia=idDependencia;
	}
	public String getIdDependencia(){
		return this.idDependencia;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDependencia: " +  getCdDependencia();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdTipoDependencia: " +  getCdTipoDependencia();
		valueToString += ", nmDependencia: " +  getNmDependencia();
		valueToString += ", txtDependencia: " +  getTxtDependencia();
		valueToString += ", stDependencia: " +  getStDependencia();
		valueToString += ", lgPermanente: " +  getLgPermanente();
		valueToString += ", tpLocalizacao: " +  getTpLocalizacao();
		valueToString += ", vlCapacidade: " +  getVlCapacidade();
		valueToString += ", lgRampaAcesso: " +  getLgRampaAcesso();
		valueToString += ", idDependencia: " +  getIdDependencia();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoDependencia(getCdDependencia(),
			getCdInstituicao(),
			getCdTipoDependencia(),
			getNmDependencia(),
			getTxtDependencia(),
			getStDependencia(),
			getLgPermanente(),
			getTpLocalizacao(),
			getVlCapacidade(),
			getLgRampaAcesso(),
			getIdDependencia(),
			getCdPeriodoLetivo());
	}

}