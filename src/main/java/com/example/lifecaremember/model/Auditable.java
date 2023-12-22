package com.example.lifecaremember.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Slf4j
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "created_date", "last_modified_date", "created_by", "last_modified_by"}, allowGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Auditable implements Serializable {

    @CreatedDate
//    @JsonProperty("created_date")
    @Column(name = "created_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    protected LocalDate createdDate;

    @LastModifiedDate
//    @JsonProperty("last_modified_date")
    @Column(name = "last_modified_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    protected LocalDate lastModifiedDate;

    @CreatedBy
    @Column(name = "created_by")
    @JsonProperty("created_by")
    protected String createdBy;

    @LastModifiedBy
    @JsonProperty("last_modified_by")
    @Column(name = "last_modified_by")
    protected String lastModifiedBy;


}
