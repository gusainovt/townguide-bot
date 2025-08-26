package io.project.townguidebot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "commands")
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "command_type")
    private CommandType commandType;

    @Column(name = "description")
    private String description;

}
