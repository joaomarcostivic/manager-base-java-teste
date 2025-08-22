package com.tivic.manager.prc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.CidadeServices;

public class CidadeOrgaoServices {

	public static Result save(CidadeOrgao cidadeOrgao){
		return save(cidadeOrgao, null);
	}

	public static Result save(CidadeOrgao cidadeOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cidadeOrgao==null)
				return new Result(-1, "Erro ao salvar. CidadeOrgao é nulo");

			int retorno;
			
			
			if(CidadeOrgaoDAO.get(cidadeOrgao.getCdCidade(), cidadeOrgao.getCdOrgao(), connect)==null){
				retorno = CidadeOrgaoDAO.insert(cidadeOrgao, connect);
				cidadeOrgao.setCdCidade(retorno);
			}
			else {
				retorno = CidadeOrgaoDAO.update(cidadeOrgao, connect);
			}
			
			if(retorno<=0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao salvar cidade.");
			}
			
//			TODO: Incluir estadoOrgao correspondente a cidadeOrgao
//			Cidade cidade = CidadeDAO.get(cidadeOrgao.getCdCidade(), connect);
//			EstadoOrgao estadoOrgao = new EstadoOrgao(cidade.getCdEstado(), cidadeOrgao.getCdOrgao());
//			retorno = EstadoOrgaoServices.save(estadoOrgao, connect).getCode();
//			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CIDADEORGAO", cidadeOrgao);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdCidade, int cdOrgao){
		return remove(cdCidade, cdOrgao, false, null);
	}
	public static Result remove(int cdCidade, int cdOrgao, boolean cascade){
		return remove(cdCidade, cdOrgao, cascade, null);
	}
	public static Result remove(int cdCidade, int cdOrgao, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0) {
				retorno = CidadeOrgaoDAO.delete(cdCidade, cdOrgao, connect);
			}
				
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_CIDADE_ORGAO");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM PRC_CIDADE_ORGAO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	/**
	 * Busca todos os orgãos que atendem a cidade
	 * 
	 * @param cdCidade código da cidade
	 * @return ResultSetMap lista de orgãos
	 */
	public static ResultSetMap getAllOrgaoByCidade(int cdCidade) {
		return getAllOrgaoByCidade(cdCidade, -1, null);
	}
	
	public static ResultSetMap getAllOrgaoByCidade(int cdCidade, int lgPrincipal) {
		return getAllOrgaoByCidade(cdCidade, lgPrincipal, null);
	}

	public static ResultSetMap getAllOrgaoByCidade(int cdCidade, int lgPrincipal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.lg_principal, B.qt_distancia, C.cd_cidade,"
										   + " D.*, E.*, F.nr_cnpj"
										   + " FROM prc_orgao A"
										   + " JOIN prc_cidade_orgao B ON (A.cd_orgao = B.cd_orgao)"
										   + " JOIN grl_cidade C ON (B.cd_cidade = C.cd_cidade AND C.cd_cidade="+cdCidade+")"
									   	   + " LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_pessoa = D.cd_pessoa AND D.lg_principal=1)"
									   	   + " LEFT OUTER JOIN grl_pessoa E ON (A.cd_pessoa = E.cd_pessoa)"
									   	   + " LEFT OUTER JOIN grl_pessoa_juridica F ON (A.cd_pessoa = F.cd_pessoa)"
									   	   + " WHERE 1=1"
									   	   + (lgPrincipal>=0 ? " AND B.lg_principal = "+lgPrincipal : ""));
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServices.getAllOrgaoByCidade: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServices.getAllOrgaoByCidade: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca todas as cidades atendidas por um determinado Orgão
	 * 
	 * @param cdOrgao código do orgão
	 * @return ResultSetMap lista de cidades
	 * 
	 * @author Maurício
	 * @since 09/03/2015
	 */
	public static ResultSetMap getAllCidadeByOrgao(int cdOrgao) {
		return getAllCidadeByOrgao(cdOrgao, null);
	}

	public static ResultSetMap getAllCidadeByOrgao(int cdOrgao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
		
			pstmt = connect.prepareStatement(
					"SELECT A.*, A1.sg_estado, A1.nm_estado, "
					+ " B.lg_principal, B.qt_distancia, C.cd_orgao"
					+ " FROM grl_cidade A"
					+ " LEFT OUTER JOIN grl_estado A1 ON (A.cd_estado = A1.cd_estado)"
					+ " JOIN prc_cidade_orgao B ON (A.cd_cidade = B.cd_cidade)"
					+ " JOIN prc_orgao C ON (B.cd_orgao = C.cd_orgao AND C.cd_orgao = "+cdOrgao+")"
			);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServices.getAllCidadeByOrgao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoServices.getAllCidadeByOrgao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
