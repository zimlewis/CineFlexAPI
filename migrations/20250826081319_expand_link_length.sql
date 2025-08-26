-- +goose Up
-- +goose StatementBegin
ALTER TABLE [dbo].[Advertisement] ALTER COLUMN [Link] NVARCHAR(MAX);
ALTER TABLE [dbo].[Advertisement] ALTER COLUMN [Image] NVARCHAR(MAX);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
ALTER TABLE [dbo].[Advertisement] ALTER COLUMN [Link] NVARCHAR(100);
ALTER TABLE [dbo].[Advertisement] ALTER COLUMN [Image] NVARCHAR(100);
-- +goose StatementEnd
