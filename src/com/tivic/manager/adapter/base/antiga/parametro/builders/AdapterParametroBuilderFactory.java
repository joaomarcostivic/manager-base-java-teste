package com.tivic.manager.adapter.base.antiga.parametro.builders;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOld;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.parametro.enums.TipoDadoParametroEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AdapterParametroBuilderFactory {
	public ParametroOld<?> getStrategy(Parametro parametro) throws ValidacaoException {
		if (parametro.getTpDado() == TipoDadoParametroEnum.NUMERICO.getKey()) {
			return new DirectorParametroOld().toBaseAntigaAsInt(parametro);
		}
		else if (parametro.getTpDado() == TipoDadoParametroEnum.STRING.getKey()) {
			return new DirectorParametroOld().toBaseAntigaAsString(parametro);
		}
		else if (parametro.getTpDado() == TipoDadoParametroEnum.BYTE.getKey()) {
			return new DirectorParametroOld().toBaseAntigaAsByte(parametro);
		}
		else 
			throw new ValidacaoException("Tipo de dado não definido");
		
	}
}
