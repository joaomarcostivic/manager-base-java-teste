package com.tivic.manager.seg;

public class Formulario {

	private int cdFormulario;
	private int cdSistema;
	private String nmFormulario;
	private String nmTitulo;
	
	public Formulario() { }
			
	public Formulario(int cdFormulario,
			int cdSistema,
			String nmFormulario,
			String nmTitulo){
		setCdFormulario(cdFormulario);
		setCdSistema(cdSistema);
		setNmFormulario(nmFormulario);
		setNmTitulo(nmTitulo);
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setNmFormulario(String nmFormulario){
		this.nmFormulario=nmFormulario;
	}
	public String getNmFormulario(){
		return this.nmFormulario;
	}
	public void setNmTitulo(String nmTitulo){
		this.nmTitulo=nmTitulo;
	}
	public String getNmTitulo(){
		return this.nmTitulo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormulario: " +  getCdFormulario();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", nmFormulario: " +  getNmFormulario();
		valueToString += ", nmTitulo: " +  getNmTitulo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Formulario(cdFormulario,
			cdSistema,
			nmFormulario,
			nmTitulo);
	}

}