package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class EmpresaExercicioDAO{

	public static int insert(EmpresaExercicio objeto) {
		return insert(objeto, null);
	}

	public static int insert(EmpresaExercicio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_empresa_exercicio (cd_empresa,"+
			                                  "nr_ano_exercicio,"+
			                                  "cd_plano_contas,"+
			                                  "cd_contador,"+
			                                  "cd_estado_crc,"+
			                                  "dt_inicio,"+
			                                  "dt_encerramento,"+
			                                  "vl_capital_social,"+
			                                  "tp_calculo_irpj,"+
			                                  "nr_crc_contador,"+
			                                  "nr_livro_razao,"+
			                                  "nr_pagina_razao,"+
			                                  "nr_livro_diario,"+
			                                  "nr_pagina_diario,"+
			                                  "nr_livro_caixa,"+
			                                  "nr_pagina_caixa,"+
			                                  "tp_termos,"+
			                                  "lg_lote,"+
			                                  "cd_plano_centro_custo,"+
			                                  "st_exercicio,"+
			                                  "cd_responsavel_encerramento,"+
			                                  "dt_termino,"+
			                                  "cd_lancamento_resultado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2, objeto.getNrAnoExercicio());
			if(objeto.getCdPlanoContas()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanoContas());
			if(objeto.getCdContador()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContador());
			if(objeto.getCdEstadoCrc()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEstadoCrc());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtEncerramento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEncerramento().getTimeInMillis()));
			pstmt.setFloat(8,objeto.getVlCapitalSocial());
			pstmt.setInt(9,objeto.getTpCalculoIrpj());
			pstmt.setString(10,objeto.getNrCrcContador());
			pstmt.setInt(11,objeto.getNrLivroRazao());
			pstmt.setInt(12,objeto.getNrPaginaRazao());
			pstmt.setInt(13,objeto.getNrLivroDiario());
			pstmt.setInt(14,objeto.getNrPaginaDiario());
			pstmt.setInt(15,objeto.getNrLivroCaixa());
			pstmt.setInt(16,objeto.getNrPaginaCaixa());
			pstmt.setInt(17,objeto.getTpTermos());
			pstmt.setInt(18,objeto.getLgLote());
			if(objeto.getCdPlanoCentroCusto()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdPlanoCentroCusto());
			pstmt.setInt(20,objeto.getStExercicio());
			if(objeto.getCdResponsavelEncerramento()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdResponsavelEncerramento());
			if(objeto.getDtTermino()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			if(objeto.getCdLancamentoResultado()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdLancamentoResultado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EmpresaExercicio objeto) {
		return update(objeto, 0, null, null);
	}

	public static int update(EmpresaExercicio objeto, int cdEmpresaOld, String nrAnoExercicioOld) {
		return update(objeto, cdEmpresaOld, nrAnoExercicioOld, null);
	}

	public static int update(EmpresaExercicio objeto, Connection connect) {
		return update(objeto, 0, null, connect);
	}

	public static int update(EmpresaExercicio objeto, int cdEmpresaOld, String nrAnoExercicioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_empresa_exercicio SET cd_empresa=?,"+
												      		   "nr_ano_exercicio=?,"+
												      		   "cd_plano_contas=?,"+
												      		   "cd_contador=?,"+
												      		   "cd_estado_crc=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_encerramento=?,"+
												      		   "vl_capital_social=?,"+
												      		   "tp_calculo_irpj=?,"+
												      		   "nr_crc_contador=?,"+
												      		   "nr_livro_razao=?,"+
												      		   "nr_pagina_razao=?,"+
												      		   "nr_livro_diario=?,"+
												      		   "nr_pagina_diario=?,"+
												      		   "nr_livro_caixa=?,"+
												      		   "nr_pagina_caixa=?,"+
												      		   "tp_termos=?,"+
												      		   "lg_lote=?,"+
												      		   "cd_plano_centro_custo=?,"+
												      		   "st_exercicio=?,"+
												      		   "cd_responsavel_encerramento=?,"+
												      		   "dt_termino=?,"+
												      		   "cd_lancamento_resultado=? WHERE cd_empresa=? AND nr_ano_exercicio=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setString(2,objeto.getNrAnoExercicio());
			if(objeto.getCdPlanoContas()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPlanoContas());
			if(objeto.getCdContador()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContador());
			if(objeto.getCdEstadoCrc()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEstadoCrc());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtEncerramento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtEncerramento().getTimeInMillis()));
			pstmt.setFloat(8,objeto.getVlCapitalSocial());
			pstmt.setInt(9,objeto.getTpCalculoIrpj());
			pstmt.setString(10,objeto.getNrCrcContador());
			pstmt.setInt(11,objeto.getNrLivroRazao());
			pstmt.setInt(12,objeto.getNrPaginaRazao());
			pstmt.setInt(13,objeto.getNrLivroDiario());
			pstmt.setInt(14,objeto.getNrPaginaDiario());
			pstmt.setInt(15,objeto.getNrLivroCaixa());
			pstmt.setInt(16,objeto.getNrPaginaCaixa());
			pstmt.setInt(17,objeto.getTpTermos());
			pstmt.setInt(18,objeto.getLgLote());
			if(objeto.getCdPlanoCentroCusto()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdPlanoCentroCusto());
			pstmt.setInt(20,objeto.getStExercicio());
			if(objeto.getCdResponsavelEncerramento()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdResponsavelEncerramento());
			if(objeto.getDtTermino()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			if(objeto.getCdLancamentoResultado()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdLancamentoResultado());
			pstmt.setInt(24, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setString(25, nrAnoExercicioOld!=null ? nrAnoExercicioOld : objeto.getNrAnoExercicio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, String nrAnoExercicio) {
		return delete(cdEmpresa, nrAnoExercicio, null);
	}

	public static int delete(int cdEmpresa, String nrAnoExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_empresa_exercicio WHERE cd_empresa=? AND nr_ano_exercicio=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setString(2, nrAnoExercicio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EmpresaExercicio get(int cdEmpresa, String nrAnoExercicio) {
		return get(cdEmpresa, nrAnoExercicio, null);
	}

	public static EmpresaExercicio get(int cdEmpresa, String nrAnoExercicio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_empresa_exercicio WHERE cd_empresa=? AND nr_ano_exercicio=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setString(2, nrAnoExercicio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EmpresaExercicio(rs.getInt("cd_empresa"),
						rs.getString("nr_ano_exercicio"),
						rs.getInt("cd_plano_contas"),
						rs.getInt("cd_contador"),
						rs.getInt("cd_estado_crc"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_encerramento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_encerramento").getTime()),
						rs.getFloat("vl_capital_social"),
						rs.getInt("tp_calculo_irpj"),
						rs.getString("nr_crc_contador"),
						rs.getInt("nr_livro_razao"),
						rs.getInt("nr_pagina_razao"),
						rs.getInt("nr_livro_diario"),
						rs.getInt("nr_pagina_diario"),
						rs.getInt("nr_livro_caixa"),
						rs.getInt("nr_pagina_caixa"),
						rs.getInt("tp_termos"),
						rs.getInt("lg_lote"),
						rs.getInt("cd_plano_centro_custo"),
						rs.getInt("st_exercicio"),
						rs.getInt("cd_responsavel_encerramento"),
						(rs.getTimestamp("dt_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino").getTime()),
						rs.getInt("cd_lancamento_resultado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_empresa_exercicio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpresaExercicioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_empresa_exercicio", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
