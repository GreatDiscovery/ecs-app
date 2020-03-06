import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author: Gavin
 * @date: 2020/3/6 18:45
 * @description:
 */
public class TextTest {
    public static void main(String[] args) {
        String text = "0.9403764231656435 0.9403764231656435 ";
        System.out.println(text.replaceAll(" ", "," ));
    }
}
