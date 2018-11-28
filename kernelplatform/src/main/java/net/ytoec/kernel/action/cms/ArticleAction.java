package net.ytoec.kernel.action.cms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.JsonResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.bean.Column;
import com.ytoec.cms.service.ArticleService;
import com.ytoec.cms.service.ColumnService;
import com.ytoec.uninet.service.MediaHessianService;
import com.ytoec.uninet.service.impl.MediaHessianServiceImpl;
import com.ytoec.uninet.util.HessianUtil;


@Controller
@Scope("prototype")
public class ArticleAction extends AbstractActionSupport {

    /**
	 * 
	 */
	private static final long       serialVersionUID = -5572914485701793113L;

    @Inject
    private ArticleService<Article> articleService;
    @Inject
    private ColumnService<Column>   columnService;

	private Article                 article;

	private Pagination<Article>     pagination;
    private Integer                 currentPage      = 1;
    private List<Article>           articles;
    private List<Column>            columns;
    private String                  colScriptStr;


	public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

	public List<Column> getColumns() {
        return columns;
    }

	public Article getArticle() {
        return article;
    }

	public void setArticle(Article article) {
        this.article = article;
    }

	public List<Article> getArticles() {
        return articles;
    }

	public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

	public void setColScriptStr(String colScriptStr) {
        this.colScriptStr = colScriptStr;
    }

	public String getColScriptStr() {
        return colScriptStr;
    }
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public Pagination<Article> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<Article> pagination) {
		this.pagination = pagination;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	/**
     * 查询文章并分页
     * 
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String search() {
        if (pagination == null) {
            pagination = new Pagination(currentPage, pageNum);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        // params.put("status", Article.STATUS_NORMAL);
        if (article != null) {
            if (article.getColumnId() != null && article.getColumnId() > 1) {
                params.put("columnId", article.getColumnId());
            }
        }
        columns = columnService.getAll(null);
        colScriptStr = getColScript();
        pagination = articleService.getPageList(pagination, params);
        articles = pagination.getRecords();
        return "search";
    }

	/**
     * 跳转到新增页面
     * 
     * @return
     */
    public String add() {
        if (article == null) {
            article = new Article();
        }
        article.setArticleId(articleService.getMaxId() + 1);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentColumnId", 1);
        columns = columnService.getAll(null);
        return "toAdd";
    }


	/**
     * 保存新增信息
     * 
     * @return
     */
    public String create() {
        if (article != null) {
            article.setCreateTime(new Date());
            article.setCreateUser(super.readCookieUser().getId());
            // 获取新增或修改的图片以上传
            List<String> uris = getImgUris(article.getContent(), true);
            // 去掉图片地址中的服务器域名
            article.setContent(remImgAbsPath(article.getContent()));
            article.setStatus(Article.STATUS_NORMAL);
            articleService.add(article);
            if (uris != null && uris.size() > 0) {
                MediaHessianService mediaHessianService;
                try {
                    mediaHessianService = MediaHessianServiceImpl.getMediaService();
                    mediaHessianService.copyFileToBasePath(uris.toArray(new String[] {}));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    System.out.println("文件服务器连接失败！");
                    putMsg(JsonResponse.INFO_TYPE_WARNING,true,"数据保存成功，但图片上传失败！", "searchArticle.action?menuFlag=home_article_list&article.columnId=" + article.getColumnId());
                    return "jsonRes";
                }
            }
            putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"文章添加成功！", "searchArticle.action?menuFlag=home_article_list&article.columnId=" + article.getColumnId());
            return "jsonRes";
        } else {
            putMsg(JsonResponse.INFO_TYPE_ERROR,false,"数据丢失，请重新操作！", "searchArticle.action?menuFlag=home_article_list&article.columnId=" + article.getColumnId());
            return "jsonRes";
        }
    }

	/**
     * 获取一个article对象，并返回到编辑页面
     * 
     * @return
     */
    public String edit() {
        if (article != null && article.getArticleId() > 0) {
            article = articleService.get(article.getArticleId());
            article.setContent(addPathToImg(article.getContent()));
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("parentColumnId", 1);
            columns = columnService.getAll(null);
        }
        return "toEdit";
    }

	/**
     * 保存修改信息
     * 
     * @return
     */
    public String update() {
        if (article != null && article.getArticleId() > 0) {
            // 获取新增或修改的图片以上传
            List<String> uris = getImgUris(article.getContent(), true);
            // 去掉图片地址中的服务器域名
            article.setContent(remImgAbsPath(article.getContent()));
            article.setUpdateUser(super.readCookieUser().getId());
            articleService.edit(article);

			// 对临时图片（本次修改新增图片）进行上传
            if (uris != null && uris.size() > 0) {
                MediaHessianService mediaHessianService;
                try {
                    mediaHessianService = MediaHessianServiceImpl.getMediaService();
                    mediaHessianService.copyFileToBasePath(uris.toArray(new String[] {}));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    // e.printStackTrace();
                    System.out.println("文件服务器连接失败！");
                    putMsg(JsonResponse.INFO_TYPE_WARNING,true,"文章信息修改成功，但图片上传失败，请查看文件服务器日志！",
                           "searchArticle.action?article.columnId=" + article.getColumnId());
                }
            }
            putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"文章信息修改成功！", "searchArticle.action?menuFlag=home_article_list&article.columnId=" + article.getColumnId());
        } else {
            putMsg(JsonResponse.INFO_TYPE_ERROR,false,"数据丢失，请重新操作！", "searchArticle.action?menuFlag=home_article_list");
        }
        return "jsonRes";
    }

	/**
     * 查看详细
     * 
     * @return
     */
    public String detail() {
        if (article != null && article.getArticleId() > 0) {
			article = articleService.get(article.getArticleId());
            article.setContent(addPathToImg(article.getContent()));
        }
        return "detailArticle";
    }

	/**
     * 逻辑删除，可以恢复
     * 
     * @return
     */
    public String delete() {
        if (article != null) {
            article = articleService.get(article.getArticleId());
            articleService.delete(article.getArticleId());
            putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"文章信息删除操作已成功！", "searchArticle.action?menuFlag=home_article_list&article.columnId=" + article.getColumnId());
            article = null;
            return "jsonRes";
        }
        return "toList";
    }

	/**
     * 恢复报道数据
     */
    public String recovery() {
        if (article != null && article.getArticleId() != null) {
            article = articleService.get(article.getArticleId());
            if (article != null) {
                article.setStatus(Article.STATUS_NORMAL);
                articleService.edit(article);
                putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"文章信息恢复操作已成功！", "searchArticle.action?menuFlag=home_article_list&article.columnId=" + article.getColumnId());
            }
            article = null;
            return "jsonRes";
        }
        return "toList";
    }

	/**
     * 物理删除，数据不可恢复
     */
    public String remove() {
        if (article != null && article.getArticleId() != null) {
            articleService.remove(article);
            putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"文章信息删除操作已成功！", "searchArticle.action?menuFlag=home_article_list&article.columnId=" + article.getColumnId());
            article = null;
            return "jsonRes";
        }
        return "toList";
    }

	/**
     * 给文章内容中的图片等文件添加域名，以便显示
     * (注：在应用服务器上添加/media路径别名后，此方法不需要了。)
     * @param content
     * @return
     */
    private String addPathToImg(String content) {
        if (!StringUtils.isNotEmpty(content)) {
            return "";
        }
        List<String> imgUris = getImgUris(content, false);
        for (String uri : imgUris) {
            content = content.toLowerCase().replace(uri, getAbsImgUrl(uri));
        }
        return content;
    }

	/**
     * 获取新增的或修改的图片uri以上传（原有的附件不作任何操作）
     * 
     * @param content 文章内容
     * @param onlyTemp 是否只取临时图片URI(需要当前上传的图片)
     * @return
     */
    private List<String> getImgUris(String content, boolean onlyTemp) {
        List<String> imgUris = new ArrayList<String>();
        if (!StringUtils.isNotEmpty(content)) {
            return imgUris;
        }
        if (content.toLowerCase().contains("<img")) {
            int start = content.toLowerCase().indexOf("<img");
            int end = content.toLowerCase().indexOf("/>", start);
            String tmpAbsURL = HessianUtil.getMediaPath() + "/temp";
            while (start > -1 && end > -1) {
                start = content.toLowerCase().indexOf("src=", start) + 5;
                end = content.toLowerCase().indexOf("\"", start);
                String uri = content.substring(start, end);
                start = content.toLowerCase().indexOf("<img", end);
                end = content.toLowerCase().indexOf("/>", start);
                if (!uri.contains(tmpAbsURL) && onlyTemp) {
                    continue;
                }
                if (uri.contains(tmpAbsURL)) {
                    uri = uri.replace(tmpAbsURL, "");
                }
                imgUris.add(uri);
            }
        }
        return imgUris;
    }

	/**
     * 给图片添加服务器域名，以便页面显示
     * 
     * @param uri
     * @return
     */
    private String getAbsImgUrl(String uri) {
        if (StringUtils.isNotEmpty(uri) && uri.startsWith("/")) {
            return HessianUtil.getMediaPath() + uri;
            // return HessianUtil.getDomain() + uri;
        } else {
            return uri;
        }
    }

	/**
     * 清楚图片src中的域名，以便保存
     * 
     * @param content
     * @return
     */
    private String remImgAbsPath(String content) {
        if (!StringUtils.isNotEmpty(content)) {
            return "";
        }
        String absURL = HessianUtil.getMediaPath();
        String tmpAbsURL = HessianUtil.getMediaPath() + "/temp";
        content = content.replaceAll(tmpAbsURL, "");
        content = content.replaceAll(absURL, "");
        return content;
    }

    /**
     * 生成栏目树js代码，以便页面使用.
     * 
     * @return
     */
    public String getColScript() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String path = request.getContextPath();
        path = path.equals("/")?"":path;
        List<Column> allColumnList = columnService.getAll(null);
        StringBuilder columnBuilder = new StringBuilder();
        //columnBuilder.append("[");

        List<Column> expandList = new ArrayList<Column>();
        if (article != null && article.getColumnId() > 0) {
            expandList = columnService.getColTopLineTree(article.getColumnId());
        }
        for (Column c : allColumnList) {
            if(columnBuilder.length() > 1){
                columnBuilder.append(",");
            }
            columnBuilder.append("{id:" + c.getColumnId()
                    + ",rootId:" + c.getRootId() 
                    + ",pId: "+ c.getParentColumnId() 
                    + ",name: '" + c.getColumnName()+"'"
                    + ",url: 'searchArticle.action?menuFlag=home_article_list&article.columnId="+ c.getColumnId()+"'"
                    + ",target: '_self'"
                    + ",level: " + c.getLevel());
            //如果是栏目根节点
            if(/*c.getColumnId() == 1 ||*/"col_root_node".equals(c.getColumnCode())){
                columnBuilder.append(",open:true,icon:'"+path+"/js/zTree/icons/diy/1_open.png'");
            }
            if(article != null && c.getColumnId().equals(article.getColumnId())){
                columnBuilder.append(",checked:true");
            }
            if (expandList.contains(c) && columnService.getChildList(c.getColumnId()).size()>0) {
                columnBuilder.append(",open:true");
            }
            columnBuilder.append("}");
        }
        //columnBuilder.append("]");
        return columnBuilder.toString();
    }

}
