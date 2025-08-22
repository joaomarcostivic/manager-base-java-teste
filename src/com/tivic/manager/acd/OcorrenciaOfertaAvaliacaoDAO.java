package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OcorrenciaOfertaAvaliacaoDAO{

	public static int insert(OcorrenciaOfertaAvaliacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(OcorrenciaOfertaAvaliacao objeto, Connection connect){
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
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_ocorrencia_oferta_avaliacao (cd_ocorrencia,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "vl_peso_anterior,"+
			                                  "vl_peso_posterior,"+
			                                  "dt_avaliacao_anterior,"+
			                                  "dt_avaliacao_posterior,"+
			                                  "st_oferta_avaliacao_anterior,"+
			                                  "st_oferta_avaliacao_posterior) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOferta());
			pstmt.setFloat(4,objeto.getVlPesoAnterior());
			pstmt.setFloat(5,objeto.getVlPesoPosterior());
			if(objeto.getDtAvaliacaoAnterior()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAvaliacaoAnterior().getTimeInMillis()));
			if(objeto.getDtAvaliacaoPosterior()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAvaliacaoPosterior().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStOfertaAvaliacaoAnterior());
			pstmt.setInt(9,objeto.getStOfertaAvaliacaoPosterior());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OcorrenciaOfertaAvaliacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OcorrenciaOfertaAvaliacao objeto, int cdOcorrenciaOld) {
		return update(objeto, cdOcorrenciaOld, null);
	}

	public static int update(OcorrenciaOfertaAvaliacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OcorrenciaOfertaAvaliacao objeto, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			OcorrenciaOfertaAvaliacao objetoTemp = get(objeto.getCdOcorrencia(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_ocorrencia_oferta_avaliacao (cd_ocorrencia,"+
			                                  "cd_oferta_avaliacao,"+
			                                  "cd_oferta,"+
			                                  "vl_peso_anterior,"+
			                                  "vl_peso_posterior,"+
			                                  "dt_avaliacao_anterior,"+
			                                  "dt_avaliacao_posterior,"+
			                                  "st_oferta_avaliacao_anterior,"+
			                                  "st_oferta_avaliacao_posterior) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_ocorrencia_oferta_avaliacao SET cd_ocorrencia=?,"+
												      		   "cd_oferta_avaliacao=?,"+
												      		   "cd_oferta=?,"+
												      		   "vl_peso_anterior=?,"+
												      		   "vl_peso_posterior=?,"+
												      		   "dt_avaliacao_anterior=?,"+
												      		   "dt_avaliacao_posterior=?,"+
												      		   "st_oferta_avaliacao_anterior=?,"+
												      		   "st_oferta_avaliacao_posterior=? WHERE cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdOfertaAvaliacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOfertaAvaliacao());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOferta());
			pstmt.setFloat(4,objeto.getVlPesoAnterior());
			pstmt.setFloat(5,objeto.getVlPesoPosterior());
			if(objeto.getDtAvaliacaoAnterior()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAvaliacaoAnterior().getTimeInMillis()));
			if(objeto.getDtAvaliacaoPosterior()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtAvaliacaoPosterior().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStOfertaAvaliacaoAnterior());
			pstmt.setInt(9,objeto.getStOfertaAvaliacaoPosterior());
			if (objetoTemp != null) {
				pstmt.setInt(10, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
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
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_ocorrencia_oferta_avaliacao WHERE cd_ocorrencia=?");
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
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OcorrenciaOfertaAvaliacao get(int cdOcorrencia) {
		return get(cdOcorrencia, null);
	}

	public static OcorrenciaOfertaAvaliacao get(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_oferta_avaliacao A, grl_ocorrencia B WHERE A.cd_ocorrencia=B.cd_ocorrencia AND A.cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OcorrenciaOfertaAvaliacao(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("st_ocorrencia"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_oferta_avaliacao"),
						rs.getInt("cd_oferta"),
						rs.getFloat("vl_peso_anterior"),
						rs.getFloat("vl_peso_posterior"),
						(rs.getTimestamp("dt_avaliacao_anterior")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_avaliacao_anterior").getTime()),
						(rs.getTimestamp("dt_avaliacao_posterior")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_avaliacao_posterior").getTime()),
						rs.getInt("st_oferta_avaliacao_anterior"),
						rs.getInt("st_oferta_avaliacao_posterior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_ocorrencia_oferta_avaliacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OcorrenciaOfertaAvaliacao> getList() {
		return getList(null);
	}

	public static ArrayList<OcorrenciaOfertaAvaliacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OcorrenciaOfertaAvaliacao> list = new ArrayList<OcorrenciaOfertaAvaliacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OcorrenciaOfertaAvaliacao obj = OcorrenciaOfertaAvaliacaoDAO.get(rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OcorrenciaOfertaAvaliacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_ocorrencia_oferta_avaliacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}