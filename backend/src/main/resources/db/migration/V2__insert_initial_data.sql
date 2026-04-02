-- 插入初始用户数据
-- 密码都是: 123456 (BCrypt加密后的值)
INSERT INTO users (username, password, real_name, email, phone, role, enabled) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '超级管理员', 'admin@projmaster.com', '13800000000', 'SUPER_ADMIN', TRUE),
('pm1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '项目经理1', 'pm1@projmaster.com', '13800000001', 'PROJECT_MANAGER', TRUE),
('po1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '产品经理1', 'po1@projmaster.com', '13800000002', 'PRODUCT_OWNER', TRUE),
('dev1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '开发工程师1', 'dev1@projmaster.com', '13800000003', 'MEMBER', TRUE),
('dev2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '开发工程师2', 'dev2@projmaster.com', '13800000004', 'MEMBER', TRUE),
('test1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '测试工程师1', 'test1@projmaster.com', '13800000005', 'MEMBER', TRUE),
('test2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E.', '测试工程师2', 'test2@projmaster.com', '13800000006', 'MEMBER', TRUE);

-- 插入示例项目
INSERT INTO projects (name, description, type, start_date, end_date, owner_id, status, visibility, archived, deleted) VALUES
('ProjMaster 项目管理系统', '内部项目管理工具开发', 'SCRUM', '2026-01-01', '2026-12-31', 1, 'ACTIVE', 'PRIVATE', FALSE, FALSE),
('电商平台重构', '电商平台技术架构升级项目', 'SCRUM', '2026-02-01', '2026-08-31', 2, 'ACTIVE', 'PRIVATE', FALSE, FALSE),
('移动端APP开发', 'iOS和Android双端APP开发', 'KANBAN', '2026-03-01', '2026-09-30', 3, 'ACTIVE', 'PRIVATE', FALSE, FALSE);

-- 插入项目成员
INSERT INTO project_members (project_id, user_id, role) VALUES
(1, 1, 'MANAGER'),
(1, 2, 'MANAGER'),
(1, 3, 'PRODUCT'),
(1, 4, 'DEVELOPER'),
(1, 5, 'DEVELOPER'),
(1, 6, 'TESTER'),
(1, 7, 'TESTER'),
(2, 2, 'MANAGER'),
(2, 3, 'PRODUCT'),
(2, 4, 'DEVELOPER'),
(2, 6, 'TESTER'),
(3, 3, 'MANAGER'),
(3, 5, 'DEVELOPER'),
(3, 7, 'TESTER');
