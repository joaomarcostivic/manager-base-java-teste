package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class AitPagamentoDAO{

	public static int insert(AitPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ait");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_pagamento");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("mob_ait_pagamento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPagamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_pagamento (cd_ait,"+
			                                  "cd_pagamento,"+
			                                  "vl_tarifa,"+
			                                  "nr_banco,"+
			                                  "nr_agencia,"+
			                                  "dt_pagamento,"+
			                                  "dt_credito,"+
			                                  "tp_arrecadacao,"+
			                                  "nr_conta_credito,"+
			                                  "nr_agencia_credito,"+
			                                  "tp_condicionalidade,"+
			                                  "tp_pagamento,"+
			                                  "tp_modalidade,"+
			                                  "vl_pago,"+
			                                  "uf_pagamento,"+
			                                  "nr_documento,"+
			                                  "vl_repasse,"+
			                                  "cd_conta_receber,"+
			                                  "vl_detran_arrecadador,"+
			                                  "vl_funset,"+
			                                  "vl_denatran,"+
			                                  "vl_orgao,"+
			                                  "nr_erro," + 
			                                  "cd_arquivo,"+
			                                  "st_pagamento,"+ 
			                                  "dt_cancelamento,"+
			                                  "cd_movimento ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdAit()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAit());
			pstmt.setInt(2, code);
			if(objeto.getVlTarifa()==null)
				pstmt.setNull(3, Types.DOUBLE);
			else
				pstmt.setDouble(3,objeto.getVlTarifa());
			pstmt.setString(4,objeto.getNrBanco());
			pstmt.setString(5,objeto.getNrAgencia());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			if(objeto.getDtCredito()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCredito().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpArrecadacao());
			pstmt.setString(9,objeto.getNrContaCredito());
			pstmt.setString(10,objeto.getNrAgenciaCredito());
			pstmt.setInt(11,objeto.getTpCondicionalidade());
			pstmt.setInt(12,objeto.getTpPagamento());
			pstmt.setInt(13,objeto.getTpModalidade());
			
			if(objeto.getVlPago()==null)
				pstmt.setNull(14, Types.DOUBLE);
			else
				pstmt.setDouble(14,objeto.getVlPago());
			
			pstmt.setString(15,objeto.getUfPagamento());
			pstmt.setString(16,objeto.getNrDocumento());
			
			if(objeto.getVlRepasse()==null)
				pstmt.setNull(17, Types.DOUBLE);
			else
				pstmt.setDouble(17,objeto.getVlRepasse());
			
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdContaReceber());
			
			if(objeto.getVlDetranArrecadador()==null)
				pstmt.setNull(19, Types.DOUBLE);
			else
				pstmt.setDouble(19,objeto.getVlDetranArrecadador());
			

			if(objeto.getVlFunset()==null)
				pstmt.setNull(20, Types.DOUBLE);
			else
				pstmt.setDouble(20,objeto.getVlFunset());
			

			if(objeto.getVlDenatran()==null)
				pstmt.setNull(21, Types.DOUBLE);
			else
				pstmt.setDouble(21,objeto.getVlDenatran());
			

			if(objeto.getVlOrgao()==null)
				pstmt.setNull(22, Types.DOUBLE);
			else
				pstmt.setDouble(22,objeto.getVlOrgao());
						
			pstmt.setString(23,objeto.getNrErro());
			
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdArquivo());
			
			if(objeto.getStPagamento()==0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25, objeto.getStPagamento());
			
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			if(objeto.getCdMovimento()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdMovimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitPagamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitPagamento objeto, int cdAitOld, int cdPagamentoOld) {
		return update(objeto, cdAitOld, cdPagamentoOld, null);
	}

	public static int update(AitPagamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitPagamento objeto, int cdAitOld, int cdPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_pagamento SET cd_ait=?,"+
												      		   "cd_pagamento=?,"+
												      		   "vl_tarifa=?,"+
												      		   "nr_banco=?,"+
												      		   "nr_agencia=?,"+
												      		   "dt_pagamento=?,"+
												      		   "dt_credito=?,"+
												      		   "tp_arrecadacao=?,"+
												      		   "nr_conta_credito=?,"+
												      		   "nr_agencia_credito=?,"+
												      		   "tp_condicionalidade=?,"+
												      		   "tp_pagamento=?,"+
												      		   "tp_modalidade=?,"+
												      		   "vl_pago=?,"+
												      		   "uf_pagamento=?,"+
												      		   "nr_documento=?,"+
												      		   "vl_repasse=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "vl_detran_arrecadador=?,"+
												      		   "vl_funset=?,"+
												      		   "vl_denatran=?,"+
												      		   "vl_orgao=?,"+
												      		   "nr_erro=?,"+
												      		   "cd_arquivo=?,"+
												      		   "st_pagamento=?,"+
												      		   "dt_cancelamento=?,"+
												      		   "cd_movimento=?"+
												      		   "WHERE cd_ait=? AND cd_pagamento=?");
			pstmt.setInt(1,objeto.getCdAit());
			pstmt.setInt(2,objeto.getCdPagamento());
			if(objeto.getVlTarifa()==null)
				pstmt.setNull(3, Types.DOUBLE);
			else
				pstmt.setDouble(3,objeto.getVlTarifa());
			pstmt.setString(4,objeto.getNrBanco());
			pstmt.setString(5,objeto.getNrAgencia());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			if(objeto.getDtCredito()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCredito().getTimeInMillis()));
			pstmt.setInt(8,objeto.getTpArrecadacao());
			pstmt.setString(9,objeto.getNrContaCredito());
			pstmt.setString(10,objeto.getNrAgenciaCredito());
			pstmt.setInt(11,objeto.getTpCondicionalidade());
			pstmt.setInt(12,objeto.getTpPagamento());
			pstmt.setInt(13,objeto.getTpModalidade());
			pstmt.setDouble(14,objeto.getVlPago());
			pstmt.setString(15,objeto.getUfPagamento());
			pstmt.setString(16,objeto.getNrDocumento());
			if(objeto.getVlRepasse()==null)
				pstmt.setNull(17, Types.DOUBLE);
			else
				pstmt.setDouble(17,objeto.getVlRepasse());
			
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdContaReceber());
			if(objeto.getVlDetranArrecadador()==null)
				pstmt.setNull(19, Types.DOUBLE);
			else
				pstmt.setDouble(19,objeto.getVlDetranArrecadador());
			if(objeto.getVlFunset()==null)
				pstmt.setNull(20, Types.DOUBLE);
			else
				pstmt.setDouble(20,objeto.getVlFunset());
			if(objeto.getVlDenatran()==null)
				pstmt.setNull(21, Types.DOUBLE);
			else
				pstmt.setDouble(21,objeto.getVlDenatran());	
			if(objeto.getVlOrgao()==null)
				pstmt.setNull(22, Types.DOUBLE);
			else
				pstmt.setDouble(22,objeto.getVlOrgao());						
			pstmt.setString(23,objeto.getNrErro());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdArquivo());
			pstmt.setInt(25, objeto.getStPagamento());
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26, new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			if(objeto.getCdMovimento()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdMovimento());
			pstmt.setInt(28, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.setInt(29, cdPagamentoOld!=0 ? cdPagamentoOld : objeto.getCdPagamento());			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit, int cdPagamento) {
		return delete(cdAit, cdPagamento, null);
	}

	public static int delete(int cdAit, int cdPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_pagamento WHERE cd_ait=? AND cd_pagamento=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitPagamento get(int cdAit, int cdPagamento) {
		return get(cdAit, cdPagamento, null);
	}

	public static AitPagamento get(int cdAit, int cdPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_pagamento WHERE cd_ait=? AND cd_pagamento=?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitPagamento(rs.getInt("cd_ait"),
						rs.getInt("cd_pagamento"),
						rs.getDouble("vl_tarifa"),
						rs.getString("nr_banco"),
						rs.getString("nr_agencia"),
						(rs.getTimestamp("dt_pagamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pagamento").getTime()),
						(rs.getTimestamp("dt_credito")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_credito").getTime()),
						rs.getInt("tp_arrecadacao"),
						rs.getString("nr_conta_credito"),
						rs.getString("nr_agencia_credito"),
						rs.getInt("tp_condicionalidade"),
						rs.getInt("tp_pagamento"),
						rs.getInt("tp_modalidade"),
						rs.getDouble("vl_pago"),
						rs.getString("uf_pagamento"),
						rs.getString("nr_documento"),
						rs.getDouble("vl_repasse"),
						rs.getInt("cd_conta_receber"),
						rs.getDouble("vl_detran_arrecadador"),
						rs.getDouble("vl_funset"),
						rs.getDouble("vl_denatran"),
						rs.getDouble("vl_orgao"),
						rs.getString("nr_erro"),
						rs.getInt("cd_arquivo"),
						rs.getInt("st_pagamento"),
						(rs.getTimestamp("dt_cancelamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						rs.getInt("cd_movimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitPagamento> getList() {
		return getList(null);
	}

	public static ArrayList<AitPagamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitPagamento> list = new ArrayList<AitPagamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitPagamento obj = AitPagamentoDAO.get(rsm.getInt("cd_ait"), rsm.getInt("cd_pagamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_pagamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	

	public static AitPagamento get(int cdAit) {
		return get(cdAit, null);
	}
	
	public static AitPagamento get(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_pagamento WHERE cd_ait=?");
			pstmt.setInt(1, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitPagamento(rs.getInt("cd_ait"),
						rs.getInt("cd_pagamento"),
						rs.getDouble("vl_tarifa"),
						rs.getString("nr_banco"),
						rs.getString("nr_agencia"),
						(rs.getTimestamp("dt_pagamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pagamento").getTime()),
						(rs.getTimestamp("dt_credito")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_credito").getTime()),
						rs.getInt("tp_arrecadacao"),
						rs.getString("nr_conta_credito"),
						rs.getString("nr_agencia_credito"),
						rs.getInt("tp_condicionalidade"),
						rs.getInt("tp_pagamento"),
						rs.getInt("tp_modalidade"),
						rs.getDouble("vl_pago"),
						rs.getString("uf_pagamento"),
						rs.getString("nr_documento"),
						rs.getDouble("vl_repasse"),
						rs.getInt("cd_conta_receber"),
						rs.getDouble("vl_detran_arrecadador"),
						rs.getDouble("vl_funset"),
						rs.getDouble("vl_denatran"),
						rs.getDouble("vl_orgao"),
						rs.getString("nr_erro"),
						rs.getInt("cd_arquivo"),
						rs.getInt("st_pagamento"),
						(rs.getTimestamp("dt_cancelamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						rs.getInt("cd_movimento"));
			} else {
				return null;
			}
		}		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPagamentoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
