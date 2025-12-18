package com.xbk.controller;

import com.xbk.dto.request.ReaderRegisterRequest;
import com.xbk.dto.request.ReaderUpdateRequest;
import com.xbk.dto.response.ReaderResponse;
import com.xbk.entity.Reader;
import com.xbk.exception.BusinessException;
import com.xbk.exception.ResourceNotFoundException;
import com.xbk.service.ReaderService;
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
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 读者管理控制器
 */
@Tag(name = "读者管理", description = "读者相关接口")
@RestController
@RequestMapping("/readers")
@Validated
public class ReaderController {

    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

    @Autowired
    private ReaderService readerService;

    @Operation(summary = "注册读者", description = "创建新的读者账号")
    @PostMapping
    public ApiResponse<ReaderResponse> registerReader(@Valid @RequestBody ReaderRegisterRequest request) {
        logger.info("注册读者: {}", request.getName());
        
        Reader reader = new Reader();
        reader.setCardNumber(request.getCardNumber());
        reader.setName(request.getName());
        reader.setGender(request.getGender());
        reader.setPhone(request.getPhone());
        reader.setEmail(request.getEmail());
        reader.setAddress(request.getAddress());
        reader.setMaxBorrowCount(5);
        
        boolean result = readerService.registerReader(reader);
        if (!result) {
            logger.error("注册读者失败: {}", request.getName());
            throw new BusinessException("注册读者失败");
        }
        
        logger.info("注册读者成功: {} (ID: {})", request.getName(), reader.getId());
        return ApiResponse.success(ReaderResponse.fromEntity(reader));
    }

    @Operation(summary = "更新读者信息", description = "更新已有读者信息")
    @PutMapping("/{id}")
    public ApiResponse<ReaderResponse> updateReader(
            @Parameter(description = "读者ID", required = true) @PathVariable @Min(value = 1, message = "读者ID必须大于0") Integer id,
            @Valid @RequestBody ReaderUpdateRequest request) {
        logger.info("更新读者信息: ID={}", id);
        
        Reader existingReader = readerService.getReaderById(id);
        if (existingReader == null) {
            logger.error("读者不存在: ID={}", id);
            throw new ResourceNotFoundException("读者不存在: ID=" + id);
        }
        
        Reader reader = new Reader();
        reader.setId(id);
        if (request.getCardNumber() != null) {
            reader.setCardNumber(request.getCardNumber());
        }
        if (request.getName() != null) {
            reader.setName(request.getName());
        }
        if (request.getGender() != null) {
            reader.setGender(request.getGender());
        }
        if (request.getPhone() != null) {
            reader.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            reader.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            reader.setAddress(request.getAddress());
        }
        
        boolean result = readerService.updateReader(reader);
        if (!result) {
            logger.error("更新读者信息失败: ID={}", id);
            throw new BusinessException("更新读者信息失败");
        }
        
        Reader updatedReader = readerService.getReaderById(id);
        logger.info("更新读者信息成功: ID={}", id);
        return ApiResponse.success(ReaderResponse.fromEntity(updatedReader));
    }

    @Operation(summary = "删除读者", description = "删除指定的读者")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReader(
            @Parameter(description = "读者ID", required = true) @PathVariable @Min(value = 1, message = "读者ID必须大于0") Integer id) {
        logger.info("删除读者: ID={}", id);
        
        Reader existingReader = readerService.getReaderById(id);
        if (existingReader == null) {
            logger.error("读者不存在: ID={}", id);
            throw new ResourceNotFoundException("读者不存在: ID=" + id);
        }
        
        boolean result = readerService.deleteReader(id);
        if (!result) {
            logger.error("删除读者失败: ID={}", id);
            throw new BusinessException("删除读者失败");
        }
        
        logger.info("删除读者成功: ID={}", id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取读者详情", description = "根据ID获取读者详细信息")
    @GetMapping("/{id}")
    public ApiResponse<ReaderResponse> getReaderById(
            @Parameter(description = "读者ID", required = true) @PathVariable @Min(value = 1, message = "读者ID必须大于0") Integer id) {
        logger.info("获取读者详情: ID={}", id);
        
        Reader reader = readerService.getReaderById(id);
        if (reader == null) {
            logger.error("读者不存在: ID={}", id);
            throw new ResourceNotFoundException("读者不存在: ID=" + id);
        }
        
        logger.info("获取读者详情成功: ID={}", id);
        return ApiResponse.success(ReaderResponse.fromEntity(reader));
    }

    @Operation(summary = "根据借书证号查询读者", description = "通过借书证号查询读者信息")
    @GetMapping("/card/{cardNumber}")
    public ApiResponse<ReaderResponse> getReaderByCardNumber(
            @Parameter(description = "借书证号", required = true) @PathVariable @NotBlank(message = "借书证号不能为空") String cardNumber) {
        logger.info("根据借书证号查询读者: {}", cardNumber);
        
        Reader reader = readerService.getReaderByCardNumber(cardNumber);
        if (reader == null) {
            logger.error("读者不存在: 借书证号={}", cardNumber);
            throw new ResourceNotFoundException("读者不存在: 借书证号=" + cardNumber);
        }
        
        logger.info("根据借书证号查询读者成功: {}", cardNumber);
        return ApiResponse.success(ReaderResponse.fromEntity(reader));
    }

    @Operation(summary = "获取所有读者", description = "获取所有读者列表")
    @GetMapping
    public ApiResponse<List<ReaderResponse>> getAllReaders() {
        logger.info("获取所有读者");
        
        List<Reader> readers = readerService.getAllReaders();
        List<ReaderResponse> responses = readers.stream()
                .map(ReaderResponse::fromEntity)
                .collect(Collectors.toList());
        
        logger.info("获取所有读者成功，共{}人", responses.size());
        return ApiResponse.success(responses);
    }
}
