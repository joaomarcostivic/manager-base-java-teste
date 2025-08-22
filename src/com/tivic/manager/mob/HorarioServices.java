package com.tivic.manager.mob;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoPessoaDAO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class HorarioServices {

	public static Result save(Horario horario){
		return save(horario, null, null);
	}

	public static Result save(Horario horario, AuthData authData){
		return save(horario, authData, null);
	}

	public static Result save(Horario horario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(horario==null)
				return new Result(-1, "Erro ao salvar. Horario é nulo");

			int retorno;
			
			Horario h = HorarioDAO.get(horario.getCdHorario(), horario.getCdTabelaHorario(), horario.getCdLinha(), horario.getCdRota(), horario.getCdTrecho());
			
			if(h==null){
				retorno = HorarioDAO.insert(horario, connect);
				horario.setCdHorario(retorno);
			}
			else {
				retorno = HorarioDAO.update(horario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "HORARIO", horario);
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
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_horario";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM mob_horario";
			
			pstmt = connect.prepareStatement(sql);	
			
			return new ResultSetMap(pstmt.executeQuery());
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
	
	public static HashMap<String, Object> getSync() {
		return getSync(null);
	}

	public static HashMap<String, Object> getSync(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_horario ORDER BY cd_horario, cd_tabela_horario, cd_linha, cd_rota, cd_trecho";
								
			pstmt = connect.prepareStatement(sql);	

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("Horario", Util.resultSetToArrayList(pstmt.executeQuery()));
			
			return register;
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
	
	/**
	 * Metodo para gerar e salvar horarios entre duas datas de acordo um periodo
	 * @return Result 1 - salvo
	 */
	public static Result saveHorarios(LoteHorariosDTO loteHorariosDTO) {
		return saveHorarios(loteHorariosDTO, null);
	}

	public static Result saveHorarios(LoteHorariosDTO loteHorariosDTO, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			while (loteHorariosDTO.getHrInicial().before(loteHorariosDTO.getHrFinal())) {
				GregorianCalendar horarioTrecho = new GregorianCalendar(loteHorariosDTO.getHrInicial().get(Calendar.YEAR), 
						loteHorariosDTO.getHrInicial().get(Calendar.MONTH), loteHorariosDTO.getHrInicial().get(Calendar.DAY_OF_MONTH), 
						loteHorariosDTO.getHrInicial().get(Calendar.HOUR_OF_DAY), loteHorariosDTO.getHrInicial().get(Calendar.MINUTE));
				retorno = 0;
				
		    	for (com.tivic.manager.mob.tabelashorarios.ParadaDTO registro : loteHorariosDTO.getParadas()) {
	    			if ((Integer) registro.getCdTrechoAnterior() == 0) {
	    				Horario horario = new Horario();
				    	horario.setCdTabelaHorario((Integer) loteHorariosDTO.getCdTabelaHorario());
				    	horario.setCdLinha((Integer) loteHorariosDTO.getCdLinha());
				    	horario.setCdRota((Integer) loteHorariosDTO.getCdRota());
		    			horario.setHrPartida(loteHorariosDTO.getHrInicial());
				    	horario.setHrChegada(loteHorariosDTO.getHrInicial());
		    			horario.setCdTrecho((Integer) registro.getCdTrecho());
		    			
				    	retorno = save(horario, null, connect).getCode();
				    }
		    		if (retorno!=0) {
			    		horarioTrecho.add(Calendar.MINUTE, loteHorariosDTO.getPeriodo());
			    		
			    		Horario horario = new Horario();
			    		horario.setCdTabelaHorario((Integer) loteHorariosDTO.getCdTabelaHorario());
				    	horario.setCdLinha((Integer) loteHorariosDTO.getCdLinha());
				    	horario.setCdRota((Integer) loteHorariosDTO.getCdRota());
		    			horario.setCdTrecho((Integer) registro.getCdTrechoAnterior());
			    		horario.setHrPartida(horarioTrecho);
			    		horario.setHrChegada(horarioTrecho);
			    		
			    		retorno = save(horario, null, connect).getCode();
			    	} else
			    		break;

		    	}
		    	
	    		if(retorno<=0) {
		    		Conexao.rollback(connect);
		    		break;
		    	}
		    	
	    		loteHorariosDTO.getHrInicial().add(Calendar.MINUTE, loteHorariosDTO.getPeriodo());
		    }
		    
		    if (retorno>0)
		    	connect.commit();
			
			return new Result(retorno);
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.saveHorarios: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.saveHorarios: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveGrupoHorarios(ArrayList<Horario> horarios) {
		return saveGrupoHorarios(horarios, null);
	}

	public static Result saveGrupoHorarios(ArrayList<Horario> horarios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result retorno = new Result(0);
			ArrayList<Horario> respHorarios = new ArrayList();
			
			for(Horario horario: horarios) {
				if(horario.getHrChegada() == null && horario.getCdHorario() > 0) {
					retorno = remove(horario, connect);
				}else {
					retorno = save(horario, null, connect);
				}
				
				if(retorno.getCode()<=0) {
		    		Conexao.rollback(connect);
		    		break;
		    	}
				respHorarios.add((Horario)retorno.getObjects().get("HORARIO"));
			}

		    if (isConnectionNull && retorno.getCode()>0)
		    	connect.commit();

			return new Result(retorno.getCode(), retorno.getCode()<=0?"Erro ao salvar...":"Salvo com sucesso...", "HORARIOS", respHorarios);
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.saveHorarios: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.saveHorarios: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result saveVariacao(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdVariacao) {
		return saveVariacao(cdHorario, cdTabelaHorario, cdLinha, cdRota, cdVariacao, null);
	}
	
	public static Result saveVariacao(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdVariacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;

			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_HORARIO", "" + cdHorario, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_TABELA_HORARIO", "" + cdTabelaHorario, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_LINHA", "" + cdLinha, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_ROTA", "" + cdRota, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmHorarios = find(criterios);
			
			while (rsmHorarios.next()) {
				Horario horario = HorarioDAO.get(cdHorario, cdTabelaHorario, cdLinha, cdRota, rsmHorarios.getInt("CD_TRECHO"), connect);
				horario.setCdVariacao(cdVariacao);
				
				retorno = HorarioServices.save(horario, null, connect).getCode();
				
				if (retorno<=0)
					break;
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao salvar variação!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro salvo com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao salvar registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(Horario horario) {
		return remove(horario.getCdHorario(), horario.getCdTabelaHorario(), horario.getCdLinha(), horario.getCdRota(), horario.getCdTrecho());
	}
	public static Result remove(Horario horario, Connection connection) {
		return remove(horario.getCdHorario(), horario.getCdTabelaHorario(), horario.getCdLinha(), horario.getCdRota(), horario.getCdTrecho(), false, null, connection);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota){
		return remove(cdHorario, cdTabelaHorario, cdLinha, cdRota, 0, false, null, null);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho){
		return remove(cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, false, null, null);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, boolean cascade){
		return remove(cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cascade, null, null);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, boolean cascade, AuthData authData){
		return remove(cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cascade, authData, null);
	}
	public static Result remove(int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, boolean cascade, AuthData authData, Connection connect){
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
			if(!cascade || retorno>0) {
				
				if (cdTrecho!=0)
					retorno = HorarioDAO.delete(cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, connect);
				else {
					
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.CD_HORARIO", "" + cdHorario, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.CD_TABELA_HORARIO", "" + cdTabelaHorario, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.CD_LINHA", "" + cdLinha, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("A.CD_ROTA", "" + cdRota, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmHorarios = find(criterios);
					
					while (rsmHorarios.next()) {
						retorno = HorarioDAO.delete(cdHorario, cdTabelaHorario, cdLinha, cdRota, rsmHorarios.getInt("CD_TRECHO"), connect);
						
						if (retorno<=0)
							break;
					}
				}
			}
				
			
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
	
	public static Result removeLote(int cdTabelaHorario, int cdLinha, int cdRota){
		return removeLote(cdTabelaHorario, cdLinha, cdRota, null, null);
	}
	public static Result removeLote(int cdTabelaHorario, int cdLinha, int cdRota, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
					
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_TABELA_HORARIO", "" + cdTabelaHorario, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_LINHA", "" + cdLinha, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_ROTA", "" + cdRota, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmHorarios = find(criterios);
			
			while (rsmHorarios.next()) {
				retorno = HorarioDAO.delete(rsmHorarios.getInt("CD_HORARIO"), cdTabelaHorario, cdLinha, cdRota, rsmHorarios.getInt("CD_TRECHO"), connect);
				
				if (retorno<=0)
					break;
			}
			
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
			pstmt = connect.prepareStatement("SELECT A.*, B.*,C.*, "+
											 " D.cd_parada as cd_parada_partida, D.cd_grupo_parada as cd_grupo_parada_partida,             "+
											 " D.cd_concessao as cd_concessao_partida, D.cd_logradouro as cd_logradouro_partida,           "+
											 " D.ds_referencia as ds_referencia_partida, D.cd_georreferencia as cd_georreferencia_partida,   "+
											 " D2.cd_parada as cd_parada_chegada, D2.cd_grupo_parada as cd_grupo_parada_chegada,           "+
											 " D2.cd_concessao as cd_concessao_chegada, D2.cd_logradouro as cd_logradouro_chegada,         "+
											 " D2.ds_referencia as ds_referencia_chegada, D2.cd_georreferencia as cd_georreferencia_chegada  "+
											 " FROM mob_horario A "+
											 " JOIN mob_tabela_horario B on ( A.cd_tabela_horario = B.cd_tabela_horario "+
											 "                              AND A.cd_linha = B.cd_linha )               "+
											 " JOIN mob_linha C on ( A.cd_linha = C.cd_linha )               "+
											 " JOIN mob_parada D ON  ( A.cd_parada_partida = D.cd_parada )      "+
											 " JOIN mob_parada D2 ON ( A.cd_parada_chegada = D2.cd_parada ) ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getHorarios(int idTabela, int cdLinha, int cdRotaIda, int cdRotaVolta) {
		
		ResultSetMap rsmIda = TabelaHorarioRotaServices.getHorarios(idTabela, cdLinha, cdRotaIda);
		ResultSetMap rsmVolta = TabelaHorarioRotaServices.getHorarios(idTabela, cdLinha, cdRotaVolta);
		
		Result result = new Result(1);
		result.addObject("rsmHorariosIda", rsmIda);
		result.addObject("rsmHorariosVolta", rsmVolta);
		
		return result;
	}
	
    public static ResultSetMap getHorariosIda(int idTabela, int cdLinha, int cdRotaIda) {
	    	
			return getHorariosIda(idTabela, cdLinha, cdRotaIda);
	}
	
    public static ResultSetMap getHorariosIda(int idTabela, int cdLinha, int cdRotaIda, Connection connect) {
    	
    	boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		ResultSetMap rsmIda = TabelaHorarioRotaServices.getHorarios(idTabela, cdLinha, cdRotaIda);
		return rsmIda;
	}
    
    public static ResultSetMap getHorariosVolta(int idTabela, int cdLinha, int cdRotaVolta) {
  		ResultSetMap rsmVolta = TabelaHorarioRotaServices.getHorarios(idTabela, cdLinha, cdRotaVolta);
  		return rsmVolta;
  	}
	
	public static ResultSetMap getAllByTabelaHorarioRota( int cdLinha, int cdTabelaHorario, int cdRota) {
		return getAllByTabelaHorarioRota( cdLinha, cdTabelaHorario, cdRota, null);
	}

	public static ResultSetMap getAllByTabelaHorarioRota( int cdLinha, int cdTabelaHorario, int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, "+
											" B2.NM_GRUPO_PARADA AS NM_GRUPO_PARADA_PARTIDA,  "+
											" B3.NM_GRUPO_PARADA AS NM_GRUPO_PARADA_PARTIDA_SUPERIOR,  "+
											" C2.NM_GRUPO_PARADA AS NM_GRUPO_PARADA_CHEGADA,  "+
											" C3.NM_GRUPO_PARADA AS NM_GRUPO_PARADA_CHEGADA_SUPERIOR  "+
											" FROM MOB_HORARIO A "+
											
											" JOIN MOB_LINHA_TRECHO D ON (A.CD_LINHA = D.CD_LINHA AND A.CD_ROTA = D.CD_ROTA AND A.CD_TRECHO = D.CD_TRECHO) "+
											
											" LEFT JOIN MOB_PARADA B ON ( D.CD_PARADA = B.CD_PARADA ) "+
											" LEFT JOIN MOB_GRUPO_PARADA B2 ON ( B.CD_GRUPO_PARADA = B2.CD_GRUPO_PARADA ) "+
											" LEFT JOIN MOB_GRUPO_PARADA B3 ON ( B2.CD_GRUPO_PARADA_SUPERIOR = B3.CD_GRUPO_PARADA ) "+
											
											" LEFT JOIN MOB_PARADA C ON ( D.CD_PARADA = C.CD_PARADA ) "+
											" LEFT JOIN MOB_GRUPO_PARADA C2 ON ( C.CD_GRUPO_PARADA = C2.CD_GRUPO_PARADA ) "+
											" LEFT JOIN MOB_GRUPO_PARADA C3 ON ( C2.CD_GRUPO_PARADA_SUPERIOR = C3.CD_GRUPO_PARADA ) "+
											
											" WHERE A.CD_LINHA = " + cdLinha + 
											"   AND A.CD_TABELA_HORARIO = "+ cdTabelaHorario +
											"   AND A.CD_ROTA = " + cdRota + 
											" ORDER BY HR_PARTIDA, HR_CHEGADA ASC " 
											
										);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAllByTabelaHorarioRota: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! HorarioServices.getAllByTabelaHorarioRota: " + e);
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
		return Search.find("SELECT A.*, B.*,C.*,E.cd_parada "+
//						 " D.cd_parada as cd_parada_partida, D.cd_grupo_parada as cd_grupo_parada_partida,             "+
//						 " D.cd_concessao as cd_concessao_partida, D.cd_logradouro as cd_logradouro_partida,           "+
//						 " D.ds_referencia as ds_referencia_partida, D.cd_georreferencia as cd_georreferencia_partida,   "+
//						 " D2.cd_parada as cd_parada_chegada, D2.cd_grupo_parada as cd_grupo_parada_chegada,           "+
//						 " D2.cd_concessao as cd_concessao_chegada, D2.cd_logradouro as cd_logradouro_chegada,         "+
//						 " D2.ds_referencia as ds_referencia_chegada, D2.cd_georreferencia as cd_georreferencia_chegada  "+
						 " FROM mob_horario A "+
						 " JOIN mob_tabela_horario B on ( A.cd_tabela_horario = B.cd_tabela_horario "+
						 "                              AND A.cd_linha = B.cd_linha )               "+
						 " JOIN mob_linha C on ( A.cd_linha = C.cd_linha )               " +
						 " JOIN mob_linha_trecho E on (A.cd_linha = E.cd_linha"
						 							+ " AND A.cd_rota = E.cd_rota	"
						 							+ " AND A.cd_trecho = E.cd_trecho)",
//						 " JOIN mob_parada D ON  ( A.cd_parada_partida = D.cd_parada )      "+
//						 " JOIN mob_parada D2 ON ( A.cd_parada_chegada = D2.cd_parada ) ",
						   criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static boolean validaExistenciaHorarios(int cdLinha, int cdRota) {
		return validaExistenciaHorarios(0, cdLinha, cdRota, null);
	}
	
	public static boolean validaExistenciaHorarios(int cdTabelaHorario, int cdLinha, int cdRota ) {
		return validaExistenciaHorarios(cdTabelaHorario, cdLinha, cdRota, null);
	}
	
	public static boolean validaExistenciaHorarios(int cdTabelaHorario, int cdLinha, int cdRota, Connection connect ) {
		
		Criterios crt = new Criterios();
		
		if(cdTabelaHorario > 0)
			crt.add("cd_tabela_horario", Integer.toString(cdTabelaHorario), ItemComparator.EQUAL, Types.INTEGER);	
		if(cdLinha > 0)
			crt.add("cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);	
		if(cdRota > 0)
			crt.add("cd_rota", Integer.toString(cdRota), ItemComparator.EQUAL, Types.INTEGER);	

		
		
		ResultSetMap rsm =  Search.find("SELECT A.*  FROM mob_horario A ",
				crt, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		rsm.beforeFirst();
		
		return rsm.hasMore();
	}

}