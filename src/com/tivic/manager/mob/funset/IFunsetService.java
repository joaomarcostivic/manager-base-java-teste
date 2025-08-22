package com.tivic.manager.mob.funset;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.mob.funset.file.FunsetFile;

public interface IFunsetService {
	public List<FunsetAitDTO> findAits(FunsetParametrosEntrada funsetParametrosEntrada) throws Exception, SQLException;
	public FunsetFile generateArquivo(FunsetParametrosEntrada funsetParametrosEntrada) throws Exception, SQLException;
}
