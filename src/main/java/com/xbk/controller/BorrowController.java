package com.xbk.controller;

import com.xbk.dto.request.BorrowBookRequest;
import com.xbk.dto.response.BorrowRecordResponse;
import com.xbk.entity.BorrowRecord;
import com.xbk.exception.BusinessException;
import com.xbk.exception.ResourceNotFoundException;
import com.xbk.service.BorrowService;
import com.xbk.vo.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 借阅管理控制器
 */
@Tag(name = "借阅管理", description = "图书借阅相关接口")
@RestController
@RequestMapping("/borrows")
@Validated
public class BorrowController {

    private static final Logger logger = LoggerFactory.getLogger(BorrowController.class);

    @Autowired
    private BorrowService borrowService;

    @Operation(summary = "借书", description = "借阅图书")
    @PostMapping
    public ApiResponse<Void> borrowBook(@Valid @RequestBody BorrowBookRequest request) {
        logger.info("借书: 图书ID={}, 读者ID={}, 借阅天数={}", 
                request.getBookId(), request.getReaderId(), request.getBorrowDays());
        
        boolean result = borrowService.borrowBook(
                request.getBookId(), 
                request.getReaderId(), 
                request.getBorrowDays()
        );
        
        if (!result) {
            logger.error("借书失败: 图书ID={}, 读者ID={}", request.getBookId(), request.getReaderId());
            throw new BusinessException("借书失败，请检查图书库存或读者借阅权限");
        }
        
        logger.info("借书成功: 图书ID={}, 读者ID={}", request.getBookId(), request.getReaderId());
        return ApiResponse.success();
    }

    @Operation(summary = "还书", description = "归还图书")
    @PostMapping("/{id}/return")
    public ApiResponse<Void> returnBook(
            @Parameter(description = "借阅记录ID", required = true) @PathVariable @Min(value = 1, message = "借阅记录ID必须大于0") Integer id) {
        logger.info("还书: 借阅记录ID={}", id);
        
        boolean result = borrowService.returnBook(id);
        if (!result) {
            logger.error("还书失败: 借阅记录ID={}", id);
            throw new BusinessException("还书失败");
        }
        
        logger.info("还书成功: 借阅记录ID={}", id);
        return ApiResponse.success();
    }

    @Operation(summary = "查询读者借阅历史", description = "获取读者的所有借阅记录")
    @GetMapping("/reader/{readerId}/history")
    public ApiResponse<List<BorrowRecordResponse>> getReaderBorrowHistory(
            @Parameter(description = "读者ID", required = true) @PathVariable @Min(value = 1, message = "读者ID必须大于0") Integer readerId) {
        logger.info("查询读者借阅历史: 读者ID={}", readerId);
        
        List<BorrowRecord> records = borrowService.getReaderBorrowHistory(readerId);
        List<BorrowRecordResponse> responses = records.stream()
                .map(BorrowRecordResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("查询读者借阅历史成功: 读者ID={}, 共{}条记录", readerId, responses.size());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "查询读者当前借阅", description = "获取读者当前正在借阅的图书")
    @GetMapping("/reader/{readerId}/active")
    public ApiResponse<List<BorrowRecordResponse>> getActiveBorrows(
            @Parameter(description = "读者ID", required = true) @PathVariable @Min(value = 1, message = "读者ID必须大于0") Integer readerId) {
        logger.info("查询读者当前借阅: 读者ID={}", readerId);
        
        List<BorrowRecord> records = borrowService.getActiveBorrows(readerId);
        List<BorrowRecordResponse> responses = records.stream()
                .map(BorrowRecordResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("查询读者当前借阅成功: 读者ID={}, 共{}本", readerId, responses.size());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "查询逾期记录", description = "获取所有逾期未还的借阅记录")
    @GetMapping("/overdue")
    public ApiResponse<List<BorrowRecordResponse>> getOverdueRecords() {
        logger.info("查询逾期记录");
        
        List<BorrowRecord> records = borrowService.getOverdueRecords();
        List<BorrowRecordResponse> responses = records.stream()
                .map(BorrowRecordResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("查询逾期记录成功，共{}条", responses.size());
        return ApiResponse.success(responses);
    }

    @Operation(summary = "计算罚金", description = "计算指定借阅记录的罚金")
    @GetMapping("/{id}/fine")
    public ApiResponse<Map<String, Object>> calculateFine(
            @Parameter(description = "借阅记录ID", required = true) @PathVariable @Min(value = 1, message = "借阅记录ID必须大于0") Integer id) {
        logger.info("计算罚金: 借阅记录ID={}", id);
        
        List<BorrowRecord> allRecords = borrowService.getOverdueRecords();
        BorrowRecord record = null;
        for (BorrowRecord r : allRecords) {
            if (r.getId().equals(id)) {
                record = r;
                break;
            }
        }
        
        if (record == null) {
            List<BorrowRecord> historyRecords = borrowService.getReaderBorrowHistory(id);
            for (BorrowRecord r : historyRecords) {
                if (r.getId().equals(id)) {
                    record = r;
                    break;
                }
            }
        }
        
        if (record == null) {
            logger.error("借阅记录不存在: ID={}", id);
            throw new ResourceNotFoundException("借阅记录不存在: ID=" + id);
        }
        
        double fine = borrowService.calculateFine(record);
        
        Map<String, Object> result = new HashMap<>();
        result.put("recordId", id);
        result.put("fine", fine);
        result.put("status", record.getStatus());
        
        logger.info("计算罚金成功: 借阅记录ID={}, 罚金={}", id, fine);
        return ApiResponse.success(result);
    }
}
