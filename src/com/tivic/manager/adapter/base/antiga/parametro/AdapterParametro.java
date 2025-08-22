package com.tivic.manager.adapter.base.antiga.parametro;

import com.tivic.manager.adapter.base.antiga.parametro.builders.AdapterParametroBuilderFactory;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroValor;
import com.tivic.manager.grl.parametro.builders.ParametroBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterParametro {
	
	public ParametroOld<?> toOld(Parametro parametro) throws ValidacaoException {
		ParametroOld<?> parametroOld = new AdapterParametroBuilderFactory().getStrategy(parametro);
		return parametroOld;
	}
	
	public Parametro toBaseNova(ParametroOld<?> parametroOld) {
		Parametro parametro = new ParametroBuilder()
				.setNmParametro(parametroOld.getNmParametro())
				.setValores(setValor(String.valueOf(parametroOld.getValorParametro())))
				.build();
		return parametro;
	}
	
	private ParametroValor[] setValor(String vlParametro) {
	    ParametroValor parametroValor = new ParametroValor();
	    parametroValor.setVlInicial(vlParametro);
	    ParametroValor[] parametroValorArray = { parametroValor };
	    return parametroValorArray;
	}

}
