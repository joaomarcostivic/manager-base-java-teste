package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class AcertoConsignacaoEntradaDAO{

	public static int insert(AcertoConsignacaoEntrada objeto) {
		return insert(objeto, null);
	}

	public static int insert(AcertoConsignacaoEntrada objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_acerto_consignacao_entrada", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAcertoConsignacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_acerto_consignacao_entrada (cd_acerto_consignacao,"+
			                                  "dt_acerto,"+
			                                  "st_acerto,"+
			                                  "tp_acerto,"+
			                                  "cd_fornecedor,"+
			                                  "dt_emissao,"+
			                                  "dt_vencimento,"+
			                                  "cd_empresa,"+
			                                  "cd_documento_saida,"+
			                                  "cd_documento_entrada,"+
			                                  "id_acerto_consignacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtAcerto()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtAcerto().getTimeInMillis()));
			pstmt.setInt(3,objeto.getStAcerto());
			pstmt.setInt(4,objeto.getTpAcerto());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFornecedor());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdDocumentoSaida());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdDocumentoEntrada());
			pstmt.setString(11,objeto.getIdAcertoConsignacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AcertoConsignacaoEntrada objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AcertoConsignacaoEntrada objeto, int cdAcertoConsignacaoOld) {
		return update(objeto, cdAcertoConsignacaoOld, null);
	}

	public static int update(AcertoConsignacaoEntrada objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AcertoConsignacaoEntrada objeto, int cdAcertoConsignacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_acerto_consignacao_entrada SET cd_acerto_consignacao=?,"+
												      		   "dt_acerto=?,"+
												      		   "st_acerto=?,"+
												      		   "tp_acerto=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "dt_emissao=?,"+
												      		   "dt_vencimento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "id_acerto_consignacao=? WHERE cd_acerto_consignacao=?");
			pstmt.setInt(1,objeto.getCdAcertoConsignacao());
			if(objeto.getDtAcerto()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtAcerto().getTimeInMillis()));
			pstmt.setInt(3,objeto.getStAcerto());
			pstmt.setInt(4,objeto.getTpAcerto());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFornecedor());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdDocumentoSaida());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdDocumentoEntrada());
			pstmt.setString(11,objeto.getIdAcertoConsignacao());
			pstmt.setInt(12, cdAcertoConsignacaoOld!=0 ? cdAcertoConsignacaoOld : objeto.getCdAcertoConsignacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAcertoConsignacao) {
		return delete(cdAcertoConsignacao, null);
	}

	public static int delete(int cdAcertoConsignacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_acerto_consignacao_entrada WHERE cd_acerto_consignacao=?");
			pstmt.setInt(1, cdAcertoConsignacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AcertoConsignacaoEntrada get(int cdAcertoConsignacao) {
		return get(cdAcertoConsignacao, null);
	}

	public static AcertoConsignacaoEntrada get(int cdAcertoConsignacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_acerto_consignacao_entrada WHERE cd_acerto_consignacao=?");
			pstmt.setInt(1, cdAcertoConsignacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AcertoConsignacaoEntrada(rs.getInt("cd_acerto_consignacao"),
						(rs.getTimestamp("dt_acerto")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_acerto").getTime()),
						rs.getInt("st_acerto"),
						rs.getInt("tp_acerto"),
						rs.getInt("cd_fornecedor"),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_documento_entrada"),
						rs.getString("id_acerto_consignacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_acerto_consignacao_entrada");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntradaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT * FROM alm_acerto_consignacao_entrada", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
