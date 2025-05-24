-- 사용자
INSERT INTO member (member_id)
VALUES
    (1),  -- 김작가
    (2),  -- 소비자
    (3);  -- 박작가

-- 작가
INSERT INTO artist (artist_id, description, insta, name, profile_url, wish_count, member_id)
VALUES
    (1, '임시 작가입니다', 'good_artist', '김작가', 'https://example.com/profile.jpg', 0, 1),
    (2, '유명 도예가', 'pottery_queen', '박작가', 'https://example.com/profile2.jpg', 0, 3);

-- 작품
INSERT INTO item (
    item_id, created_at, updated_at, description, name, price, sale_status, stock, wish_count, artist_id
)
VALUES
    (1,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'핸드메이드 컵','도자기 컵',10000,'ON_SALE',10,0,1),
    (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '세련된 접시', '도자기 접시', 15000, 'ON_SALE', 5, 0, 2),
    (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '빈티지 느낌의 화병', '도자기 화병', 20000, 'ON_SALE', 7, 0, 1);

-- 작품 이미지
INSERT INTO item_image (item_image_id, image_url, is_thumbnail, item_id)
VALUES
    (1,'https://example.com/image.jpg',TRUE,1),
    (2, 'https://example.com/image2.jpg', TRUE, 2),
    (3, 'https://example.com/image3.jpg', TRUE, 3);

-- 위시리스트
INSERT INTO wish (wish_id, target_id, target_type, member_id)
VALUES
    (1,1,'ITEM',2),
    (2, 2, 'ITEM', 2);

-- 기획전
INSERT INTO exhibition (exhibition_id, created_at, updated_at, description, title)
VALUES (1, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'따뜻한 봄의 작품들','봄 기획전');

-- 기획전-작품 연결
INSERT INTO exhibition_item (exhibition_item_id, exhibition_id, item_id)
VALUES
    (1,1,1),
    (2,1,2),
    (3,1,3);