package myapp.fibo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import myapp.fibo.FiboTask;

import java.io.IOException;
import java.io.StringWriter;

public class JsonUtils {

    public static String taskToJson(FiboTask task) {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter swTask = new StringWriter();
        try {
            mapper.writeValue(swTask, task);
        } catch (IOException e) {
            throw new RuntimeException("Fail to convert task to json due to: " + e);
        }
        return swTask.toString();
    }

    public static FiboTask jsonToTask(String value) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(value, FiboTask.class);
    }
}
