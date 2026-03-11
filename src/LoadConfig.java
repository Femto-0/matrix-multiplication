import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class LoadConfig {

    public static AppConfig loadConfig() throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        return objectMapper.readValue(new File("src/config.json"), AppConfig.class);
    }
}
