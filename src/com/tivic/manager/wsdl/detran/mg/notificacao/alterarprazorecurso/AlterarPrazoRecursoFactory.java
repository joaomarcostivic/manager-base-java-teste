package com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso;

import com.tivic.manager.wsdl.detran.mg.notificacao.TipoNotificacaoEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AlterarPrazoRecursoFactory {
	
	public IAlterarPrazoRecurso getStrategy(int tipoRecurso) throws Exception {
		if (tipoRecurso == TipoNotificacaoEnum.NOVO_PRAZO_DEFESA.getKey()) {
			return new AlterarPrazoRecursoDefesa();
		}
		else if (tipoRecurso == TipoNotificacaoEnum.NOVO_PRAZO_JARI.getKey()) {
			return new AlterarPrazoRecursoJari();
		}
		else {
			throw new ValidacaoException("Tipo de Recurso Não Encontrado");
		}
	}
	
}
