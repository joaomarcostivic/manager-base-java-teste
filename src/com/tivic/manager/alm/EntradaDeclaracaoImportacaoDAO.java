package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class EntradaDeclaracaoImportacaoDAO{

	public static int insert(EntradaDeclaracaoImportacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(EntradaDeclaracaoImportacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_entrada_declaracao_importacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_documento_entrada");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdDocumentoEntrada()));
			int code = Conexao.getSequenceCode("alm_entrada_declaracao_importacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEntradaDeclaracaoImportacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_entrada_declaracao_importacao (cd_entrada_declaracao_importacao,"+
			                                  "cd_documento_entrada,"+
			                                  "nr_declaracao_importacao,"+
			                                  "dt_registro,"+
			                                  "nm_local,"+
			                                  "sg_uf_desembaraco,"+
			                                  "dt_desembaraco,"+
			                                  "qt_adicao,"+
			                                  "vl_taxa_dolar,"+
			                                  "tp_via_transporte,"+
			                                  "tp_intermedio,"+
			                                  "nr_cnpj_intermediario,"+
			                                  "cd_estado_intermediario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			pstmt.setString(3,objeto.getNrDeclaracaoImportacao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmLocal());
			pstmt.setString(6,objeto.getSgUfDesembaraco());
			if(objeto.getDtDesembaraco()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtDesembaraco().getTimeInMillis()));
			pstmt.setInt(8,objeto.getQtAdicao());
			pstmt.setFloat(9,objeto.getVlTaxaDolar());
			pstmt.setInt(10,objeto.getTpViaTransporte());
			pstmt.setInt(11,objeto.getTpIntermedio());
			pstmt.setString(12,objeto.getNrCnpjIntermediario());
			if(objeto.getCdEstadoIntermediario()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdEstadoIntermediario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EntradaDeclaracaoImportacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(EntradaDeclaracaoImportacao objeto, int cdEntradaDeclaracaoImportacaoOld, int cdDocumentoEntradaOld) {
		return update(objeto, cdEntradaDeclaracaoImportacaoOld, cdDocumentoEntradaOld, null);
	}

	public static int update(EntradaDeclaracaoImportacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(EntradaDeclaracaoImportacao objeto, int cdEntradaDeclaracaoImportacaoOld, int cdDocumentoEntradaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_entrada_declaracao_importacao SET cd_entrada_declaracao_importacao=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "nr_declaracao_importacao=?,"+
												      		   "dt_registro=?,"+
												      		   "nm_local=?,"+
												      		   "sg_uf_desembaraco=?,"+
												      		   "dt_desembaraco=?,"+
												      		   "qt_adicao=?,"+
												      		   "vl_taxa_dolar=?,"+
												      		   "tp_via_transporte=?,"+
												      		   "tp_intermedio=?,"+
												      		   "nr_cnpj_intermediario=?,"+
												      		   "cd_estado_intermediario=? WHERE cd_entrada_declaracao_importacao=? AND cd_documento_entrada=?");
			pstmt.setInt(1,objeto.getCdEntradaDeclaracaoImportacao());
			pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			pstmt.setString(3,objeto.getNrDeclaracaoImportacao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmLocal());
			pstmt.setString(6,objeto.getSgUfDesembaraco());
			if(objeto.getDtDesembaraco()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtDesembaraco().getTimeInMillis()));
			pstmt.setInt(8,objeto.getQtAdicao());
			pstmt.setFloat(9,objeto.getVlTaxaDolar());
			pstmt.setInt(10,objeto.getTpViaTransporte());
			pstmt.setInt(11,objeto.getTpIntermedio());
			pstmt.setString(12,objeto.getNrCnpjIntermediario());
			if(objeto.getCdEstadoIntermediario()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdEstadoIntermediario());
			pstmt.setInt(14, cdEntradaDeclaracaoImportacaoOld!=0 ? cdEntradaDeclaracaoImportacaoOld : objeto.getCdEntradaDeclaracaoImportacao());
			pstmt.setInt(15, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada) {
		return delete(cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, null);
	}

	public static int delete(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_entrada_declaracao_importacao WHERE cd_entrada_declaracao_importacao=? AND cd_documento_entrada=?");
			pstmt.setInt(1, cdEntradaDeclaracaoImportacao);
			pstmt.setInt(2, cdDocumentoEntrada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EntradaDeclaracaoImportacao get(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada) {
		return get(cdEntradaDeclaracaoImportacao, cdDocumentoEntrada, null);
	}

	public static EntradaDeclaracaoImportacao get(int cdEntradaDeclaracaoImportacao, int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_declaracao_importacao WHERE cd_entrada_declaracao_importacao=? AND cd_documento_entrada=?");
			pstmt.setInt(1, cdEntradaDeclaracaoImportacao);
			pstmt.setInt(2, cdDocumentoEntrada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EntradaDeclaracaoImportacao(rs.getInt("cd_entrada_declaracao_importacao"),
						rs.getInt("cd_documento_entrada"),
						rs.getString("nr_declaracao_importacao"),
						(rs.getTimestamp("dt_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro").getTime()),
						rs.getString("nm_local"),
						rs.getString("sg_uf_desembaraco"),
						(rs.getTimestamp("dt_desembaraco")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_desembaraco").getTime()),
						rs.getInt("qt_adicao"),
						rs.getFloat("vl_taxa_dolar"),
						rs.getInt("tp_via_transporte"),
						rs.getInt("tp_intermedio"),
						rs.getString("nr_cnpj_intermediario"),
						rs.getInt("cd_estado_intermediario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_declaracao_importacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EntradaDeclaracaoImportacao> getList() {
		return getList(null);
	}

	public static ArrayList<EntradaDeclaracaoImportacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EntradaDeclaracaoImportacao> list = new ArrayList<EntradaDeclaracaoImportacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EntradaDeclaracaoImportacao obj = EntradaDeclaracaoImportacaoDAO.get(rsm.getInt("cd_entrada_declaracao_importacao"), rsm.getInt("cd_documento_entrada"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaDeclaracaoImportacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM alm_entrada_declaracao_importacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
