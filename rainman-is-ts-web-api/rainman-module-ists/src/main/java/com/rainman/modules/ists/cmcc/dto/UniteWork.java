package com.rainman.modules.ists.cmcc.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Title: UniteWork
 * Description: 统一待办webservices 结构体
 *
 * @author 李京泽
 * @date 2021年12月7日
 */
@XStreamAlias("UniteWork")
public class UniteWork {
    @XStreamAlias("UniteWorkItems")
    private List<UniteWorkItem> uniteWorkItems;

    public UniteWork(List<UniteWorkItem> uniteWorkItems) {
        this.uniteWorkItems = uniteWorkItems;
    }

    public UniteWork() {
    }

    public List<UniteWorkItem> getUniteWorkItems() {
        return uniteWorkItems;
    }

    public void setUniteWorkItems(List<UniteWorkItem> uniteWorkItems) {
        this.uniteWorkItems = uniteWorkItems;
    }
}
