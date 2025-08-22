package com.tivic.manager.mob;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import sol.dao.ItemComparator;

public class AitReportGetCriteriosCondutor {
	AitReportValidatorsNAI validators = new AitReportValidatorsNAI();
	
	public ArrayList<ItemComparator> getCriteriosCondutor(int cdAit) throws ValidacaoException, SQLException {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if(validators.verificaPrimeiraVia(cdAit, null))
		{
			criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			return criterios;
		}
		else
			throw new ValidacaoException("Não a NAI emitida para este AIT");
	}
}
