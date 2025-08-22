package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class InfracaoMotivoServices {

	public static Result save(InfracaoMotivo infracaoMotivo){
		return save(infracaoMotivo, null);
	}

	public static Result save(InfracaoMotivo infracaoMotivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(infracaoMotivo==null)
				return new Result(-1, "Erro ao salvar. InfracaoMotivo é nulo");

			int retorno;
			InfracaoMotivo motivo = InfracaoMotivoDAO.get( infracaoMotivo.getCdInfracao(), infracaoMotivo.getCdMotivo());
			if( motivo==null ){
				retorno = InfracaoMotivoDAO.insert(infracaoMotivo, connect);
				infracaoMotivo.setCdInfracao(retorno);
			}else {
				retorno = InfracaoMotivoDAO.update(infracaoMotivo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INFRACAOMOTIVO", infracaoMotivo);
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
	public static Result remove(int cdInfracao, int cdMotivo){
		return remove(cdInfracao, cdMotivo, false, null);
	}
	public static Result remove(int cdInfracao, int cdMotivo, boolean cascade){
		return remove(cdInfracao, cdMotivo, cascade, null);
	}
	public static Result remove(int cdInfracao, int cdMotivo, boolean cascade, Connection connect){
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
			retorno = InfracaoMotivoDAO.delete(cdInfracao, cdMotivo, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_infracao_motivo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoMotivoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String orderBy = "";
		
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
				orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				criterios.remove(i);
				i--;
			}
		}
		
		return Search.find("SELECT A.* FROM mob_infracao_motivo A", orderBy, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_infracao_motivo";
						
			pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
