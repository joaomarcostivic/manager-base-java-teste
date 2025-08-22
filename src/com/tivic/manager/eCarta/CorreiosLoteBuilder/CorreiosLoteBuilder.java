package com.tivic.manager.eCarta.CorreiosLoteBuilder;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.CorreiosLoteServices;
import com.tivic.manager.mob.StCorreiosLote;

public class CorreiosLoteBuilder {
	public CorreiosLote build() {
		CorreiosLote correiosLote = new CorreiosLote();
		correiosLote.setDtLote(new GregorianCalendar());
		correiosLote.setTpLote(CorreiosLoteServices.E_CARTAS);
		correiosLote.setStLote(StCorreiosLote.CONCLUIDO);
		return correiosLote;
	}
}
