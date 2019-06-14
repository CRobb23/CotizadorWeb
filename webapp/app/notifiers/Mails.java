package notifiers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;

import models.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import helpers.ERConstants;
import helpers.FieldAccesor;
import play.Logger;
import play.Play;
import play.classloading.enhancers.LocalvariablesNamesEnhancer.LocalVariablesNamesTracer;
import play.exceptions.MailException;
import play.exceptions.TemplateNotFoundException;
import play.exceptions.UnexpectedException;
import play.libs.Mail;
import play.mvc.Mailer;
import play.templates.Template;
import play.templates.TemplateLoader;

import static helpers.ERConstants.*;

public class Mails extends Mailer {

	private static final String FROM_MAIL = Play.configuration.getProperty("mail.smtp.from");
	private static String SERVER_URL =  Play.configuration.getProperty("serverUrl");;

	public static boolean welcome(ER_User user, String password){
		boolean result = false;
		try {
			setFrom(FROM_MAIL);
			setSubject("Bienvenido %s", user.getFullName());
			addRecipient(user.email);
            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(WELCOME_MAIL));
			String body = mail.body;
			body = body.replace("{cliente}",user.getFullName()).replace("{usuario}", user.email).replace("{password}",password);
			Future<Boolean> send = send(body);
			result = send.get();
		} catch (Exception e) {
			Logger.error(e, "Welcome mail user message %s", e.getMessage());
		}
		
		return result;
	}
	
	public static boolean welcomeUserFinal(ER_User user){
		boolean result = false;
		try{
			setFrom(FROM_MAIL);
			setSubject("Bienvenido %s", user.getFullName());
			addRecipient(user.email);
			String serverUrl = SERVER_URL;


			Future<Boolean> send = send(user,serverUrl);
			result = send.get();
		}catch(Exception e){
			Logger.error(e, "Welcome mail user message %s", e.getMessage());
		}
		return result;
	}
	
	public static boolean finishInspection(ER_Incident incident){
		boolean result = false;
		try{
			setFrom(FROM_MAIL);
			setSubject("Inspección del caso No. %s Finalizada", incident.number);
			addRecipient(incident.creator.email);
			String serverUrl = SERVER_URL;
            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(INSPECTION_FINISHED_MAIL));
            String body = mail.body;
            body = body.replace("{NoInspeccion}", incident.inspection.inspectionNumber);
            body = body.replace("{cliente}", incident.client.getFullName());
			Future<Boolean> send = send(incident,serverUrl,body);
			result = send.get();
		}catch(Exception e){
			Logger.error(e, "Finish Inspection: %s", e.getMessage());
		}
		return result;
	}
	
	public static boolean incidentDetail(ER_Incident incident){
		boolean result = false;
		try{
			setFrom(FROM_MAIL);
			setSubject("Caso No. %s", incident.number);
			addRecipient(incident.creator.email);
			String serverUrl = SERVER_URL;
			Future<Boolean> send = send(incident,serverUrl);
			result = send.get();
		}catch(Exception e){
			Logger.error(e, "Incident Details: %s", e.getMessage());
		}
		return result;
	}

	public static boolean sendPolicy(ER_Incident incident){
		boolean result = false;
		try{
			setFrom(FROM_MAIL);
			setSubject("Poliza de vehiculo");
			String link= "";
			Boolean isWebPath= true;
			List<String> recipients = new ArrayList<String>();
			recipients.add(incident.client.email);
			if (incident.client.additionalEmails != null && incident.client.additionalEmails.split(",").length>0) {
				for (String mail : incident.client.additionalEmails.split(",")) {
					if (!mail.trim().isEmpty()) {
						recipients.add(mail.trim());
					}
				}
			}

			if (incident.policyFileDownload != null && incident.policyFileDownload == true && (incident.policyWebPath == null || incident.policyWebPath == "" || incident.policyFileUpload == null || incident.policyFileUpload == false ))
			{	link = incident.policyFilePath;
				isWebPath = false;}
				else if(incident.policyFileUpload != null && incident.policyFileUpload == true && (incident.policyFilePath == "" || incident.policyFilePath == null || incident.policyFileDownload == null || incident.policyFileDownload == false ))
				link = incident.policyWebPath;
				else if(incident.policyFileUpload != null && incident.policyFileDownload != null && incident.policyFileUpload == true && incident.policyFileDownload == true)
				link = incident.policyWebPath;

			String serverUrl = SERVER_URL;
			for (String mail : recipients) {
				addRecipient(mail);
			}
			Future<Boolean> send = send(incident,link,serverUrl,isWebPath);
			result = send.get();
		}catch(Exception e){
			Logger.error(e, "Send policy: %s", e.getMessage());
		}
		return result;
	}
	
	public static boolean requestSupport(ER_Incident incident){
		boolean result = false;
		try{
			setFrom(FROM_MAIL);
			setSubject("Caso No. %s ha solicitado soporte", incident.number);
			List<String> recipients = new ArrayList<String>();
			ER_Channel channel = ER_Channel.findById(ERConstants.CHANNEL_PUBLIC);


			/*if(!FieldAccesor.isEmptyOrNull(incident, "distributor.administrators")){
				for(ER_User user: incident.distributor.administrators){
					recipients.add(user.email);
				}
			}*/
			//if(!FieldAccesor.isEmptyOrNull(incident, "distributor.channel.administrators")){
				for(ER_User adminMail: channel.administrators){
					recipients.add(adminMail.email);
				}
			//}
			String serverUrl = SERVER_URL;
			for (String mail : recipients) {
				addRecipient(mail);
			}

            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(SUPPORT_MAIL));
            String body = mail.body;
            body = body.replace("{caso}", incident.number.toString());
			Future<Boolean> send = send(incident,serverUrl,body);
			result = send.get();
		}catch(Exception e){
			Logger.error(e, "Incident Details: %s", e.getMessage());
		}
		return result;
	}
	
	public static boolean passwordReset(ER_User user, String password){
		boolean result = false;
		try{
			setFrom(FROM_MAIL);
			setSubject("Reinicio de contraseña");
			addRecipient(user.email);
            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(RESET_PASSWORD_MAIL));
            String body = mail.body;
            body = body.replace("{usuario}", user.email).replace("{password}",password);


			Future<Boolean> send = send(body);
			result = send.get();
		}catch(Exception e){
			Logger.error(e, "PasswordReset mail user message %s", e.getMessage());
		}
		return result;
	}
	
	public static boolean taskReminder(ER_Task_Reminder reminder){
		boolean result = false;
		try {
			setFrom(FROM_MAIL);
			setSubject(reminder.subject);
			for (String email : reminder.emailsList) {
				addRecipient(email);
			}
			
			Future<Boolean> send = send(reminder);
			result = send.get();
		} catch (Exception e) {
			Logger.error(e, "Reminder mail message %s", e.getMessage());
		}
		
		return result;
	}
	
	public static boolean guardReminderClient(ER_Guard_Reminder reminder){
		boolean result = false;
		try {
            if(reminder.guard.incident != null) {
                ER_Incident incident = ER_Incident.findById(reminder.guard.incident.id);
                if (incident.status.id == INCIDENT_STATUS_FINALIZED || incident.status.id == INCIDENT_STATUS_INCOMPLETE || incident.status.id == INCIDENT_STATUS_ANULLED)
                    return true;
            }
			setFrom(FROM_MAIL);
			setSubject("Resguardo a punto de vencer");
			addRecipient(reminder.guard.incident.client.email);
			String placa = reminder.guard.incident.vehicle.plate !=null ? reminder.guard.incident.vehicle.plate : "";
            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(GUARD_REMINDER_MAIL));
            String body = mail.body;
            body = body.replace("{linea}",reminder.guard.incident.vehicle.line.brand.name).replace("{marca}",reminder.guard.incident.vehicle.line.name).replace("{anio}",reminder.guard.incident.vehicle.erYear.year);
            body = body.replace("{placa}",placa).replace("{motor}",reminder.guard.incident.vehicle.engine).replace("{chasis}",reminder.guard.incident.vehicle.chassis);
            body = body.replace("{fechaExp}",new SimpleDateFormat("dd/MM/yyyy").format(reminder.guard.expirationDate)).replace("{caso}",reminder.guard.incident.number.toString());
                    if (reminder.guard.incident.distributor != null)
                        body = body.replace("{distribuidor}" ,reminder.guard.incident.distributor.name);
                    else
                        body = body.replace("{distribuidor}" ,"");

                    body = body.replace("{cliente}",reminder.guard.incident.client.getFullName());
            Future<Boolean> send = send(body);
			result = send.get();
		} catch (Exception e) {
			Logger.error(e, "Reminder mail message %s", e.getMessage());
		}
		
		return result;
	}
	
	public static boolean guardReminderAgent(ER_Guard_Reminder reminder){
		boolean result = false;
		try {
            if(reminder.guard.incident != null) {
                ER_Incident incident = ER_Incident.findById(reminder.guard.incident.id);
                if (incident.status.id == INCIDENT_STATUS_FINALIZED || incident.status.id == INCIDENT_STATUS_INCOMPLETE || incident.status.id == INCIDENT_STATUS_ANULLED)
                    return true;
            }
			setFrom(FROM_MAIL);
			setSubject("Resguardo a punto de vencer");
			addRecipient(reminder.user.email);
			String placa = reminder.guard.incident.vehicle.plate !=null ? reminder.guard.incident.vehicle.plate : "";
            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(GUARD_REMINDER_MAIL));
            String body = mail.body;
            body = body.replace("{linea}",reminder.guard.incident.vehicle.line.brand.name).replace("{marca}",reminder.guard.incident.vehicle.line.name).replace("{anio}",reminder.guard.incident.vehicle.erYear.year);
            body = body.replace("{placa}",placa).replace("{motor}",reminder.guard.incident.vehicle.engine).replace("{chasis}",reminder.guard.incident.vehicle.chassis);
            body = body.replace("{fechaExp}",new SimpleDateFormat("dd/MM/yyyy").format(reminder.guard.expirationDate)).replace("{caso}",reminder.guard.incident.number.toString());
            if(reminder.guard.incident.distributor != null)
                body = body.replace("{distribuidor}" ,reminder.guard.incident.distributor.name);
            else
                body = body.replace("{distribuidor}" ,"");
            body = body.replace("{cliente}",reminder.guard.incident.client.getFullName());
            body = body.replace("{vigencia}",new SimpleDateFormat("dd/MM/yyyy").format(reminder.guard.creationDate));
            Future<Boolean> send = send(body);
			result = send.get();
		} catch (Exception e) {
			Logger.error(e, "Reminder mail message %s", e.getMessage());
		}
		
		return result;
	}
	
	public static boolean guardExpiration(ER_Guard_Reminder reminder){
		boolean result = false;
		try {
		    if(reminder.guard.incident != null) {
                ER_Incident incident = ER_Incident.findById(reminder.guard.incident.id);
                if (incident.status.id == INCIDENT_STATUS_FINALIZED || incident.status.id == INCIDENT_STATUS_INCOMPLETE || incident.status.id == INCIDENT_STATUS_ANULLED)
                    return true;
            }
			setFrom(FROM_MAIL);
			setSubject("Resguardo ha vencido");
			addRecipient(reminder.guard.incident.client.email);
            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(GUARD_EXPIRATED_MAIL));
            String body = mail.body;
			String placa = reminder.guard.incident.vehicle.plate !=null ? reminder.guard.incident.vehicle.plate : "";
            body = body.replace("{linea}",reminder.guard.incident.vehicle.line.brand.name).replace("{marca}",reminder.guard.incident.vehicle.line.name).replace("{anio}",reminder.guard.incident.vehicle.erYear.year);
            body = body.replace("{placa}",placa).replace("{motor}",reminder.guard.incident.vehicle.engine).replace("{chasis}",reminder.guard.incident.vehicle.chassis);
            body = body.replace("{fechaExp}",new SimpleDateFormat("dd/MM/yyyy").format(reminder.guard.expirationDate)).replace("{caso}",reminder.guard.incident.number.toString());
            if(reminder.guard.incident.distributor != null)
                body = body.replace("{distribuidor}" ,reminder.guard.incident.distributor.name);
            else
                body = body.replace("{distribuidor}" ,"");
            body = body.replace("{cliente}",reminder.guard.incident.client.getFullName());
            body = body.replace("{vigencia}",new SimpleDateFormat("dd/MM/yyyy").format(reminder.guard.creationDate));
			Future<Boolean> send = send(body);
			result = send.get();
		} catch (Exception e) {
			Logger.error(e, "Reminder mail message %s", e.getMessage());
			result = false;
		}
		
		return result;
	}

	public static boolean welcomePolicyGenerated(ER_Incident incident, String poliza){
		boolean result = false;

		try {
				setFrom(FROM_MAIL);
				setSubject("Bienvenido a Seguros El Roble");
				if (incident.client.email !=null) {
					addRecipient(incident.client.email);
				}
                ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(POLICY_GENERATED_MAIL));
                String body = mail.body;
                body = body.replace("{poliza}" , incident.policy).replace("{cliente}", incident.client.getFullName());
				Future<Boolean> send = send(body);
				result = send.get();

		} catch (Exception e) {
			Logger.error(e, "welcome Policy Generated mail user message %s", e.getMessage());
			result = false;
		}

		return result;
	}
	
	public static boolean addressInspection(ER_Incident incident, ER_Inspection inspection){
		boolean result = false;
		
		try {
			
			if (incident.distributor!=null) {
				setFrom(FROM_MAIL);
				setSubject("Nueva inspección en domicilio");
				if (incident.distributor.inspectionEmail!=null) {
					addRecipient(incident.distributor.inspectionEmail);
				}
				
				Future<Boolean> send = send(incident, inspection);
				result = send.get();
			}
		} catch (Exception e) {
			Logger.error(e, "Address inspection mail user message %s", e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public static boolean centersList(ER_Incident incident){
		boolean result = false;
		try {
			ER_Attachment centersList = ER_Attachment.find("code = ?", ERConstants.FILE_CENTERS_LIST).first();
			if (centersList!=null && centersList.attachment!=null) {
			
				setFrom(FROM_MAIL);
				setSubject("Centros de inspección autorizados");
				if (incident.client.email!=null) {
					addRecipient(incident.client.email);
				}
				
				EmailAttachment attachment = new EmailAttachment();  
				attachment.setName(centersList.fileName);
				attachment.setPath(centersList.attachment.getFile().getPath());
				addAttachment(attachment);
                ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(INSPECTION_CENTERS_MAIL));
                String body = mail.body;
                body = body.replace("{cliente}" , incident.client.getFullName());
				Future<Boolean> send = send(body);
				result = send.get();
			}
		} catch (Exception e) {
			Logger.error(e, "CenterstList mail user message %s", e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public static boolean generatedGuard(ER_Guard guard){
		boolean result = false;
		List<String> recipients = new ArrayList<String>();

		try {
			setFrom(FROM_MAIL);
			ER_Incident incident = guard.incident;
			String placa = incident.vehicle.plate !=null ? incident.vehicle.plate : "";
			setSubject("Emisión de resguardo cliente " + incident.client.getFullName() + " Placas: " + placa);

			recipients.add(incident.client.email);
			if (incident.client.additionalEmails != null && incident.client.additionalEmails.split(",").length>0) {
				for (String mail : incident.client.additionalEmails.split(",")) {
					if (!mail.trim().isEmpty()) {
						recipients.add(mail.trim());
					}
				}
			}
            ER_General_Configuration currentConfiguration = ER_General_Configuration.find("").first();
            if (currentConfiguration.guardMails != null && currentConfiguration.guardMails.split(",").length>0) {
                for (String mail : currentConfiguration.guardMails.split(",")) {
                    if (!mail.trim().isEmpty()) {
                        recipients.add(mail.trim());
                    }
                }
            }

			ER_General_Configuration configuration = ER_General_Configuration.find("").first();
			for (String mail : recipients) {
				addRecipient(mail);
			}
			//set body message
            ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(GUARD_MAIL));
            String body = mail.body;
            body = body.replace("{linea}",guard.incident.vehicle.line.brand.name).replace("{marca}",guard.incident.vehicle.line.name).replace("{anio}",guard.incident.vehicle.erYear.year);
            body = body.replace("{placa}",placa).replace("{motor}",guard.incident.vehicle.engine).replace("{chasis}",guard.incident.vehicle.chassis);
            body = body.replace("{fechaExp}",new SimpleDateFormat("dd/MM/yyyy").format(guard.expirationDate)).replace("{caso}",guard.incident.number.toString());
            if(guard.incident.distributor != null)
                body = body.replace("{distribuidor}" ,guard.incident.distributor.name);
            else
                body = body.replace("{distribuidor}" ,"");
            body = body.replace("{cliente}",guard.incident.client.getFullName());
            body = body.replace("{vigencia}",new SimpleDateFormat("dd/MM/yyyy").format(guard.creationDate));
			Future<Boolean> send = send(guard, configuration, body);
			result = send.get();
		} catch (Exception e) {
			Logger.error(e, "GeneratedGuard mail user message %s", e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public static boolean guardReport(String pathFile, String emailRecipient){
		boolean result = false;
		
		try {
			
			setFrom(FROM_MAIL);
			setSubject("Listado de Resguardos");
			
			addRecipient(emailRecipient);
			
			EmailAttachment attachment = new EmailAttachment();  
			attachment.setName("Listado Resguardos.xls");
			attachment.setPath(pathFile);
			addAttachment(attachment);
			
			Future<Boolean> send = send();
			result = send.get();
			
		} catch (Exception e) {
			Logger.error(e, "GuardsList mail user message %s", e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	public static boolean quotations(ER_Incident incident, List<ByteArrayOutputStream> streamArray, boolean copyCreator){
		boolean result = false;
		
		try {
			
			setFrom(FROM_MAIL);
			setSubject("Cotización Seguro de Vehículos");
			
			int recipientsCount = 0;
			if (incident.client.email!=null && !incident.client.email.isEmpty()) {
				addRecipient(incident.client.email);
				recipientsCount++;
			}
			if (incident.client.additionalEmails != null && !incident.client.additionalEmails.isEmpty()) {
			    String[] emailList = incident.client.additionalEmails.split(",");
			    for (String email : emailList) {
			        if (!email.trim().isEmpty()) {
                        addRecipient(email.trim());
                        recipientsCount++;
                    }
                }
            }
			
			if (copyCreator) {
				addRecipient(incident.creator.email.trim());
				recipientsCount++;
			}
			
			if (recipientsCount >0) {
				int i=1;
				for (ByteArrayOutputStream stream : streamArray) {
					byte[] bytes = stream.toByteArray();
					DataSource source = new ByteArrayDataSource(bytes, "application/pdf");
					// add the attachment
					addAttachment(source, "COTIZACION"+i+".pdf", "COTIZACION");
					i++;
				}
                ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(QUOTATION_MAIL));
                String body = mail.body;
				Future<Boolean> send = sendMail("Mails/quotations",incident, infos.get(),body);
				result = send.get();
			}
		} catch (Exception e) {
			Logger.error(e, "Quotations mail user message %s", e.getMessage());
		} finally {
			for (ByteArrayOutputStream stream : streamArray) {
				IOUtils.closeQuietly(stream);
			}
		}
		
		return result;
	}
	
	/*
	 * ************************************************************************************************************************
	 * Agrega un adjunto de datos
	 * ************************************************************************************************************************
	 */
	
	private static void addAttachment(DataSource source, String name, String description) {
        HashMap<String, Object> map = (HashMap<String, Object>) infos.get();
        if (map == null) {
            throw new UnexpectedException("Mailer not instrumented ?");
        }
        List<Map<String,Object>> attachmentsList = (List<Map<String,Object>>) map.get("dataAttachments");
        if (attachmentsList == null) {
            attachmentsList = new ArrayList<Map<String,Object>>();
            map.put("dataAttachments", attachmentsList);
        }
        Map<String, Object> attachment = new HashMap<String, Object>();
        attachment.put("source", source);
        attachment.put("name", name);
        attachment.put("description", description);
        attachmentsList.add(attachment);
        infos.set(map);
    }
	
    /*
	 * ************************************************************************************************************************
	 * Reemplaza la función send para permitir adjuntos de datos.
	 * ************************************************************************************************************************
	 */
    
	public static Future<Boolean> sendMail(Object... args) {
        try {
            final HashMap<String, Object> map = (HashMap<String, Object>)args[2];
            infos.set(map);
            if (map == null) {
                throw new UnexpectedException("Mailer not instrumented ?");
            }

            // Body character set
            final String charset = (String) infos.get().get("charset");

            // Headers
            final Map<String, String> headers = (Map<String, String>) infos.get().get("headers");

            // Subject
            final String subject = (String) infos.get().get("subject");

            String templateName = (String) infos.get().get("method");
            if (templateName.startsWith("notifiers.")) {
                templateName = templateName.substring("notifiers.".length());
            }
            if (templateName.startsWith("controllers.")) {
                templateName = templateName.substring("controllers.".length());
            }
            templateName = templateName.substring(0, templateName.indexOf("("));
            templateName = templateName.replace(".", "/");

            // overrides Template name
            if (args.length > 0 && args[0] instanceof String && LocalVariablesNamesTracer.getAllLocalVariableNames(args[0]).isEmpty()) {
                templateName = args[0].toString();
            }

            final Map<String, Object> templateHtmlBinding = new HashMap<String, Object>();
            final Map<String, Object> templateTextBinding = new HashMap<String, Object>();
            for (Object o : args) {
                List<String> names = LocalVariablesNamesTracer.getAllLocalVariableNames(o);
                for (String name : names) {
                    templateHtmlBinding.put(name, o);
                    templateTextBinding.put(name, o);
                }
            }

            // The rule is as follow: If we ask for text/plain, we don't care about the HTML
            // If we ask for HTML and there is a text/plain we add it as an alternative.
            // If contentType is not specified look at the template available:
            // - .txt only -> text/plain
            // else
            // -           -> text/html
            String contentType = (String) infos.get().get("contentType");
            String bodyHtml = null;
            String bodyText = "";
            try {
                Template templateHtml = TemplateLoader.load(templateName + ".html");
                bodyHtml = templateHtml.render(templateHtmlBinding);
            } catch (TemplateNotFoundException e) {
                if (contentType != null && !contentType.startsWith("text/plain")) {
                    throw e;
                }
            }

            try {
                Template templateText = TemplateLoader.load(templateName + ".txt");
                bodyText = templateText.render(templateTextBinding);
            } catch (TemplateNotFoundException e) {
                if (bodyHtml == null && (contentType == null || contentType.startsWith("text/plain"))) {
                    throw e;
                }
            }

            // Content type

            if (contentType == null) {
                if (bodyHtml != null) {
                    contentType = "text/html";
                } else {
                    contentType = "text/plain";
                }
            }

            // Recipients
            final List<Object> recipientList = (List<Object>) infos.get().get("recipients");
            // From
            final Object from = infos.get().get("from");
            final Object replyTo = infos.get().get("replyTo");

            Email email = null;
            if (infos.get().get("attachments") == null && infos.get().get("dataAttachments") == null) {
                if (StringUtils.isEmpty(bodyHtml)) {
                    email = new SimpleEmail();
                    email.setMsg(bodyText);
                } else {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    htmlEmail.setHtmlMsg(bodyHtml);
                    if (!StringUtils.isEmpty(bodyText)) {
                        htmlEmail.setTextMsg(bodyText);
                    }
                    email = htmlEmail;
                }

            } else {
                if (StringUtils.isEmpty(bodyHtml)) {
                    email = new MultiPartEmail();
                    email.setMsg(bodyText);
                } else {
                    HtmlEmail htmlEmail = new HtmlEmail();
                    htmlEmail.setHtmlMsg(bodyHtml);
                    if (!StringUtils.isEmpty(bodyText)) {
                        htmlEmail.setTextMsg(bodyText);
                    }
                    email = htmlEmail;
                }
                MultiPartEmail multiPartEmail = (MultiPartEmail) email;
                
                List<EmailAttachment> objectList = (List<EmailAttachment>) infos.get().get("attachments");
                if (objectList!=null) {
	                for (EmailAttachment object : objectList) {
	                    multiPartEmail.attach(object);
	                }
                }
                
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) infos.get().get("dataAttachments");
                if (dataList!=null) {
	                for (Map<String, Object> object : dataList) {
	                	multiPartEmail.attach((DataSource)object.get("source"), (String)object.get("name"), (String)object.get("description"));
	                }
                }
            }
            email.setCharset("utf-8");

            if (from != null) {
                try {
                    InternetAddress iAddress = new InternetAddress(from.toString());
                    email.setFrom(iAddress.getAddress(), iAddress.getPersonal());
                } catch (Exception e) {
                    email.setFrom(from.toString());
                }

            }

            if (replyTo != null) {
                try {
                    InternetAddress iAddress = new InternetAddress(replyTo.toString());
                    email.addReplyTo(iAddress.getAddress(), iAddress.getPersonal());
                } catch (Exception e) {
                    email.addReplyTo(replyTo.toString());
                }

            }

            if (recipientList != null) {
                for (Object recipient : recipientList) {
                    try {
                        InternetAddress iAddress = new InternetAddress(recipient.toString());
                        email.addTo(iAddress.getAddress(), iAddress.getPersonal());
                    } catch (Exception e) {
                        email.addTo(recipient.toString());
                    }
                }
            } else {
                throw new MailException("You must specify at least one recipient.");
            }


            List<Object> ccsList = (List<Object>) infos.get().get("ccs");
            if (ccsList != null) {
                for (Object cc : ccsList) {
                    email.addCc(cc.toString());
                }
            }

            List<Object> bccsList = (List<Object>) infos.get().get("bccs");
            if (bccsList != null) {

                for (Object bcc : bccsList) {
                    try {
                        InternetAddress iAddress = new InternetAddress(bcc.toString());
                        email.addBcc(iAddress.getAddress(), iAddress.getPersonal());
                    } catch (Exception e) {
                        email.addBcc(bcc.toString());
                    }
                }
            }
            if (!StringUtils.isEmpty(charset)) {
                email.setCharset(charset);
            }

            email.setSubject(subject);
            email.updateContentType(contentType);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    email.addHeader(key, headers.get(key));
                }
            }

            return Mail.send(email);
        } catch (EmailException ex) {
            throw new MailException("Cannot send email", ex);
        }
    }
}
