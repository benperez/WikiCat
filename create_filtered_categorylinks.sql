-- Create the table of filtered category links
DROP TABLE IF EXISTS filtered_categorylinks;
CREATE TABLE filtered_categorylinks LIKE categorylinks;
-- Drop all indexes for speed
ALTER TABLE filtered_categorylinks
    DROP INDEX cl_from,
    DROP INDEX cl_to;


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

