package com.tivic.manager.srh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.sol.util.date.DateUtil;
import com.tivic.manager.grl.FeriadoServices;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.util.Result;

public class PontoServices {

	// Normal = Indica que o horário foi fechado normalmente pelo colaborador
	// 	O horário normal quando indicar hora extra deverá ser confirmado pelo supervisor
	public static final String[] statusPonto = {"Normal",
												"Aberto",
		                                        "Extra Pendente",
		                                        "Extra Confirmada",
		                                        "Extra Cancelada",
		                                        "Fechado(Supervisor)",
		                                        "Cancelado",
		                                        "Feriado",
		                                        "Repouso Semanal",
		                                        "Plantão",
		                                        "Falta",
		                                        "Horas a Menos"};

	public static final int ST_FECHADO = 0; // Deprecated

	public static final int ST_NORMAL  			  = 0;
	public static final int ST_ABERTO  			  = 1;
	public static final int ST_EXTRA_PENDENTE     = 2;
	public static final int ST_EXTRA_CONFIRMADA   = 3;
	public static final int ST_EXTRA_CANCELADA    = 4;
	public static final int ST_FECHADO_SUPERVIDOR = 5;
	public static final int ST_CANCELADO 		  = 6;
	public static final int ST_FERIADO 			  = 7;
	public static final int ST_REPOUSO_SEMANAL 	  = 8;
	public static final int ST_PLANTAO 	  		  = 9;
	public static final int ST_FALTA 	  		  = 10;
	public static final int ST_HORAS_AMENOS 	  = 11;


	public static int save(Ponto ponto){
		return save(ponto, null);
	}
	public static int save(Ponto ponto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno;
			if(ponto.getCdPonto()<=0){
				if((retorno = PontoDAO.insert(ponto, connect))<=0)
					retorno = -2;
			}
			else{
				if(PontoDAO.update(ponto, connect)>0)
					retorno = ponto.getCdPonto();
				else
					retorno = -3;
			}

			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.save: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.save: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static int registrar(String nmLogin, String nmSenha){
		return registrar(nmLogin, nmSenha, null);
	}
	public static int registrar(String nmLogin, String nmSenha, Connection connect){
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			pstmt = connect.prepareStatement("SELECT * " +
					"FROM SEG_USUARIO A, SRH_DADOS_FUNCIONAIS B " +
					"WHERE NM_LOGIN=? " +
					"  AND st_usuario = 1" +
					"  AND A.cd_pessoa = B.cd_pessoa ");
			pstmt.setString(1, nmLogin);
			rs = pstmt.executeQuery();
			int cdMatricula = -1;
			if(rs.next() && nmSenha.equals(rs.getString("NM_SENHA")))
				cdMatricula =  rs.getInt("CD_MATRICULA");


			int retorno = 0;
			if(cdMatricula!=0){
				retorno = registrar(cdMatricula, false, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.registrar: " + e);
			Conexao.rollback(connect);
			return -3;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> getUltimoPontoAberto(int cdMatricula, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;

			PreparedStatement pstmt = connection.prepareStatement("SELECT * " +
					"FROM srh_ponto A " +
					"WHERE A.cd_matricula = ? " +
					"  AND A.dt_fechamento IS NULL " +
					"  AND A.st_ponto <> ? " +
					"  AND NOT EXISTS (SELECT B.cd_ponto " +
					"				   FROM srh_ponto B " +
					"				   WHERE B.cd_matricula = A.cd_matricula " +
					"					 AND B.cd_ponto <> A.cd_ponto " +
					"					 AND B.dt_abertura > A.dt_abertura) "+
					"ORDER BY A.dt_abertura DESC ");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, ST_CANCELADO);
			ResultSet rs = pstmt.executeQuery();
			boolean next = rs.next();

			HashMap<String, Object> hash = !next ? null : new HashMap<String, Object>();
			if (hash!=null) {
				hash.put("cdPonto", rs.getInt("cd_ponto"));
				hash.put("dtAbertura", Util.convTimestampToCalendar(rs.getTimestamp("dt_abertura")));
				hash.put("stPonto", rs.getInt("st_ponto"));
			}

			return hash;
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.getUltimoPontoAberto: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int registrar(int cdMatricula){
		return registrar(cdMatricula, false, null);
	}

	public static int registrar(int cdMatricula, boolean forceInsert){
		return registrar(cdMatricula, forceInsert, null);
	}

	public static int registrar(int cdMatricula, boolean forceInsert, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (cdMatricula<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			PreparedStatement pstmt = connection.prepareStatement(
					 "SELECT * " +
					 "FROM srh_ponto A " +
					 "WHERE A.cd_matricula = " + cdMatricula +
					 "  AND A.dt_fechamento IS NULL " +
					 "  AND NOT EXISTS (SELECT B.cd_ponto " +
					 "					FROM srh_ponto B " +
					 "					WHERE B.cd_matricula = A.cd_matricula " +
					 "					  AND B.cd_ponto <> A.cd_ponto " +
					 "					  AND B.dt_abertura > A.dt_abertura) "+
					 "  AND A.st_ponto    <> "+ST_CANCELADO+
					 "ORDER BY A.dt_abertura DESC");
			ResultSet rs = pstmt.executeQuery();
			int cdPonto = !rs.next() ? 0 : rs.getInt("CD_PONTO");
			int cdRetorno = cdPonto==0 || forceInsert ? abrir(cdMatricula, connection) : fechar(cdMatricula, cdPonto, connection);

			if(cdRetorno<=0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return cdRetorno;
			}

			if (isConnectionNull)
				connection.commit();
			System.out.println("cdPonto="+cdPonto);
			return cdPonto==0 ? 1 : 2; //1=abrir / 2=fechar
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.registrar: " + e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -3;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int abrir(int cdMatricula){
		return abrir(cdMatricula, null);
	}

	public static int abrir(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdMatricula<=0)
				return -2;

			if (isConnectionNull)
				connect = Conexao.conectar();

			int retorno = PontoDAO.insert(new Ponto(0, cdMatricula, DateUtil.getDataAtual(), null, 1, ""), connect);

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.abrir: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	//fecha o ultimo ponto aberto
	public static int fechar(int cdMatricula){
		return fechar(cdMatricula, null);
	}

	public static int fechar(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdMatricula<=0)
				return -3;

			if (isConnectionNull)
				connect = Conexao.conectar();

			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM srh_ponto " +
					" WHERE cd_matricula = " + cdMatricula +
					"   AND dt_fechamento IS NULL " +
					"   AND st_ponto <> "+ST_CANCELADO+
					" ORDER BY dt_abertura DESC");
			ResultSet rs = pstmt.executeQuery();

			int cdPonto = 0;
			if(rs.next())
				cdPonto = rs.getInt("CD_PONTO");

			return fechar(cdMatricula, cdPonto, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.fechar: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	//fecha um ponto especifico
	public static int fechar(int cdMatricula, int cdPonto){
		return fechar(cdMatricula, cdPonto, null);
	}

	public static int fechar(int cdMatricula, int cdPonto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdMatricula<=0)
				return -3;

			if (isConnectionNull)
				connect = Conexao.conectar();

			Ponto ponto = PontoDAO.get(cdPonto, cdMatricula, connect);

			if(ponto==null)
				return -2;

			ponto.setDtFechamento(DateUtil.getDataAtual());
			ponto.setStPonto(ST_NORMAL);

			int retorno = PontoDAO.update(ponto, connect);

			return (retorno>0)?cdPonto:retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.fechar: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	//cancela um ponto especifico
	public static int cancelar(int cdMatricula, int cdPonto){
		return cancelar(cdMatricula, cdPonto, null);
	}

	public static int cancelar(int cdMatricula, int cdPonto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if(cdMatricula<=0)
				return -3;

			if (isConnectionNull)
				connect = Conexao.conectar();

			Ponto ponto = PontoDAO.get(cdPonto, cdMatricula, connect);

			if(ponto==null)
				return -2;

			ponto.setStPonto(ST_CANCELADO);

			int retorno = PontoDAO.update(ponto, connect);

			return (retorno>0)?cdPonto:retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PontoServices.cancelar: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findDadosFuncionariosByPonto(int cdPonto){
		return findDadosFuncionariosByPonto(cdPonto, null);
	}
	public static ResultSetMap findDadosFuncionariosByPonto(int cdPonto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT C.*, B.* " +
											 "FROM srh_ponto A, srh_dados_funcionais B, grl_pessoa C " +
											 "WHERE A.cd_matricula = B.cd_matricula " +
											 " AND B.cd_pessoa = C.cd_pessoa " +
											 " AND A.cd_ponto = "+cdPonto);
			return new ResultSetMap(pstmt.executeQuery());

		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.findDadosFuncionariosByPonto: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findDadosFuncionariosByPonto(int cdPonto, String nmLogin, String nmSenha){
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " +
					"FROM SEG_USUARIO A, SRH_DADOS_FUNCIONAIS B " +
					"WHERE NM_LOGIN=? " +
					"  AND st_usuario = 1" +
					"  AND A.cd_pessoa = B.cd_pessoa ");
			pstmt.setString(1, nmLogin);
			ResultSet rs = pstmt.executeQuery();
			int cdMatricula = -1;
			if(rs.next() && nmSenha.equals(rs.getString("NM_SENHA")))
				cdMatricula =  rs.getInt("CD_MATRICULA");
			
			pstmt = connect.prepareStatement("SELECT C.*, B.* " +
											 "FROM srh_ponto A, srh_dados_funcionais B, grl_pessoa C " +
											 "WHERE A.cd_matricula = B.cd_matricula " +
											 " AND B.cd_pessoa = C.cd_pessoa " +
											 " AND A.cd_ponto = "+cdPonto +" AND A.cd_matricula="+cdMatricula);
			return new ResultSetMap(pstmt.executeQuery());

		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.findDadosFuncionariosByPonto: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findPontosAbertos(int cdMatricula){
		return findPontosAbertos(cdMatricula, null);
	}
	public static ResultSetMap findPontosAbertos(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar hoje = new GregorianCalendar();
			hoje.set(GregorianCalendar.HOUR, 0);
			hoje.set(GregorianCalendar.MINUTE, 0);
			hoje.set(GregorianCalendar.SECOND, 0);
			hoje.set(GregorianCalendar.MILLISECOND, 0);
			pstmt = connect.prepareStatement("SELECT * FROM srh_ponto " +
					 " WHERE cd_matricula = " + cdMatricula +
					 "   AND st_ponto = 1 " +
					 "   AND dt_abertura < ? " +
					 " ORDER BY dt_abertura DESC");
			pstmt.setTimestamp(1, new Timestamp(hoje.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.findPontosAbertos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findPontosByMatricula(int cdMatricula){
		return findPontosByMatricula(cdMatricula, null);
	}
	public static ResultSetMap findPontosByMatricula(int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar hoje = new GregorianCalendar();
			hoje.set(GregorianCalendar.HOUR, 0);
			hoje.set(GregorianCalendar.MINUTE, 0);
			hoje.set(GregorianCalendar.SECOND, 0);
			hoje.set(GregorianCalendar.MILLISECOND, 0);

			pstmt = connect.prepareStatement("SELECT * " +
					"FROM SRH_PONTO " +
					"WHERE dt_abertura > ? " +
					" AND cd_matricula = ?" +
					" ORDER BY dt_abertura DESC");

			pstmt.setTimestamp(1, new Timestamp(hoje.getTimeInMillis()));
			pstmt.setInt(2, cdMatricula);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.findDadosFuncionariosByPonto: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result find(int cdEmpresa, int cdMatricula, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return find(cdEmpresa, cdMatricula, dtInicial, dtFinal, null);
	}
	@SuppressWarnings("unchecked")
	public static Result find(int cdEmpresa, int cdMatricula, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect)	{
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = new ResultSetMap();
			//buscando usuarios
			ResultSetMap rsmFunc = new ResultSetMap(connect.prepareStatement("SELECT a.cd_matricula, a.cd_empresa, a.nr_matricula, a.cd_tabela_horario," +
																			 "b.nm_pessoa, b.cd_pessoa, c.nm_pessoa AS nm_empresa" +
																			 "	FROM srh_dados_funcionais a, grl_pessoa b, grl_pessoa c	" +
																			 "	WHERE a.cd_pessoa=b.cd_pessoa" +
																			 "  AND a.cd_empresa = c.cd_pessoa" +
																			 "  AND a.st_funcional = "+DadosFuncionaisServices.sfATIVO+
																			 (cdEmpresa>0  ? " AND A.cd_empresa   = "+cdEmpresa   : "")+
																			 (cdMatricula>0? " AND A.cd_matricula = "+cdMatricula : "")).executeQuery());

			// Pontos (Jornadas)
			PreparedStatement pstmtPonto = connect.prepareStatement("SELECT * FROM srh_ponto " +
				             									    "WHERE cd_matricula = ? " +
				             									    "  AND CAST(dt_abertura AS DATE) = ? " +
				             									    //"  AND dt_fechamento IS NOT NULL " +
				             									    "ORDER BY dt_abertura, dt_fechamento");
			// Tabela de Horário
			PreparedStatement pstmtHorario = connect.prepareStatement("SELECT * FROM srh_horario " +
				             									      "WHERE cd_tabela_horario = ? "+
				             									      "  AND nr_dia_semana     = ? " +
				             									      "  AND hr_entrada IS NOT NULL " +
				             									      "  AND hr_saida IS NOT NULL");

			GregorianCalendar dtDia = new GregorianCalendar(dtInicial.get(Calendar.YEAR), dtInicial.get(Calendar.MONTH), dtInicial.get(Calendar.DATE));
			while(rsmFunc.size()>0 && !dtDia.after(dtFinal))	{
				// Verifica se foi feriado
				boolean isFeriado = FeriadoServices.isFeriado(dtDia, connect);
				// Verifica os horários de todos os funcionários selecionados
				rsmFunc.beforeFirst();
				while(rsmFunc.next())	{
					int nrJornada = 1;
					// Prepara registro do funcionário no dia
					HashMap<String,Object> reg = new HashMap<String,Object>();
					reg.put("CD_PESSOA", rsmFunc.getString("cd_pessoa"));
					reg.put("NM_PESSOA", rsmFunc.getString("nm_pessoa"));
					reg.put("CD_MATRICULA", rsmFunc.getString("cd_matricula"));
					reg.put("NR_MATRICULA", rsmFunc.getString("nr_matricula"));
					reg.put("NM_EMPRESA", rsmFunc.getString("nm_empresa"));
					reg.put("CL_DIA", Util.formatDate(dtDia, "dd/MM")+"-"+com.tivic.manager.util.Recursos.diasSemana[dtDia.get(Calendar.DAY_OF_WEEK)-1]);
					reg.put("DT_DIA", Util.formatDate(dtDia, "dd/MM/yyyy HH:mm:ss"));
					reg.put("NR_JORNADA", "1ª");
					// Busca horários do funcionario
					ArrayList<Integer> minJornadas = new ArrayList<Integer>();
					int minDia = 0;
					pstmtHorario.setInt(1, rsmFunc.getInt("cd_tabela_horario"));
					pstmtHorario.setInt(2, dtDia.get(Calendar.DAY_OF_WEEK));
					ResultSet rsHorario = pstmtHorario.executeQuery();
					boolean isRSR = false, isFALTA = false;
					while(rsHorario.next())	{
						if(rsHorario.getInt("tp_horario")==TabelaHorarioServices.TP_NORMAL && !isFeriado)	{
							GregorianCalendar entrada = Util.convTimestampToCalendar(rsHorario.getTimestamp("hr_entrada"));
							GregorianCalendar saida   = Util.convTimestampToCalendar(rsHorario.getTimestamp("hr_saida"));
							minDia 					 += Math.round((saida.getTimeInMillis() - entrada.getTimeInMillis())  / 1000 / 60);
							minJornadas.add(new Integer(Math.round((saida.getTimeInMillis() - entrada.getTimeInMillis())  / 1000 / 60)));
							isFALTA = true;
						}
						isRSR =  isRSR || rsHorario.getInt("tp_horario")==TabelaHorarioServices.TP_RSR;
					}
					reg.put("MIN_DIA", new Integer(minDia));
					reg.put("ST_PONTO", isRSR ? ST_REPOUSO_SEMANAL : (isFeriado ? ST_FERIADO : (isFALTA ?  ST_FALTA : ST_NORMAL)));
					// Busca pontos do funcionario no dia
					boolean hasPontoDia = false;
					pstmtPonto.setInt(1, rsmFunc.getInt("cd_matricula"));
					pstmtPonto.setTimestamp(2, new Timestamp(dtDia.getTimeInMillis()));
					ResultSetMap rsmPonto = new ResultSetMap(pstmtPonto.executeQuery());
					int minTrabDia    = 0;
					while(rsmPonto.next())	{
						GregorianCalendar entrada = rsmPonto.getGregorianCalendar("dt_abertura");
						GregorianCalendar saida   = rsmPonto.getGregorianCalendar("dt_fechamento");
						verifyHour(entrada, saida);
						int minTrabJornada = 0;

						if(saida!=null)
							minTrabJornada = Math.round((saida.getTimeInMillis() - entrada.getTimeInMillis()) / 1000 / 60);

						if(rsmPonto.getInt("ST_PONTO")!=ST_CANCELADO && saida!=null)
							minTrabDia += minTrabJornada;

						HashMap<String,Object> regPonto = (HashMap<String,Object>)reg.clone();
						regPonto.put("CD_PONTO", rsmPonto.getInt("CD_PONTO"));
						regPonto.put("ST_PONTO", rsmPonto.getInt("ST_PONTO"));
						regPonto.put("CD_MATRICULA", rsmFunc.getString("CD_MATRICULA"));
						regPonto.put("TXT_OBSERVACAO", rsmPonto.getString("TXT_OBSERVACAO"));
						regPonto.put("DT_ABERTURA", entrada);
						regPonto.put("DT_FECHAMENTO", saida);
						regPonto.put("NR_JORNADA", nrJornada+"ª");
						//
						regPonto.put("MIN_JORNADA", new Integer(minTrabJornada));
						regPonto.put("CL_HORAS_JORNADA", new Integer(minTrabJornada / 60).intValue()+"h "+(minTrabJornada % 60)+"m");
						regPonto.put("MIN_TRABALHADO_JORNADA", new Integer(minTrabJornada));
						regPonto.put("MIN_TRABALHADO_DIA", new Integer(minTrabDia));

						if(rsmPonto.getPosition()==rsmPonto.size()-1)	{
							//if(((Integer)regPonto.get("ST_PONTO")).intValue()==ST_FALTA)
								//regPonto.put("ST_PONTO", ST_NORMAL);
							regPonto.put("CL_HORAS_DIA", new Integer(minTrabDia / 60).intValue()+"h "+(minTrabDia % 60)+"m");
							regPonto.put("CL_EXTRA_FALTA", new Integer((minTrabDia - minDia) / 60).intValue()+"h "+(Math.abs(minTrabDia - minDia) % 60)+"m");
							regPonto.put("MIN_EXTRA_FALTA", new Integer(minTrabDia - minDia));
						}
						//else
							//regPonto.put("ST_PONTO", -1);
						if(nrJornada>1)
							regPonto.put("CL_DIA", null);
						hasPontoDia = true;
						nrJornada++;
						rsm.addRegister(regPonto);
					}
					if(!hasPontoDia)	{
						HashMap<String,Object> regPonto = (HashMap<String,Object>)reg.clone();
						if(!isFeriado && !isRSR)	{
							regPonto.put("MIN_EXTRA_FALTA", new Integer(minTrabDia - minDia));
							regPonto.put("CL_EXTRA_FALTA", new Integer(-1 * minDia / 60).intValue()+"h "+(minDia % 60)+"m");
						}
						rsm.addRegister(regPonto);
					}
				}
				// Incrementa o dia
				dtDia.add(Calendar.DATE, 1);
			}
			ArrayList<String> columns = new ArrayList<String>();
			columns.add("NM_PESSOA");
			rsm.orderBy(columns);
			return new Result(1, "Pesquisa realizada com sucesso!", "rsm", rsm);
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! PontoServices.findFolhaMensal: " + e);
			return new Result(-1, "Erro ao tentar pesquisar dados!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result verifyHour(GregorianCalendar hrEntrada, GregorianCalendar hrSaida)	{
		if(hrEntrada==null || hrSaida==null)
			return new Result(1);
		// Variáveis
		GregorianCalendar dt_entrada, dt_saida;
		int nrDia, qtDias, iMinutos_noturnos, iMinutos_diurnos, nrDiaSemana;
		float vlHoraE, vlHoraS;
		GregorianCalendar dtPonto;

		int[][] iMinutos = {new int[]{0,0}, new int[]{0,0}};
		int cNOT = HorarioServices.TP_NOTURNO;
		int cDIU = HorarioServices.TP_DIURNO;
		int cSEM = HorarioServices.TP_NORMAL;
		int cDOM = HorarioServices.TP_RSR;
		iMinutos[cNOT][cSEM] = 0;
		iMinutos[cDIU][cSEM] = 0;
		iMinutos[cNOT][cDOM] = 0;
		iMinutos[cDIU][cDOM] = 0;
		dt_entrada  = hrEntrada;
		dt_saida    = hrSaida;
		dtPonto     = (GregorianCalendar)hrEntrada.clone();
		nrDiaSemana = hrEntrada.get(Calendar.DAY_OF_WEEK);
		qtDias      = hrSaida.get(Calendar.DAY_OF_YEAR) - hrEntrada.get(Calendar.DAY_OF_YEAR);
		for(nrDia=0; nrDia<=qtDias; nrDia++)	{
			iMinutos_noturnos = 0;
			iMinutos_diurnos  = 0;
	    	// Se o dia não é o da entrada, conta desde o primeiro minuto do dia
	    	if (nrDia != 0)	{
	    		dt_entrada = (GregorianCalendar)hrEntrada.clone();
	    		dt_entrada.add(Calendar.DATE, nrDia);
	    		dt_entrada.set(Calendar.HOUR_OF_DAY, 0);
	    		dt_entrada.set(Calendar.MINUTE, 0);
	    		dt_entrada.set(Calendar.SECOND, 0);
	    	}
		    // Se a saída não for no mesmo dia da entrada
	    	dt_saida = (GregorianCalendar)hrEntrada.clone();
	    	dt_saida.add(Calendar.DATE, nrDia);
	    	nrDiaSemana = dtPonto.get(Calendar.DAY_OF_WEEK);
	    	if (nrDia < qtDias)	{
	    		dt_saida.set(Calendar.HOUR_OF_DAY, 23);
	    		dt_saida.set(Calendar.MINUTE, 59);
	    		dt_saida.set(Calendar.SECOND, 59);
	    	}
	    	else
	    		dt_saida = hrSaida;
		    vlHoraE = dt_entrada.get(Calendar.HOUR_OF_DAY)+((float)dt_entrada.get(Calendar.MINUTE)/60);
		    vlHoraS = dt_saida.get(Calendar.HOUR_OF_DAY)+((float)dt_saida.get(Calendar.MINUTE)/60);
		    // Se entrou entre 00:00 e 05:00 (madrugada)
		    if(vlHoraE < 5/*5:00am*/) {
		    	// Se saiu depois da cinco horas, conta como noturna somente as horas até 05:00 da manhã
		    	if(vlHoraS > 5/*5:00am*/) {
		    		iMinutos_noturnos = Math.round((float)(5/*5:00am*/ - vlHoraE) * 60);
		    		dt_entrada.set(Calendar.HOUR_OF_DAY, 5);
		    		dt_entrada.set(Calendar.MINUTE, 0);
		    		dt_entrada.set(Calendar.SECOND, 0);
		    	}
		    	else
		    		iMinutos_noturnos = Math.round((float)(vlHoraS - vlHoraE) * 60)+(nrDia < qtDias ? 1 : 0);
		    }
		    vlHoraE = dt_entrada.get(Calendar.HOUR_OF_DAY)+((float)dt_entrada.get(Calendar.MINUTE)/60);
		    vlHoraS = dt_saida.get(Calendar.HOUR_OF_DAY)+((float)dt_saida.get(Calendar.MINUTE)/60);
		    // Se entrou entre 5 e 22 horas
		    if((vlHoraE >= 5/*5:00am*/) && (vlHoraE < 22/*22:00*/))	{
		    	if(vlHoraS > 22/*22:00*/)	{
		    		iMinutos_diurnos = Math.round((float)(22/*22:00*/ - vlHoraE) * 60);
		    		dt_entrada.set(Calendar.HOUR_OF_DAY, 22);
		    		dt_entrada.set(Calendar.MINUTE, 0);
		    		dt_entrada.set(Calendar.SECOND, 0);
		    		vlHoraE = dt_entrada.get(Calendar.HOUR_OF_DAY)+((float)dt_entrada.get(Calendar.MINUTE)/60);
		    	}
		    	else
		    		iMinutos_diurnos = Math.round((float)(vlHoraS - vlHoraE) * 60);
		    }
		    if(vlHoraE >= 22/*22:00*/)
		    	iMinutos_noturnos += Math.round((float)(vlHoraS - vlHoraE) * 60)+(nrDia < qtDias ? 1 : 0);
		    /* DOMINGO OU FERIADO */
		    if((nrDiaSemana==1) || FeriadoServices.isFeriado(dtPonto))	{
		    	iMinutos[cNOT][cDOM] += iMinutos_noturnos;
		    	iMinutos[cDIU][cDOM] += iMinutos_diurnos;
		    }
			else	{
				iMinutos[cNOT][cSEM] += iMinutos_noturnos;
				iMinutos[cDIU][cSEM] += iMinutos_diurnos;
			}
		};
		Result result = new Result(1);
		result.addObject("iMinutos[cDIU][cSEM]", iMinutos[cDIU][cSEM]);
		result.addObject("iMinutos[cNOT][cSEM]", iMinutos[cNOT][cSEM]);
		result.addObject("iMinutos[cDIU][cDOM]", iMinutos[cDIU][cDOM]);
		result.addObject("iMinutos[cNOT][cDOM]", iMinutos[cNOT][cDOM]);
		return result;
	};
}