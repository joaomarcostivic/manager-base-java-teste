package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class PessoaContato {

	private int cdPessoaContato;
	private int cdPessoa;
	private String nmPessoaContato;
	private String nmApelido;
	private String nmSetor;
	private String nrTelefone1;
	private String nrTelefone2;
	private String nrCelular;
	private String nmEmail;
	private String nmUrl;
	private GregorianCalendar dtCadastro;
	private String txtObservacao;
	private int cdFuncao;
	private int cdEndereco;
	private int cdUsuario;
	private String nrCelular2;
	private String nrCelular3;
	private String nrCelular4;
	private int lgEmailPrincipal;
	private int lgEmailExtrato;
	private int lgEmailBoleto;
	private int lgEmailCobranca;
	private int lgEmailNfe;
	private String nmServidorSmtp;
	private String nmServidorPop;
	private int tpTelefone1;
	private int tpTelefone2;
	private int nrTelefone1Ramal;
	private int nrTelefone2Ramal;
	private int nrFaxRamal;
	private int tpCelularOperadora;
	private int tpCelular2Operadora;
	private int tpCelular3Operadora;
	private int tpCelular4Operadora;
	private int lgSmsCelular;
	private int lgSmsCelular2;
	private int lgSmsCelular3;
	private int lgSmsCelular4;
	private String nmTurno;
	private String nmSkype;
	private String nmTipo;

	public PessoaContato(){ }

	public PessoaContato(int cdPessoaContato,
			int cdPessoa,
			String nmPessoaContato,
			String nmApelido,
			String nmSetor,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nmEmail,
			String nmUrl,
			GregorianCalendar dtCadastro,
			String txtObservacao,
			int cdFuncao,
			int cdEndereco,
			int cdUsuario){
		setCdPessoaContato(cdPessoaContato);
		setCdPessoa(cdPessoa);
		setNmPessoaContato(nmPessoaContato);
		setNmApelido(nmApelido);
		setNmSetor(nmSetor);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCelular(nrCelular);
		setNmEmail(nmEmail);
		setNmUrl(nmUrl);
		setDtCadastro(dtCadastro);
		setTxtObservacao(txtObservacao);
		setCdFuncao(cdFuncao);
		setCdEndereco(cdEndereco);
		setCdUsuario(cdUsuario);
	}
	
	public PessoaContato(int cdPessoaContato,
			int cdPessoa,
			String nmPessoaContato,
			String nmApelido,
			String nmSetor,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nmEmail,
			String nmUrl,
			GregorianCalendar dtCadastro,
			String txtObservacao,
			int cdFuncao,
			int cdEndereco,
			int cdUsuario,
			String nrCelular2,
			String nrCelular3,
			String nrCelular4,
			int lgEmailPrincipal,
			int lgEmailExtrato,
			int lgEmailBoleto,
			int lgEmailCobranca,
			int lgEmailNfe,
			String nmServidorSmtp,
			String nmServidorPop,
			int tpTelefone1,
			int tpTelefone2,
			int nrTelefone1Ramal,
			int nrTelefone2Ramal,
			int nrFaxRamal,
			int tpCelularOperadora,
			int tpCelular2Operadora,
			int tpCelular3Operadora,
			int tpCelular4Operadora,
			int lgSmsCelular,
			int lgSmsCelular2,
			int lgSmsCelular3,
			int lgSmsCelular4,
			String nmTurno,
			String nmSkype,
			String nmTipo){
		setCdPessoaContato(cdPessoaContato);
		setCdPessoa(cdPessoa);
		setNmPessoaContato(nmPessoaContato);
		setNmApelido(nmApelido);
		setNmSetor(nmSetor);
		setNrTelefone1(nrTelefone1);
		setNrTelefone2(nrTelefone2);
		setNrCelular(nrCelular);
		setNmEmail(nmEmail);
		setNmUrl(nmUrl);
		setDtCadastro(dtCadastro);
		setTxtObservacao(txtObservacao);
		setCdFuncao(cdFuncao);
		setCdEndereco(cdEndereco);
		setCdUsuario(cdUsuario);
		setNrCelular2(nrCelular2);
		setNrCelular3(nrCelular3);
		setNrCelular4(nrCelular4);
		setLgEmailPrincipal(lgEmailPrincipal);
		setLgEmailExtrato(lgEmailExtrato);
		setLgEmailBoleto(lgEmailBoleto);
		setLgEmailCobranca(lgEmailCobranca);
		setLgEmailNfe(lgEmailNfe);
		setNmServidorSmtp(nmServidorSmtp);
		setNmServidorPop(nmServidorPop);
		setTpTelefone1(tpTelefone1);
		setTpTelefone2(tpTelefone2);
		setNrTelefone1Ramal(nrTelefone1Ramal);
		setNrTelefone2Ramal(nrTelefone2Ramal);
		setNrFaxRamal(nrFaxRamal);
		setTpCelularOperadora(tpCelularOperadora);
		setTpCelular2Operadora(tpCelular2Operadora);
		setTpCelular3Operadora(tpCelular3Operadora);
		setTpCelular4Operadora(tpCelular4Operadora);
		setLgSmsCelular(lgSmsCelular);
		setLgSmsCelular2(lgSmsCelular2);
		setLgSmsCelular3(lgSmsCelular3);
		setLgSmsCelular4(lgSmsCelular4);
		setNmTurno(nmTurno);
		setNmSkype(nmSkype);
		setNmTipo(nmTipo);
	}
	public void setCdPessoaContato(int cdPessoaContato){
		this.cdPessoaContato=cdPessoaContato;
	}
	public int getCdPessoaContato(){
		return this.cdPessoaContato;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setNmPessoaContato(String nmPessoaContato){
		this.nmPessoaContato=nmPessoaContato;
	}
	public String getNmPessoaContato(){
		return this.nmPessoaContato;
	}
	public void setNmApelido(String nmApelido){
		this.nmApelido=nmApelido;
	}
	public String getNmApelido(){
		return this.nmApelido;
	}
	public void setNmSetor(String nmSetor){
		this.nmSetor=nmSetor;
	}
	public String getNmSetor(){
		return this.nmSetor;
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
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setNmUrl(String nmUrl){
		this.nmUrl=nmUrl;
	}
	public String getNmUrl(){
		return this.nmUrl;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdFuncao(int cdFuncao){
		this.cdFuncao=cdFuncao;
	}
	public int getCdFuncao(){
		return this.cdFuncao;
	}
	public void setCdEndereco(int cdEndereco){
		this.cdEndereco=cdEndereco;
	}
	public int getCdEndereco(){
		return this.cdEndereco;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setNrCelular2(String nrCelular2){
		this.nrCelular2=nrCelular2;
	}
	public String getNrCelular2(){
		return this.nrCelular2;
	}
	public void setNrCelular3(String nrCelular3){
		this.nrCelular3=nrCelular3;
	}
	public String getNrCelular3(){
		return this.nrCelular3;
	}
	public void setNrCelular4(String nrCelular4){
		this.nrCelular4=nrCelular4;
	}
	public String getNrCelular4(){
		return this.nrCelular4;
	}
	public void setLgEmailPrincipal(int lgEmailPrincipal){
		this.lgEmailPrincipal=lgEmailPrincipal;
	}
	public int getLgEmailPrincipal(){
		return this.lgEmailPrincipal;
	}
	public void setLgEmailExtrato(int lgEmailExtrato){
		this.lgEmailExtrato=lgEmailExtrato;
	}
	public int getLgEmailExtrato(){
		return this.lgEmailExtrato;
	}
	public void setLgEmailBoleto(int lgEmailBoleto){
		this.lgEmailBoleto=lgEmailBoleto;
	}
	public int getLgEmailBoleto(){
		return this.lgEmailBoleto;
	}
	public void setLgEmailCobranca(int lgEmailCobranca){
		this.lgEmailCobranca=lgEmailCobranca;
	}
	public int getLgEmailCobranca(){
		return this.lgEmailCobranca;
	}
	public void setLgEmailNfe(int lgEmailNfe){
		this.lgEmailNfe=lgEmailNfe;
	}
	public int getLgEmailNfe(){
		return this.lgEmailNfe;
	}
	public void setNmServidorSmtp(String nmServidorSmtp){
		this.nmServidorSmtp=nmServidorSmtp;
	}
	public String getNmServidorSmtp(){
		return this.nmServidorSmtp;
	}
	public void setNmServidorPop(String nmServidorPop){
		this.nmServidorPop=nmServidorPop;
	}
	public String getNmServidorPop(){
		return this.nmServidorPop;
	}
	public void setTpTelefone1(int tpTelefone1){
		this.tpTelefone1=tpTelefone1;
	}
	public int getTpTelefone1(){
		return this.tpTelefone1;
	}
	public void setTpTelefone2(int tpTelefone2){
		this.tpTelefone2=tpTelefone2;
	}
	public int getTpTelefone2(){
		return this.tpTelefone2;
	}
	public void setNrTelefone1Ramal(int nrTelefone1Ramal){
		this.nrTelefone1Ramal=nrTelefone1Ramal;
	}
	public int getNrTelefone1Ramal(){
		return this.nrTelefone1Ramal;
	}
	public void setNrTelefone2Ramal(int nrTelefone2Ramal){
		this.nrTelefone2Ramal=nrTelefone2Ramal;
	}
	public int getNrTelefone2Ramal(){
		return this.nrTelefone2Ramal;
	}
	public void setNrFaxRamal(int nrFaxRamal){
		this.nrFaxRamal=nrFaxRamal;
	}
	public int getNrFaxRamal(){
		return this.nrFaxRamal;
	}
	public void setTpCelularOperadora(int tpCelularOperadora){
		this.tpCelularOperadora=tpCelularOperadora;
	}
	public int getTpCelularOperadora(){
		return this.tpCelularOperadora;
	}
	public void setTpCelular2Operadora(int tpCelular2Operadora){
		this.tpCelular2Operadora=tpCelular2Operadora;
	}
	public int getTpCelular2Operadora(){
		return this.tpCelular2Operadora;
	}
	public void setTpCelular3Operadora(int tpCelular3Operadora){
		this.tpCelular3Operadora=tpCelular3Operadora;
	}
	public int getTpCelular3Operadora(){
		return this.tpCelular3Operadora;
	}
	public void setTpCelular4Operadora(int tpCelular4Operadora){
		this.tpCelular4Operadora=tpCelular4Operadora;
	}
	public int getTpCelular4Operadora(){
		return this.tpCelular4Operadora;
	}
	public void setLgSmsCelular(int lgSmsCelular){
		this.lgSmsCelular=lgSmsCelular;
	}
	public int getLgSmsCelular(){
		return this.lgSmsCelular;
	}
	public void setLgSmsCelular2(int lgSmsCelular2){
		this.lgSmsCelular2=lgSmsCelular2;
	}
	public int getLgSmsCelular2(){
		return this.lgSmsCelular2;
	}
	public void setLgSmsCelular3(int lgSmsCelular3){
		this.lgSmsCelular3=lgSmsCelular3;
	}
	public int getLgSmsCelular3(){
		return this.lgSmsCelular3;
	}
	public void setLgSmsCelular4(int lgSmsCelular4){
		this.lgSmsCelular4=lgSmsCelular4;
	}
	public int getLgSmsCelular4(){
		return this.lgSmsCelular4;
	}
	public void setNmTurno(String nmTurno){
		this.nmTurno=nmTurno;
	}
	public String getNmTurno(){
		return this.nmTurno;
	}
	public void setNmSkype(String nmSkype){
		this.nmSkype=nmSkype;
	}
	public String getNmSkype(){
		return this.nmSkype;
	}
	public void setNmTipo(String nmTipo){
		this.nmTipo=nmTipo;
	}
	public String getNmTipo(){
		return this.nmTipo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoaContato: " +  getCdPessoaContato();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", nmPessoaContato: " +  getNmPessoaContato();
		valueToString += ", nmApelido: " +  getNmApelido();
		valueToString += ", nmSetor: " +  getNmSetor();
		valueToString += ", nrTelefone1: " +  getNrTelefone1();
		valueToString += ", nrTelefone2: " +  getNrTelefone2();
		valueToString += ", nrCelular: " +  getNrCelular();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", nmUrl: " +  getNmUrl();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdFuncao: " +  getCdFuncao();
		valueToString += ", cdEndereco: " +  getCdEndereco();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", nrCelular2: " +  getNrCelular2();
		valueToString += ", nrCelular3: " +  getNrCelular3();
		valueToString += ", nrCelular4: " +  getNrCelular4();
		valueToString += ", lgEmailPrincipal: " +  getLgEmailPrincipal();
		valueToString += ", lgEmailExtrato: " +  getLgEmailExtrato();
		valueToString += ", lgEmailBoleto: " +  getLgEmailBoleto();
		valueToString += ", lgEmailCobranca: " +  getLgEmailCobranca();
		valueToString += ", lgEmailNfe: " +  getLgEmailNfe();
		valueToString += ", nmServidorSmtp: " +  getNmServidorSmtp();
		valueToString += ", nmServidorPop: " +  getNmServidorPop();
		valueToString += ", tpTelefone1: " +  getTpTelefone1();
		valueToString += ", tpTelefone2: " +  getTpTelefone2();
		valueToString += ", nrTelefone1Ramal: " +  getNrTelefone1Ramal();
		valueToString += ", nrTelefone2Ramal: " +  getNrTelefone2Ramal();
		valueToString += ", nrFaxRamal: " +  getNrFaxRamal();
		valueToString += ", tpCelularOperadora: " +  getTpCelularOperadora();
		valueToString += ", tpCelular2Operadora: " +  getTpCelular2Operadora();
		valueToString += ", tpCelular3Operadora: " +  getTpCelular3Operadora();
		valueToString += ", tpCelular4Operadora: " +  getTpCelular4Operadora();
		valueToString += ", lgSmsCelular: " +  getLgSmsCelular();
		valueToString += ", lgSmsCelular2: " +  getLgSmsCelular2();
		valueToString += ", lgSmsCelular3: " +  getLgSmsCelular3();
		valueToString += ", lgSmsCelular4: " +  getLgSmsCelular4();
		valueToString += ", nmTurno: " +  getNmTurno();
		valueToString += ", nmSkype: " +  getNmSkype();
		valueToString += ", nmTipo: " +  getNmTipo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaContato(getCdPessoaContato(),
			getCdPessoa(),
			getNmPessoaContato(),
			getNmApelido(),
			getNmSetor(),
			getNrTelefone1(),
			getNrTelefone2(),
			getNrCelular(),
			getNmEmail(),
			getNmUrl(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getTxtObservacao(),
			getCdFuncao(),
			getCdEndereco(),
			getCdUsuario(),
			getNrCelular2(),
			getNrCelular3(),
			getNrCelular4(),
			getLgEmailPrincipal(),
			getLgEmailExtrato(),
			getLgEmailBoleto(),
			getLgEmailCobranca(),
			getLgEmailNfe(),
			getNmServidorSmtp(),
			getNmServidorPop(),
			getTpTelefone1(),
			getTpTelefone2(),
			getNrTelefone1Ramal(),
			getNrTelefone2Ramal(),
			getNrFaxRamal(),
			getTpCelularOperadora(),
			getTpCelular2Operadora(),
			getTpCelular3Operadora(),
			getTpCelular4Operadora(),
			getLgSmsCelular(),
			getLgSmsCelular2(),
			getLgSmsCelular3(),
			getLgSmsCelular4(),
			getNmTurno(),
			getNmSkype(),
			getNmTipo());
	}

}