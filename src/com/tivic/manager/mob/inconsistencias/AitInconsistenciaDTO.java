package com.tivic.manager.mob.inconsistencias;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AitInconsistenciaDTO extends AitInconsistencia {

	private int cdAit;
	private String idAit;
	private int cdAitInconsistencia;
	private int cdMovimentoAtual;
	private int cdInconsistencia;
	private int tpStatusAtual;
	private int tpStatusPretendido;
	private int tpInconsistencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInclusaoInconsistencia;
	private int stInconsistencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtResolucaoInconsistencia;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private String nmInconsistencia;
	private String dsObservacao;
	private int totalInconsistencias;
	private int cdUsuario;
	private int cdOcorrencia;
	
	public AitInconsistenciaDTO() {
	}
	
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

	public int getCdAitInconsistencia() {
		return cdAitInconsistencia;
	}

	public void setCdAitInconsistencia(int cdAitInconsistencia) {
		this.cdAitInconsistencia = cdAitInconsistencia;
	}

	public int getCdMovimentoAtual() {
		return cdMovimentoAtual;
	}

	public void setCdMovimentoAtual(int cdMovimentoAtual) {
		this.cdMovimentoAtual = cdMovimentoAtual;
	}

	public int getCdInconsistencia() {
		return cdInconsistencia;
	}

	public void setCdInconsistencia(int cdInconsistencia) {
		this.cdInconsistencia = cdInconsistencia;
	}

	public int getTpStatusAtual() {
		return tpStatusAtual;
	}

	public void setTpStatusAtual(int tpStatusAtual) {
		this.tpStatusAtual = tpStatusAtual;
	}

	public int getTpStatusPretendido() {
		return tpStatusPretendido;
	}

	public void setTpStatusPretendido(int tpStatusPretendido) {
		this.tpStatusPretendido = tpStatusPretendido;
	}

	public int getTpInconsistencia() {
		return tpInconsistencia;
	}

	public void setTpInconsistencia(int tpInconsistencia) {
		this.tpInconsistencia = tpInconsistencia;
	}

	public GregorianCalendar getDtInclusaoInconsistencia() {
		return dtInclusaoInconsistencia;
	}

	public void setDtInclusaoInconsistencia(GregorianCalendar dtInclusaoInconsistencia) {
		this.dtInclusaoInconsistencia = dtInclusaoInconsistencia;
	}

	public int getStInconsistencia() {
		return stInconsistencia;
	}

	public void setStInconsistencia(int stInconsistencia) {
		this.stInconsistencia = stInconsistencia;
	}

	public GregorianCalendar getDtResolucaoInconsistencia() {
		return dtResolucaoInconsistencia;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}
	
	public void setDtResolucaoInconsistencia(GregorianCalendar dtResolucaoInconsistencia) {
		this.dtResolucaoInconsistencia = dtResolucaoInconsistencia;
	}
	
	public String getNmInconsistencia() {
		return nmInconsistencia;
	}

	public void setNmInconsistencia(String nmInconsistencia) {
		this.nmInconsistencia = nmInconsistencia;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public int getTotalInconsistencias() {
		return totalInconsistencias;
	}

	public void setTotal(int totalInconsistencias) {
		this.totalInconsistencias = totalInconsistencias;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}
	
	public int getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(int cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
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
