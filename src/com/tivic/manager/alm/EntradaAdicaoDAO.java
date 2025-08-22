package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class EntradaAdicaoDAO{

	public static int insert(EntradaAdicao objeto) {
		return insert(objeto, null);
	}

	public static int insert(EntradaAdicao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_entrada_adicao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_entrada_declaracao_importacao");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEntradaDeclaracaoImportacao()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_documento_entrada");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdDocumentoEntrada()));
			int code = Conexao.getSequenceCode("alm_entrada_adicao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEntradaAdicao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_entrada_adicao (cd_entrada_adicao,"+
			                                  "cd_entrada_declaracao_importacao,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_ncm,"+
			                                  "qt_itens,"+
			                                  "vl_total,"+
			                                  "vl_base_calculo_icms,"+
			                                  "pr_aliquota_icms,"+
			                                  "vl_base_calculo_ipi,"+
			                                  "pr_aliquota_ipi,"+
			                                  "vl_base_calculo_pis,"+
			                                  "pr_aliquota_pis,"+
			                                  "vl_base_calculo_cofins,"+
			                                  "pr_aliquota_cofins,"+
			                                  "vl_base_calculo_ii,"+
			                                  "pr_aliquota_ii,"+
			                                  "vl_base_calculo_anti_dumping,"+
			                                  "pr_aliquota_anti_dumping) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEntradaDeclaracaoImportacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEntradaDeclaracaoImportacao());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoEntrada());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNcm());
			pstmt.setFloat(5,objeto.getQtItens());
			pstmt.setFloat(6,objeto.getVlTotal());
			pstmt.setFloat(7,objeto.getVlBaseCalculoIcms());
			pstmt.setFloat(8,objeto.getPrAliquotaIcms());
			pstmt.setFloat(9,objeto.getVlBaseCalculoIpi());
			pstmt.setFloat(10,objeto.getPrAliquotaIpi());
			pstmt.setFloat(11,objeto.getVlBaseCalculoPis());
			pstmt.setFloat(12,objeto.getPrAliquotaPis());
			pstmt.setFloat(13,objeto.getVlBaseCalculoCofins());
			pstmt.setFloat(14,objeto.getPrAliquotaCofins());
			pstmt.setFloat(15,objeto.getVlBaseCalculoIi());
			pstmt.setFloat(16,objeto.getPrAliquotaIi());
			pstmt.setFloat(17,objeto.getVlBaseCalculoAntiDumping());
			pstmt.setFloat(18,objeto.getPrAliquotaAntiDumping());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EntradaAdicao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(EntradaAdicao objeto, int cdEntradaAdicaoOld, int cdEntradaDeclaracaoImportacaoOld, int cdDocumentoEntradaOld) {
		return update(objeto, cdEntradaAdicaoOld, cdEntradaDeclaracaoImportacaoOld, cdDocumentoEntradaOld, null);
	}

	public static int update(EntradaAdicao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(EntradaAdicao objeto, int cdEntradaAdicaoOld, int cdEntradaDeclaracaoImportacaoOld, int cdDocumentoEntradaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_entrada_adicao SET cd_entrada_adicao=?,"+
												      		   "cd_entrada_declaracao_importacao=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_ncm=?,"+
												      		   "qt_itens=?,"+
												      		   "vl_total=?,"+
												      		   "vl_base_calculo_icms=?,"+
												      		   "pr_aliquota_icms=?,"+
												      		   "vl_base_calculo_ipi=?,"+
												      		   "pr_aliquota_ipi=?,"+
												      		   "vl_base_calculo_pis=?,"+
												      		   "pr_aliquota_pis=?,"+
												      		   "vl_base_calculo_cofins=?,"+
												      		   "pr_aliquota_cofins=?,"+
												      		   "vl_base_calculo_ii=?,"+
												      		   "pr_aliquota_ii=?,"+
												      		   "vl_base_calculo_anti_dumping=?,"+
												      		   "pr_aliquota_anti_dumping=? WHERE cd_entrada_adicao=? AND cd_entrada_declaracao_importacao=? AND cd_documento_entrada=?");
			pstmt.setInt(1,objeto.getCdEntradaAdicao());
			pstmt.setInt(2,objeto.getCdEntradaDeclaracaoImportacao());
			pstmt.setInt(3,objeto.getCdDocumentoEntrada());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNcm());
			pstmt.setFloat(5,objeto.getQtItens());
			pstmt.setFloat(6,objeto.getVlTotal());
			pstmt.setFloat(7,objeto.getVlBaseCalculoIcms());
			pstmt.setFloat(8,objeto.getPrAliquotaIcms());
			pstmt.setFloat(9,objeto.getVlBaseCalculoIpi());
			pstmt.setFloat(10,objeto.getPrAliquotaIpi());
			pstmt.setFloat(11,objeto.getVlBaseCalculoPis());
			pstmt.setFloat(12,objeto.getPrAliquotaPis());
			pstmt.setFloat(13,objeto.getVlBaseCalculoCofins());
			pstmt.setFloat(14,objeto.getPrAliquotaCofins());
			pstmt.setFloat(15,objeto.getVlBaseCalculoIi());
			pstmt.setFloat(16,objeto.getPrAliquotaIi());
			pstmt.setFloat(17,objeto.getVlBaseCalculoAntiDumping());
			pstmt.setFloat(18,objeto.getPrAliquotaAntiDumping());
			pstmt.setInt(19, cdEntradaAdicaoOld!=0 ? cdEntradaAdicaoOld : objeto.getCdEntradaAdicao());
			pstmt.setInt(20, cdEntradaDeclaracaoImportacaoOld!=0 ? cdEntradaDeclaracaoImportacaoOld : objeto.getCdEntradaDeclaracaoImportacao());
			pstmt.setInt(21, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEntradaAdicao, int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada) {
		return delete(cdEntradaAdicao, cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, null);
	}

	public static int delete(int cdEntradaAdicao, int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_entrada_adicao WHERE cd_entrada_adicao=? AND cd_entrada_declaracao_importacao=? AND cd_documento_entrada=?");
			pstmt.setInt(1, cdEntradaAdicao);
			pstmt.setInt(2, cdEntradaDeclaracaoImportacao);
			pstmt.setInt(3, cdDocumentoEntrada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EntradaAdicao get(int cdEntradaAdicao, int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada) {
		return get(cdEntradaAdicao, cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, null);
	}

	public static EntradaAdicao get(int cdEntradaAdicao, int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_adicao WHERE cd_entrada_adicao=? AND cd_entrada_declaracao_importacao=? AND cd_documento_entrada=?");
			pstmt.setInt(1, cdEntradaAdicao);
			pstmt.setInt(2, cdEntradaDeclaracaoImportacao);
			pstmt.setInt(3, cdDocumentoEntrada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EntradaAdicao(rs.getInt("cd_entrada_adicao"),
						rs.getInt("cd_entrada_declaracao_importacao"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_ncm"),
						rs.getFloat("qt_itens"),
						rs.getFloat("vl_total"),
						rs.getFloat("vl_base_calculo_icms"),
						rs.getFloat("pr_aliquota_icms"),
						rs.getFloat("vl_base_calculo_ipi"),
						rs.getFloat("pr_aliquota_ipi"),
						rs.getFloat("vl_base_calculo_pis"),
						rs.getFloat("pr_aliquota_pis"),
						rs.getFloat("vl_base_calculo_cofins"),
						rs.getFloat("pr_aliquota_cofins"),
						rs.getFloat("vl_base_calculo_ii"),
						rs.getFloat("pr_aliquota_ii"),
						rs.getFloat("vl_base_calculo_anti_dumping"),
						rs.getFloat("pr_aliquota_anti_dumping"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_adicao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EntradaAdicao> getList() {
		return getList(null);
	}

	public static ArrayList<EntradaAdicao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EntradaAdicao> list = new ArrayList<EntradaAdicao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EntradaAdicao obj = EntradaAdicaoDAO.get(rsm.getInt("cd_entrada_adicao"), rsm.getInt("cd_entrada_declaracao_importacao"), rsm.getInt("cd_documento_entrada"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaAdicaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM alm_entrada_adicao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
