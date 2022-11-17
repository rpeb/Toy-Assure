package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"clientId", "channelId", "channelOrderId"})})
public class OrderPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_ORDER, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = TableConstants.SEQ_ORDER)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false, unique = true)
    private String channelOrderId;

    @Column(columnDefinition = "ENUM('CREATED', 'ALLOCATED', 'FULFILLED')", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String invoiceUrl;
}
