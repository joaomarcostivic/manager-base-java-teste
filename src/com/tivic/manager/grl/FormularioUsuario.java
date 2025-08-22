package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class FormularioUsuario {

	private int cdFormularioUsuario;
	private int cdFormulario;
	private int cdUsuario;
	private int stResposta;
	private int qtVisualizacao;
	private String txtObservacao;
	private GregorianCalendar dtResposta;

	public FormularioUsuario(){ }

	public FormularioUsuario(int cdFormularioUsuario,
			int cdFormulario,
			int cdUsuario,
			int stResposta,
			int qtVisualizacao,
			String txtObservacao,
			GregorianCalendar dtResposta){
		setCdFormularioUsuario(cdFormularioUsuario);
		setCdFormulario(cdFormulario);
		setCdUsuario(cdUsuario);
		setStResposta(stResposta);
		setQtVisualizacao(qtVisualizacao);
		setTxtObservacao(txtObservacao);
		setDtResposta(dtResposta);
	}
	public void setCdFormularioUsuario(int cdFormularioUsuario){
		this.cdFormularioUsuario=cdFormularioUsuario;
	}
	public int getCdFormularioUsuario(){
		return this.cdFormularioUsuario;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setStResposta(int stResposta){
		this.stResposta=stResposta;
	}
	public int getStResposta(){
		return this.stResposta;
	}
	public void setQtVisualizacao(int qtVisualizacao){
		this.qtVisualizacao=qtVisualizacao;
	}
	public int getQtVisualizacao(){
		return this.qtVisualizacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setDtResposta(GregorianCalendar dtResposta){
		this.dtResposta=dtResposta;
	}
	public GregorianCalendar getDtResposta(){
		return this.dtResposta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormularioUsuario: " +  getCdFormularioUsuario();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", stResposta: " +  getStResposta();
		valueToString += ", qtVisualizacao: " +  getQtVisualizacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", dtResposta: " +  sol.util.Util.formatDateTime(getDtResposta(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormularioUsuario(getCdFormularioUsuario(),
			getCdFormulario(),
			getCdUsuario(),
			getStResposta(),
			getQtVisualizacao(),
			getTxtObservacao(),
			getDtResposta()==null ? null : (GregorianCalendar)getDtResposta().clone());
	}

}