package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class OfertaAvaliacao {

	private int cdOfertaAvaliacao;
	private int cdOferta;
	private int cdTipoAvaliacao;
	private int cdCurso;
	private int cdUnidade;
	private int cdDisciplinaAvaliacao;
	private int cdCursoPeriodo;
	private int cdDisciplina;
	private int cdMatriz;
	private int cdFormulario;
	private GregorianCalendar dtAvaliacao;
	private String nmAvaliacao;
	private String txtObservacao;
	private float vlPeso;
	private String idOfertaAvaliacao;
	private int stOfertaAvaliacao;
	private int cdAula;
	
	public OfertaAvaliacao(){ }

	public OfertaAvaliacao(int cdOfertaAvaliacao,
			int cdOferta,
			int cdTipoAvaliacao,
			int cdCurso,
			int cdUnidade,
			int cdDisciplinaAvaliacao,
			int cdCursoPeriodo,
			int cdDisciplina,
			int cdMatriz,
			int cdFormulario,
			GregorianCalendar dtAvaliacao,
			String nmAvaliacao,
			String txtObservacao,
			float vlPeso,
			String idOfertaAvaliacao,
			int stOfertaAvaliacao,
			int cdAula){
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setCdTipoAvaliacao(cdTipoAvaliacao);
		setCdCurso(cdCurso);
		setCdUnidade(cdUnidade);
		setCdDisciplinaAvaliacao(cdDisciplinaAvaliacao);
		setCdCursoPeriodo(cdCursoPeriodo);
		setCdDisciplina(cdDisciplina);
		setCdMatriz(cdMatriz);
		setCdFormulario(cdFormulario);
		setDtAvaliacao(dtAvaliacao);
		setNmAvaliacao(nmAvaliacao);
		setTxtObservacao(txtObservacao);
		setVlPeso(vlPeso);
		setIdOfertaAvaliacao(idOfertaAvaliacao);
		setStOfertaAvaliacao(stOfertaAvaliacao);
		setCdAula(cdAula);
	}
	public void setCdOfertaAvaliacao(int cdOfertaAvaliacao){
		this.cdOfertaAvaliacao=cdOfertaAvaliacao;
	}
	public int getCdOfertaAvaliacao(){
		return this.cdOfertaAvaliacao;
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setCdTipoAvaliacao(int cdTipoAvaliacao){
		this.cdTipoAvaliacao=cdTipoAvaliacao;
	}
	public int getCdTipoAvaliacao(){
		return this.cdTipoAvaliacao;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdUnidade(int cdUnidade){
		this.cdUnidade=cdUnidade;
	}
	public int getCdUnidade(){
		return this.cdUnidade;
	}
	public void setCdDisciplinaAvaliacao(int cdDisciplinaAvaliacao){
		this.cdDisciplinaAvaliacao=cdDisciplinaAvaliacao;
	}
	public int getCdDisciplinaAvaliacao(){
		return this.cdDisciplinaAvaliacao;
	}
	public void setCdCursoPeriodo(int cdCursoPeriodo){
		this.cdCursoPeriodo=cdCursoPeriodo;
	}
	public int getCdCursoPeriodo(){
		return this.cdCursoPeriodo;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setDtAvaliacao(GregorianCalendar dtAvaliacao){
		this.dtAvaliacao=dtAvaliacao;
	}
	public GregorianCalendar getDtAvaliacao(){
		return this.dtAvaliacao;
	}
	public void setNmAvaliacao(String nmAvaliacao){
		this.nmAvaliacao=nmAvaliacao;
	}
	public String getNmAvaliacao(){
		return this.nmAvaliacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setVlPeso(float vlPeso){
		this.vlPeso=vlPeso;
	}
	public float getVlPeso(){
		return this.vlPeso;
	}
	public void setIdOfertaAvaliacao(String idOfertaAvaliacao){
		this.idOfertaAvaliacao=idOfertaAvaliacao;
	}
	public String getIdOfertaAvaliacao(){
		return this.idOfertaAvaliacao;
	}
	public void setStOfertaAvaliacao(int stOfertaAvaliacao) {
		this.stOfertaAvaliacao = stOfertaAvaliacao;
	}
	public int getStOfertaAvaliacao() {
		return stOfertaAvaliacao;
	}
	public void setCdAula(int cdAula) {
		this.cdAula = cdAula;
	}
	public int getCdAula() {
		return cdAula;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdOfertaAvaliacao\": " +  getCdOfertaAvaliacao();
		valueToString += ", \"cdOferta\": " +  getCdOferta();
		valueToString += ", \"cdTipoAvaliacao\": " +  getCdTipoAvaliacao();
		valueToString += ", \"cdCurso\": " +  getCdCurso();
		valueToString += ", \"cdUnidade\": " +  getCdUnidade();
		valueToString += ", \"cdDisciplinaAvaliacao\": " +  getCdDisciplinaAvaliacao();
		valueToString += ", \"cdCursoPeriodo\": " +  getCdCursoPeriodo();
		valueToString += ", \"cdDisciplina\": " +  getCdDisciplina();
		valueToString += ", \"cdMatriz\": " +  getCdMatriz();
		valueToString += ", \"cdFormulario\": " +  getCdFormulario();
		valueToString += ", \"dtAvaliacao\": \"" +  sol.util.Util.formatDateTime(getDtAvaliacao(), "dd/MM/yyyy HH:mm:ss:SSS", "") + "\"";
		valueToString += ", \"nmAvaliacao\": \"" +  getNmAvaliacao()+ "\"";
		valueToString += ", \"txtObservacao\": \"" +  getTxtObservacao()+ "\"";
		valueToString += ", \"vlPeso\": " +  getVlPeso();
		valueToString += ", \"idOfertaAvaliacao\": \"" +  getIdOfertaAvaliacao()+ "\"";
		valueToString += ", \"stOfertaAvaliacao\": " +  getStOfertaAvaliacao();
		valueToString += ", \"cdAula\": " +  getCdAula();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OfertaAvaliacao(getCdOfertaAvaliacao(),
			getCdOferta(),
			getCdTipoAvaliacao(),
			getCdCurso(),
			getCdUnidade(),
			getCdDisciplinaAvaliacao(),
			getCdCursoPeriodo(),
			getCdDisciplina(),
			getCdMatriz(),
			getCdFormulario(),
			getDtAvaliacao()==null ? null : (GregorianCalendar)getDtAvaliacao().clone(),
			getNmAvaliacao(),
			getTxtObservacao(),
			getVlPeso(),
			getIdOfertaAvaliacao(),
			getStOfertaAvaliacao(),
			getCdAula());
	}

	

}
