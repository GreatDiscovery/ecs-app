package com.gavin.app.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 对于<T extends Comparable<? super T>>的理解
 * T和T之间，T的父类之间都可以进行比较,?占位符代表比较器的类型是T的父类
 * <T extends Comparable<T>> 只能T和T比较
 * @author: Gavin
 * @date: 2020/3/30 17:46
 * @description:
 */
public class GenericTest {
    public static void main(String[] args) {
        List<Animal> l1 = new ArrayList<>();
        l1.add(new Animal(1));
        l1.add(new Dog(2));

        List<Dog> l2 = new ArrayList<>();
        l2.add(new Dog(3));
        l2.add(new Dog(1));

        sort1(l1);
//        sort1(l2);
//        sort2(l1);
        sort2(l2);
        System.out.println(l1);
        System.out.println(l2);
    }

    public static <T extends Comparable<T>> void sort1(List<T> list) {
        Collections.sort(list);
    }

    public static <T extends Comparable<? super T>> void sort2(List<T> list) {
        Collections.sort(list);
    }
}

class Animal implements Comparable<Animal>{
    int age;

    public Animal(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Animal o) {
        return this.age - o.age;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "age=" + age +
                '}';
    }
}

class Dog extends Animal {
    public Dog(int age) {
        super(age);
    }

    @Override
    public int compareTo(Animal o) {
        return 0;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "age=" + age +
                '}';
    }
}
