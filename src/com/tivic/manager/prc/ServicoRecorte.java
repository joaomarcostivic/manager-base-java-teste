package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class ServicoRecorte {

	private int cdServico;
	private String nmServico;
	private int tpServico;
	private String idToken;
	private String idCliente;
	private String nmSenha;
	private String dsUrl;
	private String txtFormato;
	private int vlTimeout;
	private GregorianCalendar dtUltimaBusca;
	private int lgEstado;
	private int stServico;
	private int lgConfirmarLeitura;

	public ServicoRecorte() { }

	public ServicoRecorte(int cdServico,
			String nmServico,
			int tpServico,
			String idToken,
			String idCliente,
			String nmSenha,
			String dsUrl,
			String txtFormato,
			int vlTimeout,
			GregorianCalendar dtUltimaBusca,
			int lgEstado,
			int stServico,
			int lgConfirmarLeitura) {
		setCdServico(cdServico);
		setNmServico(nmServico);
		setTpServico(tpServico);
		setIdToken(idToken);
		setIdCliente(idCliente);
		setNmSenha(nmSenha);
		setDsUrl(dsUrl);
		setTxtFormato(txtFormato);
		setVlTimeout(vlTimeout);
		setDtUltimaBusca(dtUltimaBusca);
		setLgEstado(lgEstado);
		setStServico(stServico);
		setLgConfirmarLeitura(lgConfirmarLeitura);
	}
	public void setCdServico(int cdServico){
		this.cdServico=cdServico;
	}
	public int getCdServico(){
		return this.cdServico;
	}
	public void setNmServico(String nmServico){
		this.nmServico=nmServico;
	}
	public String getNmServico(){
		return this.nmServico;
	}
	public void setTpServico(int tpServico){
		this.tpServico=tpServico;
	}
	public int getTpServico(){
		return this.tpServico;
	}
	public void setIdToken(String idToken){
		this.idToken=idToken;
	}
	public String getIdToken(){
		return this.idToken;
	}
	public void setIdCliente(String idCliente){
		this.idCliente=idCliente;
	}
	public String getIdCliente(){
		return this.idCliente;
	}
	public void setNmSenha(String nmSenha){
		this.nmSenha=nmSenha;
	}
	public String getNmSenha(){
		return this.nmSenha;
	}
	public void setDsUrl(String dsUrl){
		this.dsUrl=dsUrl;
	}
	public String getDsUrl(){
		return this.dsUrl;
	}
	public void setTxtFormato(String txtFormato){
		this.txtFormato=txtFormato;
	}
	public String getTxtFormato(){
		return this.txtFormato;
	}
	public void setVlTimeout(int vlTimeout){
		this.vlTimeout=vlTimeout;
	}
	public int getVlTimeout(){
		return this.vlTimeout;
	}
	public void setDtUltimaBusca(GregorianCalendar dtUltimaBusca){
		this.dtUltimaBusca=dtUltimaBusca;
	}
	public GregorianCalendar getDtUltimaBusca(){
		return this.dtUltimaBusca;
	}
	public void setLgEstado(int lgEstado){
		this.lgEstado=lgEstado;
	}
	public int getLgEstado(){
		return this.lgEstado;
	}
	public void setStServico(int stServico){
		this.stServico=stServico;
	}
	public int getStServico(){
		return this.stServico;
	}
	public void setLgConfirmarLeitura(int lgConfirmarLeitura){
		this.lgConfirmarLeitura=lgConfirmarLeitura;
	}
	public int getLgConfirmarLeitura(){
		return this.lgConfirmarLeitura;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdServico: " +  getCdServico();
		valueToString += ", nmServico: " +  getNmServico();
		valueToString += ", tpServico: " +  getTpServico();
		valueToString += ", idToken: " +  getIdToken();
		valueToString += ", idCliente: " +  getIdCliente();
		valueToString += ", nmSenha: " +  getNmSenha();
		valueToString += ", dsUrl: " +  getDsUrl();
		valueToString += ", txtFormato: " +  getTxtFormato();
		valueToString += ", vlTimeout: " +  getVlTimeout();
		valueToString += ", dtUltimaBusca: " +  sol.util.Util.formatDateTime(getDtUltimaBusca(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgEstado: " +  getLgEstado();
		valueToString += ", stServico: " +  getStServico();
		valueToString += ", lgConfirmarLeitura: " +  getLgConfirmarLeitura();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ServicoRecorte(getCdServico(),
			getNmServico(),
			getTpServico(),
			getIdToken(),
			getIdCliente(),
			getNmSenha(),
			getDsUrl(),
			getTxtFormato(),
			getVlTimeout(),
			getDtUltimaBusca()==null ? null : (GregorianCalendar)getDtUltimaBusca().clone(),
			getLgEstado(),
			getStServico(),
			getLgConfirmarLeitura());
	}

}