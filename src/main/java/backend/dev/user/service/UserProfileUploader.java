package backend.dev.user.service;

import backend.dev.setting.exception.ErrorCode;
import backend.dev.setting.exception.PublicPlusCustomException;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UserProfileUploader {

    private static final String FILE_NAME_REGEX = "[^a-zA-Z0-9.\\-_]";

    @Value("${file.dir}")
    private String uploadPath;

    @PostConstruct
    private void init() {
        File tempDir = new File(uploadPath);

        if (!tempDir.exists()) {
            boolean created = tempDir.mkdir();
            if (!created) {
                throw new PublicPlusCustomException(ErrorCode.PROFILE_CREATE_DIRECTORY_FAIL);
            }
        }
        uploadPath = tempDir.getAbsolutePath();
    }

    public String upload(String userId,MultipartFile file) {
        String saveFileName = userId + "_" + cleanFileName(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            file.transferTo(new File(uploadPath, saveFileName));
        } catch (IOException e) {
            throw new PublicPlusCustomException(ErrorCode.WRONG_FILE_TYPE);
        }
        return Paths.get(uploadPath, saveFileName).toString();
    }

    // 파일 이름에 특수 문자나 공백이 포함되어 있는 경우, 그 문자를 밑줄(_)로 대체
    private String cleanFileName(String fileName) {
        return fileName.replaceAll(FILE_NAME_REGEX, "_");
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);

        if (file.exists() && !file.delete()) {
            throw new PublicPlusCustomException(ErrorCode.PROFILE_DELETE_FAIL);
        }
    }

}
