package com.tivic.manager.ptc.protocolos.julgamento;

import java.util.List;

import com.tivic.manager.mob.lotes.dto.impressao.CreateLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface ICartaJulgamentoService {
	
	LoteImpressao save(LoteImpressao loteImpressao) throws Exception;
	LoteImpressao save(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception;
	LoteImpressao gerarLoteCartaJulgamento(CreateLoteImpressaoDTO createLoteImpressao) throws ValidacaoException, Exception;
	Search<CartaJulgamentoDTO> buscarLotes(SearchCriterios searchCriterios) throws ValidacaoException, Exception;
	List<AitDTO> buscarQuantidadeAitsParaLoteImpressao(int quantidadeAit, int tipoLote) throws ValidacaoException, Exception;
	void gerarDocumentosLote(int cdLoteImpressao, int cdUsuario) throws Exception;
	Object iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario) throws Exception;
	Object getStatusGeracaoDocumentos(int cdLoteImpressao) throws Exception;
	Object buscarAitsParaLoteImpressao(int tipoLote, SearchCriterios searchCriterios) throws ValidacaoException, Exception;
	Object imprimirLoteJulgamento(int cdLoteImpressao) throws Exception;
}
