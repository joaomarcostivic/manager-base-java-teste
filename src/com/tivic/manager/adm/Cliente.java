package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class Cliente {

	private int cdEmpresa;
	private int cdPessoa;
	private int lgConvenio;
	private int lgEcommerce;
	private int lgLimiteCredito;
	private int lgAgenda;
	private int nrDiasCarenciaFatura;
	private float vlLimiteCredito;
	private float vlLimiteMensal;
	private float vlLimiteFactoring;
	private float vlLimiteFactoringEmissor;
	private float vlLimiteFactoringUnitario;
	private int lgPista;
	private int lgLoja;
	private int lgVeiculosCadastrados;
	private int nrLimiteAbastecimentos;
	private float vlLimiteVale;
	private int cdConvenio;
	private int cdTabelaPreco;
	private int cdCidade;
	private int cdTipoLogradouro;
	private String nmCargo;
	private String nmLogradouro;
	private String nrEndereco;
	private String nmComplemento;
	private String nmBairro;
	private String nmPontoReferencia;
	private String nrCep;
	private String nmContato;
	private String nmEmail;
	private int nrDependentes;
	private float vlSalario;
	private int stAluguel;
	private float vlAluguel;
	private GregorianCalendar dtAdmissao;
	private String nmOutraRenda;
	private float vlOutraRenda;
	private int cdFaixaRenda;
	private int lgControleVeiculo;
	private String nmEmpresaTrabalho;
	private String nrTelefoneTrabalho;
	private int qtPrazoMinimoFactoring;
	private int qtPrazoMaximoFactoring;
	private int qtIdadeMinimaFactoring;
	private float vlGanhoMinimoFactoring;
	private float prTaxaMinimaFactoring;
	private float vlTaxaDevolucaoFactoring;
	private float prTaxaPadraoFactoring;
	private float prTaxaJurosFactoring;
	private float prTaxaProrrogacaoFactoring;
	private int qtMaximoDocumento;
	private int cdClassificacaoCliente;
	private String nrCodigoBarras;
	private int cdRota;
	private int lgAnalise;
	private int cdProfissao;
	private int cdPessoaCobranca;
	private int cdCobranca;
	private int cdProgramaFatura;
	private String txtObservacao;
	private int tpCredito;
	private int lgSPC;
	private int lgSERASA;
	
	/**
	 * Construtor usado para transformar aquelas pessoas que tem vinculo com cliente em registros em adm_cliente
	 * @since 12/08/2014
	 * @author Gabriel
	 * @param cdEmpresa
	 * @param cdPessoa
	 */
	public Cliente(int cdEmpresa,
			int cdPessoa){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
	}
	
	
	public Cliente(int cdEmpresa,
			int cdPessoa,
			int lgConvenio,
			int lgEcommerce,
			int lgLimiteCredito,
			int lgAgenda,
			int nrDiasCarenciaFatura,
			float vlLimiteCredito,
			float vlLimiteMensal,
			float vlLimiteFactoring,
			float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario,
			int lgPista,
			int lgLoja,
			int lgVeiculosCadastrados,
			int nrLimiteAbastecimentos,
			float vlLimiteVale,
			int cdConvenio,
			int cdTabelaPreco,
			int cdCidade,
			int cdTipoLogradouro,
			String nmCargo,
			String nmLogradouro,
			String nrEndereco,
			String nmComplemento,
			String nmBairro,
			String nmPontoReferencia,
			String nrCep,
			String nmContato,
			String nmEmail,
			int nrDependentes,
			float vlSalario,
			int stAluguel,
			float vlAluguel,
			GregorianCalendar dtAdmissao,
			String nmOutraRenda,
			float vlOutraRenda,
			int cdFaixaRenda,
			int lgControleVeiculo,
			String nmEmpresaTrabalho,
			String nrTelefoneTrabalho,
			int qtPrazoMinimoFactoring,
			int qtPrazoMaximoFactoring,
			int qtIdadeMinimaFactoring,
			float vlGanhoMinimoFactoring,
			float prTaxaMinimaFactoring,
			float vlTaxaDevolucaoFactoring,
			float prTaxaPadraoFactoring,
			float prTaxaJurosFactoring,
			float prTaxaProrrogacaoFactoring,
			int qtMaximoDocumento,
			int cdClassificacaoCliente,
			String nrCodigoBarras,
			int cdRota,
			int lgAnalise,
			int cdProfissao,
			int cdPessoaCobranca,
			int cdCobranca,
			int cdProgramaFatura,
			String txtObservacao,
			int tpCredito){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setLgConvenio(lgConvenio);
		setLgEcommerce(lgEcommerce);
		setLgLimiteCredito(lgLimiteCredito);
		setLgAgenda(lgAgenda);
		setNrDiasCarenciaFatura(nrDiasCarenciaFatura);
		setVlLimiteCredito(vlLimiteCredito);
		setVlLimiteMensal(vlLimiteMensal);
		setVlLimiteFactoring(vlLimiteFactoring);
		setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
		setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
		setLgPista(lgPista);
		setLgLoja(lgLoja);
		setLgVeiculosCadastrados(lgVeiculosCadastrados);
		setNrLimiteAbastecimentos(nrLimiteAbastecimentos);
		setVlLimiteVale(vlLimiteVale);
		setCdConvenio(cdConvenio);
		setCdTabelaPreco(cdTabelaPreco);
		setCdCidade(cdCidade);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmCargo(nmCargo);
		setNmLogradouro(nmLogradouro);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNmBairro(nmBairro);
		setNmPontoReferencia(nmPontoReferencia);
		setNrCep(nrCep);
		setNmContato(nmContato);
		setNmEmail(nmEmail);
		setNrDependentes(nrDependentes);
		setVlSalario(vlSalario);
		setStAluguel(stAluguel);
		setVlAluguel(vlAluguel);
		setDtAdmissao(dtAdmissao);
		setNmOutraRenda(nmOutraRenda);
		setVlOutraRenda(vlOutraRenda);
		setCdFaixaRenda(cdFaixaRenda);
		setLgControleVeiculo(lgControleVeiculo);
		setNmEmpresaTrabalho(nmEmpresaTrabalho);
		setNrTelefoneTrabalho(nrTelefoneTrabalho);
		setQtPrazoMinimoFactoring(qtPrazoMinimoFactoring);
		setQtPrazoMaximoFactoring(qtPrazoMaximoFactoring);
		setQtIdadeMinimaFactoring(qtIdadeMinimaFactoring);
		setVlGanhoMinimoFactoring(vlGanhoMinimoFactoring);
		setPrTaxaMinimaFactoring(prTaxaMinimaFactoring);
		setVlTaxaDevolucaoFactoring(vlTaxaDevolucaoFactoring);
		setPrTaxaPadraoFactoring(prTaxaPadraoFactoring);
		setPrTaxaJurosFactoring(prTaxaJurosFactoring);
		setPrTaxaProrrogacaoFactoring(prTaxaProrrogacaoFactoring);
		setQtMaximoDocumento(qtMaximoDocumento);
		setCdClassificacaoCliente(cdClassificacaoCliente);
		setNrCodigoBarras(nrCodigoBarras);
		setCdRota(cdRota);
		setLgAnalise(lgAnalise);
		setCdProfissao(cdProfissao);
		setCdPessoaCobranca(cdPessoaCobranca);
		setCdCobranca(cdCobranca);
		setCdProgramaFatura(cdProgramaFatura);
		setTxtObservacao(txtObservacao);
		setTpCredito(tpCredito);
	}
	
	public Cliente(int cdEmpresa,
			int cdPessoa,
			int lgConvenio,
			int lgEcommerce,
			int lgLimiteCredito,
			int lgAgenda,
			int nrDiasCarenciaFatura,
			float vlLimiteCredito,
			float vlLimiteMensal,
			float vlLimiteFactoring,
			float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario,
			int lgPista,
			int lgLoja,
			int lgVeiculosCadastrados,
			int nrLimiteAbastecimentos,
			float vlLimiteVale,
			int cdConvenio,
			int cdTabelaPreco,
			int cdCidade,
			int cdTipoLogradouro,
			String nmCargo,
			String nmLogradouro,
			String nrEndereco,
			String nmComplemento,
			String nmBairro,
			String nmPontoReferencia,
			String nrCep,
			String nmContato,
			String nmEmail,
			int nrDependentes,
			float vlSalario,
			int stAluguel,
			float vlAluguel,
			GregorianCalendar dtAdmissao,
			String nmOutraRenda,
			float vlOutraRenda,
			int cdFaixaRenda,
			int lgControleVeiculo,
			String nmEmpresaTrabalho,
			String nrTelefoneTrabalho,
			int qtPrazoMinimoFactoring,
			int qtPrazoMaximoFactoring,
			int qtIdadeMinimaFactoring,
			float vlGanhoMinimoFactoring,
			float prTaxaMinimaFactoring,
			float vlTaxaDevolucaoFactoring,
			float prTaxaPadraoFactoring,
			float prTaxaJurosFactoring,
			float prTaxaProrrogacaoFactoring,
			int qtMaximoDocumento,
			int cdClassificacaoCliente,
			String nrCodigoBarras,
			int cdRota,
			int lgAnalise,
			int cdProfissao,
			int cdPessoaCobranca){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setLgConvenio(lgConvenio);
		setLgEcommerce(lgEcommerce);
		setLgLimiteCredito(lgLimiteCredito);
		setLgAgenda(lgAgenda);
		setNrDiasCarenciaFatura(nrDiasCarenciaFatura);
		setVlLimiteCredito(vlLimiteCredito);
		setVlLimiteMensal(vlLimiteMensal);
		setVlLimiteFactoring(vlLimiteFactoring);
		setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
		setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
		setLgPista(lgPista);
		setLgLoja(lgLoja);
		setLgVeiculosCadastrados(lgVeiculosCadastrados);
		setNrLimiteAbastecimentos(nrLimiteAbastecimentos);
		setVlLimiteVale(vlLimiteVale);
		setCdConvenio(cdConvenio);
		setCdTabelaPreco(cdTabelaPreco);
		setCdCidade(cdCidade);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmCargo(nmCargo);
		setNmLogradouro(nmLogradouro);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNmBairro(nmBairro);
		setNmPontoReferencia(nmPontoReferencia);
		setNrCep(nrCep);
		setNmContato(nmContato);
		setNmEmail(nmEmail);
		setNrDependentes(nrDependentes);
		setVlSalario(vlSalario);
		setStAluguel(stAluguel);
		setVlAluguel(vlAluguel);
		setDtAdmissao(dtAdmissao);
		setNmOutraRenda(nmOutraRenda);
		setVlOutraRenda(vlOutraRenda);
		setCdFaixaRenda(cdFaixaRenda);
		setLgControleVeiculo(lgControleVeiculo);
		setNmEmpresaTrabalho(nmEmpresaTrabalho);
		setNrTelefoneTrabalho(nrTelefoneTrabalho);
		setQtPrazoMinimoFactoring(qtPrazoMinimoFactoring);
		setQtPrazoMaximoFactoring(qtPrazoMaximoFactoring);
		setQtIdadeMinimaFactoring(qtIdadeMinimaFactoring);
		setVlGanhoMinimoFactoring(vlGanhoMinimoFactoring);
		setPrTaxaMinimaFactoring(prTaxaMinimaFactoring);
		setVlTaxaDevolucaoFactoring(vlTaxaDevolucaoFactoring);
		setPrTaxaPadraoFactoring(prTaxaPadraoFactoring);
		setPrTaxaJurosFactoring(prTaxaJurosFactoring);
		setPrTaxaProrrogacaoFactoring(prTaxaProrrogacaoFactoring);
		setQtMaximoDocumento(qtMaximoDocumento);
		setCdClassificacaoCliente(cdClassificacaoCliente);
		setNrCodigoBarras(nrCodigoBarras);
		setCdRota(cdRota);
		setLgAnalise(lgAnalise);
		setCdProfissao(cdProfissao);
		setCdPessoaCobranca(cdPessoaCobranca);
	}
	
	public Cliente(int cdEmpresa,
			int cdPessoa,
			int lgConvenio,
			int cdConvenio,
			String nrCodigoBarras,
			int cdRota,
			int lgAnalise,
			int cdProfissao,
			int cdPessoaCobranca,
			int cdCobranca,
			int cdProgramaFatura,
			String txtObservacao,
			float vlLimiteCredito,
			float vlLimiteVale, 
			float vlLimiteFactoring,
			int tpCredito){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setLgConvenio(lgConvenio);
		setCdConvenio(cdConvenio);
		setNrCodigoBarras(nrCodigoBarras);
		setCdRota(cdRota);
		setLgAnalise(lgAnalise);
		setCdProfissao(cdProfissao);
		setCdPessoaCobranca(cdPessoaCobranca);
		setCdCobranca(cdCobranca);
		setCdProgramaFatura(cdProgramaFatura);
		setTxtObservacao(txtObservacao);
		setVlLimiteCredito(vlLimiteCredito);
		setVlLimiteVale(vlLimiteVale);
		setVlLimiteFactoring(vlLimiteFactoring);
		setTpCredito(tpCredito);
	}
	
	public Cliente(int cdEmpresa,
			int cdPessoa,
			int lgConvenio,
			int lgEcommerce,
			int lgLimiteCredito,
			int lgAgenda,
			int nrDiasCarenciaFatura,
			float vlLimiteCredito,
			float vlLimiteMensal,
			float vlLimiteFactoring,
			float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario,
			int lgPista,
			int lgLoja,
			int lgVeiculosCadastrados,
			int nrLimiteAbastecimentos,
			float vlLimiteVale,
			int cdConvenio,
			int cdTabelaPreco,
			int cdCidade,
			int cdTipoLogradouro,
			String nmCargo,
			String nmLogradouro,
			String nrEndereco,
			String nmComplemento,
			String nmBairro,
			String nmPontoReferencia,
			String nrCep,
			String nmContato,
			String nmEmail,
			int nrDependentes,
			float vlSalario,
			int stAluguel,
			float vlAluguel,
			GregorianCalendar dtAdmissao,
			String nmOutraRenda,
			float vlOutraRenda,
			int cdFaixaRenda,
			int lgControleVeiculo,
			String nmEmpresaTrabalho,
			String nrTelefoneTrabalho,
			int qtPrazoMinimoFactoring,
			int qtPrazoMaximoFactoring,
			int qtIdadeMinimaFactoring,
			float vlGanhoMinimoFactoring,
			float prTaxaMinimaFactoring,
			float vlTaxaDevolucaoFactoring,
			float prTaxaPadraoFactoring,
			float prTaxaJurosFactoring,
			float prTaxaProrrogacaoFactoring,
			int qtMaximoDocumento,
			int cdClassificacaoCliente){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setLgConvenio(lgConvenio);
		setLgEcommerce(lgEcommerce);
		setLgLimiteCredito(lgLimiteCredito);
		setLgAgenda(lgAgenda);
		setNrDiasCarenciaFatura(nrDiasCarenciaFatura);
		setVlLimiteCredito(vlLimiteCredito);
		setVlLimiteMensal(vlLimiteMensal);
		setVlLimiteFactoring(vlLimiteFactoring);
		setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
		setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
		setLgPista(lgPista);
		setLgLoja(lgLoja);
		setLgVeiculosCadastrados(lgVeiculosCadastrados);
		setNrLimiteAbastecimentos(nrLimiteAbastecimentos);
		setVlLimiteVale(vlLimiteVale);
		setCdConvenio(cdConvenio);
		setCdTabelaPreco(cdTabelaPreco);
		setCdCidade(cdCidade);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmCargo(nmCargo);
		setNmLogradouro(nmLogradouro);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNmBairro(nmBairro);
		setNmPontoReferencia(nmPontoReferencia);
		setNrCep(nrCep);
		setNmContato(nmContato);
		setNmEmail(nmEmail);
		setNrDependentes(nrDependentes);
		setVlSalario(vlSalario);
		setStAluguel(stAluguel);
		setVlAluguel(vlAluguel);
		setDtAdmissao(dtAdmissao);
		setNmOutraRenda(nmOutraRenda);
		setVlOutraRenda(vlOutraRenda);
		setCdFaixaRenda(cdFaixaRenda);
		setLgControleVeiculo(lgControleVeiculo);
		setNmEmpresaTrabalho(nmEmpresaTrabalho);
		setNrTelefoneTrabalho(nrTelefoneTrabalho);
		setQtPrazoMinimoFactoring(qtPrazoMinimoFactoring);
		setQtPrazoMaximoFactoring(qtPrazoMaximoFactoring);
		setQtIdadeMinimaFactoring(qtIdadeMinimaFactoring);
		setVlGanhoMinimoFactoring(vlGanhoMinimoFactoring);
		setPrTaxaMinimaFactoring(prTaxaMinimaFactoring);
		setVlTaxaDevolucaoFactoring(vlTaxaDevolucaoFactoring);
		setPrTaxaPadraoFactoring(prTaxaPadraoFactoring);
		setPrTaxaJurosFactoring(prTaxaJurosFactoring);
		setPrTaxaProrrogacaoFactoring(prTaxaProrrogacaoFactoring);
		setQtMaximoDocumento(qtMaximoDocumento);
		setCdClassificacaoCliente(cdClassificacaoCliente);
	}
	public Cliente(int cdEmpresa,
			int cdPessoa,
			int lgConvenio,
			int lgEcommerce,
			int lgLimiteCredito,
			int lgAgenda,
			int nrDiasCarenciaFatura,
			float vlLimiteCredito,
			float vlLimiteMensal,
			float vlLimiteFactoring,
			float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario,
			int lgPista,
			int lgLoja,
			int lgVeiculosCadastrados,
			int nrLimiteAbastecimentos,
			float vlLimiteVale,
			int cdConvenio,
			int cdTabelaPreco,
			int cdCidade,
			int cdTipoLogradouro,
			String nmCargo,
			String nmLogradouro,
			String nrEndereco,
			String nmComplemento,
			String nmBairro,
			String nmPontoReferencia,
			String nrCep,
			String nmContato,
			String nmEmail,
			int nrDependentes,
			float vlSalario,
			int stAluguel,
			float vlAluguel,
			GregorianCalendar dtAdmissao,
			String nmOutraRenda,
			float vlOutraRenda,
			int cdFaixaRenda,
			int lgControleVeiculo,
			String nmEmpresaTrabalho,
			String nrTelefoneTrabalho){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setLgConvenio(lgConvenio);
		setLgEcommerce(lgEcommerce);
		setLgLimiteCredito(lgLimiteCredito);
		setLgAgenda(lgAgenda);
		setNrDiasCarenciaFatura(nrDiasCarenciaFatura);
		setVlLimiteCredito(vlLimiteCredito);
		setVlLimiteMensal(vlLimiteMensal);
		setVlLimiteFactoring(vlLimiteFactoring);
		setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
		setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
		setLgPista(lgPista);
		setLgLoja(lgLoja);
		setLgVeiculosCadastrados(lgVeiculosCadastrados);
		setNrLimiteAbastecimentos(nrLimiteAbastecimentos);
		setVlLimiteVale(vlLimiteVale);
		setCdConvenio(cdConvenio);
		setCdTabelaPreco(cdTabelaPreco);
		setCdCidade(cdCidade);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmCargo(nmCargo);
		setNmLogradouro(nmLogradouro);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNmBairro(nmBairro);
		setNmPontoReferencia(nmPontoReferencia);
		setNrCep(nrCep);
		setNmContato(nmContato);
		setNmEmail(nmEmail);
		setNrDependentes(nrDependentes);
		setVlSalario(vlSalario);
		setStAluguel(stAluguel);
		setVlAluguel(vlAluguel);
		setDtAdmissao(dtAdmissao);
		setNmOutraRenda(nmOutraRenda);
		setVlOutraRenda(vlOutraRenda);
		setCdFaixaRenda(cdFaixaRenda);
		setLgControleVeiculo(lgControleVeiculo);
		setNmEmpresaTrabalho(nmEmpresaTrabalho);
		setNrTelefoneTrabalho(nrTelefoneTrabalho);
	}
	public Cliente() {
	}
	public Cliente(int cdEmpresa,
			int cdPessoa,
			int lgConvenio,
			int lgEcommerce,
			int lgLimiteCredito,
			int lgAgenda,
			int nrDiasCarenciaFatura,
			float vlLimiteCredito,
			float vlLimiteMensal,
			float vlLimiteFactoring,
			float vlLimiteFactoringEmissor,
			float vlLimiteFactoringUnitario,
			int lgPista,
			int lgLoja,
			int lgVeiculosCadastrados,
			int nrLimiteAbastecimentos,
			float vlLimiteVale,
			int cdConvenio,
			int cdTabelaPreco,
			int cdCidade,
			int cdTipoLogradouro,
			String nmCargo,
			String nmLogradouro,
			String nrEndereco,
			String nmComplemento,
			String nmBairro,
			String nmPontoReferencia,
			String nrCep,
			String nmContato,
			String nmEmail,
			int nrDependentes,
			float vlSalario,
			int stAluguel,
			float vlAluguel,
			GregorianCalendar dtAdmissao,
			String nmOutraRenda,
			float vlOutraRenda,
			int cdFaixaRenda,
			int lgControleVeiculo,
			String nmEmpresaTrabalho,
			String nrTelefoneTrabalho,
			int qtPrazoMinimoFactoring,
			int qtPrazoMaximoFactoring,
			int qtIdadeMinimaFactoring,
			float vlGanhoMinimoFactoring,
			float prTaxaMinimaFactoring,
			float vlTaxaDevolucaoFactoring,
			float prTaxaPadraoFactoring,
			float prTaxaJurosFactoring,
			float prTaxaProrrogacaoFactoring,
			int qtMaximoDocumento,
			int cdClassificacaoCliente,
			String nrCodigoBarras,
			int cdRota,
			int lgAnalise,
			int cdProfissao,
			int cdPessoaCobranca,
			int cdCobranca,
			int cdProgramaFatura,
			String txtObservacao,
			int tpCredito,
			int lgSPC,
			int lgSERASA){
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setLgConvenio(lgConvenio);
		setLgEcommerce(lgEcommerce);
		setLgLimiteCredito(lgLimiteCredito);
		setLgAgenda(lgAgenda);
		setNrDiasCarenciaFatura(nrDiasCarenciaFatura);
		setVlLimiteCredito(vlLimiteCredito);
		setVlLimiteMensal(vlLimiteMensal);
		setVlLimiteFactoring(vlLimiteFactoring);
		setVlLimiteFactoringEmissor(vlLimiteFactoringEmissor);
		setVlLimiteFactoringUnitario(vlLimiteFactoringUnitario);
		setLgPista(lgPista);
		setLgLoja(lgLoja);
		setLgVeiculosCadastrados(lgVeiculosCadastrados);
		setNrLimiteAbastecimentos(nrLimiteAbastecimentos);
		setVlLimiteVale(vlLimiteVale);
		setCdConvenio(cdConvenio);
		setCdTabelaPreco(cdTabelaPreco);
		setCdCidade(cdCidade);
		setCdTipoLogradouro(cdTipoLogradouro);
		setNmCargo(nmCargo);
		setNmLogradouro(nmLogradouro);
		setNrEndereco(nrEndereco);
		setNmComplemento(nmComplemento);
		setNmBairro(nmBairro);
		setNmPontoReferencia(nmPontoReferencia);
		setNrCep(nrCep);
		setNmContato(nmContato);
		setNmEmail(nmEmail);
		setNrDependentes(nrDependentes);
		setVlSalario(vlSalario);
		setStAluguel(stAluguel);
		setVlAluguel(vlAluguel);
		setDtAdmissao(dtAdmissao);
		setNmOutraRenda(nmOutraRenda);
		setVlOutraRenda(vlOutraRenda);
		setCdFaixaRenda(cdFaixaRenda);
		setLgControleVeiculo(lgControleVeiculo);
		setNmEmpresaTrabalho(nmEmpresaTrabalho);
		setNrTelefoneTrabalho(nrTelefoneTrabalho);
		setQtPrazoMinimoFactoring(qtPrazoMinimoFactoring);
		setQtPrazoMaximoFactoring(qtPrazoMaximoFactoring);
		setQtIdadeMinimaFactoring(qtIdadeMinimaFactoring);
		setVlGanhoMinimoFactoring(vlGanhoMinimoFactoring);
		setPrTaxaMinimaFactoring(prTaxaMinimaFactoring);
		setVlTaxaDevolucaoFactoring(vlTaxaDevolucaoFactoring);
		setPrTaxaPadraoFactoring(prTaxaPadraoFactoring);
		setPrTaxaJurosFactoring(prTaxaJurosFactoring);
		setPrTaxaProrrogacaoFactoring(prTaxaProrrogacaoFactoring);
		setQtMaximoDocumento(qtMaximoDocumento);
		setCdClassificacaoCliente(cdClassificacaoCliente);
		setNrCodigoBarras(nrCodigoBarras);
		setCdRota(cdRota);
		setLgAnalise(lgAnalise);
		setCdProfissao(cdProfissao);
		setCdPessoaCobranca(cdPessoaCobranca);
		setCdCobranca(cdCobranca);
		setCdProgramaFatura(cdProgramaFatura);
		setTxtObservacao(txtObservacao);
		setTpCredito(tpCredito);
		setLgSPC(lgSPC);
		setLgSERASA(lgSERASA);
	}

	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setLgConvenio(int lgConvenio){
		this.lgConvenio=lgConvenio;
	}
	public int getLgConvenio(){
		return this.lgConvenio;
	}
	public void setLgEcommerce(int lgEcommerce){
		this.lgEcommerce=lgEcommerce;
	}
	public int getLgEcommerce(){
		return this.lgEcommerce;
	}
	public void setLgLimiteCredito(int lgLimiteCredito){
		this.lgLimiteCredito=lgLimiteCredito;
	}
	public int getLgLimiteCredito(){
		return this.lgLimiteCredito;
	}
	public void setLgAgenda(int lgAgenda){
		this.lgAgenda=lgAgenda;
	}
	public int getLgAgenda(){
		return this.lgAgenda;
	}
	public void setNrDiasCarenciaFatura(int nrDiasCarenciaFatura){
		this.nrDiasCarenciaFatura=nrDiasCarenciaFatura;
	}
	public int getNrDiasCarenciaFatura(){
		return this.nrDiasCarenciaFatura;
	}
	public void setVlLimiteCredito(float vlLimiteCredito){
		this.vlLimiteCredito=vlLimiteCredito;
	}
	public float getVlLimiteCredito(){
		return this.vlLimiteCredito;
	}
	public void setVlLimiteMensal(float vlLimiteMensal){
		this.vlLimiteMensal=vlLimiteMensal;
	}
	public float getVlLimiteMensal(){
		return this.vlLimiteMensal;
	}
	public void setVlLimiteFactoring(float vlLimiteFactoring){
		this.vlLimiteFactoring=vlLimiteFactoring;
	}
	public float getVlLimiteFactoring(){
		return this.vlLimiteFactoring;
	}
	public void setVlLimiteFactoringEmissor(float vlLimiteFactoringEmissor){
		this.vlLimiteFactoringEmissor=vlLimiteFactoringEmissor;
	}
	public float getVlLimiteFactoringEmissor(){
		return this.vlLimiteFactoringEmissor;
	}
	public void setVlLimiteFactoringUnitario(float vlLimiteFactoringUnitario){
		this.vlLimiteFactoringUnitario=vlLimiteFactoringUnitario;
	}
	public float getVlLimiteFactoringUnitario(){
		return this.vlLimiteFactoringUnitario;
	}
	public void setLgPista(int lgPista){
		this.lgPista=lgPista;
	}
	public int getLgPista(){
		return this.lgPista;
	}
	public void setLgLoja(int lgLoja){
		this.lgLoja=lgLoja;
	}
	public int getLgLoja(){
		return this.lgLoja;
	}
	public void setLgVeiculosCadastrados(int lgVeiculosCadastrados){
		this.lgVeiculosCadastrados=lgVeiculosCadastrados;
	}
	public int getLgVeiculosCadastrados(){
		return this.lgVeiculosCadastrados;
	}
	public void setNrLimiteAbastecimentos(int nrLimiteAbastecimentos){
		this.nrLimiteAbastecimentos=nrLimiteAbastecimentos;
	}
	public int getNrLimiteAbastecimentos(){
		return this.nrLimiteAbastecimentos;
	}
	public void setVlLimiteVale(float vlLimiteVale){
		this.vlLimiteVale=vlLimiteVale;
	}
	public float getVlLimiteVale(){
		return this.vlLimiteVale;
	}
	public void setCdConvenio(int cdConvenio){
		this.cdConvenio=cdConvenio;
	}
	public int getCdConvenio(){
		return this.cdConvenio;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdTipoLogradouro(int cdTipoLogradouro){
		this.cdTipoLogradouro=cdTipoLogradouro;
	}
	public int getCdTipoLogradouro(){
		return this.cdTipoLogradouro;
	}
	public void setNmCargo(String nmCargo){
		this.nmCargo=nmCargo;
	}
	public String getNmCargo(){
		return this.nmCargo;
	}
	public void setNmLogradouro(String nmLogradouro){
		this.nmLogradouro=nmLogradouro;
	}
	public String getNmLogradouro(){
		return this.nmLogradouro;
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
	public void setNmBairro(String nmBairro){
		this.nmBairro=nmBairro;
	}
	public String getNmBairro(){
		return this.nmBairro;
	}
	public void setNmPontoReferencia(String nmPontoReferencia){
		this.nmPontoReferencia=nmPontoReferencia;
	}
	public String getNmPontoReferencia(){
		return this.nmPontoReferencia;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setNmContato(String nmContato){
		this.nmContato=nmContato;
	}
	public String getNmContato(){
		return this.nmContato;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setNrDependentes(int nrDependentes){
		this.nrDependentes=nrDependentes;
	}
	public int getNrDependentes(){
		return this.nrDependentes;
	}
	public void setVlSalario(float vlSalario){
		this.vlSalario=vlSalario;
	}
	public float getVlSalario(){
		return this.vlSalario;
	}
	public void setStAluguel(int stAluguel){
		this.stAluguel=stAluguel;
	}
	public int getStAluguel(){
		return this.stAluguel;
	}
	public void setVlAluguel(float vlAluguel){
		this.vlAluguel=vlAluguel;
	}
	public float getVlAluguel(){
		return this.vlAluguel;
	}
	public void setDtAdmissao(GregorianCalendar dtAdmissao){
		this.dtAdmissao=dtAdmissao;
	}
	public GregorianCalendar getDtAdmissao(){
		return this.dtAdmissao;
	}
	public void setNmOutraRenda(String nmOutraRenda){
		this.nmOutraRenda=nmOutraRenda;
	}
	public String getNmOutraRenda(){
		return this.nmOutraRenda;
	}
	public void setVlOutraRenda(float vlOutraRenda){
		this.vlOutraRenda=vlOutraRenda;
	}
	public float getVlOutraRenda(){
		return this.vlOutraRenda;
	}
	public void setCdFaixaRenda(int cdFaixaRenda){
		this.cdFaixaRenda=cdFaixaRenda;
	}
	public int getCdFaixaRenda(){
		return this.cdFaixaRenda;
	}
	public void setLgControleVeiculo(int lgControleVeiculo){
		this.lgControleVeiculo=lgControleVeiculo;
	}
	public int getLgControleVeiculo(){
		return this.lgControleVeiculo;
	}
	public void setNmEmpresaTrabalho(String nmEmpresaTrabalho){
		this.nmEmpresaTrabalho=nmEmpresaTrabalho;
	}
	public String getNmEmpresaTrabalho(){
		return this.nmEmpresaTrabalho;
	}
	public void setNrTelefoneTrabalho(String nrTelefoneTrabalho){
		this.nrTelefoneTrabalho=nrTelefoneTrabalho;
	}
	public String getNrTelefoneTrabalho(){
		return this.nrTelefoneTrabalho;
	}
	public void setQtPrazoMinimoFactoring(int qtPrazoMinimoFactoring){
		this.qtPrazoMinimoFactoring=qtPrazoMinimoFactoring;
	}
	public int getQtPrazoMinimoFactoring(){
		return this.qtPrazoMinimoFactoring;
	}
	public void setQtPrazoMaximoFactoring(int qtPrazoMaximoFactoring){
		this.qtPrazoMaximoFactoring=qtPrazoMaximoFactoring;
	}
	public int getQtPrazoMaximoFactoring(){
		return this.qtPrazoMaximoFactoring;
	}
	public void setQtIdadeMinimaFactoring(int qtIdadeMinimaFactoring){
		this.qtIdadeMinimaFactoring=qtIdadeMinimaFactoring;
	}
	public int getQtIdadeMinimaFactoring(){
		return this.qtIdadeMinimaFactoring;
	}
	public void setVlGanhoMinimoFactoring(float vlGanhoMinimoFactoring){
		this.vlGanhoMinimoFactoring=vlGanhoMinimoFactoring;
	}
	public float getVlGanhoMinimoFactoring(){
		return this.vlGanhoMinimoFactoring;
	}
	public void setPrTaxaMinimaFactoring(float prTaxaMinimaFactoring){
		this.prTaxaMinimaFactoring=prTaxaMinimaFactoring;
	}
	public float getPrTaxaMinimaFactoring(){
		return this.prTaxaMinimaFactoring;
	}
	public void setVlTaxaDevolucaoFactoring(float vlTaxaDevolucaoFactoring){
		this.vlTaxaDevolucaoFactoring=vlTaxaDevolucaoFactoring;
	}
	public float getVlTaxaDevolucaoFactoring(){
		return this.vlTaxaDevolucaoFactoring;
	}
	public void setPrTaxaPadraoFactoring(float prTaxaPadraoFactoring){
		this.prTaxaPadraoFactoring=prTaxaPadraoFactoring;
	}
	public float getPrTaxaPadraoFactoring(){
		return this.prTaxaPadraoFactoring;
	}
	public void setPrTaxaJurosFactoring(float prTaxaJurosFactoring){
		this.prTaxaJurosFactoring=prTaxaJurosFactoring;
	}
	public float getPrTaxaJurosFactoring(){
		return this.prTaxaJurosFactoring;
	}
	public void setPrTaxaProrrogacaoFactoring(float prTaxaProrrogacaoFactoring){
		this.prTaxaProrrogacaoFactoring=prTaxaProrrogacaoFactoring;
	}
	public float getPrTaxaProrrogacaoFactoring(){
		return this.prTaxaProrrogacaoFactoring;
	}
	public void setQtMaximoDocumento(int qtMaximoDocumento){
		this.qtMaximoDocumento=qtMaximoDocumento;
	}
	public int getQtMaximoDocumento(){
		return this.qtMaximoDocumento;
	}
	public void setCdClassificacaoCliente(int cdClassificacaoCliente){
		this.cdClassificacaoCliente=cdClassificacaoCliente;
	}
	public int getCdClassificacaoCliente(){
		return this.cdClassificacaoCliente;
	}
	public void setNrCodigoBarras(String nrCodigoBarras) {
		this.nrCodigoBarras = nrCodigoBarras;
	}
	public String getNrCodigoBarras() {
		return nrCodigoBarras;
	}
	public void setCdRota(int cdRota) {
		this.cdRota = cdRota;
	}
	public int getCdRota() {
		return cdRota;
	}
	public void setLgAnalise(int lgAnalise) {
		this.lgAnalise = lgAnalise;
	}
	public int getLgAnalise() {
		return lgAnalise;
	}
	public void setCdProfissao(int cdProfissao) {
		this.cdProfissao = cdProfissao;
	}
	public int getCdProfissao() {
		return cdProfissao;
	}
	public void setCdPessoaCobranca(int cdPessoaCobranca) {
		this.cdPessoaCobranca = cdPessoaCobranca;
	}
	public int getCdPessoaCobranca() {
		return cdPessoaCobranca;
	}
	public void setCdCobranca(int cdCobranca) {
		this.cdCobranca = cdCobranca;
	}
	public int getCdCobranca() {
		return cdCobranca;
	}
	public void setCdProgramaFatura(int cdProgramaFatura) {
		this.cdProgramaFatura = cdProgramaFatura;
	}
	public int getCdProgramaFatura() {
		return cdProgramaFatura;
	}
	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}
	public String getTxtObservacao() {
		return txtObservacao;
	}
	public void setTpCredito(int tpCredito) {
		this.tpCredito = tpCredito;
	}
	public int getTpCredito() {
		return tpCredito;
	}
	public void setLgSPC(int lgSPC) {
		this.lgSPC = lgSPC;
	}
	public int getLgSPC() {
		return lgSPC;
	}
	public void setLgSERASA(int lgSERASA) {
		this.lgSERASA = lgSERASA;
	}
	public int getLgSERASA() {
		return lgSERASA;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", lgConvenio: " +  getLgConvenio();
		valueToString += ", lgEcommerce: " +  getLgEcommerce();
		valueToString += ", lgLimiteCredito: " +  getLgLimiteCredito();
		valueToString += ", lgAgenda: " +  getLgAgenda();
		valueToString += ", nrDiasCarenciaFatura: " +  getNrDiasCarenciaFatura();
		valueToString += ", vlLimiteCredito: " +  getVlLimiteCredito();
		valueToString += ", vlLimiteMensal: " +  getVlLimiteMensal();
		valueToString += ", vlLimiteFactoring: " +  getVlLimiteFactoring();
		valueToString += ", vlLimiteFactoringEmissor: " +  getVlLimiteFactoringEmissor();
		valueToString += ", vlLimiteFactoringUnitario: " +  getVlLimiteFactoringUnitario();
		valueToString += ", lgPista: " +  getLgPista();
		valueToString += ", lgLoja: " +  getLgLoja();
		valueToString += ", lgVeiculosCadastrados: " +  getLgVeiculosCadastrados();
		valueToString += ", nrLimiteAbastecimentos: " +  getNrLimiteAbastecimentos();
		valueToString += ", vlLimiteVale: " +  getVlLimiteVale();
		valueToString += ", cdConvenio: " +  getCdConvenio();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdTipoLogradouro: " +  getCdTipoLogradouro();
		valueToString += ", nmCargo: " +  getNmCargo();
		valueToString += ", nmLogradouro: " +  getNmLogradouro();
		valueToString += ", nrEndereco: " +  getNrEndereco();
		valueToString += ", nmComplemento: " +  getNmComplemento();
		valueToString += ", nmBairro: " +  getNmBairro();
		valueToString += ", nmPontoReferencia: " +  getNmPontoReferencia();
		valueToString += ", nrCep: " +  getNrCep();
		valueToString += ", nmContato: " +  getNmContato();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", nrDependentes: " +  getNrDependentes();
		valueToString += ", vlSalario: " +  getVlSalario();
		valueToString += ", stAluguel: " +  getStAluguel();
		valueToString += ", vlAluguel: " +  getVlAluguel();
		valueToString += ", dtAdmissao: " +  sol.util.Util.formatDateTime(getDtAdmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmOutraRenda: " +  getNmOutraRenda();
		valueToString += ", vlOutraRenda: " +  getVlOutraRenda();
		valueToString += ", cdFaixaRenda: " +  getCdFaixaRenda();
		valueToString += ", lgControleVeiculo: " +  getLgControleVeiculo();
		valueToString += ", nmEmpresaTrabalho: " +  getNmEmpresaTrabalho();
		valueToString += ", nrTelefoneTrabalho: " +  getNrTelefoneTrabalho();
		valueToString += ", qtPrazoMinimoFactoring: " +  getQtPrazoMinimoFactoring();
		valueToString += ", qtPrazoMaximoFactoring: " +  getQtPrazoMaximoFactoring();
		valueToString += ", qtIdadeMinimaFactoring: " +  getQtIdadeMinimaFactoring();
		valueToString += ", vlGanhoMinimoFactoring: " +  getVlGanhoMinimoFactoring();
		valueToString += ", prTaxaMinimaFactoring: " +  getPrTaxaMinimaFactoring();
		valueToString += ", vlTaxaDevolucaoFactoring: " +  getVlTaxaDevolucaoFactoring();
		valueToString += ", prTaxaPadraoFactoring: " +  getPrTaxaPadraoFactoring();
		valueToString += ", prTaxaJurosFactoring: " +  getPrTaxaJurosFactoring();
		valueToString += ", prTaxaProrrogacaoFactoring: " +  getPrTaxaProrrogacaoFactoring();
		valueToString += ", qtMaximoDocumento: " +  getQtMaximoDocumento();
		valueToString += ", cdClassificacaoCliente: " +  getCdClassificacaoCliente();
		valueToString += ", nrCodigoBarras: " +  getNrCodigoBarras();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", lgAnalise: " +  getLgAnalise();
		valueToString += ", cdProfissao: " +  getCdProfissao();
		valueToString += ", cdPessoaCobranca: " +  getCdPessoaCobranca();
		valueToString += ", cdCobranca: " +  getCdCobranca();
		valueToString += ", cdProgramaFatura: " +  getCdProgramaFatura();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", tpCredito: " +  getTpCredito();
		valueToString += ", lgSPC: " +  getLgSPC();
		valueToString += ", lgSERASA: " +  getLgSERASA();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cliente(getCdEmpresa(),
			getCdPessoa(),
			getLgConvenio(),
			getLgEcommerce(),
			getLgLimiteCredito(),
			getLgAgenda(),
			getNrDiasCarenciaFatura(),
			getVlLimiteCredito(),
			getVlLimiteMensal(),
			getVlLimiteFactoring(),
			getVlLimiteFactoringEmissor(),
			getVlLimiteFactoringUnitario(),
			getLgPista(),
			getLgLoja(),
			getLgVeiculosCadastrados(),
			getNrLimiteAbastecimentos(),
			getVlLimiteVale(),
			getCdConvenio(),
			getCdTabelaPreco(),
			getCdCidade(),
			getCdTipoLogradouro(),
			getNmCargo(),
			getNmLogradouro(),
			getNrEndereco(),
			getNmComplemento(),
			getNmBairro(),
			getNmPontoReferencia(),
			getNrCep(),
			getNmContato(),
			getNmEmail(),
			getNrDependentes(),
			getVlSalario(),
			getStAluguel(),
			getVlAluguel(),
			getDtAdmissao()==null ? null : (GregorianCalendar)getDtAdmissao().clone(),
			getNmOutraRenda(),
			getVlOutraRenda(),
			getCdFaixaRenda(),
			getLgControleVeiculo(),
			getNmEmpresaTrabalho(),
			getNrTelefoneTrabalho(),
			getQtPrazoMinimoFactoring(),
			getQtPrazoMaximoFactoring(),
			getQtIdadeMinimaFactoring(),
			getVlGanhoMinimoFactoring(),
			getPrTaxaMinimaFactoring(),
			getVlTaxaDevolucaoFactoring(),
			getPrTaxaPadraoFactoring(),
			getPrTaxaJurosFactoring(),
			getPrTaxaProrrogacaoFactoring(),
			getQtMaximoDocumento(),
			getCdClassificacaoCliente(),
			getNrCodigoBarras(),
			getCdRota(),
			getLgAnalise(),
			getCdProfissao(),
			getCdPessoaCobranca(),
			getCdCobranca(),
			getCdProgramaFatura(),
			getTxtObservacao(),
			getTpCredito(),
			getLgSPC(),
			getLgSERASA());
	}

}