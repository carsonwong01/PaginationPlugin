package com.carson.pagination.page;

/**
 * 分页工具类
 * @author wsc
 * @Date 2018/9/12 0:19
 */
public class PageContext extends Page {

    private static final long serialVersionUID = -8980405586436719845L;

    private static ThreadLocal<PageContext> context = new ThreadLocal<>();

    public PageContext() {
    }

    public static PageContext getContext(){
        PageContext pageContext = context.get();
        if(pageContext == null){
            pageContext = new PageContext();
            context.set(pageContext);
        }
        return pageContext;
    }

    public static void removeContext(){
        context.remove();
    }

    protected void initialize() {
    }
}
