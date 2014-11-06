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
public class GeoGetByXYRetVO {

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
        //详细地址描述
        private String formatted_address;
        //周围商圈
        private String business;
        private AddressComponent addressComponent;
        private Integer cityCode;

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
         * @return the formatted_address
         */
        public String getFormatted_address() {
            return formatted_address;
        }

        /**
         * @param formatted_address the formatted_address to set
         */
        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        /**
         * @return the business
         */
        public String getBusiness() {
            return business;
        }

        /**
         * @param business the business to set
         */
        public void setBusiness(String business) {
            this.business = business;
        }

        /**
         * @return the addressComponent
         */
        public AddressComponent getAddressComponent() {
            return addressComponent;
        }

        /**
         * @param addressComponent the addressComponent to set
         */
        public void setAddressComponent(AddressComponent addressComponent) {
            this.addressComponent = addressComponent;
        }

        /**
         * @return the cityCode
         */
        public Integer getCityCode() {
            return cityCode;
        }

        /**
         * @param cityCode the cityCode to set
         */
        public void setCityCode(Integer cityCode) {
            this.cityCode = cityCode;
        }

    }

    private class AddressComponent {

        private String city;
        private String district;
        private String province;
        private String street;
        private String streetNumber;

        /**
         * @return the city
         */
        public String getCity() {
            return city;
        }

        /**
         * @param city the city to set
         */
        public void setCity(String city) {
            this.city = city;
        }

        /**
         * @return the district
         */
        public String getDistrict() {
            return district;
        }

        /**
         * @param district the district to set
         */
        public void setDistrict(String district) {
            this.district = district;
        }

        /**
         * @return the province
         */
        public String getProvince() {
            return province;
        }

        /**
         * @param province the province to set
         */
        public void setProvince(String province) {
            this.province = province;
        }

        /**
         * @return the street
         */
        public String getStreet() {
            return street;
        }

        /**
         * @param street the street to set
         */
        public void setStreet(String street) {
            this.street = street;
        }

        /**
         * @return the streetNumber
         */
        public String getStreetNumber() {
            return streetNumber;
        }

        /**
         * @param streetNumber the streetNumber to set
         */
        public void setStreetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
        }
    }

}
