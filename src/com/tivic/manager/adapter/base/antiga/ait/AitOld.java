package com.tivic.manager.adapter.base.antiga.ait;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitOld {

	private int codBanco;
	private int codigoAit;
	private int codBairro;
	private int codInfracao;
	private int codUsuario;
	private int codEspecie;
	private int codMarca;
	private int codAgente;
	private int codCor;
	private int codTipo;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private int codCategoria;
	private int codMunicipio;
	private String dsObservacao;
	private String dsLocalInfracao;
	private String ufVeiculo;
	private Long cdRenavan;
	private String dsAnoFabricacao;
	private String dsAnoModelo;
	private String nmProprietario;
	private int tpDocumento;
	private String nrDocumento;
	private String dsLogradouro;
	private String dsNrImovel;
	private String nrCep;
	private String nrDdd;
	private String nrTelefone;
	private int nrRemessa;
	private String nrAit;
	private String nrCodigoBarras;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String ufCnhCondutor;
	private String dsEnderecoCondutor;
	private String nrCpfProprietario;
	private Double vlVelocidadePermitida;
	private Double vlVelocidadeAferida;
	private Double vlVelocidadePenalidade;
	private String nrPlaca;
	private int cdUsuarioExclusao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	private String dsPontoReferencia;
	private int lgAutoAssinado;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtDigitacao;
	private int tpNaturezaAutuacao;
	private Integer tpCnhCondutor;
	private int tpPessoaProprietario;
	private String nrCpfCnpjProprietario;
	private String nmComplemento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	private int tpStatus;
	private int tpArquivo;
	private int codOcorrencia;
	private String dsParecer;
	private String nrErro;
	private int lgEnviadoDetran;
	private int stEntrega;
	private String nrProcesso;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtRegistroDetran;
	private int stRecurso;
	private int lgAdvertencia;
	private int nrControle;
	private Long nrRenainf;
	private int nrSequencial;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtArNai;
	private int nrNotificacaoNai;
	private String dsBairroCondutor;
	private String nrImovelCondutor;
	private String dsComplementoCondutor;
	private int cdMunicipioCondutor;
	private String nrCepCondutor;
	private int nrNotificacaoNip;
	private int tpCancelamento;
	private int lgCancelaMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCancelamentoMovimento;
	private int nrRemessaRegistro;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrimeiroRegistro;
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
	private byte[] blbFoto;
	private int lgAdvertenciaJari;
	private int lgAdvertenciaCetran;
	private int lgNotrigger;
	private String stDetran;
	private int lgErro;
	private String nrCpfCondutor;
	private int cdVeiculo;
	private int cdEndereco;
	private int cdProprietario;
	private int cdCondutor;
	private int cdEnderecoCondutor;
	private String txtObservacao;
	private int cdMovimentoAtual;
	private int cdEquipamento;
	private Double vlLatitude;
	private Double vlLongitude;
	private int codMarcaAutuacao;
	private String nmCondutorAutuacao;
	private String nmProprietarioAutuacao;
	private String nrCnhAutuacao;
	private String ufCnhAutuacao;
	private String nrDocumentoAutuacao;
	private int cdAitOrigem;
	private Double vlMulta;
	private int nrFatorNic;
	private int lgDetranFebraban;
	private int tpOrigem;
	private String nrPlacaAntiga;
	private int stAit;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtSincronizacao;
	private int cdEventoEquipamento;
	private int tpConvenio;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAfericao;
	private String nrLacre;
	private String nrInventarioInmetro;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAdesaoSne;
	private int stOptanteSne;
	private int lgPenalidadeAdvertenciaNip;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtPrazoDefesa;
	private int codEspecieAutuacao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtNotificacaoDevolucao;
	private int codTalao;
	
	private String txtCancelamento;

	public AitOld () { }

	public AitOld (int codBanco,
			int codigoAit,
			int codBairro,
			int codInfracao,
			int codUsuario,
			int codEspecie,
			int codMarca,
			int codAgente,
			int codCor,
			int codTipo,
			GregorianCalendar dtInfracao,
			int codCategoria,
			int codMunicipio,
			String dsObservacao,
			String dsLocalInfracao,
			String ufVeiculo,
			Long cdRenavan,
			String dsAnoFabricacao,
			String dsAnoModelo,
			String nmProprietario,
			int tpDocumento,
			String nrDocumento,
			String dsLogradouro,
			String dsNrImovel,
			String nrCep,
			String nrDdd,
			String nrTelefone,
			int nrRemessa,
			String nrAit,
			String nrCodigoBarras,
			String nmCondutor,
			String nrCnhCondutor,
			String ufCnhCondutor,
			String dsEnderecoCondutor,
			String nrCpfProprietario,
			Double vlVelocidadePermitida,
			Double vlVelocidadeAferida,
			Double vlVelocidadePenalidade,
			String nrPlaca,
			int cdUsuarioExclusao,
			GregorianCalendar dtVencimento,
			String dsPontoReferencia,
			int lgAutoAssinado,
			GregorianCalendar dtDigitacao,
			int tpNaturezaAutuacao,
			Integer tpCnhCondutor,
			int tpPessoaProprietario,
			String nrCpfCnpjProprietario,
			String nmComplemento,
			GregorianCalendar dtMovimento,
			int tpStatus,
			int tpArquivo,
			int codOcorrencia,
			String dsParecer,
			String nrErro,
			int lgEnviadoDetran,
			int stEntrega,
			String nrProcesso,
			GregorianCalendar dtRegistroDetran,
			int stRecurso,
			int lgAdvertencia,
			int nrControle,
			Long nrRenainf,
			int nrSequencial,
			GregorianCalendar dtArNai,
			int nrNotificacaoNai,
			String dsBairroCondutor,
			String nrImovelCondutor,
			String dsComplementoCondutor,
			int cdMunicipioCondutor,
			String nrCepCondutor,
			int nrNotificacaoNip,
			int tpCancelamento,
			int lgCancelaMovimento,
			GregorianCalendar dtCancelamentoMovimento,
			int nrRemessaRegistro,
			GregorianCalendar dtPrimeiroRegistro,
			GregorianCalendar dtEmissaoNip,
			GregorianCalendar dtResultadoDefesa,
			GregorianCalendar dtAtualizacao,
			GregorianCalendar dtResultadoJari,
			GregorianCalendar dtResultadoCetran,
			byte[] blbFoto,
			int lgAdvertenciaJari,
			int lgAdvertenciaCetran,
			int lgNotrigger,
			String stDetran,
			int lgErro,
			String nrCpfCondutor,
			int cdVeiculo,
			int cdEndereco,
			int cdProprietario,
			int cdCondutor,
			int cdEnderecoCondutor,
			String txtObservacao,
			int cdMovimentoAtual,
			int cdEquipamento,
			Double vlLatitude,
			Double vlLongitude,
			int codMarcaAutuacao,
			String nmCondutorAutuacao,
			String nmProprietarioAutuacao,
			String nrCnhAutuacao,
			String ufCnhAutuacao,
			String nrDocumentoAutuacao,
			int cdAitOrigem,
			Double vlMulta,
			int nrFatorNic,
			int lgDetranFebraban,
			int tpOrigem,
			String nrPlacaAntiga,
			int stAit,
			GregorianCalendar dtSincronizacao,
			int cdEventoEquipamento,
			int tpConvenio,
			GregorianCalendar dtAfericao,
			String nrLacre,
			String nrInventarioInmetro,
			GregorianCalendar dtAdesaoSne,
			int stOptanteSne,
			int lgPenalidadeAdvertenciaNip,
			GregorianCalendar dtPrazoDefesa,
			int codEspecieAutuacao,
			GregorianCalendar dtNotificacaoDevolucao,
			int codTalao,
			String txtCancelamento) {
		setCodBanco(codBanco);
		setCodigoAit(codigoAit);
		setCodBairro(codBairro);
		setCodInfracao(codInfracao);
		setCodUsuario(codUsuario);
		setCodEspecie(codEspecie);
		setCodMarca(codMarca);
		setCodAgente(codAgente);
		setCodCor(codCor);
		setCodTipo(codTipo);
		setDtInfracao(dtInfracao);
		setCodCategoria(codCategoria);
		setCodMunicipio(codMunicipio);
		setDsObservacao(dsObservacao);
		setDsLocalInfracao(dsLocalInfracao);
		setUfVeiculo(ufVeiculo);
		setCdRenavan(cdRenavan);
		setDsAnoFabricacao(dsAnoFabricacao);
		setDsAnoModelo(dsAnoModelo);
		setNmProprietario(nmProprietario);
		setTpDocumento(tpDocumento);
		setNrDocumento(nrDocumento);
		setDsLogradouro(dsLogradouro);
		setDsNrImovel(dsNrImovel);
		setNrCep(nrCep);
		setNrDdd(nrDdd);
		setNrTelefone(nrTelefone);
		setNrRemessa(nrRemessa);
		setNrAit(nrAit);
		setNrCodigoBarras(nrCodigoBarras);
		setNmCondutor(nmCondutor);
		setNrCnhCondutor(nrCnhCondutor);
		setUfCnhCondutor(ufCnhCondutor);
		setDsEnderecoCondutor(dsEnderecoCondutor);
		setNrCpfProprietario(nrCpfProprietario);
		setVlVelocidadePermitida(vlVelocidadePermitida);
		setVlVelocidadeAferida(vlVelocidadeAferida);
		setVlVelocidadePenalidade(vlVelocidadePenalidade);
		setNrPlaca(nrPlaca);
		setCdUsuarioExclusao(cdUsuarioExclusao);
		setDtVencimento(dtVencimento);
		setDsPontoReferencia(dsPontoReferencia);
		setLgAutoAssinado(lgAutoAssinado);
		setDtDigitacao(dtDigitacao);
		setTpNaturezaAutuacao(tpNaturezaAutuacao);
		setTpCnhCondutor(tpCnhCondutor);
		setTpPessoaProprietario(tpPessoaProprietario);
		setNrCpfCnpjProprietario(nrCpfCnpjProprietario);
		setNmComplemento(nmComplemento);
		setDtMovimento(dtMovimento);
		setTpStatus(tpStatus);
		setTpArquivo(tpArquivo);
		setCodOcorrencia(codOcorrencia);
		setDsParecer(dsParecer);
		setNrErro(nrErro);
		setLgEnviadoDetran(lgEnviadoDetran);
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
		setDsBairroCondutor(dsBairroCondutor);
		setNrImovelCondutor(nrImovelCondutor);
		setDsComplementoCondutor(dsComplementoCondutor);
		setCdMunicipioCondutor(cdMunicipioCondutor);
		setNrCepCondutor(nrCepCondutor);
		setNrNotificacaoNip(nrNotificacaoNip);
		setTpCancelamento(tpCancelamento);
		setLgCancelaMovimento(lgCancelaMovimento);
		setDtCancelamentoMovimento(dtCancelamentoMovimento);
		setNrRemessaRegistro(nrRemessaRegistro);
		setDtPrimeiroRegistro(dtPrimeiroRegistro);
		setDtEmissaoNip(dtEmissaoNip);
		setDtResultadoDefesa(dtResultadoDefesa);
		setDtAtualizacao(dtAtualizacao);
		setDtResultadoJari(dtResultadoJari);
		setDtResultadoCetran(dtResultadoCetran);
		setBlbFoto(blbFoto);
		setLgAdvertenciaJari(lgAdvertenciaJari);
		setLgAdvertenciaCetran(lgAdvertenciaCetran);
		setLgNotrigger(lgNotrigger);
		setStDetran(stDetran);
		setLgErro(lgErro);
		setNrCpfCondutor(nrCpfCondutor);
		setCdVeiculo(cdVeiculo);
		setCdEndereco(cdEndereco);
		setCdProprietario(cdProprietario);
		setCdCondutor(cdCondutor);
		setCdEnderecoCondutor(cdEnderecoCondutor);
		setTxtObservacao(txtObservacao);
		setCdMovimentoAtual(cdMovimentoAtual);
		setCdEquipamento(cdEquipamento);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCodMarcaAutuacao(codMarcaAutuacao);
		setNmCondutorAutuacao(nmCondutorAutuacao);
		setNmProprietarioAutuacao(nmProprietarioAutuacao);
		setNrCnhAutuacao(nrCnhAutuacao);
		setUfCnhAutuacao(ufCnhAutuacao);
		setNrDocumentoAutuacao(nrDocumentoAutuacao);
		setCdAitOrigem(cdAitOrigem);
		setVlMulta(vlMulta);
		setNrFatorNic(nrFatorNic);
		setLgDetranFebraban(lgDetranFebraban);
		setTpOrigem(tpOrigem);
		setNrPlacaAntiga(nrPlacaAntiga);
		setStAit(stAit);
		setDtSincronizacao(dtSincronizacao);
		setCdEventoEquipamento(cdEventoEquipamento);
		setTpConvenio(tpConvenio);
		setDtAfericao(dtAfericao);
		setNrLacre(nrLacre);
		setNrInventarioInmetro(nrInventarioInmetro);
		setDtAdesaoSne(dtAdesaoSne);
		setStOptanteSne(stOptanteSne);
		setLgPenalidadeAdvertenciaNip(lgPenalidadeAdvertenciaNip);
		setDtPrazoDefesa(dtPrazoDefesa);
		setCodEspecieAutuacao(codEspecieAutuacao);
		setDtNotificacaoDevolucao(dtNotificacaoDevolucao);
		setCodTalao(codTalao);
		setTxtCancelamento(txtCancelamento);
	}
	public void setCodBanco(int codBanco){
		this.codBanco=codBanco;
	}
	public int getCodBanco(){
		return this.codBanco;
	}
	public void setCodigoAit(int codigoAit){
		this.codigoAit=codigoAit;
	}
	public int getCodigoAit(){
		return this.codigoAit;
	}
	public void setCodBairro(int codBairro){
		this.codBairro=codBairro;
	}
	public int getCodBairro(){
		return this.codBairro;
	}
	public void setCodInfracao(int codInfracao){
		this.codInfracao=codInfracao;
	}
	public int getCodInfracao(){
		return this.codInfracao;
	}
	public void setCodUsuario(int codUsuario){
		this.codUsuario=codUsuario;
	}
	public int getCodUsuario(){
		return this.codUsuario;
	}
	public void setCodEspecie(int codEspecie){
		this.codEspecie=codEspecie;
	}
	public int getCodEspecie(){
		return this.codEspecie;
	}
	public void setCodMarca(int codMarca){
		this.codMarca=codMarca;
	}
	public int getCodMarca(){
		return this.codMarca;
	}
	public void setCodAgente(int codAgente){
		this.codAgente=codAgente;
	}
	public int getCodAgente(){
		return this.codAgente;
	}
	public void setCodCor(int codCor){
		this.codCor=codCor;
	}
	public int getCodCor(){
		return this.codCor;
	}
	public void setCodTipo(int codTipo){
		this.codTipo=codTipo;
	}
	public int getCodTipo(){
		return this.codTipo;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao){
		this.dtInfracao=dtInfracao;
	}
	public GregorianCalendar getDtInfracao(){
		return this.dtInfracao;
	}
	public void setCodCategoria(int codCategoria){
		this.codCategoria=codCategoria;
	}
	public int getCodCategoria(){
		return this.codCategoria;
	}
	public void setCodMunicipio(int codMunicipio){
		this.codMunicipio=codMunicipio;
	}
	public int getCodMunicipio(){
		return this.codMunicipio;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setDsLocalInfracao(String dsLocalInfracao){
		this.dsLocalInfracao=dsLocalInfracao;
	}
	public String getDsLocalInfracao(){
		return this.dsLocalInfracao;
	}
	public void setUfVeiculo(String ufVeiculo){
		this.ufVeiculo=ufVeiculo;
	}
	public String getUfVeiculo(){
		return this.ufVeiculo;
	}
	public void setCdRenavan(Long cdRenavan){
		this.cdRenavan=cdRenavan;
	}
	public Long getCdRenavan(){
		return this.cdRenavan;
	}
	public void setDsAnoFabricacao(String dsAnoFabricacao){
		this.dsAnoFabricacao=dsAnoFabricacao;
	}
	public String getDsAnoFabricacao(){
		return this.dsAnoFabricacao;
	}
	public void setDsAnoModelo(String dsAnoModelo){
		this.dsAnoModelo=dsAnoModelo;
	}
	public String getDsAnoModelo(){
		return this.dsAnoModelo;
	}
	public void setNmProprietario(String nmProprietario){
		this.nmProprietario=nmProprietario;
	}
	public String getNmProprietario(){
		return this.nmProprietario;
	}
	public void setTpDocumento(int tpDocumento){
		this.tpDocumento=tpDocumento;
	}
	public int getTpDocumento(){
		return this.tpDocumento;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setDsLogradouro(String dsLogradouro){
		this.dsLogradouro=dsLogradouro;
	}
	public String getDsLogradouro(){
		return this.dsLogradouro;
	}
	public void setDsNrImovel(String dsNrImovel){
		this.dsNrImovel=dsNrImovel;
	}
	public String getDsNrImovel(){
		return this.dsNrImovel;
	}
	public void setNrCep(String nrCep){
		this.nrCep=nrCep;
	}
	public String getNrCep(){
		return this.nrCep;
	}
	public void setNrDdd(String nrDdd){
		this.nrDdd=nrDdd;
	}
	public String getNrDdd(){
		return this.nrDdd;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setNrRemessa(int nrRemessa){
		this.nrRemessa=nrRemessa;
	}
	public int getNrRemessa(){
		return this.nrRemessa;
	}
	public void setNrAit(String nrAit){
		this.nrAit=nrAit;
	}
	public String getNrAit(){
		return this.nrAit;
	}
	public void setNrCodigoBarras(String nrCodigoBarras){
		this.nrCodigoBarras=nrCodigoBarras;
	}
	public String getNrCodigoBarras(){
		return this.nrCodigoBarras;
	}
	public void setNmCondutor(String nmCondutor){
		this.nmCondutor=nmCondutor;
	}
	public String getNmCondutor(){
		return this.nmCondutor;
	}
	public void setNrCnhCondutor(String nrCnhCondutor){
		this.nrCnhCondutor=nrCnhCondutor;
	}
	public String getNrCnhCondutor(){
		return this.nrCnhCondutor;
	}
	public void setUfCnhCondutor(String ufCnhCondutor){
		this.ufCnhCondutor=ufCnhCondutor;
	}
	public String getUfCnhCondutor(){
		return this.ufCnhCondutor;
	}
	public void setDsEnderecoCondutor(String dsEnderecoCondutor){
		this.dsEnderecoCondutor=dsEnderecoCondutor;
	}
	public String getDsEnderecoCondutor(){
		return this.dsEnderecoCondutor;
	}
	public void setNrCpfProprietario(String nrCpfProprietario){
		this.nrCpfProprietario=nrCpfProprietario;
	}
	public String getNrCpfProprietario(){
		return this.nrCpfProprietario;
	}
	public void setVlVelocidadePermitida(Double vlVelocidadePermitida){
		this.vlVelocidadePermitida=vlVelocidadePermitida;
	}
	public Double getVlVelocidadePermitida(){
		return this.vlVelocidadePermitida;
	}
	public void setVlVelocidadeAferida(Double vlVelocidadeAferida){
		this.vlVelocidadeAferida=vlVelocidadeAferida;
	}
	public Double getVlVelocidadeAferida(){
		return this.vlVelocidadeAferida;
	}
	public void setVlVelocidadePenalidade(Double vlVelocidadePenalidade){
		this.vlVelocidadePenalidade=vlVelocidadePenalidade;
	}
	public Double getVlVelocidadePenalidade(){
		return this.vlVelocidadePenalidade;
	}
	public void setNrPlaca(String nrPlaca){
		this.nrPlaca=nrPlaca;
	}
	public String getNrPlaca(){
		return this.nrPlaca;
	}
	public void setCdUsuarioExclusao(int cdUsuarioExclusao){
		this.cdUsuarioExclusao=cdUsuarioExclusao;
	}
	public int getCdUsuarioExclusao(){
		return this.cdUsuarioExclusao;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento){
		this.dtVencimento=dtVencimento;
	}
	public GregorianCalendar getDtVencimento(){
		return this.dtVencimento;
	}
	public void setDsPontoReferencia(String dsPontoReferencia){
		this.dsPontoReferencia=dsPontoReferencia;
	}
	public String getDsPontoReferencia(){
		return this.dsPontoReferencia;
	}
	public void setLgAutoAssinado(int lgAutoAssinado){
		this.lgAutoAssinado=lgAutoAssinado;
	}
	public int getLgAutoAssinado(){
		return this.lgAutoAssinado;
	}
	public void setDtDigitacao(GregorianCalendar dtDigitacao){
		this.dtDigitacao=dtDigitacao;
	}
	public GregorianCalendar getDtDigitacao(){
		return this.dtDigitacao;
	}
	public void setTpNaturezaAutuacao(int tpNaturezaAutuacao){
		this.tpNaturezaAutuacao=tpNaturezaAutuacao;
	}
	public int getTpNaturezaAutuacao(){
		return this.tpNaturezaAutuacao;
	}
	public void setTpCnhCondutor(Integer tpCnhCondutor){
		this.tpCnhCondutor=tpCnhCondutor;
	}
	public Integer getTpCnhCondutor(){
		return this.tpCnhCondutor;
	}
	public void setTpPessoaProprietario(int tpPessoaProprietario){
		this.tpPessoaProprietario=tpPessoaProprietario;
	}
	public int getTpPessoaProprietario(){
		return this.tpPessoaProprietario;
	}
	public void setNrCpfCnpjProprietario(String nrCpfCnpjProprietario){
		this.nrCpfCnpjProprietario=nrCpfCnpjProprietario;
	}
	public String getNrCpfCnpjProprietario(){
		return this.nrCpfCnpjProprietario;
	}
	public void setNmComplemento(String nmComplemento){
		this.nmComplemento=nmComplemento;
	}
	public String getNmComplemento(){
		return this.nmComplemento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento){
		this.dtMovimento=dtMovimento;
	}
	public GregorianCalendar getDtMovimento(){
		return this.dtMovimento;
	}
	public void setTpStatus(int tpStatus){
		this.tpStatus=tpStatus;
	}
	public int getTpStatus(){
		return this.tpStatus;
	}
	public void setTpArquivo(int tpArquivo){
		this.tpArquivo=tpArquivo;
	}
	public int getTpArquivo(){
		return this.tpArquivo;
	}
	public void setCodOcorrencia(int codOcorrencia){
		this.codOcorrencia=codOcorrencia;
	}
	public int getCodOcorrencia(){
		return this.codOcorrencia;
	}
	public void setDsParecer(String dsParecer){
		this.dsParecer=dsParecer;
	}
	public String getDsParecer(){
		return this.dsParecer;
	}
	public void setNrErro(String nrErro){
		this.nrErro=nrErro;
	}
	public String getNrErro(){
		return this.nrErro;
	}
	public void setLgEnviadoDetran(int lgEnviadoDetran){
		this.lgEnviadoDetran=lgEnviadoDetran;
	}
	public int getLgEnviadoDetran(){
		return this.lgEnviadoDetran;
	}
	public void setStEntrega(int stEntrega){
		this.stEntrega=stEntrega;
	}
	public int getStEntrega(){
		return this.stEntrega;
	}
	public void setNrProcesso(String nrProcesso){
		this.nrProcesso=nrProcesso;
	}
	public String getNrProcesso(){
		return this.nrProcesso;
	}
	public void setDtRegistroDetran(GregorianCalendar dtRegistroDetran){
		this.dtRegistroDetran=dtRegistroDetran;
	}
	public GregorianCalendar getDtRegistroDetran(){
		return this.dtRegistroDetran;
	}
	public void setStRecurso(int stRecurso){
		this.stRecurso=stRecurso;
	}
	public int getStRecurso(){
		return this.stRecurso;
	}
	public void setLgAdvertencia(int lgAdvertencia){
		this.lgAdvertencia=lgAdvertencia;
	}
	public int getLgAdvertencia(){
		return this.lgAdvertencia;
	}
	public void setNrControle(int nrControle){
		this.nrControle=nrControle;
	}
	public int getNrControle(){
		return this.nrControle;
	}
	public void setNrRenainf(Long nrRenainf){
		this.nrRenainf=nrRenainf;
	}
	public Long getNrRenainf(){
		return this.nrRenainf;
	}
	public void setNrSequencial(int nrSequencial){
		this.nrSequencial=nrSequencial;
	}
	public int getNrSequencial(){
		return this.nrSequencial;
	}
	public void setDtArNai(GregorianCalendar dtArNai){
		this.dtArNai=dtArNai;
	}
	public GregorianCalendar getDtArNai(){
		return this.dtArNai;
	}
	public void setNrNotificacaoNai(int nrNotificacaoNai){
		this.nrNotificacaoNai=nrNotificacaoNai;
	}
	public int getNrNotificacaoNai(){
		return this.nrNotificacaoNai;
	}
	public void setDsBairroCondutor(String dsBairroCondutor){
		this.dsBairroCondutor=dsBairroCondutor;
	}
	public String getDsBairroCondutor(){
		return this.dsBairroCondutor;
	}
	public void setNrImovelCondutor(String nrImovelCondutor){
		this.nrImovelCondutor=nrImovelCondutor;
	}
	public String getNrImovelCondutor(){
		return this.nrImovelCondutor;
	}
	public void setDsComplementoCondutor(String dsComplementoCondutor){
		this.dsComplementoCondutor=dsComplementoCondutor;
	}
	public String getDsComplementoCondutor(){
		return this.dsComplementoCondutor;
	}
	public void setCdMunicipioCondutor(int cdMunicipioCondutor){
		this.cdMunicipioCondutor=cdMunicipioCondutor;
	}
	public int getCdMunicipioCondutor(){
		return this.cdMunicipioCondutor;
	}
	public void setNrCepCondutor(String nrCepCondutor){
		this.nrCepCondutor=nrCepCondutor;
	}
	public String getNrCepCondutor(){
		return this.nrCepCondutor;
	}
	public void setNrNotificacaoNip(int nrNotificacaoNip){
		this.nrNotificacaoNip=nrNotificacaoNip;
	}
	public int getNrNotificacaoNip(){
		return this.nrNotificacaoNip;
	}
	public void setTpCancelamento(int tpCancelamento){
		this.tpCancelamento=tpCancelamento;
	}
	public int getTpCancelamento(){
		return this.tpCancelamento;
	}
	public void setLgCancelaMovimento(int lgCancelaMovimento){
		this.lgCancelaMovimento=lgCancelaMovimento;
	}
	public int getLgCancelaMovimento(){
		return this.lgCancelaMovimento;
	}
	public void setDtCancelamentoMovimento(GregorianCalendar dtCancelamentoMovimento){
		this.dtCancelamentoMovimento=dtCancelamentoMovimento;
	}
	public GregorianCalendar getDtCancelamentoMovimento(){
		return this.dtCancelamentoMovimento;
	}
	public void setNrRemessaRegistro(int nrRemessaRegistro){
		this.nrRemessaRegistro=nrRemessaRegistro;
	}
	public int getNrRemessaRegistro(){
		return this.nrRemessaRegistro;
	}
	public void setDtPrimeiroRegistro(GregorianCalendar dtPrimeiroRegistro){
		this.dtPrimeiroRegistro=dtPrimeiroRegistro;
	}
	public GregorianCalendar getDtPrimeiroRegistro(){
		return this.dtPrimeiroRegistro;
	}
	public void setDtEmissaoNip(GregorianCalendar dtEmissaoNip){
		this.dtEmissaoNip=dtEmissaoNip;
	}
	public GregorianCalendar getDtEmissaoNip(){
		return this.dtEmissaoNip;
	}
	public void setDtResultadoDefesa(GregorianCalendar dtResultadoDefesa){
		this.dtResultadoDefesa=dtResultadoDefesa;
	}
	public GregorianCalendar getDtResultadoDefesa(){
		return this.dtResultadoDefesa;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao){
		this.dtAtualizacao=dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao(){
		return this.dtAtualizacao;
	}
	public void setDtResultadoJari(GregorianCalendar dtResultadoJari){
		this.dtResultadoJari=dtResultadoJari;
	}
	public GregorianCalendar getDtResultadoJari(){
		return this.dtResultadoJari;
	}
	public void setDtResultadoCetran(GregorianCalendar dtResultadoCetran){
		this.dtResultadoCetran=dtResultadoCetran;
	}
	public GregorianCalendar getDtResultadoCetran(){
		return this.dtResultadoCetran;
	}
	public void setBlbFoto(byte[] blbFoto){
		this.blbFoto=blbFoto;
	}
	public byte[] getBlbFoto(){
		return this.blbFoto;
	}
	public void setLgAdvertenciaJari(int lgAdvertenciaJari){
		this.lgAdvertenciaJari=lgAdvertenciaJari;
	}
	public int getLgAdvertenciaJari(){
		return this.lgAdvertenciaJari;
	}
	public void setLgAdvertenciaCetran(int lgAdvertenciaCetran){
		this.lgAdvertenciaCetran=lgAdvertenciaCetran;
	}
	public int getLgAdvertenciaCetran(){
		return this.lgAdvertenciaCetran;
	}
	public void setLgNotrigger(int lgNotrigger){
		this.lgNotrigger=lgNotrigger;
	}
	public int getLgNotrigger(){
		return this.lgNotrigger;
	}
	public void setStDetran(String stDetran){
		this.stDetran=stDetran;
	}
	public String getStDetran(){
		return this.stDetran;
	}
	public void setLgErro(int lgErro){
		this.lgErro=lgErro;
	}
	public int getLgErro(){
		return this.lgErro;
	}
	public void setNrCpfCondutor(String nrCpfCondutor){
		this.nrCpfCondutor=nrCpfCondutor;
	}
	public String getNrCpfCondutor(){
		return this.nrCpfCondutor;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdEndereco(int cdEndereco){
		this.cdEndereco=cdEndereco;
	}
	public int getCdEndereco(){
		return this.cdEndereco;
	}
	public void setCdProprietario(int cdProprietario){
		this.cdProprietario=cdProprietario;
	}
	public int getCdProprietario(){
		return this.cdProprietario;
	}
	public void setCdCondutor(int cdCondutor){
		this.cdCondutor=cdCondutor;
	}
	public int getCdCondutor(){
		return this.cdCondutor;
	}
	public void setCdEnderecoCondutor(int cdEnderecoCondutor){
		this.cdEnderecoCondutor=cdEnderecoCondutor;
	}
	public int getCdEnderecoCondutor(){
		return this.cdEnderecoCondutor;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdMovimentoAtual(int cdMovimentoAtual){
		this.cdMovimentoAtual=cdMovimentoAtual;
	}
	public int getCdMovimentoAtual(){
		return this.cdMovimentoAtual;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setVlLatitude(Double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public Double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(Double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public Double getVlLongitude(){
		return this.vlLongitude;
	}
	public void setCodMarcaAutuacao(int codMarcaAutuacao){
		this.codMarcaAutuacao=codMarcaAutuacao;
	}
	public int getCodMarcaAutuacao(){
		return this.codMarcaAutuacao;
	}
	public void setNmCondutorAutuacao(String nmCondutorAutuacao){
		this.nmCondutorAutuacao=nmCondutorAutuacao;
	}
	public String getNmCondutorAutuacao(){
		return this.nmCondutorAutuacao;
	}
	public void setNmProprietarioAutuacao(String nmProprietarioAutuacao){
		this.nmProprietarioAutuacao=nmProprietarioAutuacao;
	}
	public String getNmProprietarioAutuacao(){
		return this.nmProprietarioAutuacao;
	}
	public void setNrCnhAutuacao(String nrCnhAutuacao){
		this.nrCnhAutuacao=nrCnhAutuacao;
	}
	public String getNrCnhAutuacao(){
		return this.nrCnhAutuacao;
	}
	public void setUfCnhAutuacao(String ufCnhAutuacao){
		this.ufCnhAutuacao=ufCnhAutuacao;
	}
	public String getUfCnhAutuacao(){
		return this.ufCnhAutuacao;
	}
	public void setNrDocumentoAutuacao(String nrDocumentoAutuacao){
		this.nrDocumentoAutuacao=nrDocumentoAutuacao;
	}
	public String getNrDocumentoAutuacao(){
		return this.nrDocumentoAutuacao;
	}
	public void setCdAitOrigem(int cdAitOrigem){
		this.cdAitOrigem=cdAitOrigem;
	}
	public int getCdAitOrigem(){
		return this.cdAitOrigem;
	}
	public void setVlMulta(Double vlMulta){
		this.vlMulta=vlMulta;
	}
	public Double getVlMulta(){
		return this.vlMulta;
	}
	public void setNrFatorNic(int nrFatorNic){
		this.nrFatorNic=nrFatorNic;
	}
	public int getNrFatorNic(){
		return this.nrFatorNic;
	}
	public void setLgDetranFebraban(int lgDetranFebraban){
		this.lgDetranFebraban=lgDetranFebraban;
	}
	public int getLgDetranFebraban(){
		return this.lgDetranFebraban;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setNrPlacaAntiga(String nrPlacaAntiga){
		this.nrPlacaAntiga=nrPlacaAntiga;
	}
	public String getNrPlacaAntiga(){
		return this.nrPlacaAntiga;
	}
	public void setStAit(int stAit){
		this.stAit=stAit;
	}
	public int getStAit(){
		return this.stAit;
	}
	public void setDtSincronizacao(GregorianCalendar dtSincronizacao){
		this.dtSincronizacao=dtSincronizacao;
	}
	public GregorianCalendar getDtSincronizacao(){
		return this.dtSincronizacao;
	}
	public void setCdEventoEquipamento(int cdEventoEquipamento){
		this.cdEventoEquipamento=cdEventoEquipamento;
	}
	public int getCdEventoEquipamento(){
		return this.cdEventoEquipamento;
	}
	public void setTpConvenio(int tpConvenio){
		this.tpConvenio=tpConvenio;
	}
	public int getTpConvenio(){
		return this.tpConvenio;
	}
	public void setDtAfericao(GregorianCalendar dtAfericao){
		this.dtAfericao=dtAfericao;
	}
	public GregorianCalendar getDtAfericao(){
		return this.dtAfericao;
	}
	public void setNrLacre(String nrLacre){
		this.nrLacre=nrLacre;
	}
	public String getNrLacre(){
		return this.nrLacre;
	}
	public void setNrInventarioInmetro(String nrInventarioInmetro){
		this.nrInventarioInmetro=nrInventarioInmetro;
	}
	public String getNrInventarioInmetro(){
		return this.nrInventarioInmetro;
	}
	public void setDtAdesaoSne(GregorianCalendar dtAdesaoSne){
		this.dtAdesaoSne=dtAdesaoSne;
	}
	public GregorianCalendar getDtAdesaoSne(){
		return this.dtAdesaoSne;
	}
	public void setStOptanteSne(int stOptanteSne){
		this.stOptanteSne=stOptanteSne;
	}
	public int getStOptanteSne(){
		return this.stOptanteSne;
	}
	public void setLgPenalidadeAdvertenciaNip(int lgPenalidadeAdvertenciaNip){
		this.lgPenalidadeAdvertenciaNip=lgPenalidadeAdvertenciaNip;
	}
	public int getLgPenalidadeAdvertenciaNip(){
		return this.lgPenalidadeAdvertenciaNip;
	}
	public void setDtPrazoDefesa(GregorianCalendar dtPrazoDefesa){
		this.dtPrazoDefesa=dtPrazoDefesa;
	}
	public GregorianCalendar getDtPrazoDefesa(){
		return this.dtPrazoDefesa;
	}
	public void setCodEspecieAutuacao(int codEspecieAutuacao){
		this.codEspecieAutuacao=codEspecieAutuacao;
	}
	public int getCodEspecieAutuacao(){
		return this.codEspecieAutuacao;
	}
	public void setDtNotificacaoDevolucao(GregorianCalendar dtNotificacaoDevolucao){
		this.dtNotificacaoDevolucao=dtNotificacaoDevolucao;
	}
	public GregorianCalendar getDtNotificacaoDevolucao(){
		return this.dtNotificacaoDevolucao;
	}
	public int getCodTalao() {
		return codTalao;
	}
	public void setCodTalao(int codTalao) {
		this.codTalao = codTalao;
	}
	public String getTxtCancelamento() {
		return txtCancelamento;
	}
	public void setTxtCancelamento(String txtCancelamento) {
		this.txtCancelamento = txtCancelamento;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}

	public Object clone() {
		return new AitOld (getCodBanco(),
			getCodigoAit(),
			getCodBairro(),
			getCodInfracao(),
			getCodUsuario(),
			getCodEspecie(),
			getCodMarca(),
			getCodAgente(),
			getCodCor(),
			getCodTipo(),
			getDtInfracao()==null ? null : (GregorianCalendar)getDtInfracao().clone(),
			getCodCategoria(),
			getCodMunicipio(),
			getDsObservacao(),
			getDsLocalInfracao(),
			getUfVeiculo(),
			getCdRenavan(),
			getDsAnoFabricacao(),
			getDsAnoModelo(),
			getNmProprietario(),
			getTpDocumento(),
			getNrDocumento(),
			getDsLogradouro(),
			getDsNrImovel(),
			getNrCep(),
			getNrDdd(),
			getNrTelefone(),
			getNrRemessa(),
			getNrAit(),
			getNrCodigoBarras(),
			getNmCondutor(),
			getNrCnhCondutor(),
			getUfCnhCondutor(),
			getDsEnderecoCondutor(),
			getNrCpfProprietario(),
			getVlVelocidadePermitida(),
			getVlVelocidadeAferida(),
			getVlVelocidadePenalidade(),
			getNrPlaca(),
			getCdUsuarioExclusao(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone(),
			getDsPontoReferencia(),
			getLgAutoAssinado(),
			getDtDigitacao()==null ? null : (GregorianCalendar)getDtDigitacao().clone(),
			getTpNaturezaAutuacao(),
			getTpCnhCondutor(),
			getTpPessoaProprietario(),
			getNrCpfCnpjProprietario(),
			getNmComplemento(),
			getDtMovimento()==null ? null : (GregorianCalendar)getDtMovimento().clone(),
			getTpStatus(),
			getTpArquivo(),
			getCodOcorrencia(),
			getDsParecer(),
			getNrErro(),
			getLgEnviadoDetran(),
			getStEntrega(),
			getNrProcesso(),
			getDtRegistroDetran()==null ? null : (GregorianCalendar)getDtRegistroDetran().clone(),
			getStRecurso(),
			getLgAdvertencia(),
			getNrControle(),
			getNrRenainf(),
			getNrSequencial(),
			getDtArNai()==null ? null : (GregorianCalendar)getDtArNai().clone(),
			getNrNotificacaoNai(),
			getDsBairroCondutor(),
			getNrImovelCondutor(),
			getDsComplementoCondutor(),
			getCdMunicipioCondutor(),
			getNrCepCondutor(),
			getNrNotificacaoNip(),
			getTpCancelamento(),
			getLgCancelaMovimento(),
			getDtCancelamentoMovimento()==null ? null : (GregorianCalendar)getDtCancelamentoMovimento().clone(),
			getNrRemessaRegistro(),
			getDtPrimeiroRegistro()==null ? null : (GregorianCalendar)getDtPrimeiroRegistro().clone(),
			getDtEmissaoNip()==null ? null : (GregorianCalendar)getDtEmissaoNip().clone(),
			getDtResultadoDefesa()==null ? null : (GregorianCalendar)getDtResultadoDefesa().clone(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getDtResultadoJari()==null ? null : (GregorianCalendar)getDtResultadoJari().clone(),
			getDtResultadoCetran()==null ? null : (GregorianCalendar)getDtResultadoCetran().clone(),
			getBlbFoto(),
			getLgAdvertenciaJari(),
			getLgAdvertenciaCetran(),
			getLgNotrigger(),
			getStDetran(),
			getLgErro(),
			getNrCpfCondutor(),
			getCdVeiculo(),
			getCdEndereco(),
			getCdProprietario(),
			getCdCondutor(),
			getCdEnderecoCondutor(),
			getTxtObservacao(),
			getCdMovimentoAtual(),
			getCdEquipamento(),
			getVlLatitude(),
			getVlLongitude(),
			getCodMarcaAutuacao(),
			getNmCondutorAutuacao(),
			getNmProprietarioAutuacao(),
			getNrCnhAutuacao(),
			getUfCnhAutuacao(),
			getNrDocumentoAutuacao(),
			getCdAitOrigem(),
			getVlMulta(),
			getNrFatorNic(),
			getLgDetranFebraban(),
			getTpOrigem(),
			getNrPlacaAntiga(),
			getStAit(),
			getDtSincronizacao()==null ? null : (GregorianCalendar)getDtSincronizacao().clone(),
			getCdEventoEquipamento(),
			getTpConvenio(),
			getDtAfericao()==null ? null : (GregorianCalendar)getDtAfericao().clone(),
			getNrLacre(),
			getNrInventarioInmetro(),
			getDtAdesaoSne()==null ? null : (GregorianCalendar)getDtAdesaoSne().clone(),
			getStOptanteSne(),
			getLgPenalidadeAdvertenciaNip(),
			getDtPrazoDefesa()==null ? null : (GregorianCalendar)getDtPrazoDefesa().clone(),
			getCodEspecieAutuacao(),
			getDtNotificacaoDevolucao()==null ? null : (GregorianCalendar)getDtNotificacaoDevolucao().clone(),
			getCodTalao(),
			getTxtCancelamento());
	}

}