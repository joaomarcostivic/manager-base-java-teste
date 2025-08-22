package com.tivic.manager.mob.ait.relatorios;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class RelatorioAitDTO {

	int cdAit;
	int tpStatus;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtMovimento;
	String idAit;
	int cdUsuario;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtDigitacao;
	String nrPlaca;
	String nrCpfCnpjProprietario;
	String nrCpfCondutor;
	int tpTalao;
	@JsonSerialize(converter = CalendarSerializer.class)
	GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	GregorianCalendar dtVencimento;
	int tpEquipamento;
	int tpCompetencia;
	String nmAgente;
	String nrMatricula;
	int nrCodDetran;
	Double vlMulta;
	String nmProprietario;
	int cdAgente;
	Double totalMultas;
	int lgAutoAssinado;
	int lgEnviadoDetran;
	int ctMovimento;
	int stAtual;
	String dsLocalInfracao;
	String dsPontoReferencia;
	String dsLogradouro;
	String nmCondutor;
	String nrCnhCondutor;
	String dsEnderecoCondutor;
	String ufCnhCondutor;
	String dsTpStatus;
	String nmPessoa;
	int cdPessoa;
	String nmLogin;
	String sgUfVeiculo;
	int cdOcorrencia;
	
	public String getNrMatricula() {
		return nrMatricula;
	}

	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}

	public RelatorioAitDTO() {
	}
	
	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	public int getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(int tpStatus) {
		this.tpStatus = tpStatus;
	}

	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public GregorianCalendar getDtDigitacao() {
		return dtDigitacao;
	}

	public void setDtDigitacao(GregorianCalendar dtDigitacao) {
		this.dtDigitacao = dtDigitacao;
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

	public String getNrCpfCondutor() {
		return nrCpfCondutor;
	}

	public void setNrCpfCondutor(String nrCpfCondutor) {
		this.nrCpfCondutor = nrCpfCondutor;
	}

	public int getTpTalao() {
		return tpTalao;
	}

	public void setTpTalao(int tpTalao) {
		this.tpTalao = tpTalao;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public int getTpEquipamento() {
		return tpEquipamento;
	}

	public void setTpEquipamento(int tpEquipamento) {
		this.tpEquipamento = tpEquipamento;
	}
	public int getTpCompetencia() {
		return tpCompetencia;
	}

	public void setTpCompetencia(int tpCompetencia) {
		this.tpCompetencia = tpCompetencia;
	}

	public String getNmAgente() {
		return nmAgente;
	}

	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}

	public int getNrCodDetran() {
		return nrCodDetran;
	}

	public void setNrCodDetran(int nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}

	public Double getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(Double vlMulta) {
		this.vlMulta = vlMulta;
	}

	public String getNmProprietario() {
		return nmProprietario;
	}

	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	
	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	
	public int getCdAgente() {
		return cdAgente;
	}

	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}
	
	public Double getTotalMultas() {
		return totalMultas;
	}

	public void setTotalMultas(Double totalMultas) {
		this.totalMultas = totalMultas;
	}
	
	public int getLgAutoAssinado() {
		return lgAutoAssinado;
	}

	public void setLgAutoAssinado(int lgAutoAssinado) {
		this.lgAutoAssinado = lgAutoAssinado;
	}
	
	public int getCtMovimento() {
		return ctMovimento;
	}

	public void setCtMovimento(int ctMovimento) {
		this.ctMovimento = ctMovimento;
	}

	public int getStAtual() {
		return stAtual;
	}

	public void setStAtual(int stAtual) {
		this.stAtual = stAtual;
	}
	
	public String getDsLocalInfracao() {
		return dsLocalInfracao;
	}

	public void setDsLocalInfracao(String dsLocalInfracao) {
		this.dsLocalInfracao = dsLocalInfracao;
	}

	public String getDsPontoReferencia() {
		return dsPontoReferencia;
	}

	public void setDsPontoReferencia(String dsPontoReferencia) {
		this.dsPontoReferencia = dsPontoReferencia;
	}

	public String getDsLogradouro() {
		return dsLogradouro;
	}

	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}

	public String getNmCondutor() {
		return nmCondutor;
	}

	public void setNmCondutor(String nmCondutor) {
		this.nmCondutor = nmCondutor;
	}

	public String getNrCnhCondutor() {
		return nrCnhCondutor;
	}

	public void setNrCnhCondutor(String nrCnhCondutor) {
		this.nrCnhCondutor = nrCnhCondutor;
	}

	public String getDsEnderecoCondutor() {
		return dsEnderecoCondutor;
	}

	public void setDsEnderecoCondutor(String dsEnderecoCondutor) {
		this.dsEnderecoCondutor = dsEnderecoCondutor;
	}
	
	public String getUfCnhCondutor() {
		return ufCnhCondutor;
	}

	public void setUfCnhCondutor(String ufCnhCondutor) {
		this.ufCnhCondutor = ufCnhCondutor;
	}
	
	public String getDsTpStatus() {
		return dsTpStatus;
	}

	public void setDsTpStatus(String dsTpStatus) {
		this.dsTpStatus = dsTpStatus;
	}
	
	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	
	public int getCdPessoa() {
		return cdPessoa;
	}

	public void setCdPessoa(int cdPessoa) {
		this.cdPessoa = cdPessoa;
	}

	public String getNmLogin() {
		return nmLogin;
	}

	public void setNmLogin(String nmLogin) {
		this.nmLogin = nmLogin;
	}	
	
	public String getSgUfVeiculo() {
		return sgUfVeiculo;
	}

	public void setSgUfVeiculo(String sgUfVeiculo) {
		this.sgUfVeiculo = sgUfVeiculo;
	}

	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public int getLgEnviadoDetran() {
		return lgEnviadoDetran;
	}

	public void setLgEnviadoDetran(int lgEnviadoDetran) {
		this.lgEnviadoDetran = lgEnviadoDetran;
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

}
