package service.implementation;

import com.google.gson.Gson;
import com.jcraft.jsch.*;
import models.ER_Incident;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.modules.guice.InjectSupport;
import service.PolicyFileService;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@InjectSupport
public class PolicyFileServiceFtpImpl implements PolicyFileService {

    private final String PARENT_FOLDER_ID = Play.configuration.getProperty("drive.parentFolderId");
    private final String WS_GDRIVE = Play.configuration.getProperty("drive.proxy.url");

    @Override
    public void searchDownloadablePolicies() {
        List<ER_Incident> incidentList = ER_Incident.find("byPolicyFileDownload", Boolean.FALSE).fetch();
        Logger.info("Inicia a buscar casos para descarga de poliza local, numero de casos: " + incidentList.size());
        for (ER_Incident incident : incidentList) {
            Logger.info("Busca la poliza: " + incident.policyFileName);
            String url = searchSpecificFile(incident.policyFileName);
            if (url != null) {
                incident.policyFileDownload = Boolean.TRUE;
                incident.policyFileUpload = Boolean.FALSE;
                incident.policyFilePath = url;
                incident.save();
            }
        }
    }

    @Override
    public void searchUploadablePolicies() {
        List<ER_Incident> incidentList = ER_Incident.find("byPolicyFileUpload", Boolean.FALSE).fetch();
        Logger.info("Inicia a buscar casos para subir a drive, numero de casos: " + incidentList.size());
        for (ER_Incident incident : incidentList) {
            Logger.info("Buscar poliza local para subir a drive en ruta " + incident.policyFilePath  + " del caso : " + incident.number);
            String url = uploadSpecificFile(incident.number, incident.policyFilePath);
            Logger.info("Logro subir la url: " + url + " del caso: " + incident.number);
            if (url != null) {
                incident.policyFileUpload = Boolean.TRUE;
                incident.policyWebPath = url;
                incident.save();
            }
        }
    }


    private String searchSpecificFile(String fileName) {
        FTPClient ftpClient = new FTPClient();
        String host = Play.configuration.getProperty("sftp.host");
        Integer port = Play.configuration.getProperty("sftp.port") != null ? Integer.parseInt(Play.configuration.getProperty("sftp.port")) : 22;
        String username = Play.configuration.getProperty("sftp.username");
        String password = Play.configuration.getProperty("sftp.password");
        try {
            ftpClient.connect(host, port);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                Logger.error("Connect failed");
                return null;
            }
            boolean success = ftpClient.login(username, password);
            if (!success) {
                Logger.error("Could not login to the server");

                return null;
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String filePath = generateFilePath(fileName);
            File downloadFile1 = new File(filePath);
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            success = ftpClient.retrieveFile(fileName, outputStream1);
            outputStream1.close();
            if (!success) {
                Logger.error("Could not retrieve file " + fileName);
                return null;
            }
            Logger.error("Guardo poliza en ruta: " + filePath);
            return filePath;
        } catch (IOException e) {
            Logger.error(e, e.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

//    private String searchSpecificFile(String fileName) {
//        JSch jsch = new JSch();
//        Session session = null;
//        Channel channel = null;
//        ChannelSftp sftpChannel = null;
//        String host = Play.configuration.getProperty("sftp.host");
//        Integer port = Play.configuration.getProperty("sftp.port") != null ? Integer.parseInt(Play.configuration.getProperty("sftp.port")) : 22;
//        String username = Play.configuration.getProperty("sftp.username");
//        String password = Play.configuration.getProperty("sftp.password");
//        try {
//            session = jsch.getSession(username, host, port);
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.setPassword(password);
//            session.connect();
//            channel = session.openChannel(Play.configuration.getProperty("ftp.type"));
//            channel.connect();
//            sftpChannel = (ChannelSftp) channel;
//            String filePath = generateFilePath(fileName);
//            sftpChannel.get(fileName, filePath);
//            return filePath;
//        } catch (JSchException e) {
//            Logger.error(e, e.getMessage());
//        } catch (SftpException e) {
//            Logger.error(e, e.getMessage());
//        } finally {
//            if (sftpChannel != null)
//                sftpChannel.exit();
//            if (channel != null)
//                channel.disconnect();
//            if (session != null)
//                session.disconnect();
//        }
//        return null;
//    }

    private String uploadSpecificFile(Long incidentNumber, String filePath) {
        try{
            WS.WSRequest requestDirectory = WS.url(WS_GDRIVE + "/findFolderByName");
            requestDirectory.setParameter("folderName", incidentNumber);
            requestDirectory.setParameter("parentFolderId", null);
            Map<String, Object> responseDirectory = new Gson().fromJson(requestDirectory.post().getString(), Map.class);
            if((Boolean) responseDirectory.get("success")) {
                //Check if file exist
                File fileMultimedia = new File (filePath);
                if (fileMultimedia.exists()) {
                    String urlFile = uploadFile(responseDirectory,fileMultimedia);
                    return urlFile;
                } else {
                    Logger.error("El archivo " + filePath + "  no existe.");
                }
            }
        }catch(Exception e){
            Logger.error(e, e.getMessage());
        }
        return null;
    }

    private static String generateFilePath(String fileName){
        try{
            String files = Play.applicationPath.getAbsolutePath() + "/tmpFiles/";
            Logger.info(files);
            File directory = new File(files);
            if(!directory.exists()){
                directory.mkdirs();
            }
            String generatedFileName = files + UUID.randomUUID().toString().replaceAll("-", "");
            generatedFileName += fileName.substring(fileName.lastIndexOf("."), fileName.length()) ;

            return generatedFileName;
        }catch(Exception e){
            Logger.error(e, e.getMessage());
        }
        return null;
    }

    private String uploadFile(Map<String, Object> responseDirectory, File file){
        try {
            if ((Boolean) responseDirectory.get("success") && file != null) {
                WS.WSRequest request = WS.url(WS_GDRIVE + "/sendPublicFile");
                request.setHeader("Content-Type", "multipart/form-data");
                request.setParameter("folderId", responseDirectory.get("data"));
                request.files(new WS.FileParam(file, "upload"));
                Map<String, Object> response = new Gson().fromJson(request.post().getString(), Map.class);
                System.out.println(response);
                if ((Boolean) response.get("success")) {
                    return response.get("data").toString();
                }
            }
        }
        catch(Exception e){
            Logger.error(e, e.getMessage());
        }
        return null;
    }
}
