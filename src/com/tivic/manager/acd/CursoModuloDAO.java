package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServicoDAO;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.ArrayList;

public class CursoModuloDAO{

	public static int insert(CursoModulo objeto) {
		return insert(objeto, null);
	}

	public static int insert(CursoModulo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = ProdutoServicoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProdutoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso_modulo (cd_curso_modulo,"+
			                                  "cd_curso,"+
			                                  "nr_ordem) VALUES (?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setInt(3,objeto.getNrOrdem());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CursoModulo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CursoModulo objeto, int cdCursoModuloOld) {
		return update(objeto, cdCursoModuloOld, null);
	}

	public static int update(CursoModulo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CursoModulo objeto, int cdCursoModuloOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			CursoModulo objetoTemp = get(objeto.getCdProdutoServico(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_curso_modulo (cd_curso_modulo,"+
			                                  "cd_curso,"+
			                                  "nr_ordem) VALUES (?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_curso_modulo SET cd_curso_modulo=?,"+
												      		   "cd_curso=?,"+
												      		   "nr_ordem=? WHERE cd_curso_modulo=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdCurso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCurso());
			pstmt.setInt(3,objeto.getNrOrdem());
			if (objetoTemp != null) {
				pstmt.setInt(4, cdCursoModuloOld!=0 ? cdCursoModuloOld : objeto.getCdProdutoServico());
			}
			pstmt.executeUpdate();
			if (ProdutoServicoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCursoModulo) {
		return delete(cdCursoModulo, null);
	}

	public static int delete(int cdCursoModulo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso_modulo WHERE cd_curso_modulo=?");
			pstmt.setInt(1, cdCursoModulo);
			pstmt.executeUpdate();
			if (ProdutoServicoDAO.delete(cdCursoModulo, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CursoModulo get(int cdCursoModulo) {
		return get(cdCursoModulo, null);
	}

	public static CursoModulo get(int cdCursoModulo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_modulo A, grl_produto_servico B WHERE A.cd_curso_modulo=B.cd_produto_servico AND A.cd_curso_modulo=?");
			pstmt.setInt(1, cdCursoModulo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoModulo(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_categoria_economica"),
						rs.getString("nm_produto_servico"),
						rs.getString("txt_produto_servico"),
						rs.getString("txt_especificacao"),
						rs.getString("txt_dado_tecnico"),
						rs.getString("txt_prazo_entrega"),
						rs.getInt("tp_produto_servico"),
						rs.getString("id_produto_servico"),
						rs.getString("sg_produto_servico"),
						rs.getInt("cd_classificacao_fiscal"),
						rs.getInt("cd_fabricante"),
						rs.getInt("cd_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("cd_ncm"),
						rs.getString("nr_referencia"),
						rs.getInt("cd_categoria_receita"),
						rs.getInt("cd_categoria_despesa"),
						rs.getInt("cd_curso"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static CursoModulo getById(String idProudutoServico, int cdCurso) {
		return getById(idProudutoServico, cdCurso, null);
	}
	
	public static CursoModulo getById(String idProdutoServico, int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_modulo A, grl_produto_servico B WHERE B.id_produto_servico = ? " +
											 " 																AND A.cd_curso= ? ");
			pstmt.setString(1, idProdutoServico);
			pstmt.setInt(2, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CursoModulo(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_categoria_economica"),
						rs.getString("nm_produto_servico"),
						rs.getString("txt_produto_servico"),
						rs.getString("txt_especificacao"),
						rs.getString("txt_dado_tecnico"),
						rs.getString("txt_prazo_entrega"),
						rs.getInt("tp_produto_servico"),
						rs.getString("id_produto_servico"),
						rs.getString("sg_produto_servico"),
						rs.getInt("cd_classificacao_fiscal"),
						rs.getInt("cd_fabricante"),
						rs.getInt("cd_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("cd_ncm"),
						rs.getString("nr_referencia"),
						rs.getInt("cd_categoria_receita"),
						rs.getInt("cd_categoria_despesa"),
						rs.getInt("cd_curso"),
						rs.getInt("nr_ordem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.getById: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.getById: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso_modulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CursoModulo> getList() {
		return getList(null);
	}

	public static ArrayList<CursoModulo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CursoModulo> list = new ArrayList<CursoModulo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CursoModulo obj = CursoModuloDAO.get(rsm.getInt("cd_curso_modulo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoModuloDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso_modulo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
