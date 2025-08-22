package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class PlanoParcelamentoDAO{

	public static int insert(PlanoParcelamento objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(PlanoParcelamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_plano_pagamento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPlanoPagamento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "nr_ordem");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_plano_parcelamento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setNrOrdem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_plano_parcelamento (cd_plano_pagamento,"+
			                                  "nr_ordem,"+
			                                  "nr_dias,"+
			                                  "pr_valor_total,"+
			                                  "tp_intervalo,"+
			                                  "qt_parcelas) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPlanoPagamento());
			pstmt.setInt(2, code);
			pstmt.setInt(3,objeto.getNrDias());
			pstmt.setFloat(4,objeto.getPrValorTotal());
			pstmt.setInt(5,objeto.getTpIntervalo());
			pstmt.setInt(6,objeto.getQtParcelas());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoParcelamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PlanoParcelamento objeto, int cdPlanoPagamentoOld, int nrOrdemOld) {
		return update(objeto, cdPlanoPagamentoOld, nrOrdemOld, null);
	}

	public static int update(PlanoParcelamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PlanoParcelamento objeto, int cdPlanoPagamentoOld, int nrOrdemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_plano_parcelamento SET cd_plano_pagamento=?,"+
												      		   "nr_ordem=?,"+
												      		   "nr_dias=?,"+
												      		   "pr_valor_total=?,"+
												      		   "tp_intervalo=?,"+
												      		   "qt_parcelas=? WHERE cd_plano_pagamento=? AND nr_ordem=?");
			pstmt.setInt(1,objeto.getCdPlanoPagamento());
			pstmt.setInt(2,objeto.getNrOrdem());
			pstmt.setInt(3,objeto.getNrDias());
			pstmt.setFloat(4,objeto.getPrValorTotal());
			pstmt.setInt(5,objeto.getTpIntervalo());
			pstmt.setInt(6,objeto.getQtParcelas());
			pstmt.setInt(7, cdPlanoPagamentoOld!=0 ? cdPlanoPagamentoOld : objeto.getCdPlanoPagamento());
			pstmt.setInt(8, nrOrdemOld!=0 ? nrOrdemOld : objeto.getNrOrdem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoPagamento, int nrOrdem) {
		return delete(cdPlanoPagamento, nrOrdem, null);
	}

	public static int delete(int cdPlanoPagamento, int nrOrdem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_plano_parcelamento WHERE cd_plano_pagamento=? AND nr_ordem=?");
			pstmt.setInt(1, cdPlanoPagamento);
			pstmt.setInt(2, nrOrdem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoParcelamento get(int cdPlanoPagamento, int nrOrdem) {
		return get(cdPlanoPagamento, nrOrdem, null);
	}

	public static PlanoParcelamento get(int cdPlanoPagamento, int nrOrdem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_parcelamento WHERE cd_plano_pagamento=? AND nr_ordem=?");
			pstmt.setInt(1, cdPlanoPagamento);
			pstmt.setInt(2, nrOrdem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoParcelamento(rs.getInt("cd_plano_pagamento"),
						rs.getInt("nr_ordem"),
						rs.getInt("nr_dias"),
						rs.getFloat("pr_valor_total"),
						rs.getInt("tp_intervalo"),
						rs.getInt("qt_parcelas"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_parcelamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoParcelamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_plano_parcelamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
