package com.tivic.manager.ptc.protocolosv3.documento.ata;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class Ata {

	private int cdAta;
	private int cdUsuario;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtCadastro;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAta;
	private String idAta;

	public Ata() { }

	public Ata(int cdAta,
			int cdUsuario,
			GregorianCalendar dtCadastro,
			GregorianCalendar dtAta,
			String idAit) {
		setCdAta(cdAta);
		setCdUsuario(cdUsuario);
		setDtCadastro(dtCadastro);
		setDtAta(dtAta);
		setIdAta(idAta);
	}
	public void setCdAta(int cdAta){
		this.cdAta=cdAta;
	}
	
	public int getCdAta(){
		return this.cdAta;
	}
	
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	
	public void setDtAta(GregorianCalendar dtAta){
		this.dtAta=dtAta;
	}
	
	public GregorianCalendar getDtAta(){
		return this.dtAta;
	}
	
	public void setIdAta(String idAta){
		this.idAta=idAta;
	}

	public String getIdAta(){
		return this.idAta;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "Não foi possível serializar o objeto informado";
		}
	}

	public Object clone() {
		return new Ata(getCdAta(),
			getCdUsuario(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getDtAta()==null ? null : (GregorianCalendar)getDtAta().clone(),
			getIdAta());
	}

}
