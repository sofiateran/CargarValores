package CargarValores.demo.users;

import CargarValores.demo.comanys.Company;
import CargarValores.demo.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private final StringRedisTemplate redisTemplate;

    public void save(User user) throws ResourceNotFoundException, JsonProcessingException {
        // Extraer valores necesarios de los campos del usuario
        var userId = user.getId();
        var displayName = user.getName();
        var email = user.getEmail();
        var companyId = user.getCompanyId();

        // Crear los comandos Redis que deseas ejecutar
        List<Object> redisCommands = new ArrayList<>();
        redisCommands.add("SADD 0.1." + userId + ":val \"1..1.:@" + userId + "|key0\"");
        redisCommands.add("SADD 0.1." + userId + ":val \"1..1.:@" + displayName + "|name|0\"");
        redisCommands.add("SADD 0.1." + userId + ":val \"1..1.:@" + email + "|email|0\"");
        redisCommands.add("SADD 0.1." + userId + ":val \"1..1.:@" + companyId + "\"");
        redisCommands.add("ZADD 0.@displayName.sidx 0 " + displayName + ":1..1.:0.1." + userId + "\"");
        redisCommands.add("ZADD 0.@email.sidx 0 " + email + ":1..1.:0.1." + userId + "\"");
        redisCommands.add("ZADD 0.@userId.sidx 0 \"0.1.\" + userId+\":1..1.:0.1.\" + userId+\"\"");
        redisCommands.add("SET 0.1.\" + userId+\" \"1..1.\"");
        redisCommands.add("SADD 0.1.\" + userId+\":ocfg \"1..1.:user\"");

        // Ejecutar los comandos Redis utilizando la transacción
        redisTemplate.execute(new SessionCallback<Object>() {
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch("user:" + user.getId()); // Verificar que no ha cambiado el valor en Redis

                operations.multi(); // Iniciar transacción

                // Ejecutar comandos Redis dentro de la transacción
                for (Object command : redisCommands) {
                    operations.execute((RedisCallback<Object>) conn -> conn.eval((byte[]) command, ReturnType.STATUS, 0));
                }

                operations.exec(); // Ejecutar transacción

                return null;
            }
        });

        // Guardar el objeto User en Redis
        redisTemplate.opsForValue().set("user:" + user.getId(), user.toJsonString());
    }

    public List<User> findAll() throws JsonProcessingException{
        Set<String> keys = redisTemplate.keys("user:*");
        List<User> users = new ArrayList<>();
        for (String key : keys) {
            String json = redisTemplate.opsForValue().get(key);
            User user = User.fromJsonString(json);
            users.add(user);
        }
        return users;
    }

    public void delete(int id) {
        redisTemplate.delete("user:" + id);
    }

    public void modify(User user) throws JsonProcessingException {
        redisTemplate.opsForValue().set("user:" + user.getId(), user.toJsonString());
    }

    public User getById(int id) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get("user:" + id);
        return User.fromJsonString(json);
    }

}