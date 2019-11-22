package com.wfmyzyz.book.controller.api;

import com.wfmyzyz.book.common.FileType;
import com.wfmyzyz.book.utils.FileTools;
import com.wfmyzyz.book.utils.Msg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author aa
 */
@RestController
@RequestMapping("admin/admin")
public class UploadBookController {

    @Value("${myuploadurl.url}")
    private String filePath;
    @Autowired
    private FileTools fileTools;

    /**
     * 上传书籍封面
     * @param file
     * @return
     */
    @RequestMapping(value = "book/uploadHead",method = RequestMethod.POST)
    public Msg uploadHead(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return Msg.error().add("error","没有图片文件！");
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
        if (!Objects.equals(suffix, FileType.PNG.getType())&&!Objects.equals(suffix,FileType.JPG.getType())&&!Objects.equals(suffix,FileType.GIF.getType())&&!Objects.equals(suffix,FileType.SVG.getType())){
            return Msg.error().add("error","图片仅支持jpg、png、gif、svg格式！");
        }
        String fileName = fileTools.uploadFile(file,filePath+"/admin/bookHead/");
        if (fileName == null){
            return Msg.error().add("error","上传失败！请重新上传");
        }
        return Msg.success().add("data","/outimg/admin/bookHead/"+fileName);
    }

    /**
     * 删除书籍封面
     * @param imagePath
     * @return
     */
    @RequestMapping(value = "book/deleteHead",method = RequestMethod.POST)
    public Msg deleteHead(@RequestParam("imagePath") String imagePath){
        if (!imagePath.isEmpty()){
            String deleteName = imagePath.substring(imagePath.lastIndexOf("/"));
            String oldFile = filePath + "/admin/bookHead"+deleteName;
            boolean flag = fileTools.deleteFile(oldFile);
            if (!flag){
                return Msg.error().add("error","删除失败！请重新上传");
            }
            return Msg.success().add("data","删除成功！");
        }
        return Msg.error().add("error","文件路径为空！");
    }

    /**
     * 上传书籍pdf
     * @return
     */
    @RequestMapping(value = "book/uploadBookPdf",method = RequestMethod.POST)
    public Msg uploadBookPdf(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return Msg.error().add("error","没有pdf文件！");
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
        if (!Objects.equals(suffix,FileType.PDF.getType())){
            return Msg.error().add("error","书籍仅支持pdf格式！");
        }
        String fileName = fileTools.uploadFile(file,filePath+"/admin/book/pdf/");
        if (fileName == null){
            return Msg.error().add("error","上传失败！请重新上传");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("data","/outimg/admin/book/pdf/"+fileName);
        map.put("name",file.getOriginalFilename());
        return Msg.success().add("map",map);
    }

    /**
     * 删除pdf书籍
     * @param path
     * @return
     */
    @RequestMapping(value = "book/deleteBookPdf",method = RequestMethod.POST)
    public Msg deleteBookPdf(@RequestParam("path") String path){
        if (StringUtils.isBlank(path)){
            return Msg.error().add("error","文件路径为空！");
        }
        String deleteName = path.substring(path.lastIndexOf("/"));
        String oldFile = filePath + "/admin/book/pdf/"+deleteName;
        boolean flag = fileTools.deleteFile(oldFile);
        if (!flag){
            return Msg.error().add("error","删除失败！请重新上传");
        }
        return Msg.success().add("data","删除成功！");
    }

    /**
     * 章回上传图片
     * @param files
     * @return
     */
    @RequestMapping(value = "serial/uploadInto",method = RequestMethod.POST)
    public Map<String,Object> uploadInto(@RequestParam("file") MultipartFile[] files){
        Map<String,Object> map = new HashMap<>();
        if (files.length == 0){
            map.put("errno","1");
            map.put("error","没有文件！");
            return map;
        }
        List<String> urlList = new ArrayList<>();
        for (MultipartFile file:files){
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),file.getOriginalFilename().length());
            if (!suffix.toLowerCase().equals(".png")&&!suffix.toLowerCase().equals(".jpg")&&!suffix.toLowerCase().equals(".gif")&&!suffix.toLowerCase().equals(".svg")){
                continue;
            }
            String flagName = fileTools.uploadFile(file,filePath+"/admin/serial/");
            if (flagName != null){
                urlList.add("/outimg/admin/serial/"+flagName);
            }
        }
        map.put("errno",0);
        map.put("data",urlList);
        return map;
    }

}
