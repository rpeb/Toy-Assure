package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class ChannelPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_CHANNEL, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = TableConstants.SEQ_CHANNEL)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(columnDefinition = "ENUM('SELF', 'CHANNEL')", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;
}
