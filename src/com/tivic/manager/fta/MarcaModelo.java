package com.tivic.manager.fta;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MarcaModelo {

	private int cdMarca;
	private String nmMarca;
	private String nmModelo;
	private int tpMarca;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAtualizacao;
	
	private String nrMarca;
	
	public MarcaModelo(){ }

	public MarcaModelo(int cdMarca,
			String nmMarca,
			String nmModelo,
			int tpMarca,
			GregorianCalendar dtAtualizacao,
			String nrMarca){
		setCdMarca(cdMarca);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setTpMarca(tpMarca);
		setDtAtualizacao(dtAtualizacao);
		setNrMarca(nrMarca);
	}

	public void setCdMarca(int cdMarca){
		this.cdMarca=cdMarca;
	}
	public int getCdMarca(){
		return this.cdMarca;
	}
	public void setNmMarca(String nmMarca){
		this.nmMarca=nmMarca;
	}
	public String getNmMarca(){
		return this.nmMarca;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}	
	public void setTpMarca(int tpMarca) {
		this.tpMarca=tpMarca;
	}
	public int getTpMarca() {
		return this.tpMarca;
	}	
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao() {
		return dtAtualizacao;
	}
	public void setNrMarca(String nrMarca) {
		this.nrMarca = nrMarca;
	}
	public String getNrMarca() {
		return nrMarca;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdMarca\": " +  getCdMarca();
		valueToString += ", \"nmMarca\": \"" +  getNmMarca() + "\"";
		valueToString += ", \"nmModelo\": \"" +  getNmModelo() + "\"";
		valueToString += ", \"tpMarca\": " +  getTpMarca();
		valueToString += ", \"dtAtualizacao\": " +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", \"nrMarca\": \"" +  getNrMarca() + "\"";
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MarcaModelo(getCdMarca(),
			getNmMarca(),
			getNmModelo(),
			getTpMarca(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getNrMarca());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdMarca;
		result = prime * result + ((dtAtualizacao == null) ? 0 : dtAtualizacao.hashCode());
		result = prime * result + ((nmMarca == null) ? 0 : nmMarca.hashCode());
		result = prime * result + ((nmModelo == null) ? 0 : nmModelo.hashCode());
		result = prime * result + ((nrMarca == null) ? 0 : nrMarca.hashCode());
		result = prime * result + tpMarca;
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
		MarcaModelo other = (MarcaModelo) obj;
		if (cdMarca != other.cdMarca)
			return false;
		if (dtAtualizacao == null) {
			if (other.dtAtualizacao != null)
				return false;
		} else if (!dtAtualizacao.equals(other.dtAtualizacao))
			return false;
		if (nmMarca == null) {
			if (other.nmMarca != null)
				return false;
		} else if (!nmMarca.equals(other.nmMarca))
			return false;
		if (nmModelo == null) {
			if (other.nmModelo != null)
				return false;
		} else if (!nmModelo.equals(other.nmModelo))
			return false;
		if (nrMarca == null) {
			if (other.nrMarca != null)
				return false;
		} else if (!nrMarca.equals(other.nrMarca))
			return false;
		if (tpMarca != other.tpMarca)
			return false;
		return true;
	}
	
	

}