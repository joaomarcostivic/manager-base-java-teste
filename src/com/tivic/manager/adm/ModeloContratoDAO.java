package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ModeloContratoDAO{

	public static int insert(ModeloContrato objeto) {
		return insert(objeto, null);
	}

	public static int insert(ModeloContrato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.ModeloDocumentoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdModelo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_modelo_contrato (cd_modelo_contrato,"+
			                                  "cd_indicador,"+
			                                  "nr_parcelas,"+
			                                  "vl_adesao,"+
			                                  "pr_juros_mora,"+
			                                  "pr_multa_mora,"+
			                                  "pr_desconto) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdModelo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdModelo());
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdIndicador());
			pstmt.setInt(3,objeto.getNrParcelas());
			pstmt.setFloat(4,objeto.getVlAdesao());
			pstmt.setFloat(5,objeto.getPrJurosMora());
			pstmt.setFloat(6,objeto.getPrMultaMora());
			pstmt.setFloat(7,objeto.getPrDesconto());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ModeloContrato objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ModeloContrato objeto, int cdModeloContratoOld) {
		return update(objeto, cdModeloContratoOld, null);
	}

	public static int update(ModeloContrato objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ModeloContrato objeto, int cdModeloContratoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			ModeloContrato objetoTemp = get(objeto.getCdModelo(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO adm_modelo_contrato (cd_modelo_contrato,"+
			                                  "cd_indicador,"+
			                                  "nr_parcelas,"+
			                                  "vl_adesao,"+
			                                  "pr_juros_mora,"+
			                                  "pr_multa_mora,"+
			                                  "pr_desconto) VALUES (?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE adm_modelo_contrato SET cd_modelo_contrato=?,"+
												      		   "cd_indicador=?,"+
												      		   "nr_parcelas=?,"+
												      		   "vl_adesao=?,"+
												      		   "pr_juros_mora=?,"+
												      		   "pr_multa_mora=?,"+
												      		   "pr_desconto=? WHERE cd_modelo_contrato=?");
			pstmt.setInt(1,objeto.getCdModelo());
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdIndicador());
			pstmt.setInt(3,objeto.getNrParcelas());
			pstmt.setFloat(4,objeto.getVlAdesao());
			pstmt.setFloat(5,objeto.getPrJurosMora());
			pstmt.setFloat(6,objeto.getPrMultaMora());
			pstmt.setFloat(7,objeto.getPrDesconto());
			if (objetoTemp != null) {
				pstmt.setInt(8, cdModeloContratoOld!=0 ? cdModeloContratoOld : objeto.getCdModelo());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ModeloDocumentoDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModeloContrato) {
		return delete(cdModeloContrato, null);
	}

	public static int delete(int cdModeloContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_modelo_contrato WHERE cd_modelo_contrato=?");
			pstmt.setInt(1, cdModeloContrato);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ModeloDocumentoDAO.delete(cdModeloContrato, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ModeloContrato get(int cdModeloContrato) {
		return get(cdModeloContrato, null);
	}

	public static ModeloContrato get(int cdModeloContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_modelo_contrato A, grl_modelo_documento B WHERE A.cd_modelo_contrato=B.cd_modelo AND A.cd_modelo_contrato=?");
			pstmt.setInt(1, cdModeloContrato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ModeloContrato(rs.getInt("cd_modelo"),
						rs.getString("nm_modelo"),
						rs.getString("txt_modelo"),
						rs.getInt("tp_modelo"),
						rs.getBytes("blb_conteudo")==null?null:rs.getBytes("blb_conteudo"),
						rs.getString("txt_conteudo"),
						rs.getInt("st_modelo"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("cd_indicador"),
						rs.getInt("nr_parcelas"),
						rs.getFloat("vl_adesao"),
						rs.getFloat("pr_juros_mora"),
						rs.getFloat("pr_multa_mora"),
						rs.getFloat("pr_desconto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_modelo_contrato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloContratoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_modelo_contrato", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
