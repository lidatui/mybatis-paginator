package com.github.miemiedev.mybatis.paginator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 排序的列
 * @author badqiu
 * @author miemiedev
 */
public class SortInfo implements Serializable{
    private static String INJECT_STRING = " ' | and | exec | insert | select | delete | update | count | * | % | chr | mid | master | truncate | char | declare | ; | or | - | + | , ";

    private String columnName;
	private String sortStatus;
	
	public SortInfo() {
	}
	
	public SortInfo(String columnName, String sortStatus) {
		super();
		this.columnName = columnName;
		this.sortStatus = sortStatus;
	}

	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

    public String getSortStatus() {
        return sortStatus;
    }

    public void setSortStatus(String sortStatus) {
        this.sortStatus = sortStatus;
    }

    public static List<SortInfo> parseSortColumns(String sortColumns) {
		if(sortColumns == null) {
			return new ArrayList(0);
		}
		
		List<SortInfo> results = new ArrayList();
		String[] sortSegments = sortColumns.trim().split(",");
		for(int i = 0; i < sortSegments.length; i++) {
			String sortSegment = sortSegments[i];
            SortInfo sortInfo = parseSortColumn(sortSegment);
            if(sortInfo != null){
                results.add(sortInfo);
            }
		}
		return results;
	}

    public static SortInfo parseSortColumn(String sortSegment) {
        if(sortSegment == null || sortSegment.trim().equals("") || sortSegment.startsWith("null.") ||  sortSegment.startsWith(".")){
            return null;
        }

        String[] injectStrings = INJECT_STRING.split("\\|");
        for (int i=0 ; i < injectStrings.length ; i++ ){
              if (sortSegment.toLowerCase().contains(injectStrings[i])){
                  return null;
              }
        }

        String[] array = sortSegment.trim().split("\\.");
        SortInfo sortInfo = new SortInfo();
        sortInfo.setColumnName(array[0]);
        sortInfo.setSortStatus(array.length == 2 ? array[1] : "asc");
        return sortInfo;
    }
	
	public String toString() {
		return columnName + (sortStatus == null ? "" : " " + sortStatus);
	}
}
