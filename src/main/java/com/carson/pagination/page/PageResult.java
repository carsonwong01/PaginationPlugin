package com.carson.pagination.page;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wsc
 * @Date 2018/9/12 0:25
 */
public class PageResult implements Serializable {
    private static final long serialVersionUID = 4720243398004341815L;

    private int recordCount;

    private int pageIndex;

    private int pageSize;

    private int pageTotal;

    private boolean hasNextpage = false;

    private boolean hasPreviousPage = false;

    private List<?> list;

    public PageResult(PageContext page, List<?> list){
        this.recordCount = page.getTotalRecords();
        this.pageIndex = page.getCurrentPage();
        this.pageSize = page.getPageSize();
        this.list = (list == null ? new ArrayList<>() : list);
        this.pageTotal = this.recordCount % this.pageSize == 0 ?
                (this.recordCount / this.pageSize) : (this.recordCount / this.pageSize + 1);
        this.hasNextpage = this.pageTotal > 1 && this.pageIndex < this.pageTotal;
        this.hasPreviousPage = this.pageTotal > 1 && this.pageIndex != 1;
    }

    @XmlElement
    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    @XmlElement
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @XmlElement
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @XmlElement
    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    @XmlElement
    public boolean isHasNextpage() {
        return hasNextpage;
    }

    public void setHasNextpage(boolean hasNextpage) {
        this.hasNextpage = hasNextpage;
    }

    @XmlElement
    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    @XmlElement
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}