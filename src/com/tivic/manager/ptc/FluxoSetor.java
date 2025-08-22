package com.tivic.manager.ptc;

public class FluxoSetor {

	private int cdFluxo;
	private int cdSetor;

	public FluxoSetor(){ }

	public FluxoSetor(int cdFluxo,
			int cdSetor){
		setCdFluxo(cdFluxo);
		setCdSetor(cdSetor);
	}
	public void setCdFluxo(int cdFluxo){
		this.cdFluxo=cdFluxo;
	}
	public int getCdFluxo(){
		return this.cdFluxo;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFluxo: " +  getCdFluxo();
		valueToString += ", cdSetor: " +  getCdSetor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FluxoSetor(getCdFluxo(),
			getCdSetor());
	}

}
