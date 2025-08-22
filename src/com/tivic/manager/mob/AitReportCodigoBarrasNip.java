package com.tivic.manager.mob;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AitReportCodigoBarrasNip {
	public IAitReportCriarCodigoBarrasNip getEstrategiaCodigoBarras (String estado) throws ValidacaoException
	{

		switch (estado)
		{
			case "BA":
			case "MG":
				return new AitReportCriarCodigoBarras();
			default:
				throw new ValidacaoException ("Código de barras não implementado para este estado, verifique com a equipe de desenvolvimento.");
		}
		
	}
}
