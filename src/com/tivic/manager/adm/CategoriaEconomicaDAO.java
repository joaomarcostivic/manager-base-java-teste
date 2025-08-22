package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CategoriaEconomicaDAO{

	public static int insert(CategoriaEconomica objeto) {
		return insert(objeto, null);
	}

	public static int insert(CategoriaEconomica objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_categoria_economica", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCategoriaEconomica(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_categoria_economica (cd_categoria_economica,"+
			                                  "cd_categoria_superior,"+
			                                  "nm_categoria_economica,"+
			                                  "tp_categoria_economica,"+
			                                  "nr_categoria_economica,"+
			                                  "id_categoria_economica,"+
			                                  "nr_nivel,"+
			                                  "cd_tabela_cat_economica,"+
			                                  "lg_ativo,"+
			                                  "vl_aliquota,"+
			                                  "lg_lancar_faturamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCategoriaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaSuperior());
			pstmt.setString(3,objeto.getNmCategoriaEconomica());
			pstmt.setInt(4,objeto.getTpCategoriaEconomica());
			pstmt.setString(5,objeto.getNrCategoriaEconomica());
			pstmt.setString(6,objeto.getIdCategoriaEconomica());
			pstmt.setInt(7,objeto.getNrNivel());
			pstmt.setInt(8,objeto.getCdTabelaCatEconomica());
			pstmt.setInt(9,objeto.getLgAtivo());
			pstmt.setDouble(10,objeto.getVlAliquota());
			pstmt.setInt(11,objeto.getLgLancarFaturamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CategoriaEconomica objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CategoriaEconomica objeto, int cdCategoriaEconomicaOld) {
		return update(objeto, cdCategoriaEconomicaOld, null);
	}

	public static int update(CategoriaEconomica objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CategoriaEconomica objeto, int cdCategoriaEconomicaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_categoria_economica SET cd_categoria_economica=?,"+
												      		   "cd_categoria_superior=?,"+
												      		   "nm_categoria_economica=?,"+
												      		   "tp_categoria_economica=?,"+
												      		   "nr_categoria_economica=?,"+
												      		   "id_categoria_economica=?,"+
												      		   "nr_nivel=?,"+
												      		   "cd_tabela_cat_economica=?,"+
												      		   "lg_ativo=?,"+
												      		   "vl_aliquota=?,"+
												      		   "lg_lancar_faturamento=? WHERE cd_categoria_economica=?");
			pstmt.setInt(1,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCategoriaSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaSuperior());
			pstmt.setString(3,objeto.getNmCategoriaEconomica());
			pstmt.setInt(4,objeto.getTpCategoriaEconomica());
			pstmt.setString(5,objeto.getNrCategoriaEconomica());
			pstmt.setString(6,objeto.getIdCategoriaEconomica());
			pstmt.setInt(7,objeto.getNrNivel());
			pstmt.setInt(8,objeto.getCdTabelaCatEconomica());
			pstmt.setInt(9,objeto.getLgAtivo());
			pstmt.setDouble(10,objeto.getVlAliquota());
			pstmt.setInt(11,objeto.getLgLancarFaturamento());
			pstmt.setInt(12, cdCategoriaEconomicaOld!=0 ? cdCategoriaEconomicaOld : objeto.getCdCategoriaEconomica());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCategoriaEconomica) {
		return delete(cdCategoriaEconomica, null);
	}

	public static int delete(int cdCategoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_categoria_economica WHERE cd_categoria_economica=?");
			pstmt.setInt(1, cdCategoriaEconomica);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CategoriaEconomica get(int cdCategoriaEconomica) {
		return get(cdCategoriaEconomica, null);
	}

	public static CategoriaEconomica get(int cdCategoriaEconomica, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_categoria_economica WHERE cd_categoria_economica=?");
			pstmt.setInt(1, cdCategoriaEconomica);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CategoriaEconomica(rs.getInt("cd_categoria_economica"),
						rs.getInt("cd_categoria_superior"),
						rs.getString("nm_categoria_economica"),
						rs.getInt("tp_categoria_economica"),
						rs.getString("nr_categoria_economica"),
						rs.getString("id_categoria_economica"),
						rs.getInt("nr_nivel"),
						rs.getInt("cd_tabela_cat_economica"),
						rs.getInt("lg_ativo"),
						rs.getDouble("vl_aliquota"),
						rs.getInt("lg_lancar_faturamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_categoria_economica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CategoriaEconomica> getList() {
		return getList(null);
	}

	public static ArrayList<CategoriaEconomica> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CategoriaEconomica> list = new ArrayList<CategoriaEconomica>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CategoriaEconomica obj = CategoriaEconomicaDAO.get(rsm.getInt("cd_categoria_economica"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CategoriaEconomicaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_categoria_economica", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}