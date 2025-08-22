package com.tivic.manager.prc;

public class SistemaProcesso {

	private int cdSistemaProcesso;
	private String nmSistemaProcesso;
	private String dsSistemaProcesso;
	private String urlSistemaProcesso;

	public SistemaProcesso() { }

	public SistemaProcesso(int cdSistemaProcesso,
			String nmSistemaProcesso,
			String dsSistemaProcesso,
			String urlSistemaProcesso) {
		setCdSistemaProcesso(cdSistemaProcesso);
		setNmSistemaProcesso(nmSistemaProcesso);
		setDsSistemaProcesso(dsSistemaProcesso);
		setUrlSistemaProcesso(urlSistemaProcesso);
	}
	public void setCdSistemaProcesso(int cdSistemaProcesso){
		this.cdSistemaProcesso=cdSistemaProcesso;
	}
	public int getCdSistemaProcesso(){
		return this.cdSistemaProcesso;
	}
	public void setNmSistemaProcesso(String nmSistemaProcesso){
		this.nmSistemaProcesso=nmSistemaProcesso;
	}
	public String getNmSistemaProcesso(){
		return this.nmSistemaProcesso;
	}
	public void setDsSistemaProcesso(String dsSistemaProcesso){
		this.dsSistemaProcesso=dsSistemaProcesso;
	}
	public String getDsSistemaProcesso(){
		return this.dsSistemaProcesso;
	}
	public void setUrlSistemaProcesso(String urlSistemaProcesso){
		this.urlSistemaProcesso=urlSistemaProcesso;
	}
	public String getUrlSistemaProcesso(){
		return this.urlSistemaProcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSistemaProcesso: " +  getCdSistemaProcesso();
		valueToString += ", nmSistemaProcesso: " +  getNmSistemaProcesso();
		valueToString += ", dsSistemaProcesso: " +  getDsSistemaProcesso();
		valueToString += ", urlSistemaProcesso: " +  getUrlSistemaProcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SistemaProcesso(getCdSistemaProcesso(),
			getNmSistemaProcesso(),
			getDsSistemaProcesso(),
			getUrlSistemaProcesso());
	}

}