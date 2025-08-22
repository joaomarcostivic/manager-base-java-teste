package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaCarteiraDAO{

	public static int insert(ContaCarteira objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(ContaCarteira objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_conta_carteira");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_conta");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdConta()));
			int code = Conexao.getSequenceCode("adm_conta_carteira", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaCarteira(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_carteira (cd_conta_carteira,"+
			                                  "cd_conta,"+
			                                  "nm_carteira,"+
			                                  "sg_carteira,"+
			                                  "nm_local_pagamento,"+
			                                  "nm_moeda,"+
			                                  "nm_aceite,"+
			                                  "tp_digito,"+
			                                  "txt_mensagem,"+
			                                  "vl_despesa_cobranca,"+
			                                  "nr_cedente,"+
			                                  "tp_cobranca,"+
			                                  "pr_juros,"+
			                                  "pr_multa,"+
			                                  "pr_desconto_adimplencia,"+
			                                  "qt_dias_devolucao,"+
			                                  "qt_dias_desconto,"+
			                                  "qt_dias_multa,"+
			                                  "qt_digitos_numero,"+
			                                  "nr_digitos_inicio,"+
			                                  "img_logotipo,"+
			                                  "qt_dias_protesto,"+
			                                  "nr_carteira,"+
			                                  "id_carteira,"+
			                                  "nr_base_inicial,"+
			                                  "nr_base_final,"+
			                                  "nr_convenio,"+
			                                  "tp_arquivo_edi,"+
			                                  "nr_servico,"+
			                                  "txt_campo_livre) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConta());
			pstmt.setString(3,objeto.getNmCarteira());
			pstmt.setString(4,objeto.getSgCarteira());
			pstmt.setString(5,objeto.getNmLocalPagamento());
			pstmt.setString(6,objeto.getNmMoeda());
			pstmt.setString(7,objeto.getNmAceite());
			pstmt.setInt(8,objeto.getTpDigito());
			pstmt.setString(9,objeto.getTxtMensagem());
			pstmt.setFloat(10,objeto.getVlDespesaCobranca());
			pstmt.setString(11,objeto.getNrCedente());
			pstmt.setInt(12,objeto.getTpCobranca());
			pstmt.setFloat(13,objeto.getPrJuros());
			pstmt.setFloat(14,objeto.getPrMulta());
			pstmt.setFloat(15,objeto.getPrDescontoAdimplencia());
			pstmt.setInt(16,objeto.getQtDiasDevolucao());
			pstmt.setInt(17,objeto.getQtDiasDesconto());
			pstmt.setInt(18,objeto.getQtDiasMulta());
			pstmt.setInt(19,objeto.getQtDigitosNumero());
			pstmt.setString(20,objeto.getNrDigitosInicio());
			if(objeto.getImgLogotipo()==null)
				pstmt.setNull(21, Types.BINARY);
			else
				pstmt.setBytes(21,objeto.getImgLogotipo());
			pstmt.setInt(22,objeto.getQtDiasProtesto());
			pstmt.setString(23,objeto.getNrCarteira());
			pstmt.setString(24,objeto.getIdCarteira());
			pstmt.setInt(25,objeto.getNrBaseInicial());
			pstmt.setInt(26,objeto.getNrBaseFinal());
			pstmt.setString(27,objeto.getNrConvenio());
			pstmt.setInt(28,objeto.getTpArquivoEdi());
			pstmt.setString(29,objeto.getNrServico());
			pstmt.setString(30,objeto.getTxtCampoLivre());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaCarteira objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContaCarteira objeto, int cdContaCarteiraOld, int cdContaOld) {
		return update(objeto, cdContaCarteiraOld, cdContaOld, null);
	}

	public static int update(ContaCarteira objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContaCarteira objeto, int cdContaCarteiraOld, int cdContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_carteira SET cd_conta_carteira=?,"+
												      		   "cd_conta=?,"+
												      		   "nm_carteira=?,"+
												      		   "sg_carteira=?,"+
												      		   "nm_local_pagamento=?,"+
												      		   "nm_moeda=?,"+
												      		   "nm_aceite=?,"+
												      		   "tp_digito=?,"+
												      		   "txt_mensagem=?,"+
												      		   "vl_despesa_cobranca=?,"+
												      		   "nr_cedente=?,"+
												      		   "tp_cobranca=?,"+
												      		   "pr_juros=?,"+
												      		   "pr_multa=?,"+
												      		   "pr_desconto_adimplencia=?,"+
												      		   "qt_dias_devolucao=?,"+
												      		   "qt_dias_desconto=?,"+
												      		   "qt_dias_multa=?,"+
												      		   "qt_digitos_numero=?,"+
												      		   "nr_digitos_inicio=?,"+
												      		   "img_logotipo=?,"+
												      		   "qt_dias_protesto=?,"+
												      		   "nr_carteira=?,"+
												      		   "id_carteira=?,"+
												      		   "nr_base_inicial=?,"+
												      		   "nr_base_final=?,"+
												      		   "nr_convenio=?,"+
												      		   "tp_arquivo_edi=?,"+
												      		   "nr_servico=?,"+
												      		   "txt_campo_livre=? WHERE cd_conta_carteira=? AND cd_conta=?");
			pstmt.setInt(1,objeto.getCdContaCarteira());
			pstmt.setInt(2,objeto.getCdConta());
			pstmt.setString(3,objeto.getNmCarteira());
			pstmt.setString(4,objeto.getSgCarteira());
			pstmt.setString(5,objeto.getNmLocalPagamento());
			pstmt.setString(6,objeto.getNmMoeda());
			pstmt.setString(7,objeto.getNmAceite());
			pstmt.setInt(8,objeto.getTpDigito());
			pstmt.setString(9,objeto.getTxtMensagem());
			pstmt.setFloat(10,objeto.getVlDespesaCobranca());
			pstmt.setString(11,objeto.getNrCedente());
			pstmt.setInt(12,objeto.getTpCobranca());
			pstmt.setFloat(13,objeto.getPrJuros());
			pstmt.setFloat(14,objeto.getPrMulta());
			pstmt.setFloat(15,objeto.getPrDescontoAdimplencia());
			pstmt.setInt(16,objeto.getQtDiasDevolucao());
			pstmt.setInt(17,objeto.getQtDiasDesconto());
			pstmt.setInt(18,objeto.getQtDiasMulta());
			pstmt.setInt(19,objeto.getQtDigitosNumero());
			pstmt.setString(20,objeto.getNrDigitosInicio());
			if(objeto.getImgLogotipo()==null)
				pstmt.setNull(21, Types.BINARY);
			else
				pstmt.setBytes(21,objeto.getImgLogotipo());
			pstmt.setInt(22,objeto.getQtDiasProtesto());
			pstmt.setString(23,objeto.getNrCarteira());
			pstmt.setString(24,objeto.getIdCarteira());
			pstmt.setInt(25,objeto.getNrBaseInicial());
			pstmt.setInt(26,objeto.getNrBaseFinal());
			pstmt.setString(27,objeto.getNrConvenio());
			pstmt.setInt(28,objeto.getTpArquivoEdi());
			pstmt.setString(29,objeto.getNrServico());
			pstmt.setString(30,objeto.getTxtCampoLivre());
			pstmt.setInt(31, cdContaCarteiraOld!=0 ? cdContaCarteiraOld : objeto.getCdContaCarteira());
			pstmt.setInt(32, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaCarteira, int cdConta) {
		return delete(cdContaCarteira, cdConta, null);
	}

	public static int delete(int cdContaCarteira, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_carteira WHERE cd_conta_carteira=? AND cd_conta=?");
			pstmt.setInt(1, cdContaCarteira);
			pstmt.setInt(2, cdConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaCarteira get(int cdContaCarteira, int cdConta) {
		return get(cdContaCarteira, cdConta, null);
	}

	public static ContaCarteira get(int cdContaCarteira, int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_carteira WHERE cd_conta_carteira=? AND cd_conta=?");
			pstmt.setInt(1, cdContaCarteira);
			pstmt.setInt(2, cdConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaCarteira(rs.getInt("cd_conta_carteira"),
						rs.getInt("cd_conta"),
						rs.getString("nm_carteira"),
						rs.getString("sg_carteira"),
						rs.getString("nm_local_pagamento"),
						rs.getString("nm_moeda"),
						rs.getString("nm_aceite"),
						rs.getInt("tp_digito"),
						rs.getString("txt_mensagem"),
						rs.getFloat("vl_despesa_cobranca"),
						rs.getString("nr_cedente"),
						rs.getInt("tp_cobranca"),
						rs.getFloat("pr_juros"),
						rs.getFloat("pr_multa"),
						rs.getFloat("pr_desconto_adimplencia"),
						rs.getInt("qt_dias_devolucao"),
						rs.getInt("qt_dias_desconto"),
						rs.getInt("qt_dias_multa"),
						rs.getInt("qt_digitos_numero"),
						rs.getString("nr_digitos_inicio"),
						rs.getBytes("img_logotipo")==null?null:rs.getBytes("img_logotipo"),
						rs.getInt("qt_dias_protesto"),
						rs.getString("nr_carteira"),
						rs.getString("id_carteira"),
						rs.getInt("nr_base_inicial"),
						rs.getInt("nr_base_final"),
						rs.getString("nr_convenio"),
						rs.getInt("tp_arquivo_edi"),
						rs.getString("nr_servico"),
						rs.getString("txt_campo_livre"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_carteira");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaCarteiraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_carteira", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
