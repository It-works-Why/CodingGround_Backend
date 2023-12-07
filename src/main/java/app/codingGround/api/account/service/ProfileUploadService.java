package app.codingGround.api.account.service;

import app.codingGround.api.account.repository.AccountRepository;
import app.codingGround.api.entity.User;
import app.codingGround.domain.common.dto.response.DefaultResultDto;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class ProfileUploadService {

    private final AmazonS3 amazonS3;
    private final AccountRepository accountRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public DefaultResultDto uploadProfile(MultipartFile multipartFile, String userEmail) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();
        String createFilename = UUID.randomUUID() + "." + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, createFilename, multipartFile.getInputStream(), metadata);

        User user = accountRepository.findByUserEmail(userEmail);
        user.setUserProfileImg(amazonS3.getUrl(bucket, createFilename).toString());
        accountRepository.save(user);

        return DefaultResultDto.builder().success(true).message("성공").build();
    }

}
