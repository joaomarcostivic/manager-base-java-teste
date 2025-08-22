package com.tivic.manager.mob;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AitReportDataDefesaNAI {
	
	public IAitReportCalcularPrazoDefesaNAI getEstrategiaDefesaNAI(String estado) throws ValidacaoException
	{
		switch (estado)
		{
			case "BA":
				return new AitReportPrazoDefesaNaiBA();
			case "MG":
				return new AitReportPrazoDefesaNaiMG();
			default:
				throw new ValidacaoException ("Calculo de defesa NAI não implementado para este estado, verifique com a equipe de desenvolvimento.");
		}
	}

}
