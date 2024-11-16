package chat.flirtbackend.repository;

import chat.flirtbackend.entity.MyPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<MyPayment, Long> {
    @Query(value = "select * from payment where payment_id = :paymentId and source_id = :paymentSourceId", nativeQuery = true)
    Optional<MyPayment> findByPaymentIdAndSourceId(String paymentId, Long paymentSourceId);

}
