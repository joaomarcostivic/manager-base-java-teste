package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoProdutoServicoDAO{

	public static int insert(ContratoProdutoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContratoProdutoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_produto_servico (cd_contrato,"+
			                                  "cd_produto_servico,"+
			                                  "qt_produto_servico,"+
			                                  "vl_produto_servico,"+
			                                  "dt_contratacao,"+
			                                  "cd_tipo_produto_servico,"+
			                                  "cd_garantia," +
			                                  "cd_referencia," +
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setFloat(3,objeto.getQtProdutoServico());
			pstmt.setFloat(4,objeto.getVlProdutoServico());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			if(objeto.getCdTipoProdutoServico()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoProdutoServico());
			if(objeto.getCdGarantia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGarantia());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdReferencia());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdEmpresa());
			return pstmt.executeUpdate();
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

	public static int update(ContratoProdutoServico objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoProdutoServico objeto, int cdContratoOld, int cdProdutoServicoOld) {
		return update(objeto, cdContratoOld, cdProdutoServicoOld, null);
	}

	public static int update(ContratoProdutoServico objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoProdutoServico objeto, int cdContratoOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_produto_servico SET cd_contrato=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "qt_produto_servico=?,"+
												      		   "vl_produto_servico=?,"+
												      		   "dt_contratacao=?,"+
												      		   "cd_tipo_produto_servico=?,"+
												      		   "cd_garantia=?," +
												      		   "cd_referencia=?," +
												      		   "cd_empresa=? WHERE cd_contrato=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setFloat(3,objeto.getQtProdutoServico());
			pstmt.setFloat(4,objeto.getVlProdutoServico());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			if(objeto.getCdTipoProdutoServico()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoProdutoServico());
			if(objeto.getCdGarantia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGarantia());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdReferencia());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdEmpresa());
			pstmt.setInt(10, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(11, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoProdutoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdProdutoServico) {
		return delete(cdContrato, cdProdutoServico, null);
	}

	public static int delete(int cdContrato, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_produto_servico WHERE cd_contrato=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
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

	public static ContratoProdutoServico get(int cdContrato, int cdProdutoServico) {
		return get(cdContrato, cdProdutoServico, null);
	}

	public static ContratoProdutoServico get(int cdContrato, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_produto_servico WHERE cd_contrato=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoProdutoServico(rs.getInt("cd_contrato"),
						rs.getInt("cd_produto_servico"),
						rs.getFloat("qt_produto_servico"),
						rs.getFloat("vl_produto_servico"),
						(rs.getTimestamp("dt_contratacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_contratacao").getTime()),
						rs.getInt("cd_tipo_produto_servico"),
						rs.getInt("cd_garantia"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_empresa"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM adm_contrato_produto_servico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
