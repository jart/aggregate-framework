package org.aggregateframework.test.command.domain.entity;

import org.aggregateframework.entity.AbstractSimpleAggregateRoot;
import org.aggregateframework.spring.entity.DaoAwareQuery;
import org.aggregateframework.test.command.domainevents.OrderCreatedEvent;
import org.aggregateframework.test.command.domainevents.OrderUpdatedEvent;
import org.aggregateframework.test.command.domainevents.SeatAvailabilityRemovedEvent;

import java.util.ArrayList;
import java.util.List;

public class Order extends AbstractSimpleAggregateRoot<UserShardingId> {

    private static final long serialVersionUID = -1431035454011931259L;
    private String content;

    @DaoAwareQuery(mappedBy = "order", select = "findByOrderId")
    private List<SeatAvailability> seatAvailabilities = new ArrayList<SeatAvailability>();

    private Payment payment;

    private boolean recovered;

    public Order() {
        apply(new OrderCreatedEvent(this));
    }

    @Override
    public UserShardingId getId() {
        return super.getId();
    }

    @Override
    public void setId(UserShardingId userShardingId) {
        super.setId(userShardingId);
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
        this.apply(new OrderUpdatedEvent(this.getId(), content));
    }

    public void removeSeatAvailabilities() {
        seatAvailabilities.clear();
        this.apply(new SeatAvailabilityRemovedEvent(this));
    }

    public Payment getPayment() {
        return payment;
    }

    public void updatePayment(Payment payment) {
        this.payment = payment;
    }


    public List<SeatAvailability> getSeatAvailabilities() {
        return seatAvailabilities;
    }

    public void addSeatAvailability(SeatAvailability seatAvailability) {

        seatAvailability.setOrder(this);
        seatAvailabilities.add(seatAvailability);
    }

    public void removeSeatAvailability(SeatAvailability seatAvailability) {
        seatAvailabilities.remove(seatAvailability);
    }


    public void recovered() {
        recovered = true;
    }

    public boolean isRecovered() {
        return recovered;
    }
}
