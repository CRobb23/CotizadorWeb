package jobs;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import helpers.ERConstants;
import models.ER_Aditional_Multimedia;
import models.ER_Client;
import models.ER_Incident;
import models.ER_Multimedia;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.WS;
import play.libs.WS.FileParam;
import play.libs.WS.WSRequest;

@Every("100mn")
public class MultimediaJob extends Job {

	private final String PARENT_FOLDER_ID = Play.configuration.getProperty("drive.parentFolderId");
	private final String WS_GDRIVE = Play.configuration.getProperty("drive.proxy.url");
	
	private List<ER_Multimedia> multimediaList;
	
	public MultimediaJob(){}
	
	public MultimediaJob(Long id){
		multimediaList = new ArrayList<ER_Multimedia>();
		multimediaList.add((ER_Multimedia)ER_Multimedia.findById(id));
	}
	
	public void doJob(){
	    try {
            multimediaList = ER_Multimedia.find("uploaded_files_gd = false and can_upload_files = true").fetch(50);
            Logger.info("Inicia multimedia Job, lista de multimedia a subir: " + multimediaList.size());

            for (ER_Multimedia multimedia:multimediaList) {
                    uploadMultimediaFiles(multimedia);
            }
        }
        catch (Exception e){
	        Logger.error(e.getMessage());
        }
    }
	
	private void uploadMultimediaFiles(ER_Multimedia multimedia){
	    try {
            multimedia = ER_Multimedia.findById(multimedia.id);
            ER_Client client = ER_Client.find("multimedia.id = ?", multimedia.id).first();
            if (client != null) {
                ER_Incident incident = ER_Incident.find("client.id = ?", client.id).first();
                if (incident != null) {
                    if (incident.status.code == ERConstants.INCIDENT_STATUS_FINALIZED) {
                        Logger.info("Sube multimedia de caso: " + incident.number);
                        Boolean uploadedFilesGD = true;

                        WSRequest requestDirectory = WS.url(WS_GDRIVE + "/findFolderByName");
                        requestDirectory.setParameter("folderName", incident.number);
                        requestDirectory.setParameter("parentFolderId", null);
                        Map<String, Object> responseDirectory = new Gson().fromJson(requestDirectory.post().getString(), Map.class);
                        if ((Boolean) responseDirectory.get("success")) {
                            Logger.info("Crea directorio en drive ");
                            for (Field field : multimedia.getClass().getDeclaredFields()) {
                                if (field.getName().contains("url")) {
                                    try {
                                        Object value = field.get(multimedia);
                                        if (value != null && !"".equals(value.toString().trim()) && !value.toString().contains("http") && !value.toString().contains("https")) {
                                            //Check if file exist
                                            File fileMultimedia = new File(value.toString());
                                            Logger.info("Intenta subir el archivo: " + value.toString());
                                            if (fileMultimedia.exists()) {
                                                String urlFile = uploadFile(responseDirectory, fileMultimedia);
                                                if (urlFile != null) {
                                                    field.set(multimedia, urlFile);
                                                }
                                            } else
                                                System.out.println("El archivo " + value.toString() + "  no existe.");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        uploadedFilesGD = false;
                                    }
                                }
                            }
                            for (Field field : multimedia.getClass().getDeclaredFields()) {
                                if (field.getName().contains("url")) {
                                    try {
                                        Object value = field.get(multimedia);
                                        if (value != null && !"".equals(value.toString().trim()) && !value.toString().contains("http") && !value.toString().contains("https")) {
                                            uploadedFilesGD = false;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            //Checks if has aditional multimedia
                            if (multimedia != null && multimedia.hasAditionalFilesGD != null && multimedia.hasAditionalFilesGD) {
                                Logger.info("Intenta subir multimedia adicional");

                                List<ER_Aditional_Multimedia> aditional_multimedia = ER_Aditional_Multimedia.find("multimedia_id = ?", multimedia.id).fetch();
                                for (ER_Aditional_Multimedia currentAditional : aditional_multimedia) {
                                    try {
                                        if (currentAditional.urlFile != null && !"".equals(currentAditional.urlFile.trim()) && !currentAditional.urlFile.contains("http") && !currentAditional.urlFile.contains("https")) {
                                            //Check if file exist
                                            File fileMultimedia = new File(currentAditional.urlFile);
                                            Logger.info("Intenta subir el archivo adicional: " + currentAditional.urlFile);
                                            if (fileMultimedia.exists()) {
                                                String urlFile = uploadFile(responseDirectory, fileMultimedia);
                                                if (urlFile != null) {
                                                    currentAditional.urlFile = urlFile;
                                                    currentAditional.uploaded = true;
                                                }
                                            } else
                                                System.out.println("El archivo adicional" + currentAditional.urlFile + "  no existe.");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        uploadedFilesGD = false;
                                        Logger.info("ERROR EN adicional: " + e.getMessage());
                                    }
                                    currentAditional.save();
                                }
                            }
                        } else {
                            uploadedFilesGD = false;
                        }
                        Logger.info("Logro subir la multimedia del caso: " + incident.number + " exitoso: " + uploadedFilesGD);
                        multimedia.uploadedFilesGD = uploadedFilesGD;
                        multimedia.save();
                    }
                }
            }
        }
        catch(Exception e){
	        Logger.error(e.getMessage());
        }
	}
	
	private String uploadFile(Map<String, Object> responseDirectory, File file){
	    try{
    	if((Boolean) responseDirectory.get("success") && file != null){
    		WSRequest request = WS.url(WS_GDRIVE + "/sendPublicFile");
    		request.setHeader("Content-Type", "multipart/form-data");
        	request.setParameter("folderId", responseDirectory.get("data"));
        	request.files(new FileParam(file, "upload"));
        	Map<String, Object> response = new Gson().fromJson(request.post().getString(), Map.class);
        	System.out.println(response);
        	if((Boolean) response.get("success")){
        		return response.get("data").toString();
        	}
    	}
    	
    	return null;
        }
        catch(Exception e){
            Logger.error(e.getMessage());
            return null;
        }
    }
}
