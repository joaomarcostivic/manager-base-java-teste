package com.tivic.manager.acd;

import java.util.Map;

import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;

public class Disciplina extends com.tivic.manager.grl.ProdutoServico {

	private int cdDisciplina;
	private int cdDisciplinaTeoria;
	private int gnDisciplina;
	private int cdAreaConhecimento;
	private int tpClassificacao;
	
	public Disciplina(){ }

	public Disciplina(int cdProdutoServico,
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
			double vlServico,
			int cdDisciplinaTeoria,
			int gnDisciplina,
			int cdAreaConhecimento,
			int tpClassificacao){
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
			nmModelo,
			cdNcm,
			nrReferencia,
			cdCategoriaReceita,
			cdCategoriaDespesa,
			vlServico);
		setCdDisciplina(cdProdutoServico);
		setCdDisciplinaTeoria(cdDisciplinaTeoria);
		setGnDisciplina(gnDisciplina);
		setCdAreaConhecimento(cdAreaConhecimento);
		setTpClassificacao(tpClassificacao);
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setCdDisciplinaTeoria(int cdDisciplinaTeoria){
		this.cdDisciplinaTeoria=cdDisciplinaTeoria;
	}
	public int getCdDisciplinaTeoria(){
		return this.cdDisciplinaTeoria;
	}
	public void setGnDisciplina(int gnDisciplina){
		this.gnDisciplina=gnDisciplina;
	}
	public int getGnDisciplina(){
		return this.gnDisciplina;
	}
	public void setCdAreaConhecimento(int cdAreaConhecimento){
		this.cdAreaConhecimento=cdAreaConhecimento;
	}
	public int getCdAreaConhecimento(){
		return this.cdAreaConhecimento;
	}
	public void setTpClassificacao(int tpClassificacao){
		this.tpClassificacao=tpClassificacao;
	}
	public int getTpClassificacao(){
		return this.tpClassificacao;
	}
	public String toString() {
		String valueToString = super.toString().replaceAll("\\{", "").replaceAll("\\}", "");
		valueToString += ", \"cdDisciplina\": " +  getCdDisciplina();
		valueToString += ", \"cdDisciplinaTeoria\": " +  getCdDisciplinaTeoria();
		valueToString += ", \"gnDisciplina\": " +  getGnDisciplina();
		valueToString += ", \"cdAreaConhecimento\": " +  getCdAreaConhecimento();
		valueToString += ", \"tpClassificacao\": " +  getTpClassificacao();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdDisciplina\": " +  getCdDisciplina();
		valueToString += ", \"cdDisciplinaTeoria\": " +  getCdDisciplinaTeoria();
		valueToString += ", \"gnDisciplina\": " +  getGnDisciplina();
		valueToString += ", \"cdAreaConhecimento\": " +  getCdAreaConhecimento();
		valueToString += ", \"tpClassificacao\": " +  getTpClassificacao();
		valueToString += ", \"produtoServico\": " + ProdutoServicoDAO.get(getCdProdutoServico()).toORM();
		return "{" + valueToString + "}";
	}
	
	public static String fromRegister(Map<String, Object> register) {
		String valueToString = "";
		valueToString += "\"cdDisciplina\": " +  register.get("CD_DISCIPLINA");
		valueToString += ", \"cdDisciplinaTeoria\": " +  register.get("CD_DISCIPLINA_TEORIA");
		valueToString += ", \"gnDisciplina\": " +  register.get("GN_DISCIPLINA");
		valueToString += ", \"cdAreaConhecimento\": " + register.get("CD_AREA_CONHECIMENTO");
		valueToString += ", \"tpClassificacao\": " + register.get("TP_CLASSIFICACAO");
		valueToString += ", \"produtoServico\": " + ProdutoServico.fromRegister(register);
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Disciplina(getCdProdutoServico(),
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
			getVlServico(),
			getCdDisciplinaTeoria(),
			getGnDisciplina(),
			getCdAreaConhecimento(),
			getTpClassificacao());
	}

}