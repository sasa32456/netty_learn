package com.n33.netty.learn.zerocopy.proxylearn;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 突然想学下动态代理
 *
 * @author N33
 * @date 2019/7/18
 */
public class DynamicProxy implements InvocationHandler {


    public static void main(String[] args) {
        Student student = new Student();
        DynamicProxy dynamicProxy = new DynamicProxy();
        Person person = dynamicProxy.getProxyInterface(student);
        person.setName("zhangsan");
        System.out.println(person.getName());
        System.out.println("---------------------");
        System.out.println(((Person) dynamicProxy.getProxyInterface2()).getName());
        System.out.println("---------------------");
        System.out.println(((Student)dynamicProxy.getProxyInterface3()).getName());

    }

    /**
     * 基本用法begin
     */
    private Person person = null;
    private Student student = new Student("lisi");

    public Person getProxyInterface(Student student) {
        this.person = student;
        Person person = (Person) Proxy.newProxyInstance(student.getClass().getClassLoader(), student.getClass().getInterfaces(), this);
        return person;
    }

    public Object getProxyInterface2() {
        return Proxy.newProxyInstance(student.getClass().getClassLoader(), student.getClass().getInterfaces(),
                (proxy, method, args) -> method.invoke(student, args));
    }

    public Object getProxyInterface3() {
        return Proxy.newProxyInstance(student.getClass().getClassLoader(), student.getClass().getInterfaces(),
                new DynamicProxy(){
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return ((DynamicProxy)proxy).getStudent();
                    }
                });
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(person, args);
    }

    private interface Person {
        String getName();

        Person setName(String name);
    }

    private static class Student implements Person {
        private String name;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Student setName(String name) {
            this.name = name;
            return this;
        }

        public Student() {
        }

        public Student(String name) {
            this.name = name;
        }
    }
    /**
     * 基本用法end
     */


    /**
     * proxy自调用用法begin
     */
    private interface Account {
        Account deposit(double value);

        double getBalance();
    }

    private class ExampleInvocationHandler implements InvocationHandler {
        private double balance;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("deposit".equals(method.getName())) {
                balance += (double) args[0];
                return proxy;
            }
            if ("getBalance".equals(method.getName())) {
                return balance;
            }
            return null;
        }

    }

    DynamicProxy() {
        Account account = (Account) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Account.class, Serializable.class}, new ExampleInvocationHandler());
        account.deposit(1).deposit(2).deposit(3);
        System.out.println("Balance: " + account.getBalance());
        System.out.println("------------------------------------------------------------");
    }
    /**
     * proxy自调用用法end
     */

}
