package com.tivic.manager.psq;

public class Tema {

	private int cdTema;
	private int cdQuestionario;
	private int cdTemaSuperior;
	private String nmTema;
	private String txtTema;

	public Tema(int cdTema,
			int cdQuestionario,
			int cdTemaSuperior,
			String nmTema,
			String txtTema){
		setCdTema(cdTema);
		setCdQuestionario(cdQuestionario);
		setCdTemaSuperior(cdTemaSuperior);
		setNmTema(nmTema);
		setTxtTema(txtTema);
	}
	public void setCdTema(int cdTema){
		this.cdTema=cdTema;
	}
	public int getCdTema(){
		return this.cdTema;
	}
	public void setCdQuestionario(int cdQuestionario){
		this.cdQuestionario=cdQuestionario;
	}
	public int getCdQuestionario(){
		return this.cdQuestionario;
	}
	public void setCdTemaSuperior(int cdTemaSuperior){
		this.cdTemaSuperior=cdTemaSuperior;
	}
	public int getCdTemaSuperior(){
		return this.cdTemaSuperior;
	}
	public void setNmTema(String nmTema){
		this.nmTema=nmTema;
	}
	public String getNmTema(){
		return this.nmTema;
	}
	public void setTxtTema(String txtTema){
		this.txtTema=txtTema;
	}
	public String getTxtTema(){
		return this.txtTema;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTema: " +  getCdTema();
		valueToString += ", cdQuestionario: " +  getCdQuestionario();
		valueToString += ", cdTemaSuperior: " +  getCdTemaSuperior();
		valueToString += ", nmTema: " +  getNmTema();
		valueToString += ", txtTema: " +  getTxtTema();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Tema(getCdTema(),
			getCdQuestionario(),
			getCdTemaSuperior(),
			getNmTema(),
			getTxtTema());
	}

}