package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class DeclaracaoImportacaoDAO{

	public static int insert(DeclaracaoImportacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(DeclaracaoImportacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_declaracao_importacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDeclaracaoImportacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_declaracao_importacao (cd_declaracao_importacao,"+
			                                  "cd_exportador,"+
			                                  "cd_estado,"+
			                                  "cd_item,"+
			                                  "cd_nota_fiscal,"+
			                                  "nr_declaracao_importacao,"+
			                                  "dt_registro,"+
			                                  "ds_local_desembaraco,"+
			                                  "dt_desembaraco,"+
			                                  "tp_via_transporte,"+
			                                  "tp_intermedio,"+
			                                  "nr_cnpj_intermediario,"+
			                                  "cd_estado_intermediario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdExportador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdExportador());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEstado());
			if(objeto.getCdItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdItem());
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNotaFiscal());
			pstmt.setString(6,objeto.getNrDeclaracaoImportacao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setString(8,objeto.getDsLocalDesembaraco());
			if(objeto.getDtDesembaraco()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtDesembaraco().getTimeInMillis()));
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
			System.err.println("Erro! DeclaracaoImportacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DeclaracaoImportacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DeclaracaoImportacao objeto, int cdDeclaracaoImportacaoOld) {
		return update(objeto, cdDeclaracaoImportacaoOld, null);
	}

	public static int update(DeclaracaoImportacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DeclaracaoImportacao objeto, int cdDeclaracaoImportacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_declaracao_importacao SET cd_declaracao_importacao=?,"+
												      		   "cd_exportador=?,"+
												      		   "cd_estado=?,"+
												      		   "cd_item=?,"+
												      		   "cd_nota_fiscal=?,"+
												      		   "nr_declaracao_importacao=?,"+
												      		   "dt_registro=?,"+
												      		   "ds_local_desembaraco=?,"+
												      		   "dt_desembaraco=?,"+
												      		   "tp_via_transporte=?,"+
												      		   "tp_intermedio=?,"+
												      		   "nr_cnpj_intermediario=?,"+
												      		   "cd_estado_intermediario=? WHERE cd_declaracao_importacao=?");
			pstmt.setInt(1,objeto.getCdDeclaracaoImportacao());
			if(objeto.getCdExportador()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdExportador());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEstado());
			if(objeto.getCdItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdItem());
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdNotaFiscal());
			pstmt.setString(6,objeto.getNrDeclaracaoImportacao());
			if(objeto.getDtRegistro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtRegistro().getTimeInMillis()));
			pstmt.setString(8,objeto.getDsLocalDesembaraco());
			if(objeto.getDtDesembaraco()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtDesembaraco().getTimeInMillis()));
			pstmt.setInt(10,objeto.getTpViaTransporte());
			pstmt.setInt(11,objeto.getTpIntermedio());
			pstmt.setString(12,objeto.getNrCnpjIntermediario());
			if(objeto.getCdEstadoIntermediario()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdEstadoIntermediario());
			pstmt.setInt(14, cdDeclaracaoImportacaoOld!=0 ? cdDeclaracaoImportacaoOld : objeto.getCdDeclaracaoImportacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDeclaracaoImportacao) {
		return delete(cdDeclaracaoImportacao, null);
	}

	public static int delete(int cdDeclaracaoImportacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_declaracao_importacao WHERE cd_declaracao_importacao=?");
			pstmt.setInt(1, cdDeclaracaoImportacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DeclaracaoImportacao get(int cdDeclaracaoImportacao) {
		return get(cdDeclaracaoImportacao, null);
	}

	public static DeclaracaoImportacao get(int cdDeclaracaoImportacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_declaracao_importacao WHERE cd_declaracao_importacao=?");
			pstmt.setInt(1, cdDeclaracaoImportacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DeclaracaoImportacao(rs.getInt("cd_declaracao_importacao"),
						rs.getInt("cd_exportador"),
						rs.getInt("cd_estado"),
						rs.getInt("cd_item"),
						rs.getInt("cd_nota_fiscal"),
						rs.getString("nr_declaracao_importacao"),
						(rs.getTimestamp("dt_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro").getTime()),
						rs.getString("ds_local_desembaraco"),
						(rs.getTimestamp("dt_desembaraco")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_desembaraco").getTime()),
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
			System.err.println("Erro! DeclaracaoImportacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_declaracao_importacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DeclaracaoImportacao> getList() {
		return getList(null);
	}

	public static ArrayList<DeclaracaoImportacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DeclaracaoImportacao> list = new ArrayList<DeclaracaoImportacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DeclaracaoImportacao obj = DeclaracaoImportacaoDAO.get(rsm.getInt("cd_declaracao_importacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DeclaracaoImportacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fsc_declaracao_importacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
