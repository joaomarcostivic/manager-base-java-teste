package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import sol.dao.*;
import java.util.*;

import com.tivic.sol.connection.Conexao;

public class ArquivoRegistroServices	{

	public static int insert(ArquivoRegistro objeto, Connection connect)	{
		return ArquivoRegistroDAO.insert(objeto, connect);
	}

	public static ResultSetMap getRegistrosOfContaReceber(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.nm_tipo_registro, B.id_tipo_registro " +
					"FROM grl_arquivo_registro A " +
					"LEFT OUTER JOIN grl_arquivo_tipo_registro B ON (A.cd_tipo_registro = B.cd_tipo_registro) " +
					"WHERE A.cd_conta_receber = "+cdContaReceber);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ArquivoRegistroServices.getRegistrosOfContaReceber: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findRegistroContaReceber(ArrayList<sol.dao.ItemComparator> criterios){
		return findRegistroContaReceber(criterios, null);
	}

	public static ResultSetMap findRegistroContaReceber(ArrayList<sol.dao.ItemComparator> criterios, Connection connect){
		String sql = "SELECT A.*, B.nm_tipo_registro, B.id_tipo_registro, C.*, " +
				     "       D.nm_pessoa " +
					 "FROM grl_arquivo_registro A " +
					 "LEFT OUTER JOIN grl_arquivo_tipo_registro B ON (A.cd_tipo_registro = B.cd_tipo_registro) "+
					 "JOIN adm_conta_receber C ON (A.cd_conta_receber = C.cd_conta_receber) "+
					 "JOIN grl_pessoa        D ON (C.cd_pessoa = D.cd_pessoa)";
		return Search.find(sql, "ORDER BY nm_tipo_registro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null, true);
	}

	public static int deleteAllOfContaReceber(int cdContaReceber) {
		return deleteAllOfContaReceber(cdContaReceber, null);
	}

	public static int deleteAllOfContaReceber(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM grl_arquivo_registro " +
					"WHERE cd_conta_receber=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaServices.deleteAllOfContaReceber: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}