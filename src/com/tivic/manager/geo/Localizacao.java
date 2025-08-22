package com.tivic.manager.geo;

public class Localizacao {

	private int cdLocalizacao;
	private int cdReferencia;
	private int cdPais;
	private int cdEstado;
	private int cdRegiao;
	private int cdCidade;
	private int cdDistrito;
	private int cdBairro;
	private int cdLogradouro;
	private int cdEndereco;
	private int cdPessoa;
	private int tpLocalizacao;

	public Localizacao(){ }

	public Localizacao(int cdLocalizacao,
			int cdReferencia,
			int cdPais,
			int cdEstado,
			int cdRegiao,
			int cdCidade,
			int cdDistrito,
			int cdBairro,
			int cdLogradouro,
			int cdEndereco,
			int cdPessoa,
			int tpLocalizacao){
		setCdLocalizacao(cdLocalizacao);
		setCdReferencia(cdReferencia);
		setCdPais(cdPais);
		setCdEstado(cdEstado);
		setCdRegiao(cdRegiao);
		setCdCidade(cdCidade);
		setCdDistrito(cdDistrito);
		setCdBairro(cdBairro);
		setCdLogradouro(cdLogradouro);
		setCdEndereco(cdEndereco);
		setCdPessoa(cdPessoa);
		setTpLocalizacao(tpLocalizacao);
	}
	public void setCdLocalizacao(int cdLocalizacao){
		this.cdLocalizacao=cdLocalizacao;
	}
	public int getCdLocalizacao(){
		return this.cdLocalizacao;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdPais(int cdPais){
		this.cdPais=cdPais;
	}
	public int getCdPais(){
		return this.cdPais;
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdDistrito(int cdDistrito){
		this.cdDistrito=cdDistrito;
	}
	public int getCdDistrito(){
		return this.cdDistrito;
	}
	public void setCdBairro(int cdBairro){
		this.cdBairro=cdBairro;
	}
	public int getCdBairro(){
		return this.cdBairro;
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
	}
	public void setCdEndereco(int cdEndereco){
		this.cdEndereco=cdEndereco;
	}
	public int getCdEndereco(){
		return this.cdEndereco;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTpLocalizacao(int tpLocalizacao){
		this.tpLocalizacao=tpLocalizacao;
	}
	public int getTpLocalizacao(){
		return this.tpLocalizacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocalizacao: " +  getCdLocalizacao();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdPais: " +  getCdPais();
		valueToString += ", cdEstado: " +  getCdEstado();
		valueToString += ", cdRegiao: " +  getCdRegiao();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdDistrito: " +  getCdDistrito();
		valueToString += ", cdBairro: " +  getCdBairro();
		valueToString += ", cdLogradouro: " +  getCdLogradouro();
		valueToString += ", cdEndereco: " +  getCdEndereco();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", tpLocalizacao: " +  getTpLocalizacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Localizacao(getCdLocalizacao(),
			getCdReferencia(),
			getCdPais(),
			getCdEstado(),
			getCdRegiao(),
			getCdCidade(),
			getCdDistrito(),
			getCdBairro(),
			getCdLogradouro(),
			getCdEndereco(),
			getCdPessoa(),
			getTpLocalizacao());
	}

}
