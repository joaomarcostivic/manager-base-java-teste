package com.tivic.manager.adapter.base.antiga.infracao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.Infracao;

public class InfracaoOld {
	private int codInfracao;
	private String dsInfracao;
	private String dsInfracao2;
	private int nrPontuacao;
	private int nrCodDetran;
	private Double nrValorUfir;
	private String nmNatureza;
	private String nrArtigo;
	private String nrParagrafo;
	private String nrInciso;
	private String nrAlinea;
	private int tpCompetencia;
	private int lgPrioritaria;
	private GregorianCalendar dtFimVigencia;
	private Double vlInfracao;
	private int lgSuspensaoCnh;
	private int tpResponsabilidade;

	public InfracaoOld() { }

	public InfracaoOld(int codInfracao,
			String dsInfracao,
			String dsInfracao2,
			int nrPontuacao,
			int nrCodDetran,
			Double nrValorUfir,
			String nmNatureza,
			String nrArtigo,
			String nrParagrafo,
			String nrInciso,
			String nrAlinea,
			int tpCompetencia,
			int lgPrioritaria,
			GregorianCalendar dtFimVigencia,
			Double vlInfracao,
			int lgSuspensaoCnh,
			int tpResponsabilidade) {
		setCodInfracao(codInfracao);
		setDsInfracao(dsInfracao);
		setDsInfracao2(dsInfracao2);
		setNrPontuacao(nrPontuacao);
		setNrCodDetran(nrCodDetran);
		setNrValorUfir(nrValorUfir);
		setNmNatureza(nmNatureza);
		setNrArtigo(nrArtigo);
		setNrParagrafo(nrParagrafo);
		setNrInciso(nrInciso);
		setNrAlinea(nrAlinea);
		setTpCompetencia(tpCompetencia);
		setLgPrioritaria(lgPrioritaria);
		setDtFimVigencia(dtFimVigencia);
		setVlInfracao(vlInfracao);
		setLgSuspensaoCnh(lgSuspensaoCnh);
		setTpResponsabilidade(tpResponsabilidade);
	}
	public void setCodInfracao(int codInfracao){
		this.codInfracao=codInfracao;
	}
	public int getCodInfracao(){
		return this.codInfracao;
	}
	public void setDsInfracao(String dsInfracao){
		this.dsInfracao=dsInfracao;
	}
	public String getDsInfracao(){
		return this.dsInfracao;
	}
	public String getDsInfracao2() {
		return dsInfracao2;
	}
	public void setDsInfracao2(String dsInfracao2) {
		this.dsInfracao2 = dsInfracao2;
	}
	public void setNrPontuacao(int nrPontuacao){
		this.nrPontuacao=nrPontuacao;
	}
	public int getNrPontuacao(){
		return this.nrPontuacao;
	}
	public void setNrCodDetran(int nrCodDetran){
		this.nrCodDetran=nrCodDetran;
	}
	public int getNrCodDetran(){
		return this.nrCodDetran;
	}
	public void setNrValorUfir(Double nrValorUfir){
		this.nrValorUfir=nrValorUfir;
	}
	public Double getNrValorUfir(){
		return this.nrValorUfir;
	}
	public void setNmNatureza(String nmNatureza){
		this.nmNatureza=nmNatureza;
	}
	public String getNmNatureza(){
		return this.nmNatureza;
	}
	public void setNrArtigo(String nrArtigo){
		this.nrArtigo=nrArtigo;
	}
	public String getNrArtigo(){
		return this.nrArtigo;
	}
	public void setNrParagrafo(String nrParagrafo){
		this.nrParagrafo=nrParagrafo;
	}
	public String getNrParagrafo(){
		return this.nrParagrafo;
	}
	public void setNrInciso(String nrInciso){
		this.nrInciso=nrInciso;
	}
	public String getNrInciso(){
		return this.nrInciso;
	}
	public void setNrAlinea(String nrAlinea){
		this.nrAlinea=nrAlinea;
	}
	public String getNrAlinea(){
		return this.nrAlinea;
	}
	public void setTpCompetencia(int tpCompetencia){
		this.tpCompetencia=tpCompetencia;
	}
	public int getTpCompetencia(){
		return this.tpCompetencia;
	}
	public void setLgPrioritaria(int lgPrioritaria){
		this.lgPrioritaria=lgPrioritaria;
	}
	public int getLgPrioritaria(){
		return this.lgPrioritaria;
	}
	public void setDtFimVigencia(GregorianCalendar dtFimVigencia){
		this.dtFimVigencia=dtFimVigencia;
	}
	public GregorianCalendar getDtFimVigencia(){
		return this.dtFimVigencia;
	}
	public void setVlInfracao(Double vlInfracao){
		this.vlInfracao=vlInfracao;
	}
	public Double getVlInfracao(){
		return this.vlInfracao;
	}
	public void setLgSuspensaoCnh(int lgSuspensaoCnh){
		this.lgSuspensaoCnh=lgSuspensaoCnh;
	}
	public int getLgSuspensaoCnh(){
		return this.lgSuspensaoCnh;
	}
	public void setTpResponsabilidade(int tpResponsabilidade){
		this.tpResponsabilidade=tpResponsabilidade;
	}
	public int getTpResponsabilidade(){
		return this.tpResponsabilidade;
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
		return new Infracao(getCodInfracao(),
			getDsInfracao(),
			getNrPontuacao(),
			getNrCodDetran(),
			getNrValorUfir(),
			getNmNatureza(),
			getNrArtigo(),
			getNrParagrafo(),
			getNrInciso(),
			getNrAlinea(),
			getTpCompetencia(),
			getLgPrioritaria(),
			getDtFimVigencia()==null ? null : (GregorianCalendar)getDtFimVigencia().clone(),
			getVlInfracao(),
			getLgSuspensaoCnh(),
			getTpResponsabilidade());
	}
}
