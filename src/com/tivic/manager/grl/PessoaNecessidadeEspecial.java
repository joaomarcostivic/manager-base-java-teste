package com.tivic.manager.grl;

public class PessoaNecessidadeEspecial {

	private int cdPessoa;
	private int cdTipoNecessidadeEspecial;

	public PessoaNecessidadeEspecial(){ }

	public PessoaNecessidadeEspecial(int cdPessoa,
			int cdTipoNecessidadeEspecial){
		setCdPessoa(cdPessoa);
		setCdTipoNecessidadeEspecial(cdTipoNecessidadeEspecial);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdTipoNecessidadeEspecial(int cdTipoNecessidadeEspecial){
		this.cdTipoNecessidadeEspecial=cdTipoNecessidadeEspecial;
	}
	public int getCdTipoNecessidadeEspecial(){
		return this.cdTipoNecessidadeEspecial;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdTipoNecessidadeEspecial: " +  getCdTipoNecessidadeEspecial();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaNecessidadeEspecial(getCdPessoa(),
			getCdTipoNecessidadeEspecial());
	}

}