package com.tivic.manager.flp;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FolhaPagamentoFuncionarioDAO{

	public static int insert(FolhaPagamentoFuncionario objeto) {
		return insert(objeto, null);
	}

	public static int insert(FolhaPagamentoFuncionario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_folha_pagamento_funcionario (cd_matricula,"+
			                                  "cd_folha_pagamento,"+
			                                  "qt_dias_trabalhados,"+
			                                  "qt_horas_mes,"+
			                                  "vl_base_irrf,"+
			                                  "vl_base_inss,"+
			                                  "vl_base_fgts,"+
			                                  "vl_inss,"+
			                                  "vl_inss_patronal,"+
			                                  "vl_total_provento,"+
			                                  "vl_total_desconto,"+
			                                  "vl_hora,"+
			                                  "vl_dia,"+
			                                  "vl_provento_principal,"+
			                                  "vl_vale_transporte,"+
			                                  "vl_fgts,"+
			                                  "vl_irrf,"+
			                                  "pr_fgts,"+
			                                  "pr_inss,"+
			                                  "pr_irrf,"+
			                                  "pr_inss_patronal,"+
			                                  "vl_parcela_deducao_irrf,"+
			                                  "vl_deducao_dependente_irrf,"+
			                                  "vl_salario_familia,"+
			                                  "vl_base_inss_patronal,"+
			                                  "vl_salario_comissao,"+
			                                  "vl_sat,"+
			                                  "cd_vinculo_empregaticio,"+
			                                  "cd_setor,"+
			                                  "cd_evento_principal,"+
			                                  "cd_convenio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdFolhaPagamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFolhaPagamento());
			pstmt.setInt(3,objeto.getQtDiasTrabalhados());
			pstmt.setInt(4,objeto.getQtHorasMes());
			pstmt.setFloat(5,objeto.getVlBaseIrrf());
			pstmt.setFloat(6,objeto.getVlBaseInss());
			pstmt.setFloat(7,objeto.getVlBaseFgts());
			pstmt.setFloat(8,objeto.getVlInss());
			pstmt.setFloat(9,objeto.getVlInssPatronal());
			pstmt.setFloat(10,objeto.getVlTotalProvento());
			pstmt.setFloat(11,objeto.getVlTotalDesconto());
			pstmt.setFloat(12,objeto.getVlHora());
			pstmt.setFloat(13,objeto.getVlDia());
			pstmt.setFloat(14,objeto.getVlProventoPrincipal());
			pstmt.setFloat(15,objeto.getVlValeTransporte());
			pstmt.setFloat(16,objeto.getVlFgts());
			pstmt.setFloat(17,objeto.getVlIrrf());
			pstmt.setFloat(18,objeto.getPrFgts());
			pstmt.setFloat(19,objeto.getPrInss());
			pstmt.setFloat(20,objeto.getPrIrrf());
			pstmt.setFloat(21,objeto.getPrInssPatronal());
			pstmt.setFloat(22,objeto.getVlParcelaDeducaoIrrf());
			pstmt.setFloat(23,objeto.getVlDeducaoDependenteIrrf());
			pstmt.setFloat(24,objeto.getVlSalarioFamilia());
			pstmt.setFloat(25,objeto.getVlBaseInssPatronal());
			pstmt.setFloat(26,objeto.getVlSalarioComissao());
			pstmt.setFloat(27,objeto.getVlSat());
			if(objeto.getCdVinculoEmpregaticio()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdVinculoEmpregaticio());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdSetor());
			if(objeto.getCdEventoPrincipal()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdEventoPrincipal());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdConvenio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FolhaPagamentoFuncionario objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(FolhaPagamentoFuncionario objeto, int cdMatriculaOld, int cdFolhaPagamentoOld) {
		return update(objeto, cdMatriculaOld, cdFolhaPagamentoOld, null);
	}

	public static int update(FolhaPagamentoFuncionario objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(FolhaPagamentoFuncionario objeto, int cdMatriculaOld, int cdFolhaPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_folha_pagamento_funcionario SET cd_matricula=?,"+
												      		   "cd_folha_pagamento=?,"+
												      		   "qt_dias_trabalhados=?,"+
												      		   "qt_horas_mes=?,"+
												      		   "vl_base_irrf=?,"+
												      		   "vl_base_inss=?,"+
												      		   "vl_base_fgts=?,"+
												      		   "vl_inss=?,"+
												      		   "vl_inss_patronal=?,"+
												      		   "vl_total_provento=?,"+
												      		   "vl_total_desconto=?,"+
												      		   "vl_hora=?,"+
												      		   "vl_dia=?,"+
												      		   "vl_provento_principal=?,"+
												      		   "vl_vale_transporte=?,"+
												      		   "vl_fgts=?,"+
												      		   "vl_irrf=?,"+
												      		   "pr_fgts=?,"+
												      		   "pr_inss=?,"+
												      		   "pr_irrf=?,"+
												      		   "pr_inss_patronal=?,"+
												      		   "vl_parcela_deducao_irrf=?,"+
												      		   "vl_deducao_dependente_irrf=?,"+
												      		   "vl_salario_familia=?,"+
												      		   "vl_base_inss_patronal=?,"+
												      		   "vl_salario_comissao=?,"+
												      		   "vl_sat=?,"+
												      		   "cd_vinculo_empregaticio=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_evento_principal=?,"+
												      		   "cd_convenio=? WHERE cd_matricula=? AND cd_folha_pagamento=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdFolhaPagamento());
			pstmt.setInt(3,objeto.getQtDiasTrabalhados());
			pstmt.setInt(4,objeto.getQtHorasMes());
			pstmt.setFloat(5,objeto.getVlBaseIrrf());
			pstmt.setFloat(6,objeto.getVlBaseInss());
			pstmt.setFloat(7,objeto.getVlBaseFgts());
			pstmt.setFloat(8,objeto.getVlInss());
			pstmt.setFloat(9,objeto.getVlInssPatronal());
			pstmt.setFloat(10,objeto.getVlTotalProvento());
			pstmt.setFloat(11,objeto.getVlTotalDesconto());
			pstmt.setFloat(12,objeto.getVlHora());
			pstmt.setFloat(13,objeto.getVlDia());
			pstmt.setFloat(14,objeto.getVlProventoPrincipal());
			pstmt.setFloat(15,objeto.getVlValeTransporte());
			pstmt.setFloat(16,objeto.getVlFgts());
			pstmt.setFloat(17,objeto.getVlIrrf());
			pstmt.setFloat(18,objeto.getPrFgts());
			pstmt.setFloat(19,objeto.getPrInss());
			pstmt.setFloat(20,objeto.getPrIrrf());
			pstmt.setFloat(21,objeto.getPrInssPatronal());
			pstmt.setFloat(22,objeto.getVlParcelaDeducaoIrrf());
			pstmt.setFloat(23,objeto.getVlDeducaoDependenteIrrf());
			pstmt.setFloat(24,objeto.getVlSalarioFamilia());
			pstmt.setFloat(25,objeto.getVlBaseInssPatronal());
			pstmt.setFloat(26,objeto.getVlSalarioComissao());
			pstmt.setFloat(27,objeto.getVlSat());
			if(objeto.getCdVinculoEmpregaticio()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdVinculoEmpregaticio());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdSetor());
			if(objeto.getCdEventoPrincipal()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdEventoPrincipal());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdConvenio());
			pstmt.setInt(32, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(33, cdFolhaPagamentoOld!=0 ? cdFolhaPagamentoOld : objeto.getCdFolhaPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdFolhaPagamento) {
		return delete(cdMatricula, cdFolhaPagamento, null);
	}

	public static int delete(int cdMatricula, int cdFolhaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_folha_pagamento_funcionario WHERE cd_matricula=? AND cd_folha_pagamento=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdFolhaPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FolhaPagamentoFuncionario get(int cdMatricula, int cdFolhaPagamento) {
		return get(cdMatricula, cdFolhaPagamento, null);
	}

	public static FolhaPagamentoFuncionario get(int cdMatricula, int cdFolhaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_folha_pagamento_funcionario WHERE cd_matricula=? AND cd_folha_pagamento=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdFolhaPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FolhaPagamentoFuncionario(rs.getInt("cd_matricula"),
						rs.getInt("cd_folha_pagamento"),
						rs.getInt("qt_dias_trabalhados"),
						rs.getInt("qt_horas_mes"),
						rs.getFloat("vl_base_irrf"),
						rs.getFloat("vl_base_inss"),
						rs.getFloat("vl_base_fgts"),
						rs.getFloat("vl_inss"),
						rs.getFloat("vl_inss_patronal"),
						rs.getFloat("vl_total_provento"),
						rs.getFloat("vl_total_desconto"),
						rs.getFloat("vl_hora"),
						rs.getFloat("vl_dia"),
						rs.getFloat("vl_provento_principal"),
						rs.getFloat("vl_vale_transporte"),
						rs.getFloat("vl_fgts"),
						rs.getFloat("vl_irrf"),
						rs.getFloat("pr_fgts"),
						rs.getFloat("pr_inss"),
						rs.getFloat("pr_irrf"),
						rs.getFloat("pr_inss_patronal"),
						rs.getFloat("vl_parcela_deducao_irrf"),
						rs.getFloat("vl_deducao_dependente_irrf"),
						rs.getFloat("vl_salario_familia"),
						rs.getFloat("vl_base_inss_patronal"),
						rs.getFloat("vl_salario_comissao"),
						rs.getFloat("vl_sat"),
						rs.getInt("cd_vinculo_empregaticio"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_evento_principal"),
						rs.getInt("cd_convenio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_folha_pagamento_funcionario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaPagamentoFuncionarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM flp_folha_pagamento_funcionario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
