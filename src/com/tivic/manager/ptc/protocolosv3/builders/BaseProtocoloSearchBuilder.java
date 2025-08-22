package com.tivic.manager.ptc.protocolosv3.builders;

import com.tivic.manager.ptc.protocolosv3.search.DadosProtocoloDTO;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class BaseProtocoloSearchBuilder {
	
	SearchBuilder<DadosProtocoloDTO> search;
	
	public BaseProtocoloSearchBuilder() throws Exception {
		search = new SearchBuilder<DadosProtocoloDTO>("mob_ait_movimento A");
	}
	
	public SearchBuilder<DadosProtocoloDTO> getSearch(SearchCriterios searchCriterios) throws Exception {
				search.fields(" A.*, B.ID_AIT, B.NR_PLACA, D.NR_DOCUMENTO, D.NM_REQUERENTE, "
					  + "D.CD_DOCUMENTO, D.DT_PROTOCOLO, D.TXT_OBSERVACAO, D.TP_DOCUMENTO, B.DT_INFRACAO, E.NM_TIPO_DOCUMENTO,"
					  + "E.ID_TIPO_DOCUMENTO, F.CD_FASE, F.NM_FASE, H.CD_SITUACAO_DOCUMENTO, H.NM_SITUACAO_DOCUMENTO")
				.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
				.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
				.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
				.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
				.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
				.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
				.searchCriterios(searchCriterios)
				.orderBy(" B.NR_PLACA ");
		return search;
	}
}
