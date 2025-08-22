package com.tivic.manager.mob.lotes.service.impressao.consulta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;

import sol.dao.ItemComparator;

public interface IListAitsCandidatosNai {
	List<Ait> build(ArrayList<ItemComparator> criterios, Connection connect) throws IllegalArgumentException, SQLException, Exception;

}
