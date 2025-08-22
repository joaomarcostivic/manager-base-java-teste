package com.tivic.manager.acd;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;

public class Professor extends PessoaFisica {

	private int cdProfessor;
	private int lgPos;
	private int lgMestrado;
	private int lgDoutorado;
	private String nrInep;
	private int cdModalidadeEducarte;

	public Professor(){ }

	public Professor(int cdPessoa,
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
			int cdNaturalidade,
			int cdEscolaridade,
			GregorianCalendar dtNascimento,
			String nrCpf,
			String sgOrgaoRg,
			String nmMae,
			String nmPai,
			int tpSexo,
			int stEstadoCivil,
			String nrRg,
			String nrCnh,
			GregorianCalendar dtValidadeCnh,
			GregorianCalendar dtPrimeiraHabilitacao,
			int tpCategoriaHabilitacao,
			int tpRaca,
			int lgDeficienteFisico,
			String nmFormaTratamento,
			int cdEstadoRg,
			GregorianCalendar dtEmissaoRg,
			byte[] blbFingerprint,
			int cdConjuge,
			int qtMembrosFamilia,
			float vlRendaFamiliarPerCapta,
			int tpNacionalidade,
			int lgPos,
			int lgMestrado,
			int lgDoutorado,
			String nrInep,
			int cdModalidadeEducarte){
		super(cdPessoa,
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
				cdNaturalidade,
				cdEscolaridade,
				dtNascimento,
				nrCpf,
				sgOrgaoRg,
				nmMae,
				nmPai,
				tpSexo,
				stEstadoCivil,
				nrRg,
				nrCnh,
				dtValidadeCnh,
				dtPrimeiraHabilitacao,
				tpCategoriaHabilitacao,
				tpRaca,
				lgDeficienteFisico,
				nmFormaTratamento,
				cdEstadoRg,
				dtEmissaoRg,
				blbFingerprint,
				cdConjuge,
				qtMembrosFamilia,
				vlRendaFamiliarPerCapta,
				tpNacionalidade);
		setCdProfessor(cdPessoa);
		setLgPos(lgPos);
		setLgMestrado(lgMestrado);
		setLgDoutorado(lgDoutorado);
		setNrInep(nrInep);
		setCdModalidadeEducarte(cdModalidadeEducarte);
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public void setLgPos(int lgPos){
		this.lgPos=lgPos;
	}
	public int getLgPos(){
		return this.lgPos;
	}
	public void setLgMestrado(int lgMestrado){
		this.lgMestrado=lgMestrado;
	}
	public int getLgMestrado(){
		return this.lgMestrado;
	}
	public void setLgDoutorado(int lgDoutorado){
		this.lgDoutorado=lgDoutorado;
	}
	public int getLgDoutorado(){
		return this.lgDoutorado;
	}
	public void setNrInep(String nrInep){
		this.nrInep=nrInep;
	}
	public String getNrInep(){
		return this.nrInep;
	}
	public void setCdModalidadeEducarte(int cdModalidadeEducarte) {
		this.cdModalidadeEducarte = cdModalidadeEducarte;
	}
	public int getCdModalidadeEducarte() {
		return cdModalidadeEducarte;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += super.toString();
		valueToString += "cdProfessor: " +  getCdProfessor();
		valueToString += ", lgPos: " +  getLgPos();
		valueToString += ", lgMestrado: " +  getLgMestrado();
		valueToString += ", lgDoutorado: " +  getLgDoutorado();
		valueToString += ", nrInep: " +  getNrInep();
		valueToString += ", cdModalidadeEducarte: " +  getCdModalidadeEducarte();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdProfessor\": " +  getCdProfessor();
		valueToString += ", \"lgPos\": " +  getLgPos();
		valueToString += ", \"lgMestrado\": " +  getLgMestrado();
		valueToString += ", \"lgDoutorado\": " +  getLgDoutorado();
		valueToString += ", \"nrInep\": \"" +  getNrInep() + "\"";
		valueToString += ", \"cdModalidadeEducarte\": " +  getCdModalidadeEducarte();
		valueToString += ", \"pessoaFisica\": " + PessoaFisicaDAO.get(getCdPessoa()).toString();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Professor(getCdPessoa(),
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
			getCdNaturalidade(),
			getCdEscolaridade(),
			getDtNascimento()==null ? null : (GregorianCalendar)getDtNascimento().clone(),
			getNrCpf(),
			getSgOrgaoRg(),
			getNmMae(),
			getNmPai(),
			getTpSexo(),
			getStEstadoCivil(),
			getNrRg(),
			getNrCnh(),
			getDtValidadeCnh()==null ? null : (GregorianCalendar)getDtValidadeCnh().clone(),
			getDtPrimeiraHabilitacao()==null ? null : (GregorianCalendar)getDtPrimeiraHabilitacao().clone(),
			getTpCategoriaHabilitacao(),
			getTpRaca(),
			getLgDeficienteFisico(),
			getNmFormaTratamento(),
			getCdEstadoRg(),
			getDtEmissaoRg()==null ? null : (GregorianCalendar)getDtEmissaoRg().clone(),
			getBlbFingerprint(),
			getCdConjuge(),
			getQtMembrosFamilia(),
			getVlRendaFamiliarPerCapta(),
			getTpNacionalidade(),
			getLgPos(),
			getLgMestrado(),
			getLgDoutorado(),
			getNrInep(),
			getCdModalidadeEducarte());
	}

}