package com.tivic.manager.adm;

public class EntradaItemAliquota {

	private int cdProdutoServico;
	private int cdDocumentoEntrada;
	private int cdEmpresa;
	private int cdTributoAliquota;
	private int cdTributo;
	private int cdItem;
	private float prAliquota;
	private float vlBaseCalculo;
	private int cdSituacaoTributaria;
		
	public EntradaItemAliquota() {
		// TODO Auto-generated constructor stub
	}
	
	public EntradaItemAliquota(int cdProdutoServico,
			int cdDocumentoEntrada,
			int cdEmpresa,
			int cdTributoAliquota,
			int cdTributo,
			int cdItem,
			float prAliquota,
			float vlBaseCalculo){
		setCdProdutoServico(cdProdutoServico);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdTributoAliquota(cdTributoAliquota);
		setCdTributo(cdTributo);
		setCdItem(cdItem);
		setPrAliquota(prAliquota);
		setVlBaseCalculo(vlBaseCalculo);
	}
	public EntradaItemAliquota(int cdProdutoServico,
			int cdDocumentoEntrada,
			int cdEmpresa,
			int cdTributoAliquota,
			int cdTributo,
			int cdItem,
			float prAliquota,
			float vlBaseCalculo,
			int cdSituacaoTributaria){
		setCdProdutoServico(cdProdutoServico);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdEmpresa(cdEmpresa);
		setCdTributoAliquota(cdTributoAliquota);
		setCdTributo(cdTributo);
		setCdItem(cdItem);
		setPrAliquota(prAliquota);
		setVlBaseCalculo(vlBaseCalculo);
		setCdSituacaoTributaria(cdSituacaoTributaria);
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
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
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
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
	public void setCdSituacaoTributaria(int cdSituacaoTributaria) {
		this.cdSituacaoTributaria = cdSituacaoTributaria;
	}
	public int getCdSituacaoTributaria() {
		return cdSituacaoTributaria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdTributoAliquota: " +  getCdTributoAliquota();
		valueToString += ", cdTributo: " +  getCdTributo();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", prAliquota: " +  getPrAliquota();
		valueToString += ", vlBaseCalculo: " +  getVlBaseCalculo();
		valueToString += ", cdSituacaoTributaria: " +  getCdSituacaoTributaria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EntradaItemAliquota(getCdProdutoServico(),
			getCdDocumentoEntrada(),
			getCdEmpresa(),
			getCdTributoAliquota(),
			getCdTributo(),
			getCdItem(),
			getPrAliquota(),
			getVlBaseCalculo(),
			getCdSituacaoTributaria());
	}

}
