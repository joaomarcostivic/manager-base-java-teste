package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoReferenciaDAO{

	public static int insert(ContratoReferencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContratoReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_referencia (cd_contrato,"+
			                                  "cd_referencia,"+
			                                  "dt_contratacao,"+
			                                  "qt_produto_servico,"+
			                                  "vl_produto_servico,"+
			                                  "cd_tipo_produto_servico,"+
			                                  "cd_garantia) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getQtProdutoServico());
			pstmt.setFloat(5,objeto.getVlProdutoServico());
			if(objeto.getCdTipoProdutoServico()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoProdutoServico());
			if(objeto.getCdGarantia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGarantia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoReferencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoReferencia objeto, int cdContratoOld, int cdReferenciaOld) {
		return update(objeto, cdContratoOld, cdReferenciaOld, null);
	}

	public static int update(ContratoReferencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoReferencia objeto, int cdContratoOld, int cdReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_referencia SET cd_contrato=?,"+
												      		   "cd_referencia=?,"+
												      		   "dt_contratacao=?,"+
												      		   "qt_produto_servico=?,"+
												      		   "vl_produto_servico=?,"+
												      		   "cd_tipo_produto_servico=?,"+
												      		   "cd_garantia=? WHERE cd_contrato=? AND cd_referencia=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdReferencia());
			if(objeto.getDtContratacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtContratacao().getTimeInMillis()));
			pstmt.setFloat(4,objeto.getQtProdutoServico());
			pstmt.setFloat(5,objeto.getVlProdutoServico());
			if(objeto.getCdTipoProdutoServico()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoProdutoServico());
			if(objeto.getCdGarantia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdGarantia());
			pstmt.setInt(8, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(9, cdReferenciaOld!=0 ? cdReferenciaOld : objeto.getCdReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdReferencia) {
		return delete(cdContrato, cdReferencia, null);
	}

	public static int delete(int cdContrato, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_referencia WHERE cd_contrato=? AND cd_referencia=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoReferencia get(int cdContrato, int cdReferencia) {
		return get(cdContrato, cdReferencia, null);
	}

	public static ContratoReferencia get(int cdContrato, int cdReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_referencia WHERE cd_contrato=? AND cd_referencia=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoReferencia(rs.getInt("cd_contrato"),
						rs.getInt("cd_referencia"),
						(rs.getTimestamp("dt_contratacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_contratacao").getTime()),
						rs.getFloat("qt_produto_servico"),
						rs.getFloat("vl_produto_servico"),
						rs.getInt("cd_tipo_produto_servico"),
						rs.getInt("cd_garantia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoReferenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
