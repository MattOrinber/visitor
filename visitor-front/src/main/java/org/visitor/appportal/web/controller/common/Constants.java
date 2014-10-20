package org.visitor.appportal.web.controller.common;


public class Constants {
    public static enum productMark {// 广告类型及位置
        GIF(1, "礼包"),//礼包
        NEW(2, "新品"), //新品
        FISSUE(3, "首发"), //首发
        HOT(4, "热门"),//热门
        CLASSICS(5, "精品"), //精品
        RECOMMEND(6, "推荐"); //推荐
        int key;
        String value;

        productMark(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int val() {
            return (key);
        }


    }
}
