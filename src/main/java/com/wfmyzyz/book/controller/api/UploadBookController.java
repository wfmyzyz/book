package com.wfmyzyz.book.controller.api;

import com.wfmyzyz.book.common.FileType;
import com.wfmyzyz.book.domain.Book;
import com.wfmyzyz.book.domain.BookSerial;
import com.wfmyzyz.book.domain.enums.BookEnum;
import com.wfmyzyz.book.domain.enums.BookStatusEnum;
import com.wfmyzyz.book.service.IBookSerialService;
import com.wfmyzyz.book.service.IBookService;
import com.wfmyzyz.book.utils.FileTools;
import com.wfmyzyz.book.utils.Msg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private IBookService bookService;
    @Autowired
    private IBookSerialService bookSerialService;

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

    /**
     * 上传轮播图
     * @param file
     * @return
     */
    @RequestMapping(value = "rotation/uploadRotation",method = RequestMethod.POST)
    public Msg uploadRotation(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return Msg.error().add("error","没有图片文件！");
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
        if (!Objects.equals(suffix, FileType.PNG.getType())&&!Objects.equals(suffix,FileType.JPG.getType())&&!Objects.equals(suffix,FileType.GIF.getType())&&!Objects.equals(suffix,FileType.SVG.getType())){
            return Msg.error().add("error","图片仅支持jpg、png、gif、svg格式！");
        }
        String fileName = fileTools.uploadFile(file,filePath+"/admin/rotation/");
        if (fileName == null){
            return Msg.error().add("error","上传失败！请重新上传");
        }
        return Msg.success().add("data","/outimg/admin/rotation/"+fileName);
    }


    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("uploadBook")
    @Transactional
    public Msg uploadBook(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return Msg.error().add("error","没有书籍文件！");
        }
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1).toLowerCase();
        if (!Objects.equals(suffix, FileType.TXT.getType())){
            return Msg.error().add("error","上传书籍仅支持txt格式！");
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Book book = new Book();
            book.setHeadImage("/outimg/admin/bookHead/test.jpg");
            String name = getTxt(bufferedReader);
            if (name == null || StringUtils.isBlank(name)){
                return Msg.error().add("error","txt格式错误！");
            }
            book.setName(name);
            String introduce = getTxt(bufferedReader);
            if (introduce == null){
                return Msg.error().add("error","txt格式错误！");
            }
            book.setIntroduce(introduce);
            String bookExplain = getTxt(bufferedReader);
            if (bookExplain == null){
                return Msg.error().add("error","txt格式错误！");
            }
            book.setBookExplain(bookExplain);
            book.setAuthor("admin");
            book.setPubliser("admin");
            book.setBookType(BookEnum.章回.toString());
            book.setBookStatus(BookStatusEnum.连载.toString());
            boolean bookSave = bookService.save(book);
            if (!bookSave){
                return Msg.error().add("data","上传失败！");
            }
            String templeString;
            StringBuilder txt = new StringBuilder();
            List<BookSerial> bookSerialList = new ArrayList<>();
            BookSerial bookSerial = new BookSerial();
            while ( (templeString = bufferedReader.readLine()) != null ){
                if (StringUtils.isBlank(templeString)){
                    continue;
                }
                boolean matches = templeString.matches(".*title:第\\d*章.*");
                if (matches){
                    if (!StringUtils.isBlank(bookSerial.getTitle())){
                        bookSerial.setText(txt.toString());
                        bookSerial.setBookId(book.getBookId());
                        bookSerialList.add(bookSerial);
                        bookSerial = new BookSerial();
                    }
                    bookSerial.setSerialNum(0);
                    String titleReg = "title:第\\d+章";
                    Pattern titleCompile = Pattern.compile(titleReg);
                    Matcher titleMatcher = titleCompile.matcher(templeString);
                    if (titleMatcher.find()){
                        String numReg = "\\d+";
                        Pattern numCompile = Pattern.compile(numReg);
                        Matcher numMatcher = numCompile.matcher(titleMatcher.group());
                        if (numMatcher.find()){
                            bookSerial.setSerialNum(Integer.parseInt(numMatcher.group()));
                        }
                    }
                    bookSerial.setTitle(templeString.substring(templeString.indexOf("[")+1,templeString.lastIndexOf("]")));
                    txt = new StringBuilder();
                }else {
                    txt.append("<p>");
                    txt.append(templeString);
                    txt.append("<br></p>");
                }
            }
            bookSerial.setText(txt.toString());
            bookSerial.setBookId(book.getBookId());
            bookSerialList.add(bookSerial);
            bookSerialService.saveBatch(bookSerialList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileTools.uploadFile(file,filePath+"/admin/bookTxt/");
        return Msg.success().add("data","上传成功！");
    }

    private String getTxt(BufferedReader bufferedReader) throws IOException {
        String templeStr = bufferedReader.readLine();
        if (templeStr == null){
            return null;
        }
        return templeStr.substring(templeStr.indexOf("[")+1,templeStr.lastIndexOf("]"));
    }
}
