package com.tivic.manager.grl;

public class TipoProcesso {

	private int cdTipoProcesso;
	private String nmTipoProcesso;

	public TipoProcesso(int cdTipoProcesso,
			String nmTipoProcesso){
		setCdTipoProcesso(cdTipoProcesso);
		setNmTipoProcesso(nmTipoProcesso);
	}
	public void setCdTipoProcesso(int cdTipoProcesso){
		this.cdTipoProcesso=cdTipoProcesso;
	}
	public int getCdTipoProcesso(){
		return this.cdTipoProcesso;
	}
	public void setNmTipoProcesso(String nmTipoProcesso){
		this.nmTipoProcesso=nmTipoProcesso;
	}
	public String getNmTipoProcesso(){
		return this.nmTipoProcesso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoProcesso: " +  getCdTipoProcesso();
		valueToString += ", nmTipoProcesso: " +  getNmTipoProcesso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoProcesso(getCdTipoProcesso(),
			getNmTipoProcesso());
	}

}
