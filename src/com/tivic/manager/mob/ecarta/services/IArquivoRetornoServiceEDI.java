package com.tivic.manager.mob.ecarta.services;

import java.util.List;

import com.tivic.manager.mob.ecarta.dtos.ArquivoEcartaDTO;
import com.tivic.manager.mob.ecarta.dtos.StatusLeituraArquivoRetornoDTO;

public interface IArquivoRetornoServiceEDI {
	StatusLeituraArquivoRetornoDTO processar(List<ArquivoEcartaDTO> arquivosRetorno, int cdLoteImpressao,
			int cdUsuario)
			throws Exception;
}
