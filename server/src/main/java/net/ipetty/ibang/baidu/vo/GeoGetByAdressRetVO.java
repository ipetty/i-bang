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
public class GeoGetByAdressRetVO {

    private String status;
    private Result result;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(Result result) {
        this.result = result;
    }

    private class Location {

        private Double lng;
        private Double lat;

        /**
         * @return the lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         * @param lng the lng to set
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

        /**
         * @return the lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         * @param lat the lat to set
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }
    }

    private class Result {

        private Location location;
        private Integer precise;
        private Integer confidence;
        private String level;

        /**
         * @return the location
         */
        public Location getLocation() {
            return location;
        }

        /**
         * @param location the location to set
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         * @return the precise
         */
        public Integer getPrecise() {
            return precise;
        }

        /**
         * @param precise the precise to set
         */
        public void setPrecise(Integer precise) {
            this.precise = precise;
        }

        /**
         * @return the confidence
         */
        public Integer getConfidence() {
            return confidence;
        }

        /**
         * @param confidence the confidence to set
         */
        public void setConfidence(Integer confidence) {
            this.confidence = confidence;
        }

        /**
         * @return the level
         */
        public String getLevel() {
            return level;
        }

        /**
         * @param level the level to set
         */
        public void setLevel(String level) {
            this.level = level;
        }
    }

}
