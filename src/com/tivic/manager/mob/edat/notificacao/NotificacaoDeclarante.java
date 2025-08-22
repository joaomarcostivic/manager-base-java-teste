package com.tivic.manager.mob.edat.notificacao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.InconsistenciaServices;
import com.tivic.manager.util.mail.Address;
import com.tivic.manager.util.mail.Authenticator;
import com.tivic.manager.util.mail.Mailer;
import com.tivic.manager.util.mail.enums.SecurityType;
import com.tivic.manager.util.mail.providers.MailerProvider;

public class NotificacaoDeclarante {
	
	private Mailer mailer;
	private Boat boat;
	
	public NotificacaoDeclarante(Boat boat) throws Exception {
		this.boat = boat;
		configurar();
	}
	
	private void configurar() throws Exception {
		String nmHost = ManagerConf.getInstance().get("EMAIL_PROVEDOR_HOST");
		int    nrPort = ManagerConf.getInstance().getAsInteger("EMAIL_PROVEDOR_PORT");
		String nmEmail = ManagerConf.getInstance().get("EMAIL_REMETENTE_EDAT");
		String nmPassword = ManagerConf.getInstance().get("SENHA_REMETENTE_EDAT");
		String nmSenderName = ManagerConf.getInstance().get("HEADER_EMAIL_DAT");

		if (nmHost == null || nrPort == 0 || nmEmail == null || nmPassword == null) {
			throw new Exception("Os parametros de usuário para envio de e-mail não estão configurados.");
		}
		
		autenticar(nmHost, nrPort, nmEmail, nmPassword, nmSenderName);
	}
	
	private void autenticar(String nmHost, int nrPort, String nmEmail, String nmPassword, String nmSenderName) {
		MailerProvider provider = new MailerProvider(nmHost, nrPort, SecurityType.TLS);
		Authenticator auth = new Authenticator(nmEmail, nmPassword, nmSenderName);
		mailer = new Mailer(provider, auth);
	}

	public Mailer enviarNotificacaoRegistroDeclaracao() {
		try {
			if (boat == null) {
				throw new Exception("E necessário informar um BOAT a ser notificado.");
			}

			HashMap<String, String> vars = new HashMap<String, String>();
			vars.put("nome", boat.getDeclarante().getNmDeclarante());
			vars.put("protocolo", boat.getNrProtocolo());
			vars.put("data_registro",
					new SimpleDateFormat("dd/MM/yyyy HH:mm")
							.format(boat.getDtComunicacao() != null ? boat.getDtComunicacao().getTime()
									: (new GregorianCalendar()).getTime()));
			vars.put("url_sistema", ManagerConf.getInstance().get("MOB_URL_VALIDAR_BOAT"));
			vars.put("email_header", ManagerConf.getInstance().get("HEADER_EMAIL_DAT"));
			vars.put("email_footer", new String(ManagerConf.getInstance().get("FOOTER_EMAIL_DAT").getBytes(), "UTF-8"));

			return mailer
					.addAddress(new Address(boat.getDeclarante().getNmDeclarante(), boat.getDeclarante().getNmEmail()))
					.setSubject("Declaração de Acidente de Trânsito cadastrada com sucesso!")
					.setContent(mailer.getTemplate("cadastro", vars));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public Mailer enviarNotificacaoCorrecaoDeclaracao(ArrayList<Inconsistencia> inconsistencias) {
		try {
			if (boat == null && inconsistencias.size() > 0) {
				throw new Exception("E necessário informar um BOAT a ser notificado.");
			}

			String lista = "<ul>";
			for (Inconsistencia inconsistencia : inconsistencias) {
				if (inconsistencia.getTpInconsistencia() == InconsistenciaServices.TP_CORRETIVO) {
					lista += "<li>" + inconsistencia.getNmInconsistencia() + "</li>";
				}
			}
			lista += "</ul>";

			HashMap<String, String> vars = new HashMap<String, String>();
			vars.put("nome", boat.getDeclarante().getNmDeclarante());
			vars.put("protocolo", boat.getNrProtocolo());
			vars.put("data_registro",
					new SimpleDateFormat("dd/MM/yyyy HH:mm").format(boat.getDtComunicacao().getTime()));
			vars.put("correcoes", lista);
			vars.put("url_sistema", ManagerConf.getInstance().get("MOB_URL_VALIDAR_BOAT"));
			vars.put("email_header", ManagerConf.getInstance().get("HEADER_EMAIL_DAT"));
			vars.put("email_footer", new String(ManagerConf.getInstance().get("FOOTER_EMAIL_DAT").getBytes(), "UTF-8"));

			return mailer
				.addAddress(new Address(boat.getDeclarante().getNmDeclarante(), boat.getDeclarante().getNmEmail()))
				.setSubject("Sua declaração necessita de correções")
				.setContent(mailer.getTemplate("correcoes", vars)).send();

		} catch (Exception e) {
			throw new BadRequestException("Erro ao enviar email de correção para: " + boat.getDeclarante().getNmEmail());
		}
	}

	public Mailer enviarNotificacaoIndeferimentoDeclaracao(ArrayList<Inconsistencia> inconsistencias) {
		try {
			if (boat == null && inconsistencias.size() > 0) {
				throw new Exception("E necessário informar um BOAT a ser notificado.");
			}

			String lista = "<ul>";
			for (Inconsistencia inconsistencia : inconsistencias) {
				if (inconsistencia.getTpInconsistencia() == InconsistenciaServices.TP_INDEFERIMENTO) {
					lista += "<li>" + inconsistencia.getNmInconsistencia() + "</li>";
				}
			}
			lista += "</ul>";

			HashMap<String, String> vars = new HashMap<String, String>();
			vars.put("nome", boat.getDeclarante().getNmDeclarante());
			vars.put("protocolo", boat.getNrProtocolo());
			vars.put("data_registro",
					new SimpleDateFormat("dd/MM/yyyy HH:mm").format(boat.getDtComunicacao().getTime()));
			vars.put("indeferimentos", lista);
			vars.put("url_sistema", ManagerConf.getInstance().get("MOB_URL_VALIDAR_BOAT"));
			vars.put("email_header", ManagerConf.getInstance().get("HEADER_EMAIL_DAT"));
			vars.put("email_footer", new String(ManagerConf.getInstance().get("FOOTER_EMAIL_DAT").getBytes(), "UTF-8"));

			return mailer
					.addAddress(new Address(boat.getDeclarante().getNmDeclarante(), boat.getDeclarante().getNmEmail()))
					.setSubject("Sua Declaração de Acidente de Trânsito foi indeferida!")
					.setContent(mailer.getTemplate("indeferimento", vars));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public Mailer enviarNotificacaoDeferimentoDeclaracao() {
		try {
			if (boat == null) {
				throw new Exception("E necessário informar um BOAT a ser notificado.");
			}

			HashMap<String, String> vars = new HashMap<String, String>();
			vars.put("nome", boat.getDeclarante().getNmDeclarante());
			vars.put("protocolo", boat.getNrProtocolo());
			vars.put("data_registro",
					new SimpleDateFormat("dd/MM/yyyy HH:mm").format(boat.getDtComunicacao().getTime()));
			vars.put("url_sistema", ManagerConf.getInstance().get("MOB_URL_VALIDAR_BOAT"));
			vars.put("email_header", ManagerConf.getInstance().get("HEADER_EMAIL_DAT"));
			vars.put("email_footer", new String(ManagerConf.getInstance().get("FOOTER_EMAIL_DAT").getBytes(), "UTF-8"));

			return mailer
					.addAddress(new Address(boat.getDeclarante().getNmDeclarante(), boat.getDeclarante().getNmEmail()))
					.setSubject("Sua Declaração de Acidente de Trânsito foi deferida!")
					.setContent(mailer.getTemplate("deferimento", vars));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
