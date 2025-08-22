package com.tivic.manager.hlp;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TopicoDAO{

	public static int insert(Topico objeto) {
		return insert(objeto, null);
	}

	public static int insert(Topico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("hlp_topico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					sol.dao.Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTopico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO hlp_topico (cd_topico,"+
			                                  "cd_topico_superior,"+
			                                  "cd_usuario_criacao,"+
			                                  "cd_usuario_alteracao,"+
			                                  "cd_usuario_revisao,"+
			                                  "nm_titulo,"+
			                                  "nm_subtitulo,"+
			                                  "txt_texto,"+
			                                  "txt_resumo,"+
			                                  "dt_criacao,"+
			                                  "dt_alteracao,"+
			                                  "dt_revisao,"+
			                                  "id_referencia,"+
			                                  "lg_revisado,"+
			                                  "nr_ordem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTopicoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTopicoSuperior());
			if(objeto.getCdUsuarioCriacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuarioCriacao());
			if(objeto.getCdUsuarioAlteracao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioAlteracao());
			if(objeto.getCdUsuarioRevisao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuarioRevisao());
			pstmt.setString(6,objeto.getNmTitulo());
			pstmt.setString(7,objeto.getNmSubtitulo());
			pstmt.setString(8,objeto.getTxtTexto());
			pstmt.setString(9,objeto.getTxtResumo());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			if(objeto.getDtRevisao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtRevisao().getTimeInMillis()));
			pstmt.setString(13,objeto.getIdReferencia());
			pstmt.setInt(14,objeto.getLgRevisado());
			pstmt.setInt(15,objeto.getNrOrdem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Topico objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Topico objeto, int cdTopicoOld) {
		return update(objeto, cdTopicoOld, null);
	}

	public static int update(Topico objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Topico objeto, int cdTopicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE hlp_topico SET cd_topico=?,"+
												      		   "cd_topico_superior=?,"+
												      		   "cd_usuario_criacao=?,"+
												      		   "cd_usuario_alteracao=?,"+
												      		   "cd_usuario_revisao=?,"+
												      		   "nm_titulo=?,"+
												      		   "nm_subtitulo=?,"+
												      		   "txt_texto=?,"+
												      		   "txt_resumo=?,"+
												      		   "dt_criacao=?,"+
												      		   "dt_alteracao=?,"+
												      		   "dt_revisao=?,"+
												      		   "id_referencia=?,"+
												      		   "lg_revisado=?,"+
												      		   "nr_ordem=? WHERE cd_topico=?");
			pstmt.setInt(1,objeto.getCdTopico());
			if(objeto.getCdTopicoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTopicoSuperior());
			if(objeto.getCdUsuarioCriacao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuarioCriacao());
			if(objeto.getCdUsuarioAlteracao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioAlteracao());
			if(objeto.getCdUsuarioRevisao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuarioRevisao());
			pstmt.setString(6,objeto.getNmTitulo());
			pstmt.setString(7,objeto.getNmSubtitulo());
			pstmt.setString(8,objeto.getTxtTexto());
			pstmt.setString(9,objeto.getTxtResumo());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			if(objeto.getDtRevisao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtRevisao().getTimeInMillis()));
			pstmt.setString(13,objeto.getIdReferencia());
			pstmt.setInt(14,objeto.getLgRevisado());
			pstmt.setInt(15,objeto.getNrOrdem());
			pstmt.setInt(16, cdTopicoOld!=0 ? cdTopicoOld : objeto.getCdTopico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTopico) {
		return delete(cdTopico, null);
	}

	public static int delete(int cdTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM hlp_topico WHERE cd_topico=?");
			pstmt.setInt(1, cdTopico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Topico get(int cdTopico) {
		return get(cdTopico, null);
	}

	public static Topico get(int cdTopico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM hlp_topico WHERE cd_topico=?");
			pstmt.setInt(1, cdTopico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Topico(rs.getInt("cd_topico"),
						rs.getInt("cd_topico_superior"),
						rs.getInt("cd_usuario_criacao"),
						rs.getInt("cd_usuario_alteracao"),
						rs.getInt("cd_usuario_revisao"),
						rs.getString("nm_titulo"),
						rs.getString("nm_subtitulo"),
						rs.getString("txt_texto"),
						rs.getString("txt_resumo"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_alteracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_alteracao").getTime()),
						(rs.getTimestamp("dt_revisao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_revisao").getTime()),
						rs.getString("id_referencia"),
						rs.getInt("lg_revisado"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM hlp_topico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TopicoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM hlp_topico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
