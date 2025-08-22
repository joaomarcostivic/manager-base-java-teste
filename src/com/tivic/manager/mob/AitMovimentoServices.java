package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.mob.correios.CorreiosEtiquetaRepositoryDAO;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.ServicoDetranFactory;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class AitMovimentoServices {

	public static final int CADASTRO_CANCELADO               			= -1;
	public static final int CADASTRADO                       			= 0;
	public static final int ENVIADO_AO_DETRAN                			= 1;
	public static final int REGISTRO_INFRACAO                			= 2;
	public static final int NAI_ENVIADO                      			= 3;
	public static final int AR_NAI                           			= 4;
	public static final int NIP_ENVIADA                      			= 5;
	public static final int AR_NIP                           			= 6;
	public static final int DEFESA_PREVIA                    			= 7;
	public static final int DEFESA_DEFERIDA                  			= 8;
	public static final int DEFESA_INDEFERIDA                			= 9;
	public static final int RECURSO_JARI                     			= 10;
	public static final int JARI_COM_PROVIMENTO              			= 11;
	public static final int JARI_SEM_PROVIMENTO              			= 12;
	public static final int APRESENTACAO_CONDUTOR            			= 19;
	public static final int RECURSO_CETRAN                   			= 51; // Antigo registro 13;
	public static final int CETRAN_DEFERIDO                  			= 52; // Antigo registro 14;
	public static final int CETRAN_INDEFERIDO                			= 53; // Antigo registro 15;
	public static final int PENALIDADE_SUSPENSA              			= 16;
	public static final int CANCELA_REGISTRO_MULTA           			= 17;
	public static final int PENALIDADE_REATIVADA             			= 18;
	public static final int TRANSFERENCIA_PONTUACAO          			= 19;
	public static final int CANCELAMENTO_PONTUACAO           			= 20;
	public static final int REGISTRA_PONTUACAO               			= 21;
	public static final int SUSPENDE_PONTUACAO               			= 22;
	public static final int REATIVA_MULTA_PONTUACAO          			= 23;
	public static final int MULTA_PAGA                       			= 24;
	public static final int CANCELAMENTO_AUTUACAO            			= 25;
	public static final int CANCELAMENTO_MULTA               			= 26;
	public static final int MUDANCA_VENCIMENTO_NIP           			= 27;
	public static final int DEVOLUCAO_PAGAMENTO              			= 29;
	public static final int REATIVACAO                       			= 30;
	public static final int MULTA_PAGA_OUTRA_UF              			= 31;
	public static final int AR_RESULTADO                     			= 32;
	public static final int DEVOLUCAO_SOLICITADA             			= 33;
	public static final int DEVOLUCAO_NEGADA                 			= 34;
	public static final int RECALCULO_NIC                    			= 35;
	public static final int ATUALIZACAO_REGISTRO             			= 36;
	public static final int FIM_PRAZO_DEFESA            	 			= 39;
	public static final int CANCELAMENTO_DEFESA_PREVIA		 			= 40;
	public static final int CANCELAMENTO_PAGAMENTO						= 46;
	public static final int CANCELAMENTO_RECURSO_JARI					= 70;
	public static final int CANCELAMENTO_RECURSO_CETRAN					= 71;
	public static final int CANCELAMENTO_JARI_COM_PROVIMENTO			= 72;
	public static final int CANCELAMENTO_CETRAN_COM_PROVIMENTO			= 73;
	public static final int CANCELAMENTO_DEFESA_DEFERIDA	 			= 79;
	public static final int CANCELAMENTO_JARI_SEM_PROVIMENTO			= 80;
	public static final int CANCELAMENTO_CETRAN_SEM_PROVIMENTO			= 81;
	public static final int CANCELAMENTO_DEFESA_INDEFERIDA	 			= 82;
	public static final int APRESENTACAO_CONDUTOR_DEFERIDO	 			= 90;
	public static final int PUBLICACAO_RESULTADO_JARI	 				= 95;
	public static final int CANCELAMENTO_PUBLICACAO_RESULTADO_JARI	 	= 96;
	public static final int REABERTURA_FICI								= 97; //VALORES REAIS CONFLITANTES COM DEFESA PRÉVIA
	public static final int REABERTURA_DEFESA							= 98; //VALORES REAIS CONFLITANTES COM DEFESA DEFERIDA
	public static final int REABERTURA_JARI								= 99; //VALORES REAIS CONFLITANTES COM DEFESA INDEFERIDA
	public static final int PUBLICACAO_NAI								= 100;
	public static final int PUBLICACAO_NIP								= 101;
	public static final int ADVERTENCIA_DEFESA_ENTRADA					= 104;
	public static final int ADVERTENCIA_DEFESA_DEFERIDA					= 105;
	public static final int ADVERTENCIA_DEFESA_INDEFERIDA				= 106;
	public static final int CANCELAMENTO_ADVERTENCIA_DEFESA_ENTRADA		= 109;
	public static final int CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA	= 110;
	public static final int CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA	= 111;
	public static final int NOVO_PRAZO_DEFESA							= 112;
	public static final int NOVO_PRAZO_JARI								= 113;
	public static final int NOTIFICACAO_ADVERTENCIA                     = 114;
	public static final int BAIXA_DESVINCULADA_LEILAO                   = 115; //Dicionário SRAM: 29
	public static final int SUSPENSAO_DIVIDA_ATIVA						= 123;
	public static final int CANCELAMENTO_NIP							= 127;
	public static final int CARTAO_IDOSO								= 128;
	public static final int CARTAO_PCD									= 129;
	public static final int CANCELAMENTO_DIVIDA_ATIVA					= 143;
	public static final int CANCELAMENTO_TRANSFERENCIA_PONTUACAO		= -19; //SEM VALOR NA DOCUMENTAÇÃO
	public static final int SITUACAO_NAO_DEFINIDA	                    = -100;
	public static final int NIC_ENVIADO	                    			= 91;
	public static final int NIP_ESTORNADA                               = 500;
	public static final int ENVIOS_MULTIPLOS_RENAINF                    = 501;	
	public static final int DADOS_CORREIO_NA                           = 130;
	public static final int DADOS_CORREIO_NP                           = 131;

	//setCANCELADOS = [17, 25, 26, 29, 46]
	public static final int[] CANCELAMENTOS = {
		CADASTRO_CANCELADO,
		CANCELA_REGISTRO_MULTA, 
		CANCELAMENTO_AUTUACAO,
		CANCELAMENTO_MULTA,
		DEVOLUCAO_PAGAMENTO,
		CANCELAMENTO_PAGAMENTO
	};
	
	public static final int[] RECURSOS = {
			DEFESA_PREVIA,
			RECURSO_JARI,
			RECURSO_CETRAN
	};
	
	public static final int REGISTRO_CANCELADO = -1;
	public static final int NAO_ENVIADO   = 0;
	public static final int REGISTRADO    = 1;
	public static final int MOV_SEM_ENVIO = 2; // Somente para movimentos que não são enviados ao DETRAN (AR-NAI, AR-NIP)
	public static final int ENVIADO_AGUARDANDO = 3; // Pendente de Registro
	public static final int NAO_ENVIAR    = 4; // Para casos em que a situação atual do AIT não recebe mais o movimento a ser enviado
	
	public static final int NAO_PUBLICADO_DO = 0;
	public static final int PUBLICADO_DO 	 = 1;
		  
	public static Result save(AitMovimento aitMovimento){
		return save(aitMovimento, null, null);
	}

	public static Result save(AitMovimento aitMovimento, AuthData authData){
		return save(aitMovimento, authData, null);
	}

	public static Result save(AitMovimento aitMovimento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aitMovimento==null)
				return new Result(-1, "Erro ao salvar. AitMovimento é nulo");

			int retorno;
			if(aitMovimento.getCdMovimento()==0){
				retorno = AitMovimentoDAO.insert(aitMovimento, connect);
				aitMovimento.setCdMovimento(retorno);
			} else {
				retorno = AitMovimentoDAO.update(aitMovimento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AITMOVIMENTO", aitMovimento);
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
	public static Result remove(AitMovimento aitMovimento) {
		return remove(aitMovimento.getCdMovimento(), aitMovimento.getCdAit());
	}
	public static Result remove(int cdMovimento, int cdAit){
		return remove(cdMovimento, cdAit, false, null, null);
	}
	public static Result remove(int cdMovimento, int cdAit, boolean cascade){
		return remove(cdMovimento, cdAit, cascade, null, null);
	}
	public static Result remove(int cdMovimento, int cdAit, boolean cascade, AuthData authData){
		return remove(cdMovimento, cdAit, cascade, authData, null);
	}
	public static Result remove(int cdMovimento, int cdAit, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AitMovimentoDAO.delete(cdMovimento, cdAit, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estï¿½ vinculado a outros e nï¿½o pode ser excluï¿½do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluï¿½do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT A.*, "
					+ " (SELECT COUNT(B.cd_arquivo_movimento) FROM mob_arquivo_movimento B "
					+ "   WHERE B.cd_ait = A.cd_ait "
					+ "     AND B.cd_movimento = A.cd_movimento) as qt_arquivo_movimento "
					+ " FROM mob_ait_movimento A ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAll: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait=? AND tp_status IN ("+CADASTRO_CANCELADO+", "+CANCELAMENTO_AUTUACAO+", "+CANCELAMENTO_MULTA+", "+CANCELAMENTO_PONTUACAO+")");
			pstmt.setInt(1, cdAit);
			
			List<AitMovimento> aitsMovimento = new ArrayList<AitMovimento>();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				aitsMovimento.add(AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait")));
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

	public static List<AitMovimento> getAllDefesas(int cdAit) {
		return getAllDefesas(cdAit, null);
	}

	public static List<AitMovimento> getAllDefesas(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait=? AND tp_status IN (?,?,?,?,?,?)");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, DEFESA_DEFERIDA);
			pstmt.setInt(3, DEFESA_INDEFERIDA);
			pstmt.setInt(4, DEFESA_PREVIA);
			pstmt.setInt(5, ADVERTENCIA_DEFESA_DEFERIDA);
			pstmt.setInt(6, ADVERTENCIA_DEFESA_ENTRADA);
			pstmt.setInt(7, ADVERTENCIA_DEFESA_INDEFERIDA);
			
			List<AitMovimento> aitsMovimento = new ArrayList<AitMovimento>();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				aitsMovimento.add(AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait")));
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
	
	public static List<AitMovimento> getAllDefesasCanceladas(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait=? AND tp_status IN (?,?)");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA);
			pstmt.setInt(3, CANCELAMENTO_DEFESA_INDEFERIDA);
			
			List<AitMovimento> aitsMovimento = new ArrayList<AitMovimento>();
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				aitsMovimento.add(AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait")));
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
	
	public static AitMovimento getMovimentoRegistro(int cdAit) {
		return getMovimentoRegistro(cdAit, null);
	}

	public static AitMovimento getMovimentoRegistro(int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait=? AND tp_status = "+REGISTRO_INFRACAO);
			pstmt.setInt(1, cdAit);
			
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return AitMovimentoDAO.registerTo(rs);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getMovimentoRegistro: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getMovimentoRegistro: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static AitMovimento getMovimentoTpStatus(int cdAit, int tpStatus) {
		return getMovimentoTpStatus(cdAit, tpStatus, null);
	}

	public static AitMovimento getMovimentoTpStatus(int cdAit, int tpStatus, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull) {
			connect = Conexao.conectar();
		}
		
		PreparedStatement pstmt;
		String sql = " SELECT * FROM "
				   + " mob_ait_movimento WHERE cd_ait=? AND tp_status = " + tpStatus;
		
		try {
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdAit);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return AitMovimentoDAO.registerTo(rs);
			}
			else {
				return new AitMovimento();
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getMovimentoRegistro: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getMovimentoRegistro: " + e);
			return null;
		}
		finally {
			if (isConnectionNull) {
				Conexao.desconectar(connect);
			}
		}
	}
	
	public static ResultSetMap getAllArquivosMovimento(int cdAit, int cdMovimento) {
		return getAllArquivosMovimento(cdAit, cdMovimento, null);
	}

	public static ResultSetMap getAllArquivosMovimento(int cdAit, int cdMovimento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_arquivo_movimento WHERE cd_ait = ? AND cd_movimento = ?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, cdMovimento);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAllArquivosMovimento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getAllArquivosMovimento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getFici(AitMovimento fici) {
		return getFici(fici, null);
	}

	public static AitMovimento getFici(AitMovimento fici, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		PreparedStatement pstmt;
		
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ?");
			pstmt.setInt(1, fici.getCdAit());
			pstmt.setInt(2, TRANSFERENCIA_PONTUACAO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getDefesaPrevia: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getDefesaPrevia: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getDefesaPrevia(AitMovimento defesa) throws Exception {
		return getDefesaPrevia(defesa, null);
	}

	public static AitMovimento getDefesaPrevia(AitMovimento defesa, Connection connect) throws Exception {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ?");
			pstmt.setInt(1, defesa.getCdAit());
			pstmt.setInt(2, DEFESA_PREVIA);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			throw new Exception("AIT não possui entrada de Defesa Prévia");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitMovimento getDefesaPreviaEnviado(AitMovimento defesa) throws Exception {
		return getDefesaPreviaEnviado(defesa, null);
	}

	public static AitMovimento getDefesaPreviaEnviado(AitMovimento defesa, Connection connect) throws Exception {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ? AND lg_enviado_detran = ?");
			pstmt.setInt(1, defesa.getCdAit());
			pstmt.setInt(2, DEFESA_PREVIA);
			pstmt.setInt(3, ENVIADO_AO_DETRAN);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			throw new Exception("AIT não possui entrada de Defesa Prévia");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getAdvertenciaDefesa(AitMovimento advertencia) throws Exception {
		return getAdvertenciaDefesa(advertencia, null);
	}

	public static AitMovimento getAdvertenciaDefesa(AitMovimento advertencia, Connection connect) throws Exception {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ?");
			pstmt.setInt(1, advertencia.getCdAit());
			pstmt.setInt(2, ADVERTENCIA_DEFESA_ENTRADA);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			throw new Exception("AIT não possui entrada de Advertência de Defesa");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, NAI_ENVIADO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
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
	
	public static AitMovimento getNip(int cdAit) {
		return getNip(cdAit, null);
	}

	public static AitMovimento getNip(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ? AND lg_enviado_detran <> ?");
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, NIP_ENVIADA);
			pstmt.setInt(3, AitMovimentoServices.REGISTRO_CANCELADO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
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
			System.err.println("Erro! AitMovimentoServices.getNip: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getRecursoJari(AitMovimento recursoJari) throws Exception {
		return getRecursoJari(recursoJari, null);
	}

	public static AitMovimento getRecursoJari(AitMovimento recursoJari, Connection connect) throws Exception {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ?");
			pstmt.setInt(1, recursoJari.getCdAit());
			pstmt.setInt(2, RECURSO_JARI);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			throw new Exception("AIT não possui entrada de recurso JARI");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getRecursoCetran(AitMovimento recursoCetran) throws Exception {
		return getRecursoCetran(recursoCetran, null);
	}

	public static AitMovimento getRecursoCetran(AitMovimento recursoCetran, Connection connect) throws Exception {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ?");
			pstmt.setInt(1, recursoCetran.getCdAit());
			pstmt.setInt(2, RECURSO_CETRAN);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			throw new Exception("AIT não possui entrada de recurso CETRAN");
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getJariIndeferida(AitMovimento recursoJari) {
		return getJariIndeferida(recursoJari, null);
	}

	public static AitMovimento getJariIndeferida(AitMovimento recursoJari, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status = ?");
			pstmt.setInt(1, recursoJari.getCdAit());
			pstmt.setInt(2, JARI_SEM_PROVIMENTO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getRecursoJari: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getRecursoJari: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AitMovimento getResultadoJari(AitMovimento recursoJari) {
		return getResultadoJari(recursoJari, null);
	}

	public static AitMovimento getResultadoJari(AitMovimento recursoJari, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_movimento WHERE cd_ait = ? AND tp_status IN (?, ?)");
			pstmt.setInt(1, recursoJari.getCdAit());
			pstmt.setInt(2, JARI_SEM_PROVIMENTO);
			pstmt.setInt(3, JARI_COM_PROVIMENTO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return AitMovimentoDAO.get(rsm.getInt("cd_movimento"), rsm.getInt("cd_ait"), connect);
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getResultadoJari: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getResultadoJari: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findRemessa(ArrayList<ItemComparator> criterios) {
		return findRemessa(criterios, null);
	}

	public static ResultSetMap findRemessa(ArrayList<ItemComparator> criterios, Connection connect) {
		String countArquivos = "SELECT COUNT(B.cd_arquivo_movimento) FROM mob_arquivo_movimento B WHERE B.cd_ait = A.cd_ait AND B.cd_movimento = A.cd_movimento";
		String ultimoRetorno = "SELECT DS_SAIDA -> 'mensagemRetorno' as ultimo_retorno FROM MOB_ARQUIVO_MOVIMENTO ARQ "
							 + "WHERE A.cd_ait = ARQ.CD_AIT AND A.CD_MOVIMENTO = ARQ.CD_MOVIMENTO ORDER BY CD_ARQUIVO_MOVIMENTO DESC LIMIT 1";
		
		String sql = "SELECT A.*, AIT.id_ait, (" + countArquivos + ") as qt_arquivo_movimento, REPLACE((" + ultimoRetorno + ")::varchar, '\"', '') as ultimo_retorno"
				+ " FROM mob_ait_movimento A "
				+ " JOIN mob_ait AIT ON (A.cd_ait = AIT.cd_ait) "
				+ " WHERE 1=1";
					
		return Search.find(sql, " ORDER BY dt_movimento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		if(!Util.isStrBaseAntiga()) {
			String sql = "SELECT A.*, B.nr_controle, E.nm_login, "
					+ " (SELECT COUNT(C.cd_arquivo_movimento) FROM mob_arquivo_movimento C "
					+ "   WHERE C.cd_ait = A.cd_ait "
					+ "     AND C.cd_movimento = A.cd_movimento) as qt_arquivo_movimento, " 
					+ " (SELECT D.ds_saida FROM mob_arquivo_movimento D "
					+ "   WHERE D.cd_ait = A.cd_ait "
					+ "     AND D.cd_movimento = A.cd_movimento ORDER BY D.dt_arquivo DESC LIMIT 1 ) as ultimo_retorno "
					+ " FROM mob_ait_movimento A"
					+ " JOIN mob_ait B ON (A.cd_ait = B.cd_ait) "
					+ " LEFT OUTER JOIN seg_usuario E ON (A.cd_usuario = E.cd_usuario) "
					+ " WHERE 1=1";
			return Search.find(sql, " ORDER BY dt_movimento DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		}
		else {
			for(ItemComparator criterio : criterios) {
				if(criterio.getColumn().equals("CD_AIT") || criterio.getColumn().equals("cd_ait") || criterio.getColumn().equals("A.cd_ait")) 
					criterio.setColumn("CODIGO_AIT");				
			}
			
			return Search.find("SELECT * FROM AIT_MOVIMENTO A", " ORDER BY dt_movimento DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);			
		}
	}
	
	public static Integer getNumeroRemessaAtual() {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT MAX(nr_remessa) AS nr_remessa_atual FROM mob_ait_movimento");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()) {
				return rsm.getInt("nr_remessa_atual");
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getNumeroRemessa: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitMovimentoServices.getNumeroRemessa: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}

	
	
	public static ArrayList<ItemComparator> getCriteriosRemessa(){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.lg_enviado_detran", String.valueOf(AitServices.LG_DETRAN_NAO_ENVIADA), ItemComparator.EQUAL, Types.VARCHAR));
		criterios.add(new ItemComparator("AIT.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.VARCHAR));
		return criterios;
	}

	public static AitMovimento imporPenalidade(int cdAit, int cdUsuario) throws ValidacaoException, Exception {
		return imporPenalidade(cdAit, false, cdUsuario, null);
	}

	@SuppressWarnings("deprecation")
	public static AitMovimento imporPenalidade(int cdAit, boolean lote, int cdUsuario, Connection connect) throws ValidacaoException, Exception 
	{
		
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		AitReportServicesNip reportServicesNip = new AitReportServicesNip();
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
		}
		
		Ait ait = AitDAO.get(cdAit, connect);
		validarImposicaoPenalidade(ait, lote, connect);
		
		AitMovimento aitPenalidade = new AitMovimento();
		com.tivic.manager.str.AitMovimento aitPenalidadeOld = new com.tivic.manager.str.AitMovimento();
		
		if (lgBaseAntiga)
			 aitPenalidadeOld = com.tivic.manager.str.AitMovimentoServices.createPenalidade(ait, cdUsuario, connect);
		else
			 aitPenalidade = createPenalidade(ait, cdUsuario); 

		try {
			
			
			if (lgBaseAntiga)
			{
				aitPenalidadeOld = savePenalidadeOld(aitPenalidadeOld, connect);
				com.tivic.manager.str.AitMovimentoServices.updateTpStatusAit(cdAit, NIP_ENVIADA, connect);
				reportServicesNip.insertNrNotificacaoNipOld(cdAit, connect);
			}
			else
			{
				aitPenalidade = savePenalidade(aitPenalidade, connect);
				salvarMovimento(ait, aitPenalidade, connect);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			connect.rollback();
		}
		finally{
			if(isConnectionNull)
			{
				connect.commit();
				Conexao.desconectar(connect);
			}
		}
		
		return aitPenalidade;
	}
	
	public static AitMovimento getNrProcessoPendente(int cdAit) {
		
		Connection connect = Conexao.conectar();
		
		try { 
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT * FROM MOB_AIT_MOVIMENTO WHERE CD_MOVIMENTO = "
					+ "(SELECT B.CD_MOVIMENTO FROM PTC_DOCUMENTO A INNER JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON "
					+ "(A.CD_DOCUMENTO = B.CD_DOCUMENTO) WHERE B.CD_AIT = ? AND A.CD_FASE = ? LIMIT 1) AND CD_AIT = ?");
			
			pstmt.setInt(1, cdAit);
			pstmt.setInt(2, FaseServices.getCdFaseByNome("Pendente", connect));
			pstmt.setInt(3, cdAit);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
				return AitMovimentoDAO.registerTo(rs);
			
			return null;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	protected static AitMovimento createPenalidade(Ait ait, int cdUsuario) {
		AitMovimento aitPenalidade = new AitMovimento();
		aitPenalidade.setCdAit(ait.getCdAit());
		aitPenalidade.setCdUsuario(cdUsuario);
		aitPenalidade.setDtMovimento(Util.getDataAtual());
		aitPenalidade.setTpStatus(NIP_ENVIADA);
		return aitPenalidade;
	}
	
	
	protected static AitMovimento savePenalidade(AitMovimento aitPenalidade, Connection connect) throws Exception {
		Result result = save(aitPenalidade, null, connect);
		if(result.getCode() < 0) {
			throw new Exception(result.getMessage());
		}
		return (AitMovimento) result.getObjects().get("AITMOVIMENTO");
	}
	
	protected static com.tivic.manager.str.AitMovimento savePenalidadeOld(com.tivic.manager.str.AitMovimento aitPenalidade, Connection connect) throws Exception {
		Result result = com.tivic.manager.str.AitMovimentoServices.save(aitPenalidade, null, connect);
		if(result.getCode() < 0) {
			throw new Exception(result.getMessage());
		}
		return (com.tivic.manager.str.AitMovimento) result.getObjects().get("AITMOVIMENTO");
	}
	
	private static int atualizarDataVencimentoAit(Ait ait, Connection connect) {
		int nrDiasJari = ParametroServices.getValorOfParametroAsInteger("MOB_PRAZOS_NR_RECURSO_JARI", 0);
		ait.setDtVencimento(Util.getDataAtual());
		ait.getDtVencimento().add(Calendar.DAY_OF_MONTH, nrDiasJari);
		return AitDAO.update(ait, connect);
	}
	
	private static int atualizarDataVencimentoAitOld(Ait ait, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		int nrDiasJari = ParametroServices.getValorOfParametroAsInteger("MOB_PRAZOS_NR_RECURSO_JARI", 0);
		ait.setDtVencimento(Util.getDataAtual());
		ait.getDtVencimento().add(Calendar.DAY_OF_MONTH, nrDiasJari);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dtVencimento = "";
		
		dtVencimento = dateFormat.format(new Timestamp(ait.getDtVencimento().getTimeInMillis()));
		
		try
		{
			PreparedStatement pstmt;
			String sql = "UPDATE ait SET dt_vencimento = '" + dtVencimento
					   + "' 	WHERE codigo_ait = ?"; 
			pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, ait.getCdAit());
			return pstmt.executeUpdate();
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
	
	public static void validarImposicaoPenalidade(Ait ait, boolean lote, Connection connect) throws ValidacaoException, Exception {
		validarExistenciaNip(ait, connect);
		validarExistenciaNai(ait, connect);
		validarCancelamento(ait, connect);
		validarDefesaProtocolada(ait, connect);
		validarSemDefesa(ait, lote, connect);
	}

	protected static void validarExistenciaNip(Ait ait, Connection connect) throws ValidacaoException{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (lgBaseAntiga)
		{
			com.tivic.manager.str.AitMovimento aitNai = com.tivic.manager.str.AitMovimentoServices.getNip(ait.getCdAit(), connect);
			if(aitNai != null)
				throw new ValidacaoException("NIP já gerada para esse AIT");
		}
		else
		{
			AitMovimento aitNai = getNip(ait.getCdAit(), connect);
			if(aitNai != null)
				throw new ValidacaoException("NIP já gerada para esse AIT");
		
		}
	}
	
	private static void validarExistenciaNai(Ait ait, Connection connect) throws ValidacaoException{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao(), connect);
		
		if (lgBaseAntiga)
		{
			com.tivic.manager.str.AitMovimento aitNai = com.tivic.manager.str.AitMovimentoServices.getNai(ait.getCdAit(), connect);
			
			if(aitNai == null && !isNIC(infracao))
				throw new ValidacaoException("NAI não gerada para esse AIT");
		}
		else
		{
			AitMovimento aitNai = getNai(ait.getCdAit(), connect);
			if(aitNai == null && !isNIC(infracao))
				throw new ValidacaoException("NAI não gerada para esse AIT");
		}
	}
	
	private static boolean isNIC(Infracao infracao) {
		return infracao.getNrCodDetran() == 50002 || infracao.getNrCodDetran() == 50020;
	}

	protected static void validarCancelamento(Ait ait, Connection connect ) throws ValidacaoException{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (lgBaseAntiga)
		{
			List<com.tivic.manager.str.AitMovimento> aitsMovimentoCancelamento = com.tivic.manager.str.AitMovimentoServices.getAllCancelamentos(ait.getCdAit(), connect);
			if(!aitsMovimentoCancelamento.isEmpty())
				throw new ValidacaoException("AIT cancelado");
		}
		else
		{
			List<AitMovimento> aitsMovimentoCancelamento = getAllCancelamentos(ait.getCdAit(), connect);
			if(!aitsMovimentoCancelamento.isEmpty())
				throw new ValidacaoException("AIT cancelado");
		}
	}

	@SuppressWarnings("deprecation")
	public static void validarDefesaProtocolada(Ait ait, Connection connect ) throws ValidacaoException {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (!lgBaseAntiga)
		{
			List<AitMovimento> aitsDefesas = getAllDefesas(ait.getCdAit(), connect);
			if(aitsDefesas.isEmpty())
				return;
		
			for(AitMovimento aitDefesa : aitsDefesas) {
				if(aitDefesa.getTpStatus() == DEFESA_INDEFERIDA ||
						aitDefesa.getTpStatus() == ADVERTENCIA_DEFESA_INDEFERIDA) {
					verificarCacelamentoDeIndeferimento(ait.getCdAit(), connect);
					return;
				}
					
		}
		
			throw new ValidacaoException("AIT possui uma defesa deferida");
		}
		else
		{
			List<com.tivic.manager.str.AitMovimento> aitsDefesas = com.tivic.manager.str.AitMovimentoServices.getAllDefesas(ait.getCdAit(), connect);
			if(aitsDefesas.isEmpty())
				return;
		
			for(com.tivic.manager.str.AitMovimento aitDefesa : aitsDefesas) {
				if(aitDefesa.getTpStatus() == DEFESA_INDEFERIDA || 
						aitDefesa.getTpStatus() ==ADVERTENCIA_DEFESA_INDEFERIDA) {
					return;
				}
		}
		
			throw new ValidacaoException("AIT possui uma defesa deferida");
		}
		
	}
	
	private static void verificarCacelamentoDeIndeferimento(int cdAit, Connection connect) throws ValidacaoException {
		List<AitMovimento> aitsDefesasCanceladas = getAllDefesasCanceladas(cdAit, connect);
		for(AitMovimento aitDefesa : aitsDefesasCanceladas) {
			if(aitDefesa.getTpStatus() == AitMovimentoServices.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA ||
							aitDefesa.getTpStatus() == AitMovimentoServices.CANCELAMENTO_DEFESA_INDEFERIDA){
				throw new ValidacaoException("AIT possui uma defesa");
			}
		}
		return;
	}

	@SuppressWarnings("deprecation")
	public static void validarSemDefesa(Ait ait, boolean lote, Connection connect ) throws ValidacaoException, Exception {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		
		if (!lgBaseAntiga)
		{
			
			List<AitMovimento> aitsMovimentoDefesa = getAllDefesas(ait.getCdAit(), connect);
			AitMovimento aitNai = getNai(ait.getCdAit(), connect);
			CorreiosEtiqueta etiqueta = getEtiqueta(aitNai.getCdAit(), aitNai.getTpStatus());
			
			if(isNIC(aitNai))
			{
				return;
			}
			
			if(aitsMovimentoDefesa.isEmpty()) 
			{
				
				if (naiEntregue(etiqueta))
				{
					comDadosEntregaPublicacao(ait.getCdAit(), lote, etiqueta.getDtAvisoRecebimento(), connect);
				}
				else if (naiPublicada(aitNai))
				{
					comDadosEntregaPublicacao(ait.getCdAit(), lote, aitNai.getDtPublicacaoDo() , connect);
				}
				else
				{
					semDadosEntrega(ait, lote, aitNai);
				}

			}
		}
		else
		{
			List<com.tivic.manager.str.AitMovimento> aitsMovimentoDefesa = com.tivic.manager.str.AitMovimentoServices.getAllDefesas(ait.getCdAit(), connect);
			com.tivic.manager.str.AitMovimento aitNai = com.tivic.manager.str.AitMovimentoServices.getNai(ait.getCdAit(), connect);
			if(com.tivic.manager.str.AitMovimentoServices.isNIC(aitNai))
				return;
			if(aitsMovimentoDefesa.isEmpty()) {
				if(!com.tivic.manager.str.AitMovimentoServices.naiEntregue(aitNai))
					com.tivic.manager.str.AitMovimentoServices.semDadosEntrega(ait, lote, aitNai);
				else
					com.tivic.manager.str.AitMovimentoServices.comDadosEntrega(ait, aitNai, connect);
			}
		}
	}
	
	private static CorreiosEtiqueta getEtiqueta(int cdAit, int tpStatus) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("B.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus);
		CorreiosEtiqueta etiqueta = new CorreiosEtiquetaRepositoryDAO().find(searchCriterios, new CustomConnection()).get(0);
		return etiqueta;
	}
	
	public static int getStatusEnvio(int cdRetorno, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		if(cdRetorno == 0)
			return REGISTRADO;
		
		ErroRetorno _erro = ErroRetornoDAO.get(Util.fillNum(cdRetorno, 4), connect);
		
		if(isConnectionNull)
			Conexao.desconectar(connect);

		if(_erro != null)
			return _erro.getTpRetorno();
		
		return NAO_ENVIADO;

	}
	
	public static ResultSetMap getAllByRemessa(int nrRemessa) {
		return getAllByRemessa(nrRemessa, null);
	}
	
	public static ResultSetMap getAllByRemessa(int nrRemessa, Connection connect) {
		boolean isConnectionNull = (connect == null);		
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT MOV.*, AIT.* FROM MOB_AIT_MOVIMENTO MOV JOIN MOB_AIT AIT "
															 + "ON (MOV.CD_AIT = AIT.CD_AIT) WHERE MOV.NR_REMESSA = ?");
			pstmt.setInt(1, nrRemessa);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next())
				return _rsm;

			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	public static Result cancelarNip(AitMovimento nip) {
		return cancelarNip(nip, null);
	}
	
	public static Result cancelarNip(AitMovimento nip, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				Conexao.conectar();
			
			AitMovimento cancelamento = (AitMovimento) nip.clone();
			Ait ait = AitDAO.get(nip.getCdAit(), connect);
			boolean nipEnviada = nip.getLgEnviadoDetran() == 1;
			
			nip.setLgEnviadoDetran(REGISTRO_CANCELADO);
			AitMovimentoDAO.update(nip);

			cancelamento.setDtMovimento(new GregorianCalendar());
			cancelamento.setLgEnviadoDetran(NAO_ENVIADO);
			cancelamento.setTpStatus(CANCELAMENTO_NIP);
			AitMovimentoDAO.insert(cancelamento, connect);
			
			if(nipEnviada) {
				boolean isProducao = ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO", false);
				
				ServicoDetran servicoDetran = ServicoDetranFactory.gerarServico(EstadoServices.getEstadoOrgaoAutuador().getSgEstado(), cancelamento.getTpStatus(), isProducao);
				DadosRetornoMG retorno = (DadosRetornoMG )servicoDetran.executar(new AitDetranObject(ait, cancelamento)).getDadosRetorno();
				if(retorno.getCodigoRetorno() == 0)
					return new Result(1, "NIP cancelada na base estadual.");
				else 
					return new Result(-1, "Erro ao cancelar NIP na base estadual, reenvie o cancelamento na tela de remessas");
			}
			
			return new Result(1, "NIP Cancelada");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1, "Erro no processo de requisição", ex);
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static boolean isNIC(AitMovimento aitNai) {
		return aitNai == null; 
	}
	
	public static boolean naiEntregue(CorreiosEtiqueta etiqueta) throws Exception {
		return etiqueta.getStAvisoRecebimento() == 1 || etiqueta.getStAvisoRecebimento() == 100;
	}
	
	public static boolean naiPublicada(AitMovimento aitNai) {
		return aitNai.getStPublicacaoDo() == 1 || aitNai.getStPublicacaoDo() == 100;
	}
	
	private static void semDadosEntrega(Ait ait, boolean lote, AitMovimento aitNai)  throws ValidacaoException{
		if (!lote)
			return;
		
		GregorianCalendar dtExpiracao = (GregorianCalendar) aitNai.getDtMovimento();
		dtExpiracao.add(Calendar.DAY_OF_MONTH, 60);
		if(Util.getDataAtual().before(dtExpiracao))
			throw new ValidacaoException("NAI não expirou prazo de 60 dias e não possui dados de entrega");
		
	}

	private static void comDadosEntregaPublicacao(int cdAit, boolean lote, GregorianCalendar dtEntreguePublicado, Connection connect)  throws ValidacaoException{
		if (!lote)
			return;
		IVerificarPrazoDefesa estrategiaVerificarPrazoDefesa = new EstrategiaVerificarPrazoDefesa().getEstrategia(AitReportServices.verificarEstado(connect));
		estrategiaVerificarPrazoDefesa.verificarPrazoDefesa(cdAit, dtEntreguePublicado, connect);
	}
	
	protected static void salvarMovimento(Ait ait, AitMovimento aitNip, Connection connect) throws ValidacaoException 
	{
		AitReportServicesNip reportServicesNip = new AitReportServicesNip();
		
		ait.setCdMovimentoAtual(aitNip.getCdMovimento());
		ait.setTpStatus(NIP_ENVIADA);
		ait.setNrNotificacaoNip(reportServicesNip.insertNrNotificacaoNip(connect));

		AitServices.save(ait, null, null, null, null, connect);
	}
	
	public static void setDtPublicacaoDO(List<AitMovimento> _movimentos, GregorianCalendar dtPublicacao) {
		setDtPublicacaoDO(_movimentos, dtPublicacao, null);
	}
	
	public static void setDtPublicacaoDO(List<AitMovimento> _movimentos, GregorianCalendar dtPublicacao, Connection connect) {
		boolean isConnectionNull = (connect == null);		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			GregorianCalendar _dtPublicacao = new GregorianCalendar();
			
			if(dtPublicacao != null)
				_dtPublicacao = dtPublicacao;
			
			for(AitMovimento movimento: _movimentos) {
				movimento.setDtPublicacaoDo(_dtPublicacao);
				movimento.setStPublicacaoDo(PUBLICADO_DO);
				AitMovimentoDAO.update(movimento, connect);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static MovimentosDashboard getContagemMovimentosDashboard() throws Exception {
		return getContagemMovimentosDashboard(null);
	}
	
	public static MovimentosDashboard getContagemMovimentosDashboard(Connection connect) throws Exception {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			MovimentosDashboard movimentosDashboard = new MovimentosDashboard();
			PrazosDashboardServices prazoDashboardServices = new PrazosDashboardServices();
			movimentosDashboard.setQtdRegistroInfracao(prazoDashboardServices.getMovimentosRegistroInfracao(connect).getLines().size());
			movimentosDashboard.setQtdNai(prazoDashboardServices.getMovimentosNai(connect).getLines().size());
			movimentosDashboard.setQtdNip(prazoDashboardServices.getMovimentosNip(connect).getLines().size());
			
			return movimentosDashboard;
		} finally {
			if(isConnectionNull) 
				Conexao.desconectar(connect);
		}
	}
	
	public static MovimentosDashboard getMovimentosPendentesCorrecao() throws Exception {
		return getMovimentosPendentesCorrecao(null);
	}
	
	public static MovimentosDashboard getMovimentosPendentesCorrecao(Connection connect) throws Exception {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			MovimentosDashboard movimentosDashboard = new MovimentosDashboard();
			movimentosDashboard.setQtdRegistroInfracao(getMovimentosComErro(REGISTRO_INFRACAO, connect));
			movimentosDashboard.setQtdNai(getMovimentosComErro(NAI_ENVIADO, connect));
			movimentosDashboard.setQtdNip(getMovimentosComErro(NIP_ENVIADA, connect));
			
			return movimentosDashboard;
		} finally {
			if(isConnectionNull) 
				Conexao.desconectar(connect);
		}
	}
	
	public static void getDataMovimentoAVencer(int tpStatus, ArrayList<ItemComparator> criterios, String dtVencimento) throws Exception {
		PrazosDashboardServices prazoDashboardServices = new PrazosDashboardServices();
		ResultSetMap rsm;
		
		switch(tpStatus) {
			case REGISTRO_INFRACAO:
				rsm = prazoDashboardServices.getMovimentosRegistroInfracao();
				break;
			case NAI_ENVIADO:
				rsm = prazoDashboardServices.getMovimentosNai();
				break;
			case NIP_ENVIADA:
				rsm = prazoDashboardServices.getMovimentosNip();
				break;
			default:
				rsm = new ResultSetMap();
				break;
		}
		
		String aits = getAitsFromResultSetMap(rsm);
		
		if(aitStringValid(aits))
			criterios.add(new ItemComparator("AIT.cd_ait", aits, ItemComparator.IN, Types.INTEGER));
	}
	
	public static String getAitsFromResultSetMap(ResultSetMap rsm) {
		String aits = "";
		rsm.beforeFirst();
		
		while(rsm.next()) {
			aits += rsm.getInt("CD_AIT") + ", ";
		}
		
		if(aitStringValid(aits))
			return aits.substring(0, aits.length() - 2);
		
		return "";
	}
	
	public static List<ServicoDetranDTO> publicarNotificacoes(List<AitMovimento> notificacoes) throws Exception {
		return publicarNotificacoes(notificacoes, null);
	}

	public static List<ServicoDetranDTO> publicarNotificacoes(List<AitMovimento> notificacoes, Connection connect) throws Exception {
		boolean isConnectionNull = (connect == null);

		if (isConnectionNull) {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
		}

		try {
			List<AitMovimento> publicacoes = new ArrayList<>();

			for (AitMovimento notificacao : notificacoes) {
				AitMovimento publicacao = (AitMovimento) notificacao.clone();

				publicacao.setCdMovimento(0);
				publicacao.setNrMovimento(0);
				publicacao.setDtMovimento(notificacao.getDtPublicacaoDo() != null ? notificacao.getDtPublicacaoDo() : new GregorianCalendar());
				publicacao.setTpStatus(getTpPublicacaoDo(notificacao.getTpStatus()));
				publicacao.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIADO);
				publicacao.setDtRegistroDetran(null);

				save(publicacao, null, connect);
				publicacoes.add(publicacao);
			}
			if(isConnectionNull)
				connect.commit();

			ServicoDetranServices servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
			List<ServicoDetranDTO> servicosDetranDTO = servicoDetranServices.remessa(publicacoes);
			

			return servicosDetranDTO;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static int getMovimentosComErro(int tpStatus) throws Exception {
		return getMovimentosComErro(tpStatus, null);
	}
	
	private static int getMovimentosComErro(int tpStatus, Connection connect) throws Exception {
		boolean isConnectionNull = (connect == null);
		
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();		
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = ? AND LG_ENVIADO_DETRAN = ? AND NR_ERRO IS NOT NULL");
			pstmt.setInt(1, tpStatus);
			pstmt.setInt(2, NAO_ENVIADO);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(_rsm.next()) 
				return _rsm.getLines().size();
						
			return 0;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static boolean aitStringValid(String aits) {
		return !aits.trim().equals("");
	}

	protected void inserirPostagemMovimento(ResultSetMap documentosNaoEntregues, int tpStatus, GregorianCalendar dtPublicacao) throws SQLException, ValidacaoException {
		inserirPostagemMovimento(documentosNaoEntregues, tpStatus, dtPublicacao, null);
	}
	
	protected void inserirPostagemMovimento(ResultSetMap documentosNaoEntregues, int tpStatus, GregorianCalendar dtPublicacao, Connection connect) throws SQLException, ValidacaoException{
		boolean isConnectionNull = connect==null;
		try {
			List<AitMovimento> listAitMovimento = new ArrayList<AitMovimento>();
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			while (documentosNaoEntregues.next()){
				AitMovimento aitMovimento = AitMovimentoBuilder.buildPublicacaoDO(documentosNaoEntregues.getInt("CD_AIT"), tpStatus, dtPublicacao);
				listAitMovimento.add(aitMovimento);
				AitMovimentoDAO.update(aitMovimento, connect);
			}
			if (isConnectionNull) {
				connect.commit();
			}
			publicarNotificacoes(listAitMovimento);
		}
		catch (Exception e) {
			System.out.println("Erro em AitMovimentoServices > inserirPostagemMovimento()");
			e.printStackTrace();
			if (isConnectionNull)
				connect.rollback();
			
			throw new ValidacaoException("Erro ao inserir dados de postagem");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static int getTpPublicacaoDo(int tpStatus) {
		if(tpStatus == NAI_ENVIADO)
			return PUBLICACAO_NAI;
		
		if(tpStatus == NIP_ENVIADA)
			return PUBLICACAO_NIP;
		
		return -1;
	}
}
