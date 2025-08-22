package com.tivic.manager.seg;

public class PerguntaSecreta {

	private int cdPerguntaSecreta;
	private String nmPerguntaSecreta;

	public PerguntaSecreta(int cdPerguntaSecreta,
			String nmPerguntaSecreta){
		setCdPerguntaSecreta(cdPerguntaSecreta);
		setNmPerguntaSecreta(nmPerguntaSecreta);
	}
	public void setCdPerguntaSecreta(int cdPerguntaSecreta){
		this.cdPerguntaSecreta=cdPerguntaSecreta;
	}
	public int getCdPerguntaSecreta(){
		return this.cdPerguntaSecreta;
	}
	public void setNmPerguntaSecreta(String nmPerguntaSecreta){
		this.nmPerguntaSecreta=nmPerguntaSecreta;
	}
	public String getNmPerguntaSecreta(){
		return this.nmPerguntaSecreta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPerguntaSecreta: " +  getCdPerguntaSecreta();
		valueToString += ", nmPerguntaSecreta: " +  getNmPerguntaSecreta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PerguntaSecreta(cdPerguntaSecreta,
			nmPerguntaSecreta);
	}

}