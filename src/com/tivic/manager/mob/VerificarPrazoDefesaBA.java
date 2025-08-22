package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class VerificarPrazoDefesaBA implements IVerificarPrazoDefesa{
	public void verificarPrazoDefesa(int cdAit, GregorianCalendar dtEntreguePublicado, Connection connect) throws ValidacaoException {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		GregorianCalendar dtExpiracao = dtEntreguePublicado;
		if (lgBaseAntiga)
			dtExpiracao.add(Calendar.DAY_OF_MONTH, Integer.parseInt((String)  ParametroServices.getValorOfParametro("nr_dias_defesa_previa", connect)) + 15);
		else
			dtExpiracao.add(Calendar.DAY_OF_MONTH, Integer.parseInt((String) ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", connect)) + 15);									
		if (Util.getDataAtual().before(dtExpiracao)){
			throw new ValidacaoException("NAI possui dados de entrega/publicação e não expirou prazo de defesa");
		}
	}
	
}
