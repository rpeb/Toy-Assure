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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"clientSkuId", "clientId"})})
public class ProductPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_PRODUCT, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = TableConstants.SEQ_PRODUCT)
    private Long globalSkuId;

    @Column(nullable = false)
    private String clientSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brandId;

    @Column(nullable = false)
    private Double mrp;

    @Column(nullable = false)
    private String description;
}
