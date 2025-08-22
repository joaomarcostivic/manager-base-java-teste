package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class Infracao {

	private int cdInfracao;
	private String dsInfracao;
	private int nrPontuacao;
	private int nrCodDetran;
	private Double vlUfir;
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

	public Infracao() { }

	public Infracao(int cdInfracao,
			String dsInfracao,
			int nrPontuacao,
			int nrCodDetran,
			Double vlUfir,
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
		setCdInfracao(cdInfracao);
		setDsInfracao(dsInfracao);
		setNrPontuacao(nrPontuacao);
		setNrCodDetran(nrCodDetran);
		setVlUfir(vlUfir);
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
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setDsInfracao(String dsInfracao){
		this.dsInfracao=dsInfracao;
	}
	public String getDsInfracao(){
		return this.dsInfracao;
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
	public void setVlUfir(Double vlUfir){
		this.vlUfir=vlUfir;
	}
	public Double getVlUfir(){
		return this.vlUfir;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdInfracao: " +  getCdInfracao();
		valueToString += ", dsInfracao: " +  getDsInfracao();
		valueToString += ", nrPontuacao: " +  getNrPontuacao();
		valueToString += ", nrCodDetran: " +  getNrCodDetran();
		valueToString += ", vlUfir: " +  getVlUfir();
		valueToString += ", nmNatureza: " +  getNmNatureza();
		valueToString += ", nrArtigo: " +  getNrArtigo();
		valueToString += ", nrParagrafo: " +  getNrParagrafo();
		valueToString += ", nrInciso: " +  getNrInciso();
		valueToString += ", nrAlinea: " +  getNrAlinea();
		valueToString += ", tpCompetencia: " +  getTpCompetencia();
		valueToString += ", lgPrioritaria: " +  getLgPrioritaria();
		valueToString += ", dtFimVigencia: " +  sol.util.Util.formatDateTime(getDtFimVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlInfracao: " +  getVlInfracao();
		valueToString += ", lgSuspensaoCnh: " +  getLgSuspensaoCnh();
		valueToString += ", tpResponsabilidade: " +  getTpResponsabilidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Infracao(getCdInfracao(),
			getDsInfracao(),
			getNrPontuacao(),
			getNrCodDetran(),
			getVlUfir(),
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdInfracao;
		result = prime * result + ((dsInfracao == null) ? 0 : dsInfracao.hashCode());
		result = prime * result + ((dtFimVigencia == null) ? 0 : dtFimVigencia.hashCode());
		result = prime * result + lgPrioritaria;
		result = prime * result + lgSuspensaoCnh;
		result = prime * result + ((nmNatureza == null) ? 0 : nmNatureza.hashCode());
		result = prime * result + ((nrAlinea == null) ? 0 : nrAlinea.hashCode());
		result = prime * result + ((nrArtigo == null) ? 0 : nrArtigo.hashCode());
		result = prime * result + nrCodDetran;
		result = prime * result + ((nrInciso == null) ? 0 : nrInciso.hashCode());
		result = prime * result + ((nrParagrafo == null) ? 0 : nrParagrafo.hashCode());
		result = prime * result + nrPontuacao;
		result = prime * result + tpCompetencia;
		result = prime * result + tpResponsabilidade;
		result = prime * result + ((vlInfracao == null) ? 0 : vlInfracao.hashCode());
		result = prime * result + ((vlUfir == null) ? 0 : vlUfir.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Infracao other = (Infracao) obj;
		if (cdInfracao != other.cdInfracao)
			return false;
		if (dsInfracao == null) {
			if (other.dsInfracao != null)
				return false;
		} else if (!dsInfracao.equals(other.dsInfracao))
			return false;
		if (dtFimVigencia == null) {
			if (other.dtFimVigencia != null)
				return false;
		} else if (!dtFimVigencia.equals(other.dtFimVigencia))
			return false;
		if (lgPrioritaria != other.lgPrioritaria)
			return false;
		if (lgSuspensaoCnh != other.lgSuspensaoCnh)
			return false;
		if (nmNatureza == null) {
			if (other.nmNatureza != null)
				return false;
		} else if (!nmNatureza.equals(other.nmNatureza))
			return false;
		if (nrAlinea == null) {
			if (other.nrAlinea != null)
				return false;
		} else if (!nrAlinea.equals(other.nrAlinea))
			return false;
		if (nrArtigo == null) {
			if (other.nrArtigo != null)
				return false;
		} else if (!nrArtigo.equals(other.nrArtigo))
			return false;
		if (nrCodDetran != other.nrCodDetran)
			return false;
		if (nrInciso == null) {
			if (other.nrInciso != null)
				return false;
		} else if (!nrInciso.equals(other.nrInciso))
			return false;
		if (nrParagrafo == null) {
			if (other.nrParagrafo != null)
				return false;
		} else if (!nrParagrafo.equals(other.nrParagrafo))
			return false;
		if (nrPontuacao != other.nrPontuacao)
			return false;
		if (tpCompetencia != other.tpCompetencia)
			return false;
		if (tpResponsabilidade != other.tpResponsabilidade)
			return false;
		if (vlInfracao == null) {
			if (other.vlInfracao != null)
				return false;
		} else if (!vlInfracao.equals(other.vlInfracao))
			return false;
		if (vlUfir == null) {
			if (other.vlUfir != null)
				return false;
		} else if (!vlUfir.equals(other.vlUfir))
			return false;
		return true;
	}
	
	

}