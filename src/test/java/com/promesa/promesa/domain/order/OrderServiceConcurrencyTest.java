package com.promesa.promesa.domain.order;

import com.promesa.promesa.domain.item.dao.ItemRepository;
import com.promesa.promesa.domain.item.domain.Item;
import com.promesa.promesa.domain.member.dao.MemberRepository;
import com.promesa.promesa.domain.member.domain.Member;
import com.promesa.promesa.domain.order.application.OrderService;
import com.promesa.promesa.domain.order.dto.request.OrderItemRequest;
import com.promesa.promesa.domain.order.dto.request.OrderRequest;
import com.promesa.promesa.domain.order.dto.request.PaymentRequest;
import com.promesa.promesa.domain.shippingAddress.dto.request.AddressRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.*;

/*
@SpringBootTest(properties = {
        "spring.security.oauth2.client.registration.kakao.client-id=dummy",
        "spring.security.oauth2.client.registration.kakao.client-secret=dummy",
        "spring.security.oauth2.client.registration.kakao.redirect-uri=http://localhost"
})
class OrderServiceConcurrencyTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 동시에_주문하면_재고가_음수가_되지_않는다() throws InterruptedException {
        // given
        Member member1 = memberRepository.save(Member.builder()
                .name("김회원")
                .provider("kakao")
                .providerId("test1234")
                .build());

        Member member2 = memberRepository.save(Member.builder()
                .name("정회원")
                .provider("kakao")
                .providerId("test5678")
                .build());

        Item item = itemRepository.save(Item.builder()
                .name("동시성 테스트 작품")
                .stock(1)          // 재고 1
                .price(10000)
                .productCode("TEST-CODE-001")
                .build());

        OrderRequest orderRequest = new OrderRequest(
                "SINGLE",
                List.of(new OrderItemRequest(item.getId(), 1)),
                new AddressRequest(
                        "홍길동",
                        "12345",
                        "서울시 강남구",
                        "역삼동 101호",
                        "010-1234-5678"
                ),
                new PaymentRequest(
                        "무통장입금",
                        "신한은행 123-456-789098",
                        "홍길동"
                )
        );

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            Member member = (i == 0) ? member1 : member2;
            executor.submit(() -> {
                try {
                    orderService.createOrder(orderRequest, member);
                } catch (Exception e) {
                    System.out.println("❌ 실패한 주문: " + e.getClass().getSimpleName());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Item updated = itemRepository.findById(item.getId()).get();
        System.out.println("✅ 남은 재고: " + updated.getStock());
        assertThat(updated.getStock()).isGreaterThanOrEqualTo(0);
    }

}

 */

