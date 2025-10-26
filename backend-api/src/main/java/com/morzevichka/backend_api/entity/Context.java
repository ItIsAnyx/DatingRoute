package com.morzevichka.backend_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contexts")
public class Context {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", insertable = false, updatable = false)
    private Long chatId;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private Chat chat;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<ContextJson> context;
}