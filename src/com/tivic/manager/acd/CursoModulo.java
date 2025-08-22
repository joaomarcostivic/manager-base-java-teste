package com.tivic.manager.acd;

import com.tivic.manager.grl.ProdutoServico;

public class CursoModulo extends ProdutoServico{

	private int cdCursoModulo;
	private int cdCurso;
	private int nrOrdem;

	public CursoModulo(){ }

	public CursoModulo(int cdProdutoServico,
			int cdCategoriaEconomica,
			String nmProdutoServico,
			String txtProdutoServico,
			String txtEspecificacao,
			String txtDadoTecnico,
			String txtPrazoEntrega,
			int tpProdutoServico,
			String idProdutoServico,
			String sgProdutoServico,
			int cdClassificacaoFiscal,
			int cdFabricante,
			int cdMarca,
			String nmModelo,
			int cdNcm,
			String nrReferencia,
			int cdCategoriaReceita,
			int cdCategoriaDespesa,
			int cdCurso,
			int nrOrdem){
		super(cdProdutoServico,
				cdCategoriaEconomica,
				nmProdutoServico,
				txtProdutoServico,
				txtEspecificacao,
				txtDadoTecnico,
				txtPrazoEntrega,
				tpProdutoServico,
				idProdutoServico,
				sgProdutoServico,
				cdClassificacaoFiscal,
				cdFabricante,
				cdMarca,
				nmModelo);
		setCdCursoModulo(cdProdutoServico);
		setCdCurso(cdCurso);
		setNrOrdem(nrOrdem);
	}
	public void setCdCursoModulo(int cdCursoModulo){
		this.cdCursoModulo=cdCursoModulo;
	}
	public int getCdCursoModulo(){
		return this.cdCursoModulo;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCursoModulo: " +  getCdCursoModulo();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoModulo(getCdProdutoServico(),
			getCdCategoriaEconomica(),
			getNmProdutoServico(),
			getTxtProdutoServico(),
			getTxtEspecificacao(),
			getTxtDadoTecnico(),
			getTxtPrazoEntrega(),
			getTpProdutoServico(),
			getIdProdutoServico(),
			getSgProdutoServico(),
			getCdClassificacaoFiscal(),
			getCdFabricante(),
			getCdMarca(),
			getNmModelo(),
			getCdNcm(),
			getNrReferencia(),
			getCdCategoriaReceita(),
			getCdCategoriaDespesa(),
			getCdCurso(),
			getNrOrdem());
	}
}
