package com.tivic.manager.mob;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ImportacaoAitServices {
	// Nome das Tableas do Firebird
	public final static int INFRACAO_TRANSPORTE = 0;
	public final static int AGENTE = 1;
	public final static int STP_INFRACAO_MOTIVO = 2;
	public final static int GRL_PESSOA_FISICA = 3;
	public final static int USUARIO = 4;
	public final static int TALONARIO = 5;
	public final static int AIT_TRANSPORTE = 6;
	public final static int STR_INFRACAO_VARIACAO = 7;
	

	// Nome das Tabelas do Postgres
	public final static int MOB_INFRACAO_TRANSPORTE = 0;
	public final static int MOB_AGENTE = 1;
	public final static int MOB_INFRACAO_MOTIVO = 2;
	public final static int MOB_GRL_PESSOA_FISCA = 3;
	public final static int SEG_USUARIO = 4;
	public final static int MOB_TALONARIO = 5;
	public final static int MOB_CONCESSAO = 6;
	public final static int PTC_DOCUMENTO = 7;
	public final static int MOB_AIT_TRANSPORTE = 8;
	public final static int MOB_INFRACAO_VARIACAO = 9;
	
	

	public static Result save(ImportacaoAit importacaoAit){
		return save(importacaoAit, null);
	}

	public static Result save(ImportacaoAit importacaoAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(importacaoAit==null)
				return new Result(-1, "Erro ao salvar. ImportacaoAit é nulo");

			int retorno;
			if(importacaoAit.getCdImportacao()==0){
				retorno = ImportacaoAitDAO.insert(importacaoAit, connect);
				importacaoAit.setCdImportacao(retorno);
			}
			else {
				retorno = ImportacaoAitDAO.update(importacaoAit, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "IMPORTACAOAIT", importacaoAit);
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
	public static Result remove(int cdImportacao){
		return remove(cdImportacao, false, null);
	}
	public static Result remove(int cdImportacao, boolean cascade){
		return remove(cdImportacao, cascade, null);
	}
	public static Result remove(int cdImportacao, boolean cascade, Connection connect){
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
				retorno = ImportacaoAitDAO.delete(cdImportacao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_importacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImportacaoAitServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_importacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static String nomeTabelaFirebird(int constanteFirebird){

		switch (constanteFirebird) {
		case INFRACAO_TRANSPORTE:
			return "INFRACAO_TRANSPORTE";
		case AGENTE:
			return "AGENTE";
		case STP_INFRACAO_MOTIVO:
			return "STP_INFRACAO_MOTIVO";
		case GRL_PESSOA_FISICA:
			return "GRL_PESSOA_FISICA";
		case USUARIO:
			return "USUARIO";
		case TALONARIO:
			return "TALONARIO";
		case AIT_TRANSPORTE:
			return "AIT_TRANSPORTE";
		case STR_INFRACAO_VARIACAO:
			return "STR_INFRACAO_VARIACAO";	
		default:
			return "";	
		}

	}	

	public static String nomeTabelaPostegres(int constantePostgres){		
		switch (constantePostgres) {
		case MOB_INFRACAO_TRANSPORTE:
			return "mob_infracao_transporte";
		case MOB_AGENTE:
			return "mob_agente";
		case MOB_INFRACAO_MOTIVO:
			return "mob_infracao_motivo";
		case MOB_GRL_PESSOA_FISCA:
			return "grl_pessoa_fisica";
		case SEG_USUARIO:
			return "seg_usuario";
		case MOB_TALONARIO:
			return "mob_talonario";
		case MOB_CONCESSAO:
			return "mob_concessao";
		case PTC_DOCUMENTO:
			return "ptc_documento";
		case MOB_AIT_TRANSPORTE:
			return "mob_ait_transporte";
		case MOB_INFRACAO_VARIACAO:
			return "mob_infracao_variacao";	
		default:
			return "";	
		}		
	}

}
