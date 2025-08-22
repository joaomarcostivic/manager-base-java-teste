package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class ClassificacaoFiscalServices {

	public static ResultSetMap getAllHierarquia() {
		return getAllHierarquia(null);
	}

	public static ResultSetMap getAllHierarquia(Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			ResultSetMap rsm = ClassificacaoFiscalDAO.find(criterios, connect);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("cd_classificacao_fiscal_superior") != 0) {
					int pointer = rsm.getPointer();
					int cdClassificacaoFiscal = rsm.getInt("cd_classificacao_fiscal_superior");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("cd_classificacao_fiscal", new Integer(rsm.getInt("cd_classificacao_fiscal_superior")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("cd_classificacao_fiscal")==cdClassificacaoFiscal;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("cd_classificacao_fiscal")==cdClassificacaoFiscal;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao_fiscal ORDER BY nm_classificacao_fiscal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result delete(int cdClassificacaoFiscal, int cdEmpresa) {
		return delete(cdClassificacaoFiscal, cdEmpresa, null);
	}

	public static Result delete(int cdClassificacaoFiscal, int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM grl_produto_servico " +
					"WHERE cd_classificacao_fiscal = ?");
			pstmt.setInt(1, cdClassificacaoFiscal);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Essa classificacão não pode ser deletada por fazer parte de um Produto/Serviço!");
			}
	
			/* exclusão dos itens */
			ResultSet rs = connection.prepareStatement("SELECT cd_ncm FROM adm_ncm " +
					                         		   "WHERE  cd_classificacao_fiscal = "+cdClassificacaoFiscal+" AND cd_empresa = " + cdEmpresa).executeQuery();
			while (rs.next()) {
				Result result = new Result(ClassificacaoFiscalServices.deleteNcmsClassificacaoFiscal(cdEmpresa, cdClassificacaoFiscal, rs.getInt("cd_ncm"), connection));
				if (result.getCode() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					result.setMessage("Exclusão de entrada: "+result.getMessage());
					return result;
				}
			}
			if (ClassificacaoFiscalDAO.delete(cdClassificacaoFiscal, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return new Result(-1, "Falha ao tentar excluir classificacao!");
			}

			if (isConnectionNull)
				connection.commit();

			return new Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Falha ao tentar excluir classificacao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllNcmsClassificacao(int cdEmpresa, int cdClassificacaoFiscal){
		return getAllNcmsClassificacao(cdEmpresa, cdClassificacaoFiscal, null);
	}
	
	public static ResultSetMap getAllNcmsClassificacao(int cdEmpresa, int cdClassificacaoFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_unidade_medida FROM grl_ncm A " +
											 " JOIN adm_ncm N ON (A.cd_ncm = N.cd_ncm)" +
											 " JOIN  grl_unidade_medida B ON(B.cd_unidade_medida = A.cd_unidade_medida) " +
						                     " WHERE N.cd_empresa = ?"+
						                     " AND N.cd_classificacao_fiscal = ?"+
						                     " ORDER BY nm_ncm");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdClassificacaoFiscal);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result insertNcmsClassificacaoFiscal(int cdEmpresa, int cdClassificacaoFiscal, int cdNcm) {
		return insertNcmsClassificacaoFiscal(cdEmpresa, cdClassificacaoFiscal, cdNcm, null);
	}

	public static Result insertNcmsClassificacaoFiscal(int cdEmpresa, int cdClassificacaoFiscal, int cdNcm, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * " +
					"FROM adm_ncm " +
					"WHERE cd_ncm = ?" + 
					"AND cd_empresa = ?");
			pstmt.setInt(1, cdNcm);
			pstmt.setInt(2, cdEmpresa);
			if (pstmt.executeQuery().next()) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Esse NCM já está vinculado a alguma classficação fiscal!");
			}
			
			pstmt = connect.prepareStatement("INSERT INTO adm_ncm (cd_ncm,"+
			                                  "cd_classificacao_fiscal,"+
			                                  "cd_empresa) VALUES (?, ?, ?)");
			pstmt.setInt(1, cdNcm);
			pstmt.setInt(2,cdClassificacaoFiscal);
			pstmt.setInt(3,cdEmpresa);
			pstmt.executeUpdate();
			return new Result(1);
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalServices.insertNcmsClassificacaoFiscal: " + sqlExpt);
			return new Result(-1, "Falha ao tentar excluir classificacao!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalServices.insertNcmsClassificacaoFiscal: " +  e);
			return new Result(-1, "Falha ao tentar excluir classificacao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int deleteNcmsClassificacaoFiscal(int cdEmpresa, int cdClassificacaoFiscal, int cdNcm) {
		return deleteNcmsClassificacaoFiscal(cdEmpresa, cdClassificacaoFiscal, cdNcm, null);
	}

	public static int deleteNcmsClassificacaoFiscal(int cdEmpresa, int cdClassificacaoFiscal, int cdNcm, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_ncm WHERE cd_ncm = ?"+
							                                   "AND cd_classificacao_fiscal = ?" +
							                                   "AND cd_empresa = ?");
			pstmt.setInt(1, cdNcm);
			pstmt.setInt(2,cdClassificacaoFiscal);
			pstmt.setInt(3,cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalServices.deleteNcmsClassificacaoFiscal: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalServices.deleteNcmsClassificacaoFiscal: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllTributosAliquotasClassificacao(int cdClassificacaoFiscal) {
		return getAllTributosAliquotasClassificacao(cdClassificacaoFiscal, null);
	}

	public static ResultSetMap getAllTributosAliquotasClassificacao(int cdClassificacaoFiscal, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.*, C.*, D.nm_cidade, E.nm_estado, F.nm_pais, G.nr_codigo_fiscal, G.nm_natureza_operacao FROM adm_tributo A "+
					"JOIN adm_produto_servico_tributo B ON(A.cd_tributo = B.cd_tributo AND B.cd_classificacao_fiscal = "+cdClassificacaoFiscal+") "+
					"JOIN adm_tributo_aliquota C ON(B.cd_tributo_aliquota = C.cd_tributo_aliquota AND B.cd_tributo = C.cd_tributo) " + 					
					"LEFT OUTER JOIN grl_cidade D ON (B.cd_cidade = D.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado E ON (B.cd_estado = E.cd_estado) " +
					"LEFT OUTER JOIN grl_pais   F ON (B.cd_pais   = F.cd_pais) " +
					"LEFT OUTER JOIN adm_natureza_operacao G ON (B.cd_natureza_operacao = G.cd_natureza_operacao)");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
	}

}
