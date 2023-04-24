package CargarValores.demo.comanys;

import CargarValores.demo.exceptions.ResourceNotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class CompanyService {
    private final StringRedisTemplate redisTemplate;

    public CompanyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(Company company) throws ResourceNotFoundException, JsonProcessingException {
        redisTemplate.opsForValue().set("company:" + company.getId(), company.toJsonString());
    }

    public List<Company> findAll() throws JsonProcessingException {
        Set<String> keys = redisTemplate.keys("company:*");
        List<Company> companies = new ArrayList<>();
        for (String key : keys) {
            String json = redisTemplate.opsForValue().get(key);
            Company company = Company.fromJsonString(json);
            companies.add(company);
        }
        return companies;
    }

    public void delete(int id) {
        redisTemplate.delete("company:" + id);
    }

    public void modify(Company company) throws JsonProcessingException {
        redisTemplate.opsForValue().set("company:" + company.getId(), company.toJsonString());
    }

    public Company getById(int id) throws JsonProcessingException {
        String json = redisTemplate.opsForValue().get("company:" + id);
        return Company.fromJsonString(json);
    }
}
