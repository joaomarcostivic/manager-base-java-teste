package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrcamentoCategoriaDAO{

	public static int insert(OrcamentoCategoria objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrcamentoCategoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_orcamento_categoria (cd_orcamento,"+
			                                  "cd_categoria_economica,"+
			                                  "cd_competencia,"+
			                                  "cd_exercicio,"+
			                                  "vl_orcado) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdOrcamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOrcamento());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCompetencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCompetencia());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdExercicio());
			pstmt.setInt(5,objeto.getVlOrcado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrcamentoCategoria objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(OrcamentoCategoria objeto, int cdOrcamentoOld, int cdCategoriaEconomicaOld, int cdCompetenciaOld) {
		return update(objeto, cdOrcamentoOld, cdCategoriaEconomicaOld, cdCompetenciaOld, null);
	}

	public static int update(OrcamentoCategoria objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(OrcamentoCategoria objeto, int cdOrcamentoOld, int cdCategoriaEconomicaOld, int cdCompetenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_orcamento_categoria SET cd_orcamento=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "cd_competencia=?,"+
												      		   "cd_exercicio=?,"+
												      		   "vl_orcado=? WHERE cd_orcamento=? AND cd_categoria_economica=? AND cd_competencia=?");
			pstmt.setInt(1,objeto.getCdOrcamento());
			pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setInt(3,objeto.getCdCompetencia());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdExercicio());
			pstmt.setInt(5,objeto.getVlOrcado());
			pstmt.setInt(6, cdOrcamentoOld!=0 ? cdOrcamentoOld : objeto.getCdOrcamento());
			pstmt.setInt(7, cdCategoriaEconomicaOld!=0 ? cdCategoriaEconomicaOld : objeto.getCdCategoriaEconomica());
			pstmt.setInt(8, cdCompetenciaOld!=0 ? cdCompetenciaOld : objeto.getCdCompetencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia) {
		return delete(cdOrcamento, cdCategoriaEconomica, cdCompetencia, null);
	}

	public static int delete(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_orcamento_categoria WHERE cd_orcamento=? AND cd_categoria_economica=? AND cd_competencia=?");
			pstmt.setInt(1, cdOrcamento);
			pstmt.setInt(2, cdCategoriaEconomica);
			pstmt.setInt(3, cdCompetencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrcamentoCategoria get(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia) {
		return get(cdOrcamento, cdCategoriaEconomica, cdCompetencia, null);
	}

	public static OrcamentoCategoria get(int cdOrcamento, int cdCategoriaEconomica, int cdCompetencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_orcamento_categoria WHERE cd_orcamento=? AND cd_categoria_economica=? AND cd_competencia=?");
			pstmt.setInt(1, cdOrcamento);
			pstmt.setInt(2, cdCategoriaEconomica);
			pstmt.setInt(3, cdCompetencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrcamentoCategoria(rs.getInt("cd_orcamento"),
						rs.getInt("cd_categoria_economica"),
						rs.getInt("cd_competencia"),
						rs.getInt("cd_exercicio"),
						rs.getInt("vl_orcado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_orcamento_categoria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrcamentoCategoria> getList() {
		return getList(null);
	}

	public static ArrayList<OrcamentoCategoria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrcamentoCategoria> list = new ArrayList<OrcamentoCategoria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrcamentoCategoria obj = OrcamentoCategoriaDAO.get(rsm.getInt("cd_orcamento"), rsm.getInt("cd_categoria_economica"), rsm.getInt("cd_competencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoCategoriaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_orcamento_categoria", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}