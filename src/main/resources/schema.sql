CREATE TABLE to_do_items
(
id IDENTITY,
title varchar(255) NOT NULL,
description varchar(1256) NOT NULL,
completed BOOLEAN NOT NULL
);
-- Sadly H2 doesn't support indexes involving functions. I wanted to create index on the lowercase of title i.e LOWER(title).
-- It would have prevented insert of a record with title = "Foo" if "fOO" was an already existing title in one of it's rows.
CREATE UNIQUE INDEX title_unique_index ON to_do_items (title);