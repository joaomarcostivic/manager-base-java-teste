package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoRecursoInterligacaoComputadoresDAO{

	public static int insert(TipoRecursoInterligacaoComputadores objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoRecursoInterligacaoComputadores objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_tipo_recurso_interligacao_computadores", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoRecursoInterligacaoComputadores(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_tipo_recurso_interligacao_computadores (cd_tipo_recurso_interligacao_computadores,"+
			                                  "nm_tipo_recurso_interligacao_computadores,"+
			                                  "id_tipo_recurso_interligacao_computadores,"+
			                                  "st_tipo_recurso_interligacao_computadores) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoRecursoInterligacaoComputadores());
			pstmt.setString(3,objeto.getIdTipoRecursoInterligacaoComputadores());
			pstmt.setInt(4,objeto.getStTipoRecursoInterligacaoComputadores());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoRecursoInterligacaoComputadores objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoRecursoInterligacaoComputadores objeto, int cdTipoRecursoInterligacaoComputadoresOld) {
		return update(objeto, cdTipoRecursoInterligacaoComputadoresOld, null);
	}

	public static int update(TipoRecursoInterligacaoComputadores objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoRecursoInterligacaoComputadores objeto, int cdTipoRecursoInterligacaoComputadoresOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_tipo_recurso_interligacao_computadores SET cd_tipo_recurso_interligacao_computadores=?,"+
												      		   "nm_tipo_recurso_interligacao_computadores=?,"+
												      		   "id_tipo_recurso_interligacao_computadores=?,"+
												      		   "st_tipo_recurso_interligacao_computadores=? WHERE cd_tipo_recurso_interligacao_computadores=?");
			pstmt.setInt(1,objeto.getCdTipoRecursoInterligacaoComputadores());
			pstmt.setString(2,objeto.getNmTipoRecursoInterligacaoComputadores());
			pstmt.setString(3,objeto.getIdTipoRecursoInterligacaoComputadores());
			pstmt.setInt(4,objeto.getStTipoRecursoInterligacaoComputadores());
			pstmt.setInt(5, cdTipoRecursoInterligacaoComputadoresOld!=0 ? cdTipoRecursoInterligacaoComputadoresOld : objeto.getCdTipoRecursoInterligacaoComputadores());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoRecursoInterligacaoComputadores) {
		return delete(cdTipoRecursoInterligacaoComputadores, null);
	}

	public static int delete(int cdTipoRecursoInterligacaoComputadores, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_tipo_recurso_interligacao_computadores WHERE cd_tipo_recurso_interligacao_computadores=?");
			pstmt.setInt(1, cdTipoRecursoInterligacaoComputadores);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoRecursoInterligacaoComputadores get(int cdTipoRecursoInterligacaoComputadores) {
		return get(cdTipoRecursoInterligacaoComputadores, null);
	}

	public static TipoRecursoInterligacaoComputadores get(int cdTipoRecursoInterligacaoComputadores, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_interligacao_computadores WHERE cd_tipo_recurso_interligacao_computadores=?");
			pstmt.setInt(1, cdTipoRecursoInterligacaoComputadores);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoRecursoInterligacaoComputadores(rs.getInt("cd_tipo_recurso_interligacao_computadores"),
						rs.getString("nm_tipo_recurso_interligacao_computadores"),
						rs.getString("id_tipo_recurso_interligacao_computadores"),
						rs.getInt("st_tipo_recurso_interligacao_computadores"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_recurso_interligacao_computadores");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoRecursoInterligacaoComputadores> getList() {
		return getList(null);
	}

	public static ArrayList<TipoRecursoInterligacaoComputadores> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoRecursoInterligacaoComputadores> list = new ArrayList<TipoRecursoInterligacaoComputadores>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoRecursoInterligacaoComputadores obj = TipoRecursoInterligacaoComputadoresDAO.get(rsm.getInt("cd_tipo_recurso_interligacao_computadores"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoRecursoInterligacaoComputadoresDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_recurso_interligacao_computadores", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}