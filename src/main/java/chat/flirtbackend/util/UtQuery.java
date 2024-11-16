package chat.flirtbackend.util;

import chat.flirtbackend.entity.Character;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Map;

public class UtQuery {

    public static Query getItemsQueryAndSetParams(String sql, Map<String, Object> params, EntityManager entityManager) {
        Query query = entityManager.createNativeQuery(sql);
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }

    public static Query getItemsQueryAndSetParams(String sql, Map<String, Object> params, Class<?> clazz, EntityManager entityManager) {
        Query query = entityManager.createNativeQuery(sql, clazz);
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }

    public static Query getCountQueryAndSetParams(String sql, Map<String, Object> params, EntityManager entityManager) {
        Query query = entityManager.createNativeQuery("select count(*) from (" + sql + ") t");
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }
}
