package com.xbk.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 借阅记录实体类
 */
@TableName("borrow_record")
public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("book_id")
    private Integer bookId;

    @TableField("reader_id")
    private Integer readerId;

    @TableField("borrow_date")
    private Timestamp borrowDate;

    @TableField("due_date")
    private Date dueDate;

    @TableField("return_date")
    private Timestamp returnDate;

    @TableField("status")
    private String status;

    @TableField("fine")
    private BigDecimal fine;

    @TableField("remarks")
    private String remarks;

    public static final String STATUS_BORROWED = "BORROWED";
    public static final String STATUS_RETURNED = "RETURNED";
    public static final String STATUS_OVERDUE = "OVERDUE";

    public BorrowRecord() {
    }

    public BorrowRecord(Integer bookId, Integer readerId, Date dueDate) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.dueDate = dueDate;
        this.status = STATUS_BORROWED;
        this.fine = BigDecimal.ZERO;
    }

    public BorrowRecord(Integer bookId, Integer readerId, Timestamp borrowDate, Date dueDate) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = STATUS_BORROWED;
        this.fine = BigDecimal.ZERO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    public Timestamp getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Timestamp borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", readerId=" + readerId +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status='" + status + '\'' +
                ", fine=" + fine +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
