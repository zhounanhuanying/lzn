package com.bfdb.entity;

import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

public class PageInfo implements Serializable {
    private int curPage=1;//当前页码
    private int pageCount;//总页数
    private int evePageRecordCnt =20;//每页记录数
    private int page = 1;//跳转页码
    private int prePage;
    private int nextPage;
    private int allRecordCount;

    public int getEvePageRecordCnt() {
        return evePageRecordCnt;
    }
    public void setEvePageRecordCnt(int evePageRecordCnt) {
        this.evePageRecordCnt = evePageRecordCnt;
    }
    public int getAllRecordCount() {
        return allRecordCount;
    }
    public void setAllRecordCount(int allRecordCount) {
        this.allRecordCount = allRecordCount;
    }
    public int getNextPage() {
        return nextPage;
    }
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
    public int getPrePage() {
        return prePage;
    }
    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getCurPage() {
        return curPage;
    }
    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }


    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public RowBounds setRowBounds(int page){
        if(page==0){
            page=1;
        }
        return new RowBounds((page-1)*evePageRecordCnt, evePageRecordCnt);
    }
    public PageInfo updatePageInfo(long allRecordCount) {
        this.setAllRecordCount((int)allRecordCount);
        if(allRecordCount%evePageRecordCnt==0){
            this.setPageCount((int)allRecordCount/evePageRecordCnt);
        }else{
            this.setPageCount((int)allRecordCount/evePageRecordCnt+1);
        }

        this.setCurPage(page);
        if(this.curPage==0||this.curPage==1){
            this.setPrePage(1);
        }else{
            this.setPrePage(this.curPage-1);
        }
        if(this.curPage==pageCount){
            this.setNextPage(this.getCurPage());
        }else{
            this.setNextPage(this.getCurPage()+1);
        }
        if(pageCount==0){
            pageCount=1;
        }
        return this;
    }

}
