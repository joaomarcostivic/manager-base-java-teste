package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DeclaranteServices {
	
	public static final int TP_CONDUTOR = 1;
	public static final int TP_CONDUTOR_PROPRIETARIO = 2;
	public static final int TP_PROPRIETARIO = 3;
	public static final int TP_TERCEIRO = 4;
	public static final String[] tpRelacao = {"Condutor", "Condutor e Proprietário", "Proprietário", "Terceiro Envolvido"};

	public static final String[] tpSexo = {"Masculino", "Feminino", "Não informado"};
	
	public static Result save(Declarante declarante){
		return save(declarante, null, null);
	}

	public static Result save(Declarante declarante, AuthData authData){
		return save(declarante, authData, null);
	}

	public static Result save(Declarante declarante, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(declarante==null)
				return new Result(-1, "Erro ao salvar. Declarante é nulo");

			int retorno;
			
			// TODO: validar se já existe
			
			if(declarante.getCdDeclarante()==0){
				retorno = DeclaranteDAO.insert(declarante, connect);
				declarante.setCdDeclarante(retorno);
			}
			else {
				retorno = DeclaranteDAO.update(declarante, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DECLARANTE", declarante);
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
	public static Result remove(Declarante declarante) {
		return remove(declarante.getCdDeclarante());
	}
	public static Result remove(int cdDeclarante){
		return remove(cdDeclarante, false, null, null);
	}
	public static Result remove(int cdDeclarante, boolean cascade){
		return remove(cdDeclarante, cascade, null, null);
	}
	public static Result remove(int cdDeclarante, boolean cascade, AuthData authData){
		return remove(cdDeclarante, cascade, authData, null);
	}
	public static Result remove(int cdDeclarante, boolean cascade, AuthData authData, Connection connect){
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
			retorno = DeclaranteDAO.delete(cdDeclarante, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_declarante");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_declarante", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	

	public static Declarante get(int cdBoat) {
		return get(cdBoat, null);
	}

	public static Declarante get(int cdBoat, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			Declarante declarante = null;
			ResultSet rs = null;
			
			pstmt = connect.prepareStatement(
					" SELECT A.cd_declarante FROM mob_declarante A"
					+ " JOIN mob_boat_declarante B ON (A.cd_declarante = B.cd_declarante)"
					+ " WHERE B.cd_boat = ?");
			pstmt.setInt(1, cdBoat);
			
			if((rs = pstmt.executeQuery()).next()) {
				declarante = DeclaranteDAO.get(rs.getInt("cd_declarante"), connect);
			}
			
			return declarante;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteServices.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaranteServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
