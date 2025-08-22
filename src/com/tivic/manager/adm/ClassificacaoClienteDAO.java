package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

import com.tivic.sol.connection.Conexao;

public class ClassificacaoClienteDAO{

	public static int insert(ClassificacaoCliente objeto) {
		return insert(objeto, null);
	}

	public static int insert(ClassificacaoCliente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_classificacao_cliente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdClassificacaoCliente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_classificacao_cliente (cd_classificacao_cliente,"+
			                                  "nm_classificacao_cliente,"+
			                                  "pr_taxa_padrao_factoring,"+
			                                  "vl_taxa_devolucao_factoring,"+
			                                  "pr_taxa_juros_factoring,"+
			                                  "pr_taxa_prorrogacao_factoring,"+
			                                  "vl_limite_factoring,"+
			                                  "vl_limite_factoring_emissor,"+
			                                  "vl_limite_factoring_unitario,"+
			                                  "qt_prazo_minimo_factoring,"+
			                                  "qt_prazo_maximo_factoring,"+
			                                  "qt_idade_minima_factoring,"+
			                                  "vl_ganho_minimo_factoring,"+
			                                  "pr_taxa_minima_factoring,"+
			                                  "qt_maximo_documento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmClassificacaoCliente());
			pstmt.setFloat(3,objeto.getPrTaxaPadraoFactoring());
			pstmt.setFloat(4,objeto.getVlTaxaDevolucaoFactoring());
			pstmt.setFloat(5,objeto.getPrTaxaJurosFactoring());
			pstmt.setFloat(6,objeto.getPrTaxaProrrogacaoFactoring());
			pstmt.setFloat(7,objeto.getVlLimiteFactoring());
			pstmt.setFloat(8,objeto.getVlLimiteFactoringEmissor());
			pstmt.setFloat(9,objeto.getVlLimiteFactoringUnitario());
			pstmt.setInt(10,objeto.getQtPrazoMinimoFactoring());
			pstmt.setInt(11,objeto.getQtPrazoMaximoFactoring());
			pstmt.setInt(12,objeto.getQtIdadeMinimaFactoring());
			pstmt.setFloat(13,objeto.getVlGanhoMinimoFactoring());
			pstmt.setFloat(14,objeto.getPrTaxaMinimaFactoring());
			pstmt.setInt(15,objeto.getQtMaximoDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ClassificacaoCliente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ClassificacaoCliente objeto, int cdClassificacaoClienteOld) {
		return update(objeto, cdClassificacaoClienteOld, null);
	}

	public static int update(ClassificacaoCliente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ClassificacaoCliente objeto, int cdClassificacaoClienteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_classificacao_cliente SET cd_classificacao_cliente=?,"+
												      		   "nm_classificacao_cliente=?,"+
												      		   "pr_taxa_padrao_factoring=?,"+
												      		   "vl_taxa_devolucao_factoring=?,"+
												      		   "pr_taxa_juros_factoring=?,"+
												      		   "pr_taxa_prorrogacao_factoring=?,"+
												      		   "vl_limite_factoring=?,"+
												      		   "vl_limite_factoring_emissor=?,"+
												      		   "vl_limite_factoring_unitario=?,"+
												      		   "qt_prazo_minimo_factoring=?,"+
												      		   "qt_prazo_maximo_factoring=?,"+
												      		   "qt_idade_minima_factoring=?,"+
												      		   "vl_ganho_minimo_factoring=?,"+
												      		   "pr_taxa_minima_factoring=?,"+
												      		   "qt_maximo_documento=? WHERE cd_classificacao_cliente=?");
			pstmt.setInt(1,objeto.getCdClassificacaoCliente());
			pstmt.setString(2,objeto.getNmClassificacaoCliente());
			pstmt.setFloat(3,objeto.getPrTaxaPadraoFactoring());
			pstmt.setFloat(4,objeto.getVlTaxaDevolucaoFactoring());
			pstmt.setFloat(5,objeto.getPrTaxaJurosFactoring());
			pstmt.setFloat(6,objeto.getPrTaxaProrrogacaoFactoring());
			pstmt.setFloat(7,objeto.getVlLimiteFactoring());
			pstmt.setFloat(8,objeto.getVlLimiteFactoringEmissor());
			pstmt.setFloat(9,objeto.getVlLimiteFactoringUnitario());
			pstmt.setInt(10,objeto.getQtPrazoMinimoFactoring());
			pstmt.setInt(11,objeto.getQtPrazoMaximoFactoring());
			pstmt.setInt(12,objeto.getQtIdadeMinimaFactoring());
			pstmt.setFloat(13,objeto.getVlGanhoMinimoFactoring());
			pstmt.setFloat(14,objeto.getPrTaxaMinimaFactoring());
			pstmt.setInt(15,objeto.getQtMaximoDocumento());
			pstmt.setInt(16, cdClassificacaoClienteOld!=0 ? cdClassificacaoClienteOld : objeto.getCdClassificacaoCliente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdClassificacaoCliente) {
		return delete(cdClassificacaoCliente, null);
	}

	public static int delete(int cdClassificacaoCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_classificacao_cliente WHERE cd_classificacao_cliente=?");
			pstmt.setInt(1, cdClassificacaoCliente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ClassificacaoCliente get(int cdClassificacaoCliente) {
		return get(cdClassificacaoCliente, null);
	}

	public static ClassificacaoCliente get(int cdClassificacaoCliente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao_cliente WHERE cd_classificacao_cliente=?");
			pstmt.setInt(1, cdClassificacaoCliente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ClassificacaoCliente(rs.getInt("cd_classificacao_cliente"),
						rs.getString("nm_classificacao_cliente"),
						rs.getFloat("pr_taxa_padrao_factoring"),
						rs.getFloat("vl_taxa_devolucao_factoring"),
						rs.getFloat("pr_taxa_juros_factoring"),
						rs.getFloat("pr_taxa_prorrogacao_factoring"),
						rs.getFloat("vl_limite_factoring"),
						rs.getFloat("vl_limite_factoring_emissor"),
						rs.getFloat("vl_limite_factoring_unitario"),
						rs.getInt("qt_prazo_minimo_factoring"),
						rs.getInt("qt_prazo_maximo_factoring"),
						rs.getInt("qt_idade_minima_factoring"),
						rs.getFloat("vl_ganho_minimo_factoring"),
						rs.getFloat("pr_taxa_minima_factoring"),
						rs.getInt("qt_maximo_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao_cliente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoClienteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_classificacao_cliente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
