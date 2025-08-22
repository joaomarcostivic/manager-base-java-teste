package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class OrcamentoDAO{

	public static int insert(Orcamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Orcamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_orcamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrcamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_orcamento (cd_orcamento,"+
			                                  "nm_orcamento,"+
			                                  "cd_empresa,"+
			                                  "cd_exercicio,"+
			                                  "cd_centro_custo) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmOrcamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdExercicio());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCentroCusto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Orcamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Orcamento objeto, int cdOrcamentoOld) {
		return update(objeto, cdOrcamentoOld, null);
	}

	public static int update(Orcamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Orcamento objeto, int cdOrcamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_orcamento SET cd_orcamento=?,"+
												      		   "nm_orcamento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_exercicio=?,"+
												      		   "cd_centro_custo=? WHERE cd_orcamento=?");
			pstmt.setInt(1,objeto.getCdOrcamento());
			pstmt.setString(2,objeto.getNmOrcamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdExercicio()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdExercicio());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCentroCusto());
			pstmt.setInt(6, cdOrcamentoOld!=0 ? cdOrcamentoOld : objeto.getCdOrcamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrcamento) {
		return delete(cdOrcamento, null);
	}

	public static int delete(int cdOrcamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_orcamento WHERE cd_orcamento=?");
			pstmt.setInt(1, cdOrcamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Orcamento get(int cdOrcamento) {
		return get(cdOrcamento, null);
	}

	public static Orcamento get(int cdOrcamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_orcamento WHERE cd_orcamento=?");
			pstmt.setInt(1, cdOrcamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Orcamento(rs.getInt("cd_orcamento"),
						rs.getString("nm_orcamento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_exercicio"),
						rs.getInt("cd_centro_custo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_orcamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Orcamento> getList() {
		return getList(null);
	}

	public static ArrayList<Orcamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Orcamento> list = new ArrayList<Orcamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Orcamento obj = OrcamentoDAO.get(rsm.getInt("cd_orcamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrcamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_orcamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}