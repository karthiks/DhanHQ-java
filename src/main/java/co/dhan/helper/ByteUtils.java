package co.dhan.helper;

import java.nio.ByteBuffer;

public class ByteUtils {

    private static byte[] stringToBytes(String value) {
        return value.getBytes();
    }

    private static byte[] shortToBytes(short value) {
        return ByteBuffer
                .allocate(2)
                .putShort(value)
                .array();
    }

    private static byte[] intToBytes(int value) {
        return ByteBuffer
                .allocate(4)
                .putInt(value)
                .array();
    }

    public static byte[] doubleToBytes(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(8); // 8 bytes for a double
        buffer.putDouble(value);
        return buffer.array();
    }
}
