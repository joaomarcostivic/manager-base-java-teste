package com.tivic.manager.alm;

public class ProdutoGrupo {

	private int cdProdutoServico;
	private int cdGrupo;
	private int cdEmpresa;
	private int lgPrincipal;

	public ProdutoGrupo(int cdProdutoServico,
			int cdGrupo,
			int cdEmpresa,
			int lgPrincipal){
		setCdProdutoServico(cdProdutoServico);
		setCdGrupo(cdGrupo);
		setCdEmpresa(cdEmpresa);
		setLgPrincipal(lgPrincipal);
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setLgPrincipal(int lgPrincipal){
		this.lgPrincipal=lgPrincipal;
	}
	public int getLgPrincipal(){
		return this.lgPrincipal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", lgPrincipal: " +  getLgPrincipal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoGrupo(getCdProdutoServico(),
			getCdGrupo(),
			getCdEmpresa(),
			getLgPrincipal());
	}

}
