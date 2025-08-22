package com.tivic.manager.grl;

public class Parametro {

	private int cdParametro;
	private int cdEmpresa;
	private String nmParametro;
	private int tpDado;
	private int tpApresentacao;
	private String nmRotulo;
	private String txtUrlFiltro;
	private int cdPessoa;
	private int tpParametro;
	private int cdModulo;
	private int cdSistema;
	private int tpNivelAcesso;
	
	private ParametroValor[] valores;
	
	public Parametro(){ }
	
	public Parametro(int cdParametro,
			int cdEmpresa,
			String nmParametro,
			int tpDado,
			int tpApresentacao,
			String nmRotulo,
			String txtUrlFiltro,
			int cdPessoa,
			int tpParametro,
			int cdModulo,
			int cdSistema,
			int tpNivelAcesso){
		setCdParametro(cdParametro);
		setCdEmpresa(cdEmpresa);
		setNmParametro(nmParametro);
		setTpDado(tpDado);
		setTpApresentacao(tpApresentacao);
		setNmRotulo(nmRotulo);
		setTxtUrlFiltro(txtUrlFiltro);
		setCdPessoa(cdPessoa);
		setTpParametro(tpParametro);
		setCdModulo(cdModulo);
		setCdSistema(cdSistema);
		setTpNivelAcesso(tpNivelAcesso);
	}
	public Parametro(int cdParametro, int cdEmpresa, String nmParametro, int tpDado, int tpApresentacao,
			String nmRotulo, String txtUrlFiltro, int cdPessoa, int tpParametro, int cdModulo, int cdSistema,
			int tpNivelAcesso, ParametroValor[] valores) {
		super();
		this.cdParametro = cdParametro;
		this.cdEmpresa = cdEmpresa;
		this.nmParametro = nmParametro;
		this.tpDado = tpDado;
		this.tpApresentacao = tpApresentacao;
		this.nmRotulo = nmRotulo;
		this.txtUrlFiltro = txtUrlFiltro;
		this.cdPessoa = cdPessoa;
		this.tpParametro = tpParametro;
		this.cdModulo = cdModulo;
		this.cdSistema = cdSistema;
		this.tpNivelAcesso = tpNivelAcesso;
		this.valores = valores;
	}

	public void setCdParametro(int cdParametro){
		this.cdParametro=cdParametro;
	}
	public int getCdParametro(){
		return this.cdParametro;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmParametro(String nmParametro){
		this.nmParametro=nmParametro;
	}
	public String getNmParametro(){
		return this.nmParametro;
	}
	public void setTpDado(int tpDado){
		this.tpDado=tpDado;
	}
	public int getTpDado(){
		return this.tpDado;
	}
	public void setTpApresentacao(int tpApresentacao){
		this.tpApresentacao=tpApresentacao;
	}
	public int getTpApresentacao(){
		return this.tpApresentacao;
	}
	public void setNmRotulo(String nmRotulo){
		this.nmRotulo=nmRotulo;
	}
	public String getNmRotulo(){
		return this.nmRotulo;
	}
	public void setTxtUrlFiltro(String txtUrlFiltro){
		this.txtUrlFiltro=txtUrlFiltro;
	}
	public String getTxtUrlFiltro(){
		return this.txtUrlFiltro;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpParametro(int tpParametro){
		this.tpParametro=tpParametro;
	}
	public int getTpParametro(){
		return this.tpParametro;
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
	public void setTpNivelAcesso(int tpNivelAcesso){
		this.tpNivelAcesso=tpNivelAcesso;
	}
	public int getTpNivelAcesso(){
		return this.tpNivelAcesso;
	}
	public ParametroValor[] getValores() {
		return valores;
	}
	public void setValores(ParametroValor[] valores) {
		this.valores = valores;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdParametro: " +  getCdParametro();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmParametro: " +  getNmParametro();
		valueToString += ", tpDado: " +  getTpDado();
		valueToString += ", tpApresentacao: " +  getTpApresentacao();
		valueToString += ", nmRotulo: " +  getNmRotulo();
		valueToString += ", txtUrlFiltro: " +  getTxtUrlFiltro();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpParametro: " +  getTpParametro();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", tpNivelAcesso: " +  getTpNivelAcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Parametro(getCdParametro(),
			getCdEmpresa(),
			getNmParametro(),
			getTpDado(),
			getTpApresentacao(),
			getNmRotulo(),
			getTxtUrlFiltro(),
			getCdPessoa(),
			getTpParametro(),
			getCdModulo(),
			getCdSistema(),
			getTpNivelAcesso());
	}

}
