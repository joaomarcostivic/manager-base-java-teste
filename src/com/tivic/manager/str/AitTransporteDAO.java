package com.tivic.manager.str;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AitTransporteDAO{

	public static int insert(AitTransporte objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("STR_AIT_TRANSPORTE", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAit(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO STR_AIT_TRANSPORTE (CD_AIT,"+
			                                  "NM_EMPRESA,"+
			                                  "CD_EMPRESA,"+
			                                  "CD_INFRACAO,"+
			                                  "DS_PROVIDENCIA,"+
			                                  "DT_PRAZO,"+
			                                  "CD_AGENTE,"+
			                                  "NM_TESTEMUNHA1,"+
			                                  "NR_RG_TESTEMUNHA1,"+
			                                  "NM_ENDERECO_TESTEMUNHA1,"+
			                                  "NM_TESTEMUNHA2,"+
			                                  "NR_RG_TESTEMUNHA2,"+
			                                  "NM_ENDERECO_TESTEMUNHA2,"+
			                                  "NM_PREPOSTO,"+
			                                  "NR_AIT,"+
			                                  "DS_OBSERVACAO,"+
			                                  "DT_INFRACAO,"+
			                                  "NR_PROCESSO1,"+
			                                  "NR_PROCESSO2,"+
			                                  "DT_PROCESSO1,"+
			                                  "DT_PROCESSO2,"+
			                                  "ST_PROCESSO1,"+
			                                  "ST_PROCESSO2,"+
			                                  "DT_NOTIFICACAO1,"+
			                                  "DT_NOTIFICACAO2,"+
			                                  "DT_LIMITE,"+
			                                  "DT_EMISSAO_NIP,"+
			                                  "NR_VIA_NIP,"+
			                                  "ST_AIT,"+
			                                  "CD_PERMISSIONARIO,"+
			                                  "CD_MOTIVO,"+
			                                  "CD_USUARIO_CANCELAMENTO,"+
			                                  "CD_OCORRENCIA,"+
			                                  "VL_LONGITUDE,"+
			                                  "VL_LATITUDE,"+
			                                  "CD_CIDADE,"+
			                                  "CD_EQUIPAMENTO,"+
			                                  "NM_LINHA_PREFIXO,"+
			                                  "NM_VEICULO_PREFIXO,"+
			                                  "NR_PONTO,"+
			                                  "DS_LOCAL_INFRACAO,"+
			                                  "LG_REINCIDENCIA,"+
			                                  "TP_AIT,"+
			                                  "CD_TALAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEmpresa());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdInfracao());
			pstmt.setString(5,objeto.getDsProvidencia());
			if(objeto.getDtPrazo()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPrazo().getTimeInMillis()));
			pstmt.setInt(7,objeto.getCdAgente());
			pstmt.setString(8,objeto.getNmTestemunha1());
			pstmt.setString(9,objeto.getNrRgTestemunha1());
			pstmt.setString(10,objeto.getNmEnderecoTestemunha1());
			pstmt.setString(11,objeto.getNmTestemunha2());
			pstmt.setString(12,objeto.getNrRgTestemunha2());
			pstmt.setString(13,objeto.getNmEnderecoTestemunha2());
			pstmt.setString(14,objeto.getNmPreposto());
			pstmt.setString(15,objeto.getNrAit());
			pstmt.setString(16,objeto.getDsObservacao());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			pstmt.setString(18,objeto.getNrProcesso1());
			pstmt.setString(19,objeto.getNrProcesso2());
			if(objeto.getDtProcesso1()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtProcesso1().getTimeInMillis()));
			if(objeto.getDtProcesso2()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtProcesso2().getTimeInMillis()));
			pstmt.setInt(22,objeto.getStProcesso1());
			pstmt.setInt(23,objeto.getStProcesso2());
			if(objeto.getDtNotificacao1()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtNotificacao1().getTimeInMillis()));
			if(objeto.getDtNotificacao2()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtNotificacao2().getTimeInMillis()));
			if(objeto.getDtLimite()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtLimite().getTimeInMillis()));
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(27, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(27,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			pstmt.setInt(28,objeto.getNrViaNip());
			pstmt.setInt(29,objeto.getStAit());
			pstmt.setInt(30,objeto.getCdPermissionario());
			pstmt.setInt(31,objeto.getCdMotivo());
			pstmt.setInt(32,objeto.getCdUsuarioCancelamento());
			pstmt.setInt(33,objeto.getCdOcorrencia());
			pstmt.setDouble(34,objeto.getVlLongitude());
			pstmt.setDouble(35,objeto.getVlLatitude());
			pstmt.setInt(36,objeto.getCdCidade());
			pstmt.setInt(37,objeto.getCdEquipamento());
			pstmt.setString(38,objeto.getNmLinhaPrefixo());
			pstmt.setString(39,objeto.getNmVeiculoPrefixo());
			pstmt.setString(40,objeto.getNrPonto());
			pstmt.setString(41,objeto.getDsLocalInfracao());
			pstmt.setInt(42,objeto.getLgReincidencia());
			pstmt.setInt(43,objeto.getTpAit());
			if(objeto.getCdTalao()<=0)
				pstmt.setNull(44, Types.INTEGER);
			else
				pstmt.setInt(44,objeto.getCdTalao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitTransporte objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AitTransporte objeto, int cdAitOld) {
		return update(objeto, cdAitOld, null);
	}

	public static int update(AitTransporte objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AitTransporte objeto, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE STR_AIT_TRANSPORTE SET CD_AIT=?,"+
												      		   "NM_EMPRESA=?,"+
												      		   "CD_EMPRESA=?,"+
												      		   "CD_INFRACAO=?,"+
												      		   "DS_PROVIDENCIA=?,"+
												      		   "DT_PRAZO=?,"+
												      		   "CD_AGENTE=?,"+
												      		   "NM_TESTEMUNHA1=?,"+
												      		   "NR_RG_TESTEMUNHA1=?,"+
												      		   "NM_ENDERECO_TESTEMUNHA1=?,"+
												      		   "NM_TESTEMUNHA2=?,"+
												      		   "NR_RG_TESTEMUNHA2=?,"+
												      		   "NM_ENDERECO_TESTEMUNHA2=?,"+
												      		   "NM_PREPOSTO=?,"+
												      		   "NR_AIT=?,"+
												      		   "DS_OBSERVACAO=?,"+
												      		   "DT_INFRACAO=?,"+
												      		   "NR_PROCESSO1=?,"+
												      		   "NR_PROCESSO2=?,"+
												      		   "DT_PROCESSO1=?,"+
												      		   "DT_PROCESSO2=?,"+
												      		   "ST_PROCESSO1=?,"+
												      		   "ST_PROCESSO2=?,"+
												      		   "DT_NOTIFICACAO1=?,"+
												      		   "DT_NOTIFICACAO2=?,"+
												      		   "DT_LIMITE=?,"+
												      		   "DT_EMISSAO_NIP=?,"+
												      		   "NR_VIA_NIP=?,"+
												      		   "ST_AIT=?,"+
												      		   "CD_PERMISSIONARIO=?,"+
												      		   "CD_MOTIVO=?,"+
												      		   "CD_USUARIO_CANCELAMENTO=?,"+
												      		   "CD_OCORRENCIA=?,"+
												      		   "VL_LONGITUDE=?,"+
												      		   "VL_LATITUDE=?,"+
												      		   "CD_CIDADE=?,"+
												      		   "CD_EQUIPAMENTO=?,"+
												      		   "NM_LINHA_PREFIXO=?,"+
												      		   "NM_VEICULO_PREFIXO=?,"+
												      		   "NR_PONTO=?,"+
												      		   "DS_LOCAL_INFRACAO=?,"+
												      		   "LG_REINCIDENCIA=?,"+
												      		   "TP_AIT=?,"+
												      		   "CD_TALAO=? WHERE CD_AIT=?");
			pstmt.setInt(1,objeto.getCdAit());
			pstmt.setString(2,objeto.getNmEmpresa());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdInfracao());
			pstmt.setString(5,objeto.getDsProvidencia());
			if(objeto.getDtPrazo()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPrazo().getTimeInMillis()));
			pstmt.setInt(7,objeto.getCdAgente());
			pstmt.setString(8,objeto.getNmTestemunha1());
			pstmt.setString(9,objeto.getNrRgTestemunha1());
			pstmt.setString(10,objeto.getNmEnderecoTestemunha1());
			pstmt.setString(11,objeto.getNmTestemunha2());
			pstmt.setString(12,objeto.getNrRgTestemunha2());
			pstmt.setString(13,objeto.getNmEnderecoTestemunha2());
			pstmt.setString(14,objeto.getNmPreposto());
			pstmt.setString(15,objeto.getNrAit());
			pstmt.setString(16,objeto.getDsObservacao());
			if(objeto.getDtInfracao()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtInfracao().getTimeInMillis()));
			pstmt.setString(18,objeto.getNrProcesso1());
			pstmt.setString(19,objeto.getNrProcesso2());
			if(objeto.getDtProcesso1()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtProcesso1().getTimeInMillis()));
			if(objeto.getDtProcesso2()==null)
				pstmt.setNull(21, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(21,new Timestamp(objeto.getDtProcesso2().getTimeInMillis()));
			pstmt.setInt(22,objeto.getStProcesso1());
			pstmt.setInt(23,objeto.getStProcesso2());
			if(objeto.getDtNotificacao1()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtNotificacao1().getTimeInMillis()));
			if(objeto.getDtNotificacao2()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtNotificacao2().getTimeInMillis()));
			if(objeto.getDtLimite()==null)
				pstmt.setNull(26, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(26,new Timestamp(objeto.getDtLimite().getTimeInMillis()));
			if(objeto.getDtEmissaoNip()==null)
				pstmt.setNull(27, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(27,new Timestamp(objeto.getDtEmissaoNip().getTimeInMillis()));
			pstmt.setInt(28,objeto.getNrViaNip());
			pstmt.setInt(29,objeto.getStAit());
			pstmt.setInt(30,objeto.getCdPermissionario());
			pstmt.setInt(31,objeto.getCdMotivo());
			pstmt.setInt(32,objeto.getCdUsuarioCancelamento());
			pstmt.setInt(33,objeto.getCdOcorrencia());
			pstmt.setDouble(34,objeto.getVlLongitude());
			pstmt.setDouble(35,objeto.getVlLatitude());
			pstmt.setInt(36,objeto.getCdCidade());
			pstmt.setInt(37,objeto.getCdEquipamento());
			pstmt.setString(38,objeto.getNmLinhaPrefixo());
			pstmt.setString(39,objeto.getNmVeiculoPrefixo());
			pstmt.setString(40,objeto.getNrPonto());
			pstmt.setString(41,objeto.getDsLocalInfracao());
			pstmt.setInt(42,objeto.getLgReincidencia());
			pstmt.setInt(43,objeto.getTpAit());
			if(objeto.getCdTalao()<=0)
				pstmt.setNull(44, Types.INTEGER);
			else
				pstmt.setInt(44,objeto.getCdTalao());
			pstmt.setInt(45, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAit) {
		return delete(cdAit, null);
	}

	public static int delete(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM STR_AIT_TRANSPORTE WHERE CD_AIT=?");
			pstmt.setInt(1, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitTransporte get(int cdAit) {
		return get(cdAit, null);
	}

	public static AitTransporte get(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM STR_AIT_TRANSPORTE WHERE CD_AIT=?");
			pstmt.setInt(1, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitTransporte(rs.getInt("CD_AIT"),
						rs.getString("NM_EMPRESA"),
						rs.getInt("CD_EMPRESA"),
						rs.getInt("CD_INFRACAO"),
						rs.getString("DS_PROVIDENCIA"),
						(rs.getTimestamp("DT_PRAZO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_PRAZO").getTime()),
						rs.getInt("CD_AGENTE"),
						rs.getString("NM_TESTEMUNHA1"),
						rs.getString("NR_RG_TESTEMUNHA1"),
						rs.getString("NM_ENDERECO_TESTEMUNHA1"),
						rs.getString("NM_TESTEMUNHA2"),
						rs.getString("NR_RG_TESTEMUNHA2"),
						rs.getString("NM_ENDERECO_TESTEMUNHA2"),
						rs.getString("NM_PREPOSTO"),
						rs.getString("NR_AIT"),
						rs.getString("DS_OBSERVACAO"),
						(rs.getTimestamp("DT_INFRACAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_INFRACAO").getTime()),
						rs.getString("NR_PROCESSO1"),
						rs.getString("NR_PROCESSO2"),
						(rs.getTimestamp("DT_PROCESSO1")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_PROCESSO1").getTime()),
						(rs.getTimestamp("DT_PROCESSO2")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_PROCESSO2").getTime()),
						rs.getInt("ST_PROCESSO1"),
						rs.getInt("ST_PROCESSO2"),
						(rs.getTimestamp("DT_NOTIFICACAO1")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_NOTIFICACAO1").getTime()),
						(rs.getTimestamp("DT_NOTIFICACAO2")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_NOTIFICACAO2").getTime()),
						(rs.getTimestamp("DT_LIMITE")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_LIMITE").getTime()),
						(rs.getTimestamp("DT_EMISSAO_NIP")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_EMISSAO_NIP").getTime()),
						rs.getInt("NR_VIA_NIP"),
						rs.getInt("ST_AIT"),
						rs.getInt("CD_PERMISSIONARIO"),
						rs.getInt("CD_MOTIVO"),
						rs.getInt("CD_USUARIO_CANCELAMENTO"),
						rs.getInt("CD_OCORRENCIA"),
						rs.getDouble("VL_LONGITUDE"),
						rs.getDouble("VL_LATITUDE"),
						rs.getInt("CD_CIDADE"),
						rs.getInt("CD_EQUIPAMENTO"),
						rs.getString("NM_LINHA_PREFIXO"),
						rs.getString("NM_VEICULO_PREFIXO"),
						rs.getString("NR_PONTO"),
						rs.getString("DS_LOCAL_INFRACAO"),
						rs.getInt("LG_REINCIDENCIA"),
						rs.getInt("TP_AIT"), 
						rs.getInt("CD_AIT"),
						(rs.getTimestamp("DT_EMISSAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_EMISSAO_").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM STR_AIT_TRANSPORTE");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitTransporte> getList() {
		return getList(null);
	}

	public static ArrayList<AitTransporte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitTransporte> list = new ArrayList<AitTransporte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitTransporte obj = AitTransporteDAO.get(rsm.getInt("CD_AIT"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitTransporteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM STR_AIT_TRANSPORTE", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
