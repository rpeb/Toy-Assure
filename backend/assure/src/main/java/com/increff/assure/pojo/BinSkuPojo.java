package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"binId", "globalSkuId"})})
public class BinSkuPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_BINSKU, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = TableConstants.SEQ_BINSKU)
    private Long id;

    @Column(nullable = false)
    private Long binId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long quantity;
}
