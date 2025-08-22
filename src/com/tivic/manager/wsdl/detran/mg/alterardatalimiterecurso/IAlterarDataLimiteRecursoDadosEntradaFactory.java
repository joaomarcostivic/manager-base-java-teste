package com.tivic.manager.wsdl.detran.mg.alterardatalimiterecurso;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IAlterarDataLimiteRecursoDadosEntradaFactory {
	AlterarDataLimiteRecursoDadosEntrada fazerDadoEntrada(Ait ait) throws ValidacaoException;
}
