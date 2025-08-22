package com.tivic.manager.mob;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AitReportDataJariNIP {

	public IAitReportCalcularPrazoNIP getEstrategiaJariNIP(String estado) throws ValidacaoException
	{
		
		switch(estado)
		{
			case "BA":
				return new AitReportPrazoJariBA();
			case "MG":
				return new AitReportPrazoJariMG();
			default:
				throw new ValidacaoException ("Calculo de recurso JARI não implementado para este estado, verifique com a equipe de desenvolvimento.");
		}
		
	}
	
}
