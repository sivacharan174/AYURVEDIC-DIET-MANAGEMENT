-- =====================================================
-- UPDATE RECIPE IMAGES FOR AYURVEDIC APP
-- Run this in phpMyAdmin to update existing recipe images
-- =====================================================

-- Update Golden Milk (matches id 8 in existing database)
UPDATE `recipes` SET `image_url` = 'images/recipes/golden_milk.png' 
WHERE `title` LIKE '%Golden Milk%';

-- Update Khichdi (matches id 3, 16 in existing database)
UPDATE `recipes` SET `image_url` = 'images/recipes/khichdi.png' 
WHERE `title` LIKE '%Khichdi%';

-- Update Poha (matches id 12)
UPDATE `recipes` SET `image_url` = 'images/recipes/poha.png' 
WHERE `title` LIKE '%Poha%';

-- Update Ragi Porridge (matches id 13)
UPDATE `recipes` SET `image_url` = 'images/recipes/ragi_porridge.png' 
WHERE `title` LIKE '%Ragi%';

-- Update Moong Dal Chilla (matches id 14)
UPDATE `recipes` SET `image_url` = 'images/recipes/moong_chilla.png' 
WHERE `title` LIKE '%Moong Dal Chilla%' OR `title` LIKE '%Chilla%';

-- Update Palak Dal (matches id 18)
UPDATE `recipes` SET `image_url` = 'images/recipes/palak_dal.png' 
WHERE `title` LIKE '%Palak Dal%';

-- Update Rasam (matches id 21)
UPDATE `recipes` SET `image_url` = 'images/recipes/rasam.png' 
WHERE `title` LIKE '%Rasam%';

-- Update Energy Balls (matches id 24)
UPDATE `recipes` SET `image_url` = 'images/recipes/energy_balls.png' 
WHERE `title` LIKE '%Energy Balls%' OR `title` LIKE '%Dates Almond%';

-- Update Makhana (matches id 25)
UPDATE `recipes` SET `image_url` = 'images/recipes/makhana.png' 
WHERE `title` LIKE '%Makhana%';

-- Update CCF Tea (matches id 10)
UPDATE `recipes` SET `image_url` = 'images/recipes/ccf_tea.png' 
WHERE `title` LIKE '%CCF%';

-- Update Buttermilk/Chaas (matches id 32)
UPDATE `recipes` SET `image_url` = 'images/recipes/buttermilk.png' 
WHERE `title` LIKE '%Buttermilk%' OR `title` LIKE '%Chaas%';

-- Verification query - run this to see updated recipes
-- SELECT id, title, image_url FROM recipes;
