package com.tivic.manager.grl.equipamento.radar.v3;

import java.util.List;

import com.tivic.sol.connection.CustomConnection;

public interface IRadarService {
	public List<RadarLocalizacaoDTO> findRadar() throws Exception;
	public List<RadarLocalizacaoDTO> findRadar(CustomConnection customConnection) throws Exception;
}
