package com.wuyk.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.controller.admin.AttachController;
import com.wuyk.blog.dao.AttachDoMapper;
import com.wuyk.blog.dao.CommentsDoMapper;
import com.wuyk.blog.dao.ContentsDoMapper;
import com.wuyk.blog.dao.MetasDoMapper;
import com.wuyk.blog.exception.TipException;
import com.wuyk.blog.pojo.CommentsDo;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.bo.BackResponseBo;
import com.wuyk.blog.pojo.bo.StatisticsBo;
import com.wuyk.blog.pojo.vo.AttachVo;
import com.wuyk.blog.pojo.vo.CommentsVo;
import com.wuyk.blog.pojo.vo.ContentsVo;
import com.wuyk.blog.pojo.vo.MetasVo;
import com.wuyk.blog.service.ISiteService;
import com.wuyk.blog.utils.DateKit;
import com.wuyk.blog.utils.TaleUtils;
import com.wuyk.blog.utils.ZipUtils;
import com.wuyk.blog.utils.backup.Backup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wuyk
 */
@Service
public class SiteServiceImpl implements ISiteService {

    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Resource
    private CommentsDoMapper commentsDoMapper;

    @Resource
    private ContentsDoMapper contentsDoMapper;

    @Resource
    private AttachDoMapper attachDoMapper;

    @Resource
    private MetasDoMapper metasDoMapper;

    @Override
    public List<CommentsDo> recentComments(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        CommentsVo commentsVo = new CommentsVo();
        commentsVo.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        return commentsDoMapper.selectByExampleWithBLOBs(commentsVo);
    }

    @Override
    public List<ContentsDo> recentContents(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        ContentsVo contentsVo = new ContentsVo();
        contentsVo.createCriteria().andStatusEqualTo(TypeEnum.PUBLISH.getType()).andTypeEqualTo(TypeEnum.ARTICLE.getType());
        contentsVo.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        return contentsDoMapper.selectByExampleWithBLOBs(contentsVo);
    }

    @Override
    public StatisticsBo getStatistics() {
        StatisticsBo statistics = new StatisticsBo();

        ContentsVo contentsVo = new ContentsVo();
        contentsVo.createCriteria().andTypeEqualTo(TypeEnum.ARTICLE.getType()).andStatusEqualTo(TypeEnum.PUBLISH.getType());
        Long articles =   contentsDoMapper.countByExample(contentsVo);

        Long comments = commentsDoMapper.countByExample(new CommentsVo());

        Long attachs = attachDoMapper.countByExample(new AttachVo());

        MetasVo metaVoExample = new MetasVo();
        metaVoExample.createCriteria().andTypeEqualTo(TypeEnum.LINK.getType());
        Long links = metasDoMapper.countByExample(metaVoExample);

        statistics.setArticles(articles);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setLinks(links);
        return statistics;
    }

    @Override
    public BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception {
        BackResponseBo backResponse = new BackResponseBo();
        if (bk_type.equals("attach")) {
            if (StringUtils.isBlank(bk_path)) {
                throw new TipException("请输入备份文件存储路径");
            }
            if (!(new File(bk_path)).isDirectory()) {
                throw new TipException("请输入一个存在的目录");
            }
            String bkAttachDir = AttachController.CLASSPATH + "upload";
            String bkThemesDir = AttachController.CLASSPATH + "templates/themes";

            String fname = DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".zip";

            String attachPath = bk_path + "/" + "attachs_" + fname;
            String themesPath = bk_path + "/" + "themes_" + fname;

            ZipUtils.zipFolder(bkAttachDir, attachPath);
            ZipUtils.zipFolder(bkThemesDir, themesPath);

            backResponse.setAttachPath(attachPath);
            backResponse.setThemePath(themesPath);
        }
        if (bk_type.equals("db")) {

            String bkAttachDir = AttachController.CLASSPATH + "upload/";
            if (!(new File(bkAttachDir)).isDirectory()) {
                File file = new File(bkAttachDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            String sqlFileName = "tale_" + DateKit.dateFormat(new Date(), fmt) + "_" + TaleUtils.getRandomNumber(5) + ".sql";
            String zipFile = sqlFileName.replace(".sql", ".zip");

            Backup backup = new Backup(TaleUtils.getNewDataSource().getConnection());
            String sqlContent = backup.execute();

            File sqlFile = new File(bkAttachDir + sqlFileName);
            write(sqlContent, sqlFile, Charset.forName("UTF-8"));

            String zip = bkAttachDir + zipFile;
            ZipUtils.zipFile(sqlFile.getPath(), zip);

            if (!sqlFile.exists()) {
                throw new TipException("数据库备份失败");
            }
            sqlFile.delete();

            backResponse.setSqlPath(zipFile);

            // 10秒后删除备份文件
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new File(zip).delete();
                }
            }, 10 * 1000);
        }
        return backResponse;
    }

    private void write(String data, File file, Charset charset) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data.getBytes(charset));
        } catch (IOException var8) {
            throw new IllegalStateException(var8);
        } finally {
            if(null != os) {
                try {
                    os.close();
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }
        }

    }
}
