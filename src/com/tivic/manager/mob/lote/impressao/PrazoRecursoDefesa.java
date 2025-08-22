package com.tivic.manager.mob.lote.impressao;

import java.util.GregorianCalendar;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class PrazoRecursoDefesa implements IPrazoRecurso {
	ILoteNotificacaoService loteNotificacaoService;
	
	public PrazoRecursoDefesa() throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
	}

	@Override
	public GregorianCalendar gerarPrazo(int cdAit, CustomConnection customConnection) throws Exception {
		String sgEstado = loteNotificacaoService.getEstadoOrgaoAutuador();
		switch (sgEstado) {
			case "BA":
				return new PrazoRecursoDefesaBA().gerarPrazo(cdAit, customConnection);
			case "MG":
				return new PrazoRecursoDefesaMG().gerarPrazo(cdAit, customConnection);
			default: 
				throw new ValidacaoException("Estado não identificado para gerar prazo de recurso.");
		}
	}
}
