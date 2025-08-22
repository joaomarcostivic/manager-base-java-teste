package com.tivic.manager.mob.funset.lists;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.mob.funset.FunsetParametrosEntrada;
import com.tivic.manager.mob.funset.parts.FunsetCampo;
import com.tivic.manager.mob.funset.parts.FunsetResumo;

public interface FunsetList {
	public List<List<FunsetCampo>> build(FunsetParametrosEntrada funsetParametrosEntrada, FunsetResumo funsetResumo) throws SQLException, Exception;
}
