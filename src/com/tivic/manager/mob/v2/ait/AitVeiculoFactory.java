package com.tivic.manager.mob.v2.ait;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitVeiculo;

public class AitVeiculoFactory {

	public static AitVeiculo factory(Ait ait) {
		AitVeiculo aitVeiculo = new AitVeiculo();
		aitVeiculo.setCdCor(ait.getCdCorVeiculo());
		aitVeiculo.setCdTipo(ait.getCdTipoVeiculo());
		aitVeiculo.setCdEspecie(ait.getCdEspecieVeiculo());
		aitVeiculo.setCdCategoria(ait.getCdCategoriaVeiculo());
		aitVeiculo.setCdMarca(ait.getCdMarcaVeiculo());
		aitVeiculo.setDsAnoFabricacao(ait.getNrAnoFabricacao());
		aitVeiculo.setDsAnoModelo(ait.getNrAnoFabricacao());
		aitVeiculo.setUfVeiculo(ait.getSgUfVeiculo());
		return aitVeiculo;
	}
}
