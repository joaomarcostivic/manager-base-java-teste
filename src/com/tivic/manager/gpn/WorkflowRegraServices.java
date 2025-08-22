package com.tivic.manager.gpn;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.manager.agd.Grupo;
import com.tivic.manager.agd.GrupoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.prc.Processo;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.prc.TipoPrazo;
import com.tivic.manager.prc.TipoPrazoDAO;
import com.tivic.manager.prc.TipoPrazoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

public class WorkflowRegraServices {
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO = 1;

	public static Result save(WorkflowRegra workflowRegra){
		return save(workflowRegra, null, null, null, null);
	}

	public static Result save(WorkflowRegra workflowRegra, AuthData authData){
		return save(workflowRegra, null, null, authData, null);
	}
	
	public static Result save(WorkflowRegra workflowRegra, WorkflowGatilho workflowGatilho, ArrayList<WorkflowAcao> workflowAcao, AuthData authData){
		return save(workflowRegra, workflowGatilho, workflowAcao, authData, null);
	}

	public static Result save(WorkflowRegra workflowRegra, WorkflowGatilho workflowGatilho, ArrayList<WorkflowAcao> workflowAcao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result;

			if(workflowRegra==null)
				return new Result(-1, "Erro ao salvar. WorkflowRegra é nulo");

			int retorno;
			if(workflowRegra.getCdRegra()==0){
				workflowRegra.setDtCadastro(new GregorianCalendar());
				retorno = WorkflowRegraDAO.insert(workflowRegra, connect);
				workflowRegra.setCdRegra(retorno);
			}
			else {
				retorno = WorkflowRegraDAO.update(workflowRegra, connect);
			}
			
			//gatilho
			if(workflowGatilho!=null) {
				workflowGatilho.setCdRegra(workflowRegra.getCdRegra());
				result = WorkflowGatilhoServices.save(workflowGatilho, authData, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}
				workflowGatilho = (WorkflowGatilho)result.getObjects().get("WORKFLOWGATILHO");
			}
			
			//acao
			if(workflowAcao!=null) {
				result = WorkflowAcaoServices.remove(workflowRegra.getCdRegra(), authData, connect);
				if(result.getCode()<=0) {
					if(isConnectionNull)
						connect.rollback();
					return result;
				}				
				
				for (WorkflowAcao acao : workflowAcao) {
					acao.setCdRegra(workflowRegra.getCdRegra());
					acao.setCdAcao(0);
					
					result = WorkflowAcaoServices.save(acao, authData, connect);
					if(result.getCode()<=0) {
						if(isConnectionNull)
							connect.rollback();
						return result;
					}
					acao = (WorkflowAcao)result.getObjects().get("WORKFLOWACAO");
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "WORKFLOWREGRA", workflowRegra);
			result.addObject("WORKFLOWGATILHO", workflowGatilho);
			result.addObject("WORKFLOWACAO", workflowAcao);
			
			return result;
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
	public static Result remove(int cdRegra){
		return remove(cdRegra, false, null, null);
	}
	public static Result remove(int cdRegra, boolean cascade){
		return remove(cdRegra, cascade, null, null);
	}
	public static Result remove(int cdRegra, boolean cascade, AuthData authData){
		return remove(cdRegra, cascade, authData, null);
	}
	public static Result remove(int cdRegra, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			Result result = null;
			
			//acoes
			result = WorkflowAcaoServices.remove(cdRegra, connect);
			if(result.getCode()<=0) {
				if(isConnectionNull)
					connect.rollback();
				return result;
			}
			
			//gatilhos
			result = WorkflowGatilhoServices.remove(cdRegra, connect);
			if(result.getCode()<=0) {
				if(isConnectionNull)
					connect.rollback();
				return result;
			}
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = WorkflowRegraDAO.delete(cdRegra, connect);
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
			pstmt = connect.prepareStatement("SELECT A.* FROM gpn_workflow_regra A");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()) {
				ArrayList<ItemComparator> crt = new ArrayList<>();
				crt.add(new ItemComparator("CD_REGRA", Integer.toString(rsm.getInt("cd_regra")), ItemComparator.EQUAL, Types.INTEGER));
				
				rsm.setValueToField("RSMGATILHO", WorkflowGatilhoServices.find(crt, connect));
				rsm.setValueToField("RSMACAO", WorkflowAcaoServices.find(crt, connect));
			}
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Busca as regras correspondentes ao gatilhos passados.
	 * 
	 * @param gatilhos ArrayList<Integer> lista de TP_GATILHO
	 * @return
	 * @since 08/06/2016
	 * @author Maurício
	 */
	@Deprecated
	public static ResultSetMap getAllByGatilhos(ArrayList<Integer> gatilhos) {
		return getAllByGatilhos(gatilhos, null);
	}

	@Deprecated
	public static ResultSetMap getAllByGatilhos(ArrayList<Integer> gatilhos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
//			GregorianCalendar hoje  = new GregorianCalendar();
			
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* "
											+ " FROM gpn_workflow_regra A"
											+ " JOIN gpn_workflow_gatilho B ON (A.cd_regra = B.cd_regra)"
											+ " JOIN gpn_workflow_acao C ON (A.cd_regra = C.cd_regra)"
											+ " WHERE B.tp_gatilho IN ("+Util.join(gatilhos)+")"
											+ " AND A.st_regra=?");
//											+ " AND A.dt_inicial <= ?"
//											+ " AND A.dt_final >= ?");
			pstmt.setInt(1, ST_ATIVO);
//			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(hoje));
//			pstmt.setTimestamp(3, Util.convCalendarToTimestamp(hoje));
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAllByGatilhos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAllByGatilhos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByGatilho(int tpGatilho, int cdTipoPrazo, int cdProdutoServico, int cdTipoAndamento, int cdPessoa) {
		return getByGatilho(tpGatilho, cdTipoPrazo, cdProdutoServico, cdTipoAndamento, cdPessoa, null);
	}

	public static ResultSetMap getByGatilho(int tpGatilho, int cdTipoPrazo, int cdProdutoServico, int cdTipoAndamento, int cdPessoa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {
			TipoPrazo tipoPrazo = TipoPrazoDAO.get(cdTipoPrazo, connect);
			
			/*
			 * Regras
			 */
			String sql = 
					  " SELECT A.*, B.*"
					+ " FROM gpn_workflow_regra A"
					+ " LEFT OUTER JOIN gpn_workflow_gatilho B ON (A.cd_regra = B.cd_regra)"
					+ " WHERE 1=1";
		
			ArrayList<ItemComparator> crt = new ArrayList<>();

			crt.add(new ItemComparator("A.ST_REGRA", Integer.toString(ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			crt.add(new ItemComparator("B.TP_GATILHO", Integer.toString(tpGatilho), ItemComparator.EQUAL, Types.INTEGER));
			if(cdTipoPrazo>0)
				crt.add(new ItemComparator("B.CD_TIPO_PRAZO", Integer.toString(cdTipoPrazo), ItemComparator.EQUAL, Types.INTEGER));
			if(cdProdutoServico>0)
				crt.add(new ItemComparator("B.CD_PRODUTO_SERVICO", Integer.toString(cdProdutoServico), ItemComparator.EQUAL, Types.INTEGER));
			if(cdTipoAndamento>0)
				sql += " AND (B.CD_TIPO_ANDAMENTO="+cdTipoAndamento+" OR B.CD_TIPO_ANDAMENTO IS NULL) ";
			if(cdPessoa>0)
				crt.add(new ItemComparator("B.CD_PESSOA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
			
			
			ResultSetMap rsm = Search.find(sql, crt, connect, isConnectionNull);
//			ResultSetMap rsm = Search.findAndLog(sql, "", crt, connect, isConnectionNull);
			while(rsm.next()) {
				crt = new ArrayList<>();
				crt.add(new ItemComparator("A.CD_REGRA", Integer.toString(rsm.getInt("cd_regra")), ItemComparator.EQUAL, Types.INTEGER));
				
				rsm.setValueToField("RSMACAO", WorkflowAcaoServices.find(crt, connect));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! WorkflowRegraServices.getAllByGatilhos: " + e);
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
		connect = (connect==null ? Conexao.conectar() : connect);
		String nmRegra = "";
		String sql = " SELECT A.* "
				+ " FROM gpn_workflow_regra A"
				+ " LEFT OUTER JOIN gpn_workflow_gatilho  B ON (A.cd_regra = B.cd_regra)"
				+ " LEFT OUTER JOIN gpn_workflow_entidade C ON (B.cd_entidade = C.cd_entidade)"
				+ " WHERE 1=1";
		
		for(int i=0; i<criterios.size(); i++)	{
			if(criterios.get(i).getColumn().equalsIgnoreCase("TP_ACAO")) {
				sql += 
					" AND EXISTS ("
					+ "     SELECT Z.* FROM gpn_workflow_acao Z "
					+ "     WHERE A.cd_regra = Z.cd_regra"
					+ "     AND Z.tp_acao = "+criterios.get(i).getValue();
				
				criterios.remove(i);
				i++;
			} else 
				if(criterios.get(i).getColumn().equalsIgnoreCase("A.NM_REGRA")) {
					nmRegra =	Util.limparTexto(criterios.get(i).getValue());
					nmRegra = nmRegra.trim();
					sql += (!nmRegra.equals("") ?
							" AND TRANSLATE (A.nm_regra, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmRegra)+"%'"
							: "" );
				}

			criterios.remove(i);
			i++;
		}
		
		ResultSetMap rsm = Search.findAndLog(sql, " ORDER BY A.nm_regra ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
//		ResultSetMap rsm = Search.findAndLog(sql, "", criterios, connect, false);
		while(rsm!=null && rsm.next()) {
			ArrayList<ItemComparator> crt = new ArrayList<>();
			crt.add(new ItemComparator("A.CD_REGRA", Integer.toString(rsm.getInt("cd_regra")), ItemComparator.EQUAL, Types.INTEGER));
			
			rsm.setValueToField("RSMGATILHO", WorkflowGatilhoServices.find(crt, connect));
			rsm.setValueToField("RSMACAO", WorkflowAcaoServices.find(crt, connect));
		}
		
		return rsm;
	}

}