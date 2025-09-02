-- =================================================================
-- POPULATE CATEGORIES
-- =================================================================
-- Note: The 'id' column will be auto-incremented by the database.
INSERT INTO categories (name) VALUES
                                  ('Electronics'),
                                  ('Books'),
                                  ('Apparel'),
                                  ('Home & Kitchen'),
                                  ('Sports & Outdoors');


-- Products for Category 1: Electronics
INSERT INTO products (name, description, price, category_id) VALUES
                                                                 ('Wireless Noise-Cancelling Headphones', 'Immersive sound with adaptive noise cancellation. 30-hour battery life.', 249.99, 1),
                                                                 ('65-Inch 4K UHD Smart TV', 'Stunning 4K picture quality with HDR and a built-in smart platform.', 599.50, 1),
                                                                 ('Portable Bluetooth Speaker', 'Waterproof and dustproof speaker with deep bass and 12-hour playtime.', 89.95, 1),
                                                                 ('Smartwatch Fitness Tracker', 'Track your workouts, heart rate, and sleep patterns. GPS included.', 175.00, 1);

-- Products for Category 2: Books
INSERT INTO products (name, description, price, category_id) VALUES
                                                                 ('The Midnight Library', 'A novel by Matt Haig. A dazzling story about choices, regrets, and the infinite possibilities of life.', 15.99, 2),
                                                                 ('Atomic Habits', 'An Easy & Proven Way to Build Good Habits & Break Bad Ones by James Clear.', 19.99, 2),
                                                                 ('Dune by Frank Herbert', 'The sci-fi epic set on the desert planet Arrakis. A bestseller for generations.', 22.50, 2),
                                                                 ('The Catcher in the Rye', 'A classic novel by J. D. Salinger about teenage angst and alienation.', 12.75, 2);

-- Products for Category 3: Apparel
INSERT INTO products (name, description, price, category_id) VALUES
                                                                 ('Men''s Classic Cotton T-Shirt', 'A soft, breathable 100% cotton t-shirt for everyday comfort. White.', 25.00, 3),
                                                                 ('Women''s High-Waist Skinny Jeans', 'Flattering, comfortable jeans made with stretch-denim. Dark blue wash.', 79.99, 3),
                                                                 ('Unisex Running Shoes', 'Lightweight and cushioned for maximum performance and comfort on your runs.', 120.00, 3),
                                                                 ('Waterproof Rain Jacket', 'A lightweight, packable jacket to keep you dry during any downpour.', 95.50, 3);

-- Products for Category 4: Home & Kitchen
INSERT INTO products (name, description, price, category_id) VALUES
                                                                 ('12-Piece Non-Stick Cookware Set', 'Includes frying pans, saucepans, and utensils. PFOA-free.', 150.00, 4),
                                                                 ('Digital Air Fryer Oven', '5.8 Quart capacity. Cook your favorite meals with less oil. 8 pre-set functions.', 119.99, 4),
                                                                 ('Robotic Vacuum Cleaner', 'Smart-mapping technology with Wi-Fi connectivity. Works with Alexa.', 299.00, 4),
                                                                 ('Single-Serve Coffee Maker', 'Brew a perfect cup of coffee in minutes. Compatible with K-Cup pods.', 69.95, 4);

-- Products for Category 5: Sports & Outdoors
INSERT INTO products (name, description, price, category_id) VALUES
                                                                 ('2-Person Camping Tent', 'Waterproof and easy to set up, perfect for weekend backpacking trips.', 85.00, 5),
                                                                 ('High-Density Yoga Mat', 'Extra-thick non-slip mat with a carrying strap for comfort and portability.', 35.99, 5),
                                                                 ('Insulated Stainless Steel Water Bottle', '32 oz bottle that keeps drinks cold for 24 hours or hot for 12 hours.', 29.95, 5),
                                                                 ('Adjustable Dumbbell Set', 'Replaces a full rack of weights. Adjusts from 5 to 52.5 lbs.', 399.00, 5);