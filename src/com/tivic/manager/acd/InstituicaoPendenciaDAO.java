package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class InstituicaoPendenciaDAO{

	public static int insert(InstituicaoPendencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoPendencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_instituicao_pendencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_instituicao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdInstituicao()));
			int code = Conexao.getSequenceCode("acd_instituicao_pendencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInstituicaoPendencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_pendencia (cd_instituicao_pendencia,"+
			                                  "cd_instituicao,"+
			                                  "cd_turma,"+
			                                  "cd_aluno,"+
			                                  "cd_professor,"+
			                                  "tp_registro,"+
			                                  "tp_pendencia,"+
			                                  "dt_criacao,"+
			                                  "dt_atualizacao,"+
			                                  "st_instituicao_pendencia,"+
			                                  "txt_instituicao_pendencia,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTurma());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAluno());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProfessor());
			pstmt.setInt(6,objeto.getTpRegistro());
			pstmt.setInt(7,objeto.getTpPendencia());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(10,objeto.getStInstituicaoPendencia());
			pstmt.setString(11,objeto.getTxtInstituicaoPendencia());
			pstmt.setInt(12,objeto.getCdUsuario());
			int ret = pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoPendencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(InstituicaoPendencia objeto, int cdInstituicaoPendenciaOld, int cdInstituicaoOld) {
		return update(objeto, cdInstituicaoPendenciaOld, cdInstituicaoOld, null);
	}

	public static int update(InstituicaoPendencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(InstituicaoPendencia objeto, int cdInstituicaoPendenciaOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_pendencia SET cd_instituicao_pendencia=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_aluno=?,"+
												      		   "cd_professor=?,"+
												      		   "tp_registro=?,"+
												      		   "tp_pendencia=?,"+
												      		   "dt_criacao=?,"+
												      		   "dt_atualizacao=?,"+
												      		   "st_instituicao_pendencia=?,"+
												      		   "txt_instituicao_pendencia=?,"+
												      		   "cd_usuario=? WHERE cd_instituicao_pendencia=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdInstituicaoPendencia());
			pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTurma());
			if(objeto.getCdAluno()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAluno());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProfessor());
			pstmt.setInt(6,objeto.getTpRegistro());
			pstmt.setInt(7,objeto.getTpPendencia());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			pstmt.setInt(10,objeto.getStInstituicaoPendencia());
			pstmt.setString(11,objeto.getTxtInstituicaoPendencia());
			pstmt.setInt(12,objeto.getCdUsuario());
			pstmt.setInt(13, cdInstituicaoPendenciaOld!=0 ? cdInstituicaoPendenciaOld : objeto.getCdInstituicaoPendencia());
			pstmt.setInt(14, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicaoPendencia, int cdInstituicao) {
		return delete(cdInstituicaoPendencia, cdInstituicao, null);
	}

	public static int delete(int cdInstituicaoPendencia, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_pendencia WHERE cd_instituicao_pendencia=? AND cd_instituicao=?");
			pstmt.setInt(1, cdInstituicaoPendencia);
			pstmt.setInt(2, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoPendencia get(int cdInstituicaoPendencia, int cdInstituicao) {
		return get(cdInstituicaoPendencia, cdInstituicao, null);
	}

	public static InstituicaoPendencia get(int cdInstituicaoPendencia, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_pendencia WHERE cd_instituicao_pendencia=? AND cd_instituicao=?");
			pstmt.setInt(1, cdInstituicaoPendencia);
			pstmt.setInt(2, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoPendencia(rs.getInt("cd_instituicao_pendencia"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_aluno"),
						rs.getInt("cd_professor"),
						rs.getInt("tp_registro"),
						rs.getInt("tp_pendencia"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						rs.getInt("st_instituicao_pendencia"),
						rs.getString("txt_instituicao_pendencia"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_pendencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoPendencia> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoPendencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoPendencia> list = new ArrayList<InstituicaoPendencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoPendencia obj = InstituicaoPendenciaDAO.get(rsm.getInt("cd_instituicao_pendencia"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_pendencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}