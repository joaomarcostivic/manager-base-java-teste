package com.tivic.manager.adm;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.Moeda;
import com.tivic.manager.grl.MoedaDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class TipoOperacaoServices {
	
	public static int init()	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			ResultSetMap rsmTabelaPreco = com.tivic.manager.adm.TabelaPrecoDAO.getAll();
			if(rsmTabelaPreco.next()){
				if(TipoOperacaoDAO.getAll().size() == 0)
					if(com.tivic.manager.adm.TipoOperacaoDAO.insert(new TipoOperacao(0, "VENDA AO CONSUMIDOR", "01", 1, 0, rsmTabelaPreco.getInt("cd_tabela_preco")), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return -1;
					}
			}
			else	{
				int cdMoeda      = 0;
				ResultSetMap rsm = MoedaDAO.getAll(connect);
				if(rsm.next())
					cdMoeda = rsm.getInt("cd_moeda");
				else
					cdMoeda = MoedaDAO.insert(new Moeda(0, 0, "REAL", "R$", "R", 1), connect);
				//
				int cdTabelaPreco = TabelaPrecoDAO.insert(new TabelaPreco(0, EmpresaServices.getDefaultEmpresa().getCdEmpresa(), cdMoeda, 0, "TABELA PRECO", Util.getDataAtual(), null, 1, null, null, 0, 0), connect);
				
				if(cdTabelaPreco < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
				
				if(TipoOperacaoDAO.getAll().size() == 0)
					if(com.tivic.manager.adm.TipoOperacaoDAO.insert(new TipoOperacao(0, "VENDA AO CONSUMIDOR", "01", 1, 0, cdTabelaPreco), connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return -1;
					}
			}
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
	
	public static int getCdTabelaPrecoOfOperacao(int cdTipoOperacao)	{
		return getCdTabelaPrecoOfOperacao(cdTipoOperacao, null);
	}

	public static int getCdTabelaPrecoOfOperacao(int cdTipoOperacao, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT cd_tabela_preco FROM adm_tipo_operacao " +
					                                "WHERE cd_tipo_operacao = "+cdTipoOperacao).executeQuery();
			if(rs.next())
				return rs.getInt("cd_tabela_preco");
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaPreco getTabelaPrecoOfOperacao(int cdTipoOperacao)	{
		return getTabelaPrecoOfOperacao(cdTipoOperacao, null);
	}

	public static TabelaPreco getTabelaPrecoOfOperacao(int cdTipoOperacao, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT cd_tabela_preco FROM adm_tipo_operacao " +
					                                "WHERE cd_tipo_operacao = "+cdTipoOperacao).executeQuery();
			if(rs.next())
				return TabelaPrecoDAO.get(rs.getInt("cd_tabela_preco"));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		return null;
	}

	public static ResultSetMap getAllIsContrato() {
		return getAllIsContrato(null);
	}

	public static ResultSetMap getAllIsContrato(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tipo_operacao WHERE (lg_contrato = 1) AND (st_tipo_operacao = 1)");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoServices.getAllIsContrato: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoServices.getAllIsContrato: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(-1, null);
	}

	public static ResultSetMap getAll(int lgContrato) {
		return getAll(lgContrato, null);
	}

	public static ResultSetMap getAll(int lgContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tabela_preco " +
					                         "FROM adm_tipo_operacao A " +
					                         "LEFT OUTER JOIN adm_tabela_preco B ON (A.cd_tabela_preco = B.cd_tabela_preco) " +
					                         (lgContrato>=0?"WHERE A.lg_contrato = "+lgContrato:"")+
					                         " ORDER BY A.nm_tipo_operacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoOperacao) {
		return delete(cdTipoOperacao, null);
	}

	public static int delete(int cdTipoOperacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
	
			int code = 0;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_tipo_operacao", Integer.toString(cdTipoOperacao), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmTipoOperacaoCfop = TipoOperacaoCfopDAO.find(criterios);
			while(rsmTipoOperacaoCfop.next()){
				code = TipoOperacaoCfopDAO.delete(cdTipoOperacao, rsmTipoOperacaoCfop.getInt("cd_natureza_operacao"), connect);
				if(code < 1){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return code;
				}
			}
			
			code = TipoOperacaoDAO.delete(cdTipoOperacao, connect);
			if(code < 1)
				if(isConnectionNull)
					Conexao.rollback(connect);
			
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOperacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
