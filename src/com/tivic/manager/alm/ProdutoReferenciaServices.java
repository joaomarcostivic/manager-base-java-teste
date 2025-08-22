package com.tivic.manager.alm;

import java.sql.*;
import java.util.*;

import com.tivic.sol.connection.Conexao;

import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ProdutoReferenciaServices {

	public static ResultSetMap getReferenciasOfProduto(int cdProduto) {
		return getReferenciasOfProduto(cdProduto, false);
	}
	public static ResultSetMap getReferenciasOfProduto(int cdProduto, boolean lgSomenteDisponiveis) {
		ArrayList<sol.dao.ItemComparator> criterios = new ArrayList<sol.dao.ItemComparator>();
		criterios.add(new sol.dao.ItemComparator("A.cd_produto_servico", String.valueOf(cdProduto), sol.dao.ItemComparator.EQUAL, java.sql.Types.INTEGER));
		if(lgSomenteDisponiveis)
			criterios.add(new sol.dao.ItemComparator("lgSomenteDisponiveis", "", sol.dao.ItemComparator.ISNULL, java.sql.Types.INTEGER));
		return find(criterios, new ArrayList<String>(), null);
	}
	
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, new ArrayList<String>(), null);
	}
	
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, ArrayList<String> groupBy) {
		return find(criterios, groupBy, null);
	}
	
	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, ArrayList<String> groupBy, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int cdLocalRaiz = 0, cdLocalArmazenamento = 0, cdAtendimento = 0, stReserva = -10;
			boolean lgSomenteDisponiveis = false;
			
			ArrayList<sol.dao.ItemComparator> crt = new ArrayList<sol.dao.ItemComparator>(); 
			for(int i=0;i < criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("cd_local_raiz"))
					cdLocalRaiz = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgSomenteDisponiveis"))
					lgSomenteDisponiveis = true;
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stReserva"))
					stReserva = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cd_local_armazenamento"))
					cdLocalArmazenamento = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cd_atendimento"))
					cdAtendimento = Integer.parseInt(criterios.get(i).getValue());
				else
					crt.add(criterios.get(i));
			if(lgSomenteDisponiveis)
				stReserva = -2; // Não existir reserva
			
			String groups = "";
			String fields = " A.*, B.nm_local_armazenamento, B.cd_local_armazenamento_superior, D.nm_grupo, " +
							" F.cd_contrato, F.dt_assinatura AS dt_contrato, F.st_contrato, F.vl_contrato, G.nm_pessoa AS nm_cliente," +
							" H.nm_produto_servico, I.nm_pessoa AS nm_agente, L.nm_tipo_operacao ";
			// Processa agrupamentos enviados em groupBy
			String [] retorno = com.tivic.manager.util.Util.getFieldsAndGroupBy(groupBy, fields, groups,
					                                                     "SUM(VL_CONTRATO) AS VL_TOTAL, COUNT(*) AS QT_TOTAL");
			fields = retorno[0];
			groups = retorno[1];

			String locais = ""; 
			cdLocalArmazenamento = cdLocalArmazenamento>0 ? cdLocalArmazenamento : cdLocalRaiz;
			if(cdLocalRaiz>0 || cdLocalArmazenamento>0)
				locais = LocalArmazenamentoServices.getListaLocaisInferiores(cdLocalArmazenamento, "", connect);
			ResultSetMap rsm = Search.find(
					"SELECT " +fields+
         		    "FROM alm_produto_referencia A " +
                    "LEFT OUTER JOIN alm_local_armazenamento B ON (B.cd_local_armazenamento = A.cd_local_armazenamento) " +
                    "LEFT OUTER JOIN alm_produto_grupo       C ON (A.cd_produto_servico = C.cd_produto_servico) " +
                    "LEFT OUTER JOIN alm_grupo               D ON (C.cd_grupo = D.cd_grupo) " +
                    /* Dados do Comprador */
                    "LEFT OUTER JOIN adm_contrato_produto_servico E ON (A.cd_empresa         = E.cd_empresa" +
                    "                                               AND A.cd_produto_servico = E.cd_produto_servico" +
                    "                                               AND A.cd_referencia      = E.cd_referencia ) "+
                    "LEFT OUTER JOIN adm_contrato F ON (F.cd_contrato = E.cd_contrato" +
                    "                               AND F.st_contrato IN (1,2)) "+
                    "LEFT OUTER JOIN grl_pessoa   G ON (G.cd_pessoa   = F.cd_pessoa) "+
                    "JOIN grl_produto_servico     H ON (H.cd_produto_servico = A.cd_produto_servico) "+
                    "LEFT OUTER JOIN grl_pessoa   I ON (F .cd_agente   = I.cd_pessoa) "+
                    "LEFT OUTER JOIN adm_tipo_operacao L ON (F.cd_tipo_operacao = L.cd_tipo_operacao) "+
                    (cdAtendimento>0 || stReserva>=-1 ? 
                    		"JOIN grl_reserva R ON (A.cd_produto_servico = R.cd_produto_servico " +
                    		"                   AND A.cd_referencia      = R.cd_referencia" +
                    		"                   AND A.cd_empresa         = R.cd_empresa " +
                    		"                   AND R.st_reserva         = 1 " +
                    		"                   AND R.dt_validade       >= current_timestamp " +
                    		(cdAtendimento>0? " AND R.cd_atendimento     = "+cdAtendimento:"")+
                    		(stReserva   >=0? " AND R.tp_reserva         = "+stReserva:"")+")" : "")+
                    "WHERE 1=1 "+
                    (cdLocalArmazenamento>0 ? " AND B.cd_local_armazenamento IN ("+locais+")" : "")+
                    (lgSomenteDisponiveis ? " AND NOT EXISTS (SELECT * FROM adm_contrato_produto_servico EE, adm_contrato FF " +
                    		                "                 WHERE A.cd_empresa         = EE.cd_empresa" +
                                            "                   AND A.cd_produto_servico = EE.cd_produto_servico" +
                                            "                   AND A.cd_referencia      = EE.cd_referencia " +
                                            "                   AND FF.cd_contrato       = EE.cd_contrato" +
                                            "                   AND FF.st_contrato IN (1,2)) ":"")+
                    (stReserva==-2 ? " AND NOT EXISTS (SELECT * FROM grl_reserva R " +
                    		         "                 WHERE A.cd_produto_servico = R.cd_produto_servico " +
                    		         "                   AND A.cd_referencia      = R.cd_referencia" +
                    		         "                   AND A.cd_empresa         = R.cd_empresa" +
                    		         "                   AND R.st_reserva         = 1 " +
                    		         "                   AND R.dt_validade        >= current_timestamp)" :""),
                    		         groups, crt, connect!=null ? connect: Conexao.conectar(), connect==null);
			//
			ResultSetMap rsmReturn = new ResultSetMap();
			while(rsm.next())	{
				// Completando LOCAL DE ARMAZENAMENTO
				int cdLocalSuperior    = rsm.getInt("cd_local_armazenamento_superior");
				String nmLocalSuperior = "";
				while(cdLocalSuperior>0)	{
					ResultSet rs = connect.prepareStatement("SELECT * FROM alm_local_armazenamento WHERE cd_local_armazenamento = "+cdLocalSuperior).executeQuery();
					if(rs.next())	{
						rsm.setValueToField("NM_LOCAL_SUPERIOR2", nmLocalSuperior);
						nmLocalSuperior = rs.getString("nm_local_armazenamento")+(nmLocalSuperior.equals("") ? "" : " -> ")+nmLocalSuperior;
						cdLocalSuperior = rs.getInt("cd_local_armazenamento_superior");
						rsm.setValueToField("NM_LOCAL_RAIZ", rs.getString("nm_local_armazenamento"));
					}
				}
				rsm.setValueToField("NM_LOCAL_SUPERIOR", nmLocalSuperior);
				// Verificando reservas
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa AS nm_atendimento, D.nm_pessoa AS nm_cliente, " +
						                                           "       E.nm_pessoa AS nm_responsavel, F.nm_login, G.nm_pessoa AS nm_usuario " +
						                                           "FROM grl_reserva A " +
						                                           "LEFT OUTER JOIN crm_atendimento B ON (A.cd_atendimento = B.cd_atendimento)" +
						                                           "LEFT OUTER JOIN grl_pessoa      C ON (B.cd_pessoa = C.cd_pessoa) " +
						                                           "LEFT OUTER JOIN grl_pessoa      D ON (A.cd_pessoa = D.cd_pessoa) " +
						                                           "LEFT OUTER JOIN grl_pessoa      E ON (A.cd_responsavel = E.cd_pessoa) " +
						                                           "LEFT OUTER JOIN seg_usuario     F ON (A.cd_usuario = F.cd_usuario) " +
						                                           "LEFT OUTER JOIN grl_pessoa      G ON (F.cd_pessoa = G.cd_pessoa) " +
																   "WHERE A.cd_produto_servico = "+rsm.getInt("cd_produto_servico")+
													     		   "  AND A.cd_referencia      = "+rsm.getInt("cd_referencia")+
													     		   "  AND A.cd_empresa         = "+rsm.getInt("cd_empresa")+
													     		   "  AND A.st_reserva         = 1 "+
													     		   "  AND A.dt_validade       >= ? ");

				pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())	{
					rsm.setValueToField("LG_RESERVA", new Integer(1));
					rsm.setValueToField("CD_RESERVA", rs.getObject("cd_reserva"));
					rsm.setValueToField("TP_RESERVA", rs.getObject("tp_reserva"));
					rsm.setValueToField("DT_RESERVA", rs.getObject("dt_reserva"));
					rsm.setValueToField("DT_VALIDADE", rs.getObject("dt_validade"));
					rsm.setValueToField("NM_RESPONSAVEL_RESERVA", rs.getObject("nm_responsavel"));
					rsm.setValueToField("NM_USUARIO_RESERVA", rs.getObject("nm_usuario"));
					rsm.setValueToField("NM_LOGIN_RESERVA", rs.getObject("nm_login"));
					if(rs.getObject("nm_atendimento")!=null)
						rsm.setValueToField("NM_RESERVA", rs.getObject("nm_atendimento"));
					if(rs.getObject("nm_cliente")!=null)
						rsm.setValueToField("NM_RESERVA", rs.getObject("nm_cliente"));
				}
				//
				rsmReturn.addRegister(rsm.getRegister());
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NM_LOCAL_SUPERIOR");
			orderBy.add("NM_LOCAL_ARMAZENAMENTO");
			orderBy.add("NM_REFERENCIA");			
			rsmReturn.orderBy(orderBy);
			rsmReturn.beforeFirst();
			return rsmReturn;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result insertIn(int cdProduto, int qtReferencias, ArrayList<Integer> locais) {
		Connection connect = Conexao.conectar();
		try	{
			connect.setAutoCommit(false);
			for(int i=0; i<locais.size(); i++)
				insertIn(cdProduto, locais.get(i), qtReferencias, connect);
			connect.commit();
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			Conexao.rollback(connect);
			return new sol.util.Result(-1, "Erro ao tentar criar Referencia!", e);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	public static sol.util.Result insertIn(int cdProduto, int cdLocalArmazenamento, int qtReferencias) {
		return insertIn(cdProduto, cdLocalArmazenamento, qtReferencias, null);
	}
	public static sol.util.Result insertIn(int cdProduto, int cdLocalArmazenamento, int qtReferencias, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			LocalArmazenamento local = LocalArmazenamentoDAO.get(cdLocalArmazenamento, connect);
			int cdEmpresa = local.getCdEmpresa();
			if (isConnectionNull)	{
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int nrReferencia = 0;
			for(int i=1; i<=qtReferencias; i++)	{
				nrReferencia++;
				
				String nmReferencia = local.getIdLocalArmazenamento();
				String idReferencia = "";
				
				if(nmReferencia.indexOf("-")>=0)
					nmReferencia = nmReferencia.substring(nmReferencia.indexOf("-")+1);
				
				nmReferencia += com.tivic.manager.util.Util.fillNum(nrReferencia, 2);
				System.out.println("["+i+"/"+qtReferencias+"]Incluindo referência nº: "+nmReferencia);
				
				String locais = String.valueOf(local.getCdLocalArmazenamento());
				if(local.getIdLocalArmazenamento()==null || local.getIdLocalArmazenamento().equals(""))	{
					locais = LocalArmazenamentoServices.getListaLocaisInferiores(local.getCdLocalArmazenamentoSuperior(), "", connect);
				}
				
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_produto_referencia A " +
						                                            "WHERE nm_referencia = ? " +
						                                            "  AND cd_local_armazenamento IN ("+locais+")");
				pstmt.setString(1, nmReferencia);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())	{
					System.out.print("\t["+i+"/"+qtReferencias+"] referencia duplicada: "+nmReferencia);
					nrReferencia++;
					nmReferencia = local.getIdLocalArmazenamento();
					if(nmReferencia.indexOf("-")>=0)
						nmReferencia = nmReferencia.substring(nmReferencia.indexOf("-")+1);
					nmReferencia += com.tivic.manager.util.Util.fillNum(nrReferencia, nrReferencia < 100 ? 2 : 3);
					pstmt.setString(1, nmReferencia);
					rs = pstmt.executeQuery();
					//
					System.out.println(" ... mudando para: "+nmReferencia);
				}
					
				
				ProdutoReferencia referencia = new ProdutoReferencia(0/*cdReferencia*/, cdProduto, cdEmpresa, nmReferencia, idReferencia, null /*dtValidade*/, 
																	 null /*dtChegada*/, 0/*tpReferencia*/, 1/*stReferencia*/, 0, 0, "", cdLocalArmazenamento);
				
				ProdutoReferenciaDAO.insert(referencia, connect);
			}
				
			if(isConnectionNull)
				connect.commit();
			
			return new sol.util.Result(1);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new sol.util.Result(-1, "Erro ao tentar criar Referencia!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * 
	 * @param cdReferencia
	 * @param cdProdutoServico
	 * @param cdEmpresa
	 * @param newNmReferencia
	 * @param cdLocalArmazenamento
	 * @return
	 * Método que altera os dados na tabela alm_produto_referencia
	 * @author João Marlon
	 */
	public static sol.util.Result update(int cdReferencia, int cdProdutoServico, int cdEmpresa, String newNmReferencia, int cdLocalArmazenamento) {
		return update(cdReferencia, cdProdutoServico, cdEmpresa, newNmReferencia, cdLocalArmazenamento, null);
	}

	public static sol.util.Result update(int cdReferencia, int cdProdutoServico, int cdEmpresa, String newNmReferencia, int cdLocalArmazenamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_produto_servico " +
					                                           "WHERE cd_referencia      = " +cdReferencia+
					                                           "  AND cd_produto_servico = " +cdProdutoServico+
					                                           "  AND cd_empresa         = " +cdEmpresa);
			if(pstmt.executeQuery().next())	{
				return new sol.util.Result(-1, "Essa unidade não pode ser alterada porque já foi vendida!");
			}
			ProdutoReferencia referencia = new ProdutoReferencia(cdReferencia, cdProdutoServico, cdEmpresa, newNmReferencia, ""/*idReferencia*/, null /*dtValidade*/, 
					 										    null /*dtChegada*/, 0/*tpReferencia*/, 1/*stReferencia*/, 0, 0, "", cdLocalArmazenamento);
			
			return new sol.util.Result(ProdutoReferenciaDAO.update(referencia, cdReferencia, cdProdutoServico, cdEmpresa));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.update: " +  e);
			return new sol.util.Result(-1, "Erro ao tentar alterar Unidade", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static sol.util.Result delete(int cdReferencia, int cdProdutoServico, int cdEmpresa) {
		return delete(cdReferencia, cdProdutoServico, cdEmpresa, null);
	}

	public static sol.util.Result delete(int cdReferencia, int cdProdutoServico, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_produto_servico " +
					                                           "WHERE cd_referencia      = " +cdReferencia+
					                                           "  AND cd_produto_servico = " +cdProdutoServico+
					                                           "  AND cd_empresa         = " +cdEmpresa);
			if(pstmt.executeQuery().next())	{
				return new sol.util.Result(-1, "Essa unidade não pode ser excluída porque já foi vendida!");
			}
			return new sol.util.Result(ProdutoReferenciaDAO.delete(cdReferencia, cdProdutoServico, cdEmpresa));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoReferenciaDAO.delete: " +  e);
			return new sol.util.Result(-1, "Erro ao tentar excluir Unidade", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getResumoOfEmpreendimento(ArrayList<sol.dao.ItemComparator> criterios) {
		return getResumoOfEmpreendimento(criterios, null);
	}
	
	public static ResultSetMap getResumoOfEmpreendimento(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT DISTINCT B1.nm_local_armazenamento AS nm_local_empreendimento, "+ 
						 "       E.nm_produto_servico,  "+
						 "       (SELECT COUNT(*) FROM alm_produto_referencia X "+ 
						 "        WHERE A.cd_produto_servico     = X.cd_produto_servico "+
						 "          AND A.cd_local_armazenamento = X.cd_local_armazenamento "+
						 "          AND NOT EXISTS (SELECT * FROM adm_contrato_produto_servico Y "+ 
						 "                          WHERE X.cd_produto_servico = Y.cd_produto_servico "+
						 "                            AND X.cd_referencia      = Y.cd_referencia "+
						 "                            AND X.cd_empresa         = Y.cd_empresa)) AS qt_disponivel, "+
						 "       (SELECT COUNT(*) FROM alm_produto_referencia X  "+
						 "        WHERE A.cd_produto_servico     = X.cd_produto_servico "+
						 "          AND A.cd_local_armazenamento = X.cd_local_armazenamento "+
						 "          AND EXISTS (SELECT * FROM adm_contrato_produto_servico Y "+ 
						 "                          WHERE X.cd_produto_servico = Y.cd_produto_servico "+
						 "                            AND X.cd_referencia      = Y.cd_referencia "+
						 "                            AND X.cd_empresa         = Y.cd_empresa)) AS qt_vendida "+
						 "FROM alm_produto_referencia A "+ 
						 "LEFT OUTER JOIN alm_local_armazenamento B  ON (B.cd_local_armazenamento = A.cd_local_armazenamento) "+
						 "LEFT OUTER JOIN alm_local_armazenamento B1 ON (B1.cd_local_armazenamento = B.cd_local_armazenamento_superior) "+
						 "LEFT OUTER JOIN alm_produto_grupo       C  ON (A.cd_produto_servico = C.cd_produto_servico) "+
						 "LEFT OUTER JOIN grl_produto_servico     E  ON (E.cd_produto_servico = A.cd_produto_servico)  "+
						 "ORDER BY B2.nm_local_armazenamento, B1.nm_local_armazenamento ";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}