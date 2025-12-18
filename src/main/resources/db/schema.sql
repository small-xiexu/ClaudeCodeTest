-- 图书管理系统数据库初始化脚本 (MySQL)

-- 分类表
CREATE TABLE IF NOT EXISTS category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 图书表
CREATE TABLE IF NOT EXISTS book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(50) NOT NULL,
    publisher VARCHAR(50),
    publish_date DATE,
    category_id INT,
    total_quantity INT DEFAULT 1,
    available_quantity INT DEFAULT 1,
    location VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id),
    CHECK (available_quantity >= 0),
    CHECK (available_quantity <= total_quantity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 读者表
CREATE TABLE IF NOT EXISTS reader (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    phone VARCHAR(20),
    email VARCHAR(50),
    address VARCHAR(100),
    max_borrow_count INT DEFAULT 5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 借阅记录表
CREATE TABLE IF NOT EXISTS borrow_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    reader_id INT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date DATE NOT NULL,
    return_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'BORROWED',
    fine DECIMAL(10, 2) DEFAULT 0.00,
    remarks VARCHAR(200),
    FOREIGN KEY (book_id) REFERENCES book(id),
    FOREIGN KEY (reader_id) REFERENCES reader(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引提升查询性能
CREATE INDEX idx_book_category ON book(category_id);
CREATE INDEX idx_book_isbn ON book(isbn);
CREATE INDEX idx_reader_card ON reader(card_number);
CREATE INDEX idx_borrow_book ON borrow_record(book_id);
CREATE INDEX idx_borrow_reader ON borrow_record(reader_id);
CREATE INDEX idx_borrow_status ON borrow_record(status);

-- 插入初始分类数据
INSERT IGNORE INTO category (name, description) VALUES
('文学', '小说、诗歌、散文等文学作品'),
('科技', '计算机、工程、科学等技术类书籍'),
('历史', '历史、传记、考古等历史类书籍'),
('艺术', '绘画、音乐、摄影等艺术类书籍'),
('教育', '教材、教辅、教育理论等书籍');
