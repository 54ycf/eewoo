package com.eewoo.chat.service.impl;

import com.eewoo.chat.service.FileService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    static final String rootPath = System.getProperty("user.dir");
    @SneakyThrows
    @Override
    public void getFileStream(String filePath, HttpServletResponse response) {
        String fileFullName = rootPath + "/files/" +  filePath;
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

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (suffix.length() == 1){
            return null; //没有后缀去指定类型
        }
        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileNewName = uuid + suffix;
        String resourcePath = "/files/"+ fileNewName;
        String targetPath = rootPath + resourcePath;
        FileOutputStream fos = new FileOutputStream(targetPath);
        fos.write(file.getBytes());
        fos.close();

        return fileNewName;
    }


}
