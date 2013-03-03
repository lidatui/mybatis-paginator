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
	private int page;
	/** 分页大小 */
	private int limit;
	/** 分页排序信息 */
	private List<SortInfo> sortInfoList;

	public PageQuery() {
        this.page = NO_PAGE;
        this.limit = NO_ROW_LIMIT;
        this.sortInfoList = new ArrayList();
	}

	public PageQuery(int limit) {
        this.page = NO_PAGE;
		this.limit = limit;
        this.sortInfoList = new ArrayList();
	}

	public PageQuery(PageQuery query) {
		this.page = query.page;
		this.limit = query.limit;
        this.sortInfoList = query.sortInfoList;
	}

	public PageQuery(int page, int limit) {
		this.page = page;
		this.limit = limit;
        this.sortInfoList = new ArrayList();
	}

    public PageQuery(SortInfo sortInfo) {
        this.page = NO_PAGE;
        this.limit = NO_ROW_LIMIT;
        this.sortInfoList = new ArrayList();
        if(sortInfo != null){
            this.sortInfoList.add(sortInfo);
        }
    }

    public PageQuery(int page, int limit, SortInfo sortInfo) {
        this.page = page;
        this.limit = limit;
        this.sortInfoList = new ArrayList();
        if(sortInfo != null){
            this.sortInfoList.add(sortInfo);
        }
    }

    public PageQuery(int page, int limit, List<SortInfo> sortInfoList) {
        this.page = page;
        this.limit = limit;
        this.sortInfoList = sortInfoList;
    }



	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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
		final StringBuilder sb = new StringBuilder();
		sb.append("PageQuery");
		sb.append("{page=").append(page);
		sb.append(", limit=").append(limit);
		sb.append(", sortInfoList=").append(sortInfoList);
		sb.append('}');
		return sb.toString();
	}
}
