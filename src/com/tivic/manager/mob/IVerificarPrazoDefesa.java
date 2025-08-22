package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IVerificarPrazoDefesa {
	void verificarPrazoDefesa(int cdAit, GregorianCalendar dtEntreguePublicado, Connection connect) throws ValidacaoException;
}
