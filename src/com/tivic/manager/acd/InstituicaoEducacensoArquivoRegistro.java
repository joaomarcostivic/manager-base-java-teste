package com.tivic.manager.acd;

public class InstituicaoEducacensoArquivoRegistro {

	private int cdInstituicao;
	private int cdPeriodoLetivo;
	private int cdRegistro;
	private String tpRegistro;
	private String txtRegistro;

	public InstituicaoEducacensoArquivoRegistro() { }

	public InstituicaoEducacensoArquivoRegistro(int cdInstituicao,
			int cdPeriodoLetivo,
			int cdRegistro,
			String tpRegistro,
			String txtRegistro) {
		setCdInstituicao(cdInstituicao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setCdRegistro(cdRegistro);
		setTpRegistro(tpRegistro);
		setTxtRegistro(txtRegistro);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setCdRegistro(int cdRegistro){
		this.cdRegistro=cdRegistro;
	}
	public int getCdRegistro(){
		return this.cdRegistro;
	}
	public void setTpRegistro(String tpRegistro){
		this.tpRegistro=tpRegistro;
	}
	public String getTpRegistro(){
		return this.tpRegistro;
	}
	public void setTxtRegistro(String txtRegistro){
		this.txtRegistro=txtRegistro;
	}
	public String getTxtRegistro(){
		return this.txtRegistro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", cdRegistro: " +  getCdRegistro();
		valueToString += ", tpRegistro: " +  getTpRegistro();
		valueToString += ", txtRegistro: " +  getTxtRegistro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoEducacensoArquivoRegistro(getCdInstituicao(),
			getCdPeriodoLetivo(),
			getCdRegistro(),
			getTpRegistro(),
			getTxtRegistro());
	}

}