package com.eewoo.chat.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {
    void getFileStream(String filePath, HttpServletResponse response);

    String uploadFile(MultipartFile file) throws IOException;
}
