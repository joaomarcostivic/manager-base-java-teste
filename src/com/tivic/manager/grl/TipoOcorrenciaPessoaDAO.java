package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoOcorrenciaPessoaDAO{

	public static int insert(TipoOcorrenciaPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoOcorrenciaPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = TipoOcorrenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_ocorrencia_pessoa (cd_tipo_ocorrencia,"+
			                                  "nm_tipo_ocorrencia,"+
			                                  "id_tipo_ocorrencia,"+
			                                  "lg_email) VALUES (?, ?, ?, ?)");
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoOcorrencia());
			pstmt.setString(2,objeto.getNmTipoOcorrencia());
			pstmt.setString(3,objeto.getIdTipoOcorrencia());
			pstmt.setInt(4,objeto.getLgEmail());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoOcorrenciaPessoa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoOcorrenciaPessoa objeto, int cdTipoOcorrenciaOld) {
		return update(objeto, cdTipoOcorrenciaOld, null);
	}

	public static int update(TipoOcorrenciaPessoa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoOcorrenciaPessoa objeto, int cdTipoOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			TipoOcorrenciaPessoa objetoTemp = get(objeto.getCdTipoOcorrencia(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO grl_tipo_ocorrencia_pessoa (cd_tipo_ocorrencia,"+
			                                  "nm_tipo_ocorrencia,"+
			                                  "id_tipo_ocorrencia,"+
			                                  "lg_email) VALUES (?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE grl_tipo_ocorrencia_pessoa SET cd_tipo_ocorrencia=?,"+
												      		   "nm_tipo_ocorrencia=?,"+
												      		   "id_tipo_ocorrencia=?,"+
												      		   "lg_email=? WHERE cd_tipo_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdTipoOcorrencia());
			pstmt.setString(2,objeto.getNmTipoOcorrencia());
			pstmt.setString(3,objeto.getIdTipoOcorrencia());
			pstmt.setInt(4,objeto.getLgEmail());
			if (objetoTemp != null) {
				pstmt.setInt(5, cdTipoOcorrenciaOld!=0 ? cdTipoOcorrenciaOld : objeto.getCdTipoOcorrencia());
			}
			pstmt.executeUpdate();
			if (TipoOcorrenciaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_ocorrencia_pessoa WHERE cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			pstmt.executeUpdate();
			if (TipoOcorrenciaDAO.delete(cdTipoOcorrencia, connect)<=0) {
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
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoOcorrenciaPessoa get(int cdTipoOcorrencia) {
		return get(cdTipoOcorrencia, null);
	}

	public static TipoOcorrenciaPessoa get(int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_ocorrencia_pessoa A, grl_tipo_ocorrencia B WHERE A.cd_tipo_ocorrencia=B.cd_tipo_ocorrencia AND A.cd_tipo_ocorrencia=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoOcorrenciaPessoa(rs.getInt("cd_tipo_ocorrencia"),
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
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_ocorrencia_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoOcorrenciaPessoa> getList() {
		return getList(null);
	}

	public static ArrayList<TipoOcorrenciaPessoa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoOcorrenciaPessoa> list = new ArrayList<TipoOcorrenciaPessoa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoOcorrenciaPessoa obj = TipoOcorrenciaPessoaDAO.get(rsm.getInt("cd_tipo_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_ocorrencia_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
