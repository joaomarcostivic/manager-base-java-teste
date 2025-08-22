package com.tivic.manager.mob.funset;

import java.sql.SQLException;
import java.util.List;

import com.tivic.manager.mob.funset.file.FunsetFile;
import com.tivic.manager.mob.funset.file.FunsetFileBuilder;
import com.tivic.manager.mob.funset.parts.FunsetRegistro;
import com.tivic.manager.mob.funset.parts.FunsetRegistroBuilder;
import com.tivic.sol.cdi.BeansFactory;

public class FunsetService implements IFunsetService {
	
	FunsetBuscaAits funsetBuscaAits;
	
	public FunsetService() throws Exception {
		funsetBuscaAits = (FunsetBuscaAits)BeansFactory.get(FunsetBuscaAits.class);
	}
	
	public List<FunsetAitDTO> findAits(FunsetParametrosEntrada funsetParametrosEntrada) throws Exception, SQLException{
		return funsetBuscaAits.find(funsetParametrosEntrada);
	}
	
	public FunsetFile generateArquivo(FunsetParametrosEntrada funsetParametrosEntrada) throws Exception, SQLException{
		FunsetRegistro funsetRegistro = gerarFunsetRegistro(funsetParametrosEntrada);
		String textoArquivo = funsetRegistro.build();
		FunsetFile funsetFile = new FunsetFileBuilder(textoArquivo).build();
		return funsetFile;
	}
	
	private FunsetRegistro gerarFunsetRegistro(FunsetParametrosEntrada funsetParametrosEntrada) throws SQLException, Exception {
		return new FunsetRegistroBuilder(funsetParametrosEntrada).build();
	}
	
}
