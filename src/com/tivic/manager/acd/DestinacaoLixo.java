package com.tivic.manager.acd;

public class DestinacaoLixo {

	private int cdDestinacaoLixo;
	private String nmDestinacaoLixo;
	private String idDestinacaoLixo;

	public DestinacaoLixo(){ }

	public DestinacaoLixo(int cdDestinacaoLixo,
			String nmDestinacaoLixo,
			String idDestinacaoLixo){
		setCdDestinacaoLixo(cdDestinacaoLixo);
		setNmDestinacaoLixo(nmDestinacaoLixo);
		setIdDestinacaoLixo(idDestinacaoLixo);
	}
	public void setCdDestinacaoLixo(int cdDestinacaoLixo){
		this.cdDestinacaoLixo=cdDestinacaoLixo;
	}
	public int getCdDestinacaoLixo(){
		return this.cdDestinacaoLixo;
	}
	public void setNmDestinacaoLixo(String nmDestinacaoLixo){
		this.nmDestinacaoLixo=nmDestinacaoLixo;
	}
	public String getNmDestinacaoLixo(){
		return this.nmDestinacaoLixo;
	}
	public void setIdDestinacaoLixo(String idDestinacaoLixo){
		this.idDestinacaoLixo=idDestinacaoLixo;
	}
	public String getIdDestinacaoLixo(){
		return this.idDestinacaoLixo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDestinacaoLixo: " +  getCdDestinacaoLixo();
		valueToString += ", nmDestinacaoLixo: " +  getNmDestinacaoLixo();
		valueToString += ", idDestinacaoLixo: " +  getIdDestinacaoLixo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DestinacaoLixo(getCdDestinacaoLixo(),
			getNmDestinacaoLixo(),
			getIdDestinacaoLixo());
	}

}