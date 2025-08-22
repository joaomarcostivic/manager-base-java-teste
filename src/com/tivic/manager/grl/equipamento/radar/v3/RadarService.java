package com.tivic.manager.grl.equipamento.radar.v3;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class RadarService implements IRadarService {
	
	@Override
	public List<RadarLocalizacaoDTO> findRadar() throws Exception{
		List<RadarLocalizacaoDTO> radarLocalizacao = new ArrayList<RadarLocalizacaoDTO>();
		radarLocalizacao = findRadar(new CustomConnection());
		if (radarLocalizacao.isEmpty()) {
	        throw new NoContentException("Nenhum radar encontrado.");
	    }
		return radarLocalizacao;
	}
	
	@Override
	public List<RadarLocalizacaoDTO> findRadar(CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			SearchCriterios searchCriterios = new SearchCriterios();
			Search<RadarLocalizacaoDTO> search = new SearchBuilder<RadarLocalizacaoDTO>("grl_equipamento A")
					.fields("A.cd_equipamento, A.nm_equipamento, A.ds_local, C.nr_faixa")
					.addJoinTable("LEFT OUTER JOIN mob_via B ON (A.ds_local = B.nm_via)")
					.addJoinTable("LEFT OUTER JOIN mob_faixa C ON (B.cd_via = C.cd_via)")
	        		.searchCriterios(searchCriterios)
	        		.additionalCriterias("A.tp_equipamento IN (" + EquipamentoEnum.RADAR_FIXO.getKey()+ ", " + EquipamentoEnum.RADAR_MOVEL.getKey() + ", " + EquipamentoEnum.RADAR_ESTATICO.getKey() + ")")
	        		.orderBy("B.cd_via, C.nr_faixa")
					.count()
				.build();
				
		    customConnection.finishConnection();
		    return search.getList(RadarLocalizacaoDTO.class);
		} finally {
			customConnection.closeConnection();
		}
	}
}
