package com.tivic.manager.ptc;

public class TipoAnexo {

	private int cdTipoAnexo;
	private String nmTipoAnexo;

	public TipoAnexo() { }
			
	public TipoAnexo(int cdTipoAnexo,
			String nmTipoAnexo){
		setCdTipoAnexo(cdTipoAnexo);
		setNmTipoAnexo(nmTipoAnexo);
	}
	public void setCdTipoAnexo(int cdTipoAnexo){
		this.cdTipoAnexo=cdTipoAnexo;
	}
	public int getCdTipoAnexo(){
		return this.cdTipoAnexo;
	}
	public void setNmTipoAnexo(String nmTipoAnexo){
		this.nmTipoAnexo=nmTipoAnexo;
	}
	public String getNmTipoAnexo(){
		return this.nmTipoAnexo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoAnexo: " +  getCdTipoAnexo();
		valueToString += ", nmTipoAnexo: " +  getNmTipoAnexo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoAnexo(getCdTipoAnexo(),
			getNmTipoAnexo());
	}

}