package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class CursoUnidadeConceito {

	private int cdUnidade;
	private int cdCurso;
	private int cdMatriculaDisciplina;
	private Double vlConceito;
	private Double vlConceitoAproveitamento;
	private GregorianCalendar dtLancamento;
	private GregorianCalendar dtResultado;
	private int cdConceito;
	private String txtObservacao;
	private int lgAprovado;

	public CursoUnidadeConceito() { }

	public CursoUnidadeConceito(int cdUnidade,
			int cdCurso,
			int cdMatriculaDisciplina,
			Double vlConceito,
			Double vlConceitoAproveitamento,
			GregorianCalendar dtLancamento,
			GregorianCalendar dtResultado,
			int cdConceito,
			String txtObservacao,
			int lgAprovado) {
		setCdUnidade(cdUnidade);
		setCdCurso(cdCurso);
		setCdMatriculaDisciplina(cdMatriculaDisciplina);
		setVlConceito(vlConceito);
		setVlConceitoAproveitamento(vlConceitoAproveitamento);
		setDtLancamento(dtLancamento);
		setDtResultado(dtResultado);
		setCdConceito(cdConceito);
		setTxtObservacao(txtObservacao);
		setLgAprovado(lgAprovado);
	}
	public void setCdUnidade(int cdUnidade){
		this.cdUnidade=cdUnidade;
	}
	public int getCdUnidade(){
		return this.cdUnidade;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdMatriculaDisciplina(int cdMatriculaDisciplina){
		this.cdMatriculaDisciplina=cdMatriculaDisciplina;
	}
	public int getCdMatriculaDisciplina(){
		return this.cdMatriculaDisciplina;
	}
	public void setVlConceito(Double vlConceito){
		this.vlConceito=vlConceito;
	}
	public Double getVlConceito(){
		return this.vlConceito;
	}
	public void setVlConceitoAproveitamento(Double vlConceitoAproveitamento){
		this.vlConceitoAproveitamento=vlConceitoAproveitamento;
	}
	public Double getVlConceitoAproveitamento(){
		return this.vlConceitoAproveitamento;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setDtResultado(GregorianCalendar dtResultado){
		this.dtResultado=dtResultado;
	}
	public GregorianCalendar getDtResultado(){
		return this.dtResultado;
	}
	public void setCdConceito(int cdConceito){
		this.cdConceito=cdConceito;
	}
	public int getCdConceito(){
		return this.cdConceito;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setLgAprovado(int lgAprovado){
		this.lgAprovado=lgAprovado;
	}
	public int getLgAprovado(){
		return this.lgAprovado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUnidade: " +  getCdUnidade();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdMatriculaDisciplina: " +  getCdMatriculaDisciplina();
		valueToString += ", vlConceito: " +  getVlConceito();
		valueToString += ", vlConceitoAproveitamento: " +  getVlConceitoAproveitamento();
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtResultado: " +  sol.util.Util.formatDateTime(getDtResultado(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdConceito: " +  getCdConceito();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", lgAprovado: " +  getLgAprovado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoUnidadeConceito(getCdUnidade(),
			getCdCurso(),
			getCdMatriculaDisciplina(),
			getVlConceito(),
			getVlConceitoAproveitamento(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getDtResultado()==null ? null : (GregorianCalendar)getDtResultado().clone(),
			getCdConceito(),
			getTxtObservacao(),
			getLgAprovado());
	}

}