import org.junit.*;

public class MyTest {
    @org.junit.Test
    public void someTest() {
        SnakeBody player = new SnakeBody("nigga");
        Assert.assertEquals(0, player.getX());
    }
}