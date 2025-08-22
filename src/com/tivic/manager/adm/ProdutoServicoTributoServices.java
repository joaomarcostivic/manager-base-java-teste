package com.tivic.manager.adm;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProdutoServicoTributoServices {

	public static Result insert(ProdutoServicoTributo objeto, int cdRegiao)	{
		return insert(objeto, cdRegiao, null);
	}

	public static Result insert(ProdutoServicoTributo objeto, int cdRegiao, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if(cdRegiao > 0)	{
				Regiao regiao = RegiaoDAO.get(cdRegiao, connect);
				Tributo tributo = TributoDAO.get(objeto.getCdTributo(), connect);
				/*
				 * Verificando compatibilidade da região e do tributo
				 */
				if((regiao.getTpRegiao()==RegiaoServices._PAISES && tributo.getTpEsferaAplicacao()!=TributoServices._ESFERA_FEDERAL) ||
				   (regiao.getTpRegiao()==RegiaoServices._ESTADOS && tributo.getTpEsferaAplicacao()!=TributoServices._ESFERA_ESTADUAL) ||
				   (regiao.getTpRegiao()==RegiaoServices._CIDADES && tributo.getTpEsferaAplicacao()!=TributoServices._ESFERA_MUNICIPAL))
				{
					Exception e = new Exception("A região informada deve ser igual a esfera de aplicação do tributo!");
					com.tivic.manager.util.Util.registerLog(e);
					return new Result(-10, e.getMessage(), e);
				}
				/*
				 * Incluindo para países
				 */
				Result retorno = new Result(0, "Não foi possível incluir a regra!");
				if(regiao.getTpRegiao()==RegiaoServices._PAISES)	{
					ResultSetMap rsmPaises = RegiaoServices.getPaisesOfRegiao(cdRegiao, connect);
					while(rsmPaises.next())	{
						objeto.setCdEstado(rsmPaises.getInt("cd_pais"));
						retorno = insert(objeto, connect);
						if(retorno.getCode() <= 0)
							break;
					}
				}
				/*
				 * Incluindo para estados
				 */
				if(regiao.getTpRegiao()==RegiaoServices._ESTADOS)	{
					ResultSetMap rsmEstados = RegiaoServices.getEstadosOfRegiao(cdRegiao, connect);
					while(rsmEstados.next())	{
						objeto.setCdEstado(rsmEstados.getInt("cd_estado"));
						retorno = insert(objeto, connect);
						if(retorno.getCode() <= 0)
							break;
					}
				}
				/*
				 * Incluindo para cidades
				 */
				if(regiao.getTpRegiao()==RegiaoServices._CIDADES)	{
					ResultSetMap rsmCidades = RegiaoServices.getEstadosOfRegiao(cdRegiao, connect);
					while(rsmCidades.next())	{
						objeto.setCdEstado(rsmCidades.getInt("cd_cidade"));
						retorno = insert(objeto, connect);
						if(retorno.getCode() <= 0)
							break;
					}
				}

				return retorno;
			}
			else	{
				return insert(objeto, connect);
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir nova regra de tributação!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result insert(ProdutoServicoTributo objeto, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			/*
			 * Verifica a caracter exclusivo dos atributos Produto/Serviço e Classificação Fiscal
			 */
			if(objeto.getCdClassificacaoFiscal()>0 && objeto.getCdProdutoServico()>0)	{
				Exception e = new Exception("Não é permitido informar o produto e a classificação fiscal! A regra deve valer para um ou para o outro!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(-70, e.getMessage(), e);
			}
			/*
			 * Verifica o preenchimento da questão geográfica
			 */
			Tributo tributo = TributoDAO.get(objeto.getCdTributo(), connect);
			if(tributo.getTpEsferaAplicacao()==TributoServices._ESFERA_ESTADUAL && objeto.getCdEstado()<=0)	{
				Exception e = new Exception("Para tributos estaduais é obrigatório informar o estado!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(-80, e.getMessage(), e);
			}
			/*
			 * Verifica o preenchimento da questão geográfica
			 */
			if(tributo.getTpEsferaAplicacao()==TributoServices._ESFERA_MUNICIPAL && objeto.getCdCidade()<=0)	{
				Exception e = new Exception("Para tributos municipais é obrigatório informar a cidade!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(-90, e.getMessage(), e);
			}
			PreparedStatement pstmt = connect.prepareStatement(
													   "SELECT * FROM adm_produto_servico_tributo " +
													   "WHERE cd_tributo = " +objeto.getCdTributo()+
													   "  AND cd_tributo_aliquota = " +objeto.getCdTributoAliquota()+
													   "  AND cd_natureza_operacao = "+objeto.getCdNaturezaOperacao()+
													   (objeto.getCdCidade()>0	? " AND cd_cidade = "+objeto.getCdCidade(): "")+
													   (objeto.getCdEstado()>0	? " AND cd_estado = "+objeto.getCdEstado(): "")+
													   (objeto.getCdPais()>0	? " AND cd_pais = "+objeto.getCdPais()	  : "")+
													   (objeto.getCdProdutoServico()>0?"  AND cd_produto_servico = "+objeto.getCdProdutoServico():"")+
													   (objeto.getCdClassificacaoFiscal()>0?"  AND cd_classificacao_fiscal = "+objeto.getCdClassificacaoFiscal():""));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())	{
				Exception e = new Exception("Regra duplicada!");
				com.tivic.manager.util.Util.registerLog(e);
				return new Result(-100, e.getMessage(), e);
			}
			return ProdutoServicoTributoDAO.insert(objeto, connect);
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir regra de tributação!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int deleteAliquotasOfProdutoServico(int cdTributo, int cdTributoAliquota, int cdProdutoServico) {
		return deleteAliquotasOfProdutoServico(cdTributo, cdTributoAliquota, cdProdutoServico, null);
	}

	public static int deleteAliquotasOfProdutoServico(int cdTributo, int cdTributoAliquota, int cdProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
													   "DELETE FROM adm_produto_servico_tributo " +
													   "WHERE cd_tributo = " +cdTributo+
													   "  AND cd_tributo_aliquota = " +cdTributoAliquota+
													   "  AND cd_produto_servico = "+cdProdutoServico);
			pstmt.execute();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllConfiguracoesProdutoServico(int cdTributo, int cdAtributoAliquota, int cdProdutoServico) {
		return getAllConfiguracoesProdutoServico(cdTributo, cdAtributoAliquota, cdProdutoServico, null);
	}

	public static ResultSetMap getAllConfiguracoesProdutoServico(int cdTributo, int cdAtributoAliquota, int cdProdutoServico, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_cidade, C.nm_estado, d.nm_pais, E.nm_natureza_operacao, " +
					"       F.id_classificacao_fiscal, F.nm_classificacao_fiscal " +
					"FROM adm_produto_servico_tributo A " +
					"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado C ON (A.cd_estado = C.cd_estado) " +
					"LEFT OUTER JOIN grl_pais   D ON (A.cd_pais = D.cd_pais) " +
					"LEFT OUTER JOIN adm_natureza_operacao E ON (A.cd_natureza_operacao = E.cd_natureza_operacao) " +
					"LEFT OUTER JOIN adm_classificacao_fiscal  F ON (A.cd_classificacao_fiscal = F.cd_classificacao_fiscal) " +
					"WHERE A.cd_tributo = " +cdTributo+
					"  AND A.cd_tributo_aliquota = " +cdAtributoAliquota+
					"  AND A.cd_produto_servico = "+cdProdutoServico);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int cdTributo = 0;
		int cdTributoAliquota = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cd_tributo")) {
				cdTributo = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("cd_tributo_aliquota")) {
				cdTributoAliquota = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * FROM grl_produto_servico A " +
						   "WHERE NOT A.cd_produto_servico IN (SELECT cd_produto_servico " +
						   "                                   FROM adm_produto_servico_tributo B " +
						   "                                   WHERE B.cd_produto_servico = A.cd_produto_servico " +
						   "                                     AND B.cd_tributo = " + cdTributo +
						   "                                     AND B.cd_tributo_aliquota = " + cdTributoAliquota + ")", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static int delete(int cdProdutoServicoTributo, int cdTributoAliquota, int cdTributo, int cdProdutoServico) {
		return delete(cdProdutoServicoTributo, cdTributoAliquota, cdTributo, cdProdutoServico, null);
	}

	public static int delete(int cdProdutoServicoTributo, int cdTributoAliquota, int cdTributo, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_produto_servico_tributo " +
					"WHERE cd_produto_servico=? " +
					(cdTributo!=0 && cdTributoAliquota!=0 && cdProdutoServicoTributo!=0 ? "  AND cd_produto_servico_tributo = ? " : "") +
					(cdTributo!=0 && cdTributoAliquota!=0 ? "  AND cd_tributo_aliquota=? " : "") +
					(cdTributo!=0 ? "  AND cd_tributo=? " : ""));
			pstmt.setInt(1, cdProdutoServico);
			int i = 2;
			if (cdTributo!=0 && cdTributoAliquota!=0 && cdProdutoServicoTributo!=0)
				pstmt.setInt(i++, cdProdutoServicoTributo);
			if (cdTributo!=0 && cdTributoAliquota!=0)
				pstmt.setInt(i++, cdTributoAliquota);
			if (cdTributo!=0)
				pstmt.setInt(i++, cdTributo);
			pstmt.execute();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getConfiguracoesOfTributoAliquota(int cdTributo, int cdAtributoAliquota) {
		return getConfiguracoesOfTributoAliquota(cdTributo, cdAtributoAliquota, null);
	}

	public static ResultSetMap getConfiguracoesOfTributoAliquota(int cdTributo, int cdAtributoAliquota, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_cidade, C.nm_estado, d.nm_pais, E.nm_natureza_operacao, " +
					"       F.id_classificacao_fiscal, F.nm_classificacao_fiscal, " +
					"FROM adm_produto_servico_tributo A " +
					"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado C ON (A.cd_estado = C.cd_estado) " +
					"LEFT OUTER JOIN grl_pais   D ON (A.cd_pais = D.cd_pais) " +
					"LEFT OUTER JOIN adm_natureza_operacao E ON (A.cd_natureza_operacao = E.cd_natureza_operacao) " +
					"LEFT OUTER JOIN adm_classificacao_fiscal F ON (A.cd_classificacao_fiscal = F.cd_classificacao_fiscal) " +
					"WHERE A.cd_tributo = " +cdTributo+
					"  AND A.cd_tributo_aliquota = " +cdAtributoAliquota);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.rollback(connection);
		}
	}

}
