package com.sda;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Spying is useful for scenarios in which you don't want to mock
// many methods of a mock object. so you prefer to keep real methods(behaviors) but
// mock/spy them whenever you want
@ExtendWith(MockitoExtension.class)
class OrderAuditServiceTest {

    @Mock
    private OrderRepository repository;

    @Spy
    @InjectMocks
    private OrderAuditService spyService;

    private OrderAuditService realService; // SUT-Test Object


    @BeforeEach
    void setUp() {
        realService = new OrderAuditService(repository);
    }

    @Nested
    @DisplayName("Spying")
    class SpyingTests {

        @Test
        @DisplayName("Should demonstrate difference between spy & mock")
        void testSpyMock() {
            // given
            realService.recordAudit("TEST", "123"); // using real service
            OrderAuditService mockService = mock(OrderAuditService.class); // using mock service

            // when - with mock
            // this method does nothing, since we haven't trained our mock object what to do
            mockService.recordAudit("TEST", "123");

            // when - with spy
            spyService.recordAudit("TEST", "123");

            // then - with mock
            assertThat(mockService.getAuditLogs()).isEmpty();

            // then - with spy
            // the spy behaves just like a real object, unless you state otherwise
            assertThat(spyService.getAuditLogs()).hasSize(1);
        }

        @Test
        @DisplayName("Should allow partial mocking with spy")
        void testSpyingPartialMethods() {
            // given
            when(spyService.getFormattedTimeStamp()).thenReturn("2025-02-01T12:00:00");

            // when
            spyService.recordAudit("TEST", "123");

            //  then
            verify(spyService).recordAudit("TEST", "123");
            assertThat(spyService.getAuditLogs()).hasSize(1);
            assertThat(spyService.getAuditLogs().get(0)).startsWith("[2025-02-01T12:00:00]");
        }
    }

    @Nested
    @DisplayName("Stubbing")
    class StubbingTests {

        @Test
        @DisplayName("Should handle consecutive calls")
        void testConsecutiveCalls() {
            // given
            Order o1 = new Order("1", 100.0);
            Order o2 = new Order("2", 200.0);
            Order o3 = new Order("3", 300.0);

            when(spyService.shouldProcessOrder(any()))
                    .thenReturn(true)  // first call to shouldProcessOrder method
                    .thenReturn(false) // second call
                    .thenReturn(true); // third call

            // when
            spyService.processOrderUpdate(List.of(o1, o2, o3));

            // then
            verify(repository, times(2)).update(any());
            verify(repository, never()).update(o2);

        }

        @Test
        @DisplayName("Should demonstrate conditional responses")
        void testConditionalResponses() {
            // given
            when(spyService.shouldProcessOrder(any()))
                    .thenAnswer(invocation -> {
                        Order order = invocation.getArgument(0);
                        return order.getAmount() > 100.0;
                    });

            Order lowValueOrder = new Order("1", 50.0);
            Order highValueOrder = new Order("2", 150.0);

            // when
            spyService.processOrderUpdate(List.of(lowValueOrder, highValueOrder));

            // then
            verify(repository).update(highValueOrder);
            verify(repository, never()).update(lowValueOrder);
        }
    }

    @Nested
    @DisplayName("Argument Matchers Demonstration")
    class ArgumentMatcherTests {
        // ArgumentMatcher is a useful test tool when you want to test/verify
        // something related to your arguments

        @Test
        @DisplayName("Should use custom argument matcher")
        void testCustomArgumentMatcher() {
            // given
            ArgumentMatcher<Order> condition =
                    (order) -> order != null && order.getAmount() > 100;

            Order lowValueOrder = new Order("1", 50.0);
            Order highValueOrder = new Order("2", 150.0);

            // when
            realService.processOrderUpdate(List.of(lowValueOrder, highValueOrder));

            // then
            verify(repository).update(highValueOrder); // verifying against the exact object

            // but what if we want to verify if the object passed to update() is
            // satisfying a certain condition
            verify(repository).update(argThat(condition));
        }

        @Test
        @DisplayName("Should demonstrate Answer interface")
        void testAnswerInterface() {
            // given
            spyService.recordAudit("OLD", "1");
            spyService.recordAudit("NEW", "2");

            when(spyService.isEntryOlderThan(any(), any()))
                    .thenAnswer(invocation -> {
                        String entry = invocation.getArgument(0);
                        return entry.contains("OLD");
                    });

            // when
            int removedCount = spyService.clearOldEntries(LocalDateTime.now().minusDays(7));

            // then
            assertThat(removedCount).isEqualTo(1); // OLD audit entry will be removed
            assertThat(spyService.getAuditLogs()).hasSize(1); // NEW audit entry remains
            assertThat(spyService.getAuditLogs().get(0)).contains("NEW");
        }
    }

    @Nested
    @DisplayName("Captor tests")
    class CaptorTests {
        // Argument Captor is a useful test tool when you want
        // to get access to(or capture) the argument object itself during test time
        // for later verification

        @Test
        @DisplayName("Should capture order passed to repository update")
        void testArgumentCaptor() {
            // given
            Order order = new Order("123", 100.0);

            // when
            realService.processOrderUpdate(List.of(order));

            // then
             verify(repository).update(order);

            // now, in addition, I need to verify more things about the 'order' itself
            ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);

            verify(repository).update(orderArgumentCaptor.capture());

            Order capturedOrder = orderArgumentCaptor.getValue();

            assertThat(capturedOrder)
                    .extracting(Order::getOrderId, Order::getAmount)
                    .containsExactly("123", 100.0);

        }
    }

}
