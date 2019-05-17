package com.wuyk.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wuyk.blog.constant.LogActionEnum;
import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.constant.WebConst;
import com.wuyk.blog.controller.BaseController;
import com.wuyk.blog.pojo.AttachDo;
import com.wuyk.blog.pojo.UsersDo;
import com.wuyk.blog.service.IAttachService;
import com.wuyk.blog.service.ILogService;
import com.wuyk.blog.utils.Commons;
import com.wuyk.blog.utils.RestResponse;
import com.wuyk.blog.utils.TaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件管理
 */
@Controller
@RequestMapping("/admin/attach")
public class AttachController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AttachController.class);

    public static final String CLASSPATH = TaleUtils.getUploadFilePath();

    @Resource
    private IAttachService attachService;

    @Resource
    private ILogService logService;

    /**
     * 附件页面
     *
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "")
    public String index(HttpServletRequest request, @RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "12") int limit) {
        PageInfo<AttachDo> attachPaginator = attachService.getAttachs(page, limit);
        request.setAttribute("attachs", attachPaginator);
        request.setAttribute(TypeEnum.ATTACH_URL.getType(), Commons.site_option(TypeEnum.ATTACH_URL.getType(), Commons.site_url()));
        request.setAttribute("max_file_size", WebConst.MAX_FILE_SIZE / 1024);
        return "admin/attach";
    }

    /**
     * 上传文件接口
     *
     * @param request
     * @return
     */
    @PostMapping(value = "upload")
    @ResponseBody
    public RestResponse upload(HttpServletRequest request, @RequestParam("file") MultipartFile[] multipartFiles) throws IOException {
        UsersDo users = this.user(request);
        Integer uid = users.getUid();
        List<String> errorFiles = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String fname = multipartFile.getOriginalFilename();
                if (multipartFile.getSize() <= WebConst.MAX_FILE_SIZE) {
                    String fkey = TaleUtils.getFileKey(fname);
                    String ftype = TaleUtils.isImage(multipartFile.getInputStream()) ? TypeEnum.IMAGE.getType() : TypeEnum.FILE.getType();
                    File file = new File(CLASSPATH + fkey);
                    try {
                        FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    attachService.save(fname, fkey, ftype, uid);
                } else {
                    errorFiles.add(fname);
                }
            }
        } catch (Exception e) {
            return RestResponse.fail();
        }
        return RestResponse.ok(errorFiles);
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public RestResponse delete(@RequestParam Integer id, HttpServletRequest request) {
        try {
            AttachDo attach = attachService.selectById(id);
            if (null == attach) {
                return RestResponse.fail("不存在该附件");
            }
            attachService.deleteById(id);
            new File(CLASSPATH + attach.getFkey()).delete();
            logService.insertLog(LogActionEnum.DEL_ARTICLE.getAction(), attach.getFkey(), request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "附件删除失败";
            logger.error(msg, e);
            return RestResponse.fail(msg);
        }
        return RestResponse.ok();
    }
}
