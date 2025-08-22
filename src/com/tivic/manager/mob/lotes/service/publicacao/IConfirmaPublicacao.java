package com.tivic.manager.mob.lotes.service.publicacao;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;

public interface IConfirmaPublicacao {

	public LotePublicacao confirmar(int cdLotePublicacao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception;

}
