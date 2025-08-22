package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoProdutoTerceiroDAO{

	public static int insert(ContratoProdutoTerceiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContratoProdutoTerceiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_produto_terceiro (cd_contrato,"+
			                                  "cd_produto_terceiro,"+
			                                  "dt_contratacao,"+
			                                  "qt_produto_servico,"+
			                                  "vl_produto_servico) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdProdutoTerceiro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoTerceiro());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getQtProdutoServico());
			pstmt.setFloat(5,objeto.getVlProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoProdutoTerceiro objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoProdutoTerceiro objeto, int cdContratoOld, int cdProdutoTerceiroOld) {
		return update(objeto, cdContratoOld, cdProdutoTerceiroOld, null);
	}

	public static int update(ContratoProdutoTerceiro objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoProdutoTerceiro objeto, int cdContratoOld, int cdProdutoTerceiroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_produto_terceiro SET cd_contrato=?,"+
												      		   "cd_produto_terceiro=?,"+
												      		   "dt_contratacao=?,"+
												      		   "qt_produto_servico=?,"+
												      		   "vl_produto_servico=? WHERE cd_contrato=? AND cd_produto_terceiro=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdProdutoTerceiro());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getQtProdutoServico());
			pstmt.setFloat(5,objeto.getVlProdutoServico());
			pstmt.setInt(6, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(7, cdProdutoTerceiroOld!=0 ? cdProdutoTerceiroOld : objeto.getCdProdutoTerceiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdProdutoTerceiro) {
		return delete(cdContrato, cdProdutoTerceiro, null);
	}

	public static int delete(int cdContrato, int cdProdutoTerceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_produto_terceiro WHERE cd_contrato=? AND cd_produto_terceiro=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdProdutoTerceiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoProdutoTerceiro get(int cdContrato, int cdProdutoTerceiro) {
		return get(cdContrato, cdProdutoTerceiro, null);
	}

	public static ContratoProdutoTerceiro get(int cdContrato, int cdProdutoTerceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_produto_terceiro WHERE cd_contrato=? AND cd_produto_terceiro=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdProdutoTerceiro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoProdutoTerceiro(rs.getInt("cd_contrato"),
						rs.getInt("cd_produto_terceiro"),
						(rs.getTimestamp("dt_contratacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_contratacao").getTime()),
						rs.getFloat("qt_produto_servico"),
						rs.getFloat("vl_produto_servico"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_produto_terceiro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoTerceiroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_produto_terceiro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
