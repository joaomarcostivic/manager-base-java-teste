package com.tivic.manager.mob.talonario;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class TalonarioDTO {
	
	private int cdTalao;
	private int nrTalao;
	private int nrInicial;
	private int nrFinal;
	private int cdAgente;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtEntrega;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtDevolucao;
	private int stTalao;
	private int tpTalao;
	private String sgTalao;
	private int stLogin;
	private int nrUltimoAit;
	private String nmAgente;
	private int qtFolhasDisponiveis;
	
	public int getCdTalao() {
		return cdTalao;
	}
	public void setCdTalao(int cdTalao) {
		this.cdTalao = cdTalao;
	}
	public int getNrTalao() {
		return nrTalao;
	}
	public void setNrTalao(int nrTalao) {
		this.nrTalao = nrTalao;
	}
	public int getNrInicial() {
		return nrInicial;
	}
	public void setNrInicial(int nrInicial) {
		this.nrInicial = nrInicial;
	}
	public int getNrFinal() {
		return nrFinal;
	}
	public void setNrFinal(int nrFinal) {
		this.nrFinal = nrFinal;
	}
	public int getCdAgente() {
		return cdAgente;
	}
	public void setCdAgente(int cdAgente) {
		this.cdAgente = cdAgente;
	}
	public GregorianCalendar getDtEntrega() {
		return dtEntrega;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega) {
		this.dtEntrega = dtEntrega;
	}
	public GregorianCalendar getDtDevolucao() {
		return dtDevolucao;
	}
	public void setDtDevolucao(GregorianCalendar dtDevolucao) {
		this.dtDevolucao = dtDevolucao;
	}
	public int getStTalao() {
		return stTalao;
	}
	public void setStTalao(int stTalao) {
		this.stTalao = stTalao;
	}
	public int getTpTalao() {
		return tpTalao;
	}
	public void setTpTalao(int tpTalao) {
		this.tpTalao = tpTalao;
	}
	public String getSgTalao() {
		return sgTalao;
	}
	public void setSgTalao(String sgTalao) {
		this.sgTalao = sgTalao;
	}
	public int getStLogin() {
		return stLogin;
	}
	public void setStLogin(int stLogin) {
		this.stLogin = stLogin;
	}
	public int getNrUltimoAit() {
		return nrUltimoAit;
	}
	public void setNrUltimoAit(int nrUltimoAit) {
		this.nrUltimoAit = nrUltimoAit;
	}
	public String getNmAgente() {
		return nmAgente;
	}
	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}
	public int getQtFolhasDisponiveis() {
		return qtFolhasDisponiveis;
	}
	public void setQtFolhasDisponiveis(int qtFolhasDisponiveis) {
		this.qtFolhasDisponiveis = qtFolhasDisponiveis;
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
