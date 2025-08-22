package com.tivic.manager.flp;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class FolhaPagamentoDAO{

	public static int insert(FolhaPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FolhaPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("flp_folha_pagamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFolhaPagamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_folha_pagamento (cd_folha_pagamento,"+
			                                  "cd_empresa,"+
			                                  "nr_mes,"+
			                                  "nr_ano,"+
			                                  "tp_folha_pagamento,"+
			                                  "id_folha_pagamento,"+
			                                  "st_folha_pagamento,"+
			                                  "vl_gprs,"+
			                                  "cd_indicador_salario_minimo,"+
			                                  "vl_deducao_ir_dependente,"+
			                                  "vl_deducao_ir_idoso,"+
			                                  "vl_minimo_ir,"+
			                                  "pr_vale_transporte,"+
			                                  "pr_fgts,"+
			                                  "nr_dias_uteis,"+
			                                  "vl_hora_aula_p1,"+
			                                  "vl_hora_aula_p2,"+
			                                  "vl_hora_aula_p3,"+
			                                  "vl_hora_aula_p4,"+
			                                  "vl_hora_aula_p5,"+
			                                  "dt_fechamento,"+
			                                  "txt_mensagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getNrMes());
			pstmt.setInt(4,objeto.getNrAno());
			pstmt.setInt(5,objeto.getTpFolhaPagamento());
			pstmt.setString(6,objeto.getIdFolhaPagamento());
			pstmt.setInt(7,objeto.getStFolhaPagamento());
			pstmt.setFloat(8,objeto.getVlGprs());
			if(objeto.getCdIndicadorSalarioMinimo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdIndicadorSalarioMinimo());
			pstmt.setFloat(10,objeto.getVlDeducaoIrDependente());
			pstmt.setFloat(11,objeto.getVlDeducaoIrIdoso());
			pstmt.setFloat(12,objeto.getVlMinimoIr());
			pstmt.setFloat(13,objeto.getPrValeTransporte());
			pstmt.setFloat(14,objeto.getPrFgts());
			pstmt.setInt(15,objeto.getNrDiasUteis());
			pstmt.setFloat(16,objeto.getVlHoraAulaP1());
			pstmt.setInt(17,objeto.getVlHoraAulaP2());
			pstmt.setInt(18,objeto.getVlHoraAulaP3());
			pstmt.setInt(19,objeto.getVlHoraAulaP4());
			pstmt.setInt(20,objeto.getVlHoraAulaP5());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setString(22,objeto.getTxtMensagem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FolhaPagamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FolhaPagamento objeto, int cdFolhaPagamentoOld) {
		return update(objeto, cdFolhaPagamentoOld, null);
	}

	public static int update(FolhaPagamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FolhaPagamento objeto, int cdFolhaPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_folha_pagamento SET cd_folha_pagamento=?,"+
												      		   "cd_empresa=?,"+
												      		   "nr_mes=?,"+
												      		   "nr_ano=?,"+
												      		   "tp_folha_pagamento=?,"+
												      		   "id_folha_pagamento=?,"+
												      		   "st_folha_pagamento=?,"+
												      		   "vl_gprs=?,"+
												      		   "cd_indicador_salario_minimo=?,"+
												      		   "vl_deducao_ir_dependente=?,"+
												      		   "vl_deducao_ir_idoso=?,"+
												      		   "vl_minimo_ir=?,"+
												      		   "pr_vale_transporte=?,"+
												      		   "pr_fgts=?,"+
												      		   "nr_dias_uteis=?,"+
												      		   "vl_hora_aula_p1=?,"+
												      		   "vl_hora_aula_p2=?,"+
												      		   "vl_hora_aula_p3=?,"+
												      		   "vl_hora_aula_p4=?,"+
												      		   "vl_hora_aula_p5=?,"+
												      		   "dt_fechamento=?,"+
												      		   "txt_mensagem=? WHERE cd_folha_pagamento=?");
			pstmt.setInt(1,objeto.getCdFolhaPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getNrMes());
			pstmt.setInt(4,objeto.getNrAno());
			pstmt.setInt(5,objeto.getTpFolhaPagamento());
			pstmt.setString(6,objeto.getIdFolhaPagamento());
			pstmt.setInt(7,objeto.getStFolhaPagamento());
			pstmt.setFloat(8,objeto.getVlGprs());
			if(objeto.getCdIndicadorSalarioMinimo()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdIndicadorSalarioMinimo());
			pstmt.setFloat(10,objeto.getVlDeducaoIrDependente());
			pstmt.setFloat(11,objeto.getVlDeducaoIrIdoso());
			pstmt.setFloat(12,objeto.getVlMinimoIr());
			pstmt.setFloat(13,objeto.getPrValeTransporte());
			pstmt.setFloat(14,objeto.getPrFgts());
			pstmt.setInt(15,objeto.getNrDiasUteis());
			pstmt.setFloat(16,objeto.getVlHoraAulaP1());
			pstmt.setInt(17,objeto.getVlHoraAulaP2());
			pstmt.setInt(18,objeto.getVlHoraAulaP3());
			pstmt.setInt(19,objeto.getVlHoraAulaP4());
			pstmt.setInt(20,objeto.getVlHoraAulaP5());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setString(22,objeto.getTxtMensagem());
			pstmt.setInt(23, cdFolhaPagamentoOld!=0 ? cdFolhaPagamentoOld : objeto.getCdFolhaPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFolhaPagamento) {
		return delete(cdFolhaPagamento, null);
	}

	public static int delete(int cdFolhaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_folha_pagamento WHERE cd_folha_pagamento=?");
			pstmt.setInt(1, cdFolhaPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FolhaPagamento get(int cdFolhaPagamento) {
		return get(cdFolhaPagamento, null);
	}

	public static FolhaPagamento get(int cdFolhaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_folha_pagamento WHERE cd_folha_pagamento=?");
			pstmt.setInt(1, cdFolhaPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FolhaPagamento(rs.getInt("cd_folha_pagamento"),
						rs.getInt("cd_empresa"),
						rs.getInt("nr_mes"),
						rs.getInt("nr_ano"),
						rs.getInt("tp_folha_pagamento"),
						rs.getString("id_folha_pagamento"),
						rs.getInt("st_folha_pagamento"),
						rs.getFloat("vl_gprs"),
						rs.getInt("cd_indicador_salario_minimo"),
						rs.getFloat("vl_deducao_ir_dependente"),
						rs.getFloat("vl_deducao_ir_idoso"),
						rs.getFloat("vl_minimo_ir"),
						rs.getFloat("pr_vale_transporte"),
						rs.getFloat("pr_fgts"),
						rs.getInt("nr_dias_uteis"),
						rs.getFloat("vl_hora_aula_p1"),
						rs.getInt("vl_hora_aula_p2"),
						rs.getInt("vl_hora_aula_p3"),
						rs.getInt("vl_hora_aula_p4"),
						rs.getInt("vl_hora_aula_p5"),
						(rs.getTimestamp("dt_fechamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fechamento").getTime()),
						rs.getString("txt_mensagem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_folha_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM flp_folha_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
