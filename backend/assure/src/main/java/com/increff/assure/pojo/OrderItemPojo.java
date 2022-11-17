package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"orderId", "globalSkuId"})})
public class OrderItemPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_ORDERITEM, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = TableConstants.SEQ_ORDERITEM)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long orderedQuantity;

    private Long allocatedQuantity;

    private Long fulfilledQuantity;

    @Column(nullable = false)
    private Double sellingPricePerUnit;
}
