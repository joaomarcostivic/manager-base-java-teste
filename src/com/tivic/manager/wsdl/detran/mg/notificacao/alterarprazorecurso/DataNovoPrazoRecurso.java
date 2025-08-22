package com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso;

import java.util.GregorianCalendar;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.TipoStatusEnum;

public class DataNovoPrazoRecurso {

	public GregorianCalendar get(int tpStatus, Ait ait) {
		if (tpStatus == TipoStatusEnum.NOVO_PRAZO_DEFESA.getKey()) {
			return ait.getDtPrazoDefesa();
		}
		else if (tpStatus == TipoStatusEnum.NOVO_PRAZO_JARI.getKey()) {
			return ait.getDtVencimento();
		}
		else {
			return new GregorianCalendar();
		}
	}
	
}
