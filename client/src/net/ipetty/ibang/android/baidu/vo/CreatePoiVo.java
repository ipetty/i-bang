/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.baidu.vo;

/**
 *
 * @author yneos
 */
public class CreatePoiVo {

        private String ak;
        private String geotable_id;
        private Integer coord_type;

        private String bid;
        private String title;
        private String address;
        private String tags;
        private Double latitude;
        private Double longitude;

        /**
         * @return the ak
         */
        public String getAk() {
                return ak;
        }

        /**
         * @param ak the ak to set
         */
        public void setAk(String ak) {
                this.ak = ak;
        }

        /**
         * @return the geotable_id
         */
        public String getGeotable_id() {
                return geotable_id;
        }

        /**
         * @param geotable_id the geotable_id to set
         */
        public void setGeotable_id(String geotable_id) {
                this.geotable_id = geotable_id;
        }

        /**
         * @return the coord_type
         */
        public Integer getCoord_type() {
                return coord_type;
        }

        /**
         * @param coord_type the coord_type to set
         */
        public void setCoord_type(Integer coord_type) {
                this.coord_type = coord_type;
        }

        /**
         * @return the bid
         */
        public String getBid() {
                return bid;
        }

        /**
         * @param bid the bid to set
         */
        public void setBid(String bid) {
                this.bid = bid;
        }

        /**
         * @return the title
         */
        public String getTitle() {
                return title;
        }

        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
                this.title = title;
        }

        /**
         * @return the address
         */
        public String getAddress() {
                return address;
        }

        /**
         * @param address the address to set
         */
        public void setAddress(String address) {
                this.address = address;
        }

        /**
         * @return the tags
         */
        public String getTags() {
                return tags;
        }

        /**
         * @param tags the tags to set
         */
        public void setTags(String tags) {
                this.tags = tags;
        }

        /**
         * @return the latitude
         */
        public Double getLatitude() {
                return latitude;
        }

        /**
         * @param latitude the latitude to set
         */
        public void setLatitude(Double latitude) {
                this.latitude = latitude;
        }

        /**
         * @return the longitude
         */
        public Double getLongitude() {
                return longitude;
        }

        /**
         * @param longitude the longitude to set
         */
        public void setLongitude(Double longitude) {
                this.longitude = longitude;
        }

}
