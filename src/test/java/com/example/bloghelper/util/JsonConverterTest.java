package com.example.bloghelper.util;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JsonConverterTest {


    @Test
    @DisplayName("JSON을 자바 객체로 변환")
    void test() {
        String targetJson = """
                    ["짜장면", "짬뽕", "닭갈비"]
                """;

        List<String> foods = JsonConverter.fromJson(targetJson, new TypeReference<List<String>>() {
        });
        System.out.println("foods = " + foods);

        String targetJson2 = """
                    {
                        "name": "삐약이",
                        "age": 3
                    }
                """;
        Pet myPet = JsonConverter.fromJson(targetJson2, new TypeReference<Pet>() {
        });
        System.out.println("myPet = " + myPet);
    }

    record Pet(String name, int age) {}


    @Test
    @DisplayName("자바객체를 JSON으로 변환")
    void test2() {
        List<String> pets = List.of("왈왈이", "먀옹이", "두식이");
        String json1 = JsonConverter.toJson(pets);
        System.out.println("json1 = " + json1);

        Pet myPet = new Pet("까꿍이", 5);
        String json2 = JsonConverter.toJson(myPet);
        System.out.println("json2 = " + json2);
    }

}