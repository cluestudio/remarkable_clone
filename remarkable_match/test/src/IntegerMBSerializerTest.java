import com.clue.util.IntegerMBSerializer;
import org.junit.Assert;

public class IntegerMBSerializerTest {
    @org.junit.Test
    public void lengthEncodingDecoding() throws Exception {
        byte [] bytes127 = IntegerMBSerializer.encodeLength(127);
        byte [] bytes128 = IntegerMBSerializer.encodeLength(128);
        byte [] bytes2047 = IntegerMBSerializer.encodeLength(2047);
        byte [] bytes2048 = IntegerMBSerializer.encodeLength(2048);
        byte [] bytes65535 = IntegerMBSerializer.encodeLength(65535);

        Assert.assertEquals(127, IntegerMBSerializer.decodeLength(bytes127));
        Assert.assertEquals(128, IntegerMBSerializer.decodeLength(bytes128));
        Assert.assertEquals(2047, IntegerMBSerializer.decodeLength(bytes2047));
        Assert.assertEquals(2048, IntegerMBSerializer.decodeLength(bytes2048));
        Assert.assertEquals(65535, IntegerMBSerializer.decodeLength(bytes65535));
    }
}