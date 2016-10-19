import com.clue.mapper.AccountMapper;
import org.junit.Assert;

public class AccountMapperTest {
    AccountMapper mapper = new AccountMapper();

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void antiXssTest() throws Exception {
        Assert.assertEquals(mapper.antiXss("';"), "");
        Assert.assertEquals(mapper.antiXss("abc123"), "abc123");
        Assert.assertEquals(mapper.antiXss("a\"bc'1;2+3"), "abc123");
    }
}