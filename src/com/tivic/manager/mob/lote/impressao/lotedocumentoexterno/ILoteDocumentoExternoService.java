package com.tivic.manager.mob.lote.impressao.lotedocumentoexterno;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ILoteDocumentoExternoService {
	public LoteImpressao saveOrUpdateLoteImpressao(LoteImpressao loteImpressao) throws Exception;
	public LoteImpressao saveOrUpdateLoteImpressao(LoteImpressao loteImpressao,  CustomConnection customConnection) throws Exception;
	public PagedResponse<LoteDocumentoExternoDTO> find(SearchCriterios searchCriterios) throws Exception;
	public Arquivo getArquivoLote(int cdLoteImpressao, int cdTpArquivo) throws Exception;
}
