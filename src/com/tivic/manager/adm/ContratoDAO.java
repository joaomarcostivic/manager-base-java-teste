package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContratoDAO{

	public static int insert(Contrato objeto) {
		return insert(objeto, null);
	}

	public static int insert(Contrato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_contrato", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContrato(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato (cd_contrato,"+
			                                  "cd_convenio,"+
			                                  "cd_categoria_parcelas,"+
			                                  "cd_empresa,"+
			                                  "cd_pessoa,"+
			                                  "cd_modelo_contrato,"+
			                                  "cd_indicador,"+
			                                  "dt_assinatura,"+
			                                  "dt_primeira_parcela,"+
			                                  "nr_dia_vencimento,"+
			                                  "nr_parcelas,"+
			                                  "pr_juros_mora,"+
			                                  "pr_multa_mora,"+
			                                  "pr_desconto_adimplencia,"+
			                                  "pr_desconto,"+
			                                  "tp_contrato,"+
			                                  "vl_parcelas,"+
			                                  "vl_adesao,"+
			                                  "vl_contrato,"+
			                                  "nr_contrato,"+
			                                  "txt_contrato,"+
			                                  "st_contrato,"+
			                                  "id_contrato,"+
			                                  "dt_inicio_vigencia,"+
			                                  "dt_final_vigencia,"+
			                                  "cd_agente,"+
			                                  "cd_conta_carteira,"+
			                                  "cd_conta,"+
			                                  "tp_amortizacao,"+
			                                  "gn_contrato,"+
			                                  "pr_juros,"+
			                                  "cd_tipo_operacao,"+
			                                  "cd_documento,"+
			                                  "tp_desconto,"+
			                                  "vl_desconto,"+
			                                  "cd_contrato_origem,"+
			                                  "txt_observacao,"+
			                                  "cd_categoria_adesao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConvenio());
			if(objeto.getCdCategoriaParcelas()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaParcelas());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdModeloContrato()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdModeloContrato());
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdIndicador());
			if(objeto.getDtAssinatura()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtAssinatura().getTimeInMillis()));
			if(objeto.getDtPrimeiraParcela()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrimeiraParcela().getTimeInMillis()));
			pstmt.setInt(10,objeto.getNrDiaVencimento());
			pstmt.setInt(11,objeto.getNrParcelas());
			pstmt.setFloat(12,objeto.getPrJurosMora());
			pstmt.setFloat(13,objeto.getPrMultaMora());
			pstmt.setFloat(14,objeto.getPrDescontoAdimplencia());
			pstmt.setFloat(15,objeto.getPrDesconto());
			pstmt.setInt(16,objeto.getTpContrato());
			pstmt.setFloat(17,objeto.getVlParcelas());
			pstmt.setFloat(18,objeto.getVlAdesao());
			pstmt.setFloat(19,objeto.getVlContrato());
			pstmt.setString(20,objeto.getNrContrato());
			pstmt.setString(21,objeto.getTxtContrato());
			pstmt.setInt(22,objeto.getStContrato());
			pstmt.setString(23,objeto.getIdContrato());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			if(objeto.getCdAgente()==0)
				pstmt.setNull(26, Types.INTEGER);
			else
				pstmt.setInt(26,objeto.getCdAgente());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdConta());
			pstmt.setInt(29,objeto.getTpAmortizacao());
			pstmt.setInt(30,objeto.getGnContrato());
			pstmt.setFloat(31,objeto.getPrJuros());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTipoOperacao());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdDocumento());
			pstmt.setInt(34,objeto.getTpDesconto());
			pstmt.setFloat(35,objeto.getVlDesconto());
			if(objeto.getCdContratoOrigem()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdContratoOrigem());
			pstmt.setString(37,objeto.getTxtObservacao());
			if(objeto.getCdCategoriaAdesao()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdCategoriaAdesao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Contrato objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Contrato objeto, int cdContratoOld) {
		return update(objeto, cdContratoOld, null);
	}

	public static int update(Contrato objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Contrato objeto, int cdContratoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato SET cd_contrato=?,"+
												      		   "cd_convenio=?,"+
												      		   "cd_categoria_parcelas=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_modelo_contrato=?,"+
												      		   "cd_indicador=?,"+
												      		   "dt_assinatura=?,"+
												      		   "dt_primeira_parcela=?,"+
												      		   "nr_dia_vencimento=?,"+
												      		   "nr_parcelas=?,"+
												      		   "pr_juros_mora=?,"+
												      		   "pr_multa_mora=?,"+
												      		   "pr_desconto_adimplencia=?,"+
												      		   "pr_desconto=?,"+
												      		   "tp_contrato=?,"+
												      		   "vl_parcelas=?,"+
												      		   "vl_adesao=?,"+
												      		   "vl_contrato=?,"+
												      		   "nr_contrato=?,"+
												      		   "txt_contrato=?,"+
												      		   "st_contrato=?,"+
												      		   "id_contrato=?,"+
												      		   "dt_inicio_vigencia=?,"+
												      		   "dt_final_vigencia=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_conta_carteira=?,"+
												      		   "cd_conta=?,"+
												      		   "tp_amortizacao=?,"+
												      		   "gn_contrato=?,"+
												      		   "pr_juros=?,"+
												      		   "cd_tipo_operacao=?,"+
												      		   "cd_documento=?,"+
												      		   "tp_desconto=?,"+
												      		   "vl_desconto=?,"+
												      		   "cd_contrato_origem=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_categoria_adesao=? WHERE cd_contrato=?");
			pstmt.setInt(1,objeto.getCdContrato());
			if(objeto.getCdConvenio()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConvenio());
			if(objeto.getCdCategoriaParcelas()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaParcelas());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPessoa());
			if(objeto.getCdModeloContrato()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdModeloContrato());
			if(objeto.getCdIndicador()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdIndicador());
			if(objeto.getDtAssinatura()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtAssinatura().getTimeInMillis()));
			if(objeto.getDtPrimeiraParcela()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtPrimeiraParcela().getTimeInMillis()));
			pstmt.setInt(10,objeto.getNrDiaVencimento());
			pstmt.setInt(11,objeto.getNrParcelas());
			pstmt.setFloat(12,objeto.getPrJurosMora());
			pstmt.setFloat(13,objeto.getPrMultaMora());
			pstmt.setFloat(14,objeto.getPrDescontoAdimplencia());
			pstmt.setFloat(15,objeto.getPrDesconto());
			pstmt.setInt(16,objeto.getTpContrato());
			pstmt.setFloat(17,objeto.getVlParcelas());
			pstmt.setFloat(18,objeto.getVlAdesao());
			pstmt.setFloat(19,objeto.getVlContrato());
			pstmt.setString(20,objeto.getNrContrato());
			pstmt.setString(21,objeto.getTxtContrato());
			pstmt.setInt(22,objeto.getStContrato());
			pstmt.setString(23,objeto.getIdContrato());
			if(objeto.getDtInicioVigencia()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtInicioVigencia().getTimeInMillis()));
			if(objeto.getDtFinalVigencia()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtFinalVigencia().getTimeInMillis()));
			if(objeto.getCdAgente()==0)
				pstmt.setNull(26, Types.INTEGER);
			else
				pstmt.setInt(26,objeto.getCdAgente());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdConta());
			pstmt.setInt(29,objeto.getTpAmortizacao());
			pstmt.setInt(30,objeto.getGnContrato());
			pstmt.setFloat(31,objeto.getPrJuros());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTipoOperacao());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdDocumento());
			pstmt.setInt(34,objeto.getTpDesconto());
			pstmt.setFloat(35,objeto.getVlDesconto());
			if(objeto.getCdContratoOrigem()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdContratoOrigem());
			pstmt.setString(37,objeto.getTxtObservacao());
			if(objeto.getCdCategoriaAdesao()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdCategoriaAdesao());
			pstmt.setInt(39, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContrato) {
		return delete(cdContrato, null);
	}

	public static int delete(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Contrato get(int cdContrato) {
		return get(cdContrato, null);
	}

	public static Contrato get(int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato WHERE cd_contrato=?");
			pstmt.setInt(1, cdContrato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Contrato(rs.getInt("cd_contrato"),
						rs.getInt("cd_convenio"),
						rs.getInt("cd_categoria_parcelas"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_modelo_contrato"),
						rs.getInt("cd_indicador"),
						(rs.getTimestamp("dt_assinatura")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_assinatura").getTime()),
						(rs.getTimestamp("dt_primeira_parcela")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeira_parcela").getTime()),
						rs.getInt("nr_dia_vencimento"),
						rs.getInt("nr_parcelas"),
						rs.getFloat("pr_juros_mora"),
						rs.getFloat("pr_multa_mora"),
						rs.getFloat("pr_desconto_adimplencia"),
						rs.getFloat("pr_desconto"),
						rs.getInt("tp_contrato"),
						rs.getFloat("vl_parcelas"),
						rs.getFloat("vl_adesao"),
						rs.getFloat("vl_contrato"),
						rs.getString("nr_contrato"),
						rs.getString("txt_contrato"),
						rs.getInt("st_contrato"),
						rs.getString("id_contrato"),
						(rs.getTimestamp("dt_inicio_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_vigencia").getTime()),
						(rs.getTimestamp("dt_final_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_vigencia").getTime()),
						rs.getInt("cd_agente"),
						rs.getInt("cd_conta_carteira"),
						rs.getInt("cd_conta"),
						rs.getInt("tp_amortizacao"),
						rs.getInt("gn_contrato"),
						rs.getFloat("pr_juros"),
						rs.getInt("cd_tipo_operacao"),
						rs.getInt("cd_documento"),
						rs.getInt("tp_desconto"),
						rs.getFloat("vl_desconto"),
						rs.getInt("cd_contrato_origem"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_categoria_adesao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
