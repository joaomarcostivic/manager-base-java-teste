package com.tivic.manager.seg;

public class Objeto {

	private int cdFormulario;
	private int cdObjeto;
	private int cdSistema;
	private int cdAcao;
	private int cdModulo;
	private int tpObjeto;
	private String nmHint;
	private String nmObjeto;

	public Objeto() { }
	
	public Objeto(int cdFormulario,
			int cdObjeto,
			int cdSistema,
			int cdAcao,
			int cdModulo,
			int tpObjeto,
			String nmHint,
			String nmObjeto){
		setCdFormulario(cdFormulario);
		setCdObjeto(cdObjeto);
		setCdSistema(cdSistema);
		setCdAcao(cdAcao);
		setCdModulo(cdModulo);
		setTpObjeto(tpObjeto);
		setNmHint(nmHint);
		setNmObjeto(nmObjeto);
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setCdObjeto(int cdObjeto){
		this.cdObjeto=cdObjeto;
	}
	public int getCdObjeto(){
		return this.cdObjeto;
	}
	public void setCdSistema(int cdSistema){
		this.cdSistema=cdSistema;
	}
	public int getCdSistema(){
		return this.cdSistema;
	}
	public void setCdAcao(int cdAcao){
		this.cdAcao=cdAcao;
	}
	public int getCdAcao(){
		return this.cdAcao;
	}
	public void setCdModulo(int cdModulo){
		this.cdModulo=cdModulo;
	}
	public int getCdModulo(){
		return this.cdModulo;
	}
	public void setTpObjeto(int tpObjeto){
		this.tpObjeto=tpObjeto;
	}
	public int getTpObjeto(){
		return this.tpObjeto;
	}
	public void setNmHint(String nmHint){
		this.nmHint=nmHint;
	}
	public String getNmHint(){
		return this.nmHint;
	}
	public void setNmObjeto(String nmObjeto){
		this.nmObjeto=nmObjeto;
	}
	public String getNmObjeto(){
		return this.nmObjeto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormulario: " +  getCdFormulario();
		valueToString += ", cdObjeto: " +  getCdObjeto();
		valueToString += ", cdSistema: " +  getCdSistema();
		valueToString += ", cdAcao: " +  getCdAcao();
		valueToString += ", cdModulo: " +  getCdModulo();
		valueToString += ", tpObjeto: " +  getTpObjeto();
		valueToString += ", nmHint: " +  getNmHint();
		valueToString += ", nmObjeto: " +  getNmObjeto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Objeto(cdFormulario,
			cdObjeto,
			cdSistema,
			cdAcao,
			cdModulo,
			tpObjeto,
			nmHint,
			nmObjeto);
	}

}