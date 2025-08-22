package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoNegociacaoDAO{

	public static int insert(ContratoNegociacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContratoNegociacao objeto, Connection connect){
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
			keys[1].put("FIELD_NAME", "cd_negociacao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_contrato_negociacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNegociacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_negociacao (cd_contrato,"+
			                                  "cd_negociacao,"+
			                                  "dt_negociacao,"+
			                                  "cd_usuario,"+
			                                  "cd_responsavel,"+
			                                  "cd_documento_saida,"+
			                                  "pr_multa_mora,"+
			                                  "pr_juros_mora,"+
			                                  "vl_multa_mora,"+
			                                  "vl_juros_mora,"+
			                                  "pr_multa_penal,"+
			                                  "vl_desconto,"+
			                                  "vl_acrescimo,"+
			                                  "qt_parcelas,"+
			                                  "dt_primeiro_vencimento,"+
			                                  "nr_dia_vencimento,"+
			                                  "vl_parcela,"+
			                                  "vl_final,"+
			                                  "pr_carencia_anterior,"+
			                                  "vl_carencia,"+
			                                  "vl_pago,"+
			                                  "txt_observacao,"+
			                                  "vl_multa_penal,"+
			                                  "vl_corrido_contrato,"+
			                                  "st_negociacao,"+
			                                  "tp_negociacao,"+
			                                  "lg_carencia,"+
			                                  "pr_corrido_contrato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdContrato()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2, code);
			if(objeto.getDtNegociacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtNegociacao().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoSaida());
			pstmt.setFloat(7,objeto.getPrMultaMora());
			pstmt.setFloat(8,objeto.getPrJurosMora());
			pstmt.setFloat(9,objeto.getVlMultaMora());
			pstmt.setFloat(10,objeto.getVlJurosMora());
			pstmt.setFloat(11,objeto.getPrMultaPenal());
			pstmt.setFloat(12,objeto.getVlDesconto());
			pstmt.setFloat(13,objeto.getVlAcrescimo());
			pstmt.setInt(14,objeto.getQtParcelas());
			if(objeto.getDtPrimeiroVencimento()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtPrimeiroVencimento().getTimeInMillis()));
			pstmt.setInt(16,objeto.getNrDiaVencimento());
			pstmt.setFloat(17,objeto.getVlParcela());
			pstmt.setFloat(18,objeto.getVlFinal());
			pstmt.setFloat(19,objeto.getPrCarenciaAnterior());
			pstmt.setFloat(20,objeto.getVlCarencia());
			pstmt.setFloat(21,objeto.getVlPago());
			pstmt.setString(22,objeto.getTxtObservacao());
			pstmt.setFloat(23,objeto.getVlMultaPenal());
			pstmt.setFloat(24,objeto.getVlCorridoContrato());
			pstmt.setInt(25,objeto.getStNegociacao());
			pstmt.setInt(26,objeto.getTpNegociacao());
			pstmt.setInt(27,objeto.getLgCarencia());
			pstmt.setFloat(28,objeto.getPrCorridoContrato());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoNegociacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoNegociacao objeto, int cdContratoOld, int cdNegociacaoOld) {
		return update(objeto, cdContratoOld, cdNegociacaoOld, null);
	}

	public static int update(ContratoNegociacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoNegociacao objeto, int cdContratoOld, int cdNegociacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_negociacao SET cd_contrato=?,"+
												      		   "cd_negociacao=?,"+
												      		   "dt_negociacao=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "pr_multa_mora=?,"+
												      		   "pr_juros_mora=?,"+
												      		   "vl_multa_mora=?,"+
												      		   "vl_juros_mora=?,"+
												      		   "pr_multa_penal=?,"+
												      		   "vl_desconto=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "qt_parcelas=?,"+
												      		   "dt_primeiro_vencimento=?,"+
												      		   "nr_dia_vencimento=?,"+
												      		   "vl_parcela=?,"+
												      		   "vl_final=?,"+
												      		   "pr_carencia_anterior=?,"+
												      		   "vl_carencia=?,"+
												      		   "vl_pago=?,"+
												      		   "txt_observacao=?,"+
												      		   "vl_multa_penal=?,"+
												      		   "vl_corrido_contrato=?,"+
												      		   "st_negociacao=?,"+
												      		   "tp_negociacao=?,"+
												      		   "lg_carencia=?,"+
												      		   "pr_corrido_contrato=? WHERE cd_contrato=? AND cd_negociacao=?");
			pstmt.setInt(1,objeto.getCdContrato());
			pstmt.setInt(2,objeto.getCdNegociacao());
			if(objeto.getDtNegociacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtNegociacao().getTimeInMillis()));
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoSaida());
			pstmt.setFloat(7,objeto.getPrMultaMora());
			pstmt.setFloat(8,objeto.getPrJurosMora());
			pstmt.setFloat(9,objeto.getVlMultaMora());
			pstmt.setFloat(10,objeto.getVlJurosMora());
			pstmt.setFloat(11,objeto.getPrMultaPenal());
			pstmt.setFloat(12,objeto.getVlDesconto());
			pstmt.setFloat(13,objeto.getVlAcrescimo());
			pstmt.setInt(14,objeto.getQtParcelas());
			if(objeto.getDtPrimeiroVencimento()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtPrimeiroVencimento().getTimeInMillis()));
			pstmt.setInt(16,objeto.getNrDiaVencimento());
			pstmt.setFloat(17,objeto.getVlParcela());
			pstmt.setFloat(18,objeto.getVlFinal());
			pstmt.setFloat(19,objeto.getPrCarenciaAnterior());
			pstmt.setFloat(20,objeto.getVlCarencia());
			pstmt.setFloat(21,objeto.getVlPago());
			pstmt.setString(22,objeto.getTxtObservacao());
			pstmt.setFloat(23,objeto.getVlMultaPenal());
			pstmt.setFloat(24,objeto.getVlCorridoContrato());
			pstmt.setInt(25,objeto.getStNegociacao());
			pstmt.setInt(26,objeto.getTpNegociacao());
			pstmt.setInt(27,objeto.getLgCarencia());
			pstmt.setFloat(28,objeto.getPrCorridoContrato());
			pstmt.setInt(29, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.setInt(30, cdNegociacaoOld!=0 ? cdNegociacaoOld : objeto.getCdNegociacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato, int cdNegociacao) {
		return delete(cdContrato, cdNegociacao, null);
	}

	public static int delete(int cdContrato, int cdNegociacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_negociacao WHERE cd_contrato=? AND cd_negociacao=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoNegociacao get(int cdContrato, int cdNegociacao) {
		return get(cdContrato, cdNegociacao, null);
	}

	public static ContratoNegociacao get(int cdContrato, int cdNegociacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_negociacao WHERE cd_contrato=? AND cd_negociacao=?");
			pstmt.setInt(1, cdContrato);
			pstmt.setInt(2, cdNegociacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoNegociacao(rs.getInt("cd_contrato"),
						rs.getInt("cd_negociacao"),
						(rs.getTimestamp("dt_negociacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_negociacao").getTime()),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_documento_saida"),
						rs.getFloat("pr_multa_mora"),
						rs.getFloat("pr_juros_mora"),
						rs.getFloat("vl_multa_mora"),
						rs.getFloat("vl_juros_mora"),
						rs.getFloat("pr_multa_penal"),
						rs.getFloat("vl_desconto"),
						rs.getFloat("vl_acrescimo"),
						rs.getInt("qt_parcelas"),
						(rs.getTimestamp("dt_primeiro_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeiro_vencimento").getTime()),
						rs.getInt("nr_dia_vencimento"),
						rs.getFloat("vl_parcela"),
						rs.getFloat("vl_final"),
						rs.getFloat("pr_carencia_anterior"),
						rs.getFloat("vl_carencia"),
						rs.getFloat("vl_pago"),
						rs.getString("txt_observacao"),
						rs.getFloat("vl_multa_penal"),
						rs.getFloat("vl_corrido_contrato"),
						rs.getInt("st_negociacao"),
						rs.getInt("tp_negociacao"),
						rs.getInt("lg_carencia"),
						rs.getFloat("pr_corrido_contrato"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_negociacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoNegociacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_negociacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
