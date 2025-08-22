package com.tivic.manager.mob;

import com.tivic.manager.grl.Pessoa;

public class BoatVitima {

	private int cdBoat;
	private int cdVeiculo;
	private int cdPessoa;
	private int tpClassificacao;
	private int tpNaturezaFerimento;
	private String txtObservacao;
	private String nmConducao;
	private int cdBoatVeiculo;
	
	private int qtIdade;
	private String nrRg;
	private String sgOrgaoRg;
	private String txtEndereco;
	private String txtAtendente;
	private int tpSexo;
	
	private Pessoa pessoa;
	
	public BoatVitima() { }

	public BoatVitima(int cdBoat,
			int cdVeiculo,
			int cdPessoa,
			int tpClassificacao,
			int tpNaturezaFerimento,
			String txtObservacao,
			String nmConducao,
			int cdBoatVeiculo,
			int qtIdade,
			String nrRg,
			String sgOrgaoRg,
			String txtEndereco,
			String txtAtendente,
			int tpSexo) {
		setCdBoat(cdBoat);
		setCdVeiculo(cdVeiculo);
		setCdPessoa(cdPessoa);
		setTpClassificacao(tpClassificacao);
		setTpNaturezaFerimento(tpNaturezaFerimento);
		setTxtObservacao(txtObservacao);
		setNmConducao(nmConducao);
		setCdBoatVeiculo(cdBoatVeiculo);
		
		setQtIdade(qtIdade);
		setNrRg(nrRg);
		setSgOrgaoRg(sgOrgaoRg);
		setTxtEndereco(txtEndereco);
		setTxtAtendente(txtAtendente);
		setTpSexo(tpSexo);
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpClassificacao(int tpClassificacao){
		this.tpClassificacao=tpClassificacao;
	}
	public int getTpClassificacao(){
		return this.tpClassificacao;
	}
	public void setTpNaturezaFerimento(int tpNaturezaFerimento){
		this.tpNaturezaFerimento=tpNaturezaFerimento;
	}
	public int getTpNaturezaFerimento(){
		return this.tpNaturezaFerimento;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNmConducao(String nmConducao){
		this.nmConducao=nmConducao;
	}
	public String getNmConducao(){
		return this.nmConducao;
	}
	public void setCdBoatVeiculo(int cdBoatVeiculo){
		this.cdBoatVeiculo=cdBoatVeiculo;
	}
	public int getCdBoatVeiculo(){
		return this.cdBoatVeiculo;
	}
	
	
	public void setQtIdade(int qtIdade){
		this.qtIdade=qtIdade;
	}
	public int getQtIdade(){
		return this.qtIdade;
	}
	public void setNrRg(String nrRg){
		this.nrRg=nrRg;
	}
	public String getNrRg(){
		return this.nrRg;
	}
	public void setSgOrgaoRg(String sgOrgaoRg){
		this.sgOrgaoRg=sgOrgaoRg;
	}
	public String getSgOrgaoRg(){
		return this.sgOrgaoRg;
	}
	public void setTxtEndereco(String txtEndereco){
		this.txtEndereco=txtEndereco;
	}
	public String getTxtEndereco(){
		return this.txtEndereco;
	}
	public void setTxtAtendente(String txtAtendente){
		this.txtAtendente=txtAtendente;
	}
	public String getTxtAtendente(){
		return this.txtAtendente;
	}
	public void setTpSexo(int tpSexo){
		this.tpSexo=tpSexo;
	}
	public int getTpSexo(){
		return this.tpSexo;
	}
	
	public void setPessoa(Pessoa pessoa){
		this.pessoa=pessoa;
	}
	public Pessoa getPessoa(){
		return this.pessoa;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdBoat: " +  getCdBoat();
		valueToString += "cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpClassificacao: " +  getTpClassificacao();
		valueToString += ", tpNaturezaFerimento: " +  getTpNaturezaFerimento();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nmConducao: " +  getNmConducao();
		valueToString += ", cdBoatVeiculo: " +  getCdBoatVeiculo();

		valueToString += ", qtIdade: " +  getQtIdade();
		valueToString += ", nrRg: " +  getNrRg();
		valueToString += ", sgOrgaoRg: " +  getSgOrgaoRg();
		valueToString += ", txtEndereco: " +  getTxtEndereco();
		valueToString += ", txtAtendente: " +  getTxtAtendente();
		valueToString += ", tpSexo: " +  getTpSexo();
		
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatVitima(getCdBoat(),
				getCdVeiculo(),
			getCdPessoa(),
			getTpClassificacao(),
			getTpNaturezaFerimento(),
			getTxtObservacao(),
			getNmConducao(),
			getCdBoatVeiculo(),
			getQtIdade(),
			getNrRg(),
			getSgOrgaoRg(),
			getTxtEndereco(),
			getTxtAtendente(),
			getTpSexo());
	}

	public int getCdVeiculo() {
		return cdVeiculo;
	}

	public void setCdVeiculo(int cdVeiculo) {
		this.cdVeiculo = cdVeiculo;
	}

}