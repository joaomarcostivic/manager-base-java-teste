package com.tivic.manager.srh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoAdmissaoServices {
	public static int init()	{
		String[] classificao = new String[] {"Primeiro emprego", "Reemprego",
				"Transferência/Movimentação com ônus", "Transferência/Movimentação sem ônus"};

		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			//
			pstmt = connect.prepareStatement("SELECT * " +
			           						 "FROM srh_tipo_admissao " +
			           						 "WHERE id_tipo_admissao = ?");
			for(int i=0; i<classificao.length; i++)	{
				pstmt.setString(1, Util.fillNum(i+1, 2));
				if(!pstmt.executeQuery().next())	{
					AgenteNocivo agenteNocivo = new AgenteNocivo(0, Util.fillNum(i+1, 2)+"-"+classificao[i], Util.fillNum(i, 2));
					AgenteNocivoDAO.insert(agenteNocivo, connect);
				}
			}
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoAdmissaoServices.cargaInicial: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	public static Result save(TipoAdmissao tipoAdmissao){
		return save(tipoAdmissao, null);
	}

	public static Result save(TipoAdmissao tipoAdmissao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoAdmissao==null)
				return new Result(-1, "Erro ao salvar. TipoAdmissao é nulo");

			int retorno;
			if(tipoAdmissao.getCdTipoAdmissao()==0){
				retorno = TipoAdmissaoDAO.insert(tipoAdmissao, connect);
				tipoAdmissao.setCdTipoAdmissao(retorno);
			}
			else {
				retorno = TipoAdmissaoDAO.update(tipoAdmissao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOADMISSAO", tipoAdmissao);
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
	public static Result remove(int cdTipoAdmissao){
		return remove(cdTipoAdmissao, false, null);
	}
	public static Result remove(int cdTipoAdmissao, boolean cascade){
		return remove(cdTipoAdmissao, cascade, null);
	}
	public static Result remove(int cdTipoAdmissao, boolean cascade, Connection connect){
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
			retorno = TipoAdmissaoDAO.delete(cdTipoAdmissao, connect);
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM srh_tipo_admissao", "ORDER BY nm_tipo_admissao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		return Search.find("SELECT * FROM srh_tipo_admissao WHERE (st_tipo_admissao = 1 OR st_tipo_admissao IS NULL)", "ORDER BY nm_tipo_admissao", new ArrayList<ItemComparator>(), connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllSuperior() {
		return getAllSuperior(null);
	}

	public static ResultSetMap getAllSuperior(Connection connect) {
		return Search.find("SELECT * FROM srh_tipo_admissao WHERE (st_tipo_admissao = 1 OR st_tipo_admissao IS NULL) AND cd_tipo_admissao_superior IS NULL", "ORDER BY nm_tipo_admissao", new ArrayList<ItemComparator>(), connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}