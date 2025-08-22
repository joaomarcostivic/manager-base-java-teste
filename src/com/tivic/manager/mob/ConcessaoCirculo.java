package com.tivic.manager.mob;

public class ConcessaoCirculo {

	private int cdConcessao;
	private int cdCirculo;

	public ConcessaoCirculo() { }

	public ConcessaoCirculo(int cdConcessao,
			int cdCirculo) {
		setCdConcessao(cdConcessao);
		setCdCirculo(cdCirculo);
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setCdCirculo(int cdCirculo){
		this.cdCirculo=cdCirculo;
	}
	public int getCdCirculo(){
		return this.cdCirculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessao: " +  getCdConcessao();
		valueToString += ", cdCirculo: " +  getCdCirculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ConcessaoCirculo(getCdConcessao(),
			getCdCirculo());
	}

}