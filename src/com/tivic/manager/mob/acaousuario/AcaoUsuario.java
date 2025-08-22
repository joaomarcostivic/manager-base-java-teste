package com.tivic.manager.mob.acaousuario;

import java.util.GregorianCalendar;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class AcaoUsuario {

	private int cdAcao;
	private int cdUsuario;
	private String dsAcao;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtAcao;

	public int getCdAcao() {
		return cdAcao;
	}

	public void setCdAcao(int cdAcao) {
		this.cdAcao = cdAcao;
	}

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getDsAcao() {
		return dsAcao;
	}

	public void setDsAcao(String dsAcao) {
		this.dsAcao = dsAcao;
	}

	public GregorianCalendar getDtAcao() {
		return dtAcao;
	}

	public void setDtAcao(GregorianCalendar dtAcao) {
		this.dtAcao = dtAcao;
	}
}
