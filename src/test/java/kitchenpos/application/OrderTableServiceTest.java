package kitchenpos.application;

import fixtures.OrderTableBuilder;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class OrderTableServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @DisplayName("주문 테이블 생성한다")
    @Test
    void createOrderTableTest() {

        OrderTable createdOrderTable = createdOrderTable(new OrderTableBuilder()
                .with("1번")
                .withOccupied(false)
                .build());

        assertThat(createdOrderTable).isNotNull();
        assertThat(createdOrderTable.getId()).isNotNull();
        assertThat(createdOrderTable.getName()).isEqualTo("1번");
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(0);
        assertThat(createdOrderTable.isOccupied()).isFalse();
    }

    @DisplayName("주문 테이블에 손님이 앉는다")
    @Test
    void sitTest() {

        // given

        OrderTable createdOrderTable = createdOrderTable(new OrderTableBuilder()
                .with("1번")
                .withOccupied(false)
                .build());

        // when
        OrderTable sitOrderTable = orderTableService.sit(createdOrderTable.getId());

        // then
        assertThat(sitOrderTable.isOccupied()).isTrue();
    }


    @DisplayName("주문 테이블에 손님을 비운다")
    @Test
    void clearTest() {

            // given
            OrderTable createdOrderTable = createdOrderTable(new OrderTableBuilder()
                    .with("1번")
                    .withOccupied(true)
                    .build());

            // when
            OrderTable clearOrderTable = orderTableService.clear(createdOrderTable.getId());

            // then
            assertThat(clearOrderTable.isOccupied()).isFalse();
            assertThat(clearOrderTable.getNumberOfGuests()).isEqualTo(0);
    }


    @DisplayName("주문 테이블에 손님 수를 변경한다")
    @Test
    void changeNumberOfGuestsTest() {

        // given

        OrderTable createdOrderTable = createdOrderTable(new OrderTableBuilder()
                .with("1번")
                .build());
        OrderTable sitOrderTable = orderTableService.sit(createdOrderTable.getId());

        // when
        sitOrderTable.setNumberOfGuests(4);
        OrderTable changedOrderTable = orderTableService.changeNumberOfGuests(sitOrderTable.getId(), sitOrderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }


    @DisplayName("주문테이블에 손님이 비어있다면 인원 수 변경은 불가능하다")
    @Test
    void changeNumberOfGuestsFailTest() {

        // given
        OrderTable createdOrderTable = createdOrderTable(new OrderTableBuilder()
                    .with("1번")
                    .withOccupied(false)
                    .build());

        // then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(createdOrderTable.getId(), createdOrderTable))
                .isInstanceOf(IllegalStateException.class);
    }

    private OrderTable createdOrderTable(OrderTable orderTable) {
        return orderTableService.create(orderTable);
    }
}
