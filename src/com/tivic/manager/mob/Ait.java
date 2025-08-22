package com.tivic.manager.mob;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ait {

	private int cdAit;
	private int cdInfracao;
	private int cdAgente;
	private int cdUsuario;

	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInfracao;

	private String dsObservacao;
	private String dsLocalInfracao;
	private int nrAit;
	private Double vlVelocidadePermitida;
	private Double vlVelocidadeAferida;
	private Double vlVelocidadePenalidade;
	private String nrPlaca;
	private String dsPontoReferencia;
	private int lgAutoAssinado;
	private Double vlLatitude;
	private Double vlLongitude;
	private int cdCidade;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtDigitacao;

	private int cdEquipamento;
	private Double vlMulta;
	private int lgEnviadoDetran;
	private int stAit;
	private int cdEventoEquipamento;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtSincronizacao;

	private int nrTentativaSincronizacao;
	private int cdBairro;
	private int cdUsuarioExclusao;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	private int tpNaturezaAutuacao;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;

	private int tpStatus;
	private int tpArquivo;
	private int cdOcorrencia;
	private String dsParecer;
	private String nrErro;
	private int stEntrega;
	private String nrProcesso;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtRegistroDetran;

	private int stRecurso;
	private int lgAdvertencia;
	private String nrControle;
	private String nrRenainf;
	private int nrSequencial;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtArNai;

	private int nrNotificacaoNai;
	private int nrNotificacaoNip;
	private int tpCancelamento;
	private int lgCancelaMovimento;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCancelamentoMovimento;

	private int nrRemessa;
	private String nrCodigoBarras;
	private int nrRemessaRegistro;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEmissaoNip;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtResultadoDefesa;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAtualizacao;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtResultadoJari;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtResultadoCetran;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazoDefesa;

	private int lgAdvertenciaJari;
	private int lgAdvertenciaCetran;
	private int lgNotrigger;
	private int stDetran;
	private int lgErro;
	private String txtObservacao;
	private int cdMovimentoAtual;
	private int cdAitOrigem;
	private int nrFatorNic;
	private int lgDetranFebraban;
	private String nrPlacaAntiga;
	private int cdEspecieVeiculo;
	private int cdMarcaVeiculo;
	private int cdCorVeiculo;
	private int cdTipoVeiculo;
	private int cdCategoriaVeiculo;
	private String nrRenavan;
	private String nrAnoFabricacao;
	private String nrAnoModelo;
	private String nmProprietario;
	private int tpDocumento;
	private String nrDocumento;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String ufCnhCondutor;
	private int tpCnhCondutor;
	private int cdMarcaAutuacao;
	private String nmCondutorAutuacao;
	private String nmProprietarioAutuacao;
	private String nrCnhAutuacao;
	private String ufCnhAutuacao;
	private String nrDocumentoAutuacao;
	private int tpPessoaProprietario;
	private int cdBanco;
	private String sgUfVeiculo;
	private String dsLogradouro;
	private String dsNrImovel;
	private String nrCep;
	private String nrDdd;
	private String nrTelefone;
	private String dsEnderecoCondutor;
	private String nrCpfProprietario;
	private String nrCpfCnpjProprietario;
	private String nmComplemento;
	private String dsBairroCondutor;
	private String nrImovelCondutor;
	private String dsComplementoCondutor;
	private int cdCidadeCondutor;

	private String nrCepCondutor;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrimeiroRegistro;

	private String nrCpfCondutor;
	private int cdVeiculo;
	private int cdEndereco;
	private int cdProprietario;
	private int cdCondutor;
	private int cdEnderecoCondutor;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtNotificacaoDevolucao;

	private int tpOrigem;
	private int lgPublicarNaiDiarioOficial;
	private int tpConvenio;
	private String idAit;
	private int cdTalao;

	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAfericao;
	private String nrLacre;
	private String nrInventarioInmetro;

	private int cdLogradouroInfracao;

	private List<AitImagem> imagens;
	private List<AitMovimento> movimentos;

	private AitMovimento movimentoAtual;

	private int cdCidadeProprietario;
	private int cdConvenio;
	private int cdPais;
	
	private String txtCancelamento;

	public Ait() {
		this.movimentos = new ArrayList<AitMovimento>();
	}

	public Ait(int cdAit,
			int cdInfracao,
			int cdAgente,
			int cdUsuario,
			GregorianCalendar dtInfracao,
			String dsObservacao,
			String dsLocalInfracao,
			int nrAit,
			Double vlVelocidadePermitida,
			Double vlVelocidadeAferida,
			Double vlVelocidadePenalidade,
			String nrPlaca,
			String dsPontoReferencia,
			int lgAutoAssinado,
			Double vlLatitude,
			Double vlLongitude,
			int cdCidade,
			GregorianCalendar dtDigitacao,
			int cdEquipamento,
			Double vlMulta,
			int lgEnviadoDetran,
			int stAit,
			int cdEventoEquipamento,
			GregorianCalendar dtSincronizacao,
			int nrTentativaSincronizacao,
			int cdBairro,
			int cdUsuarioExclusao,
			GregorianCalendar dtVencimento,
			int tpNaturezaAutuacao,
			GregorianCalendar dtMovimento,
			int tpStatus,
			int tpArquivo,
			int cdOcorrencia,
			String dsParecer,
			String nrErro,
			int stEntrega,
			String nrProcesso,
			GregorianCalendar dtRegistroDetran,
			int stRecurso,
			int lgAdvertencia,
			String nrControle,
			String nrRenainf,
			int nrSequencial,
			GregorianCalendar dtArNai,
			int nrNotificacaoNai,
			int nrNotificacaoNip,
			int tpCancelamento,
			int lgCancelaMovimento,
			GregorianCalendar dtCancelamentoMovimento,
			int nrRemessa,
			String nrCodigoBarras,
			int nrRemessaRegistro,
			GregorianCalendar dtEmissaoNip,
			GregorianCalendar dtResultadoDefesa,
			GregorianCalendar dtAtualizacao,
			GregorianCalendar dtResultadoJari,
			GregorianCalendar dtResultadoCetran,
			int lgAdvertenciaJari,
			int lgAdvertenciaCetran,
			int lgNotrigger,
			int stDetran,
			int lgErro,
			String txtObservacao,
			int cdMovimentoAtual,
			int cdAitOrigem,
			int nrFatorNic,
			int lgDetranFebraban,
			String nrPlacaAntiga,
			int cdEspecieVeiculo,
			int cdMarcaVeiculo,
			int cdCorVeiculo,
			int cdTipoVeiculo,
			int cdCategoriaVeiculo,
			String nrRenavan,
			String nrAnoFabricacao,
			String nrAnoModelo,
			String nmProprietario,
			int tpDocumento,
			String nrDocumento,
			String nmCondutor,
			String nrCnhCondutor,
			String ufCnhCondutor,
			int tpCnhCondutor,
			int cdMarcaAutuacao,
			String nmCondutorAutuacao,
			String nmProprietarioAutuacao,
			String nrCnhAutuacao,
			String ufCnhAutuacao,
			String nrDocumentoAutuacao,
			int tpPessoaProprietario,
			int cdBanco,
			String sgUfVeiculo,
			String dsLogradouro,
			String dsNrImovel,
			String nrCep,
			String nrDdd,
			String nrTelefone,
			String dsEnderecoCondutor,
			String nrCpfProprietario,
			String nrCpfCnpjProprietario,
			String nmComplemento,
			String dsBairroCondutor,
			String nrImovelCondutor,
			String dsComplementoCondutor,
			int cdCidadeCondutor,
			String nrCepCondutor,
			GregorianCalendar dtPrimeiroRegistro,
			String nrCpfCondutor,
			int cdVeiculo,
			int cdEndereco,
			int cdProprietario,
			int cdCondutor,
			int cdEnderecoCondutor,
			GregorianCalendar dtNotificacaoDevolucao,
			int tpOrigem,
			int lgPublicarNaiDiarioOficial,
			int tpConvenio,
			String idAit,
			int cdTalao,
			GregorianCalendar dtAfericao,
			String nrLacre,
			String nrInventarioInmetro,
			int cdCidadeProprietario,
			int cdConvenio,
			int cdPais,
			String txtCancelamento) {
		setCdAit(cdAit);
		setCdInfracao(cdInfracao);
		setCdAgente(cdAgente);
		setCdUsuario(cdUsuario);
		setDtInfracao(dtInfracao);
		setDsObservacao(dsObservacao);
		setDsLocalInfracao(dsLocalInfracao);
		setNrAit(nrAit);
		setVlVelocidadePermitida(vlVelocidadePermitida);
		setVlVelocidadeAferida(vlVelocidadeAferida);
		setVlVelocidadePenalidade(vlVelocidadePenalidade);
		setNrPlaca(nrPlaca);
		setDsPontoReferencia(dsPontoReferencia);
		setLgAutoAssinado(lgAutoAssinado);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdCidade(cdCidade);
		setDtDigitacao(dtDigitacao);
		setCdEquipamento(cdEquipamento);
		setVlMulta(vlMulta);
		setLgEnviadoDetran(lgEnviadoDetran);
		setStAit(stAit);
		setCdEventoEquipamento(cdEventoEquipamento);
		setDtSincronizacao(dtSincronizacao);
		setNrTentativaSincronizacao(nrTentativaSincronizacao);
		setCdBairro(cdBairro);
		setCdUsuarioExclusao(cdUsuarioExclusao);
		setDtVencimento(dtVencimento);
		setTpNaturezaAutuacao(tpNaturezaAutuacao);
		setDtMovimento(dtMovimento);
		setTpStatus(tpStatus);
		setTpArquivo(tpArquivo);
		setCdOcorrencia(cdOcorrencia);
		setDsParecer(dsParecer);
		setNrErro(nrErro);
		setStEntrega(stEntrega);
		setNrProcesso(nrProcesso);
		setDtRegistroDetran(dtRegistroDetran);
		setStRecurso(stRecurso);
		setLgAdvertencia(lgAdvertencia);
		setNrControle(nrControle);
		setNrRenainf(nrRenainf);
		setNrSequencial(nrSequencial);
		setDtArNai(dtArNai);
		setNrNotificacaoNai(nrNotificacaoNai);
		setNrNotificacaoNip(nrNotificacaoNip);
		setTpCancelamento(tpCancelamento);
		setLgCancelaMovimento(lgCancelaMovimento);
		setDtCancelamentoMovimento(dtCancelamentoMovimento);
		setNrRemessa(nrRemessa);
		setNrCodigoBarras(nrCodigoBarras);
		setNrRemessaRegistro(nrRemessaRegistro);
		setDtEmissaoNip(dtEmissaoNip);
		setDtResultadoDefesa(dtResultadoDefesa);
		setDtAtualizacao(dtAtualizacao);
		setDtResultadoJari(dtResultadoJari);
		setDtResultadoCetran(dtResultadoCetran);
		setLgAdvertenciaJari(lgAdvertenciaJari);
		setLgAdvertenciaCetran(lgAdvertenciaCetran);
		setLgNotrigger(lgNotrigger);
		setStDetran(stDetran);
		setLgErro(lgErro);
		setTxtObservacao(txtObservacao);
		setCdMovimentoAtual(cdMovimentoAtual);
		setCdAitOrigem(cdAitOrigem);
		setNrFatorNic(nrFatorNic);
		setLgDetranFebraban(lgDetranFebraban);
		setNrPlacaAntiga(nrPlacaAntiga);
		setCdEspecieVeiculo(cdEspecieVeiculo);
		setCdMarcaVeiculo(cdMarcaVeiculo);
		setCdCorVeiculo(cdCorVeiculo);
		setCdTipoVeiculo(cdTipoVeiculo);
		setCdCategoriaVeiculo(cdCategoriaVeiculo);
		setNrRenavan(nrRenavan);
		setNrAnoFabricacao(nrAnoFabricacao);
		setNrAnoModelo(nrAnoModelo);
		setNmProprietario(nmProprietario);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setUfCnhCondutor(ufCnhCondutor);
		setTpCnhCondutor(tpCnhCondutor);
		setCdMarcaAutuacao(cdMarcaAutuacao);
		setNmCondutorAutuacao(nmCondutorAutuacao);
		setNmProprietarioAutuacao(nmProprietarioAutuacao);
		setNrCnhAutuacao(nrCnhAutuacao);
		setUfCnhAutuacao(ufCnhAutuacao);
		setNrDocumentoAutuacao(nrDocumentoAutuacao);
		setTpPessoaProprietario(tpPessoaProprietario);
		setCdBanco(cdBanco);
		setSgUfVeiculo(sgUfVeiculo);
		setDsLogradouro(dsLogradouro);
		setDsNrImovel(dsNrImovel);
		setNrCep(nrCep);
		setNrDdd(nrDdd);
		setNrTelefone(nrTelefone);
		setDsEnderecoCondutor(dsEnderecoCondutor);
		setNrCpfProprietario(nrCpfProprietario);
		setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
		setNmComplemento(nmComplemento);
		setDsBairroCondutor(dsBairroCondutor);
		setNrImovelCondutor(nrImovelCondutor);
		setDsComplementoCondutor(dsComplementoCondutor);
		setCdCidadeCondutor(cdCidadeCondutor);
		setNrCepCondutor(nrCepCondutor);
		setDtPrimeiroRegistro(dtPrimeiroRegistro);
		setNrCpfCondutor(nrCpfCondutor);
		setCdVeiculo(cdVeiculo);
		setCdEndereco(cdEndereco);
		setCdProprietario(cdProprietario);
		setCdCondutor(cdCondutor);
		setCdEnderecoCondutor(cdEnderecoCondutor);
		setDtNotificacaoDevolucao(dtNotificacaoDevolucao);
		setTpOrigem(tpOrigem);
		setLgPublicarNaiDiarioOficial(lgPublicarNaiDiarioOficial);
		setTpConvenio(tpConvenio);
		setIdAit(idAit);
		setCdTalao(cdTalao);
		setDtAfericao(dtAfericao);
		setNrLacre(nrLacre);
		setCdCidadeProprietario(cdCidadeProprietario);
		setCdConvenio(cdConvenio);
		setCdPais(cdPais);
		setTxtCancelamento(txtCancelamento);
		setNrInventarioInmetro(nrInventarioInmetro);
		setMovimentos(new ArrayList<AitMovimento>());
	}

	public Ait(int cdAit,
			int cdInfracao,
			int cdAgente,
			int cdUsuario,
			GregorianCalendar dtInfracao,
			String dsObservacao,
			String dsLocalInfracao,
			int nrAit,
			Double vlVelocidadePermitida,
			Double vlVelocidadeAferida,
			Double vlVelocidadePenalidade,
			String nrPlaca,
			String dsPontoReferencia,
			int lgAutoAssinado,
			Double vlLatitude,
			Double vlLongitude,
			int cdCidade,
			GregorianCalendar dtDigitacao,
			int cdEquipamento,
			Double vlMulta,
			int lgEnviadoDetran,
			int stAit,
			int cdEventoEquipamento,
			GregorianCalendar dtSincronizacao,
			int nrTentativaSincronizacao,
			int cdBairro,
			int cdUsuarioExclusao,
			GregorianCalendar dtVencimento,
			int tpNaturezaAutuacao,
			GregorianCalendar dtMovimento,
			int tpStatus,
			int tpArquivo,
			int cdOcorrencia,
			String dsParecer,
			String nrErro,
			int stEntrega,
			String nrProcesso,
			GregorianCalendar dtRegistroDetran,
			int stRecurso,
			int lgAdvertencia,
			String nrControle,
			String nrRenainf,
			int nrSequencial,
			GregorianCalendar dtArNai,
			int nrNotificacaoNai,
			int nrNotificacaoNip,
			int tpCancelamento,
			int lgCancelaMovimento,
			GregorianCalendar dtCancelamentoMovimento,
			int nrRemessa,
			String nrCodigoBarras,
			int nrRemessaRegistro,
			GregorianCalendar dtEmissaoNip,
			GregorianCalendar dtResultadoDefesa,
			GregorianCalendar dtAtualizacao,
			GregorianCalendar dtResultadoJari,
			GregorianCalendar dtResultadoCetran,
			GregorianCalendar dtPrazoDefesa,
			int lgAdvertenciaJari,
			int lgAdvertenciaCetran,
			int lgNotrigger,
			int stDetran,
			int lgErro,
			String txtObservacao,
			int cdMovimentoAtual,
			int cdAitOrigem,
			int nrFatorNic,
			int lgDetranFebraban,
			String nrPlacaAntiga,
			int cdEspecieVeiculo,
			int cdMarcaVeiculo,
			int cdCorVeiculo,
			int cdTipoVeiculo,
			int cdCategoriaVeiculo,
			String nrRenavan,
			String nrAnoFabricacao,
			String nrAnoModelo,
			String nmProprietario,
			int tpDocumento,
			String nrDocumento,
			String nmCondutor,
			String nrCnhCondutor,
			String ufCnhCondutor,
			int tpCnhCondutor,
			int cdMarcaAutuacao,
			String nmCondutorAutuacao,
			String nmProprietarioAutuacao,
			String nrCnhAutuacao,
			String ufCnhAutuacao,
			String nrDocumentoAutuacao,
			int tpPessoaProprietario,
			int cdBanco,
			String sgUfVeiculo,
			String dsLogradouro,
			String dsNrImovel,
			String nrCep,
			String nrDdd,
			String nrTelefone,
			String dsEnderecoCondutor,
			String nrCpfProprietario,
			String nrCpfCnpjProprietario,
			String nmComplemento,
			String dsBairroCondutor,
			String nrImovelCondutor,
			String dsComplementoCondutor,
			int cdCidadeCondutor,
			String nrCepCondutor,
			GregorianCalendar dtPrimeiroRegistro,
			String nrCpfCondutor,
			int cdVeiculo,
			int cdEndereco,
			int cdProprietario,
			int cdCondutor,
			int cdEnderecoCondutor,
			GregorianCalendar dtNotificacaoDevolucao,
			int tpOrigem,
			int lgPublicarNaiDiarioOficial,
			int tpConvenio,
			String idAit,
			int cdTalao,
			GregorianCalendar dtAfericao,
			String nrLacre,
			String nrInventarioInmetro,
			int cdLogradouroInfracao,
			int cdCidadeProprietario,
			int cdConvenio,
			int cdPais,
			String txtCancelamento) {
		setCdAit(cdAit);
		setCdInfracao(cdInfracao);
		setCdAgente(cdAgente);
		setCdUsuario(cdUsuario);
		setDtInfracao(dtInfracao);
		setDsObservacao(dsObservacao);
		setDsLocalInfracao(dsLocalInfracao);
		setNrAit(nrAit);
		setVlVelocidadePermitida(vlVelocidadePermitida);
		setVlVelocidadeAferida(vlVelocidadeAferida);
		setVlVelocidadePenalidade(vlVelocidadePenalidade);
		setNrPlaca(nrPlaca);
		setDsPontoReferencia(dsPontoReferencia);
		setLgAutoAssinado(lgAutoAssinado);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdCidade(cdCidade);
		setDtDigitacao(dtDigitacao);
		setCdEquipamento(cdEquipamento);
		setVlMulta(vlMulta);
		setLgEnviadoDetran(lgEnviadoDetran);
		setStAit(stAit);
		setCdEventoEquipamento(cdEventoEquipamento);
		setDtSincronizacao(dtSincronizacao);
		setNrTentativaSincronizacao(nrTentativaSincronizacao);
		setCdBairro(cdBairro);
		setCdUsuarioExclusao(cdUsuarioExclusao);
		setDtVencimento(dtVencimento);
		setTpNaturezaAutuacao(tpNaturezaAutuacao);
		setDtMovimento(dtMovimento);
		setTpStatus(tpStatus);
		setTpArquivo(tpArquivo);
		setCdOcorrencia(cdOcorrencia);
		setDsParecer(dsParecer);
		setNrErro(nrErro);
		setStEntrega(stEntrega);
		setNrProcesso(nrProcesso);
		setDtRegistroDetran(dtRegistroDetran);
		setStRecurso(stRecurso);
		setLgAdvertencia(lgAdvertencia);
		setNrControle(nrControle);
		setNrRenainf(nrRenainf);
		setNrSequencial(nrSequencial);
		setDtArNai(dtArNai);
		setNrNotificacaoNai(nrNotificacaoNai);
		setNrNotificacaoNip(nrNotificacaoNip);
		setTpCancelamento(tpCancelamento);
		setLgCancelaMovimento(lgCancelaMovimento);
		setDtCancelamentoMovimento(dtCancelamentoMovimento);
		setNrRemessa(nrRemessa);
		setNrCodigoBarras(nrCodigoBarras);
		setNrRemessaRegistro(nrRemessaRegistro);
		setDtEmissaoNip(dtEmissaoNip);
		setDtResultadoDefesa(dtResultadoDefesa);
		setDtAtualizacao(dtAtualizacao);
		setDtResultadoJari(dtResultadoJari);
		setDtResultadoCetran(dtResultadoCetran);
		setDtPrazoDefesa(dtPrazoDefesa);
		setLgAdvertenciaJari(lgAdvertenciaJari);
		setLgAdvertenciaCetran(lgAdvertenciaCetran);
		setLgNotrigger(lgNotrigger);
		setStDetran(stDetran);
		setLgErro(lgErro);
		setTxtObservacao(txtObservacao);
		setCdMovimentoAtual(cdMovimentoAtual);
		setCdAitOrigem(cdAitOrigem);
		setNrFatorNic(nrFatorNic);
		setLgDetranFebraban(lgDetranFebraban);
		setNrPlacaAntiga(nrPlacaAntiga);
		setCdEspecieVeiculo(cdEspecieVeiculo);
		setCdMarcaVeiculo(cdMarcaVeiculo);
		setCdCorVeiculo(cdCorVeiculo);
		setCdTipoVeiculo(cdTipoVeiculo);
		setCdCategoriaVeiculo(cdCategoriaVeiculo);
		setNrRenavan(nrRenavan);
		setNrAnoFabricacao(nrAnoFabricacao);
		setNrAnoModelo(nrAnoModelo);
		setNmProprietario(nmProprietario);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setUfCnhCondutor(ufCnhCondutor);
		setTpCnhCondutor(tpCnhCondutor);
		setCdMarcaAutuacao(cdMarcaAutuacao);
		setNmCondutorAutuacao(nmCondutorAutuacao);
		setNmProprietarioAutuacao(nmProprietarioAutuacao);
		setNrCnhAutuacao(nrCnhAutuacao);
		setUfCnhAutuacao(ufCnhAutuacao);
		setNrDocumentoAutuacao(nrDocumentoAutuacao);
		setTpPessoaProprietario(tpPessoaProprietario);
		setCdBanco(cdBanco);
		setSgUfVeiculo(sgUfVeiculo);
		setDsLogradouro(dsLogradouro);
		setDsNrImovel(dsNrImovel);
		setNrCep(nrCep);
		setNrDdd(nrDdd);
		setNrTelefone(nrTelefone);
		setDsEnderecoCondutor(dsEnderecoCondutor);
		setNrCpfProprietario(nrCpfProprietario);
		setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
		setNmComplemento(nmComplemento);
		setDsBairroCondutor(dsBairroCondutor);
		setNrImovelCondutor(nrImovelCondutor);
		setDsComplementoCondutor(dsComplementoCondutor);
		setCdCidadeCondutor(cdCidadeCondutor);
		setNrCepCondutor(nrCepCondutor);
		setDtPrimeiroRegistro(dtPrimeiroRegistro);
		setNrCpfCondutor(nrCpfCondutor);
		setCdVeiculo(cdVeiculo);
		setCdEndereco(cdEndereco);
		setCdProprietario(cdProprietario);
		setCdCondutor(cdCondutor);
		setCdEnderecoCondutor(cdEnderecoCondutor);
		setDtNotificacaoDevolucao(dtNotificacaoDevolucao);
		setTpOrigem(tpOrigem);
		setLgPublicarNaiDiarioOficial(lgPublicarNaiDiarioOficial);
		setTpConvenio(tpConvenio);
		setIdAit(idAit);
		setCdTalao(cdTalao);
		setDtAfericao(dtAfericao);
		setNrLacre(nrLacre);
		setNrInventarioInmetro(nrInventarioInmetro);
		setCdLogradouroInfracao(cdLogradouroInfracao);
		setCdCidadeProprietario(cdCidadeProprietario);
		setCdConvenio(cdConvenio);
		setCdPais(cdPais);
		setTxtCancelamento(txtCancelamento);
		setMovimentos(new ArrayList<AitMovimento>());
	}

	public GregorianCalendar getDtPrazoDefesa() {
		return dtPrazoDefesa;
	}

	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa) {
		this.dtPrazoDefesa = dtPrazoDefesa;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdAit() {
		return this.cdAit;
	}

	public void setCdInfracao(int cdInfracao) {
		this.cdInfracao = cdInfracao;
	}

	public int getCdInfracao() {
		return this.cdInfracao;
	}

	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}

	public int getCdAgente() {
		return this.cdAgente;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public int getCdUsuario() {
		return this.cdUsuario;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public GregorianCalendar getDtInfracao() {
		return this.dtInfracao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public String getDsObservacao() {
		return this.dsObservacao;
	}

	public void setDsLocalInfracao(String dsLocalInfracao) {
		this.dsLocalInfracao = dsLocalInfracao;
	}

	public String getDsLocalInfracao() {
		return this.dsLocalInfracao;
	}

	public void setNrAit(int nrAit) {
		this.nrAit = nrAit;
	}

	public int getNrAit() {
		return this.nrAit;
	}

	public void setVlVelocidadePermitida(Double vlVelocidadePermitida) {
		this.vlVelocidadePermitida = vlVelocidadePermitida;
	}

	public Double getVlVelocidadePermitida() {
		return this.vlVelocidadePermitida;
	}

	public void setVlVelocidadeAferida(Double vlVelocidadeAferida) {
		this.vlVelocidadeAferida = vlVelocidadeAferida;
	}

	public Double getVlVelocidadeAferida() {
		return this.vlVelocidadeAferida;
	}

	public void setVlVelocidadePenalidade(Double vlVelocidadePenalidade) {
		this.vlVelocidadePenalidade = vlVelocidadePenalidade;
	}

	public Double getVlVelocidadePenalidade() {
		return this.vlVelocidadePenalidade;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public String getNrPlaca() {
		return this.nrPlaca;
	}

	public void setDsPontoReferencia(String dsPontoReferencia) {
		this.dsPontoReferencia = dsPontoReferencia;
	}

	public String getDsPontoReferencia() {
		return this.dsPontoReferencia;
	}

	public void setLgAutoAssinado(int lgAutoAssinado) {
		this.lgAutoAssinado = lgAutoAssinado;
	}

	public int getLgAutoAssinado() {
		return this.lgAutoAssinado;
	}

	public void setVlLatitude(Double vlLatitude) {
		this.vlLatitude = vlLatitude;
	}

	public Double getVlLatitude() {
		return this.vlLatitude;
	}

	public void setVlLongitude(Double vlLongitude) {
		this.vlLongitude = vlLongitude;
	}

	public Double getVlLongitude() {
		return this.vlLongitude;
	}

	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}

	public int getCdCidade() {
		return this.cdCidade;
	}

	public void setDtDigitacao(GregorianCalendar dtDigitacao) {
		this.dtDigitacao = dtDigitacao;
	}

	public GregorianCalendar getDtDigitacao() {
		return this.dtDigitacao;
	}

	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
	}

	public int getCdEquipamento() {
		return this.cdEquipamento;
	}

	public void setVlMulta(Double vlMulta) {
		this.vlMulta = vlMulta;
	}

	public Double getVlMulta() {
		return this.vlMulta;
	}

	public void setLgEnviadoDetran(int lgEnviadoDetran) {
		this.lgEnviadoDetran = lgEnviadoDetran;
	}

	public int getLgEnviadoDetran() {
		return this.lgEnviadoDetran;
	}

	public void setStAit(int stAit) {
		this.stAit = stAit;
	}

	public int getStAit() {
		return this.stAit;
	}

	public void setCdEventoEquipamento(int cdEventoEquipamento) {
		this.cdEventoEquipamento = cdEventoEquipamento;
	}

	public int getCdEventoEquipamento() {
		return this.cdEventoEquipamento;
	}

	public void setDtSincronizacao(GregorianCalendar dtSincronizacao) {
		this.dtSincronizacao = dtSincronizacao;
	}

	public GregorianCalendar getDtSincronizacao() {
		return this.dtSincronizacao;
	}

	public void setNrTentativaSincronizacao(int nrTentativaSincronizacao) {
		this.nrTentativaSincronizacao = nrTentativaSincronizacao;
	}

	public int getNrTentativaSincronizacao() {
		return this.nrTentativaSincronizacao;
	}

	public void setCdBairro(int cdBairro) {
		this.cdBairro = cdBairro;
	}

	public int getCdBairro() {
		return this.cdBairro;
	}

	public void setCdUsuarioExclusao(int cdUsuarioExclusao) {
		this.cdUsuarioExclusao = cdUsuarioExclusao;
	}

	public int getCdUsuarioExclusao() {
		return this.cdUsuarioExclusao;
	}

	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public GregorianCalendar getDtVencimento() {
		return this.dtVencimento;
	}

	public void setTpNaturezaAutuacao(int tpNaturezaAutuacao) {
		this.tpNaturezaAutuacao = tpNaturezaAutuacao;
	}

	public int getTpNaturezaAutuacao() {
		return this.tpNaturezaAutuacao;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public GregorianCalendar getDtMovimento() {
		return this.dtMovimento;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public int getTpStatus() {
		return this.tpStatus;
	}

	public void setTpArquivo(int tpArquivo) {
		this.tpArquivo = tpArquivo;
	}

	public int getTpArquivo() {
		return this.tpArquivo;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public int getCdOcorrencia() {
		return this.cdOcorrencia;
	}

	public void setDsParecer(String dsParecer) {
		this.dsParecer = dsParecer;
	}

	public String getDsParecer() {
		return this.dsParecer;
	}

	public void setNrErro(String nrErro) {
		this.nrErro = nrErro;
	}

	public String getNrErro() {
		return this.nrErro;
	}

	public void setStEntrega(int stEntrega) {
		this.stEntrega = stEntrega;
	}

	public int getStEntrega() {
		return this.stEntrega;
	}

	public void setNrProcesso(String nrProcesso) {
		this.nrProcesso = nrProcesso;
	}

	public String getNrProcesso() {
		return this.nrProcesso;
	}
	
	public String getTxtCancelamento() {
		return txtCancelamento;
	}

	public void setTxtCancelamento(String txtCancelamento) {
		this.txtCancelamento = txtCancelamento;
	}

	public void setDtRegistroDetran(GregorianCalendar dtRegistroDetran) {
		this.dtRegistroDetran = dtRegistroDetran;
	}

	public GregorianCalendar getDtRegistroDetran() {
		return this.dtRegistroDetran;
	}

	public void setStRecurso(int stRecurso) {
		this.stRecurso = stRecurso;
	}

	public int getStRecurso() {
		return this.stRecurso;
	}

	public void setLgAdvertencia(int lgAdvertencia) {
		this.lgAdvertencia = lgAdvertencia;
	}

	public int getLgAdvertencia() {
		return this.lgAdvertencia;
	}

	public void setNrControle(String nrControle) {
		this.nrControle = nrControle;
	}

	public String getNrControle() {
		return this.nrControle;
	}

	public void setNrRenainf(String nrRenainf) {
		this.nrRenainf = nrRenainf;
	}

	public String getNrRenainf() {
		return this.nrRenainf;
	}

	public void setNrSequencial(int nrSequencial) {
		this.nrSequencial = nrSequencial;
	}

	public int getNrSequencial() {
		return this.nrSequencial;
	}

	public void setDtArNai(GregorianCalendar dtArNai) {
		this.dtArNai = dtArNai;
	}

	public GregorianCalendar getDtArNai() {
		return this.dtArNai;
	}

	public void setNrNotificacaoNai(int nrNotificacaoNai) {
		this.nrNotificacaoNai = nrNotificacaoNai;
	}

	public int getNrNotificacaoNai() {
		return this.nrNotificacaoNai;
	}

	public void setNrNotificacaoNip(int nrNotificacaoNip) {
		this.nrNotificacaoNip = nrNotificacaoNip;
	}

	public int getNrNotificacaoNip() {
		return this.nrNotificacaoNip;
	}

	public void setTpCancelamento(int tpCancelamento) {
		this.tpCancelamento = tpCancelamento;
	}

	public int getTpCancelamento() {
		return this.tpCancelamento;
	}

	public void setLgCancelaMovimento(int lgCancelaMovimento) {
		this.lgCancelaMovimento = lgCancelaMovimento;
	}

	public int getLgCancelaMovimento() {
		return this.lgCancelaMovimento;
	}

	public void setDtCancelamentoMovimento(GregorianCalendar dtCancelamentoMovimento) {
		this.dtCancelamentoMovimento = dtCancelamentoMovimento;
	}

	public GregorianCalendar getDtCancelamentoMovimento() {
		return this.dtCancelamentoMovimento;
	}

	public void setNrRemessa(int nrRemessa) {
		this.nrRemessa = nrRemessa;
	}

	public int getNrRemessa() {
		return this.nrRemessa;
	}

	public void setNrCodigoBarras(String nrCodigoBarras) {
		this.nrCodigoBarras = nrCodigoBarras;
	}

	public String getNrCodigoBarras() {
		return this.nrCodigoBarras;
	}

	public void setNrRemessaRegistro(int nrRemessaRegistro) {
		this.nrRemessaRegistro = nrRemessaRegistro;
	}

	public int getNrRemessaRegistro() {
		return this.nrRemessaRegistro;
	}

	public void setDtEmissaoNip(GregorianCalendar dtEmissaoNip) {
		this.dtEmissaoNip = dtEmissaoNip;
	}

	public GregorianCalendar getDtEmissaoNip() {
		return this.dtEmissaoNip;
	}

	public void setDtResultadoDefesa(GregorianCalendar dtResultadoDefesa) {
		this.dtResultadoDefesa = dtResultadoDefesa;
	}

	public GregorianCalendar getDtResultadoDefesa() {
		return this.dtResultadoDefesa;
	}

	public void setDtAtualizacao(GregorianCalendar dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	public GregorianCalendar getDtAtualizacao() {
		return this.dtAtualizacao;
	}

	public void setDtResultadoJari(GregorianCalendar dtResultadoJari) {
		this.dtResultadoJari = dtResultadoJari;
	}

	public GregorianCalendar getDtResultadoJari() {
		return this.dtResultadoJari;
	}

	public void setDtResultadoCetran(GregorianCalendar dtResultadoCetran) {
		this.dtResultadoCetran = dtResultadoCetran;
	}

	public GregorianCalendar getDtResultadoCetran() {
		return this.dtResultadoCetran;
	}

	public void setLgAdvertenciaJari(int lgAdvertenciaJari) {
		this.lgAdvertenciaJari = lgAdvertenciaJari;
	}

	public int getLgAdvertenciaJari() {
		return this.lgAdvertenciaJari;
	}

	public void setLgAdvertenciaCetran(int lgAdvertenciaCetran) {
		this.lgAdvertenciaCetran = lgAdvertenciaCetran;
	}

	public int getLgAdvertenciaCetran() {
		return this.lgAdvertenciaCetran;
	}

	public void setLgNotrigger(int lgNotrigger) {
		this.lgNotrigger = lgNotrigger;
	}

	public int getLgNotrigger() {
		return this.lgNotrigger;
	}

	public void setStDetran(int stDetran) {
		this.stDetran = stDetran;
	}

	public int getStDetran() {
		return this.stDetran;
	}

	public void setLgErro(int lgErro) {
		this.lgErro = lgErro;
	}

	public int getLgErro() {
		return this.lgErro;
	}

	public void setTxtObservacao(String txtObservacao) {
		this.txtObservacao = txtObservacao;
	}

	public String getTxtObservacao() {
		return this.txtObservacao;
	}

	public void setCdMovimentoAtual(int cdMovimentoAtual) {
		this.cdMovimentoAtual = cdMovimentoAtual;
	}

	public int getCdMovimentoAtual() {
		return this.cdMovimentoAtual;
	}

	public void setCdAitOrigem(int cdAitOrigem) {
		this.cdAitOrigem = cdAitOrigem;
	}

	public int getCdAitOrigem() {
		return this.cdAitOrigem;
	}

	public void setNrFatorNic(int nrFatorNic) {
		this.nrFatorNic = nrFatorNic;
	}

	public int getNrFatorNic() {
		return this.nrFatorNic;
	}

	public void setLgDetranFebraban(int lgDetranFebraban) {
		this.lgDetranFebraban = lgDetranFebraban;
	}

	public int getLgDetranFebraban() {
		return this.lgDetranFebraban;
	}

	public void setNrPlacaAntiga(String nrPlacaAntiga) {
		this.nrPlacaAntiga = nrPlacaAntiga;
	}

	public String getNrPlacaAntiga() {
		return this.nrPlacaAntiga;
	}

	public void setCdEspecieVeiculo(int cdEspecieVeiculo) {
		this.cdEspecieVeiculo = cdEspecieVeiculo;
	}

	public int getCdEspecieVeiculo() {
		return this.cdEspecieVeiculo;
	}

	public void setCdMarcaVeiculo(int cdMarcaVeiculo) {
		this.cdMarcaVeiculo = cdMarcaVeiculo;
	}

	public int getCdMarcaVeiculo() {
		return this.cdMarcaVeiculo;
	}

	public void setCdCorVeiculo(int cdCorVeiculo) {
		this.cdCorVeiculo = cdCorVeiculo;
	}

	public int getCdCorVeiculo() {
		return this.cdCorVeiculo;
	}

	public void setCdTipoVeiculo(int cdTipoVeiculo) {
		this.cdTipoVeiculo = cdTipoVeiculo;
	}

	public int getCdTipoVeiculo() {
		return this.cdTipoVeiculo;
	}

	public void setCdCategoriaVeiculo(int cdCategoriaVeiculo) {
		this.cdCategoriaVeiculo = cdCategoriaVeiculo;
	}

	public int getCdCategoriaVeiculo() {
		return this.cdCategoriaVeiculo;
	}

	public void setNrRenavan(String nrRenavan) {
		this.nrRenavan = nrRenavan;
	}

	public String getNrRenavan() {
		return this.nrRenavan;
	}

	public void setNrAnoFabricacao(String nrAnoFabricacao) {
		this.nrAnoFabricacao = nrAnoFabricacao;
	}

	public String getNrAnoFabricacao() {
		return this.nrAnoFabricacao;
	}

	public void setNrAnoModelo(String nrAnoModelo) {
		this.nrAnoModelo = nrAnoModelo;
	}

	public String getNrAnoModelo() {
		return this.nrAnoModelo;
	}

	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}

	public String getNmProprietario() {
		return this.nmProprietario;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public int getTpDocumento() {
		return this.tpDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getNrDocumento() {
		return this.nrDocumento;
	}

	public void setNmCondutor(String nmCondutor) {
		this.nmCondutor = nmCondutor;
	}

	public String getNmCondutor() {
		return this.nmCondutor;
	}

	public void setNrCnhCondutor(String nrCnhCondutor) {
		this.nrCnhCondutor = nrCnhCondutor;
	}

	public String getNrCnhCondutor() {
		return this.nrCnhCondutor;
	}

	public void setUfCnhCondutor(String ufCnhCondutor) {
		this.ufCnhCondutor = ufCnhCondutor;
	}

	public String getUfCnhCondutor() {
		return this.ufCnhCondutor;
	}

	public void setTpCnhCondutor(int tpCnhCondutor) {
		this.tpCnhCondutor = tpCnhCondutor;
	}

	public int getTpCnhCondutor() {
		return this.tpCnhCondutor;
	}

	public void setCdMarcaAutuacao(int cdMarcaAutuacao) {
		this.cdMarcaAutuacao = cdMarcaAutuacao;
	}

	public int getCdMarcaAutuacao() {
		return this.cdMarcaAutuacao;
	}

	public void setNmCondutorAutuacao(String nmCondutorAutuacao) {
		this.nmCondutorAutuacao = nmCondutorAutuacao;
	}

	public String getNmCondutorAutuacao() {
		return this.nmCondutorAutuacao;
	}

	public void setNmProprietarioAutuacao(String nmProprietarioAutuacao) {
		this.nmProprietarioAutuacao = nmProprietarioAutuacao;
	}

	public String getNmProprietarioAutuacao() {
		return this.nmProprietarioAutuacao;
	}

	public void setNrCnhAutuacao(String nrCnhAutuacao) {
		this.nrCnhAutuacao = nrCnhAutuacao;
	}

	public String getNrCnhAutuacao() {
		return this.nrCnhAutuacao;
	}

	public void setUfCnhAutuacao(String ufCnhAutuacao) {
		this.ufCnhAutuacao = ufCnhAutuacao;
	}

	public String getUfCnhAutuacao() {
		return this.ufCnhAutuacao;
	}

	public void setNrDocumentoAutuacao(String nrDocumentoAutuacao) {
		this.nrDocumentoAutuacao = nrDocumentoAutuacao;
	}

	public String getNrDocumentoAutuacao() {
		return this.nrDocumentoAutuacao;
	}

	public void setTpPessoaProprietario(int tpPessoaProprietario) {
		this.tpPessoaProprietario = tpPessoaProprietario;
	}

	public int getTpPessoaProprietario() {
		return this.tpPessoaProprietario;
	}

	public void setCdBanco(int cdBanco) {
		this.cdBanco = cdBanco;
	}

	public int getCdBanco() {
		return this.cdBanco;
	}

	public void setSgUfVeiculo(String sgUfVeiculo) {
		this.sgUfVeiculo = sgUfVeiculo;
	}

	public String getSgUfVeiculo() {
		return this.sgUfVeiculo;
	}

	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}

	public String getDsLogradouro() {
		return this.dsLogradouro;
	}

	public void setDsNrImovel(String dsNrImovel) {
		this.dsNrImovel = dsNrImovel;
	}

	public String getDsNrImovel() {
		return this.dsNrImovel;
	}

	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public String getNrCep() {
		return this.nrCep;
	}

	public void setNrDdd(String nrDdd) {
		this.nrDdd = nrDdd;
	}

	public String getNrDdd() {
		return this.nrDdd;
	}

	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}

	public String getNrTelefone() {
		return this.nrTelefone;
	}

	public void setDsEnderecoCondutor(String dsEnderecoCondutor) {
		this.dsEnderecoCondutor = dsEnderecoCondutor;
	}

	public String getDsEnderecoCondutor() {
		return this.dsEnderecoCondutor;
	}

	public void setNrCpfProprietario(String nrCpfProprietario) {
		this.nrCpfProprietario = nrCpfProprietario;
	}

	public String getNrCpfProprietario() {
		return this.nrCpfProprietario;
	}

	public void setNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		this.nrCpfCnpjProprietario = nrCpfCnpjProprietario;
	}

	public String getNrCpfCnpjProprietario() {
		return this.nrCpfCnpjProprietario;
	}

	public void setNmComplemento(String nmComplemento) {
		this.nmComplemento = nmComplemento;
	}

	public String getNmComplemento() {
		return this.nmComplemento;
	}

	public void setDsBairroCondutor(String dsBairroCondutor) {
		this.dsBairroCondutor = dsBairroCondutor;
	}

	public String getDsBairroCondutor() {
		return this.dsBairroCondutor;
	}

	public void setNrImovelCondutor(String nrImovelCondutor) {
		this.nrImovelCondutor = nrImovelCondutor;
	}

	public String getNrImovelCondutor() {
		return this.nrImovelCondutor;
	}

	public void setDsComplementoCondutor(String dsComplementoCondutor) {
		this.dsComplementoCondutor = dsComplementoCondutor;
	}

	public String getDsComplementoCondutor() {
		return this.dsComplementoCondutor;
	}

	public void setCdCidadeCondutor(int cdCidadeCondutor) {
		this.cdCidadeCondutor = cdCidadeCondutor;
	}

	public int getCdCidadeCondutor() {
		return this.cdCidadeCondutor;
	}

	public void setNrCepCondutor(String nrCepCondutor) {
		this.nrCepCondutor = nrCepCondutor;
	}

	public String getNrCepCondutor() {
		return this.nrCepCondutor;
	}

	public void setDtPrimeiroRegistro(GregorianCalendar dtPrimeiroRegistro) {
		this.dtPrimeiroRegistro = dtPrimeiroRegistro;
	}

	public GregorianCalendar getDtPrimeiroRegistro() {
		return this.dtPrimeiroRegistro;
	}

	public void setNrCpfCondutor(String nrCpfCondutor) {
		this.nrCpfCondutor = nrCpfCondutor;
	}

	public String getNrCpfCondutor() {
		return this.nrCpfCondutor;
	}

	public void setCdVeiculo(int cdVeiculo) {
		this.cdVeiculo = cdVeiculo;
	}

	public int getCdVeiculo() {
		return this.cdVeiculo;
	}

	public void setCdEndereco(int cdEndereco) {
		this.cdEndereco = cdEndereco;
	}

	public int getCdEndereco() {
		return this.cdEndereco;
	}

	public void setCdProprietario(int cdProprietario) {
		this.cdProprietario = cdProprietario;
	}

	public int getCdProprietario() {
		return this.cdProprietario;
	}

	public void setCdCondutor(int cdCondutor) {
		this.cdCondutor = cdCondutor;
	}

	public int getCdCondutor() {
		return this.cdCondutor;
	}

	public void setCdEnderecoCondutor(int cdEnderecoCondutor) {
		this.cdEnderecoCondutor = cdEnderecoCondutor;
	}

	public int getCdEnderecoCondutor() {
		return this.cdEnderecoCondutor;
	}

	public void setDtNotificacaoDevolucao(GregorianCalendar dtNotificacaoDevolucao) {
		this.dtNotificacaoDevolucao = dtNotificacaoDevolucao;
	}

	public GregorianCalendar getDtNotificacaoDevolucao() {
		return this.dtNotificacaoDevolucao;
	}

	public void setTpOrigem(int tpOrigem) {
		this.tpOrigem = tpOrigem;
	}

	public int getTpOrigem() {
		return this.tpOrigem;
	}

	public void setLgPublicarNaiDiarioOficial(int lgPublicarNaiDiarioOficial) {
		this.lgPublicarNaiDiarioOficial = lgPublicarNaiDiarioOficial;
	}

	public int getLgPublicarNaiDiarioOficial() {
		return this.lgPublicarNaiDiarioOficial;
	}

	public void setTpConvenio(int tpConvenio) {
		this.tpConvenio = tpConvenio;
	}

	public int getTpConvenio() {
		return this.tpConvenio;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public int getCdTalao() {
		return cdTalao;
	}

	public void setCdTalao(int cdTalao) {
		this.cdTalao = cdTalao;
	}

	public GregorianCalendar getDtAfericao() {
		return dtAfericao;
	}

	public void setDtAfericao(GregorianCalendar dtAfericao) {
		this.dtAfericao = dtAfericao;
	}

	public String getNrLacre() {
		return nrLacre;
	}

	public void setNrLacre(String nrLacre) {
		this.nrLacre = nrLacre;
	}

	public String getNrInventarioInmetro() {
		return nrInventarioInmetro;
	}

	public void setNrInventarioInmetro(String nrInventarioInmetro) {
		this.nrInventarioInmetro = nrInventarioInmetro;
	}

	public List<AitImagem> getImagens() {
		return imagens;
	}

	public void setImagens(List<AitImagem> imagens) {
		this.imagens = imagens;
	}

	public List<AitMovimento> getMovimentos() {
		return movimentos;
	}

	public void setMovimentos(List<AitMovimento> movimentos) {
		this.movimentos = movimentos;
	}

	public AitMovimento getMovimentoAtual() {
		return movimentoAtual;
	}

	public void setMovimentoAtual(AitMovimento movimentoAtual) {
		this.movimentoAtual = movimentoAtual;
	}

	public int getCdLogradouroInfracao() {
		return cdLogradouroInfracao;
	}

	public void setCdLogradouroInfracao(int cdLogradouroInfracao) {
		this.cdLogradouroInfracao = cdLogradouroInfracao;
	}

	public int getCdCidadeProprietario() {
		return cdCidadeProprietario;
	}

	public void setCdCidadeProprietario(int cdCidadeProprietario) {
		this.cdCidadeProprietario = cdCidadeProprietario;
	}
	
	public int getCdConvenio() {
		return cdConvenio;
	}

	public void setCdConvenio(int cdConvenio) {
		this.cdConvenio = cdConvenio;
	}
	
	public int getCdPais() {
		return cdPais;
	}

	public void setCdPais(int cdPais) {
		this.cdPais = cdPais;
	}	

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Ait ait = (Ait) o;
		return java.util.Objects.equals(idAit, ait.idAit);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idAit);
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " + getCdAit();
		valueToString += ", cdInfracao: " + getCdInfracao();
		valueToString += ", cdAgente: " + getCdAgente();
		valueToString += ", cdUsuario: " + getCdUsuario();
		valueToString += ", dtInfracao: "
				+ sol.util.Util.formatDateTime(getDtInfracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsObservacao: " + getDsObservacao();
		valueToString += ", dsLocalInfracao: " + getDsLocalInfracao();
		valueToString += ", nrAit: " + getNrAit();
		valueToString += ", vlVelocidadePermitida: " + getVlVelocidadePermitida();
		valueToString += ", vlVelocidadeAferida: " + getVlVelocidadeAferida();
		valueToString += ", vlVelocidadePenalidade: " + getVlVelocidadePenalidade();
		valueToString += ", nrPlaca: " + getNrPlaca();
		valueToString += ", dsPontoReferencia: " + getDsPontoReferencia();
		valueToString += ", lgAutoAssinado: " + getLgAutoAssinado();
		valueToString += ", vlLatitude: " + getVlLatitude();
		valueToString += ", vlLongitude: " + getVlLongitude();
		valueToString += ", cdCidade: " + getCdCidade();
		valueToString += ", dtDigitacao: "
				+ sol.util.Util.formatDateTime(getDtDigitacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdEquipamento: " + getCdEquipamento();
		valueToString += ", vlMulta: " + getVlMulta();
		valueToString += ", lgEnviadoDetran: " + getLgEnviadoDetran();
		valueToString += ", stAit: " + getStAit();
		valueToString += ", cdEventoEquipamento: " + getCdEventoEquipamento();
		valueToString += ", dtSincronizacao: "
				+ sol.util.Util.formatDateTime(getDtSincronizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrTentativaSincronizacao: " + getNrTentativaSincronizacao();
		valueToString += ", cdBairro: " + getCdBairro();
		valueToString += ", cdUsuarioExclusao: " + getCdUsuarioExclusao();
		valueToString += ", dtVencimento: "
				+ sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpNaturezaAutuacao: " + getTpNaturezaAutuacao();
		valueToString += ", dtMovimento: "
				+ sol.util.Util.formatDateTime(getDtMovimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpStatus: " + getTpStatus();
		valueToString += ", tpArquivo: " + getTpArquivo();
		valueToString += ", cdOcorrencia: " + getCdOcorrencia();
		valueToString += ", dsParecer: " + getDsParecer();
		valueToString += ", nrErro: " + getNrErro();
		valueToString += ", stEntrega: " + getStEntrega();
		valueToString += ", nrProcesso: " + getNrProcesso();
		valueToString += ", dtRegistroDetran: "
				+ sol.util.Util.formatDateTime(getDtRegistroDetran(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stRecurso: " + getStRecurso();
		valueToString += ", lgAdvertencia: " + getLgAdvertencia();
		valueToString += ", nrControle: " + getNrControle();
		valueToString += ", nrRenainf: " + getNrRenainf();
		valueToString += ", nrSequencial: " + getNrSequencial();
		valueToString += ", dtArNai: " + sol.util.Util.formatDateTime(getDtArNai(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrNotificacaoNai: " + getNrNotificacaoNai();
		valueToString += ", nrNotificacaoNip: " + getNrNotificacaoNip();
		valueToString += ", tpCancelamento: " + getTpCancelamento();
		valueToString += ", lgCancelaMovimento: " + getLgCancelaMovimento();
		valueToString += ", dtCancelamentoMovimento: "
				+ sol.util.Util.formatDateTime(getDtCancelamentoMovimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrRemessa: " + getNrRemessa();
		valueToString += ", nrCodigoBarras: " + getNrCodigoBarras();
		valueToString += ", nrRemessaRegistro: " + getNrRemessaRegistro();
		valueToString += ", dtEmissaoNip: "
				+ sol.util.Util.formatDateTime(getDtEmissaoNip(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtResultadoDefesa: "
				+ sol.util.Util.formatDateTime(getDtResultadoDefesa(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAtualizacao: "
				+ sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtResultadoJari: "
				+ sol.util.Util.formatDateTime(getDtResultadoJari(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtResultadoCetran: "
				+ sol.util.Util.formatDateTime(getDtResultadoCetran(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgAdvertenciaJari: " + getLgAdvertenciaJari();
		valueToString += ", lgAdvertenciaCetran: " + getLgAdvertenciaCetran();
		valueToString += ", lgNotrigger: " + getLgNotrigger();
		valueToString += ", stDetran: " + getStDetran();
		valueToString += ", lgErro: " + getLgErro();
		valueToString += ", txtObservacao: " + getTxtObservacao();
		valueToString += ", cdMovimentoAtual: " + getCdMovimentoAtual();
		valueToString += ", cdAitOrigem: " + getCdAitOrigem();
		valueToString += ", nrFatorNic: " + getNrFatorNic();
		valueToString += ", lgDetranFebraban: " + getLgDetranFebraban();
		valueToString += ", nrPlacaAntiga: " + getNrPlacaAntiga();
		valueToString += ", cdEspecieVeiculo: " + getCdEspecieVeiculo();
		valueToString += ", cdMarcaVeiculo: " + getCdMarcaVeiculo();
		valueToString += ", cdCorVeiculo: " + getCdCorVeiculo();
		valueToString += ", cdTipoVeiculo: " + getCdTipoVeiculo();
		valueToString += ", cdCategoriaVeiculo: " + getCdCategoriaVeiculo();
		valueToString += ", nrRenavan: " + getNrRenavan();
		valueToString += ", nrAnoFabricacao: " + getNrAnoFabricacao();
		valueToString += ", nrAnoModelo: " + getNrAnoModelo();
		valueToString += ", nmProprietario: " + getNmProprietario();
		valueToString += ", tpDocumento: " + getTpDocumento();
		valueToString += ", nrDocumento: " + getNrDocumento();
		valueToString += ", nmCondutor: " + getNmCondutor();
		valueToString += ", nrCnhCondutor: " + getNrCnhCondutor();
		valueToString += ", ufCnhCondutor: " + getUfCnhCondutor();
		valueToString += ", tpCnhCondutor: " + getTpCnhCondutor();
		valueToString += ", cdMarcaAutuacao: " + getCdMarcaAutuacao();
		valueToString += ", nmCondutorAutuacao: " + getNmCondutorAutuacao();
		valueToString += ", nmProprietarioAutuacao: " + getNmProprietarioAutuacao();
		valueToString += ", nrCnhAutuacao: " + getNrCnhAutuacao();
		valueToString += ", ufCnhAutuacao: " + getUfCnhAutuacao();
		valueToString += ", nrDocumentoAutuacao: " + getNrDocumentoAutuacao();
		valueToString += ", tpPessoaProprietario: " + getTpPessoaProprietario();
		valueToString += ", cdBanco: " + getCdBanco();
		valueToString += ", sgUfVeiculo: " + getSgUfVeiculo();
		valueToString += ", dsLogradouro: " + getDsLogradouro();
		valueToString += ", dsNrImovel: " + getDsNrImovel();
		valueToString += ", nrCep: " + getNrCep();
		valueToString += ", nrDdd: " + getNrDdd();
		valueToString += ", nrTelefone: " + getNrTelefone();
		valueToString += ", dsEnderecoCondutor: " + getDsEnderecoCondutor();
		valueToString += ", nrCpfProprietario: " + getNrCpfProprietario();
		valueToString += ", nrCpfCnpjProprietario: " + getNrCpfCnpjProprietario();
		valueToString += ", nmComplemento: " + getNmComplemento();
		valueToString += ", dsBairroCondutor: " + getDsBairroCondutor();
		valueToString += ", nrImovelCondutor: " + getNrImovelCondutor();
		valueToString += ", dsComplementoCondutor: " + getDsComplementoCondutor();
		valueToString += ", cdCidadeCondutor: " + getCdCidadeCondutor();
		valueToString += ", nrCepCondutor: " + getNrCepCondutor();
		valueToString += ", dtPrimeiroRegistro: "
				+ sol.util.Util.formatDateTime(getDtPrimeiroRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrCpfCondutor: " + getNrCpfCondutor();
		valueToString += ", cdVeiculo: " + getCdVeiculo();
		valueToString += ", cdEndereco: " + getCdEndereco();
		valueToString += ", cdProprietario: " + getCdProprietario();
		valueToString += ", cdCondutor: " + getCdCondutor();
		valueToString += ", cdEnderecoCondutor: " + getCdEnderecoCondutor();
		valueToString += ", dtNotificacaoDevolucao: "
				+ sol.util.Util.formatDateTime(getDtNotificacaoDevolucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpOrigem: " + getTpOrigem();
		valueToString += ", lgPublicarNaiDiarioOficial: " + getLgPublicarNaiDiarioOficial();
		valueToString += ", tpConvenio: " + getTpConvenio();
		valueToString += ", idAit: " + getIdAit();
		valueToString += ", dtAfericao: "
				+ sol.util.Util.formatDateTime(getDtAfericao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrLacre: " + getNrLacre();
		valueToString += ", nrInventarioInmetro: " + getNrInventarioInmetro();
		valueToString += ", cdLogradouroInfracao: " + getCdLogradouroInfracao();
		valueToString += ", cdCidadeProprietario: " + getCdCidadeProprietario();
		valueToString += ", cdConvenio: " + getCdConvenio();
		valueToString += ", cdPais: " + getCdPais();
		valueToString += ", txtCancelamento: " + getTxtCancelamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ait(getCdAit(),
				getCdInfracao(),
				getCdAgente(),
				getCdUsuario(),
				getDtInfracao() == null ? null : (GregorianCalendar) getDtInfracao().clone(),
				getDsObservacao(),
				getDsLocalInfracao(),
				getNrAit(),
				getVlVelocidadePermitida(),
				getVlVelocidadeAferida(),
				getVlVelocidadePenalidade(),
				getNrPlaca(),
				getDsPontoReferencia(),
				getLgAutoAssinado(),
				getVlLatitude(),
				getVlLongitude(),
				getCdCidade(),
				getDtDigitacao() == null ? null : (GregorianCalendar) getDtDigitacao().clone(),
				getCdEquipamento(),
				getVlMulta(),
				getLgEnviadoDetran(),
				getStAit(),
				getCdEventoEquipamento(),
				getDtSincronizacao() == null ? null : (GregorianCalendar) getDtSincronizacao().clone(),
				getNrTentativaSincronizacao(),
				getCdBairro(),
				getCdUsuarioExclusao(),
				getDtVencimento() == null ? null : (GregorianCalendar) getDtVencimento().clone(),
				getTpNaturezaAutuacao(),
				getDtMovimento() == null ? null : (GregorianCalendar) getDtMovimento().clone(),
				getTpStatus(),
				getTpArquivo(),
				getCdOcorrencia(),
				getDsParecer(),
				getNrErro(),
				getStEntrega(),
				getNrProcesso(),
				getDtRegistroDetran() == null ? null : (GregorianCalendar) getDtRegistroDetran().clone(),
				getStRecurso(),
				getLgAdvertencia(),
				getNrControle(),
				getNrRenainf(),
				getNrSequencial(),
				getDtArNai() == null ? null : (GregorianCalendar) getDtArNai().clone(),
				getNrNotificacaoNai(),
				getNrNotificacaoNip(),
				getTpCancelamento(),
				getLgCancelaMovimento(),
				getDtCancelamentoMovimento() == null ? null : (GregorianCalendar) getDtCancelamentoMovimento().clone(),
				getNrRemessa(),
				getNrCodigoBarras(),
				getNrRemessaRegistro(),
				getDtEmissaoNip() == null ? null : (GregorianCalendar) getDtEmissaoNip().clone(),
				getDtResultadoDefesa() == null ? null : (GregorianCalendar) getDtResultadoDefesa().clone(),
				getDtAtualizacao() == null ? null : (GregorianCalendar) getDtAtualizacao().clone(),
				getDtResultadoJari() == null ? null : (GregorianCalendar) getDtResultadoJari().clone(),
				getDtResultadoCetran() == null ? null : (GregorianCalendar) getDtResultadoCetran().clone(),
				getDtPrazoDefesa() == null ? null : (GregorianCalendar) getDtPrazoDefesa().clone(),
				getLgAdvertenciaJari(),
				getLgAdvertenciaCetran(),
				getLgNotrigger(),
				getStDetran(),
				getLgErro(),
				getTxtObservacao(),
				getCdMovimentoAtual(),
				getCdAitOrigem(),
				getNrFatorNic(),
				getLgDetranFebraban(),
				getNrPlacaAntiga(),
				getCdEspecieVeiculo(),
				getCdMarcaVeiculo(),
				getCdCorVeiculo(),
				getCdTipoVeiculo(),
				getCdCategoriaVeiculo(),
				getNrRenavan(),
				getNrAnoFabricacao(),
				getNrAnoModelo(),
				getNmProprietario(),
				getTpDocumento(),
				getNrDocumento(),
				getNmCondutor(),
				getNrCnhCondutor(),
				getUfCnhCondutor(),
				getTpCnhCondutor(),
				getCdMarcaAutuacao(),
				getNmCondutorAutuacao(),
				getNmProprietarioAutuacao(),
				getNrCnhAutuacao(),
				getUfCnhAutuacao(),
				getNrDocumentoAutuacao(),
				getTpPessoaProprietario(),
				getCdBanco(),
				getSgUfVeiculo(),
				getDsLogradouro(),
				getDsNrImovel(),
				getNrCep(),
				getNrDdd(),
				getNrTelefone(),
				getDsEnderecoCondutor(),
				getNrCpfProprietario(),
				getNrCpfCnpjProprietario(),
				getNmComplemento(),
				getDsBairroCondutor(),
				getNrImovelCondutor(),
				getDsComplementoCondutor(),
				getCdCidadeCondutor(),
				getNrCepCondutor(),
				getDtPrimeiroRegistro() == null ? null : (GregorianCalendar) getDtPrimeiroRegistro().clone(),
				getNrCpfCondutor(),
				getCdVeiculo(),
				getCdEndereco(),
				getCdProprietario(),
				getCdCondutor(),
				getCdEnderecoCondutor(),
				getDtNotificacaoDevolucao() == null ? null : (GregorianCalendar) getDtNotificacaoDevolucao().clone(),
				getTpOrigem(),
				getLgPublicarNaiDiarioOficial(),
				getTpConvenio(),
				getIdAit(),
				getCdTalao(),
				getDtAfericao(),
				getNrLacre(),
				getNrInventarioInmetro(),
				getCdLogradouroInfracao(),
				getCdCidadeProprietario(),
				getCdConvenio(),
				getCdPais(),
				getTxtCancelamento());
	}
}
