	package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class Agencia extends com.tivic.manager.grl.PessoaJuridica {

	private int cdAgencia;
	private int cdBanco;
	private String nmContato;
	private String nrAgencia;
	
	public Agencia(){ }
	
	public Agencia(int cdAgencia,
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
			String nrCnpj,
			String nmRazaoSocial,
			String nrInscricaoEstadual,
			String nrInscricaoMunicipal,
			int nrFuncionarios,
			GregorianCalendar dtInicioAtividade,
			int cdNaturezaJuridica,
			int tpEmpresa,
			GregorianCalendar dtTerminoAtividade,
			int cdBanco,
			String nmContato,
			String nrAgencia){
		super(cdAgencia,
				cdPessoaSuperior,
				cdPais,
				nmPessoa,
				nrTelefone1,
				nrTelefone2,
				nrCelular,
				nrFax,
				nmEmail,
				dtCadastro,
				gnPessoa,
				imgFoto,
				stCadastro,
				nmUrl,
				nmApelido,
				txtObservacao,
				lgNotificacao,
				idPessoa,
				cdClassificacao,
				cdFormaDivulgacao,
				dtChegadaPais,
				nrCnpj,
				nmRazaoSocial,
				nrInscricaoEstadual,
				nrInscricaoMunicipal,
				nrFuncionarios,
				dtInicioAtividade,
				cdNaturezaJuridica,
				tpEmpresa,
				dtTerminoAtividade);
		setCdAgencia(cdAgencia);
		setCdBanco(cdBanco);
		setNmContato(nmContato);
		setNrAgencia(nrAgencia);
	}
	public void setCdAgencia(int cdAgencia){
		this.cdAgencia=cdAgencia;
	}
	public int getCdAgencia(){
		return this.cdAgencia;
	}
	public void setCdBanco(int cdBanco){
		this.cdBanco=cdBanco;
	}
	public int getCdBanco(){
		return this.cdBanco;
	}
	public void setNmContato(String nmContato){
		this.nmContato=nmContato;
	}
	public String getNmContato(){
		return this.nmContato;
	}
	public void setNrAgencia(String nrAgencia){
		this.nrAgencia=nrAgencia;
	}
	public String getNrAgencia(){
		return this.nrAgencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgencia: " +  getCdAgencia();
		valueToString += ", cdBanco: " +  getCdBanco();
		valueToString += ", nmContato: " +  getNmContato();
		valueToString += ", nrAgencia: " +  getNrAgencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Agencia(getCdPessoa(),
				getCdPessoaSuperior(),
				getCdPais(),
				getNmPessoa(),
				getNrTelefone1(),
				getNrTelefone2(),
				getNrCelular(),
				getNrFax(),
				getNmEmail(),
				getDtCadastro(),
				getGnPessoa(),
				getImgFoto(),
				getStCadastro(),
				getNmUrl(),
				getNmApelido(),
				getTxtObservacao(),
				getLgNotificacao(),
				getIdPessoa(),
				getCdClassificacao(),
				getCdFormaDivulgacao(),
				getDtChegadaPais(),
				getNrCnpj(),
				getNmRazaoSocial(),
				getNrInscricaoEstadual(),
				getNrInscricaoMunicipal(),
				getNrFuncionarios(),
				getDtInicioAtividade()==null ? null : (GregorianCalendar)getDtInicioAtividade().clone(),
				getCdNaturezaJuridica(),
				getTpEmpresa(),
				getDtTerminoAtividade()==null ? null : (GregorianCalendar)getDtTerminoAtividade().clone(),
				getCdBanco(),
				getNmContato(),
				getNrAgencia());
	}

}