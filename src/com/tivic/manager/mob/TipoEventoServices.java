package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class TipoEventoServices {

	public static Result save(TipoEvento tipoEvento){
		return save(tipoEvento, null, null);
	}

	public static Result save(TipoEvento tipoEvento, AuthData authData){
		return save(tipoEvento, authData, null);
	}

	public static Result save(TipoEvento tipoEvento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoEvento==null)
				return new Result(-1, "Erro ao salvar. TipoEvento é nulo");

			int retorno;
			if(tipoEvento.getCdTipoEvento()==0){
				retorno = TipoEventoDAO.insert(tipoEvento, connect);
				tipoEvento.setCdTipoEvento(retorno);
			}
			else {
				retorno = TipoEventoDAO.update(tipoEvento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOEVENTO", tipoEvento);
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
	public static Result remove(TipoEvento tipoEvento) {
		return remove(tipoEvento.getCdTipoEvento());
	}
	public static Result remove(int cdTipoEvento){
		return remove(cdTipoEvento, false, null, null);
	}
	public static Result remove(int cdTipoEvento, boolean cascade){
		return remove(cdTipoEvento, cascade, null, null);
	}
	public static Result remove(int cdTipoEvento, boolean cascade, AuthData authData){
		return remove(cdTipoEvento, cascade, authData, null);
	}
	public static Result remove(int cdTipoEvento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoEventoDAO.delete(cdTipoEvento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_evento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoEvento getByIdTipoEvento(String idTipoEvento) {
		return getByIdTipoEvento(idTipoEvento, null);
	}

	public static TipoEvento getByIdTipoEvento(String idTipoEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_evento WHERE id_tipo_evento=?");
			pstmt.setString(1, idTipoEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoEvento(rs.getInt("cd_tipo_evento"),
						rs.getString("nm_tipo_evento"),
						rs.getString("id_tipo_evento"),
						rs.getString("txt_tipo_evento"),
						rs.getInt("cd_infracao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoServices.getByIdTipoEvento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEventoServices.getByIdTipoEvento: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_evento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}