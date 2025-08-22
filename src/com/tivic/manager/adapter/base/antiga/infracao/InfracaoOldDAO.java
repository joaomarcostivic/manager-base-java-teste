package com.tivic.manager.adapter.base.antiga.infracao;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.sol.connection.Conexao;

public class InfracaoOldDAO{

	public static int insert(InfracaoOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(InfracaoOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("infracao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCodInfracao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO infracao (cod_infracao,"+
			                                  "ds_infracao2,"+
			                                  "nr_pontuacao,"+
			                                  "nr_cod_detran,"+
			                                  "nr_valor_ufir,"+
			                                  "nm_natureza,"+
			                                  "nr_artigo,"+
			                                  "nr_paragrafo,"+
			                                  "nr_inciso,"+
			                                  "nr_alinea,"+
			                                  "tp_competencia,"+
			                                  "lg_prioritaria,"+
			                                  "dt_fim_vigencia,"+
			                                  "vl_infracao,"+
			                                  "lg_suspensao_cnh,"+
			                                  "tp_responsabilidade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getDsInfracao2());
			pstmt.setInt(3,objeto.getNrPontuacao());
			pstmt.setInt(4,objeto.getNrCodDetran());
			pstmt.setDouble(5,objeto.getNrValorUfir());
			pstmt.setString(6,objeto.getNmNatureza());
			pstmt.setString(7,objeto.getNrArtigo());
			pstmt.setString(8,objeto.getNrParagrafo());
			pstmt.setString(9,objeto.getNrInciso());
			pstmt.setString(10,objeto.getNrAlinea());
			pstmt.setInt(11,objeto.getTpCompetencia());
			pstmt.setInt(12,objeto.getLgPrioritaria());
			if(objeto.getDtFimVigencia()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtFimVigencia().getTimeInMillis()));
			pstmt.setDouble(14,objeto.getVlInfracao());
			pstmt.setInt(15,objeto.getLgSuspensaoCnh());
			pstmt.setInt(16,objeto.getTpResponsabilidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InfracaoOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(InfracaoOld objeto, int cdInfracaoOld) {
		return update(objeto, cdInfracaoOld, null);
	}

	public static int update(InfracaoOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(InfracaoOld objeto, int cdInfracaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE infracao SET cod_infracao=?,"+
												      		   "ds_infracao2=?,"+
												      		   "nr_pontuacao=?,"+
												      		   "nr_cod_detran=?,"+
												      		   "nr_valor_ufir=?,"+
												      		   "nm_natureza=?,"+
												      		   "nr_artigo=?,"+
												      		   "nr_paragrafo=?,"+
												      		   "nr_inciso=?,"+
												      		   "nr_alinea=?,"+
												      		   "tp_competencia=?,"+
												      		   "lg_prioritaria=?,"+
												      		   "dt_fim_vigencia=?,"+
												      		   "vl_infracao=?,"+
												      		   "lg_suspensao_cnh=?,"+
												      		   "tp_responsabilidade=? WHERE cod_infracao=?");
			pstmt.setInt(1,objeto.getCodInfracao());
			pstmt.setString(2,objeto.getDsInfracao2());
			pstmt.setInt(3,objeto.getNrPontuacao());
			pstmt.setInt(4,objeto.getNrCodDetran());
			pstmt.setDouble(5,objeto.getNrValorUfir());
			pstmt.setString(6,objeto.getNmNatureza());
			pstmt.setString(7,objeto.getNrArtigo());
			pstmt.setString(8,objeto.getNrParagrafo());
			pstmt.setString(9,objeto.getNrInciso());
			pstmt.setString(10,objeto.getNrAlinea());
			pstmt.setInt(11,objeto.getTpCompetencia());
			pstmt.setInt(12,objeto.getLgPrioritaria());
			if(objeto.getDtFimVigencia()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtFimVigencia().getTimeInMillis()));
			pstmt.setDouble(14,objeto.getVlInfracao());
			pstmt.setInt(15,objeto.getLgSuspensaoCnh());
			pstmt.setInt(16,objeto.getTpResponsabilidade());
			pstmt.setInt(17, cdInfracaoOld!=0 ? cdInfracaoOld : objeto.getCodInfracao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInfracao) {
		return delete(cdInfracao, null);
	}

	public static int delete(int cdInfracao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM infracao WHERE cod_infracao=?");
			pstmt.setInt(1, cdInfracao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InfracaoOld get(int cdInfracao) {
		return get(cdInfracao, null);
	}

	public static InfracaoOld get(int cdInfracao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
			if(lgBaseAntiga) 
				pstmt = connect.prepareStatement("SELECT * FROM infracao WHERE cod_infracao=?");
			else
				pstmt = connect.prepareStatement("SELECT * FROM infracao WHERE cod_infracao=?");
			
			pstmt.setInt(1, cdInfracao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InfracaoOld(
						rs.getInt("cod_infracao"),
						rs.getString("ds_infracao"),
						rs.getString("ds_infracao2"),
						rs.getInt("nr_pontuacao"),
						rs.getInt("nr_cod_detran"),
						rs.getDouble("nr_valor_ufir"),
						rs.getString("nm_natureza"),
						rs.getString("nr_artigo"),
						rs.getString("nr_paragrafo"),
						rs.getString("nr_inciso"),
						rs.getString("nr_alinea"),
						rs.getInt("tp_competencia"),
						rs.getInt("lg_prioritario"),
						(rs.getTimestamp("dt_fim_vigencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fim_vigencia").getTime()),
						rs.getDouble("vl_infracao"),
						rs.getInt("lg_suspensao_cnh"),
						rs.getInt("tp_responsabilidade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM infracao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InfracaoOld> getList() {
		return getList(null);
	}

	public static ArrayList<InfracaoOld> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InfracaoOld> list = new ArrayList<InfracaoOld>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InfracaoOld obj = InfracaoOldDAO.get(rsm.getInt("cod_infracao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InfracaoOldDAO.getList: " + e);
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
		return Search.find("SELECT * FROM infracao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
