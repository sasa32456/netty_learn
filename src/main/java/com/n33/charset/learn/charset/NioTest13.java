package com.n33.charset.learn.charset;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * 编码
 *
 * @author N33
 * @date 2019/5/2
 */
public class NioTest13 {
    public static void main(String[] args) throws IOException {


        String inputFile = "NioTest13_In.txt";
        String outputFile = "NioTest13_Out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");

        long inputLength = new File(inputFile).length();

        final FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        final FileChannel outputFileChannel = outputRandomAccessFile.getChannel();

        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);


//        System.out.println("========================");
//
//        Charset.availableCharsets().forEach((s, charset) -> {
//            System.out.println(s + ", " + charset);
//        });
//
//        System.out.println("========================");


        //Charset charset = Charset.forName("utf-8");
        Charset charset = Charset.forName("iso-8859-1");//为什么中文不乱?不会丢失一个字节中任何一位，因为整个文件原封不动的输出，输出文件是UTF-8,解析自然不错
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        //转码
        CharBuffer charBuffer = decoder.decode(inputData);

        System.out.println("========================");
        System.out.println(charBuffer.get(11));//换行符号
        System.out.println("========================");
        System.out.println(charBuffer.get(12));//中间过程已经乱码



        //解码
        //ByteBuffer outputData = encoder.encode(charBuffer);

        ByteBuffer outputData = Charset.forName("utf-8").encode(charBuffer);//解码换编码，出现乱码


        outputFileChannel.write(outputData);

        inputRandomAccessFile.close();
        outputRandomAccessFile.close();

    }
}
