package com.wuyk.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.constant.WebConst;
import com.wuyk.blog.controller.BaseController;
import com.wuyk.blog.exception.TipException;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.MetasDo;
import com.wuyk.blog.pojo.UsersDo;
import com.wuyk.blog.pojo.vo.ContentsVo;
import com.wuyk.blog.service.IContentService;
import com.wuyk.blog.service.IMetasService;
import com.wuyk.blog.utils.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章controller
 */
@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = TipException.class)
public class ArticleController extends BaseController {

    @Resource
    private IContentService contentService;

    @Resource
    private IMetasService metasService;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit, HttpServletRequest request) {
        ContentsVo contentsVo = new ContentsVo();
        contentsVo.setOrderByClause("created desc");
        contentsVo.createCriteria().andTypeEqualTo(TypeEnum.ARTICLE.getType());
        PageInfo<ContentsDo> contentsPaginator = contentService.getArticlesWithpage(contentsVo, page, limit);
        request.setAttribute("articles", contentsPaginator);
        return "admin/article_list";
    }

    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        List<MetasDo> categories = metasService.getMetas(TypeEnum.CATEGORY.getType());
        request.setAttribute("categories", categories);
        return "admin/article_edit";
    }

    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid, HttpServletRequest request) {
        ContentsDo contentsDo = contentService.getContents(cid);
        request.setAttribute("contents", contentsDo);
        List<MetasDo> categories = metasService.getMetas(TypeEnum.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute("active", "article");
        return "admin/article_edit";
    }

    @PostMapping(value = "/publish")
    @ResponseBody
    public RestResponse publishArticle(ContentsDo contentsDo, HttpServletRequest request) {
        UsersDo users = this.user(request);
        contentsDo.setAuthorId(users.getUid());
        contentsDo.setType(TypeEnum.ARTICLE.getType());
        if (StringUtils.isBlank(contentsDo.getCategories())) {
            contentsDo.setCategories("默认分类");
        }
        String result = contentService.publish(contentsDo);
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok();
    }

    @PostMapping(value = "/modify")
    @ResponseBody
    public RestResponse modifyArticle(ContentsDo contentsDo, HttpServletRequest request) {
        UsersDo users = this.user(request);
        contentsDo.setAuthorId(users.getUid());
        contentsDo.setType(TypeEnum.ARTICLE.getType());
        String result = contentService.updateArticle(contentsDo);
        if (!WebConst.SUCCESS_RESULT.equals(result)) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok();
    }

}
