package com.tivic.manager.ptc.portal.dtos;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AndamentoAitDTO {

	private int cdOrgaoAutuador;
	private String nmMunicipioAutuador;
	private int cdMunicipioAutuador;
	private String nmUf;
	private int cdAit;
	private String nrPlaca;
	private String sgUfVeiculo;
	private String nmMunicipio;
	private int cdMunicipio;
	private String nrRenavam;
	private String nmModelo;
	private String dsEspecie;
	private String nmCategoria;
	private String nmProprietario;
	private String nrCpfProprietario;
	private String nmCondutor;
	private String nrCnhCondutor;
	private String ufCnh;
	private String ufCnhCondutor;
	private String dsLocalInfracao;
	private String dsPontoReferencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar hrInfracao;
	private int cdInfracao;
	private String dsInfracao;
	private double vlMulta;
	private int nrMovimento;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtSituacao; 
	private String nmSituacao;
	private boolean lgRegistrado;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtRegistro;
	private String nrProcesso;
	private String idAit;
	private String nrMatricula;
	private int nrCodDetran;
	private int tpConvenio;
	private String idEquipamento;
	
	public int getCdOrgaoAutuador() {
		return cdOrgaoAutuador;
	}
	public void setCdOrgaoAutuador(int cdOrgaoAutuador) {
		this.cdOrgaoAutuador = cdOrgaoAutuador;
	}
	public String getNmMunicipioAutuador() {
		return nmMunicipioAutuador;
	}
	public void setNmMunicipioAutuador(String nmMunicipioAutuador) {
		this.nmMunicipioAutuador = nmMunicipioAutuador;
	}
	public int getCdMunicipioAutuador() {
		return cdMunicipioAutuador;
	}
	public void setCdMunicipioAutuador(int cdMunicipioAutuador) {
		this.cdMunicipioAutuador = cdMunicipioAutuador;
	}
	public String getNmUf() {
		return nmUf;
	}
	public void setNmUf(String nmUf) {
		this.nmUf = nmUf;
	}
	public int getCdAit() {
		return cdAit;
	}
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	public String getNrPlaca() {
		return nrPlaca;
	}
	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	public String getSgUfVeiculo() {
		return sgUfVeiculo;
	}
	public void setSgUfVeiculo(String ufVeiculo) {
		this.sgUfVeiculo = ufVeiculo;
	}
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	public int getCdMunicipio() {
		return cdMunicipio;
	}
	public void setCdMunicipio(int cdMunicipio) {
		this.cdMunicipio = cdMunicipio;
	}
	public String getNrRenavam() {
		return nrRenavam;
	}
	public void setNrRenavam(String nrRenavam) {
		this.nrRenavam = nrRenavam;
	}
	public String getNmModelo() {
		return nmModelo;
	}
	public void setNmModelo(String nmModelo) {
		this.nmModelo = nmModelo;
	}
	public String getDsEspecie() {
		return dsEspecie;
	}
	public void setDsEspecie(String dsEspecie) {
		this.dsEspecie = dsEspecie;
	}
	public String getNmCategoria() {
		return nmCategoria;
	}
	public void setNmCategoria(String nmCategoria) {
		this.nmCategoria = nmCategoria;
	}
	public String getNmProprietario() {
		return nmProprietario;
	}
	public void setNmProprietario(String nmProprietario) {
		this.nmProprietario = nmProprietario;
	}
	public String getNrCpfProprietario() {
		return nrCpfProprietario;
	}
	public void setNrCpfProprietario(String nrCpfProprietario) {
		this.nrCpfProprietario = nrCpfProprietario;
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
	public String getUfCnh() {
		return ufCnh;
	}
	public void setUfCnh(String ufCnh) {
		this.ufCnh = ufCnh;
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
	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}
	public GregorianCalendar getHrInfracao() {
		return hrInfracao;
	}
	public void setHrInfracao(GregorianCalendar hrInfracao) {
		this.hrInfracao = hrInfracao;
	}
	public int getCdInfracao() {
		return cdInfracao;
	}
	public void setCdInfracao(int cdInfracao) {
		this.cdInfracao = cdInfracao;
	}
	public String getDsInfracao() {
		return dsInfracao;
	}
	public void setDsInfracao(String dsInfracao) {
		this.dsInfracao = dsInfracao;
	}
	public double getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(double vlMulta) {
		this.vlMulta = vlMulta;
	}
	public int getNrMovimento() {
		return nrMovimento;
	}
	public void setNrMovimento(int nrMovimento) {
		this.nrMovimento = nrMovimento;
	}
	public GregorianCalendar getDtSituacao() {
		return dtSituacao;
	}
	public void setDtSituacao(GregorianCalendar dtSituacao) {
		this.dtSituacao = dtSituacao;
	}
	public String getNmSituacao() {
		return nmSituacao;
	}
	public void setNmSituacao(String nmSituacao) {
		this.nmSituacao = nmSituacao;
	}
	public boolean isLgRegistrado() {
		return lgRegistrado;
	}
	public void setLgRegistrado(boolean lgRegistrado) {
		this.lgRegistrado = lgRegistrado;
	}
	public GregorianCalendar getDtRegistro() {
		return dtRegistro;
	}
	public void setDtRegistro(GregorianCalendar dtRegistro) {
		this.dtRegistro = dtRegistro;
	}
	public String getNrProcesso() {
		return nrProcesso;
	}
	public void setNrProcesso(String nrProcesso) {
		this.nrProcesso = nrProcesso;
	}
	public String getUfCnhCondutor() {
		return ufCnhCondutor;
	}
	public void setUfCnhCondutor(String ufCnhCondutor) {
		this.ufCnhCondutor = ufCnhCondutor;
	}
	public String getIdAit() {
		return idAit;
	}
	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}
	public String getNrMatricula() {
		return nrMatricula;
	}
	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}
	public int getNrCodDetran() {
		return nrCodDetran;
	}
	public void setNrCodDetran(int nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}
	public int getTpConvenio() {
		return tpConvenio;
	}
	public void setTpConvenio(int tpConvenio) {
		this.tpConvenio = tpConvenio;
	}
	public String getIdEquipamento() {
		return idEquipamento;
	}
	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
	}
	
	@Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
}
