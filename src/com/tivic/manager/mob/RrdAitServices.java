package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class RrdAitServices {

	public static Result save(int cdRrd, ArrayList<RrdAit> rrdAits) {
		return save(cdRrd, rrdAits, null);
	}

	public static Result save(int cdRrd, ArrayList<RrdAit> rrdAits, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int retorno = 0;
			for (RrdAit rrdAit: rrdAits) {
				rrdAit.setCdRrd(cdRrd);
				retorno = RrdAitDAO.insert(rrdAit, connect);
				
				if(retorno<=0)
					break;
			}
			
			if(retorno>0)
				return new Result(1, "RrdAit incluídos com sucesso.");
			else
				return new Result(-1, "Erro ao incluir RrdAit.");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir RrdAit.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(RrdAit rrdAit) {
		return save(rrdAit, null);
	}

	public static Result save(RrdAit rrdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			int retorno = 0;
			retorno = RrdAitDAO.insert(rrdAit, connect);
						
			if(retorno>0)
				return new Result(1, "RrdAit incluídos com sucesso.");
			else
				return new Result(-1, "Erro ao incluir RrdAit.");
				
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-2, "Erro ao incluir RrdAit.");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}