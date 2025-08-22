package com.tivic.manager.pcb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.sql.*;

import com.tivic.manager.adm.ContaFechamento;
import com.tivic.manager.adm.ContaFechamentoDAO;
import com.tivic.manager.adm.ContaFinanceira;
import com.tivic.manager.adm.ContaFinanceiraDAO;
import com.tivic.manager.adm.TurnoServices;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.*;
import sol.util.Result;

@DestinationConfig(enabled = false)
public class MedicaoFisicaServices {
	
	public static ResultSetMap getMedicaoFisicaOf(int cdConta, int cdFechamento, GregorianCalendar dtMovimento) {
		return getMedicaoFisicaOf(cdConta, cdFechamento, dtMovimento, null);
	}
	
	public static ResultSetMap getMedicaoFisicaOf(int cdConta, int cdFechamento, GregorianCalendar dtMovimento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ContaFinanceira conta      = ContaFinanceiraDAO.get(cdConta, connect);
			if(cdFechamento > 0) {
				ContaFechamento fechamento = ContaFechamentoDAO.get(cdConta, cdFechamento, connect);
				if(fechamento!=null && fechamento.getDtFechamento()!=null)
					dtMovimento = fechamento.getDtFechamento();
			}
			
			String sql = "SELECT T.*, LA.*, LA.nm_local_armazenamento AS nm_tanque, PSE.qt_precisao_custo, " +
					     "       PS.nm_produto_servico as nm_combustivel, TT.nm_tipo_tanque, TT.qt_capacidade, MF.* " +
					 	 "FROM pcb_tanque T " +
					 	 "JOIN alm_local_armazenamento       LA  ON (T.cd_tanque           = LA.cd_local_armazenamento) " +
					 	 "JOIN pcb_tipo_tanque               TT  ON (TT.cd_tipo_tanque     = T.cd_tipo_tanque) " +
					 	 "LEFT OUTER JOIN pcb_medicao_fisica MF  ON (T.cd_tanque           = MF.cd_tanque" +
					 	 "                                       AND MF.cd_conta           = "+cdConta+
					 	 "                                       AND MF.cd_fechamento      = "+cdFechamento+") "+
					 	 "JOIN grl_produto_servico           PS  ON ((MF.cd_combustivel IS NULL AND T.cd_produto_servico = PS.cd_produto_servico) OR " +
					 	 "                                           (MF.cd_combustivel = PS.cd_produto_servico)) " +
						 "JOIN grl_produto_servico_empresa   PSE ON (PS.cd_produto_servico = PSE.cd_produto_servico " +
						 "                                       AND LA.cd_empresa         = PSE.cd_empresa)" +
					 	 "WHERE LA.cd_empresa = "+conta.getCdEmpresa();
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			while(rsm.next()) 
				rsm.setValueToField("QT_ESTOQUE_ESCRITURAL", getMedicaoDiaAnterior(cdConta, rsm.getInt("cd_tanque"), dtMovimento, connect));
			
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result iniciarFechamento(int cdConta, int cdFechamento, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
						
			ResultSetMap rsm = TanqueDAO.getAllTanques(cdEmpresa, connect);
			while(rsm.next())	{
				int cdTanque      = rsm.getInt("cd_tanque");
				int cdCombustivel = rsm.getInt("cd_produto_servico");
				MedicaoFisica medicaoFisica = MedicaoFisicaDAO.get(cdConta, cdFechamento, cdTanque, connect);
				if (medicaoFisica==null)	{
					int vlRegua               = 0;
					int qtVolume              = 0;
					float qtEstoqueEscritural = 0;
					int qtCapacidade          = 0;
					medicaoFisica = new MedicaoFisica(cdConta,cdFechamento,cdTanque,vlRegua,qtVolume,qtEstoqueEscritural,qtCapacidade, cdCombustivel);
					MedicaoFisicaDAO.insert(medicaoFisica, connect);
				}
			}
			
			return new Result(1);
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Falha ao tentar incluir tanques no fechamento!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result setMedicaoFisica(int cdConta, int cdFechamento, int cdTanque, float vlRegua, float qtVolume)	{
		return setMedicaoFisica(cdConta, cdFechamento, cdTanque, vlRegua, qtVolume, null);
	}
	
	public static Result setMedicaoFisica(int cdConta, int cdFechamento, int cdTanque, float vlRegua, float qtVolume, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
						
			MedicaoFisica medicaoFisica = MedicaoFisicaDAO.get(cdConta, cdFechamento, cdTanque, connect);
			// Busca na régua
			if(vlRegua>0 && qtVolume==0){
				
			}
			// Atualizando valores
			medicaoFisica.setVlRegua(vlRegua);
			medicaoFisica.setQtVolume(qtVolume);
			return new Result(MedicaoFisicaDAO.update(medicaoFisica, connect));
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Falha ao tentar gravar dados da medição física!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result lancarMedicaoFisica(ArrayList<MedicaoFisica> al){
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			for(int i=0;i<=al.size()-1;i++)	{
				MedicaoFisica objeto = (MedicaoFisica) al.get(i);
				System.out.println("objeto MED = " + objeto);
				// Verifica se não já foi incluído
				MedicaoFisica medicaoFisica = MedicaoFisicaDAO.get(objeto.getCdConta(), objeto.getCdFechamento(), objeto.getCdTanque(), connect);
				int ret = 0;
				if(medicaoFisica!=null)
					ret = MedicaoFisicaDAO.update(objeto, connect);
				else
					ret = MedicaoFisicaDAO.insert(objeto, connect);
				//
				if(ret<=0) {
					Conexao.rollback(connect);
					return new Result(ret, "Falha ao tentar inserir medição física!");
				}
			}
			// Gravando informações
			connect.commit();
			//
			return new Result(1, "Medição fisica inserida com sucesso");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new Result(-1, "Falha ao tentar lançar medição física!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static float getMedicaoDiaAnterior(int cdConta, int cdTanque, GregorianCalendar dtFechamento){
		GregorianCalendar dtDiaAnterior = (GregorianCalendar) dtFechamento.clone();
		dtDiaAnterior.add(Calendar.DAY_OF_MONTH, -1);
		return getEstoqueEscritural(cdConta, cdTanque, dtDiaAnterior, null);
	}
	
	public static float getMedicaoDiaAnterior(int cdConta, int cdTanque, GregorianCalendar dtFechamento, Connection connect){
		GregorianCalendar dtDiaAnterior = (GregorianCalendar) dtFechamento.clone();
		dtDiaAnterior.add(Calendar.DAY_OF_MONTH, -1);
		return getEstoqueEscritural(cdConta, cdTanque, dtDiaAnterior, connect);
	}
	
	public static float getEstoqueEscritural(int cdConta, int cdTanque, GregorianCalendar dtFechamento){		
		return getEstoqueEscritural(cdConta, cdTanque, dtFechamento, null);
	}
	public static float getEstoqueEscritural(int cdConta, int cdTanque, GregorianCalendar dtFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			float qtMedicaoFisica = 0;
			float qtSaidas        = 0;
			float qtEntradas      = 0;
			//
			dtFechamento.set(Calendar.HOUR,0);
			dtFechamento.set(Calendar.MINUTE,0);
			dtFechamento.set(Calendar.SECOND,0);
			dtFechamento.set(Calendar.MILLISECOND,0);
			//
			PreparedStatement pstmt = connect.prepareStatement("SELECT qt_volume FROM pcb_medicao_fisica MF " +
															   "JOIN adm_conta_fechamento  CF ON (MF.cd_fechamento = CF.cd_fechamento) " +
															   "WHERE CAST(CF.dt_fechamento AS DATE) = ?" +
															   "  AND MF.cd_tanque = "+cdTanque+
															   "  AND CF.cd_conta  = "+cdConta+
															   "  AND CF.cd_turno  = "+TurnoServices.getPrimeiroTurno());
			pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));				
			ResultSetMap rsmMedicaoFisica = new ResultSetMap(pstmt.executeQuery());

			if(rsmMedicaoFisica.next()) {
				qtMedicaoFisica = rsmMedicaoFisica.getFloat("QT_VOLUME");

				pstmt = connect.prepareStatement("SELECT SUM(qt_litros) AS qt_litros " +
											 	 "FROM pcb_bico_encerrante  BE " +
											 	 "JOIN adm_conta_fechamento CF ON (BE.cd_fechamento = CF.cd_fechamento) " +
											 	 "WHERE CF.cd_conta                    = "+cdConta +
											 	 "  AND BE.cd_tanque                   = "+cdTanque+
											 	 "  AND CAST(CF.dt_fechamento AS DATE) = ? ");
				pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
				ResultSetMap rsmVendasDias = new ResultSetMap(pstmt.executeQuery());
				if(rsmVendasDias.next())
					qtSaidas = rsmVendasDias.getFloat("QT_LITROS");
				pstmt= connect.prepareStatement("SELECT SUM(A.qt_entrada) AS QT_ENTRADA " +
												"FROM alm_entrada_local_item A, alm_documento_entrada B " +
												"WHERE A.cd_local_armazenamento = "+cdTanque+
												"  AND A.cd_documento_entrada   = B.cd_documento_entrada " +
												"  AND B.tp_entrada        NOT IN ("+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+","+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") "+
												"  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
												"  AND CAST(B.dt_documento_entrada AS DATE) = ? ");
				pstmt.setTimestamp(1, new Timestamp(dtFechamento.getTimeInMillis()));
				ResultSetMap rsmEntrada = new ResultSetMap(pstmt.executeQuery());
				if(rsmEntrada.next())
					qtEntradas = rsmEntrada.getFloat("QT_ENTRADA");	
				
				
				System.out.println("qtMedicaoFisica: " + qtMedicaoFisica);
				System.out.println("qtEntradas: " + qtEntradas);
				System.out.println("qtSaidas: " + qtSaidas);
				System.out.println("qtEstoqueEscritural: " + (qtMedicaoFisica + qtEntradas - qtSaidas));
				
				
				return (qtMedicaoFisica + qtEntradas - qtSaidas);
			}
			// Se não encontrar medição do dia anterior, retorna 0
			else
				return 0;				
		}
		catch(Exception e){
			Util.registerLog(e);
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap gerarRelatorioConsolidacaoMedicaoFisica( ResultSetMap rsmFechamentos) {
		return gerarRelatorioConsolidacaoMedicaoFisica( rsmFechamentos, null);
	}
	
	public static ResultSetMap gerarRelatorioConsolidacaoMedicaoFisica(ResultSetMap rsmFechamentos, Connection connect) {
		boolean isConnectionNull = connect==null;
		ResultSetMap rsmMedicaoFisica = new ResultSetMap();
		rsmFechamentos.beforeFirst();
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			while(rsmFechamentos.next()){
				ResultSetMap rsmTmpMedicaoFisica = getMedicaoFisicaOf(  rsmFechamentos.getInt("CD_CONTA"), rsmFechamentos.getInt("CD_FECHAMENTO"), rsmFechamentos.getGregorianCalendar("DT_MOVIMENTO"));
				rsmTmpMedicaoFisica.beforeFirst();
				while( rsmTmpMedicaoFisica.next() ){
					boolean hasRegister = false;
					while( rsmMedicaoFisica.next() ){
						if( rsmMedicaoFisica.getInt("CD_LOCAL_ARMAZENAMENTO") == rsmTmpMedicaoFisica.getInt("CD_LOCAL_ARMAZENAMENTO") ){
							//CONTABILIZA TOTAL E SINALIZA QUE JÁ POSSUI O REGISTRO NO RSM
							Double vlRegua = rsmMedicaoFisica.getDouble("VL_REGUA")+rsmTmpMedicaoFisica.getDouble("VL_REGUA");
							Double qtVolume = rsmMedicaoFisica.getDouble("QT_VOLUME")+rsmTmpMedicaoFisica.getDouble("QT_VOLUME");
							
							rsmMedicaoFisica.setValueToField("VL_REGUA", vlRegua);
							rsmMedicaoFisica.setValueToField("QT_VOLUME", qtVolume);
							hasRegister = true;
						}
					}
					if(!hasRegister){
						rsmMedicaoFisica.addRegister( rsmTmpMedicaoFisica.getRegister() );
					}
					rsmMedicaoFisica.beforeFirst();
				}
			}
			
			return rsmMedicaoFisica;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
