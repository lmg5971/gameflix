INSERT INTO games (id, title, genre, platform, maturity_rating, description, thumbnail_image_path, availability_status, active, created_at, updated_at)
VALUES
    (1, 'Counter-Strike 2', 'Action, Free To Play', 'Multiplayer', 'Unrated', 'Valve''s tactical team-based shooter focused on objective play, competitive matchmaking, and high-skill multiplayer rounds.', '/images/catalog/counter_strike_2.jpg', 'AVAILABLE', TRUE, '2026-06-21 16:25:00.000000', '2026-06-21 16:25:00.000000'),
    (2, 'Marvel Rivals', 'Action, Free To Play', 'Multiplayer', 'Unrated', 'NetEase Games'' superhero team shooter featuring Marvel characters, fast-paced objective battles, and multiplayer-focused progression.', '/images/catalog/marvel_rivals.jpg', 'AVAILABLE', TRUE, '2026-06-21 16:30:00.000000', '2026-06-21 16:30:00.000000'),
    (3, 'Path of Exile 2', 'Action, Adventure, Massively Multiplayer, RPG', 'Multiplayer', 'Unrated', 'Grinding Gear Games'' online action RPG sequel with deep character customization, cooperative combat, and a dark fantasy campaign.', '/images/catalog/path_of_exile_2.jpg', 'AVAILABLE', TRUE, '2026-06-21 16:35:00.000000', '2026-06-21 16:35:00.000000'),
    (4, 'PUBG: Battlegrounds', 'Action, Adventure, Massively Multiplayer, Free To Play', 'Multiplayer', 'Unrated', 'PUBG Corporation''s battle royale shooter where players scavenge equipment, fight to survive, and compete to be the last squad standing.', '/images/catalog/pubg_battlegrounds.jpg', 'AVAILABLE', TRUE, '2026-06-21 16:40:00.000000', '2026-06-21 16:40:00.000000'),
    (5, 'Cyberpunk 2077', 'RPG', 'Single-player', 'Mature', 'CD PROJEKT RED''s open-world role-playing game set in Night City, following a mercenary navigating cybernetic upgrades, factions, and high-stakes choices.', '/images/catalog/cyberpunk_2077.jpg', 'AVAILABLE', TRUE, '2026-06-21 16:45:00.000000', '2026-06-21 16:45:00.000000'),
    (6, 'Forza Horizon 6', 'Racing, Simulation, Sports', 'Single-player, Online PvP, Online Co-op', 'Everyone', 'Playground Games'' open-world racing experience with single-player events, competitive online racing, and cooperative driving activities.', '/images/catalog/forza_horizon_6.jpg', 'AVAILABLE', TRUE, '2026-06-21 16:50:00.000000', '2026-06-21 16:50:00.000000'),
    (7, 'Final Fantasy VII Remake Intergrade', 'Action, Adventure, RPG', 'Single-player', 'Teen', 'Square Enix''s expanded action RPG reimagining of Final Fantasy VII with real-time combat, cinematic storytelling, and a single-player campaign.', '/images/catalog/final_fantasy_vii_remake_intergrade.jpg', 'AVAILABLE', TRUE, '2026-06-21 16:55:00.000000', '2026-06-21 16:55:00.000000'),
    (8, 'Destiny 2', 'Action, Adventure, Free To Play', 'Single-player, Online PvP, Online Co-op', 'Teen', 'Bungie''s shared-world action game featuring story missions, cooperative strikes and raids, competitive PvP, and ongoing seasonal content.', '/images/catalog/destiny_2.jpg', 'AVAILABLE', TRUE, '2026-06-21 17:00:00.000000', '2026-06-21 17:00:00.000000'),
    (9, 'Battlefield 6', 'Action', 'Single-player, Online PvP', 'Mature', 'Battlefield Studios'' large-scale action shooter with cinematic single-player combat and online PvP battles built around vehicles and squad play.', '/images/catalog/battlefield_6.jpg', 'AVAILABLE', TRUE, '2026-06-21 17:05:00.000000', '2026-06-21 17:05:00.000000'),
    (10, 'Apex Legends', 'Action, Adventure, Free To Play', 'Online PvP, Online Co-op', 'Teen', 'Respawn''s squad-based battle royale with unique Legends, tactical abilities, online PvP competition, and cooperative team strategy.', '/images/catalog/apex_legends.jpg', 'AVAILABLE', TRUE, '2026-06-21 17:10:00.000000', '2026-06-21 17:10:00.000000')
AS seed
ON DUPLICATE KEY UPDATE
    title = seed.title,
    genre = seed.genre,
    platform = seed.platform,
    maturity_rating = seed.maturity_rating,
    description = seed.description,
    thumbnail_image_path = seed.thumbnail_image_path,
    availability_status = seed.availability_status,
    active = seed.active,
    updated_at = seed.updated_at;