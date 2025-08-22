package com.tivic.manager.mob.v2.ait;

import org.json.JSONObject;

public class InformacoesVeiculoDTOMapper {

	public static InformacoesVeiculoDTO map(JSONObject jsonVeiculo) throws Exception{
		return new InformacoesVeiculoDTO(
			jsonVeiculo.has("codMunicipio") ? jsonVeiculo.get("codMunicipio").toString() : null,
			jsonVeiculo.has("nmMunicipio") ? jsonVeiculo.get("nmMunicipio").toString() : null,
			jsonVeiculo.has("sgEstado") ? jsonVeiculo.get("sgEstado").toString() : null
		);
	}
	
}
