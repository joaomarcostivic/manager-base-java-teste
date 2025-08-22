package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PlanoPagtoProdutoServicoDAO{

	public static int insert(PlanoPagtoProdutoServico objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(PlanoPagtoProdutoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_plano_pagto_produto_servico");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_plano_pagamento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdPlanoPagamento()));
			int code = Conexao.getSequenceCode("adm_plano_pagto_produto_servico", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlanoPagtoProdutoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_plano_pagto_produto_servico (cd_plano_pagto_produto_servico,"+
			                                  "cd_plano_pagamento,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_grupo,"+
			                                  "dt_inicio_vigencia,"+
			                                  "dt_final_vigencia,"+
			                                  "cd_tipo_operacao,"+
			                                  "cd_forma_pagamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPlanoPagamento());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTabelaPreco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGrupo());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoOperacao());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdFormaPagamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoPagtoProdutoServico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PlanoPagtoProdutoServico objeto, int cdPlanoPagtoProdutoServicoOld, int cdPlanoPagamentoOld) {
		return update(objeto, cdPlanoPagtoProdutoServicoOld, cdPlanoPagamentoOld, null);
	}

	public static int update(PlanoPagtoProdutoServico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PlanoPagtoProdutoServico objeto, int cdPlanoPagtoProdutoServicoOld, int cdPlanoPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_plano_pagto_produto_servico SET cd_plano_pagto_produto_servico=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_grupo=?,"+
												      		   "dt_inicio_vigencia=?,"+
												      		   "dt_final_vigencia=?,"+
												      		   "cd_tipo_operacao=?,"+
												      		   "cd_forma_pagamento=? WHERE cd_plano_pagto_produto_servico=? AND cd_plano_pagamento=?");
			pstmt.setInt(1,objeto.getCdPlanoPagtoProdutoServico());
			pstmt.setInt(2,objeto.getCdPlanoPagamento());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTabelaPreco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGrupo());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdTipoOperacao());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdFormaPagamento());
			pstmt.setInt(11, cdPlanoPagtoProdutoServicoOld!=0 ? cdPlanoPagtoProdutoServicoOld : objeto.getCdPlanoPagtoProdutoServico());
			pstmt.setInt(12, cdPlanoPagamentoOld!=0 ? cdPlanoPagamentoOld : objeto.getCdPlanoPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlanoPagtoProdutoServico, int cdPlanoPagamento) {
		return delete(cdPlanoPagtoProdutoServico, cdPlanoPagamento, null);
	}

	public static int delete(int cdPlanoPagtoProdutoServico, int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_plano_pagto_produto_servico WHERE cd_plano_pagto_produto_servico=? AND cd_plano_pagamento=?");
			pstmt.setInt(1, cdPlanoPagtoProdutoServico);
			pstmt.setInt(2, cdPlanoPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoPagtoProdutoServico get(int cdPlanoPagtoProdutoServico, int cdPlanoPagamento) {
		return get(cdPlanoPagtoProdutoServico, cdPlanoPagamento, null);
	}

	public static PlanoPagtoProdutoServico get(int cdPlanoPagtoProdutoServico, int cdPlanoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_pagto_produto_servico WHERE cd_plano_pagto_produto_servico=? AND cd_plano_pagamento=?");
			pstmt.setInt(1, cdPlanoPagtoProdutoServico);
			pstmt.setInt(2, cdPlanoPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoPagtoProdutoServico(rs.getInt("cd_plano_pagto_produto_servico"),
						rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_grupo"),
						(rs.getTimestamp("dt_inicio_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_vigencia").getTime()),
						(rs.getTimestamp("dt_final_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_vigencia").getTime()),
						rs.getInt("cd_tipo_operacao"),
						rs.getInt("cd_forma_pagamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_plano_pagto_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoPagtoProdutoServicoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_plano_pagto_produto_servico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
