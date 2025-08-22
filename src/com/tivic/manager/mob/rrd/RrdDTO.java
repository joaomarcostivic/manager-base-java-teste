package com.tivic.manager.mob.rrd;

import java.util.GregorianCalendar;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.RecolhimentoDocumentacao;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class RrdDTO {
	private int cdRrd;
	private int nrRrd;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtOcorrencia;
	private int cdUsuario;
	private int cdAgente;
	private String dsObservacao;
	private String dsLocalOcorrencia;
	private String dsPontoReferencia;
	private int cdCidade;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtRegularizar;
	private int cdRrdOrgao;
	private int cdAit;
	private int codigoAit;
	private int codBairro;
	private int codInfracao;
	private int codUsuario;
	private int codEspecie;
	private int codMarca;
	private int codAgente;
	private int codTipo;
	private int codCategoria;
	private int codMunicipio;
	private int nrAit;
	private String nrPlaca;
	private String ufVeiculo;
	private int cdRenavan;
	private String nmCidade;
	private String nmCondutor;
	private String dsEnderecoCondutor;
	private String nrTelefone;
	private String cpfCondutor;
	private String nmProprietario;
	private String nrCpfCnpjProprietario;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private String bairroInfracao;
	private String ruaInfracao;
	private String nrArtigo;
	private String nrParagrafo;
	private String nrInciso;
	private String nrAlinea;
	private String dsInfracao2;
	private String dsInfracao;
	private String nmAgente;
	private String nrMatricula;
	private String dsEspecie;
	private String nmTipo;
	private String nmMarca;
	private String nmModelo;
	private String nmCategoria;
	private String nmTipoDocumentacao;
	private String nrDocumentoRecolhido;
	
	private List<RecolhimentoDocumentacao> documentacao;
	
	public int getCdRrd() {
		return cdRrd;
	}
	public void setCdRrd(int cdRrd) {
		this.cdRrd = cdRrd;
	}
	public int getNrRrd() {
		return nrRrd;
	}
	public void setNrRrd(int nrRrd) {
		this.nrRrd = nrRrd;
	}
	public GregorianCalendar getDtOcorrencia() {
		return dtOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}
	public int getCdUsuario() {
		return cdUsuario;
	}
	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	public int getCdAgente() {
		return cdAgente;
	}
	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}
	public String getDsObservacao() {
		return dsObservacao;
	}
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	public String getDsLocalOcorrencia() {
		return dsLocalOcorrencia;
	}
	public void setDsLocalOcorrencia(String dsLocalOcorrencia) {
		this.dsLocalOcorrencia = dsLocalOcorrencia;
	}
	public String getDsPontoReferencia() {
		return dsPontoReferencia;
	}
	public void setDsPontoReferencia(String dsPontoReferencia) {
		this.dsPontoReferencia = dsPontoReferencia;
	}
	public int getCdCidade() {
		return cdCidade;
	}
	public void setCdCidade(int cdCidade) {
		this.cdCidade = cdCidade;
	}
	public GregorianCalendar getDtRegularizar() {
		return dtRegularizar;
	}
	public void setDtRegularizar(GregorianCalendar dtRegularizar) {
		this.dtRegularizar = dtRegularizar;
	}
	public int getCdRrdOrgao() {
		return cdRrdOrgao;
	}
	public void setCdRrdOrgao(int cdRrdOrgao) {
		this.cdRrdOrgao = cdRrdOrgao;
	}
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public int getCodigoAit() {
		return codigoAit;
	}
	public void setCodigoAit(int codigoAit) {
		this.codigoAit = codigoAit;
	}
	public int getCodBairro() {
		return codBairro;
	}
	public void setCodBairro(int codBairro) {
		this.codBairro = codBairro;
	}
	public int getCodInfracao() {
		return codInfracao;
	}
	public void setCodInfracao(int codInfracao) {
		this.codInfracao = codInfracao;
	}
	public int getCodUsuario() {
		return codUsuario;
	}
	public void setCodUsuario(int codUsuario) {
		this.codUsuario = codUsuario;
	}
	public int getCodEspecie() {
		return codEspecie;
	}
	public void setCodEspecie(int codEspecie) {
		this.codEspecie = codEspecie;
	}
	public int getCodMarca() {
		return codMarca;
	}
	public void setCodMarca(int codMarca) {
		this.codMarca = codMarca;
	}
	public int getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(int codAgente) {
		this.codAgente = codAgente;
	}
	public int getCodTipo() {
		return codTipo;
	}
	public void setCodTipo(int codTipo) {
		this.codTipo = codTipo;
	}
	public int getCodCategoria() {
		return codCategoria;
	}
	public void setCodCategoria(int codCategoria) {
		this.codCategoria = codCategoria;
	}
	public int getCodMunicipio() {
		return codMunicipio;
	}
	public void setCodMunicipio(int codMunicipio) {
		this.codMunicipio = codMunicipio;
	}
	public int getNrAit() {
		return nrAit;
	}
	public void setNrAit(int nrAit) {
		this.nrAit = nrAit;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public String getUfVeiculo() {
		return ufVeiculo;
	}
	public void setUfVeiculo(String ufVeiculo) {
		this.ufVeiculo = ufVeiculo;
	}
	public int getCdRenavan() {
		return cdRenavan;
	}
	public void setCdRenavan(int cdRenavan) {
		this.cdRenavan = cdRenavan;
	}
	public String getNmCidade() {
		return nmCidade;
	}
	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}
	public String getNmCondutor() {
		return nmCondutor;
	}
	public void setNmCondutor(String nmCondutor) {
		this.nmCondutor = nmCondutor;
	}
	public String getDsEnderecoCondutor() {
		return dsEnderecoCondutor;
	}
	public void setDsEnderecoCondutor(String dsEnderecoCondutor) {
		this.dsEnderecoCondutor = dsEnderecoCondutor;
	}
	public String getNrTelefone() {
		return nrTelefone;
	}
	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}
	public String getCpfCondutor() {
		return cpfCondutor;
	}
	public void setCpfCondutor(String cpfCondutor) {
		this.cpfCondutor = cpfCondutor;
	}
	public String getNmProprietario() {
		return nmProprietario;
	}
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	public String getNrCpfCnpjProprietario() {
		return nrCpfCnpjProprietario;
	}
	public void setNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		this.nrCpfCnpjProprietario = nrCpfCnpjProprietario;
	}
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}
	public String getBairroInfracao() {
		return bairroInfracao;
	}
	public void setBairroInfracao(String bairroInfracao) {
		this.bairroInfracao = bairroInfracao;
	}
	public String getRuaInfracao() {
		return ruaInfracao;
	}
	public void setRuaInfracao(String ruaInfracao) {
		this.ruaInfracao = ruaInfracao;
	}
	public String getNrArtigo() {
		return nrArtigo;
	}
	public void setNrArtigo(String nrArtigo) {
		this.nrArtigo = nrArtigo;
	}
	public String getNrParagrafo() {
		return nrParagrafo;
	}
	public void setNrParagrafo(String nrParagrafo) {
		this.nrParagrafo = nrParagrafo;
	}
	public String getNrInciso() {
		return nrInciso;
	}
	public void setNrInciso(String nrInciso) {
		this.nrInciso = nrInciso;
	}
	public String getNrAlinea() {
		return nrAlinea;
	}
	public void setNrAlinea(String nrAlinea) {
		this.nrAlinea = nrAlinea;
	}
	public String getDsInfracao2() {
		return dsInfracao2;
	}
	public void setDsInfracao2(String dsInfracao2) {
		this.dsInfracao2 = dsInfracao2;
	}
	public String getDsInfracao() {
		return dsInfracao;
	}
	public void setDsInfracao(String dsInfracao) {
		this.dsInfracao = dsInfracao;
	}
	public String getNmAgente() {
		return nmAgente;
	}
	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}
	public String getNrMatricula() {
		return nrMatricula;
	}
	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}
	public String getDsEspecie() {
		return dsEspecie;
	}
	public void setDsEspecie(String dsEspecie) {
		this.dsEspecie = dsEspecie;
	}
	public String getNmTipo() {
		return nmTipo;
	}
	public void setNmTipo(String nmTipo) {
		this.nmTipo = nmTipo;
	}
	public String getNmMarca() {
		return nmMarca;
	}
	public void setNmMarca(String nmMarca) {
		this.nmMarca = nmMarca;
	}
	public String getNmModelo() {
		return nmModelo;
	}
	public void setNmModelo(String nmModelo) {
		this.nmModelo = nmModelo;
	}
	public String getNmCategoria() {
		return nmCategoria;
	}
	public void setNmCategoria(String nmCategoria) {
		this.nmCategoria = nmCategoria;
	}
	public List<RecolhimentoDocumentacao> getDocumentacao() {
		return documentacao;
	}
	public void setDocumentacao(List<RecolhimentoDocumentacao> documentacao) {
		this.documentacao = documentacao;
	}
	public String getNmTipoDocumentacao() {
		return nmTipoDocumentacao;
	}
	public void setNmTipoDocumentacao(String nmTipoDocumentacao) {
		this.nmTipoDocumentacao = nmTipoDocumentacao;
	}
	public String getNrDocumentoRecolhido() {
		return nrDocumentoRecolhido;
	}
	public void setNrDocumentoRecolhido(String nrDocumentoRecolhido) {
		this.nrDocumentoRecolhido = nrDocumentoRecolhido;
	}
	
}
