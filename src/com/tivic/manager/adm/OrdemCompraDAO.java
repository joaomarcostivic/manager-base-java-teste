package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class OrdemCompraDAO{

	public static int insert(OrdemCompra objeto) {
		return insert(objeto, null);
	}

	public static int insert(OrdemCompra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_ordem_compra", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOrdemCompra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_ordem_compra (cd_ordem_compra,"+
			                                  "dt_ordem_compra,"+
			                                  "st_ordem_compra,"+
			                                  "dt_limite_entrega,"+
			                                  "id_ordem_compra,"+
			                                  "nr_ordem_compra,"+
			                                  "cd_fornecedor,"+
			                                  "cd_local_entrega,"+
			                                  "cd_moeda,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_comprador,"+
			                                  "vl_desconto,"+
			                                  "vl_acrescimo,"+
			                                  "tp_movimento_estoque,"+
			                                  "txt_observacao,"+
			                                  "vl_total_documento,"+
			                                  "cd_empresa,"+
			                                  "cd_usuario_autorizacao,"+
			                                  "cd_ordem_compra_origem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtOrdemCompra()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtOrdemCompra().getTimeInMillis()));
			pstmt.setInt(3,objeto.getStOrdemCompra());
			if(objeto.getDtLimiteEntrega()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLimiteEntrega().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdOrdemCompra());
			pstmt.setString(6,objeto.getNrOrdemCompra());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdFornecedor());
			if(objeto.getCdLocalEntrega()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLocalEntrega());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMoeda());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTabelaPreco());
			if(objeto.getCdComprador()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdComprador());
			pstmt.setFloat(12,objeto.getVlDesconto());
			pstmt.setFloat(13,objeto.getVlAcrescimo());
			pstmt.setInt(14,objeto.getTpMovimentoEstoque());
			pstmt.setString(15,objeto.getTxtObservacao());
			pstmt.setFloat(16,objeto.getVlTotalDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdEmpresa());
			if(objeto.getCdUsuarioAutorizacao()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdUsuarioAutorizacao());
			if(objeto.getCdOrdemCompraOrigem()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdOrdemCompraOrigem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(OrdemCompra objeto) {
		return update(objeto, 0, null);
	}

	public static int update(OrdemCompra objeto, int cdOrdemCompraOld) {
		return update(objeto, cdOrdemCompraOld, null);
	}

	public static int update(OrdemCompra objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(OrdemCompra objeto, int cdOrdemCompraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_ordem_compra SET cd_ordem_compra=?,"+
												      		   "dt_ordem_compra=?,"+
												      		   "st_ordem_compra=?,"+
												      		   "dt_limite_entrega=?,"+
												      		   "id_ordem_compra=?,"+
												      		   "nr_ordem_compra=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "cd_local_entrega=?,"+
												      		   "cd_moeda=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_comprador=?,"+
												      		   "vl_desconto=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "tp_movimento_estoque=?,"+
												      		   "txt_observacao=?,"+
												      		   "vl_total_documento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_usuario_autorizacao=?,"+
												      		   "cd_ordem_compra_origem=? WHERE cd_ordem_compra=?");
			pstmt.setInt(1,objeto.getCdOrdemCompra());
			if(objeto.getDtOrdemCompra()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtOrdemCompra().getTimeInMillis()));
			pstmt.setInt(3,objeto.getStOrdemCompra());
			if(objeto.getDtLimiteEntrega()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLimiteEntrega().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdOrdemCompra());
			pstmt.setString(6,objeto.getNrOrdemCompra());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdFornecedor());
			if(objeto.getCdLocalEntrega()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLocalEntrega());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMoeda());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTabelaPreco());
			if(objeto.getCdComprador()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdComprador());
			pstmt.setFloat(12,objeto.getVlDesconto());
			pstmt.setFloat(13,objeto.getVlAcrescimo());
			pstmt.setInt(14,objeto.getTpMovimentoEstoque());
			pstmt.setString(15,objeto.getTxtObservacao());
			pstmt.setFloat(16,objeto.getVlTotalDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdEmpresa());
			if(objeto.getCdUsuarioAutorizacao()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdUsuarioAutorizacao());
			if(objeto.getCdOrdemCompraOrigem()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdOrdemCompraOrigem());
			pstmt.setInt(20, cdOrdemCompraOld!=0 ? cdOrdemCompraOld : objeto.getCdOrdemCompra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOrdemCompra) {
		return delete(cdOrdemCompra, null);
	}

	public static int delete(int cdOrdemCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_ordem_compra WHERE cd_ordem_compra=?");
			pstmt.setInt(1, cdOrdemCompra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static OrdemCompra get(int cdOrdemCompra) {
		return get(cdOrdemCompra, null);
	}

	public static OrdemCompra get(int cdOrdemCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_ordem_compra WHERE cd_ordem_compra=?");
			pstmt.setInt(1, cdOrdemCompra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new OrdemCompra(rs.getInt("cd_ordem_compra"),
						(rs.getTimestamp("dt_ordem_compra")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ordem_compra").getTime()),
						rs.getInt("st_ordem_compra"),
						(rs.getTimestamp("dt_limite_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_entrega").getTime()),
						rs.getString("id_ordem_compra"),
						rs.getString("nr_ordem_compra"),
						rs.getInt("cd_fornecedor"),
						rs.getInt("cd_local_entrega"),
						rs.getInt("cd_moeda"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_comprador"),
						rs.getFloat("vl_desconto"),
						rs.getFloat("vl_acrescimo"),
						rs.getInt("tp_movimento_estoque"),
						rs.getString("txt_observacao"),
						rs.getFloat("vl_total_documento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_usuario_autorizacao"),
						rs.getInt("cd_ordem_compra_origem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_ordem_compra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<OrdemCompra> getList() {
		return getList(null);
	}

	public static ArrayList<OrdemCompra> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<OrdemCompra> list = new ArrayList<OrdemCompra>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				OrdemCompra obj = OrdemCompraDAO.get(rsm.getInt("cd_ordem_compra"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_ordem_compra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
