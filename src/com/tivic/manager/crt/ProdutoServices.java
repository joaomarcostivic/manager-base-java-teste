package com.tivic.manager.crt;

import java.sql.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class ProdutoServices {
	public static final int PERCENTUAL_EMPRESTADO = 0;
	public static final int PERCENTUAL_DIFERENCA = 1;
	public static final int PERCENTUAL_SPREAD = 1;
	public static final int FIXO_POR_OPERACAO = 2;
	public static final int PERCENTUAL_TAC = 3;
	public static final int PERCENTUAL_OUTRA_COMISSAO = 4;
	public static final int TAC_MENOS_VALOR = 5;
	public static final int PERC_TAXA_PLANO = 6;

	public static final int COMISSAO_EMPRESA   = 0;
	public static final int COMISSAO_ATENDENTE = 1;
	public static final int COMISSAO_AGENTE    = 2;
	public static final int COMISSAO_SUBAGENTE = 3;
	public static final int COMISSAO_GERENTE   = 4;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_PRODUTO A "+
		                   "LEFT OUTER JOIN SCE_ORGAO B ON (A.CD_ORGAO = B.CD_ORGAO) ", "ORDER BY nm_produto", criterios, Conexao.conectar(), true, false);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, B.NM_PESSOA AS NM_INSTITUICAO_FINANCEIRA, C.NM_ORGAO "+
		                   "FROM SCE_PRODUTO A "+
		                   "JOIN GRL_PESSOA B ON (A.CD_INSTITUICAO_FINANCEIRA = B.CD_PESSOA) "+
		                   "LEFT OUTER JOIN SCE_ORGAO C ON (A.CD_ORGAO = C.CD_ORGAO) ", "ORDER BY nm_produto", criterios, Conexao.conectar(), true, false);
	}

	public static int insertProdutoPlano(int cdProduto, int cdPlano, int qtParcelas, int qtParcelasSup,
				float vlTac, float vlTacSup, float prJuros, float prCoeficiente)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Verificando plano por produto
			pstmt = connect.prepareStatement("SELECT * FROM SCE_PRODUTO_PLANO "+
			                                 "WHERE cd_produto = ? AND qt_parcelas = ? AND cd_plano <> ?");
			pstmt.setInt(1, cdProduto);
			pstmt.setInt(2, qtParcelas);
			pstmt.setInt(3, cdPlano);
			if(pstmt.executeQuery().next())
				return -10;
			if(cdPlano==0)	{
				cdPlano = Conexao.getSequenceCode("SCE_PRODUTO_PLANO");
				pstmt = connect.prepareStatement("INSERT INTO SCE_PRODUTO_PLANO "+
			                                     "(CD_PRODUTO, CD_PLANO, QT_PARCELAS, QT_PARCELAS_SUP, VL_TAC, VL_TAC_SUP, PR_JUROS, PR_COEFICIENTE)"+
			                                     "VALUES (?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, cdProduto);
				pstmt.setInt(2, cdPlano);
				pstmt.setInt(3, qtParcelas);
				pstmt.setInt(4, qtParcelasSup);
				pstmt.setFloat(5, vlTac);
				pstmt.setFloat(6, vlTacSup);
				pstmt.setFloat(7, prJuros);
				pstmt.setFloat(8, prCoeficiente);
			}
			else	{
				pstmt = connect.prepareStatement("UPDATE SCE_PRODUTO_PLANO SET "+
			                                     "QT_PARCELAS=?, VL_TAC=?, VL_TAC_SUP=?, PR_JUROS=?, PR_COEFICIENTE=?, QT_PARCELAS_SUP=? "+
			                                     "WHERE CD_PRODUTO=? AND CD_PLANO=?");
				pstmt.setInt(1, qtParcelas);
				pstmt.setFloat(2, vlTac);
				pstmt.setFloat(3, vlTacSup);
				pstmt.setFloat(4, prJuros);
				pstmt.setFloat(5, prCoeficiente);
				pstmt.setInt(6, qtParcelasSup);
				pstmt.setInt(7, cdProduto);
				pstmt.setInt(8, cdPlano);
			}
			pstmt.executeUpdate();
			return cdPlano;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int insertProdutoTabela(int cdProduto, int cdTabelaComissao)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("INSERT INTO SCE_PRODUTO_TABELA (CD_PRODUTO, CD_TABELA_COMISSAO) VALUES (?,?)");
			pstmt.setInt(1, cdProduto);
			pstmt.setInt(2, cdTabelaComissao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteProdutoPlano(int cdProduto, int cdPlano) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_PRODUTO_PLANO "+
		                                     "WHERE CD_PRODUTO  = ? "+
		                                     "  AND CD_PLANO = ?");
			pstmt.setInt(1, cdProduto);
			pstmt.setInt(2, cdPlano);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.deleteProdutoPlano: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.deleteProdutoPlano: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static int deleteProdutoTabela(int cdProduto, int cdTabelaComissao)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_PRODUTO_TABELA WHERE CD_PRODUTO = ? AND CD_TABELA_COMISSAO = ?");
			pstmt.setInt(1, cdProduto);
			pstmt.setInt(2, cdTabelaComissao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.insertProdutoTabela: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.insertProdutoTabela: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findProdutoPlano(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_PRODUTO A "+
		                   "LEFT OUTER JOIN SCE_PRODUTO_PLANO B ON (A.CD_PRODUTO = B.CD_PRODUTO) "+
		                   "JOIN GRL_PESSOA C ON (A.CD_INSTITUICAO_FINANCEIRA = C.CD_PESSOA) ",
		                   "ORDER BY NM_PESSOA, NM_PRODUTO, QT_PARCELAS", criterios, Conexao.conectar(), true, false);
	}

	public static int insertComissao(int cdTipoComissao, int tpCalculo, int tpComissao, float vlComissao,
			int cdProduto, int cdPlano, int cdOperacao, int cdComissaoOrigem, int nrDiasVencimento,
			int cdTabelaComissao, float vlDeducao)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			// Verifica duplicidade
			/*pstmt = connect.prepareStatement("SELECT * FROM SCE_PRODUTO_COMISSAO "+
			                                 "WHERE CD_PRODUTO = ? "+
			                                 "  AND CD_PLANO   = ? "+
			                                 "  AND CD_TIPO_COMISSAO <> ? "+
			                                 "  AND CD_OPERACAO "+(cdOperacao==0?"IS NULL":" = ? "));

			pstmt.setInt(1,cdProduto);
			pstmt.setInt(2,cdPlano);
			pstmt.setInt(3,cdTipoComissao);
			if(cdOperacao>0)
				pstmt.setInt(4,cdOperacao);
			if(pstmt.executeQuery().next())
				return -10;*/
			//
			if(cdTipoComissao<=0)	{
				cdTipoComissao = Conexao.getSequenceCode("SCE_PRODUTO_COMISSAO");
				pstmt = connect.prepareStatement("INSERT INTO SCE_PRODUTO_COMISSAO (CD_TIPO_COMISSAO,"+
				                                  "CD_COMISSAO_ORIGEM,"+
				                                  "TP_COMISSAO,"+
				                                  "VL_COMISSAO,"+
				                                  "CD_OPERACAO,"+
				                                  "CD_PRODUTO,"+
				                                  "CD_PLANO,"+
				                                  "TP_CALCULO, "+
				                                  "NR_DIAS_VENCIMENTO,"+
				                                  "CD_TABELA_COMISSAO, "+
				                                  "VL_DEDUCAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setInt(1, cdTipoComissao);
				if(cdComissaoOrigem==0)
					pstmt.setNull(2, Types.INTEGER);
				else
					pstmt.setInt(2,cdComissaoOrigem);
				pstmt.setInt(3,tpComissao);
				pstmt.setFloat(4,vlComissao);
				if(cdOperacao==0)
					pstmt.setNull(5, Types.INTEGER);
				else
					pstmt.setInt(5,cdOperacao);
				if(cdProduto==0)
					pstmt.setNull(6, Types.INTEGER);
				else
					pstmt.setInt(6,cdProduto);
				if(cdPlano==0)
					pstmt.setNull(7, Types.INTEGER);
				else
					pstmt.setInt(7,cdPlano);
				pstmt.setInt(8,tpCalculo);
				pstmt.setInt(9,nrDiasVencimento);
				if(cdTabelaComissao==0)
					pstmt.setNull(10, Types.INTEGER);
				else
					pstmt.setInt(10,cdTabelaComissao);
				pstmt.setFloat(11, vlDeducao);
			}
			else	{
				pstmt = connect.prepareStatement("UPDATE SCE_PRODUTO_COMISSAO SET CD_COMISSAO_ORIGEM=?,"+
				                                  "TP_COMISSAO=?,"+
				                                  "VL_COMISSAO=?,"+
				                                  "CD_OPERACAO=?,"+
				                                  "CD_PRODUTO=?,"+
				                                  "CD_PLANO=?, "+
				                                  "TP_CALCULO=?, "+
				                                  "NR_DIAS_VENCIMENTO=?, "+
				                                  "CD_TABELA_COMISSAO=?, "+
				                                  "VL_DEDUCAO =? WHERE CD_TIPO_COMISSAO=?");
				if(cdComissaoOrigem==0)
					pstmt.setNull(1, Types.INTEGER);
				else
					pstmt.setInt(1,cdComissaoOrigem);
				pstmt.setInt(2,tpComissao);
				pstmt.setFloat(3,vlComissao);
				if(cdOperacao==0)
					pstmt.setNull(4, Types.INTEGER);
				else
					pstmt.setInt(4,cdOperacao);
				if(cdProduto==0)
					pstmt.setNull(5, Types.INTEGER);
				else
					pstmt.setInt(5,cdProduto);
				if(cdPlano==0)
					pstmt.setNull(6, Types.INTEGER);
				else
					pstmt.setInt(6,cdPlano);
				pstmt.setInt(7,tpCalculo);
				pstmt.setInt(8,nrDiasVencimento);
				if(cdTabelaComissao==0)
					pstmt.setNull(9, Types.INTEGER);
				else
					pstmt.setInt(9, cdTabelaComissao);
				pstmt.setFloat(10, vlDeducao);
				pstmt.setInt(11,cdTipoComissao);
			}
			pstmt.executeUpdate();
			return cdTipoComissao;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.insertComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int deleteComissao(int cdTipoComissao){
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_PRODUTO_COMISSAO WHERE CD_TIPO_COMISSAO=?");
			pstmt.setInt(1, cdTipoComissao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.deleteComissao: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.deleteComissao: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findProdutoComissao(int cdProduto, int cdPlano, int cdTabelaComissao, int cdOperacao) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_produto", String.valueOf(cdProduto), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("A.cd_plano", String.valueOf(cdPlano), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		if(cdTabelaComissao>0)
			criterios.add(new ItemComparator("A.cd_tabela_comissao", String.valueOf(cdTabelaComissao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		else
			criterios.add(new ItemComparator("A.cd_tabela_comissao", "0", ItemComparator.ISNULL, java.sql.Types.VARCHAR));
		if(cdOperacao>0)
			criterios.add(new ItemComparator("A.cd_operacao", String.valueOf(cdOperacao), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		else
			criterios.add(new ItemComparator("A.cd_operacao", "0", ItemComparator.ISNULL, java.sql.Types.VARCHAR));
		return findProdutoComissao(criterios);
	}

	public static ResultSetMap findProdutoComissao(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_PRODUTO_COMISSAO A "+
		                   "JOIN SCE_PRODUTO_PLANO B ON (A.CD_PRODUTO = B.CD_PRODUTO "+
		                   "                         AND A.CD_PLANO   = B.CD_PLANO) "+
		                   "JOIN SCE_PRODUTO D ON (A.CD_PRODUTO = D.CD_PRODUTO) "+
		                   "LEFT OUTER JOIN SCE_TIPO_OPERACAO C ON (A.CD_OPERACAO = C.CD_OPERACAO) "+
		                   "LEFT OUTER JOIN SCE_TABELA_COMISSAO E ON (A.CD_TABELA_COMISSAO = E.CD_TABELA_COMISSAO) ",
		                   "ORDER BY NM_PRODUTO, QT_PARCELAS, NM_TABELA_COMISSAO, TP_COMISSAO, NM_OPERACAO",criterios, Conexao.conectar(), true);
	}
	public static boolean hasConfigComissao(int cdProduto, int cdPlano){
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sce_produto_comissao "+
											 "WHERE cd_produto = ? "+
											 "  AND cd_plano   = ? ");
			pstmt.setInt(1, cdProduto);
			pstmt.setInt(2, cdPlano);
			return pstmt.executeQuery().next();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServices.hasConfigComissao: " +  e);
			return false;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTabelaOfProduto(int cdProduto)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tabela_comissao "+
											 "FROM sce_produto_tabela A, sce_tabela_comissao B "+
					                         "WHERE A.cd_tabela_comissao = B.cd_tabela_comissao "+
											 "  AND A.cd_produto = ? " +
											 "ORDER BY B.nm_tabela_comissao");
			pstmt.setInt(1, cdProduto);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoDAO.getAll: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
}