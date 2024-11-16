package chat.flirtbackend.service;

import chat.flirtbackend.dto.FaqAdminSearchDTO;
import chat.flirtbackend.dto.FaqSearchDTO;
import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.entity.ContactRequest;
import chat.flirtbackend.entity.Faq;
import chat.flirtbackend.util.UtPagination;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FaqSearchService {

    private final EntityManager entityManager;

    public FaqSearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public SearchResponse<Faq> search(FaqAdminSearchDTO filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder("select * from faq where 1=1\n");
        String text = filter.getText();
        if(text != null) {
            params.put("text", text);
            builder.append("and (lower(faq.name) like concat('%', lower(:text), '%') or lower(faq.description) like concat('%', lower(:text), '%'))\n");
        }

        Boolean archived = filter.getArchived();
        if(archived != null) {
            builder.append(archived ? "and faq.dt_archive is not null\n" : "and faq.dt_archive is null\n");
        }

        builder.append("order by faq.id desc\n");
        List<Faq> contactRequests = getItemsQuery(builder.toString(), params)
                .setFirstResult(UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()))
                .setMaxResults(filter.getPageSize())
                .getResultList();
        Long total = ((BigInteger) getCountQuery(builder.toString(), params).getSingleResult()).longValue();
        return new SearchResponse<>(contactRequests, total);
    }

    public SearchResponse<Faq> search(FaqSearchDTO filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder("select * from faq where 1=1 and faq.dt_archive is null\n");
        String text = filter.getText();
        if(text != null) {
            params.put("text", text);
            builder.append("and (lower(faq.name) like concat('%', lower(:text), '%') or lower(faq.description) like concat('%', lower(:text), '%'))\n");
        }
        builder.append("order by faq.id desc\n");
        List<Faq> contactRequests = getItemsQuery(builder.toString(), params).getResultList();
        Long total = ((BigInteger) getCountQuery(builder.toString(), params).getSingleResult()).longValue();
        return new SearchResponse<>(contactRequests, total);
    }

    private Query getItemsQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql, Faq.class);
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }

    private Query getCountQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery("select count(*) from (" + sql + ") t");
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }
}
