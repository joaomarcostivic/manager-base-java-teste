package com.tivic.manager.relatorios.estatisticasaits;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RelatorioEstatisticasAitsDTO {
	private int qtdInfracoes;
	private String dsInfracao;
	private int cdInfracao;
	private int nrCodDetran;
	private String nmAgente;
	private String dsLocalInfracao;
	private int mesInfracao;
	private int anoInfracao;
	private String nmNatureza;
	private String nmMunicipioPlaca;
	private double somaParcial;
	private int qtdInfracoesFora;
	private int qtdInfracoesLocal;
	private int qtdNais;
	private int qtdNips;
	private GregorianCalendar dtMovimento;
	private String categoryAxis;
	private String xaxisLabel;
	
	public String getNmMunicipioPlaca() {
		return nmMunicipioPlaca;
	}
	public void setNmMunicipioPlaca(String nmMunicipioPlaca) {
		this.nmMunicipioPlaca = nmMunicipioPlaca;
	}
	public String getDsLocalInfracao() {
		return dsLocalInfracao;
	}
	public void setDsLocalInfracao(String dsLocalInfracao) {
		this.dsLocalInfracao = dsLocalInfracao;
	}
	public double getSomaParcial() {
		return somaParcial;
	}
	public void setSomaParcial(double somaParcial) {
		this.somaParcial = somaParcial;
	}
	public String getXaxisLabel() {
		return xaxisLabel;
	}
	public void setXaxisLabel(String xaxisLabel) {
		this.xaxisLabel = xaxisLabel;
	}
	public String getCategoryAxis() {
		return categoryAxis;
	}
	public void setCategoryAxis(String categoryAxis) {
		this.categoryAxis = categoryAxis;
	}
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}
	public int getQtdNais() {
		return qtdNais;
	}
	public void setQtdNais(int qtdNais) {
		this.qtdNais = qtdNais;
	}
	public int getQtdNips() {
		return qtdNips;
	}
	public void setQtdNips(int qtdNips) {
		this.qtdNips = qtdNips;
	}
	public int getQtdInfracoesFora() {
		return qtdInfracoesFora;
	}
	public void setQtdInfracoesFora(int qtdInfracoesFora) {
		this.qtdInfracoesFora = qtdInfracoesFora;
	}
	public int getQtdInfracoesLocal() {
		return qtdInfracoesLocal;
	}
	public void setQtdInfracoesLocal(int qtdInfracoesLocal) {
		this.qtdInfracoesLocal = qtdInfracoesLocal;
	}
	public String getNmNatureza() {
		return nmNatureza;
	}
	public void setNmNatureza(String nmNatureza) {
		this.nmNatureza = nmNatureza;
	}
	public int getMesInfracao() {
		return mesInfracao;
	}
	public void setMesInfracao(int mesInfracao) {
		this.mesInfracao = mesInfracao;
	}
	public int getAnoInfracao() {
		return anoInfracao;
	}
	public void setAnoInfracao(int anoInfracao) {
		this.anoInfracao = anoInfracao;
	}
	public String getNmAgente() {
		return nmAgente;
	}
	public void setNmAgente(String nmAgente) {
		this.nmAgente = nmAgente;
	}
	public int getQtdInfracoes() {
		return qtdInfracoes;
	}
	public void setQtdInfracoes(int qtdInfracoes) {
		this.qtdInfracoes = qtdInfracoes;
	}
	public String getDsInfracao() {
		return dsInfracao;
	}
	public void setDsInfracao(String dsInfracao) {
		this.dsInfracao = dsInfracao;
	}
	public int getCdInfracao() {
		return cdInfracao;
	}
	public void setCdInfracao(int cdInfracao) {
		this.cdInfracao = cdInfracao;
	}
	public int getNrCodDetran() {
		return nrCodDetran;
	}
	public void setNrCodDetran(int nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
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
