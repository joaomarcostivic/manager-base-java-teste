package com.tivic.manager.mob.lotes.dto.dividaativa;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class DividaAtivaDTO {

	private int cdAit;
	private int cdMovimento;
	private String idAit;
	private String nrPlaca;
	private String nrCpfCnpjProprietario;
	private double vlMulta;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtVencimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtValidade;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtEnvio;
	private String nmPessoa;
	private String dsLogradouro;
	private int nrLogradouro;
	private String nmBairro;
	private int nrCep;
	private String nmCidade;
	private String nmEstado;
	private String sgEstado;
	private String tpLogradouro;
	private String stAit;
	private int stLote;
	private String idLote;
	private int cdArquivoRetorno;

	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getCdMovimento() {
		return cdMovimento;
	}

	public void setCdMovimento(int cdMovimento) {
		this.cdMovimento = cdMovimento;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}

	public String getNrCpfCnpjProprietario() {
		return nrCpfCnpjProprietario;
	}

	public void setNrCpfCnpjProprietario(String nrCpfCnpjProprietario) {
		this.nrCpfCnpjProprietario = nrCpfCnpjProprietario;
	}

	public double getVlMulta() {
		return vlMulta;
	}

	public void setVlMulta(double vlMulta) {
		this.vlMulta = vlMulta;
	}

	public GregorianCalendar getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(GregorianCalendar dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

	public String getDsLogradouro() {
		return dsLogradouro;
	}

	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}

	public int getNrLogradouro() {
		return nrLogradouro;
	}

	public void setNrLogradouro(int nrLogradouro) {
		this.nrLogradouro = nrLogradouro;
	}

	public String getNmBairro() {
		return nmBairro;
	}

	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}

	public int getNrCep() {
		return nrCep;
	}

	public void setNrCep(int nrCep) {
		this.nrCep = nrCep;
	}

	public String getNmCidade() {
		return nmCidade;
	}

	public void setNmCidade(String nmCidade) {
		this.nmCidade = nmCidade;
	}

	public String getNmEstado() {
		return nmEstado;
	}

	public void setNmEstado(String nmEstado) {
		this.nmEstado = nmEstado;
	}

	public String getSgEstado() {
		return sgEstado;
	}

	public void setSgEstado(String sgEstado) {
		this.sgEstado = sgEstado;
	}

	public String getTpLogradouro() {
		return tpLogradouro;
	}

	public void setTpLogradouro(String tpLogradouro) {
		this.tpLogradouro = tpLogradouro;
	}

	public GregorianCalendar getDtValidade() {
		return dtValidade;
	}

	public void setDtValidade(GregorianCalendar dtValidade) {
		this.dtValidade = dtValidade;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public String getStAit() {
		return stAit;
	}

	public void setStAit(String stAit) {
		this.stAit = stAit;
	}

	public GregorianCalendar getDtEnvio() {
		return dtEnvio;
	}

	public void setDtEnvio(GregorianCalendar dtEnvio) {
		this.dtEnvio = dtEnvio;
	}

	public String getIdLote() {
		return idLote;
	}

	public void setIdLote(String idLote) {
		this.idLote = idLote;
	}

	public int getCdArquivoRetorno() {
		return cdArquivoRetorno;
	}

	public void setCdArquivoRetorno(int cdArquivoRetorno) {
		this.cdArquivoRetorno = cdArquivoRetorno;
	}

	public int getStLote() {
		return stLote;
	}

	public void setStLote(int stLote) {
		this.stLote = stLote;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possivel serializar o objeto";
		}
	}
}
