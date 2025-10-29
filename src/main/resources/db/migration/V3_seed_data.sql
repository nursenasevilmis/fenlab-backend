-- Test verileri (Development için)
-- ===========================

-- Şifreler BCrypt ile hash'lenmiş: "password123"
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- Test kullanıcıları ekle
INSERT INTO users (id,username, full_name, email, password, role, branch, experience_years, bio, is_public, created_at) VALUES
(1,'ahmet_ogretmen', 'Ahmet Yılmaz', 'ahmet.ogretmen@fenlab.com', 'pass123', 'TEACHER', 'Fizik', 10, 'Fizik öğretmeniyim. Deneyleri seviyorum.', true, CURRENT_TIMESTAMP),
(2,'ayse_ogretmen', 'Ayşe Demir', 'ayse.ogretmen@fenlab.com', 'ayse1234', 'TEACHER', 'Kimya', 8, 'Kimya öğretmeniyim.', true, CURRENT_TIMESTAMP),
(3,'mehmet_ogrenci', 'Mehmet Kaya', 'mehmet.ogrenci@fenlab.com', 'mehmet4545', 'USER', NULL, NULL, 'Fen bilimlerine meraklı bir öğrenciyim.', true, CURRENT_TIMESTAMP),
(4,'zeynep_ogrenci', 'Zeynep Şahin', 'zeynep.ogrenci@fenlab.com', 'zeynep7878', 'USER', NULL, NULL, NULL, true, CURRENT_TIMESTAMP);

-- Test deneyleri ekle
INSERT INTO experiments (id,user_id, title, description, grade_level, subject, difficulty, expected_result, safety_notes, is_published, created_at) VALUES
(1,1, 'Basit Elektrik Devresi', 'Bu deneyde basit bir elektrik devresi kuracağız. Pil, ampul ve kablolar kullanarak elektriğin nasıl çalıştığını gözlemleyeceğiz.', 7, 'Fizik', 'EASY', 'Ampul yanacak ve elektrik akımının devreyi nasıl tamamladığını göreceğiz.', 'Pilleri ters bağlamayın. Yüksek voltajlı pillerle çalışmayın.', true, CURRENT_TIMESTAMP),
(2,1, 'Su Döngüsü Modeli', 'Su döngüsünü gözlemlemek için basit bir model oluşturacağız. Buharlaşma, yoğunlaşma ve yağış süreçlerini göreceğiz.', 5, 'Fen Bilgisi', 'MEDIUM', 'Suyun farklı hallerini ve döngüsünü gözlemleyeceğiz.', 'Sıcak su kullanırken dikkatli olun.', true, CURRENT_TIMESTAMP),
(3,2, 'Asit-Baz Göstergesi', 'Kırmızı lahana kullanarak doğal bir pH göstergesi yapacağız ve farklı maddelerin asit-baz özelliklerini test edeceğiz.', 8, 'Kimya', 'MEDIUM', 'Farklı maddeler farklı renkler gösterecek.', 'Kimyasal maddelerle çalışırken eldiven kullanın. Gözlerinizi koruyun.', true, CURRENT_TIMESTAMP);

-- Deney malzemeleri ekle
INSERT INTO experiment_materials (experiment_id, material_name, quantity) VALUES
(1, 'AA Pil', '2 adet'),
(1, 'Ampul (3V)', '1 adet'),
(1, 'Kablo', '3 adet'),
(1, 'Anahtar', '1 adet'),
(2, 'Plastik kap', '1 adet'),
(2, 'Sıcak su', '200ml'),
(2, 'Buz küpleri', '5-6 adet'),
(2, 'Streç film', '1 adet'),
(3, 'Kırmızı lahana', '100g'),
(3, 'Su', '500ml'),
(3, 'Test tüpleri', '5 adet'),
(3, 'Sirke', '50ml'),
(3, 'Karbonat', '1 çay kaşığı');

-- Deney adımları ekle
INSERT INTO experiment_steps (experiment_id, step_order, step_text) VALUES
(1, 1, 'Pilleri seri bağlayın. Bir pilin (+) ucunu diğer pilin (-) ucuna bağlayın.'),
(1, 2, 'Kablolardan birini pilin boştaki (+) ucuna, diğerini (-) ucuna bağlayın.'),
(1, 3, 'Ampulü devreye ekleyin. Ampulün bir ucunu bir kabloya, diğer ucunu diğer kabloya bağlayın.'),
(1, 4, 'Anahtarı devreye ekleyin ve açıp kapatarak ampulün yanıp sönmesini gözlemleyin.'),
(2, 1, 'Plastik kabın dibine sıcak su koyun.'),
(2, 2, 'Kabın üstünü streç filmle kapatın.'),
(2, 3, 'Streç filmin üstüne buz küplerini yerleştirin.'),
(2, 4, 'Bekleyin ve streç filmin altında su damlacıklarının oluşmasını gözlemleyin.'),
(3, 1, 'Kırmızı lahanayı küçük parçalara ayırın ve kaynar suya ekleyin.'),
(3, 2, '15-20 dakika kaynatın ve suyun mor renge dönmesini bekleyin.'),
(3, 3, 'Suyu süzün ve soğumaya bırakın.'),
(3, 4, 'Test tüplerine farklı maddeler (sirke, karbonat çözeltisi, su, vb.) koyun.'),
(3, 5, 'Her test tüpüne kırmızı lahana suyundan ekleyin ve renk değişimlerini gözlemleyin.');

-- Deney medyası ekle (örnek URL'ler)
INSERT INTO experiment_media (experiment_id, media_type, media_url, media_order) VALUES
(1, 'VIDEO', 'http://localhost:9000/fenlab-videos/elektrik-devresi-deney.mp4', 1),
(1, 'IMAGE', 'http://localhost:9000/fenlab-images/elektrik-devresi-malzeme.jpg', 2),
(2, 'VIDEO', 'http://localhost:9000/fenlab-videos/su-dongusu-deney.mp4', 1),
(3, 'VIDEO', 'http://localhost:9000/fenlab-videos/asit-baz-deney.mp4', 1),
(3, 'IMAGE', 'http://localhost:9000/fenlab-images/asit-baz-sonuc.jpg', 2);

-- Favoriler ekle
INSERT INTO favorites (user_id, experiment_id, created_at) VALUES
(3, 1, CURRENT_TIMESTAMP),
(3, 2, CURRENT_TIMESTAMP),
(4, 1, CURRENT_TIMESTAMP);

-- Yorumlar ekle
INSERT INTO comments (experiment_id, user_id, content, created_at) VALUES
(1, 3, 'Harika bir deney! Çocuğumla birlikte yaptık, çok eğlendik.', CURRENT_TIMESTAMP),
(1, 4, 'Malzemeler kolay bulunuyor, teşekkürler!', CURRENT_TIMESTAMP),
(2, 3, 'Su döngüsünü çok güzel anlatmışsınız.', CURRENT_TIMESTAMP);

-- Sorular ekle
INSERT INTO questions (experiment_id, asker_id, question_text, created_at) VALUES
(1, 3, 'Pil yerine adaptör kullanabilir miyim?', CURRENT_TIMESTAMP),
(1, 4, 'Ampul yanmazsa ne yapmalıyım?', CURRENT_TIMESTAMP),
(2, 3, 'Bu deneyi kaç kere tekrarlayabilirim?', CURRENT_TIMESTAMP);

-- Bazı sorulara cevap ekle (sadece deney sahibi cevap verebilir)
UPDATE questions SET answer_text = 'Evet, ama düşük voltajlı (3-6V) bir adaptör kullanmanızı öneririm.', answerer_id = 1, answered_at = CURRENT_TIMESTAMP WHERE id = 1;
UPDATE questions SET answer_text = 'Önce bağlantıları kontrol edin. Ampul yanmıyorsa yeni bir ampul deneyin.', answerer_id = 1, answered_at = CURRENT_TIMESTAMP WHERE id = 2;

-- Puanlar ekle
INSERT INTO ratings (experiment_id, user_id, rating, created_at) VALUES
(1, 3, 5, CURRENT_TIMESTAMP),
(1, 4, 4, CURRENT_TIMESTAMP),
(2, 3, 5, CURRENT_TIMESTAMP),
(2, 4, 5, CURRENT_TIMESTAMP),
(3, 3, 4, CURRENT_TIMESTAMP);

-- Bildirimler ekle
INSERT INTO notifications (user_id, type, message, is_read, experiment_id, related_user_id, created_at) VALUES
(1, 'COMMENT', 'Mehmet Kaya deneyinize yorum yaptı.', false, 1, 3, CURRENT_TIMESTAMP),
(1, 'QUESTION', 'Zeynep Şahin deneyinize soru sordu.', false, 1, 4, CURRENT_TIMESTAMP),
(1, 'FAVORITE', 'Mehmet Kaya deneyinizi favorilerine ekledi.', false, 1, 3, CURRENT_TIMESTAMP),
(3, 'ANSWER', 'Ahmet Yılmaz sorunuzu cevapladı.', false, 1, 1, CURRENT_TIMESTAMP);