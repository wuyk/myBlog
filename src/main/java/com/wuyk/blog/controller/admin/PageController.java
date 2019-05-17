package com.wuyk.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wuyk.blog.constant.LogActionEnum;
import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.constant.WebConst;
import com.wuyk.blog.controller.BaseController;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.UsersDo;
import com.wuyk.blog.pojo.vo.ContentsVo;
import com.wuyk.blog.service.IContentService;
import com.wuyk.blog.service.ILogService;
import com.wuyk.blog.utils.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("admin/page")
public class PageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PageController.class);

    @Resource
    private IContentService contentsService;

    @Resource
    private ILogService logService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        ContentsVo contentVoExample = new ContentsVo();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo(TypeEnum.PAGE.getType());
        PageInfo<ContentsDo> contentsPaginator = contentsService.getArticlesWithpage(contentVoExample, 1, WebConst.MAX_POSTS);
        request.setAttribute("articles", contentsPaginator);
        return "admin/page_list";
    }

    @GetMapping(value = "new")
    public String newPage() {
        return "admin/page_edit";
    }

    @GetMapping(value = "/{cid}")
    public String editPage(@PathVariable String cid, HttpServletRequest request) {
        ContentsDo contents = contentsService.getContents(cid);
        request.setAttribute("contents", contents);
        return "admin/page_edit";
    }

    @PostMapping(value = "publish")
    @ResponseBody
    public RestResponse publishPage(@RequestParam String title, @RequestParam String content,
                                    @RequestParam String status, @RequestParam String slug,
                                    @RequestParam(required = false) Integer allowComment, @RequestParam(required = false) Integer allowPing, HttpServletRequest request) {

        UsersDo users = this.user(request);
        ContentsDo contentsDo = new ContentsDo();
        contentsDo.setTitle(title);
        contentsDo.setContent(content);
        contentsDo.setStatus(status);
        contentsDo.setSlug(slug);
        contentsDo.setType(TypeEnum.PAGE.getType());
        if (null != allowComment) {
            contentsDo.setAllowComment(allowComment == 1);
        }
        if (null != allowPing) {
            contentsDo.setAllowPing(allowPing == 1);
        }
        contentsDo.setAuthorId(users.getUid());
        String result = contentsService.publish(contentsDo);
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok();
    }

    @PostMapping(value = "modify")
    @ResponseBody
    public RestResponse modifyArticle(@RequestParam Integer cid, @RequestParam String title,
                                        @RequestParam String content,
                                        @RequestParam String status, @RequestParam String slug,
                                        @RequestParam(required = false) Integer allowComment, @RequestParam(required = false) Integer allowPing, HttpServletRequest request) {

        UsersDo users = this.user(request);
        ContentsDo contentsDo = new ContentsDo();
        contentsDo.setCid(cid);
        contentsDo.setTitle(title);
        contentsDo.setContent(content);
        contentsDo.setStatus(status);
        contentsDo.setSlug(slug);
        contentsDo.setType(TypeEnum.PAGE.getType());
        if (null != allowComment) {
            contentsDo.setAllowComment(allowComment == 1);
        }
        if (null != allowPing) {
            contentsDo.setAllowPing(allowPing == 1);
        }
        contentsDo.setAuthorId(users.getUid());
        String result = contentsService.updateArticle(contentsDo);
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok();
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public RestResponse delete(@RequestParam int cid, HttpServletRequest request) {
        String result = contentsService.deleteByCid(cid);
        logService.insertLog(LogActionEnum.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok();
    }
}
