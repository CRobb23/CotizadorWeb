package com.digitalgeko.servicebus.service.impl;

import com.digitalgeko.servicebus.exceptions.ConvertException;
import com.digitalgeko.servicebus.model.rest.response.MultimediaDriveResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CompletableFuture;

@Service
public class MultimediaServiceImpl extends AbstractBusServiceImpl {

    private String BASE_URL;
    private RestTemplate restTemplate;

    @Autowired
    public MultimediaServiceImpl(@Value("${drive.ws.defaulturi}") String defaultUri, RestTemplate restTemplate) {
        this.BASE_URL = defaultUri;
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<Boolean> processImage(String folderName, String filename, String urlStr) {
        log.info("PROCESANDO IMAGEN " + urlStr);
        ReadableByteChannel readableByteChannel = null;
        FileChannel fileChannel = null;
        try {
            URL url = new URL(urlStr);
            readableByteChannel = Channels.newChannel(url.openStream());
            fileChannel = new FileOutputStream(filename).getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            log.info("DESCARGA COMPLETA");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(readableByteChannel);
            IOUtils.closeQuietly(fileChannel);
        }
        try {
            File file = new File(filename);
            if (file.isFile()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> simpleMap= new LinkedMultiValueMap();
                simpleMap.add("folderName", folderName);
                HttpEntity<MultiValueMap<String, String>> simpleRequest = new HttpEntity(simpleMap, headers);
                String response = restTemplate.postForObject(BASE_URL + "/findFolderByName", simpleRequest, String.class);
                MultimediaDriveResponse driveResponse = (MultimediaDriveResponse) fromJSON(response, MultimediaDriveResponse.class);
                log.info("FOLDER LISTO " + driveResponse.getData());
                headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                MultiValueMap<String, Object> complexMap = new LinkedMultiValueMap();
                complexMap.add("upload", file);
                complexMap.add("folderId", driveResponse.getData());
                HttpEntity<MultiValueMap<String, Object>> complexRequest = new HttpEntity(simpleMap, headers);
                response = restTemplate.postForObject(BASE_URL + "/sendPrivateFile", complexRequest, String.class);
                log.info("ARCHIVO EN DRIVE");
            } else {
                log.info("ARCHIVO NO EXISTE");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("FIN DE PROCESO");
        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

}
