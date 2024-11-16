package chat.flirtbackend.util;

public class UtPagination {

    public static Integer getPagination(Integer pageNum, Integer pageSize) {
        return (pageNum - 1) * pageSize;
    }

}
