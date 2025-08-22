package com.tivic.manager.mob.lotes.service.dividaativa;

import java.util.List;

import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaDTO;
import com.tivic.manager.mob.lotes.dto.dividaativa.DividaAtivaImportacaoDTO;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ILoteDividaAtivaService {
	Lote gerarLoteDividaAtiva(List<DividaAtivaDTO> aitList, int cdUsuario) throws Exception;
	Lote gerarLoteDividaAtiva(List<DividaAtivaDTO> aitList, int cdUsuario, CustomConnection customConnection) throws Exception;
	byte[] getArquivo(int cdArquivo) throws Exception;
	byte[] getArquivo(int cdArquivo, CustomConnection customConnection) throws Exception;
	DividaAtivaImportacaoDTO confirmarDividaAtiva(DividaAtivaImportacaoDTO aits, int cdUsuario) throws Exception;
	List<DividaAtivaDTO> searchInLoteDividaAtiva(SearchCriterios searchCriterios) throws Exception;
	List<DividaAtivaDTO> searchInLoteDividaAtiva(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
