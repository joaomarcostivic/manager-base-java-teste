package com.tivic.manager.mob;

public class MeioRemocao {

	private int cdMeioRemocao;
	private String nmMeioRemocao;

	public MeioRemocao() { }

	public MeioRemocao(int cdMeioRemocao,
			String nmMeioRemocao) {
		setCdMeioRemocao(cdMeioRemocao);
		setNmMeioRemocao(nmMeioRemocao);
	}
	public void setCdMeioRemocao(int cdMeioRemocao){
		this.cdMeioRemocao=cdMeioRemocao;
	}
	public int getCdMeioRemocao(){
		return this.cdMeioRemocao;
	}
	public void setNmMeioRemocao(String nmMeioRemocao){
		this.nmMeioRemocao=nmMeioRemocao;
	}
	public String getNmMeioRemocao(){
		return this.nmMeioRemocao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMeioRemocao: " +  getCdMeioRemocao();
		valueToString += ", nmMeioRemocao: " +  getNmMeioRemocao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MeioRemocao(getCdMeioRemocao(),
			getNmMeioRemocao());
	}

}