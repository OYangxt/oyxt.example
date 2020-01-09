package com.oyxt.example.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.oyxt.example.mode.Student;
import org.junit.Test;

import java.util.Map;

/**
 * @author 20190712713
 * @date 2019/12/30 20:34
 */
public class JsonTest {

    @Test
    public void testJsonToString(){
       Student student = new Student("张三", 12);
       System.out.println(student);
       String jsonString = JSON.toJSONString(student);

       System.out.println(jsonString);
    }

    @Test
    public void testJsonStringToObject() {
        TypeReference<Student> studentTypeReference = new TypeReference<Student>(){};
        Student student = new Student("张三", 12);

        String jsonString = JSON.toJSONString(student);
        Student student1 = JSON.parseObject(jsonString, studentTypeReference);
        System.out.println(student1);
    }


    @Test
    public  void testJsonStringToMap() {
        TypeReference<Map<String,String>> typeReference = new TypeReference<Map<String, String>>(){};
        Student student = new Student("张三", 12);

        String jsonString = JSON.toJSONString(student);
        Map<String, String> stringStringMap = JSON.parseObject(jsonString, typeReference);
        System.out.println(stringStringMap);
        System.out.println(stringStringMap.get("name"));
    }

    @Test
    public void testJSONObject() {
        String content = "";
        JSONObject jsonObject = JSON.parseObject(content);
        System.out.println(jsonObject);
    }
}
