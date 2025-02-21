package co.dhan.helper;

import co.dhan.UnitTestRoot;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;

public class ByteUtilsTest extends UnitTestRoot {

    @Test
    void testStringToBytes() {
        String input = "Hello";
        byte[] result = ByteUtils.stringToBytes(input);
        assertThat(result).isEqualTo(input.getBytes());
    }

    @Test
    void testShortToBytes() {
        short input = 1234;
        byte[] result = ByteUtils.shortToBytes(input);
        byte[] expected = ByteBuffer.allocate(2).putShort(input).array();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testIntToBytes() {
        int input = 123456;
        byte[] result = ByteUtils.intToBytes(input);
        byte[] expected = ByteBuffer.allocate(4).putInt(input).array();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testDoubleToBytes() {
        double input = 123.456;
        byte[] result = ByteUtils.doubleToBytes(input);
        byte[] expected = ByteBuffer.allocate(8).putDouble(input).array();
        assertThat(result).isEqualTo(expected);
    }
}
