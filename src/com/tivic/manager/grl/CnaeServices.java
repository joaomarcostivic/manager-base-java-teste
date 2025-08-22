package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;


public class CnaeServices {
	
	
	public static Result save(Cnae cnae){
		return save(cnae, null, null);
	}

	public static Result save(Cnae cnae, AuthData authData){
		return save(cnae, authData, null);
	}

	public static Result save(Cnae cnae, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cnae==null)
				return new Result(-1, "Erro ao salvar. Cnae é nulo");

			int retorno;
			if(cnae.getCdCnae()==0){
				retorno = CnaeDAO.insert(cnae, connect);
				cnae.setCdCnae(retorno);
			}
			else {
				retorno = CnaeDAO.update(cnae, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CNAE", cnae);
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
	public static Result remove(int cdCnae){
		return remove(cdCnae, false, null, null);
	}
	public static Result remove(int cdCnae, boolean cascade){
		return remove(cdCnae, cascade, null, null);
	}
	public static Result remove(int cdCnae, boolean cascade, AuthData authData){
		return remove(cdCnae, cascade, authData, null);
	}
	public static Result remove(int cdCnae, boolean cascade, AuthData authData, Connection connect){
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
			if(!cascade || retorno>0)
			retorno = CnaeDAO.delete(cdCnae, connect);
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
	
	public static ResultSetMap getAllPessoaJuridicaCnae(int cdCnae) {
		return getAllPessoaJuridicaCnae(cdCnae, null);
	}

	public static ResultSetMap getAllPessoaJuridicaCnae(int cdCnae, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if (cdCnae <= 0) {
				System.err.println("Erro! CnaeServices.getAllPessoaJuridicaCnae: código do CNAE não informado.");
				return null;
			}
			PreparedStatement pstmt = connect.prepareStatement(
				"SELECT A.*, " +
				"	B.nm_cnae, B.nr_cnae, B.sg_cnae, B.id_cnae, " +
				"	C.nm_pessoa, D.nm_razao_social, D.nr_cnpj " +
				"FROM grl_cnae_pessoa_juridica A, grl_cnae B, grl_pessoa C, grl_pessoa_juridica D " +
				"WHERE (A.cd_cnae = B.cd_cnae) " +
				"	AND (A.cd_pessoa = C.cd_pessoa) " +
				"	AND (C.cd_pessoa = D.cd_pessoa) " +
				"	AND A.cd_cnae = " + cdCnae +
				" ORDER BY C.nm_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeServices.getAllPessoaJuridicaCnae: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static void updateNrCnaeInferior(int cdCnae, String prefix, int nrNivel, Connection connect)	{
		try {
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM grl_cnae " +
	                "WHERE cd_cnae_superior = " + cdCnae).executeQuery();
			while(rs.next())	{
				Cnae cnae = new Cnae(rs.getInt("cd_cnae"),
						  rs.getString("nm_cnae"),
						  rs.getString("sg_cnae"),
						  prefix + "." + rs.getString("id_cnae"),
						  nrNivel + 1,
						  rs.getInt("cd_cnae_superior"),
						  rs.getString("nr_cnae"));
				CnaeDAO.update(cnae, connect);
				updateNrCnaeInferior(cnae.getCdCnae(), cnae.getNrCnae(), nrNivel + 1, connect);
			}
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeServices.updateNrCnaeInferior: " + sqlExpt);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeServices.updateNrCnaeInferior: " +  e);
		}
	}

	public static String gerarNrCnae(int cdCnaeSuperior, String nrCnae, Connection connect)	{
		try {
			ResultSet rs;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT nr_cnae, cd_cnae_superior " +
					"FROM grl_cnae " +
	                		"WHERE cd_cnae = ? ");
			while(cdCnaeSuperior > 0)	{
				pstmt.setInt(1, cdCnaeSuperior);
				rs = pstmt.executeQuery();
				if(rs.next())	{
					nrCnae = (rs.getString("nr_cnae")==null ? "" : rs.getString("nr_cnae")) + "." + nrCnae;
					cdCnaeSuperior = rs.getInt("cd_cnae_superior");
				}
			}
			return nrCnae;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeServices.gerarNrCnae: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeServices.gerarNrCnae: " +  e);
			return null;
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql =
			"SELECT A.*, " +
			"	B.nm_cnae AS nm_cnae_superior, B.nr_cnae AS nr_cnae_superior, B.sg_cnae AS sg_cnae_superior, B.id_cnae AS id_cnae_superior " +
			"FROM grl_cnae A " +
			"LEFT OUTER JOIN grl_cnae B ON (B.cd_cnae = A.cd_cnae_superior) ";
		return Search.find(sql, "ORDER BY A.nm_cnae", criterios, Conexao.conectar(), true);
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
			pstmt = connect.prepareStatement(
										" SELECT *, B.nm_cnae AS nm_cnae_superior FROM grl_cnae A " +
										" LEFT OUTER JOIN grl_cnae B ON (A.cd_cnae_superior = B.cd_cnae)"+
										" ORDER BY A.ID_CNAE  ");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll(int countRegistros, int offSetRegistros) {
		return getAll(countRegistros, offSetRegistros, null);
	}

	public static ResultSetMap getAll(int countRegistros, int offSetRegistros, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
				"SELECT * FROM grl_cnae " +
				"ORDER BY nm_cnae " +
				"OFFSET " + offSetRegistros + " LIMIT " + countRegistros);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CnaeServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCnaeCbo(ArrayList<sol.dao.ItemComparator> criterios) {
		return findCnaeCbo(criterios, null);
	}

	public static ResultSetMap findCnaeCbo(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String whereCbo = "WHERE (1=1)";
			String whereCnae = "WHERE (1=1)";
			String[][] valueParams = new String[criterios.size()][2];
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				ItemComparator criterio = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				valueParams[i][0] = "" + (i + 1);
				valueParams[i][1] = "%" + criterio.getValue() + "%";
				if (criterio.getColumn().equalsIgnoreCase("NM_ATIVIDADE")) {
					whereCbo += " AND (A.nm_cbo LIKE ?)";
					whereCnae += " AND (B.nm_cnae LIKE ?)";
				}
				else if (criterio.getColumn().equalsIgnoreCase("SG_ATIVIDADE")) {
					whereCbo += " AND (A.sg_cbo LIKE ?)";
					whereCnae += " AND (B.sg_cnae LIKE ?)";
				}
				else if (criterio.getColumn().equalsIgnoreCase("ID_ATIVIDADE")) {
					whereCbo += " AND (A.id_cbo LIKE ?)";
					whereCnae += " AND (B.id_cnae LIKE ?)";
				}
			}
			String sql =
				"SELECT A.cd_cbo AS CD_ATIVIDADE, A.nm_cbo AS NM_ATIVIDADE, A.sg_cbo AS SG_ATIVIDADE, A.id_cbo AS ID_ATIVIDADE " +
				 "	FROM grl_cbo A " + whereCbo +
				 "UNION " +
				 "SELECT B.cd_cnae, B.nm_cnae, B.sg_cnae, B.id_cnae " +
				 "	FROM grl_cnae B " + whereCnae;
			ResultSetMap rsm = new ResultSetMap();
			PreparedStatement pstmt = connect.prepareStatement(sql);
			//int numberParams = valueParams.length;
			for (int i=0; i < valueParams.length; i++)	{
				//pstmt.setString(new Integer(valueParams[i][0]), valueParams[i][1]);
				//pstmt.setString(new Integer(valueParams[i][0])+numberParams, valueParams[i][1]);
			}
			rsm = new ResultSetMap(pstmt.executeQuery());
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
}