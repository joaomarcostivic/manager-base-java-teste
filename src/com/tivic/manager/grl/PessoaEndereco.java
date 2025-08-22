package com.tivic.manager.grl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PessoaEndereco {

	private int cdEndereco;
	private int cdPessoa;
	private String dsEndereco;
	private int cdTipoLogradouro;
	private int cdTipoEndereco;
	private int cdLogradouro;
	private int cdBairro;
	private int cdCidade;
	private String nmLogradouro;
	private String nmBairro;
	private String nrCep;
	private String nrEndereco;
	private String nmComplemento;
	private String nrTelefone;
	private String nmPontoReferencia;
	private int lgCobranca;
	private int lgPrincipal;
	private int tpZona;
	private int tpLocalizacaoDiferenciada;
	
	public PessoaEndereco() { }
	
	public PessoaEndereco(int cdEndereco,
			int cdPessoa,
			String dsEndereco,
			int cdTipoLogradouro,
			int cdTipoEndereco,
			int cdLogradouro,
			int cdBairro,
			int cdCidade,
			String nmLogradouro,
			String nmBairro,
			String nrCep,
			String nrEndereco,
			String nmComplemento,
			String nrTelefone,
			String nmPontoReferencia,
			int lgCobranca,
			int lgPrincipal,
			int tpZona,
			int tpLocalizacaoDiferenciada){
		setCdEndereco(cdEndereco);
		setCdPessoa(cdPessoa);
		setDsEndereco(dsEndereco);
		setCdTipoLogradouro(cdTipoLogradouro);
		setCdTipoEndereco(cdTipoEndereco);
		setCdLogradouro(cdLogradouro);
		setCdBairro(cdBairro);
		setCdCidade(cdCidade);
		setNmLogradouro(nmLogradouro);
		setNmBairro(nmBairro);
		setNrCep(nrCep);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNrTelefone(nrTelefone);
		setNmPontoReferencia(nmPontoReferencia);
		setLgCobranca(lgCobranca);
		setLgPrincipal(lgPrincipal);
		setTpZona(tpZona);
		setTpLocalizacaoDiferenciada(tpLocalizacaoDiferenciada);
	}
	
	public PessoaEndereco(int cdEndereco,
			int cdPessoa,
			String dsEndereco,
			int cdTipoLogradouro,
			int cdTipoEndereco,
			int cdLogradouro,
			int cdBairro,
			int cdCidade,
			String nmLogradouro,
			String nmBairro,
			String nrCep,
			String nrEndereco,
			String nmComplemento,
			String nrTelefone,
			String nmPontoReferencia,
			int lgCobranca,
			int lgPrincipal,
			int tpZona){
		setCdEndereco(cdEndereco);
		setCdPessoa(cdPessoa);
		setDsEndereco(dsEndereco);
		setCdTipoLogradouro(cdTipoLogradouro);
		setCdTipoEndereco(cdTipoEndereco);
		setCdLogradouro(cdLogradouro);
		setCdBairro(cdBairro);
		setCdCidade(cdCidade);
		setNmLogradouro(nmLogradouro);
		setNmBairro(nmBairro);
		setNrCep(nrCep);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNrTelefone(nrTelefone);
		setNmPontoReferencia(nmPontoReferencia);
		setLgCobranca(lgCobranca);
		setLgPrincipal(lgPrincipal);
		setTpZona(tpZona);
	}
	
	public PessoaEndereco(int cdEndereco,
			int cdPessoa,
			String dsEndereco,
			int cdTipoLogradouro,
			int cdTipoEndereco,
			int cdLogradouro,
			int cdBairro,
			int cdCidade,
			String nmLogradouro,
			String nmBairro,
			String nrCep,
			String nrEndereco,
			String nmComplemento,
			String nrTelefone,
			String nmPontoReferencia,
			int lgCobranca,
			int lgPrincipal){
		setCdEndereco(cdEndereco);
		setCdPessoa(cdPessoa);
		setDsEndereco(dsEndereco);
		setCdTipoLogradouro(cdTipoLogradouro);
		setCdTipoEndereco(cdTipoEndereco);
		setCdLogradouro(cdLogradouro);
		setCdBairro(cdBairro);
		setCdCidade(cdCidade);
		setNmLogradouro(nmLogradouro);
		setNmBairro(nmBairro);
		setNrCep(nrCep);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNrTelefone(nrTelefone);
		setNmPontoReferencia(nmPontoReferencia);
		setLgCobranca(lgCobranca);
		setLgPrincipal(lgPrincipal);
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
	public void setDsEndereco(String dsEndereco){
		this.dsEndereco=dsEndereco;
	}
	public String getDsEndereco(){
		return this.dsEndereco;
	}
	public void setCdTipoLogradouro(int cdTipoLogradouro){
		this.cdTipoLogradouro=cdTipoLogradouro;
	}
	public int getCdTipoLogradouro(){
		return this.cdTipoLogradouro;
	}
	public void setCdTipoEndereco(int cdTipoEndereco){
		this.cdTipoEndereco=cdTipoEndereco;
	}
	public int getCdTipoEndereco(){
		return this.cdTipoEndereco;
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
	}
	public void setCdBairro(int cdBairro){
		this.cdBairro=cdBairro;
	}
	public int getCdBairro(){
		return this.cdBairro;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setNmLogradouro(String nmLogradouro){
		this.nmLogradouro=nmLogradouro;
	}
	public String getNmLogradouro(){
		return this.nmLogradouro;
	}
	public void setNmBairro(String nmBairro){
		this.nmBairro=nmBairro;
	}
	public String getNmBairro(){
		return this.nmBairro;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setNrEndereco(String nrEndereco){
		this.nrEndereco=nrEndereco;
	}
	public String getNrEndereco(){
		return this.nrEndereco;
	}
	public void setNmComplemento(String nmComplemento){
		this.nmComplemento=nmComplemento;
	}
	public String getNmComplemento(){
		return this.nmComplemento;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setNmPontoReferencia(String nmPontoReferencia){
		this.nmPontoReferencia=nmPontoReferencia;
	}
	public String getNmPontoReferencia(){
		return this.nmPontoReferencia;
	}
	public void setLgCobranca(int lgCobranca){
		this.lgCobranca=lgCobranca;
	}
	public int getLgCobranca(){
		return this.lgCobranca;
	}
	public void setLgPrincipal(int lgPrincipal){
		this.lgPrincipal=lgPrincipal;
	}
	public int getLgPrincipal(){
		return this.lgPrincipal;
	}
	public void setTpZona(int tpZona) {
		this.tpZona = tpZona;
	}
	public int getTpZona() {
		return tpZona;
	}
	public void setTpLocalizacaoDiferenciada(int tpLocalizacaoDiferenciada) {
		this.tpLocalizacaoDiferenciada = tpLocalizacaoDiferenciada;
	}
	public int getTpLocalizacaoDiferenciada() {
		return tpLocalizacaoDiferenciada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEndereco: " +  getCdEndereco();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", dsEndereco: " +  getDsEndereco();
		valueToString += ", cdTipoLogradouro: " +  getCdTipoLogradouro();
		valueToString += ", cdTipoEndereco: " +  getCdTipoEndereco();
		valueToString += ", cdLogradouro: " +  getCdLogradouro();
		valueToString += ", cdBairro: " +  getCdBairro();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", nmLogradouro: " +  getNmLogradouro();
		valueToString += ", nmBairro: " +  getNmBairro();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", nrEndereco: " +  getNrEndereco();
		valueToString += ", nmComplemento: " +  getNmComplemento();
		valueToString += ", nrTelefone: " +  getNrTelefone();
		valueToString += ", nmPontoReferencia: " +  getNmPontoReferencia();
		valueToString += ", lgCobranca: " +  getLgCobranca();
		valueToString += ", lgPrincipal: " +  getLgPrincipal();
		valueToString += ", tpZona: " +  getTpZona();
		valueToString += ", tpLocalizacaoDiferenciada: " +  getTpLocalizacaoDiferenciada();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaEndereco(getCdEndereco(),
			getCdPessoa(),
			getDsEndereco(),
			getCdTipoLogradouro(),
			getCdTipoEndereco(),
			getCdLogradouro(),
			getCdBairro(),
			getCdCidade(),
			getNmLogradouro(),
			getNmBairro(),
			getNrCep(),
			getNrEndereco(),
			getNmComplemento(),
			getNrTelefone(),
			getNmPontoReferencia(),
			getLgCobranca(),
			getLgPrincipal(),
			getTpZona(),
			getTpLocalizacaoDiferenciada());
	}

}
