package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class PlanoPagtoDocumentoSaidaDAO{

	public static int insert(PlanoPagtoDocumentoSaida objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoPagtoDocumentoSaida objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_plano_pagto_documento_saida (cd_plano_pagamento,"+
			                                  "cd_documento_saida,"+
			                                  "cd_forma_pagamento,"+
			                                  "cd_usuario,"+
			                                  "vl_pagamento," +
			                                  "vl_desconto," +
			                                  "nr_autorizacao," +
			                                  "vl_acrescimo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPlanoPagamento());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumentoSaida());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFormaPagamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			pstmt.setFloat(5,objeto.getVlPagamento());
			pstmt.setFloat(6,objeto.getVlDesconto());
			pstmt.setString(7,objeto.getNrAutorizacao());
			pstmt.setFloat(8,objeto.getVlAcrescimo());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoPagtoDocumentoSaida objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(PlanoPagtoDocumentoSaida objeto, int cdPlanoPagamentoOld, int cdDocumentoSaidaOld, int cdFormaPagamentoOld) {
		return update(objeto, cdPlanoPagamentoOld, cdDocumentoSaidaOld, cdFormaPagamentoOld, null);
	}

	public static int update(PlanoPagtoDocumentoSaida objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(PlanoPagtoDocumentoSaida objeto, int cdPlanoPagamentoOld, int cdDocumentoSaidaOld, int cdFormaPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_plano_pagto_documento_saida SET cd_plano_pagamento=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_forma_pagamento=?,"+
												      		   "cd_usuario=?,"+
												      		   "vl_pagamento=?," +
												      		   "vl_desconto=?," +
												      		   "nr_autorizacao=?," +
												      		   "vl_acrescimo=? WHERE cd_plano_pagamento=? AND cd_documento_saida=? AND cd_forma_pagamento=?");
			pstmt.setInt(1,objeto.getCdPlanoPagamento());
			pstmt.setInt(2,objeto.getCdDocumentoSaida());
			pstmt.setInt(3,objeto.getCdFormaPagamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			pstmt.setFloat(5,objeto.getVlPagamento());
			pstmt.setFloat(6,objeto.getVlDesconto());
			pstmt.setString(7,objeto.getNrAutorizacao());
			pstmt.setFloat(8,objeto.getVlAcrescimo());
			pstmt.setInt(9, cdPlanoPagamentoOld!=0 ? cdPlanoPagamentoOld : objeto.getCdPlanoPagamento());
			pstmt.setInt(10, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.setInt(11, cdFormaPagamentoOld!=0 ? cdFormaPagamentoOld : objeto.getCdFormaPagamento());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoPagamento, int cdDocumentoSaida, int cdFormaPagamento) {
		return delete(cdPlanoPagamento, cdDocumentoSaida, cdFormaPagamento, null);
	}

	public static int delete(int cdPlanoPagamento, int cdDocumentoSaida, int cdFormaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_plano_pagto_documento_saida WHERE cd_plano_pagamento=? AND cd_documento_saida=? AND cd_forma_pagamento=?");
			pstmt.setInt(1, cdPlanoPagamento);
			pstmt.setInt(2, cdDocumentoSaida);
			pstmt.setInt(3, cdFormaPagamento);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoPagtoDocumentoSaida get(int cdPlanoPagamento, int cdDocumentoSaida, int cdFormaPagamento) {
		return get(cdPlanoPagamento, cdDocumentoSaida, cdFormaPagamento, null);
	}

	public static PlanoPagtoDocumentoSaida get(int cdPlanoPagamento, int cdDocumentoSaida, int cdFormaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_pagto_documento_saida WHERE cd_plano_pagamento=? AND cd_documento_saida=? AND cd_forma_pagamento=?");
			pstmt.setInt(1, cdPlanoPagamento);
			pstmt.setInt(2, cdDocumentoSaida);
			pstmt.setInt(3, cdFormaPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoPagtoDocumentoSaida(rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_forma_pagamento"),
						rs.getInt("cd_usuario"),
						rs.getFloat("vl_pagamento"),
						rs.getFloat("vl_desconto"),
						rs.getString("nr_autorizacao"),
						rs.getFloat("vl_acrescimo"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_pagto_documento_saida");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_plano_pagto_documento_saida", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
