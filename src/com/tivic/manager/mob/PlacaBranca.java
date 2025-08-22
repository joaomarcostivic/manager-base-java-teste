package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class PlacaBranca {

	private int cdPlaca;
	private String nrPlaca;
	private GregorianCalendar dtCadastro;
	private GregorianCalendar dtAtualizacao;
	private int stPlaca;
	private String txtObservacao;
	private int cdUsuario;
	private int cdOrgao;

	public PlacaBranca() { }

	public PlacaBranca(int cdPlaca,
			String nrPlaca,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtAtualizacao,
			int stPlaca,
			String txtObservacao,
			int cdUsuario,
			int cdOrgao) {
		setCdPlaca(cdPlaca);
		setNrPlaca(nrPlaca);
		setDtCadastro(dtCadastro);
		setDtAtualizacao(dtAtualizacao);
		setStPlaca(stPlaca);
		setTxtObservacao(txtObservacao);
		setCdUsuario(cdUsuario);
		setCdOrgao(cdOrgao);
	}
	public void setCdPlaca(int cdPlaca){
		this.cdPlaca=cdPlaca;
	}
	public int getCdPlaca(){
		return this.cdPlaca;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao){
		this.dtAtualizacao=dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao(){
		return this.dtAtualizacao;
	}
	public void setStPlaca(int stPlaca){
		this.stPlaca=stPlaca;
	}
	public int getStPlaca(){
		return this.stPlaca;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlaca: " +  getCdPlaca();
		valueToString += ", nrPlaca: " +  getNrPlaca();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAtualizacao: " +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stPlaca: " +  getStPlaca();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdOrgao: " +  getCdOrgao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlacaBranca(getCdPlaca(),
			getNrPlaca(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getStPlaca(),
			getTxtObservacao(),
			getCdUsuario(),
			getCdOrgao());
	}

}