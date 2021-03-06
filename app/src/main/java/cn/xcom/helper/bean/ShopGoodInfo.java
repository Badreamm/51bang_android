package cn.xcom.helper.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class ShopGoodInfo {


    /**
     * id : 65
     * userid : 612
     * goodsname : 丽测试201609
     * type : 21
     * price : 1
     * oprice : 8
     * unit :
     * description : 测试
     * picture : null
     * sound :
     * showid : 0
     * address : 国美
     * longitude : 119.654776553621
     * latitude : 27.0908258561152
     * status : 1
     * racking : 0
     * delivery : 同城自取
     * time : 1474681547
     * sellnumber : 1
     * name : 丽
     * phone : 13515067861
     * picturelist : [{"id":"162","goodsid":"65","file":"avatar2016:09:24:09:45:47.1990.png","time":null}]
     * commentlist : []
     */

    private String id;
    private String userid;
    private String goodsname;
    private String type;
    private String price;
    private String oprice;
    private String unit;
    private String description;
    private Object picture;
    private String sound;
    private String showid;
    private String address;
    private String longitude;
    private String latitude;
    private String status;
    private String racking;
    private String delivery;
    private String time;
    private String sellnumber;
    private String name;
    private String phone;
    /**
     * id : 162
     * goodsid : 65
     * file : avatar2016:09:24:09:45:47.1990.png
     * time : null
     */

    private List<PicturelistBean> picturelist;
    private List<?> commentlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOprice() {
        return oprice;
    }

    public void setOprice(String oprice) {
        this.oprice = oprice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getShowid() {
        return showid;
    }

    public void setShowid(String showid) {
        this.showid = showid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRacking() {
        return racking;
    }

    public void setRacking(String racking) {
        this.racking = racking;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSellnumber() {
        return sellnumber;
    }

    public void setSellnumber(String sellnumber) {
        this.sellnumber = sellnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<PicturelistBean> getPicturelist() {
        return picturelist;
    }

    public void setPicturelist(List<PicturelistBean> picturelist) {
        this.picturelist = picturelist;
    }

    public List<?> getCommentlist() {
        return commentlist;
    }

    public void setCommentlist(List<?> commentlist) {
        this.commentlist = commentlist;
    }

    public static class PicturelistBean {
        private String id;
        private String goodsid;
        private String file;
        private Object time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }
    }
}



