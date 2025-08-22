package com.tivic.manager.agd;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.util.Util;

public class LocalHorarioServices {
	
	public static final int ST_PRIVADO = 0;
	public static final int ST_PUBLICO = 1;

	public static final String[] situacaoHorario = {"Privado", "Público"};
	
	public static Result save(LocalHorario localHorario){
		return save(localHorario, null);
	}

	public static Result save(LocalHorario localHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(localHorario==null)
				return new Result(-1, "Erro ao salvar. LocalHorario é nulo");

			int retorno;
			if(localHorario.getCdHorario()==0){
				retorno = LocalHorarioDAO.insert(localHorario, connect);
				localHorario.setCdHorario(retorno);
			}
			else {
				retorno = LocalHorarioDAO.update(localHorario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOCALHORARIO", localHorario);
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
	
	public static Result saveAll(int cdLocal, ArrayList<LocalHorario> horarios) {
		return saveAll(cdLocal, horarios, null);
	}

	public static Result saveAll(int cdLocal, ArrayList<LocalHorario> horarios, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			Local local = LocalDAO.get(cdLocal, connection);
			if (local == null) {
				return new Result(-2, "Local indicado não existe.");
			}
			
			for (int i=0; horarios!=null && i<horarios.size(); i++) {
				if(horarios.get(i).getCdHorario() <= 0){
					if (LocalHorarioDAO.insert(horarios.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao salvar horários");
					}
				}
				else{
					if (LocalHorarioDAO.update(horarios.get(i), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao atualizar horários");
					}
				}
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return new Result(1, "Horários salvos com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.saveAll: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, "Erro ao salvar horários");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result remove(int cdHorario){
		return remove(cdHorario, false, null);
	}
	public static Result remove(int cdHorario, boolean cascade){
		return remove(cdHorario, cascade, null);
	}
	public static Result remove(int cdHorario, boolean cascade, Connection connect){
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
			retorno = LocalHorarioDAO.delete(cdHorario, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_local_horario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para pegar todos os horario de acordo o local
	 * @param cdLocal
	 * @return
	 */
	public static ResultSetMap getAllByLocal(int cdLocal) {
		return getAllByLocal(cdLocal, null);
	}
	
	public static ResultSetMap getAllByLocal(int cdLocal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " + 
											" FROM agd_local_horario" +
											" WHERE cd_local = " + cdLocal +
											" ORDER BY nr_dia_semana, hr_inicio");
			
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para pegar todos os horarios do local de acordo o dia da semana
	 * @param nrDiaSemana
	 * @param cdLocal
	 * @return itens
	 */
	public static ArrayList<LocalHorario> getAllByDia(int nrDiaSemana, int cdLocal) {
		return getAllByDia(nrDiaSemana, cdLocal, null);
	}

	public static ArrayList<LocalHorario> getAllByDia(int nrDiaSemana, int cdLocal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " +
											" FROM agd_local_horario" +
											" WHERE cd_local = " + cdLocal +
											" AND nr_dia_semana = " + nrDiaSemana +
											" ORDER BY hr_inicio");
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<LocalHorario> itens = new ArrayList<LocalHorario>();
			LocalHorario c;
			
			while(rs.next()){
				c = LocalHorarioDAO.get(rs.getInt("cd_horario"), connect);
				itens.add(c);
			}
			rs.close();
			
			return itens;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAllByDia: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAllByDia: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método para pegar todos os horarios de um tipo de local, de acordo o dia da semana
	 * @param nrDiaSemana
	 * @param cdTipoLocal
	 * @return 
	 */
	public static ArrayList<LocalHorario> getAllHorariosByTipoLocal(int cdTipoLocal, int nrDiaSemana) {
		return getAllHorariosByTipoLocal(cdTipoLocal, nrDiaSemana, 1, null);
	}
	
	public static ArrayList<LocalHorario> getAllHorariosByTipoLocal(int cdTipoLocal, int nrDiaSemana, int stHorario) {
		return getAllHorariosByTipoLocal(cdTipoLocal, nrDiaSemana, stHorario, null);
	}

	public static ArrayList<LocalHorario> getAllHorariosByTipoLocal(int cdTipoLocal, int nrDiaSemana, int stHorario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT A.*" +
					" FROM agd_local_horario A" +
					" LEFT OUTER JOIN agd_local B ON (A.cd_local = B.cd_local)" +
					" WHERE B.cd_tipo_local = " + cdTipoLocal +
					" AND nr_dia_semana = " + nrDiaSemana +
					(stHorario == 1 ? " AND st_horario = " + stHorario : "" )+
					" ORDER BY A.hr_inicio";
					
			pstmt = connect.prepareStatement(sql);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			
			ArrayList<LocalHorario> itens = new ArrayList<LocalHorario>();
			LocalHorario c;
			
			while(rsm.next()){
				c = LocalHorarioDAO.get(rsm.getInt("cd_horario"), connect);
				itens.add(c);
			}
			//rsm.close();
			
			return itens;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAllHorarioByTipoLocal: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalHorarioServices.getAllHorarioByTipoLocal: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
	/**
	 * Método para retornar uma lista de horarios disponível mais proximo da data, buscando em todos locais do mesmo tipo
	 * USAR getDisponiveis
	 */
	@Deprecated
	public static ResultSetMap getDiaDisponivel(int dia, int mes, int ano, int nrDiaSemana, int cdTipoLocal, int cdEmpresa) {
		return getDiaDisponivel(dia, mes, ano, nrDiaSemana, cdTipoLocal, 1, cdEmpresa);
	}
	
	@Deprecated
	public static ResultSetMap getDiaDisponivel(int dia, int mes, int ano, int nrDiaSemana, int cdTipoLocal, int stHorario, int cdEmpresa) {
		GregorianCalendar dt = new GregorianCalendar(ano, mes, dia);
		
		ResultSetMap rsmHorariosDisponiveis = new ResultSetMap();
		ArrayList<LocalHorario> localHorario = new ArrayList<LocalHorario>();
		
		ResultSetMap rsm = new ResultSetMap();
				
		while (rsm.size() < 25){
			String data = Util.formatDateTime(dt, "dd/MM/yyyy");
			
			if (!FeriadoServices.isFeriado(dt)) {
				
				// Busca todas as agendas do dia
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("P.CD_TIPO_LOCAL", Integer.toString(cdTipoLocal), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.DT_INICIAL", data +" 00:00:00", ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
				criterios.add(new ItemComparator("A.DT_INICIAL", data +" 23:59:59", ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));	
				criterios.add(new ItemComparator("A.CD_EMPRESA", Integer.toString(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("orderByField", "A.DT_INICIAL", ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmOcupados = AgendaItemServices.getList(criterios);
				// Busca todos os horarios de funcionamento do local para o dia da semana solicitado
				localHorario = getAllHorariosByTipoLocal(cdTipoLocal, dt.get(Calendar.DAY_OF_WEEK)-1, stHorario);
				
				// Para cada horario de funcionamento encontrado é acrescentado o dia, mês, ano e local em rsmHorariosDisponiveis
				for (LocalHorario lh : localHorario) {
					dt = new GregorianCalendar(dt.get(Calendar.YEAR), dt.get(Calendar.MONTH), dt.get(Calendar.DAY_OF_MONTH), lh.getHrInicio().get(Calendar.HOUR_OF_DAY), lh.getHrInicio().get(Calendar.MINUTE));
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("DT_INICIAL", Util.convCalendarToTimestamp(dt));
					register.put("CD_LOCAL", lh.getCdLocal());
					
					rsmHorariosDisponiveis.addRegister(register);
				}
				rsmHorariosDisponiveis.beforeFirst();
				
				// Percorre a lista de horarios disponiveis
				while (rsmHorariosDisponiveis.next()) {
					
					boolean incluir = true;
					while (rsmOcupados.next()) {
						
						if (rsmOcupados.getInt("CD_LOCAL") == rsmHorariosDisponiveis.getInt("CD_LOCAL") &&
							Util.convCalendarStringCompleto(rsmOcupados.getGregorianCalendar("DT_INICIAL")).equals(Util.convCalendarStringCompleto(rsmHorariosDisponiveis.getGregorianCalendar("DT_INICIAL")))) {
							
							rsmOcupados.deleteRow();
							incluir = false;
							break;
						}

					}
					/**
					 * VERIFICAR NOS HORARIOS BLOQUEADOS
					 */
					
					if(incluir) {
						incluir = !LocalHorarioBloqueadoServices.verificarBloqueados(rsmHorariosDisponiveis.getInt("CD_LOCAL"), rsmHorariosDisponiveis.getGregorianCalendar("DT_INICIAL"));
					}
					
					if(incluir) {
						rsm.addRegister(rsmHorariosDisponiveis.getRegister());
					}
					rsmOcupados.beforeFirst();
				}
			}
			
			rsmHorariosDisponiveis = new ResultSetMap();
			dt.add(Calendar.DAY_OF_MONTH, 1);
		}
		return rsm;
	}
	
	/**
	 * Método para buscar os 25 proximos horario disponiveis
	 * @return
	 */
	public static ResultSetMap getDisponiveis(int dia, int mes, int ano, int nrDiaSemana, int cdTipoLocal, int stHorario) {
		GregorianCalendar dt = new GregorianCalendar(ano, mes, dia);
		
		ArrayList<LocalHorario> localHorario = new ArrayList<LocalHorario>();
		ResultSetMap rsmDisponiveis = new ResultSetMap();
				
		while (rsmDisponiveis.size() < 25){
			if (!FeriadoServices.isFeriado(dt)) {
				
				// Busca todos os horarios de funcionamento do local para o dia da semana solicitado
				localHorario = getAllHorariosByTipoLocal(cdTipoLocal, dt.get(Calendar.DAY_OF_WEEK)-1, stHorario);
				
				// Para cada horario de funcionamento encontrado é acrescentado o dia, mês e ano
				for (LocalHorario lh : localHorario) {
					
					dt = new GregorianCalendar(dt.get(Calendar.YEAR), dt.get(Calendar.MONTH), dt.get(Calendar.DAY_OF_MONTH), lh.getHrInicio().get(Calendar.HOUR_OF_DAY), lh.getHrInicio().get(Calendar.MINUTE));
					boolean incluir = true;
					
					// Verificar se o horario esta bloqueado
					incluir = !LocalHorarioBloqueadoServices.isBloqueado(lh.getCdLocal(), dt);
					
					// Verificar se existe agenda no horario
					if (incluir)
						incluir = !AgendaItemServices.isOcupado(lh.getCdLocal(), dt);
						
					// Inclui na lista de disponiveis o horario
					if(incluir) {
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("DT_INICIAL", Util.convCalendarToTimestamp(dt));
						register.put("CD_LOCAL", lh.getCdLocal());
						
						rsmDisponiveis.addRegister(register);
					}
				}
			}
			dt.add(Calendar.DAY_OF_MONTH, 1);
		}		
		return rsmDisponiveis;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM agd_local_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
	