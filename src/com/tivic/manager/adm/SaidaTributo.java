package com.tivic.manager.adm;

public class SaidaTributo {

	private int cdDocumentoSaida;
	private int cdTributo;
	private float vlBaseCalculo;
	private float prAliquota;
	private float vlTributo;
	private float vlBaseRetencao;
	private float vlRetido;

	public SaidaTributo(int cdDocumentoSaida,
			int cdTributo,
			float vlBaseCalculo,
			float prAliquota,
			float vlTributo,
			float vlBaseRetencao,
			float vlRetido){
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdTributo(cdTributo);
		setVlBaseCalculo(vlBaseCalculo);
		setPrAliquota(prAliquota);
		setVlTributo(vlTributo);
		setVlBaseRetencao(vlBaseRetencao);
		setVlRetido(vlRetido);
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
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
	public void setPrAliquota(float prAliquota){
		this.prAliquota=prAliquota;
	}
	public float getPrAliquota(){
		return this.prAliquota;
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
		valueToString += "cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdTributo: " +  getCdTributo();
		valueToString += ", vlBaseCalculo: " +  getVlBaseCalculo();
		valueToString += ", prAliquota: " +  getPrAliquota();
		valueToString += ", vlTributo: " +  getVlTributo();
		valueToString += ", vlBaseRetencao: " +  getVlBaseRetencao();
		valueToString += ", vlRetido: " +  getVlRetido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SaidaTributo(getCdDocumentoSaida(),
			getCdTributo(),
			getVlBaseCalculo(),
			getPrAliquota(),
			getVlTributo(),
			getVlBaseRetencao(),
			getVlRetido());
	}

}