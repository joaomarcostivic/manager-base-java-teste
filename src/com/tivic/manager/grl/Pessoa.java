package com.tivic.manager.grl;

import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "pessoa")
public class Pessoa {

	private int cdPessoa;
	private int cdPessoaSuperior;
	private int cdPais;
	private String nmPessoa;
	private String nrTelefone1;
	private String nrTelefone2;
	private String nrCelular;
	private String nrFax;
	private String nmEmail;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtCadastro;
	
	private int gnPessoa;
	private byte[] imgFoto;
	private int stCadastro;
	private String nmUrl;
	private String nmApelido;
	private String txtObservacao;
	private int lgNotificacao;
	private String idPessoa;
	private int cdClassificacao;
	private int cdFormaDivulgacao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtChegadaPais;
	
	private int cdUsuarioCadastro;
	private String nrOab;
	private String nmParceiro;
	private String nrCelular2;
	
	public Pessoa(){ }
	
	public Pessoa(int cdPessoa,
			int cdPessoaSuperior,
			int cdPais,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			byte[] imgFoto,
			int stCadastro,
			String nmUrl,
			String nmApelido,
			String txtObservacao,
			int lgNotificacao,
			String idPessoa,
			int cdClassificacao,
			int cdFormaDivulgacao,
			GregorianCalendar dtChegadaPais,
			int cdUsuarioCadastro,
			String nrOab,
			String nmParceiro,
			String nrCelular2) {
		setCdPessoa(cdPessoa);
		setCdPessoaSuperior(cdPessoaSuperior);
		setCdPais(cdPais);
		setNmPessoa(nmPessoa);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCelular(nrCelular);
		setNrFax(nrFax);
		setNmEmail(nmEmail);
		setDtCadastro(dtCadastro);
		setGnPessoa(gnPessoa);
		setImgFoto(imgFoto);
		setStCadastro(stCadastro);
		setNmUrl(nmUrl);
		setNmApelido(nmApelido);
		setTxtObservacao(txtObservacao);
		setLgNotificacao(lgNotificacao);
		setIdPessoa(idPessoa);
		setCdClassificacao(cdClassificacao);
		setCdFormaDivulgacao(cdFormaDivulgacao);
		setDtChegadaPais(dtChegadaPais);
		setCdUsuarioCadastro(cdUsuarioCadastro);
		setNrOab(nrOab);
		setNmParceiro(nmParceiro);
		setNrCelular2(nrCelular2);
	}

	public Pessoa(int cdPessoa,
			int cdPessoaSuperior,
			int cdPais,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			byte[] imgFoto,
			int stCadastro,
			String nmUrl,
			String nmApelido,
			String txtObservacao,
			int lgNotificacao,
			String idPessoa,
			int cdClassificacao,
			int cdFormaDivulgacao,
			GregorianCalendar dtChegadaPais,
			int cdUsuarioCadastro,
			String nrOab,
			String nmParceiro) {
		setCdPessoa(cdPessoa);
		setCdPessoaSuperior(cdPessoaSuperior);
		setCdPais(cdPais);
		setNmPessoa(nmPessoa);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCelular(nrCelular);
		setNrFax(nrFax);
		setNmEmail(nmEmail);
		setDtCadastro(dtCadastro);
		setGnPessoa(gnPessoa);
		setImgFoto(imgFoto);
		setStCadastro(stCadastro);
		setNmUrl(nmUrl);
		setNmApelido(nmApelido);
		setTxtObservacao(txtObservacao);
		setLgNotificacao(lgNotificacao);
		setIdPessoa(idPessoa);
		setCdClassificacao(cdClassificacao);
		setCdFormaDivulgacao(cdFormaDivulgacao);
		setDtChegadaPais(dtChegadaPais);
		setCdUsuarioCadastro(cdUsuarioCadastro);
		setNrOab(nrOab);
		setNmParceiro(nmParceiro);
	}

	public Pessoa(int cdPessoa,
			int cdPessoaSuperior,
			int cdPais,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			byte[] imgFoto,
			int stCadastro,
			String nmUrl,
			String nmApelido,
			String txtObservacao,
			int lgNotificacao,
			String idPessoa,
			int cdClassificacao,
			int cdFormaDivulgacao,
			GregorianCalendar dtChegadaPais){
		setCdPessoa(cdPessoa);
		setCdPessoaSuperior(cdPessoaSuperior);
		setCdPais(cdPais);
		setNmPessoa(nmPessoa);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCelular(nrCelular);
		setNrFax(nrFax);
		setNmEmail(nmEmail);
		setDtCadastro(dtCadastro);
		setGnPessoa(gnPessoa);
		setImgFoto(imgFoto);
		setStCadastro(stCadastro);
		setNmUrl(nmUrl);
		setNmApelido(nmApelido);
		setTxtObservacao(txtObservacao);
		setLgNotificacao(lgNotificacao);
		setIdPessoa(idPessoa);
		setCdClassificacao(cdClassificacao);
		setCdFormaDivulgacao(cdFormaDivulgacao);
		setDtChegadaPais(dtChegadaPais);
	}
	
	public Pessoa(int cdPessoa,
			int cdPessoaSuperior,
			int cdPais,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			byte[] imgFoto,
			int stCadastro,
			String nmUrl,
			String nmApelido,
			String txtObservacao,
			int lgNotificacao,
			String idPessoa,
			int cdClassificacao,
			int cdFormaDivulgacao,
			GregorianCalendar dtChegadaPais,
			String nrCelular2){
		setCdPessoa(cdPessoa);
		setCdPessoaSuperior(cdPessoaSuperior);
		setCdPais(cdPais);
		setNmPessoa(nmPessoa);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCelular(nrCelular);
		setNrFax(nrFax);
		setNmEmail(nmEmail);
		setDtCadastro(dtCadastro);
		setGnPessoa(gnPessoa);
		setImgFoto(imgFoto);
		setStCadastro(stCadastro);
		setNmUrl(nmUrl);
		setNmApelido(nmApelido);
		setTxtObservacao(txtObservacao);
		setLgNotificacao(lgNotificacao);
		setIdPessoa(idPessoa);
		setCdClassificacao(cdClassificacao);
		setCdFormaDivulgacao(cdFormaDivulgacao);
		setDtChegadaPais(dtChegadaPais);
		setNrCelular2(nrCelular2);
	}
	/**
	 * Usado para cadastro de pessoa cobrança
	 * @since 13/08/2014
	 * @author Gabriel
	 * @param cdPessoa
	 * @param nmPessoa
	 * @param nmEmail
	 * @param dtCadastro
	 * @param gnPessoa
	 * @param nmApelido
	 * @param txtObservacao
	 */
	public Pessoa(int cdPessoa,
			String nmPessoa,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			String nmApelido,
			String txtObservacao){
		setCdPessoa(cdPessoa);
		setNmPessoa(nmPessoa);
		setNmEmail(nmEmail);
		setDtCadastro(dtCadastro);
		setGnPessoa(gnPessoa);
		setNmApelido(nmApelido);
		setTxtObservacao(txtObservacao);
	}
	//Usado na importação do Educacenso
	public Pessoa(int cdPessoa,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			int stCadastro,
			int lgNotificacao){
		setCdPessoa(cdPessoa);
		setNmPessoa(nmPessoa);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCelular(nrCelular);
		setNrFax(nrFax);
		setNmEmail(nmEmail);
		setDtCadastro(dtCadastro);
		setGnPessoa(gnPessoa);
		setStCadastro(stCadastro);
		setLgNotificacao(lgNotificacao);
	}
	
	@Deprecated
	public Pessoa(int cdPessoa,
			String nmPessoa){
		setCdPessoa(cdPessoa);
		setNmPessoa(nmPessoa);
	}
	
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdPessoaSuperior(int cdPessoaSuperior){
		this.cdPessoaSuperior=cdPessoaSuperior;
	}
	public int getCdPessoaSuperior(){
		return this.cdPessoaSuperior;
	}
	public void setCdPais(int cdPais){
		this.cdPais=cdPais;
	}
	public int getCdPais(){
		return this.cdPais;
	}
	public void setNmPessoa(String nmPessoa){
		this.nmPessoa=nmPessoa;
	}
	public String getNmPessoa(){
		return this.nmPessoa;
	}
	public void setNrTelefone1(String nrTelefone1){
		this.nrTelefone1=nrTelefone1;
	}
	public String getNrTelefone1(){
		return this.nrTelefone1;
	}
	public void setNrTelefone2(String nrTelefone2){
		this.nrTelefone2=nrTelefone2;
	}
	public String getNrTelefone2(){
		return this.nrTelefone2;
	}
	public void setNrCelular(String nrCelular){
		this.nrCelular=nrCelular;
	}
	public String getNrCelular(){
		return this.nrCelular;
	}
	public void setNrFax(String nrFax){
		this.nrFax=nrFax;
	}
	public String getNrFax(){
		return this.nrFax;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setGnPessoa(int gnPessoa){
		this.gnPessoa=gnPessoa;
	}
	public int getGnPessoa(){
		return this.gnPessoa;
	}
	public void setImgFoto(byte[] imgFoto){
		this.imgFoto=imgFoto;
	}
	public byte[] getImgFoto(){
		return this.imgFoto;
	}
	public void setStCadastro(int stCadastro){
		this.stCadastro=stCadastro;
	}
	public int getStCadastro(){
		return this.stCadastro;
	}
	public void setNmUrl(String nmUrl){
		this.nmUrl=nmUrl;
	}
	public String getNmUrl(){
		return this.nmUrl;
	}
	public void setNmApelido(String nmApelido){
		this.nmApelido=nmApelido;
	}
	public String getNmApelido(){
		return this.nmApelido;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setLgNotificacao(int lgNotificacao){
		this.lgNotificacao=lgNotificacao;
	}
	public int getLgNotificacao(){
		return this.lgNotificacao;
	}
	public void setIdPessoa(String idPessoa){
		this.idPessoa=idPessoa;
	}
	public String getIdPessoa(){
		return this.idPessoa;
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setCdFormaDivulgacao(int cdFormaDivulgacao){
		this.cdFormaDivulgacao=cdFormaDivulgacao;
	}
	public int getCdFormaDivulgacao(){
		return this.cdFormaDivulgacao;
	}
	public void setDtChegadaPais(GregorianCalendar dtChegadaPais){
		this.dtChegadaPais=dtChegadaPais;
	}
	public GregorianCalendar getDtChegadaPais(){
		return this.dtChegadaPais;
	}
	public void setCdUsuarioCadastro(int cdUsuarioCadastro){
		this.cdUsuarioCadastro=cdUsuarioCadastro;
	}
	public int getCdUsuarioCadastro(){
		return this.cdUsuarioCadastro;
	}
	public void setNrOab(String nrOab){
		this.nrOab=nrOab;
	}
	public String getNrOab(){
		return this.nrOab;
	}
	public void setNmParceiro(String nmParceiro){
		this.nmParceiro=nmParceiro;
	}
	public String getNmParceiro(){
		return this.nmParceiro;
	}
	public void setNrCelular2(String nrCelular2){
		this.nrCelular2=nrCelular2;
	}
	public String getNrCelular2(){
		return this.nrCelular2;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdPessoa\": " +  getCdPessoa();
		valueToString += ", \"cdPessoaSuperior\": " +  getCdPessoaSuperior();
		valueToString += ", \"cdPais\": " +  getCdPais();
		valueToString += ", \"nmPessoa\": \"" +  getNmPessoa()+"\"";
		valueToString += ", \"nrTelefone1\": \"" +  getNrTelefone1()+"\"";
		valueToString += ", \"nrTelefone2\": \"" +  getNrTelefone2()+"\"";
		valueToString += ", \"nrCelular\": \"" +  getNrCelular()+"\"";
		valueToString += ", \"nrFax\": \"" +  getNrFax()+"\"";
		valueToString += ", \"nmEmail\": \"" +  getNmEmail()+"\"";
		valueToString += ", \"dtCadastro\": \"" +  sol.util.Util.formatDateTime(getDtCadastro(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "")+"\"";
		valueToString += ", \"gnPessoa\": " +  getGnPessoa();
		valueToString += ", \"imgFoto\": \"" +  getImgFoto()+"\"";
		valueToString += ", \"stCadastro\": " +  getStCadastro();
		valueToString += ", \"nmUrl\": \"" +  getNmUrl()+"\"";
		valueToString += ", \"nmApelido\": \"" +  getNmApelido()+"\"";
		valueToString += ", \"txtObservacao\": \"" +  getTxtObservacao()+"\"";
		valueToString += ", \"lgNotificacao\": " +  getLgNotificacao();
		valueToString += ", \"idPessoa\": \"" +  getIdPessoa()+"\"";
		valueToString += ", \"cdClassificacao\": " +  getCdClassificacao();
		valueToString += ", \"cdFormaDivulgacao\": " +  getCdFormaDivulgacao();
		valueToString += ", \"dtChegadaPais\": \"" +  sol.util.Util.formatDateTime(getDtChegadaPais(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "")+"\"";
		valueToString += ", \"cdUsuarioCadastro\": " + getCdUsuarioCadastro();
		valueToString += ", \"nrOab\": \"" +  getNrOab()+"\"";
		valueToString += ", \"nmParceiro\": \"" + getNmParceiro()+"\"";
		valueToString += ", \"nrCelular2\": " +  getNrCelular2();
		return "{" + valueToString + "}";
	}
	
	private Pessoa(Builder builder) {
		this.setCdPessoa(builder.getCdPessoa());
		this.setCdPessoaSuperior(builder.getCdPessoaSuperior());
		this.setCdPais(builder.getCdPais());
		this.setNmPessoa(builder.getNmPessoa());
		this.setNrTelefone1(builder.getNrTelefone1());
		this.setNrTelefone2(builder.getNrTelefone2());
		this.setNrCelular(builder.getNrCelular());
		this.setNrFax(builder.getNrFax());
		this.setNmEmail(builder.getNmEmail());
		this.setDtCadastro(builder.getDtCadastro());
		this.setGnPessoa(builder.getGnPessoa());
		this.setImgFoto(builder.getImgFoto());
		this.setStCadastro(builder.getStCadastro());
		this.setNmUrl(builder.getNmUrl());
		this.setNmApelido(builder.getNmApelido());
		this.setTxtObservacao(builder.getTxtObservacao());
		this.setLgNotificacao(builder.getLgNotificacao());
		this.setIdPessoa(builder.getIdPessoa());
		this.setCdClassificacao(builder.getCdClassificacao());
		this.setCdFormaDivulgacao(builder.getCdFormaDivulgacao());
		this.setDtChegadaPais(builder.getDtChegadaPais());
		this.setCdUsuarioCadastro(builder.getCdUsuarioCadastro());
		this.setNrOab(builder.getNrOab());
		this.setNmParceiro(builder.getNmParceiro());
		this.setNrCelular2(builder.getNrCelular2());
	}
	
	public static class Builder {
		
		private int cdPessoa;
		private String nmPessoa;
		
		private int cdPessoaSuperior;
		private int cdPais;
		private String nrTelefone1;
		private String nrTelefone2;
		private String nrCelular;
		private String nrFax;
		private String nmEmail;
		private GregorianCalendar dtCadastro;
		private int gnPessoa;
		private byte[] imgFoto;
		private int stCadastro;
		private String nmUrl;
		private String nmApelido;
		private String txtObservacao;
		private int lgNotificacao;
		private String idPessoa;
		private int cdClassificacao;
		private int cdFormaDivulgacao;
		private GregorianCalendar dtChegadaPais;
		private int cdUsuarioCadastro;
		private String nrOab;
		private String nmParceiro;
		private String nrCelular2;
		
		
		public Builder(int cdPessoa, String nmPessoa) {
			this.setCdPessoa(cdPessoa);
			this.setNmPessoa(nmPessoa);
		}
		
		public Pessoa build() {
			return new Pessoa(this);
		}

		public int getCdPessoa() {
			return cdPessoa;
		}

		public void setCdPessoa(int cdPessoa) {
			this.cdPessoa = cdPessoa;
		}

		public String getNmPessoa() {
			return nmPessoa;
		}

		public void setNmPessoa(String nmPessoa) {
			this.nmPessoa = nmPessoa;
		}

		public int getCdPessoaSuperior() {
			return cdPessoaSuperior;
		}

		public Builder setCdPessoaSuperior(int cdPessoaSuperior) {
			this.cdPessoaSuperior = cdPessoaSuperior;
			return this;
		}

		public int getCdPais() {
			return cdPais;
		}

		public Builder setCdPais(int cdPais) {
			this.cdPais = cdPais;
			return this;
		}

		public String getNrTelefone1() {
			return nrTelefone1;
		}

		public Builder setNrTelefone1(String nrTelefone1) {
			this.nrTelefone1 = nrTelefone1;
			return this;
		}

		public String getNrTelefone2() {
			return nrTelefone2;
		}

		public Builder setNrTelefone2(String nrTelefone2) {
			this.nrTelefone2 = nrTelefone2;
			return this;
		}

		public String getNrCelular() {
			return nrCelular;
		}

		public Builder setNrCelular(String nrCelular) {
			this.nrCelular = nrCelular;
			return this;
		}

		public String getNrFax() {
			return nrFax;
		}

		public Builder setNrFax(String nrFax) {
			this.nrFax = nrFax;
			return this;
		}

		public String getNmEmail() {
			return nmEmail;
		}

		public Builder setNmEmail(String nmEmail) {
			this.nmEmail = nmEmail;
			return this;
		}

		public GregorianCalendar getDtCadastro() {
			return dtCadastro;
		}

		public Builder setDtCadastro(GregorianCalendar dtCadastro) {
			this.dtCadastro = dtCadastro;
			return this;
		}

		public int getGnPessoa() {
			return gnPessoa;
		}

		public Builder setGnPessoa(int gnPessoa) {
			this.gnPessoa = gnPessoa;
			return this;
		}

		public byte[] getImgFoto() {
			return imgFoto;
		}

		public Builder setImgFoto(byte[] imgFoto) {
			this.imgFoto = imgFoto;
			return this;
		}

		public int getStCadastro() {
			return stCadastro;
		}

		public Builder setStCadastro(int stCadastro) {
			this.stCadastro = stCadastro;
			return this;
		}

		public String getNmUrl() {
			return nmUrl;
		}

		public Builder setNmUrl(String nmUrl) {
			this.nmUrl = nmUrl;
			return this;
		}

		public String getNmApelido() {
			return nmApelido;
		}

		public Builder setNmApelido(String nmApelido) {
			this.nmApelido = nmApelido;
			return this;
		}

		public String getTxtObservacao() {
			return txtObservacao;
		}

		public Builder setTxtObservacao(String txtObservacao) {
			this.txtObservacao = txtObservacao;
			return this;
		}

		public int getLgNotificacao() {
			return lgNotificacao;
		}

		public Builder setLgNotificacao(int lgNotificacao) {
			this.lgNotificacao = lgNotificacao;
			return this;
		}

		public String getIdPessoa() {
			return idPessoa;
		}

		public Builder setIdPessoa(String idPessoa) {
			this.idPessoa = idPessoa;
			return this;
		}

		public int getCdClassificacao() {
			return cdClassificacao;
		}

		public Builder setCdClassificacao(int cdClassificacao) {
			this.cdClassificacao = cdClassificacao;
			return this;
		}

		public int getCdFormaDivulgacao() {
			return cdFormaDivulgacao;
		}

		public Builder setCdFormaDivulgacao(int cdFormaDivulgacao) {
			this.cdFormaDivulgacao = cdFormaDivulgacao;
			return this;
		}

		public GregorianCalendar getDtChegadaPais() {
			return dtChegadaPais;
		}

		public Builder setDtChegadaPais(GregorianCalendar dtChegadaPais) {
			this.dtChegadaPais = dtChegadaPais;
			return this;
		}

		public int getCdUsuarioCadastro() {
			return cdUsuarioCadastro;
		}

		public Builder setCdUsuarioCadastro(int cdUsuarioCadastro) {
			this.cdUsuarioCadastro = cdUsuarioCadastro;
			return this;
		}

		public String getNrOab() {
			return nrOab;
		}

		public Builder setNrOab(String nrOab) {
			this.nrOab = nrOab;
			return this;
		}

		public String getNmParceiro() {
			return nmParceiro;
		}

		public Builder setNmParceiro(String nmParceiro) {
			this.nmParceiro = nmParceiro;
			return this;
		}

		public String getNrCelular2() {
			return nrCelular2;
		}

		public Builder setNrCelular2(String nrCelular2) {
			this.nrCelular2 = nrCelular2;
			return this;
		}
		
	}

}