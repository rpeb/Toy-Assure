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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"channelSkuId","channelId","globalSkuId"})})
public class ChannelListingPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = TableConstants.SEQ_CHANNEL_LISTING, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConstants.SEQ_CHANNEL_LISTING)
    private Long id;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long globalSkuId;
}
