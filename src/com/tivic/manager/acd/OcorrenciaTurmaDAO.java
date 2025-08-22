package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OcorrenciaTurmaDAO{

	public static int insert(OcorrenciaTurma objeto) {
		return insert(objeto, null);
	}

	public static int insert(OcorrenciaTurma objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.OcorrenciaDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_ocorrencia_turma (cd_ocorrencia,"+
			                                  "cd_turma,"+
			                                  "dt_ultima_modificacao,"+
			                                  "cd_usuario_modificador,"+
			                                  "st_turma_anterior,"+
			                                  "st_turma_posterior) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTurma());
			if(objeto.getDtUltimaModificacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtUltimaModificacao().getTimeInMillis()));
			if(objeto.getCdUsuarioModificador()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioModificador());
			pstmt.setInt(5,objeto.getStTurmaAnterior());
			pstmt.setInt(6,objeto.getStTurmaPosterior());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OcorrenciaTurma objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OcorrenciaTurma objeto, int cdOcorrenciaOld) {
		return update(objeto, cdOcorrenciaOld, null);
	}

	public static int update(OcorrenciaTurma objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OcorrenciaTurma objeto, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			OcorrenciaTurma objetoTemp = get(objeto.getCdOcorrencia(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_ocorrencia_turma (cd_ocorrencia,"+
			                                  "cd_turma,"+
			                                  "dt_ultima_modificacao,"+
			                                  "cd_usuario_modificador,"+
			                                  "st_turma_anterior,"+
			                                  "st_turma_posterior) VALUES (?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_ocorrencia_turma SET cd_ocorrencia=?,"+
												      		   "cd_turma=?,"+
												      		   "dt_ultima_modificacao=?,"+
												      		   "cd_usuario_modificador=?,"+
												      		   "st_turma_anterior=?,"+
												      		   "st_turma_posterior=? WHERE cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTurma());
			if(objeto.getDtUltimaModificacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtUltimaModificacao().getTimeInMillis()));
			if(objeto.getCdUsuarioModificador()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioModificador());
			pstmt.setInt(5,objeto.getStTurmaAnterior());
			pstmt.setInt(6,objeto.getStTurmaPosterior());
			if (objetoTemp != null) {
				pstmt.setInt(7, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! OcorrenciaTurmaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia) {
		return delete(cdOcorrencia, null);
	}

	public static int delete(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_ocorrencia_turma WHERE cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.OcorrenciaDAO.delete(cdOcorrencia, connect)<=0) {
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
			System.err.println("Erro! OcorrenciaTurmaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OcorrenciaTurma get(int cdOcorrencia) {
		return get(cdOcorrencia, null);
	}

	public static OcorrenciaTurma get(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_turma A, grl_ocorrencia B WHERE A.cd_ocorrencia=B.cd_ocorrencia AND A.cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OcorrenciaTurma(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("st_ocorrencia"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_turma"),
						(rs.getTimestamp("dt_ultima_modificacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ultima_modificacao").getTime()),
						rs.getInt("cd_usuario_modificador"),
						rs.getInt("st_turma_anterior"),
						rs.getInt("st_turma_posterior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_turma");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OcorrenciaTurma> getList() {
		return getList(null);
	}

	public static ArrayList<OcorrenciaTurma> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OcorrenciaTurma> list = new ArrayList<OcorrenciaTurma>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OcorrenciaTurma obj = OcorrenciaTurmaDAO.get(rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaTurmaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_ocorrencia_turma", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
