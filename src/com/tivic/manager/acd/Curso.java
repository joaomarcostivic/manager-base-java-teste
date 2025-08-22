package com.tivic.manager.acd;

import java.util.HashMap;
import java.util.Map;

import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;

public class Curso extends ProdutoServico {

	private int cdCurso;
	private String nmHabilitacao;
	private String nmDuracao;
	private String txtCompetencia;
	private String txtCurso;
	private int cdTipoPeriodo;
	private int tpContratacao;
	private int tpModalidadeEnsino;
	private int tpEtapaEnsino;
	private int tpPresenca;
	private int nrOrdem;
	private int lgMulti;
	private int lgReferencia;
	private int nrIdade;
	private int tpAvaliacao;
	
	public Curso(){ }

	public Curso(int cdProdutoServico,
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
			String nmHabilitacao,
			String nmDuracao,
			String txtCompetencia,
			String txtCurso,
			int cdTipoPeriodo,
			int tpContratacao,
			int tpModalidadeEnsino,
			int tpEtapaEnsino,
			int tpPresenca,
			int nrOrdem,
			int lgMulti,
			int lgReferencia,
			int nrIdade,
			int tpAvaliacao){
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
		setCdCurso(cdProdutoServico);
		setNmHabilitacao(nmHabilitacao);
		setNmDuracao(nmDuracao);
		setTxtCompetencia(txtCompetencia);
		setTxtCurso(txtCurso);
		setCdTipoPeriodo(cdTipoPeriodo);
		setTpContratacao(tpContratacao);
		setTpModalidadeEnsino(tpModalidadeEnsino);
		setTpEtapaEnsino(tpEtapaEnsino);
		setTpPresenca(tpPresenca);
		setNrOrdem(nrOrdem);
		setLgMulti(lgMulti);
		setLgReferencia(lgReferencia);
		setNrIdade(nrIdade);
		setTpAvaliacao(tpAvaliacao);
	}
	
	public Curso(int cdProdutoServico,
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
			String nmHabilitacao,
			String nmDuracao,
			String txtCompetencia,
			String txtCurso,
			int cdTipoPeriodo,
			int tpContratacao,
			int tpModalidadeEnsino,
			int tpEtapaEnsino,
			int tpPresenca,
			int nrOrdem,
			int lgMulti,
			int lgReferencia){
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
		setCdCurso(cdProdutoServico);
		setNmHabilitacao(nmHabilitacao);
		setNmDuracao(nmDuracao);
		setTxtCompetencia(txtCompetencia);
		setTxtCurso(txtCurso);
		setCdTipoPeriodo(cdTipoPeriodo);
		setTpContratacao(tpContratacao);
		setTpModalidadeEnsino(tpModalidadeEnsino);
		setTpEtapaEnsino(tpEtapaEnsino);
		setTpPresenca(tpPresenca);
		setNrOrdem(nrOrdem);
		setLgMulti(lgMulti);
		setLgReferencia(lgReferencia);
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setNmHabilitacao(String nmHabilitacao){
		this.nmHabilitacao=nmHabilitacao;
	}
	public String getNmHabilitacao(){
		return this.nmHabilitacao;
	}
	public void setNmDuracao(String nmDuracao){
		this.nmDuracao=nmDuracao;
	}
	public String getNmDuracao(){
		return this.nmDuracao;
	}
	public void setTxtCompetencia(String txtCompetencia){
		this.txtCompetencia=txtCompetencia;
	}
	public String getTxtCompetencia(){
		return this.txtCompetencia;
	}
	public void setTxtCurso(String txtCurso){
		this.txtCurso=txtCurso;
	}
	public String getTxtCurso(){
		return this.txtCurso;
	}
	public void setCdTipoPeriodo(int cdTipoPeriodo){
		this.cdTipoPeriodo=cdTipoPeriodo;
	}
	public int getCdTipoPeriodo(){
		return this.cdTipoPeriodo;
	}
	public void setTpContratacao(int tpContratacao){
		this.tpContratacao=tpContratacao;
	}
	public int getTpContratacao(){
		return this.tpContratacao;
	}
	public void setTpModalidadeEnsino(int tpModalidadeEnsino){
		this.tpModalidadeEnsino=tpModalidadeEnsino;
	}
	public int getTpModalidadeEnsino(){
		return this.tpModalidadeEnsino;
	}
	public void setTpEtapaEnsino(int tpEtapaEnsino){
		this.tpEtapaEnsino=tpEtapaEnsino;
	}
	public int getTpEtapaEnsino(){
		return this.tpEtapaEnsino;
	}
	public void setTpPresenca(int tpPresenca){
		this.tpPresenca=tpPresenca;
	}
	public int getTpPresenca(){
		return this.tpPresenca;
	}
	public void setNrOrdem(int nrOrdem) {
		this.nrOrdem = nrOrdem;
	}
	public int getNrOrdem() {
		return nrOrdem;
	}
	public void setLgMulti(int lgMulti) {
		this.lgMulti = lgMulti;
	}
	public int getLgMulti() {
		return lgMulti;
	}
	public void setLgReferencia(int lgReferencia) {
		this.lgReferencia = lgReferencia;
	}
	public int getLgReferencia() {
		return lgReferencia;
	}
	public void setNrIdade(int nrIdade) {
		this.nrIdade = nrIdade;
	}
	public int getNrIdade() {
		return nrIdade;
	}
	public void setTpAvaliacao(int tpAvaliacao) {
		this.tpAvaliacao = tpAvaliacao;
	}
	public int getTpAvaliacao() {
		return tpAvaliacao;
	}
	public String toString() {
		String valueToString = super.toString().replaceAll("\\{", "").replaceAll("\\}", "");
		valueToString += ", \"cdCurso\": " +  getCdCurso();
		valueToString += ", \"nmHabilitacao\": \"" +  getNmHabilitacao()+"\"";
		valueToString += ", \"nmDuracao\": \"" +  getNmDuracao()+"\"";
		valueToString += ", \"txtCompetencia\": \"" +  getTxtCompetencia()+"\"";
		valueToString += ", \"txtCurso\": \"" +  getTxtCurso()+"\"";
		valueToString += ", \"cdTipoPeriodo\": " +  getCdTipoPeriodo();
		valueToString += ", \"tpContratacao\": " +  getTpContratacao();
		valueToString += ", \"tpModalidadeEnsino\": " +  getTpModalidadeEnsino();
		valueToString += ", \"tpEtapaEnsino\": " +  getTpEtapaEnsino();
		valueToString += ", \"tpPresenca\": " +  getTpPresenca();
		valueToString += ", \"nrOdem\": " +  getNrOrdem();
		valueToString += ", \"lgMulti\": " +  getLgMulti();
		valueToString += ", \"lgReferencia\": " +  getLgReferencia();
		valueToString += ", \"nrIdade\": " +  getNrIdade();
		valueToString += ", \"tpAvaliacao\": " +  getTpAvaliacao();
		return "{" + valueToString + "}";
	}
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdCurso\": " +  getCdCurso();
		valueToString += ", \"nmHabilitacao\": \"" +  getNmHabilitacao()+"\"";
		valueToString += ", \"nmDuracao\": \"" +  getNmDuracao()+"\"";
		valueToString += ", \"txtCompetencia\": \"" +  getTxtCompetencia()+"\"";
		valueToString += ", \"txtCurso\": \"" +  getTxtCurso()+"\"";
		valueToString += ", \"cdTipoPeriodo\": " +  getCdTipoPeriodo();
		valueToString += ", \"tpContratacao\": " +  getTpContratacao();
		valueToString += ", \"tpModalidadeEnsino\": " +  getTpModalidadeEnsino();
		valueToString += ", \"tpEtapaEnsino\": " +  getTpEtapaEnsino();
		valueToString += ", \"tpPresenca\": " +  getTpPresenca();
		valueToString += ", \"nrOdem\": " +  getNrOrdem();
		valueToString += ", \"lgMulti\": " +  getLgMulti();
		valueToString += ", \"lgReferencia\": " +  getLgReferencia();
		valueToString += ", \"nrIdade\": " +  getNrIdade();
		valueToString += ", \"tpAvaliacao\": " +  getTpAvaliacao();
		valueToString += ", \"produtoServico\": " + ProdutoServicoDAO.get(getCdProdutoServico()).toORM();
		return "{" + valueToString + "}";
	}
	
	public static String fromRegister(Map<String, Object> register) {
		String valueToString = "";
		valueToString += "\"cdCurso\": " +  register.get("CD_CURSO");
		valueToString += ", \"nmHabilitacao\": \"" +  register.get("NM_HABILITACAO")+"\"";
		valueToString += ", \"nmDuracao\": \"" +  register.get("NM_DURACAO")+"\"";
		valueToString += ", \"txtCompetencia\": \"" + register.get("TXT_COMPETENCIA")+"\"";
		valueToString += ", \"txtCurso\": \"" + register.get("TXT_CURSO")+"\"";
		valueToString += ", \"cdTipoPeriodo\": " +  register.get("CD_TIPO_PERIODO");
		valueToString += ", \"tpContratacao\": " +  register.get("TP_CONTRATACAO");
		valueToString += ", \"tpModalidadeEnsino\": " +  register.get("TP_MODALIDADE_ENSINO");
		valueToString += ", \"tpEtapaEnsino\": " +  register.get("TP_ETAPA_ENSINO");
		valueToString += ", \"tpPresenca\": " + register.get("TP_PRESENCA");
		valueToString += ", \"nrOdem\": " + register.get("NR_ORDEM");
		valueToString += ", \"lgMulti\": " + register.get("LG_MULTI");
		valueToString += ", \"lgReferencia\": " + register.get("LG_REFERENCIA");
		valueToString += ", \"nrIdade\": " + register.get("NR_IDADE");
		valueToString += ", \"tpAvaliacao\": " + register.get("TP_AVALIACAO");
		valueToString += ", \"produtoServico\": " + ProdutoServico.fromRegister(register);
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Curso(getCdProdutoServico(),
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
			getNmHabilitacao(),
			getNmDuracao(),
			getTxtCompetencia(),
			getTxtCurso(),
			getCdTipoPeriodo(),
			getTpContratacao(),
			getTpModalidadeEnsino(),
			getTpEtapaEnsino(),
			getTpPresenca(),
			getNrOrdem(),
			getLgMulti(),
			getLgReferencia(),
			getNrIdade(),
			getTpAvaliacao());
	}

	

}