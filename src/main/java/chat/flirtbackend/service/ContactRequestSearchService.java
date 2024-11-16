package chat.flirtbackend.service;

import chat.flirtbackend.dto.ContactRequestSearchDTO;
import chat.flirtbackend.dto.SearchResponse;
import chat.flirtbackend.entity.Character;
import chat.flirtbackend.entity.ContactRequest;
import chat.flirtbackend.util.UtPagination;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContactRequestSearchService {

    private final EntityManager entityManager;

    public ContactRequestSearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public SearchResponse<ContactRequest> search(ContactRequestSearchDTO filter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder("select * from contact_request cr where 1=1\n");
        String text = filter.getText();
        if (text != null && !text.isBlank()) {
            params.put("text", text);
            builder.append("and lower(cr.text) like concat('%', lower(:text), '%')\n");
        }
        Boolean archived = filter.getArchived();
        if (archived != null) {
            builder.append(archived ? "and cr.dt_archive is not null\n" : "and cr.dt_archive is null\n");
        }
        builder.append("order by cr.id desc\n");

        List<ContactRequest> contactRequests = getItemsQuery(builder.toString(), params)
                .setFirstResult(UtPagination.getPagination(filter.getPageNum(), filter.getPageSize()))
                .setMaxResults(filter.getPageSize())
                .getResultList();
        Long total = ((BigInteger) getCountQuery(builder.toString(), params).getSingleResult()).longValue();
        return new SearchResponse<>(contactRequests, total);
    }

    private Query getItemsQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql, ContactRequest.class);
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }

    private Query getCountQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery("select count(*) from (" + sql + ") t");
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));
        return query;
    }

}
