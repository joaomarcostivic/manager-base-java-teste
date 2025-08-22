package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class TabelaComissaoServices {

	public static int insertTabelaComissaoUso(int cdTabelaUso, int cdTabelaComissao, int cdEmpresa,
			int cdPessoa, int cdProduto, int cdPlano, int cdTipoComissao, int cdInstituicaoFinanceira)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			if(cdTabelaUso<=0){
				cdTabelaUso = Conexao.getSequenceCode("SCE_TABELA_COMISSAO_USO", connect);
				pstmt = connect.prepareStatement("INSERT INTO sce_tabela_comissao_uso "+
			                                     "(cd_tabela_comissao,cd_empresa,cd_pessoa,cd_produto,"+
			                                     "cd_plano,cd_tipo_comissao,cd_instituicao_financeira,cd_tabela_uso) "+
			                                     "VALUES (?,?,?,?,?,?,?,?)");
			}
			else	{
				pstmt = connect.prepareStatement(
						"UPDATE sce_tabela_comissao_uso "+
                        "SET cd_tabela_comissao=?, cd_empresa=?, cd_pessoa=?, cd_produto=?, "+
                        "    cd_plano=?, cd_tipo_comissao=?, cd_instituicao_financeira=? "+
                        "WHERE cd_tabela_uso = ?");
			}
			if(cdTabelaComissao>0)
				pstmt.setInt(1, cdTabelaComissao);
			else
				pstmt.setNull(1, java.sql.Types.INTEGER);
			if(cdEmpresa>0)
				pstmt.setInt(2, cdEmpresa);
			else
				pstmt.setNull(2, java.sql.Types.INTEGER);
			if(cdPessoa>0)
				pstmt.setInt(3, cdPessoa);
			else
				pstmt.setNull(3, java.sql.Types.INTEGER);
			if(cdProduto>0)
				pstmt.setInt(4, cdProduto);
			else
				pstmt.setNull(4, java.sql.Types.INTEGER);
			if(cdPlano>0)
				pstmt.setInt(5, cdPlano);
			else
				pstmt.setNull(5, java.sql.Types.INTEGER);
			if(cdTipoComissao>0)
				pstmt.setInt(6, cdTipoComissao);
			else
				pstmt.setNull(6, java.sql.Types.INTEGER);
			if(cdInstituicaoFinanceira>0)
				pstmt.setInt(7, cdInstituicaoFinanceira);
			else
				pstmt.setNull(7, java.sql.Types.INTEGER);
			pstmt.setInt(8, cdTabelaUso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoServices.insertTabelaComissaoUso: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoServices.insertTabelaComissaoUso: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	public static int deleteTabelaComissaoUso(int cdTabelaUso) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM sce_tabela_comissao_uso "+
		                                     "WHERE cd_tabela_uso = ?");
			pstmt.setInt(1, cdTabelaUso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoServices.deleteTabelaComissaoUso: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoServices.deleteTabelaComissaoUso: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findTabelaComissaoUso(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, F.nm_tabela_comissao, "+
						   "       B.nm_produto, B.cd_instituicao_financeira as cd_parceiro, "+
						   "       C.qt_parcelas, D.cd_comissao_origem, D.tp_calculo, D.vl_comissao, "+
						   "       D.cd_operacao, D.nr_dias_vencimento, D.tp_comissao, D.vl_deducao,  "+
						   "       E.nm_pessoa AS nm_parceiro "+
				           "FROM sce_tabela_comissao_uso A "+
						   "LEFT OUTER JOIN sce_produto B ON (A.cd_produto = B.cd_produto)"+
						   "LEFT OUTER JOIN sce_produto_plano C ON (A.cd_produto = C.cd_produto "+
						   "                                    AND A.cd_plano   = C.cd_plano) "+
				           "LEFT OUTER JOIN sce_produto_comissao D ON (A.cd_tipo_comissao = D.cd_tipo_comissao)"+
				           "LEFT OUTER JOIN grl_pessoa E ON (B.cd_instituicao_financeira = E.cd_pessoa " +
				           "                             OR  A.cd_instituicao_financeira = E.cd_pessoa ) "+
				           "LEFT OUTER JOIN sce_tabela_comissao F ON (A.cd_tabela_comissao = F.cd_tabela_comissao)",
		                   criterios, Conexao.conectar());
	}

	public static int getTabelaComissao(int cdEmpresa, int cdPessoa, int cdParceiro, int cdProduto, int cdPlano, int cdTabelaComissaToOut)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int cdProdutoOld = cdProduto, cdPlanoOld = cdPlano, cdParceiroOld=cdParceiro;
			while(true)	{
				pstmt = connect.prepareStatement(
						   "SELECT cd_tabela_comissao FROM sce_tabela_comissao_uso "+
	                       "WHERE cd_empresa = "+cdEmpresa+
	                       "  AND cd_pessoa "+(cdPessoa>0 ? " = "+cdPessoa:" IS NULL ")+
	                       "  AND cd_instituicao_financeira "+(cdParceiro>0 && cdProduto==0 && cdPlano==0 ? " = "+cdParceiro:" IS NULL ")+
	                       "  AND cd_produto "+(cdProduto>0 ? " = "+cdProduto:" IS NULL ")+
	                       "  AND cd_plano "+(cdPlano>0 ? " = "+cdPlano:" IS NULL ")+
	                       (cdTabelaComissaToOut>0? " AND cd_tabela_comissao <> "+cdTabelaComissaToOut:""));
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())
					return rs.getInt(1);
				if(cdPlano==0 && cdProduto==0 && cdPessoa==0 && cdParceiro==0)
					return -1;
				if(cdPlano>0)
					cdPlano = 0;
				else if(cdProduto>0)
					cdProduto = 0;
				else if(cdParceiro>0)
					cdParceiro = 0;
				else if(cdPessoa>0)	{
					cdPessoa 	= 0;
					cdProduto 	= cdProdutoOld;
					cdPlano 	= cdPlanoOld;
					cdParceiro 	= cdParceiroOld;
				}
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoServices.getTabelaComissao: " + sqlExpt);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoServices.getTabelaComissao: " + e);
		}
		finally {
			Conexao.desconectar(connect);
		}
		return -1;
	}
}