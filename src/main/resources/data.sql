-- 插入宿舍資料
INSERT INTO dormitories (name) VALUES ('宿舍A');

-- 1樓機台
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '1F-洗衣機-01', 'WASHING', 'AVAILABLE', 1, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '1F-洗衣機-02', 'WASHING', 'IN_USE', 1, '101', 25);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '1F-洗衣機-03', 'WASHING', 'AVAILABLE', 1, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '1F-烘衣機-01', 'DRYING', 'AVAILABLE', 1, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '1F-烘衣機-02', 'DRYING', 'OUT_OF_ORDER', 1, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '1F-烘衣機-03', 'DRYING', 'IN_USE', 1, '102', 40);

-- 2樓機台
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '2F-洗衣機-01', 'WASHING', 'AVAILABLE', 2, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '2F-洗衣機-02', 'WASHING', 'IN_USE', 2, '205', 45);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '2F-洗衣機-03', 'WASHING', 'OUT_OF_ORDER', 2, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '2F-烘衣機-01', 'DRYING', 'AVAILABLE', 2, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '2F-烘衣機-02', 'DRYING', 'AVAILABLE', 2, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '2F-烘衣機-03', 'DRYING', 'IN_USE', 2, '208', 30);

-- 3樓機台
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '3F-洗衣機-01', 'WASHING', 'IN_USE', 3, '312', 15);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '3F-洗衣機-02', 'WASHING', 'AVAILABLE', 3, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '3F-洗衣機-03', 'WASHING', 'AVAILABLE', 3, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '3F-烘衣機-01', 'DRYING', 'IN_USE', 3, '315', 20);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '3F-烘衣機-02', 'DRYING', 'AVAILABLE', 3, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '3F-烘衣機-03', 'DRYING', 'OUT_OF_ORDER', 3, NULL, NULL);

-- 4樓機台
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '4F-洗衣機-01', 'WASHING', 'AVAILABLE', 4, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '4F-洗衣機-02', 'WASHING', 'AVAILABLE', 4, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '4F-洗衣機-03', 'WASHING', 'OUT_OF_ORDER', 4, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '4F-烘衣機-01', 'DRYING', 'IN_USE', 4, '403', 50);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '4F-烘衣機-02', 'DRYING', 'AVAILABLE', 4, NULL, NULL);
INSERT INTO machines (dormitory_id, name, type, status, floor, current_user_room, remaining_minutes) VALUES (1, '4F-烘衣機-03', 'DRYING', 'AVAILABLE', 4, NULL, NULL);

-- 插入使用者資料（密碼皆為 password，已用 BCrypt 加密）
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('B12345678', '張小明', '101', '$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK', 'STUDENT', true);
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('B12345679', '李小華', '102', '$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK', 'STUDENT', true);
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('B12345680', '王小美', '205', '$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK', 'STUDENT', true);
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('B12345681', '陳大同', '208', '$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK', 'STUDENT', true);
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('B12345682', '林小芳', '312', '$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK', 'STUDENT', true);
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('B12345683', '黃志明', '315', '$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK', 'STUDENT', true);
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('B12345684', '吳大偉', '403', '$2a$10$dXJ3SW6G7P7E.LB4xB1/d.rPrpvQKLOhwWKfOLDZP7gAtYLHhTMiK', 'STUDENT', true);
-- 找到類似這行的 INSERT 語句，更新密碼雜湊
INSERT INTO users (student_id, name, room_number, password_hash, role, enabled) VALUES
('ADMIN001', '系統管理員', 'ADMIN', '$2a$10$qJVabwf2vFa/3esC1pSSIeWdvQWzgbYDum8GBCxc1DiLMcmX59rtu', 'ADMIN', true);
-- ↑ 將舊的雜湊替換為你系統產生的正確雜湊

