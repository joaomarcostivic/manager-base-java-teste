package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.Contrato;
import com.tivic.manager.adm.ContratoDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;


import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class MatriculaReservaServices {

	public static final String[] situacao = {"Não confirmada",
		"Confirmada",
		"Cancelada"};

	public static final int ST_NAO_CONFIRMADA = 0;
	public static final int ST_CONFIRMADA = 1;
	public static final int ST_CANCELADA = 2;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, H.nm_pessoa AS nm_aluno, G.dt_nascimento, G.nr_cpf, G.sg_orgao_rg, " +
				"G.nm_mae, G.nm_pai, G.tp_sexo, C.cd_convenio, " +
				"C.cd_categoria_economica, C.cd_empresa AS cd_instituicao, C.cd_pessoa, C.cd_modelo_contrato, C.cd_indicador, " +
				"C.dt_assinatura, C.dt_primeira_parcela, C.nr_dia_vencimento, C.nr_parcelas, C.pr_juros_mora, C.pr_multa_mora, " +
				"C.pr_desconto_adimplencia, C.pr_desconto, C.tp_contrato, C.vl_parcelas, C.vl_adesao, C.vl_contrato, C.nr_contrato, " +
				"C.txt_contrato, C.st_contrato, C.id_contrato, C.dt_inicio_vigencia, C.dt_final_vigencia, C.cd_agente, C.cd_conta_carteira, " +
				"C.cd_conta, C.tp_amortizacao, C.gn_contrato, C.pr_juros, C.cd_tipo_operacao, C.cd_documento, " +
				"I.nm_pessoa AS nm_contratante, E.nm_produto_servico AS nm_curso, F.nm_turma " +
				"FROM acd_matricula_reserva A " +
				"LEFT OUTER JOIN acd_aluno B ON (A.cd_aluno = B.cd_aluno) " +
				"LEFT OUTER JOIN grl_pessoa_fisica G ON (B.cd_aluno = G.cd_pessoa) " +
				"LEFT OUTER JOIN grl_pessoa H ON (G.cd_pessoa = H.cd_pessoa) " +
				"LEFT OUTER JOIN adm_contrato C ON (A.cd_contrato = C.cd_contrato) " +
				"LEFT OUTER JOIN grl_pessoa I ON (C.cd_pessoa = I.cd_pessoa) " +
				"LEFT OUTER JOIN acd_curso D ON (A.cd_instituicao = D.cd_instituicao AND " +
				"								 A.cd_curso = D.cd_curso) " +
				"LEFT OUTER JOIN grl_produto_servico E ON (D.cd_curso = E.cd_produto_servico) " +
				"LEFT OUTER JOIN acd_turma F ON (A.cd_turma = F.cd_turma) " +
				"WHERE 1 = 1", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static HashMap<String, Object> insert(MatriculaReserva reserva, Contrato contrato) {
		return insert(reserva, contrato, null);
	}

	public static HashMap<String, Object> insert(MatriculaReserva reserva) {
		return insert(reserva, null, null);
	}

	public static HashMap<String, Object> insert(MatriculaReserva reserva, Contrato contrato, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("codeResult", 1);

			int cdContrato = 0;
			if (contrato!=null && (cdContrato = ContratoDAO.insert(contrato, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				hash.put("codeResult", -1);
				return hash;
			}

			reserva.setIdReserva(getNrReserva(connection));
			reserva.setCdContrato(cdContrato);
			int cdReserva = 0;
			if ((cdReserva = MatriculaReservaDAO.insert(reserva, connection)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				hash.put("codeResult", -1);
				return hash;
			}

			if (isConnectionNull)
				connection.commit();

			hash.put("cdReserva", cdReserva);
			hash.put("cdContrato", cdContrato);
			hash.put("idReserva", reserva.getIdReserva());
			return hash;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static String getNrReserva() {
		return getNrReserva(null);
	}

	public static String getNrReserva(Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT MAX(id_reserva) AS last " +
					"FROM acd_matricula_reserva " +
					"WHERE dt_reserva >= ? " +
					"  AND dt_reserva <= ?");
			pstmt.setTimestamp(1, Util.convCalendarToTimestamp(new GregorianCalendar(new GregorianCalendar().get(Calendar.YEAR), Calendar.JANUARY,
					1)));
			pstmt.setTimestamp(2, Util.convCalendarToTimestamp(new GregorianCalendar(new GregorianCalendar().get(Calendar.YEAR), Calendar.DECEMBER, 31)));
			ResultSet rs = pstmt.executeQuery();
			return rs==null || !rs.next() || rs.getString("last")==null || rs.getString("last").trim().equals("") ?
					"0101" + Integer.toString(new GregorianCalendar().get(Calendar.YEAR)).substring(2) :
					new DecimalFormat("0000").format(Integer.parseInt(rs.getString("last").substring(0, 4) + 1)) + Integer.toString(new GregorianCalendar().get(Calendar.YEAR)).substring(2);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaServices.getNrReserva: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static HashMap<String, Object> update(MatriculaReserva reserva, Contrato contrato) {
		return update(reserva, contrato, null);
	}

	public static HashMap<String, Object> update(MatriculaReserva reserva) {
		return update(reserva, null, null);
	}

	public static HashMap<String, Object> update(MatriculaReserva reserva, Contrato contrato, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("codeResult", 1);

			if (contrato == null) {
				MatriculaReserva matrReserva = MatriculaReservaDAO.get(reserva.getCdReserva(), connection);
				if (matrReserva.getCdContrato()>0)
					if (ContratoDAO.delete(matrReserva.getCdContrato(), connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						hash.put("codeResult", -1);
						return hash;
					}
			}

			if (MatriculaReservaDAO.update(reserva, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				hash.put("codeResult", -1);
				return hash;
			}


			if (contrato!=null && ContratoDAO.update(contrato, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				hash.put("codeResult", -1);
				return hash;
			}

			if (isConnectionNull)
				connection.commit();

			hash.put("cdReserva", reserva.getCdReserva());
			hash.put("cdContrato", contrato==null ? 0 : contrato.getCdContrato());
			hash.put("idReserva", reserva.getIdReserva());
			return hash;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaReservaServices.update: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
