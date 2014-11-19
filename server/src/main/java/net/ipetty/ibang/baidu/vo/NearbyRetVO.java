/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.baidu.vo;

/**
 *
 * @author yneos
 */
public class NearbyRetVO extends RetVO {

    private PoiVO[] contents;
    private Integer total;
    private Integer size;

    /**
     * @return the contents
     */
    public PoiVO[] getContents() {
        return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(PoiVO[] contents) {
        this.contents = contents;
    }

    /**
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

}
