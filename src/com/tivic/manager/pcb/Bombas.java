package com.tivic.manager.pcb;

import java.util.GregorianCalendar;

public class Bombas {

	private int cdBomba;
	private int nrIlha;
	private String nmBomba;
	private String idBomba;
	private int nrOrdem;
	private int tpBomba;
	private int stSituacao;
	private GregorianCalendar dtFabricacao;
	private String nmModelo;
	private GregorianCalendar dtCadastro;
	private GregorianCalendar dtUltimaAlteracao;
	private GregorianCalendar dtInstalacao;
	private GregorianCalendar dtBaixa;
	private String txtObservacao;
	private int cdFabricante;

	public Bombas(int cdBomba,
			int nrIlha,
			String nmBomba,
			String idBomba,
			int nrOrdem,
			int tpBomba,
			int stSituacao,
			GregorianCalendar dtFabricacao,
			String nmModelo,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtUltimaAlteracao,
			GregorianCalendar dtInstalacao,
			GregorianCalendar dtBaixa,
			String txtObservacao,
			int cdFabricante){
		setCdBomba(cdBomba);
		setNrIlha(nrIlha);
		setNmBomba(nmBomba);
		setIdBomba(idBomba);
		setNrOrdem(nrOrdem);
		setTpBomba(tpBomba);
		setStSituacao(stSituacao);
		setDtFabricacao(dtFabricacao);
		setNmModelo(nmModelo);
		setDtCadastro(dtCadastro);
		setDtUltimaAlteracao(dtUltimaAlteracao);
		setDtInstalacao(dtInstalacao);
		setDtBaixa(dtBaixa);
		setTxtObservacao(txtObservacao);
		setCdFabricante(cdFabricante);
	}
	public void setCdBomba(int cdBomba){
		this.cdBomba=cdBomba;
	}
	public int getCdBomba(){
		return this.cdBomba;
	}
	public void setNrIlha(int nrIlha){
		this.nrIlha=nrIlha;
	}
	public int getNrIlha(){
		return this.nrIlha;
	}
	public void setNmBomba(String nmBomba){
		this.nmBomba=nmBomba;
	}
	public String getNmBomba(){
		return this.nmBomba;
	}
	public void setIdBomba(String idBomba){
		this.idBomba=idBomba;
	}
	public String getIdBomba(){
		return this.idBomba;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setTpBomba(int tpBomba){
		this.tpBomba=tpBomba;
	}
	public int getTpBomba(){
		return this.tpBomba;
	}
	public void setStSituacao(int stSituacao){
		this.stSituacao=stSituacao;
	}
	public int getStSituacao(){
		return this.stSituacao;
	}
	public void setDtFabricacao(GregorianCalendar dtFabricacao){
		this.dtFabricacao=dtFabricacao;
	}
	public GregorianCalendar getDtFabricacao(){
		return this.dtFabricacao;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setDtUltimaAlteracao(GregorianCalendar dtUltimaAlteracao){
		this.dtUltimaAlteracao=dtUltimaAlteracao;
	}
	public GregorianCalendar getDtUltimaAlteracao(){
		return this.dtUltimaAlteracao;
	}
	public void setDtInstalacao(GregorianCalendar dtInstalacao){
		this.dtInstalacao=dtInstalacao;
	}
	public GregorianCalendar getDtInstalacao(){
		return this.dtInstalacao;
	}
	public void setDtBaixa(GregorianCalendar dtBaixa){
		this.dtBaixa=dtBaixa;
	}
	public GregorianCalendar getDtBaixa(){
		return this.dtBaixa;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdFabricante(int cdFabricante){
		this.cdFabricante=cdFabricante;
	}
	public int getCdFabricante(){
		return this.cdFabricante;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBomba: " +  getCdBomba();
		valueToString += ", nrIlha: " +  getNrIlha();
		valueToString += ", nmBomba: " +  getNmBomba();
		valueToString += ", idBomba: " +  getIdBomba();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", tpBomba: " +  getTpBomba();
		valueToString += ", stSituacao: " +  getStSituacao();
		valueToString += ", dtFabricacao: " +  sol.util.Util.formatDateTime(getDtFabricacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmModelo: " +  getNmModelo();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtUltimaAlteracao: " +  sol.util.Util.formatDateTime(getDtUltimaAlteracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInstalacao: " +  sol.util.Util.formatDateTime(getDtInstalacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtBaixa: " +  sol.util.Util.formatDateTime(getDtBaixa(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdFabricante: " +  getCdFabricante();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Bombas(getCdBomba(),
			getNrIlha(),
			getNmBomba(),
			getIdBomba(),
			getNrOrdem(),
			getTpBomba(),
			getStSituacao(),
			getDtFabricacao()==null ? null : (GregorianCalendar)getDtFabricacao().clone(),
			getNmModelo(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtUltimaAlteracao()==null ? null : (GregorianCalendar)getDtUltimaAlteracao().clone(),
			getDtInstalacao()==null ? null : (GregorianCalendar)getDtInstalacao().clone(),
			getDtBaixa()==null ? null : (GregorianCalendar)getDtBaixa().clone(),
			getTxtObservacao(),
			getCdFabricante());
	}

}