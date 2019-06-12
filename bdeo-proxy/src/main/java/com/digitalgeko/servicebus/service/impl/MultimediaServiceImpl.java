package com.digitalgeko.servicebus.service.impl;

import com.digitalgeko.servicebus.model.rest.response.MultimediaDriveResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MultimediaServiceImpl extends AbstractBusServiceImpl {

    private String BASE_URL;
    private String APP_NAME;
    private RestTemplate restTemplate;

    @Autowired
    public MultimediaServiceImpl(@Value("${drive.ws.defaulturi}") String defaultUri,
                                 @Value("${drive.ws.appName}") String appName, RestTemplate restTemplate) {
        this.BASE_URL = defaultUri;
        this.APP_NAME = appName;
        this.restTemplate = restTemplate;
    }

    @Async ("threadPoolExecutor")
    public void processMultimedia(String caseRef, String folderId, List<String> list) {
        log.info("PROCESANDO MULTIMEDIAS " + list.size());
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("BDEO");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return;
        }
        int counter = 1;
        for (String processUrl : list) {
            String fileName = "BDEO_IMAGE_" + caseRef + "_" + counter + ".JPG";
            if (counter >= list.size()) {
                fileName = "BDEO_INSPECTION_REPORT_" + caseRef + ".PDF";
            }
            counter++;

            File tempFile = new File(tempDir.toFile(), fileName);

            ReadableByteChannel readableByteChannel = null;
            FileChannel fileChannel = null;
            try {

                URL url = new URL(processUrl);
                readableByteChannel = Channels.newChannel(url.openStream());
                fileChannel = new FileOutputStream(tempFile).getChannel();
                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                log.info("DESCARGA COMPLETA");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                IOUtils.closeQuietly(readableByteChannel);
                IOUtils.closeQuietly(fileChannel);
            }
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("appName", APP_NAME);
                map.add("upload", new FileSystemResource(tempFile));
                map.add("folderId", folderId);
                map.add("public", true);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
                String response = restTemplate.postForObject(BASE_URL + "/files/create", request, String.class);
                log.info("ARCHIVO EN DRIVE " + response);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        tempDir.toFile().deleteOnExit();
        log.info("FIN DE PROCESO");
        return;
    }

    public String findFolder(String folderName) {
        try {
            log.info("BUSCANDO FOLDER " + folderName);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/folders/find")
                    .queryParam("appName", APP_NAME)
                    .queryParam("folderName", folderName);
            log.info("URI: "+ builder.build().encode().toUri());
            String response = restTemplate.getForObject(builder.build().encode().toUri(), String.class);
            log.info("FOLDER LISTO " + response);
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
