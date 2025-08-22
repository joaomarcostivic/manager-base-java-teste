package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoGrupoDAO{

	public static int insert(ProdutoGrupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoGrupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_produto_grupo (cd_produto_servico,"+
			                                  "cd_grupo,"+
			                                  "cd_empresa,"+
			                                  "lg_principal) VALUES (?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupo());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getLgPrincipal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoGrupo objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ProdutoGrupo objeto, int cdProdutoServicoOld, int cdGrupoOld, int cdEmpresaOld) {
		return update(objeto, cdProdutoServicoOld, cdGrupoOld, cdEmpresaOld, null);
	}

	public static int update(ProdutoGrupo objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ProdutoGrupo objeto, int cdProdutoServicoOld, int cdGrupoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_produto_grupo SET cd_produto_servico=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_empresa=?,"+
												      		   "lg_principal=? WHERE cd_produto_servico=? AND cd_grupo=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdGrupo());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getLgPrincipal());
			pstmt.setInt(5, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(6, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			pstmt.setInt(7, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico, int cdGrupo, int cdEmpresa) {
		return delete(cdProdutoServico, cdGrupo, cdEmpresa, null);
	}

	public static int delete(int cdProdutoServico, int cdGrupo, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_produto_grupo WHERE cd_produto_servico=? AND cd_grupo=? AND cd_empresa=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdGrupo);
			pstmt.setInt(3, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoGrupo get(int cdProdutoServico, int cdGrupo, int cdEmpresa) {
		return get(cdProdutoServico, cdGrupo, cdEmpresa, null);
	}

	public static ProdutoGrupo get(int cdProdutoServico, int cdGrupo, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_produto_grupo WHERE cd_produto_servico=? AND cd_grupo=? AND cd_empresa=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdGrupo);
			pstmt.setInt(3, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoGrupo(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_empresa"),
						rs.getInt("lg_principal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_produto_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoGrupoDAO.getAll: " + e);
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

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT * FROM alm_produto_grupo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
