package com.example.lifecaremember.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(value = {"created_by", "created_date", "last_modified_by", "last_modified_date"}, allowGetters = true)
public class BaseDTO<T> {
//    @JsonProperty("created_by")
//    public T createdBy;

    public long createdDate;

//    @JsonProperty("last_modified_by")
//    public T lastModifiedBy;

    public long lastModifiedDate;

}
