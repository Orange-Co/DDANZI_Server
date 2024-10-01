package co.orange.ddanzi.service.common;

import co.orange.ddanzi.domain.order.Order;
import co.orange.ddanzi.domain.order.OrderOptionDetail;
import co.orange.ddanzi.domain.product.OptionDetail;
import co.orange.ddanzi.repository.OptionDetailRepository;
import co.orange.ddanzi.repository.OrderOptionDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderOptionDetailService {
    private final OptionDetailRepository optionDetailRepository;
    private final OrderOptionDetailRepository orderOptionDetailRepository;

    public void createOrderOptionDetail(Order order, OptionDetail optionDetail) {
        OrderOptionDetail orderOptionDetailSaved = OrderOptionDetail.builder()
                .order(order)
                .optionDetail(optionDetail)
                .build();
        orderOptionDetailRepository.save(orderOptionDetailSaved);
    }

    public OptionDetail getOptionDetailById(Long id) {
        return optionDetailRepository.findById(id).orElse(null);
    }
}
