package com.tivic.manager.pcb;

import java.util.GregorianCalendar;

public class Bico {

	private int cdBico;
	private String idBico;
	private int nrOrdem;
	private int stBico;
	private GregorianCalendar dtInstalacao;
	private GregorianCalendar dtExclusao;
	private GregorianCalendar dtCadastro;
	private GregorianCalendar dtUltimaAlteracao;
	private int nrCasasInteiras;
	private int nrCasasDecimais;
	private float qtEncerranteInicial;
	private float vlEncerranteInicial;
	private String txtObservacao;
	private int cdBomba;
	private int cdTanque;
	private GregorianCalendar dtFabricacao;
	private int cdTabelaPreco;
	private int cdTurnoInstalacao;
	private int nrEnderecoAutomacao;
	private String nrBicoAutomacao;

	public Bico(int cdBico,
			String idBico,
			int nrOrdem,
			int stBico,
			GregorianCalendar dtInstalacao,
			GregorianCalendar dtExclusao,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtUltimaAlteracao,
			int nrCasasInteiras,
			int nrCasasDecimais,
			float qtEncerranteInicial,
			float vlEncerranteInicial,
			String txtObservacao,
			int cdBomba,
			int cdTanque,
			GregorianCalendar dtFabricacao,
			int cdTabelaPreco,
			int cdTurnoInstalacao,
			int nrEnderecoAutomacao,
			String nrBicoAutomacao){
		setCdBico(cdBico);
		setIdBico(idBico);
		setNrOrdem(nrOrdem);
		setStBico(stBico);
		setDtInstalacao(dtInstalacao);
		setDtExclusao(dtExclusao);
		setDtCadastro(dtCadastro);
		setDtUltimaAlteracao(dtUltimaAlteracao);
		setNrCasasInteiras(nrCasasInteiras);
		setNrCasasDecimais(nrCasasDecimais);
		setQtEncerranteInicial(qtEncerranteInicial);
		setVlEncerranteInicial(vlEncerranteInicial);
		setTxtObservacao(txtObservacao);
		setCdBomba(cdBomba);
		setCdTanque(cdTanque);
		setDtFabricacao(dtFabricacao);
		setCdTabelaPreco(cdTabelaPreco);
		setCdTurnoInstalacao(cdTurnoInstalacao);
		setNrEnderecoAutomacao(nrEnderecoAutomacao);
		setNrBicoAutomacao(nrBicoAutomacao);
	}
	public void setCdBico(int cdBico){
		this.cdBico=cdBico;
	}
	public int getCdBico(){
		return this.cdBico;
	}
	public void setIdBico(String idBico){
		this.idBico=idBico;
	}
	public String getIdBico(){
		return this.idBico;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setStBico(int stBico){
		this.stBico=stBico;
	}
	public int getStBico(){
		return this.stBico;
	}
	public void setDtInstalacao(GregorianCalendar dtInstalacao){
		this.dtInstalacao=dtInstalacao;
	}
	public GregorianCalendar getDtInstalacao(){
		return this.dtInstalacao;
	}
	public void setDtExclusao(GregorianCalendar dtExclusao){
		this.dtExclusao=dtExclusao;
	}
	public GregorianCalendar getDtExclusao(){
		return this.dtExclusao;
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
	public void setNrCasasInteiras(int nrCasasInteiras){
		this.nrCasasInteiras=nrCasasInteiras;
	}
	public int getNrCasasInteiras(){
		return this.nrCasasInteiras;
	}
	public void setNrCasasDecimais(int nrCasasDecimais){
		this.nrCasasDecimais=nrCasasDecimais;
	}
	public int getNrCasasDecimais(){
		return this.nrCasasDecimais;
	}
	public void setQtEncerranteInicial(float qtEncerranteInicial){
		this.qtEncerranteInicial=qtEncerranteInicial;
	}
	public float getQtEncerranteInicial(){
		return this.qtEncerranteInicial;
	}
	public void setVlEncerranteInicial(float vlEncerranteInicial){
		this.vlEncerranteInicial=vlEncerranteInicial;
	}
	public float getVlEncerranteInicial(){
		return this.vlEncerranteInicial;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdBomba(int cdBomba){
		this.cdBomba=cdBomba;
	}
	public int getCdBomba(){
		return this.cdBomba;
	}
	public void setCdTanque(int cdTanque){
		this.cdTanque=cdTanque;
	}
	public int getCdTanque(){
		return this.cdTanque;
	}
	public GregorianCalendar getDtFabricacao() {
		return dtFabricacao;
	}
	public void setDtFabricacao(GregorianCalendar dtFabricacao) {
		this.dtFabricacao = dtFabricacao;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdTurnoInstalacao(int cdTurnoInstalacao){
		this.cdTurnoInstalacao=cdTurnoInstalacao;
	}
	public int getCdTurnoInstalacao(){
		return this.cdTurnoInstalacao;
	}
	public void setNrEnderecoAutomacao(int nrEnderecoAutomacao){
		this.nrEnderecoAutomacao=nrEnderecoAutomacao;
	}
	public int getNrEnderecoAutomacao()	{
		return this.nrEnderecoAutomacao;
	}
	public void setNrBicoAutomacao(String nrBicoAutomacao){
		this.nrBicoAutomacao=nrBicoAutomacao;
	}
	public String getNrBicoAutomacao()	{
		return this.nrBicoAutomacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBico: " +  getCdBico();
		valueToString += ", idBico: " +  getIdBico();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", stBico: " +  getStBico();
		valueToString += ", dtInstalacao: " +  sol.util.Util.formatDateTime(getDtInstalacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtExclusao: " +  sol.util.Util.formatDateTime(getDtExclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtUltimaAlteracao: " +  sol.util.Util.formatDateTime(getDtUltimaAlteracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrCasasInteiras: " +  getNrCasasInteiras();
		valueToString += ", nrCasasDecimais: " +  getNrCasasDecimais();
		valueToString += ", qtEncerranteInicial: " +  getQtEncerranteInicial();
		valueToString += ", vlEncerranteInicial: " +  getVlEncerranteInicial();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdBomba: " +  getCdBomba();
		valueToString += ", cdTanque: " +  getCdTanque();
		valueToString += ", dtFabricacao: " +  sol.util.Util.formatDateTime(getDtFabricacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdTurnoInstalacao: " +  getCdTurnoInstalacao();
		valueToString += ", nrEnderecoAutomacao: " +  getNrEnderecoAutomacao();
		valueToString += ", nrBicoAutomacao: " +  getNrBicoAutomacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Bico(getCdBico(),
			getIdBico(),
			getNrOrdem(),
			getStBico(),
			getDtInstalacao()==null ? null : (GregorianCalendar)getDtInstalacao().clone(),
			getDtExclusao()==null ? null : (GregorianCalendar)getDtExclusao().clone(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtUltimaAlteracao()==null ? null : (GregorianCalendar)getDtUltimaAlteracao().clone(),
			getNrCasasInteiras(),
			getNrCasasDecimais(),
			getQtEncerranteInicial(),
			getVlEncerranteInicial(),
			getTxtObservacao(),
			getCdBomba(),
			getCdTanque(),
			getDtFabricacao(),
			getCdTabelaPreco(),
			getCdTurnoInstalacao(),
			getNrEnderecoAutomacao(),
			getNrBicoAutomacao());
	}

}