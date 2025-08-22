package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TalonarioServices {
	public static int TP_TALONARIO_TRANSITO 			 = 0;
	public static int TP_TALONARIO_ELETRONICO_TRANSITO 	 = 1;
	public static int TP_TALONARIO_TRANSPORTE 			 = 2;
	public static int TP_TALONARIO_ELETRONICO_TRANSPORTE = 3;
	public static int TP_TALONARIO_BOAT 			   	 = 5;
	public static int TP_TALONARIO_ELETRONICO_BOAT 	   	 = 6;
	public static int TP_TALONARIO_RRD 				   	 = 7;
	public static int TP_TALONARIO_ELETRONICO_RRD 	   	 = 8;
	public static int TP_TALONARIO_TRRAV 			   	 = 9;
	public static int TP_TALONARIO_ELETRONICO_TRRAV    	 = 10;
	public static int TP_TALONARIO_TRANSPORTE_NIC 	   	 = 11;
	public static int TP_TALONARIO_VIDEO_MONITORAMENTO 	 = 12;
	public static int TP_TALONARIO_RADAR_ESTATICO 	 = 13;
	public static int TP_TALONARIO_RADAR_FIXO 	 = 14;
	public static int TP_TALONARIO_ZONA_AZUL 	 = 15;
	
	public static int ST_TALAO_INATIVO    = 0;
	public static int ST_TALAO_ATIVO      = 1;
	public static int ST_TALAO_CONCLUIDO  = 2;
	public static int ST_TALAO_CONFERIDO  = 3;
	public static int ST_TALAO_DIVERGENTE = 4;
	public static int ST_TALAO_PENDENTE   = 5;
	
	public static Result save(Talonario talonario){
		return save(talonario, null, null);
	}

	public static Result save(Talonario talonario, AuthData authData){
		return save(talonario, authData, null);
	}

	public static Result save(Talonario talonario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(talonario==null)
				return new Result(-1, "Erro ao salvar. Talonario é nulo");

			int retorno;
			if(talonario.getCdTalao()==0){
				retorno = TalonarioDAO.insert(talonario, connect);
				talonario.setCdTalao(retorno);
			}
			else {
				retorno = TalonarioDAO.update(talonario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TALONARIO", talonario);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(Talonario talonario) {
		return remove(talonario.getCdTalao());
	}
	public static Result remove(int cdTalao){
		return remove(cdTalao, false, null, null);
	}
	public static Result remove(int cdTalao, boolean cascade){
		return remove(cdTalao, cascade, null, null);
	}
	public static Result remove(int cdTalao, boolean cascade, AuthData authData){
		return remove(cdTalao, cascade, authData, null);
	}
	public static Result remove(int cdTalao, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = TalonarioDAO.delete(cdTalao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
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
			pstmt = connect.prepareStatement("SELECT A.*, A.cod_talao AS cd_talao, "
										   + " B.nm_agente, B.cod_agente AS cd_agente "
										   + " FROM talonario A"
										   + " LEFT OUTER JOIN agente B ON (A.cod_agente = B.cod_agente)"
										   + " ORDER BY A.nr_talao DESC");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioServices.getAll: " + e);
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
		return Search.find(
				" SELECT A.*, A.cod_talao AS cd_talao, "
				+ " B.nm_agente, B.cod_agente AS cd_agente "
				+ " FROM talonario A "
				+ " LEFT OUTER JOIN agente B ON (A.cod_agente = B.cod_agente) ", 
				" ORDER BY A.nr_talao DESC ", 
				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result getTalonariosByAgente(com.tivic.manager.str.Agente agente) {
		return getTalonariosByAgente(agente, null);
	}
	public static Result getTalonariosByAgente(com.tivic.manager.str.Agente agente, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(true);
			}

			boolean lgGctPadrao = ManagerConf.getInstance().getAsBoolean("GCT_PADRAO");
			ArrayList<Talonario> talonarios = new ArrayList<Talonario>();
			
			String sql =  "SELECT * FROM talonario " +
						  "WHERE cod_agente = ? " 	+
						  "  AND (tp_talao = ? OR tp_talao = ? OR tp_talao = ?)" +
						  "  AND st_talao = 1" +
						  " ORDER BY dt_entrega, A.nr_talao DESC";
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, agente.getCdAgente());
			pstmt.setInt(2, TP_TALONARIO_ELETRONICO_TRANSITO);
			pstmt.setInt(3, TP_TALONARIO_ELETRONICO_BOAT);
			pstmt.setInt(4, TP_TALONARIO_ELETRONICO_RRD);
			
			ResultSetMap rsmTalonario = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmTalonario.next()) {
				
				int nrUltimoAitTalao = 0;
				
				
				if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE){
					sql = "SELECT * FROM mob_ait_transporte WHERE nr_ait >= ? AND nr_ait <= ? ";			
				} else {
					if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSITO){
						sql = "SELECT MAX(nr_ait) as nr_ultimo_ait FROM mob_ait WHERE cd_agente = ? AND nr_ait >=  ? AND nr_ait <= ?";
					}
					else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD) {
						sql = "SELECT MAX(nr_rrd) as nr_ultimo_rrd FROM mob_rrd WHERE cd_agente = ? AND nr_rrd >=  ? AND nr_rrd <= ?";
					}
					else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT) {
						sql = "SELECT MAX(nr_boat) as nr_ultimo_boat FROM mob_boat WHERE cd_agente = ? AND nr_boat >=  ? AND nr_boat <= ?";
					}
				}
				
				PreparedStatement pstmt2 = connect.prepareStatement(sql);
				
				if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_TRANSPORTE) {
					pstmt2.setString(1, rsmTalonario.getString("NR_INICIAL"));
					pstmt2.setString(2, rsmTalonario.getString("NR_FINAL"));
				}
				else{
					if(lgGctPadrao){
						pstmt2.setInt(1, agente.getCdAgente());
					}
					else{ 
						pstmt2.setInt(1, agente.getCdAgente());
						pstmt2.setInt(2, rsmTalonario.getInt("NR_INICIAL"));
						pstmt2.setInt(3, rsmTalonario.getInt("NR_FINAL"));																		
					}

				}
				
				ResultSet rsNr = pstmt2.executeQuery();
				
				//List<Integer> aitsAgente = new ArrayList<Integer>();
				
				if(rsNr.next()){
					if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_RRD){									
						nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_RRD") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_RRD");
					}
					else if(rsmTalonario.getInt("tp_talao") == TalonarioServices.TP_TALONARIO_ELETRONICO_BOAT){									
						nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_BOAT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_RRD");
					}
					else { 									
						nrUltimoAitTalao = rsNr.getInt("NR_ULTIMO_AIT") == 0 ? rsmTalonario.getInt("NR_INICIAL")-1 : rsNr.getInt("NR_ULTIMO_AIT");
					}									
				}
				else{
				    nrUltimoAitTalao = rsmTalonario.getInt("NR_INICIAL")-1;//rsmTalonario.getInt("NR_FINAL");
				}
				
				rsNr.close();
				
				//SE A NUMERACAO DO TALONARIO JA FOI UTILIZADA, NAO CONSIDERAR O TALONARIO
				if(nrUltimoAitTalao >= rsmTalonario.getInt("NR_FINAL"))
					continue;
				
				Talonario talao;
				
				talao = new Talonario();
				talao.setCdTalao(rsmTalonario.getInt("CD_TALAO"));
				talao.setCdAgente(rsmTalonario.getInt("CD_AGENTE"));
				talao.setNrInicial(rsmTalonario.getInt("NR_INICIAL"));
				talao.setNrFinal(rsmTalonario.getInt("NR_FINAL"));
				talao.setDtEntrega((rsmTalonario.getTimestamp("DT_ENTREGA")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_ENTREGA").getTime()));
				talao.setDtDevolucao((rsmTalonario.getTimestamp("DT_DEVOLUCAO")==null)?null:Util.longToCalendar(rsmTalonario.getTimestamp("DT_DEVOLUCAO").getTime()));
				talao.setStTalao(rsmTalonario.getInt("ST_TALAO"));
				talao.setNrTalao(rsmTalonario.getInt("NR_TALAO"));
				talao.setTpTalao(rsmTalonario.getInt("TP_TALAO"));
				talao.setNrUltimoAit(nrUltimoAitTalao);				
				
				talonarios.add(talao);				
			}
			
			if(talonarios.size()==0) {
				return new Result(-5, "Nenhum talão distribuído para o agente. Entre em contato com a administração.");
			}
			
			return new Result(1, "Sucesso!", "TALOES", talonarios);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
