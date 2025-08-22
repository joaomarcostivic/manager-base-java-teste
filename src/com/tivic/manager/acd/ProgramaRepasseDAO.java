package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProgramaRepasseDAO{

	public static int insert(ProgramaRepasse objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProgramaRepasse objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_programa_repasse (cd_programa,"+
			                                  "cd_fonte_receita,"+
			                                  "cd_categoria_economica_receita,"+
			                                  "cd_centro_custo_receita) VALUES (?, ?, ?, ?)");
			if(objeto.getCdPrograma()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPrograma());
			if(objeto.getCdFonteReceita()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFonteReceita());
			if(objeto.getCdCategoriaEconomicaReceita()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaEconomicaReceita());
			if(objeto.getCdCentroCustoReceita()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCustoReceita());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProgramaRepasse objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProgramaRepasse objeto, int cdProgramaOld, int cdFonteReceitaOld) {
		return update(objeto, cdProgramaOld, cdFonteReceitaOld, null);
	}

	public static int update(ProgramaRepasse objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProgramaRepasse objeto, int cdProgramaOld, int cdFonteReceitaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_programa_repasse SET cd_programa=?,"+
												      		   "cd_fonte_receita=?,"+
												      		   "cd_categoria_economica_receita=?,"+
												      		   "cd_centro_custo_receita=? WHERE cd_programa=? AND cd_fonte_receita=?");
			pstmt.setInt(1,objeto.getCdPrograma());
			pstmt.setInt(2,objeto.getCdFonteReceita());
			if(objeto.getCdCategoriaEconomicaReceita()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaEconomicaReceita());
			if(objeto.getCdCentroCustoReceita()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCustoReceita());
			pstmt.setInt(5, cdProgramaOld!=0 ? cdProgramaOld : objeto.getCdPrograma());
			pstmt.setInt(6, cdFonteReceitaOld!=0 ? cdFonteReceitaOld : objeto.getCdFonteReceita());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPrograma, int cdFonteReceita) {
		return delete(cdPrograma, cdFonteReceita, null);
	}

	public static int delete(int cdPrograma, int cdFonteReceita, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_programa_repasse WHERE cd_programa=? AND cd_fonte_receita=?");
			pstmt.setInt(1, cdPrograma);
			pstmt.setInt(2, cdFonteReceita);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProgramaRepasse get(int cdPrograma, int cdFonteReceita) {
		return get(cdPrograma, cdFonteReceita, null);
	}

	public static ProgramaRepasse get(int cdPrograma, int cdFonteReceita, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_repasse WHERE cd_programa=? AND cd_fonte_receita=?");
			pstmt.setInt(1, cdPrograma);
			pstmt.setInt(2, cdFonteReceita);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProgramaRepasse(rs.getInt("cd_programa"),
						rs.getInt("cd_fonte_receita"),
						rs.getInt("cd_categoria_economica_receita"),
						rs.getInt("cd_centro_custo_receita"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_programa_repasse");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProgramaRepasse> getList() {
		return getList(null);
	}

	public static ArrayList<ProgramaRepasse> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProgramaRepasse> list = new ArrayList<ProgramaRepasse>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProgramaRepasse obj = ProgramaRepasseDAO.get(rsm.getInt("cd_programa"), rsm.getInt("cd_fonte_receita"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaRepasseDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_programa_repasse", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
