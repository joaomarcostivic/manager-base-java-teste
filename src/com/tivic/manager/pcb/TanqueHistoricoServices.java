package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.adm.Turno;
import com.tivic.manager.adm.TurnoDAO;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

@DestinationConfig(enabled = false)
public class TanqueHistoricoServices {

	public static int TP_TANQUE_HISTORICO_PRODUTO = 0;
	
	
	public static final String[] tipoTanqueHistorico = {"Combustivel"};
	
	public static Result save(TanqueHistorico obj){
		return save(obj, null);
	}
	
	public static Result save(TanqueHistorico obj, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Mudança de Tanque
			if(obj.getTpTanqueHistorico()==TP_TANQUE_HISTORICO_PRODUTO) {
				if(obj.getCdCombustivelAnterior()<=0 || obj.getCdCombustivelNovo()<=0)
					return new Result(-1, "Você deve informar o código do combustível! O anterior e o novo!");
			}
			else
				return new Result(-1, "Tipo de Manutenção desconhecido!");

			// Mudança de Bico
			
			int ret = 0;
			if(obj.getCdTanqueHistorico() <= 0)
				ret = TanqueHistoricoDAO.insert(obj, connect);
			else 
				ret = TanqueHistoricoDAO.update(obj, connect);
			/*
			 * MUDANÇA DE COMBUSTÍVEL
			 */
			if(ret > 0 && obj.getTpTanqueHistorico()==TP_TANQUE_HISTORICO_PRODUTO) {
				Turno turno   = TurnoDAO.get(obj.getCdTurno(), connect);
				Tanque tanque = TanqueDAO.get(obj.getCdTanque(), connect);
				// ENCERRANTES
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_encerrante A, adm_conta_fechamento B, adm_turno C, adm_conta_financeira D " +
						                                           "WHERE A.cd_conta      = B.cd_conta" +
						                                           "  AND A.cd_fechamento = B.cd_fechamento " +
						                                           "  AND A.cd_conta      = D.cd_conta " +
						                                           "  AND D.cd_empresa    = "+tanque.getCdEmpresa()+
						                                           "  AND B.cd_turno      = C.cd_turno " +
						                                           "  AND A.cd_tanque     = "+obj.getCdTanque()+
						                                           "  AND ((CAST(B.dt_fechamento AS DATE) = ? AND C.nr_ordem >= "+turno.getNrOrdem()+") " +
						                                           "    OR (CAST(B.dt_fechamento AS DATE) > ?))"); 
				pstmt.setTimestamp(1, new Timestamp(obj.getDtTanqueHistorico().getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(obj.getDtTanqueHistorico().getTimeInMillis()));
				ResultSetMap rsmEncerrantes = new ResultSetMap(pstmt.executeQuery());
				while(rsmEncerrantes.next()) {
					BicoEncerrante encerrante = BicoEncerranteDAO.get(rsmEncerrantes.getInt("cd_conta"), rsmEncerrantes.getInt("cd_fechamento"), rsmEncerrantes.getInt("cd_bico"), connect);
					encerrante.setCdCombustivel(obj.getCdCombustivelNovo());
					BicoEncerranteDAO.update(encerrante, connect);
				}
				// MEDIÇÕES FÍSICAS
				pstmt = connect.prepareStatement("SELECT * FROM pcb_medicao_fisica A, adm_conta_fechamento B, adm_turno C, adm_conta_financeira D " +
						                         "WHERE A.cd_conta      = B.cd_conta " +
						                         "  AND A.cd_fechamento = B.cd_fechamento " +
		                                         "  AND A.cd_conta      = D.cd_conta " +
		                                         "  AND D.cd_empresa    = "+tanque.getCdEmpresa()+
						                         "  AND B.cd_turno      = C.cd_turno " +
						                         "  AND A.cd_tanque     = "+obj.getCdTanque()+
						                         "  AND ((CAST(B.dt_fechamento AS DATE) = ? AND C.nr_ordem >= "+turno.getNrOrdem()+") " +
						                         "    OR (CAST(B.dt_fechamento AS DATE) > ?))"); 
				pstmt.setTimestamp(1, new Timestamp(obj.getDtTanqueHistorico().getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(obj.getDtTanqueHistorico().getTimeInMillis()));
				ResultSetMap rsmMedFis = new ResultSetMap(pstmt.executeQuery());
				while(rsmMedFis.next()) {
					MedicaoFisica medicao = MedicaoFisicaDAO.get(rsmMedFis.getInt("cd_conta"), rsmMedFis.getInt("cd_fechamento"), obj.getCdTanque(), connect);
					medicao.setCdCombustivel(obj.getCdCombustivelNovo());
					MedicaoFisicaDAO.update(medicao, connect);
				}
			}
			//
			verificarTrocaCombustivel(obj.getCdTanque(), null, 0, connect);
			return new Result(ret, "");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar a manutenção de tanque!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/*
	 * Esse método verifica a ultima mudança de combustível e atribui ao tanque
	 */
	public static Result verificarTrocaCombustivel(int cdTanque, GregorianCalendar dtAbertura, int cdTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			if(cdTanque <= 0) {
				ResultSet rs = connect.prepareStatement("SELECT * FROM pcb_tanque").executeQuery();
				while(rs.next())
					verificarTrocaCombustivel(rs.getInt("cd_tanque"), dtAbertura, cdTurno, connect);
				
				return new Result(1, "");
			}
			/*
			 * MUDANÇA DE COMBUSTIVEL NO TANQUE
			 */
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_tanque_historico A, adm_turno B " +
											                   "WHERE A.cd_turno            = B.cd_turno " +
											                   "  AND A.cd_tanque           = "+cdTanque+
											                   "  AND A.tp_tanque_historico = "+TP_TANQUE_HISTORICO_PRODUTO+
											                   (cdTurno>0        ? " AND A.cd_turno = "+cdTurno : "" )+
											                   (dtAbertura!=null ? " AND A.dt_tanque_historico = ? " : "" )+
											                   "ORDER BY A.dt_tanque_historico DESC, B.nr_ordem DESC ");
			if(dtAbertura != null)
				pstmt.setTimestamp(1, new Timestamp(dtAbertura.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Tanque tanque = TanqueDAO.get(cdTanque, connect);
				System.out.println("Novo Combustível: "+rs.getInt("cd_combustivel_novo"));
				tanque.setCdProdutoServico(rs.getInt("cd_combustivel_novo"));
				TanqueDAO.update(tanque);
			}
			//
			return new Result(1, "");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar a manutenção de tanque!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		String sql = "SELECT TH.*, T.nm_turno, PS1.nm_produto_servico AS nm_combustivel_anterior, PS2.nm_produto_servico AS nm_combustivel_novo, " +
				     "       P.nm_pessoa as nm_usuario, PT.nm_pessoa AS nm_tecnico, PE.nm_pessoa AS nm_empresa_interventora " +
				     "FROM pcb_tanque_historico TH " +
				     "LEFT OUTER JOIN grl_produto_servico PS1 ON (TH.cd_combustivel_anterior = PS1.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_produto_servico PS2 ON (TH.cd_combustivel_novo     = PS2.cd_produto_servico) " +
					 "LEFT OUTER JOIN adm_turno                T ON (TH.cd_turno = T.cd_turno) " +
					 "LEFT OUTER JOIN seg_usuario     		   S ON (TH.cd_usuario = S.cd_usuario) " +
					 "LEFT OUTER JOIN grl_pessoa               P ON (S.cd_pessoa = P.cd_pessoa) "+
		 			 "LEFT OUTER JOIN grl_pessoa               PT ON (TH.cd_tecnico = PT.cd_pessoa) "+
		 			 "LEFT OUTER JOIN grl_pessoa               PE ON (TH.cd_empresa_interventora = PE.cd_pessoa) ";
		return Search.find(sql, "ORDER BY dt_tanque_historico DESC, T.nr_ordem DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static String getTanqueHistoricoByCombustivel(int cdCombustivel, GregorianCalendar dtMovimento){
		return getTanqueHistoricoByCombustivel(cdCombustivel, dtMovimento, null);
	}
	
	public static String getTanqueHistoricoByCombustivel(int cdCombustivel, GregorianCalendar dtMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			dtMovimento = (GregorianCalendar)dtMovimento.clone();
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			
			String sql = "SELECT * " +
					     "FROM pcb_tanque_historico TH " +
						 "WHERE (TH.cd_combustivel_novo = "+cdCombustivel+" OR TH.cd_combustivel_anterior = "+cdCombustivel+") " +
						 "  AND CAST(TH.dt_tanque_historico AS DATE) = ?";
						
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			String txtObservacao = "";
			while(rsm.next())
				txtObservacao += (txtObservacao.equals("") ? "" : "\n" ) + rsm.getString("txt_observacao");
			return txtObservacao;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return "";
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
