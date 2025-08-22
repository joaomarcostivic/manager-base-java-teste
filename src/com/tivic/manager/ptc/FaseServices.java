package com.tivic.manager.ptc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.EelUtils;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class FaseServices {
	public static Result save(Fase fase){
		return save(fase, null);
	}
	
	public static Result save(Fase fase, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(fase==null)
				return new Result(-1, "Erro ao salvar. Fase é nulo");
			
			int retorno;
			if(fase.getCdFase()==0){
				retorno = FaseDAO.insert(fase, connect);
				fase.setCdFase(retorno);
			}
			else {
				retorno = FaseDAO.update(fase, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FASE", fase);
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
	
	public static Result remove(int cdFase){
		return remove(cdFase, false, null);
	}
	
	public static Result remove(int cdFase, boolean cascade){
		return remove(cdFase, cascade, null);
	}
	
	public static Result remove(int cdFase, boolean cascade, Connection connect){
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
				retorno = FaseDAO.delete(cdFase, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta fase está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Fase excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir fase!");
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM ptc_fase ORDER BY nr_ordem, nm_fase").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<Fase> getFaseByTpDocumento(int cdTipoDocumento) {
		return getFaseByTpDocumento(cdTipoDocumento, null);
	}

	public static List<Fase> getFaseByTpDocumento(int cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			List<Fase> fases = sortFases(cdTipoDocumento, connect);
			return fases;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static List<Fase> sortFases(int cdTipoDocumento, Connection connect) {
		boolean isConnectionNull = connect==null;
		List<Fase> fases = new ArrayList<Fase>();
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			if(cdTipoDocumento == ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", cdTipoDocumento)) {
				fases.add(FaseServices.getFaseByNome("Deferido", connect));
				fases.add(FaseServices.getFaseByNome("Indeferido", connect));
			} else {
				fases.add(FaseServices.getFaseByNome("Pendente", connect));
				fases.add(FaseServices.getFaseByNome("Deferido", connect));
				fases.add(FaseServices.getFaseByNome("Indeferido", connect));
			}
			
			return fases;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM ptc_fase ", "ORDER BY nr_ordem, nm_fase", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllFaseEel() {
		return getAllFaseEel(null);
	}
	
	public static ResultSetMap getAllFaseEel(Connection connect) {
		return EelUtils.getAllSituacoes();
	}
	
	public static int getFaseByFaseEel(String cdSituacao) {
		return getFaseByFaseEel(cdSituacao, null);
	}
	
	public static int getFaseByFaseEel(String cdSituacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			int cdFase = 0;
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT cd_fase FROM ptc_fase "
					+ " WHERE nr_fase_externa ='"+cdSituacao+"'");
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				cdFase = rs.getInt("cd_fase");
			}
			
			return cdFase;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! FaseServices.getFaseByFaseEel: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! FaseServices.getFaseByFaseEel: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getCdFaseByNome(String nmFase) {
		return getCdFaseByNome(nmFase, null);
	}
	
	public static int getCdFaseByNome(String nmFase, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM PTC_FASE WHERE NM_FASE ILIKE ?");
			pstmt.setString(1, nmFase);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) 
				return rs.getInt("cd_fase");
			
			return -1;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! FaseServices.getCdFaseByNome: " + sqlExpt);
			return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! FaseServices.getCdFaseByNome: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Fase getFaseByNome(String nmFase, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM PTC_FASE WHERE NM_FASE ILIKE ?");
			pstmt.setString(1, nmFase);
			
			ResultSet rs = pstmt.executeQuery();
			Fase _fase;
			
			if(rs.next()) {
				_fase = new Fase(
						rs.getInt("cd_fase"),
						rs.getString("nm_fase"),
						rs.getString("nm_fase"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_empresa"),
						rs.getString("nr_fase_externa"),
						rs.getInt("nr_ordem")
						);
				
				return _fase;
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! FaseServices.getCdFaseByNome: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro! FaseServices.getCdFaseByNome: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}