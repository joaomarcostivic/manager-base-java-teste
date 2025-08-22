package com.tivic.manager.adapter.base.antiga.aitmovimento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class AitMovimentoOldDAO{

	public static int insert(AitMovimentoOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitMovimentoOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "codigo_ait");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCodigoAit()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "nr_movimento");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("ait_movimento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setNrMovimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ait_movimento (codigo_ait,"+
			                                  "nr_movimento,"+
			                                  "dt_movimento,"+
			                                  "nr_remessa,"+
			                                  "tp_status,"+
			                                  "tp_arquivo,"+
			                                  "cod_ocorrencia,"+
			                                  "lg_enviado_detran,"+
			                                  "st_entrega,"+
			                                  "nr_processo,"+
			                                  "dt_registro_detran,"+
			                                  "st_recurso,"+
			                                  "nr_sequencial,"+
			                                  "nr_erro,"+
			                                  "dt_digitacao,"+
			                                  "lg_cancela_movimento,"+
			                                  "dt_cancelamento,"+
			                                  "nr_remessa_registro,"+
			                                  "dt_primeiro_registro,"+
			                                  "st_registro_detran,"+
			                                  "st_aviso_recebimento,"+
			                                  "dt_aviso_recebimento,"+
			                                  "cd_processo,"+
			                                  "cd_usuario,"+
			                                  "cd_conta_receber,"+
			                                  "dt_expedicao,"+
			                                  "ds_observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCodigoAit()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCodigoAit());
			pstmt.setInt(2, code);
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			if(objeto.getNrRemessa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getNrRemessa());
			pstmt.setInt(5,objeto.getTpStatus());
			if(objeto.getTpArquivo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getTpArquivo());
			pstmt.setInt(7,objeto.getCodOcorrencia());
			pstmt.setInt(8,objeto.getLgEnviadoDetran());
			pstmt.setInt(9,objeto.getStEntrega());
			pstmt.setString(10,objeto.getNrProcesso());
			if(objeto.getDtRegistroDetran()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(12,objeto.getStRecurso());
			pstmt.setInt(13,objeto.getNrSequencial());
			pstmt.setString(14,objeto.getNrErro());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			pstmt.setInt(16,objeto.getLgCancelaMovimento());
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			pstmt.setInt(18,objeto.getNrRemessaRegistro());
			if(objeto.getDtPrimeiroRegistro()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			pstmt.setInt(20,objeto.getStRegistroDetran());
			pstmt.setInt(21,objeto.getStAvisoRecebimento());
			if(objeto.getDtAvisoRecebimento()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtAvisoRecebimento().getTimeInMillis()));
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdProcesso());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdUsuario());
			pstmt.setInt(25,objeto.getCdContaReceber());
			if(objeto.getDtExpedicao()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtExpedicao().getTimeInMillis()));
			if(objeto.getDsObservacao()==null)
				pstmt.setNull(27, Types.BINARY);
			else
				pstmt.setBytes(27,objeto.getDsObservacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitMovimentoOld objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitMovimentoOld objeto, int codigoAitOld, int nrMovimentoOld) {
		return update(objeto, codigoAitOld, nrMovimentoOld, null);
	}

	public static int update(AitMovimentoOld objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitMovimentoOld objeto, int codigoAitOld, int nrMovimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ait_movimento SET codigo_ait=?,"+
												      		   "nr_movimento=?,"+
												      		   "dt_movimento=?,"+
												      		   "nr_remessa=?,"+
												      		   "tp_status=?,"+
												      		   "tp_arquivo=?,"+
												      		   "cod_ocorrencia=?,"+
												      		   "lg_enviado_detran=?,"+
												      		   "st_entrega=?,"+
												      		   "nr_processo=?,"+
												      		   "dt_registro_detran=?,"+
												      		   "st_recurso=?,"+
												      		   "nr_sequencial=?,"+
												      		   "nr_erro=?,"+
												      		   "dt_digitacao=?,"+
												      		   "lg_cancela_movimento=?,"+
												      		   "dt_cancelamento=?,"+
												      		   "nr_remessa_registro=?,"+
												      		   "dt_primeiro_registro=?,"+
												      		   "st_registro_detran=?,"+
												      		   "st_aviso_recebimento=?,"+
												      		   "dt_aviso_recebimento=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_usuario=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "dt_expedicao=?,"+
												      		   "ds_observacao=? WHERE codigo_ait=? AND nr_movimento=?");
			pstmt.setInt(1,objeto.getCodigoAit());
			pstmt.setInt(2,objeto.getNrMovimento());
			if(objeto.getDtMovimento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtMovimento().getTimeInMillis()));
			if(objeto.getNrRemessa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getNrRemessa());
			pstmt.setInt(5,objeto.getTpStatus());
			if(objeto.getTpArquivo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getTpArquivo());
			pstmt.setInt(7,objeto.getCodOcorrencia());
			pstmt.setInt(8,objeto.getLgEnviadoDetran());
			pstmt.setInt(9,objeto.getStEntrega());
			pstmt.setString(10,objeto.getNrProcesso());
			if(objeto.getDtRegistroDetran()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtRegistroDetran().getTimeInMillis()));
			pstmt.setInt(12,objeto.getStRecurso());
			pstmt.setInt(13,objeto.getNrSequencial());
			pstmt.setString(14,objeto.getNrErro());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			pstmt.setInt(16,objeto.getLgCancelaMovimento());
			if(objeto.getDtCancelamento()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtCancelamento().getTimeInMillis()));
			pstmt.setInt(18,objeto.getNrRemessaRegistro());
			if(objeto.getDtPrimeiroRegistro()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtPrimeiroRegistro().getTimeInMillis()));
			pstmt.setInt(20,objeto.getStRegistroDetran());
			pstmt.setInt(21,objeto.getStAvisoRecebimento());
			if(objeto.getDtAvisoRecebimento()==null)
				pstmt.setNull(22, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(22,new Timestamp(objeto.getDtAvisoRecebimento().getTimeInMillis()));
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdProcesso());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdUsuario());
			pstmt.setInt(25,objeto.getCdContaReceber());
			if(objeto.getDtExpedicao()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtExpedicao().getTimeInMillis()));
			if(objeto.getDsObservacao()==null)
				pstmt.setNull(27, Types.BINARY);
			else
				pstmt.setBytes(27,objeto.getDsObservacao());
			pstmt.setInt(28, codigoAitOld!=0 ? codigoAitOld : objeto.getCodigoAit());
			pstmt.setInt(29, nrMovimentoOld!=0 ? nrMovimentoOld : objeto.getNrMovimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codigoAit, int nrMovimento) {
		return delete(codigoAit, nrMovimento, null);
	}

	public static int delete(int codigoAit, int nrMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ait_movimento WHERE codigo_ait=? AND nr_movimento=?");
			pstmt.setInt(1, codigoAit);
			pstmt.setInt(2, nrMovimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitMovimentoOld get(int codigoAit, int nrMovimento) {
		return get(codigoAit, nrMovimento, null);
	}

	public static AitMovimentoOld get(int codigoAit, int nrMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento WHERE codigo_ait=? AND nr_movimento=?");
			pstmt.setInt(1, codigoAit);
			pstmt.setInt(2, nrMovimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitMovimentoOld(rs.getInt("codigo_ait"),
						rs.getInt("nr_movimento"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("nr_remessa"),
						rs.getInt("tp_status"),
						rs.getInt("tp_arquivo"),
						rs.getInt("cod_ocorrencia"),
						rs.getInt("lg_enviado_detran"),
						rs.getInt("st_entrega"),
						rs.getString("nr_processo"),
						(rs.getTimestamp("dt_registro_detran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
						rs.getInt("st_recurso"),
						rs.getInt("nr_sequencial"),
						rs.getString("nr_erro"),
						(rs.getTimestamp("dt_digitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						rs.getInt("lg_cancela_movimento"),
						(rs.getTimestamp("dt_cancelamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						rs.getInt("nr_remessa_registro"),
						(rs.getTimestamp("dt_primeiro_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
						rs.getInt("st_registro_detran"),
						rs.getInt("st_aviso_recebimento"),
						(rs.getTimestamp("dt_aviso_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aviso_recebimento").getTime()),
						rs.getInt("cd_processo"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_conta_receber"),
						(rs.getTimestamp("dt_expedicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_expedicao").getTime()),
						rs.getBytes("ds_observacao")==null?null:rs.getBytes("ds_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimentoOld getByStatus(int codigoAit, int tpStatus) {
		return getByStatus(codigoAit, tpStatus, null);
	}
	
	public static AitMovimentoOld getByStatus(int codigoAit, int tpStatus, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento WHERE codigo_ait=? AND tp_status=?");
			pstmt.setInt(1, codigoAit);
			pstmt.setInt(2, tpStatus);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitMovimentoOld(rs.getInt("codigo_ait"),
						rs.getInt("nr_movimento"),
						(rs.getTimestamp("dt_movimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimento").getTime()),
						rs.getInt("nr_remessa"),
						rs.getInt("tp_status"),
						rs.getInt("tp_arquivo"),
						rs.getInt("cod_ocorrencia"),
						rs.getInt("lg_enviado_detran"),
						rs.getInt("st_entrega"),
						rs.getString("nr_processo"),
						(rs.getTimestamp("dt_registro_detran")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_registro_detran").getTime()),
						rs.getInt("st_recurso"),
						rs.getInt("nr_sequencial"),
						rs.getString("nr_erro"),
						(rs.getTimestamp("dt_digitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						rs.getInt("lg_cancela_movimento"),
						(rs.getTimestamp("dt_cancelamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cancelamento").getTime()),
						rs.getInt("nr_remessa_registro"),
						(rs.getTimestamp("dt_primeiro_registro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_primeiro_registro").getTime()),
						rs.getInt("st_registro_detran"),
						rs.getInt("st_aviso_recebimento"),
						(rs.getTimestamp("dt_aviso_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aviso_recebimento").getTime()),
						rs.getInt("cd_processo"),
						rs.getInt("cd_usuario"),
						rs.getInt("cd_conta_receber"),
						(rs.getTimestamp("dt_expedicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_expedicao").getTime()),
						rs.getBytes("ds_observacao")==null?null:rs.getBytes("ds_observacao"));
			}
			else{
				return new AitMovimentoOld();
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitMovimentoOld> getList() {
		return getList(null);
	}

	public static ArrayList<AitMovimentoOld> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitMovimentoOld> list = new ArrayList<AitMovimentoOld>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitMovimentoOld obj = get(rsm.getInt("codigo_ait"), rsm.getInt("nr_movimento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ait_movimento", " ORDER BY dt_movimento DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
