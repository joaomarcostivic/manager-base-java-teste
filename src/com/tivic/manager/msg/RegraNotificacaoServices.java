package com.tivic.manager.msg;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.sun.xml.internal.ws.encoding.StringDataContentHandler;
import com.tivic.manager.agd.AgendaItem;
import com.tivic.manager.agd.AgendaItemDAO;
import com.tivic.manager.agd.GrupoPessoaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.prc.ProcessoDAO;
import com.tivic.manager.prc.ProcessoServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.seg.GrupoServices;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.Util;

public class RegraNotificacaoServices {
	
	public static int TP_ENTIDADE_PROCESSO  = 0;
	public static int TP_ENTIDADE_ANDAMENTO = 1;
	public static int TP_ENTIDADE_ARQUIVO   = 2;
	public static int TP_ENTIDADE_FATURA    = 3;
	public static int TP_ENTIDADE_AGENDA    = 4;
	
	public static int TP_ACAO_DELETE 	   = 0;
	public static int TP_ACAO_INSERT 	   = 1;
	public static int TP_ACAO_UPDATE 	   = 2;
	public static int TP_ACAO_PERIODICAL   = 3;
	public static int TP_ACAO_NAO_AJUIZADO = 4;
	
	public static int ST_INATIVO = 0;
	public static int ST_ATIVO   = 1;
	
	public static Result save(RegraNotificacao regraNotificacao){
		return save(regraNotificacao, null, null, null, null, null);
	}

	public static Result save(RegraNotificacao regraNotificacao, AuthData authData){
		return save(regraNotificacao, null, null, null, authData, null);
	}
	
	public static Result save(RegraNotificacao regraNotificacao, 
			ArrayList<NotificacaoUsuario> usuarios, ArrayList<NotificacaoGrupoUsuario> gruposUsuario, ArrayList<NotificacaoGrupo> gruposTrabalho,
			AuthData authData) {
		return save(regraNotificacao, usuarios, gruposUsuario, gruposTrabalho, authData, null);
	}

	public static Result save(RegraNotificacao regraNotificacao, 
			ArrayList<NotificacaoUsuario> usuarios, ArrayList<NotificacaoGrupoUsuario> gruposUsuario, ArrayList<NotificacaoGrupo> gruposTrabalho,
			AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(regraNotificacao==null)
				return new Result(-1, "Erro ao salvar. RegraNotificacao é nulo");
			
			if(regraNotificacao.getTpAcao()==TP_ACAO_NAO_AJUIZADO) {
				ArrayList<ItemComparator> crt = new ArrayList<>();
				crt.add(new ItemComparator("A.cd_regra_notificacao", Integer.toString(regraNotificacao.getCdRegraNotificacao()), ItemComparator.DIFFERENT, Types.INTEGER));
				crt.add(new ItemComparator("A.tp_acao", Integer.toString(TP_ACAO_NAO_AJUIZADO), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = find(crt, connect);
				if(rsm.next()) {
					connect.rollback();
					return new Result(-6, "Já existe uma regra para notificar processos não ajuizados.");
				}				
			}
			
			int retorno;
			if(regraNotificacao.getCdRegraNotificacao()==0){
				retorno = RegraNotificacaoDAO.insert(regraNotificacao, connect);
				regraNotificacao.setCdRegraNotificacao(retorno);
			}
			else {
				retorno = RegraNotificacaoDAO.update(regraNotificacao, connect);
			}
			
			if(retorno<=0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inclur regra.");
			}
			
			/*
			 * Usuários
			 */
			if(usuarios!=null) {
				Result r = NotificacaoUsuarioServices.removeAll(regraNotificacao.getCdRegraNotificacao(), authData, connect);
				if(r.getCode()<0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return r;
				}
				
				for (NotificacaoUsuario usuario : usuarios) {
					usuario.setCdRegraNotificacao(regraNotificacao.getCdRegraNotificacao());
					
					retorno = NotificacaoUsuarioServices.save(usuario, authData, connect).getCode();
					if(retorno<=0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-2, "Erro ao inclur usuário.");
					}
				}
			}
			
			/*
			 * Grupos de Usuários
			 */
			if(gruposUsuario!=null) {
				Result r = NotificacaoGrupoUsuarioServices.removeAll(regraNotificacao.getCdRegraNotificacao(), authData, connect);
				if(r.getCode()<0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return r;
				}
				
				for(NotificacaoGrupoUsuario grupo : gruposUsuario) {
					grupo.setCdRegraNotificacao(regraNotificacao.getCdRegraNotificacao());
					
					retorno = NotificacaoGrupoUsuarioServices.save(grupo, authData, connect).getCode();
					if(retorno<=0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-3, "Erro ao inclur grupo de usuário.");
					}
				}
			}
			
			/*
			 * Grupos de Trabalho
			 */
			if(gruposTrabalho!=null) {
				Result r = NotificacaoGrupoServices.removeAll(regraNotificacao.getCdRegraNotificacao(), authData, connect);
				if(r.getCode()<0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return r;
				}
				
				for(NotificacaoGrupo grupo : gruposTrabalho) {
					grupo.setCdRegraNotificacao(regraNotificacao.getCdRegraNotificacao());
					
					retorno = NotificacaoGrupoServices.save(grupo, authData, connect).getCode();
					if(retorno<=0) {
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-3, "Erro ao inclur grupo de trabalho.");
					}
				}
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			Result result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "REGRANOTIFICACAO", regraNotificacao);
			result.addObject("NOTIFICACAOUSUARIO", 		usuarios);
			result.addObject("NOTIFICACAOGRUPOUSUARIO", gruposUsuario);
			result.addObject("NOTIFICACAOGRUPO", 		gruposTrabalho);
			
			return result;
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
	public static Result remove(RegraNotificacao regraNotificacao) {
		return remove(regraNotificacao.getCdRegraNotificacao());
	}
	public static Result remove(int cdRegraNotificacao){
		return remove(cdRegraNotificacao, false, null, null);
	}
	public static Result remove(int cdRegraNotificacao, boolean cascade){
		return remove(cdRegraNotificacao, cascade, null, null);
	}
	public static Result remove(int cdRegraNotificacao, boolean cascade, AuthData authData){
		return remove(cdRegraNotificacao, cascade, authData, null);
	}
	public static Result remove(int cdRegraNotificacao, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				retorno = NotificacaoUsuarioServices.removeAll(cdRegraNotificacao, authData, connect).getCode();
				if(retorno>0)
					retorno = NotificacaoGrupoUsuarioServices.removeAll(cdRegraNotificacao, authData, connect).getCode();
				if(retorno>0)
					retorno = NotificacaoGrupoServices.removeAll(cdRegraNotificacao, authData, connect).getCode();	
			}
			
			if(!cascade || retorno>0)
				retorno = RegraNotificacaoDAO.delete(cdRegraNotificacao, connect);
			
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
			pstmt = connect.prepareStatement("SELECT * FROM msg_regra_notificacao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		String nmRegraNotificacao = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("A.NM_REGRA_NOTIFICACAO")) {
				nmRegraNotificacao =	Util.limparTexto(criterios.get(i).getValue());
				nmRegraNotificacao = nmRegraNotificacao.trim();
				criterios.remove(i);
				i--;
			}
		}
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = null;

			String sql = 
					" SELECT "
					+ " A.*, "
					+ " B.nr_processo, "
					+ " C.nm_grupo AS nm_grupo_trabalho, "
					+ " D.nm_pessoa AS nm_advogado, "
					+ " E.nm_pessoa AS nm_cliente, "
					+ " F.nm_pessoa AS nm_responsavel_agenda, "
					+ " G.nm_tipo_andamento,"
					+ " H.nm_grupo AS nm_grupo_responsavel_agenda"
					+ " FROM msg_regra_notificacao 			A"
					+ " LEFT OUTER JOIN prc_processo 		B ON (A.cd_processo 	 	  		= B.cd_processo)"
					+ " LEFT OUTER JOIN agd_grupo 			C ON (A.cd_grupo_trabalho 	  		= C.cd_grupo)"
					+ " LEFT OUTER JOIN grl_pessoa 			D ON (A.cd_advogado 	 	  		= D.cd_pessoa)"
					+ " LEFT OUTER JOIN grl_pessoa 			E ON (A.cd_cliente		 	  		= E.cd_pessoa)"
					+ " LEFT OUTER JOIN grl_pessoa 			F ON (A.cd_responsavel_agenda 		= F.cd_pessoa)"
					+ " LEFT OUTER JOIN prc_tipo_andamento  G ON (A.cd_tipo_andamento 	  		= G.cd_tipo_andamento)"
					+ " LEFT OUTER JOIN agd_grupo			H ON (A.cd_grupo_responsavel_agenda = H.cd_grupo)"
					+ " WHERE 1=1 "+
					(!nmRegraNotificacao.equals("") ?
							" AND TRANSLATE (A.nm_regra_notificacao, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
							"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmRegraNotificacao)+"%' "
							: "");
			
			rsm = Search.find(sql, " ORDER BY A.lg_ativo DESC, A.nm_regra_notificacao ", criterios, connect, false);
			
			while(rsm.next()) {
				ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
				crt.add(new ItemComparator("A.CD_REGRA_NOTIFICACAO", Integer.toString(rsm.getInt("cd_regra_notificacao")), ItemComparator.EQUAL, Types.INTEGER));
				
				rsm.setValueToField("RSM_USUARIO", NotificacaoUsuarioServices.find(crt, connect));
				rsm.setValueToField("RSM_GRUPO_USUARIO", NotificacaoGrupoUsuarioServices.find(crt, connect));
				rsm.setValueToField("RSM_GRUPO_TRABALHO", NotificacaoGrupoServices.find(crt, connect));
				
				if(rsm.getInt("cd_responsavel_agenda", 0)>0) {
					rsm.setValueToField("CD_RESPONSAVEL", rsm.getInt("cd_responsavel_agenda"));
					rsm.setValueToField("NM_RESPONSAVEL", rsm.getString("nm_responsavel_agenda"));
				}
				else {
					rsm.setValueToField("CD_RESPONSAVEL", rsm.getInt("cd_grupo_responsavel_agenda"));
					rsm.setValueToField("NM_RESPONSAVEL", rsm.getString("nm_grupo_responsavel_agenda"));
				}
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result notificar(int cdRegra, Notificacao notificacao) {
		return notificar(cdRegra, notificacao, 0, 0, null);
	}
	
	public static Result notificar(int cdRegra, Notificacao notificacao, int cdProcesso, int cdAgendaItem) {
		return notificar(cdRegra, notificacao, cdProcesso, cdAgendaItem, null);
	}
	
	public static Result notificar(int cdRegra, Notificacao notificacao, int cdProcesso, int cdAgendaItem, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("A.CD_REGRA_NOTIFICACAO", Integer.toString(cdRegra), ItemComparator.EQUAL, Types.INTEGER));
			
			// USUÁRIOS NOTIFICÁVEIS
			String[] usuarios = new String[0];
			ResultSetMap rsmUsuario = NotificacaoUsuarioServices.find(crt, connect);
			if(rsmUsuario.next()) {
				rsmUsuario.beforeFirst();
				usuarios = Util.concat(usuarios, Util.join(rsmUsuario, "CD_USUARIO", true).split(","), true);
			}

			ResultSetMap rsmGrupoUsuario = NotificacaoGrupoUsuarioServices.find(crt, connect);
			while(rsmGrupoUsuario.next()) {
				usuarios = Util
						.concat(usuarios, Util
								.join(GrupoServices
										.getUsuariosOfGrupo(rsmGrupoUsuario.getInt("CD_GRUPO"), connect), "CD_USUARIO", true).split(","), true);
			}
			
			ResultSetMap rsmGrupoTrabalho = NotificacaoGrupoServices.find(crt, connect);
			while(rsmGrupoTrabalho.next()) {
				//pessoas do grupo
				ResultSetMap rsmPessoaGrupoTrabalho = GrupoPessoaServices.getAllByGrupo(rsmGrupoTrabalho.getInt("CD_GRUPO"), connect);
				
				//usuarios das pessoas
				ArrayList<ItemComparator> crtUser = new ArrayList<ItemComparator>();
				crtUser.add(new ItemComparator("A.CD_PESSOA", Util.join(rsmPessoaGrupoTrabalho, "CD_PESSOA", true), ItemComparator.IN, Types.INTEGER));
				
				usuarios = Util
						.concat(usuarios, Util.join(UsuarioServices.find(crtUser, connect), "CD_USUARIO", true).split(","), true);
			}
			
			RegraNotificacao regra = RegraNotificacaoDAO.get(cdRegra, connect);
			
			AgendaItem agenda = AgendaItemDAO.get(cdAgendaItem, connect);
			
			if(regra.getLgNotificarAdvogado()==1) {
				int cdPessoa = ProcessoDAO.get(cdProcesso, connect).getCdAdvogado();
				String[] cdUsuario = {Integer.toString(UsuarioServices.getByPessoa(cdPessoa, connect).getCdUsuario())};
				
				usuarios = Util.concat(usuarios, cdUsuario, true);
			}
			if(regra.getLgNotificarGrupoTrabalho()==1) {
				int cdGrupoTrabalho = ProcessoDAO.get(cdProcesso, connect).getCdGrupoTrabalho();
				//pessoas do grupo
				ResultSetMap rsmPessoaGrupoTrabalho = GrupoPessoaServices.getAllByGrupo(cdGrupoTrabalho, connect);
				
				//usuarios das pessoas
				ArrayList<ItemComparator> crtUser = new ArrayList<ItemComparator>();
				crtUser.add(new ItemComparator("A.CD_PESSOA", Util.join(rsmPessoaGrupoTrabalho, "CD_PESSOA", true), ItemComparator.IN, Types.INTEGER));
				
				usuarios = Util
						.concat(usuarios, Util.join(UsuarioServices.find(crtUser, connect), "CD_USUARIO", true).split(","), true);
			}
			if(regra.getLgNotificarResponsavel()==1) {
				if(agenda.getCdPessoa()>0) {
					String[] cdResp = {Integer.toString(UsuarioServices.getByPessoa(agenda.getCdPessoa(), connect).getCdUsuario())};
					usuarios = Util.concat(usuarios, cdResp, true);
				}
				else if(agenda.getCdGrupoTrabalho()>0) {
					usuarios = Util
							.concat(usuarios, Util
									.join(GrupoServices
											.getUsuariosOfGrupo(agenda.getCdGrupoTrabalho(), connect), "CD_USUARIO", true).split(","), true);
				}
			}
			if(regra.getLgNotificarAutor()==1) {
				if(agenda.getCdPessoa()>0) {
					String[] cdUser = {Integer.toString(agenda.getCdUsuario())};
					usuarios = Util.concat(usuarios, cdUser, true);
				}
				else if(agenda.getCdGrupoTrabalho()>0) {
					usuarios = Util
							.concat(usuarios, Util
									.join(GrupoServices
											.getUsuariosOfGrupo(agenda.getCdGrupoTrabalho(), connect), "CD_USUARIO", true).split(","), true);
				}
			}
			
			
			// NOTIFICAR
			for (int i=0; i<usuarios.length; i++) {
				notificacao.setCdNotificacao(0);
				notificacao.setCdUsuario(Integer.parseInt(usuarios[i].trim()));
				Result result = NotificacaoServices.save(notificacao, null, connect);
				if(result.getCode()<=0) {
					System.out.println(result.getMessage());
				}
			}
			
			
			return new Result(1);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoServices.notificar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	//GET por Entidade ================================================================================

	public static ResultSetMap getRegrasProcesso(int tpAcao) {
		return getRegrasProcesso(tpAcao, null);
	}

	public static ResultSetMap getRegrasProcesso(int tpAcao, Connection connect) {
		return getRegras(TP_ENTIDADE_PROCESSO, tpAcao, connect);
	}
	
	public static ResultSetMap getRegrasAndamento(int tpAcao) {
		return getRegrasAndamento(tpAcao, null);
	}
	
	public static ResultSetMap getRegrasAndamento(int tpAcao, Connection connect) {
		return getRegras(TP_ENTIDADE_ANDAMENTO, tpAcao, connect);
	}
	
	public static ResultSetMap getRegrasArquivo(int tpAcao) {
		return getRegrasArquivo(tpAcao, null);
	}

	public static ResultSetMap getRegrasArquivo(int tpAcao, Connection connect) {
		return getRegras(TP_ENTIDADE_ARQUIVO, tpAcao, connect);
	}
	
	public static ResultSetMap getRegrasFatura(int tpAcao) {
		return getRegrasFatura(tpAcao, null);
	}

	public static ResultSetMap getRegrasFatura(int tpAcao, Connection connect) {
		return getRegras(TP_ENTIDADE_FATURA, tpAcao, connect);
	}
	
	public static ResultSetMap getRegrasAgenda(int tpAcao) {
		return getRegrasAgenda(tpAcao, null);
	}

	public static ResultSetMap getRegrasAgenda(int tpAcao, Connection connect) {
		return getRegras(TP_ENTIDADE_AGENDA, tpAcao, connect);
	}
	
	public static ResultSetMap getRegras(int tpEntidade, int tpAcao) {
		return getRegras(tpEntidade, tpAcao, null);
	}

	public static ResultSetMap getRegras(int tpEntidade, int tpAcao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.TP_ENTIDADE", Integer.toString(tpEntidade), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.TP_ACAO", Integer.toString(tpAcao), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.LG_ATIVO", Integer.toString(ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
						
			return find(criterios, connect);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraNotificacaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	// ================================================================================================

}
