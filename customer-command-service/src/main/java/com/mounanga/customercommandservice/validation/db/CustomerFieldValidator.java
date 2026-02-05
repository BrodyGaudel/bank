package com.mounanga.customercommandservice.validation.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity for validating customer field uniqueness and constraints.
 * This entity is used to ensure data integrity by validating NIP and email uniqueness
 * and format before persisting customer data.
 *
 * <p>The validation messages are provided in both French and English to support
 * internationalization and user-friendly error messages.</p>
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 * @since 1.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerFieldValidator implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank
    @Size(min = 8, max = 16)
    @Column(unique = true, nullable = false)
    private String nip;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean isDeleted = false;


}
