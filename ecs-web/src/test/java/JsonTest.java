import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gavin
 * @date 2019-12-18 22:22
 */
public class JsonTest {
    public static void main(String[] args) throws IOException {
        String json = "{\"bool\":{\"must\":[{\"term\":\"hello\"},{\"term\":\"world\"}]}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode boolNode = jsonNode.get("bool");
        JsonNode mustNode = boolNode.get("must");
        System.out.println(mustNode.get(0).get("term").get("value").asText());
    }
}

class StreamTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.stream().map(String::toLowerCase).sorted().collect(Collectors.toList());
        list.parallelStream().sorted().collect(Collectors.toList());
    }
}
