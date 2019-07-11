package com.n33.grpc;

import com.n33.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 客户端
 *
 * @author N33
 * @date 2019/7/7
 */
public class GrpcClient {
    public static void main(String[] args) throws InterruptedException {

        /**
         * 初始化连接
         */
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899).usePlaintext().build();

        //同步
        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);

        //异步
        StudentServiceGrpc.StudentServiceStub stub= StudentServiceGrpc.newStub(managedChannel);

        /**
         * 执行方法区
         */
        oneReturnOne(blockingStub);
//        oneReturnStream(blockingStub);
//        streamReturnOne(stub);
//        streamReturnStream(stub);



        /**
         * 自然关闭防止异常,同时防止双向流异步走完main方法导致JVM停止
         */
        managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }



    /**
     * 发送返回
     */
    private static void oneReturnOne(StudentServiceGrpc.StudentServiceBlockingStub blockingStub) {
        MyResponse myResponse = blockingStub.getRealNameByUsername(MyRequest.newBuilder().setUsername("zhangsan").build());
        System.out.println(myResponse.getRealname());
        System.out.println("------------------------------------");
    }


    /**
     * 发送返回流
     */
    private static void oneReturnStream(StudentServiceGrpc.StudentServiceBlockingStub blockingStub) {
        Iterator<StudentResponse> iter = blockingStub.getStudentsByAge(StudentRequest.newBuilder().setAge(20).build());
        while (iter.hasNext()) {
            StudentResponse studentResponse = iter.next();
            System.out.println(studentResponse.getName()+" , "+studentResponse.getAge()+" , "+studentResponse.getCity());
        }
        System.out.println("====================================");
    }



    /**
     * 发送流返回
     */
    private static void streamReturnOne(StudentServiceGrpc.StudentServiceStub stub) {
        StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList value) {
                value.getStudentResponseList().forEach(studentResponse -> {
                    System.out.println(studentResponse.getName());
                    System.out.println(studentResponse.getAge());
                    System.out.println(studentResponse.getCity());
                    System.out.println("****************");
                });
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("completed!");
            }
        };

        /**
         * 流一定要异步,请注意JVM是否关闭（正式不会关闭JVM）
         */
        StreamObserver<StudentRequest> studentRequestStreamObserver = stub.getStudentsWrapperByAges(studentResponseListStreamObserver);

        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(20).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(30).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(40).build());
        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(50).build());

        studentRequestStreamObserver.onCompleted();
    }



    /**
     * 双向流传递
     */
    private static void streamReturnStream(StudentServiceGrpc.StudentServiceStub stub) {
        StreamObserver<StreamRequest> requestStreamObserver = stub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse value) {
                System.out.println(value.getResponseInfo());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        });

        IntStream.range(0, 10).forEach(i->{
            requestStreamObserver.onNext(StreamRequest.newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
