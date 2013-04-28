/*
 * Copyright (c) 2012-2013, Poplar Yfyang 杨友峰 (poplar1123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.miemiedev.mybatis.paginator;


import org.apache.ibatis.session.RowBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询对象
 *
 * @author badqiu
 * @author hunhun
 */
public class PageQuery extends RowBounds implements java.io.Serializable {
	private static final long serialVersionUID = -8000900575354501298L;
    public final static int NO_PAGE = 1;
	/** 页数 */
	private int page = NO_PAGE;
	/** 分页大小 */
	private int limit = NO_ROW_LIMIT;
	/** 分页排序信息 */
	private List<SortInfo> sortInfoList = new ArrayList<SortInfo>();
    /** 结果集是否包含TotalCount */
    private boolean containsTotalCount = true;

    public PageQuery(){

    }

    public PageQuery(RowBounds rowBounds) {
        if(rowBounds instanceof PageQuery){
            PageQuery pageQuery = (PageQuery)rowBounds;
            this.page = pageQuery.page;
            this.limit = pageQuery.limit;
            this.sortInfoList = pageQuery.sortInfoList;
            this.containsTotalCount = pageQuery.containsTotalCount;
        }else{
            this.page = (rowBounds.getOffset()/rowBounds.getLimit())+1;
            this.limit = rowBounds.getLimit();
        }

    }

	public PageQuery(int limit) {
		this.limit = limit;
	}

    public PageQuery(int limit, boolean containsTotalCount) {
        this.limit = limit;
        this.containsTotalCount = containsTotalCount;
    }

	public PageQuery(int page, int limit) {
		this(page, limit, null, null);
	}

    public PageQuery(int page, int limit, boolean containsTotalCount) {
        this(page, limit, null, null, containsTotalCount);
    }

    public PageQuery(String sortString){
        this(SortInfo.parseSortColumns(sortString));
    }

    public PageQuery(String sortString, String sortExpression){
        this(SortInfo.parseSortColumns(sortString,sortExpression));
    }

    public PageQuery(SortInfo sortInfo) {
        this(NO_PAGE, NO_ROW_LIMIT,sortInfo);
    }

    public PageQuery(List<SortInfo> sortInfos) {
        this(NO_PAGE, NO_ROW_LIMIT,sortInfos);
    }

    public PageQuery(int page, int limit, SortInfo sortInfo) {
        this(page, limit, sortInfo, true);
    }

    public PageQuery(int page, int limit, SortInfo sortInfo, boolean containsTotalCount) {
        this.page = page;
        this.limit = limit;
        if(sortInfo != null){
            this.sortInfoList = new ArrayList();
            this.sortInfoList.add(sortInfo);
        }
        this.containsTotalCount = containsTotalCount;
    }

    public PageQuery(int page, int limit, String sortString){
        this(page,limit, sortString, null);
    }

    public PageQuery(int page, int limit, String sortString, boolean containsTotalCount){
        this(page,limit, sortString, null, containsTotalCount);
    }

    public PageQuery(int page, int limit, String sortString, String sortExpression){
        this(page, limit, sortString, sortExpression, true);
    }

    public PageQuery(int page, int limit, String sortString, String sortExpression, boolean containsTotalCount){
        this(page, limit, SortInfo.parseSortColumns(sortString ,sortExpression), containsTotalCount);
    }

    public PageQuery(int page, int limit, List<SortInfo> sortInfoList) {
        this(page, limit, sortInfoList, true);
    }

    public PageQuery(int page, int limit, List<SortInfo> sortInfoList, boolean containsTotalCount) {
        this.page = page;
        this.limit = limit;
        this.sortInfoList = sortInfoList;
        this.containsTotalCount = containsTotalCount;
    }



    public PageQuery addSortInfo(SortInfo sortInfo){
        this.sortInfoList.add(sortInfo);
        return this;
    }

    public PageQuery addSortInfo(String sortString, String sortExpression){
        this.sortInfoList.addAll(SortInfo.parseSortColumns(sortString, sortExpression));
        return this;
    }

    public PageQuery addSortInfo(String sortString){
        return addSortInfo(sortString, null);
    }



	public int getPage() {
		return page;
	}

	public PageQuery setPage(int page) {
		this.page = page;
        return this;
	}

    public int getLimit() {
        return limit;
    }

    public PageQuery setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public boolean isContainsTotalCount() {
        return containsTotalCount;
    }

    public void setContainsTotalCount(boolean containsTotalCount) {
        this.containsTotalCount = containsTotalCount;
    }

    public List<SortInfo> getSortInfoList() {
		return sortInfoList;
	}

	public void setSortInfoList(List<SortInfo> sortInfoList) {
		this.sortInfoList = sortInfoList;
	}

    @Override
    public int getOffset() {
        if(page >= 1){
            return (page-1) * limit;
        }
        return 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageQuery{");
        sb.append("page=").append(page);
        sb.append(", limit=").append(limit);
        sb.append(", sortInfoList=").append(sortInfoList);
        sb.append(", containsTotalCount=").append(containsTotalCount);
        sb.append('}');
        return sb.toString();
    }
}
