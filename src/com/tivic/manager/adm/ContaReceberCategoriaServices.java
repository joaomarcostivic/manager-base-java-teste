package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.ctb.CentroCusto;
import com.tivic.manager.ctb.CentroCustoDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ComplianceManager;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ContaReceberCategoriaServices {
	
	public static Result save(ContaReceberCategoria contaReceberCategoria){
		return save(contaReceberCategoria, null, null);
	}

	public static Result save(ContaReceberCategoria contaReceberCategoria, AuthData auth){
		return save(contaReceberCategoria, auth, null);
	}

	public static Result save(ContaReceberCategoria contaReceberCategoria, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaReceberCategoria==null)
				return new Result(-1, "Erro ao salvar. ContaReceberCategoria é nulo");

			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			int retorno;
			if(contaReceberCategoria.getCdContaReceberCategoria()==0){
				retorno = ContaReceberCategoriaDAO.insert(contaReceberCategoria, connect);
				contaReceberCategoria.setCdContaReceberCategoria(retorno);
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
			}
			else {
				retorno = ContaReceberCategoriaDAO.update(contaReceberCategoria, connect);
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}
			
			// COMPLIANCE
			ComplianceManager.process(contaReceberCategoria, auth, tpAcao, connect);

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTARECEBERCATEGORIA", contaReceberCategoria);
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
	public static Result remove(int cdContaReceberCategoria, AuthData auth){
		return remove(cdContaReceberCategoria, false, auth, null);
	}
	public static Result remove(int cdContaReceberCategoria, boolean cascade, AuthData auth){
		return remove(cdContaReceberCategoria, cascade, auth, null);
	}
	public static Result remove(int cdContaReceberCategoria, boolean cascade, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			ContaReceberCategoria crc = ContaReceberCategoriaDAO.get(cdContaReceberCategoria, connect);
			int tpAcao = ComplianceManager.TP_ACAO_DELETE;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
				retorno = ContaReceberCategoriaDAO.delete(cdContaReceberCategoria, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}

			// COMPLIANCE
			ComplianceManager.process(crc, auth, tpAcao, connect);
			
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_categoria");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static Result remove(int cdContaReceber, int cdCategoriaEconomica){
		return remove(cdContaReceber, cdCategoriaEconomica, false, null);
	}
	
	public static Result remove(int cdContaReceber, int cdCategoriaEconomica, boolean cascade){
		return remove(cdContaReceber, cdCategoriaEconomica, cascade, null);
	}
	
	public static Result remove(int cdContaReceber, int cdCategoriaEconomica, boolean cascade, Connection connect){
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
			retorno = ContaReceberCategoriaDAO.delete(cdContaReceber, cdCategoriaEconomica, connect);
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
	
	public static ResultSetMap getCategoriaOfContaReceber(int cdContaReceber) {
		return getCategoriaOfContaReceber(cdContaReceber, null);
	}

	public static ResultSetMap getCategoriaOfContaReceber(int cdContaReceber, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					   "SELECT A.*, B.nm_categoria_economica, B.nr_categoria_economica, B.tp_categoria_economica, C.nm_centro_custo, C.nr_centro_custo " +
			           "FROM adm_conta_receber_categoria A " +
			           "JOIN adm_categoria_economica     B ON (A.cd_categoria_economica = B.cd_categoria_economica) "+
			           "LEFT OUTER JOIN ctb_centro_custo C ON (A.cd_centro_custo = C.cd_centro_custo) " + 
			           "WHERE A.cd_conta_receber = "+cdContaReceber+
			           "ORDER BY nr_categoria_economica");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteAll(int cdContaReceber) {
		return deleteAll(cdContaReceber, null);
	}

	public static int deleteAll(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM adm_conta_receber_categoria " +
					"WHERE cd_conta_receber=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaServices.deleteAll: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result substituirCategoria(int cdContaReceber, int cdCategoriaAnterior, int cdNovaCategoria) {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			/*
			 * Susbstitui classificação da conta
			 * 	Colocando o valor da categoria anterior na nova categoria (quando a categoria nova já existir)
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_conta_receber A, adm_conta_receber_categoria B " +
					 								"WHERE A.cd_conta_receber       = B.cd_conta_receber " +
					 								"  AND B.cd_categoria_economica = "+cdCategoriaAnterior+
					 								"  AND A.cd_conta_receber       = "+cdContaReceber).executeQuery();
			float prValor = 0;
			if(rs.next())
				prValor = rs.getFloat("vl_conta_categoria") / rs.getFloat("vl_conta");
			else
				return new Result(-1, "Erro ao tentar consultar dados da conta e da classificação!", new Exception(""));
			// No caso da conta substitui o valor total da categoria
			connect.prepareStatement(
					 "UPDATE adm_conta_receber_categoria " +
					 "SET vl_conta_categoria = vl_conta_categoria + (SELECT vl_conta_categoria FROM adm_conta_receber_categoria A " +
					 "                                               WHERE A.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber" +
					 "                                                 AND A.cd_categoria_economica = "+cdCategoriaAnterior+") "+
					 "WHERE cd_categoria_economica = "+cdNovaCategoria+
					 "  AND cd_conta_receber       = "+cdContaReceber+
					 "  AND EXISTS (SELECT * FROM adm_conta_receber_categoria B " +
					 "              WHERE B.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber" +
					 "                AND B.cd_categoria_economica = "+cdNovaCategoria+") ").executeUpdate();
			// Excluindo a categoria anterior onde já existir a categoria nova
			connect.prepareStatement(
					 "DELETE FROM adm_conta_receber_categoria " +
					 "WHERE cd_categoria_economica = "+cdCategoriaAnterior+
					 "  AND cd_conta_receber       = "+cdContaReceber+
					 "  AND EXISTS (SELECT * FROM adm_conta_receber_categoria A " +
					 "              WHERE A.cd_conta_receber = adm_conta_receber_categoria.cd_conta_receber" +
					 "                AND A.cd_categoria_economica = "+cdNovaCategoria+") ").executeUpdate();
			// Atualizando da categoria anterior para a nova categoria
			connect.prepareStatement("UPDATE adm_conta_receber_categoria SET cd_categoria_economica = "+cdNovaCategoria+
					 				 " WHERE cd_categoria_economica = "+cdCategoriaAnterior+
					 				 "   AND cd_conta_receber       = "+cdContaReceber).executeUpdate();
			/*
			 * Substitui classificação de movimentações
			 */
			rs = connect.prepareStatement("SELECT * FROM adm_movimento_conta A, adm_movimento_conta_receber B " +
										  "WHERE A.cd_conta           = B.cd_conta " +
										  "  AND A.cd_movimento_conta = B.cd_movimento_conta " +
										  "  AND B.cd_conta_receber   = " +cdContaReceber).executeQuery();
			while(rs.next())	{
				float vlTransferido = prValor * rs.getFloat("vl_recebido");
				// Colocando o valor da categoria anterior na nova categoria (quando a categoria nova já existir)
				connect.prepareStatement(
						 "UPDATE adm_movimento_conta_categoria " +
						 "SET vl_movimento_categoria = (vl_movimento_categoria + "+vlTransferido+") "+
						 " WHERE cd_categoria_economica = "+cdNovaCategoria+
						 "   AND cd_conta               = "+rs.getInt("cd_conta")+
						 "   AND cd_movimento_conta     = "+rs.getInt("cd_movimento_conta")+
						 "   AND EXISTS (SELECT * FROM adm_movimento_conta_categoria B " +
						 "               WHERE B.cd_conta               = adm_movimento_conta_categoria.cd_conta " +
						 "                 AND B.cd_movimento_conta     = adm_movimento_conta_categoria.cd_movimento_conta " +
						 "                 AND B.cd_categoria_economica = "+cdNovaCategoria+") ").executeUpdate();
				// Excluindo a categoria anterior onde já existir a categoria nova
				connect.prepareStatement(
						 "DELETE FROM adm_movimento_conta_categoria " +
						 " WHERE cd_categoria_economica = "+cdCategoriaAnterior+
						 "  AND cd_conta               = "+rs.getInt("cd_conta")+
						 "  AND cd_movimento_conta     = "+rs.getInt("cd_movimento_conta")+
						 "  AND EXISTS (SELECT * FROM adm_movimento_conta_categoria B " +
						 "              WHERE B.cd_conta               = adm_movimento_conta_categoria.cd_conta " +
						 "                AND B.cd_movimento_conta     = adm_movimento_conta_categoria.cd_movimento_conta " +
						 "                AND B.cd_categoria_economica = "+cdNovaCategoria+") ").executeUpdate();
				// Atualizando da categoria anterior para a nova categoria
				connect.prepareStatement("UPDATE adm_movimento_conta_categoria SET cd_categoria_economica = "+cdNovaCategoria+
										 " WHERE cd_categoria_economica = "+cdCategoriaAnterior+
										 "   AND cd_conta               = "+rs.getInt("cd_conta")+
										 "   AND cd_movimento_conta     = "+rs.getInt("cd_movimento_conta")).executeUpdate();
			}
			connect.commit();
			return new Result(1);
		}
		catch(Exception e){
			Conexao.rollback(connect);
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberCategoriaServices.substituirClassificacao: " +  e);
			return new Result(-1, "Erro ao tentar substituir categoria!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_conta_receber_categoria", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getLogCompliance(int cdContaCategoria, int cdConta, boolean lgDelete) {
		return getLogCompliance(cdContaCategoria, cdConta, lgDelete, null);
	}
	
	public static ResultSetMap getLogCompliance(int cdContaCategoria, int cdConta, boolean lgDelete, Connection connect) {
		boolean isConnectionNull = connect == null;
		try {
			if(isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			ResultSetMap rsm = ComplianceManager
					.search(" SELECT * FROM adm_conta_receber_categoria "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE+" AND cd_conta_receber="+cdConta
						  :
						  " AND cd_conta_receber_categoria="+cdContaCategoria+" AND cd_conta_receber="+cdConta)
						+ " ORDER BY dt_compliance DESC ");
			
			while(rsm.next()) {
				if(rsm.getPointer()==0 && !lgDelete)
					rsm.setValueToField("tp_versao_compliance", "ATUAL");
				else
					rsm.setValueToField("tp_versao_compliance", "ANTIGA");
				
				rsm.setValueToField("nm_tp_acao", ComplianceManager.tipoAcao[rsm.getInt("tp_acao_compliance")].toUpperCase());
				
				if(rsm.getInt("cd_usuario_compliance", 0) > 0) {
					Usuario usuario = UsuarioDAO.get(rsm.getInt("cd_usuario_compliance"), connect);
					Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa(), connect);
					rsm.setValueToField("nm_usuario_compliance", pessoa.getNmPessoa());
				}
				
				if(rsm.getInt("cd_categoria_economica", 0) > 0) {
					CategoriaEconomica categoria = CategoriaEconomicaDAO.get(rsm.getInt("cd_categoria_economica"), connect);
					rsm.setValueToField("nm_categoria_economica", categoria.getNmCategoriaEconomica());
					rsm.setValueToField("nr_categoria_economica", categoria.getNrCategoriaEconomica());
					rsm.setValueToField("nm_tp_categoria_economica", (categoria.getTpCategoriaEconomica()==0 ? "Receita" : "Despesa"));
				}
				
				if(rsm.getInt("cd_centro_custo", 0) > 0) {
					CentroCusto centroCusto = CentroCustoDAO.get(rsm.getInt("cd_centro_custo"), connect);
					rsm.setValueToField("nm_centro_custo", centroCusto.getNmCentroCusto());
				}
				
				if(lgDelete) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", " ");
					rsmDetalhes.addRegister(regAtual);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
			}
			rsm.beforeFirst();

			
			if(!lgDelete) {
				while(rsm.next()) {
					ResultSetMap rsmDetalhes = new ResultSetMap();
					HashMap<String, Object> regAtual = (HashMap<String, Object>)rsm.getRegister().clone();
					regAtual.put("TP_ITEM_COMPLIANCE", "PARA");
					rsmDetalhes.addRegister(regAtual);
					
					if(rsm.next()) { //como a ordem é decrescente, o próximo registro representa a versão anterior
						HashMap<String, Object> regAnterior = (HashMap<String, Object>)rsm.getRegister().clone();
						regAnterior.put("TP_ITEM_COMPLIANCE", "DE");
						rsmDetalhes.addRegister(regAnterior);
						rsm.previous();
					}
					
					ArrayList<String> fields = new ArrayList<>();
					fields.add("TP_ITEM_COMPLIANCE");
					rsmDetalhes.orderBy(fields);
					rsm.setValueToField("RSM_DETALHE", rsmDetalhes);
					
				}
				rsm.beforeFirst();
			}
			
			return rsm;
		}
		catch (Exception e) {
			System.out.println("Erro ao buscar log: "+e.getMessage());
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}

