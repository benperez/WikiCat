-- Create the table of filtered pages
DROP TABLE IF EXISTS filtered_page;
CREATE TABLE filtered_page LIKE page;
-- Remove PK information
ALTER TABLE filtered_page
    CHANGE COLUMN page_id page_id INT(8) UNSIGNED NOT NULL,
    DROP PRIMARY KEY;
-- Drop all indexes for speed
ALTER TABLE filtered_page
    DROP INDEX name_title,
    DROP INDEX page_random,
    DROP INDEX page_len,
    DROP INDEX page_redirect_namespace_len;


-- Filter the items from page into our new table
INSERT INTO filtered_page
    SELECT *
    FROM page
    WHERE
    -- Retain only pages in the 'main' namespace
    page_namespace=0;