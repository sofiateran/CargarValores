package CargarValores.demo.users;

import CargarValores.demo.comanys.Company;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private int id;
    private String name;
    private String email;
    private int companyId;

    public static User fromJsonString(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, User.class);
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
