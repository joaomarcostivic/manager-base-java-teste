package com.tivic.manager.acd;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tivic.manager.util.Util;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aula {

	private int cdAula;
	private int cdTipoAula;
	private String txtConteudo;
	private String txtObservacao;
	private int stAula;
	private int cdProfessor;
	private int cdProfessorSubstituto;
	private int cdOferta;
	private GregorianCalendar dtAula;
	private int cdPlano;
	private int cdDisciplina;
	private int cdTurma;
	private int cdHorario;
	private String txtMotivoCancelamento;
	private int lgReposicao;
	private int cdAulaReposta;
	private GregorianCalendar hrInicioReposicao;
	private GregorianCalendar hrTerminoReposicao;
	private String txtTitulo;
	private String txtObjetivosAprendizagem;
	private String txtObjetosConhecimento;
	private String txtCamposExperiencia;
	private String txtProcedimentosMetodologicos;
	private String txtRecursosDidaticos;
	private String txtAvaliacao;
	
	public Aula(){ }

	public Aula(int cdAula,
			int cdTipoAula,
			String txtConteudo,
			String txtObservacao,
			int stAula,
			int cdProfessor,
			int cdProfessorSubstituto,
			int cdOferta,
			GregorianCalendar dtAula,
			int cdPlano,
			int cdDisciplina,
			int cdTurma,
			int cdHorario,
			String txtMotivoCancelamento,
			int lgReposicao,
			int cdAulaReposta,
			GregorianCalendar hrInicioReposicao,
			GregorianCalendar hrTerminoReposicao,
			String txtTitulo,
			String txtObjetivosAprendizagem,
			String txtObjetosConhecimento,
			String txtCamposExperiencia,
			String txtProcedimentosMetodologicos,
			String txtRecursosDidaticos,
			String txtAvaliacao){
		setCdAula(cdAula);
		setCdTipoAula(cdTipoAula);
		setTxtConteudo(txtConteudo);
		setTxtObservacao(txtObservacao);
		setStAula(stAula);
		setCdProfessor(cdProfessor);
		setCdProfessorSubstituto(cdProfessorSubstituto);
		setCdOferta(cdOferta);
		setDtAula(dtAula);
		setCdPlano(cdPlano);
		setCdDisciplina(cdDisciplina);
		setCdTurma(cdTurma);
		setCdHorario(cdHorario);
		setTxtMotivoCancelamento(txtMotivoCancelamento);
		setLgReposicao(lgReposicao);
		setCdAulaReposta(cdAulaReposta);
		setHrInicioReposicao(hrInicioReposicao);
		setHrTerminoReposicao(hrTerminoReposicao);	
		setTxtTitulo(txtTitulo);
		setTxtObjetivosAprendizagem(txtObjetivosAprendizagem);
		setTxtObjetosConhecimento(txtObjetosConhecimento);
		setTxtCamposExperiencia(txtCamposExperiencia);
		setTxtProcedimentosMetodologicos(txtProcedimentosMetodologicos);
		setTxtRecursosDidaticos(txtRecursosDidaticos);
		setTxtAvaliacao(txtAvaliacao);
	}
	public void setCdAula(int cdAula){
		this.cdAula=cdAula;
	}
	public int getCdAula(){
		return this.cdAula;
	}
	public void setCdTipoAula(int cdTipoAula){
		this.cdTipoAula=cdTipoAula;
	}
	public int getCdTipoAula(){
		return this.cdTipoAula;
	}
	public void setTxtConteudo(String txtConteudo){
		this.txtConteudo=txtConteudo;
	}
	public String getTxtConteudo(){
		return this.txtConteudo;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setStAula(int stAula){
		this.stAula=stAula;
	}
	public int getStAula(){
		return this.stAula;
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public void setCdProfessorSubstituto(int cdProfessorSubstituto){
		this.cdProfessorSubstituto=cdProfessorSubstituto;
	}
	public int getCdProfessorSubstituto(){
		return this.cdProfessorSubstituto;
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setDtAula(GregorianCalendar dtAula){
		this.dtAula=dtAula;
	}
	public GregorianCalendar getDtAula(){
		return this.dtAula;
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
	}
	public void setTxtMotivoCancelamento(String txtMotivoCancelamento) {
		this.txtMotivoCancelamento = txtMotivoCancelamento;
	}
	public String getTxtMotivoCancelamento() {
		return txtMotivoCancelamento;
	}
	public void setLgReposicao(int lgReposicao) {
		this.lgReposicao = lgReposicao;
	}
	public int getLgReposicao() {
		return lgReposicao;
	}
	public void setCdAulaReposta(int cdAulaReposta) {
		this.cdAulaReposta = cdAulaReposta;
	}
	public int getCdAulaReposta() {
		return cdAulaReposta;
	}
	public void setHrInicioReposicao(GregorianCalendar hrInicioReposicao) {
		this.hrInicioReposicao = hrInicioReposicao;
	}
	public GregorianCalendar getHrInicioReposicao() {
		return hrInicioReposicao;
	}
	public void setHrTerminoReposicao(GregorianCalendar hrTerminoReposicao) {
		this.hrTerminoReposicao = hrTerminoReposicao;
	}
	public GregorianCalendar getHrTerminoReposicao() {
		return hrTerminoReposicao;
	}
	public void setTxtTitulo(String txtTitulo) {
		this.txtTitulo = txtTitulo;
	}
	public String getTxtTitulo() {
		return txtTitulo;
	}
	public void setTxtObjetivosAprendizagem(String txtObjetivosAprendizagem) {
		this.txtObjetivosAprendizagem = txtObjetivosAprendizagem;
	}
	public String getTxtObjetivosAprendizagem() {
		return txtObjetivosAprendizagem;
	}
	public void setTxtObjetosConhecimento(String txtObjetosConhecimento) {
		this.txtObjetosConhecimento = txtObjetosConhecimento;
	}
	public String getTxtObjetosConhecimento() {
		return txtObjetosConhecimento;
	}
	public void setTxtCamposExperiencia(String txtCamposExperiencia) {
		this.txtCamposExperiencia = txtCamposExperiencia;
	}
	public String getTxtCamposExperiencia() {
		return txtCamposExperiencia;
	}
	public void setTxtProcedimentosMetodologicos(
			String txtProcedimentosMetodologicos) {
		this.txtProcedimentosMetodologicos = txtProcedimentosMetodologicos;
	}
	public String getTxtProcedimentosMetodologicos() {
		return txtProcedimentosMetodologicos;
	}
	public void setTxtRecursosDidaticos(String txtRecursosDidaticos) {
		this.txtRecursosDidaticos = txtRecursosDidaticos;
	}
	public String getTxtRecursosDidaticos() {
		return txtRecursosDidaticos;
	}
	public void setTxtAvaliacao(String txtAvaliacao) {
		this.txtAvaliacao = txtAvaliacao;
	}
	public String getTxtAvaliacao() {
		return txtAvaliacao;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdAula\": " +  getCdAula();
		valueToString += ", \"cdTipoAula\": " +  getCdTipoAula();
		valueToString += ", \"txtConteudo\": \"" +  getTxtConteudo() + "\"";
		valueToString += ", \"txtObservacao\": \"" +  getTxtObservacao() + "\"";
		valueToString += ", \"stAula\": " +  getStAula();
		valueToString += ", \"cdProfessor\": " +  getCdProfessor();
		valueToString += ", \"cdProfessorSubstituto\": " +  getCdProfessorSubstituto();
		valueToString += ", \"cdOferta\": " +  getCdOferta();
		valueToString += ", \"dtAula\": "+ (getDtAula() != null ? "\"" +  Util.convCalendarStringIso(getDtAula()) +"\"" : null);
		valueToString += ", \"cdPlano\": " +  getCdPlano();
		valueToString += ", \"cdDisciplina\": " +  getCdDisciplina();
		valueToString += ", \"cdTurma\": " +  getCdTurma();
		valueToString += ", \"cdHorario\": " +  getCdHorario();
		valueToString += ", \"txtMotivoCancelamento\": \"" +  getTxtMotivoCancelamento() + "\"";
		valueToString += ", \"lgReposicao\": " +  getLgReposicao();
		valueToString += ", \"cdAulaReposta\": " +  getCdAulaReposta();
		valueToString += ", \"hrInicioReposicao\": \"" +  sol.util.Util.formatDateTime(getHrInicioReposicao(), "HH:mm", "") + "\"";
		valueToString += ", \"hrTerminoReposicao\": \"" +  sol.util.Util.formatDateTime(getHrTerminoReposicao(), "HH:mm", "") + "\"";
		valueToString += ", \"txtTitulo\": \"" +  getTxtTitulo() + "\"";
		valueToString += ", \"txtObjetivosAprendizagem\": \"" +  getTxtObjetivosAprendizagem() + "\"";
		valueToString += ", \"txtObjetosConhecimento\": \"" +  getTxtObjetosConhecimento() + "\"";
		valueToString += ", \"txtCamposExperiencia\": \"" +  getTxtCamposExperiencia() + "\"";
		valueToString += ", \"txtProcedimentosMetodologicos\": \"" +  getTxtProcedimentosMetodologicos() + "\"";
		valueToString += ", \"txtRecursosDidaticos\": \"" +  getTxtRecursosDidaticos() + "\"";
		valueToString += ", \"txtAvaliacao\": \"" +  getTxtAvaliacao() + "\"";
		
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Aula(getCdAula(),
			getCdTipoAula(),
			getTxtConteudo(),
			getTxtObservacao(),
			getStAula(),
			getCdProfessor(),
			getCdProfessorSubstituto(),
			getCdOferta(),
			getDtAula()==null ? null : (GregorianCalendar)getDtAula().clone(),
			getCdPlano(),
			getCdDisciplina(),
			getCdTurma(),
			getCdHorario(),
			getTxtMotivoCancelamento(),
			getLgReposicao(),
			getCdAulaReposta(),
			getHrInicioReposicao(),
			getHrTerminoReposicao(),
			getTxtTitulo(),
			getTxtObjetivosAprendizagem(),
			getTxtObjetosConhecimento(),
			getTxtCamposExperiencia(),
			getTxtProcedimentosMetodologicos(),
			getTxtRecursosDidaticos(),
			getTxtAvaliacao());
	}

}