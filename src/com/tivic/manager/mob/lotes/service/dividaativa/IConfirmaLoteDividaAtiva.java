package com.tivic.manager.mob.lotes.service.dividaativa;

import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaImportacaoDTO;

public interface IConfirmaLoteDividaAtiva {
	DividaAtivaImportacaoDTO confirmar(DividaAtivaImportacaoDTO dividaAtivaImportacaoDTO, int cdUsuario) throws Exception;
}
