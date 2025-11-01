
-- ENUM YAPILARI
CREATE TYPE user_role AS ENUM ('TEACHER', 'USER');
CREATE TYPE difficulty_level AS ENUM ('KOLAY', 'ORTA', 'ZOR');
CREATE TYPE notification_type AS ENUM ('COMMENT', 'QUESTION', 'ANSWER', 'FAVORITE');
CREATE TYPE media_type AS ENUM ('IMAGE', 'VIDEO');
CREATE TYPE sort_type AS ENUM ('MOST_RECENT', 'MOST_FAVORITED', 'HIGHEST_RATED', 'OLDEST');

-- TABLOLAR

-- Users Tablosu
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    branch VARCHAR(100),
    experience_years INT CHECK (experience_years >= 0),
    bio TEXT,
    profile_image_url TEXT,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Experiments Tablosu
CREATE TABLE experiments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    grade_level INT NOT NULL CHECK (grade_level >= 1 AND grade_level <= 12),
    subject VARCHAR(100) NOT NULL,
    difficulty difficulty_level NOT NULL,
    expected_result TEXT,
    safety_notes TEXT,
    is_published BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Experiment Materials
CREATE TABLE experiment_materials (
    id BIGSERIAL PRIMARY KEY,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    material_name VARCHAR(255) NOT NULL,
    quantity VARCHAR(50) NOT NULL
);

-- Experiment Steps
CREATE TABLE experiment_steps (
    id BIGSERIAL PRIMARY KEY,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    step_order INT NOT NULL,
    step_text TEXT NOT NULL,
    UNIQUE (experiment_id, step_order)
);

-- Experiment Media (Fotoğraf ve Video)
CREATE TABLE experiment_media (
    id BIGSERIAL PRIMARY KEY,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    media_type media_type NOT NULL,
    media_url TEXT NOT NULL,
    media_order INT DEFAULT 0
);

-- Favorites
CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, experiment_id)
);

-- Comments
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Questions (Sadece deney sahibi cevaplayabilir)
CREATE TABLE questions (
    id BIGSERIAL PRIMARY KEY,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    asker_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    answer_text TEXT,
    answerer_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    answered_at TIMESTAMP
);

-- Ratings
CREATE TABLE ratings (
    id BIGSERIAL PRIMARY KEY,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (experiment_id, user_id)
);

-- Notifications
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type notification_type NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    experiment_id BIGINT REFERENCES experiments(id) ON DELETE CASCADE,
    related_user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PDF Downloads
CREATE TABLE pdf_downloads (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    experiment_id BIGINT NOT NULL REFERENCES experiments(id) ON DELETE CASCADE,
    downloaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================
-- INDEX'LER
-- ===========================
CREATE INDEX idx_users_is_deleted ON users(is_deleted);

CREATE INDEX idx_experiments_user_id ON experiments(user_id);
CREATE INDEX idx_experiments_subject ON experiments(subject);
CREATE INDEX idx_experiments_grade_level ON experiments(grade_level);
CREATE INDEX idx_experiments_difficulty ON experiments(difficulty);
CREATE INDEX idx_experiments_created_at ON experiments(created_at);
CREATE INDEX idx_experiments_is_deleted ON experiments(is_deleted);

CREATE INDEX idx_experiment_media_experiment_id ON experiment_media(experiment_id);
CREATE INDEX idx_experiment_media_type ON experiment_media(media_type);

CREATE INDEX idx_comments_experiment_id ON comments(experiment_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_comments_is_deleted ON comments(is_deleted);

CREATE INDEX idx_questions_experiment_id ON questions(experiment_id);
CREATE INDEX idx_questions_asker_id ON questions(asker_id);
CREATE INDEX idx_questions_answerer_id ON questions(answerer_id);
CREATE INDEX idx_questions_is_deleted ON questions(is_deleted);

CREATE INDEX idx_favorites_user_id ON favorites(user_id);
CREATE INDEX idx_favorites_experiment_id ON favorites(experiment_id);

CREATE INDEX idx_ratings_experiment_id ON ratings(experiment_id);
CREATE INDEX idx_ratings_user_id ON ratings(user_id);

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_type ON notifications(type);

CREATE INDEX idx_pdf_downloads_user_id ON pdf_downloads(user_id);
CREATE INDEX idx_pdf_downloads_experiment_id ON pdf_downloads(experiment_id);