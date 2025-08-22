package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class MatriculaTipoDocumentacaoDAO{

	public static int insert(MatriculaTipoDocumentacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaTipoDocumentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_tipo_documentacao (cd_matricula,"+
			                                  "cd_tipo_documentacao,"+
			                                  "lg_apresentacao,"+
			                                  "blb_copia,"+
			                                  "dt_apresentacao) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdTipoDocumentacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumentacao());
			pstmt.setInt(3,objeto.getLgApresentacao());
			if(objeto.getBlbCopia()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbCopia());
			if(objeto.getDtApresentacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtApresentacao().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaTipoDocumentacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaTipoDocumentacao objeto, int cdMatriculaOld, int cdTipoDocumentacaoOld) {
		return update(objeto, cdMatriculaOld, cdTipoDocumentacaoOld, null);
	}

	public static int update(MatriculaTipoDocumentacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaTipoDocumentacao objeto, int cdMatriculaOld, int cdTipoDocumentacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_tipo_documentacao SET cd_matricula=?,"+
												      		   "cd_tipo_documentacao=?,"+
												      		   "lg_apresentacao=?,"+
												      		   "blb_copia=?,"+
												      		   "dt_apresentacao=? WHERE cd_matricula=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdTipoDocumentacao());
			pstmt.setInt(3,objeto.getLgApresentacao());
			if(objeto.getBlbCopia()==null)
				pstmt.setNull(4, Types.BINARY);
			else
				pstmt.setBytes(4,objeto.getBlbCopia());
			if(objeto.getDtApresentacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtApresentacao().getTimeInMillis()));
			pstmt.setInt(6, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(7, cdTipoDocumentacaoOld!=0 ? cdTipoDocumentacaoOld : objeto.getCdTipoDocumentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdTipoDocumentacao) {
		return delete(cdMatricula, cdTipoDocumentacao, null);
	}

	public static int delete(int cdMatricula, int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_tipo_documentacao WHERE cd_matricula=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdTipoDocumentacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaTipoDocumentacao get(int cdMatricula, int cdTipoDocumentacao) {
		return get(cdMatricula, cdTipoDocumentacao, null);
	}

	public static MatriculaTipoDocumentacao get(int cdMatricula, int cdTipoDocumentacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_documentacao WHERE cd_matricula=? AND cd_tipo_documentacao=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdTipoDocumentacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaTipoDocumentacao(rs.getInt("cd_matricula"),
						rs.getInt("cd_tipo_documentacao"),
						rs.getInt("lg_apresentacao"),
						rs.getBytes("blb_copia")==null?null:rs.getBytes("blb_copia"),
						(rs.getTimestamp("dt_apresentacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_apresentacao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_documentacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaTipoDocumentacao> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaTipoDocumentacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaTipoDocumentacao> list = new ArrayList<MatriculaTipoDocumentacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaTipoDocumentacao obj = MatriculaTipoDocumentacaoDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_tipo_documentacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoDocumentacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_tipo_documentacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
