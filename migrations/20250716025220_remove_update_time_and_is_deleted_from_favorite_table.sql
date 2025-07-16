-- +goose Up
-- +goose StatementBegin
ALTER TABLE [dbo].[Rating]
DROP CONSTRAINT DF_Rating_IsDeleted;

ALTER TABLE [dbo].[Rating] 
DROP COLUMN [UpdatedTime];

ALTER TABLE [dbo].[Rating] 
DROP COLUMN [IsDeleted];
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
ALTER TABLE [dbo].[Rating]
ADD [UpdatedTime] DATETIME;
ALTER TABLE [dbo].[Rating]
ADD [IsDeleted] BIT NOT NULL CONSTRAINT DF_Rating_IsDeleted DEFAULT 0;
-- +goose StatementEnd
