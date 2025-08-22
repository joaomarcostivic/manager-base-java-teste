package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CidadeOrgaoServicoDAO{

	public static int insert(CidadeOrgaoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(CidadeOrgaoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_cidade_orgao_servico (cd_orgao,"+
			                                  "cd_cidade,"+
			                                  "cd_produto_servico,"+
			                                  "vl_servico) VALUES (?, ?, ?, ?)");
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOrgao());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidade());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setDouble(4,objeto.getVlServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CidadeOrgaoServico objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(CidadeOrgaoServico objeto, int cdOrgaoOld, int cdCidadeOld, int cdProdutoServicoOld) {
		return update(objeto, cdOrgaoOld, cdCidadeOld, cdProdutoServicoOld, null);
	}

	public static int update(CidadeOrgaoServico objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(CidadeOrgaoServico objeto, int cdOrgaoOld, int cdCidadeOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_cidade_orgao_servico SET cd_orgao=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "vl_servico=? WHERE cd_orgao=? AND cd_cidade=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdOrgao());
			pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setDouble(4,objeto.getVlServico());
			pstmt.setInt(5, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			pstmt.setInt(6, cdCidadeOld!=0 ? cdCidadeOld : objeto.getCdCidade());
			pstmt.setInt(7, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrgao, int cdCidade, int cdProdutoServico) {
		return delete(cdOrgao, cdCidade, cdProdutoServico, null);
	}

	public static int delete(int cdOrgao, int cdCidade, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_cidade_orgao_servico WHERE cd_orgao=? AND cd_cidade=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.setInt(2, cdCidade);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CidadeOrgaoServico get(int cdOrgao, int cdCidade, int cdProdutoServico) {
		return get(cdOrgao, cdCidade, cdProdutoServico, null);
	}

	public static CidadeOrgaoServico get(int cdOrgao, int cdCidade, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_cidade_orgao_servico WHERE cd_orgao=? AND cd_cidade=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdOrgao);
			pstmt.setInt(2, cdCidade);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CidadeOrgaoServico(rs.getInt("cd_orgao"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_produto_servico"),
						rs.getDouble("vl_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_cidade_orgao_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CidadeOrgaoServico> getList() {
		return getList(null);
	}

	public static ArrayList<CidadeOrgaoServico> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CidadeOrgaoServico> list = new ArrayList<CidadeOrgaoServico>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CidadeOrgaoServico obj = CidadeOrgaoServicoDAO.get(rsm.getInt("cd_orgao"), rsm.getInt("cd_cidade"), rsm.getInt("cd_produto_servico"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServicoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_cidade_orgao_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
