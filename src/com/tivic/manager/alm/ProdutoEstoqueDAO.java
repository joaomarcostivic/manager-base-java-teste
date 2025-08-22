package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProdutoEstoqueDAO{

	public static int insert(ProdutoEstoque objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoEstoque objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_produto_estoque (cd_local_armazenamento,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "qt_estoque,"+
			                                  "qt_ideal,"+
			                                  "qt_minima,"+
			                                  "qt_maxima,"+
			                                  "qt_dias_estoque,"+
			                                  "qt_minima_ecommerce,"+
			                                  "qt_estoque_consignado,"+
			                                  "lg_default,"+
			                                  "cd_local_armazenamento_origem,"+
			                                  "tp_abastecimento,"+
			                                  "qt_transferencia,"+
			                                  "st_estoque) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLocalArmazenamento());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setFloat(4,objeto.getQtEstoque());
			pstmt.setFloat(5,objeto.getQtIdeal());
			pstmt.setFloat(6,objeto.getQtMinima());
			pstmt.setFloat(7,objeto.getQtMaxima());
			pstmt.setInt(8,objeto.getQtDiasEstoque());
			pstmt.setFloat(9,objeto.getQtMinimaEcommerce());
			pstmt.setFloat(10,objeto.getQtEstoqueConsignado());
			pstmt.setInt(11,objeto.getLgDefault());
			if(objeto.getCdLocalArmazenamentoOrigem()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdLocalArmazenamentoOrigem());
			pstmt.setInt(13,objeto.getTpAbastecimento());
			pstmt.setFloat(14,objeto.getQtTransferencia());
			pstmt.setInt(15,objeto.getStEstoque());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoEstoque objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ProdutoEstoque objeto, int cdLocalArmazenamentoOld, int cdProdutoServicoOld, int cdEmpresaOld) {
		return update(objeto, cdLocalArmazenamentoOld, cdProdutoServicoOld, cdEmpresaOld, null);
	}

	public static int update(ProdutoEstoque objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ProdutoEstoque objeto, int cdLocalArmazenamentoOld, int cdProdutoServicoOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_produto_estoque SET cd_local_armazenamento=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "qt_estoque=?,"+
												      		   "qt_ideal=?,"+
												      		   "qt_minima=?,"+
												      		   "qt_maxima=?,"+
												      		   "qt_dias_estoque=?,"+
												      		   "qt_minima_ecommerce=?,"+
												      		   "qt_estoque_consignado=?,"+
												      		   "lg_default=?,"+
												      		   "cd_local_armazenamento_origem=?,"+
												      		   "tp_abastecimento=?,"+
												      		   "qt_transferencia=?,"+
												      		   "st_estoque=? WHERE cd_local_armazenamento=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdLocalArmazenamento());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setFloat(4,objeto.getQtEstoque());
			pstmt.setFloat(5,objeto.getQtIdeal());
			pstmt.setFloat(6,objeto.getQtMinima());
			pstmt.setFloat(7,objeto.getQtMaxima());
			pstmt.setInt(8,objeto.getQtDiasEstoque());
			pstmt.setFloat(9,objeto.getQtMinimaEcommerce());
			pstmt.setFloat(10,objeto.getQtEstoqueConsignado());
			pstmt.setInt(11,objeto.getLgDefault());
			if(objeto.getCdLocalArmazenamentoOrigem()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdLocalArmazenamentoOrigem());
			pstmt.setInt(13,objeto.getTpAbastecimento());
			pstmt.setFloat(14,objeto.getQtTransferencia());
			pstmt.setInt(15,objeto.getStEstoque());
			pstmt.setInt(16, cdLocalArmazenamentoOld!=0 ? cdLocalArmazenamentoOld : objeto.getCdLocalArmazenamento());
			pstmt.setInt(17, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(18, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLocalArmazenamento, int cdProdutoServico, int cdEmpresa) {
		return delete(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, null);
	}

	public static int delete(int cdLocalArmazenamento, int cdProdutoServico, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_produto_estoque WHERE cd_local_armazenamento=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setInt(1, cdLocalArmazenamento);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoEstoque get(int cdLocalArmazenamento, int cdProdutoServico, int cdEmpresa) {
		return get(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, null);
	}

	public static ProdutoEstoque get(int cdLocalArmazenamento, int cdProdutoServico, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_produto_estoque WHERE cd_local_armazenamento=? AND cd_produto_servico=? AND cd_empresa=?");
			pstmt.setInt(1, cdLocalArmazenamento);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoEstoque(rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getFloat("qt_estoque"),
						rs.getFloat("qt_ideal"),
						rs.getFloat("qt_minima"),
						rs.getFloat("qt_maxima"),
						rs.getInt("qt_dias_estoque"),
						rs.getFloat("qt_minima_ecommerce"),
						rs.getFloat("qt_estoque_consignado"),
						rs.getInt("lg_default"),
						rs.getInt("cd_local_armazenamento_origem"),
						rs.getInt("tp_abastecimento"),
						rs.getFloat("qt_transferencia"),
						rs.getInt("st_estoque"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_produto_estoque");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProdutoEstoque> getList() {
		return getList(null);
	}

	public static ArrayList<ProdutoEstoque> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProdutoEstoque> list = new ArrayList<ProdutoEstoque>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProdutoEstoque obj = ProdutoEstoqueDAO.get(rsm.getInt("cd_local_armazenamento"), rsm.getInt("cd_produto_servico"), rsm.getInt("cd_empresa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueDAO.getList: " + e);
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
		return Search.find("SELECT * FROM alm_produto_estoque", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
