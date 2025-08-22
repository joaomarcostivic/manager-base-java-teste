package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.GregorianCalendar;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class VerificarPrazoDefesaMG implements IVerificarPrazoDefesa{
	public void verificarPrazoDefesa(int cdAit, GregorianCalendar dtEntreguePublicado, Connection connect) throws ValidacaoException {
		if (dtEntreguePublicado == null) {
			throw new ValidacaoException ("Data de entrega publicação não encontrada.");
		}
		Ait ait = AitDAO.get(cdAit, connect);
		GregorianCalendar dtExpiracao = ait.getDtPrazoDefesa();	
		if (Util.getDataAtual().before(dtExpiracao)){
			throw new ValidacaoException("NAI possui dados de entrega/publicação e não expirou prazo de defesa");
		}
	}
}
