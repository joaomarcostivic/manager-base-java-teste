package com.tivic.manager.adapter.base.antiga.parametro.builders;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOld;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class ParametroOldBuilder<T> {
	ParametroOld<T> parametroOld;
	
	public ParametroOldBuilder() throws ValidacaoException {
		this.parametroOld = new ParametroOld<T>();
	}
	
	public ParametroOldBuilder<T> setNmParametro(String nmParametro) {
		this.parametroOld.setNmParametro(nmParametro);
		return this;
	}
	
	public ParametroOldBuilder<T> setTpDadoParametro(String tpDadoParametro) {
		this.parametroOld.setTpDadoParametro(tpDadoParametro);
		return this;
	}
	
	public ParametroOldBuilder<T> setVlParametro(T valorParametro) {
		this.parametroOld.setValorParametro(valorParametro);
		return this;
	}
	
	public ParametroOld<T> build() {
		return this.parametroOld;
	}
}
