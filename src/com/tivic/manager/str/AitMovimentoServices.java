package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.mob.AitReportGetParamns;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitServices;
import com.tivic.manager.mob.grafica.LoteImpressaoTipoDocumentoEnum;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

@Deprecated
public class AitMovimentoServices {
	
	public static final int CADASTRO_CANCELADO               = -1;
	public static final int CADASTRADO                       = 0;
	public static final int ENVIADO_AO_DETRAN                = 1;
	public static final int REGISTRO_INFRACAO                = 2;
	public static final int NAI_ENVIADO                      = 3;
	public static final int AR_NAI                           = 4;
	public static final int NIP_ENVIADA                      = 5;
	public static final int AR_NIP                           = 6;
	public static final int DEFESA_PREVIA                    = 7;
	public static final int DEFESA_DEFERIDA                  = 8;
	public static final int DEFESA_INDEFERIDA                = 9;
	public static final int RECURSO_JARI                     = 10;
	public static final int JARI_COM_PROVIMENTO              = 11;
	public static final int JARI_SEM_PROVIMENTO              = 12;
	public static final int RECURSO_CETRAN                   = 51; // Antigo registro 13;
	public static final int CETRAN_DEFERIDO                  = 52; // Antigo registro 14;
	public static final int CETRAN_INDEFERIDO                = 53; // Antigo registro 15;
	public static final int PENALIDADE_SUSPENSA              = 16;
	public static final int CANCELA_REGISTRO_MULTA           = 17;
	public static final int PENALIDADE_REATIVADA             = 18;
	public static final int TRANSFERENCIA_PONTUACAO          = 19;
	public static final int CANCELAMENTO_PONTUACAO           = 20;
	public static final int REGISTRA_PONTUACAO               = 21;
	public static final int SUSPENDE_PONTUACAO               = 22;
	public static final int REATIVA_MULTA_PONTUACAO          = 23;
	public static final int MULTA_PAGA                       = 24;
	public static final int CANCELAMENTO_AUTUACAO            = 25;
	public static final int CANCELAMENTO_MULTA               = 26;
	public static final int MUDANCA_VENCIMENTO_NIP           = 27;
	public static final int DEVOLUCAO_PAGAMENTO              = 29;
	public static final int REATIVACAO                       = 30;
	public static final int MULTA_PAGA_OUTRA_UF              = 31;
	public static final int AR_RESULTADO                     = 32;
	public static final int DEVOLUCAO_SOLICITADA             = 33;
	public static final int DEVOLUCAO_NEGADA                 = 34;
	public static final int RECALCULO_NIC                    = 35;
	public static final int ATUALIZACAO_REGISTRO             = 36;
	public static final int FIM_PRAZO_DEFESA            	 = 39;
	public static final int PUBLICADO                        = 100;

	public static Result save(AitMovimento movimento){
		return save(movimento, null, null);
	}

	public static Result save(AitMovimento movimento, AuthData authData){
		return save(movimento, authData, null);
	}

	public static Result save(AitMovimento movimento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(movimento==null)
				return new Result(-1, "Erro ao salvar. AitMovimento é nulo");

			int retorno;
			AitMovimento flag = AitMovimentoDAO.get(movimento.getCodigoAit(), movimento.getNrMovimento(), connect);
			if(flag == null){
				retorno = AitMovimentoDAO.insert(movimento, connect);
				movimento.setCodigoAit(retorno);
			}
			else {
				retorno = AitMovimentoDAO.update(movimento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MOVIMENTO", movimento);
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
	public static Result remove(com.tivic.manager.str.AitMovimento movimento) {
		return remove(movimento.getCodigoAit(), movimento.getNrMovimento());
	}
	public static Result remove(int codigoAit, int nrMovimento){
		return remove(codigoAit, nrMovimento, false, null, null);
	}
	public static Result remove(int codigoAit, int nrMovimento, boolean cascade){
		return remove(codigoAit, nrMovimento, cascade, null, null);
	}
	public static Result remove(int codigoAit, int nrMovimento, boolean cascade, AuthData authData){
		return remove(codigoAit, nrMovimento, cascade, authData, null);
	}
	public static Result remove(int codigoAit, int nrMovimento, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AitMovimentoDAO.delete(codigoAit, nrMovimento, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM ait_movimento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static List<com.tivic.manager.str.AitMovimento> getAllDefesas(int cdAit) {
		return getAllDefesas(cdAit, null);
	}

	public static List<AitMovimento> getAllDefesas(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento WHERE codigo_ait=? AND tp_status IN ("+DEFESA_DEFERIDA+", "+DEFESA_INDEFERIDA+", "+DEFESA_PREVIA+", "+ FIM_PRAZO_DEFESA +")");
			pstmt.setInt(1, cdAit);
			
			List<AitMovimento> aitsMovimento = new ArrayList<AitMovimento>();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				aitsMovimento.add(AitMovimentoDAO.get(rsm.getInt("codigo_ait"), rsm.getInt("nr_movimento")));
			}
			
			return aitsMovimento;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAllCancelamentos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAllCancelamentos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public AitMovimento getUltimoMovimento(int cdAit, int tpStatus) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			AitMovimento aitMovimento = getUltimoMovimento(cdAit, tpStatus, customConnection);
			customConnection.finishConnection();
			return aitMovimento;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public AitMovimento getUltimoMovimento(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("codigo_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus);
		com.tivic.sol.search.Search<AitMovimento> search = new SearchBuilder<AitMovimento>("ait_movimento")
				.fields("*")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.orderBy("nr_movimento desc")
				.build();
		
		return search.getList(AitMovimento.class).get(0);
	}
	
	public static AitMovimento getNai(int cdAit) {
		return getNai(cdAit, null);
	}

	public static AitMovimento getNai(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento WHERE codigo_ait = ? AND tp_status = ?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, NAI_ENVIADO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("codigo_ait"), rsm.getInt("nr_movimento"), connect);
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getNai: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getNai: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean isNIC(AitMovimento aitNai) {
		return aitNai == null; 
	}
	
	public static boolean naiEntregue(AitMovimento aitNai) {
		return aitNai.getStAvisoRecebimento() == 1 || aitNai.getStAvisoRecebimento() == 100;
	}
	
	public static void semDadosEntrega(com.tivic.manager.mob.Ait ait, boolean lote, AitMovimento aitNai)  throws ValidacaoException{
		if (!lote)
			return;
		
		GregorianCalendar dtExpiracao = (GregorianCalendar) aitNai.getDtMovimento();
		dtExpiracao.add(Calendar.DAY_OF_MONTH, 60);
		if(Util.getDataAtual().before(dtExpiracao))
			throw new ValidacaoException("NAI não expirou prazo de 60 dias e não possui dados de entrega");
		
	}
	
	public static void comDadosEntrega(com.tivic.manager.mob.Ait ait, AitMovimento aitNai, Connection connect)  throws ValidacaoException{
		HashMap<String, Object> paramns = AitReportGetParamns.getParamnsNIP(connect);
		GregorianCalendar dtExpiracao = (GregorianCalendar) aitNai.getDtAvisoRecebimento();
		dtExpiracao.add(Calendar.DAY_OF_MONTH, Integer.parseInt((String) paramns.get("MOB_PRAZOS_NR_DEFESA_PREVIA")) + 15);
		
		if (Util.getDataAtual().before(dtExpiracao))
		{
			throw new ValidacaoException("NAI possui dados de entrega e não expirou prazo para entrar com recurso");
		}
	}
	
	public static AitMovimento getNip(int cdAit) {
		return getNip(cdAit, null);
	}

	public static AitMovimento getNip(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento WHERE codigo_ait = ? AND tp_status = ?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, NIP_ENVIADA);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("codigo_ait"), rsm.getInt("nr_movimento"), connect);
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getNip: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getNai: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static List<AitMovimento> getAllCancelamentos(int cdAit) {
		return getAllCancelamentos(cdAit, null);
	}

	public static List<AitMovimento> getAllCancelamentos(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento WHERE codigo_ait=? AND tp_status IN ("+CADASTRO_CANCELADO+", "+CANCELAMENTO_AUTUACAO+", "+CANCELAMENTO_MULTA+", "+CANCELAMENTO_PONTUACAO+")");
			pstmt.setInt(1, cdAit);
			
			List<AitMovimento> aitsMovimento = new ArrayList<AitMovimento>();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				aitsMovimento.add(AitMovimentoDAO.get(rsm.getInt("codigo_ait"), rsm.getInt("nr_movimento"), connect));
			}
			
			return aitsMovimento;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAllCancelamentos: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAllCancelamentos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento createPenalidade(com.tivic.manager.mob.Ait ait, int cdUsuario,  Connection connect) throws Exception {
		if (cdUsuario <= 0)
		{
			cdUsuario = buscarUsuario(ait, connect);
		}
		
		AitMovimento aitPenalidade = new AitMovimento();
		aitPenalidade.setCodigoAit(ait.getCdAit());
		aitPenalidade.setCdUsuario(cdUsuario);
		aitPenalidade.setDtMovimento(Util.getDataAtual());
		aitPenalidade.setTpStatus(NIP_ENVIADA);
		aitPenalidade.setStAvisoRecebimento(AitMovimentoServices.PUBLICADO);
		aitPenalidade.setDtAvisoRecebimento(getDtEnvio(ait.getCdAit()));
		
		return aitPenalidade;
	}
	
	private static GregorianCalendar getDtEnvio(int cdAit) throws Exception {
		LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitServices().getByCdAit(cdAit, LoteImpressaoTipoDocumentoEnum.NIP.getKey());
		return loteImpressaoAit != null ? loteImpressaoAit.getDtEnvio() : null ;
	}
	
	private static int buscarUsuario(com.tivic.manager.mob.Ait ait)
	{
		return buscarUsuario(ait, null);
	}
	
	private static int buscarUsuario(com.tivic.manager.mob.Ait ait, Connection connect)
	{
		int cdUsuario = 0;
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try
		{
			PreparedStatement pstmt;
		
			pstmt = connect.prepareStatement("SELECT * FROM ait_movimento WHERE codigo_ait=? AND tp_status = " + NAI_ENVIADO);
			pstmt.setInt(1, ait.getCdAit());
				
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());	
			
			while (rsm.next())
			{
				cdUsuario = rsm.getInt("cd_usuario");
			}
			
			return cdUsuario;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAllCancelamentos: " + e);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getMovimentoTpStatus(int cdAit, int tpStatus){
		return getMovimentoTpStatus(cdAit, tpStatus, null);
	}

	public static AitMovimento getMovimentoTpStatus(int cdAit, int tpStatus, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		PreparedStatement pstmt;
		
		String sql = "";
		if (lgBaseAntiga)
			sql = " SELECT * FROM "
					   + " ait_movimento WHERE codigo_ait=? AND tp_status = " + tpStatus;
		else
			sql = " SELECT * FROM "
					   + " mob_ait_movimento WHERE cd_ait=? AND tp_status = " + tpStatus;
		
		try 
		{
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdAit);
			
			ResultSetMap rs = new ResultSetMap(pstmt.executeQuery());
			
			if(rs.next())
			{
				AitMovimento movimento = new AitMovimento();
				
				movimento.setCodigoAit(rs.getInt("CODIGO_AIT"));
				movimento.setDtMovimento(rs.getGregorianCalendar("DT_MOVIMENTO"));
				movimento.setNrMovimento(rs.getInt("NR_MOVIMENTO"));
				movimento.setNrProcesso(rs.getString("NR_PROCESSO"));
				
				return movimento;
			}
			else
			{
				return null;
			}
		}
		catch(SQLException sqlExpt) 
		{
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getMovimentoRegistro: " + sqlExpt);
			return null;
		}
		catch(Exception e) 
		{
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getMovimentoRegistro: " + e);
			return null;
		}
		finally 
		{
			if (isConnectionNull)
			{
				Conexao.desconectar(connect);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public static void updateTpStatusAit(int codAit, int tpStatus, Connection connect) throws SQLException
	{
		try
		{
			//As variaveis a baixo representão o valor para ativar ou desativar a função crianda por Anderson na base antiga
			//Esta função tenta inserir um movimento para o ait baseado no tpStatus que esta sendo passado
			int inserirMovimentoAtivado = 0;
			int inserirMovimentoDesativado = 1;
			
			String sql = " UPDATE ait SET LG_NOTRIGGER = " + inserirMovimentoDesativado  + " , tp_status = " + tpStatus 
					   + " , dt_movimento = '" + new Timestamp(new GregorianCalendar().getTimeInMillis())
					   + "' WHERE codigo_ait = " + codAit;
			
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.execute();
			pstmt.close();
			
		}
		catch (Exception e)
		{
			System.out.println("Error in AitMovimentoServices em str > updateTpStatusAir");
			e.printStackTrace();
		}
	}
	
}
	

