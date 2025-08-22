package com.tivic.manager.mob.ait.validacao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitPendenteDTO {

	int cdAit;
	String idAit;
	int cdUsuario;
	String nrPlaca;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	GregorianCalendar dtInfracao;
	int cdEquipamento;
	int tpEquipamento;
	int tpCompetencia;
	String nrCodDetran;
	String dsInfracao;
	Double vlMulta;
	int stAtual;
	String dsOcorrencia;
	String nrMatricula;
	String nmAgente;
	String dsObservacao;

	public int getCdAit() {
		return cdAit;
	}
	
	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}
	
	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
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
	
	public int getCdEquipamento() {
		return cdEquipamento;
	}
	
	public void setCdEquipamento(int cdEquipamento) {
		this.cdEquipamento = cdEquipamento;
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

	public String getNrCodDetran() {
		return nrCodDetran;
	}
	
	public void setNrCodDetran(String nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}
	
	public String getDsInfracao() {
		return dsInfracao;
	}
	
	public void setDsInfracao(String dsInfracao) {
		this.dsInfracao = dsInfracao;
	}

	public Double getVlMulta() {
		return vlMulta;
	}

	public void setVlMulta(Double vlMulta) {
		this.vlMulta = vlMulta;
	}

	public int getStAtual() {
		return stAtual;
	}

	public void setStAtual(int stAtual) {
		this.stAtual = stAtual;
	}

	public String getDsOcorrencia() {
		return dsOcorrencia;
	}

	public void setDsOcorrencia(String dsOcorrencia) {
		this.dsOcorrencia = dsOcorrencia;
	}

	public String getNrMatricula() {
		return nrMatricula;
	}
	
	public void setNrMatricula(String nrMatricula) {
		this.nrMatricula = nrMatricula;
	}
	
	public String getNmAgente() {
		return nmAgente;
	}

	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}
	
	public String getDsObservacao() {
		return dsObservacao;
	}
	
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
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
