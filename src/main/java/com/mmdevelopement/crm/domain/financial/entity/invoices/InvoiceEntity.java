package com.mmdevelopement.crm.domain.financial.entity.invoices;

import com.mmdevelopement.crm.domain.financial.entity.enums.InvoiceDirection;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true, fluent = true)
@Entity
@Table(name = "invoice")
public class InvoiceEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "total_amount")
    private Float total;

    @Column(name = "remaining_amount")
    private Float remainingAmount;

    @Column(name = "partner_id")
    private Integer partnerId;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "direction")
    private InvoiceDirection direction;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "completed")
    private Boolean completed;

    // join elements and also have them be persisted when the invoice is saved
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(org.hibernate.annotations.FetchMode.JOIN)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private List<InvoiceElementEntity> elements;

}
