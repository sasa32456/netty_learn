package com.n33.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 测试生成文件
 *
 * @author N33
 * @date 2019/6/25
 */
public class ProtoBufTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setName("张三")
                .setAge(20)
                .setAddress("北京")
                .build();

        byte[] student2ByteArray = student.toByteArray();

        DataInfo.Student student2 = DataInfo.Student.parseFrom(student2ByteArray);

        //产生转义，调用toString {TextFormat.printToString(this);}导致
        System.out.println(student2);

        System.out.println("================");

        System.out.println(student.getName());
        System.out.println(student.getAge());
        System.out.println(student.getAddress());

    }
}
