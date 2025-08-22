package com.tivic.manager.adm;

public class SaidaItemAliquota {

	private int cdProdutoServico;
	private int cdDocumentoSaida;
	private int cdEmpresa;
	private int cdTributoAliquota;
	private int cdTributo;
	private float prAliquota;
	private float vlBaseCalculo;
	private int cdItem;

	public SaidaItemAliquota(int cdProdutoServico,
			int cdDocumentoSaida,
			int cdEmpresa,
			int cdTributoAliquota,
			int cdTributo,
			float prAliquota,
			float vlBaseCalculo,
			int cdItem){
		setCdProdutoServico(cdProdutoServico);
		setCdDocumentoSaida(cdDocumentoSaida);
		setCdEmpresa(cdEmpresa);
		setCdTributoAliquota(cdTributoAliquota);
		setCdTributo(cdTributo);
		setPrAliquota(prAliquota);
		setVlBaseCalculo(vlBaseCalculo);
		setCdItem(cdItem);
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdDocumentoSaida(int cdDocumentoSaida){
		this.cdDocumentoSaida=cdDocumentoSaida;
	}
	public int getCdDocumentoSaida(){
		return this.cdDocumentoSaida;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdTributoAliquota(int cdTributoAliquota){
		this.cdTributoAliquota=cdTributoAliquota;
	}
	public int getCdTributoAliquota(){
		return this.cdTributoAliquota;
	}
	public void setCdTributo(int cdTributo){
		this.cdTributo=cdTributo;
	}
	public int getCdTributo(){
		return this.cdTributo;
	}
	public void setPrAliquota(float prAliquota){
		this.prAliquota=prAliquota;
	}
	public float getPrAliquota(){
		return this.prAliquota;
	}
	public void setVlBaseCalculo(float vlBaseCalculo){
		this.vlBaseCalculo=vlBaseCalculo;
	}
	public float getVlBaseCalculo(){
		return this.vlBaseCalculo;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdDocumentoSaida: " +  getCdDocumentoSaida();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdTributoAliquota: " +  getCdTributoAliquota();
		valueToString += ", cdTributo: " +  getCdTributo();
		valueToString += ", prAliquota: " +  getPrAliquota();
		valueToString += ", vlBaseCalculo: " +  getVlBaseCalculo();
		valueToString += ", cdItem: " +  getCdItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SaidaItemAliquota(getCdProdutoServico(),
			getCdDocumentoSaida(),
			getCdEmpresa(),
			getCdTributoAliquota(),
			getCdTributo(),
			getPrAliquota(),
			getVlBaseCalculo(),
			getCdItem());
	}

}
