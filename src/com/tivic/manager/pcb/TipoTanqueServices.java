package com.tivic.manager.pcb;

import java.util.ArrayList;

import com.tivic.manager.amf.DestinationConfig;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class TipoTanqueServices {

	public static ResultSetMap getAllTipoTanqueAtivo()
	{
		ItemComparator item = new ItemComparator("st_tipo_tanque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(item);
		return TipoTanqueDAO.find(criterios);
	}
	
	public static ResultSetMap getAll()
	{
		return TipoTanqueDAO.getAll();
	}
	
	
}