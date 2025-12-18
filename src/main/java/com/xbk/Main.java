package com.xbk;

import com.xbk.entity.*;
import com.xbk.service.*;
import com.xbk.service.impl.*;
import com.xbk.util.DBUtil;

import java.sql.Date;
import java.util.List;

/**
 * 图书管理系统主程序
 * 演示核心功能：图书管理、读者管理、借还书等
 * 使用Service层进行业务逻辑处理
 *
 * @author xiexu
 * @date 2025/12/18
 */
public class Main {

    // 使用Service层而不是直接使用DAO
    private static CategoryService categoryService = new CategoryServiceImpl();
    private static BookService bookService = new BookServiceImpl();
    private static ReaderService readerService = new ReaderServiceImpl();
    private static BorrowService borrowService = new BorrowServiceImpl();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("       图书管理系统演示程序");
        System.out.println("========================================\n");

        try {
            System.out.println("[1/7] 初始化数据库...");
            DBUtil.initDatabase();
            System.out.println("✓ 数据库初始化成功\n");

            System.out.println("[2/7] 查看图书分类...");
            displayCategories();

            System.out.println("[3/7] 添加图书...");
            addBooks();

            System.out.println("[4/7] 注册读者...");
            registerReaders();

            System.out.println("[5/7] 借阅图书...");
            borrowBooks();

            System.out.println("[6/7] 查看借阅记录...");
            displayBorrowRecords();

            System.out.println("[7/7] 归还图书...");
            returnBooks();

            System.out.println("\n========================================");
            System.out.println("       图书管理系统演示完成！");
            System.out.println("========================================");

            System.out.println("\n系统功能总结：");
            System.out.println("✓ 图书分类管理");
            System.out.println("✓ 图书信息管理（增删改查）");
            System.out.println("✓ 读者信息管理");
            System.out.println("✓ 图书借阅管理");
            System.out.println("✓ 图书归还管理");
            System.out.println("✓ 借阅记录查询");
            System.out.println("\n数据库文件位置: ~/library_db.mv.db");

        } catch (Exception e) {
            System.err.println("程序执行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayCategories() {
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("系统中的图书分类：");
        for (Category category : categories) {
            System.out.println("  " + category.getId() + ". " + category.getName() +
                             " - " + category.getDescription());
        }
        System.out.println();
    }

    private static void addBooks() {
        Book book1 = new Book("978-7-115-54321-0", "Java编程思想", "Bruce Eckel");
        book1.setPublisher("人民邮电出版社");
        book1.setPublishDate(Date.valueOf("2020-01-01"));
        book1.setCategoryId(2);
        book1.setTotalQuantity(3);
        book1.setAvailableQuantity(3);
        book1.setLocation("A区-1层-001");

        Book book2 = new Book("978-7-115-43210-1", "红楼梦", "曹雪芹");
        book2.setPublisher("人民文学出版社");
        book2.setPublishDate(Date.valueOf("2018-05-01"));
        book2.setCategoryId(1);
        book2.setTotalQuantity(5);
        book2.setAvailableQuantity(5);
        book2.setLocation("B区-2层-015");

        Book book3 = new Book("978-7-115-98765-4", "中国通史", "吕思勉");
        book3.setPublisher("中华书局");
        book3.setPublishDate(Date.valueOf("2019-03-15"));
        book3.setCategoryId(3);
        book3.setTotalQuantity(2);
        book3.setAvailableQuantity(2);
        book3.setLocation("C区-1层-032");

        bookService.addBook(book1);
        bookService.addBook(book2);
        bookService.addBook(book3);

        System.out.println("✓ 图书添加完成\n");
    }

    private static void registerReaders() {
        Reader reader1 = new Reader("R20251001", "张三", "男", "13800138001", "zhangsan@example.com", "北京市朝阳区");
        Reader reader2 = new Reader("R20251002", "李四", "女", "13800138002", "lisi@example.com", "上海市浦东区");
        Reader reader3 = new Reader("R20251003", "王五", "男", "13800138003", "wangwu@example.com", "广州市天河区");

        readerService.registerReader(reader1);
        readerService.registerReader(reader2);
        readerService.registerReader(reader3);

        System.out.println("✓ 读者注册完成\n");
    }

    private static void borrowBooks() {
        System.out.println("开始借书操作（使用Service层的事务管理）：");

        // 使用BorrowService的borrowBook方法，它会自动处理：
        // 1. 验证图书和读者
        // 2. 检查库存和借书限额
        // 3. 创建借阅记录
        // 4. 更新图书数量
        // 5. 事务管理（全部成功或全部回滚）

        borrowService.borrowBook(1, 1, 30); // 张三借《Java编程思想》，30天
        borrowService.borrowBook(2, 1, 30); // 张三借《红楼梦》，30天
        borrowService.borrowBook(3, 2, 30); // 李四借《中国通史》，30天

        System.out.println();
    }

    private static void displayBorrowRecords() {
        System.out.println("查看读者张三(ID: 1)的借阅记录：");
        List<BorrowRecord> records = borrowService.getReaderBorrowHistory(1);
        for (BorrowRecord record : records) {
            Book book = bookService.getBookById(record.getBookId());
            System.out.println("  记录ID: " + record.getId() +
                             ", 图书: 《" + book.getTitle() + "》" +
                             ", 借阅日期: " + record.getBorrowDate() +
                             ", 应还日期: " + record.getDueDate() +
                             ", 状态: " + record.getStatus());
        }
        System.out.println();

        System.out.println("查询所有活跃的借阅记录：");
        List<BorrowRecord> activeRecords = borrowService.getActiveBorrows(1);
        System.out.println("  张三当前借阅图书数量: " + activeRecords.size());

        List<BorrowRecord> activeRecords2 = borrowService.getActiveBorrows(2);
        System.out.println("  李四当前借阅图书数量: " + activeRecords2.size());
        System.out.println();
    }

    private static void returnBooks() {
        System.out.println("开始还书操作（使用Service层的事务管理）：");

        // 使用BorrowService的returnBook方法，它会自动处理：
        // 1. 查询借阅记录
        // 2. 计算罚金（如果逾期）
        // 3. 更新借阅记录状态
        // 4. 更新图书可借数量
        // 5. 事务管理（全部成功或全部回滚）

        borrowService.returnBook(1); // 归还借阅记录ID为1的图书

        Book book = bookService.getBookById(1);
        System.out.println("  该书当前可借数量: " + book.getAvailableQuantity() + "/" + book.getTotalQuantity());
        System.out.println();
    }
}