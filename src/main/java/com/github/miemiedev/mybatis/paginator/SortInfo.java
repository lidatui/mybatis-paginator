package com.github.miemiedev.mybatis.paginator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(SortInfo.class);
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
		return parseSortColumns(sortColumns, null);
	}

    public static List<SortInfo> parseSortColumns(String sortColumns, String sortExpression){
        if(sortColumns == null) {
            return new ArrayList(0);
        }

        List<SortInfo> results = new ArrayList();
        String[] sortSegments = sortColumns.trim().split(",");
        for(int i = 0; i < sortSegments.length; i++) {
            String sortSegment = sortSegments[i];
            SortInfo sortInfo = parseSortColumn(sortSegment, sortExpression);
            if(sortInfo != null){
                results.add(sortInfo);
            }
        }
        return results;
    }

    public static SortInfo parseSortColumn(String sortSegment) {
        return parseSortColumn(sortSegment, null);
    }

    /**
     *
     * @param sortSegment  str "id.asc" or "code.desc"
     * @param sortExpression  placeholder is "?", in oracle like: "nlssort( ? ,'NLS_SORT=SCHINESE_PINYIN_M')".
     *                        Warning: you must prevent SQL injection.
     * @return
     */
    public static SortInfo parseSortColumn(String sortSegment, String sortExpression){


        if(sortSegment == null || sortSegment.trim().equals("") ||
                sortSegment.startsWith("null.") ||  sortSegment.startsWith(".")){
            logger.warn("Could not parse SortInfo from {} string.", sortSegment);
            return null;
        }

        String[] array = sortSegment.trim().split("\\.");
        if(isSQLInjection(array[0]) || isSQLInjection(array[1])){
            logger.warn("SQLInjection ? -> {} .", sortSegment);
            return null;
        }

        SortInfo sortInfo = new SortInfo();

        if(sortExpression != null && sortExpression.indexOf("?") != -1){
            sortExpression = sortExpression.replaceAll("\\?","%s");
            array[0] = String.format(sortExpression,array[0]);
        }
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
