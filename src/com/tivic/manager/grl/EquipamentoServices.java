package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.msg.Notificacao;
import com.tivic.manager.msg.NotificacaoServices;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

public class EquipamentoServices {
	
	public static final int TALONARIO_ELETRONICO = 0;
	public static final int SEMAFORO = 1;
	public static final int RADAR_FIXO = 2;
	public static final int RADAR_MOVEL = 3;
	public static final int GPS = 4;
	public static final int TAXIMETRO = 5;
	public static final int IMPRESSORA = 6;
	public static final int FISCALIZADOR = 7;
	public static final int TACOGRAFO = 8;
	public static final int CAMERA = 9;
	public static final int RADAR_ESTATICO = 10;
	public static final int DETECTOR = 11;
	
	public static final int INATIVO = 0;
	public static final int ATIVO = 1;
	
	public static final String[] tiposEquipamento  = {
			"Talonário Eletrônico", "Semáforo", "Radar Fixo", 
			"Radar Móvel", "GPS", "Taxímetro", "Impressora", 
			"Fiscalizador", "Tacógrafo", "Vídeo-monitoramento", 
			"Radar Estático", "Detector"};

	public static final String[] situacoesEquipamento  = {"Inativo", "Ativo"};
	
	public static Result save(Equipamento equipamento){
		return save(equipamento, null);
	}
	
	public static Result save(Equipamento equipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(equipamento==null)
				return new Result(-1, "Erro ao salvar. Equipamento é nulo");
			
			int retorno;
			if(equipamento.getCdEquipamento() == 0) {
				retorno = EquipamentoDAO.insert(equipamento, connect);
				equipamento.setCdEquipamento(retorno);
			}
			else {
				retorno = EquipamentoDAO.update(equipamento, connect);
			}
			
			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EQUIPAMENTO", equipamento);
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
	
	public static Result registrar(Equipamento equipamento) {
		return registrar(equipamento, null);
	}

	public static Result registrar(Equipamento equipamento, Connection connect)	{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			if(equipamento==null)
				return new Result(-1, "Equipamento é nulo.");
			
			if(equipamento.getNmEquipamento()==null || equipamento.getNmEquipamento().equals(""))
				return new Result(-2, "Nome de equipamento inválido.");
			
			if(equipamento.getIdEquipamento()==null || equipamento.getIdEquipamento().equals(""))
				return new Result(-2, "ID de equipamento inválido.");
					
			boolean lgAtivarEquipamento = ParametroServices.getValorOfParametroAsInteger("LG_ATIVAR_EQUIPAMENTO", 0, 0, connect)==EquipamentoServices.ATIVO;
			equipamento.setStEquipamento(lgAtivarEquipamento ? EquipamentoServices.ATIVO : EquipamentoServices.INATIVO);
			
			Result result = save(equipamento, connect);
			
			
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); //Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			if(!lgBaseAntiga){
				int retornoCdEquipamento =	com.tivic.manager.mob.EquipamentoDAO.insertCdEquipamento(equipamento.getCdEquipamento(), connect);
				
				if(retornoCdEquipamento < 0)				
					return new Result(-1, "Erro ao registrar código de equipamento nas table mob_equipamento.");
				
			}
			
			if(result.getCode()>0) {
				return new Result((lgAtivarEquipamento ? 1 : 2), 
						"Equipamento registrado. "+ (lgAtivarEquipamento ? "" : "Aguarde a ativação por um de nossos operadores."), 
						"EQUIPAMENTO", equipamento);
			}
			else
				return new Result(-1, "Erro ao registrar aquipamento.");
		}
		catch(Exception e) {
			e.printStackTrace(java.lang.System.out);
			Util.registerLog(e);
			return new Result(-4, "Erro ao autenticar...");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(Equipamento equipamento) {
		return remove(equipamento.getCdEquipamento());
	}
	
	public static Result remove(int cdAreaDireito){
		return remove(cdAreaDireito, false, null);
	}
	
	public static Result remove(int cdAreaDireito, boolean cascade){
		return remove(cdAreaDireito, cascade, null);
	}
	
	public static Result remove(int cdAreaDireito, boolean cascade, Connection connect){
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
				retorno = EquipamentoDAO.delete(cdAreaDireito, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta área do direito está vinculada a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Equipamento excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir área do direito!");
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
			
			String sql = " SELECT A.*, B.cd_usuario "
					+ " FROM grl_equipamento A"
					+ " LEFT OUTER JOIN seg_usuario B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " ORDER BY A.nm_equipamento";
			
			if(Util.isStrBaseAntiga()) {
				sql = " SELECT A.*, B.cod_usuario "
						+ " FROM grl_equipamento A"
						+ " LEFT OUTER JOIN usuario B ON (A.cd_equipamento = B.cd_equipamento)"
						+ " ORDER BY A.nm_equipamento";
			}
			
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

	public static ResultSetMap getAllRadarFtp() {
		return getAllRadarFtp(null);
	}

	public static ResultSetMap getAllRadarFtp(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					  " SELECT A.*, B.cd_usuario "
					+ " FROM grl_equipamento A"
					+ " LEFT OUTER JOIN seg_usuario B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " WHERE A.tp_equipamento = "+ RADAR_FIXO +" AND  A.lg_sync_ftp = 1 "
					+ " ORDER BY A.nm_equipamento");
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
	
	public static ResultSetMap getAllRadarEstatico() {
		return getAllRadarEstatico(null);
	}

	public static ResultSetMap getAllRadarEstatico(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					  " SELECT A.*, B.cd_usuario "
					+ " FROM grl_equipamento A"
					+ " LEFT OUTER JOIN seg_usuario B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " WHERE A.tp_equipamento = "+ RADAR_ESTATICO 
					+ " AND A.st_equipamento = "+ ATIVO
					+ " ORDER BY A.nm_equipamento");
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
	
	public static ResultSetMap getAllRadarFixo() {
		return getAllRadarFixo(null);
	}

	public static ResultSetMap getAllRadarFixo(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					  " SELECT A.*, B.cd_usuario "
					+ " FROM grl_equipamento A"
					+ " LEFT OUTER JOIN seg_usuario B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " WHERE A.tp_equipamento = "+ RADAR_FIXO 
					+ " AND A.st_equipamento = "+ ATIVO
					+ " ORDER BY A.nm_equipamento");
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
	
	public static HashMap<String, String> getAllParametros() {
		return getAllParametros(null);
	}

	public static HashMap<String, String> getAllParametros(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			// ParÃ¢metros relacionados a Equipamentos
			String[] keys = new String[]{"LG_ATIVAR_EQUIPAMENTO"};
			
			return ParametroServices.getValues(keys, null, 0, connect);
					
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

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			String limit = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
					limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsm = Search.find("SELECT * FROM grl_equipamento ", "ORDER BY nm_equipamento"+limit, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			while(rsm.next()) {
				rsm.setValueToField("NM_TP_EQUIPAMENTO", tiposEquipamento[rsm.getInt("TP_EQUIPAMENTO")].toUpperCase());
			}
			rsm.beforeFirst();
			return rsm;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public static ResultSetMap loadToBorrow(ArrayList<ItemComparator> criterios) {
		return loadToBorrow(criterios, null);
	}

	public static ResultSetMap loadToBorrow(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			ResultSetMap rsm = Search.find(
					  " SELECT A.* "
					+ " FROM grl_equipamento A"
					+ " WHERE NOT EXISTS ("
					+ "		SELECT Z.* FROM grl_equipamento_pessoa Z"
					+ "		WHERE A.cd_equipamento = Z.cd_equipamento"
					+ "		AND Z.st_emprestimo = "+EquipamentoPessoaServices.ST_EM_ANDAMENTO
					+ " )", 
					"ORDER BY A.nm_equipamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			while(rsm.next()) {
				rsm.setValueToField("NM_TP_EQUIPAMENTO", tiposEquipamento[rsm.getInt("TP_EQUIPAMENTO")].toUpperCase());
			}
			rsm.beforeFirst();
			return rsm;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}


	public static Equipamento getByIdEquipamento(String idEquipamento) {
		return getByIdEquipamento(idEquipamento, null);
	}

	public static Equipamento getByIdEquipamento(String idEquipamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM GRL_EQUIPAMENTO WHERE ID_EQUIPAMENTO=?");
			pstmt.setString(1, idEquipamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Equipamento(rs.getInt("cd_equipamento"),
						rs.getString("nm_equipamento"),
						rs.getString("id_equipamento"),
						rs.getInt("tp_equipamento"),
						rs.getString("txt_observacao"),
						rs.getString("nm_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("st_equipamento"),
						rs.getInt("cd_logradouro"),
						rs.getDouble("vl_latitude"),
						rs.getDouble("vl_longitude"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("cd_orgao"),
						rs.getString("nm_host"),
						rs.getInt("nr_port"),
						rs.getString("nm_pwd"),
						rs.getInt("nr_channel"),
						rs.getString("nm_login"),
						rs.getString("nm_url_snapshot"),
						rs.getString("nm_url_stream"),
						rs.getString("ds_local"),
						rs.getInt("lg_criptografia"),
						rs.getInt("lg_sync_ftp"),
						(rs.getTimestamp("dt_afericao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_afericao").getTime()),
						rs.getString("nr_lacre"),
						rs.getString("nr_inventario_inmetro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoServices.getByIdEquipamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoServices.getByIdEquipamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getInfoRadar() {
		return getInfoRadar(null);
	}

	public static ResultSetMap getInfoRadar(Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();
			
			String sql = " SELECT A.cd_equipamento, B.id_equipamento, B.nm_equipamento, B.ds_local, " + 
						"	C.id_orgao, D.nm_cidade, " + 
						"	MAX(A.dt_evento) AS dt_ultimo_evento, " + 
						"	round(((EXTRACT(EPOCH FROM TIMESTAMP '"+ Util.convCalendarStringSqlCompleto(new GregorianCalendar()) +"')) - EXTRACT(EPOCH FROM MAX(A.dt_evento)))/60/60) as qt_horas " + 
						"	FROM mob_evento_equipamento A " + 
						"	JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento) " + 
						"	JOIN mob_orgao C ON (B.cd_orgao = C.cd_orgao) " + 
						"	JOIN grl_cidade D ON (C.cd_cidade = D.cd_cidade) " + 
						"	WHERE B.st_equipamento = ? " + 
						"	GROUP BY A.cd_equipamento, B.cd_equipamento, C.cd_orgao, D.cd_cidade " + 
						"	ORDER BY id_equipamento, dt_ultimo_evento";
			
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setInt(1, EquipamentoServices.ATIVO);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			
			/**			
			PreparedStatement ps = connection.prepareStatement(""
					+ " SELECT MAX(A.dt_evento) AS dt_ultimo_evento, A.cd_equipamento "
					+ " FROM mob_evento_equipamento A"
					+ " JOIN grl_equipamento B ON (A.cd_equipamento = B.cd_equipamento)"
					+ " WHERE B.st_equipamento = ?"
					+ " GROUP BY A.cd_equipamento "
					+ " ORDER BY dt_ultimo_evento");
			ps.setInt(1, EquipamentoServices.ATIVO);
			
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			
			while(rsm.next()) {
				
				Equipamento equipamento = EquipamentoDAO.get(rsm.getInt("cd_equipamento"), connection);
				
				rsm.setValueToField("id_equipamento", equipamento.getIdEquipamento());
				rsm.setValueToField("nm_equipamento", equipamento.getNmEquipamento());
				rsm.setValueToField("ds_local", 	  equipamento.getDsLocal().toUpperCase());
				
				rsm.setValueToField("ds_dt_ultimo_evento", Util.formatDate(rsm.getGregorianCalendar("dt_ultimo_evento"), "dd/MM/yyyy HH:mm"));
				
				GregorianCalendar dtEvento = rsm.getGregorianCalendar("dt_ultimo_evento");
				GregorianCalendar dtAtual = new GregorianCalendar();
				
				Interval interval = new Interval(dtEvento.getTimeInMillis(), dtAtual.getTimeInMillis() > dtEvento.getTimeInMillis() ? dtAtual.getTimeInMillis() : dtEvento.getTimeInMillis() );
				int qtHoras = (int) (((interval.toDurationMillis() / 1000) / 60) / 60);
				rsm.setValueToField("qt_horas", qtHoras);
				
				Orgao orgao = OrgaoDAO.get(equipamento.getCdOrgao(), connection);
				if(orgao != null) {
					Cidade cidade = CidadeDAO.get(orgao.getCdCidade(), connection);
					if(cidade != null) {
						rsm.setValueToField("nm_cidade", cidade.getNmCidade().toUpperCase());
					}
				}
				
			}
			rsm.beforeFirst();
			
			rsm.orderBy(new ArrayList<String>() {{
				add("nm_cidade"); 
			}});**/
			
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoServices.getInfoRadar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result notificarFalhaRadar() {
		return notificarFalhaRadar(null);
	}
	
	public static Result notificarFalhaRadar(Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			
			int qtTolerancia = ManagerConf.getInstance().getAsInteger("QT_HORAS_FALHA_RADAR", 6);
			
			ResultSetMap rsmInfo = getInfoRadar(connection);
			
			while(rsmInfo.next()) {				
				// TODO: se ultrapassar a tolerancia, notificar
					// TODO: salvar notificação
					// TODO: mandar email
				
				if(rsmInfo.getInt("qt_horas") > qtTolerancia) {
					NotificacaoServices.save(new Notificacao(0, 
							0, 
							"Alerta de funcionamento de radar", 
							1, 
							"O radar "+rsmInfo.getString("id_equipamento")+" enviou o último evento em "+rsmInfo.getString("ds_dt_ultimo_evento")+", estando há "+rsmInfo.getInt("qt_horas")+" horas sem funcionar.", 
							new GregorianCalendar(), 
							null, 
							0, 
							0, 
							null
						), null, connection);
				}
			}
			
			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoServices.notificarFalhaRadar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result updateHost(String idEquipamento, String ip, Connection connection) {
		boolean isConnectionNull = connection == null;
		try {
			
			if(isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			Equipamento equipamento = getByIdEquipamento(idEquipamento, connection);
			if(equipamento == null) {
				throw new Exception("Equipamento não encontrado");
			}
			
			equipamento.setNmHost(ip);
			Result r = save(equipamento, connection);
			
			if(r.getCode() >= 0) {
				connection.commit();
			}
			
			return r;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EquipamentoServices.updateIp: " + e);
			
			if(isConnectionNull) {
				Conexao.rollback(connection);
			}
			
			return null;
		}
		finally {
			if (isConnectionNull) {
				Conexao.desconectar(connection);
			}
		}
	}
	
	public static Equipamento getEquipamentoByNome(String nmEquipamento) {
		return getEquipamentoByNome(nmEquipamento, null);
	}
	
	public static Equipamento getEquipamentoByNome(String nmEquipamento, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT FROM GRL_EQUIPAMENTO WHERE NM_EQUIPAMENTO = ?");
			pstmt.setString(1, nmEquipamento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			List<Equipamento> rsmConv = new ResultSetMapper<Equipamento>(rsm, Equipamento.class).toList();
			
			if(rsm.next()) {
				return rsmConv.get(rsmConv.size() - 1);
			}
			
			return null;
			
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validEquipamentoInsert(Equipamento equipamento) {
		return validEquipamentoInsert(equipamento, null);
	}
	
	public static Result validEquipamentoInsert(Equipamento equipamento, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			Equipamento equipResId = getByIdEquipamento(equipamento.getIdEquipamento());
			Equipamento equipResNm = getEquipamentoByNome(equipamento.getNmEquipamento());
			
			if(equipResId != null || equipResNm != null)
				return new Result(-1);
			
			return new Result(1);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static boolean verificarBloqueio(String idSerial) throws Exception{
		return verificarBloqueio(idSerial, new CustomConnection());
	}
	
	public static boolean verificarBloqueio(String idSerial, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			if(idSerial == null || idSerial.trim().equals("")) {
				throw new BadRequestException("Número de serial deve ser passado para a verificação");
			}
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("id_serial", idSerial);
			com.tivic.sol.search.Search<Equipamento> search = new SearchBuilder<Equipamento>("grl_equipamento")
				.searchCriterios(searchCriterios)
			.build();
			List<Equipamento> equipamentos = search.getList(Equipamento.class);
			if(equipamentos.isEmpty())
				throw new NoContentException("Nenhum equipamento encontrado");
			customConnection.finishConnection();
			return equipamentos.get(0).getStEquipamento() == EquipamentoServices.INATIVO;
		} finally {
			customConnection.closeConnection();
		}
		
	}
	
}
