-- Create the table of filtered categories
DROP TABLE IF EXISTS filtered_category;
CREATE TABLE filtered_category LIKE category;
-- Remove PK information
ALTER TABLE filtered_category
    CHANGE COLUMN cat_id cat_id INT(10) UNSIGNED NOT NULL,
    DROP PRIMARY KEY;
-- Drop all indexes for speed
ALTER TABLE filtered_category
    DROP INDEX cat_title,
    DROP INDEX cat_pages;
    

-- Filter the items from category (via filtered_categorylinks) into our new table
INSERT INTO filtered_category
    SELECT c.*
    FROM category AS c, (SELECT DISTINCT cl_to FROM filtered_categorylinks) AS cl
    WHERE
    -- Retain only categories which are referenced by the filtered categorylinks
    c.cat_title=cl.cl_to;