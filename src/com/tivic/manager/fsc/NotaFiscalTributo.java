package com.tivic.manager.fsc;

public class NotaFiscalTributo {

	private int cdNotaFiscal;
	private int cdTributo;
	private float vlBaseCalculo;
	private float vlOutrasDespesas;
	private float vlOutrosImpostos;
	private float vlTributo;
	private float vlBaseRetencao;
	private float vlRetido;
	
	public NotaFiscalTributo(int cdNotaFiscal,
			int cdTributo,
			float vlBaseCalculo,
			float vlOutrasDespesas,
			float vlOutrosImpostos,
			float vlTributo,
			float vlBaseRetencao,
			float vlRetido){
		setCdNotaFiscal(cdNotaFiscal);
		setCdTributo(cdTributo);
		setVlBaseCalculo(vlBaseCalculo);
		setVlOutrasDespesas(vlOutrasDespesas);
		setVlOutrosImpostos(vlOutrosImpostos);
		setVlTributo(vlTributo);
		setVlBaseRetencao(vlBaseRetencao);
		setVlRetido(vlRetido);
	}
	public void setCdNotaFiscal(int cdNotaFiscal){
		this.cdNotaFiscal=cdNotaFiscal;
	}
	public int getCdNotaFiscal(){
		return this.cdNotaFiscal;
	}
	public void setCdTributo(int cdTributo){
		this.cdTributo=cdTributo;
	}
	public int getCdTributo(){
		return this.cdTributo;
	}
	public void setVlBaseCalculo(float vlBaseCalculo){
		this.vlBaseCalculo=vlBaseCalculo;
	}
	public float getVlBaseCalculo(){
		return this.vlBaseCalculo;
	}
	public void setVlOutrasDespesas(float vlOutrasDespesas){
		this.vlOutrasDespesas=vlOutrasDespesas;
	}
	public float getVlOutrasDespesas(){
		return this.vlOutrasDespesas;
	}
	public void setVlOutrosImpostos(float vlOutrosImpostos){
		this.vlOutrosImpostos=vlOutrosImpostos;
	}
	public float getVlOutrosImpostos(){
		return this.vlOutrosImpostos;
	}
	public void setVlTributo(float vlTributo){
		this.vlTributo=vlTributo;
	}
	public float getVlTributo(){
		return this.vlTributo;
	}
	public void setVlBaseRetencao(float vlBaseRetencao){
		this.vlBaseRetencao=vlBaseRetencao;
	}
	public float getVlBaseRetencao(){
		return this.vlBaseRetencao;
	}
	public void setVlRetido(float vlRetido){
		this.vlRetido=vlRetido;
	}
	public float getVlRetido(){
		return this.vlRetido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNotaFiscal: " +  getCdNotaFiscal();
		valueToString += ", cdTributo: " +  getCdTributo();
		valueToString += ", vlBaseCalculo: " +  getVlBaseCalculo();
		valueToString += ", vlOutrasDespesas: " +  getVlOutrasDespesas();
		valueToString += ", vlOutrosImpostos: " +  getVlOutrosImpostos();
		valueToString += ", vlTributo: " +  getVlTributo();
		valueToString += ", vlBaseRetencao: " +  getVlBaseRetencao();
		valueToString += ", vlRetido: " +  getVlRetido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new NotaFiscalTributo(getCdNotaFiscal(),
			getCdTributo(),
			getVlBaseCalculo(),
			getVlOutrasDespesas(),
			getVlOutrosImpostos(),
			getVlTributo(),
			getVlBaseRetencao(),
			getVlRetido());
	}

}