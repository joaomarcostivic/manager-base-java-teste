package com.tivic.manager.mob.listagempagamentos;

import java.util.List;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;


public class ListagemPagamentosService implements IListagemPagamentosService {

	@Override
	public PagedResponse<RelatorioPagamentoDTO> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<RelatorioPagamentoDTO> search = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			List<RelatorioPagamentoDTO> listResultadoEntradaNaiDTO = search.getList(RelatorioPagamentoDTO.class);
			return new PagedResponse<RelatorioPagamentoDTO>(listResultadoEntradaNaiDTO, search.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public Search<RelatorioPagamentoDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioPagamentoDTO> search = new SearchBuilder<RelatorioPagamentoDTO>("mob_ait_pagamento A")
				.fields("A.cd_ait, B.id_ait, C.nr_cod_detran, A.st_pagamento, date(A.dt_pagamento) AS dt_pagamento, "
						+ "date(A.dt_credito) AS dt_credito, B.vl_multa, A.vl_pago, uf_pagamento AS sg_uf_pagamento, "
						+ "tp_arrecadacao, tp_modalidade AS tp_forma_pagamento, D.nm_banco")
				.addJoinTable(" JOIN mob_ait B ON (A.cd_ait = B.cd_ait) ")
				.addJoinTable(" JOIN mob_infracao C ON (B.cd_infracao = C.cd_infracao) ")
				.addJoinTable(" JOIN grl_banco D ON (A.nr_banco = D.nr_banco) ")
				.addJoinTable(" LEFT OUTER JOIN grl_agencia E ON (A.nr_agencia = E.nr_agencia) ")
				.orderBy("A.dt_pagamento DESC, B.id_ait DESC ")
				.searchCriterios(searchCriterios)
			.build();
		return search;
	}
}
