package com.shenxin.core.api.pojo.bo;

import io.vertx.core.buffer.Buffer;

import java.io.*;

/**
 * @Author: gaobaozong
 * @Description: 注册总线传输实体
 * @Date: Created in 2017/9/20 - 20:21
 * @Version: V1.0
 */
public class MessageCodec implements io.vertx.core.eventbus.MessageCodec<VertxMessage, VertxMessage> {


    @Override
    public void encodeToWire(Buffer buffer, VertxMessage requestBO) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o;
        try {
            o = new ObjectOutputStream(b);
            o.writeObject(requestBO);
            o.close();
            buffer.appendBytes(b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VertxMessage decodeFromWire(int i, Buffer buffer) {
        final ByteArrayInputStream b = new ByteArrayInputStream(buffer.getBytes());
        ObjectInputStream o = null;
        VertxMessage msg = null;
        try {
            o = new ObjectInputStream(b);
            msg = (VertxMessage) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public VertxMessage transform(VertxMessage requestBO) {
        return requestBO;
    }

    @Override
    public String name() {
        return "18911321619";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
