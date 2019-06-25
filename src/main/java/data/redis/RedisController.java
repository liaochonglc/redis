package data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/a/{b}")
    //value是缓存名字，key不用说 但是如果b是个实体那么#b.id可以把它的id当做key值
    //value可以是多个值
    //condition是条件
    @Cacheable(value = "mycache", key = "#b", condition = "#b>0")
    public Object test(@PathVariable("b") int b) {
        return b;
    }

    @RequestMapping("/c/{c}")
    //这个注解和@Cacheable的区别是 这个每次都会查缓存不管有没有
    @CachePut(value = "mycache", key = "#c")
    public Object tes(@PathVariable("c") int c) {
        return c;
    }

    //这个注解是清除缓存，但是是方法执行成功之后触发,如果加了beforeInvocation意思是方法执行前执行清除缓存
    @CacheEvict(value = "mycache", beforeInvocation = true)
    @RequestMapping("/d/{d}")
    public void delete(@PathVariable("d") int d) {
        System.out.println("删除" + d);
    }

    //这个注解是让一个方法或者类上同时指定多个Spring Cache相关的注解。其拥有三个属性：
    // cacheable、put和evict，分别用于指定@Cacheable、@CachePut和@CacheEvict。
    @RequestMapping("/e")
    @Caching(cacheable = @Cacheable("5"), evict = { @CacheEvict("5"),
            @CacheEvict(value = "4", allEntries = true) })
    public Object find() {
        return "success";
    }
}
