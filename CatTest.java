import static org.junit.Assert.assertEquals;

import org.junit.*;
public class CatTest {
    @Test
    public void test_getAge()
    {
        Cat cat = new Cat(12, false);
        assertEquals(12, cat.getAge());
    }

    @Test
    public void test_getisThirsty()
    {
        Cat cat = new Cat(12, false);
        assertEquals(false, cat.getThirst());
    }
}
