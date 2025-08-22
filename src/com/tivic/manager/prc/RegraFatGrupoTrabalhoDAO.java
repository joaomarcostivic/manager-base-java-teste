package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFatGrupoTrabalhoDAO{

	public static int insert(RegraFatGrupoTrabalho objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatGrupoTrabalho objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_regra_fat_grupo_trabalho (cd_regra_faturamento,"+
			                                  "cd_grupo_trabalho) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdGrupoTrabalho()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoTrabalho());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatGrupoTrabalho objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatGrupoTrabalho objeto, int cdRegraFaturamentoOld, int cdGrupoTrabalhoOld) {
		return update(objeto, cdRegraFaturamentoOld, cdGrupoTrabalhoOld, null);
	}

	public static int update(RegraFatGrupoTrabalho objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatGrupoTrabalho objeto, int cdRegraFaturamentoOld, int cdGrupoTrabalhoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_regra_fat_grupo_trabalho SET cd_regra_faturamento=?,"+
												      		   "cd_grupo_trabalho=? WHERE cd_regra_faturamento=? AND cd_grupo_trabalho=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getCdGrupoTrabalho());
			pstmt.setInt(3, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.setInt(4, cdGrupoTrabalhoOld!=0 ? cdGrupoTrabalhoOld : objeto.getCdGrupoTrabalho());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraFaturamento, int cdGrupoTrabalho) {
		return delete(cdRegraFaturamento, cdGrupoTrabalho, null);
	}

	public static int delete(int cdRegraFaturamento, int cdGrupoTrabalho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_regra_fat_grupo_trabalho WHERE cd_regra_faturamento=? AND cd_grupo_trabalho=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdGrupoTrabalho);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatGrupoTrabalho get(int cdRegraFaturamento, int cdGrupoTrabalho) {
		return get(cdRegraFaturamento, cdGrupoTrabalho, null);
	}

	public static RegraFatGrupoTrabalho get(int cdRegraFaturamento, int cdGrupoTrabalho, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_grupo_trabalho WHERE cd_regra_faturamento=? AND cd_grupo_trabalho=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdGrupoTrabalho);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatGrupoTrabalho(rs.getInt("cd_regra_faturamento"),
						rs.getInt("cd_grupo_trabalho"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_regra_fat_grupo_trabalho");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatGrupoTrabalho> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatGrupoTrabalho> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatGrupoTrabalho> list = new ArrayList<RegraFatGrupoTrabalho>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatGrupoTrabalho obj = RegraFatGrupoTrabalhoDAO.get(rsm.getInt("cd_regra_faturamento"), rsm.getInt("cd_grupo_trabalho"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoTrabalhoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_regra_fat_grupo_trabalho", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
