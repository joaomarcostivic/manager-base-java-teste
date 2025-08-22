package com.tivic.manager.eCarta.CorreiosEtiquetaBuilder;

import java.util.GregorianCalendar;
import com.tivic.manager.eCarta.Etiqueta;
import com.tivic.manager.eCarta.monitors.MonitorsResponseTransito;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.str.AitMovimentoServices;

public class CorreiosEtiquetaBuilder {
	public CorreiosEtiqueta build(Etiqueta etiqueta,int cdLoteCorreios) {
		CorreiosEtiqueta correiosEtiqueta = new CorreiosEtiqueta();
		correiosEtiqueta.setCdAit(etiqueta.getCodAit());
		correiosEtiqueta.setCdLote(cdLoteCorreios);
		correiosEtiqueta.setDtEnvio(new GregorianCalendar());
		correiosEtiqueta.setNrEtiqueta((int) etiqueta.getNumEtiqueta());;
		correiosEtiqueta.setTpStatus(verificartpStatus(etiqueta.getTpDocumento()));
		correiosEtiqueta.setSgServico(etiqueta.getSgInicial());
		correiosEtiqueta.setNrMovimento(AitMovimentoServices.getMovimentoTpStatus(etiqueta.getCodAit(), verificartpStatus(etiqueta.getTpDocumento())).getNrMovimento());
		return correiosEtiqueta;
	}
	
	private int verificartpStatus(int tpDocumento) {
		int tpStatus = 0;
		switch (tpDocumento) {
			case MonitorsResponseTransito.LOTE_NAI:
				tpStatus = com.tivic.manager.mob.AitMovimentoServices.NAI_ENVIADO;
				break;
			case MonitorsResponseTransito.LOTE_NIP:
				tpStatus = com.tivic.manager.mob.AitMovimentoServices.NIP_ENVIADA;
				break;
		}
		return tpStatus;
	}
}
