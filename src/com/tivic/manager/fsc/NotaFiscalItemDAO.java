package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.*;
import java.util.ArrayList;

public class NotaFiscalItemDAO{

	public static int insert(NotaFiscalItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotaFiscalItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_nota_fiscal_item (cd_nota_fiscal,"+
			                                  "cd_item,"+
			                                  "cd_documento_saida,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_natureza_operacao,"+
			                                  "qt_tributario,"+
			                                  "vl_unitario,"+
			                                  "txt_informacao_adicional," +
			                                  "cd_item_documento," +
			                                  "vl_acrescimo," +
			                                  "vl_desconto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdNotaFiscal());
			if(objeto.getCdItem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdItem());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoSaida());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoEntrada());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdNaturezaOperacao());
			pstmt.setFloat(8,objeto.getQtTributario());
			pstmt.setFloat(9,objeto.getVlUnitario());
			pstmt.setString(10,objeto.getTxtInformacaoAdicional());
			if(objeto.getCdItemDocumento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdItemDocumento());
			pstmt.setFloat(12,objeto.getVlAcrescimo());
			pstmt.setFloat(13,objeto.getVlDesconto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotaFiscalItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(NotaFiscalItem objeto, int cdNotaFiscalOld, int cdItemOld) {
		return update(objeto, cdNotaFiscalOld, cdItemOld, null);
	}

	public static int update(NotaFiscalItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(NotaFiscalItem objeto, int cdNotaFiscalOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_nota_fiscal_item SET cd_nota_fiscal=?,"+
												      		   "cd_item=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_natureza_operacao=?,"+
												      		   "qt_tributario=?,"+
												      		   "vl_unitario=?,"+
												      		   "txt_informacao_adicional=?," +
												      		   "cd_item_documento=?, " +
												      		   "vl_acrescimo=?, " +
												      		   "vl_desconto=? WHERE cd_nota_fiscal=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdNotaFiscal());
			pstmt.setInt(2,objeto.getCdItem());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoSaida());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoEntrada());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else{
				pstmt.setInt(7,objeto.getCdNaturezaOperacao());
			}
			pstmt.setFloat(8,objeto.getQtTributario());
			pstmt.setFloat(9,objeto.getVlUnitario());
			pstmt.setString(10,objeto.getTxtInformacaoAdicional());
			pstmt.setInt(11,objeto.getCdItemDocumento());
			pstmt.setFloat(12,objeto.getVlAcrescimo());
			pstmt.setFloat(13,objeto.getVlDesconto());
			pstmt.setInt(14, cdNotaFiscalOld!=0 ? cdNotaFiscalOld : objeto.getCdNotaFiscal());
			pstmt.setInt(15, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNotaFiscal, int cdItem) {
		return delete(cdNotaFiscal, cdItem, null);
	}

	public static int delete(int cdNotaFiscal, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_nota_fiscal_item WHERE cd_nota_fiscal=? AND cd_item=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdItem);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotaFiscalItem get(int cdNotaFiscal, int cdItem) {
		return get(cdNotaFiscal, cdItem, null);
	}

	public static NotaFiscalItem get(int cdNotaFiscal, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_item WHERE cd_nota_fiscal=? AND cd_item=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotaFiscalItem(rs.getInt("cd_nota_fiscal"),
						rs.getInt("cd_item"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_natureza_operacao"),
						rs.getFloat("qt_tributario"),
						rs.getFloat("vl_unitario"),
						rs.getString("txt_informacao_adicional"),
						rs.getInt("cd_item_documento"), 
						rs.getFloat("vl_acrescimo"),
						rs.getFloat("vl_desconto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_nota_fiscal_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
