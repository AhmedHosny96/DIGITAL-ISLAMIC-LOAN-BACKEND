package com.sahay.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "CUSTOMER_CONTACT_MODE" , schema = "raystestdb")
public class Customer {

    @Id
    private long customer_id;
    private String contact;

    private String ident_no;


}
