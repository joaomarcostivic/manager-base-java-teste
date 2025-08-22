package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class ConcessaoCirculoServices {

	public static Result save(ConcessaoCirculo concessaoCirculo){
		return save(concessaoCirculo, null, null);
	}

	public static Result save(ConcessaoCirculo concessaoCirculo, AuthData authData){
		return save(concessaoCirculo, authData, null);
	}

	public static Result save(ConcessaoCirculo concessaoCirculo, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(concessaoCirculo==null)
				return new Result(-1, "Erro ao salvar. ConcessaoCirculo é nulo");

			int retorno;
			if(ConcessaoCirculoDAO.get(concessaoCirculo.getCdConcessao(), concessaoCirculo.getCdCirculo())==null){
				retorno = ConcessaoCirculoDAO.insert(concessaoCirculo, connect);
				concessaoCirculo.setCdConcessao(retorno);
			}
			else {
				retorno = ConcessaoCirculoDAO.update(concessaoCirculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONCESSAOCIRCULO", concessaoCirculo);
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
	public static Result remove(ConcessaoCirculo concessaoCirculo) {
		return remove(concessaoCirculo.getCdConcessao(), concessaoCirculo.getCdCirculo());
	}
	public static Result remove(int cdConcessao, int cdCirculo){
		return remove(cdConcessao, cdCirculo, false, null, null);
	}
	public static Result remove(int cdConcessao, int cdCirculo, boolean cascade){
		return remove(cdConcessao, cdCirculo, cascade, null, null);
	}
	public static Result remove(int cdConcessao, int cdCirculo, boolean cascade, AuthData authData){
		return remove(cdConcessao, cdCirculo, cascade, authData, null);
	}
	public static Result remove(int cdConcessao, int cdCirculo, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ConcessaoCirculoDAO.delete(cdConcessao, cdCirculo, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_circulo");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_concessao_circulo A, mob_concessao B, acd_circulo C WHERE A.cd_concessao = B.cd_concessao AND A.cd_circulo = C.cd_circulo ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}