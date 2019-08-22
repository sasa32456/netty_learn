package com.n33.netty.learn.handler3.mydecoder;
/**
 * 自定义协议
 *
 * @author N33
 * @date 2019/8/22
 */
public class PersonProtocol {
    private int length;

    private byte[] content;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
