package co.orange.ddanzi.service.common;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class GcsService {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public String generateSignedUrl(String objectName){
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();

        Map<String, String> extensionHeaders = new HashMap<>();
        String contentType = getConentType(objectName);

        extensionHeaders.put("Content-Type", contentType);

        URL url = storage.signUrl(blobInfo,
                15,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                Storage.SignUrlOption.withV4Signature()
                );

        return url.toString();
    }

    private static String getConentType(String objectName){
        String contentType;

        int lastDotIndex = objectName.lastIndexOf('.');
        if(lastDotIndex != -1){
            String extension = objectName.substring(lastDotIndex+1).toLowerCase();
            contentType = switch (extension){
                case "jpg","jpeg" -> "image/jpeg";
                case "png" -> "image/png";
                default -> "application/octet-stream";
            };
        }
        else {
            contentType = "application/octet-stream";
        }
        return contentType;
    }
}
