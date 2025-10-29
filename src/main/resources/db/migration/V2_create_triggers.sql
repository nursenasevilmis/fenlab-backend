-- ===========================
-- TRIGGER FUNCTIONS
-- ===========================

-- Trigger: Sadece deney sahibi cevap verebilir
CREATE OR REPLACE FUNCTION check_answerer_is_owner()
RETURNS TRIGGER AS $$
DECLARE
    experiment_owner_id BIGINT;
BEGIN
    IF NEW.answerer_id IS NOT NULL THEN
        SELECT user_id INTO experiment_owner_id
        FROM experiments
        WHERE id = NEW.experiment_id;

        IF experiment_owner_id IS NULL THEN
            RAISE EXCEPTION 'Experiment does not exist.';
        END IF;

        IF experiment_owner_id != NEW.answerer_id THEN
            RAISE EXCEPTION 'Only the experiment owner can answer this question.';
        END IF;

        IF NEW.asker_id = NEW.answerer_id THEN
            RAISE EXCEPTION 'A user cannot answer their own question.';
        END IF;

        NEW.answered_at = CURRENT_TIMESTAMP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Kullanıcı kendi deneyini rating'leyemez
CREATE OR REPLACE FUNCTION check_self_rating()
RETURNS TRIGGER AS $$
DECLARE
    experiment_owner_id BIGINT;
BEGIN
    SELECT user_id INTO experiment_owner_id
    FROM experiments
    WHERE id = NEW.experiment_id;

    IF experiment_owner_id = NEW.user_id THEN
        RAISE EXCEPTION 'Users cannot rate their own experiments.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger: Updated_at otomatik güncelleme
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ===========================
-- TRIGGERS
-- ===========================

-- Questions tablosu için trigger
CREATE TRIGGER trg_check_answerer
BEFORE INSERT OR UPDATE ON questions
FOR EACH ROW
EXECUTE FUNCTION check_answerer_is_owner();

-- Ratings tablosu için trigger
CREATE TRIGGER trg_check_self_rating
BEFORE INSERT OR UPDATE ON ratings
FOR EACH ROW
EXECUTE FUNCTION check_self_rating();

-- Experiments tablosu için updated_at trigger
CREATE TRIGGER trg_experiments_updated_at
BEFORE UPDATE ON experiments
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- Comments tablosu için updated_at trigger
CREATE TRIGGER trg_comments_updated_at
BEFORE UPDATE ON comments
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();