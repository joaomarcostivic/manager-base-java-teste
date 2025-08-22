package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FidelidadeRegraDAO{

	public static int insert(FidelidadeRegra objeto) {
		return insert(objeto, null);
	}

	public static int insert(FidelidadeRegra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("crm_fidelidade_regra", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFidelidadeRegra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_fidelidade_regra (cd_fidelidade_regra,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_plano_pagto_produto_servico,"+
			                                  "cd_plano_pagamento,"+
			                                  "cd_tipo_operacao,"+
			                                  "cd_forma_pagamento,"+
			                                  "dt_inicio_vigencia,"+
			                                  "dt_final_vigencia,"+
			                                  "tp_aplicacao,"+
			                                  "tp_base_calculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTabelaPreco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdPlanoPagtoProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPlanoPagtoProdutoServico());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPlanoPagamento());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoOperacao());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdFormaPagamento());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.setInt(11,objeto.getTpAplicacao());
			pstmt.setInt(12,objeto.getTpBaseCalculo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FidelidadeRegra objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FidelidadeRegra objeto, int cdFidelidadeRegraOld) {
		return update(objeto, cdFidelidadeRegraOld, null);
	}

	public static int update(FidelidadeRegra objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FidelidadeRegra objeto, int cdFidelidadeRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_fidelidade_regra SET cd_fidelidade_regra=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_plano_pagto_produto_servico=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "cd_tipo_operacao=?,"+
												      		   "cd_forma_pagamento=?,"+
												      		   "dt_inicio_vigencia=?,"+
												      		   "dt_final_vigencia=?,"+
												      		   "tp_aplicacao=?,"+
												      		   "tp_base_calculo=? WHERE cd_fidelidade_regra=?");
			pstmt.setInt(1,objeto.getCdFidelidadeRegra());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTabelaPreco());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdPlanoPagtoProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPlanoPagtoProdutoServico());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPlanoPagamento());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoOperacao());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdFormaPagamento());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			pstmt.setInt(11,objeto.getTpAplicacao());
			pstmt.setInt(12,objeto.getTpBaseCalculo());
			pstmt.setInt(13, cdFidelidadeRegraOld!=0 ? cdFidelidadeRegraOld : objeto.getCdFidelidadeRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFidelidadeRegra) {
		return delete(cdFidelidadeRegra, null);
	}

	public static int delete(int cdFidelidadeRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_fidelidade_regra WHERE cd_fidelidade_regra=?");
			pstmt.setInt(1, cdFidelidadeRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FidelidadeRegra get(int cdFidelidadeRegra) {
		return get(cdFidelidadeRegra, null);
	}

	public static FidelidadeRegra get(int cdFidelidadeRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade_regra WHERE cd_fidelidade_regra=?");
			pstmt.setInt(1, cdFidelidadeRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FidelidadeRegra(rs.getInt("cd_fidelidade_regra"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_plano_pagto_produto_servico"),
						rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_tipo_operacao"),
						rs.getInt("cd_forma_pagamento"),
						(rs.getTimestamp("dt_inicio_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_vigencia").getTime()),
						(rs.getTimestamp("dt_final_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_vigencia").getTime()),
						rs.getInt("tp_aplicacao"),
						rs.getInt("tp_base_calculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_fidelidade_regra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FidelidadeRegraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_fidelidade_regra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
