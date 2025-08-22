package com.tivic.manager.adm;

public class ProdutoServicoDesconto {

	private int cdTipoDesconto;
	private int cdEmpresa;
	private int cdFaixaDesconto;
	private int cdDesconto;
	private int cdProdutoServico;
	private int cdGrupo;

	public ProdutoServicoDesconto(int cdTipoDesconto,
			int cdEmpresa,
			int cdFaixaDesconto,
			int cdDesconto,
			int cdProdutoServico,
			int cdGrupo){
		setCdTipoDesconto(cdTipoDesconto);
		setCdEmpresa(cdEmpresa);
		setCdFaixaDesconto(cdFaixaDesconto);
		setCdDesconto(cdDesconto);
		setCdProdutoServico(cdProdutoServico);
		setCdGrupo(cdGrupo);
	}
	public void setCdTipoDesconto(int cdTipoDesconto){
		this.cdTipoDesconto=cdTipoDesconto;
	}
	public int getCdTipoDesconto(){
		return this.cdTipoDesconto;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdFaixaDesconto(int cdFaixaDesconto){
		this.cdFaixaDesconto=cdFaixaDesconto;
	}
	public int getCdFaixaDesconto(){
		return this.cdFaixaDesconto;
	}
	public void setCdDesconto(int cdDesconto){
		this.cdDesconto=cdDesconto;
	}
	public int getCdDesconto(){
		return this.cdDesconto;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDesconto: " +  getCdTipoDesconto();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdFaixaDesconto: " +  getCdFaixaDesconto();
		valueToString += ", cdDesconto: " +  getCdDesconto();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoServicoDesconto(getCdTipoDesconto(),
			getCdEmpresa(),
			getCdFaixaDesconto(),
			getCdDesconto(),
			getCdProdutoServico(),
			getCdGrupo());
	}

}
