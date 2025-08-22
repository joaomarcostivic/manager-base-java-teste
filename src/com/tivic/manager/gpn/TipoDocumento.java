package com.tivic.manager.gpn;

public class TipoDocumento {

	private int cdTipoDocumento;
	private String nmTipoDocumento;
	private String idTipoDocumento;
	private int stTipoDocumento;
	private int cdEmpresa;
	private int cdSetor;
	private int cdFormulario;
	private int tpNumeracao;
	private String idPrefixoNumeracao;
	private String dsMascaraNumeracao;
	private int nrUltimaNumeracao;
	private int cdTipoDocumentoSuperior;
	private int lgNumeracaoSuperior;
	private String nrExterno;

	public TipoDocumento() { }

	public TipoDocumento(int cdTipoDocumento,
			String nmTipoDocumento,
			String idTipoDocumento,
			int stTipoDocumento,
			int cdEmpresa,
			int cdSetor,
			int cdFormulario,
			int tpNumeracao,
			String idPrefixoNumeracao,
			String dsMascaraNumeracao,
			int nrUltimaNumeracao,
			int cdTipoDocumentoSuperior,
			int lgNumeracaoSuperior,
			String nrExterno) {
		setCdTipoDocumento(cdTipoDocumento);
		setNmTipoDocumento(nmTipoDocumento);
		setIdTipoDocumento(idTipoDocumento);
		setStTipoDocumento(stTipoDocumento);
		setCdEmpresa(cdEmpresa);
		setCdSetor(cdSetor);
		setCdFormulario(cdFormulario);
		setTpNumeracao(tpNumeracao);
		setIdPrefixoNumeracao(idPrefixoNumeracao);
		setDsMascaraNumeracao(dsMascaraNumeracao);
		setNrUltimaNumeracao(nrUltimaNumeracao);
		setCdTipoDocumentoSuperior(cdTipoDocumentoSuperior);
		setLgNumeracaoSuperior(lgNumeracaoSuperior);
		setNrExterno(nrExterno);
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setNmTipoDocumento(String nmTipoDocumento){
		this.nmTipoDocumento=nmTipoDocumento;
	}
	public String getNmTipoDocumento(){
		return this.nmTipoDocumento;
	}
	public void setIdTipoDocumento(String idTipoDocumento){
		this.idTipoDocumento=idTipoDocumento;
	}
	public String getIdTipoDocumento(){
		return this.idTipoDocumento;
	}
	public void setStTipoDocumento(int stTipoDocumento){
		this.stTipoDocumento=stTipoDocumento;
	}
	public int getStTipoDocumento(){
		return this.stTipoDocumento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setTpNumeracao(int tpNumeracao){
		this.tpNumeracao=tpNumeracao;
	}
	public int getTpNumeracao(){
		return this.tpNumeracao;
	}
	public void setIdPrefixoNumeracao(String idPrefixoNumeracao){
		this.idPrefixoNumeracao=idPrefixoNumeracao;
	}
	public String getIdPrefixoNumeracao(){
		return this.idPrefixoNumeracao;
	}
	public void setDsMascaraNumeracao(String dsMascaraNumeracao){
		this.dsMascaraNumeracao=dsMascaraNumeracao;
	}
	public String getDsMascaraNumeracao(){
		return this.dsMascaraNumeracao;
	}
	public void setNrUltimaNumeracao(int nrUltimaNumeracao){
		this.nrUltimaNumeracao=nrUltimaNumeracao;
	}
	public int getNrUltimaNumeracao(){
		return this.nrUltimaNumeracao;
	}
	public void setCdTipoDocumentoSuperior(int cdTipoDocumentoSuperior){
		this.cdTipoDocumentoSuperior=cdTipoDocumentoSuperior;
	}
	public int getCdTipoDocumentoSuperior(){
		return this.cdTipoDocumentoSuperior;
	}
	public void setLgNumeracaoSuperior(int lgNumeracaoSuperior){
		this.lgNumeracaoSuperior=lgNumeracaoSuperior;
	}
	public int getLgNumeracaoSuperior(){
		return this.lgNumeracaoSuperior;
	}
	public void setNrExterno(String nrExterno){
		this.nrExterno=nrExterno;
	}
	public String getNrExterno(){
		return this.nrExterno;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", nmTipoDocumento: " +  getNmTipoDocumento();
		valueToString += ", idTipoDocumento: " +  getIdTipoDocumento();
		valueToString += ", stTipoDocumento: " +  getStTipoDocumento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", tpNumeracao: " +  getTpNumeracao();
		valueToString += ", idPrefixoNumeracao: " +  getIdPrefixoNumeracao();
		valueToString += ", dsMascaraNumeracao: " +  getDsMascaraNumeracao();
		valueToString += ", nrUltimaNumeracao: " +  getNrUltimaNumeracao();
		valueToString += ", cdTipoDocumentoSuperior: " +  getCdTipoDocumentoSuperior();
		valueToString += ", lgNumeracaoSuperior: " +  getLgNumeracaoSuperior();
		valueToString += ", nrExterno: " +  getNrExterno();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumento(getCdTipoDocumento(),
			getNmTipoDocumento(),
			getIdTipoDocumento(),
			getStTipoDocumento(),
			getCdEmpresa(),
			getCdSetor(),
			getCdFormulario(),
			getTpNumeracao(),
			getIdPrefixoNumeracao(),
			getDsMascaraNumeracao(),
			getNrUltimaNumeracao(),
			getCdTipoDocumentoSuperior(),
			getLgNumeracaoSuperior(),
			getNrExterno());
	}

}