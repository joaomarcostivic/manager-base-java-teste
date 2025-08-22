package com.tivic.manager.adapter.base.antiga.parametro.builders;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOld;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IDirectorParametroOld {
	ParametroOld<Integer> toBaseAntigaAsInt(Parametro parametro) throws ValidacaoException;
	ParametroOld<String> toBaseAntigaAsString(Parametro parametro) throws ValidacaoException;
	ParametroOld<byte[]> toBaseAntigaAsByte(Parametro parametro) throws ValidacaoException;
}
