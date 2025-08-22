package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.GregorianCalendar;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.manager.mob.Ait;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;
import com.tivic.sol.util.date.DateUtil;

public class AitDTO extends Ait {
	
	private int cdAit;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtMovimento;
	private String idAit;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtInfracao;
	private String nrPlaca;

	
	public AitDTO() {}

	public GregorianCalendar getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(GregorianCalendar dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public String getIdAit() {
		return idAit;
	}

	public void setIdAit(String idAit) {
		this.idAit = idAit;
	}

	public GregorianCalendar getDtInfracao() {
		return dtInfracao;
	}

	public void setDtInfracao(GregorianCalendar dtInfracao) {
		this.dtInfracao = dtInfracao;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}

	public void setNrPlaca(String nrPlaca) {
		this.nrPlaca = nrPlaca;
	}
	
	public int getCdAit() {
		return cdAit;
	}

	public void setCdAit(int cdAit) {
		this.cdAit = cdAit;
	}

	@Override
	public String toString() {
		try {
			JSONObject jsonDadosJulgamentoDTO = new JSONObject();
			jsonDadosJulgamentoDTO.put("cdAit", getCdAit());
			jsonDadosJulgamentoDTO.put("idAit", getIdAit());
			jsonDadosJulgamentoDTO.put("dtMovimento", DateUtil.convCalendarStringIso(getDtMovimento()));
			jsonDadosJulgamentoDTO.put("nrPlaca", getNrPlaca());
			jsonDadosJulgamentoDTO.put("dtInfracao", DateUtil.convCalendarStringIso(getDtInfracao()));
			return jsonDadosJulgamentoDTO.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return "cdAit: " + getCdAit();
		}
	}

}


