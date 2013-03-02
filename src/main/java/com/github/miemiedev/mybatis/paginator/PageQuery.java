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


import java.util.List;

/**
 * 分页查询对象
 *
 * @author badqiu
 * @author hunhun
 */
public class PageQuery implements java.io.Serializable {
	private static final long serialVersionUID = -8000900575354501298L;
	/** 页数 */
	private int page;
	/** 分页大小 */
	private int pageSize;
	/** 分页排序信息 */
	private List<SortInfo> sortInfoList;

	public PageQuery() {
	}

	public PageQuery(int pageSize) {
		this.pageSize = pageSize;
	}

	public PageQuery(PageQuery query) {
		this.page = query.page;
		this.pageSize = query.pageSize;
	}

	public PageQuery(int page, int pageSize) {
		this.page = page;
		this.pageSize = pageSize;
	}

    public PageQuery(int page, int pageSize, List<SortInfo> sortInfoList) {
        this.page = page;
        this.pageSize = pageSize;
        this.sortInfoList = sortInfoList;
    }

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<SortInfo> getSortInfoList() {
		return sortInfoList;
	}

	public void setSortInfoList(List<SortInfo> sortInfoList) {
		this.sortInfoList = sortInfoList;
	}

    public int getStartIndex(){
        if(page >= 1){
            return (page-1) * pageSize;
        }
        return 0;
    }

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PageQuery");
		sb.append("{page=").append(page);
		sb.append(", pageSize=").append(pageSize);
		sb.append(", sortInfoList=").append(sortInfoList);
		sb.append('}');
		return sb.toString();
	}
}
