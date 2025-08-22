package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProgramaRepasseProgramacaoDAO{

	public static int insert(ProgramaRepasseProgramacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProgramaRepasseProgramacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_programa_repasse_programacao (cd_programa,"+
			                                  "cd_fonte_receita,"+
			                                  "cd_centro_custo_despesa,"+
			                                  "cd_categoria_economica_despesa,"+
			                                  "pr_programacao_min,"+
			                                  "pr_programacao_max,"+
			                                  "cd_unidade_executora,"+
			                                  "cd_instituicao,"+
			                                  "cd_exercicio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPrograma()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPrograma());
			if(objeto.getCdFonteReceita()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFonteReceita());
			if(objeto.getCdCentroCustoDespesa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCentroCustoDespesa());
			if(objeto.getCdCategoriaEconomicaDespesa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCategoriaEconomicaDespesa());
			pstmt.setDouble(5,objeto.getPrProgramacaoMin());
			pstmt.setDouble(6,objeto.getPrProgramacaoMax());
			if(objeto.getCdUnidadeExecutora()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUnidadeExecutora());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdInstituicao());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdExercicio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProgramaRepasseProgramacao objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(ProgramaRepasseProgramacao objeto, int cdProgramaOld, int cdFonteReceitaOld, int cdCentroCustoDespesaOld, int cdCategoriaEconomicaDespesaOld) {
		return update(objeto, cdProgramaOld, cdFonteReceitaOld, cdCentroCustoDespesaOld, cdCategoriaEconomicaDespesaOld, null);
	}

	public static int update(ProgramaRepasseProgramacao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(ProgramaRepasseProgramacao objeto, int cdProgramaOld, int cdFonteReceitaOld, int cdCentroCustoDespesaOld, int cdCategoriaEconomicaDespesaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_programa_repasse_programacao SET cd_programa=?,"+
												      		   "cd_fonte_receita=?,"+
												      		   "cd_centro_custo_despesa=?,"+
												      		   "cd_categoria_economica_despesa=?,"+
												      		   "pr_programacao_min=?,"+
												      		   "pr_programacao_max=?,"+
												      		   "cd_unidade_executora=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_exercicio=? WHERE cd_programa=? AND cd_fonte_receita=? AND cd_centro_custo_despesa=? AND cd_categoria_economica_despesa=?");
			pstmt.setInt(1,objeto.getCdPrograma());
			pstmt.setInt(2,objeto.getCdFonteReceita());
			pstmt.setInt(3,objeto.getCdCentroCustoDespesa());
			pstmt.setInt(4,objeto.getCdCategoriaEconomicaDespesa());
			pstmt.setDouble(5,objeto.getPrProgramacaoMin());
			pstmt.setDouble(6,objeto.getPrProgramacaoMax());
			if(objeto.getCdUnidadeExecutora()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUnidadeExecutora());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdInstituicao());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdExercicio());
			pstmt.setInt(10, cdProgramaOld!=0 ? cdProgramaOld : objeto.getCdPrograma());
			pstmt.setInt(11, cdFonteReceitaOld!=0 ? cdFonteReceitaOld : objeto.getCdFonteReceita());
			pstmt.setInt(12, cdCentroCustoDespesaOld!=0 ? cdCentroCustoDespesaOld : objeto.getCdCentroCustoDespesa());
			pstmt.setInt(13, cdCategoriaEconomicaDespesaOld!=0 ? cdCategoriaEconomicaDespesaOld : objeto.getCdCategoriaEconomicaDespesa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPrograma, int cdFonteReceita, int cdCentroCustoDespesa, int cdCategoriaEconomicaDespesa) {
		return delete(cdPrograma, cdFonteReceita, cdCentroCustoDespesa, cdCategoriaEconomicaDespesa, null);
	}

	public static int delete(int cdPrograma, int cdFonteReceita, int cdCentroCustoDespesa, int cdCategoriaEconomicaDespesa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_programa_repasse_programacao WHERE cd_programa=? AND cd_fonte_receita=? AND cd_centro_custo_despesa=? AND cd_categoria_economica_despesa=?");
			pstmt.setInt(1, cdPrograma);
			pstmt.setInt(2, cdFonteReceita);
			pstmt.setInt(3, cdCentroCustoDespesa);
			pstmt.setInt(4, cdCategoriaEconomicaDespesa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProgramaRepasseProgramacao get(int cdPrograma, int cdFonteReceita, int cdCentroCustoDespesa, int cdCategoriaEconomicaDespesa) {
		return get(cdPrograma, cdFonteReceita, cdCentroCustoDespesa, cdCategoriaEconomicaDespesa, null);
	}

	public static ProgramaRepasseProgramacao get(int cdPrograma, int cdFonteReceita, int cdCentroCustoDespesa, int cdCategoriaEconomicaDespesa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_repasse_programacao WHERE cd_programa=? AND cd_fonte_receita=? AND cd_centro_custo_despesa=? AND cd_categoria_economica_despesa=?");
			pstmt.setInt(1, cdPrograma);
			pstmt.setInt(2, cdFonteReceita);
			pstmt.setInt(3, cdCentroCustoDespesa);
			pstmt.setInt(4, cdCategoriaEconomicaDespesa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProgramaRepasseProgramacao(rs.getInt("cd_programa"),
						rs.getInt("cd_fonte_receita"),
						rs.getInt("cd_centro_custo_despesa"),
						rs.getInt("cd_categoria_economica_despesa"),
						rs.getDouble("pr_programacao_min"),
						rs.getDouble("pr_programacao_max"),
						rs.getInt("cd_unidade_executora"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_exercicio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_repasse_programacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProgramaRepasseProgramacao> getList() {
		return getList(null);
	}

	public static ArrayList<ProgramaRepasseProgramacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProgramaRepasseProgramacao> list = new ArrayList<ProgramaRepasseProgramacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProgramaRepasseProgramacao obj = ProgramaRepasseProgramacaoDAO.get(rsm.getInt("cd_programa"), rsm.getInt("cd_fonte_receita"), rsm.getInt("cd_centro_custo_despesa"), rsm.getInt("cd_categoria_economica_despesa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseProgramacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_programa_repasse_programacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
