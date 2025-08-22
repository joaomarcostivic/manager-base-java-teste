package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class UsuarioServices {

	public static int insert(Usuario objeto, String nmUsuario) {
		return insert(objeto, nmUsuario, "", false);
	}

	public static int insert(Usuario objeto, String nmUsuario, String nmEmail) {
		return insert(objeto, nmUsuario, nmEmail, false);
	}

	public static int insert(Usuario objeto, String nmUsuario, String nmEmail, boolean verifyCount) {
		return insert(objeto, new PessoaFisica(0, /*cdPessoa*/
				0, /*cdPessoaSuperior*/
				0, /*cdPais*/
				nmUsuario, /*nmPessoa*/
				"", /*nrTelefone1*/
				"", /*nrTelefone2*/
				"", /*nrCelular*/
				"", /*nrFax*/
				nmEmail, /*nmEmail*/
				new GregorianCalendar(), /*dtCadastro*/
				PessoaServices.TP_FISICA, /*gnPessoa*/
				null, /*imgFoto*/
				1, /*stCadastro*/
				"", /*nmUrl*/
				"", /*nmApelido*/
				"", /*txtObservacao*/
				0, /*lgNotificacao*/
				"", /*idPessoa*/
				0, /*cdClassificacao*/
				0, /*cdFormaDivulgacao*/
				null, /*dtChegadaPais*/
				0, /*cdNaturalidade*/
				0, /*cdEscolaridade*/
				null, /*dtNascimento*/
				"", /*nrCpf*/
				"", /*sgOrgaoRg*/
				"", /*nmMae*/
				"", /*nmPai*/
				0, /*tpSexo*/
				0, /*stEstadoCivil*/
				"", /*nrRg*/
				"", /*nrCnh*/
				null, /*dtValidadeCnh*/
				null, /*dtPrimeiraHabilitacao*/
				0, /*tpCategoriaHabilitacao*/
				0, /*tpRaca*/
				0, /*lgDeficienteFisico*/
				"", /*nmFormaTratamento*/
				0, /*cdEstadoRg*/
				null /*dtEmissaoRg*/,
				null /*blbFingerprint*/), verifyCount, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT A.*, C.nr_minutos_update, B.nm_pessoa AS nm_usuario " +
						   "FROM SEG_USUARIO A "  +
						   "LEFT OUTER JOIN GRL_PESSOA B ON (A.CD_PESSOA=B.CD_PESSOA) " +
						   "LEFT OUTER JOIN agd_usuario C ON (A.cd_usuario = C.cd_usuario)", criterios, Conexao.conectar());
	}

	public static int insert(Usuario objeto, PessoaFisica pessoa) {
		return insert(objeto, pessoa, false, null);
	}

	public static int insert(Usuario objeto, PessoaFisica pessoa, boolean verifyCount, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			int cdPessoa = 0;

			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (verifyCount && objeto.getStUsuario()==com.tivic.manager.seg.UsuarioServices.ST_ATIVO) {
				int qtUsuariosMax = -1;
				int tpModalidade = ParametroServices.getValorOfParametroAsInteger("TP_MODALIDADE_AGENDA", Configuration.MOD_PROFISSIONAL,
						0 /*cdEmpresa*/, connect);
				qtUsuariosMax = tpModalidade==Configuration.MOD_GRATUITO ? 1 :
					ParametroServices.getValorOfParametroAsInteger("QT_USUARIOS_AGENDA", 0, 0/*cdEmpresa*/, connect);
				if (qtUsuariosMax!=-1) {
					PreparedStatement pstmt = connect.prepareStatement("SELECT COUNT(*) " +
							"FROM seg_usuario A " +
							"WHERE A.st_usuario = ?");
					pstmt.setInt(1, com.tivic.manager.seg.UsuarioServices.ST_ATIVO);
					ResultSet rs = pstmt.executeQuery();
					int qtUsuariosAtivos = !rs.next() ? 0 : rs.getInt(1);
					if (qtUsuariosAtivos >= qtUsuariosMax) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return com.tivic.manager.seg.UsuarioServices.ERROR_MAX_USUARIOS_ATIVOS;
					}
				}
			}

			if ((cdPessoa = PessoaFisicaDAO.insert(pessoa, connect)) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			objeto.setCdPessoa(cdPessoa);

			int code = UsuarioDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.desconectar(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return code;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Usuario objeto, String nmUsuario) {
		return update(objeto, nmUsuario, "", false, false, null);
	}

	public static int update(Usuario objeto, String nmUsuario, String nmEmail, boolean notUpdateAudio) {
		return update(objeto, nmUsuario, nmEmail, notUpdateAudio, false, null);
	}

	public static int update(Usuario objeto, String nmUsuario, String nmEmail, boolean notUpdateAudio, boolean verifyCount) {
		return update(objeto, nmUsuario, nmEmail, notUpdateAudio, verifyCount, null);
	}

	public static int update(Usuario objeto, String nmUsuario, String nmEmail, boolean notUpdateAudio, boolean verifyCount,
			Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			PessoaFisica pessoa = objeto.getCdPessoa()==0 ? new PessoaFisica(0, /*cdPessoa*/
					0, /*cdPessoaSuperior*/
					0, /*cdPais*/
					nmUsuario, /*nmPessoa*/
					"", /*nrTelefone1*/
					"", /*nrTelefone2*/
					"", /*nrCelular*/
					"", /*nrFax*/
					nmEmail, /*nmEmail*/
					new GregorianCalendar(), /*dtCadastro*/
					PessoaServices.TP_FISICA, /*gnPessoa*/
					null, /*imgFoto*/
					1, /*stCadastro*/
					"", /*nmUrl*/
					"", /*nmApelido*/
					"", /*txtObservacao*/
					0, /*lgNotificacao*/
					"", /*idPessoa*/
					0, /*cdClassificacao*/
					0, /*cdFormaDivulgacao*/
					null, /*dtChegadaPais*/
					0, /*cdNaturalidade*/
					0, /*cdEscolaridade*/
					null, /*dtNascimento*/
					"", /*nrCpf*/
					"", /*sgOrgaoRg*/
					"", /*nmMae*/
					"", /*nmPai*/
					0, /*tpSexo*/
					0, /*stEstadoCivil*/
					"", /*nrRg*/
					"", /*nrCnh*/
					null, /*dtValidadeCnh*/
					null, /*dtPrimeiraHabilitacao*/
					0, /*tpCategoriaHabilitacao*/
					0, /*tpRaca*/
					0, /*lgDeficienteFisico*/
					"", /*nmFormaTratamento*/
					0, /*cdEstadoRg*/
					null /*dtEmissaoRg*/,
					null /*blbFingerprint*/) : PessoaFisicaDAO.get(objeto.getCdPessoa(), connection);
			pessoa.setNmPessoa(nmUsuario);
			pessoa.setNmEmail(nmEmail);
			int code = -1;
			if ((code = update(objeto, pessoa, notUpdateAudio, verifyCount, connection)) <= 0) {
				if (isConnectionNull) {
					Conexao.rollback(connection);
					return code;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(Usuario objeto, PessoaFisica pessoa) {
		return update(objeto, pessoa, false, false, null);
	}

	public static int update(Usuario objeto, PessoaFisica pessoa, boolean notUpdateAudio) {
		return update(objeto, pessoa, notUpdateAudio, false, null);
	}

	public static int update(Usuario objeto, PessoaFisica pessoa, boolean notUpdateAudio, boolean verifyCount, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			if (pessoa.getCdPessoa()==0) {
				int cdPessoa = 0;
				if ((cdPessoa = PessoaFisicaDAO.insert(pessoa, connect)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
				else {
					pessoa.setCdPessoa(cdPessoa);
					objeto.setCdPessoa(cdPessoa);
				}
			}
			else if (PessoaFisicaDAO.update(pessoa, connect) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			int code = -1;
			if ((code = update(objeto, notUpdateAudio, verifyCount, connect))<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return code;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Usuario objeto){
		return update(objeto, false, false, (Connection)null);
	}

	public static int update(Usuario objeto, boolean notUpdateAudio){
		return update(objeto, notUpdateAudio, false, (Connection)null);
	}

	public static int update(Usuario objeto, boolean notUpdateAudio, boolean verifyCount, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			Usuario usuarioTemp = UsuarioDAO.get(objeto.getCdUsuario(), connect);
			if (usuarioTemp==null) {
				PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_usuario (cd_usuario,"+
                		"nr_minutos_update) VALUES (?, ?)");
				pstmt.setInt(1,objeto.getCdUsuario());
				pstmt.setInt(2,objeto.getNrMinutosUpdate());
				pstmt.execute();
			}
			else {
				if (notUpdateAudio)
					objeto.setBlbAudio(usuarioTemp.getBlbAudio());
			}

			if (verifyCount && objeto.getStUsuario()==com.tivic.manager.seg.UsuarioServices.ST_ATIVO) {
				int qtUsuariosMax = -1;
				int tpModalidade = ParametroServices.getValorOfParametroAsInteger("TP_MODALIDADE_AGENDA", Configuration.MOD_PROFISSIONAL,
						0 /*cdEmpresa*/, connect);
				qtUsuariosMax = tpModalidade==Configuration.MOD_GRATUITO ? 1 :
					ParametroServices.getValorOfParametroAsInteger("QT_USUARIOS_AGENDA", 0, 0/*cdEmpresa*/, connect);
				if (qtUsuariosMax!=-1) {
					PreparedStatement pstmt = connect.prepareStatement("SELECT COUNT(*) " +
							"FROM seg_usuario A " +
							"WHERE A.st_usuario = ? " +
							"  AND A.cd_usuario <> ?");
					pstmt.setInt(1, com.tivic.manager.seg.UsuarioServices.ST_ATIVO);
					pstmt.setInt(2, objeto.getCdUsuario());
					ResultSet rs = pstmt.executeQuery();
					int qtUsuariosAtivos = !rs.next() ? 0 : rs.getInt(1);
					if (qtUsuariosAtivos >= qtUsuariosMax) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return com.tivic.manager.seg.UsuarioServices.ERROR_MAX_USUARIOS_ATIVOS;
					}
				}
			}

			if (UsuarioDAO.update(objeto, connect) <= 0) {
				if (isConnectionNull) {
					Conexao.rollback(connect);
					return -1;
				}
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(Exception e){
			e.printStackTrace(java.lang.System.out);
			java.lang.System.err.println("Erro! UsuarioServices.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(-1, null);
	}

	public static ResultSetMap getAll(int stUsuario) {
		return getAll(stUsuario, null);
	}

	public static ResultSetMap getAll(int stUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nr_minutos_update, " +
					"not (B.blb_audio is null) AS lg_audio, C.nm_pessoa AS nm_usuario, " +
					"C.nm_email " +
					"FROM seg_usuario A " +
					"LEFT OUTER JOIN agd_usuario B ON (A.cd_usuario = B.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
					"WHERE 1=1 " +
					(stUsuario==-1 ? "" : "  AND st_usuario = " + stUsuario + " "));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAsResultSet(int cdUsuario) {
		return getAsResultSet(cdUsuario, null);
	}

	public static ResultSetMap getAsResultSet(int cdUsuario, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.nr_minutos_update, " +
					"not (B.blb_audio is null) AS lg_audio, C.nm_pessoa AS nm_usuario " +
					"FROM seg_usuario A " +
					"LEFT OUTER JOIN agd_usuario B ON (A.cd_usuario = B.cd_usuario) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_pessoa = C.cd_pessoa) " +
					"WHERE A.cd_usuario = " + cdUsuario);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int setBlbAudio(byte[] blbAudio, int cdUsuario) {
		return setBlbAudio(blbAudio, cdUsuario, null);
	}

	public static int setBlbAudio(byte[] blbAudio, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_usuario SET blb_audio=? WHERE cd_usuario=?");
			if(blbAudio==null)
				pstmt.setNull(1, Types.BINARY);
			else
				pstmt.setBytes(1, blbAudio);
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.setBlbAudio: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static byte[] getBlbAudio(int cdUsuario) {
		return getBlbAudio(cdUsuario, null);
	}

	public static byte[] getBlbAudio(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT blb_audio " +
					"FROM agd_usuario " +
					"WHERE cd_usuario=?");
			pstmt.setInt(1, cdUsuario);
			ResultSet rs = pstmt.executeQuery();
			return rs.next() ? rs.getBytes(1): null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioServices.getBlbAudio: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
