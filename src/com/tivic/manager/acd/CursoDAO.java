package com.tivic.manager.acd;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServicoDAO;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.ArrayList;

public class CursoDAO{

	public static int insert(Curso objeto) {
		return insert(objeto, null);
	}

	public static int insert(Curso objeto, Connection connect){
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
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_curso (cd_curso,"+
			                                  "nm_habilitacao,"+
			                                  "nm_duracao,"+
			                                  "txt_competencia,"+
			                                  "txt_curso,"+
			                                  "cd_tipo_periodo,"+
			                                  "tp_contratacao,"+
			                                  "tp_modalidade_ensino,"+
			                                  "tp_etapa_ensino,"+
			                                  "tp_presenca,"+
			                                  "nr_ordem,"+
			                                  "lg_multi,"+
			                                  "lg_referencia,"+
			                                  "nr_idade,"+
			                                  "tp_avaliacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setString(2,objeto.getNmHabilitacao());
			pstmt.setString(3,objeto.getNmDuracao());
			pstmt.setString(4,objeto.getTxtCompetencia());
			pstmt.setString(5,objeto.getTxtCurso());
			if(objeto.getCdTipoPeriodo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoPeriodo());
			pstmt.setInt(7,objeto.getTpContratacao());
			pstmt.setInt(8,objeto.getTpModalidadeEnsino());
			pstmt.setInt(9,objeto.getTpEtapaEnsino());
			pstmt.setInt(10,objeto.getTpPresenca());
			pstmt.setInt(11,objeto.getNrOrdem());
			pstmt.setInt(12,objeto.getLgMulti());
			pstmt.setInt(13,objeto.getLgReferencia());
			pstmt.setInt(14,objeto.getNrIdade());
			pstmt.setInt(15,objeto.getTpAvaliacao());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Curso objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Curso objeto, int cdCursoOld) {
		return update(objeto, cdCursoOld, null);
	}

	public static int update(Curso objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Curso objeto, int cdCursoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Curso objetoTemp = get(objeto.getCdProdutoServico(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO acd_curso (cd_curso,"+
			                                  "nm_habilitacao,"+
			                                  "nm_duracao,"+
			                                  "txt_competencia,"+
			                                  "txt_curso,"+
			                                  "cd_tipo_periodo,"+
			                                  "tp_contratacao,"+
			                                  "tp_modalidade_ensino,"+
			                                  "tp_etapa_ensino,"+
			                                  "tp_presenca,"+
			                                  "nr_ordem,"+
			                                  "lg_multi,"+
			                                  "lg_referencia,"+
			                                  "nr_idade,"+
			                                  "tp_avaliacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE acd_curso SET cd_curso=?,"+
												      		   "nm_habilitacao=?,"+
												      		   "nm_duracao=?,"+
												      		   "txt_competencia=?,"+
												      		   "txt_curso=?,"+
												      		   "cd_tipo_periodo=?,"+
												      		   "tp_contratacao=?,"+
												      		   "tp_modalidade_ensino=?,"+
												      		   "tp_etapa_ensino=?,"+
												      		   "tp_presenca=?,"+
												      		   "nr_ordem=?,"+
												      		   "lg_multi=?,"+
												      		   "lg_referencia=?,"+
												      		   "nr_idade=?,"+
												      		   "tp_avaliacao=? WHERE cd_curso=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setString(2,objeto.getNmHabilitacao());
			pstmt.setString(3,objeto.getNmDuracao());
			pstmt.setString(4,objeto.getTxtCompetencia());
			pstmt.setString(5,objeto.getTxtCurso());
			if(objeto.getCdTipoPeriodo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoPeriodo());
			pstmt.setInt(7,objeto.getTpContratacao());
			pstmt.setInt(8,objeto.getTpModalidadeEnsino());
			pstmt.setInt(9,objeto.getTpEtapaEnsino());
			pstmt.setInt(10,objeto.getTpPresenca());
			pstmt.setInt(11,objeto.getNrOrdem());
			pstmt.setInt(12,objeto.getLgMulti());
			pstmt.setInt(13,objeto.getLgReferencia());
			pstmt.setInt(14,objeto.getNrIdade());
			pstmt.setInt(15,objeto.getTpAvaliacao());
			if (objetoTemp != null) {
				pstmt.setInt(16, cdCursoOld!=0 ? cdCursoOld : objeto.getCdProdutoServico());
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
			System.err.println("Erro! CursoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCurso) {
		return delete(cdCurso, null);
	}

	public static int delete(int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_curso WHERE cd_curso=?");
			pstmt.setInt(1, cdCurso);
			pstmt.executeUpdate();
			if (ProdutoServicoDAO.delete(cdCurso, connect)<=0) {
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
			System.err.println("Erro! CursoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Curso get(int cdCurso) {
		return get(cdCurso, null);
	}

	public static Curso get(int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso A, grl_produto_servico B WHERE A.cd_curso=B.cd_produto_servico AND A.cd_curso=?");
			pstmt.setInt(1, cdCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Curso(rs.getInt("cd_produto_servico"),
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
						rs.getFloat("vl_servico"),
						rs.getString("nm_habilitacao"),
						rs.getString("nm_duracao"),
						rs.getString("txt_competencia"),
						rs.getString("txt_curso"),
						rs.getInt("cd_tipo_periodo"),
						rs.getInt("tp_contratacao"),
						rs.getInt("tp_modalidade_ensino"),
						rs.getInt("tp_etapa_ensino"),
						rs.getInt("tp_presenca"),
						rs.getInt("nr_ordem"),
						rs.getInt("lg_multi"),
						rs.getInt("lg_referencia"),
						rs.getInt("nr_idade"),
						rs.getInt("tp_avaliacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_curso");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Curso> getList() {
		return getList(null);
	}

	public static ArrayList<Curso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Curso> list = new ArrayList<Curso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Curso obj = CursoDAO.get(rsm.getInt("cd_curso"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_curso", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
