package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MultaDAO{

	public static int insert(Multa objeto) {
		return insert(objeto, null);
	}

	public static int insert(Multa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_multa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMulta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_multa (cd_multa,"+
			                                  "cd_conta_pagar,"+
			                                  "cd_veiculo,"+
			                                  "dt_infracao,"+
			                                  "nm_local_infracao,"+
			                                  "nm_infracao,"+
			                                  "id_infracao,"+
			                                  "vl_multa,"+
			                                  "txt_observacao,"+
			                                  "nr_agente_transito,"+
			                                  "nm_agente_transito,"+
			                                  "st_multa,"+
			                                  "dt_final_recurso,"+
			                                  "lg_advertencia,"+
			                                  "dt_pagamento_desconto,"+
			                                  "dt_notificacao,"+
			                                  "dt_vencimento,"+
			                                  "cd_responsavel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmLocalInfracao());
			pstmt.setString(6,objeto.getNmInfracao());
			pstmt.setString(7,objeto.getIdInfracao());
			pstmt.setFloat(8,objeto.getVlMulta());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setString(10,objeto.getNrAgenteTransito());
			pstmt.setString(11,objeto.getNmAgenteTransito());
			pstmt.setInt(12,objeto.getStMulta());
			if(objeto.getDtFinalRecurso()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtFinalRecurso().getTimeInMillis()));
			pstmt.setInt(14,objeto.getLgAdvertencia());
			if(objeto.getDtPagamentoDesconto()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtPagamentoDesconto().getTimeInMillis()));
			if(objeto.getDtNotificacao()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtNotificacao().getTimeInMillis()));
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdResponsavel());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Multa objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Multa objeto, int cdMultaOld) {
		return update(objeto, cdMultaOld, null);
	}

	public static int update(Multa objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Multa objeto, int cdMultaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_multa SET cd_multa=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "cd_veiculo=?,"+
												      		   "dt_infracao=?,"+
												      		   "nm_local_infracao=?,"+
												      		   "nm_infracao=?,"+
												      		   "id_infracao=?,"+
												      		   "vl_multa=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_agente_transito=?,"+
												      		   "nm_agente_transito=?,"+
												      		   "st_multa=?,"+
												      		   "dt_final_recurso=?,"+
												      		   "lg_advertencia=?,"+
												      		   "dt_pagamento_desconto=?,"+
												      		   "dt_notificacao=?,"+
												      		   "dt_vencimento=?,"+
												      		   "cd_responsavel=? WHERE cd_multa=?");
			pstmt.setInt(1,objeto.getCdMulta());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdVeiculo());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmLocalInfracao());
			pstmt.setString(6,objeto.getNmInfracao());
			pstmt.setString(7,objeto.getIdInfracao());
			pstmt.setFloat(8,objeto.getVlMulta());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setString(10,objeto.getNrAgenteTransito());
			pstmt.setString(11,objeto.getNmAgenteTransito());
			pstmt.setInt(12,objeto.getStMulta());
			if(objeto.getDtFinalRecurso()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtFinalRecurso().getTimeInMillis()));
			pstmt.setInt(14,objeto.getLgAdvertencia());
			if(objeto.getDtPagamentoDesconto()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtPagamentoDesconto().getTimeInMillis()));
			if(objeto.getDtNotificacao()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtNotificacao().getTimeInMillis()));
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdResponsavel());
			pstmt.setInt(19, cdMultaOld!=0 ? cdMultaOld : objeto.getCdMulta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMulta) {
		return delete(cdMulta, null);
	}

	public static int delete(int cdMulta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_multa WHERE cd_multa=?");
			pstmt.setInt(1, cdMulta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Multa get(int cdMulta) {
		return get(cdMulta, null);
	}

	public static Multa get(int cdMulta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_multa WHERE cd_multa=?");
			pstmt.setInt(1, cdMulta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Multa(rs.getInt("cd_multa"),
						rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_veiculo"),
						(rs.getTimestamp("dt_infracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_infracao").getTime()),
						rs.getString("nm_local_infracao"),
						rs.getString("nm_infracao"),
						rs.getString("id_infracao"),
						rs.getFloat("vl_multa"),
						rs.getString("txt_observacao"),
						rs.getString("nr_agente_transito"),
						rs.getString("nm_agente_transito"),
						rs.getInt("st_multa"),
						(rs.getTimestamp("dt_final_recurso")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_recurso").getTime()),
						rs.getInt("lg_advertencia"),
						(rs.getTimestamp("dt_pagamento_desconto")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pagamento_desconto").getTime()),
						(rs.getTimestamp("dt_notificacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_notificacao").getTime()),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						rs.getInt("cd_responsavel"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_multa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MultaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_multa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
