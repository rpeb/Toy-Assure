package com.increff.assure.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

/**
 * Used for versioning of POJOs
 */

@Getter
@Setter
@MappedSuperclass
public class AbstractPojo {
    @Version
    private Integer version;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
