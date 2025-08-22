package com.tivic.manager.adm;

public class ProdutoServicoTributo {

	private int cdProdutoServicoTributo;
	private int cdTributoAliquota;
	private int cdTributo;
	private int cdProdutoServico;
	private int cdCidade;
	private int cdEstado;
	private int cdPais;
	private int cdNaturezaOperacao;
	private int cdClassificacaoFiscal;

	public ProdutoServicoTributo(int cdProdutoServicoTributo,
			int cdTributoAliquota,
			int cdTributo,
			int cdProdutoServico,
			int cdCidade,
			int cdEstado,
			int cdPais,
			int cdNaturezaOperacao,
			int cdClassificacaoFiscal){
		setCdProdutoServicoTributo(cdProdutoServicoTributo);
		setCdTributoAliquota(cdTributoAliquota);
		setCdTributo(cdTributo);
		setCdProdutoServico(cdProdutoServico);
		setCdCidade(cdCidade);
		setCdEstado(cdEstado);
		setCdPais(cdPais);
		setCdNaturezaOperacao(cdNaturezaOperacao);
		setCdClassificacaoFiscal(cdClassificacaoFiscal);
	}
	public void setCdProdutoServicoTributo(int cdProdutoServicoTributo){
		this.cdProdutoServicoTributo=cdProdutoServicoTributo;
	}
	public int getCdProdutoServicoTributo(){
		return this.cdProdutoServicoTributo;
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
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdPais(int cdPais){
		this.cdPais=cdPais;
	}
	public int getCdPais(){
		return this.cdPais;
	}
	public void setCdNaturezaOperacao(int cdNaturezaOperacao){
		this.cdNaturezaOperacao=cdNaturezaOperacao;
	}
	public int getCdNaturezaOperacao(){
		return this.cdNaturezaOperacao;
	}
	public void setCdClassificacaoFiscal(int cdClassificacaoFiscal){
		this.cdClassificacaoFiscal=cdClassificacaoFiscal;
	}
	public int getCdClassificacaoFiscal(){
		return this.cdClassificacaoFiscal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProdutoServicoTributo: " +  getCdProdutoServicoTributo();
		valueToString += ", cdTributoAliquota: " +  getCdTributoAliquota();
		valueToString += ", cdTributo: " +  getCdTributo();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdEstado: " +  getCdEstado();
		valueToString += ", cdPais: " +  getCdPais();
		valueToString += ", cdNaturezaOperacao: " +  getCdNaturezaOperacao();
		valueToString += ", cdClassificacaoFiscal: " +  getCdClassificacaoFiscal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoServicoTributo(getCdProdutoServicoTributo(),
			getCdTributoAliquota(),
			getCdTributo(),
			getCdProdutoServico(),
			getCdCidade(),
			getCdEstado(),
			getCdPais(),
			getCdNaturezaOperacao(),
			getCdClassificacaoFiscal());
	}

}