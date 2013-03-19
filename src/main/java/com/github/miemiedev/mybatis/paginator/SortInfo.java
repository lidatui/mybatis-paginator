package com.github.miemiedev.mybatis.paginator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 排序的列
 * @author badqiu
 * @author miemiedev
 */
public class SortInfo implements Serializable{
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
        if(sortSegment == null || sortSegment.trim().equals("") ||
                sortSegment.startsWith("null.") ||  sortSegment.startsWith(".") ||
                isSQLInjection(sortSegment)){
            return null;
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

    private static String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";
    public static boolean isSQLInjection(String str){
        return !Pattern.matches(INJECTION_REGEX,str);
    }
}
