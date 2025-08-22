package com.tivic.manager.mob.ecarta.services;

public interface IAtualizaLoteImpressaoECT {

	void confirmarProducao(int cdLoteImpressao) throws Exception;

	void rejeitarProducao(int cdLoteImpressao) throws Exception;

}
