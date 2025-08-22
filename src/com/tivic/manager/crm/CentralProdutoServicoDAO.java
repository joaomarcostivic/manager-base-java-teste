package com.tivic.manager.crm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CentralProdutoServicoDAO{

	public static int insert(CentralProdutoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(CentralProdutoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO crm_central_produto_servico (cd_central,"+
			                                  "cd_produto_servico,"+
			                                  "st_produto_servico) VALUES (?, ?, ?)");
			if(objeto.getCdCentral()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdCentral());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getStProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CentralProdutoServico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CentralProdutoServico objeto, int cdCentralOld, int cdProdutoServicoOld) {
		return update(objeto, cdCentralOld, cdProdutoServicoOld, null);
	}

	public static int update(CentralProdutoServico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CentralProdutoServico objeto, int cdCentralOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE crm_central_produto_servico SET cd_central=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "st_produto_servico=? WHERE cd_central=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdCentral());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getStProdutoServico());
			pstmt.setInt(4, cdCentralOld!=0 ? cdCentralOld : objeto.getCdCentral());
			pstmt.setInt(5, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCentral, int cdProdutoServico) {
		return delete(cdCentral, cdProdutoServico, null);
	}

	public static int delete(int cdCentral, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM crm_central_produto_servico WHERE cd_central=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdCentral);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CentralProdutoServico get(int cdCentral, int cdProdutoServico) {
		return get(cdCentral, cdProdutoServico, null);
	}

	public static CentralProdutoServico get(int cdCentral, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM crm_central_produto_servico WHERE cd_central=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdCentral);
			pstmt.setInt(2, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CentralProdutoServico(rs.getInt("cd_central"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("st_produto_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM crm_central_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CentralProdutoServicoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM crm_central_produto_servico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
