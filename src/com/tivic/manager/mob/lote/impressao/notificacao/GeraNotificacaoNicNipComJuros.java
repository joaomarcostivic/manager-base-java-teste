package com.tivic.manager.mob.lote.impressao.notificacao;

import com.tivic.manager.mob.lote.impressao.DadosNotificacao;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public class GeraNotificacaoNicNipComJuros extends DadosNotificacaoComJurosBase {
	
	public GeraNotificacaoNicNipComJuros() throws Exception {
		super();
	}
	
	@Override
	protected Search<DadosNotificacao> searchPenalidade(SearchCriterios searchCriterios) throws Exception {
        return searchBuilder(searchCriterios)
        		.addField(" A.vl_multa, S.id_ait AS id_ait_geradora, S.ds_local_infracao AS ds_local_infracao_geradora, S.dt_infracao AS dt_infracao_geradora, "
                        + " S.vl_multa as vl_multa_geradora, S.vl_velocidade_permitida AS vl_velocidade_permitida_geradora, S.vl_velocidade_aferida AS vl_velocidade_aferida_geradora, "
                        + " S.vl_velocidade_penalidade AS vl_velocidade_penalidade_geradora, S.vl_velocidade_penalidade AS vl_velocidade_penalidade_geradora, "
                        + " T.nr_cod_detran AS nr_cod_detran_geradora, T.nr_paragrafo AS nr_paragrafo_geradora, T.nr_alinea AS nr_alinea_geradora, "
                        + " T.nm_natureza AS nm_natureza_geradora, T.nr_pontuacao AS nr_pontuacao_geradora, T.nr_artigo AS nr_artigo_geradora, "
                        + " T.nr_inciso AS nr_inciso_geradora, T.ds_infracao AS ds_infracao_geradora ")
                .addJoinTable(" LEFT OUTER JOIN mob_ait S ON (A.cd_ait_origem = S.cd_ait)")
                .addJoinTable(" LEFT OUTER JOIN mob_infracao T ON (S.cd_infracao = T.cd_infracao)")
                .build();
    }

	@Override
	protected String getReportPath() {
		return "mob/nic_nip_juros";
	}
	
}
