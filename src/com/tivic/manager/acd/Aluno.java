package com.tivic.manager.acd;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.PessoaFisicaDAO;

public class Aluno extends com.tivic.manager.grl.PessoaFisica {

	private int cdAluno;
	private int cdResponsavel;
	private String nmParentesco;
	private String nmPlanoSaude;
	private String nmMedico;
	private String nmProfissaoPai;
	private String nmProfissaoMae;
	private int tpFiliacao;
	private String nrInep;
	private String idAluno;
	private int lgBolsaFamilia;
	private int lgMaisEducacao;
	private String nmResponsavel;
	private int tpEscolaridadeMae;
	private int tpEscolaridadePai;
	private int tpEscolaridadeResponsavel;
	private int lgCadastroProblema;
	private int lgPaiNaoDeclarado;
	private int lgFaltaDocumento;
	private int idAlunoCentaurus;
	private String nmAlunoCenso;
	private String nrCodigoEstadual;
	private int tpAvaliacao;
	
	public Aluno(){ }

	public Aluno(int cdPessoa,
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
			int tpFiliacaoPai,
			int cdResponsavel,
			String nmParentesco,
			String nmPlanoSaude,
			String nmMedico,
			String nmProfissaoPai,
			String nmProfissaoMae,
			int tpFiliacao,
			String nrInep,
			String idAluno,
			int lgBolsaFamilia,
			int lgMaisEducacao,
			String nmResponsavel,
			int tpEscolaridadeMae,
			int tpEscolaridadePai,
			int tpEscolaridadeResponsavel){
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
			tpNacionalidade,
			tpFiliacaoPai);
		setCdAluno(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setNmParentesco(nmParentesco);
		setNmPlanoSaude(nmPlanoSaude);
		setNmMedico(nmMedico);
		setNmProfissaoPai(nmProfissaoPai);
		setNmProfissaoMae(nmProfissaoMae);
		setTpFiliacao(tpFiliacao);
		setNrInep(nrInep);
		setIdAluno(idAluno);
		setLgBolsaFamilia(lgBolsaFamilia);	
		setLgMaisEducacao(lgMaisEducacao);
		setNmResponsavel(nmResponsavel);
		setTpEscolaridadeMae(tpEscolaridadeMae);
		setTpEscolaridadePai(tpEscolaridadePai);
		setTpEscolaridadeResponsavel(tpEscolaridadeResponsavel);
	}
	
	public Aluno(int cdPessoa,
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
			int cdResponsavel,
			String nmParentesco,
			String nmPlanoSaude,
			String nmMedico,
			String nmProfissaoPai,
			String nmProfissaoMae,
			int tpFiliacao,
			String nrInep,
			String idAluno,
			int lgBolsaFamilia,
			int lgMaisEducacao,
			String nmResponsavel,
			int tpEscolaridadeMae,
			int tpEscolaridadePai,
			int tpEscolaridadeResponsavel){
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
		setCdAluno(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setNmParentesco(nmParentesco);
		setNmPlanoSaude(nmPlanoSaude);
		setNmMedico(nmMedico);
		setNmProfissaoPai(nmProfissaoPai);
		setNmProfissaoMae(nmProfissaoMae);
		setTpFiliacao(tpFiliacao);
		setNrInep(nrInep);
		setIdAluno(idAluno);
		setLgBolsaFamilia(lgBolsaFamilia);	
		setLgMaisEducacao(lgMaisEducacao);
		setNmResponsavel(nmResponsavel);
		setTpEscolaridadeMae(tpEscolaridadeMae);
		setTpEscolaridadePai(tpEscolaridadePai);
		setTpEscolaridadeResponsavel(tpEscolaridadeResponsavel);
	}
	
	public Aluno(int cdPessoa,
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
			int cdResponsavel,
			String nmParentesco,
			String nmPlanoSaude,
			String nmMedico,
			String nmProfissaoPai,
			String nmProfissaoMae,
			int tpFiliacao,
			String nrInep,
			String idAluno,
			int lgBolsaFamilia,
			int lgMaisEducacao,
			String nmResponsavel,
			int tpEscolaridadeMae,
			int tpEscolaridadePai,
			int tpEscolaridadeResponsavel,
			int lgCadastroProblema,
			int lgPaiNaoDeclarado,
			int lgFaltaDocumento){
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
		setCdAluno(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setNmParentesco(nmParentesco);
		setNmPlanoSaude(nmPlanoSaude);
		setNmMedico(nmMedico);
		setNmProfissaoPai(nmProfissaoPai);
		setNmProfissaoMae(nmProfissaoMae);
		setTpFiliacao(tpFiliacao);
		setNrInep(nrInep);
		setIdAluno(idAluno);
		setLgBolsaFamilia(lgBolsaFamilia);	
		setLgMaisEducacao(lgMaisEducacao);
		setNmResponsavel(nmResponsavel);
		setTpEscolaridadeMae(tpEscolaridadeMae);
		setTpEscolaridadePai(tpEscolaridadePai);
		setTpEscolaridadeResponsavel(tpEscolaridadeResponsavel);
		setLgCadastroProblema(lgCadastroProblema);
		setLgPaiNaoDeclarado(lgPaiNaoDeclarado);
		setLgFaltaDocumento(lgFaltaDocumento);
	}
	
	public Aluno(int cdPessoa,
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
			int cdResponsavel,
			String nmParentesco,
			String nmPlanoSaude,
			String nmMedico,
			String nmProfissaoPai,
			String nmProfissaoMae,
			int tpFiliacao,
			String nrInep,
			String idAluno,
			int lgBolsaFamilia,
			int lgMaisEducacao,
			String nmResponsavel,
			int tpEscolaridadeMae,
			int tpEscolaridadePai,
			int tpEscolaridadeResponsavel,
			int lgCadastroProblema,
			int lgPaiNaoDeclarado,
			int lgFaltaDocumento,
			int idAlunoCentaurus){
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
		setCdAluno(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setNmParentesco(nmParentesco);
		setNmPlanoSaude(nmPlanoSaude);
		setNmMedico(nmMedico);
		setNmProfissaoPai(nmProfissaoPai);
		setNmProfissaoMae(nmProfissaoMae);
		setTpFiliacao(tpFiliacao);
		setNrInep(nrInep);
		setIdAluno(idAluno);
		setLgBolsaFamilia(lgBolsaFamilia);	
		setLgMaisEducacao(lgMaisEducacao);
		setNmResponsavel(nmResponsavel);
		setTpEscolaridadeMae(tpEscolaridadeMae);
		setTpEscolaridadePai(tpEscolaridadePai);
		setTpEscolaridadeResponsavel(tpEscolaridadeResponsavel);
		setLgCadastroProblema(lgCadastroProblema);
		setLgPaiNaoDeclarado(lgPaiNaoDeclarado);
		setLgFaltaDocumento(lgFaltaDocumento);
		setIdAlunoCentaurus(idAlunoCentaurus);
	}
	
	public Aluno(int cdPessoa,
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
			int cdResponsavel,
			String nmParentesco,
			String nmPlanoSaude,
			String nmMedico,
			String nmProfissaoPai,
			String nmProfissaoMae,
			int tpFiliacao,
			String nrInep,
			String idAluno,
			int lgBolsaFamilia,
			int lgMaisEducacao,
			String nmResponsavel,
			int tpEscolaridadeMae,
			int tpEscolaridadePai,
			int tpEscolaridadeResponsavel,
			int lgCadastroProblema,
			int lgPaiNaoDeclarado,
			int lgFaltaDocumento,
			int idAlunoCentaurus,
			String nmAlunoCenso){
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
		setCdAluno(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setNmParentesco(nmParentesco);
		setNmPlanoSaude(nmPlanoSaude);
		setNmMedico(nmMedico);
		setNmProfissaoPai(nmProfissaoPai);
		setNmProfissaoMae(nmProfissaoMae);
		setTpFiliacao(tpFiliacao);
		setNrInep(nrInep);
		setIdAluno(idAluno);
		setLgBolsaFamilia(lgBolsaFamilia);	
		setLgMaisEducacao(lgMaisEducacao);
		setNmResponsavel(nmResponsavel);
		setTpEscolaridadeMae(tpEscolaridadeMae);
		setTpEscolaridadePai(tpEscolaridadePai);
		setTpEscolaridadeResponsavel(tpEscolaridadeResponsavel);
		setLgCadastroProblema(lgCadastroProblema);
		setLgPaiNaoDeclarado(lgPaiNaoDeclarado);
		setLgFaltaDocumento(lgFaltaDocumento);
		setIdAlunoCentaurus(idAlunoCentaurus);
		setNmAlunoCenso(nmAlunoCenso);
	}
	
	public Aluno(int cdPessoa,
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
			int cdResponsavel,
			String nmParentesco,
			String nmPlanoSaude,
			String nmMedico,
			String nmProfissaoPai,
			String nmProfissaoMae,
			int tpFiliacao,
			String nrInep,
			String idAluno,
			int lgBolsaFamilia,
			int lgMaisEducacao,
			String nmResponsavel,
			int tpEscolaridadeMae,
			int tpEscolaridadePai,
			int tpEscolaridadeResponsavel,
			int lgCadastroProblema,
			int lgPaiNaoDeclarado,
			int lgFaltaDocumento,
			int idAlunoCentaurus,
			String nmAlunoCenso,
			String nrCodigoEstadual){
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
		setCdAluno(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setNmParentesco(nmParentesco);
		setNmPlanoSaude(nmPlanoSaude);
		setNmMedico(nmMedico);
		setNmProfissaoPai(nmProfissaoPai);
		setNmProfissaoMae(nmProfissaoMae);
		setTpFiliacao(tpFiliacao);
		setNrInep(nrInep);
		setIdAluno(idAluno);
		setLgBolsaFamilia(lgBolsaFamilia);	
		setLgMaisEducacao(lgMaisEducacao);
		setNmResponsavel(nmResponsavel);
		setTpEscolaridadeMae(tpEscolaridadeMae);
		setTpEscolaridadePai(tpEscolaridadePai);
		setTpEscolaridadeResponsavel(tpEscolaridadeResponsavel);
		setLgCadastroProblema(lgCadastroProblema);
		setLgPaiNaoDeclarado(lgPaiNaoDeclarado);
		setLgFaltaDocumento(lgFaltaDocumento);
		setIdAlunoCentaurus(idAlunoCentaurus);
		setNmAlunoCenso(nmAlunoCenso);
		setNrCodigoEstadual(nrCodigoEstadual);
	}
	
	public Aluno(int cdPessoa,
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
			String nrCelular2,
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
			int cdResponsavel,
			String nmParentesco,
			String nmPlanoSaude,
			String nmMedico,
			String nmProfissaoPai,
			String nmProfissaoMae,
			int tpFiliacao,
			String nrInep,
			String idAluno,
			int lgBolsaFamilia,
			int lgMaisEducacao,
			String nmResponsavel,
			int tpEscolaridadeMae,
			int tpEscolaridadePai,
			int tpEscolaridadeResponsavel,
			int lgCadastroProblema,
			int lgPaiNaoDeclarado,
			int lgFaltaDocumento,
			int idAlunoCentaurus,
			String nmAlunoCenso,
			String nrCodigoEstadual,
			int tpAvaliacao){
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
			nrCelular2,
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
		setCdAluno(cdPessoa);
		setCdResponsavel(cdResponsavel);
		setNmParentesco(nmParentesco);
		setNmPlanoSaude(nmPlanoSaude);
		setNmMedico(nmMedico);
		setNmProfissaoPai(nmProfissaoPai);
		setNmProfissaoMae(nmProfissaoMae);
		setTpFiliacao(tpFiliacao);
		setNrInep(nrInep);
		setIdAluno(idAluno);
		setLgBolsaFamilia(lgBolsaFamilia);	
		setLgMaisEducacao(lgMaisEducacao);
		setNmResponsavel(nmResponsavel);
		setTpEscolaridadeMae(tpEscolaridadeMae);
		setTpEscolaridadePai(tpEscolaridadePai);
		setTpEscolaridadeResponsavel(tpEscolaridadeResponsavel);
		setLgCadastroProblema(lgCadastroProblema);
		setLgPaiNaoDeclarado(lgPaiNaoDeclarado);
		setLgFaltaDocumento(lgFaltaDocumento);
		setIdAlunoCentaurus(idAlunoCentaurus);
		setNmAlunoCenso(nmAlunoCenso);
		setNrCodigoEstadual(nrCodigoEstadual);
		setTpAvaliacao(tpAvaliacao);
	}
	
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setNmParentesco(String nmParentesco){
		this.nmParentesco=nmParentesco;
	}
	public String getNmParentesco(){
		return this.nmParentesco;
	}
	public void setNmPlanoSaude(String nmPlanoSaude){
		this.nmPlanoSaude=nmPlanoSaude;
	}
	public String getNmPlanoSaude(){
		return this.nmPlanoSaude;
	}
	public void setNmMedico(String nmMedico){
		this.nmMedico=nmMedico;
	}
	public String getNmMedico(){
		return this.nmMedico;
	}
	public void setNmProfissaoPai(String nmProfissaoPai){
		this.nmProfissaoPai=nmProfissaoPai;
	}
	public String getNmProfissaoPai(){
		return this.nmProfissaoPai;
	}
	public void setNmProfissaoMae(String nmProfissaoMae){
		this.nmProfissaoMae=nmProfissaoMae;
	}
	public String getNmProfissaoMae(){
		return this.nmProfissaoMae;
	}
	public void setTpFiliacao(int tpFiliacao){
		this.tpFiliacao=tpFiliacao;
	}
	public int getTpFiliacao(){
		return this.tpFiliacao;
	}
	public void setNrInep(String nrInep){
		this.nrInep=nrInep;
	}
	public String getNrInep(){
		return this.nrInep;
	}
	public void setIdAluno(String idAluno){
		this.idAluno=idAluno;
	}
	public String getIdAluno(){
		return this.idAluno;
	}
	public void setLgBolsaFamilia(int lgBolsaFamilia) {
		this.lgBolsaFamilia = lgBolsaFamilia;
	}
	public int getLgBolsaFamilia() {
		return lgBolsaFamilia;
	}
	public void setLgMaisEducacao(int lgMaisEducacao) {
		this.lgMaisEducacao = lgMaisEducacao;
	}
	public int getLgMaisEducacao() {
		return lgMaisEducacao;
	}
	public void setNmResponsavel(String nmResponsavel){
		this.nmResponsavel=nmResponsavel;
	}
	public String getNmResponsavel(){
		return this.nmResponsavel;
	}
	public void setTpEscolaridadeMae(int tpEscolaridadeMae){
		this.tpEscolaridadeMae=tpEscolaridadeMae;
	}
	public int getTpEscolaridadeMae(){
		return this.tpEscolaridadeMae;
	}
	public void setTpEscolaridadePai(int tpEscolaridadePai){
		this.tpEscolaridadePai=tpEscolaridadePai;
	}
	public int getTpEscolaridadePai(){
		return this.tpEscolaridadePai;
	}
	public void setTpEscolaridadeResponsavel(int tpEscolaridadeResponsavel){
		this.tpEscolaridadeResponsavel=tpEscolaridadeResponsavel;
	}
	public int getTpEscolaridadeResponsavel(){
		return this.tpEscolaridadeResponsavel;
	}
	public void setLgCadastroProblema(int lgCadastroProblema) {
		this.lgCadastroProblema = lgCadastroProblema;
	}
	public int getLgCadastroProblema() {
		return lgCadastroProblema;
	}
	public void setLgPaiNaoDeclarado(int lgPaiNaoDeclarado) {
		this.lgPaiNaoDeclarado = lgPaiNaoDeclarado;
	}
	public int getLgPaiNaoDeclarado() {
		return lgPaiNaoDeclarado;
	}
	public void setLgFaltaDocumento(int lgFaltaDocumento) {
		this.lgFaltaDocumento = lgFaltaDocumento;
	}
	public int getLgFaltaDocumento() {
		return lgFaltaDocumento;
	}
	public void setIdAlunoCentaurus(int idAlunoCentaurus) {
		this.idAlunoCentaurus = idAlunoCentaurus;
	}
	public int getIdAlunoCentaurus() {
		return idAlunoCentaurus;
	}
	public void setNmAlunoCenso(String nmAlunoCenso) {
		this.nmAlunoCenso = nmAlunoCenso;
	}
	public String getNmAlunoCenso() {
		return nmAlunoCenso;
	}
	public void setNrCodigoEstadual(String nrCodigoEstadual) {
		this.nrCodigoEstadual = nrCodigoEstadual;
	}
	public String getNrCodigoEstadual() {
		return nrCodigoEstadual;
	}
	public void setTpAvaliacao(int tpAvaliacao) {
		this.tpAvaliacao = tpAvaliacao;
	}
	public int getTpAvaliacao() {
		return tpAvaliacao;
	}
	public String toString() {
		String valueToString = super.toString().replaceAll("\\{", "").replaceAll("\\}", "");
		valueToString += ", \"cdAluno\": " +  getCdAluno();
		valueToString += ", \"cdResponsavel\": " +  getCdResponsavel();
		valueToString += ", \"nmParentesco\": \"" +  getNmParentesco()+"\"";
		valueToString += ", \"nmPlanoSaude\": \"" +  getNmPlanoSaude()+"\"";
		valueToString += ", \"nmMedico\": \"" +  getNmMedico()+"\"";
		valueToString += ", \"nmProfissaoPai\": \"" +  getNmProfissaoPai()+"\"";
		valueToString += ", \"nmProfissaoMae\": \"" +  getNmProfissaoMae()+"\"";
		valueToString += ", \"tpFiliacao\": " +  getTpFiliacao();
		valueToString += ", \"nrInep\": \"" +  getNrInep()+"\"";
		valueToString += ", \"idAluno\": \"" +  getIdAluno()+"\"";
		valueToString += ", \"lgBolsaFamilia\": " +  getLgBolsaFamilia();
		valueToString += ", \"lgMaisEducacao\": " +  getLgMaisEducacao();
		valueToString += ", \"nmResponsavel\": \"" + getNmResponsavel()+"\"";
		valueToString += ", \"tpEscolaridadeMae\": " +  getTpEscolaridadeMae();
		valueToString += ", \"tpEscolaridadePai\": " +  getTpEscolaridadePai();
		valueToString += ", \"tpEscolaridadeResponsavel\": " +  getTpEscolaridadeResponsavel();
		valueToString += ", \"lgCadastroProblema\": " +  getLgCadastroProblema();
		valueToString += ", \"lgPaiNaoDeclarado\": " +  getLgPaiNaoDeclarado();
		valueToString += ", \"lgFaltaDocumento\": " +  getLgFaltaDocumento();
		valueToString += ", \"idAlunoCentaurus\": \"" +  getIdAlunoCentaurus()+"\"";
		valueToString += ", \"nmAlunoCenso\": \"" +  getNmAlunoCenso()+"\"";
		valueToString += ", \"nrCodigoEstadual\": \"" +  getNrCodigoEstadual()+"\"";
		valueToString += ", \"tpAvaliacao\": " +  getTpAvaliacao();
		return "{" + valueToString + "}";
	}
	
	public String toORM() {
		String valueToString = "";
		valueToString += ", \"cdAluno\": " +  getCdAluno();
		valueToString += ", \"cdResponsavel\": " +  getCdResponsavel();
		valueToString += ", \"nmParentesco\": \"" +  getNmParentesco()+"\"";
		valueToString += ", \"nmPlanoSaude\": \"" +  getNmPlanoSaude()+"\"";
		valueToString += ", \"nmMedico\": \"" +  getNmMedico()+"\"";
		valueToString += ", \"nmProfissaoPai\": \"" +  getNmProfissaoPai()+"\"";
		valueToString += ", \"nmProfissaoMae\": \"" +  getNmProfissaoMae()+"\"";
		valueToString += ", \"tpFiliacao\": " +  getTpFiliacao();
		valueToString += ", \"nrInep\": \"" +  getNrInep()+"\"";
		valueToString += ", \"idAluno\": \"" +  getIdAluno()+"\"";
		valueToString += ", \"lgBolsaFamilia\": " +  getLgBolsaFamilia();
		valueToString += ", \"lgMaisEducacao\": " +  getLgMaisEducacao();
		valueToString += ", \"nmResponsavel\": \"" + getNmResponsavel()+"\"";
		valueToString += ", \"tpEscolaridadeMae\": " +  getTpEscolaridadeMae();
		valueToString += ", \"tpEscolaridadePai\": " +  getTpEscolaridadePai();
		valueToString += ", \"tpEscolaridadeResponsavel\": " +  getTpEscolaridadeResponsavel();
		valueToString += ", \"lgCadastroProblema\": " +  getLgCadastroProblema();
		valueToString += ", \"lgPaiNaoDeclarado\": " +  getLgPaiNaoDeclarado();
		valueToString += ", \"lgFaltaDocumento\": " +  getLgFaltaDocumento();
		valueToString += ", \"idAlunoCentaurus\": \"" +  getIdAlunoCentaurus()+"\"";
		valueToString += ", \"nmAlunoCenso\": \"" +  getNmAlunoCenso()+"\"";
		valueToString += ", \"nrCodigoEstadual\": \"" +  getNrCodigoEstadual()+"\"";
		valueToString += ", \"tpAvaliacao\": " +  getTpAvaliacao();
		valueToString += ", \"pessoaFisica\": " + PessoaFisicaDAO.get(getCdPessoa()).toString();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Aluno(getCdPessoa(),
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
			getNrCelular2(),
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
			getCdAluno(),
			getNmParentesco(),
			getNmPlanoSaude(),
			getNmMedico(),
			getNmProfissaoPai(),
			getNmProfissaoMae(),
			getTpFiliacao(),
			getNrInep(),
			getIdAluno(),
			getLgBolsaFamilia(),
			getLgMaisEducacao(),
			getNmResponsavel(),
			getTpEscolaridadeMae(),
			getTpEscolaridadePai(),
			getTpEscolaridadeResponsavel(),
			getLgCadastroProblema(),
			getLgPaiNaoDeclarado(),
			getLgFaltaDocumento(),
			getIdAlunoCentaurus(),
			getNmAlunoCenso(),
			getNrCodigoEstadual(),
			getTpAvaliacao());
	}

}