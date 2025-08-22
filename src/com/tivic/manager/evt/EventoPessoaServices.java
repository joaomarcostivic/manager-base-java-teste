package com.tivic.manager.evt;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoConta;
import com.tivic.manager.ptc.DocumentoContaDAO;
import com.tivic.manager.ptc.DocumentoDAO;

public class EventoPessoaServices {
	
	public static final String[] tipoParticipacao = {
			"Convidado", // 0
			"Palestrante", // 1
			"Equipe", // 2
			"Participante", // 3
			"Candidato" // 4
	};
	
	public static final String[] tpCargoPublico = {
			"Efetivo", // 0
			"Contratado", // 1
			"Monitor" // 2
	};

	public static Result save(EventoPessoa eventoPessoa){
		return save(eventoPessoa, null);
	}

	public static Result save(EventoPessoa eventoPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(eventoPessoa==null)
				return new Result(-1, "Erro ao salvar. EventoPessoa é nulo");

			ResultSet rs = connect.prepareStatement("SELECT * FROM evt_evento_pessoa " +
                    "WHERE cd_pessoa = " +eventoPessoa.getCdPessoa() +
						"  AND cd_evento = " +eventoPessoa.getCdEvento() +
						"  AND cd_subevento = " +eventoPessoa.getCdSubevento()).executeQuery();
			if(rs.next())
				return new Result(-3, "Já existe esta pessoa a este evento.");

			int retorno;
			if(eventoPessoa.getCdInscricao()==0){
				retorno = EventoPessoaDAO.insert(eventoPessoa, connect);
				eventoPessoa.setCdPessoa(retorno);
			}
			else {
				retorno = EventoPessoaDAO.update(eventoPessoa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "EVENTOPESSOA", eventoPessoa);
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
	public static Result remove(int cdPessoa, int cdEvento, int cdInscricao){
		return remove(cdPessoa, cdEvento, cdInscricao, false, null);
	}
	public static Result remove(int cdPessoa, int cdEvento, int cdInscricao, boolean cascade){
		return remove(cdPessoa, cdEvento, cdInscricao, cascade, null);
	}
	public static Result remove(int cdPessoa, int cdEvento, int cdInscricao, boolean cascade, Connection connect){
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
			retorno = EventoPessoaDAO.delete(cdPessoa, cdEvento, cdInscricao, connect);
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
	
	public static ResultSetMap findCadastros(String nrMatricula) {
		try {
			Connection connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM evt_evento_pessoa " + (nrMatricula != null ? "WHERE id_cadastro=?" :""));
			
			if(nrMatricula != null){
				pstmt.setString(1, nrMatricula);
			}
			
			return new ResultSetMap(pstmt.executeQuery());			
		} catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static ResultSetMap findEventoPessoa(int cdPessoa, Connection connect) {
		try {
			connect = connect == null ? Conexao.conectar() : connect;
			connect.setAutoCommit(false);
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM evt_evento_pessoa A, evt_evento B"
					+ " WHERE cd_pessoa=? "
					+ "   AND A.cd_evento = B.cd_evento AND B.cd_evento != 1");
			
			pstmt.setInt(1, cdPessoa);
			
			return new ResultSetMap(pstmt.executeQuery());			
		} catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static ResultSetMap getAllPessoas(ArrayList<ItemComparator> criterios) {
		return getAllPessoas(criterios, null);
	}
	
	public static ResultSetMap getAllPessoas(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			connect = connect == null ? Conexao.conectar() : connect;
			connect.setAutoCommit(true);
			
			int qtFrequencia = -1;
			String qtFrequenciaOperator = "";
			String likeNmPessoa = "";
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("QT_FREQUENCIA")){
					qtFrequencia = Integer.parseInt(((ItemComparator)criterios.get(i)).getValue());
					qtFrequenciaOperator = ((ItemComparator)criterios.get(i)).getOperatorComparation();
				} else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("C.NM_PESSOA")){
					likeNmPessoa = ((ItemComparator)criterios.get(i)).getColumn() + " " + ((ItemComparator)criterios.get(i)).getOperatorComparation() + " '" + ((ItemComparator)criterios.get(i)).getValueWithPercentsLike() + "'";
				} else {
					crt.add(criterios.get(i));
				}
			}
			
			ResultSetMap rsm = Search.find(
					"SELECT A.*, E.dt_vencimento_boleto as dt_vencimento, upper(C.NM_PESSOA), B.*, C.*, D.*, E.nm_evento as nm_selecao, B.nm_evento as nm_opcao_selecao, B.vl_inscricao, F.*, G.*, H.*, I.*, J.*, J.txt_mensagem as txt_mensagem_carteira, K.sg_estado as sg_estado_rg, L.*, " +
					"(SELECT COUNT(*) FROM evt_ocorrencia WHERE cd_pessoa = A.cd_pessoa) as QT_FREQUENCIA " +
					"FROM evt_evento_pessoa A " +
					"LEFT OUTER JOIN evt_evento B ON (A.cd_subevento = B.cd_evento)  " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa)  " + 
					"LEFT OUTER JOIN grl_pessoa_fisica D ON (C.cd_pessoa = D.cd_pessoa)  " +
					"LEFT OUTER JOIN evt_evento E ON (A.cd_evento = E.cd_evento) " +
					"LEFT OUTER JOIN grl_pessoa_endereco F ON (C.cd_pessoa = F.cd_pessoa) " +
					"LEFT OUTER JOIN grl_cidade G ON (F.cd_cidade = G.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado H ON (G.cd_estado = H.cd_estado) " +
					"LEFT OUTER JOIN adm_conta_receber I ON (A.cd_conta_receber = I.cd_conta_receber) " +
					"LEFT OUTER JOIN adm_conta_carteira J ON (E.cd_conta = J.cd_conta AND E.cd_conta_carteira = J.cd_conta_carteira) " +
					"LEFT OUTER JOIN grl_estado K ON (D.cd_estado_rg = K.cd_estado) " +
					"LEFT OUTER JOIN evt_local L ON (A.cd_local = L.cd_local)" +
					" WHERE 1=1 " +
					(!likeNmPessoa.equals("") ? "AND " + likeNmPessoa : "") +
					(qtFrequencia > 0 ? " AND (SELECT COUNT(*) FROM evt_ocorrencia WHERE cd_pessoa = A.cd_pessoa) " + qtFrequenciaOperator + " " + qtFrequencia : ""), 
					crt, connect);
			return rsm;			
		} catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static Documento getInscricaoDocumento(String nrDocumento, Connection connect){
		try {
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			Documento documento = null;
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento WHERE nr_documento = '" + nrDocumento + "' LIMIT 1").executeQuery();
			
			
			while(rs.next()){
				documento = DocumentoDAO.get(rs.getInt("cd_documento"), connect);
			}	
			
			return documento;			
		} catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static DocumentoConta getInscricaoConta(int cdDocumento, Connection connect){
		try {
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			DocumentoConta conta = null;
			ResultSet rs = connect.prepareStatement("SELECT * FROM ptc_documento_conta WHERE cd_documento = " + cdDocumento).executeQuery();
			
			
			while(rs.next()){
				conta = DocumentoContaDAO.get(rs.getInt("cd_documento"), rs.getInt("cd_documento_conta"), connect);
			}	
			
			return conta;			
		} catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return EventoPessoaDAO.find(criterios, connect);
	}
	
	public static ResultSetMap findCadastrosByEvento(int cdEvento, int cdPessoa) {
		return findCadastrosByEvento(cdEvento, cdPessoa, null);
	}
	
	public static ResultSetMap findCadastrosByEvento(int cdEvento, int cdPessoa, Connection connect) {
		try {			
			
			connect = connect == null ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.*, C.nm_pessoa, C.nm_email, D.nr_cpf, D.nr_rg, D.dt_nascimento, F.*, G.*, H.* "+ 
					"FROM evt_evento_pessoa A " +
					"LEFT OUTER JOIN evt_evento B ON (A.cd_evento = B.cd_evento) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_fisica D ON (C.cd_pessoa = D.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_endereco F ON (a.cd_pessoa = F.cd_pessoa) " +
					"LEFT OUTER JOIN grl_cidade G ON (F.cd_cidade = G.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado H ON (G.cd_estado = H.cd_estado) " +
					"WHERE B.cd_evento = ? "+(cdPessoa > 0 ? " AND A.cd_pessoa = " + cdPessoa : "")+" ORDER BY C.nm_pessoa ");
			pstmt.setInt(1, cdEvento);

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("NM_PESSOA", rsm.getString("NM_PESSOA").toUpperCase());
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NM_PESSOA");
			rsm.orderBy(orderBy);
			
			rsm.beforeFirst();
			
			return rsm;			
		} catch (Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static ResultSetMap findCadastrosBySubevento(int cdSubevento, int cdPessoa) {
		return findCadastrosByEvento(cdSubevento, cdPessoa, null);
	}
	
	public static ResultSetMap findCadastrosBySubevento(int cdEvento, int cdSubevento, int cdPessoa, Connection connect) {
		try {			
			
			connect = connect == null ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.*, C.nm_pessoa, D.nr_cpf, D.nr_rg, D.dt_nascimento "+ 
					"FROM evt_evento_pessoa A " +
					"LEFT OUTER JOIN evt_evento B ON (A.cd_evento = B.cd_evento) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_fisica D ON (C.cd_pessoa = D.cd_pessoa) " +
					"WHERE A.cd_evento = ? AND A.cd_subevento = ? "+(cdPessoa > 0 ? " AND A.cd_pessoa = " + cdPessoa : "")+" ORDER BY C.nm_pessoa ");
			pstmt.setInt(1, cdEvento);
			pstmt.setInt(2, cdSubevento);

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("NM_PESSOA", rsm.getString("NM_PESSOA").toUpperCase());
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NM_PESSOA");
			rsm.orderBy(orderBy);
			
			rsm.beforeFirst();
			
			return rsm;			
		} catch (Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static ResultSetMap getAllInscritos(ArrayList<ItemComparator> criterios){
		return getAllInscritos(criterios, null);
	}
	
	public static ResultSetMap getAllInscritos(ArrayList<ItemComparator> criterios, Connection connect) {
		try {			
			
			connect = connect == null ? Conexao.conectar() : connect;

//			@SuppressWarnings("deprecation")
			ResultSetMap rsm = Search.find(
					"SELECT  A.*, B.*, C.nm_pessoa, C.nm_email, D.nr_cpf, D.nr_rg, D.dt_nascimento, E.nm_evento as nm_subevento, E.vl_inscricao as vl_inscricao_vaga, F.*, "+ 
					"G.nm_cidade, H.nm_estado, H.sg_estado, F1.*, (SELECT string_agg( sB.nm_doenca, ',') as nm_doencas FROM grl_pessoa_doenca sA, grl_doenca sB where sA.cd_pessoa = A.cd_pessoa AND sA.cd_doenca = sB.cd_doenca) as nm_doenca, " + 
					"(SELECT SUM(vl_conta) FROM adm_conta_receber WHERE ST_CONTA = 1 AND CD_CONTA_RECEBER IN (SELECT cd_conta_receber FROM evt_evento_pessoa WHERE cd_conta_receber is not null AND cd_evento = B.cd_evento)) as VL_TOTAL_RECEBIDO " +
					"FROM evt_evento_pessoa A " +
					"LEFT OUTER JOIN evt_evento B ON (A.cd_evento = B.cd_evento) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_fisica D ON (C.cd_pessoa = D.cd_pessoa) " + 
					"LEFT OUTER JOIN evt_evento E ON (A.cd_subevento = E.cd_evento) " +
					"LEFT OUTER JOIN adm_conta_receber F ON (A.cd_conta_receber = F.cd_conta_receber) " +
					"LEFT OUTER JOIN grl_pessoa_endereco F1 ON (A.cd_pessoa = F1.cd_pessoa) " +
					"LEFT OUTER JOIN grl_cidade G ON (F1.cd_cidade = G.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado H ON (G.cd_estado = H.cd_estado) " +
					"WHERE 1=1", criterios, connect);
			
			return rsm;			
		} catch (Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	public static Result toggleSituacaoInscricao(int cdEvento, int cdPessoa, int cdInscricao) {
		return toggleSituacaoInscricao(cdEvento, cdPessoa, cdInscricao);
	}
	
	public static Result toggleSituacaoInscricao(int cdEvento, int cdPessoa, int cdInscricao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;


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

}
