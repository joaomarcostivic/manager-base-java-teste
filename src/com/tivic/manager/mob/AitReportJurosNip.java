package com.tivic.manager.mob;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class AitReportJurosNip {
	public IAitReportCalcularJurosNip getEstrategiaCalcularJuros(String estado) throws Exception {	
		
		switch (estado)
		{
			case "BA":
			case "MG":
				return new AitReportCalcularJurosNipMG();
			default:
				throw new ValidacaoException ("Calculo de juros não implementado para este estado, verifique com a equipe de desenvolvimento.");
		}
	}
}
