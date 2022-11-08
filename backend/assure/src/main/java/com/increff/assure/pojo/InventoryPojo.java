package com.increff.assure.pojo;

import com.increff.assure.model.TableConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"globalSkuId"})})
public class InventoryPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_INVENTORY, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = TableConstants.SEQ_INVENTORY)
    private Long id;
    @Column(nullable = false)
    private Long globalSkuId;
    @Column(nullable = false)
    private Long availableQuantity;
    private Long allocatedQuantity;
    private Long fulfilledQuantity;
}
