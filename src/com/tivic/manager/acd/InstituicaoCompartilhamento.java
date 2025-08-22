package com.tivic.manager.acd;

public class InstituicaoCompartilhamento {

	private int cdInstituicao;
	private String nrInstituicaoCompartilha;

	public InstituicaoCompartilhamento(){ }

	public InstituicaoCompartilhamento(int cdInstituicao,
			String nrInstituicaoCompartilha){
		setCdInstituicao(cdInstituicao);
		setNrInstituicaoCompartilha(nrInstituicaoCompartilha);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setNrInstituicaoCompartilha(String nrInstituicaoCompartilha){
		this.nrInstituicaoCompartilha=nrInstituicaoCompartilha;
	}
	public String getNrInstituicaoCompartilha(){
		return this.nrInstituicaoCompartilha;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", nrInstituicaoCompartilha: " +  getNrInstituicaoCompartilha();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoCompartilhamento(getCdInstituicao(),
			getNrInstituicaoCompartilha());
	}

}