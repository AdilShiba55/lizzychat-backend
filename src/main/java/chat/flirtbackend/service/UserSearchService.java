package chat.flirtbackend.service;

import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.dto.UserAdminSearchDTO;
import chat.flirtbackend.entity.User;
import chat.flirtbackend.util.UtPagination;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserSearchService {

    private final EntityManager entityManager;

    public UserSearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public SearchResponse<User> search(UserAdminSearchDTO filter) {
        StringBuilder builder = new StringBuilder("select * from usr where 1=1\n");
        Map<String, Object> params = new HashMap<>();
        String text = filter.getText();
        if(text != null && !text.isBlank()) {
            params.put("text", text);
            builder.append("and (lower(username) like concat('%', lower(:text), '%') or lower(email) like concat('%', lower(:text), '%'))\n");
        }
        Boolean isBlocked = filter.getBlocked();
        if(isBlocked != null) {
            builder.append(isBlocked ? "and dt_blocked is not null\n" : "and dt_blocked is null\n");
        }

        builder.append("order by id desc\n");

        List<User> items = getItemsQuery(builder.toString(), params)
                .setFirstResult(UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()))
                .setMaxResults(filter.getPageSize())
                .getResultList();

        Long total = ((BigInteger) getCountQuery(builder.toString(), params)
                .getSingleResult())
                .longValue();

        return new SearchResponse<>(items, total);
    }

    private Query getItemsQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql, User.class);
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }
    private Query getCountQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery("select count(*) from (" + sql + ") t");
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }
}
