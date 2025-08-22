package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.TipoOperacao;
import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.manager.adm.TurnoServices;
import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaDAO;
import com.tivic.manager.alm.DocumentoEntradaItemServices;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.DocumentoSaidaItemServices;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@DestinationConfig(enabled = false)
public class LmcServices {

	public static ResultSetMap gerarLMC(int cdCombustivel, int cdEmpresa, int cdConta, GregorianCalendar dtLmcInicial, GregorianCalendar dtLmcFinal){
		return gerarLMC(cdCombustivel, cdEmpresa, cdConta, dtLmcInicial, dtLmcFinal, null);
	}
	
	public static ResultSetMap gerarLMC(int cdCombustivel, int cdEmpresa, int cdConta, GregorianCalendar dtLmcInicial, GregorianCalendar dtLmcFinal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			dtLmcInicial.set(Calendar.HOUR, 0);
			dtLmcInicial.set(Calendar.MINUTE, 0);
			dtLmcInicial.set(Calendar.SECOND, 0);
			dtLmcInicial.set(Calendar.MILLISECOND, 0);
			
			dtLmcFinal.set(Calendar.HOUR, 23);
			dtLmcFinal.set(Calendar.MINUTE, 59);
			dtLmcFinal.set(Calendar.SECOND, 59);
			
			// int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
			
			/*
			 * Estoque de Abertura
			 */
			int cdPrimeiroTurno = TurnoServices.getPrimeiroTurno();
			// Foi acrescentado para contornar o problema de quando a medição física não for lançada no primeiro turno
			String sqlTurnoMedicao = "SELECT C.* FROM adm_conta_fechamento A, pcb_medicao_fisica B, adm_turno C "+
									 "WHERE A.cd_conta      = B.cd_conta "+
									 "  AND A.cd_fechamento = B.cd_fechamento "+
									 "  AND A.cd_turno      = C.cd_turno "+
									 "  AND CAST(dt_fechamento AS DATE) = ? "+
									 "ORDER BY C.nr_ordem "+
									 "LIMIT 1";
			PreparedStatement pstmtTurnoMedicao = connect.prepareStatement(sqlTurnoMedicao);
			//
			String sqlEstoqueAbertura = "SELECT MF.*, LA.id_local_armazenamento, PS.nm_produto_servico, CF.dt_fechamento " +
										"FROM pcb_medicao_fisica MF " +
										"LEFT OUTER JOIN adm_conta_fechamento    CF ON (MF.cd_conta      = CF.cd_conta " +
										"                                           AND MF.cd_fechamento = CF.cd_fechamento) " +
										"LEFT OUTER JOIN pcb_tanque              T  ON (MF.cd_tanque  = T.cd_tanque) " +
										"LEFT OUTER JOIN alm_local_armazenamento LA ON (MF.cd_tanque  = LA.cd_local_armazenamento) " +
										"LEFT OUTER JOIN grl_produto_servico     PS ON (MF.cd_combustivel = PS.cd_produto_servico) " +
										"WHERE CAST(CF.dt_fechamento AS DATE) = ? " +
										"  AND CF.cd_turno                    = ? " +
										"  AND MF.cd_combustivel              = ? ";
			
			PreparedStatement pstmtEstoqueAbertura = connect.prepareStatement(sqlEstoqueAbertura);
			
			
			String sqlEncerrante = "SELECT LA.id_local_armazenamento, BE.cd_bico, B.id_bico, B.nr_casas_inteiras, B.nr_casas_decimais, B.nr_ordem, " +
					               "       SUM(BE.qt_litros) AS qt_litros, SUM(BE.vl_afericao) AS vl_afericao, " +
					               "      (SELECT BEI.qt_encerrante_inicial FROM pcb_bico_encerrante BEI, adm_turno T1, adm_conta_fechamento CF2 "+
					               "       WHERE BEI.cd_conta      = CF2.cd_conta  "+
					               "         AND BEI.cd_fechamento = CF2.cd_fechamento  "+
					               (cdConta>0?" AND CF2.cd_conta      = "+cdConta : "")+
					               "         AND BEI.cd_bico       = BE.cd_bico "+
					               "         AND CF2.dt_fechamento = ? "+
					               "         AND CF2.cd_turno      = T1.cd_turno "+
					               "         AND T1.nr_ordem       = (SELECT MIN(T2.nr_ordem) FROM adm_conta_fechamento CF3, adm_turno T2 "+
					               "                                  WHERE CF3.cd_conta      = CF2.cd_conta "+ 
					               "                                    AND CF3.dt_fechamento = CF2.dt_fechamento "+
					               "                                    AND CF3.cd_turno      = T2.cd_turno)) AS qt_encerrante_inicial, "+
					               "      (SELECT BEI.qt_encerrante_final FROM pcb_bico_encerrante BEI, adm_turno T1, adm_conta_fechamento CF2 "+
					               "       WHERE BEI.cd_conta          = CF2.cd_conta "+ 
					               "         AND BEI.cd_fechamento = CF2.cd_fechamento "+ 
					               (cdConta>0?" AND CF2.cd_conta      = "+cdConta : "")+
					               "         AND BEI.cd_bico       = BE.cd_bico "+
					               "         AND CF2.dt_fechamento = ? "+
					               "         AND CF2.cd_turno      = T1.cd_turno "+ 
					               "         AND T1.nr_ordem       = (SELECT MAX(T2.nr_ordem) FROM adm_conta_fechamento CF3, adm_turno T2 "+
					               "                                  WHERE CF3.cd_conta      = CF2.cd_conta "+
					               "                                    AND CF3.dt_fechamento = CF2.dt_fechamento "+
					               "                                    AND CF3.cd_turno      = T2.cd_turno)) AS qt_encerrante_final "+
					               "FROM pcb_bico_encerrante     BE " +
								   "JOIN pcb_bico                B  ON (BE.cd_bico                = B.cd_bico) " +
								   "JOIN pcb_tanque              T  ON (T.cd_tanque               = BE.cd_tanque) "+
								   "JOIN alm_local_armazenamento LA ON (LA.cd_local_armazenamento = BE.cd_tanque) " +
								   "JOIN adm_conta_fechamento    CF ON (BE.cd_conta               = CF.cd_conta " +
								   "                                AND BE.cd_fechamento          = CF.cd_fechamento) " +
								   "WHERE CAST(CF.dt_fechamento AS DATE) = ? " +
								   (cdConta>0?" AND BE.cd_conta          = " +cdConta: "" )+
								   "  AND LA.cd_empresa        = " +cdEmpresa+
								   "  AND BE.cd_combustivel    = ? " +
								   "GROUP BY LA.id_local_armazenamento, BE.cd_bico, B.id_bico, B.nr_casas_inteiras, B.nr_casas_decimais, B.nr_ordem " +
								   "ORDER BY B.nr_ordem, LA.id_local_armazenamento";
			PreparedStatement pstmtEncerrante = connect.prepareStatement(sqlEncerrante);
			// Preço
			int cdTabelaPreco  = 0;
			int cdTipoOperacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
			TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacao, connect);
			if(tipoOperacao!=null)
				cdTabelaPreco = tipoOperacao.getCdTabelaPreco();
			//
			PreparedStatement pstmtPreco = connect.prepareStatement("SELECT A.*, B.id_tabela_preco FROM adm_produto_servico_preco A, adm_tabela_preco B " +
												                    "WHERE A.cd_tabela_preco  = B.cd_tabela_preco " +
												                    "  AND (A.dt_termino_validade IS NULL OR A.dt_termino_validade > ?)" +
												                    "  AND A.cd_produto_servico = ? " +
												                    "  AND A.cd_tabela_preco    = " +cdTabelaPreco+
												                    "ORDER BY A.dt_termino_validade");
			//
			ResultSetMap rsmLMC = new ResultSetMap();
			/*
			 * Selecionando combustíveis
			 */
			String sqlCombustivel = "SELECT PS.nm_produto_servico, PS.cd_produto_servico " +
					                "FROM grl_produto_servico PS " +
									"WHERE "+(cdCombustivel>0 ? " PS.cd_produto_servico = "+cdCombustivel :
											                    " EXISTS (SELECT * FROM pcb_bico_encerrante BE WHERE PS.cd_produto_servico = BE.cd_combustivel)");
			PreparedStatement pstmtCombustivel = connect.prepareStatement(sqlCombustivel);
			ResultSetMap rsmCombustivel = new ResultSetMap(pstmtCombustivel.executeQuery());
			while(rsmCombustivel.next())	{
				// Reinicia o período pra cada novo combustível
				GregorianCalendar dtMovimento = (GregorianCalendar)dtLmcInicial.clone();
				while(dtMovimento.getTimeInMillis() <= dtLmcFinal.getTimeInMillis())	{
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("CD_PRODUTO_SERVICO", rsmCombustivel.getInt("cd_produto_servico"));
					register.put("NM_PRODUTO_SERVICO", rsmCombustivel.getString("nm_produto_servico"));
					register.put("DT_FECHAMENTO", new Timestamp(dtMovimento.getTimeInMillis()));
					/*
					 * Descobre o turno no qual foi lançado a medição física
					 */
					pstmtTurnoMedicao.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
					ResultSet rsTemp =pstmtTurnoMedicao.executeQuery();
					int cdTurnoMedicao = cdPrimeiroTurno;
					if(rsTemp.next())
						cdTurnoMedicao = rsTemp.getInt("cd_turno");
					/*
					 * Busca de estoques de abertura
					 */
					pstmtEstoqueAbertura.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
					pstmtEstoqueAbertura.setInt(2, cdTurnoMedicao);
					pstmtEstoqueAbertura.setInt(3, rsmCombustivel.getInt("cd_produto_servico"));
					ResultSetMap rsmEstoqueAbertura = new ResultSetMap(pstmtEstoqueAbertura.executeQuery());
					//
					int cont = 1;
					while(rsmEstoqueAbertura.next())	{
						register.put("ID_TANQUE_"+cont, rsmEstoqueAbertura.getString("id_local_armazenamento"));
						register.put("QT_VOLUME_TQ_"+cont, rsmEstoqueAbertura.getDouble("qt_volume"));
						cont++;
					}
					
					for(int i=cont; i<=9; i++)
						register.put("QT_VOLUME_TQ_"+i, 0);
					/*
					 * ENTRADAS
					 */
					ArrayList<ItemComparator> criterios;
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("B.cd_produto_servico",""+rsmCombustivel.getInt("cd_produto_servico"),ItemComparator.EQUAL,java.sql.Types.INTEGER));
					criterios.add(new ItemComparator("A.cd_empresa",""+cdEmpresa,ItemComparator.EQUAL,java.sql.Types.INTEGER));
					criterios.add(new ItemComparator("A.st_documento_entrada",""+DocumentoEntradaServices.ST_LIBERADO,ItemComparator.EQUAL,java.sql.Types.INTEGER));
					criterios.add(new ItemComparator("A.tp_documento_entrada",""+DocumentoEntradaServices.TP_DOC_NAO_FISCAL, ItemComparator.DIFFERENT,java.sql.Types.INTEGER));
					criterios.add(new ItemComparator("CAST(A.dt_documento_entrada AS DATE)",Util.convCalendarString(dtMovimento),ItemComparator.EQUAL,java.sql.Types.TIMESTAMP));
					ArrayList<String> groupBy = new ArrayList<String>();
					groupBy.add("A.cd_documento_entrada");
					
					ResultSetMap rsmNotas = DocumentoEntradaItemServices.findCompleto(criterios, groupBy, null);
					cont = 1;
					while(rsmNotas.next())	{
						DocumentoEntrada docEntrada = DocumentoEntradaDAO.get(rsmNotas.getInt("CD_DOCUMENTO_ENTRADA"));
						register.put("NR_NOTA_FISCAL_"+cont, docEntrada.getNrDocumentoEntrada());
						//
						Pessoa fornecedor = PessoaDAO.get(docEntrada.getCdFornecedor());
						register.put("NM_FORNECEDOR_"+cont,fornecedor.getNmPessoa());
						register.put("VL_RECEBIDO_"+cont,rsmNotas.getFloat("QT_ENTRADAS"));
						//
						ResultSetMap rsmLocaisArmazenamento = DocumentoEntradaItemServices.getAllLocaisArmazenamento(rsmNotas.getInt("CD_DOCUMENTO_ENTRADA"), rsmCombustivel.getInt("cd_produto_servico"), cdEmpresa);
						String idTanqueDescarga = "";
						while(rsmLocaisArmazenamento.next())
							idTanqueDescarga += (idTanqueDescarga.equals("") ? "" : ", ") + rsmLocaisArmazenamento.getString("ID_LOCAL_ARMAZENAMENTO");
						
						register.put("ID_TANQUE_DESCARGA_"+cont, idTanqueDescarga);
						cont++;
					}
					while(cont <= 3) {
						register.put("VL_RECEBIDO_"+cont, 0);
						cont++;
					}
					/*
					 * Preço
					 */
					double vlPreco = 0; 
					pstmtPreco.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
					pstmtPreco.setInt(2, rsmCombustivel.getInt("cd_produto_servico"));
					ResultSet rsPreco = pstmtPreco.executeQuery();
					if(rsPreco.next())
						vlPreco = rsPreco.getDouble("vl_preco");	
					/*
					* Encerrantes
					*/
					pstmtEncerrante.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
					pstmtEncerrante.setTimestamp(2, new Timestamp(dtMovimento.getTimeInMillis()));
					pstmtEncerrante.setTimestamp(3, new Timestamp(dtMovimento.getTimeInMillis()));
					pstmtEncerrante.setInt(4, rsmCombustivel.getInt("cd_produto_servico"));
					ResultSetMap rsEncerrantes = new ResultSetMap(pstmtEncerrante.executeQuery());
					cont = 1;
					while(rsEncerrantes.next())	{
						String mask = "";
						for(int i = 0;i<rsEncerrantes.getInt("nr_casas_inteiras");i++)
							mask += "0";
						
						for(int i = 0;i<rsEncerrantes.getInt("nr_casas_decimais");i++){
							if(i==0)
								mask += ".";
					    	mask += "0";
						}
						String qtFechamento = Util.formatNumber(rsEncerrantes.getDouble("qt_encerrante_final"), mask);
						String qtAbertura   = Util.formatNumber(rsEncerrantes.getDouble("qt_encerrante_inicial"), mask);
						String qtVendas     = Util.formatNumber(rsEncerrantes.getDouble("qt_litros"), mask);
						String qtAfericao   = Util.formatNumber(rsEncerrantes.getDouble("vl_afericao"), mask);
						register.put("ID_TQ_"+cont,         rsEncerrantes.getString("id_local_armazenamento"));
						register.put("ID_BICO_"+cont,       rsEncerrantes.getString("id_bico"));
						register.put("QT_FECHAMENTO_"+cont, qtFechamento);
						register.put("QT_ABERTURA_"+cont,   qtAbertura);
						register.put("QT_AFERICAO_"+cont,   qtAfericao);
						register.put("QT_VENDAS_"+cont,     qtVendas);
						register.put("QT_LITROS_"+cont, rsEncerrantes.getDouble("qt_litros"));
						register.put("VL_PRECO_MEDIO", vlPreco);
						cont++;
					}
					while(cont <= 17)	{
						register.put("QT_LITROS_"+cont, 0);
						cont++;
					}
					/*
					 * Conciliação dos estoques
					 */
					GregorianCalendar dtLmcDiaSeguinte = (GregorianCalendar) dtMovimento.clone();
					dtLmcDiaSeguinte.add(Calendar.DAY_OF_MONTH, 1);
					PreparedStatement pstmtEstoqueFinal = connect.prepareStatement(sqlEstoqueAbertura);
					pstmtEstoqueFinal.setTimestamp(1, new Timestamp(dtLmcDiaSeguinte.getTimeInMillis()));
					pstmtEstoqueFinal.setInt(2, cdTurnoMedicao);
					pstmtEstoqueFinal.setInt(3, rsmCombustivel.getInt("cd_produto_servico"));
					ResultSetMap rsmEstoqueFinal = new ResultSetMap(pstmtEstoqueFinal.executeQuery());
					cont = 1;
					while(rsmEstoqueFinal.next())	{
						register.put("ID_TANQUE_FISICO_"+cont, rsmEstoqueFinal.getString("id_local_armazenamento"));
						register.put("QT_VOLUME_FISICO_"+cont, rsmEstoqueFinal.getDouble("qt_volume"));	
						cont++;
					}
					
					while(cont<=9)	{
						register.put("QT_VOLUME_FISICO_"+cont, 0);
						cont++;
					}
					/*
					 * Histórico de Mudanças
					 */
					String obs = BicoHistoricoServices.getHistoricoBicoByCombustivel(rsmCombustivel.getInt("cd_produto_servico"), dtMovimento);
					
					obs += (!obs.equals("") ? "\n" : "")+" "+TanqueHistoricoServices.getTanqueHistoricoByCombustivel(rsmCombustivel.getInt("cd_produto_servico"), dtMovimento);
					/*
					 * Observação Vendas
					 */
					GregorianCalendar dtLmcVendas = (GregorianCalendar)dtMovimento.clone();
					ResultSetMap rsmVendas = DocumentoSaidaItemServices.findSaidaByProduto(0, rsmCombustivel.getInt("cd_produto_servico"), cdEmpresa, dtLmcVendas, 0);
					if(!obs.equals(""))
						obs += "\n";
					while(rsmVendas.next())	{
						if(Math.abs(rsmVendas.getDouble("vl_unitario") - vlPreco) > 0.01) {
							obs += " Venda " +   Util.formatNumber(rsmVendas.getDouble("QT_SAIDA"), "##########.000") + 
								   " LTS a R$ " + Util.formatNumber(rsmVendas.getDouble("VL_UNITARIO"), "##########.000") + 
								   " = R$ " +    Util.formatNumber((rsmVendas.getDouble("QT_SAIDA") * rsmVendas.getDouble("VL_UNITARIO")), "##########.000") + "\n";
						}
					}
					register.put("OBSERVACOES", obs);
					
					rsmLMC.addRegister(register);
					// Adiciona um dia ao movimento
					dtMovimento.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
			return rsmLMC;
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
}
