package com.tivic.manager.mob.grafica;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class LoteImpressaoDAO{

	public static int insert(LoteImpressao objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteImpressao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_lote_impressao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLoteImpressao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lote_impressao (cd_lote_impressao,"+
			                                  "id_lote_impressao,"+
			                                  "txt_observacao,"+
			                                  "dt_criacao,"+
			                                  "dt_finalizacao,"+
			                                  "dt_atualizacao,"+
			                                  "dt_envio,"+
			                                  "st_lote_impressao,"+
			                                  "tp_transporte,"+
			                                  "txt_transporte,"+
			                                  "tp_destino,"+
			                                  "cd_usuario,"+
			                                  "cd_arquivo,"+
			                                  "cd_orgao,"+
			                                  "tp_lote_impressao,"+
			                                  "tp_documento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdLoteImpressao());
			pstmt.setString(3,objeto.getTxtObservacao());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtFinalizacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinalizacao().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStLoteImpressao());
			pstmt.setInt(9,objeto.getTpTransporte());
			pstmt.setString(10,objeto.getTxtTransporte());
			pstmt.setInt(11,objeto.getTpDestino());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdArquivo());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdOrgao());
			pstmt.setInt(15,objeto.getTpLoteImpressao());
			pstmt.setInt(16,objeto.getTpDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteImpressao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LoteImpressao objeto, int cdLoteImpressaoOld) {
		return update(objeto, cdLoteImpressaoOld, null);
	}

	public static int update(LoteImpressao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LoteImpressao objeto, int cdLoteImpressaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lote_impressao SET cd_lote_impressao=?,"+
												      		   "id_lote_impressao=?,"+
												      		   "txt_observacao=?,"+
												      		   "dt_criacao=?,"+
												      		   "dt_finalizacao=?,"+
												      		   "dt_atualizacao=?,"+
												      		   "dt_envio=?,"+
												      		   "st_lote_impressao=?,"+
												      		   "tp_transporte=?,"+
												      		   "txt_transporte=?,"+
												      		   "tp_destino=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_orgao=?,"+
												      		   "tp_lote_impressao=?,"+
												      		   "tp_documento=? WHERE cd_lote_impressao=?");
			pstmt.setInt(1,objeto.getCdLoteImpressao());
			pstmt.setString(2,objeto.getIdLoteImpressao());
			pstmt.setString(3,objeto.getTxtObservacao());
			if(objeto.getDtCriacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtCriacao().getTimeInMillis()));
			if(objeto.getDtFinalizacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinalizacao().getTimeInMillis()));
			if(objeto.getDtAtualizacao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAtualizacao().getTimeInMillis()));
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStLoteImpressao());
			pstmt.setInt(9,objeto.getTpTransporte());
			pstmt.setString(10,objeto.getTxtTransporte());
			pstmt.setInt(11,objeto.getTpDestino());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdArquivo());
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdOrgao());
			pstmt.setInt(15,objeto.getTpLoteImpressao());
			pstmt.setInt(16,objeto.getTpDocumento());
			pstmt.setInt(17, cdLoteImpressaoOld!=0 ? cdLoteImpressaoOld : objeto.getCdLoteImpressao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteImpressao) {
		return delete(cdLoteImpressao, null);
	}

	public static int delete(int cdLoteImpressao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lote_impressao WHERE cd_lote_impressao=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteImpressao get(int cdLoteImpressao) {
		return get(cdLoteImpressao, null);
	}

	public static LoteImpressao get(int cdLoteImpressao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao WHERE cd_lote_impressao=?");
			pstmt.setInt(1, cdLoteImpressao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteImpressao(rs.getInt("cd_lote_impressao"),
						rs.getString("id_lote_impressao"),
						rs.getString("txt_observacao"),
						(rs.getTimestamp("dt_criacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_criacao").getTime()),
						(rs.getTimestamp("dt_finalizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_finalizacao").getTime()),
						(rs.getTimestamp("dt_atualizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao").getTime()),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()),
						rs.getInt("st_lote_impressao"),
						rs.getInt("tp_transporte"),
						rs.getString("txt_transporte"),
						rs.getInt("tp_destino"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_orgao"),
						rs.getInt("tp_lote_impressao"),
						rs.getInt("tp_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteImpressao> getList() {
		return getList(null);
	}

	public static ArrayList<LoteImpressao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteImpressao> list = new ArrayList<LoteImpressao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LoteImpressao obj = LoteImpressaoDAO.get(rsm.getInt("cd_lote_impressao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lote_impressao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
