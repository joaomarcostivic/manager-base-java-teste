package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoOcorrenciaDAO{

	public static int insert(TipoOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.TipoOcorrenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_tipo_ocorrencia (cd_tipo_ocorrencia) VALUES (?)");
			
			System.out.println("tipoOcorrencia: "+objeto);
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoOcorrencia());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoOcorrencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoOcorrencia objeto, int cdTipoOcorrenciaOld) {
		return update(objeto, cdTipoOcorrenciaOld, null);
	}

	public static int update(TipoOcorrencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoOcorrencia objeto, int cdTipoOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			TipoOcorrencia objetoTemp = get(objeto.getCdTipoOcorrencia(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO agd_tipo_ocorrencia (cd_tipo_ocorrencia) VALUES (?)");
			else
				pstmt = connect.prepareStatement("UPDATE agd_tipo_ocorrencia SET cd_tipo_ocorrencia=? WHERE cd_tipo_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdTipoOcorrencia());
			if (objetoTemp != null) {
				pstmt.setInt(2, cdTipoOcorrenciaOld!=0 ? cdTipoOcorrenciaOld : objeto.getCdTipoOcorrencia());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.TipoOcorrenciaDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoOcorrencia) {
		return delete(cdTipoOcorrencia, null);
	}

	public static int delete(int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_tipo_ocorrencia WHERE cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.TipoOcorrenciaDAO.delete(cdTipoOcorrencia, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoOcorrencia get(int cdTipoOcorrencia) {
		return get(cdTipoOcorrencia, null);
	}

	public static TipoOcorrencia get(int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_tipo_ocorrencia A, grl_tipo_ocorrencia B WHERE A.cd_tipo_ocorrencia=B.cd_tipo_ocorrencia AND A.cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoOcorrencia(rs.getInt("cd_tipo_ocorrencia"),
						rs.getString("nm_tipo_ocorrencia"),
						rs.getString("id_tipo_ocorrencia"),
						rs.getInt("lg_email"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_tipo_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<TipoOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoOcorrencia> list = new ArrayList<TipoOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoOcorrencia obj = TipoOcorrenciaDAO.get(rsm.getInt("cd_tipo_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_tipo_ocorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
