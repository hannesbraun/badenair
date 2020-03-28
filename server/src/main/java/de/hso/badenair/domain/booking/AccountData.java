package de.hso.badenair.domain.booking;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ACCOUNT_DATA")
public class AccountData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_ACCOUNT_DATA_ID")
    @SequenceGenerator(name = "GEN_ACCOUNT_DATA", sequenceName = "SEQ_ACCOUNT_DATA")
    private Long id;

    @Column(name = "CUSTOMER_USER_ID")
    private String customerUserId;

    @Column(name = "BIRTHDAY")
    private OffsetDateTime birthday;

    @Column(name = "STREET")
    private String street;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "CARD_OWNER")
    private String cardOwner;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "CVV")
    private String cvv;

    @Column(name = "EXPIRATION_DATE")
    private OffsetDateTime expirationDate;
}
