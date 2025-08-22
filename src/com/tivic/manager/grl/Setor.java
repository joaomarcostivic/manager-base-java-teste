package com.tivic.manager.grl;

public class Setor {

	private int cdSetor;
	private int cdSetorSuperior;
	private int cdEmpresa;
	private int cdResponsavel;
	private String nmSetor;
	private int stSetor;
	private String nmBairro;
	private String nmLogradouro;
	private String nrCep;
	private String nrEndereco;
	private String nmComplemento;
	private String nrTelefone;
	private String nmPontoReferencia;
	private int lgEstoque;
	private String nrRamal;
	private String idSetor;
	private String sgSetor;
	private int tpSetor;
	private int lgRecepcao;
	private String nrSetorExterno;

	public Setor() { }

	public Setor(int cdSetor,
			int cdSetorSuperior,
			int cdEmpresa,
			int cdResponsavel,
			String nmSetor,
			int stSetor,
			String nmBairro,
			String nmLogradouro,
			String nrCep,
			String nrEndereco,
			String nmComplemento,
			String nrTelefone,
			String nmPontoReferencia,
			int lgEstoque,
			String nrRamal,
			String idSetor,
			String sgSetor,
			int tpSetor,
			int lgRecepcao,
			String nrSetorExterno){
		setCdSetor(cdSetor);
		setCdSetorSuperior(cdSetorSuperior);
		setCdEmpresa(cdEmpresa);
		setCdResponsavel(cdResponsavel);
		setNmSetor(nmSetor);
		setStSetor(stSetor);
		setNmBairro(nmBairro);
		setNmLogradouro(nmLogradouro);
		setNrCep(nrCep);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNrTelefone(nrTelefone);
		setNmPontoReferencia(nmPontoReferencia);
		setLgEstoque(lgEstoque);
		setNrRamal(nrRamal);
		setIdSetor(idSetor);
		setSgSetor(sgSetor);
		setTpSetor(tpSetor);
		setLgRecepcao(lgRecepcao);
		setNrSetorExterno(nrSetorExterno);
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdSetorSuperior(int cdSetorSuperior){
		this.cdSetorSuperior=cdSetorSuperior;
	}
	public int getCdSetorSuperior(){
		return this.cdSetorSuperior;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setNmSetor(String nmSetor){
		this.nmSetor=nmSetor;
	}
	public String getNmSetor(){
		return this.nmSetor;
	}
	public void setStSetor(int stSetor){
		this.stSetor=stSetor;
	}
	public int getStSetor(){
		return this.stSetor;
	}
	public void setNmBairro(String nmBairro){
		this.nmBairro=nmBairro;
	}
	public String getNmBairro(){
		return this.nmBairro;
	}
	public void setNmLogradouro(String nmLogradouro){
		this.nmLogradouro=nmLogradouro;
	}
	public String getNmLogradouro(){
		return this.nmLogradouro;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setNrEndereco(String nrEndereco){
		this.nrEndereco=nrEndereco;
	}
	public String getNrEndereco(){
		return this.nrEndereco;
	}
	public void setNmComplemento(String nmComplemento){
		this.nmComplemento=nmComplemento;
	}
	public String getNmComplemento(){
		return this.nmComplemento;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setNmPontoReferencia(String nmPontoReferencia){
		this.nmPontoReferencia=nmPontoReferencia;
	}
	public String getNmPontoReferencia(){
		return this.nmPontoReferencia;
	}
	public void setLgEstoque(int lgEstoque){
		this.lgEstoque=lgEstoque;
	}
	public int getLgEstoque(){
		return this.lgEstoque;
	}
	public void setNrRamal(String nrRamal){
		this.nrRamal=nrRamal;
	}
	public String getNrRamal(){
		return this.nrRamal;
	}
	public void setIdSetor(String idSetor){
		this.idSetor=idSetor;
	}
	public String getIdSetor(){
		return this.idSetor;
	}
	public void setSgSetor(String sgSetor){
		this.sgSetor = sgSetor;
	}
	public String getSgSetor(){
		return this.sgSetor;
	}
	public void setTpSetor(int tpSetor){
		this.tpSetor = tpSetor;
	}
	public int getTpSetor(){
		return this.tpSetor;
	}
	public void setLgRecepcao(int lgRecepcao){
		this.lgRecepcao=lgRecepcao;
	}
	public int getLgRecepcao(){
		return this.lgRecepcao;
	}
	public void setNrSetorExterno(String nrSetorExterno){
		this.nrSetorExterno=nrSetorExterno;
	}
	public String getNrSetorExterno(){
		return this.nrSetorExterno;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdSetor: " +  getCdSetor();
		valueToString += ", cdSetorSuperior: " +  getCdSetorSuperior();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", nmSetor: " +  getNmSetor();
		valueToString += ", stSetor: " +  getStSetor();
		valueToString += ", nmBairro: " +  getNmBairro();
		valueToString += ", nmLogradouro: " +  getNmLogradouro();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", nrEndereco: " +  getNrEndereco();
		valueToString += ", nmComplemento: " +  getNmComplemento();
		valueToString += ", nrTelefone: " +  getNrTelefone();
		valueToString += ", nmPontoReferencia: " +  getNmPontoReferencia();
		valueToString += ", lgEstoque: " +  getLgEstoque();
		valueToString += ", nrRamal: " +  getNrRamal();
		valueToString += ", idSetor: " +  getIdSetor();
		valueToString += ", sgSetor: " + getSgSetor();
		valueToString += ", tpSetor: " + getTpSetor();
		valueToString += ", lgRecepcao: " + getLgRecepcao();
		valueToString += ", nrSetorExterno: " + getNrSetorExterno();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Setor(getCdSetor(),
			getCdSetorSuperior(),
			getCdEmpresa(),
			getCdResponsavel(),
			getNmSetor(),
			getStSetor(),
			getNmBairro(),
			getNmLogradouro(),
			getNrCep(),
			getNrEndereco(),
			getNmComplemento(),
			getNrTelefone(),
			getNmPontoReferencia(),
			getLgEstoque(),
			getNrRamal(),
			getIdSetor(),
			getSgSetor(),
			getTpSetor(),
			getLgRecepcao(),
			getNrSetorExterno());
	}

}
