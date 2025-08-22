package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoAtendimentoDAO{

	public static int insert(TipoAtendimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoAtendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_tipo_atendimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoAtendimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_tipo_atendimento (cd_tipo_atendimento,"+
			                                  "nm_tipo_atendimento,"+
			                                  "txt_tipo_atendimento,"+
			                                  "tp_classificacao,"+
			                                  "nr_horas_previsao_resp) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoAtendimento());
			pstmt.setString(3,objeto.getTxtTipoAtendimento());
			pstmt.setInt(4,objeto.getTpClassificacao());
			pstmt.setInt(5,objeto.getNrHorasPrevisaoResp());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoAtendimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoAtendimento objeto, int cdTipoAtendimentoOld) {
		return update(objeto, cdTipoAtendimentoOld, null);
	}

	public static int update(TipoAtendimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoAtendimento objeto, int cdTipoAtendimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_tipo_atendimento SET cd_tipo_atendimento=?,"+
												      		   "nm_tipo_atendimento=?,"+
												      		   "txt_tipo_atendimento=?,"+
												      		   "tp_classificacao=?,"+
												      		   "nr_horas_previsao_resp=? WHERE cd_tipo_atendimento=?");
			pstmt.setInt(1,objeto.getCdTipoAtendimento());
			pstmt.setString(2,objeto.getNmTipoAtendimento());
			pstmt.setString(3,objeto.getTxtTipoAtendimento());
			pstmt.setInt(4,objeto.getTpClassificacao());
			pstmt.setInt(5,objeto.getNrHorasPrevisaoResp());
			pstmt.setInt(6, cdTipoAtendimentoOld!=0 ? cdTipoAtendimentoOld : objeto.getCdTipoAtendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoAtendimento) {
		return delete(cdTipoAtendimento, null);
	}

	public static int delete(int cdTipoAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_tipo_atendimento WHERE cd_tipo_atendimento=?");
			pstmt.setInt(1, cdTipoAtendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoAtendimento get(int cdTipoAtendimento) {
		return get(cdTipoAtendimento, null);
	}

	public static TipoAtendimento get(int cdTipoAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_atendimento WHERE cd_tipo_atendimento=?");
			pstmt.setInt(1, cdTipoAtendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoAtendimento(rs.getInt("cd_tipo_atendimento"),
						rs.getString("nm_tipo_atendimento"),
						rs.getString("txt_tipo_atendimento"),
						rs.getInt("tp_classificacao"),
						rs.getInt("nr_horas_previsao_resp"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_tipo_atendimento ORDER BY nm_tipo_atendimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAtendimentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_tipo_atendimento ", "ORDER BY nm_tipo_atendimento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
