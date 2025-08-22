package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TituloCreditoDAO{

	public static int insert(TituloCredito objeto) {
		return insert(objeto, null);
	}

	public static int insert(TituloCredito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_titulo_credito", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTituloCredito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_titulo_credito (cd_titulo_credito,"+
			                                  "cd_instituicao_financeira,"+
			                                  "cd_alinea,"+
			                                  "nr_documento,"+
			                                  "nr_documento_emissor,"+
			                                  "tp_documento_emissor,"+
			                                  "nm_emissor,"+
			                                  "vl_titulo,"+
			                                  "tp_emissao,"+
			                                  "nr_agencia,"+
			                                  "dt_vencimento,"+
			                                  "dt_credito,"+
			                                  "st_titulo,"+
			                                  "ds_observacao,"+
			                                  "cd_tipo_documento,"+
			                                  "tp_circulacao,"+
			                                  "cd_conta,"+
			                                  "cd_conta_receber,"+
			                                  "cd_emissor,"+
			                                  "cd_portador,"+
			                                  "nr_conta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicaoFinanceira()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicaoFinanceira());
			if(objeto.getCdAlinea()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAlinea());
			pstmt.setString(4,objeto.getNrDocumento());
			pstmt.setString(5,objeto.getNrDocumentoEmissor());
			pstmt.setInt(6,objeto.getTpDocumentoEmissor());
			pstmt.setString(7,objeto.getNmEmissor());
			pstmt.setDouble(8,objeto.getVlTitulo());
			pstmt.setInt(9,objeto.getTpEmissao());
			pstmt.setString(10,objeto.getNrAgencia());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getDtCredito()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtCredito().getTimeInMillis()));
			pstmt.setInt(13,objeto.getStTitulo());
			pstmt.setString(14,objeto.getDsObservacao());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipoDocumento());
			pstmt.setInt(16,objeto.getTpCirculacao());
			pstmt.setInt(17,objeto.getCdConta());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdContaReceber());
			if(objeto.getCdEmissor()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEmissor());
			if(objeto.getCdPortador()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdPortador());
			pstmt.setString(21,objeto.getNrConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TituloCredito objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TituloCredito objeto, int cdTituloCreditoOld) {
		return update(objeto, cdTituloCreditoOld, null);
	}

	public static int update(TituloCredito objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TituloCredito objeto, int cdTituloCreditoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_titulo_credito SET cd_titulo_credito=?,"+
												      		   "cd_instituicao_financeira=?,"+
												      		   "cd_alinea=?,"+
												      		   "nr_documento=?,"+
												      		   "nr_documento_emissor=?,"+
												      		   "tp_documento_emissor=?,"+
												      		   "nm_emissor=?,"+
												      		   "vl_titulo=?,"+
												      		   "tp_emissao=?,"+
												      		   "nr_agencia=?,"+
												      		   "dt_vencimento=?,"+
												      		   "dt_credito=?,"+
												      		   "st_titulo=?,"+
												      		   "ds_observacao=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "tp_circulacao=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "cd_emissor=?,"+
												      		   "cd_portador=?,"+
												      		   "nr_conta=? WHERE cd_titulo_credito=?");
			pstmt.setInt(1,objeto.getCdTituloCredito());
			if(objeto.getCdInstituicaoFinanceira()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicaoFinanceira());
			if(objeto.getCdAlinea()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAlinea());
			pstmt.setString(4,objeto.getNrDocumento());
			pstmt.setString(5,objeto.getNrDocumentoEmissor());
			pstmt.setInt(6,objeto.getTpDocumentoEmissor());
			pstmt.setString(7,objeto.getNmEmissor());
			pstmt.setDouble(8,objeto.getVlTitulo());
			pstmt.setInt(9,objeto.getTpEmissao());
			pstmt.setString(10,objeto.getNrAgencia());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getDtCredito()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtCredito().getTimeInMillis()));
			pstmt.setInt(13,objeto.getStTitulo());
			pstmt.setString(14,objeto.getDsObservacao());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipoDocumento());
			pstmt.setInt(16,objeto.getTpCirculacao());
			pstmt.setInt(17,objeto.getCdConta());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdContaReceber());
			if(objeto.getCdEmissor()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdEmissor());
			if(objeto.getCdPortador()==0)
				pstmt.setNull(20, Types.INTEGER);
			else
				pstmt.setInt(20,objeto.getCdPortador());
			pstmt.setString(21,objeto.getNrConta());
			pstmt.setInt(22, cdTituloCreditoOld!=0 ? cdTituloCreditoOld : objeto.getCdTituloCredito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTituloCredito) {
		return delete(cdTituloCredito, null);
	}

	public static int delete(int cdTituloCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_titulo_credito WHERE cd_titulo_credito=?");
			pstmt.setInt(1, cdTituloCredito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TituloCredito get(int cdTituloCredito) {
		return get(cdTituloCredito, null);
	}

	public static TituloCredito get(int cdTituloCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_titulo_credito WHERE cd_titulo_credito=?");
			pstmt.setInt(1, cdTituloCredito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TituloCredito(rs.getInt("cd_titulo_credito"),
						rs.getInt("cd_instituicao_financeira"),
						rs.getInt("cd_alinea"),
						rs.getString("nr_documento"),
						rs.getString("nr_documento_emissor"),
						rs.getInt("tp_documento_emissor"),
						rs.getString("nm_emissor"),
						rs.getDouble("vl_titulo"),
						rs.getInt("tp_emissao"),
						rs.getString("nr_agencia"),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						(rs.getTimestamp("dt_credito")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_credito").getTime()),
						rs.getInt("st_titulo"),
						rs.getString("ds_observacao"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("tp_circulacao"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_conta_receber"),
						rs.getInt("cd_emissor"),
						rs.getInt("cd_portador"),
						rs.getString("nr_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_titulo_credito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TituloCredito> getList() {
		return getList(null);
	}

	public static ArrayList<TituloCredito> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TituloCredito> list = new ArrayList<TituloCredito>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TituloCredito obj = TituloCreditoDAO.get(rsm.getInt("cd_titulo_credito"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TituloCreditoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_titulo_credito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
