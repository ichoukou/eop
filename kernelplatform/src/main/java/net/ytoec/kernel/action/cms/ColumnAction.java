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
import net.ytoec.kernel.dataobject.User;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ytoec.cms.bean.Article;
import com.ytoec.cms.bean.Column;
import com.ytoec.cms.service.ArticleService;
import com.ytoec.cms.service.ColumnService;

@Controller
@Scope("prototype")
public class ColumnAction extends AbstractActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5572914485701793113L;

	@Inject
	private ColumnService<Column> columnService;
	@Inject
	private ArticleService<Article> articleService;

	@SuppressWarnings("rawtypes")
	private Pagination pagination; //分页对象
	private Integer currentPage = 1;
	private Column column;

	private List<Column> columns;
	private String columnScpritStr;

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	@SuppressWarnings("rawtypes")
	public Pagination getPagination() {
		return pagination;
	}

	@SuppressWarnings("rawtypes")
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}


	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public void setColumnScpritStr(String columnScpritStr) {
		this.columnScpritStr = columnScpritStr;
	}

	public String getColumnScpritStr() {
		return columnScpritStr;
	}

	/**
	 * 查询所有栏目
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String search() {
		if (pagination == null) {
			pagination = new Pagination(currentPage, pageNum);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (column != null) {
			if (column.getParentColumnId() != null
					&& column.getParentColumnId() > 0) {
				params.put("parentColumnId", column.getParentColumnId());
			}
			if (column.getLevel() != null && column.getLevel() > 0) {
				params.put("layerId", column.getLevel());
			}
		}
		pagination = columnService.getPageList(pagination,params);
		columns = pagination.getRecords();
		return "search";
	}

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	public String add() {
		// 如果传值 ，则是添加子栏目
		if (column != null) {
			// 如果传值 ，则是添加子菜单
			if (column.getParentColumnId() != null
					&& column.getParentColumnId() > 0) {
				column = columnService.get(column.getParentColumnId());
			}
		}
		columnScpritStr = getColScript(false);
		return "toAdd";
	}
	
	public String checkColCode(){
		if (column != null && StringUtils.isNotBlank(column.getColumnCode())) {
			column = columnService.retrive(column.getColumnCode());
			if(column == null){
				putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"该栏目代码可以使用！","");
			}
			else{
				putMsg(JsonResponse.INFO_TYPE_ERROR,false,"该栏目代码已经被使用！","");
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"数据丢失，请返回重新操作！", "");
		}
		return "jsonRes";
	}

	/**
	 * 保存新增信息
	 * 
	 * @return
	 */
	public String create() {
		if (column != null) {
            User currentUser = super.readCookieUser();
			column.setCreateTime(new Date());
			column.setCreateUser(currentUser.getId());
			column.setStatus(Column.STATUS_NORMAL);
			boolean rs =  columnService.add(column);
			if (rs && column.getRootId() == null || column.getRootId() < 2) {
				column = columnService.retrive(column.getColumnCode());
				column.setRootId(column.getColumnId());
				columnService.edit(column);
			}
			if(rs){
			    putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"新增栏目操作已成功！",
			           "searchArticle.action?menuFlag=home_article_list&article.columnId="
			           + column.getParentColumnId());
			}
			else{
			    putMsg(JsonResponse.INFO_TYPE_ERROR,true,"新增栏目操作失败！",
                       "searchArticle.action?menuFlag=home_article_list&article.columnId="
                       + column.getParentColumnId());
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"数据丢失，请返回重新操作！", "searchArticle.action?menuFlag=home_article_list");
		}
		return "jsonRes";
	}

	/**
	 * 获取一个column对象，并返回到编辑页面
	 * 
	 * @return
	 */
	public String edit() {
		if (column != null) {
			setColumnScpritStr(getColScript(true));
			if (column.getColumnId() != null && column.getColumnId() > 0) {
				column = columnService.get(column.getColumnId());
			} else if (StringUtils.isNotEmpty(column.getColumnCode())) {
				column = columnService.retrive(column.getColumnCode());
			} else {
				putMsg(JsonResponse.INFO_TYPE_ERROR,false,"操作错误，请返回！", "searchArticle.action?menuFlag=home_article_list");
				return "jsonRes";
			}
			return "toEdit";
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"操作错误，请返回！", "searchArticle.action?menuFlag=home_article_list");
			return "jsonRes";
		}
	}

	/**
	 * 保存修改信息
	 * 
	 * @return
	 */
	public String update() {
		if (column != null) {
			column.setUpdateTime(new Date());
            User currentUser = super.readCookieUser();
			column.setUpdateUser(currentUser.getId());
			if (column.getRootId() == null || column.getRootId() < 2) {
				column.setRootId(column.getColumnId());
			}
			boolean rs = columnService.edit(column);
			if(rs){
			    
			    putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,
			           "栏目信息修改操作已成功！",
			           "searchArticle.action?menuFlag=home_article_list&article.columnId="
			           + column.getColumnId());
			}
			else{
			    putMsg(JsonResponse.INFO_TYPE_ERROR,true,
                       "栏目信息修改操作失败！",
                       "searchArticle.action?menuFlag=home_article_list&article.columnId="
                       + column.getColumnId());
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"操作错误，请返回！", "searchArticle.action?menuFlag=home_article_list");
		}
		return "jsonRes";
	}

	/**
	 * 逻辑删除，可以恢复
	 * 
	 * @return
	 */
	public String delete() {
		if (column != null && column.getColumnId() != null) {
			List<Column> childList = columnService.getChildList(column
					.getColumnId());
			List<Article> artList = articleService.getArticlesByCol(column
					.getColumnId());
			if (childList.size() > 0 || artList.size() > 0) {
				putMsg(JsonResponse.INFO_TYPE_WARNING,true,
						"请先删除该栏目下所有的子栏目和文章！",
						"searchArticle.action?article.columnId="
								+ column.getColumnId());
			} else {
				columnService.delete(column.getColumnId());
				putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"栏目删除操作已成功！", "searchArticle.action?menuFlag=home_article_list");
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"操作错误，请返回！", "searchArticle.action?menuFlag=home_article_list");
		}
		return "jsonRes";
	}

	/**
	 * 物理删除，数据不可恢复
	 */
	public String remove() {
		if (column != null && column.getColumnId() != null) {
			List<Column> childList = columnService.getChildList(column
					.getColumnId());
			List<Article> artList = articleService.getArticlesByCol(column
					.getColumnId());
			if (childList.size() > 0 || artList.size() > 0) {
				putMsg(JsonResponse.INFO_TYPE_WARNING,true,
						"请先删除该栏目下所有的子栏目和文章！",
						"searchArticle.action?article.columnId="
								+ column.getColumnId());
			} else {
				columnService.remove(column);
				putMsg(JsonResponse.INFO_TYPE_SUCCESS,true,"栏目删除操作已成功！", "searchArticle.action?menuFlag=home_article_list");
			}
		} else {
			putMsg(JsonResponse.INFO_TYPE_ERROR,false,"操作错误，请返回！", "searchArticle.action?menuFlag=home_article_list");
		}
		return "jsonRes";
	}

	/**
     * 生成栏目树js代码，以便页面使用.
     * @param hidCurCol 是否需要隐藏当前栏目节点对象
     * @return
     */
    public String getColScript(boolean hidCurCol) {
        HttpServletRequest request = ServletActionContext.getRequest();
        String path = request.getContextPath();
        path = path.equals("/")?"":path;
        List<Column> allColumnList = columnService.getAll(null);
        StringBuilder columnBuilder = new StringBuilder();
        Column curParentCol = columnService.get(column.getParentColumnId());
        //columnBuilder.append("[");

        List<Column> expandList = new ArrayList<Column>();
        if(hidCurCol){
            expandList = columnService.getColTopLineTree(column.getColumnId());
        }
        else if (column != null && column.getParentColumnId() != null) {
            expandList = columnService.getColTopLineTree(column.getParentColumnId());
        }
        for (Column c : allColumnList) {
            boolean hasChild = columnService.getChildList(c.getColumnId()).size()>0;
            if(columnBuilder.length() > 1){
                columnBuilder.append(",");
            }
            columnBuilder.append("{id:" + c.getColumnId()
                    + ",rootId:" + c.getRootId() 
                    + ",pId: "+ c.getParentColumnId() 
                    + ",name: '" + c.getColumnName()+"'"
                    + ",url: 'searchArticle.htm?menuFlag=home_article_list&article.columnId="+ c.getColumnId()+"'"
                    + ",target: '_self'"
                    + ",level: " + c.getLevel());
            if(c.getColumnId() == 1){
                columnBuilder.append(",open:true,icon:'"+path+"/js/zTree/icons/diy/1_open.png'");
                if(curParentCol == null){
                    columnBuilder.append(",checked:true");
                }
            }
            
            //修改栏目节点信息
            if(hidCurCol && c.getColumnId().equals(column.getColumnId())){
                columnBuilder.append(",chkDisabled:true");
            }
            
            if(curParentCol != null && c.equals(curParentCol)){
                columnBuilder.append(",checked:true");
            }
            
            if(hasChild){
                columnBuilder.append(",isParent:true");
            }
            if (expandList.contains(c)) {
                columnBuilder.append(",open:true");
            }
            columnBuilder.append("}");
        }
        //columnBuilder.append("]");
        return columnBuilder.toString();
    }
}
