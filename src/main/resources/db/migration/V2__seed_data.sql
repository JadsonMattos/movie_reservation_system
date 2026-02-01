-- Seed default genres only (admin user created by application DataLoader)
INSERT INTO genres (name) VALUES
    ('Action'),
    ('Comedy'),
    ('Drama'),
    ('Horror'),
    ('Sci-Fi'),
    ('Romance'),
    ('Thriller'),
    ('Animation')
ON CONFLICT (name) DO NOTHING;
