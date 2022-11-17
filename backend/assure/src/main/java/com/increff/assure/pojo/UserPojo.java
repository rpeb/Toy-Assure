package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "type"})})
public class UserPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_USER, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = TableConstants.SEQ_USER)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "ENUM('CLIENT', 'CUSTOMER')", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;
}
