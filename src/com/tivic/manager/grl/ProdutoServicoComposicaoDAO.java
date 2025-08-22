package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoServicoComposicaoDAO{

	public static int insert(ProdutoServicoComposicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoServicoComposicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_produto_servico_composicao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdComposicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto_servico_composicao (cd_composicao,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_produto_servico_componente,"+
			                                  "cd_referencia,"+
			                                  "cd_unidade_medida,"+
			                                  "qt_produto_servico,"+
			                                  "pr_perda,"+
			                                  "lg_calculo_custo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdProdutoServicoComponente()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServicoComponente());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdReferencia());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUnidadeMedida());
			pstmt.setFloat(7,objeto.getQtProdutoServico());
			pstmt.setFloat(8,objeto.getPrPerda());
			pstmt.setInt(9,objeto.getLgCalculoCusto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoServicoComposicao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ProdutoServicoComposicao objeto, int cdComposicaoOld) {
		return update(objeto, cdComposicaoOld, null);
	}

	public static int update(ProdutoServicoComposicao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ProdutoServicoComposicao objeto, int cdComposicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_produto_servico_composicao SET cd_composicao=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_produto_servico_componente=?,"+
												      		   "cd_referencia=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "qt_produto_servico=?,"+
												      		   "pr_perda=?,"+
												      		   "lg_calculo_custo=? WHERE cd_composicao=?");
			pstmt.setInt(1,objeto.getCdComposicao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			if(objeto.getCdProdutoServicoComponente()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServicoComponente());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdReferencia());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUnidadeMedida());
			pstmt.setFloat(7,objeto.getQtProdutoServico());
			pstmt.setFloat(8,objeto.getPrPerda());
			pstmt.setInt(9,objeto.getLgCalculoCusto());
			pstmt.setInt(10, cdComposicaoOld!=0 ? cdComposicaoOld : objeto.getCdComposicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdComposicao) {
		return delete(cdComposicao, null);
	}

	public static int delete(int cdComposicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_servico_composicao WHERE cd_composicao=?");
			pstmt.setInt(1, cdComposicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoServicoComposicao get(int cdComposicao) {
		return get(cdComposicao, null);
	}

	public static ProdutoServicoComposicao get(int cdComposicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_composicao WHERE cd_composicao=?");
			pstmt.setInt(1, cdComposicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoServicoComposicao(rs.getInt("cd_composicao"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_produto_servico_componente"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_unidade_medida"),
						rs.getFloat("qt_produto_servico"),
						rs.getFloat("pr_perda"),
						rs.getInt("lg_calculo_custo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_composicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_produto_servico_composicao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
