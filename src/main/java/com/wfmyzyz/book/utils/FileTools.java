package com.wfmyzyz.book.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 上传文件工具类
 * @author aa
 * @since 2019-10-08
 */
@Component
public class FileTools {

    /**
     * 上传一个文件
     * @param file
     * @param path
     * @return
     */
    public String uploadFile(MultipartFile file,String path){
        String newFileName = "";
        if (!file.isEmpty()){
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            newFileName = UUID.randomUUID().toString()+suffix;
            try {
                file.transferTo(new File(path+newFileName));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return newFileName;
        }
        return null;
    }

    /**
     * 删除一个文件
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath){
        File file = new File(filePath);
        boolean flag = file.delete();
        if (!flag){
            return false;
        }
        return true;
    }
}
