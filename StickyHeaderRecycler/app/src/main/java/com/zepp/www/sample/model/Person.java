package com.zepp.www.sample.model;

/**
 * Created by xubinggui on 5/23/16.
 * //                            _ooOoo_
 * //                           o8888888o
 * //                           88" . "88
 * //                           (| -_- |)
 * //                            O\ = /O
 * //                        ____/`---'\____
 * //                      .   ' \\| |// `.
 * //                       / \\||| : |||// \
 * //                     / _||||| -:- |||||- \
 * //                       | | \\\ - /// | |
 * //                     | \_| ''\---/'' | |
 * //                      \ .-\__ `-` ___/-. /
 * //                   ___`. .' /--.--\ `. . __
 * //                ."" '< `.___\_<|>_/___.' >'"".
 * //               | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * //                 \ \ `-. \_ __\ /__ _/ .-` / /
 * //         ======`-.____`-.___\_____/___.-`____.-'======
 * //                            `=---='
 * //
 * //         .............................................
 * //                  佛祖镇楼                  BUG辟易
 */
public class Person {
    public static class Name {
        public String title;
        public String first;
        public String last;
    }

    public static class Location {
        public String street;
        public String city;
        public String state;
        public String postcode;
    }

    public static class Picture {
        public String large;
        public String medium;
        public String thumbnail;
    }

    public Name name;
    public Location location;
    public String email;
    public String phone;
    public String cell;
    public Picture picture;

    public Person() {
    }
}
