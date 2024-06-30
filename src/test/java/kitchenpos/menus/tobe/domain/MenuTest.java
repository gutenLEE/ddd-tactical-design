package kitchenpos.menus.tobe.domain;

import kitchenpos.products.tobe.Money;
import kitchenpos.products.tobe.Name;
import kitchenpos.products.tobe.ProductPrice;
import kitchenpos.products.tobe.ProductPrices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 메뉴
 * 1 개 이상의 등록된 상품으로 메뉴를 등록할 수 있다.
 * 상품이 없으면 등록할 수 없다.
 * 메뉴에 속한 상품의 수량은 0 이상이어야 한다.
 * 메뉴의 가격이 올바르지 않으면 등록할 수 없다.
 * 메뉴의 가격은 0원 이상이어야 한다.
 * 메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.
 * 메뉴는 특정 메뉴 그룹에 속해야 한다.
 * 메뉴의 이름이 올바르지 않으면 등록할 수 없다.
 * 메뉴의 이름에는 비속어가 포함될 수 없다.
 * 메뉴의 가격을 변경할 수 있다.
 * 메뉴의 가격이 올바르지 않으면 변경할 수 없다.
 * 메뉴의 가격은 0원 이상이어야 한다.
 * 메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.
 * 메뉴를 노출할 수 있다.
 * 메뉴의 가격이 메뉴에 속한 상품 금액의 합보다 높을 경우 메뉴를 노출할 수 없다.
 * 메뉴를 숨길 수 있다.
 * 메뉴의 목록을 조회할 수 있다.
 */
class MenuTest {

    @DisplayName("메뉴 생성")
    @Test
    void createMenu_successTest() {

        assertDoesNotThrow(() -> {
            new Menu(UUID.randomUUID(), new Name("메뉴"), Money.from(1000L), new MenuGroup(), true, List.of());
        });
    }

    @DisplayName("메뉴 보이기")
    @Test
    void displaySuccessTest() {

        // given
        final var p1 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(1000));
        final var p2 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(2000));
        final var productPrices = new ProductPrices(List.of(p1, p2));

        final var menuProducts = List.of(
                new MenuProduct(p1.getProductId(), 1),
                new MenuProduct(p2.getProductId(), 1)
        );


        Menu menu = new Menu(UUID.randomUUID(), new Name("메뉴"), Money.from(1000L), new MenuGroup(), true, menuProducts);

        // when
        menu.display(productPrices);

        // then
        assertTrue(menu.isDisplayed());
    }

    @DisplayName("메뉴 보이기 실패")
    @Test
    void displayFailTest() {

        // given
        final var p1 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(1000));
        final var p2 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(2000));
        final var productPrices = new ProductPrices(List.of(p1, p2));

        final var menuProducts = List.of(
                new MenuProduct(p1.getProductId(), 1),
                new MenuProduct(p2.getProductId(), 1)
        );


        Menu menu = new Menu(UUID.randomUUID(), new Name("메뉴"), Money.from(Long.MAX_VALUE), new MenuGroup(), true, menuProducts);

        // when
        // then
        assertThatThrownBy(() -> menu.display(productPrices))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴에 포함된 상품의 가격이 상품의 가격보다 높습니다.");
    }

    @DisplayName("메뉴 숨기기")
    @Test
    void hideSuccessTest() {

        // given
        final var p1 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(1000));
        final var p2 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(2000));
        final var productPrices = new ProductPrices(List.of(p1, p2));

        final var menuProducts = List.of(
                new MenuProduct(p1.getProductId(), 1),
                new MenuProduct(p2.getProductId(), 1)
        );


        Menu menu = new Menu(UUID.randomUUID(), new Name("메뉴"), Money.from(1000L), new MenuGroup(), true, menuProducts);

        // when
        menu.hide();

        // then
        assertFalse(menu.isDisplayed());
    }

    @DisplayName("메뉴 가격 변경")
    @Test
    void changeMenuPriceSuccessTest() {

        // given
        final var p1 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(1000));
        final var p2 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(2000));
        final var productPrices = new ProductPrices(List.of(p1, p2));

        final var menuProducts = List.of(
                new MenuProduct(p1.getProductId(), 1),
                new MenuProduct(p2.getProductId(), 1)
        );


        Menu menu = new Menu(UUID.randomUUID(), new Name("메뉴"), Money.from(1000L), new MenuGroup(), true, menuProducts);

        // when
        menu.changePrice(Money.from(2000L), productPrices);

        // then
        assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(2000));
    }

    @DisplayName("변경하려는 메뉴가격이 메뉴 상품 가격보다 높으면 변경할 수 없다")
    @Test
    void changeMenuPriceFailTest() {

        // given
        final var p1 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(1000));
        final var p2 = new ProductPrice(UUID.randomUUID(), BigDecimal.valueOf(2000));
        final var productPrices = new ProductPrices(List.of(p1, p2));

        final var menuProducts = List.of(
                new MenuProduct(p1.getProductId(), 1),
                new MenuProduct(p2.getProductId(), 1)
        );

        Menu menu = new Menu(UUID.randomUUID(), new Name("메뉴"), Money.from(1000L), new MenuGroup(), true, menuProducts);

        assertThatThrownBy(() -> {
            menu.changePrice(Money.from(Long.MAX_VALUE), productPrices);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}