package com.tivic.manager.mob.lotes.service.dividaativa;

import java.util.List;

import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.sol.connection.CustomConnection;

public interface IGeraLoteDividaAtiva {
	Lote gerarLote(int cdUsuario, CustomConnection customConnection) throws Exception;
	int gerarLoteDividaAtiva(int cdLote, CustomConnection customConnection) throws Exception;
	void gerarLoteDividaAtivaAit(List<DividaAtivaDTO> aitList, int cdLoteDividaAtiva, CustomConnection customConnection) throws Exception;
	int inserirArquivo(int cdLote, CustomConnection customConnection) throws Exception;
	void atualizaLote(int cdLote, int cdArquivo, CustomConnection customConnection) throws Exception;
}
