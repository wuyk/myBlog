package com.wuyk.blog.controller.admin;

import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.controller.BaseController;
import com.wuyk.blog.pojo.MetasDo;
import com.wuyk.blog.service.IMetasService;
import com.wuyk.blog.utils.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("admin/links")
public class LinksController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinksController.class);

    @Resource
    private IMetasService metasService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        List<MetasDo> metas = metasService.getMetas(TypeEnum.LINK.getType());
        request.setAttribute("links", metas);
        return "admin/links";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public RestResponse saveLink(@RequestParam String title, @RequestParam String url,
                                 @RequestParam String logo, @RequestParam Integer mid,
                                 @RequestParam(value = "sort", defaultValue = "0") int sort) {
        try {
            MetasDo metasDo = new MetasDo();
            metasDo.setName(title);
            metasDo.setSlug(url);
            metasDo.setDescription(logo);
            metasDo.setSort(sort);
            metasDo.setType(TypeEnum.LINK.getType());
            if (null != mid) {
                metasDo.setMid(mid);
                metasService.update(metasDo);
            } else {
                metasService.saveMeta(metasDo);
            }
        } catch (Exception e) {
            String msg = "友链保存失败";
            LOGGER.error(msg, e);
            return RestResponse.fail(msg);
        }
        return RestResponse.ok();
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public RestResponse delete(@RequestParam int mid) {
        try {
            metasService.delete(mid);
        } catch (Exception e) {
            String msg = "友链删除失败";
            LOGGER.error(msg, e);
            return RestResponse.fail(msg);
        }
        return RestResponse.ok();
    }

}
