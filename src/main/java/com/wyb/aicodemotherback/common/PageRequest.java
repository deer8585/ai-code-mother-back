package com.wyb.aicodemotherback.common;

import lombok.Data;

/**
 * 请求封装类
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private Long pageNum = 1L;

    /**
     * 页面大小
     */
    private Long pageSize = 10L;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}
