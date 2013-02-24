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


-- Create the table of filtered category links
DROP TABLE IF EXISTS filtered_categorylinks;
CREATE TABLE filtered_categorylinks LIKE categorylinks;
-- Drop all indexes for speed
ALTER TABLE filtered_categorylinks
    DROP INDEX cl_from,
    DROP INDEX cl_timestamp,
    DROP INDEX cl_collation,
    DROP INDEX cl_sortkey;


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


-- Filter the items from page into our new table
INSERT INTO filtered_page
    SELECT *
    FROM page
    WHERE
    -- Retain only pages in the 'main' namespace
    page_namespace=0;


-- Filter the rows from categorylinks into our new table
INSERT INTO filtered_categorylinks
    SELECT cl.*
    FROM categorylinks AS cl, filtered_page AS p
    WHERE
    -- Don't include links for pages we don't care about
    cl.cl_from=p.page_id AND
    -- Use some simple heurisitcs to filer out administrative categories
    CONVERT(cl.cl_to USING latin1) NOT LIKE 'all_articles%' AND
    CONVERT(cl.cl_to USING latin1) NOT LIKE 'articles%'     AND
    CONVERT(cl.cl_to USING latin1) NOT LIKE 'wikipedia%'    AND
    CONVERT(cl.cl_to USING latin1) NOT LIKE 'pages%'        AND
    CONVERT(cl.cl_to USING latin1) NOT LIKE 'all_pages%'    AND
    CONVERT(cl.cl_to USING latin1) NOT LIKE 'redirects%'    AND
    CONVERT(cl.cl_to USING latin1) NOT LIKE 'category_template%';


-- Filter the items from category (via filtered_categorylinks) into our new table
INSERT INTO filtered_category
    SELECT c.*
    FROM category AS c, filtered_categorylinks AS cl
    WHERE
    -- Retain only categories which are referenced by the filtered categorylinks
    c.cat_title=cl.cl_to;