package com.n33.netty.learn.zerocopy.proxylearn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {

    public static void main(String[] args) {

//        Account account = (Account) Proxy.newProxyInstance(Account.class.getClassLoader(), new Class[]{Account.class}, (proxy, method, args1) -> {
//            int i = 0;
//            if (method.getName().equals("add")) {
//                i++;
////                return null;//当需要返回自己的时候，调用proxy
//                return proxy;
//            }
//            if (method.getName().equals("get")) {
//                return i;
//            }
//            return null;
//        });


//        Account account = (Account) Proxy.newProxyInstance(Account.class.getClassLoader(), new Class[]{Account.class}, new InvocationHandler() {
//            int num = 0;
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                if (method.getName().equals("add")) {
//                    num++;
//                    return proxy;
//                }
//                if (method.getName().equals("get")) {
//                    return num;
//                }
//                return null;
//            }
//        });

        Account account = (Account) Proxy.newProxyInstance(Account.class.getClassLoader(), new Class[]{Account.class}, new InvocationHandler() {
            int num = 0;
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              return method.invoke(new Account() {
                  @Override
                  public Account add() {
                      num++;
//                      return this;//效果一样,代理类本身
                      return (Account)proxy;
                  }

                  @Override
                  public int get() {
                      return num;
                  }
              },args);
            }
        });

        System.out.println(account.add().add().get());

    }


    private interface Account{
        Account add();
        int get();
    }




}
