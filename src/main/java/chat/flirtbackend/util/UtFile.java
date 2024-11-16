package chat.flirtbackend.util;

import chat.flirtbackend.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public class UtFile {
    public static String getExtension(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public static String getExtension(FileDTO fileDTO) {
        String fileName = fileDTO.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
