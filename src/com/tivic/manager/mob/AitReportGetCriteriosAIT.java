package com.tivic.manager.mob;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import sol.dao.ItemComparator;

public class AitReportGetCriteriosAIT {

	public static ArrayList<ItemComparator> segundaVia(int cdAit) throws ValidacaoException, SQLException 
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;

	}
	
}
