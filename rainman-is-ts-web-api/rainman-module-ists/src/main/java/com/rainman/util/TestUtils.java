package com.rainman.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


/**
 * @author XieZhenhao
 * @version 1.0
 * @date 2021/12/1 16:09
 */
public class TestUtils {
    protected   int a=0;

    public void test(){

        List<String> test = new ArrayList<>();
        test.add("abc");
        test.add("cccc");
        test.add("abc");
        test.add("cccc");
        test.add("abc");
        test.add("cccc");
        long temp=test.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                if(s.contains("a")){
                    return true;
                }
                return false;
            }
        }).count();
        System.out.println(temp);
    }

    public static void main(String[] args) {
        System.out.println("啊".length());
    }
}
