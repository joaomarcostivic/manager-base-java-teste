package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.ClassificacaoFiscal;
import com.tivic.manager.adm.ClassificacaoFiscalDAO;
import com.tivic.manager.adm.ProdutoServicoPreco;
import com.tivic.manager.adm.ProdutoServicoPrecoDAO;
import com.tivic.manager.adm.ProdutoServicoPrecoServices;
import com.tivic.manager.adm.TabelaPreco;
import com.tivic.manager.adm.TabelaPrecoDAO;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.alm.Grupo;
import com.tivic.manager.alm.GrupoDAO;
import com.tivic.manager.alm.GrupoServices;
import com.tivic.manager.alm.ProdutoEstoqueServices;
import com.tivic.manager.alm.ProdutoGrupo;
import com.tivic.manager.alm.ProdutoGrupoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProdutoServicoServices {

	public static final int TP_PRODUTO = 0;
	public static final int TP_SERVICO = 1;
	//TP_GRUPO indica que o registro representa um grupo de produto_servico
	public static final int TP_GRUPO   = 2; 

	public static Result save(ProdutoServico produtoServico){
		return save(produtoServico, null);
	}
	
	public static Result save(ProdutoServico produtoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(produtoServico==null)
				return new Result(-1, "Erro ao salvar. Produto/Serviço é nulo");
			
			int retorno;
			if(produtoServico.getCdProdutoServico()==0){
				retorno = ProdutoServicoDAO.insert(produtoServico, connect);
				produtoServico.setCdProdutoServico(retorno);
			}
			else {
				retorno = ProdutoServicoDAO.update(produtoServico, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRODUTOSERVICO", produtoServico);
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
	
	public static Result remove(int cdProdutoServico){
		return remove(cdProdutoServico, false, null);
	}
	
	public static Result remove(int cdProdutoServico, boolean cascade){
		return remove(cdProdutoServico, cascade, null);
	}
	
	public static Result remove(int cdProdutoServico, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
				PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_servico_empresa WHERE cd_produto_servico = ?");
				pstmt.setInt(1, cdProdutoServico);
				retorno = pstmt.executeUpdate();
				
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE FROM grl_produto_similar WHERE cd_produto_servico = ?");
					pstmt.setInt(1, cdProdutoServico);
					retorno = pstmt.executeUpdate();
				}
				if(retorno>0) {
					pstmt = connect.prepareStatement("DELETE FROM grl_produto_servico_parametro WHERE cd_produto_servico = ?");
					pstmt.setInt(1, cdProdutoServico);
					retorno = pstmt.executeUpdate();
				}
			}
				
			if(!cascade || retorno>0)
				retorno = ProdutoServicoDAO.delete(cdProdutoServico, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este produto/serviço está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Produto/serviço excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir produto/serviço!");
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
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_empresa, B.cd_unidade_medida, D.nm_unidade_medida, D.sg_unidade_medida, " +
					"C.nm_pessoa AS nm_fabricante, B.st_produto_empresa, B.dt_desativacao, B.id_reduzido, B.vl_preco_medio, B.vl_custo_medio, " +
					"B.vl_ultimo_custo, B.qt_ideal, B.qt_minima, B.qt_maxima, B.qt_dias_estoque, B.qt_precisao_custo, B.qt_precisao_unidade, " +
					"B.qt_dias_garantia, B.tp_reabastecimento, B.tp_controle_estoque, B.tp_transporte, B.nr_ordem, " +
					"E.nm_categoria_economica as nm_categoria_receita, E1.nm_categoria_economica as nm_categoria_despesa " +
					"FROM grl_produto_servico A " +
					"LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_fabricante = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_unidade_medida D ON (B.cd_unidade_medida = D.cd_unidade_medida) " + 
					"LEFT OUTER JOIN adm_categoria_economica E ON (A.cd_categoria_receita = E.cd_categoria_economica) " +
					"LEFT OUTER JOIN adm_categoria_economica E1 ON (A.cd_categoria_despesa = E1.cd_categoria_economica) "+
					" WHERE A.tp_produto_servico<>"+TP_GRUPO+
					" ORDER BY A.nm_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllGrupo() {
		return getAllGrupo(null);
	}

	public static ResultSetMap getAllGrupo(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_empresa, B.cd_unidade_medida, D.nm_unidade_medida, D.sg_unidade_medida, " +
					"C.nm_pessoa AS nm_fabricante, B.st_produto_empresa, B.dt_desativacao, B.id_reduzido, B.vl_preco_medio, B.vl_custo_medio, " +
					"B.vl_ultimo_custo, B.qt_ideal, B.qt_minima, B.qt_maxima, B.qt_dias_estoque, B.qt_precisao_custo, B.qt_precisao_unidade, " +
					"B.qt_dias_garantia, B.tp_reabastecimento, B.tp_controle_estoque, B.tp_transporte, B.nr_ordem, " +
					"E.nm_categoria_economica as nm_categoria_receita, E1.nm_categoria_economica as nm_categoria_despesa " +
					" FROM grl_produto_servico A " +
					" LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					" LEFT OUTER JOIN grl_pessoa C ON (A.cd_fabricante = C.cd_pessoa) " +
					" LEFT OUTER JOIN grl_unidade_medida D ON (B.cd_unidade_medida = D.cd_unidade_medida) " + 
					" LEFT OUTER JOIN adm_categoria_economica E ON (A.cd_categoria_receita = E.cd_categoria_economica) " +
					" LEFT OUTER JOIN adm_categoria_economica E1 ON (A.cd_categoria_despesa = E1.cd_categoria_economica) "+
					" WHERE A.tp_produto_servico="+TP_GRUPO+
					" ORDER BY A.nm_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAllGrupo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllPod() {
		return getAllPod(null);
	}

	public static ResultSetMap getAllPod(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_produto_servico, A.nm_produto_servico, A.id_produto_servico " +
					"FROM grl_produto_servico A "+ 
					" WHERE NOT EXISTS (SELECT B.cd_curso FROM acd_curso B WHERE A.cd_produto_servico = B.cd_curso)" +
					"   AND NOT EXISTS (SELECT C.cd_disciplina FROM acd_disciplina C WHERE A.cd_produto_servico = C.cd_disciplina)" +
					"   AND NOT EXISTS (SELECT D.cd_curso_modulo FROM acd_curso_modulo D WHERE A.cd_produto_servico = D.cd_curso_modulo)" +
					"ORDER BY A.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("QT_ESTOQUE", 0);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAll: " + e);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//AQUI!!
	public static Result insertGrade(int cdProdutoServico, int cdEmpresa, ArrayList<String> grade){
		return insertGrade(cdProdutoServico, cdEmpresa, grade, false);
	}
	
	public static Result insertGrade(int cdProdutoServico, int cdEmpresa, ArrayList<String> grade, boolean hasName){
		Connection connection    = null;
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ProdutoServico        produtoServico = ProdutoServicoDAO.get(cdProdutoServico, connection) ;
			Produto        		  produto        = ProdutoDAO.get(cdProdutoServico, connection) ;
			ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico, connection);
			if(produtoServico==null || produtoEmpresa==null) {
				return new Result(-1, "Código do produto ou empresa inválido! [cdProdutoServico="+cdProdutoServico+","+cdEmpresa+"]");
			}
			ResultSetMap rsmGrupos = ProdutoServicoEmpresaServices.getAllGrupos(cdProdutoServico, cdEmpresa, connection);
			ArrayList<Integer> produtos = new ArrayList<Integer>();
			produtos.add(cdProdutoServico);
			//
			Result result = new Result(1, "Sucesso");//<====
			String erros = "";
			for(int i=0; i<grade.size(); i++)	{
				java.util.StringTokenizer tokens = new java.util.StringTokenizer((String)grade.get(i), "|", false);
				//
				if(tokens.countTokens() == 5 && !hasName) {
					String txtQuantidade = tokens.nextToken().trim();
					int qtEmbalagem;
					if(Util.isNumber(txtQuantidade)) 
						qtEmbalagem 	= Integer.parseInt(txtQuantidade);//[+]
					else
						return new Result(-1, "Quantidade Incorreta");
				}
					
				
				String txtDadoTecnico   = tokens.nextToken();
				String txtEspecificacao = tokens.nextToken();
				String idProdutoServico = tokens.nextToken().trim();
				String nrReferencia     = tokens.nextToken();
				String nmProdutoServico = null;
				if(hasName){
					nmProdutoServico = tokens.nextToken();
				}
				
				
				PreparedStatement pstmtCodBarras = connection.prepareStatement("SELECT cd_produto_servico " +
						                   "	FROM grl_produto_servico  " +
						                   "	WHERE TRIM(id_produto_servico) = ? " +
						                   "    	  AND TRIM(id_produto_servico) <> '' " +
						                   "		  AND TRIM(id_produto_servico) IS NOT NULL");
				pstmtCodBarras.setString(1, idProdutoServico);
				ResultSet rs = pstmtCodBarras.executeQuery();
				if (rs.next()) {
					erros += (erros.equals("") ? idProdutoServico : ", " + idProdutoServico);
					continue;
				}
				
				// Mudando os dados do produto/serviço
				int cdNewProdutoServico = 0;
				if(produto!=null)	{
					//produto.setQtEmbalagem(qtEmbalagem);
					produto.setIdProdutoServico(idProdutoServico);
					produto.setTxtDadoTecnico(txtDadoTecnico);
					produto.setTxtEspecificacao(txtEspecificacao);
					produto.setNrReferencia(nrReferencia);
					produto.setCdProdutoServico(0);
					if(nmProdutoServico != null)
						produto.setNmProdutoServico(nmProdutoServico);
					cdNewProdutoServico = ProdutoDAO.insert(produto, connection);
				}
				else	{
					produtoServico.setIdProdutoServico(idProdutoServico);
					produtoServico.setTxtDadoTecnico(txtDadoTecnico);
					produtoServico.setTxtEspecificacao(txtEspecificacao);
					produtoServico.setCdProdutoServico(0);
					produtoServico.setNrReferencia(nrReferencia);
					if(nmProdutoServico != null)
						produtoServico.setNmProdutoServico(nmProdutoServico);
					cdNewProdutoServico = ProdutoServicoDAO.insert(produtoServico, connection);
				}
				// Dados do produto na empresa
				
				
				produtoEmpresa.setCdProdutoServico(cdNewProdutoServico);
				ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connection);
				
				String idReduzidoMask = ParametroServices.getValorOfParametro("DS_ID_REDUZIDO_MASK", "", cdEmpresa, connection);
				idReduzidoMask 	      = idReduzidoMask!=null ? idReduzidoMask.toUpperCase() : "";
				boolean isByGrupo     = idReduzidoMask.indexOf("G")>=0;
				
				// Dados da generalização PRODUTO
				// Grupos
				rsmGrupos.beforeFirst();
				while(rsmGrupos.next())	{
					ProdutoGrupo produtoGrupo = new ProdutoGrupo(cdNewProdutoServico,rsmGrupos.getInt("cd_grupo"),cdEmpresa,rsmGrupos.getInt("lg_principal"));
					ProdutoGrupoDAO.insert(produtoGrupo, connection);
					if(isByGrupo && ParametroServices.getValorOfParametroAsInteger("LG_ID_REDUZIDO_DIFERENTE_ORIGINAL", 0, cdEmpresa)==1 && rsmGrupos.getInt("lg_principal") == 1)
						produtoEmpresa.setIdReduzido(ProdutoServicoEmpresaServices.getNextIdReduzido(cdEmpresa, rsmGrupos.getInt("cd_grupo"), connection));
					
					ProdutoServicoEmpresaDAO.update(produtoEmpresa, connection);
				}
				rsmGrupos.beforeFirst();
				if(ParametroServices.getValorOfParametroAsInteger("LG_ID_REDUZIDO_DIFERENTE_ORIGINAL", 0, cdEmpresa)==1 && rsmGrupos.size() == 0 && !isByGrupo)
					produtoEmpresa.setIdReduzido(ProdutoServicoEmpresaServices.getNextIdReduzido(cdEmpresa, connection));
				
				ProdutoServicoEmpresaDAO.update(produtoEmpresa, connection);
				
				// Salvando lista
				produtos.add(cdNewProdutoServico);
			}
			for(int i=0; i<produtos.size(); i++)	{
				cdProdutoServico = produtos.get(i); 
				for(int s=0; s<produtos.size(); s++)	{
					int cdNewProdutoServico = produtos.get(s);
					if(cdNewProdutoServico != cdProdutoServico)
						ProdutoSimilarDAO.insert(new ProdutoSimilar(cdProdutoServico, cdNewProdutoServico, 0), connection);
				}
			}
			
			//Reproduzindo preços do produto original para os produtos da grade
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_produto_servico", "" + produtos.get(0), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmProdutoPreco = ProdutoServicoPrecoDAO.find(criterios, connection);
			while(rsmProdutoPreco.next()){
				ProdutoServicoPreco produtoServicoPreco = ProdutoServicoPrecoDAO.get(rsmProdutoPreco.getInt("cd_tabela_preco"), rsmProdutoPreco.getInt("cd_produto_servico"), rsmProdutoPreco.getInt("cd_produto_servico_preco"), connection);
				for(int i = 1; i < produtos.size(); i++){
					produtoServicoPreco.setCdProdutoServico(produtos.get(i));
					if(ProdutoServicoPrecoServices.insert(produtoServicoPreco, connection) < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao criar preço para produto da grade");
					}
				}
			}
			
			// Similares
			if (isConnectionNull && erros.equals(""))
				connection.commit();
			
			result.addObject("codigos", produtos);
			if(!erros.equals("")){
				result.setCode(-2);
				result.setMessage("Códigos de Barra já existentes no sistema: " + erros);
			}
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Falha ao tentar cadastrar grade!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result updateGrade(int cdProdutoServico, int cdEmpresa, ArrayList<String> grade, boolean hasName){
		Connection connection    = null;
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ProdutoServico        produtoServico = ProdutoServicoDAO.get(cdProdutoServico, connection) ;
			Produto        		  produto        = ProdutoDAO.get(cdProdutoServico, connection) ;
			if(produtoServico==null) {
				return new Result(-1, "Código do produto ou empresa inválido! [cdProdutoServico="+cdProdutoServico+","+cdEmpresa+"]");
			}
			//
			Result result = new Result(1, "Sucesso");//<====
			for(int i=0; i<grade.size(); i++)	{
				java.util.StringTokenizer tokens = new java.util.StringTokenizer((String)grade.get(i), "|", false);
				//
				String txtDadoTecnico   = tokens.nextToken();
				String txtEspecificacao = tokens.nextToken();
				String idProdutoServico = tokens.nextToken().trim();
				String nrReferencia     = tokens.nextToken();
				String nmProdutoServico = tokens.nextToken();
				
				
				// Mudando os dados do produto/serviço
				if(produto!=null)	{
					//produto.setQtEmbalagem(qtEmbalagem);
					produto.setIdProdutoServico(idProdutoServico);
					produto.setTxtDadoTecnico(txtDadoTecnico);
					produto.setTxtEspecificacao(txtEspecificacao);
					produto.setNrReferencia(nrReferencia);
					produto.setNmProdutoServico(nmProdutoServico);
					ProdutoDAO.update(produto, connection);
				}
				else	{
					produtoServico.setIdProdutoServico(idProdutoServico);
					produtoServico.setTxtDadoTecnico(txtDadoTecnico);
					produtoServico.setTxtEspecificacao(txtEspecificacao);
					produtoServico.setNrReferencia(nrReferencia);
					produtoServico.setNmProdutoServico(nmProdutoServico);
					ProdutoServicoDAO.update(produtoServico, connection);
				}
				
			}
			
			// Similares
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Falha ao tentar cadastrar grade!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getRelacionado(int cdProdutoServico, int cdSimilar) {
		return getRelacionado(cdProdutoServico, cdSimilar, null);
	}
	
	public static ResultSetMap getRelacionado(int cdProdutoServico, int cdSimilar, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			ResultSetMap rsm = Search.find("SELECT A.cd_produto_servico, A.nm_produto_servico, PS.* " +
					"FROM grl_produto_servico A " +
					"JOIN grl_produto_similar PS ON (PS.cd_produto_servico = "+cdProdutoServico +
					"									AND PS.cd_similar = A.cd_produto_servico " +
					"									AND lg_referenciado = 1) " +
					"WHERE A.cd_produto_servico = " + cdSimilar, new ArrayList<ItemComparator>(), true, connection, false);

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getSubstituto(int cdProdutoServico, int cdSimilar) {
		return getSubstituto(cdProdutoServico, cdSimilar, null);
	}
	
	public static ResultSetMap getSubstituto(int cdProdutoServico, int cdSimilar, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			ResultSetMap rsm = Search.find("SELECT A.cd_produto_servico, A.nm_produto_servico, PS.* " +
											"FROM grl_produto_servico A " +
											"JOIN grl_produto_similar PS ON (PS.cd_produto_servico = "+cdProdutoServico +
											"									AND PS.cd_similar = A.cd_produto_servico " +
											"									AND lg_referenciado = 0) " +
											"WHERE A.cd_produto_servico = " + cdSimilar, new ArrayList<ItemComparator>(), true, connection, false);

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	//Gabriel 20121016 - Houve mudança de idReduzido para nrReferencia
//	public static Result insertGrade(int cdProdutoServico, int cdEmpresa, ArrayList<String> grade){
//		Connection connection    = null;
//		boolean isConnectionNull = connection==null;
//		try {
//			connection = isConnectionNull ? Conexao.conectar() : connection;
//			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
//
//			ProdutoServico        produtoServico = ProdutoServicoDAO.get(cdProdutoServico, connection) ;
//			Produto        		  produto        = ProdutoDAO.get(cdProdutoServico, connection) ;
//			ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico, connection);
//			if(produtoServico==null || produtoEmpresa==null) {
//				return new Result(-1, "Código do produto ou empresa inválido! [cdProdutoServico="+cdProdutoServico+","+cdEmpresa+"]");
//			}
//			ResultSetMap rsmGrupos = ProdutoServicoEmpresaServices.getAllGrupos(cdProdutoServico, cdEmpresa, connection);
//			ArrayList<Integer> produtos = new ArrayList<Integer>();
//			produtos.add(cdProdutoServico);
//			//
//			for(int i=0; i<grade.size(); i++)	{
//				java.util.StringTokenizer tokens = new java.util.StringTokenizer((String)grade.get(i), "|", false);
//				//
//				String txtDadoTecnico   = tokens.nextToken();
//				String txtEspecificacao = tokens.nextToken();
//				String idProdutoServico = tokens.nextToken().trim();
//				String idReduzido       = tokens.nextToken();
//				// Mudando os dados do produto/serviço
//				int cdNewProdutoServico = 0;
//				if(produto!=null)	{
//					produto.setIdProdutoServico(idProdutoServico);
//					produto.setTxtDadoTecnico(txtDadoTecnico);
//					produto.setTxtEspecificacao(txtEspecificacao);
//					produto.setCdProdutoServico(0);
//					cdNewProdutoServico = ProdutoDAO.insert(produto, connection);
//				}
//				else	{
//					produtoServico.setIdProdutoServico(idProdutoServico);
//					produtoServico.setTxtDadoTecnico(txtDadoTecnico);
//					produtoServico.setTxtEspecificacao(txtEspecificacao);
//					produtoServico.setCdProdutoServico(0);
//					cdNewProdutoServico = ProdutoServicoDAO.insert(produtoServico, connection);
//				}
//				// Dados do produto na empresa
//				produtoEmpresa.setCdProdutoServico(cdNewProdutoServico);
//				produtoEmpresa.setIdReduzido(idReduzido);
//				ProdutoServicoEmpresaDAO.insert(produtoEmpresa, connection);
//				// Dados da generalização PRODUTO
//				// Grupos
//				rsmGrupos.beforeFirst();
//				while(rsmGrupos.next())	{
//					ProdutoGrupo produtoGrupo = new ProdutoGrupo(cdNewProdutoServico,rsmGrupos.getInt("cd_grupo"),cdEmpresa,rsmGrupos.getInt("lg_principal"));
//					ProdutoGrupoDAO.insert(produtoGrupo, connection);
//				}
//				// Salvando lista
//				produtos.add(cdNewProdutoServico);
//			}
//			for(int i=0; i<produtos.size(); i++)	{
//				cdProdutoServico = produtos.get(i); 
//				for(int s=0; s<produtos.size(); s++)	{
//					int cdNewProdutoServico = produtos.get(s);
//					if(cdNewProdutoServico != cdProdutoServico)
//						ProdutoSimilarDAO.insert(new ProdutoSimilar(cdProdutoServico, cdNewProdutoServico, 0), connection);
//				}
//			}
//			// Similares
//			if (isConnectionNull)
//				connection.commit();
//
//			return new Result(1);
//		}
//		catch(Exception e) {
//			e.printStackTrace(System.out);
//			if (isConnectionNull)
//				Conexao.rollback(connection);
//			return new Result(-1, "Falha ao tentar cadastrar grade!", e);
//		}
//		finally {
//			if (isConnectionNull)
//				Conexao.desconectar(connection);
//		}
//	}

	public static int getEventoFinanceiro(int cdProdutoServico, int cdEmpresa, int tpEvento) {
		return getEventoFinanceiro(cdProdutoServico, cdEmpresa, tpEvento, null);
	}

	public static int getEventoFinanceiro(int cdProdutoServico, int cdEmpresa, int tpEvento, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			ResultSet rs = connection.prepareStatement("SELECT A.cd_grupo, B.cd_grupo_superior, B.cd_evento_adesao_contrato, B.cd_evento_contratacao " +
													   "FROM alm_produto_grupo A, alm_grupo B " +
													   "WHERE A.cd_grupo           = B.cd_grupo " +
													   "  AND A.cd_produto_servico = " +cdProdutoServico+
													   "  AND A.cd_empresa         = " +cdEmpresa+
													   "ORDER BY lg_principal DESC").executeQuery();

			return !rs.next() ? 0 : tpEvento==GrupoServices.EVT_ADESAO ? (rs.getInt("cd_evento_adesao_contrato")!=0 ?
					rs.getInt("cd_evento_adesao_contrato") : rs.getInt("cd_grupo_superior")!=0 ? GrupoServices.getEventoFinanceiro(rs.getInt("cd_grupo_superior"), tpEvento, connection) : 0) :
						rs.getInt("cd_evento_contratacao")>0 ? rs.getInt("cd_evento_contratacao") :
							rs.getInt("cd_grupo_superior")!=0 ? GrupoServices.getEventoFinanceiro(rs.getInt("cd_grupo_superior"), tpEvento, connection) : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getEventoFinanceiro: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllFabricante() {
		return getAllFabricante(null);
	}

	public static ResultSetMap getAllFabricante(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			return new  ResultSetMap(connection.prepareStatement("SELECT cd_pessoa, nm_pessoa FROM grl_pessoa A "+
					                                             "WHERE EXISTS (SELECT * FROM grl_produto_servico B " +
					                                             "              WHERE A.cd_pessoa = B.cd_fabricante) " +
					                                             "ORDER BY nm_pessoa").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int getCategoriaEconomica(int cdProdutoServico, int cdEmpresa, int tpCategoria) {
		return getCategoriaEconomica(cdProdutoServico, cdEmpresa, tpCategoria, null);
	}

	public static int getCategoriaEconomica(int cdProdutoServico, int cdEmpresa, int tpCategoria, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_grupo, B.cd_grupo_superior, " +
					"B.cd_categoria_receita, B.cd_categoria_despesa " +
					"FROM alm_produto_grupo A, alm_grupo B " +
					"WHERE A.cd_grupo = B.cd_grupo " +
					"  AND A.cd_produto_servico = ? " +
					"  AND A.cd_empresa = ? " +
					"ORDER BY lg_principal DESC");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();

			return !rs.next() ? 0 : tpCategoria==GrupoServices.CAT_RECEITA ? (rs.getInt("cd_categoria_receita")!=0 ?
					rs.getInt("cd_categoria_receita") : rs.getInt("cd_grupo_superior")!=0 ? GrupoServices.getCategoriaEconomomica(rs.getInt("cd_grupo_superior"), tpCategoria, connection) : 0) :
						rs.getInt("cd_categoria_despesa")>0 ? rs.getInt("cd_categoria_despesa") :
							rs.getInt("cd_grupo_superior")!=0 ? GrupoServices.getCategoriaEconomomica(rs.getInt("cd_grupo_superior"), tpCategoria, connection) : 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getEventoFinanceiro: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllPrecos(int cdProdutoServico, int cdEmpresa) {
		return getAllPrecos(cdProdutoServico, cdEmpresa, null);
	}

	public static ResultSetMap getAllPrecos(int cdProdutoServico, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_tabela_preco, C.vl_ultimo_custo " +
					"FROM ADM_PRODUTO_SERVICO_PRECO A " +
					"LEFT JOIN ADM_TABELA_PRECO B ON (A.cd_tabela_preco = B.cd_tabela_preco) " +
					"LEFT JOIN grl_produto_servico_empresa C ON (A.cd_produto_servico = C.cd_produto_servico" +
					"											AND B.cd_empresa = C.cd_empresa) " +
					"WHERE A.dt_termino_validade IS NULL " +
					"  AND A.cd_produto_servico = ? " +
					"  AND B.cd_empresa = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAllPrecos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFornecedores(int cdProdutoServico, int cdEmpresa) {
		return getAllFornecedores(cdProdutoServico, cdEmpresa, null);
	}

	public static ResultSetMap getAllFornecedores(int cdProdutoServico, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_fornecedor, " +
					"C.nm_pessoa AS nm_representante " +
					"FROM adm_produto_fornecedor A " +
					"JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_representante = C.cd_pessoa) " +
					"WHERE A.cd_produto_servico = ? " +
					"  AND A.cd_empresa = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAllFornecedores: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findFornecedores(ArrayList<ItemComparator> criterios) {
		return findFornecedores(criterios, null);
	}
	
	public static ResultSetMap findFornecedores(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			ResultSetMap rsm = Search.find("SELECT A.*, B.nm_pessoa AS nm_fornecedor, " +
					"C.nm_pessoa AS nm_representante " +
					"FROM adm_produto_fornecedor A " +
					"JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_representante = C.cd_pessoa) WHERE 1=1 ", criterios, true, connection, false);

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllSimilares(int cdProdutoServico, int lgReferenciado) {
		return getAllSimilares(cdProdutoServico, lgReferenciado, null);
	}

	public static ResultSetMap getAllSimilares(int cdProdutoServico, int lgReferenciado, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT A.cd_produto_servico AS cd_produto_servico_similar, A.nm_produto_servico, A.txt_dado_tecnico, " +
						 "		 A.txt_especificacao, A.id_produto_servico, A.nr_referencia, B.* " +
					     "FROM grl_produto_servico A, grl_produto_similar B " +
		      			 "WHERE A.cd_produto_servico = B.cd_similar " +
		      			 "  AND B.cd_produto_servico = "+cdProdutoServico+
		      			 (lgReferenciado >= 0 ? " AND B.lg_referenciado = " + lgReferenciado : "");
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_TP_REABASTECIMENTO", "");
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

	public static ResultSetMap getAllGrupos(int cdProdutoServico) {
		return getAllGrupos(cdProdutoServico, null);
	}

	public static ResultSetMap getAllGrupos(int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.lg_principal " +
					"FROM alm_grupo A, grl_produto_grupo B " +
					"WHERE A.cd_grupo = B.cd_grupo " +
					"  AND B.cd_produto_servico = ?");
			pstmt.setInt(1, cdProdutoServico);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAllGrupos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllGruposGrid(int cdProdutoServico) {
		return getAllGruposGrid(cdProdutoServico, null);
	}

	public static ResultSetMap getAllGruposGrid(int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			ResultSetMap rsm = getAllGrupos(cdProdutoServico);
			
			while(rsm.next()){
				
			}
			
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAllGrupos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico) {
		return delete(cdProdutoServico, null);
	}

	public static int delete(int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_servico_empresa WHERE cd_produto_servico = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.execute();

			pstmt = connect.prepareStatement("DELETE FROM grl_produto_similar WHERE cd_produto_servico = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.execute();

			if (ProdutoServicoDAO.delete(cdProdutoServico, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return 0;
			}

			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAllSimilares: " + e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null) ;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.cd_empresa, B.cd_unidade_medida, D.nm_unidade_medida, D.sg_unidade_medida, " +
				"C.nm_pessoa AS nm_fabricante, B.st_produto_empresa, B.dt_desativacao, B.id_reduzido, B.vl_preco_medio, B.vl_custo_medio, " +
				"B.vl_ultimo_custo, B.qt_ideal, B.qt_minima, B.qt_maxima, B.qt_dias_estoque, B.qt_precisao_custo, B.qt_precisao_unidade, " +
				"B.qt_dias_garantia, B.tp_reabastecimento, B.tp_controle_estoque, B.tp_transporte, B.nr_ordem, " +
				"E.nm_categoria_economica as nm_categoria_receita, E1.nm_categoria_economica as nm_categoria_despesa " +
				"FROM grl_produto_servico A " +
				"LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico) " +
				"LEFT OUTER JOIN grl_pessoa C ON (A.cd_fabricante = C.cd_pessoa) " +
				"LEFT OUTER JOIN grl_unidade_medida D ON (B.cd_unidade_medida = D.cd_unidade_medida) " + 
				"LEFT OUTER JOIN adm_categoria_economica E ON (A.cd_categoria_receita = E.cd_categoria_economica) " +
				"LEFT OUTER JOIN adm_categoria_economica E1 ON (A.cd_categoria_despesa = E.cd_categoria_economica) ", 
				"", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int setGrupoPrincipal(int cdProdutoServico, int cdEmpresa, int cdGrupo) {
		return setGrupoPrincipal(cdProdutoServico, cdEmpresa, cdGrupo, null);
	}

	public static int setGrupoPrincipal(int cdProdutoServico, int cdEmpresa, int cdGrupo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			PreparedStatement pstmt = connection.prepareStatement("UPDATE alm_produto_grupo " +
					"SET lg_principal = 0 " +
					"WHERE cd_produto_servico = ? " +
					"  AND cd_empresa = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			pstmt.execute();

			pstmt = connection.prepareStatement("UPDATE alm_produto_grupo " +
					"SET lg_principal = 1 " +
					"WHERE cd_produto_servico = ? " +
					"  AND cd_empresa = ? " +
					"  AND cd_grupo = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdGrupo);
			pstmt.execute();

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllTributos(int cdProdutoServico) {
		return getAllTributos(cdProdutoServico, null);
	}

	public static ResultSetMap getAllTributos(int cdProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			ResultSet rs = connection.prepareStatement("SELECT * FROM grl_produto_servico " +
					                                   "WHERE cd_produto_servico = " +cdProdutoServico).executeQuery();
			int cdClassificacaoFiscal = 0;
			if(rs.next())
				cdClassificacaoFiscal = rs.getInt("cd_classificacao_fiscal");
			/*
			 * Tributos configurados para o produto/servico
			 */
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(
												"SELECT A.cd_tributo, B.nm_tributo, B.id_tributo " +
												"FROM adm_produto_servico_tributo A, adm_tributo B " +
												"WHERE A.cd_tributo = B.cd_tributo " +
												"  AND (A.cd_produto_servico = " +cdProdutoServico+
												"   OR  A.cd_classificacao_fiscal = " +cdClassificacaoFiscal+") "+
												"GROUP BY A.cd_tributo, B.nm_tributo, B.id_tributo").executeQuery());
			while (rsm != null && rsm.next()) {
				/*
				 * Aliquotas de cada tributo configurado para o produto/servico
				 */
				PreparedStatement pstmt = connection.prepareStatement(
						"SELECT A.cd_tributo, B.cd_tributo_aliquota, B.tp_operacao, B.pr_aliquota, B.pr_credito, " +
						"       B.st_tributaria, B.dt_inicio_validade, B.dt_final_validade, B.vl_inicio_faixa," +
						"       B.vl_variacao_base, B.tp_fator_variacao_base, " +
						"       B.vl_variacao_resultado, B.tp_fator_variacao_base " +
						"FROM adm_produto_servico_tributo A, adm_tributo_aliquota B " +
						"WHERE (A.cd_produto_servico      = "+cdProdutoServico+
						"   OR  A.cd_classificacao_fiscal = "+cdClassificacaoFiscal+") "+
						"  AND A.cd_tributo          = " + rsm.getInt("cd_tributo") +
						"  AND A.cd_tributo          = B.cd_tributo " +
						"  AND A.cd_tributo_aliquota = B.cd_tributo_aliquota " +
						"GROUP BY A.cd_tributo, B.cd_tributo_aliquota, B.tp_operacao, B.pr_aliquota, B.pr_credito, " +
						"         B.st_tributaria, B.dt_inicio_validade, B.dt_final_validade, B.vl_inicio_faixa," +
						"         B.vl_variacao_base, B.tp_fator_variacao_base, " +
						"         B.vl_variacao_resultado, B.tp_fator_variacao_base");
				ResultSetMap rsmAliquotas = new ResultSetMap(pstmt.executeQuery());
				rsm.getRegister().put("subResultSetMap", rsmAliquotas);
				/*
				 * Configuracoes de locais de entrada/saida configuradados em cada aliquota para produto/servico
				 */
				while (rsmAliquotas != null && rsmAliquotas.next()) {
					pstmt = connection.prepareStatement(
							"SELECT A.*, B.nm_cidade AS nm_cidade, C.nm_estado AS nm_estado, " +
							"       D.nm_pais AS nm_pais, H.nm_natureza_operacao " +
							"FROM adm_produto_servico_tributo A " +
							"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
							"LEFT OUTER JOIN grl_estado C ON (A.cd_estado = C.cd_estado) " +
							"LEFT OUTER JOIN grl_pais   D ON (A.cd_pais = D.cd_pais) " +
							"LEFT OUTER JOIN adm_natureza_operacao H ON (A.cd_natureza_operacao = H.cd_natureza_operacao) " +
							"WHERE A.cd_tributo                 = "+rsmAliquotas.getInt("cd_tributo") +
							"  AND A.cd_tributo_aliquota        = "+rsmAliquotas.getInt("cd_tributo_aliquota") +
							"  AND (A.cd_produto_servico      = "+cdProdutoServico+
						    "   OR  A.cd_classificacao_fiscal = "+cdClassificacaoFiscal+") ");
					rsmAliquotas.getRegister().put("subResultSetMap", new ResultSetMap(pstmt.executeQuery()));
				}
				rsmAliquotas.beforeFirst();
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAsResultSet(int cdProdutoServico, int cdEmpresa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_produto_servico", Integer.toString(cdProdutoServico), ItemComparator.EQUAL, Types.INTEGER));
		if (cdEmpresa > 0)
			criterios.add(new ItemComparator("CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
		return ProdutoEstoqueServices.findCompleto(criterios);
	}



	public static ResultSetMap findProdutoPreco(ArrayList<ItemComparator> criterios, int cdTipoOperacao, int cdEmpresa) {
		return findProdutoPreco(criterios, cdTipoOperacao, cdEmpresa, null);
	}

	public static ResultSetMap findProdutoPreco(ArrayList<ItemComparator> criterios, int cdTipoOperacao, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			ResultSetMap rsm = Search.find("SELECT A.*, B.nm_tabela_preco, D.id_produto_servico, D.nm_produto_servico, " +
					"F.nm_unidade_medida, F.sg_unidade_medida, " +
					"D.txt_produto_servico, D.txt_especificacao, E.cd_unidade_medida, E.id_reduzido, "+
					"(SELECT SUM(F.cd_local_armazenamento) FROM ALM_PRODUTO_ESTOQUE F WHERE F.cd_empresa=E.cd_empresa AND F.cd_produto_servico = A.cd_produto_servico) as qt_produto_estoque "+
					"FROM ADM_PRODUTO_SERVICO_PRECO A "+
					"JOIN ADM_TABELA_PRECO B ON (A.cd_tabela_preco = B.cd_tabela_preco) "+
					"JOIN ADM_TIPO_OPERACAO C ON (C.cd_tabela_preco = B.cd_tabela_preco AND C.cd_tipo_operacao = "+cdTipoOperacao+") "+
					"JOIN GRL_PRODUTO_SERVICO D ON (A.cd_produto_servico = D.cd_produto_servico) "+
					"JOIN GRL_PRODUTO_SERVICO_EMPRESA E ON (A.cd_produto_servico = E.cd_produto_servico AND "+
					"                                       E.st_produto_empresa = 1 AND "+
					"                                       E.cd_empresa = "+cdEmpresa+") " +
					"LEFT OUTER JOIN grl_unidade_medida F ON (E.cd_unidade_medida = F.cd_unidade_medida) "+
					"WHERE A.cd_tabela_preco = B.cd_tabela_preco "+
					"AND A.dt_termino_validade IS NULL ", criterios, true, connection, false);

			while(rsm.next()){
				ResultSetMap rsmUnidades = UnidadeConversaoServices.getAllUnidadeDestino(rsm.getInt("cd_unidade_medida"), connection);
				if (rsm.getInt("cd_unidade_medida")>0 && rsmUnidades!=null & !rsmUnidades.locate("cd_unidade_medida", rsm.getInt("cd_unidade_medida"))) {
					HashMap<String, Object> reg = new HashMap<String, Object>();
					reg.put("CD_UNIDADE_MEDIDA", rsm.getInt("cd_unidade_medida"));
					reg.put("NM_UNIDADE_MEDIDA", rsm.getString("nm_unidade_medida"));
					reg.put("SG_UNIDADE_MEDIDA", rsm.getString("sg_unidade_medida"));
					rsmUnidades.addRegister(reg);
					rsmUnidades.beforeFirst();
				}
				rsm.getRegister().put("subResultSetMap", rsmUnidades);
			}
			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllRegras(int cdProdutoServico, int cdTabelaPreco) {
		return getAllRegras(cdProdutoServico, cdTabelaPreco, null);
	}

	public static ResultSetMap getAllRegras(int cdProdutoServico, int cdTabelaPreco, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			TabelaPreco tabPreco = TabelaPrecoDAO.get(cdTabelaPreco, connect);
			int cdEmpresa = tabPreco.getCdEmpresa();
			PreparedStatement pstmt = connect.prepareStatement("SELECT B.vl_preco_medio, B.vl_custo_medio, B.vl_ultimo_custo, " +
					"C.cd_produto_servico, C.nm_produto_servico, " +
					"C.tp_produto_servico, D.cd_grupo, L.vl_preco, " +
					"(SELECT MAX(G.cd_fornecedor) " +
					" FROM alm_documento_entrada G, alm_documento_entrada_item H " +
					" WHERE G.cd_empresa = ? " +
					"   AND (G.tp_entrada = ? OR G.tp_entrada = ?) " +
					"   AND G.cd_empresa = H.cd_empresa " +
					"   AND G.cd_documento_entrada = H.cd_documento_entrada " +
					"   AND NOT G.cd_fornecedor IS NULL " +
					"   AND G.st_documento_entrada = ? " +
					"   AND H.cd_produto_servico = C.cd_produto_servico " +
					"   AND G.dt_documento_entrada = (SELECT MAX(I.dt_documento_entrada) " +
					"								  FROM alm_documento_entrada I, alm_documento_entrada_item J " +
					"								  WHERE I.cd_empresa = ? " +
					"   								AND (I.tp_entrada = ? OR I.tp_entrada = ?) " +
					"   								AND I.cd_empresa = J.cd_empresa " +
					"   								AND I.cd_documento_entrada = J.cd_documento_entrada " +
					"   								AND NOT I.cd_fornecedor IS NULL " +
					"   								AND I.st_documento_entrada = ? " +
					"   								AND J.cd_produto_servico = C.cd_produto_servico)) AS cd_fornecedor " +
					"FROM grl_produto_servico_empresa B, grl_produto_servico C " +
					"LEFT OUTER JOIN alm_produto_grupo D ON (C.cd_produto_servico = D.cd_produto_servico AND " +
					"										 D.cd_empresa = ? AND " +
					"										 D.cd_grupo = (SELECT MAX(E.cd_grupo) " +
					"													   FROM alm_produto_grupo E " +
					"													   WHERE E.cd_produto_servico = D.cd_produto_servico " +
					"														 AND E.cd_empresa = ? " +
					"														 AND ((E.lg_principal = 1 " +
					"														 	   AND EXISTS (SELECT F.cd_grupo " +
					"																	 	   FROM alm_produto_grupo F " +
					"																	 	   WHERE F.cd_produto_servico = E.cd_produto_servico " +
					"																	   		 AND F.cd_empresa = ?)) " +
					"														  	  OR E.lg_principal = 0))) " +
					"LEFT OUTER JOIN adm_produto_servico_preco L ON (L.cd_tabela_preco = ? AND " +
					"												 L.cd_produto_servico = C.cd_produto_servico AND " +
					"												 L.dt_termino_validade IS NULL) " +
					"WHERE B.cd_produto_servico = C.cd_produto_servico " +
					"  AND B.cd_produto_servico = ? " +
					"  AND B.cd_empresa = ?");

			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, DocumentoEntradaServices.ENT_COMPRA);
			pstmt.setInt(3, DocumentoEntradaServices.ENT_CONSIGNACAO);
			pstmt.setInt(4, DocumentoEntradaServices.ST_LIBERADO);
			pstmt.setInt(5, cdEmpresa);
			pstmt.setInt(6, DocumentoEntradaServices.ENT_COMPRA);
			pstmt.setInt(7, DocumentoEntradaServices.ENT_CONSIGNACAO);
			pstmt.setInt(8, DocumentoEntradaServices.ST_LIBERADO);
			pstmt.setInt(9, cdEmpresa);
			pstmt.setInt(10, cdEmpresa);
			pstmt.setInt(11, cdEmpresa);
			pstmt.setInt(12, cdTabelaPreco);
			pstmt.setInt(13, cdProdutoServico);
			pstmt.setInt(14, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();

			int cdGrupo = !rs.next() ? 0 : rs.getInt("cd_grupo");
			int cdFornecedor = !rs.next() ? 0 : rs.getInt("cd_fornecedor");
			pstmt = connect.prepareStatement("SELECT A.cd_regra, A.cd_tabela_preco, A.cd_tabela_preco_base, " +
					"A.cd_produto_servico AS cd_produto_servico_regra, A.cd_fornecedor, A.cd_grupo, A.pr_desconto, A.pr_margem_lucro, " +
					"A.pr_margem_minima, A.pr_margem_maxima, A.lg_incluir_impostos, A.lg_preco_minimo, A.tp_aproximacao, " +
					"A.nr_prioridade, A.tp_valor_base, B.nm_grupo, C.nm_produto_servico AS nm_produto_servico_regra, " +
					"D.nm_tabela_preco AS nm_tabela_preco_base, G.vl_preco AS vl_preco_base, " +
					"E.nm_pessoa AS nm_fornecedor " +
					"FROM adm_tabela_preco_regra A " +
					"LEFT OUTER JOIN alm_grupo B ON (A.cd_grupo = B.cd_grupo) " +
					"LEFT OUTER JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
					"LEFT OUTER JOIN adm_tabela_preco D ON (A.cd_tabela_preco_base = D.cd_tabela_preco) " +
					"LEFT OUTER JOIN grl_pessoa E ON (A.cd_fornecedor = E.cd_pessoa) " +
					"LEFT OUTER JOIN adm_produto_servico_preco G ON (G.dt_termino_validade IS NULL AND " +
					"												 G.cd_tabela_preco = A.cd_tabela_preco_base AND " +
					"												 G.cd_produto_servico = ?) " +
					"WHERE (A.cd_produto_servico IS NULL OR A.cd_produto_servico = ?) " +
					"  AND (A.cd_grupo IS NULL" + (cdGrupo>0 ? " OR A.cd_grupo = ?" : "") + ") " +
					"  AND (A.cd_fornecedor IS NULL" + (cdFornecedor>0 ? " OR A.cd_fornecedor = ?" : "") + ") " +
					"  AND (A.cd_tabela_preco_base IS NULL OR EXISTS (SELECT F.cd_produto_servico " +
					"												  FROM adm_produto_servico_preco F " +
					"												  WHERE F.dt_termino_validade IS NULL " +
					"													AND F.cd_tabela_preco = A.cd_tabela_preco_base " +
					"													AND F.cd_produto_servico = ?)) " +
					"  AND A.cd_tabela_preco = ?");
			int i = 1;
			pstmt.setInt(i++, cdProdutoServico);
			pstmt.setInt(i++, cdProdutoServico);
			if (cdGrupo > 0)
				pstmt.setInt(i++, cdGrupo);
			if (cdFornecedor > 0)
				pstmt.setInt(i++, cdFornecedor);
			pstmt.setInt(i++, cdProdutoServico);
			pstmt.setInt(i++, cdTabelaPreco);
			ResultSetMap rsmRegras = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nr_prioridade");
			rsmRegras.orderBy(fields);
			return rsmRegras;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoServices.getAllRegras: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllFotosOfProduto(int cdProdutoServico, int cdEmpresa) {
		return getAllFotosOfProduto(cdProdutoServico, cdEmpresa, null);
	}

	public static ResultSetMap getAllFotosOfProduto(int cdProdutoServico, int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
																  "FROM ecm_foto_produto_empresa A " +
																  "WHERE A.cd_produto_servico = ? " +
																  "ORDER BY A.nr_ordem");
			pstmt.setInt(1, cdProdutoServico);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioNcmClassificacaoFiscal(int cdEmpresa, int stProdutoEmpresa, int cdGrupo, int cdClassificacaoFiscal, int cdNcm){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
		
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// Carregando Parametros
			PreparedStatement pstmt = connection.prepareStatement("SELECT PE.st_produto_empresa, C.nm_classificacao_fiscal, C.cd_classificacao_fiscal, B.nm_ncm, B.nr_ncm, UN.sg_unidade_medida, COUNT(*) AS qt_produtos " +
																(cdGrupo > 0 ? ", E.cd_grupo, E.nm_grupo" : "") + 
																"   FROM grl_produto_servico A " +
																"   JOIN grl_produto_servico_empresa PE ON (A.cd_produto_servico = PE.cd_produto_servico AND PE.cd_empresa = "+cdEmpresa+")" +
																"	LEFT OUTER JOIN grl_ncm B ON (A.cd_ncm = B.cd_ncm) " +
																"	JOIN grl_unidade_medida UN ON (B.cd_unidade_medida = UN.cd_unidade_medida) " +
																"	LEFT OUTER JOIN adm_classificacao_fiscal C ON (A.cd_classificacao_fiscal = C.cd_classificacao_fiscal) " +
																(cdGrupo > 0 ? "JOIN alm_produto_grupo D ON (A.cd_produto_servico = D.cd_produto_servico AND D.cd_grupo = "+cdGrupo+")" +
																		"		JOIN alm_grupo		   E ON (D.cd_grupo = E.cd_grupo) " : "") +
																"	WHERE 1 = 1 " +
																(stProdutoEmpresa > -1 ? " AND PE.st_produto_empresa = " + stProdutoEmpresa : "") + 
																(cdClassificacaoFiscal > 0 ? " AND A.cd_classificacao_fiscal = " + cdClassificacaoFiscal : "") + 
																(cdNcm > 0 ? " AND A.cd_ncm = " + cdNcm : "") +
																"	GROUP BY C.nm_classificacao_fiscal, C.cd_classificacao_fiscal, nm_ncm, PE.st_produto_empresa, B.nr_ncm, UN.sg_unidade_medida " + (cdGrupo > 0 ? ", E.cd_grupo, E.nm_grupo" : "") + 
																"	ORDER BY C.nm_classificacao_fiscal, nr_ncm");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("NR_NCM", rsm.getString("nr_ncm"));
				rsm.setValueToField("NM_NCM", rsm.getString("nm_ncm"));
				rsm.setValueToField("CD_CLASSIFICACAO_FISCAL", rsm.getInt("cd_classificacao_fiscal"));
				rsm.setValueToField("QT_PRODUTOS", rsm.getInt("qt_produtos"));
				rsm.setValueToField("SG_UNIDADE_MEDIDA", rsm.getString("sg_unidade_medida"));
			}
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			if(cdGrupo > 0){
				Grupo grupo = GrupoDAO.get(cdGrupo);
				param.put("nmGrupo", grupo.getNmGrupo());
			}
			if(stProdutoEmpresa > -1){
				param.put("stProdutoEmpresa", (stProdutoEmpresa == 1 ? "Ativos" : "Inativos"));
				
			}
			if(cdClassificacaoFiscal > 0){
				ClassificacaoFiscal classificacaoFiscal = ClassificacaoFiscalDAO.get(cdClassificacaoFiscal);
				param.put("nmClassificacaoFiscal", classificacaoFiscal.getNmClassificacaoFiscal());
			}
			if(cdNcm > 0){
				Ncm ncm = NcmDAO.get(cdNcm);
				param.put("nmNcm", ncm.getNmNcm());
			}
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result gerarRelatorioListaPreco(ArrayList<ItemComparator> criterios, int cdEmpresa, int Custo, int CustoMax, int cdLocalArmazenamento, boolean qtEstoqueZero){
		boolean isConnectionNull = true;
		Connection connection = null;		
		Result result = new Result(1);
		
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			ResultSetMap rsm = Search.find("SELECT A.*, B.*, C.*, E.*, "
					+ " F.cd_grupo, F.nm_grupo, F2.cd_grupo AS cd_grupo_superior, F2.nm_grupo AS NM_GRUPO_SUPERIOR, "
					+ " D.*, G.*  "
					+ " FROM grl_produto_servico A "
					+ " JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = '" + cdEmpresa + "') "
					+ " JOIN adm_tabela_preco G ON ( B.cd_empresa = G.cd_empresa "
													+ "AND G.dt_final_validade is null "
													+ "AND G.tp_aplicacao_regras = 1 "
													+ "AND G.lg_ativo = 1 "
													+ ") "
					+ " JOIN adm_produto_servico_preco D ON ( A.cd_produto_servico = D.cd_produto_servico"
														+ " AND D.cd_tabela_preco = G.cd_tabela_preco "
														+ " AND D.dt_termino_validade is null "
														+ " ) "
					+ " LEFT OUTER JOIN alm_produto_grupo E ON (E.cd_produto_servico = B.cd_produto_servico AND E.cd_empresa = B.cd_empresa AND lg_principal = 1) "
					+ " JOIN grl_unidade_medida C ON (C.cd_unidade_medida = B.cd_unidade_medida) "
					+ " LEFT OUTER JOIN alm_grupo F ON (F.cd_grupo = E.cd_grupo) "
					+ " LEFT OUTER JOIN alm_grupo F2 ON (F.cd_grupo_superior = F2.cd_grupo) WHERE 1=1 ",
			     	  " ORDER BY G.CD_TABELA_PRECO, F.CD_GRUPO_SUPERIOR, F.NM_GRUPO ", criterios, connection, false );	
			
			/** Busca Apenas os preços da tabela à vista e à prazo
			ResultSetMap rsm = Search.find("SELECT A.*, B.*, C.*, E.*, F.cd_grupo, F.nm_grupo, F2.cd_grupo AS cd_grupo_superior, F2.nm_grupo AS NM_GRUPO_SUPERIOR, "
					+ " (SELECT vl_preco"
					+ "    FROM adm_tabela_preco G, adm_produto_servico_preco D "
					+ "   WHERE G.cd_empresa = '" + cdEmpresa + "' "
					+ "     AND D.dt_termino_validade is null "
					+ "     AND G.dt_final_validade is null "
					+ "     AND D.cd_produto_servico = A.cd_produto_servico "
					+ "     AND D.cd_tabela_preco = G.cd_tabela_preco "
					+ "     AND G.tp_aplicacao_regras = 1 "
					+ "     AND G.lg_ativo = 1 "
					+ "     AND D.cd_tabela_preco in ( 1, 2 )) as vl_preco_vista, "
					+ " (SELECT vl_preco "
					+ "    FROM adm_tabela_preco G, adm_produto_servico_preco D "
					+ "   WHERE G.cd_empresa = '" + cdEmpresa + "' "
					+ "     AND D.dt_termino_validade is null "
					+ "     AND G.dt_final_validade is null "
					+ "     AND D.cd_produto_servico = A.cd_produto_servico "
					+ "     AND D.cd_tabela_preco = G.cd_tabela_preco "
					+ "     AND G.tp_aplicacao_regras = 0 "
					+ "     AND G.lg_ativo = 1 "
					+ "     AND D.cd_tabela_preco in ( 1, 2 )) as vl_preco_prazo "					
					+ " FROM grl_produto_servico A "
					+ " JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = '" + cdEmpresa + "') "
					+ " LEFT OUTER JOIN alm_produto_grupo E ON (E.cd_produto_servico = B.cd_produto_servico AND E.cd_empresa = B.cd_empresa AND lg_principal = 1) "
					+ " JOIN grl_unidade_medida C ON (C.cd_unidade_medida = B.cd_unidade_medida) "
					+ " LEFT OUTER JOIN alm_grupo F ON (F.cd_grupo = E.cd_grupo) "
					+ " LEFT OUTER JOIN alm_grupo F2 ON (F.cd_grupo_superior = F2.cd_grupo) WHERE 1=1 ",
			     	  " ORDER BY F2.NM_GRUPO, F.NM_GRUPO, A.NM_PRODUTO_SERVICO", criterios, null, connection, false, true, false
			);					
			
			 */
			// Estoque Entrada			
			PreparedStatement pstmt = connection.prepareStatement(
					" SELECT SUM(A.qt_entrada) AS QT_ENTRADA " +
					"  FROM alm_entrada_local_item A, alm_documento_entrada B, alm_documento_entrada_item C " +
				    " WHERE A.cd_empresa           = " +cdEmpresa+
				    "   AND A.cd_documento_entrada = C.cd_documento_entrada " +
				    "   AND A.cd_produto_servico   = C.cd_produto_servico " +
				    "   AND A.cd_empresa           = C.cd_empresa " +
				    "   AND A.cd_documento_entrada = B.cd_documento_entrada " +
				    "   AND B.tp_entrada NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
				    "   AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
				    "   AND C.qt_entrada           > 0 " +
				    "   AND A.cd_produto_servico   = ? " +
				    "   AND A.cd_local_armazenamento = " + cdLocalArmazenamento);
			
			while(rsm.next()){
				pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
				ResultSetMap rsEntrada = new ResultSetMap(pstmt.executeQuery());
				if(rsEntrada.next()){
					rsm.setValueToField("QT_ESTOQUE", rsEntrada.getFloat("QT_ENTRADA"));
				}
			}
			
			rsm.beforeFirst();				
			
			// Estoque saida
			pstmt = connection.prepareStatement(
					"SELECT SUM(A.qt_saida) AS qt_saida " +
					"  FROM alm_saida_local_item A, alm_documento_saida_item C, alm_documento_saida B " +
				    " WHERE A.cd_empresa         = " +cdEmpresa+
				    "   AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
				    "   AND A.cd_documento_saida = C.cd_documento_saida " +
				    "   AND A.cd_produto_servico = C.cd_produto_servico " +
				    "   AND A.cd_empresa         = C.cd_empresa " +
				    "   AND A.cd_item            = C.cd_item " +
				    "   AND A.cd_documento_saida = B.cd_documento_saida " +
				    "   AND C.qt_saida > 0 " +
				    "   AND A.cd_produto_servico   = ? " +
				    "   AND A.cd_local_armazenamento = "+cdLocalArmazenamento +
				    " GROUP BY A.cd_produto_servico ");
			
			while(rsm.next()){
				pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
				ResultSetMap rsSaida = new ResultSetMap(pstmt.executeQuery());
				if(rsSaida.next()){
					rsm.setValueToField("QT_SAIDA", rsm.getFloat("QT_ENTRADA") - rsSaida.getFloat("QT_SAIDA"));
				}
			}
			
			rsm.beforeFirst();						
						
			while(rsm.next()){
				if(rsm.getInt("cd_grupo_superior") == 0){
					rsm.setValueToField("cd_grupo_superior", rsm.getInt("cd_grupo"));
					rsm.setValueToField("nm_grupo_superior", rsm.getString("nm_grupo"));
					rsm.setValueToField("cd_grupo", 0);
					rsm.setValueToField("nm_grupo", "");
				}
			}	
			
			rsm.beforeFirst();
			
			while(rsm.next()){
				
				if(!qtEstoqueZero && rsm.getFloat("QT_ENTRADA") < 1){
					rsm.deleteRow();
				}
			}
			
			rsm.beforeFirst();
//			@TODO Incluir orderby com múltiplos critérios no ResultSetMap
//			ArrayList<String> fields = new ArrayList<String>();
//			fields.add("NM_GRUPO_SUPERIOR");
//			fields.add("NM_GRUPO");
//			fields.add("NM_PRODUTO_SERVICO");
//			
//			rsm.orderBy(fields);
//			
			result.addObject("rsm", rsm);
						
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			params.put("SHOW_CUSTO", Custo);
			params.put("SHOW_CUSTO_MAX", CustoMax);
			
			result.addObject("params", params);
			
			if (isConnectionNull)
				connection.commit();
			
						
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}	
	
	
	public static Result gerarRelatorioAfericaoCombustivel(ArrayList<ItemComparator> criterios, int cdEmpresa){
		boolean isConnectionNull = true;
		Connection connection = null;			
		
		Result result = new Result(1);
		
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
						
			ResultSetMap rsm = Search.find("SELECT A.*, B.*, C.*, E.*, F.cd_grupo, F.nm_grupo, F2.cd_grupo AS cd_grupo_superior, F2.nm_grupo AS NM_GRUPO_SUPERIOR, "
					+ " (SELECT vl_preco"
					+ "    FROM adm_tabela_preco G, adm_produto_servico_preco D "
					+ "   WHERE G.cd_empresa = '" + cdEmpresa + "' "
					+ "     AND D.dt_termino_validade is null "
					+ "     AND G.dt_final_validade is null "
					+ "     AND D.cd_produto_servico = A.cd_produto_servico "
					+ "     AND D.cd_tabela_preco = G.cd_tabela_preco "
					+ "     AND G.tp_aplicacao_regras = 1 "
					+ "     AND G.lg_ativo = 1 "
					+ "     AND D.cd_tabela_preco in ( 1, 2 )) as vl_preco_vista, "
					+ " (SELECT vl_preco "
					+ "    FROM adm_tabela_preco G, adm_produto_servico_preco D "
					+ "   WHERE G.cd_empresa = '" + cdEmpresa + "' "
					+ "     AND D.dt_termino_validade is null "
					+ "     AND G.dt_final_validade is null "
					+ "     AND D.cd_produto_servico = A.cd_produto_servico "
					+ "     AND D.cd_tabela_preco = G.cd_tabela_preco "
					+ "     AND G.tp_aplicacao_regras = 0 "
					+ "     AND G.lg_ativo = 1 "
					+ "     AND D.cd_tabela_preco in ( 1, 2 )) as vl_preco_prazo "					
					+ " FROM grl_produto_servico A "
					+ " JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = '" + cdEmpresa + "') "
					+ " LEFT OUTER JOIN alm_produto_grupo E ON (E.cd_produto_servico = B.cd_produto_servico AND E.cd_empresa = B.cd_empresa AND lg_principal = 1) "
					+ " JOIN grl_unidade_medida C ON (C.cd_unidade_medida = B.cd_unidade_medida) "
					+ " LEFT OUTER JOIN alm_grupo F ON (F.cd_grupo = E.cd_grupo) "
					+ " LEFT OUTER JOIN alm_grupo F2 ON (F.cd_grupo_superior = F2.cd_grupo) WHERE 1=1 ",
			     	  " ORDER BY F2.NM_GRUPO, F.NM_GRUPO, A.NM_PRODUTO_SERVICO", criterios, null, connection, false, true, false
			);					
						
			@SuppressWarnings("unused")
			ArrayList<String> fields = new ArrayList<String>();
			
			result.addObject("rsm", rsm);
						
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			result.addObject("params", params);
			
			if (isConnectionNull)
				connection.commit();
			
						
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}	
	
	public static Result getAllProdutosCae(int cdLocalArmazenamento, int cdEmpresa) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//		criterios.add(new ItemComparator("B.CD_LOCAL_ARMAZENAMENTO", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("B.CD_EMPRESA", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("B.ST_PRODUTO_EMPRESA", "1", ItemComparator.EQUAL, Types.INTEGER));
		System.out.println(criterios);
		return findRelatorioEdf(criterios, null) ;
	}
	
	public static Result findRelatorioEdf(ArrayList<ItemComparator> criterios) {
		return findRelatorioEdf(criterios, null) ;
	}

	public static Result findRelatorioEdf(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			int tpEstoque     = -1;
			int cdLocalArmazenamento = 0;
			int cdEmpresa = 0;
			GregorianCalendar dtMovimento = Util.getDataAtual();
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpEstoque")) 
					tpEstoque = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("B.CD_LOCAL_ARMAZENAMENTO")){ 
					cdLocalArmazenamento = Integer.parseInt(criterios.get(i).getValue());
					crt.add(criterios.get(i));
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("B.CD_EMPRESA")){ 
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
					crt.add(criterios.get(i));
				}
				else if( criterios.get(i).getColumn().equalsIgnoreCase("dtMovimento")) {
					dtMovimento = Util.convStringToCalendar(criterios.get(i).getValue());
					dtMovimento.set(Calendar.HOUR_OF_DAY, 23);
					dtMovimento.set(Calendar.MINUTE, 59);
					dtMovimento.set(Calendar.SECOND, 59);
					dtMovimento.set(Calendar.MILLISECOND, 0);
				}
				else
					crt.add(criterios.get(i));
			}
			
			
			ResultSetMap rsm = Search.find(
					"SELECT A.cd_produto_servico, A.nm_produto_servico, A.id_produto_servico, B.qt_ideal, B.qt_minima, B.qt_maxima, " + 
					"D.sg_unidade_medida, D.cd_unidade_medida " +
					"FROM grl_produto_servico A " +
					"LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_fabricante = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_unidade_medida D ON (B.cd_unidade_medida = D.cd_unidade_medida) ", 
					" ORDER BY A.cd_produto_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()){
				criterios = new ArrayList<ItemComparator>();
				if(cdLocalArmazenamento > 0)
					criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtMovimento, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
				Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connect);
				if(resultado.getObjects().get("QT_ESTOQUE") != null)
					rsm.setValueToField("QT_ESTOQUE", resultado.getObjects().get("QT_ESTOQUE"));
				else
					rsm.setValueToField("QT_ESTOQUE", 0);
				if(resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO") != null)
					rsm.setValueToField("QT_ESTOQUE_CONSIGNADO", resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO"));
				else
					rsm.setValueToField("QT_ESTOQUE_CONSIGNADO", 0);
				
				rsm.setValueToField("NM_GRUPO_PRINCIPAL", "NENHUM");
				rsm.setValueToField("CD_GRUPO_PRINCIPAL", 0);
			}
			
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao pesquisar");
			result.addObject("RSM", rsm);
			
			return result;
		}
		catch(Exception e){e.printStackTrace();return null;}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findMaterialOS(ArrayList<ItemComparator> criterios) {
		return findMaterialOS(criterios, null);
	}
	
	public static ResultSetMap findMaterialOS(ArrayList<ItemComparator> criterios, Connection connect) {
		Result result = findRelatorioEdf(criterios, connect);
		
		return (ResultSetMap)result.getObjects().get("RSM");
	}
	
	public static Result findPorFornecedor(ArrayList<ItemComparator> criterios) {
		return findPorFornecedor(criterios, null) ;
	}

	public static Result findPorFornecedor(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			int tpEstoque     = -1;
			int cdLocalArmazenamento = 0;
			int cdEmpresa = 0;
			GregorianCalendar dtMovimento = Util.getDataAtual();
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("tpEstoque")) 
					tpEstoque = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("B.CD_LOCAL_ARMAZENAMENTO")){ 
					cdLocalArmazenamento = Integer.parseInt(criterios.get(i).getValue());
					crt.add(criterios.get(i));
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("B.CD_EMPRESA")){ 
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
					crt.add(criterios.get(i));
				}
				else if( criterios.get(i).getColumn().equalsIgnoreCase("dtMovimento")) {
					dtMovimento = Util.convStringToCalendar(criterios.get(i).getValue());
					dtMovimento.set(Calendar.HOUR_OF_DAY, 23);
					dtMovimento.set(Calendar.MINUTE, 59);
					dtMovimento.set(Calendar.SECOND, 59);
					dtMovimento.set(Calendar.MILLISECOND, 0);
				}
				else
					crt.add(criterios.get(i));
			}
			
			
			ResultSetMap rsm = Search.find("SELECT A.cd_produto_servico, A.nm_produto_servico, A.id_produto_servico, E.nm_pessoa AS nm_fornecedor, C.cd_fornecedor " +
					"FROM grl_produto_servico A " +
					"LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"LEFT OUTER JOIN adm_produto_fornecedor C ON (A.cd_produto_servico = C.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_pessoa E ON (C.cd_fornecedor = E.cd_pessoa) " +
					"LEFT OUTER JOIN grl_unidade_medida D ON (B.cd_unidade_medida = D.cd_unidade_medida) ", 
					" ORDER BY E.nm_pessoa, A.nm_produto_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()){
				criterios = new ArrayList<ItemComparator>();
				if(cdLocalArmazenamento > 0)
					criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtMovimento, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
				Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connect);
				if(resultado.getObjects().get("QT_ESTOQUE") != null)
					rsm.setValueToField("QT_ESTOQUE", resultado.getObjects().get("QT_ESTOQUE"));
				else
					rsm.setValueToField("QT_ESTOQUE", 0);
				
				rsm.setValueToField("NM_GRUPO_PRINCIPAL", "NENHUM");
				rsm.setValueToField("CD_GRUPO_PRINCIPAL", 0);
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao pesquisar");
			result.addObject("RSM", rsm);
			
			return result;
		}
		catch(Exception e){e.printStackTrace();return null;}
		finally{
			Conexao.desconectar(connect);
		}
	}
}