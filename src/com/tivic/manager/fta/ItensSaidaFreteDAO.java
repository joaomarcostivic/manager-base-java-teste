package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ItensSaidaFreteDAO{

	public static int insert(ItensSaidaFrete objeto) {
		return insert(objeto, null);
	}

	public static int insert(ItensSaidaFrete objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_itens_saida_frete (cd_documento_entrada,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "cd_frete,"+
			                                  "qt_transportada) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdFrete()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFrete());
			pstmt.setFloat(5,objeto.getQtTransportada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ItensSaidaFrete objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(ItensSaidaFrete objeto, int cdDocumentoEntradaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdFreteOld) {
		return update(objeto, cdDocumentoEntradaOld, cdProdutoServicoOld, cdEmpresaOld, cdFreteOld, null);
	}

	public static int update(ItensSaidaFrete objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(ItensSaidaFrete objeto, int cdDocumentoEntradaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdFreteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_itens_saida_frete SET cd_documento_entrada=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_frete=?,"+
												      		   "qt_transportada=? WHERE cd_documento_entrada=? AND cd_produto_servico=? AND cd_empresa=? AND cd_frete=?");
			pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdFrete());
			pstmt.setFloat(5,objeto.getQtTransportada());
			pstmt.setInt(6, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setInt(7, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdFreteOld!=0 ? cdFreteOld : objeto.getCdFrete());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdFrete) {
		return delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdFrete, null);
	}

	public static int delete(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdFrete, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_itens_saida_frete WHERE cd_documento_entrada=? AND cd_produto_servico=? AND cd_empresa=? AND cd_frete=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdFrete);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ItensSaidaFrete get(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdFrete) {
		return get(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdFrete, null);
	}

	public static ItensSaidaFrete get(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdFrete, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_itens_saida_frete WHERE cd_documento_entrada=? AND cd_produto_servico=? AND cd_empresa=? AND cd_frete=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdFrete);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ItensSaidaFrete(rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_frete"),
						rs.getFloat("qt_transportada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_itens_saida_frete");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ItensSaidaFreteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_itens_saida_frete", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
