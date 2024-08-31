package co.orange.ddanzi.repository;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderOptionDetail;
import co.orange.ddanzi.dto.common.OptionDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderOptionDetailRepository extends JpaRepository<OrderOptionDetail, Long> {
    List<OrderOptionDetail> findAllByOrder(Order order);
}
