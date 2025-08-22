package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class PessoaFichaMedica extends com.tivic.manager.grl.PessoaFisica {

	private int tpSangue;
	private int tpFatorRh;
	private GregorianCalendar dtCriacao;
	private GregorianCalendar dtAlteracao;
	private int cdUsuario;
	private String txtDescricao;
	private String nrCartaoSus;

	public PessoaFichaMedica(){ }

	public PessoaFichaMedica(int cdPessoa,
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
			int tpSangue,
			int tpFatorRh,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtAlteracao,
			int cdUsuario,
			String txtDescricao,
			String nrCartaoSus){
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
		setTpSangue(tpSangue);
		setTpFatorRh(tpFatorRh);
		setDtCriacao(dtCriacao);
		setDtAlteracao(dtAlteracao);
		setCdUsuario(cdUsuario);
		setTxtDescricao(txtDescricao);
		setNrCartaoSus(nrCartaoSus);
	}
	
	public PessoaFichaMedica(int cdPessoa,
			int tpSangue,
			int tpFatorRh,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtAlteracao,
			int cdUsuario,
			String txtDescricao){
		setCdPessoa(cdPessoa);
		setTpSangue(tpSangue);
		setTpFatorRh(tpFatorRh);
		setDtCriacao(dtCriacao);
		setDtAlteracao(dtAlteracao);
		setCdUsuario(cdUsuario);
		setTxtDescricao(txtDescricao);
		setNrCartaoSus(nrCartaoSus);
	}
	public void setTpSangue(int tpSangue){
		this.tpSangue=tpSangue;
	}
	public int getTpSangue(){
		return this.tpSangue;
	}
	public void setTpFatorRh(int tpFatorRh){
		this.tpFatorRh=tpFatorRh;
	}
	public int getTpFatorRh(){
		return this.tpFatorRh;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setDtAlteracao(GregorianCalendar dtAlteracao){
		this.dtAlteracao=dtAlteracao;
	}
	public GregorianCalendar getDtAlteracao(){
		return this.dtAlteracao;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setNrCartaoSus(String nrCartaoSus){
		this.nrCartaoSus=nrCartaoSus;
	}
	public String getNrCartaoSus(){
		return this.nrCartaoSus;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", tpSangue: " +  getTpSangue();
		valueToString += ", tpFatorRh: " +  getTpFatorRh();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAlteracao: " +  sol.util.Util.formatDateTime(getDtAlteracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", nrCartaoSus: " +  getNrCartaoSus();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaFichaMedica(getCdPessoa(),
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
			getDtNascimento(),
			getNrCpf(),
			getSgOrgaoRg(),
			getNmMae(),
			getNmPai(),
			getTpSexo(),
			getStEstadoCivil(),
			getNrRg(),
			getNrCnh(),
			getDtValidadeCnh(),
			getDtPrimeiraHabilitacao(),
			getTpCategoriaHabilitacao(),
			getTpRaca(),
			getLgDeficienteFisico(),
			getNmFormaTratamento(),
			getCdEstadoRg(),
			getDtEmissaoRg(),
			getBlbFingerprint(),
			getCdConjuge(),
			getQtMembrosFamilia(),
			getVlRendaFamiliarPerCapta(),
			getTpNacionalidade(),
			getTpSangue(),
			getTpFatorRh(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getDtAlteracao()==null ? null : (GregorianCalendar)getDtAlteracao().clone(),
			getCdUsuario(),
			getTxtDescricao(),
			getNrCartaoSus());
	}

}