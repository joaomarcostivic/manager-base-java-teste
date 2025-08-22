package com.tivic.manager.grl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CboServices {
	public static Result save(Cbo cbo){
		return save(cbo, null);
	}
	
	public static Result save(Cbo cbo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cbo==null)
				return new Result(-1, "Erro ao salvar. Cbo é nulo");
			
			int retorno;
			if(cbo.getCdCbo()==0){
				retorno = CboDAO.insert(cbo, connect);
				cbo.setCdCbo(retorno);
			}
			else {
				retorno = CboDAO.update(cbo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CBO", cbo);
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
	
	public static Result remove(int cdCbo){
		return remove(cdCbo, false, null);
	}
	
	public static Result remove(int cdCbo, boolean cascade){
		return remove(cdCbo, cascade, null);
	}
	
	public static Result remove(int cdCbo, boolean cascade, Connection connect){
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
				retorno = CboDAO.delete(cdCbo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este cbo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Cbo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir cbo!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(0, 0, null);
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
				"SELECT A.*, " +
				"	B.nm_cbo AS nm_cbo_superior, B.nr_cbo AS nr_cbo_superior, B.sg_cbo AS sg_cbo_superior, B.id_cbo AS id_cbo_superior " +
				"FROM grl_cbo A " +
				"LEFT OUTER JOIN grl_cbo B ON (B.cd_cbo = A.cd_cbo_superior) " +
				"ORDER BY A.nm_cbo " +
				(offSetRegistros > 0 ? "OFFSET " + offSetRegistros : "") + (countRegistros > 0 ? " LIMIT " + countRegistros : ""));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllSinonimo(int cdCbo) {
		return getAllSinonimo(cdCbo, null);
	}

	public static ResultSetMap getAllSinonimo(int cdCbo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if (cdCbo <= 0) {
				return null;
			}
			PreparedStatement pstmt = connect.prepareStatement(
				"SELECT A.*, " +
				"	B.nm_cbo, B.nr_cbo, B.sg_cbo, B.id_cbo " +
				"FROM grl_cbo_sinonimo A, grl_cbo B " +
				"WHERE (A.cd_cbo = B.cd_cbo) " +
				"	AND A.cd_cbo = " + cdCbo +
				" ORDER BY A.nm_cbo_sinonimo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CboServices.getAllSinonimo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static void updateNrCboInferior(int cdCbo, String prefix, int nrNivel, Connection connect)	{
		try {
			ResultSet rs = connect.prepareStatement(
					"SELECT * FROM grl_cbo " +
	                "WHERE cd_cbo_superior = " + cdCbo).executeQuery();
			while(rs.next())	{
				Cbo cbo = new Cbo(rs.getInt("cd_cbo"),
						  rs.getString("nm_cbo"),
						  rs.getString("sg_cbo"),
						  prefix + "." + rs.getString("id_cbo"),
						  nrNivel + 1,
						  rs.getInt("cd_cbo_superior"),
						  rs.getString("nr_cbo"),
						  rs.getString("txt_ocupacao"),
						  rs.getString("txt_descricao_sumaria"),
						  rs.getString("txt_condicao_exercicio"),
						  rs.getString("txt_formacao"),
						  rs.getString("txt_excecao"));
				CboDAO.update(cbo, connect);
				updateNrCboInferior(cbo.getCdCbo(), cbo.getNrCbo(), nrNivel + 1, connect);
			}
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboServices.updateNrCboInferior: " + sqlExpt);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboServices.updateNrCboInferior: " +  e);
		}
	}

	public static String gerarNrCbo(int cdCboSuperior, String nrCbo, Connection connect)	{
		try {
			ResultSet rs;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT nr_cbo, cd_cbo_superior " +
					"FROM grl_cbo " +
	                		"WHERE cd_cbo = ? ");
			while(cdCboSuperior > 0)	{
				pstmt.setInt(1, cdCboSuperior);
				rs = pstmt.executeQuery();
				if(rs.next())	{
					nrCbo = (rs.getString("nr_cbo")==null ? "" : rs.getString("nr_cbo")) + "." + nrCbo;
					cdCboSuperior = rs.getInt("cd_cbo_superior");
				}
			}
			return nrCbo;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CboServices.gerarNrCbo: " + sqlExpt);
			return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboServices.gerarNrCbo: " +  e);
			return null;
		}
	}

	public static Result insert(Cbo objeto) {
		return insert(objeto, null);
	}

	public static Result insert(Cbo objeto, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			if(objeto.getCdCboSuperior() > 0)	{
				ResultSet rs = connect.prepareStatement(
					"SELECT nr_nivel FROM grl_cbo "+
	                "WHERE cd_cbo = " + objeto.getCdCboSuperior()).executeQuery();
				if(rs.next())
					objeto.setNrNivel(rs.getInt("nr_nivel") + 1);
			}
			else
				objeto.setNrNivel(0);
			String nrCbo = gerarNrCbo(objeto.getCdCboSuperior(), objeto.getNrCbo(), connect);
			if(nrCbo.length() > 20) {
				nrCbo = nrCbo.substring(0, 20);
			}
			objeto.setNrCbo(nrCbo);

			int ret = CboDAO.insert(objeto, connect);
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put("nrCbo", objeto.getNrCbo());
			hash.put("nrNivel", String.valueOf(objeto.getNrNivel()));
			return new Result(ret, (ret < 0 ? "Erro ao tentar gravar dados!" : "") , "hash", hash);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboServices.insert: " +  e);
			return new Result(-1, "Erro ao tentar gravar dados!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static Result update(Cbo objeto){
		return update(objeto, null);
	}
	public static Result update(Cbo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			if(objeto.getCdCboSuperior() > 0)	{
				ResultSet rs = connect.prepareStatement(
					"SELECT nr_nivel FROM grl_cbo "+
	                "WHERE cd_cbo = " + objeto.getCdCboSuperior()).executeQuery();
				if(rs.next())
					objeto.setNrNivel(rs.getInt("nr_nivel") + 1);
			}
			else
				objeto.setNrNivel(0);
			if (objeto.getNrCbo() == "" || objeto.getNrCbo() == null) {
				String nrCbo = gerarNrCbo(objeto.getCdCboSuperior(), objeto.getNrCbo(), connect);
				if(nrCbo.length() > 20) {
					nrCbo = nrCbo.substring(0, 20);
				}
				objeto.setNrCbo(nrCbo);
			}
			int ret = CboDAO.update(objeto, connect);
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put("nrCbo", objeto.getNrCbo());
			hash.put("nrNivel", String.valueOf(objeto.getNrNivel()));
			return new Result(ret, (ret < 0 ? "Erro ao tentar gravar dados!" : "") , "hash", hash);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboServices.update: " +  e);
			return new Result(-1, "Erro ao tentar gravar dados!", e);
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCbo){
		return delete(cdCbo, null);
	}
	public static int delete(int cdCbo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			// Exclui sinônimos
			PreparedStatement pstmt;
			ResultSet rs;
			pstmt = connect.prepareStatement(
				"SELECT * FROM grl_cbo_sinonimo " +
                "WHERE cd_cbo = ? ");
			pstmt.setInt(1, cdCbo);
			rs = pstmt.executeQuery();

			while(rs != null && rs.next()) {
				CboSinonimoDAO.delete(rs.getInt("cd_cbo_sinonimo"), cdCbo, connect);
			}

			int ret = CboDAO.delete(cdCbo, connect);
			if (ret <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CboServices.update: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllHierarquia() {
		return getAllHierarquia(null);
	}

	public static ResultSetMap getAllHierarquia(Connection connect) {
		ResultSetMap rsm = find(new ArrayList<ItemComparator>());
		while (rsm != null && rsm.next()) {
			rsm.setValueToField("NM_CBO_COMPLETO", rsm.getString("NR_CBO") + " - " + rsm.getString("NM_CBO"));
			if (rsm.getInt("CD_CBO_SUPERIOR") != 0) {
				int pointer = rsm.getPointer();
				int cdCbo = rsm.getInt("CD_CBO_SUPERIOR");
				HashMap<String,Object> register = rsm.getRegister();
				if (rsm.locate("CD_CBO", new Integer(rsm.getInt("CD_CBO_SUPERIOR")), false, true)) {
					HashMap<String,Object> parentNode = rsm.getRegister();
					boolean isFound = rsm.getInt("CD_CBO") == cdCbo;
					ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
					while (!isFound && subRsm != null) {
						subRsm = parentNode == null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
						parentNode = subRsm == null ? null : subRsm.getRegister();
						isFound = subRsm == null ? false : subRsm.getInt("CD_CBO") == cdCbo;
					}
					subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
					if (subRsm == null) {
						subRsm = new ResultSetMap();
						parentNode.put("subResultSetMap", subRsm);
					}
					subRsm.addRegister(register);
					rsm.getLines().remove(register);
					pointer --;
				}
				rsm.goTo(pointer);
			}
		}
		return rsm;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql =
			"SELECT A.*, " +
			"	B.nm_cbo AS nm_cbo_superior, B.nr_cbo AS nr_cbo_superior, B.sg_cbo AS sg_cbo_superior, B.id_cbo AS id_cbo_superior " +
			"FROM grl_cbo A " +
			"LEFT OUTER JOIN grl_cbo B ON (B.cd_cbo = A.cd_cbo_superior) ";
		return Search.find(sql, "ORDER BY A.nm_cbo", criterios, null, Conexao.conectar(), true, false, false);
	}

	public static ResultSetMap findWithSinonimo(ArrayList<ItemComparator> criterios) {
		return findWithSinonimo(criterios, null);
	}

	public static ResultSetMap findWithSinonimo(ArrayList<ItemComparator> criterios, Connection connect) {
		String nmCbo = "";
		for (int i = 0; criterios != null && i < criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("A.nm_cbo")) {
				nmCbo = (String)criterios.get(i).getValue();
				criterios.remove(i);
				i--;
			}
		}
		String sql =
			"SELECT A.cd_cbo, A.nm_cbo, A.id_cbo, 0 AS lg_sinonimo " +
			"FROM grl_cbo A " +
			(nmCbo != "" ? "WHERE (A.nm_cbo LIKE \'%" + nmCbo + "%\') " +
			"UNION " +
			"SELECT	C.cd_cbo, C.nm_cbo_sinonimo, C.id_cbo_sinonimo, 1 AS lg_sinonimo " +
			"FROM grl_cbo_sinonimo C " +
			"WHERE (C.nm_cbo_sinonimo LIKE \'%" + nmCbo + "%\') " : "");
		return Search.find(sql, "ORDER BY nm_cbo", criterios, null, Conexao.conectar(), true, false, false);
	}

	public static ResultSetMap getAllCbo() {
		String sql = "SELECT * FROM grl_cbo A";
		ResultSetMap rsm = Search.find(sql, "ORDER BY nr_cbo", new ArrayList<ItemComparator>(), Conexao.conectar(), true);
		while(rsm.next())
			rsm.setValueToField("DS_CBO", Util.fill("", rsm.getInt("nr_nivel") * 5, ' ', 'E') +
                    						    rsm.getString("nr_cbo") + " - " + rsm.getString("nm_cbo"));
		rsm.beforeFirst();
		return rsm;
	}
}