package com.tivic.manager.mob.ecarta.services;

import com.tivic.manager.mob.ecarta.dtos.ArquivoServicoDTO;

public interface IArquivoProducaoServiceEDI {

	ArquivoServicoDTO gerarRejeicao(int cdLoteImpressao) throws Exception;

	ArquivoServicoDTO gerarConfirmacao(int cdLoteImpressao, int cdUsuario) throws Exception;

}
