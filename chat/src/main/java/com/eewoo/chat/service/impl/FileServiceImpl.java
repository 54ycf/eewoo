package com.eewoo.chat.service.impl;

import com.eewoo.chat.service.FileService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

@Service
public class FileServiceImpl implements FileService {
    static final String rootPath = System.getProperty("user.dir");
    @SneakyThrows
    @Override
    public void getFileStream(String filePath, HttpServletResponse response) {
        String fileFullName = rootPath + filePath;
        ServletOutputStream os;
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileFullName, "UTF-8"));
        response.setContentType("application/octet-stream");

        File file = new File(fileFullName);
        byte[] data = new byte[(int) file.length()];
        FileInputStream fos = new FileInputStream(file);
        fos.read(data);
        os = response.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }


}
