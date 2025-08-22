package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DisciplinaDAO{

	public static int insert(Disciplina objeto) {
		return insert(objeto, null);
	}

	public static int insert(Disciplina objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.ProdutoServicoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProdutoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_disciplina (cd_disciplina,"+
			                                  "cd_disciplina_teoria,"+
			                                  "gn_disciplina,"+
			                                  "cd_area_conhecimento,"+
			                                  "tp_classificacao) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdDisciplinaTeoria()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDisciplinaTeoria());
			pstmt.setInt(3,objeto.getGnDisciplina());
			if(objeto.getCdAreaConhecimento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAreaConhecimento());
			pstmt.setInt(5,objeto.getTpClassificacao());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Disciplina objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Disciplina objeto, int cdDisciplinaOld) {
		return update(objeto, cdDisciplinaOld, null);
	}

	public static int update(Disciplina objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Disciplina objeto, int cdDisciplinaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Disciplina objetoTemp = get(objeto.getCdProdutoServico(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_disciplina (cd_disciplina,"+
			                                  "cd_disciplina_teoria,"+
			                                  "gn_disciplina,"+
			                                  "cd_area_conhecimento,"+
			                                  "tp_classificacao) VALUES (?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_disciplina SET cd_disciplina=?,"+
												      		   "cd_disciplina_teoria=?,"+
												      		   "gn_disciplina=?,"+
												      		   "cd_area_conhecimento=?,"+
												      		   "tp_classificacao=? WHERE cd_disciplina=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdDisciplinaTeoria()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDisciplinaTeoria());
			pstmt.setInt(3,objeto.getGnDisciplina());
			if(objeto.getCdAreaConhecimento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAreaConhecimento());
			pstmt.setInt(5,objeto.getTpClassificacao());
			if (objetoTemp != null) {
				pstmt.setInt(6, cdDisciplinaOld!=0 ? cdDisciplinaOld : objeto.getCdProdutoServico());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ProdutoServicoDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! DisciplinaDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDisciplina) {
		return delete(cdDisciplina, null);
	}

	public static int delete(int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_disciplina WHERE cd_disciplina=?");
			pstmt.setInt(1, cdDisciplina);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ProdutoServicoDAO.delete(cdDisciplina, connect)<=0) {
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
			System.err.println("Erro! DisciplinaDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Disciplina get(int cdDisciplina) {
		return get(cdDisciplina, null);
	}

	public static Disciplina get(int cdDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina A, grl_produto_servico B WHERE A.cd_disciplina=B.cd_produto_servico AND A.cd_disciplina=?");
			pstmt.setInt(1, cdDisciplina);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Disciplina(rs.getInt("cd_produto_servico"),
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
						rs.getDouble("vl_servico"),
						rs.getInt("cd_disciplina_teoria"),
						rs.getInt("gn_disciplina"),
						rs.getInt("cd_area_conhecimento"),
						rs.getInt("tp_classificacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Disciplina> getList() {
		return getList(null);
	}

	public static ArrayList<Disciplina> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Disciplina> list = new ArrayList<Disciplina>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Disciplina obj = DisciplinaDAO.get(rsm.getInt("cd_disciplina"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_disciplina", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
