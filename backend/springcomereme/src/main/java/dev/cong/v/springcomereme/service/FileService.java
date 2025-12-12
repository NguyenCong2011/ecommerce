    package dev.cong.v.springcomereme.service;

    import com.amazonaws.services.s3.AmazonS3;
    import com.amazonaws.services.s3.model.CannedAccessControlList;
    import com.amazonaws.services.s3.model.ObjectMetadata;
    import com.amazonaws.services.s3.model.PutObjectRequest;
    import dev.cong.v.springcomereme.response.FileUploadResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.util.*;

    @Service
    @RequiredArgsConstructor
    public class FileService {


        private final AmazonS3 amazonS3Client;

        @Value("${aws.s3.bucketName}")
        private    String bucketName;


        private String  generateFileName(MultipartFile multipartFile){
            return  new Date().getTime()+"-"+ Objects.requireNonNull
                    (multipartFile.getOriginalFilename()).replace(" ","-");
        }


        public ResponseEntity<?> upload(MultipartFile multipartFile) {
            try {
                FileUploadResponse response = getFileUploadResponse(multipartFile);

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private FileUploadResponse getFileUploadResponse(MultipartFile multipartFile) throws IOException {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getSize());

            String fileName = generateFileName(multipartFile);

            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName,
                    multipartFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String publicUrl = getPublicUrl(fileName);

            return FileUploadResponse.builder()
                    .size(multipartFile.getSize())
                    .fileName(fileName)
                    .publicUrl(publicUrl)
                    .uploadTime(System.currentTimeMillis())
                    .build();
        }

        private String getPublicUrl(String fileName) {
            return String.format("https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, amazonS3Client.getRegionName(), fileName);
        }



        public ResponseEntity<?> uploadMultiple(List<MultipartFile> files) {
            List<FileUploadResponse> responses = new ArrayList<>();

            for (MultipartFile file : files) {
                try {
                    FileUploadResponse response = getFileUploadResponse(file);
                    responses.add(response);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return new ResponseEntity<>(responses, HttpStatus.CREATED);
        }
    }
