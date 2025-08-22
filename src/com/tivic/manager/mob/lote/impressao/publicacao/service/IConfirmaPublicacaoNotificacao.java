package com.tivic.manager.mob.lote.impressao.publicacao.service;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.grafica.LoteImpressao;

public interface IConfirmaPublicacaoNotificacao {
	 LoteImpressao confirmar(int cdLoteImpressao, GregorianCalendar dtConfirmacao, int cdUsuario) throws Exception;
}
