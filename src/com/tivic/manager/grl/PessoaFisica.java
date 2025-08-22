package com.tivic.manager.grl;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class PessoaFisica extends com.tivic.manager.grl.Pessoa {

	private int cdNaturalidade;
	private int cdEscolaridade;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtNascimento;
	
	private String nrCpf;
	private String sgOrgaoRg;
	private String nmMae;
	private String nmPai;
	private int tpSexo;
	private int stEstadoCivil;
	private String nrRg;
	private String nrCnh;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtValidadeCnh;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtPrimeiraHabilitacao;
	
	private int tpCategoriaHabilitacao;
	private int tpRaca;
	private int lgDeficienteFisico;
	private String nmFormaTratamento;
	private int cdEstadoRg;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtEmissaoRg;
	
	private byte[] blbFingerprint;
	private int cdConjuge;
	private int qtMembrosFamilia;
	private float vlRendaFamiliarPerCapta;
	private int tpNacionalidade;
	private int tpFiliacaoPai;
	private int lgDocumentoAusente;
	
	public PessoaFisica(){ }

	//Principal
	public PessoaFisica(int cdPessoa,
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
			int tpFiliacaoPai){
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
			dtChegadaPais);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
		setCdConjuge(cdConjuge);
		setQtMembrosFamilia(qtMembrosFamilia);
		setVlRendaFamiliarPerCapta(vlRendaFamiliarPerCapta);
		setTpNacionalidade(tpNacionalidade);
		setTpFiliacaoPai(tpFiliacaoPai);
	}
	
	public PessoaFisica(int cdPessoa,
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
			int lgDocumentoAusente){
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
			cdUsuarioCadastro,
			nrOab,
			nmParceiro);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
		setCdConjuge(cdConjuge);
		setQtMembrosFamilia(qtMembrosFamilia);
		setVlRendaFamiliarPerCapta(vlRendaFamiliarPerCapta);
		setTpNacionalidade(tpNacionalidade);
		setTpFiliacaoPai(tpFiliacaoPai);
		setLgDocumentoAusente(lgDocumentoAusente);
	}
	
	public PessoaFisica(int cdPessoa,
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
			int tpFiliacaoPai,
			int lgDocumentoAusente){
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
			cdUsuarioCadastro,
			nrOab,
			nmParceiro,
			nrCelular2);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
		setCdConjuge(cdConjuge);
		setQtMembrosFamilia(qtMembrosFamilia);
		setVlRendaFamiliarPerCapta(vlRendaFamiliarPerCapta);
		setTpNacionalidade(tpNacionalidade);
		setTpFiliacaoPai(tpFiliacaoPai);
		setLgDocumentoAusente(lgDocumentoAusente);
	}
	
	public PessoaFisica(int cdPessoa,
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
			int tpNacionalidade){
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
			dtChegadaPais);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
		setCdConjuge(cdConjuge);
		setQtMembrosFamilia(qtMembrosFamilia);
		setVlRendaFamiliarPerCapta(vlRendaFamiliarPerCapta);
		setTpNacionalidade(tpNacionalidade);
	}
	
	public PessoaFisica(int cdPessoa,
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
			int tpNacionalidade){
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
			nrCelular2);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
		setCdConjuge(cdConjuge);
		setQtMembrosFamilia(qtMembrosFamilia);
		setVlRendaFamiliarPerCapta(vlRendaFamiliarPerCapta);
		setTpNacionalidade(tpNacionalidade);
	}
	
	public PessoaFisica(int cdPessoa,
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
			float vlRendaFamiliarPerCapta){
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
			dtChegadaPais);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
		setCdConjuge(cdConjuge);
		setQtMembrosFamilia(qtMembrosFamilia);
		setVlRendaFamiliarPerCapta(vlRendaFamiliarPerCapta);
	}
	@Deprecated
	public PessoaFisica(int cdPessoa,
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
			int cdConjuge){
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
			dtChegadaPais);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
		setCdConjuge(cdConjuge);
	}
	@Deprecated
	public PessoaFisica(int cdPessoa,
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
			byte[] blbFingerprint){
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
			dtChegadaPais);
		setCdNaturalidade(cdNaturalidade);
		setCdEscolaridade(cdEscolaridade);
		setDtNascimento(dtNascimento);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNmMae(nmMae);
		setNmPai(nmPai);
		setTpSexo(tpSexo);
		setStEstadoCivil(stEstadoCivil);
		setNrRg(nrRg);
		setNrCnh(nrCnh);
		setDtValidadeCnh(dtValidadeCnh);
		setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
		setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
		setTpRaca(tpRaca);
		setLgDeficienteFisico(lgDeficienteFisico);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setBlbFingerprint(blbFingerprint);
	}
	@Deprecated
	public PessoaFisica(int cdPessoa,
			String nmPessoa,
			String nrCpf,
			String sgOrgaoRg,
			String nrRg,
			String nmFormaTratamento,
			int cdEstadoRg,
			GregorianCalendar dtEmissaoRg,
			GregorianCalendar dtNascimento){
		super(cdPessoa,
			nmPessoa);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNrRg(nrRg);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
		setDtNascimento(dtNascimento);
	}
	public PessoaFisica(int cdPessoa,
			String nmPessoa,
			String nmEmail,
			GregorianCalendar dtCadastro,
			int gnPessoa,
			String nmApelido,
			String txtObservacao,
			String nrCpf,
			String sgOrgaoRg,
			String nrRg,
			String nmFormaTratamento,
			int cdEstadoRg,
			GregorianCalendar dtEmissaoRg){
		super(cdPessoa,
			nmPessoa,
			nmEmail,
			dtCadastro,
			gnPessoa,
			nmApelido,
			txtObservacao);
		setNrCpf(nrCpf);
		setSgOrgaoRg(sgOrgaoRg);
		setNrRg(nrRg);
		setNmFormaTratamento(nmFormaTratamento);
		setCdEstadoRg(cdEstadoRg);
		setDtEmissaoRg(dtEmissaoRg);
	}
	public void setCdNaturalidade(int cdNaturalidade){
		this.cdNaturalidade=cdNaturalidade;
	}
	public int getCdNaturalidade(){
		return this.cdNaturalidade;
	}
	public void setCdEscolaridade(int cdEscolaridade){
		this.cdEscolaridade=cdEscolaridade;
	}
	public int getCdEscolaridade(){
		return this.cdEscolaridade;
	}
	public void setDtNascimento(GregorianCalendar dtNascimento){
		this.dtNascimento=dtNascimento;
	}
	public GregorianCalendar getDtNascimento(){
		return this.dtNascimento;
	}
	public void setNrCpf(String nrCpf){
		this.nrCpf=nrCpf;
	}
	public String getNrCpf(){
		return this.nrCpf;
	}
	public void setSgOrgaoRg(String sgOrgaoRg){
		this.sgOrgaoRg=sgOrgaoRg;
	}
	public String getSgOrgaoRg(){
		return this.sgOrgaoRg;
	}
	public void setNmMae(String nmMae){
		this.nmMae=nmMae;
	}
	public String getNmMae(){
		return this.nmMae;
	}
	public void setNmPai(String nmPai){
		this.nmPai=nmPai;
	}
	public String getNmPai(){
		return this.nmPai;
	}
	public void setTpSexo(int tpSexo){
		this.tpSexo=tpSexo;
	}
	public int getTpSexo(){
		return this.tpSexo;
	}
	public void setStEstadoCivil(int stEstadoCivil){
		this.stEstadoCivil=stEstadoCivil;
	}
	public int getStEstadoCivil(){
		return this.stEstadoCivil;
	}
	public void setNrRg(String nrRg){
		this.nrRg=nrRg;
	}
	public String getNrRg(){
		return this.nrRg;
	}
	public void setNrCnh(String nrCnh){
		this.nrCnh=nrCnh;
	}
	public String getNrCnh(){
		return this.nrCnh;
	}
	public void setDtValidadeCnh(GregorianCalendar dtValidadeCnh){
		this.dtValidadeCnh=dtValidadeCnh;
	}
	public GregorianCalendar getDtValidadeCnh(){
		return this.dtValidadeCnh;
	}
	public void setDtPrimeiraHabilitacao(GregorianCalendar dtPrimeiraHabilitacao){
		this.dtPrimeiraHabilitacao=dtPrimeiraHabilitacao;
	}
	public GregorianCalendar getDtPrimeiraHabilitacao(){
		return this.dtPrimeiraHabilitacao;
	}
	public void setTpCategoriaHabilitacao(int tpCategoriaHabilitacao){
		this.tpCategoriaHabilitacao=tpCategoriaHabilitacao;
	}
	public int getTpCategoriaHabilitacao(){
		return this.tpCategoriaHabilitacao;
	}
	public void setTpRaca(int tpRaca){
		this.tpRaca=tpRaca;
	}
	public int getTpRaca(){
		return this.tpRaca;
	}
	public void setLgDeficienteFisico(int lgDeficienteFisico){
		this.lgDeficienteFisico=lgDeficienteFisico;
	}
	public int getLgDeficienteFisico(){
		return this.lgDeficienteFisico;
	}
	public void setNmFormaTratamento(String nmFormaTratamento){
		this.nmFormaTratamento=nmFormaTratamento;
	}
	public String getNmFormaTratamento(){
		return this.nmFormaTratamento;
	}
	public void setCdEstadoRg(int cdEstadoRg){
		this.cdEstadoRg=cdEstadoRg;
	}
	public int getCdEstadoRg(){
		return this.cdEstadoRg;
	}
	public void setDtEmissaoRg(GregorianCalendar dtEmissaoRg){
		this.dtEmissaoRg=dtEmissaoRg;
	}
	public GregorianCalendar getDtEmissaoRg(){
		return this.dtEmissaoRg;
	}
	public void setBlbFingerprint(byte[] blbFingerprint){
		this.blbFingerprint=blbFingerprint;
	}
	public byte[] getBlbFingerprint(){
		return this.blbFingerprint;
	}
	public void setCdConjuge(int cdConjuge){
		this.cdConjuge=cdConjuge;
	}
	public int getCdConjuge(){
		return this.cdConjuge;
	}
	public void setQtMembrosFamilia(int qtMembrosFamilia){
		this.qtMembrosFamilia=qtMembrosFamilia;
	}
	public int getQtMembrosFamilia(){
		return this.qtMembrosFamilia;
	}
	public void setVlRendaFamiliarPerCapta(float vlRendaFamiliarPerCapta){
		this.vlRendaFamiliarPerCapta=vlRendaFamiliarPerCapta;
	}
	public float getVlRendaFamiliarPerCapta(){
		return this.vlRendaFamiliarPerCapta;
	}
	public void setTpNacionalidade(int tpNacionalidade) {
		this.tpNacionalidade = tpNacionalidade;
	}
	public int getTpNacionalidade() {
		return tpNacionalidade;
	}
	public void setTpFiliacaoPai(int tpFiliacaoPai) {
		this.tpFiliacaoPai = tpFiliacaoPai;
	}
	public int getTpFiliacaoPai() {
		return tpFiliacaoPai;
	}
	public void setLgDocumentoAusente(int lgDocumentoAusente){
		this.lgDocumentoAusente=lgDocumentoAusente;
	}
	public int getLgDocumentoAusente(){
		return this.lgDocumentoAusente;
	}
	
	public String toString() {
		String valueToString = super.toString().replaceAll("\\{", "").replaceAll("\\}", "");
		valueToString += ", \"cdPessoa\": " +  getCdPessoa();
		valueToString += ", \"cdNaturalidade\": " +  getCdNaturalidade();
		valueToString += ", \"cdEscolaridade\": " +  getCdEscolaridade();
		valueToString += ", \"dtNascimento\": \"" +  sol.util.Util.formatDateTime(getDtNascimento(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "")+"\"";
		valueToString += ", \"nrCpf\": \"" +  getNrCpf()+"\"";
		valueToString += ", \"sgOrgaoRg\": \"" +  getSgOrgaoRg()+"\"";
		valueToString += ", \"nmMae\": \"" +  getNmMae()+"\"";
		valueToString += ", \"nmPai\": \"" +  getNmPai()+"\"";
		valueToString += ", \"tpSexo\": " +  getTpSexo();
		valueToString += ", \"stEstadoCivil\": " +  getStEstadoCivil();
		valueToString += ", \"nrRg\": \"" +  getNrRg()+"\"";
		valueToString += ", \"nrCnh\": \"" +  getNrCnh()+"\"";
		valueToString += ", \"dtValidadeCnh\": \"" +  sol.util.Util.formatDateTime(getDtValidadeCnh(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "")+"\"";
		valueToString += ", \"dtPrimeiraHabilitacao\": \"" +  sol.util.Util.formatDateTime(getDtPrimeiraHabilitacao(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "")+"\"";
		valueToString += ", \"tpCategoriaHabilitacao\": " +  getTpCategoriaHabilitacao();
		valueToString += ", \"tpRaca\": " +  getTpRaca();
		valueToString += ", \"lgDeficienteFisico\": " +  getLgDeficienteFisico();
		valueToString += ", \"nmFormaTratamento\": \"" +  getNmFormaTratamento()+"\"";
		valueToString += ", \"cdEstadoRg\": " +  getCdEstadoRg();
		valueToString += ", \"dtEmissaoRg\": \"" +  sol.util.Util.formatDateTime(getDtEmissaoRg(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "")+"\"";
		valueToString += ", \"blbFingerprint\": \"" +  getBlbFingerprint()+"\"";
		valueToString += ", \"cdConjuge\": " +  getCdConjuge();
		valueToString += ", \"qtMembrosFamilia\": " +  getQtMembrosFamilia();
		valueToString += ", \"vlRendaFamiliarPerCapta\": " +  getVlRendaFamiliarPerCapta();
		valueToString += ", \"tpNacionalidade\": " +  getTpNacionalidade();
		valueToString += ", \"tpFiliacaoPai\": " +  getTpFiliacaoPai();
		valueToString += ", \"lgDocumentoAusente\": " + getLgDocumentoAusente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaFisica(getCdPessoa(),
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
			getTpFiliacaoPai());
	}

}