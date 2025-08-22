package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.adm.Turno;
import com.tivic.manager.adm.TurnoDAO;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;

@DestinationConfig(enabled = false)
public class BicoHistoricoServices {

	public static int TP_BICO_HISTORICO_TANQUE        = 0;
	public static int TP_BICO_HISTORICO_ENCERRANTE_LT = 1;
	public static int TP_BICO_HISTORICO_IDBICO        = 2;
	
	public static final String[] tipoBicoHistorico = {"Mudança de Tanque", "Mudança de Bico", "Código"};

	public static Result save(BicoHistorico obj){
		return save(obj, null);
	}
	
	public static Result save(BicoHistorico obj, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			// Mudança de Tanque
			if(obj.getTpBicoHistorico()==TP_BICO_HISTORICO_TANQUE) {
				if(obj.getCdTanqueAnterior()<=0 || obj.getCdTanqueNovo()<=0)
					return new Result(-1, "Você deve informar o código dos tanques! O anterior e o novo!");
			}
			// Mudança de Bico
			
			int ret = 0;
			if(obj.getCdBicoHistorico() <= 0)
				ret = BicoHistoricoDAO.insert(obj, connect);
			else 
				ret = BicoHistoricoDAO.update(obj, connect);
			/*
			 * MUDANÇA DE TANQUE no BICO
			 */
			if(ret > 0 && obj.getTpBicoHistorico()==TP_BICO_HISTORICO_TANQUE) {
				Turno turno = TurnoDAO.get(obj.getCdTurno(), connect);
				
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_encerrante A, adm_conta_fechamento B, adm_turno C " +
						                                           "WHERE A.cd_conta      = B.cd_conta" +
						                                           "  AND A.cd_fechamento = B.cd_fechamento " +
						                                           "  AND B.cd_turno      = C.cd_turno " +
						                                           "  AND A.cd_bico       = "+obj.getCdBico()+
						                                           "  AND ((CAST(B.dt_fechamento AS DATE) = ? AND C.nr_ordem >= "+turno.getNrOrdem()+") " +
						                                           "    OR (CAST(B.dt_fechamento AS DATE) > ?))"); 
				pstmt.setTimestamp(1, new Timestamp(obj.getDtBicoHistorico().getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(obj.getDtBicoHistorico().getTimeInMillis()));
				ResultSetMap rsmEncerrantes = new ResultSetMap(pstmt.executeQuery());
				while(rsmEncerrantes.next()) {
					BicoEncerrante encerrante = BicoEncerranteDAO.get(rsmEncerrantes.getInt("cd_conta"), rsmEncerrantes.getInt("cd_fechamento"), obj.getCdBico(), connect);
					encerrante.setCdTanque(obj.getCdTanqueNovo());
					BicoEncerranteDAO.update(encerrante, connect);
				}
			}
			//
			verificarTrocaTanque(obj.getCdBico(), null, 0, connect);
			return new Result(ret, "");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar a manutenção de bico!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result verificarTrocaTanque(int cdBico, GregorianCalendar dtAbertura, int cdTurno, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			if(cdBico <= 0) {
				ResultSet rs = connect.prepareStatement("SELECT * FROM pcb_bico").executeQuery();
				while(rs.next())
					verificarTrocaTanque(rs.getInt("cd_bico"), dtAbertura, cdTurno, connect);
				return new Result(1, "");
			}
				
			/*
			 * MUDANÇA DE TANQUE NO BICO
			 */
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM pcb_bico_historico A, adm_turno B " +
								                    "WHERE A.cd_turno          = B.cd_turno " +
								                    "  AND A.cd_bico           = "+cdBico+
								                    "  AND A.tp_bico_historico = "+TP_BICO_HISTORICO_TANQUE+
								                    (cdTurno>0        ? " AND A.cd_turno = "+cdTurno : "" )+
								                    (dtAbertura!=null ? " AND A.dt_bico_historico = ? " : "" )+
								                    "ORDER BY A.dt_bico_historico DESC, B.nr_ordem DESC ");
			if(dtAbertura != null)
				pstmt.setTimestamp(1, new Timestamp(dtAbertura.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Bico bico = BicoDAO.get(cdBico, connect);
				bico.setCdTanque(rs.getInt("cd_tanque_novo"));
				BicoDAO.update(bico);
			}
			//
			return new Result(1, "");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Falha ao tentar salvar a manutenção de bico!", e);
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
		String sql = "SELECT BH.*, T.nm_turno, L1.nm_local_armazenamento AS nm_tanque_anterior, L2.nm_local_armazenamento AS nm_tanque_novo, " +
				     "       P.nm_pessoa as nm_usuario, PT.nm_pessoa AS nm_tecnico, PE.nm_pessoa AS nm_empresa_interventora " +
				     "FROM pcb_bico_historico BH " +
					 "LEFT OUTER JOIN alm_local_armazenamento L1 ON (BH.cd_tanque_anterior = L1.cd_local_armazenamento) " +
					 "LEFT OUTER JOIN alm_local_armazenamento L2 ON (BH.cd_tanque_novo     = L2.cd_local_armazenamento) " +
					 "LEFT OUTER JOIN adm_turno                T ON (BH.cd_turno = T.cd_turno) " +
					 "LEFT OUTER JOIN seg_usuario     		   S ON (BH.cd_usuario = S.cd_usuario) " +
					 "LEFT OUTER JOIN grl_pessoa               P ON (S.cd_pessoa = P.cd_pessoa) "+
					 "LEFT OUTER JOIN grl_pessoa               PT ON (BH.cd_tecnico = PT.cd_pessoa) "+
					 "LEFT OUTER JOIN grl_pessoa               PE ON (BH.cd_empresa_interventora = PE.cd_pessoa) ";
		return Search.find(sql, "ORDER BY BH.dt_bico_historico DESC, T.nr_ordem DESC ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static String getHistoricoBicoByCombustivel(int cdCombustivel, GregorianCalendar dtMovimento){
		return getHistoricoBicoByCombustivel(cdCombustivel, dtMovimento, null);
	}
	
	public static String getHistoricoBicoByCombustivel(int cdCombustivel, GregorianCalendar dtMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			dtMovimento = (GregorianCalendar)dtMovimento.clone();
			dtMovimento.set(Calendar.HOUR, 0);
			dtMovimento.set(Calendar.MINUTE, 0);
			dtMovimento.set(Calendar.SECOND, 0);
			
			String sql = "SELECT * " +
					     "FROM pcb_bico_historico BH " +
						 "LEFT OUTER JOIN pcb_bico   B ON (BH.cd_bico  = B.cd_bico) " +
						 "LEFT OUTER JOIN pcb_tanque C ON (B.cd_tanque = C.cd_tanque" +
						 "                             AND C.cd_produto_servico = "+cdCombustivel+") " +
						 "WHERE (B.cd_tanque           = C.cd_tanque " +
						 "   OR BH.cd_tanque_anterior  = C.cd_tanque " +
						 "   OR BH.cd_tanque_novo      = C.cd_tanque) " +
						 "  AND CAST(BH.dt_bico_historico AS DATE) = ?";
						
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			String txtObservacao = "";
			while(rsm.next())
				txtObservacao += (txtObservacao.equals("") ? "" : "\n" ) + rsm.getString("txt_observacao");
			return txtObservacao;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return "";
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
