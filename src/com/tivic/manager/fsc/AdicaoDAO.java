package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AdicaoDAO{

	public static int insert(Adicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Adicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_adicao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAdicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_adicao (cd_adicao,"+
			                                  "cd_declaracao_importacao,"+
			                                  "cd_pais,"+
			                                  "cd_ncm,"+
			                                  "vl_vmcv,"+
			                                  "qt_peso_liquido,"+
			                                  "nr_adicao,"+
			                                  "nr_seq_adicao_item,"+
			                                  "vl_desconto,"+
			                                  "nr_pedido_compra,"+
			                                  "nr_item_pedido_compra," +
			                                  "vl_aduaneiro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDeclaracaoImportacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDeclaracaoImportacao());
			if(objeto.getCdPais()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPais());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNcm());
			pstmt.setFloat(5,objeto.getVlVmcv());
			pstmt.setFloat(6,objeto.getQtPesoLiquido());
			pstmt.setInt(7,objeto.getNrAdicao());
			pstmt.setInt(8,objeto.getNrSeqAdicaoItem());
			pstmt.setFloat(9,objeto.getVlDesconto());
			pstmt.setString(10,objeto.getNrPedidoCompra());
			pstmt.setString(11,objeto.getNrItemPedidoCompra());
			pstmt.setFloat(12,objeto.getVlAduaneiro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Adicao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Adicao objeto, int cdAdicaoOld) {
		return update(objeto, cdAdicaoOld, null);
	}

	public static int update(Adicao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Adicao objeto, int cdAdicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_adicao SET cd_adicao=?,"+
												      		   "cd_declaracao_importacao=?,"+
												      		   "cd_pais=?,"+
												      		   "cd_ncm=?,"+
												      		   "vl_vmcv=?,"+
												      		   "qt_peso_liquido=?,"+
												      		   "nr_adicao=?,"+
												      		   "nr_seq_adicao_item=?,"+
												      		   "vl_desconto=?,"+
												      		   "nr_pedido_compra=?,"+
												      		   "nr_item_pedido_compra=?," +
												      		   "vl_aduaneiro=? WHERE cd_adicao=?");
			pstmt.setInt(1,objeto.getCdAdicao());
			if(objeto.getCdDeclaracaoImportacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDeclaracaoImportacao());
			if(objeto.getCdPais()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPais());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNcm());
			pstmt.setFloat(5,objeto.getVlVmcv());
			pstmt.setFloat(6,objeto.getQtPesoLiquido());
			pstmt.setInt(7,objeto.getNrAdicao());
			pstmt.setInt(8,objeto.getNrSeqAdicaoItem());
			pstmt.setFloat(9,objeto.getVlDesconto());
			pstmt.setString(10,objeto.getNrPedidoCompra());
			pstmt.setString(11,objeto.getNrItemPedidoCompra());
			pstmt.setFloat(12,objeto.getVlAduaneiro());
			pstmt.setInt(13, cdAdicaoOld!=0 ? cdAdicaoOld : objeto.getCdAdicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAdicao) {
		return delete(cdAdicao, null);
	}

	public static int delete(int cdAdicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_adicao WHERE cd_adicao=?");
			pstmt.setInt(1, cdAdicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Adicao get(int cdAdicao) {
		return get(cdAdicao, null);
	}

	public static Adicao get(int cdAdicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_adicao WHERE cd_adicao=?");
			pstmt.setInt(1, cdAdicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Adicao(rs.getInt("cd_adicao"),
						rs.getInt("cd_declaracao_importacao"),
						rs.getInt("cd_pais"),
						rs.getInt("cd_ncm"),
						rs.getFloat("vl_vmcv"),
						rs.getFloat("qt_peso_liquido"),
						rs.getInt("nr_adicao"),
						rs.getInt("nr_seq_adicao_item"),
						rs.getFloat("vl_desconto"),
						rs.getString("nr_pedido_compra"),
						rs.getString("nr_item_pedido_compra"),
						rs.getFloat("vl_aduaneiro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_adicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AdicaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_adicao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
