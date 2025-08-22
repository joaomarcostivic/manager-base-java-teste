package com.tivic.manager.mob.pericia;

import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class PericiaServices {
	public static PagedResponse<ResultadoPericiaDTO> find(SearchCriterios searchCriterios) throws Exception {
		try {
			Search<ResultadoPericiaDTO> search = new SearchBuilder<ResultadoPericiaDTO>(" agd_agenda_item A ")
					.fields(" A.cd_agenda_item, A.dt_realizacao, A.dt_alteracao, B.nr_documento, B.txt_observacao, B.dt_protocolo as dt_solicitacao," +
							" E.dt_validade, E.lg_acompanhante, F.nm_situacao_documento, G.nm_pessoa as nm_solicitante, H.nm_pessoa as nm_medico ")
					.addJoinTable(" JOIN ptc_documento B ON (A.cd_documento = B.cd_documento) ")
					.addJoinTable(" JOIN ptc_documento_pessoa C ON (A.cd_documento = C.cd_documento) ")
					.addJoinTable(" LEFT OUTER JOIN mob_cartao_documento D ON ( A.cd_documento = D.cd_documento) ")
					.addJoinTable(" LEFT OUTER JOIN mob_cartao E ON ( D.cd_cartao = E.cd_cartao) ")
					.addJoinTable(" LEFT OUTER JOIN ptc_situacao_documento F ON (B.cd_situacao_documento = F.cd_situacao_documento) ")
					.addJoinTable(" LEFT OUTER JOIN grl_pessoa G ON (C.cd_pessoa = G.cd_pessoa) ")
					.addJoinTable(" LEFT OUTER join grl_pessoa H ON (A.cd_pessoa = H.cd_pessoa) ")
					.searchCriterios(searchCriterios)
					.orderBy(searchCriterios.getOrderBy())
					.count()
					.build();
			
			PericiaListBuilder listBuilder = new PericiaListBuilder(search.getRsm(),search.getRsm().getTotal());
			return listBuilder.build();
		} catch( Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PericiaServices.find: " + e);
			throw e;
		}
	}
}
