package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ContratoDAO{

	public static int insert(Contrato objeto) {
		return insert(objeto, null);
	}

	public static int insert(Contrato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_contrato", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContrato(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_contrato (cd_contrato,"+
			                                  "cd_devedor,"+
			                                  "cd_processo,"+
			                                  "nr_agencia,"+
			                                  "nr_conta,"+
			                                  "nr_dv,"+
			                                  "nr_carteira,"+
			                                  "nr_contrato,"+
			                                  "nr_cnpj_incorporacao,"+
			                                  "nr_agencia_incorporacao,"+
			                                  "nr_conta_incorporacao,"+
			                                  "nr_dv_incorporacao,"+
			                                  "nr_carteira_incorporacao,"+
			                                  "nr_contrato_incorporacao,"+
			                                  "dt_inicio,"+
			                                  "vl_principal,"+
			                                  "vl_liquido,"+
			                                  "pr_juros,"+
			                                  "tp_juros,"+
			                                  "qt_parcelas,"+
			                                  "vl_parcela,"+
			                                  "dt_vencimento_primeira,"+
			                                  "vl_saldo_vencido,"+
			                                  "vl_cobranca,"+
			                                  "qt_parcelas_avencer,"+
			                                  "vl_saldo_avencer,"+
			                                  "vl_avista,"+
			                                  "vl_aprazo,"+
			                                  "vl_total,"+
			                                  "dt_atualizacao_edi,"+
			                                  "st_atualizacao_edi,"+
			                                  "st_contrato,"+
			                                  "dt_ultimo_pagamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDevedor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDevedor());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProcesso());
			pstmt.setString(4,objeto.getNrAgencia());
			pstmt.setString(5,objeto.getNrConta());
			pstmt.setString(6,objeto.getNrDv());
			pstmt.setString(7,objeto.getNrCarteira());
			pstmt.setString(8,objeto.getNrContrato());
			pstmt.setString(9,objeto.getNrCnpjIncorporacao());
			pstmt.setString(10,objeto.getNrAgenciaIncorporacao());
			pstmt.setString(11,objeto.getNrContaIncorporacao());
			pstmt.setString(12,objeto.getNrDvIncorporacao());
			pstmt.setString(13,objeto.getNrCarteiraIncorporacao());
			pstmt.setString(14,objeto.getNrContratoIncorporacao());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setDouble(16,objeto.getVlPrincipal());
			pstmt.setDouble(17,objeto.getVlLiquido());
			pstmt.setDouble(18,objeto.getPrJuros());
			pstmt.setInt(19,objeto.getTpJuros());
			pstmt.setInt(20,objeto.getQtParcelas());
			pstmt.setDouble(21,objeto.getVlParcela());
			if(objeto.getDtVencimentoPrimeira()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtVencimentoPrimeira().getTimeInMillis()));
			pstmt.setDouble(23,objeto.getVlSaldoVencido());
			pstmt.setDouble(24,objeto.getVlCobranca());
			pstmt.setInt(25,objeto.getQtParcelasAvencer());
			pstmt.setDouble(26,objeto.getVlSaldoAvencer());
			pstmt.setDouble(27,objeto.getVlAvista());
			pstmt.setDouble(28,objeto.getVlAprazo());
			pstmt.setDouble(29,objeto.getVlTotal());
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(31,objeto.getStAtualizacaoEdi());
			pstmt.setInt(32,objeto.getStContrato());
			if(objeto.getDtUltimoPagamento()==null)
				pstmt.setNull(33, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(33,new Timestamp(objeto.getDtUltimoPagamento().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Contrato objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Contrato objeto, int cdContratoOld) {
		return update(objeto, cdContratoOld, null);
	}

	public static int update(Contrato objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Contrato objeto, int cdContratoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_contrato SET cd_contrato=?,"+
												      		   "cd_devedor=?,"+
												      		   "cd_processo=?,"+
												      		   "nr_agencia=?,"+
												      		   "nr_conta=?,"+
												      		   "nr_dv=?,"+
												      		   "nr_carteira=?,"+
												      		   "nr_contrato=?,"+
												      		   "nr_cnpj_incorporacao=?,"+
												      		   "nr_agencia_incorporacao=?,"+
												      		   "nr_conta_incorporacao=?,"+
												      		   "nr_dv_incorporacao=?,"+
												      		   "nr_carteira_incorporacao=?,"+
												      		   "nr_contrato_incorporacao=?,"+
												      		   "dt_inicio=?,"+
												      		   "vl_principal=?,"+
												      		   "vl_liquido=?,"+
												      		   "pr_juros=?,"+
												      		   "tp_juros=?,"+
												      		   "qt_parcelas=?,"+
												      		   "vl_parcela=?,"+
												      		   "dt_vencimento_primeira=?,"+
												      		   "vl_saldo_vencido=?,"+
												      		   "vl_cobranca=?,"+
												      		   "qt_parcelas_avencer=?,"+
												      		   "vl_saldo_avencer=?,"+
												      		   "vl_avista=?,"+
												      		   "vl_aprazo=?,"+
												      		   "vl_total=?,"+
												      		   "dt_atualizacao_edi=?,"+
												      		   "st_atualizacao_edi=?,"+
												      		   "st_contrato=?,"+
												      		   "dt_ultimo_pagamento=? WHERE cd_contrato=?");
			pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdDevedor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDevedor());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProcesso());
			pstmt.setString(4,objeto.getNrAgencia());
			pstmt.setString(5,objeto.getNrConta());
			pstmt.setString(6,objeto.getNrDv());
			pstmt.setString(7,objeto.getNrCarteira());
			pstmt.setString(8,objeto.getNrContrato());
			pstmt.setString(9,objeto.getNrCnpjIncorporacao());
			pstmt.setString(10,objeto.getNrAgenciaIncorporacao());
			pstmt.setString(11,objeto.getNrContaIncorporacao());
			pstmt.setString(12,objeto.getNrDvIncorporacao());
			pstmt.setString(13,objeto.getNrCarteiraIncorporacao());
			pstmt.setString(14,objeto.getNrContratoIncorporacao());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			pstmt.setDouble(16,objeto.getVlPrincipal());
			pstmt.setDouble(17,objeto.getVlLiquido());
			pstmt.setDouble(18,objeto.getPrJuros());
			pstmt.setInt(19,objeto.getTpJuros());
			pstmt.setInt(20,objeto.getQtParcelas());
			pstmt.setDouble(21,objeto.getVlParcela());
			if(objeto.getDtVencimentoPrimeira()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtVencimentoPrimeira().getTimeInMillis()));
			pstmt.setDouble(23,objeto.getVlSaldoVencido());
			pstmt.setDouble(24,objeto.getVlCobranca());
			pstmt.setInt(25,objeto.getQtParcelasAvencer());
			pstmt.setDouble(26,objeto.getVlSaldoAvencer());
			pstmt.setDouble(27,objeto.getVlAvista());
			pstmt.setDouble(28,objeto.getVlAprazo());
			pstmt.setDouble(29,objeto.getVlTotal());
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(31,objeto.getStAtualizacaoEdi());
			pstmt.setInt(32,objeto.getStContrato());
			if(objeto.getDtUltimoPagamento()==null)
				pstmt.setNull(33, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(33,new Timestamp(objeto.getDtUltimoPagamento().getTimeInMillis()));
			pstmt.setInt(34, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato) {
		return delete(cdContrato, null);
	}

	public static int delete(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_contrato WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Contrato get(int cdContrato) {
		return get(cdContrato, null);
	}

	public static Contrato get(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_contrato WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Contrato(rs.getInt("cd_contrato"),
						rs.getInt("cd_devedor"),
						rs.getInt("cd_processo"),
						rs.getString("nr_agencia"),
						rs.getString("nr_conta"),
						rs.getString("nr_dv"),
						rs.getString("nr_carteira"),
						rs.getString("nr_contrato"),
						rs.getString("nr_cnpj_incorporacao"),
						rs.getString("nr_agencia_incorporacao"),
						rs.getString("nr_conta_incorporacao"),
						rs.getString("nr_dv_incorporacao"),
						rs.getString("nr_carteira_incorporacao"),
						rs.getString("nr_contrato_incorporacao"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						rs.getDouble("vl_principal"),
						rs.getDouble("vl_liquido"),
						rs.getDouble("pr_juros"),
						rs.getInt("tp_juros"),
						rs.getInt("qt_parcelas"),
						rs.getDouble("vl_parcela"),
						(rs.getTimestamp("dt_vencimento_primeira")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento_primeira").getTime()),
						rs.getDouble("vl_saldo_vencido"),
						rs.getDouble("vl_cobranca"),
						rs.getInt("qt_parcelas_avencer"),
						rs.getDouble("vl_saldo_avencer"),
						rs.getDouble("vl_avista"),
						rs.getDouble("vl_aprazo"),
						rs.getDouble("vl_total"),
						(rs.getTimestamp("dt_atualizacao_edi")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao_edi").getTime()),
						rs.getInt("st_atualizacao_edi"),
						rs.getInt("st_contrato"),
						(rs.getTimestamp("dt_ultimo_pagamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ultimo_pagamento").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_contrato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Contrato> getList() {
		return getList(null);
	}

	public static ArrayList<Contrato> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Contrato> list = new ArrayList<Contrato>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Contrato obj = ContratoDAO.get(rsm.getInt("cd_contrato"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_contrato", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}