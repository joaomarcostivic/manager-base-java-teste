package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class VistoriaPlanoItemDefeitoServices {

	public static Result save(VistoriaPlanoItemDefeito vistoriaPlanoItemDefeito){
		return save(vistoriaPlanoItemDefeito, null);
	}

	public static Result save(VistoriaPlanoItemDefeito vistoriaPlanoItemDefeito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(vistoriaPlanoItemDefeito==null)
				return new Result(-1, "Erro ao salvar. VistoriaPlanoItemDefeito é nulo");

			int retorno;
			if(vistoriaPlanoItemDefeito.getCdVistoriaPlanoItemDefeito()==0){
				retorno = VistoriaPlanoItemDefeitoDAO.insert(vistoriaPlanoItemDefeito, connect);
				vistoriaPlanoItemDefeito.setCdVistoriaPlanoItemDefeito(retorno);
			}
			else {
				retorno = VistoriaPlanoItemDefeitoDAO.update(vistoriaPlanoItemDefeito, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VISTORIAPLANOITEMDEFEITO", vistoriaPlanoItemDefeito);
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
	public static Result remove(int cdVistoriaPlanoItemDefeito){
		return remove(cdVistoriaPlanoItemDefeito, false, null);
	}
	public static Result remove(int cdVistoriaPlanoItemDefeito, boolean cascade){
		return remove(cdVistoriaPlanoItemDefeito, cascade, null);
	}
	public static Result remove(int cdVistoriaPlanoItemDefeito, boolean cascade, Connection connect){
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
			retorno = VistoriaPlanoItemDefeitoDAO.delete(cdVistoriaPlanoItemDefeito, connect);
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
	
	public static Result removeAll(int cdVistoria, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			retorno = connect.prepareStatement(
					" DELETE FROM mob_vistoria_plano_item_defeito "+
					" WHERE cd_vistoria_plano_item IN "+
					"(  SELECT cd_vistoria_plano_item FROM "+
					"          mob_vistoria_plano_item "+
					"          WHERE cd_vistoria = "+cdVistoria+" ) "
					).executeUpdate();
			
			
			if(retorno<0){
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_vistoria_plano_item_defeito");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VistoriaPlanoItemDefeitoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_vistoria_plano_item_defeito A "+
							" JOIN mob_defeito_vistoria_item B on (A.cd_defeito_vistoria_item = B.cd_defeito_vistoria_item ) ",
							criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
