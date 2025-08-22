package com.tivic.manager.adm;

import java.sql.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

public class CondicaoPagamentoDAO {
	public static int insert(CondicaoPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(CondicaoPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_condicao_pagamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCondicaoPagamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_condicao_pagamento (cd_condicao_pagamento,"+
			                                  "vl_limite,"+
			                                  "vl_limite_mensal,"+
			                                  "vl_troco,"+
			                                  "id_condicao_pagamento,"+
			                                  "st_condicao_pagamento,"+
			                                  "lg_padrao,"+
			                                  "nm_condicao_pagamento,"+
			                                  "txt_descricao,"+
			                                  "dt_validade_condicao,"+
			                                  "dt_validade_limite,"+
			                                  "lg_permite_troco) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setFloat(1,objeto.getCdCondicaoPagamento());
			pstmt.setFloat(2,objeto.getVlLimite());
			pstmt.setFloat(3,objeto.getVlLimiteMensal());
			pstmt.setFloat(4,objeto.getVlTroco());
			pstmt.setString(5,objeto.getIdCondicaoPagamento());
			pstmt.setInt(6,objeto.getStCondicaoPagamento());
			pstmt.setInt(7,objeto.getLgPadrao());
			pstmt.setString(8,objeto.getNmCondicaoPagamento());
			pstmt.setString(9,objeto.getTxtDescricao());
			if(objeto.getDtValidadeCondicao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtValidadeCondicao().getTimeInMillis()));
			if(objeto.getDtValidadeLimite()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtValidadeLimite().getTimeInMillis()));
			pstmt.setInt(12,objeto.getLgPermiteTroco());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CondicaoPagamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CondicaoPagamento objeto, int cdCondicaoPagamentoOld) {
		return update(objeto, cdCondicaoPagamentoOld, null);
	}

	public static int update(CondicaoPagamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CondicaoPagamento objeto, int cdCondicaoPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_condicao_pagamento SET cd_condicao_pagamento=?,"+
												      		   "vl_limite=?,"+
												      		   "vl_limite_mensal=?,"+
												      		   "vl_troco=?,"+
												      		   "id_condicao_pagamento=?,"+
												      		   "st_condicao_pagamento=?,"+
												      		   "lg_padrao=?,"+
												      		   "nm_condicao_pagamento=?,"+
												      		   "txt_descricao=?,"+
												      		   "dt_validade_condicao=?,"+
												      		   "dt_validade_limite=?,"+
												      		   "lg_permite_troco=? WHERE cd_condicao_pagamento=?");
			pstmt.setFloat(1,objeto.getCdCondicaoPagamento());
			pstmt.setFloat(2,objeto.getVlLimite());
			pstmt.setFloat(3,objeto.getVlLimiteMensal());
			pstmt.setFloat(4,objeto.getVlTroco());
			pstmt.setString(5,objeto.getIdCondicaoPagamento());
			pstmt.setInt(6,objeto.getStCondicaoPagamento());
			pstmt.setInt(7,objeto.getLgPadrao());
			pstmt.setString(8,objeto.getNmCondicaoPagamento());
			pstmt.setString(9,objeto.getTxtDescricao());
			if(objeto.getDtValidadeCondicao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtValidadeCondicao().getTimeInMillis()));
			if(objeto.getDtValidadeLimite()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtValidadeLimite().getTimeInMillis()));
			pstmt.setInt(12,objeto.getLgPermiteTroco());
			pstmt.setInt(13, cdCondicaoPagamentoOld!=0 ? cdCondicaoPagamentoOld : objeto.getCdCondicaoPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCondicaoPagamento) {
		return delete(cdCondicaoPagamento, null);
	}

	public static int delete(int cdCondicaoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_condicao_pagamento WHERE cd_condicao_pagamento=?");
			pstmt.setInt(1, cdCondicaoPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CondicaoPagamento get(int cdCondicaoPagamento) {
		return get(cdCondicaoPagamento, null);
	}

	public static CondicaoPagamento get(int cdCondicaoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento WHERE cd_condicao_pagamento=?");
			pstmt.setInt(1, cdCondicaoPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CondicaoPagamento(rs.getInt("cd_condicao_pagamento"),
						rs.getFloat("vl_limite"),
						rs.getFloat("vl_limite_mensal"),
						rs.getFloat("vl_troco"),
						rs.getString("id_condicao_pagamento"),
						rs.getInt("st_condicao_pagamento"),
						rs.getInt("lg_padrao"),
						rs.getString("nm_condicao_pagamento"),
						rs.getString("txt_descricao"),
						(rs.getTimestamp("dt_validade_condicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade_condicao").getTime()),
						(rs.getTimestamp("dt_validade_limite")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade_limite").getTime()),
						rs.getInt("lg_permite_troco"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_condicao_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CondicaoPagamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_condicao_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
