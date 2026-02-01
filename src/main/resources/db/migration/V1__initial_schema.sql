-- Users
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name   VARCHAR(255) NOT NULL,
    role        VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('ADMIN', 'USER')),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Genres
CREATE TABLE genres (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Movies
CREATE TABLE movies (
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    description      TEXT,
    poster_url       VARCHAR(500),
    genre_id         BIGINT NOT NULL REFERENCES genres(id),
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_movies_genre ON movies(genre_id);
CREATE INDEX idx_movies_title ON movies(title);

-- Theaters
CREATE TABLE theaters (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Seats (belong to a theater)
CREATE TABLE seats (
    id          BIGSERIAL PRIMARY KEY,
    theater_id  BIGINT NOT NULL REFERENCES theaters(id) ON DELETE CASCADE,
    row_letter  VARCHAR(5) NOT NULL,
    seat_number INTEGER NOT NULL CHECK (seat_number > 0),
    UNIQUE (theater_id, row_letter, seat_number)
);

CREATE INDEX idx_seats_theater ON seats(theater_id);

-- Showtimes
CREATE TABLE showtimes (
    id             BIGSERIAL PRIMARY KEY,
    movie_id       BIGINT NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
    theater_id     BIGINT NOT NULL REFERENCES theaters(id) ON DELETE CASCADE,
    start_time     TIMESTAMP WITH TIME ZONE NOT NULL,
    price_per_seat DECIMAL(10, 2) NOT NULL CHECK (price_per_seat >= 0),
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_showtimes_movie ON showtimes(movie_id);
CREATE INDEX idx_showtimes_theater ON showtimes(theater_id);
CREATE INDEX idx_showtimes_start_time ON showtimes(start_time);

-- Reservations
CREATE TABLE reservations (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id),
    showtime_id BIGINT NOT NULL REFERENCES showtimes(id),
    status      VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('CONFIRMED', 'CANCELLED')),
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_reservations_user ON reservations(user_id);
CREATE INDEX idx_reservations_showtime ON reservations(showtime_id);
CREATE INDEX idx_reservations_status ON reservations(status);

-- Reserved seats (links reservation to seat, prevents double-booking via unique constraint)
CREATE TABLE reserved_seats (
    id             BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    seat_id        BIGINT NOT NULL REFERENCES seats(id) ON DELETE CASCADE,
    showtime_id    BIGINT NOT NULL REFERENCES showtimes(id) ON DELETE CASCADE,
    UNIQUE (showtime_id, seat_id)
);

CREATE INDEX idx_reserved_seats_reservation ON reserved_seats(reservation_id);
CREATE INDEX idx_reserved_seats_showtime ON reserved_seats(showtime_id);

-- Trigger to update updated_at on users
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_movies_updated_at
    BEFORE UPDATE ON movies
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
