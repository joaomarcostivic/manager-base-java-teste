package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class ProcessoServices {

	public static final int NUM_INCREMENTAL_SEM_PREFIXO = 0;
	public static final int NUM_INCREMENTAL_COM_PREXIFO = 1;
	public static final int NUM_MASCARA = 2;

	public static final String[] tiposNumeracao = {"Incremental (sem prefixo)", "Incremental (com prefixo)", "Máscara"};

	public static ResultSetMap getAllAtividades(int cdProcesso) {
		return getAllAtividades(cdProcesso, null);
	}

	public static ResultSetMap getAllAtividades(int cdProcesso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* FROM grl_processo_atividade A " +
					                                              "WHERE cd_processo = "+cdProcesso);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}

	public static ResultSetMap getAll(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			return com.tivic.manager.grl.ProcessoDAO.find(criterios);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(Processo objeto) {
		return update(objeto, null);
	}

	public static int update(Processo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			Processo processo = ProcessoDAO.get(objeto.getCdProcesso(), connect);
			objeto.setNrUltimaNumeracao(processo.getNrUltimaNumeracao());
			return ProcessoDAO.update(objeto, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
