package com.tivic.manager.srh;

public class Dependente {

	private int cdPessoa;
	private int cdDependente;
	private int tpParentesco;
	private int lgIr;
	private int lgSalarioFamilia;
	private int lgPensionista;
	private int tpPagamento;
	private int tpCalculoPensao;
	private float vlAplicacao;
	private String nrConta;
	private int tpOperacao;
	private String nrAgencia;
	private int cdBanco;

	public Dependente(int cdPessoa,
			int cdDependente,
			int tpParentesco,
			int lgIr,
			int lgSalarioFamilia,
			int lgPensionista,
			int tpPagamento,
			int tpCalculoPensao,
			float vlAplicacao,
			String nrConta,
			int tpOperacao,
			String nrAgencia,
			int cdBanco){
		setCdPessoa(cdPessoa);
		setCdDependente(cdDependente);
		setTpParentesco(tpParentesco);
		setLgIr(lgIr);
		setLgSalarioFamilia(lgSalarioFamilia);
		setLgPensionista(lgPensionista);
		setTpPagamento(tpPagamento);
		setTpCalculoPensao(tpCalculoPensao);
		setVlAplicacao(vlAplicacao);
		setNrConta(nrConta);
		setTpOperacao(tpOperacao);
		setNrAgencia(nrAgencia);
		setCdBanco(cdBanco);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdDependente(int cdDependente){
		this.cdDependente=cdDependente;
	}
	public int getCdDependente(){
		return this.cdDependente;
	}
	public void setTpParentesco(int tpParentesco){
		this.tpParentesco=tpParentesco;
	}
	public int getTpParentesco(){
		return this.tpParentesco;
	}
	public void setLgIr(int lgIr){
		this.lgIr=lgIr;
	}
	public int getLgIr(){
		return this.lgIr;
	}
	public void setLgSalarioFamilia(int lgSalarioFamilia){
		this.lgSalarioFamilia=lgSalarioFamilia;
	}
	public int getLgSalarioFamilia(){
		return this.lgSalarioFamilia;
	}
	public void setLgPensionista(int lgPensionista){
		this.lgPensionista=lgPensionista;
	}
	public int getLgPensionista(){
		return this.lgPensionista;
	}
	public void setTpPagamento(int tpPagamento){
		this.tpPagamento=tpPagamento;
	}
	public int getTpPagamento(){
		return this.tpPagamento;
	}
	public void setTpCalculoPensao(int tpCalculoPensao){
		this.tpCalculoPensao=tpCalculoPensao;
	}
	public int getTpCalculoPensao(){
		return this.tpCalculoPensao;
	}
	public void setVlAplicacao(float vlAplicacao){
		this.vlAplicacao=vlAplicacao;
	}
	public float getVlAplicacao(){
		return this.vlAplicacao;
	}
	public void setNrConta(String nrConta){
		this.nrConta=nrConta;
	}
	public String getNrConta(){
		return this.nrConta;
	}
	public void setTpOperacao(int tpOperacao){
		this.tpOperacao=tpOperacao;
	}
	public int getTpOperacao(){
		return this.tpOperacao;
	}
	public void setNrAgencia(String nrAgencia){
		this.nrAgencia=nrAgencia;
	}
	public String getNrAgencia(){
		return this.nrAgencia;
	}
	public void setCdBanco(int cdBanco){
		this.cdBanco=cdBanco;
	}
	public int getCdBanco(){
		return this.cdBanco;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdDependente: " +  getCdDependente();
		valueToString += ", tpParentesco: " +  getTpParentesco();
		valueToString += ", lgIr: " +  getLgIr();
		valueToString += ", lgSalarioFamilia: " +  getLgSalarioFamilia();
		valueToString += ", lgPensionista: " +  getLgPensionista();
		valueToString += ", tpPagamento: " +  getTpPagamento();
		valueToString += ", tpCalculoPensao: " +  getTpCalculoPensao();
		valueToString += ", vlAplicacao: " +  getVlAplicacao();
		valueToString += ", nrConta: " +  getNrConta();
		valueToString += ", tpOperacao: " +  getTpOperacao();
		valueToString += ", nrAgencia: " +  getNrAgencia();
		valueToString += ", cdBanco: " +  getCdBanco();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Dependente(getCdPessoa(),
			getCdDependente(),
			getTpParentesco(),
			getLgIr(),
			getLgSalarioFamilia(),
			getLgPensionista(),
			getTpPagamento(),
			getTpCalculoPensao(),
			getVlAplicacao(),
			getNrConta(),
			getTpOperacao(),
			getNrAgencia(),
			getCdBanco());
	}

}
