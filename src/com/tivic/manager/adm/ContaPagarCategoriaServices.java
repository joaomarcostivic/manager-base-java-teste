package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ContaPagarCategoriaServices {
	
	public static Result save(ContaPagarCategoria contaPagarCategoria){
		return save(contaPagarCategoria, null, null);
	}
	
	public static Result save(ContaPagarCategoria contaPagarCategoria, AuthData auth){
		return save(contaPagarCategoria, auth, null);
	}

	public static Result save(ContaPagarCategoria contaPagarCategoria, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(contaPagarCategoria==null)
				return new Result(-1, "Erro ao salvar. ContaPagarCategoria é nulo");

			int retorno;
			
			int tpAcao = ComplianceManager.TP_ACAO_ANY;
			
			//TODO: validar valor das categorias
//			int cdConta = contaPagarCategoria.getCdContaPagar();
//			ContaPagar conta = ContaPagarDAO.get(cdConta, connect);
//			ResultSetMap rsmCategorias = getCategoriaOfContaPagar(cdConta);
//			if(conta!=null && rsmCategorias!=null) {
//				Double vlClassificacoesReceita = 0d;
//				Double vlClassificacoesDespesa = 0d;
//				
//				while(rsmCategorias.next()) {
//					int cdCategoria = rsmCategorias.getInt("cd_categoria_economica");
//					CategoriaEconomica categoria = CategoriaEconomicaDAO.get(cdCategoria, connect);
//					if(categoria.getTpCategoriaEconomica()==CategoriaEconomicaServices.TP_RECEITA) {
//						vlClassificacoesReceita += rsmCategorias.getDouble("vl_conta_categoria");
//					}
//					else if(categoria.getTpCategoriaEconomica()==CategoriaEconomicaServices.TP_DESPESA) {
//						vlClassificacoesDespesa += rsmCategorias.getDouble("vl_conta_categoria");
//					}
//				}
//				
//				if(vlClassificacoesReceita>conta.getVlAbatimento()) {
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-2, "Classificações não batem com o 'Valor do Abatimento'.");
//				}
//				
//				if(vlClassificacoesDespesa>conta.getVlConta()) {
//					if(isConnectionNull)
//						Conexao.rollback(connect);
//					return new Result(-2, "Classificações não batem com o 'Valor da Conta'.");
//				}
//
//			}
			
			
			//---
			if(contaPagarCategoria.getCdContaPagarCategoria()==0){
				retorno = ContaPagarCategoriaDAO.insert(contaPagarCategoria, connect);
				contaPagarCategoria.setCdContaPagarCategoria(retorno);
				tpAcao = ComplianceManager.TP_ACAO_INSERT;
			}
			else {
				retorno = ContaPagarCategoriaDAO.update(contaPagarCategoria, connect);
				tpAcao = ComplianceManager.TP_ACAO_UPDATE;
			}
			
			// COMPLIANCE
			ComplianceManager.process(contaPagarCategoria, auth, tpAcao, connect);

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CONTAPAGARCATEGORIA", contaPagarCategoria);
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
	public static Result remove(int cdContaPagarCategoria, AuthData auth){
		return remove(cdContaPagarCategoria, false, auth, null);
	}
	public static Result remove(int cdContaPagarCategoria, boolean cascade, AuthData auth){
		return remove(cdContaPagarCategoria, cascade, auth, null);
	}
	public static Result remove(int cdContaPagarCategoria, boolean cascade, AuthData auth, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			ContaPagarCategoria cpc = ContaPagarCategoriaDAO.get(cdContaPagarCategoria, connect);
			int tpAcao = ComplianceManager.TP_ACAO_DELETE;
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			

			// COMPLIANCE
			ComplianceManager.process(cpc, auth, tpAcao, connect);
			
			if(!cascade || retorno>0)
				retorno = ContaPagarCategoriaDAO.delete(cdContaPagarCategoria, connect);
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
	
	public static Result remove(int cdContaPagar, int cdCategoriaEconomica){
		return remove(cdContaPagar, cdCategoriaEconomica, false, null);
	}
	public static Result remove(int cdContaPagar, int cdCategoriaEconomica, boolean cascade){
		return remove(cdContaPagar, cdCategoriaEconomica, cascade, null);
	}
	public static Result remove(int cdContaPagar, int cdCategoriaEconomica, boolean cascade, Connection connect){
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
			retorno = ContaPagarCategoriaDAO.delete(cdContaPagar, cdCategoriaEconomica, connect);
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
	
	
	public static int deleteAll(int cdContaPagar) {
		return deleteAll(cdContaPagar, null);
	}

	public static int deleteAll(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			//remove movimento da categoria
			
			PreparedStatement pstmtMov = connect.prepareStatement("DELETE " +
					"FROM adm_movimento_conta_categoria " +
					"WHERE cd_conta_pagar=?");
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE " +
					"FROM adm_conta_pagar_categoria " +
					"WHERE cd_conta_pagar=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaServices.deleteAll: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getCategoriaOfContaPagar(int cdContaPagar) {
		return getCategoriaOfContaPagar(cdContaPagar, (Connection)null);
	}

	public static ResultSetMap getCategoriaOfContaPagar(int cdContaPagar, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return getCategoriaOfContaPagar(cdContaPagar, null, connect);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaServices.getCategoriaOfContaPagar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getCategoriaOfContaPagar(int cdContaPagar, ArrayList<Integer> cdCategoriasValidas) {
		return getCategoriaOfContaPagar(cdContaPagar, cdCategoriasValidas, null);
	}

	public static ResultSetMap getCategoriaOfContaPagar(int cdContaPagar, ArrayList<Integer> cdCategoriasValidas, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT A.*, B.nm_categoria_economica, B.nr_categoria_economica, B.tp_categoria_economica, C.nm_centro_custo, C.nr_centro_custo " +
			           "FROM adm_conta_pagar_categoria A " +
			           "JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica) "+
			           "LEFT OUTER JOIN ctb_centro_custo C ON (A.cd_centro_custo = C.cd_centro_custo) " + 
			           "WHERE A.cd_conta_pagar = "+cdContaPagar;
			//Apenas as categorias que estiverem no arrayList poderam ser incluidas
			if(cdCategoriasValidas != null && cdCategoriasValidas.size() > 0){
				sql += " AND ( ";
				for(int i = 0; i < cdCategoriasValidas.size(); i++){
					sql += " A.cd_categoria_economica = " + cdCategoriasValidas.get(i);
					if(i < cdCategoriasValidas.size() - 1)
						sql += " OR ";
				}
				sql += " ) ";
			}
			pstmt = connect.prepareStatement(sql);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaServices.getCategoriaOfContaPagar: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaServices.getCategoriaOfContaPagar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result substituirCategoria(int cdContaPagar, int cdCategoriaAnterior, int cdNovaCategoria) {
		Connection connect = Conexao.conectar();
		try {
			connect.setAutoCommit(false);
			/*
			 * Susbstitui classificação da conta
			 * 	Colocando o valor da categoria anterior na nova categoria (quando a categoria nova já existir)
			 */
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_conta_pagar A, adm_conta_pagar_categoria B " +
					 								"WHERE A.cd_conta_pagar         = B.cd_conta_pagar " +
					 								"  AND B.cd_categoria_economica = "+cdCategoriaAnterior+
					 								"  AND A.cd_conta_pagar         = "+cdContaPagar).executeQuery();
			float prValor = 0;
			if(rs.next())
				prValor = rs.getFloat("vl_conta_categoria") / rs.getFloat("vl_conta");
			else
				return new Result(-1, "Erro ao tentar consultar dados da conta e da classificação!", new Exception(""));
			// No caso da conta substitui o valor total da categoria
			connect.prepareStatement(
					 "UPDATE adm_conta_pagar_categoria " +
					 "SET vl_conta_categoria = vl_conta_categoria + (SELECT vl_conta_categoria FROM adm_conta_pagar_categoria A " +
					 "                                               WHERE A.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar" +
					 "                                                 AND A.cd_categoria_economica = "+cdCategoriaAnterior+") "+
					 "WHERE cd_categoria_economica = "+cdNovaCategoria+
					 "  AND cd_conta_pagar         = "+cdContaPagar+
					 "  AND EXISTS (SELECT * FROM adm_conta_pagar_categoria B " +
					 "              WHERE B.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar" +
					 "                AND B.cd_categoria_economica = "+cdNovaCategoria+") ").executeUpdate();
			// Excluindo a categoria anterior onde já existir a categoria nova
			connect.prepareStatement(
					 "DELETE FROM adm_conta_pagar_categoria " +
					 "WHERE cd_categoria_economica = "+cdCategoriaAnterior+
					 "  AND cd_conta_pagar         = "+cdContaPagar+
					 "  AND EXISTS (SELECT * FROM adm_conta_pagar_categoria A " +
					 "              WHERE A.cd_conta_pagar = adm_conta_pagar_categoria.cd_conta_pagar" +
					 "                AND A.cd_categoria_economica = "+cdNovaCategoria+") ").executeUpdate();
			// Atualizando da categoria anterior para a nova categoria
			connect.prepareStatement("UPDATE adm_conta_pagar_categoria SET cd_categoria_economica = "+cdNovaCategoria+
					 				 " WHERE cd_categoria_economica = "+cdCategoriaAnterior+
					 				 "   AND cd_conta_pagar         = "+cdContaPagar).executeUpdate();
			/*
			 * Substitui classificação de movimentações
			 */
			rs = connect.prepareStatement("SELECT * FROM adm_movimento_conta A, adm_movimento_conta_pagar B " +
										  "WHERE A.cd_conta           = B.cd_conta " +
										  "  AND A.cd_movimento_conta = B.cd_movimento_conta " +
										  "  AND B.cd_conta_pagar     = " +cdContaPagar).executeQuery();
			while(rs.next())	{
				float vlTransferido = prValor * rs.getFloat("vl_pago");
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
			System.err.println("Erro! ContaPagarCategoriaServices.substituirClassificacao: " +  e);
			return new Result(-1, "Erro ao tentar substituir categoria!", e);
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_categoria");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarCategoriaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_pagar_categoria", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
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
					.search(" SELECT * FROM adm_conta_pagar_categoria "
						+ " WHERE 1=1"
						+ (lgDelete ? 
						  " AND tp_acao_compliance="+ComplianceManager.TP_ACAO_DELETE+" AND cd_conta_pagar="+cdConta
						  :
						  " AND cd_conta_pagar_categoria="+cdContaCategoria+" AND cd_conta_pagar="+cdConta)
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

