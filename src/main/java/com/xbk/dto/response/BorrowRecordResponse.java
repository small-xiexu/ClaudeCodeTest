package com.xbk.dto.response;

import com.xbk.entity.BorrowRecord;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 借阅记录响应
 */
@Schema(description = "借阅记录响应")
public class BorrowRecordResponse {

    @Schema(description = "记录ID", example = "1")
    private Integer id;

    @Schema(description = "图书ID", example = "1")
    private Integer bookId;

    @Schema(description = "读者ID", example = "1")
    private Integer readerId;

    @Schema(description = "借阅时间", example = "2024-01-01 12:00:00")
    private Timestamp borrowDate;

    @Schema(description = "应还日期", example = "2024-01-31")
    private Date dueDate;

    @Schema(description = "归还时间", example = "2024-01-25 14:30:00")
    private Timestamp returnDate;

    @Schema(description = "状态", example = "BORROWED")
    private String status;

    @Schema(description = "罚金", example = "0.00")
    private BigDecimal fine;

    @Schema(description = "备注", example = "正常借阅")
    private String remarks;

    public BorrowRecordResponse() {
    }

    public static BorrowRecordResponse fromEntity(BorrowRecord record) {
        if (record == null) {
            return null;
        }
        BorrowRecordResponse response = new BorrowRecordResponse();
        response.setId(record.getId());
        response.setBookId(record.getBookId());
        response.setReaderId(record.getReaderId());
        response.setBorrowDate(record.getBorrowDate());
        response.setDueDate(record.getDueDate());
        response.setReturnDate(record.getReturnDate());
        response.setStatus(record.getStatus());
        response.setFine(record.getFine());
        response.setRemarks(record.getRemarks());
        return response;
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
}
