package com.tivic.manager.fta.tipoveiculo;

import com.tivic.manager.fta.TipoVeiculo;

public interface ITipoVeiculoService {
	public TipoVeiculo getByNmTipoVeiculo(String nmTipoVeiculo) throws Exception;
}
