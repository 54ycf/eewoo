package com.eewoo.chat.service;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    void getFileStream(String filePath, HttpServletResponse response);
}
