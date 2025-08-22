package com.tivic.manager.mob.lotes.dto.publicacao;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class NotificacaoPublicacaoPendenteDTO {

	private int cdAit;
	private String idAit;
	private String nrPlaca;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private int nrCodDetran;
	private String dsLocalInfracao;
	private double vlMulta;
	private double vlMultaDesconto;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	
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
	public int getNrCodDetran() {
		return nrCodDetran;
	}
	public void setNrCodDetran(int nrCodDetran) {
		this.nrCodDetran = nrCodDetran;
	}
	public String getDsLocalInfracao() {
		return dsLocalInfracao;
	}
	public void setDsLocalInfracao(String dsLocalInfracao) {
		this.dsLocalInfracao = dsLocalInfracao;
	}
	public double getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(double vlMulta) {
		this.vlMulta = vlMulta;
	}
	public double getVlMultaDesconto() {
		return vlMultaDesconto;
	}
	public void setVlMultaDesconto(double vlMultaDesconto) {
		this.vlMultaDesconto = vlMultaDesconto;
	}
	
	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
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
