package com.xbk.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 借书请求
 */
@Schema(description = "借书请求")
public class BorrowBookRequest {

    @Schema(description = "图书ID", example = "1", required = true)
    @NotNull(message = "图书ID不能为空")
    @Min(value = 1, message = "图书ID必须大于0")
    private Integer bookId;

    @Schema(description = "读者ID", example = "1", required = true)
    @NotNull(message = "读者ID不能为空")
    @Min(value = 1, message = "读者ID必须大于0")
    private Integer readerId;

    @Schema(description = "借阅天数", example = "30", required = true)
    @NotNull(message = "借阅天数不能为空")
    @Min(value = 1, message = "借阅天数至少为1天")
    @Max(value = 90, message = "借阅天数最多为90天")
    private Integer borrowDays;

    public BorrowBookRequest() {
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

    public Integer getBorrowDays() {
        return borrowDays;
    }

    public void setBorrowDays(Integer borrowDays) {
        this.borrowDays = borrowDays;
    }
}
