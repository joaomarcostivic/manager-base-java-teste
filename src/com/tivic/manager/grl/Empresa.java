package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class Empresa extends PessoaJuridica {

	private int cdEmpresa;
	private int lgMatriz;
	private byte[] imgLogomarca;
	private String idEmpresa;
	private int cdTabelaCatEconomica;
	
	public Empresa() { }
	
	public Empresa(int cdEmpresa,
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
			int lgMatriz,
			byte[] imgLogomarca,
			String idEmpresa, 
			int cdTabelaCatEconomica){
		super(cdEmpresa,
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
		setCdEmpresa(cdEmpresa);
		setLgMatriz(lgMatriz);
		setImgLogomarca(imgLogomarca);
		setIdEmpresa(idEmpresa);
		setCdTabelaCatEconomica(cdTabelaCatEconomica);
	}
	
	//Usado para a importação no Educacenso
	public Empresa(int cdPessoa,
			String nmPessoa,
			String nrTelefone1,
			String nrTelefone2,
			String nrCelular,
			String nrFax,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			int stCadastro,
			int lgNotificacao,
			String nrCnpj,
			String nmRazaoSocial,
			int tpEmpresa,
			int lgMatriz){
		super(cdPessoa,
				nmPessoa,
				nrTelefone1,
				nrTelefone2,
				nrCelular,
				nrFax,
				nmEmail,
				dtCadastro,
				gnPessoa,
				stCadastro,
				lgNotificacao,
				nrCnpj,
				nmRazaoSocial,
				tpEmpresa);
			setCdEmpresa(cdPessoa);
			setLgMatriz(lgMatriz);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setLgMatriz(int lgMatriz){
		this.lgMatriz=lgMatriz;
	}
	public int getLgMatriz(){
		return this.lgMatriz;
	}
	public void setImgLogomarca(byte[] imgLogomarca){
		this.imgLogomarca=imgLogomarca;
	}
	public byte[] getImgLogomarca(){
		return this.imgLogomarca;
	}
	public void setIdEmpresa(String idEmpresa){
		this.idEmpresa=idEmpresa;
	}
	public String getIdEmpresa(){
		return this.idEmpresa;
	}
	public int getCdTabelaCatEconomica() {
		return cdTabelaCatEconomica;
	}
	public void setCdTabelaCatEconomica(int cdTabelaCatEconomica) {
		this.cdTabelaCatEconomica = cdTabelaCatEconomica;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdEmpresa\": " +  getCdEmpresa();
		valueToString += ", \"lgMatriz\": " +  getLgMatriz();
		valueToString += ", \"imgLogomarca\": \"" +  getImgLogomarca()+"\"";
		valueToString += ", \"idEmpresa\": \"" +  getIdEmpresa()+"\"";
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += "\"cdEmpresa\": " +  getCdEmpresa();
		valueToString += ", \"lgMatriz\": " +  getLgMatriz();
		valueToString += ", \"imgLogomarca\": \"" +  getImgLogomarca()+"\"";
		valueToString += ", \"idEmpresa\": \"" +  getIdEmpresa()+"\"";
		valueToString += ", \"pessoaJuridica\": " + PessoaJuridicaDAO.get(getCdPessoa());
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Empresa(getCdPessoa(),
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
			getLgMatriz(),
			getImgLogomarca(),
			getIdEmpresa(), 
			getCdTabelaCatEconomica());
	}

}
