package com.tivic.manager.adapter.base.antiga.parametro.builders;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOld;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.parametro.enums.TipoDadoParametroEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class DirectorParametroOld implements IDirectorParametroOld{
	
	@Override
	public ParametroOld<Integer> toBaseAntigaAsInt(Parametro parametro) throws ValidacaoException {
		return new ParametroOldBuilder<Integer>()
				.setNmParametro(parametro.getNmParametro())
				.setTpDadoParametro("integer")
				.setVlParametro(parametro.getValores() == null ? null : Integer.valueOf(parametro.getValores()[0].getVlInicial()))
				.build();
	}
	
	@Override
	public ParametroOld<String> toBaseAntigaAsString(Parametro parametro) throws ValidacaoException {
		return new ParametroOldBuilder<String>()
				.setNmParametro(parametro.getNmParametro())
				.setTpDadoParametro("character varying")
				.setVlParametro(parametro.getValores() == null ? null : parametro.getValores()[0].getVlInicial())
				.build();
	}
	
	@Override
	public ParametroOld<byte[]> toBaseAntigaAsByte(Parametro parametro) throws ValidacaoException {
		return new ParametroOldBuilder<byte[]>()
				.setNmParametro(parametro.getNmParametro())
				.setTpDadoParametro("bytea")
				.setVlParametro(parametro.getValores() == null ? null : parametro.getValores()[0].getBlbValor())
				.build();
	}
	
}
