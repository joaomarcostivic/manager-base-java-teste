package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.ItemComparator;
import sol.util.Result;

public class RegiaoServices 	{
	public static final int _PAISES 	 = 0;
	public static final int _ESTADOS 	 = 1;
	public static final int _CIDADES 	 = 2;
	public static final int _LOGRADOUROS = 3;
	public static final int _BAIRRO      = 4;
	
	
	public static Result save(Regiao regiao){
		return save(regiao, null);
	}
	
	public static Result save(Regiao regiao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(regiao==null)
				return new Result(-1, "Erro ao salvar. Região é nula");
			
			int retorno;
			if(regiao.getCdRegiao()==0){
				retorno = RegiaoDAO.insert(regiao, connect);
				regiao.setCdRegiao(retorno);
			}
			else {
				retorno = RegiaoDAO.update(regiao, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "REGIAO", regiao);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdRegiao){
		return remove(cdRegiao, false, null);
	}
	
	public static Result remove(int cdRegiao, boolean cascade){
		return remove(cdRegiao, cascade, null);
	}
	
	public static Result remove(int cdRegiao, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
				
			if(!cascade || retorno>0)
				retorno = RegiaoDAO.delete(cdRegiao, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este região está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Região excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir região!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	

	public static final String[] tipoRegiao = {"Países","Estados","Cidades","Logradouros","Bairros"};

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_regiao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	// PAISES
	public static ResultSetMap findPaises(ArrayList<ItemComparator> criterios) {
		return findPaises(criterios, null);
	}

	public static ResultSetMap findPaises(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_regiao WHERE tp_regiao = " + _PAISES, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getPaisesOfRegiao(int cdRegiao) {
		return getPaisesOfRegiao(cdRegiao, null);
	}

	public static ResultSetMap getPaisesOfRegiao(int cdRegiao, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_regiao", String.valueOf(cdRegiao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return PaisServices.find(criterios, connect);
	}

	// ESTADOS
	public static ResultSetMap findEstados(ArrayList<ItemComparator> criterios) {
		return findEstados(criterios, null);
	}

	public static ResultSetMap findEstados(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_regiao WHERE tp_regiao = " + _ESTADOS, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getEstadosOfRegiao(int cdRegiao) {
		return getEstadosOfRegiao(cdRegiao, null);
	}

	public static ResultSetMap getEstadosOfRegiao(int cdRegiao, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_regiao", String.valueOf(cdRegiao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return EstadoServices.find(criterios, connect);
	}

	// CIDADES
	public static ResultSetMap findCidades(ArrayList<ItemComparator> criterios) {
		return findCidades(criterios, null);
	}

	public static ResultSetMap findCidades(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_regiao WHERE tp_regiao = " + _CIDADES, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getCidadesOfRegiao(int cdRegiao) {
		return getCidadesOfRegiao(cdRegiao, null);
	}

	public static ResultSetMap getCidadesOfRegiao(int cdRegiao, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_regiao", String.valueOf(cdRegiao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return CidadeServices.find(criterios, connect);
	}

	// LOGRADOUROS
	public static ResultSetMap findLogradouros(ArrayList<ItemComparator> criterios) {
		return findLogradouros(criterios, null);
	}

	public static ResultSetMap findLogradouros(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_regiao WHERE tp_regiao = " + _LOGRADOUROS, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getLogradourosOfRegiao(int cdRegiao) {
		return getLogradourosOfRegiao(cdRegiao, null);
	}

	public static ResultSetMap getLogradourosOfRegiao(int cdRegiao, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_regiao", String.valueOf(cdRegiao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return LogradouroServices.find(criterios, connect);
	}

	// BAIRROS
	public static ResultSetMap findBairros(ArrayList<ItemComparator> criterios) {
		return findBairros(criterios, null);
	}

	public static ResultSetMap findBairros(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_regiao WHERE tp_regiao = " + _BAIRRO, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getBairrosOfRegiao(int cdRegiao) {
		return getBairrosOfRegiao(cdRegiao, null);
	}

	public static ResultSetMap getBairrosOfRegiao(int cdRegiao, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_regiao", String.valueOf(cdRegiao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return BairroServices.find(criterios, connect);
	}

	public static ResultSetMap getAll() {
		return getAll(-1, null);
	}

	public static ResultSetMap getAll(int tpRegiao) {
		return getAll(tpRegiao, null);
	}

	public static ResultSetMap getAll(int tpRegiao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_regiao " +
					                         (tpRegiao >= 0 ? " WHERE tp_regiao = " + tpRegiao : "")+
					                         " ORDER BY tp_regiao, nm_regiao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegiaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}