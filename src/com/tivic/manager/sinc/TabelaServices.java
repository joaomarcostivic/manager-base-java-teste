package com.tivic.manager.sinc;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class TabelaServices {

	public static final int TP_CAMPO_CHAVE_COMUM_PARTICIPANTE = 0;
	public static final int TP_CAMPO_CHAVE_GERADA = 1;
	public static final int TP_CAMPO_CHAVE_COMUM_NAO_PARTICIPANTE = 2;
	
	public static final int ST_DESATIVADO = 0;
	public static final int ST_ATIVADO    = 1;
	
	public static final String[] situacoesTabela = {"Desativado", "Ativado"};
	
	public static final int TP_SINCRONIZACAO_LOCAL_SERVIDOR = 0;
	public static final int TP_SINCRONIZACAO_SERVIDOR_LOCAL = 1;
	public static final int TP_SINCRONIZACAO_FULL_DUPLEX    = 2;
	
	public static final String[] tipoSincronizacao = {"Local -> Servidor", "Servidor -> Local", "Local <-> Servidor"};
	
	
	public static int insert(Tabela tabela){
		return save(tabela).getCode();
	}
	
	public static int insert(Tabela tabela, Connection connect){
		return save(tabela, connect).getCode();
	}
	
	public static int update(Tabela tabela){
		return save(tabela).getCode();
	}
	
	public static int update(Tabela tabela, Connection connect){
		return save(tabela, connect).getCode();
	}
	
	public static Result save(Tabela tabela){
		return save(tabela, null);
	}

	public static Result save(Tabela tabela, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tabela==null)
				return new Result(-1, "Erro ao salvar. Tabela é nulo");

			
			int retorno;
			if(tabela.getCdTabela()==0){
				retorno = TabelaDAO.insert(tabela, connect);
				tabela.setCdTabela(retorno);
			}
			else {
				retorno = TabelaDAO.update(tabela, connect);
			}
			
			
			if(retorno<=0)
				Conexao.rollback(connect);
						
			//SERA USADO ESSAS INSTRUÇÔES PARA ATIVAR OU DESATIVAR DETERMINADA TABELA PARA A SINCRONIZACAO
			
			connect.prepareStatement("DROP TRIGGER IF EXISTS tr_" + tabela.getNmTabela() + " ON " + tabela.getNmTabela()).executeUpdate();
			connect.prepareStatement("DROP TRIGGER IF EXISTS tr_" + tabela.getNmTabela()+"_delete ON " + tabela.getNmTabela()).executeUpdate();
			
			if(tabela.getStSincronizacao() == ST_ATIVADO){
				connect.prepareStatement("CREATE TRIGGER tr_" + tabela.getNmTabela() +
										"	  BEFORE INSERT OR UPDATE " +
										"	  ON " + tabela.getNmTabela() + " FOR EACH ROW " +
										"	  EXECUTE PROCEDURE insertregistro()").executeUpdate();
										
				connect.prepareStatement("	CREATE TRIGGER tr_" + tabela.getNmTabela() + "_delete " +
										"	  AFTER DELETE " +
										"	  ON " + tabela.getNmTabela() + " FOR EACH ROW " +
										"	  EXECUTE PROCEDURE insertregistro()").executeUpdate();
				
			}
			
			if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TABELA", tabela);
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
	
	public static int delete(int cdTabela){
		return remove(cdTabela, false, null).getCode();
	}
	
	public static int delete(int cdTabela, Connection connect){
		return remove(cdTabela, false, connect).getCode();
	}
	
	public static Result remove(int cdTabela){
		return remove(cdTabela, false, null);
	}
	public static Result remove(int cdTabela, boolean cascade){
		return remove(cdTabela, cascade, null);
	}
	public static Result remove(int cdTabela, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				if(isConnectionNull)
					connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = TabelaDAO.delete(cdTabela, connect);
			if(retorno<=0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			
			//SERA USADO ESSAS INSTRUÇÔES PARA ATIVAR OU DESATIVAR DETERMINADA TABELA PARA A SINCRONIZACAO
			Tabela tabela = TabelaDAO.get(cdTabela, connect);
			connect.prepareStatement("DROP TRIGGER IF EXISTS tr_" + tabela.getNmTabela() + " ON " + tabela.getNmTabela());
			connect.prepareStatement("DROP TRIGGER IF EXISTS tr_" + tabela.getNmTabela()+"_delete ON " + tabela.getNmTabela());
			
			connect.prepareStatement("DELETE FROM sinc_tabela_dependencia WHERE cd_dependente = " + tabela.getCdTabela() + " OR cd_provedor = " + tabela.getCdTabela()).executeUpdate();
			
			
			if (isConnectionNull)
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_tabela ORDER BY nm_tabela");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_TP_SINCRONIZACAO", tipoSincronizacao[rsm.getInt("tp_sincronizacao")]);
				rsm.setValueToField("CL_ST_SINCRONIZACAO", situacoesTabela[rsm.getInt("st_sincronizacao")]);
				rsm.setValueToField("CL_DT_INICIO", Util.formatDate(rsm.getGregorianCalendar("dt_inicio"), "dd/MM/yyyy"));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM sinc_tabela", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static Tabela getByName(String nmTabela, Connection connect) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("nm_tabela", nmTabela, ItemComparator.EQUAL, Types.VARCHAR));
		ResultSetMap rsm = find(criterios, connect);
		if(rsm.next())
			return TabelaDAO.get(rsm.getInt("cd_tabela"), connect);
		else
			return null;
	}

}
