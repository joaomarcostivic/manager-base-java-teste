package com.tivic.manager.mob;

public class AitCondutor {

	private int cdAitCondutor;
	private int cdAit;
	private int tpDocumento;
	private String nrDocumento;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String ufCnhCondutor;
	private String dsEnderecoCondutor;
	private int tpCnhCondutor;
	private String dsBairroCondutor;
	private String nrImovelCondutor;
	private String dsComplementoCondutor;
	private int cdCidadeCondutor;
	private String nrCepCondutor;
	private String nrCpfCondutor;
	private int cdCondutor;
	private int cdEnderecoCondutor;
	private String nmCondutorAutuacao;
	private String nrCnhAutuacao;
	private String nrDocumentoAutuacao;
	private int cdPessoa;

	public AitCondutor(){ }

	public AitCondutor(int cdAitCondutor,
			int cdAit,
			int tpDocumento,
			String nrDocumento,
			String nmCondutor,
			String nrCnhCondutor,
			String ufCnhCondutor,
			String dsEnderecoCondutor,
			int tpCnhCondutor,
			String dsBairroCondutor,
			String nrImovelCondutor,
			String dsComplementoCondutor,
			int cdCidadeCondutor,
			String nrCepCondutor,
			String nrCpfCondutor,
			int cdCondutor,
			int cdEnderecoCondutor,
			String nmCondutorAutuacao,
			String nrCnhAutuacao,
			String nrDocumentoAutuacao,
			int cdPessoa){
		setCdAitCondutor(cdAitCondutor);
		setCdAit(cdAit);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setUfCnhCondutor(ufCnhCondutor);
		setDsEnderecoCondutor(dsEnderecoCondutor);
		setTpCnhCondutor(tpCnhCondutor);
		setDsBairroCondutor(dsBairroCondutor);
		setNrImovelCondutor(nrImovelCondutor);
		setDsComplementoCondutor(dsComplementoCondutor);
		setCdCidadeCondutor(cdCidadeCondutor);
		setNrCepCondutor(nrCepCondutor);
		setNrCpfCondutor(nrCpfCondutor);
		setCdCondutor(cdCondutor);
		setCdEnderecoCondutor(cdEnderecoCondutor);
		setNmCondutorAutuacao(nmCondutorAutuacao);
		setNrCnhAutuacao(nrCnhAutuacao);
		setNrDocumentoAutuacao(nrDocumentoAutuacao);
		setCdPessoa(cdPessoa);
	}
	public void setCdAitCondutor(int cdAitCondutor){
		this.cdAitCondutor=cdAitCondutor;
	}
	public int getCdAitCondutor(){
		return this.cdAitCondutor;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setTpDocumento(int tpDocumento){
		this.tpDocumento=tpDocumento;
	}
	public int getTpDocumento(){
		return this.tpDocumento;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setNmCondutor(String nmCondutor){
		this.nmCondutor=nmCondutor;
	}
	public String getNmCondutor(){
		return this.nmCondutor;
	}
	public void setNrCnhCondutor(String nrCnhCondutor){
		this.nrCnhCondutor=nrCnhCondutor;
	}
	public String getNrCnhCondutor(){
		return this.nrCnhCondutor;
	}
	public void setUfCnhCondutor(String ufCnhCondutor){
		this.ufCnhCondutor=ufCnhCondutor;
	}
	public String getUfCnhCondutor(){
		return this.ufCnhCondutor;
	}
	public void setDsEnderecoCondutor(String dsEnderecoCondutor){
		this.dsEnderecoCondutor=dsEnderecoCondutor;
	}
	public String getDsEnderecoCondutor(){
		return this.dsEnderecoCondutor;
	}
	public void setTpCnhCondutor(int tpCnhCondutor){
		this.tpCnhCondutor=tpCnhCondutor;
	}
	public int getTpCnhCondutor(){
		return this.tpCnhCondutor;
	}
	public void setDsBairroCondutor(String dsBairroCondutor){
		this.dsBairroCondutor=dsBairroCondutor;
	}
	public String getDsBairroCondutor(){
		return this.dsBairroCondutor;
	}
	public void setNrImovelCondutor(String nrImovelCondutor){
		this.nrImovelCondutor=nrImovelCondutor;
	}
	public String getNrImovelCondutor(){
		return this.nrImovelCondutor;
	}
	public void setDsComplementoCondutor(String dsComplementoCondutor){
		this.dsComplementoCondutor=dsComplementoCondutor;
	}
	public String getDsComplementoCondutor(){
		return this.dsComplementoCondutor;
	}
	public void setCdCidadeCondutor(int cdCidadeCondutor){
		this.cdCidadeCondutor=cdCidadeCondutor;
	}
	public int getCdCidadeCondutor(){
		return this.cdCidadeCondutor;
	}
	public void setNrCepCondutor(String nrCepCondutor){
		this.nrCepCondutor=nrCepCondutor;
	}
	public String getNrCepCondutor(){
		return this.nrCepCondutor;
	}
	public void setNrCpfCondutor(String nrCpfCondutor){
		this.nrCpfCondutor=nrCpfCondutor;
	}
	public String getNrCpfCondutor(){
		return this.nrCpfCondutor;
	}
	public void setCdCondutor(int cdCondutor){
		this.cdCondutor=cdCondutor;
	}
	public int getCdCondutor(){
		return this.cdCondutor;
	}
	public void setCdEnderecoCondutor(int cdEnderecoCondutor){
		this.cdEnderecoCondutor=cdEnderecoCondutor;
	}
	public int getCdEnderecoCondutor(){
		return this.cdEnderecoCondutor;
	}
	public void setNmCondutorAutuacao(String nmCondutorAutuacao){
		this.nmCondutorAutuacao=nmCondutorAutuacao;
	}
	public String getNmCondutorAutuacao(){
		return this.nmCondutorAutuacao;
	}
	public void setNrCnhAutuacao(String nrCnhAutuacao){
		this.nrCnhAutuacao=nrCnhAutuacao;
	}
	public String getNrCnhAutuacao(){
		return this.nrCnhAutuacao;
	}
	public void setNrDocumentoAutuacao(String nrDocumentoAutuacao){
		this.nrDocumentoAutuacao=nrDocumentoAutuacao;
	}
	public String getNrDocumentoAutuacao(){
		return this.nrDocumentoAutuacao;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAitCondutor: " +  getCdAitCondutor();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", tpDocumento: " +  getTpDocumento();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", nmCondutor: " +  getNmCondutor();
		valueToString += ", nrCnhCondutor: " +  getNrCnhCondutor();
		valueToString += ", ufCnhCondutor: " +  getUfCnhCondutor();
		valueToString += ", dsEnderecoCondutor: " +  getDsEnderecoCondutor();
		valueToString += ", tpCnhCondutor: " +  getTpCnhCondutor();
		valueToString += ", dsBairroCondutor: " +  getDsBairroCondutor();
		valueToString += ", nrImovelCondutor: " +  getNrImovelCondutor();
		valueToString += ", dsComplementoCondutor: " +  getDsComplementoCondutor();
		valueToString += ", cdCidadeCondutor: " +  getCdCidadeCondutor();
		valueToString += ", nrCepCondutor: " +  getNrCepCondutor();
		valueToString += ", nrCpfCondutor: " +  getNrCpfCondutor();
		valueToString += ", cdCondutor: " +  getCdCondutor();
		valueToString += ", cdEnderecoCondutor: " +  getCdEnderecoCondutor();
		valueToString += ", nmCondutorAutuacao: " +  getNmCondutorAutuacao();
		valueToString += ", nrCnhAutuacao: " +  getNrCnhAutuacao();
		valueToString += ", nrDocumentoAutuacao: " +  getNrDocumentoAutuacao();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitCondutor(getCdAitCondutor(),
			getCdAit(),
			getTpDocumento(),
			getNrDocumento(),
			getNmCondutor(),
			getNrCnhCondutor(),
			getUfCnhCondutor(),
			getDsEnderecoCondutor(),
			getTpCnhCondutor(),
			getDsBairroCondutor(),
			getNrImovelCondutor(),
			getDsComplementoCondutor(),
			getCdCidadeCondutor(),
			getNrCepCondutor(),
			getNrCpfCondutor(),
			getCdCondutor(),
			getCdEnderecoCondutor(),
			getNmCondutorAutuacao(),
			getNrCnhAutuacao(),
			getNrDocumentoAutuacao(),
			getCdPessoa());
	}

}
