package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoDescontoDAO{

	public static int insert(ContratoDesconto objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContratoDesconto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_contrato");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdContrato()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_desconto");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_contrato_desconto", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDesconto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_desconto (cd_contrato,"+
			                                  "cd_desconto,"+
			                                  "cd_tipo_desconto,"+
			                                  "cd_faixa_desconto,"+
			                                  "cd_empresa,"+
			                                  "dt_inclusao,"+
			                                  "pr_desconto,"+
			                                  "cd_movimento_fidelidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2, code);
			if(objeto.getCdTipoDesconto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoDesconto());
			if(objeto.getCdFaixaDesconto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFaixaDesconto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getDtInclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInclusao().getTimeInMillis()));
			pstmt.setFloat(7,objeto.getPrDesconto());
			if(objeto.getCdMovimentoFidelidade()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMovimentoFidelidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoDesconto objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoDesconto objeto, int cdContratoOld, int cdDescontoOld) {
		return update(objeto, cdContratoOld, cdDescontoOld, null);
	}

	public static int update(ContratoDesconto objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoDesconto objeto, int cdContratoOld, int cdDescontoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_desconto SET cd_contrato=?,"+
												      		   "cd_desconto=?,"+
												      		   "cd_tipo_desconto=?,"+
												      		   "cd_faixa_desconto=?,"+
												      		   "cd_empresa=?,"+
												      		   "dt_inclusao=?,"+
												      		   "pr_desconto=?,"+
												      		   "cd_movimento_fidelidade=? WHERE cd_contrato=? AND cd_desconto=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdDesconto());
			if(objeto.getCdTipoDesconto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoDesconto());
			if(objeto.getCdFaixaDesconto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFaixaDesconto());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getDtInclusao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInclusao().getTimeInMillis()));
			pstmt.setFloat(7,objeto.getPrDesconto());
			if(objeto.getCdMovimentoFidelidade()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMovimentoFidelidade());
			pstmt.setInt(9, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(10, cdDescontoOld!=0 ? cdDescontoOld : objeto.getCdDesconto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdDesconto) {
		return delete(cdContrato, cdDesconto, null);
	}

	public static int delete(int cdContrato, int cdDesconto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_desconto WHERE cd_contrato=? AND cd_desconto=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdDesconto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoDesconto get(int cdContrato, int cdDesconto) {
		return get(cdContrato, cdDesconto, null);
	}

	public static ContratoDesconto get(int cdContrato, int cdDesconto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_desconto WHERE cd_contrato=? AND cd_desconto=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdDesconto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoDesconto(rs.getInt("cd_contrato"),
						rs.getInt("cd_desconto"),
						rs.getInt("cd_tipo_desconto"),
						rs.getInt("cd_faixa_desconto"),
						rs.getInt("cd_empresa"),
						(rs.getTimestamp("dt_inclusao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inclusao").getTime()),
						rs.getFloat("pr_desconto"),
						rs.getInt("cd_movimento_fidelidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_desconto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDescontoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_desconto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
