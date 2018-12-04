package com.carson.pagination.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 * @author wsc
 * @Date 2018/9/12 0:05
 */
public class Page implements Serializable {
    private static final long serialVersionUID = 8133089387210209989L;
    //每页条数
    private int pageSize = 10;
    //初始当前页码
    private int currentPage = 1;
    //总页数
    private int totalPages = 0;
    //总条数
    private int totalRecords = 0;
    //
    private int pageStartRow = 0;

    private int pageEndRow = 0;

    private boolean pagination = false;

    boolean hasNextPage = false;

    boolean hasPreviousPage = false;

    private String pagedView;

    Object obj;

    List<Object> resultList;

    public Page() {
    }

    public Page(int rows, int pageSize) {
        this.init(rows, pageSize);
    }

    public void init(int totalRecords, int pageSize){
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        if(this.totalRecords % pageSize == 0){
            this.totalPages = this.totalRecords / pageSize;
        }else {
            this.totalPages = this.totalRecords / pageSize + 1;
        }
    }

    public void init(int totalRecords, int pageSize, int currentPage) {
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        if (this.totalRecords % pageSize == 0) {
            this.totalPages = this.totalRecords / pageSize;
        } else {
            this.totalPages = this.totalRecords / pageSize + 1;
        }

        if (currentPage != 0) {
            this.gotoPage(currentPage);
        }
    }

    public void gotoPage(int page) {
        this.currentPage = page;
        this.calculatePage();
    }

    private void calculatePage() {
        if (this.currentPage - 1 > 0) {
            this.hasPreviousPage = true;
        } else {
            this.hasPreviousPage = false;
        }

        if (this.currentPage >= this.totalPages) {
            this.hasNextPage = false;
        } else {
            this.hasNextPage = true;
        }

        if (this.currentPage * this.pageSize < this.totalRecords) {
            this.pageEndRow = this.currentPage * this.pageSize;
            this.pageStartRow = this.pageEndRow - this.pageSize;
        } else {
            this.pageEndRow = this.totalRecords;
            this.pageStartRow = this.pageSize * (this.totalPages - 1);
        }
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }

    public void setPageStartRow(int pageStartRow) {
        this.pageStartRow = pageStartRow;
    }

    public int getPageEndRow() {
        return pageEndRow;
    }

    public void setPageEndRow(int pageEndRow) {
        this.pageEndRow = pageEndRow;
    }

    public boolean isPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public String getPagedView() {
        return pagedView;
    }

    public void setPagedView(String pagedView) {
        this.pagedView = pagedView;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public List<Object> getResultList() {
        return resultList;
    }

    public void setResultList(List<Object> resultList) {
        this.resultList = resultList;
    }
}
