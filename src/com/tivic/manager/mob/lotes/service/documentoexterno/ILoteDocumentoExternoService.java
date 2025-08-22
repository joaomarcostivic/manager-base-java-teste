package com.tivic.manager.mob.lotes.service.documentoexterno;

import com.tivic.manager.mob.lotes.dto.documentoexterno.LoteDocumentoExternoDTO;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ILoteDocumentoExternoService {
	public LoteImpressao saveOrUpdateLoteImpressao(LoteImpressao loteImpressao) throws Exception;
	public LoteImpressao saveOrUpdateLoteImpressao(LoteImpressao loteImpressao,  CustomConnection customConnection) throws Exception;
	public PagedResponse<LoteDocumentoExternoDTO> find(SearchCriterios searchCriterios) throws Exception;
	Object imprimirOficioExterno(int cdLoteImpressao) throws Exception;

}
